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

/**
 * @author Dennis Cosgrove
 */
class BogusNode extends edu.cmu.cs.dennisc.alice.ast.Node {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType = null;
	edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expression = new edu.cmu.cs.dennisc.alice.ast.ExpressionProperty( this ) {
		@Override
		public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
			return BogusNode.this.expressionType;
		}
	};

	public BogusNode() {
		this.expression.setValue( new edu.cmu.cs.dennisc.alice.ast.NullLiteral() );
	}

	public void handleComponentTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType ) {
		this.expressionType = expressionType;
		edu.cmu.cs.dennisc.alice.ast.Expression expression;
		if( expressionType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE ) {
			expression = new edu.cmu.cs.dennisc.alice.ast.BooleanLiteral( false );
		} else if( expressionType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE ) {
			expression = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.0 );
		} else if( expressionType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) {
			expression = new edu.cmu.cs.dennisc.alice.ast.IntegerLiteral( 0 );
		} else {
			expression = new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
		}
		this.expression.setValue( expression );
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression createCopyOfExpressionValue() {
		edu.cmu.cs.dennisc.alice.ast.Expression src = this.expression.getValue();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: create copy" );
		return src;
		//		if( src != null ) {
		//			return (edu.cmu.cs.dennisc.alice.ast.Expression)org.alice.ide.IDE.getSingleton().createCopy( src );
		//		} else {
		//			return null;
		//		}
	}
}

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractInitializerPane extends swing.Pane {
	public abstract edu.cmu.cs.dennisc.alice.ast.Expression getInitializer();
	public abstract void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type );
	protected abstract void handleInitializerChange();
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
}

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

//
//class ButtonPane extends swing.GridBagPane {
//
//	public ButtonPane() {
//		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
//		gbc.fill = java.awt.GridBagConstraints.BOTH;
//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		this.add( this.addButton, gbc );
//		this.add( this.removeButton, gbc );
//		gbc.insets.top = 8;
//		this.add( this.moveUpButton, gbc );
//		gbc.insets.top = 0;
//		this.add( this.moveDownButton, gbc );
//		gbc.weighty = 1.0;
//		this.add( javax.swing.Box.createGlue(), gbc );
//	}
//}

class FauxItem extends javax.swing.AbstractButton {
	private ItemInitializerPane itemInitializerPane = new ItemInitializerPane() {
		@Override
		protected void handleInitializerChange() {
		}
	};

	public FauxItem( int index, edu.cmu.cs.dennisc.alice.ast.Expression initializer ) {
		this.itemInitializerPane.setInitializer( initializer );
		this.setModel( new javax.swing.JToggleButton.ToggleButtonModel() );
		this.setLayout( new java.awt.BorderLayout( 8, 0 ) );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
		this.add( new zoot.ZLabel( "[ " + index + " ] " ), java.awt.BorderLayout.WEST );
		this.add( new swing.LineAxisPane( this.itemInitializerPane, new javax.swing.JLabel() ), java.awt.BorderLayout.CENTER );
		//this.add( javax.swing.Box.createGlue() );
		this.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
				FauxItem.this.setSelected( !FauxItem.this.isSelected() );
				FauxItem.this.repaint();
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
			}
			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
		} );
		//todo
		//		this.setLeftButtonPressOperation( new org.alice.ide.operations.AbstractActionOperation() {
		//			public void perform( zoot.ActionContext actionContext ) {
		//				FauxItem.this.setSelected( !FauxItem.this.isSelected() );
		//			}
		//		} );
	}
	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.itemInitializerPane.handleTypeChange( type.getComponentType() );
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		//super.paintComponent( g );
		if( this.getModel().isSelected() ) {
			java.awt.Color color = new java.awt.Color( 191, 191, 255 );
			g.setColor( color );
			g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		}
	}

	//	@Override
	//	public java.awt.Color getBackground() {
	//		if( this.isSelected() ) {
	//			return java.awt.Color.BLUE;
	//		} else {
	//			return super.getBackground();
	//		}
	//	}
}

