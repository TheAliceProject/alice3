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
	class AddItemOperation extends org.alice.ide.operations.ActionOperation {
		public AddItemOperation() {
			super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "a93199db-d9f7-4c95-b82d-f6ac6720f29b" ) );
			this.setName( "Add" );
		}
		@Override
		protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
			assert ArrayInitializerPane.this.type != null;
			final edu.cmu.cs.dennisc.alice.ast.Expression expression = ExpressionUtilities.createDefaultExpression( ArrayInitializerPane.this.type.getComponentType() );
			final int index = ArrayInitializerPane.this.arrayExpressions.size();
			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					ArrayInitializerPane.this.arrayExpressions.add( index, expression );
				}
				@Override
				public void undo() {
					if( ArrayInitializerPane.this.arrayExpressions.get( index ) == expression ) {
						ArrayInitializerPane.this.arrayExpressions.remove( index );
					} else {
						throw new javax.swing.undo.CannotUndoException();
					}
				}
				@Override
				protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
					rv.append( "add: " );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, expression, locale );
					return rv;
				}
				
			});
		}
	}

	class RemoveItemOperation extends org.alice.ide.operations.ActionOperation {
		public RemoveItemOperation() {
			super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "a87ae96c-07fb-4a78-94c3-177ec3d642ce" ) );
			this.setName( "Remove" );
		}
		@Override
		protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
			final int index = ArrayInitializerPane.this.list.getSelectedIndex();
			final edu.cmu.cs.dennisc.alice.ast.Expression expression = ArrayInitializerPane.this.list.getItemAt( index );
			context.commitAndInvokeDo(new org.alice.ide.ToDoEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					if( ArrayInitializerPane.this.arrayExpressions.get( index ) == expression ) {
						ArrayInitializerPane.this.arrayExpressions.remove( index );
					} else {
						throw new javax.swing.undo.CannotRedoException();
					}
				}
				@Override
				public void undo() {
					ArrayInitializerPane.this.arrayExpressions.add( index, expression );
				}
				@Override
				protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
					rv.append( "remove: " );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, expression, locale );
					return rv;
				}
			});
		}
	}
	abstract class AbstractMoveItemOperation extends org.alice.ide.operations.ActionOperation {
		public AbstractMoveItemOperation( java.util.UUID individualId ) {
			super( edu.cmu.cs.dennisc.alice.Project.GROUP, individualId );
		}
		protected abstract int getIndex( int selectedIndex );
		protected abstract int getRedoSelectionIndexDelta();
		protected abstract int getUndoSelectionIndexDelta();
		@Override
		protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
			final int index = this.getIndex( ArrayInitializerPane.this.list.getSelectedIndex() );
			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					ArrayInitializerPane.this.swapWithNext( index );
					list.setSelectedIndex( index + getRedoSelectionIndexDelta() );
				}
				@Override
				public void undo() {
					ArrayInitializerPane.this.swapWithNext( index );
					list.setSelectedIndex( index + getUndoSelectionIndexDelta() );
				}
				@Override
				protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
					rv.append( "swap" );
					return rv;
				}
				
			});
		}
		
	}

	class MoveItemUpOperation extends AbstractMoveItemOperation {
		public MoveItemUpOperation() {
			super( java.util.UUID.fromString( "3fc4c11e-215d-4780-8083-600663d5738e" ) );
			this.setName( "Move Up" );
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
			super( java.util.UUID.fromString( "c308bb2f-e58d-4b12-861f-2767533b56a2" ) );
			this.setName( "Move Down" );
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
		public ItemSelectionOperation( int selectedIndex, edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
			super( java.util.UUID.fromString( "cd4c10ba-3c46-4344-b0df-39a3926e8c9d" ), selectedIndex, expressions );
		}
		@Override
		protected void handleSelectionChange(edu.cmu.cs.dennisc.alice.ast.Expression value) {
			ArrayInitializerPane.this.handleSelectionChange( value );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle undo/redo", this );
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

	class ExpressionList extends edu.cmu.cs.dennisc.croquet.GridBagPanel {
		private edu.cmu.cs.dennisc.croquet.Component< ? > glue = edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue();
		private javax.swing.ButtonGroup group;
		private java.awt.GridBagConstraints gbc;

		public ExpressionList( javax.swing.ComboBoxModel comboBoxModel ) {
			
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: ExpressionList", comboBoxModel );
			this.group = new javax.swing.ButtonGroup();
			this.gbc = new java.awt.GridBagConstraints();
			this.gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
			this.gbc.fill = java.awt.GridBagConstraints.BOTH;
			this.gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			this.gbc.weightx = 1.0;
			this.setBackgroundColor( java.awt.Color.WHITE );
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
				FauxItem fauxItem = (FauxItem)this.getAwtComponent().getComponent( index );
				fauxItem.setSelected( true );
			} else {
				javax.swing.ButtonModel model = this.group.getSelection();
				if( model != null ) {
					model.setSelected( false );
				}
			}
		}
		
		public edu.cmu.cs.dennisc.alice.ast.Expression getItemAt( int index ) {
			return ArrayInitializerPane.this.arrayExpressions.get( index );
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
				FauxItem fauxItem = (FauxItem)this.getAwtComponent().getComponent( i );
				this.group.remove( fauxItem );
			}
			this.removeAllComponents();
			java.util.Enumeration< javax.swing.AbstractButton > e = this.group.getElements();
			while( e.hasMoreElements() ) {
				FauxItem fauxItem = (FauxItem)e.nextElement();
				fauxItem.refresh();
				this.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( fauxItem ), this.gbc );
			}
			this.gbc.weighty = 1.0;
			this.addComponent( this.glue, this.gbc );
			this.gbc.weighty = 0.0;
		}
	}

	private ExpressionList list;

	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions;

	private AddItemOperation addItemOperation = new AddItemOperation();
	private RemoveItemOperation removeItemOperation = new RemoveItemOperation();
	private MoveItemUpOperation moveItemUpOperation = new MoveItemUpOperation();
	private MoveItemDownOperation moveItemDownOperation = new MoveItemDownOperation();

	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;

	public ArrayInitializerPane( edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions ) {
		super( 8, 0 );
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
		this.list = new ExpressionList( comboBoxModel );
		this.updateButtons();

		edu.cmu.cs.dennisc.croquet.GridBagPanel buttonPane = new edu.cmu.cs.dennisc.croquet.GridBagPanel();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		buttonPane.addComponent( this.addItemOperation.createButton(), gbc );
		buttonPane.addComponent( this.removeItemOperation.createButton(), gbc );
		gbc.insets.top = 8;
		buttonPane.addComponent( this.moveItemUpOperation.createButton(), gbc );
		gbc.insets.top = 0;
		buttonPane.addComponent( this.moveItemDownOperation.createButton(), gbc );
		gbc.weighty = 1.0;
		buttonPane.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue(), gbc );

		this.addComponent( buttonPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST );

		edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( this.list );
//		{
//			@Override
//			public java.awt.Dimension getPreferredSize() {
//				return edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumSize( super.getPreferredSize(), 240, 180 );
//			}
//		};
		scrollPane.setBorder( null );
		this.addComponent( scrollPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
	}

	private void updateButtons() {
		boolean isTypeValid = this.type != null && this.type.isArray();
		this.addItemOperation.setEnabled( isTypeValid );
		int index = this.list.getSelectedIndex();
		final int N = this.list.getComponentCount()-1;
		this.removeItemOperation.setEnabled( isTypeValid && index != -1 );
		this.moveItemUpOperation.setEnabled( isTypeValid && index > 0 );
		this.moveItemDownOperation.setEnabled( isTypeValid && index >= 0 && index < N - 1 );
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
