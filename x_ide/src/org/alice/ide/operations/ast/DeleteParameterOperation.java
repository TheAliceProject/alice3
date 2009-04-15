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

import edu.cmu.cs.dennisc.alice.ast.NodeListProperty;
import edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
public class DeleteParameterOperation extends AbstractCodeParameterOperation {
	public DeleteParameterOperation( NodeListProperty< ParameterDeclaredInAlice > parametersProperty, edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter ) {
		super( parametersProperty, parameter );
		this.putValue( javax.swing.Action.NAME, "Delete" );
	}
	public void perform( zoot.ActionContext actionContext ) {
		javax.swing.JOptionPane.showMessageDialog( getIDE(), "todo" );
		actionContext.cancel();
	}
//	def prepare( self, e, observer ):
//		invocations = self.getIDE().getInvocations( self._code )
//		if len( invocations ):
//			if self._code.isProcedure():
//				methodText = "procedure"
//			else:
//				methodText = "function"
//			message = "There are invocations to this %s.\nDeleting this parameter will also delete the arguments from those invocations.\nWould you like to continue with the deletion?" % methodText
//			result = javax.swing.JOptionPane.showConfirmDialog(self.getIDE(), message, "Delete Parameter", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION )
//			if result == javax.swing.JOptionPane.YES_OPTION:
//				pass
//			else:
//				return alice.ide.Operation.PreparationResult.CANCEL
//		return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
//
//	def perform( self ):
//		self._index = self._code.parameters.indexOf( self._paramteter )
//		invocations = self.getIDE().getInvocations( self._code )
//		self._map = {}
//		for invocation in invocations:
//			self._map[ invocation ] = invocation.arguments.get( self._index )
//		self.redo()
//	def redo( self ):
//		self._code.parameters.remove( self._index )
//		for invocation in self._map.keys():
//			invocation.arguments.remove( self._index )
//	def undo( self ):
//		self._code.parameters.add( self._index, [ self._paramteter ] )
//		for invocation, argument in self._map.items():
//			invocation.arguments.add( self._index, [ argument ] )
}
