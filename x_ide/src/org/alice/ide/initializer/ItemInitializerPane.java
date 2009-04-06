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
package org.alice.ide.initializer;

/**
 * @author Dennis Cosgrove
 */
abstract class ItemInitializerPane extends AbstractInitializerPane {
	private BogusNode bogusNode = new BogusNode();

	public ItemInitializerPane() {
		//this.setBackground( java.awt.Color.GREEN );
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
		org.alice.ide.common.ExpressionPropertyPane expressionPropertyPane = new org.alice.ide.common.ExpressionPropertyPane( getIDE().getCodeFactory(), this.bogusNode.expression, true );
		this.add( expressionPropertyPane );
		this.bogusNode.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				ItemInitializerPane.this.handleInitializerChange();
			}
		} );
	}
	@Override
	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.bogusNode.handleComponentTypeChange( type );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return this.bogusNode.createCopyOfExpressionValue();
	}
	public void setInitializer( edu.cmu.cs.dennisc.alice.ast.Expression initializer ) {
		this.bogusNode.expression.setValue( initializer );
	}
}
