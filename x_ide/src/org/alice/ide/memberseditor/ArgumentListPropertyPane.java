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
package org.alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
public class ArgumentListPropertyPane extends org.alice.ide.common.AbstractArgumentListPropertyPane {
	public ArgumentListPropertyPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty property ) {
		super( factory, property );
	}
	@Override
	protected javax.swing.JComponent createComponent( Object instance ) {
		edu.cmu.cs.dennisc.croquet.LineAxisPane rv = new edu.cmu.cs.dennisc.croquet.LineAxisPane();
		rv.setOpaque( true );
		rv.setBackground( new java.awt.Color( 255, 255, 255, 127 ) );
		rv.setBorder( edu.cmu.cs.dennisc.swing.BorderFactory.createOutlinedBorder( 1, 4, 1, 4, java.awt.Color.LIGHT_GRAY ) );
		edu.cmu.cs.dennisc.alice.ast.Argument argument = (edu.cmu.cs.dennisc.alice.ast.Argument)instance;
		
		String parameterName = argument.parameter.getValue().getName();
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( parameterName );
		if( parameterName != null && parameterName.length() > 0 ) {
			rv.add( edu.cmu.cs.dennisc.zoot.ZLabel.acquire( parameterName + ": " ) );
		}
		rv.add( new org.alice.ide.common.EmptyExpressionPane( (org.alice.ide.ast.EmptyExpression)argument.expression.getValue() ) );
		return rv;
	}
}
