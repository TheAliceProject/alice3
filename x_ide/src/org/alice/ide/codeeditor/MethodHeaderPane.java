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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class MethodHeaderPane extends AbstractCodeHeaderPane {
	public MethodHeaderPane( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice, javax.swing.JComponent parametersPane ) {
		super( methodDeclaredInAlice );
		if( org.alice.ide.IDE.getSingleton().isJava() ) {
			this.add( new org.alice.ide.common.TypeComponent( methodDeclaredInAlice.getReturnType() ) );
			this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
			//this.add( new zoot.ZLabel( " {" ) );
		} else {
			this.add( new zoot.ZLabel( "declare ", zoot.font.ZTextPosture.OBLIQUE ) );
			StringBuffer sb = new StringBuffer();
			if( methodDeclaredInAlice.isProcedure() ) {
				sb.append( " procedure " );
			} else {
				this.add( new org.alice.ide.common.TypeComponent( methodDeclaredInAlice.getReturnType() ) );
				sb.append( " function " );
			}
			this.add( new zoot.ZLabel( sb.toString(), zoot.font.ZTextPosture.OBLIQUE ) );
		}
		zoot.ZLabel nameLabel = new org.alice.ide.common.NodeNameLabel( methodDeclaredInAlice );
		nameLabel.setFontToScaledFont( 2.0f );
		this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
		this.add( nameLabel );
		this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
		if( parametersPane != null ) {
			this.add( parametersPane );
		}
	}
}
