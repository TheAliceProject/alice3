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
public abstract class AbstractCodeParameterOperation extends AbstractCodeOperation {
	private ParameterDeclaredInAlice parameter;
	private NodeListProperty< ParameterDeclaredInAlice > parametersProperty;
	private static edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice getCode( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter ) {
		return (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)parameter.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.AbstractCode.class );
	}
	public AbstractCodeParameterOperation( NodeListProperty< ParameterDeclaredInAlice > parametersProperty, ParameterDeclaredInAlice parameter ) {
		super( getCode( parameter ) );
		this.parametersProperty = parametersProperty;
		this.parameter = parameter;
	}
	protected edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice getParameter() {
		return this.parameter;
	}
	protected int getIndex() {
		return this.parametersProperty.indexOf( this.parameter );
	}
	protected int getParameterCount() {
		return this.parametersProperty.size();
	}
}
