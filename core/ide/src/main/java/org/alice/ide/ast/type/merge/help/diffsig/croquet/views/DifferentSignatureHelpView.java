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
package org.alice.ide.ast.type.merge.help.diffsig.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class DifferentSignatureHelpView extends org.alice.ide.ast.type.merge.help.croquet.views.PotentialNameChangerHelpView {
	private final org.lgna.croquet.event.ValueListener<org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureChoice> valueListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureChoice>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureChoice> e ) {
			handleTopLevelChanged( e.getNextValue() );
		}
	};

	public DifferentSignatureHelpView( org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureHelpComposite<?> composite ) {
		super( composite );
		org.lgna.croquet.views.RadioButton keepBothRadioButton = composite.getChoiceState().getItemSelectedState( org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureChoice.ADD_AND_RETAIN_BOTH ).createRadioButton();
		org.lgna.croquet.views.RadioButton selectOneRadioButton = composite.getChoiceState().getItemSelectedState( org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureChoice.ONLY_RETAIN_VERSION_ALREADY_IN_PROJECT ).createRadioButton();

		org.lgna.croquet.views.MigPanel panel = new org.lgna.croquet.views.MigPanel();
		panel.addComponent( keepBothRadioButton, "gap top 16, wrap" );
		panel.addComponent( this.getKeepBothPanel(), "gap 32, wrap" );
		panel.addComponent( selectOneRadioButton, "gap top 16, wrap" );
		this.addLineStartComponent( panel );
	}

	@Override
	public void handleCompositePreActivation() {
		org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureHelpComposite<?> composite = (org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureHelpComposite<?>)this.getComposite();
		composite.getChoiceState().addAndInvokeNewSchoolValueListener( this.valueListener );
		super.handleCompositePreActivation();
	}

	@Override
	public void handleCompositePostDeactivation() {
		super.handleCompositePostDeactivation();
		org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureHelpComposite<?> composite = (org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureHelpComposite<?>)this.getComposite();
		composite.getChoiceState().removeNewSchoolValueListener( this.valueListener );
	}

	private void handleTopLevelChanged( org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureChoice nextValue ) {
		boolean isKeepBoth = nextValue == org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureChoice.ADD_AND_RETAIN_BOTH;
		for( java.awt.Component awtComponent : this.getKeepBothPanel().getAwtComponent().getComponents() ) {
			awtComponent.setEnabled( isKeepBoth );
		}
	}

}
