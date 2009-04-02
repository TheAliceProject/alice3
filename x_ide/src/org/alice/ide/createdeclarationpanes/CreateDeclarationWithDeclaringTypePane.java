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
public abstract class CreateDeclarationWithDeclaringTypePane<E> extends CreateDeclarationPane< E > {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType declaringType;
	public CreateDeclarationWithDeclaringTypePane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		this.declaringType = declaringType;
	}
	protected abstract String getDeclarationText();
	
	private java.awt.Component createDeclarationTextComponent() {
		zoot.ZLabel rv = new zoot.ZLabel( this.getDeclarationText() );
		rv.setFontToScaledFont( 1.2f );
		return rv;
	}
//	@Override
//	protected final java.awt.Component[] createDeclarationRow() {
//		class DeclarationComponent extends swing.LineAxisPane {
//			public DeclarationComponent( edu.cmu.cs.dennisc.alice.ast.AbstractType declaringType ) {
//				super(
//						javax.swing.Box.createHorizontalGlue(),
//						new zoot.ZLabel( "declare ", zoot.font.ZTextPosture.OBLIQUE ), 
//						CreateDeclarationWithDeclaringTypePane.this.createDeclarationTextComponent(), 
//						new zoot.ZLabel( " on class:", zoot.font.ZTextPosture.OBLIQUE ) 
//						//new org.alice.ide.common.TypeComponent( declaringType )
////						new zoot.ZLabel( ": " ) 
//				);
//				this.setAlignmentX( RIGHT_ALIGNMENT );
//			}
//		}
//		//return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( new DeclarationComponent( this.declaringType ), null );
//		return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( new DeclarationComponent( this.declaringType ), new swing.LineAxisPane( new org.alice.ide.common.TypeComponent( declaringType ) ) );
//	}
	

	@Override
	protected String getTitleDefault() {
		return "Declare " + this.getDeclarationText();
	}
}
