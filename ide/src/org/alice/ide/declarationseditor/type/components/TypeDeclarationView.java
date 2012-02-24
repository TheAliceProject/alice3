/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

package org.alice.ide.declarationseditor.type.components;

/**
 * @author Dennis Cosgrove
 */
public class TypeDeclarationView extends org.alice.ide.declarationseditor.components.DeclarationView {
	public TypeDeclarationView( org.alice.ide.declarationseditor.DeclarationComposite composite ) {
		super( composite );
		org.lgna.project.ast.NamedUserType type = (org.lgna.project.ast.NamedUserType)composite.getDeclaration();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		this.setBackgroundColor( ide.getTheme().getTypeColor() );

		org.lgna.croquet.components.ToolPalette constructorsToolPalette = org.alice.ide.declarationseditor.type.ConstructorsOpenState.getInstance( type ).createToolPalette( new ConstructorList( type ) );
		constructorsToolPalette.setBackgroundColor( ide.getTheme().getConstructorColor() );

		org.lgna.croquet.components.ToolPalette proceduresToolPalette = org.alice.ide.declarationseditor.type.ProceduresOpenState.getInstance( type ).createToolPalette( new ProcedureList( type ) );
		proceduresToolPalette.setBackgroundColor( ide.getTheme().getProcedureColor() );

		org.lgna.croquet.components.ToolPalette functionsToolPalette = org.alice.ide.declarationseditor.type.FunctionsOpenState.getInstance( type ).createToolPalette( new FunctionList( type ) );
		functionsToolPalette.setBackgroundColor( ide.getTheme().getFunctionColor() );

		org.lgna.croquet.components.JComponent< ? > fieldPanel;
		if( ide.getApiConfigurationManager().isDeclaringTypeForManagedFields( type ) ) {
			fieldPanel = new org.lgna.croquet.components.PageAxisPanel(
					new org.lgna.croquet.components.Label( "managed", 1.2f ),
					org.lgna.croquet.components.BoxUtilities.createVerticalStrut( 4 ),
					new ManagedFieldList( type ),
					org.lgna.croquet.components.BoxUtilities.createVerticalStrut( 24 ),
					new org.lgna.croquet.components.Label( "unmanaged", 1.2f ),
					org.lgna.croquet.components.BoxUtilities.createVerticalStrut( 4 ),
					new UnmanagedFieldList( type )
			);
		} else {
			fieldPanel = new UnmanagedFieldList( type );
		}
		org.lgna.croquet.components.ToolPalette fieldsToolPalette = org.alice.ide.declarationseditor.type.FieldsOpenState.getInstance( type ).createToolPalette( fieldPanel );
		fieldsToolPalette.setBackgroundColor( ide.getTheme().getFieldColor() );

		for( org.lgna.croquet.components.ToolPalette toolPalette : new org.lgna.croquet.components.ToolPalette[] { constructorsToolPalette, proceduresToolPalette, functionsToolPalette, fieldsToolPalette } ) {
			toolPalette.getTitle().changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
			toolPalette.getTitle().scaleFont( 1.5f );
			toolPalette.getMainComponent().setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 14, 4, 4 ) );
		}
		
		org.lgna.croquet.components.PageAxisPanel pageAxisPanel = new org.lgna.croquet.components.PageAxisPanel();
		pageAxisPanel.addComponent( constructorsToolPalette );
		pageAxisPanel.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 16 ) );
		pageAxisPanel.addComponent( proceduresToolPalette );
		pageAxisPanel.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 16 ) );
		pageAxisPanel.addComponent( functionsToolPalette );
		pageAxisPanel.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 16 ) );
		pageAxisPanel.addComponent( fieldsToolPalette );
		pageAxisPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,14,0,0 ) );
		pageAxisPanel.setBackgroundColor( this.getBackgroundColor() );

		org.lgna.croquet.components.BorderPanel borderPanel = new org.lgna.croquet.components.BorderPanel();
		borderPanel.addComponent( pageAxisPanel, Constraint.PAGE_START );
		borderPanel.setBackgroundColor( this.getBackgroundColor() );
		
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( borderPanel );
		scrollPane.setBorder( null );
        scrollPane.setBothScrollBarIncrements( 12, 24 );
		scrollPane.setBackgroundColor( this.getBackgroundColor() );
		
		org.alice.ide.ast.declaration.components.TypeHeader typeHeader = new org.alice.ide.ast.declaration.components.TypeHeader( type );
		
		org.lgna.croquet.components.LineAxisPanel header = new org.lgna.croquet.components.LineAxisPanel( 
				typeHeader,
				org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 8 ),
				org.alice.ide.croquet.models.ast.ExportTypeOperation.getInstance( type ).createButton()
		);
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
		this.addComponent( header, Constraint.PAGE_START );
		this.addComponent( scrollPane, Constraint.CENTER );

		for( javax.swing.JComponent component : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( typeHeader.getAwtComponent(), edu.cmu.cs.dennisc.pattern.HowMuch.DESCENDANTS_ONLY, javax.swing.JComponent.class ) ) {
			edu.cmu.cs.dennisc.java.awt.FontUtilities.setFontToScaledFont( component, 1.2f );
		}
	}
	@Override
	public void addPotentialDropReceptors( java.util.List< org.lgna.croquet.DropReceptor > out, org.alice.ide.croquet.models.IdeDragModel dragModel ) {
	}
	@Override
	public boolean isPrintSupported() {
		return false;
	}
	public int print( java.awt.Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex ) throws java.awt.print.PrinterException {
		throw new RuntimeException( "todo" );
	}
}
