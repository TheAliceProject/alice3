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

import org.alice.ide.name.validators.MethodNameValidator;
import org.lgna.croquet.history.CompletionStep;

/**
 * @author Matt Mayy
 */
public abstract class AnimationProcedureDialog extends org.lgna.croquet.SimpleOperationInputDialogCoreComposite<org.lgna.croquet.views.Panel> {

	public AnimationProcedureDialog( java.util.UUID migrationId, org.lgna.ik.poser.croquet.AnimatorComposite animatorComposite ) {
		super( migrationId, org.alice.ide.IDE.PROJECT_GROUP );
		this.animatorComposite = this.registerSubComposite( animatorComposite );
		this.animatorComposite.addStatusListener( statusUpdateListener );
	}

	@Override
	protected org.lgna.croquet.AbstractSeverityStatusComposite.Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		if( animatorComposite.getControlComposite().isEmpty() ) {
			return empty;
		}
		if( validator != null ) {
			//pass
		} else {
			this.validator = new MethodNameValidator( animatorComposite.getDeclaringType() );
		}
		String candidate = animatorComposite.getControlComposite().getNameState().getValue();
		String explanation = validator.getExplanationIfOkButtonShouldBeDisabled( candidate );
		if( explanation != null ) {
			errorStatus.setText( explanation );
			return errorStatus;
		}
		return IS_GOOD_TO_GO_STATUS;
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		return new org.lgna.croquet.views.BorderPanel.Builder().center( this.animatorComposite.getRootComponent() ).build();
	}

	private final org.lgna.ik.poser.croquet.AnimatorComposite<?> animatorComposite;

	private final StatusUpdateListener statusUpdateListener = new StatusUpdateListener() {
		@Override
		public void refreshStatus() {
			AnimationProcedureDialog.this.refreshStatus();
		}
	};

	public org.lgna.ik.poser.croquet.AnimatorComposite<?> getAnimatorComposite() {
		return this.animatorComposite;
	}

	private final org.lgna.croquet.AbstractSeverityStatusComposite.WarningStatus empty = createWarningStatus( "noPoses" );
	private final org.lgna.croquet.AbstractSeverityStatusComposite.ErrorStatus errorStatus = createErrorStatus( "errorStatus" );
	private MethodNameValidator validator;
}
