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
public class InitializerPane extends swing.CardPane {
	private static final String ITEM_KEY = "ITEM_KEY";
	private static final String ARRAY_KEY = "ARRAY_KEY";
	private BogusNode bogusNode;
	private ItemInitializerPane itemInitializerPane;
	private ArrayInitializerPane arrayInitializerPane;
	public InitializerPane( BogusNode bogusNode ) {
		this.itemInitializerPane = new ItemInitializerPane( bogusNode.componentExpression );
		this.arrayInitializerPane = new ArrayInitializerPane( bogusNode.arrayExpressions );
		this.add( this.itemInitializerPane, ITEM_KEY );
		this.add( this.arrayInitializerPane, ARRAY_KEY );
		this.show( ITEM_KEY );
	}
//	private java.awt.Component getCurrentCard() {
//		if( this.itemInitializerPane.isVisible() ) {
//			return this.itemInitializerPane;
//		} else {
//			return this.arrayInitializerPane;
//		}
//	}
	private void handleIsArrayChange( boolean isArray ) {
		String key;
		if( isArray ) {
			key = ARRAY_KEY;
		} else {
			key = ITEM_KEY;
		}
		this.show( key );
	}

	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		if( type != null ) {
			this.handleIsArrayChange( type.isArray() );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handleTypeChange" );
			//this.getCurrentCard().handleTypeChange( type );
		}
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		if( this.itemInitializerPane.isVisible() ) {
			return this.bogusNode.componentExpression.getValue();
		} else {
			
			return this.bogusNode.arrayExpressions.getValue();
		}
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		if( this.itemInitializerPane.isVisible() ) {
			return this.itemInitializerPane.getPreferredSize();
		} else {
			return this.arrayInitializerPane.getPreferredSize();
		}
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
}
