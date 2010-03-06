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
package org.alice.interact.operations;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSetLocalTransformationActionOperation extends edu.cmu.cs.dennisc.zoot.AbstractActionOperation {
	private boolean isDoRequired;
	private edu.cmu.cs.dennisc.animation.Animator animator;
	public AbstractSetLocalTransformationActionOperation( java.util.UUID groupUUID, boolean isDoRequired, edu.cmu.cs.dennisc.animation.Animator animator ) {
		super( groupUUID );
		this.isDoRequired = isDoRequired;
		this.animator = animator;
	}
	protected abstract edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getSGTransformable();
	protected abstract edu.cmu.cs.dennisc.math.AffineMatrix4x4 getPrevLocalTransformation();
	protected abstract edu.cmu.cs.dennisc.math.AffineMatrix4x4 getNextLocalTransformation();
	protected abstract String getEditPresentationName( java.util.Locale locale );
	
	private void setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 lt ) {
		edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable = this.getSGTransformable();
		if( this.animator != null ) {
			edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation povAnimation = new edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation( sgTransformable, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT, null, lt );
			povAnimation.setDuration( 0.5 );
			//this.animator.complete( null );
			this.animator.invokeLater( povAnimation, null );
		} else {
			sgTransformable.localTransformation.setValue( lt );
		}
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		final edu.cmu.cs.dennisc.math.AffineMatrix4x4 prevLT = this.getPrevLocalTransformation();
		final edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextLT = this.getNextLocalTransformation();
		assert prevLT != null;
		assert nextLT != null;
		assert prevLT.isNaN() == false;
		assert nextLT.isNaN() == false;
		actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
			@Override
			public void doOrRedo( boolean isDo ) {
				if( isDo && ( isDoRequired == false ) ) {
					//pass
				} else {
					setLocalTransformation( nextLT );
				}
			}
			@Override
			public void undo() {
				setLocalTransformation( prevLT );
			}
			@Override
			protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
				rv.append( getEditPresentationName( locale ) );
				return rv;
			}
		} );
	}
}
