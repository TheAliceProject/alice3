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
package org.alice.ide.createdeclarationpanes;

class BogusNode extends edu.cmu.cs.dennisc.alice.ast.Node {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE;
	edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expression = new edu.cmu.cs.dennisc.alice.ast.ExpressionProperty( this ) {
		@Override
		public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
			return BogusNode.this.expressionType;
		}
	};
	
	public void handleComponentTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType ) {
		this.expressionType = expressionType;
		this.expression.setValue( new edu.cmu.cs.dennisc.alice.ast.NullLiteral() );
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression createCopyOfExpressionValue() {
		return (edu.cmu.cs.dennisc.alice.ast.Expression)org.alice.ide.IDE.getSingleton().createCopy( this.expression.getValue() );
	}
}

abstract class AbstractInitializerPane extends swing.Pane {
	public abstract edu.cmu.cs.dennisc.alice.ast.Expression getInitializer();
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
}


class ItemInitializerPane extends AbstractInitializerPane {
	private BogusNode bogusNode = new BogusNode();
	public ItemInitializerPane() {
		this.setBackground( java.awt.Color.GREEN );
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
		org.alice.ide.common.ExpressionPropertyPane expressionPropertyPane = new org.alice.ide.common.ExpressionPropertyPane( getIDE().getCodeFactory(), this.bogusNode.expression, true );
		this.add( expressionPropertyPane );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return this.bogusNode.createCopyOfExpressionValue();
	}
}
class ArrayInitializerPane extends AbstractInitializerPane {
	public ArrayInitializerPane() {
		this.setBackground( java.awt.Color.BLUE );
		this.setOpaque( true );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumHeight( super.getPreferredSize(), 240 );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return null;
	}
}

/**
 * @author Dennis Cosgrove
 */
class InitializerPane extends javax.swing.JPanel {
	private static final String ITEM_KEY = "ITEM_KEY";
	private static final String ARRAY_KEY = "ARRAY_KEY";
	private java.awt.CardLayout cardLayout = new java.awt.CardLayout();
	private ItemInitializerPane itemInitializerPane = new ItemInitializerPane();
	private ArrayInitializerPane arrayInitializerPane = new ArrayInitializerPane();
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
	public void handleIsArrayChange( boolean isArray ) {
		String key;
		if( isArray ) {
			key = ARRAY_KEY;
		} else {
			key = ITEM_KEY;
		}
		this.cardLayout.show( this, key );
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return getCurrentCard().getInitializer();
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		if( this.itemInitializerPane.isVisible() ) {
			return this.itemInitializerPane.getPreferredSize();
		} else {
			return this.arrayInitializerPane.getPreferredSize();
		}
	}
}

