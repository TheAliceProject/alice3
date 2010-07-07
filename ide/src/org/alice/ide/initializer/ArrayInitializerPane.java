/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.initializer;

import edu.cmu.cs.dennisc.alice.ast.Expression;
//class ListPropertyComboBoxModel<E> extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
//	private edu.cmu.cs.dennisc.property.ListProperty< E > listProperty;
//	private Object selectedItem;
//
//	public ListPropertyComboBoxModel( edu.cmu.cs.dennisc.property.ListProperty< E > listProperty ) {
//		this.listProperty = listProperty;
//		this.listProperty.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.ListPropertyListener< E >() {
//
//			private int n;
//
//			public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
//			}
//			public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
//				int i = e.getStartIndex();
//				ListPropertyComboBoxModel.this.fireIntervalAdded( e.getSource(), i, i + e.getElements().size() - 1 );
//			}
//
//			public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
//				n = e.getTypedSource().size();
//			}
//			public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
//				ListPropertyComboBoxModel.this.fireContentsChanged( e.getSource(), 0, n );
//			}
//
//			public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
//			}
//			public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
//				int i = e.getStartIndex();
//				//int n = e.getElements().size();
//				int n = 1;
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle java.util.ConcurrentModificationException" );
//				ListPropertyComboBoxModel.this.fireIntervalRemoved( e.getSource(), i, i + n - 1 );
//			}
//
//			public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
//				n = e.getTypedSource().size();
//			}
//			public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
//				int i = e.getStartIndex();
//				ListPropertyComboBoxModel.this.fireContentsChanged( e.getSource(), i, i + e.getElements().size() - 1 );
//			}
//
//		} );
//	}
//	public Object getElementAt( int index ) {
//		return this.listProperty.get( index );
//	}
//	public int getSize() {
//		return this.listProperty.size();
//	}
//	public Object getSelectedItem() {
//		return this.selectedItem;
//	}
//	public void setSelectedItem( Object selectedItem ) {
//		this.selectedItem = selectedItem;
//	}
//}
//
///**
// * @author Dennis Cosgrove
// */
//public class ArrayInitializerPane extends AbstractInitializerPane {
//	class AddItemOperation extends org.alice.ide.operations.ActionOperation {
//		public AddItemOperation() {
//			super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "a93199db-d9f7-4c95-b82d-f6ac6720f29b" ) );
//			this.setName( "Add" );
//		}
//		@Override
//		protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
//			assert ArrayInitializerPane.this.type != null;
//			final edu.cmu.cs.dennisc.alice.ast.Expression expression = ExpressionUtilities.createDefaultExpression( ArrayInitializerPane.this.type.getComponentType() );
//			final int index = ArrayInitializerPane.this.arrayExpressions.size();
//			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
//				@Override
//				public void doOrRedo( boolean isDo ) {
//					ArrayInitializerPane.this.arrayExpressions.add( index, expression );
//				}
//				@Override
//				public void undo() {
//					if( ArrayInitializerPane.this.arrayExpressions.get( index ) == expression ) {
//						ArrayInitializerPane.this.arrayExpressions.remove( index );
//					} else {
//						throw new javax.swing.undo.CannotUndoException();
//					}
//				}
//				@Override
//				protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
//					rv.append( "add: " );
//					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, expression, locale );
//					return rv;
//				}
//				
//			});
//		}
//	}
//
//	class RemoveItemOperation extends org.alice.ide.operations.ActionOperation {
//		public RemoveItemOperation() {
//			super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "a87ae96c-07fb-4a78-94c3-177ec3d642ce" ) );
//			this.setName( "Remove" );
//		}
//		@Override
//		protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
//			final int index = ArrayInitializerPane.this.list.getSelectedIndex();
//			final edu.cmu.cs.dennisc.alice.ast.Expression expression = ArrayInitializerPane.this.list.getItemAt( index );
//			context.commitAndInvokeDo(new org.alice.ide.ToDoEdit() {
//				@Override
//				public void doOrRedo( boolean isDo ) {
//					if( ArrayInitializerPane.this.arrayExpressions.get( index ) == expression ) {
//						ArrayInitializerPane.this.arrayExpressions.remove( index );
//					} else {
//						throw new javax.swing.undo.CannotRedoException();
//					}
//				}
//				@Override
//				public void undo() {
//					ArrayInitializerPane.this.arrayExpressions.add( index, expression );
//				}
//				@Override
//				protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
//					rv.append( "remove: " );
//					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, expression, locale );
//					return rv;
//				}
//			});
//		}
//	}
//	abstract class AbstractMoveItemOperation extends org.alice.ide.operations.ActionOperation {
//		public AbstractMoveItemOperation( java.util.UUID individualId ) {
//			super( edu.cmu.cs.dennisc.alice.Project.GROUP, individualId );
//		}
//		protected abstract int getIndex( int selectedIndex );
//		protected abstract int getRedoSelectionIndexDelta();
//		protected abstract int getUndoSelectionIndexDelta();
//		@Override
//		protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
//			final int index = this.getIndex( ArrayInitializerPane.this.list.getSelectedIndex() );
//			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
//				@Override
//				public void doOrRedo( boolean isDo ) {
//					ArrayInitializerPane.this.swapWithNext( index );
//					list.setSelectedIndex( index + getRedoSelectionIndexDelta() );
//				}
//				@Override
//				public void undo() {
//					ArrayInitializerPane.this.swapWithNext( index );
//					list.setSelectedIndex( index + getUndoSelectionIndexDelta() );
//				}
//				@Override
//				protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
//					rv.append( "swap" );
//					return rv;
//				}
//				
//			});
//		}
//	}
//
//	class MoveItemUpOperation extends AbstractMoveItemOperation {
//		public MoveItemUpOperation() {
//			super( java.util.UUID.fromString( "3fc4c11e-215d-4780-8083-600663d5738e" ) );
//			this.setName( "Move Up" );
//		}
//		@Override
//		protected int getIndex(int selectedIndex) {
//			return selectedIndex-1;
//		}
//		@Override
//		public int getRedoSelectionIndexDelta() {
//			return 0;
//		}
//		@Override
//		public int getUndoSelectionIndexDelta() {
//			return 1;
//		}
//	}
//
//	class MoveItemDownOperation extends AbstractMoveItemOperation {
//		public MoveItemDownOperation() {
//			super( java.util.UUID.fromString( "c308bb2f-e58d-4b12-861f-2767533b56a2" ) );
//			this.setName( "Move Down" );
//		}
//		@Override
//		protected int getIndex(int selectedIndex) {
//			return selectedIndex;
//		}
//		@Override
//		public int getRedoSelectionIndexDelta() {
//			return 1;
//		}
//		@Override
//		public int getUndoSelectionIndexDelta() {
//			return 0;
//		}
//	}
//
//	class ItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation< edu.cmu.cs.dennisc.alice.ast.Expression > {
//		public ItemSelectionOperation( int selectedIndex, edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
//			super( java.util.UUID.fromString( "cd4c10ba-3c46-4344-b0df-39a3926e8c9d" ), selectedIndex, expressions );
//		}
//		@Override
//		protected void handleSelectionChange(edu.cmu.cs.dennisc.alice.ast.Expression value) {
//			ArrayInitializerPane.this.handleSelectionChange( value );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle undo/redo", this );
//		}
//	}
//
//	class ExpressionList extends edu.cmu.cs.dennisc.croquet.GridBagPanel {
//		private edu.cmu.cs.dennisc.croquet.Component< ? > glue = edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue();
//		private javax.swing.ButtonGroup group;
//		private java.awt.GridBagConstraints gbc;
//
//		public ExpressionList( javax.swing.ComboBoxModel comboBoxModel ) {
//			
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: ExpressionList", comboBoxModel );
//			this.group = new javax.swing.ButtonGroup();
//			this.gbc = new java.awt.GridBagConstraints();
//			this.gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
//			this.gbc.fill = java.awt.GridBagConstraints.BOTH;
//			this.gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//			this.gbc.weightx = 1.0;
//			this.setBackgroundColor( java.awt.Color.WHITE );
//			this.refresh();
//		}
//		public int getSelectedIndex() {
//			javax.swing.ButtonModel selection = this.group.getSelection();
//			int index = 0;
//			java.util.Enumeration< javax.swing.AbstractButton > e = this.group.getElements();
//			while( e.hasMoreElements() ) {
//				javax.swing.AbstractButton button = e.nextElement();
//				if( button.getModel() == selection ) {
//					return index;
//				}
//				index ++;
//			}
//			return -1;
//		}
//		
//		public void setSelectedIndex( int index ) {
//			if( index >= 0 ) {
//				FauxItem fauxItem = (FauxItem)this.getAwtComponent().getComponent( index );
//				fauxItem.setSelected( true );
//			} else {
//				javax.swing.ButtonModel model = this.group.getSelection();
//				if( model != null ) {
//					model.setSelected( false );
//				}
//			}
//		}
//		
//		public edu.cmu.cs.dennisc.alice.ast.Expression getItemAt( int index ) {
//			return ArrayInitializerPane.this.arrayExpressions.get( index );
//		}
//				
//		public void refresh() {
//			int N = ArrayInitializerPane.this.arrayExpressions.size();
//			int M = this.group.getButtonCount();
//			for( int i=M; i<N; i++ ) {
//				FauxItem fauxItem = new FauxItem( i, ArrayInitializerPane.this.arrayExpressions ) {
//					@Override
//					protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getFillInType() {
//						return ArrayInitializerPane.this.type.getComponentType();
//					}
//					@Override
//					public void setSelected( boolean b ) {
//						super.setSelected( b );
//						updateButtons();
//					}
//				};
//				this.group.add( fauxItem );
//			}
//			for( int i=N; i<M; i++ ) {
//				FauxItem fauxItem = (FauxItem)this.getAwtComponent().getComponent( i );
//				this.group.remove( fauxItem );
//			}
//			this.removeAllComponents();
//			java.util.Enumeration< javax.swing.AbstractButton > e = this.group.getElements();
//			while( e.hasMoreElements() ) {
//				FauxItem fauxItem = (FauxItem)e.nextElement();
//				fauxItem.refresh();
//				this.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( fauxItem ), this.gbc );
//			}
//			this.gbc.weighty = 1.0;
//			this.addComponent( this.glue, this.gbc );
//			this.gbc.weighty = 0.0;
//		}
//	}
//
//	private ExpressionList list;
//
//	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions;
//
//	private AddItemOperation addItemOperation = new AddItemOperation();
//	private RemoveItemOperation removeItemOperation = new RemoveItemOperation();
//	private MoveItemUpOperation moveItemUpOperation = new MoveItemUpOperation();
//	private MoveItemDownOperation moveItemDownOperation = new MoveItemDownOperation();
//
//	private edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type;
//
//	public ArrayInitializerPane( edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions ) {
//		super( 8, 0 );
//		this.arrayExpressions = arrayExpressions;
//		this.arrayExpressions.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.Expression >() {
//			@Override
//			protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
//			}
//			@Override
//			protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
//				javax.swing.SwingUtilities.invokeLater( new Runnable() {
//					public void run() {
//						ArrayInitializerPane.this.list.refresh();
//					}
//				} );
//			}
//		} );
//		ListPropertyComboBoxModel< edu.cmu.cs.dennisc.alice.ast.Expression > comboBoxModel = new ListPropertyComboBoxModel< edu.cmu.cs.dennisc.alice.ast.Expression >( this.arrayExpressions );
//		this.list = new ExpressionList( comboBoxModel );
//		this.updateButtons();
//
//		edu.cmu.cs.dennisc.croquet.GridBagPanel buttonPane = new edu.cmu.cs.dennisc.croquet.GridBagPanel();
//		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
//		gbc.fill = java.awt.GridBagConstraints.BOTH;
//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		buttonPane.addComponent( this.addItemOperation.createButton(), gbc );
//		buttonPane.addComponent( this.removeItemOperation.createButton(), gbc );
//		gbc.insets.top = 8;
//		buttonPane.addComponent( this.moveItemUpOperation.createButton(), gbc );
//		gbc.insets.top = 0;
//		buttonPane.addComponent( this.moveItemDownOperation.createButton(), gbc );
//		gbc.weighty = 1.0;
//		buttonPane.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue(), gbc );
//
//		this.addComponent( buttonPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST );
//
//		edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( this.list );
////		{
////			@Override
////			public java.awt.Dimension getPreferredSize() {
////				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumSize( super.getPreferredSize(), 240, 180 );
////			}
////		};
//		scrollPane.setBorder( null );
//		this.addComponent( scrollPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
//	}
//
//	private void updateButtons() {
//		boolean isTypeValid = this.type != null && this.type.isArray();
//		this.addItemOperation.setEnabled( isTypeValid );
//		int index = this.list.getSelectedIndex();
//		final int N = this.list.getComponentCount()-1;
//		this.removeItemOperation.setEnabled( isTypeValid && index != -1 );
//		this.moveItemUpOperation.setEnabled( isTypeValid && index > 0 );
//		this.moveItemDownOperation.setEnabled( isTypeValid && index >= 0 && index < N - 1 );
//	}
//	private void handleSelectionChange( edu.cmu.cs.dennisc.alice.ast.Expression value ) {
//		this.updateButtons();
//	}
//	private void swapWithNext( int index ) {
//		if( index >= 0 ) {
//			edu.cmu.cs.dennisc.alice.ast.Expression expression0 = this.arrayExpressions.get( index );
//			edu.cmu.cs.dennisc.alice.ast.Expression expression1 = this.arrayExpressions.get( index + 1 );
//			this.arrayExpressions.set( index, expression1, expression0 );
//		}
//	}
//
//	@Override
//	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
//		this.type = type;
//		this.updateButtons();
//	}
//}

