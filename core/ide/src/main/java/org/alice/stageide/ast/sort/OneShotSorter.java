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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.ast.sort.MemberSorter;
import org.alice.nonfree.NebulousIde;
import org.lgna.project.ast.AbstractMember;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.story.Move;
import org.lgna.story.MoveAndOrientTo;
import org.lgna.story.MoveAndOrientToAGoodVantagePointOf;
import org.lgna.story.MoveAwayFrom;
import org.lgna.story.MoveDirection;
import org.lgna.story.MoveTo;
import org.lgna.story.MoveToward;
import org.lgna.story.OrientTo;
import org.lgna.story.OrientToUpright;
import org.lgna.story.Paint;
import org.lgna.story.Place;
import org.lgna.story.PointAt;
import org.lgna.story.Roll;
import org.lgna.story.RollDirection;
import org.lgna.story.SCamera;
import org.lgna.story.SFlyer;
import org.lgna.story.SGround;
import org.lgna.story.SJointedModel;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SThing;
import org.lgna.story.STurnable;
import org.lgna.story.SetOpacity;
import org.lgna.story.SetPaint;
import org.lgna.story.SpatialRelation;
import org.lgna.story.StraightenOutJoints;
import org.lgna.story.StrikePose;
import org.lgna.story.Turn;
import org.lgna.story.TurnDirection;
import org.lgna.story.TurnToFace;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public enum OneShotSorter implements MemberSorter {
  SINGLETON {
    @Override
    public <T extends AbstractMember> List<T> createSortedList(List<T> src) {
      List<T> rv = Lists.newArrayList(src);
      Collections.sort(rv, new Comparator<T>() {
        @Override
        public int compare(T o1, T o2) {
          return Double.compare(getValue(o1), getValue(o2));
        }
      });
      return rv;
    }
  };
  private static final Map<JavaMethod, Double> map = Maps.newHashMap();

  public static final JavaMethod TURN_METHOD;
  public static final JavaMethod ROLL_METHOD;
  public static final JavaMethod TURN_TO_FACE_METHOD;
  public static final JavaMethod POINT_AT_METHOD;
  public static final JavaMethod ORIENT_TO_UPRIGHT_METHOD;
  public static final JavaMethod ORIENT_TO_METHOD;

  public static final JavaMethod MOVE_METHOD;
  public static final JavaMethod MOVE_TOWARD_METHOD;
  public static final JavaMethod MOVE_AWAY_FROM_METHOD;
  public static final JavaMethod MOVE_TO_METHOD;
  public static final JavaMethod MOVE_AND_ORIENT_TO_METHOD;
  public static final JavaMethod PLACE_METHOD;

  public static final JavaMethod MOVE_AND_ORIENT_TO_A_GOOD_VANTAGE_POINT_METHOD;

  public static final JavaMethod STRAIGHTEN_OUT_JOINTS_METHOD;
  public static final JavaMethod SPREAD_WINGS_METHOD;
  public static final JavaMethod FOLD_WINGS_METHOD;

  public static final JavaMethod GROUND_SET_PAINT_METHOD;
  public static final JavaMethod MODEL_SET_PAINT_METHOD;

  public static final JavaMethod GROUND_SET_OPACITY_METHOD;
  public static final JavaMethod MODEL_SET_OPACITY_METHOD;

  static {
    JavaType turnableType = JavaType.getInstance(STurnable.class);
    JavaType movableTurnableType = JavaType.getInstance(SMovableTurnable.class);
    JavaType jointedModelType = JavaType.getInstance(SJointedModel.class);
    JavaType flyerType = JavaType.getInstance(SFlyer.class);
    JavaType cameraType = JavaType.getInstance(SCamera.class);

    JavaType groundType = JavaType.getInstance(SGround.class);
    JavaType modelType = JavaType.getInstance(SModel.class);

    TURN_METHOD = turnableType.getDeclaredMethod("turn", TurnDirection.class, Number.class, Turn.Detail[].class);
    assert TURN_METHOD != null;
    ROLL_METHOD = turnableType.getDeclaredMethod("roll", RollDirection.class, Number.class, Roll.Detail[].class);
    assert ROLL_METHOD != null;
    TURN_TO_FACE_METHOD = turnableType.getDeclaredMethod("turnToFace", SThing.class, TurnToFace.Detail[].class);
    assert TURN_TO_FACE_METHOD != null;
    POINT_AT_METHOD = turnableType.getDeclaredMethod("pointAt", SThing.class, PointAt.Detail[].class);
    assert POINT_AT_METHOD != null;
    ORIENT_TO_UPRIGHT_METHOD = turnableType.getDeclaredMethod("orientToUpright", OrientToUpright.Detail[].class);
    assert ORIENT_TO_UPRIGHT_METHOD != null;
    ORIENT_TO_METHOD = turnableType.getDeclaredMethod("orientTo", SThing.class, OrientTo.Detail[].class);
    assert ORIENT_TO_METHOD != null;

    MOVE_METHOD = movableTurnableType.getDeclaredMethod("move", MoveDirection.class, Number.class, Move.Detail[].class);
    assert MOVE_METHOD != null;
    MOVE_TOWARD_METHOD = movableTurnableType.getDeclaredMethod("moveToward", SThing.class, Number.class, MoveToward.Detail[].class);
    assert MOVE_TOWARD_METHOD != null;
    MOVE_AWAY_FROM_METHOD = movableTurnableType.getDeclaredMethod("moveAwayFrom", SThing.class, Number.class, MoveAwayFrom.Detail[].class);
    assert MOVE_AWAY_FROM_METHOD != null;
    MOVE_TO_METHOD = movableTurnableType.getDeclaredMethod("moveTo", SThing.class, MoveTo.Detail[].class);
    assert MOVE_TO_METHOD != null;
    MOVE_AND_ORIENT_TO_METHOD = movableTurnableType.getDeclaredMethod("moveAndOrientTo", SThing.class, MoveAndOrientTo.Detail[].class);
    assert MOVE_AND_ORIENT_TO_METHOD != null;
    PLACE_METHOD = movableTurnableType.getDeclaredMethod("place", SpatialRelation.class, SThing.class, Place.Detail[].class);
    assert PLACE_METHOD != null;

    STRAIGHTEN_OUT_JOINTS_METHOD = jointedModelType.getDeclaredMethod("straightenOutJoints", StraightenOutJoints.Detail[].class);
    assert STRAIGHTEN_OUT_JOINTS_METHOD != null;

    FOLD_WINGS_METHOD = flyerType.getDeclaredMethod("foldWings", StrikePose.Detail[].class);
    assert FOLD_WINGS_METHOD != null;
    SPREAD_WINGS_METHOD = flyerType.getDeclaredMethod("spreadWings", StrikePose.Detail[].class);
    assert SPREAD_WINGS_METHOD != null;

    MOVE_AND_ORIENT_TO_A_GOOD_VANTAGE_POINT_METHOD = cameraType.getDeclaredMethod("moveAndOrientToAGoodVantagePointOf", SThing.class, MoveAndOrientToAGoodVantagePointOf.Detail[].class);
    assert MOVE_AND_ORIENT_TO_A_GOOD_VANTAGE_POINT_METHOD != null;

    GROUND_SET_PAINT_METHOD = getSetPaintMethod(groundType);
    MODEL_SET_PAINT_METHOD = getSetPaintMethod(modelType);

    GROUND_SET_OPACITY_METHOD = getSetOpacityMethod(groundType);
    MODEL_SET_OPACITY_METHOD = getSetOpacityMethod(modelType);

    double value = 1.0;
    final double INCREMENT = 0.01;
    map.put(MOVE_METHOD, value += INCREMENT);
    map.put(MOVE_TOWARD_METHOD, value += INCREMENT);
    map.put(MOVE_AWAY_FROM_METHOD, value += INCREMENT);
    map.put(TURN_METHOD, value += INCREMENT);
    map.put(ROLL_METHOD, value += INCREMENT);

    value = 2.0;
    map.put(MOVE_TO_METHOD, value += INCREMENT);
    map.put(MOVE_AND_ORIENT_TO_METHOD, value += INCREMENT);
    map.put(MOVE_AND_ORIENT_TO_A_GOOD_VANTAGE_POINT_METHOD, value += INCREMENT);
    map.put(PLACE_METHOD, value += INCREMENT);
    map.put(TURN_TO_FACE_METHOD, value += INCREMENT);
    map.put(POINT_AT_METHOD, value += INCREMENT);
    map.put(ORIENT_TO_UPRIGHT_METHOD, value += INCREMENT);
    map.put(ORIENT_TO_METHOD, value += INCREMENT);

    value = 3.0;
    map.put(STRAIGHTEN_OUT_JOINTS_METHOD, value += INCREMENT);

    value = 4.0;
    map.put(GROUND_SET_PAINT_METHOD, value);
    map.put(MODEL_SET_PAINT_METHOD, value);
    value += INCREMENT;

    NebulousIde.nonfree.setOneShotSortValues(map, value, INCREMENT);

    map.put(GROUND_SET_OPACITY_METHOD, value);
    map.put(MODEL_SET_OPACITY_METHOD, value);
    value += INCREMENT;
  }

  private static JavaMethod getSetPaintMethod(JavaType type) {
    JavaMethod rv = type.getDeclaredMethod("setPaint", Paint.class, SetPaint.Detail[].class);
    assert rv != null : type;
    return rv;
  }

  static JavaMethod getSetOpacityMethod(JavaType type) {
    JavaMethod rv = type.getDeclaredMethod("setOpacity", Number.class, SetOpacity.Detail[].class);
    assert rv != null : type;
    return rv;
  }

  private static double getValue(AbstractMember method) {
    Double rv = map.get(method);
    if (rv != null) {
      //pass
    } else {
      rv = 0.0;
    }
    return rv;
  }
}
