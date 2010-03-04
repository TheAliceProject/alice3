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
public class PredeterminedSetLocalTransformationActionOperation extends AbstractSetLocalTransformationActionOperation {
	private edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 prevLT;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextLT;
	private String editPresentationKey;
	public PredeterminedSetLocalTransformationActionOperation( java.util.UUID groupUUID, boolean isDoRequired, edu.cmu.cs.dennisc.animation.Animator animator, edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable, edu.cmu.cs.dennisc.math.AffineMatrix4x4 prevLT, edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextLT, String editPresentationKey ) {
		super( groupUUID, isDoRequired, animator );
		this.sgTransformable = sgTransformable;
		this.prevLT = prevLT;
		this.nextLT = nextLT;
		this.editPresentationKey = editPresentationKey;
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getSGTransformable() {
		return this.sgTransformable;
	}
	@Override
	protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 getNextLocalTransformation() {
		return this.nextLT;
	}
	@Override
	protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 getPrevLocalTransformation() {
		return this.prevLT;
	}
	@Override
	protected String getEditPresentationName( java.util.Locale locale ) {
		return this.editPresentationKey;
	}
	
}
