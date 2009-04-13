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
	
	@Override
	protected boolean isEditableValueTypeComponentDesired() {
		return true;
	}
	@Override
	protected boolean isIsReassignableComponentDesired() {
		return true;
	}
	
	
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement block;
	private zoot.ZLabel declarationTextLabel = this.createDeclarationTextLabel();
		
	public CreateLocalPane( edu.cmu.cs.dennisc.alice.ast.BlockStatement block ) {
		super( new org.alice.ide.namevalidators.LocalNameValidator( block ) );
		assert block != null;
		this.block = block;
		this.setBackground( org.alice.ide.IDE.getLocalColor() );
	}

	@Override
	protected java.awt.Component createPreviewSubComponent() {
		edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement localDeclarationStatement = this.getActualInputValue();
		org.alice.ide.common.AbstractStatementPane pane = org.alice.ide.IDE.getSingleton().getPreviewFactory().createStatementPane( localDeclarationStatement );
//		pane.setLeftButtonPressOperation( null );
//		pane.setDragAndDropOperation( null );
		return pane;
	}
	
	@Override
	protected boolean isEditableInitializerComponentDesired() {
		return true;
	}

	private zoot.ZLabel createDeclarationTextLabel() {
		zoot.ZLabel rv = new zoot.ZLabel( "Local" );
		rv.setFontToScaledFont( 1.2f );
		return rv;
	}
	private java.awt.Component createMethodTextComponent() {
		String name;
		if( this.block != null ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = this.block.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
			name = method.getName();
		} else {
			name = "unknown";
		}
		zoot.ZLabel rv = new zoot.ZLabel( name );
		rv.setFontToScaledFont( 1.2f );
		return rv;
	}
//	@Override
//	protected final java.awt.Component[] createDeclarationRow() {
//		class DeclarationComponent extends swing.LineAxisPane {
//			public DeclarationComponent() {
//				super(
//						javax.swing.Box.createHorizontalGlue(),
//						new zoot.ZLabel( "declare ", zoot.font.ZTextPosture.OBLIQUE ), 
//						CreateLocalPane.this.declarationTextLabel, 
//						new zoot.ZLabel( " for method:", zoot.font.ZTextPosture.OBLIQUE ) 
//				);
//				this.setAlignmentX( RIGHT_ALIGNMENT );
//			}
//		}
//		//return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( new DeclarationComponent( this.declaringType ), null );
//		return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( new DeclarationComponent(), this.createMethodTextComponent() );
//	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement getActualInputValue() {
		String name = this.getDeclarationName();
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getValueType();
		boolean isFinal = this.isReassignable() == false;
		edu.cmu.cs.dennisc.alice.ast.Expression initializer = this.getInitializer();
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
}
