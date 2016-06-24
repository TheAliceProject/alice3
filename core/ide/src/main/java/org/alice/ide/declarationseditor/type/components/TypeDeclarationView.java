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

package org.alice.ide.declarationseditor.type.components;

/**
 * @author Dennis Cosgrove
 */
public class TypeDeclarationView extends org.alice.ide.declarationseditor.components.DeclarationView {
	public TypeDeclarationView( org.alice.ide.declarationseditor.TypeComposite composite ) {
		super( composite );
		org.lgna.project.ast.NamedUserType type = (org.lgna.project.ast.NamedUserType)composite.getDeclaration();
		org.alice.ide.Theme theme = org.alice.ide.ThemeUtilities.getActiveTheme();
		this.setBackgroundColor( theme.getMutedTypeColor() );

		org.lgna.croquet.views.ToolPaletteView constructorsToolPalette = composite.getConstructorsToolPaletteCoreComposite().getOuterComposite().getView();
		constructorsToolPalette.setBackgroundColor( theme.getConstructorColor() );

		org.lgna.croquet.views.ToolPaletteView proceduresToolPalette = composite.getProceduresToolPaletteCoreComposite().getOuterComposite().getView();
		proceduresToolPalette.setBackgroundColor( theme.getProcedureColor() );

		org.lgna.croquet.views.ToolPaletteView functionsToolPalette = composite.getFunctionsToolPaletteCoreComposite().getOuterComposite().getView();
		functionsToolPalette.setBackgroundColor( theme.getFunctionColor() );

		org.lgna.croquet.views.ToolPaletteView fieldsToolPalette = composite.getFieldsToolPaletteCoreComposite().getOuterComposite().getView();
		fieldsToolPalette.setBackgroundColor( theme.getFieldColor() );

		for( org.lgna.croquet.views.ToolPaletteView toolPalette : new org.lgna.croquet.views.ToolPaletteView[] { constructorsToolPalette, proceduresToolPalette, functionsToolPalette, fieldsToolPalette } ) {
			toolPalette.getTitle().changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
			toolPalette.getTitle().scaleFont( 1.4f );
			toolPalette.getTitle().setRoundedOnTop( true );
			toolPalette.getCenterView().setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 14, 4, 4 ) );
		}

		org.lgna.croquet.views.PageAxisPanel membersPanel = new org.lgna.croquet.views.PageAxisPanel();
		if( org.alice.ide.croquet.models.ui.preferences.IsIncludingConstructors.getInstance().getValue() ) {
			membersPanel.addComponent( constructorsToolPalette );
			membersPanel.addComponent( org.lgna.croquet.views.BoxUtilities.createVerticalSliver( 16 ) );
		}
		membersPanel.addComponent( proceduresToolPalette );
		membersPanel.addComponent( org.lgna.croquet.views.BoxUtilities.createVerticalSliver( 16 ) );
		membersPanel.addComponent( functionsToolPalette );
		membersPanel.addComponent( org.lgna.croquet.views.BoxUtilities.createVerticalSliver( 16 ) );
		membersPanel.addComponent( fieldsToolPalette );
		membersPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 12, 24, 0, 0 ) );
		membersPanel.setBackgroundColor( this.getBackgroundColor() );

		outerMainPanel.setBackgroundColor( this.getBackgroundColor() );
		typePanel.setBackgroundColor( this.getBackgroundColor() );

		scrollPane.setBorder( null );
		scrollPane.setBackgroundColor( this.getBackgroundColor() );

		org.alice.ide.ast.declaration.views.TypeHeader typeHeader = new org.alice.ide.ast.declaration.views.TypeHeader( type );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );

		org.alice.ide.ApiConfigurationManager apiConfigurationManager = org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager();
		if( apiConfigurationManager.isExportTypeDesiredFor( type ) ) {
			org.lgna.croquet.views.LineAxisPanel header = new org.lgna.croquet.views.LineAxisPanel(
					typeHeader,
					org.lgna.croquet.views.BoxUtilities.createHorizontalSliver( 8 ),
					composite.getImportOperation().createButton(),
					composite.getExportOperation().createButton()
					);
			this.typePanel.addPageStartComponent( header );
		} else {
			this.typePanel.addPageStartComponent( typeHeader );
		}
		this.typePanel.addCenterComponent( new org.lgna.croquet.views.BorderPanel.Builder().pageStart( membersPanel ).build() );

		for( javax.swing.JComponent component : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( typeHeader.getAwtComponent(), edu.cmu.cs.dennisc.pattern.HowMuch.DESCENDANTS_ONLY, javax.swing.JComponent.class ) ) {
			edu.cmu.cs.dennisc.java.awt.FontUtilities.setFontToScaledFont( component, 1.2f );
		}
	}

	@Override
	public void addPotentialDropReceptors( java.util.List<org.lgna.croquet.DropReceptor> out, org.alice.ide.croquet.models.IdeDragModel dragModel ) {
	}

	@Override
	protected void setJavaCodeOnTheSide( boolean value, boolean isFirstTime ) {
		super.setJavaCodeOnTheSide( value, isFirstTime );
		if( value ) {
			if( isFirstTime ) {
				//pass
			} else {
				this.outerMainPanel.removeComponent( this.scrollPane );
			}
			this.scrollPane.setViewportView( null );
			this.outerMainPanel.addCenterComponent( this.typePanel );
		} else {
			if( isFirstTime ) {
				//pass
			} else {
				this.outerMainPanel.removeComponent( this.typePanel );
			}
			this.scrollPane.setViewportView( this.typePanel );
			this.outerMainPanel.addCenterComponent( this.scrollPane );
		}
	}

	@Override
	public java.awt.print.Printable getPrintable() {
		return new edu.cmu.cs.dennisc.java.awt.PrintHelper.Builder( this.getInsets(), this.getBackgroundColor() )
				.pageStart( this.getPageStartComponent().getAwtComponent() )
				.center( this.getCenterComponent().getAwtComponent() )
				.build();
	}

	@Override
	protected org.lgna.croquet.views.AwtComponentView<?> getMainComponent() {
		return this.outerMainPanel;
	}

	private final org.lgna.croquet.views.BorderPanel outerMainPanel = new org.lgna.croquet.views.BorderPanel();
	private final org.lgna.croquet.views.BorderPanel typePanel = new org.lgna.croquet.views.BorderPanel();
	private final org.lgna.croquet.views.ScrollPane scrollPane = new org.lgna.croquet.views.ScrollPane();
}
