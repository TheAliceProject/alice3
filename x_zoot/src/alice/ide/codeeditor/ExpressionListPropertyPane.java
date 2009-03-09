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
package alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
class ExpressionListPropertyPane extends AbstractListPropertyPane< edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty > {
	public ExpressionListPropertyPane( edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty property ) {
		super( javax.swing.BoxLayout.LINE_AXIS, property );
	}
	@Override
	protected java.awt.Component createInterstitial( int i, final int N ) {
		if( i < N - 1 ) {
			return new edu.cmu.cs.dennisc.moot.ZLabel( ", " );
		} else {
			return null;
		}
	}
	@Override
	protected javax.swing.JComponent createComponent( Object instance ) {
		edu.cmu.cs.dennisc.alice.ast.Expression expression = (edu.cmu.cs.dennisc.alice.ast.Expression)instance;
		return new ExpressionPane( expression );
	}
}
