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
package org.alice.stageide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public class CustomAnglePane extends org.alice.ide.cascade.customfillin.CustomPane< org.alice.apis.moveandturn.Angle > {
	public CustomAnglePane() {
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		//todo: handle other numbers
		if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.DoubleLiteral ) {
			edu.cmu.cs.dennisc.alice.ast.DoubleLiteral doubleLiteral = (edu.cmu.cs.dennisc.alice.ast.DoubleLiteral)previousExpression;
			this.setAndSelectText( Double.toString( doubleLiteral.value.getValue() ) );
		}
	}
	@Override
	protected org.alice.apis.moveandturn.Angle valueOf( String text ) {
		double value = Double.valueOf( text );
		return new org.alice.apis.moveandturn.AngleInRevolutions( value );
	}
}