import edu.cmu.cs.dennisc.alice.ast.Expression;

abstract class FauxItem extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.AbstractButton > {
	//private static org.alice.ide.codeeditor.Factory factory = new org.alice.ide.codeeditor.Factory();
	//javax.swing.UIManager.getColor("List.selectionBackground");
	//javax.swing.UIManager.getColor("List[Selected].textBackground");
	private static java.awt.Color SELECTION_BACKGROUND = new java.awt.Color( 57, 105, 138 );
//	private static java.awt.Color SELECTION_ROLLOVER_BACKGROUND = SELECTION_BACKGROUND.brighter();;
	private int index;
	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty;
	public FauxItem( int index, edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty ) {
		this.index = index;
		this.expressionListProperty = expressionListProperty;
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getFillInType();
	
	public int getIndex() {
		return this.index;
	}
	public void addCloseButton( java.awt.event.ActionListener closeButtonActionListener ) {
		assert closeButtonActionListener != null;
		javax.swing.JButton closeButton = new edu.cmu.cs.dennisc.javax.swing.components.JCloseButton( true );
		closeButton.addActionListener( closeButtonActionListener );
		this.getAwtComponent().add( closeButton, java.awt.BorderLayout.LINE_END );
		this.getAwtComponent().setComponentZOrder( closeButton, 0 );
	}
	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		javax.swing.JToggleButton rv = new javax.swing.JToggleButton() {
			@Override
			public java.awt.Dimension getMaximumSize() {
				java.awt.Dimension rv = super.getPreferredSize();
				rv.width = Short.MAX_VALUE;
				return rv;
			}
			@Override
			public void setSelected(boolean b) {
				super.setSelected(b);
				this.requestFocus();
			}
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				//super.paintComponent(g);
				if( this.isSelected() ) {
					java.awt.Color color;
//					if( this.getModel().isRollover() ) {
//						color = SELECTION_ROLLOVER_BACKGROUND;
//					} else {
						color = SELECTION_BACKGROUND;
//					}
					g.setColor( color );
					g.fillRoundRect( 0, 0, this.getWidth(), this.getHeight(), 4, 4 );
					if( this.getModel().isRollover() ) {
						color = java.awt.Color.LIGHT_GRAY;
					} else {
						color = java.awt.Color.GRAY;
					}
					g.setColor( color );
					edu.cmu.cs.dennisc.java.awt.KnurlUtilities.paintKnurl5( g, 2, 2, 6, this.getHeight()-5 );
				}
			}
		};
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder(4, 14, 4, 4) );
		rv.setLayout( new java.awt.BorderLayout() );
		rv.setRolloverEnabled( true );

		org.alice.ide.common.AbstractDropDownListItemExpressionPane dropDownListItemExpressionPane = new org.alice.ide.common.AbstractDropDownListItemExpressionPane( this.index, this.expressionListProperty ) {
			@Override
			protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getFillInType() {
				return FauxItem.this.getFillInType();
			}
		};
		edu.cmu.cs.dennisc.croquet.LineAxisPanel lineAxisPanel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel(
				new edu.cmu.cs.dennisc.croquet.Label(  "[ " + index + " ]" ),
				edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 ),
				dropDownListItemExpressionPane
		);
		rv.add( lineAxisPanel.getAwtComponent(), java.awt.BorderLayout.LINE_START );
		rv.setFocusable( true );
		return rv;
	}
}

