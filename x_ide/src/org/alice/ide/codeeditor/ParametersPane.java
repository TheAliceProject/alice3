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

import edu.cmu.cs.dennisc.alice.ast.NodeListProperty;
import edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
public class ParametersPane extends org.alice.ide.common.AbstractListPropertyPane< NodeListProperty< ParameterDeclaredInAlice >> {
	public ParametersPane( org.alice.ide.common.Factory factory, edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code ) {
		super( factory, javax.swing.BoxLayout.LINE_AXIS, code.getParamtersProperty() );
	}
	
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}

	private edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice getCode() {
		return (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)getProperty().getOwner();
	}

	@Override
	protected java.awt.Component createComponent( Object parameter ) {
		return new TypedParameterPane( getProperty(), (edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice)parameter );
	}
	@Override
	protected void addPrefixComponents() {
		//super.addPrefixComponents();
		if( getIDE().isJava() ) {
			this.add( edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "( " ) );
		} else {
			int n = this.getProperty().size();
			String text;
			switch( n ) {
			case 0:
				text = " ";
				break;
			case 1:
				text = " with parameter: ";
				break;
			default:
				text = " with parameters: ";
			}
			edu.cmu.cs.dennisc.zoot.ZLabel label = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( text );
			label.setFontToDerivedFont( edu.cmu.cs.dennisc.zoot.font.ZTextPosture.OBLIQUE );
			this.add( label );
			this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
		}
	}
	@Override
	protected java.awt.Component createInterstitial( int i, int N ) {
		if( i<N-1 ) {
			return edu.cmu.cs.dennisc.zoot.ZLabel.acquire( ", " );
		} else {
			return javax.swing.Box.createHorizontalStrut( 8 );
		}
	}
	@Override
	protected void addPostfixComponents() {
		super.addPostfixComponents();
		edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code = getCode();

		if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
			if( method.isSignatureLocked.getValue() ) {
				//pass
			} else {
				this.add( new edu.cmu.cs.dennisc.zoot.ZButton( new org.alice.ide.operations.ast.DeclareMethodParameterOperation( method ) ) );
			}
		}
		if( getIDE().isJava() ) {
			this.add( edu.cmu.cs.dennisc.zoot.ZLabel.acquire( " )" ) );
		}
	}
}
