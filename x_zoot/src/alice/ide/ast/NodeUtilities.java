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
package alice.ide.ast;

/**
 * @author Dennis Cosgrove
 */
public class NodeUtilities {
	public static edu.cmu.cs.dennisc.alice.ast.DoInOrder createDoInOrder() {
		return new edu.cmu.cs.dennisc.alice.ast.DoInOrder( new edu.cmu.cs.dennisc.alice.ast.BlockStatement() );
	}
	public static edu.cmu.cs.dennisc.alice.ast.DoTogether createDoTogether() {
		return new edu.cmu.cs.dennisc.alice.ast.DoTogether( new edu.cmu.cs.dennisc.alice.ast.BlockStatement() );
	}
	public static edu.cmu.cs.dennisc.alice.ast.Comment createComment() {
		return new edu.cmu.cs.dennisc.alice.ast.Comment();
	}
	
	public static edu.cmu.cs.dennisc.alice.ast.CountLoop createCountLoop( edu.cmu.cs.dennisc.alice.ast.Expression count ) {
		return new edu.cmu.cs.dennisc.alice.ast.CountLoop(
				new edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice( null, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ),
				new edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice( null, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ),
				count, 
				new edu.cmu.cs.dennisc.alice.ast.BlockStatement() 
		);
	}
	public static edu.cmu.cs.dennisc.alice.ast.CountLoop createIncompleteCountLoop() {
		return createCountLoop( new alice.ide.ast.EmptyExpression( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) );
	}
	public static edu.cmu.cs.dennisc.alice.ast.WhileLoop createWhileLoop( edu.cmu.cs.dennisc.alice.ast.Expression conditional ) {
		return new edu.cmu.cs.dennisc.alice.ast.WhileLoop(
				conditional, 
				new edu.cmu.cs.dennisc.alice.ast.BlockStatement() 
		);
	}
	public static edu.cmu.cs.dennisc.alice.ast.WhileLoop createIncompleteWhileLoop() {
		return createWhileLoop( new alice.ide.ast.EmptyExpression( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE ) );
	}
	
	public static alice.ide.codeeditor.AbstractStatementPane createPane( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		return alice.ide.codeeditor.AbstractStatementPane.createPane( statement, null );
	}
}
