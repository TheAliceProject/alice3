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
public abstract class AbstractShiftCodeParameterOperation extends AbstractCodeParameterOperation {
	public AbstractShiftCodeParameterOperation( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code, edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter ) {
		super( code, parameter );
	}
//	def prepare( self, e, observer ):
//		return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
//	def perform( self ):
//		self._index = self._code.parameters.indexOf( self._paramteter )
//		self._invocations = self.getIDE().getInvocations( self._code )
//		self.redo()
//
//	def _shift( self, aIndex ):
//		bIndex = aIndex + 1
//		aParam = self._code.parameters.get( aIndex )
//		bParam = self._code.parameters.get( bIndex )
//		self._code.parameters.set( aIndex, [bParam, aParam] )
//		for invocation in self._invocations:
//			aArg = invocation.arguments.get( aIndex )
//			bArg = invocation.arguments.get( bIndex )
//			assert aArg.parameter.getValue() == aParam 
//			assert bArg.parameter.getValue() == bParam 
//			invocation.arguments.set( aIndex, [bArg, aArg] )
}
