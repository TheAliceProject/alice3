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
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class Operation extends AbstractCompletionModel {
	private javax.swing.Icon buttonIcon;

	public Operation( Group group, java.util.UUID id ) {
		super( group, id );
	}

	public org.lgna.croquet.imp.operation.OperationImp getImp() {
		return this.imp;
	}

	@Override
	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		return this.imp.getPotentialPrepModelPaths( edit );
	}

	protected String modifyNameIfNecessary( String text ) {
		return text;
	}

	@Override
	protected void localize() {
		String name = this.findDefaultLocalizedText();
		if( name != null ) {
			name = modifyNameIfNecessary( name );
			int mnemonicKey = this.getLocalizedMnemonicKey();
			safeSetNameAndMnemonic( this.imp.getSwingModel().getAction(), name, mnemonicKey );
			this.imp.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
		}
	}

	public boolean isToolBarTextClobbered() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.imp.getSwingModel().getAction().isEnabled();
	}

	@Override
	public void setEnabled( boolean isEnabled ) {
		this.imp.getSwingModel().getAction().setEnabled( isEnabled );
	}

	public void setName( String name ) {
		this.imp.setName( name );
	}

	public void setSmallIcon( javax.swing.Icon icon ) {
		this.imp.setSmallIcon( icon );
	}

	public void setToolTipText( String toolTipText ) {
		this.imp.setShortDescription( toolTipText );
	}

	public javax.swing.Icon getButtonIcon() {
		return this.buttonIcon;
	}

	public void setButtonIcon( javax.swing.Icon icon ) {
		this.buttonIcon = icon;
	}

	public StandardMenuItemPrepModel getMenuItemPrepModel() {
		return this.imp.getMenuItemPrepModel();
	}

	public <F, B> CascadeItem<F, B> getFauxCascadeItem() {
		return this.imp.getFauxCascadeItem();
	}

	public org.lgna.croquet.views.Button createButton( float fontScalar, edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return new org.lgna.croquet.views.Button( this, fontScalar, textAttributes );
	}

	public org.lgna.croquet.views.Button createButton( edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return this.createButton( 1.0f, textAttributes );
	}

	public org.lgna.croquet.views.Hyperlink createHyperlink( float fontScalar, edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return new org.lgna.croquet.views.Hyperlink( this, fontScalar, textAttributes );
	}

	public org.lgna.croquet.views.Hyperlink createHyperlink( edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return this.createHyperlink( 1.0f, textAttributes );
	}

	public org.lgna.croquet.views.ButtonWithRightClickCascade createButtonWithRightClickCascade( Cascade<?> cascade ) {
		return new org.lgna.croquet.views.ButtonWithRightClickCascade( this, cascade );
	}

	private final org.lgna.croquet.imp.operation.OperationImp imp = new org.lgna.croquet.imp.operation.OperationImp( this );
}
