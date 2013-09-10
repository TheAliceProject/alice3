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
package org.alice.ide.ast.type.merge.help.diffimp.croquet.views;

import org.alice.ide.ast.type.merge.croquet.views.MemberViewUtilities;

/**
 * @author Dennis Cosgrove
 */
public class DifferentImplementationHelpView extends org.alice.ide.ast.type.merge.help.croquet.views.PotentialNameChangerHelpView {
	private final org.lgna.croquet.State.ValueListener<org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice> valueListener = new org.lgna.croquet.State.ValueListener<org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice>() {
		public void changing( org.lgna.croquet.State<org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice> state, org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice prevValue, org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice> state, org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice prevValue, org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice nextValue, boolean isAdjusting ) {
			handleTopLevelChanged( nextValue );
		}
	};

	private final org.lgna.croquet.components.MigPanel keepBothPanel = new org.lgna.croquet.components.MigPanel( null, "fill" );
	private final org.lgna.croquet.components.MigPanel selectOnePanel = new org.lgna.croquet.components.MigPanel( null, "fill" );

	public DifferentImplementationHelpView( org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationHelpComposite<?> composite ) {
		super( composite );
		org.lgna.croquet.components.RadioButton keepBothRadioButton = composite.getTopLevelChoiceState().getItemSelectedState( org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice.KEEP_BOTH_AND_RENAME ).createRadioButton();
		org.lgna.croquet.components.RadioButton selectOneRadioButton = composite.getTopLevelChoiceState().getItemSelectedState( org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice.SELECT_ONE ).createRadioButton();

		edu.cmu.cs.dennisc.javax.swing.ColorCustomizer foregroundCustomizer = composite.getForegroundCustomizer();

		org.alice.ide.ast.type.merge.croquet.DifferentImplementation differentImplementation = composite.getPotentialNameChanger();
		this.keepBothPanel.addComponent( composite.getImportNameText().createLabel(), "align right" );
		this.keepBothPanel.addComponent( MemberViewUtilities.createTextField( differentImplementation.getImportHub().getNameState(), foregroundCustomizer ), "wrap" );
		this.keepBothPanel.addComponent( composite.getProjectNameText().createLabel(), "align right" );
		this.keepBothPanel.addComponent( MemberViewUtilities.createTextField( differentImplementation.getProjectHub().getNameState(), foregroundCustomizer ), "wrap" );

		org.lgna.croquet.components.ToggleButton fromImportToggleButton = composite.getSelectOneState().getItemSelectedState( org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationSelectOne.FROM_IMPORT ).createToggleButton();
		org.lgna.croquet.components.ToggleButton alreadyInProjectToggleButton = composite.getSelectOneState().getItemSelectedState( org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationSelectOne.ALREADY_IN_PROJECT ).createToggleButton();
		this.selectOnePanel.addComponent( fromImportToggleButton, "wrap" );
		this.selectOnePanel.addComponent( alreadyInProjectToggleButton, "wrap" );

		org.lgna.croquet.components.MigPanel panel = new org.lgna.croquet.components.MigPanel();
		panel.addComponent( composite.getHeader().createLabel(), "wrap" );
		panel.addComponent( keepBothRadioButton, "wrap" );
		panel.addComponent( this.keepBothPanel, "gap 32, wrap" );
		panel.addComponent( selectOneRadioButton, "wrap" );
		panel.addComponent( this.selectOnePanel, "gap 32, wrap" );
		this.addCenterComponent( panel );
	}

	@Override
	public void handleCompositePreActivation() {
		org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationHelpComposite<?> composite = (org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationHelpComposite<?>)this.getComposite();
		composite.getTopLevelChoiceState().addAndInvokeValueListener( this.valueListener );
		super.handleCompositePreActivation();
	}

	@Override
	public void handleCompositePostDeactivation() {
		super.handleCompositePostDeactivation();
		org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationHelpComposite<?> composite = (org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationHelpComposite<?>)this.getComposite();
		composite.getTopLevelChoiceState().removeValueListener( this.valueListener );
	}

	private void handleTopLevelChanged( org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice nextValue ) {
		boolean isKeepBoth = nextValue == org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice.KEEP_BOTH_AND_RENAME;
		boolean isSelectOne = nextValue == org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationTopLevelChoice.SELECT_ONE;
		for( java.awt.Component awtComponent : this.keepBothPanel.getAwtComponent().getComponents() ) {
			awtComponent.setEnabled( isKeepBoth );
		}
		for( java.awt.Component awtComponent : this.selectOnePanel.getAwtComponent().getComponents() ) {
			awtComponent.setEnabled( isSelectOne );
		}
	}

}