//class FauxList extends swing.GridBagPane {
//	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
//	public FauxList() {
//		buttonGroup.add( new FauxItem( 0 ) );
//		buttonGroup.add( new FauxItem( 1 ) );
//		buttonGroup.add( new FauxItem( 2 ) );
//		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
//		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
//		gbc.fill = java.awt.GridBagConstraints.BOTH;
//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		gbc.weightx = 1.0;
//		java.util.Enumeration< javax.swing.AbstractButton > e = this.buttonGroup.getElements();
//		while( e.hasMoreElements() ) {
//			javax.swing.AbstractButton button = e.nextElement();
//			this.add( button, gbc );
//		}
//		gbc.weighty = 1.0;
//		this.add( new javax.swing.JLabel(), gbc );
//	}
//	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
//		java.util.Enumeration< javax.swing.AbstractButton > e = this.buttonGroup.getElements();
//		while( e.hasMoreElements() ) {
//			javax.swing.AbstractButton button = e.nextElement();
//			if( button instanceof FauxItem ) {
//				FauxItem fauxItem = (FauxItem)button;
//				fauxItem.handleTypeChange( type );
//			}
//		}
//	}
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumSize( super.getPreferredSize(), 240, 180 );
//	}
//}

/**
 * @author Dennis Cosgrove
 */
abstract class ArrayInitializerPane extends AbstractInitializerPane {
	class AddItemOperation extends org.alice.ide.operations.AbstractActionOperation {
		public AddItemOperation() {
			this.putValue( javax.swing.Action.NAME, "add" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			ArrayInitializerPane.this.getDefaultComboBoxModel().addElement( ArrayInitializerPane.this.createDefaultInitializer() );
		}
	}

	class RemoveItemOperation extends org.alice.ide.operations.AbstractActionOperation {
		public RemoveItemOperation() {
			this.putValue( javax.swing.Action.NAME, "remove" );
		}
		public void perform( zoot.ActionContext actionContext ) {
		}
	}

	class MoveItemUpOperation extends org.alice.ide.operations.AbstractActionOperation {
		public MoveItemUpOperation() {
			this.putValue( javax.swing.Action.NAME, "move up" );
		}
		public void perform( zoot.ActionContext actionContext ) {
		}
	}

	class MoveItemDownOperation extends org.alice.ide.operations.AbstractActionOperation {
		public MoveItemDownOperation() {
			this.putValue( javax.swing.Action.NAME, "move down" );
		}
		public void perform( zoot.ActionContext actionContext ) {
		}
	}

	class ItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation< edu.cmu.cs.dennisc.alice.ast.Expression > {
		public ItemSelectionOperation() {
			super( new javax.swing.DefaultComboBoxModel() );
		}
		public void performSelectionChange( zoot.ItemSelectionContext< edu.cmu.cs.dennisc.alice.ast.Expression > context ) {
			ArrayInitializerPane.this.handleSelectionChange( context );
		}
	}

	private void updateButtons() {
		int index = this.list.getSelectedIndex();
		final int N = this.list.getModel().getSize();
		this.removeButton.setEnabled( index != -1 );
		this.moveUpButton.setEnabled( index > 0 );
		this.moveDownButton.setEnabled( index >= 0 && index < N-1 );
	}
	private void handleSelectionChange( zoot.ItemSelectionContext< edu.cmu.cs.dennisc.alice.ast.Expression > context ) {
		this.updateButtons();
	}
	private zoot.ZButton addButton = new zoot.ZButton( new AddItemOperation() );
	private zoot.ZButton removeButton = new zoot.ZButton( new RemoveItemOperation() );
	private zoot.ZButton moveUpButton = new zoot.ZButton( new MoveItemUpOperation() );
	private zoot.ZButton moveDownButton = new zoot.ZButton( new MoveItemDownOperation() );

