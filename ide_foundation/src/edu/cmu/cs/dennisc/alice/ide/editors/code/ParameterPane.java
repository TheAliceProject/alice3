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
package edu.cmu.cs.dennisc.alice.ide.editors.code;

/**
 * @author Dennis Cosgrove
 */
public class ParameterPane extends AccessiblePane {
	private edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter;
	public ParameterPane( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter ) {
		this.parameter = parameter;
		this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.NodeNameLabel( this.parameter ) );
		this.setBackground( edu.cmu.cs.dennisc.alice.ide.IDE.getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.ParameterAccess.class ) );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return parameter.getValueType();
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ide.editors.common.DropAndDropEvent e ) {
		return new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( this.parameter );
	}
}
