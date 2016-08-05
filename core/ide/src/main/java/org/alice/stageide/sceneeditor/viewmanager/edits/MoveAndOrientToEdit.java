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
package org.alice.stageide.sceneeditor.viewmanager.edits;

import org.lgna.croquet.edits.AbstractEdit;

/**
 * @author dculyba
 * 
 */
public class MoveAndOrientToEdit extends AbstractEdit {
	private final org.lgna.story.SMovableTurnable toMove;
	private final org.lgna.story.SThing target;
	private transient org.lgna.story.implementation.AbstractTransformableImp transformable;
	private transient edu.cmu.cs.dennisc.math.AffineMatrix4x4 m;

	public MoveAndOrientToEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.story.SMovableTurnable toMove, org.lgna.story.SThing target ) {
		super( completionStep );
		this.toMove = toMove;
		this.target = target;
	}

	public MoveAndOrientToEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.toMove = null;
		this.target = null;

	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		assert false : "Not implemented yet";
	}

	@Override
	protected void doOrRedoInternal( boolean isDo ) {
		if( ( this.toMove != null ) && ( this.target != null ) ) {
			this.transformable = org.lgna.story.EmployeesOnly.getImplementation( this.toMove );
			this.m = this.transformable.getAbsoluteTransformation();
			org.lgna.story.implementation.EntityImp targetImp = org.lgna.story.EmployeesOnly.getImplementation( this.target );
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 targetTransform = targetImp.getAbsoluteTransformation();
			this.transformable.animateTransformation( org.lgna.story.implementation.AsSeenBy.SCENE, targetTransform );
		} else {
			this.transformable = null;
			this.m = null;
		}
	}

	@Override
	protected void undoInternal() {
		if( ( this.transformable != null ) && ( this.m != null ) ) {
			this.transformable.animateTransformation( org.lgna.story.implementation.AsSeenBy.SCENE, this.m );
		}
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "move and orient to:" );
		//todo
	}

}
