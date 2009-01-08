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
class ArgumentListPropertyPane extends AbstractListPropertyPane< edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty > {
	public ArgumentListPropertyPane( edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty property ) {
		super( javax.swing.BoxLayout.LINE_AXIS, property );
	}
	@Override
	protected java.awt.Component createInterstitial( int i, final int N ) {
		if( i < N - 1 ) {
			return new edu.cmu.cs.dennisc.zoot.ZLabel( ", " );
		} else {
			return null;
		}
	}
	@Override
	protected javax.swing.JComponent createComponent( Object instance ) {
		edu.cmu.cs.dennisc.alice.ast.Argument argument = (edu.cmu.cs.dennisc.alice.ast.Argument)instance;
		javax.swing.JComponent prefixPane;
		if( "java".equals( edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().getLocale().getVariant() ) ) {
			prefixPane = null;
		} else {
			prefixPane = new edu.cmu.cs.dennisc.alice.ide.editors.common.NodeNameLabel( argument.parameter.getValue() );
		}
		return new ExpressionPropertyPane( argument.expression, true, prefixPane );
	}
}
