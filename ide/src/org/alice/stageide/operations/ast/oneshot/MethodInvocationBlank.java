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

package org.alice.stageide.operations.ast.oneshot;

/**
 * @author Dennis Cosgrove
 */
public class MethodInvocationBlank extends org.lgna.croquet.CascadeBlank< MethodInvocationEditFactory > {
	private static java.util.Map< org.lgna.project.ast.UserField, MethodInvocationBlank > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static MethodInvocationBlank getInstance( org.lgna.project.ast.UserField value ) {
		synchronized( map ) {
			MethodInvocationBlank rv = map.get( value );
			if( rv != null ) {
				//pass
			} else {
				rv = new MethodInvocationBlank( value );
				map.put( value, rv );
			}
			return rv;
		}
	}
	private final org.lgna.project.ast.UserField field;
	private MethodInvocationBlank( org.lgna.project.ast.UserField field ) {
		super( java.util.UUID.fromString( "3c5f528b-340b-4bcc-8094-3475867d2f6e" ) );
		this.field = field;
	}
	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< MethodInvocationEditFactory > blankNode ) {
		boolean isTurnable = this.field.getValueType().isAssignableTo( org.lgna.story.Turnable.class );
		boolean isMovable = this.field.getValueType().isAssignableTo( org.lgna.story.MovableTurnable.class );
		if( isMovable ) {
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.MovableTurnable.class, "move", org.lgna.story.MoveDirection.class, Number.class, org.lgna.story.Move.Detail[].class ) );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.Turnable.class, "turn", org.lgna.story.TurnDirection.class, Number.class, org.lgna.story.Turn.Detail[].class ) );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.Turnable.class, "roll", org.lgna.story.RollDirection.class, Number.class, org.lgna.story.Roll.Detail[].class ) );
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.MovableTurnable.class, "moveTo", org.lgna.story.Entity.class, org.lgna.story.MoveTo.Detail[].class ) );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.MovableTurnable.class, "moveToward", org.lgna.story.Entity.class, Number.class, org.lgna.story.MoveToward.Detail[].class ) );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.MovableTurnable.class, "moveAwayFrom", org.lgna.story.Entity.class, Number.class, org.lgna.story.MoveAwayFrom.Detail[].class ) );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.Turnable.class, "turnToFace", org.lgna.story.Entity.class, org.lgna.story.TurnToFace.Detail[].class ) );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.Turnable.class, "pointAt", org.lgna.story.Entity.class, org.lgna.story.PointAt.Detail[].class ) );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.Turnable.class, "orientTo", org.lgna.story.Entity.class, org.lgna.story.OrientTo.Detail[].class ) );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.MovableTurnable.class, "moveAndOrientTo", org.lgna.story.Entity.class, org.lgna.story.MoveAndOrientTo.Detail[].class ) );
			rv.add( LocalTransformationMethodInvocationFillIn.getInstance( this.field, org.lgna.story.Turnable.class, "standUp", org.lgna.story.StandUp.Detail[].class ) );
			
			//rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			//rv.add( ColorMethodInvocationFillIn.getInstance( this.field, org.lgna.story.Model.class, "setPaint", org.lgna.story.Paint.class, org.lgna.story.SetPaint.Detail[].class ) );
		}
		return rv;
	}
}
