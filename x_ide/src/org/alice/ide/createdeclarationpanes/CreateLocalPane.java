/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.createdeclarationpanes;

/**
 * @author Dennis Cosgrove
 */
public class CreateLocalPane extends CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement> {
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement block;
	private TypePane typePane = new TypePane() {
		@Override
		protected void handleComponentTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
			CreateLocalPane.this.handleTypeChange();
		}
		@Override
		protected void handleIsArrayChange( boolean isArray ) {
			CreateLocalPane.this.handleTypeChange();
		}
	};
	private InitializerPane initializerPane = new InitializerPane() {
		@Override
		protected void handleInitializerChange() {
			CreateLocalPane.this.handleInitializerChange();
		}
	};
	private zoot.ZCheckBox isFinalCheckBox;
	
	private void handleTypeChange() {
		this.initializerPane.handleTypeChange( this.typePane.getValueType() );
		this.updateSizeIfNecessary();
		this.updateOKButton();
	}
	private void handleInitializerChange() {
		this.updateSizeIfNecessary();
		this.updateOKButton();
	}
	
	public CreateLocalPane( edu.cmu.cs.dennisc.alice.ast.BlockStatement block ) {
		assert block != null;
		this.block = block;
		this.setBackground( org.alice.ide.IDE.getLocalColor() );
	}
	
	@Override
	protected java.awt.Component createValueTypeComponent() {
		return this.typePane;
	}
	@Override
	protected java.awt.Component createInitializerComponent() {
		return this.initializerPane;
	}

	@Override
	protected java.awt.Component createIsFinalComponent() {
		this.isFinalCheckBox = new zoot.ZCheckBox( new IsFinalStateOperation( false ) );
		this.isFinalCheckBox.setOpaque( false );
		this.isFinalCheckBox.setEnabled( true );
		return this.isFinalCheckBox;
	}
	private java.awt.Component createDeclarationTextComponent() {
		zoot.ZLabel rv = new zoot.ZLabel( "Local" );
		rv.setFontToScaledFont( 1.2f );
		return rv;
	}
	private java.awt.Component createMethodTextComponent() {
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = this.block.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
		zoot.ZLabel rv = new zoot.ZLabel( method.getName() );
		rv.setFontToScaledFont( 1.2f );
		return rv;
	}
	@Override
	protected final java.awt.Component[] createDeclarationRow() {
		class DeclarationComponent extends swing.LineAxisPane {
			public DeclarationComponent() {
				super(
						javax.swing.Box.createHorizontalGlue(),
						new zoot.ZLabel( "declare ", zoot.font.ZTextPosture.OBLIQUE ), 
						CreateLocalPane.this.createDeclarationTextComponent(), 
						new zoot.ZLabel( " for method:", zoot.font.ZTextPosture.OBLIQUE ) 
				);
				this.setAlignmentX( RIGHT_ALIGNMENT );
			}
		}
		//return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( new DeclarationComponent( this.declaringType ), null );
		return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( new DeclarationComponent(), this.createMethodTextComponent() );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement getActualInputValue() {
		String name = this.getDeclarationName();
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.typePane.getValueType();
		boolean isFinal = this.isFinalCheckBox.isSelected();
		edu.cmu.cs.dennisc.alice.ast.Expression initializer = this.initializerPane.getInitializer();
		edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement rv;
		if( isFinal ) {
			rv = new edu.cmu.cs.dennisc.alice.ast.ConstantDeclarationStatement(
					new edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice(
							name,
							type
					),
					initializer
			);
		} else {
			rv = new edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement(
					new edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice(
							name,
							type
					),
					initializer
			);
		}
		return rv;
	}

	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		CreateLocalPane createLocalPane = new CreateLocalPane( null );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( createLocalPane.showInJDialog( ide ) );
		System.exit( 0 );
	}
}
