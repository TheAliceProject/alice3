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
public class ArgumentListPropertyPane extends org.alice.ide.common.AbstractArgumentListPropertyPane {
	public ArgumentListPropertyPane( org.alice.ide.common.Factory factory, edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty property ) {
		super( factory, property );
	}
	protected boolean isNameDesired( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter ) {
		boolean rv;
		if( parameter.getName() != null ) {
			if( parameter instanceof edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInJavaMethod ) {
				edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInJavaMethod parameterDeclaredInJavaMethod = (edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInJavaMethod)parameter;
				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava methodDeclaredInJava = parameterDeclaredInJavaMethod.getMethod();
				rv = methodDeclaredInJava.isParameterInShortestChainedMethod( parameterDeclaredInJavaMethod ) == false;
			} else {
				rv = true;
			}
		} else {
			rv = false;
		}
		return rv;
	}
	@Override
	protected java.awt.Component createComponent( Object instance ) {
		edu.cmu.cs.dennisc.alice.ast.Argument argument = (edu.cmu.cs.dennisc.alice.ast.Argument)instance;
		javax.swing.JComponent prefixPane;
		if( org.alice.ide.IDE.getSingleton().isJava() ) {
			prefixPane = null;
		} else {
			edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter = argument.parameter.getValue();
			boolean isNameDesired = this.isNameDesired( parameter );
			if( isNameDesired ) {
				prefixPane = new swing.LineAxisPane( javax.swing.Box.createHorizontalStrut( 4 ), new org.alice.ide.common.NodeNameLabel( argument.parameter.getValue() ), zoot.ZLabel.acquire( ": " ) );
			} else {
				prefixPane = null;
			}
		}
		return this.getFactory().createExpressionPropertyPane( argument.expression, prefixPane );
	}
}
