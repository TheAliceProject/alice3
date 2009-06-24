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
package org.alice.ide.cascade.customfillin;

import org.alice.ide.choosers.ValueChooser;

/**
 * @author Dennis Cosgrove
 */
public class CustomInputPane<E extends edu.cmu.cs.dennisc.alice.ast.Expression, F> extends org.alice.ide.preview.PreviewInputPane< E > {
	private CustomFillIn< E, F > fillIn;
	private ValueChooser< F > chooser;
	public CustomInputPane( CustomFillIn< E, F > fillIn, ValueChooser< F > chooser ) {
		this.fillIn = fillIn;
		this.chooser = chooser;
		this.chooser.setInputPane( this );
	}
	
	@Override
	protected String getTitleDefault() {
		return this.chooser.getTitleDefault();
	}
	
	@Override
	protected java.awt.Component createPreviewSubComponent() {
		edu.cmu.cs.dennisc.alice.ast.Expression expression;
		try {
			expression = this.getActualInputValue();
		} catch( RuntimeException re ) {
			expression = new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
		}
		return getIDE().getPreviewFactory().createExpressionPane( expression );
	}
	@Override
	protected java.util.List< java.awt.Component[] > updateRows( java.util.List< java.awt.Component[] > rv ) {
		String[] labelTexts = this.chooser.getLabelTexts();
		java.awt.Component[] components = this.chooser.getComponents();
		final int N = labelTexts.length;
		for( int i=0; i<N; i++ ) {
			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( labelTexts[ i ] ), components[ i ] ) );
		}
		return rv;
	}
	@Override
	protected E getActualInputValue() {
		F value = this.chooser.getValue();
		return this.fillIn.createExpression( value );
	}
}
