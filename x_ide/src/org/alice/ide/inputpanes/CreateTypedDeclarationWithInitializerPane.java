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
package org.alice.ide.inputpanes;

abstract class AbstractInitializerPane extends javax.swing.JPanel {
	public abstract edu.cmu.cs.dennisc.alice.ast.Expression getInitializer();
}
class ItemInitializerPane extends AbstractInitializerPane {
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return null;
	}
}
class ArrayInitializerPane extends AbstractInitializerPane {
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return null;
	}
}

class InitializerPane extends javax.swing.JPanel {
	private static final String ITEM_KEY = "ITEM_KEY";
	private static final String ARRAY_KEY = "ARRAY_KEY";
	private java.awt.CardLayout cardLayout = new java.awt.CardLayout();
	private ItemInitializerPane itemInitializerPane = new ItemInitializerPane();
	private ItemInitializerPane arrayInitializerPane = new ItemInitializerPane();
	public InitializerPane() {
		this.setLayout( this.cardLayout );
		this.add( this.itemInitializerPane, ITEM_KEY );
		this.add( this.arrayInitializerPane, ARRAY_KEY );
		this.cardLayout.show( this, ITEM_KEY );
	}
	private AbstractInitializerPane getCurrentCard() {
		if( this.itemInitializerPane.isVisible() ) {
			return this.itemInitializerPane;
		} else {
			return this.arrayInitializerPane;
		}
	}
	
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return getCurrentCard().getInitializer();
	}
}

class IsConstantStateOperation extends zoot.AbstractStateOperation< Boolean > {
	public IsConstantStateOperation() {
		this.putValue( javax.swing.Action.NAME, "is constant" );
	}
//	public Boolean getState() {
//		return null;
//	}
//	public void setState( Boolean state ) {
//	}
	public void performStateChange( zoot.StateContext stateContext ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( stateContext );
	}
}

class IsConstantCheckBox extends zoot.ZCheckBox {
	public IsConstantCheckBox() {
		super( new IsConstantStateOperation() );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class CreateTypedDeclarationWithInitializerPane<E> extends CreateTypedDeclarationPane<E> {
	private InitializerPane initializerPane;
	private IsConstantCheckBox isConstantCheckBox;
	@Override
	protected java.util.List< java.awt.Component[] > createComponentRows() {
		zoot.ZLabel label = new zoot.ZLabel( "initializer:" );
		this.initializerPane = new InitializerPane();
		this.isConstantCheckBox = new IsConstantCheckBox();
		java.util.List< java.awt.Component[] > rv = super.createComponentRows();
		rv.add( new java.awt.Component[] { label, this.initializerPane } );
		rv.add( new java.awt.Component[] { new javax.swing.JLabel(), this.isConstantCheckBox } );
		return rv;
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return this.initializerPane.getInitializer();
	}
	
	protected boolean isConstant() {
		return this.isConstantCheckBox.isSelected();
	}
	
}
