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
public class DeclareMethodParameterOperation extends DeclareParameterOperation {
	public DeclareMethodParameterOperation( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		super( method );
	}
	public void perform( zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)this.getCode();
		java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > methodInvocations = this.getIDE().getMethodInvocations( method );
		org.alice.ide.createdeclarationpanes.CreateMethodParameterPane createMethodParameterPane = new org.alice.ide.createdeclarationpanes.CreateMethodParameterPane( method, methodInvocations );
		edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter = createMethodParameterPane.showInJDialog( getIDE() );
		if( parameter != null ) {
			this.getCode().getParamtersProperty().add( parameter );
			for( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation : methodInvocations ) {
				methodInvocation.arguments.add( new edu.cmu.cs.dennisc.alice.ast.Argument( parameter, new edu.cmu.cs.dennisc.alice.ast.NullLiteral() ) );
			}
			actionContext.put( org.alice.ide.IDE.IS_PROJECT_CHANGED_KEY, true );
			actionContext.commit();
		} else {
			actionContext.cancel();
		}
	}
	
//	def perform( self ):
//		self._index = self._code.parameters.size()
//		self._invocations = self.getIDE().getInvocations( self._code )
//		self.redo()
//	def undo( self ):
//		self._code.parameters.remove( self._index )
//		for invocation in self._invocations:
//			invocation.arguments.remove( self._index )
//	def redo( self ):
//		self._code.parameters.add( [ self._param ] )
//		for invocation in self._invocations:
//			argument = alice.ast.Argument( self._param, alice.ast.NullLiteral() )
//			invocation.arguments.add( [ argument ] )
	
}
