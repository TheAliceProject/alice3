/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.poser.croquet;

import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;

/**
 * @author Matt May
 */
public final class PoseExpressionCreatorComposite extends org.lgna.croquet.SingleValueCreatorInputDialogCoreComposite<org.lgna.croquet.views.Panel, org.lgna.project.ast.Expression> {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.project.ast.NamedUserType, PoseExpressionCreatorComposite> map = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	public static PoseExpressionCreatorComposite getInstance( org.lgna.project.ast.NamedUserType declaringType ) {
		if( PoserComposite.isPoseable( declaringType ) ) {
			return map.getInitializingIfAbsent( declaringType, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.project.ast.NamedUserType, PoseExpressionCreatorComposite>() {
				@Override
				public PoseExpressionCreatorComposite initialize( org.lgna.project.ast.NamedUserType declaringType ) {
					return new PoseExpressionCreatorComposite( declaringType );
				}
			} );
		} else {
			return null;
		}
	}

	private PoseExpressionCreatorComposite( org.lgna.project.ast.NamedUserType declaringType ) {
		super( java.util.UUID.fromString( "4fc4dd4f-b33d-429a-994a-3a5cf13b6903" ) );
		this.poserComposite = this.registerSubComposite( org.lgna.ik.poser.croquet.PoserComposite.getDialogForUserType( declaringType ) );
		this.poserComposite.addStatusListener( this.statusUpdateListener );

	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		if( this.poserComposite.isEmptyPose() ) {
			return this.isEmptyPoseError;
		} else {
			return IS_GOOD_TO_GO_STATUS;
		}
	}

	@Override
	protected org.lgna.project.ast.Expression createValue() {
		try {
			return this.poserComposite.getControlComposite().createPoseExpression();
		} catch( CannotCreateExpressionException e ) {
			throw new Error( e );
		}
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		return new org.lgna.croquet.views.BorderPanel.Builder().center( this.poserComposite.getRootComponent() ).build();
	}

	private final ErrorStatus isEmptyPoseError = this.createErrorStatus( "isEmptyPoseError" );
	private final org.lgna.ik.poser.croquet.PoserComposite<?> poserComposite;
	private final StatusUpdateListener statusUpdateListener = new StatusUpdateListener() {
		@Override
		public void refreshStatus() {
			PoseExpressionCreatorComposite.this.refreshStatus();
		}
	};
}
