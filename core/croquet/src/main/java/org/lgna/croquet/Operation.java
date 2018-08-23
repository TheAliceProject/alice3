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

import edu.cmu.cs.dennisc.java.awt.font.TextAttribute;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.imp.operation.OperationImp;
import org.lgna.croquet.views.Button;
import org.lgna.croquet.views.ButtonWithRightClickCascade;
import org.lgna.croquet.views.Hyperlink;

import javax.swing.Icon;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class Operation extends AbstractCompletionModel {
	private Icon buttonIcon;

	public Operation( Group group, UUID id ) {
		super( group, id );
	}

	public OperationImp getImp() {
		return this.imp;
	}

	@Override
	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
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

	public void setSmallIcon( Icon icon ) {
		this.imp.setSmallIcon( icon );
	}

	public void setToolTipText( String toolTipText ) {
		this.imp.setShortDescription( toolTipText );
	}

	public Icon getButtonIcon() {
		return this.buttonIcon;
	}

	public void setButtonIcon( Icon icon ) {
		this.buttonIcon = icon;
	}

	public StandardMenuItemPrepModel getMenuItemPrepModel() {
		return this.imp.getMenuItemPrepModel();
	}

	public Button createButton( float fontScalar, TextAttribute<?>... textAttributes ) {
		return new Button( this, fontScalar, textAttributes );
	}

	public Button createButton( TextAttribute<?>... textAttributes ) {
		return this.createButton( 1.0f, textAttributes );
	}

	public Hyperlink createHyperlink( float fontScalar, TextAttribute<?>... textAttributes ) {
		return new Hyperlink( this, fontScalar, textAttributes );
	}

	public Hyperlink createHyperlink( TextAttribute<?>... textAttributes ) {
		return this.createHyperlink( 1.0f, textAttributes );
	}

	public ButtonWithRightClickCascade createButtonWithRightClickCascade( Cascade<?> cascade ) {
		return new ButtonWithRightClickCascade( this, cascade );
	}

	private final OperationImp imp = new OperationImp( this );
}
