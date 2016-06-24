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
package org.alice.ide.ast.type.preview.croquet;

import org.alice.ide.ast.type.croquet.ImportTypeWizard;

/**
 * @author Dennis Cosgrove
 */
public class PreviewPage extends org.lgna.croquet.WizardPageComposite<org.lgna.croquet.views.Panel, ImportTypeWizard> {
	private final org.lgna.croquet.BooleanState isIncludingAllState = this.createBooleanState( "isIncludingAllState", false );
	private final org.lgna.croquet.event.ValueListener<Boolean> isIncludingAllListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
			getView().refreshLater();
		}
	};

	public PreviewPage( ImportTypeWizard wizard ) {
		super( java.util.UUID.fromString( "2efecc6f-eb6a-4835-80e3-6898022c3cc2" ), wizard );
	}

	public org.lgna.croquet.BooleanState getIsIncludingAllState() {
		return this.isIncludingAllState;
	}

	@Override
	public Status getPageStatus( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	@Override
	public boolean isClearToCommit() {
		return true;
	}

	@Override
	public boolean isAccountedForInPreferredSizeCalculation() {
		return false;
	}

	@Override
	public void resetData() {
	}

	@Override
	public void handlePreActivation() {
		this.isIncludingAllState.addAndInvokeNewSchoolValueListener( this.isIncludingAllListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		this.isIncludingAllState.removeNewSchoolValueListener( this.isIncludingAllListener );
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		return new org.alice.ide.ast.type.preview.croquet.views.PreviewPane( this );
	}
}
