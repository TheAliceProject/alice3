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
package org.alice.ide.initializer;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionUtilities {
	public static edu.cmu.cs.dennisc.alice.ast.Expression getNextExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression previousExpression ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( type );
		//todo: check to see if acceptable
		edu.cmu.cs.dennisc.alice.ast.Expression rv;
		if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE ) {
			rv = new edu.cmu.cs.dennisc.alice.ast.BooleanLiteral( false );
		} else if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE ) {
			rv = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.0 );
		} else if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) {
			rv = new edu.cmu.cs.dennisc.alice.ast.IntegerLiteral( 0 );
		} else {
			rv = new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( rv );
		return rv;
	}
	public static edu.cmu.cs.dennisc.alice.ast.Expression createDefaultExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return getNextExpression( type, null );
	}
}
