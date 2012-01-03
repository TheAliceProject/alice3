/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.alice.stageide.ast.sort;

/**
 * @author Dennis Cosgrove
 */
public enum OneShotSorter implements org.alice.ide.ast.sort.MemberSorter {
	SINGLETON {
		public <T extends org.lgna.project.ast.AbstractMember> java.util.List< T > createSortedList( java.util.List< T > src ) {
			java.util.List< T > rv = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( src );
			java.util.Collections.sort( rv, new java.util.Comparator< T >() {
				public int compare(T o1, T o2) {
					return Double.compare( getValue( o1 ), getValue( o2 ) );
				}
			} );
			edu.cmu.cs.dennisc.java.util.logging.Logger.testing( "src", src );
			edu.cmu.cs.dennisc.java.util.logging.Logger.testing( "rv", rv );
			return rv;
		}
	};
	private static final java.util.Map< org.lgna.project.ast.JavaMethod, Double > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public static final org.lgna.project.ast.JavaMethod TURN_METHOD;
	public static final org.lgna.project.ast.JavaMethod ROLL_METHOD;
	public static final org.lgna.project.ast.JavaMethod TURN_TO_FACE_METHOD;
	public static final org.lgna.project.ast.JavaMethod POINT_AT_METHOD;
	public static final org.lgna.project.ast.JavaMethod ORIENT_TO_UPRIGHT_METHOD;
	public static final org.lgna.project.ast.JavaMethod ORIENT_TO_METHOD;

	public static final org.lgna.project.ast.JavaMethod MOVE_METHOD;
	public static final org.lgna.project.ast.JavaMethod MOVE_TOWARD_METHOD;
	public static final org.lgna.project.ast.JavaMethod MOVE_AWAY_FROM_METHOD;
	public static final org.lgna.project.ast.JavaMethod MOVE_TO_METHOD;
	public static final org.lgna.project.ast.JavaMethod MOVE_AND_ORIENT_TO_METHOD;
	public static final org.lgna.project.ast.JavaMethod PLACE_METHOD;

	static {
		org.lgna.project.ast.JavaType turnableType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Turnable.class );
		org.lgna.project.ast.JavaType movableTurnableType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.MovableTurnable.class );

		TURN_METHOD = turnableType.getDeclaredMethod( "turn", org.lgna.story.TurnDirection.class, Number.class, org.lgna.story.Turn.Detail[].class );
		assert TURN_METHOD != null;
		ROLL_METHOD = turnableType.getDeclaredMethod( "roll", org.lgna.story.RollDirection.class, Number.class, org.lgna.story.Roll.Detail[].class );
		assert ROLL_METHOD != null;
		TURN_TO_FACE_METHOD = turnableType.getDeclaredMethod( "turnToFace", org.lgna.story.Entity.class, org.lgna.story.TurnToFace.Detail[].class );
		assert TURN_TO_FACE_METHOD != null;
		POINT_AT_METHOD = turnableType.getDeclaredMethod( "pointAt", org.lgna.story.Entity.class, org.lgna.story.PointAt.Detail[].class );
		assert POINT_AT_METHOD != null;
		ORIENT_TO_UPRIGHT_METHOD = turnableType.getDeclaredMethod( "orientToUpright", org.lgna.story.OrientToUpright.Detail[].class );
		assert ORIENT_TO_UPRIGHT_METHOD != null;
		ORIENT_TO_METHOD = turnableType.getDeclaredMethod( "orientTo", org.lgna.story.Entity.class, org.lgna.story.OrientTo.Detail[].class );
		assert ORIENT_TO_METHOD != null;

		MOVE_METHOD = movableTurnableType.getDeclaredMethod( "move", org.lgna.story.MoveDirection.class, Number.class, org.lgna.story.Move.Detail[].class );
		assert MOVE_METHOD != null;
		MOVE_TOWARD_METHOD = movableTurnableType.getDeclaredMethod( "moveToward", org.lgna.story.Entity.class, Number.class, org.lgna.story.MoveToward.Detail[].class );
		assert MOVE_TOWARD_METHOD != null;
		MOVE_AWAY_FROM_METHOD = movableTurnableType.getDeclaredMethod( "moveAwayFrom", org.lgna.story.Entity.class, Number.class, org.lgna.story.MoveAwayFrom.Detail[].class );
		assert MOVE_AWAY_FROM_METHOD != null;
		MOVE_TO_METHOD = movableTurnableType.getDeclaredMethod( "moveTo", org.lgna.story.Entity.class, org.lgna.story.MoveTo.Detail[].class );
		assert MOVE_TO_METHOD != null;
		MOVE_AND_ORIENT_TO_METHOD = movableTurnableType.getDeclaredMethod( "moveAndOrientTo", org.lgna.story.Entity.class, org.lgna.story.MoveAndOrientTo.Detail[].class );
		assert MOVE_AND_ORIENT_TO_METHOD != null;
		PLACE_METHOD = movableTurnableType.getDeclaredMethod( "place", org.lgna.story.SpatialRelation.class, org.lgna.story.Entity.class, org.lgna.story.Place.Detail[].class );
		assert PLACE_METHOD != null;

		double value = 1.0;
		final double INCREMENT = 0.01;
		map.put( MOVE_METHOD, value += INCREMENT );
		map.put( MOVE_TOWARD_METHOD, value += INCREMENT );
		map.put( MOVE_AWAY_FROM_METHOD, value += INCREMENT );
		map.put( TURN_METHOD, value += INCREMENT );
		map.put( ROLL_METHOD, value += INCREMENT );
		
		value = 2.0;
		map.put( MOVE_TO_METHOD, value += INCREMENT );
		map.put( MOVE_AND_ORIENT_TO_METHOD, value += INCREMENT );
		map.put( PLACE_METHOD, value += INCREMENT );
		map.put( TURN_TO_FACE_METHOD, value += INCREMENT );
		map.put( POINT_AT_METHOD, value += INCREMENT );
		map.put( ORIENT_TO_UPRIGHT_METHOD, value += INCREMENT );
		map.put( ORIENT_TO_METHOD, value += INCREMENT );
		
	}
	private static double getValue( org.lgna.project.ast.AbstractMember method ) {
		Double rv = map.get( method );
		if( rv != null ) {
			//pass
		} else {
			rv = 0.0;
		}
		return rv;
	}
}
