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

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.awt.FontUtilities;
import edu.cmu.cs.dennisc.java.awt.PrintHelper;
import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.pattern.HowMuch;
import org.alice.ide.ApiConfigurationManager;
import org.alice.ide.IDE;
import org.alice.ide.Theme;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.ast.declaration.views.TypeHeader;
import org.alice.ide.croquet.models.IdeDragModel;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingConstructors;
import org.alice.ide.declarationseditor.TypeComposite;
import org.alice.ide.declarationseditor.components.DeclarationView;
import org.lgna.croquet.DropReceptor;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.PageAxisPanel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.ToolPaletteView;
import org.lgna.project.ast.NamedUserType;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import java.awt.print.Printable;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class TypeDeclarationView extends DeclarationView {
	public TypeDeclarationView( TypeComposite composite ) {
		super( composite );
		NamedUserType type = (NamedUserType)composite.getDeclaration();
		Theme theme = ThemeUtilities.getActiveTheme();
		this.setBackgroundColor( theme.getMutedTypeColor() );

		ToolPaletteView constructorsToolPalette = composite.getConstructorsToolPaletteCoreComposite().getOuterComposite().getView();
		constructorsToolPalette.setBackgroundColor( theme.getConstructorColor() );

		ToolPaletteView proceduresToolPalette = composite.getProceduresToolPaletteCoreComposite().getOuterComposite().getView();
		proceduresToolPalette.setBackgroundColor( theme.getProcedureColor() );

		ToolPaletteView functionsToolPalette = composite.getFunctionsToolPaletteCoreComposite().getOuterComposite().getView();
		functionsToolPalette.setBackgroundColor( theme.getFunctionColor() );

		ToolPaletteView fieldsToolPalette = composite.getFieldsToolPaletteCoreComposite().getOuterComposite().getView();
		fieldsToolPalette.setBackgroundColor( theme.getFieldColor() );

		for( ToolPaletteView toolPalette : new ToolPaletteView[] { constructorsToolPalette, proceduresToolPalette, functionsToolPalette, fieldsToolPalette } ) {
			toolPalette.getTitle().changeFont( TextPosture.OBLIQUE );
			toolPalette.getTitle().scaleFont( 1.4f );
			toolPalette.getTitle().setRoundedOnTop( true );
			toolPalette.getCenterView().setBorder( BorderFactory.createEmptyBorder( 4, 14, 4, 4 ) );
		}

		PageAxisPanel membersPanel = new PageAxisPanel();
		if( IsIncludingConstructors.getInstance().getValue() ) {
			membersPanel.addComponent( constructorsToolPalette );
			membersPanel.addComponent( BoxUtilities.createVerticalSliver( 16 ) );
		}
		membersPanel.addComponent( proceduresToolPalette );
		membersPanel.addComponent( BoxUtilities.createVerticalSliver( 16 ) );
		membersPanel.addComponent( functionsToolPalette );
		membersPanel.addComponent( BoxUtilities.createVerticalSliver( 16 ) );
		membersPanel.addComponent( fieldsToolPalette );
		membersPanel.setBorder( BorderFactory.createEmptyBorder( 12, 24, 0, 0 ) );
		membersPanel.setBackgroundColor( this.getBackgroundColor() );

		outerMainPanel.setBackgroundColor( this.getBackgroundColor() );
		typePanel.setBackgroundColor( this.getBackgroundColor() );

		scrollPane.setBorder( null );
		scrollPane.setBackgroundColor( this.getBackgroundColor() );

		TypeHeader typeHeader = new TypeHeader( type );

		this.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );

		ApiConfigurationManager apiConfigurationManager = IDE.getActiveInstance().getApiConfigurationManager();
		if( apiConfigurationManager.isExportTypeDesiredFor( type ) ) {
			LineAxisPanel header = new LineAxisPanel(
					typeHeader,
					BoxUtilities.createHorizontalSliver( 8 ),
					composite.getImportOperation().createButton(),
					composite.getExportOperation().createButton()
					);
			this.typePanel.addPageStartComponent( header );
		} else {
			this.typePanel.addPageStartComponent( typeHeader );
		}
		this.typePanel.addCenterComponent( new BorderPanel.Builder().pageStart( membersPanel ).build() );

		for( JComponent component : ComponentUtilities.findAllMatches( typeHeader.getAwtComponent(), HowMuch.DESCENDANTS_ONLY, JComponent.class ) ) {
			FontUtilities.setFontToScaledFont( component, 1.2f );
		}
	}

	@Override
	public void addPotentialDropReceptors( List<DropReceptor> out, IdeDragModel dragModel ) {
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
	public Printable getPrintable() {
		return new PrintHelper.Builder( this.getInsets(), this.getBackgroundColor() )
				.pageStart( this.getPageStartComponent().getAwtComponent() )
				.center( this.getCenterComponent().getAwtComponent() )
				.build();
	}

	@Override
	protected AwtComponentView<?> getMainComponent() {
		return this.outerMainPanel;
	}

	private final BorderPanel outerMainPanel = new BorderPanel();
	private final BorderPanel typePanel = new BorderPanel();
	private final ScrollPane scrollPane = new ScrollPane();
}
