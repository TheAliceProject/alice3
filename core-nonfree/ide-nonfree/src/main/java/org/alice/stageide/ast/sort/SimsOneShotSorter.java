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

package org.alice.stageide.ast.sort;

import java.util.Map;

import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.story.Paint;
import org.lgna.story.SRoom;
import org.lgna.story.SetCeilingPaint;
import org.lgna.story.SetFloorPaint;
import org.lgna.story.SetWallPaint;

/**
 * @author Dennis Cosgrove
 */
public class SimsOneShotSorter {

  public static final JavaMethod ROOM_SET_CEILING_PAINT_METHOD;
  public static final JavaMethod ROOM_SET_WALL_PAINT_METHOD;
  public static final JavaMethod ROOM_SET_FLOOR_PAINT_METHOD;

  public static final JavaMethod ROOM_SET_OPACITY_METHOD;

  static {
    JavaType roomType = JavaType.getInstance(SRoom.class);

    ROOM_SET_CEILING_PAINT_METHOD = roomType.getDeclaredMethod("setCeilingPaint", Paint.class, SetCeilingPaint.Detail[].class);
    assert ROOM_SET_CEILING_PAINT_METHOD != null : roomType;
    ROOM_SET_WALL_PAINT_METHOD = roomType.getDeclaredMethod("setWallPaint", Paint.class, SetWallPaint.Detail[].class);
    assert ROOM_SET_WALL_PAINT_METHOD != null : roomType;
    ROOM_SET_FLOOR_PAINT_METHOD = roomType.getDeclaredMethod("setFloorPaint", Paint.class, SetFloorPaint.Detail[].class);
    assert ROOM_SET_FLOOR_PAINT_METHOD != null : roomType;

    ROOM_SET_OPACITY_METHOD = OneShotSorter.getSetOpacityMethod(roomType);

  }

  public static double setOneShotSortValues(Map<JavaMethod, Double> map, double value, double INCREMENT) {
    map.put(ROOM_SET_CEILING_PAINT_METHOD, value += INCREMENT);
    map.put(ROOM_SET_WALL_PAINT_METHOD, value += INCREMENT);
    map.put(ROOM_SET_FLOOR_PAINT_METHOD, value += INCREMENT);

    map.put(ROOM_SET_OPACITY_METHOD, value);
    return value;
  }
}
