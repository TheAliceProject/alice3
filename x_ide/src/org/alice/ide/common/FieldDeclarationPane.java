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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class FieldDeclarationPane extends swing.LineAxisPane {
	public FieldDeclarationPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		String text;
		if( field.isFinal() ) {
			text = "permanently set ";
		} else {
			text = "initialize ";
		}
		this.add( zoot.ZLabel.acquire( text ) );
		this.add( new TypeComponent( field.getValueType() ) );
		org.alice.ide.common.DeclarationNameLabel nameLabel = new org.alice.ide.common.DeclarationNameLabel( field );
		nameLabel.setFontToScaledFont( 1.5f );
		this.add( nameLabel );
		this.add( new org.alice.ide.common.GetsPane( true ) );
		
		//todo
		//boolean isDropDownPotentiallyDesired = factory instanceof org.alice.ide.memberseditor.Factory;
		
		java.awt.Component component = new org.alice.ide.common.ExpressionPropertyPane( factory, field.initializer );
		if( factory instanceof org.alice.ide.memberseditor.Factory ) {
			component = new org.alice.ide.codeeditor.ExpressionPropertyDropDownPane(null, component, field.initializer );
		}
		this.add( component );
	}
}