	class ExpressionList extends zoot.ZList< edu.cmu.cs.dennisc.alice.ast.Expression > {
		public ExpressionList() {
			super( new ItemSelectionOperation() );
		}
		@Override
		public void updateUI() {
			setUI( new edu.cmu.cs.dennisc.swing.plaf.ListUI< edu.cmu.cs.dennisc.alice.ast.Expression >() {
				@Override
				protected javax.swing.AbstractButton createComponentFor( int index, edu.cmu.cs.dennisc.alice.ast.Expression e ) {
					return new FauxItem( index, e );
				}
			} );
		}
	}

	private ExpressionList list = new ExpressionList();
	private edu.cmu.cs.dennisc.alice.ast.AbstractType arrayType;

	private edu.cmu.cs.dennisc.alice.ast.Expression createDefaultInitializer() {
		if( this.arrayType != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType componentType = this.arrayType.getComponentType();
			if( componentType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE ) {
				return new edu.cmu.cs.dennisc.alice.ast.BooleanLiteral( false );
			} else if( componentType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE ) {
				return new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.0 );
			} else if( componentType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) {
				return new edu.cmu.cs.dennisc.alice.ast.IntegerLiteral( 0 );
			} else {
				return new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
			}
		} else {
			return null;
		}
	}

	private javax.swing.DefaultComboBoxModel getDefaultComboBoxModel() {
		return (javax.swing.DefaultComboBoxModel)this.list.getModel();
	}
	public ArrayInitializerPane() {
		this.setLayout( new java.awt.BorderLayout( 8, 0 ) );
		this.updateButtons();
		
		swing.GridBagPane buttonPane = new swing.GridBagPane();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		buttonPane.add( this.addButton, gbc );
		buttonPane.add( this.removeButton, gbc );
		gbc.insets.top = 8;
		buttonPane.add( this.moveUpButton, gbc );
		gbc.insets.top = 0;
		buttonPane.add( this.moveDownButton, gbc );
		gbc.weighty = 1.0;
		buttonPane.add( javax.swing.Box.createGlue(), gbc );

		this.add( buttonPane, java.awt.BorderLayout.EAST );

		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.list ) {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumSize( super.getPreferredSize(), 240, 180 );
			}
		};
		scrollPane.setBorder( null );
		this.add( scrollPane, java.awt.BorderLayout.CENTER );
	}
	@Override
	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType arrayType ) {
		this.arrayType = arrayType;
		//this.list.handleTypeChange( type );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleTypeChange", this.arrayType );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		javax.swing.ListModel model = this.list.getModel();
		final int N = model.getSize();
		edu.cmu.cs.dennisc.alice.ast.Expression[] expressions = new edu.cmu.cs.dennisc.alice.ast.Expression[ N ];
		for( int i=0; i<N; i++ ) {
			expressions[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)model.getElementAt( i );
		}
		return new edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation( this.arrayType, new Integer[] { expressions.length }, expressions );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class InitializerPane extends swing.CardPane {
	private static final String ITEM_KEY = "ITEM_KEY";
	private static final String ARRAY_KEY = "ARRAY_KEY";
	private ItemInitializerPane itemInitializerPane = new ItemInitializerPane() {
		@Override
		protected void handleInitializerChange() {
			InitializerPane.this.handleInitializerChange();
		}
	};
	private ArrayInitializerPane arrayInitializerPane = new ArrayInitializerPane() {
		@Override
		protected void handleInitializerChange() {
			InitializerPane.this.handleInitializerChange();
		}
	};

	public InitializerPane() {
		this.add( this.itemInitializerPane, ITEM_KEY );
		this.add( this.arrayInitializerPane, ARRAY_KEY );
		this.show( ITEM_KEY );
	}
	private AbstractInitializerPane getCurrentCard() {
		if( this.itemInitializerPane.isVisible() ) {
			return this.itemInitializerPane;
		} else {
			return this.arrayInitializerPane;
		}
	}
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
			this.getCurrentCard().handleTypeChange( type );
		}
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
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
	protected abstract void handleInitializerChange();
}
