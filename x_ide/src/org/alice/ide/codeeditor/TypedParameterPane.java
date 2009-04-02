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
class TypedParameterPane extends org.alice.ide.common.TypedDeclarationPane {
	private edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter;
	public TypedParameterPane( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter ) {
		super( new java.awt.Component[] { new org.alice.ide.common.TypeComponent( parameter.getValueType() ), new org.alice.ide.common.ParameterPane( parameter ) } );
		this.parameter = parameter;
	}
	@Override
	protected java.util.List< zoot.Operation > getPopupOperations() {
//		N = self._code.parameters.size()
//		index = self._code.parameters.indexOf( self._parameter )
//		operations = []
//		operations.append( ecc.dennisc.alice.ide.operations.ast.RenameParameterOperation( self._parameter, self._code ) )
//		if index > 0:
//			operations.append( ShiftForwardParameterOperation( self._parameter, self._code ) )
//		if index < N-1:
//			operations.append( ShiftBackwardParameterOperation( self._parameter, self._code ) )
//		operations.append( DeleteParameterOperation( self._parameter, self._code ) )
		java.util.LinkedList< zoot.Operation > rv = new java.util.LinkedList< zoot.Operation >();
		//rv.add( new org.alice.ide.operations.ast.RenameParameterOperation( this.parameter ) );
		return rv;
	}
}
