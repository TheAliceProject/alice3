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
package org.alice.stageide.gallerybrowser.uri.merge.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddMembersView<D extends org.lgna.project.ast.Declaration> extends org.lgna.croquet.components.MigPanel {
	private static final edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>[] NO_OP_LABEL_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.REGULAR, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE };

	//private static java.awt.Dimension ICON_SIZE = new java.awt.Dimension( 36, 22 );
	private static java.awt.Dimension ICON_SIZE = new java.awt.Dimension( 22, 22 );
	public static javax.swing.Icon PLUS_ICON = org.alice.stageide.icons.PlusIconFactory.getInstance().getIcon( ICON_SIZE );

	//public static javax.swing.Icon PLUS_AND_CHECK_ICON = new org.alice.stageide.gallerybrowser.uri.merge.views.icons.CheckPlusIcon( ICON_SIZE, true );
	//public static javax.swing.Icon CHECK_ONLY_ICON = new org.alice.stageide.gallerybrowser.uri.merge.views.icons.CheckPlusIcon( ICON_SIZE, false );
	public static javax.swing.Icon EMPTY_ICON = org.lgna.croquet.icon.EmptyIconFactory.getInstance().getIcon( ICON_SIZE );

	private static org.lgna.croquet.components.AbstractLabel createNoOpLabel( org.lgna.project.ast.Declaration declaration, String bonusText ) {
		org.lgna.croquet.components.AbstractLabel rv = new org.lgna.croquet.components.Label( declaration.getName() + bonusText, NO_OP_LABEL_TEXT_ATTRIBUTES );
		//rv.setIcon( CHECK_ONLY_ICON );
		return rv;
	}

	private static org.lgna.croquet.components.AbstractLabel createHeader( org.lgna.croquet.PlainStringValue stringValue ) {
		final edu.cmu.cs.dennisc.java.awt.font.TextAttribute[] HEADER_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE };
		org.lgna.croquet.components.AbstractLabel header = stringValue.createLabel( HEADER_TEXT_ATTRIBUTES );
		//header.setForegroundColor( java.awt.Color.GRAY );
		return header;
	}

	private static org.lgna.croquet.components.TextField createTextField( org.lgna.croquet.StringState state ) {
		org.lgna.croquet.components.TextField textField = state.createTextField();
		textField.enableSelectAllWhenFocusGained();
		return textField;
	}

	private static org.lgna.croquet.components.HorizontalSeparator createSeparator() {
		org.lgna.croquet.components.HorizontalSeparator rv = new org.lgna.croquet.components.HorizontalSeparator();
		return rv;
	}

	private static <D extends org.lgna.project.ast.Declaration> org.lgna.croquet.components.PopupView createPopupView( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, D> composite, D member ) {
		return composite.getPopupDeclarationFor( member ).getElement().createPopupView();
	}

	public AddMembersView( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, D> composite, java.awt.Color backgroundColor ) {
		super( composite, "fill", "[]32[]32" );
		//todo
		backgroundColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( backgroundColor, 1.0, 1.0, 1.1 );
		this.setBackgroundColor( backgroundColor );

		if( composite.getKeepCount() > 0 ) {
			this.addComponent( createHeader( composite.getAddLabel() ) );
			this.addComponent( createHeader( composite.getKeepLabel() ), "wrap" );
			this.addComponent( createSeparator(), "grow, shrink" );
			this.addComponent( createSeparator(), "grow, shrink, wrap" );
		} else {
			if( composite.getAddCount() > 0 ) {
				this.addComponent( createHeader( composite.getAddLabel() ), "wrap" );
				this.addComponent( createSeparator(), "grow, shrink, wrap" );
			}
		}
		for( org.alice.stageide.gallerybrowser.uri.merge.data.ImportOnlyDeclaration<D> importOnlyDeclaration : composite.getImportOnlyDeclarations() ) {
			org.lgna.croquet.components.CheckBox checkBox = importOnlyDeclaration.getState().createCheckBox();
			checkBox.getAwtComponent().setIcon( PLUS_ICON );
			this.addComponent( checkBox, "split 2" );
			this.addComponent( createPopupView( composite, importOnlyDeclaration.getState().getDeclaration() ), "wrap" );
		}

		for( org.alice.stageide.gallerybrowser.uri.merge.data.DifferentSignatureDeclarations<D> differentSignatureDeclaration : composite.getDifferentSignatureDeclarations() ) {
			this.addComponent( new org.lgna.croquet.components.Label( AddMembersView.PLUS_ICON ), "split 3" );
			this.addComponent( createTextField( differentSignatureDeclaration.getImportNameState() ) );
			this.addComponent( createPopupView( composite, differentSignatureDeclaration.getImportNameState().getMember() ), "wrap" );
			//this.addComponent( new org.lgna.croquet.components.Label( AddMembersView.CHECK_ONLY_ICON ), "skip 1, split 2" );
			this.addComponent( createTextField( differentSignatureDeclaration.getProjectNameState() ), "skip 1, split 2" );
			this.addComponent( createPopupView( composite, differentSignatureDeclaration.getProjectNameState().getMember() ), "wrap" );
		}
		for( org.alice.stageide.gallerybrowser.uri.merge.data.IdenticalDeclarations<D> identicalDeclarations : composite.getIdenticalDeclarations() ) {
			org.lgna.croquet.components.AbstractLabel importLabel = createNoOpLabel( identicalDeclarations.getImportDeclaration(), " (identical)" );
			importLabel.setIcon( EMPTY_ICON );
			importLabel.setForegroundColor( java.awt.Color.GRAY );
			this.addComponent( importLabel, "split 2" );
			this.addComponent( createPopupView( composite, identicalDeclarations.getImportDeclaration() ) );
			this.addComponent( createNoOpLabel( identicalDeclarations.getProjectDeclaration(), "" ), "split 2" );
			this.addComponent( createPopupView( composite, identicalDeclarations.getProjectDeclaration() ), "wrap" );
		}
		for( org.alice.stageide.gallerybrowser.uri.merge.data.ProjectOnlyDeclaration<D> projectOnlyDeclaration : composite.getProjectOnlyDeclarations() ) {
			this.addComponent( createNoOpLabel( projectOnlyDeclaration.getProjectDeclaration(), "" ), "skip 1, split 2" );
			this.addComponent( createPopupView( composite, projectOnlyDeclaration.getProjectDeclaration() ), "wrap" );
		}
	}
}
