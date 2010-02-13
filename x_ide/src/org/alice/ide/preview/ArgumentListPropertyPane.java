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
package org.alice.ide.preview;

/**
 * @author Dennis Cosgrove
 */
public class ArgumentListPropertyPane extends org.alice.ide.common.AbstractArgumentListPropertyPane {
	public ArgumentListPropertyPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty property ) {
		super( factory, property );
	}
	@Override
	protected java.awt.Component createComponent( Object instance ) {
		edu.cmu.cs.dennisc.alice.ast.Argument argument = (edu.cmu.cs.dennisc.alice.ast.Argument)instance;
		String parameterName = argument.parameter.getValue().getName();
		java.awt.Component expressionComponent = this.getFactory().createExpressionPane( argument.expression.getValue() );
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( parameterName );
		if( parameterName != null && parameterName.length() > 0 ) {
			edu.cmu.cs.dennisc.croquet.swing.LineAxisPane rv = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane();
			rv.setOpaque( false );
			rv.add( edu.cmu.cs.dennisc.zoot.ZLabel.acquire( parameterName + ": " ) );
			rv.add( expressionComponent );
			return rv;
		} else {
			return expressionComponent;
		}
	}
}