class MutableList extends edu.cmu.cs.dennisc.croquet.PageAxisPanel {
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private int originalIndex = -1;
	private int dropIndex = -1;
	private class MouseAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
		public void mouseClicked(java.awt.event.MouseEvent e) {
		}
		public void mouseEntered(java.awt.event.MouseEvent e) {
		}
		public void mouseExited(java.awt.event.MouseEvent e) {
		}
		public void mousePressed(java.awt.event.MouseEvent e) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)e.getSource();
			button.setSelected( true );
			MutableList.this.handleMousePressed( e );
		}
		public void mouseReleased(java.awt.event.MouseEvent e) {
			MutableList.this.handleMouseReleased( e );
		}
		public void mouseDragged(java.awt.event.MouseEvent e) {
			MutableList.this.handleMouseDragged( e );
		}
		public void mouseMoved(java.awt.event.MouseEvent e) {
		}
	}
	private MouseAdapter mouseAdapter = new MouseAdapter();
	private java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
		public void keyPressed(java.awt.event.KeyEvent e) {
			MutableList.this.handleKeyPressed( e );
		}
		public void keyReleased(java.awt.event.KeyEvent e) {
		}
		public void keyTyped(java.awt.event.KeyEvent e) {
		}
	};

	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty;
	private edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > componentTypeProperty;

	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.Expression > expressionListPropertyListener = new edu.cmu.cs.dennisc.property.event.ListPropertyListener<edu.cmu.cs.dennisc.alice.ast.Expression>() {
		public void adding(edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<Expression> e) {
		}
		public void added(edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<Expression> e) {
			final int N = e.getElements().size();
			for( int i=0; i<N; i++ ) {
				MutableList.this.addTileFor( e.getStartIndex() + i );
			}
			MutableList.this.revalidateAndRepaint();
		}

		public void clearing(edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<Expression> e) {
		}
		public void cleared(edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<Expression> e) {
			MutableList.this.forgetAndRemoveAllComponents();
			MutableList.this.revalidateAndRepaint();
		}

		public void removing(edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<Expression> e) {
		}
		public void removed(edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<Expression> e) {
			final int N = e.getElements().size();
			for( int i=0; i<N; i++ ) {
				forgetAndRemoveComponent( getComponent( getComponentCount()-1 ) );
			}
			MutableList.this.revalidateAndRepaint();
		}

		public void setting(edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<Expression> e) {
		}
		public void set(edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<Expression> e) {
			//MutableList.this.refresh();
		}
	};
	public MutableList( edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > componentTypeProperty, edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty ) {
        this.componentTypeProperty = componentTypeProperty;
        this.expressionListProperty = expressionListProperty;
    	this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
    	this.getAwtComponent().setFocusable( true );
    }
    @Override
    protected javax.swing.JPanel createJPanel() {
    	return new DefaultJPanel() {
    		@Override
    		public void paint(java.awt.Graphics g) {
    			super.paint(g);
    			if( dropIndex != -1 ) {
    				if( dropIndex == originalIndex || dropIndex == originalIndex+1 ) {
    					//pass
    				} else {
            			g.setColor( java.awt.Color.GREEN.darker() );
            			int y;
        				if( dropIndex == 0 ) {
        					y = 0;
        				} else {
        					java.awt.Component c = this.getComponent( dropIndex-1 );
        					y = c.getY() + c.getHeight();
        				}
        				edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle(g, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.EAST, 0, y-6, 10, 12 );
            			g.fillRect( 0, y-2, this.getWidth(), 5  );
    				}
    			}
    		}
    	};
    }
    @Override
    protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
    	super.handleAddedTo(parent);
    	this.expressionListProperty.addListPropertyListener( this.expressionListPropertyListener );
    }
    @Override
    protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
    	this.expressionListProperty.removeListPropertyListener( this.expressionListPropertyListener );
    	super.handleRemovedFrom(parent);
    }
    private int getIndexOf( java.awt.Component component ) {
		final int N = this.getComponentCount();
		for( int i=0; i<N; i++ ) {
			java.awt.Component c = this.getAwtComponent().getComponent( i );
			if( c==component ) {
				return i;
			}
    	}
		return -1;
    }
    private int calculateDropIndex( java.awt.Point p ) {
		final int N = this.getComponentCount();
		for( int i=0; i<N; i++ ) {
			java.awt.Component c = this.getAwtComponent().getComponent( i );
			if( p.y < c.getBounds().getCenterY() ) {
				return i;
			}
		}
		return N;
    }
    private void handleMousePressed(java.awt.event.MouseEvent e) {
		java.awt.Component component = e.getComponent();
		this.originalIndex = this.getIndexOf(component);
	}
    
    private static java.awt.Insets INSETS = new java.awt.Insets( 32, 32, 32, 32 );
    private java.awt.Rectangle getGenerousBounds() {
		java.awt.Rectangle rv = this.getBounds();
		edu.cmu.cs.dennisc.java.awt.RectangleUtilities.inset( rv, INSETS );
    	return rv;
    }
    private void handleMouseDragged(java.awt.event.MouseEvent e) {
		java.awt.Component component = e.getComponent();
		java.awt.Point p = javax.swing.SwingUtilities.convertPoint( component, e.getPoint(), this.getAwtComponent() );
		boolean isWithinBounds = this.getGenerousBounds().contains( p );
		if( isWithinBounds ) {
			this.dropIndex = this.calculateDropIndex( p );
		} else {
			this.dropIndex = -1;
		}
		component.setVisible( isWithinBounds );
		this.repaint();
	}
    private void handleMouseReleased(java.awt.event.MouseEvent e) {
		java.awt.Component component = e.getComponent();
		java.awt.Point p = javax.swing.SwingUtilities.convertPoint( component, e.getPoint(), this.getAwtComponent() );
		boolean isWithinBounds = this.getGenerousBounds().contains( p );
		if( isWithinBounds ) {
			this.dropIndex = this.calculateDropIndex( p );
			if( dropIndex == originalIndex || dropIndex == originalIndex+1 ) {
				//pass
			} else {
				int i;
				if( dropIndex < originalIndex ) {
					i = dropIndex;
				} else {
					i = dropIndex - 1;
				}
				this.expressionListProperty.swap( this.originalIndex, i );
				
				FauxItem fauxItem = (FauxItem)this.getComponent( i );
				fauxItem.getAwtComponent().setSelected( true );
			}
		} else {
			this.expressionListProperty.remove( this.originalIndex );
		}
		this.dropIndex = -1;
		this.originalIndex = -1;
		component.setVisible( isWithinBounds );
		this.revalidateAndRepaint();
	}
	public void handleKeyPressed(java.awt.event.KeyEvent e) {
		java.awt.Component component = e.getComponent();
		int i = this.getIndexOf( component );
		int keyCode = e.getKeyCode();
		switch( keyCode ) {
		case java.awt.event.KeyEvent.VK_DELETE:
		case java.awt.event.KeyEvent.VK_BACK_SPACE:
			MutableList.this.expressionListProperty.remove( i );
			break;
		case java.awt.event.KeyEvent.VK_UP:
			if( i > 0 ) {
				javax.swing.AbstractButton button = (javax.swing.AbstractButton)this.getAwtComponent().getComponent( i-1 );
				button.setSelected( true );
			}
			break;
		case java.awt.event.KeyEvent.VK_DOWN:
			final int N = this.getComponentCount();
			if( i < N-1 ) {
				javax.swing.AbstractButton button = (javax.swing.AbstractButton)this.getAwtComponent().getComponent( i+1 );
				button.setSelected( true );
			}
			break;
		}
	}
	
	private void addTileFor( int index ) {
    	final FauxItem fauxItem = new FauxItem( index, this.expressionListProperty ) {
			@Override
			protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getFillInType() {
				return MutableList.this.componentTypeProperty.getValue();
			}
    		@Override
    		protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
    			super.handleAddedTo(parent);
    	    	this.addMouseListener( MutableList.this.mouseAdapter );
    	    	this.addMouseMotionListener( MutableList.this.mouseAdapter );
    	    	this.addKeyListener( MutableList.this.keyListener );
    	    	MutableList.this.buttonGroup.add( this.getAwtComponent() );
    		}
    		@Override
    		protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
    	    	MutableList.this.buttonGroup.remove( this.getAwtComponent() );
    	    	this.removeKeyListener( MutableList.this.keyListener );
    	    	this.removeMouseMotionListener( MutableList.this.mouseAdapter );
    	    	this.removeMouseListener( MutableList.this.mouseAdapter );
    			super.handleRemovedFrom(parent);
    		}
    	};
    	fauxItem.addCloseButton( new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				int index = fauxItem.getIndex();
				if( index != -1 ) {
					MutableList.this.expressionListProperty.remove( index );
				} else {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate" );
				}
			}
		} );
    	this.addComponent( fauxItem );
    	javax.swing.ButtonModel buttonModel = this.buttonGroup.getSelection();
    	if( buttonModel != null ) {
    		buttonModel.setSelected( false );
    	}
    	this.revalidateAndRepaint();
    }
	
