/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.story.event;

import edu.cmu.cs.dennisc.java.util.Lists;
import org.lgna.story.Key;
import org.lgna.story.MoveDirection;
import org.lgna.story.TurnDirection;

import java.util.List;

public class ArrowKeyEvent extends AbstractKeyEvent {

  public enum MoveDirectionPlane {
    FORWARD_BACKWARD_LEFT_RIGHT, UP_DOWN_LEFT_RIGHT
  }

  public static final List<Key> ARROWS = Lists.newArrayList(Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT, Key.A, Key.S, Key.D, Key.W);

  public ArrowKeyEvent(java.awt.event.KeyEvent e) {
    super(e);
    boolean isArrow = false;
    for (Key k : ARROWS) {
      if (this.isKey(k)) {
        isArrow = true;
        break;
      }
    }
    assert isArrow : e;
  }

  public ArrowKeyEvent(AbstractKeyEvent other) {
    this(other.getJavaEvent());
  }

  public MoveDirection getMoveDirection(MoveDirectionPlane plane) {
    if (plane.equals(MoveDirectionPlane.UP_DOWN_LEFT_RIGHT)) {
      return getUpDownLeftRightMoveDirection();
    }
    return getForwardBackwardLeftRightMoveDirection();
  }

  public TurnDirection getTurnDirection() {
    if (this.isKey(Key.A) || this.isKey(Key.LEFT)) {
      return TurnDirection.LEFT;
    } else if (this.isKey(Key.S) || this.isKey(Key.DOWN)) {
      return TurnDirection.BACKWARD;
    } else if (this.isKey(Key.W) || this.isKey(Key.UP)) {
      return TurnDirection.FORWARD;
    } else if (this.isKey(Key.D) || this.isKey(Key.RIGHT)) {
      return TurnDirection.RIGHT;
    }
    return null;
  }

  private MoveDirection getForwardBackwardLeftRightMoveDirection() {
    if (this.isKey(Key.A) || this.isKey(Key.LEFT)) {
      return MoveDirection.LEFT;
    } else if (this.isKey(Key.S) || this.isKey(Key.DOWN)) {
      return MoveDirection.BACKWARD;
    } else if (this.isKey(Key.W) || this.isKey(Key.UP)) {
      return MoveDirection.FORWARD;
    } else if (this.isKey(Key.D) || this.isKey(Key.RIGHT)) {
      return MoveDirection.RIGHT;
    }
    return null;
  }

  private MoveDirection getUpDownLeftRightMoveDirection() {
    if (this.isKey(Key.A) || this.isKey(Key.LEFT)) {
      return MoveDirection.LEFT;
    } else if (this.isKey(Key.S) || this.isKey(Key.DOWN)) {
      return MoveDirection.DOWN;
    } else if (this.isKey(Key.W) || this.isKey(Key.UP)) {
      return MoveDirection.UP;
    } else if (this.isKey(Key.D) || this.isKey(Key.RIGHT)) {
      return MoveDirection.RIGHT;
    }
    return null;
  }
}
