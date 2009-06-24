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
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class DeleteMethodOperation extends AbstractDeleteNodeOperation< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
	public DeleteMethodOperation( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		super( method, ((edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)method.getDeclaringType()).methods );
	}
	@Override
	protected boolean isClearToDelete( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > references = this.getIDE().getMethodInvocations( method );
		final int N = references.size();
		if( N > 0 ) {
			StringBuffer sb = new StringBuffer();
			sb.append( "Unable to delete " );
			if( method.isProcedure() ) {
				sb.append( "procedure" );
			} else {
				sb.append( "function" );
			}
			sb.append( " named \"" );
			sb.append( method.name.getValue() );
			sb.append( "\" because it has " );
			if( N == 1 ) {
				sb.append( "an invocation reference" );
			} else {
				sb.append( N );
			}
			sb.append( " invocation references" );
			sb.append( " to it.\nYou must remove " );
			if( N == 1 ) {
				sb.append( "this reference" );
			} else {
				sb.append( "these references" );
			}
			sb.append( " if you want to delete \"" );
			sb.append( method.name.getValue() );
			sb.append( "\" ." );

			javax.swing.JOptionPane.showMessageDialog( this.getIDE(), sb.toString() );
			return false;
		} else {
			return true;
		}
	}
}
