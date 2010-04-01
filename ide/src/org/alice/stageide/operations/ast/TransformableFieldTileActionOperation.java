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
package org.alice.stageide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class TransformableFieldTileActionOperation extends AbstractFieldTileActionOperation {
	public TransformableFieldTileActionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
	}
	protected abstract edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateNextAbsoluteTransformation( org.alice.apis.moveandturn.AbstractTransformable transformable );
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		final org.alice.apis.moveandturn.AbstractTransformable transformable;
		final org.alice.apis.moveandturn.PointOfView prevPOV;
		final org.alice.apis.moveandturn.PointOfView nextPOV;
		transformable = this.getMoveAndTurnSceneEditor().getInstanceInJavaForField( this.getField(), org.alice.apis.moveandturn.AbstractTransformable.class );
		if( transformable != null ) {
			prevPOV = transformable.getPointOfView( org.alice.apis.moveandturn.AsSeenBy.SCENE );
			nextPOV = new org.alice.apis.moveandturn.PointOfView( this.calculateNextAbsoluteTransformation( transformable ) );
			if( nextPOV.getInternal().isNaN() ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: TransformableFieldTileActionOperation isNaN" );
				actionContext.cancel();
			} else {
				actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						setAbsolutePOV( transformable, nextPOV );
					}
					@Override
					public void undo() {
						setAbsolutePOV( transformable, prevPOV );
					}
					@Override
					protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
						//todo
						rv.append( getActionForConfiguringSwing().getValue( javax.swing.Action.NAME ) );
						return rv;
					}
				} );
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: TransformableFieldTileActionOperation" );
			actionContext.cancel();
		}
	}
	
	private static void setAbsolutePOV( org.alice.apis.moveandturn.AbstractTransformable transformable, org.alice.apis.moveandturn.PointOfView pov ) {
		org.alice.apis.moveandturn.Scene scene = transformable.getScene();
		assert scene != null;
		transformable.moveAndOrientTo( scene.createOffsetStandIn( pov.getInternal() ) );
	}
}