//	private void refresh() {
//		this.forgetAndRemoveAllComponents();
//		for( Expression expression : this.expressionListProperty ) {
//			this.addTileFor( expression );
//		}
//		MutableList.this.revalidateAndRepaint();
//	}
}

class AddExpressionOperation extends edu.cmu.cs.dennisc.croquet.FauxPopupMenuOperation {
	private edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > componentTypeProperty;
	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty;
	public AddExpressionOperation( edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > componentTypeProperty, edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty ) {
        super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "2511b7a7-0920-43de-9f73-977e9ac73686" ) );
        this.componentTypeProperty = componentTypeProperty;
        this.expressionListProperty = expressionListProperty;
        this.setName( "Add..." );
    }
	@Override
	protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		class AddExpressionEdit extends org.alice.ide.ToDoEdit {
			private edu.cmu.cs.dennisc.alice.ast.Expression expression;
			private int index;
			@Override
			public void doOrRedo( boolean isDo ) {
				this.index = expressionListProperty.size();
				expressionListProperty.add( this.expression );
			}
			@Override
			public void undo() {
				//expressionListProperty.indexOf( this.expression )
				expressionListProperty.remove( this.index );
			}
			@Override
			protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
				rv.append( "add: " );
				edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, this.expression, locale);
				return rv;
			}
		}
		final edu.cmu.cs.dennisc.croquet.ViewController<?, ?> viewController = context.getViewController();
		final java.awt.Point p = context.getPoint();
		context.pend( new edu.cmu.cs.dennisc.croquet.PendResolver< AddExpressionEdit, edu.cmu.cs.dennisc.alice.ast.Expression >() {
			public AddExpressionEdit createEdit() {
				return new AddExpressionEdit();
			}
			public AddExpressionEdit initialize(AddExpressionEdit rv, edu.cmu.cs.dennisc.croquet.ModelContext context, java.util.UUID id, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = componentTypeProperty.getValue();
				org.alice.ide.IDE.getSingleton().promptUserForExpression( type, rv.expression, viewController, p, taskObserver );
				return rv;
			}
			public AddExpressionEdit handleCompletion( AddExpressionEdit rv, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
				//todo: remove?
				org.alice.ide.IDE.getSingleton().unsetPreviousExpressionAndDropStatement();
				rv.expression = expression;
				return rv;
			}
			public void handleCancelation() {
				//todo: remove?
				org.alice.ide.IDE.getSingleton().unsetPreviousExpressionAndDropStatement();
			}
		} );
	}
}

public class ArrayInitializerPane extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	//private org.alice.ide.initializer.BogusNode bogusNode = new org.alice.ide.initializer.BogusNode( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE.getArrayType(), true );
    public ArrayInitializerPane( edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > componentTypeProperty, edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions ) {
        MutableList mutableList = new MutableList( componentTypeProperty, arrayExpressions );
        AddExpressionOperation addExpressionOperation = new AddExpressionOperation( componentTypeProperty, arrayExpressions );
        edu.cmu.cs.dennisc.croquet.Button button = addExpressionOperation.createButton();
        edu.cmu.cs.dennisc.croquet.PageAxisPanel pageAxisPanel = new edu.cmu.cs.dennisc.croquet.PageAxisPanel( mutableList, button );
        edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( pageAxisPanel );
        scrollPane.setBorder( null );
        scrollPane.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
        scrollPane.setBackgroundColor( null );
        scrollPane.getAwtComponent().setOpaque( false );
        this.addComponent( scrollPane, Constraint.CENTER );
        this.setMinimumPreferredHeight( 200 );
    }
} 
