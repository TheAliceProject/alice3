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
public class MergeTypeView extends org.lgna.croquet.components.MigPanel {
	private static final edu.cmu.cs.dennisc.java.awt.font.TextAttribute[] HEADER_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD };
	private static final edu.cmu.cs.dennisc.java.awt.font.TextAttribute[] NO_OP_LABEL_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE };
	private static final String HEADER_CONSTRAINT = "gap 16, wrap";
	private static final String DECLARATION_CHECK_BOX_CONSTRAINT = "gap 32, wrap";
	private static final String DECLARATION_LABEL_CONSTRAINT = "gap 32, wrap";

	private static java.awt.Dimension ICON_SIZE = org.lgna.croquet.icon.IconSize.SMALL.getSize();
	private static javax.swing.Icon PLUS_ICON = org.alice.stageide.icons.PlusIconFactory.getInstance().getIcon( ICON_SIZE );
	private static javax.swing.Icon EMPTY_ICON = org.lgna.croquet.icon.EmptyIconFactory.getInstance().getIcon( ICON_SIZE );

	private static org.lgna.croquet.components.AbstractLabel createHeaderLabel( org.lgna.croquet.PlainStringValue stringValue ) {
		org.lgna.croquet.components.AbstractLabel rv = stringValue.createLabel( 1.2f, HEADER_TEXT_ATTRIBUTES );
		return rv;
	}

	private static org.lgna.croquet.components.AbstractLabel createNoOpLabel( org.lgna.project.ast.UserMethod method, String bonusText ) {
		org.lgna.croquet.components.AbstractLabel rv = new org.lgna.croquet.components.Label( method.name.getValue() + bonusText, NO_OP_LABEL_TEXT_ATTRIBUTES );
		rv.setIcon( EMPTY_ICON );
		return rv;
	}

	public MergeTypeView( org.alice.stageide.gallerybrowser.uri.merge.ImportTypeComposite composite ) {
		super( composite );
		org.lgna.croquet.components.Label classLabel = new org.lgna.croquet.components.Label( "class", org.alice.ide.common.TypeIcon.getInstance( composite.getDstType() ), 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		classLabel.setHorizontalTextPosition( org.lgna.croquet.components.HorizontalTextPosition.LEADING );
		this.addComponent( classLabel, "wrap" );
		this.addTypeMethodCategorization( composite.getProcedureCategorization(), composite.getProceduresHeader() );
		this.addTypeMethodCategorization( composite.getFunctionCategorization(), composite.getFunctionsHeader() );

		java.util.List<org.alice.stageide.gallerybrowser.uri.merge.data.IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>> isFieldImportDesiredStates = composite.getIsFieldImportDesiredStates();
		if( isFieldImportDesiredStates.size() > 0 ) {
			this.addComponent( createHeaderLabel( composite.getFieldsHeader() ), HEADER_CONSTRAINT );
			for( org.alice.stageide.gallerybrowser.uri.merge.data.IsDeclarationImportDesiredState<org.lgna.project.ast.UserField> isFieldImportDesiredState : isFieldImportDesiredStates ) {
				this.addComponent( isFieldImportDesiredState.createCheckBox(), DECLARATION_CHECK_BOX_CONSTRAINT );
			}
		}

		this.setBackgroundColor( org.alice.ide.theme.ThemeUtilities.getActiveTheme().getTypeColor() );
	}

	private void addTypeMethodCategorization( org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization categorization, org.lgna.croquet.PlainStringValue header ) {
		if( categorization.getTotalCount() > 0 ) {
			this.addComponent( createHeaderLabel( header ), HEADER_CONSTRAINT );
			for( org.alice.stageide.gallerybrowser.uri.merge.data.ImportOnlyMethod importOnlyMethod : categorization.getImportOnlyMethods() ) {
				org.lgna.croquet.components.CheckBox checkBox = importOnlyMethod.getState().createCheckBox();
				checkBox.getAwtComponent().setIcon( PLUS_ICON );
				this.addComponent( checkBox, DECLARATION_CHECK_BOX_CONSTRAINT );
			}

			for( org.alice.stageide.gallerybrowser.uri.merge.data.IdenticalMethods identicalMethods : categorization.getIdenticalMethods() ) {
				this.addComponent( createNoOpLabel( identicalMethods.getProjectMethod(), " (identical)" ), DECLARATION_LABEL_CONSTRAINT );
			}
			for( org.alice.stageide.gallerybrowser.uri.merge.data.ProjectOnlyMethod projectOnlyMethod : categorization.getProjectOnlyMethods() ) {
				this.addComponent( createNoOpLabel( projectOnlyMethod.getProjectMethod(), "" ), DECLARATION_LABEL_CONSTRAINT );
			}
		}
	}
}
