/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
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
