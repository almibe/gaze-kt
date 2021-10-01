/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package dev.ligature.gaze

/**
 * stringNibbler matches a given String entirely
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun stringNibbler(toMatch: String) = Step { lookAhead ->
    var offset = 0U
    var result: State? = null
    while (offset.toInt() <= toMatch.length) {
        if (offset.toInt() == toMatch.length - 1 && toMatch[offset.toInt()] == lookAhead.peek(offset)) {
            result = Complete(offset.toInt() + 1)
            break
        } else if (offset.toInt() < toMatch.length && toMatch[offset.toInt()] == lookAhead.peek(offset)) {
            offset++
        } else {
            result = Cancel
            break
        }
    }
    result ?: Cancel
}

/**
 * charNibbler matches input against the passed characters
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun charNibbler(vararg chars: Char) = Step { lookAhead ->
    var offset = 0U
    while (lookAhead.peek(offset) != null) {
        var match = false
        chars.forEach check@{
            if (it == lookAhead.peek(offset)) {
                match = true
                return@check
            }
        }
        if (!match) {
            break
        } else {
            offset++
        }
    }
    if (offset == 0U) {
        Cancel
    } else {
        Complete(offset.toInt())
    }
}

/**
 * rangeNibbler matches characters that exist within a given set of CharRanges
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun rangeNibbler(vararg ranges: CharRange) = Step { lookAhead ->
    var offset = 0U
    while (lookAhead.peek(offset) != null) {
        var match = false
        ranges.forEach check@{
            if (it.contains(lookAhead.peek(offset))) {
                match = true
                return@check
            }
        }
        if (!match) {
            break
        } else {
            offset++
        }
    }
    if (offset == 0U) {
        Cancel
    } else {
        Complete(offset.toInt())
    }
}

/**
 * predicateNibbler is helper that checks a single character against a given predicate
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun predicateNibbler(fn: (Char?) -> Boolean) = Step { lookAhead ->
    var offset = 0U
    while (lookAhead.peek(offset) != null) {
        if (fn(lookAhead.peek(offset))) {
            offset++
        } else {
            break
        }
    }
    if (offset == 0U) {
        Cancel
    } else {
        Complete(offset.toInt())
    }
}