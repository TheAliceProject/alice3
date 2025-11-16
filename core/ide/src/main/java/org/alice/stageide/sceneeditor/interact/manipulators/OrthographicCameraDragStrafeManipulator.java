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
package org.alice.stageide.sceneeditor.interact.manipulators;

import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.math.immutable.Vector2;
import org.alice.math.immutable.Vector3;
import org.alice.stageide.sceneeditor.interact.handles.ImageBasedManipulationHandle2D;

import java.awt.Color;

/**
 * @author David Culyba
 */
public class OrthographicCameraDragStrafeManipulator extends Camera2DDragManipulator {

  private static final Color UP = Color.RED;
  private static final Color LEFT = Color.GREEN;
  private static final Color RIGHT = Color.BLUE;
  private static final Color DOWN = Color.WHITE;

  public OrthographicCameraDragStrafeManipulator(ImageBasedManipulationHandle2D handle) {
    super(handle);
  }

  @Override
  protected void initializeEventMessages() {
    this.setMainManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, null, this.manipulatedTransformable));
    this.clearManipulationEvents();
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.UP, MovementType.LOCAL), this.manipulatedTransformable));
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.DOWN, MovementType.LOCAL), this.manipulatedTransformable));
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.LEFT, MovementType.LOCAL), this.manipulatedTransformable));
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.RIGHT, MovementType.LOCAL), this.manipulatedTransformable));
  }

  @Override
  protected Vector3 getMovementVectorForColor(Color color) {
    if (UP.equals(color)) {
      return new Vector3(0, INITIAL_MOVE_FACTOR, 0);
    }
    if (DOWN.equals(color)) {
      return new Vector3(0, -INITIAL_MOVE_FACTOR, 0);
    }
    if (LEFT.equals(color)) {
      return new Vector3(-INITIAL_MOVE_FACTOR, 0, 0);
    }
    if (RIGHT.equals(color)) {
      return new Vector3(INITIAL_MOVE_FACTOR, 0, 0);
    }
    return Vector3.ZERO;
  }

  @Override
  protected Vector3 getRotationVectorForColor(Color color) {
    return new Vector3(0.0d, 0.0d, 0.0d);
  }

  @Override
  protected Vector3 getRelativeMovementAmount(Vector2 mousePos, double time) {
    Vector2 relativeMousePos = mousePos.minus(this.initialMousePosition);
    double moveY = relativeMousePos.y();
    double moveX = relativeMousePos.x();
    if (LEFT.equals(this.initialHandleColor) || RIGHT.equals(this.initialHandleColor)) {
      moveY = quantizedMove(moveY);
    } else if (UP.equals(this.initialHandleColor) || DOWN.equals(this.initialHandleColor)) {
      moveX = quantizedMove(moveX);
    }
    moveY *= -1.0d;

    double amountToMoveY = moveY * WORLD_DISTANCE_PER_PIXEL_SECONDS * time;
    double amountToMoveX = moveX * WORLD_DISTANCE_PER_PIXEL_SECONDS * time;
    Vector3 amountToMoveMouse = new Vector3(amountToMoveX, amountToMoveY, 0.0d);
    return amountToMoveMouse;
  }

  private static double quantizedMove(double move) {
    if (Math.abs(move) < MIN_PIXEL_MOVE_AMOUNT) {
      return 0.0d;
    }
    return move < 0.0d ? move + MIN_PIXEL_MOVE_AMOUNT : move - MIN_PIXEL_MOVE_AMOUNT;
  }

  @Override
  protected Vector3 getRelativeRotationAmount(Vector2 mousePos, double time) {
    return new Vector3(0.0d, 0.0d, 0.0d);
  }

  @Override
  protected ReferenceFrame getRotationReferenceFrame() {
    return this.getManipulatedTransformable();
  }

  @Override
  protected ReferenceFrame getMovementReferenceFrame() {
    return this.getManipulatedTransformable();
  }

}
