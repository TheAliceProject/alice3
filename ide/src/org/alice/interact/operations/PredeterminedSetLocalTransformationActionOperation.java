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
package org.alice.interact.operations;

/**
 * @author Dennis Cosgrove
 */
public class PredeterminedSetLocalTransformationActionOperation extends AbstractSetLocalTransformationActionOperation {
	private edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 prevLT;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextLT;
	private String editPresentationKey;
	public PredeterminedSetLocalTransformationActionOperation( org.lgna.croquet.Group group, boolean isDoRequired, edu.cmu.cs.dennisc.animation.Animator animator, edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable, edu.cmu.cs.dennisc.math.AffineMatrix4x4 prevLT, edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextLT, String editPresentationKey ) {
		super( group, java.util.UUID.fromString( "461e0745-a8b4-4fca-8af7-5efb213f855b" ), isDoRequired, animator );
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
