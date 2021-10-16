/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package dev.ligature.gaze

sealed trait State

/**
 * The Cancel state means that this Nibbler didn't match and Rakkoon should jump back to its position before
 * starting this Nibbler.
 * The nibble method will also return None.
 */
object Cancel extends State

/**
 * The Complete state means that this Nibbler completed and Rakkoon should adjust its offset based on the adjust param.
 * A Some(Match) is returned by the nibble method.
 */
case class Complete(val adjust: Int = 0) extends State

trait Step {
    def taste(lookAhead: LookAhead): State
}

trait LookAhead {
    def peek(distance: Long = 0): Option[Char]
}

case class Match(val value: String, val range: IntRange)

class Rakkoon(private var input: CharSequence) extends LookAhead {
    private var offset = 0

    override def peek(distance: Int): Option[Char] =
        if (offset + distance < input.length) input[offset + distance]
        else null

    def bite(distance: Int) = {
        offset += distance
    }

    def attempt(step: Step): Option[Match] = {
        val start = offset
        val res = step.taste(this)
        res match {
            case Cancel => {
                offset = start
                None
            }
            case Complete => {
                offset += res.adjust
                Some(Match(input.substring(start, offset), IntRange(start, offset)))
            }
        }
    }

    def attempt(steps: Step*): Option[List[Match]] = {
        val resultList: MutableList[Match] = MutableList()
        val start = offset
        for (nibbler in steps) {
            when (val res = attempt(nibbler)) {
                is None -> {
                    offset = start
                    return none()
                }
                is Some -> {
                    resultList.add(res.value)
                }
            }
        }
        return Some(resultList)
    }

    def currentOffset(): Int = offset

    def remainingText(): String = input.substring(offset)

    def isComplete(): Boolean = {
        return input.length <= offset
    }
}