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
public abstract class ArrayInitializerPane extends AbstractInitializerPane {
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
			int index = ArrayInitializerPane.this.list.getSelectedIndex();
			if( index >= 0 ) {
				ArrayInitializerPane.this.getDefaultComboBoxModel().removeElementAt( index );
			}
		}
	}

	abstract class AbstractMoveItemOperation extends org.alice.ide.operations.AbstractActionOperation {
		protected void swapWithNext( int index ) {
			if( index >= 0 ) {
				javax.swing.DefaultComboBoxModel defaultComboBoxModel = ArrayInitializerPane.this.getDefaultComboBoxModel();
				Object prev = defaultComboBoxModel.getElementAt( index );
				defaultComboBoxModel.removeElementAt( index );
				defaultComboBoxModel.insertElementAt( prev, index+1 );
			}
		}
	}
	
	class MoveItemUpOperation extends AbstractMoveItemOperation {
		public MoveItemUpOperation() {
			this.putValue( javax.swing.Action.NAME, "move up" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			this.swapWithNext( ArrayInitializerPane.this.list.getSelectedIndex()-1 );
		}
	}

	class MoveItemDownOperation extends AbstractMoveItemOperation {
		public MoveItemDownOperation() {
			this.putValue( javax.swing.Action.NAME, "move down" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			this.swapWithNext( ArrayInitializerPane.this.list.getSelectedIndex() );
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
					FauxItem rv = new FauxItem( index, e );
					rv.handleTypeChange( ArrayInitializerPane.this.arrayType.getComponentType() );
					return rv;
				}
				@Override
				protected void updateIndex( javax.swing.AbstractButton button, int index ) {
					FauxItem fauxItem = (FauxItem)button;
					fauxItem.setIndex( index );
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
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleTypeChange (array)", this.arrayType );
		edu.cmu.cs.dennisc.alice.ast.AbstractType componentType = this.arrayType.getComponentType();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleTypeChange (component)", componentType );
		//this.list.handleTypeChange( type );
		for( java.awt.Component component : this.list.getComponents() ) {
			if( component instanceof FauxItem ) {
				FauxItem fauxItem = (FauxItem)component;
				fauxItem.handleTypeChange( componentType );
			}
		}
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

