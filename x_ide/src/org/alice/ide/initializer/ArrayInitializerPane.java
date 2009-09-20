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

class ListPropertyComboBoxModel<E> extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
	private edu.cmu.cs.dennisc.property.ListProperty< E > listProperty;
	private Object selectedItem;

	public ListPropertyComboBoxModel( edu.cmu.cs.dennisc.property.ListProperty< E > listProperty ) {
		this.listProperty = listProperty;
		this.listProperty.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.ListPropertyListener< E >() {

			private int n;

			public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
			}
			public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
				int i = e.getStartIndex();
				ListPropertyComboBoxModel.this.fireIntervalAdded( e.getSource(), i, i + e.getElements().size() - 1 );
			}

			public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
				n = e.getTypedSource().size();
			}
			public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
				ListPropertyComboBoxModel.this.fireContentsChanged( e.getSource(), 0, n );
			}

			public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
			}
			public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
				int i = e.getStartIndex();
				//int n = e.getElements().size();
				int n = 1;
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle java.util.ConcurrentModificationException" );
				ListPropertyComboBoxModel.this.fireIntervalRemoved( e.getSource(), i, i + n - 1 );
			}

			public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
				n = e.getTypedSource().size();
			}
			public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
				int i = e.getStartIndex();
				ListPropertyComboBoxModel.this.fireContentsChanged( e.getSource(), i, i + e.getElements().size() - 1 );
			}

		} );
	}
	public Object getElementAt( int index ) {
		return this.listProperty.get( index );
	}
	public int getSize() {
		return this.listProperty.size();
	}
	public Object getSelectedItem() {
		return this.selectedItem;
	}
	public void setSelectedItem( Object selectedItem ) {
		this.selectedItem = selectedItem;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ArrayInitializerPane extends AbstractInitializerPane {
	class AddItemOperation extends org.alice.ide.operations.AbstractActionOperation {
		private edu.cmu.cs.dennisc.alice.ast.Expression expression;
		private int index;
		public AddItemOperation() {
			this.putValue( javax.swing.Action.NAME, "Add" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			this.expression = ExpressionUtilities.createDefaultExpression( ArrayInitializerPane.this.type.getComponentType() );
			this.index = ArrayInitializerPane.this.arrayExpressions.size();
			actionContext.commitAndInvokeRedoIfAppropriate();
		}
		@Override
		public void redo() throws javax.swing.undo.CannotRedoException {
			ArrayInitializerPane.this.arrayExpressions.add( this.index, this.expression );
		}
		@Override
		public void undo() throws javax.swing.undo.CannotUndoException {
			if( ArrayInitializerPane.this.arrayExpressions.get( this.index ) == this.expression ) {
				ArrayInitializerPane.this.arrayExpressions.remove( this.index );
			} else {
				throw new javax.swing.undo.CannotUndoException();
			}
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
		
	}

	class RemoveItemOperation extends org.alice.ide.operations.AbstractActionOperation {
		private int index;
		private edu.cmu.cs.dennisc.alice.ast.Expression expression;
		public RemoveItemOperation() {
			this.putValue( javax.swing.Action.NAME, "Remove" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			this.index = ArrayInitializerPane.this.list.getSelectedIndex();
			this.expression = ArrayInitializerPane.this.list.getModel().getElementAt( this.index );
			actionContext.commitAndInvokeRedoIfAppropriate();
		}
		@Override
		public void redo() throws javax.swing.undo.CannotRedoException {
			if( ArrayInitializerPane.this.arrayExpressions.get( this.index ) == this.expression ) {
				ArrayInitializerPane.this.arrayExpressions.remove( this.index );
			} else {
				throw new javax.swing.undo.CannotRedoException();
			}
		}
		@Override
		public void undo() throws javax.swing.undo.CannotUndoException {
			ArrayInitializerPane.this.arrayExpressions.add( this.index, this.expression );
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
	}
	abstract class AbstractMoveItemOperation extends org.alice.ide.operations.AbstractActionOperation {
		private int index;
		private void swapWithNext( int index ) {
			ArrayInitializerPane.this.swapWithNext( index );
		}
		@Override
		public final void redo() throws javax.swing.undo.CannotRedoException {
			ArrayInitializerPane.this.swapWithNext( this.index );
			ArrayInitializerPane.this.swapWithNext( this.index + this.getRedoSelectionIndexDelta() );
		}
		@Override
		public final void undo() throws javax.swing.undo.CannotUndoException {
			ArrayInitializerPane.this.swapWithNext( this.index );
			ArrayInitializerPane.this.swapWithNext( this.index + this.getUndoSelectionIndexDelta() );
		}
		protected abstract int getIndex( int selectedIndex );
		protected abstract int getRedoSelectionIndexDelta();
		protected abstract int getUndoSelectionIndexDelta();
		public final void perform(zoot.ActionContext actionContext) {
			this.index = this.getIndex( ArrayInitializerPane.this.list.getSelectedIndex() );
			actionContext.commitAndInvokeRedoIfAppropriate();
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
		
	}

	class MoveItemUpOperation extends AbstractMoveItemOperation {
		public MoveItemUpOperation() {
			this.putValue( javax.swing.Action.NAME, "Move Up" );
		}
		@Override
		protected int getIndex(int selectedIndex) {
			return selectedIndex-1;
		}
		@Override
		public int getRedoSelectionIndexDelta() {
			return 0;
		}
		@Override
		public int getUndoSelectionIndexDelta() {
			return 1;
		}
	}

	class MoveItemDownOperation extends AbstractMoveItemOperation {
		public MoveItemDownOperation() {
			this.putValue( javax.swing.Action.NAME, "Move Down" );
		}
		@Override
		protected int getIndex(int selectedIndex) {
			return selectedIndex;
		}
		@Override
		public int getRedoSelectionIndexDelta() {
			return 1;
		}
		@Override
		public int getUndoSelectionIndexDelta() {
			return 0;
		}
	}

	class ItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation< edu.cmu.cs.dennisc.alice.ast.Expression > {
		public ItemSelectionOperation( javax.swing.ComboBoxModel comboBoxModel ) {
			super( comboBoxModel );
		}
		@Override
		protected void handleSelectionChange(edu.cmu.cs.dennisc.alice.ast.Expression value) {
			ArrayInitializerPane.this.handleSelectionChange( value );
		}
		@Override
		public boolean isSignificant() {
			return false;
		}
	}

	//	class ExpressionList extends zoot.ZList< edu.cmu.cs.dennisc.alice.ast.Expression > {
	//		public ExpressionList( zoot.ItemSelectionOperation< edu.cmu.cs.dennisc.alice.ast.Expression > itemSelectionOperation ) {
	//			super( itemSelectionOperation );
	//			this.setOpaque( true );
	//			this.setBackground( java.awt.Color.WHITE );
	//		}
	//		@Override
	//		public void updateUI() {
	//			setUI( new edu.cmu.cs.dennisc.swing.plaf.ListUI< edu.cmu.cs.dennisc.alice.ast.Expression >() {
	//				@Override
	//				protected javax.swing.AbstractButton createComponentFor( int index, edu.cmu.cs.dennisc.alice.ast.Expression e ) {
	//					FauxItem rv = new FauxItem( index, e );
	//					edu.cmu.cs.dennisc.alice.ast.AbstractType type = ArrayInitializerPane.this.arrayInstanceCreation.arrayType.getValue();
	//					if( type != null && type.isArray() ) {
	//						rv.handleTypeChange( type.getComponentType() );
	//					}
	//					return rv;
	//				}
	//				@Override
	//				protected void updateIndex( javax.swing.AbstractButton button, int index ) {
	////					FauxItem fauxItem = (FauxItem)button;
	////					fauxItem.setIndex( index );
	//				}
	//			} );
	//		}
	//	}

	class ExpressionList extends swing.GridBagPane {
		private java.awt.Component glue = javax.swing.Box.createGlue();
		private javax.swing.ButtonGroup group;
		private java.awt.GridBagConstraints gbc;

		public ExpressionList( zoot.ItemSelectionOperation< edu.cmu.cs.dennisc.alice.ast.Expression > itemSelectionOperation ) {
			//			super( itemSelectionOperation );
			this.group = new javax.swing.ButtonGroup();
			this.gbc = new java.awt.GridBagConstraints();
			this.gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
			this.gbc.fill = java.awt.GridBagConstraints.BOTH;
			this.gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			this.gbc.weightx = 1.0;
			this.setOpaque( true );
			this.setBackground( java.awt.Color.WHITE );
			this.refresh();
		}
		public int getSelectedIndex() {
			javax.swing.ButtonModel selection = this.group.getSelection();
			int index = 0;
			java.util.Enumeration< javax.swing.AbstractButton > e = this.group.getElements();
			while( e.hasMoreElements() ) {
				javax.swing.AbstractButton button = e.nextElement();
				if( button.getModel() == selection ) {
					return index;
				}
				index ++;
			}
			return -1;
		}
		
		public void setSelectedIndex( int index ) {
			if( index >= 0 ) {
				FauxItem fauxItem = (FauxItem)this.getComponent( index );
				fauxItem.setSelected( true );
			} else {
				javax.swing.ButtonModel model = this.group.getSelection();
				if( model != null ) {
					model.setSelected( false );
				}
			}
		}
				
		public void refresh() {
			int N = ArrayInitializerPane.this.arrayExpressions.size();
			int M = this.group.getButtonCount();
			for( int i=M; i<N; i++ ) {
				FauxItem fauxItem = new FauxItem( i, ArrayInitializerPane.this.arrayExpressions ) {
					@Override
					protected edu.cmu.cs.dennisc.alice.ast.AbstractType getFillInType() {
						return ArrayInitializerPane.this.type.getComponentType();
					}
					@Override
					public void setSelected( boolean b ) {
						super.setSelected( b );
						updateButtons();
					}
				};
				this.group.add( fauxItem );
			}
			for( int i=N; i<M; i++ ) {
				FauxItem fauxItem = (FauxItem)this.getComponent( i );
				this.group.remove( fauxItem );
			}
			this.removeAll();
			java.util.Enumeration< javax.swing.AbstractButton > e = this.group.getElements();
			while( e.hasMoreElements() ) {
				FauxItem fauxItem = (FauxItem)e.nextElement();
				fauxItem.refresh();
				this.add( fauxItem, this.gbc );
			}
			this.gbc.weighty = 1.0;
			this.add( this.glue, this.gbc );
			this.gbc.weighty = 0.0;
		}
	}

	private ExpressionList list;

	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions;

	private zoot.ZButton addButton = new zoot.ZButton( new AddItemOperation() );
	private zoot.ZButton removeButton = new zoot.ZButton( new RemoveItemOperation() );
	private zoot.ZButton moveUpButton = new zoot.ZButton( new MoveItemUpOperation() );
	private zoot.ZButton moveDownButton = new zoot.ZButton( new MoveItemDownOperation() );

	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;

	public ArrayInitializerPane( edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions ) {
		this.arrayExpressions = arrayExpressions;
		this.arrayExpressions.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.Expression >() {
			@Override
			protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
			}
			@Override
			protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						ArrayInitializerPane.this.list.refresh();
					}
				} );
			}
		} );
		ListPropertyComboBoxModel< edu.cmu.cs.dennisc.alice.ast.Expression > comboBoxModel = new ListPropertyComboBoxModel< edu.cmu.cs.dennisc.alice.ast.Expression >( this.arrayExpressions );
		this.list = new ExpressionList( new ItemSelectionOperation( comboBoxModel ) );
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

	private void updateButtons() {
		boolean isTypeValid = this.type != null && this.type.isArray();
		this.addButton.setEnabled( isTypeValid );
		int index = this.list.getSelectedIndex();
		final int N = this.list.getComponentCount()-1;
		edu.cmu.cs.dennisc.print.PrintUtilities.println( index, N );
		this.removeButton.setEnabled( isTypeValid && index != -1 );
		this.moveUpButton.setEnabled( isTypeValid && index > 0 );
		this.moveDownButton.setEnabled( isTypeValid && index >= 0 && index < N - 1 );
	}
	private void handleSelectionChange( edu.cmu.cs.dennisc.alice.ast.Expression value ) {
		this.updateButtons();
	}
	private void swapWithNext( int index ) {
		if( index >= 0 ) {
			edu.cmu.cs.dennisc.alice.ast.Expression expression0 = this.arrayExpressions.get( index );
			edu.cmu.cs.dennisc.alice.ast.Expression expression1 = this.arrayExpressions.get( index + 1 );
			this.arrayExpressions.set( index, expression1, expression0 );
		}
	}

	@Override
	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.type = type;
		this.updateButtons();
	}
	//	@Override
	//	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
	//		return this.arrayInstanceCreation;
	//	}
	//	@Override
	//	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
	//		javax.swing.ListModel model = this.list.getModel();
	//		final int N = model.getSize();
	//		edu.cmu.cs.dennisc.alice.ast.Expression[] expressions = new edu.cmu.cs.dennisc.alice.ast.Expression[ N ];
	//		for( int i=0; i<N; i++ ) {
	//			expressions[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)model.getElementAt( i );
	//		}
	//		return new edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation( this.arrayType, new Integer[] { expressions.length }, expressions );
	//	}
}
