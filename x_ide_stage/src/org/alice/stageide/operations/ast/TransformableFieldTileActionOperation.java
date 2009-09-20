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
	private org.alice.apis.moveandturn.AbstractTransformable transformable;
	private org.alice.apis.moveandturn.PointOfView prevPOV;
	private org.alice.apis.moveandturn.PointOfView nextPOV;
	public TransformableFieldTileActionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
		this.putValue( javax.swing.Action.NAME, "Orient to Upright" );
	}
	protected abstract edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateNextAbsoluteTransformation( org.alice.apis.moveandturn.AbstractTransformable transformable );
	public void perform( zoot.ActionContext actionContext ) {
		this.transformable = this.getMoveAndTurnSceneEditor().getInstanceInJavaForField( this.getField(), org.alice.apis.moveandturn.AbstractTransformable.class );
		if( this.transformable != null ) {
			this.prevPOV = this.transformable.getPointOfView( org.alice.apis.moveandturn.AsSeenBy.SCENE );
			this.nextPOV = new org.alice.apis.moveandturn.PointOfView( this.calculateNextAbsoluteTransformation( this.transformable ) );
			if( this.nextPOV.getInternal().isNaN() ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: TransformableFieldTileActionOperation isNaN" );
				actionContext.cancel();
			} else {
				actionContext.commitAndInvokeRedoIfAppropriate();
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: TransformableFieldTileActionOperation" );
			actionContext.cancel();
		}
	}
	
	private void setAbsolutePOV( org.alice.apis.moveandturn.PointOfView pov ) {
		org.alice.apis.moveandturn.Scene scene = this.transformable.getScene();
		assert scene != null;
		this.transformable.moveAndOrientTo( scene.createOffsetStandIn( this.nextPOV.getInternal() ) );
	}
	@Override
	public void redo() throws javax.swing.undo.CannotRedoException {
		this.setAbsolutePOV( this.nextPOV );
	}
	@Override
	public void undo() throws javax.swing.undo.CannotUndoException {
		this.setAbsolutePOV( this.prevPOV );
	}
}
