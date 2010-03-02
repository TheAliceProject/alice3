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
package edu.cmu.cs.dennisc.zoot.list;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractEditableListPane<E> extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	class AddItemOperation extends edu.cmu.cs.dennisc.zoot.AbstractActionOperation {
		public AddItemOperation( java.util.UUID groupUUID, String name ) {
			super( groupUUID );
			this.putValue( javax.swing.Action.NAME, name );
		}
		public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			try {
				final E e = createItem();
				final int index = getListSize();
				actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						add( index, e );
					}
					@Override
					public void undo() {
						remove( index, e );
					}
				});
			//todo
			} catch( Exception e ) {
				actionContext.cancel();
			}
		}
	}

	class RemoveItemOperation extends edu.cmu.cs.dennisc.zoot.AbstractActionOperation {
		public RemoveItemOperation( java.util.UUID groupUUID, String name ) {
			super( groupUUID );
			this.putValue( javax.swing.Action.NAME, name );
		}
		public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			final int index = getSelectedIndex();
			if( index >= 0 ) {
				final E e = getItemAt( index );
				actionContext.commitAndInvokeDo(new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						remove( index, e );
					}
					@Override
					public void undo() {
						add( index, e );
					}
				});
			} else {
				throw new AssertionError();
			}
		}
	}

	class EditItemOperation extends edu.cmu.cs.dennisc.zoot.AbstractActionOperation {
		public EditItemOperation( java.util.UUID groupUUID, String name ) {
			super( groupUUID );
			this.putValue( javax.swing.Action.NAME, name );
		}
		public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			final int index = getSelectedIndex();
			if( index >= 0 ) {
				final E e = getItemAt( index );
				edu.cmu.cs.dennisc.zoot.Edit edit = createEditEdit();
				if( edit != null ) {
					actionContext.commitAndInvokeDo( edit );
				} else {
					actionContext.cancel();
				}
			} else {
				throw new AssertionError();
			}
		}
	}

	abstract class AbstractMoveItemOperation extends edu.cmu.cs.dennisc.zoot.AbstractActionOperation {
		public AbstractMoveItemOperation( java.util.UUID groupUUID, String name ) {
			super( groupUUID );
			this.putValue( javax.swing.Action.NAME, name );
		}
		protected abstract int getIndex( int selectedIndex );
		protected abstract int getRedoSelectionIndexDelta();
		protected abstract int getUndoSelectionIndexDelta();
		public final void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
			final int index = this.getIndex( getSelectedIndex() );
			actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
				private void swapWithNext( int index ) {
					if( index >= 0 ) {
						E e0 = getItemAt( index );
						E e1 = getItemAt( index + 1 );
						setItemsAt( index, e1, e0 );
					}
				}
				@Override
				public void doOrRedo( boolean isDo ) {
					swapWithNext( index );
					setSelectedIndex( index + getRedoSelectionIndexDelta() );
				}
				@Override
				public void undo() {
					swapWithNext( index );
					setSelectedIndex( index + getUndoSelectionIndexDelta() );
				}
			});
		}
		
	}

	class MoveItemUpOperation extends AbstractMoveItemOperation {
		public MoveItemUpOperation( java.util.UUID groupUUID, String name ) {
			super( groupUUID, name );
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
		public MoveItemDownOperation( java.util.UUID groupUUID, String name ) {
			super( groupUUID, name );
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

	protected abstract boolean isEnabledAtAll();
	protected abstract edu.cmu.cs.dennisc.zoot.Edit createEditEdit();
	protected abstract E createItem() throws Exception;
	protected abstract void add( int index, E e );
	protected abstract void remove( int index, E e );
	protected abstract E getItemAt( int index );
	protected abstract void setItemsAt( int index, E e0, E e1 );
	protected abstract int getListSize();
	protected abstract int getSelectedIndex();
	protected abstract void setSelectedIndex( int index );
	

	private java.awt.Component component;
	private AddItemOperation addItemOperation;
	private RemoveItemOperation removeItemOperation;
	private EditItemOperation editItemOperation;
	private MoveItemUpOperation moveItemUpOperation;
	private MoveItemDownOperation moveItemDownOperation;

	public AbstractEditableListPane( java.util.UUID groupUUID, java.awt.Component component ) {
		this.component = component;
		this.addItemOperation = new AddItemOperation( groupUUID, this.getAddItemOperationName() );
		this.editItemOperation = new EditItemOperation( groupUUID, this.getEditItemOperationName() );
		this.removeItemOperation = new RemoveItemOperation( groupUUID, this.getRemoveItemOperationName() );
		this.moveItemUpOperation = new MoveItemUpOperation( groupUUID, this.getMoveItemUpOperationName() );
		this.moveItemDownOperation = new MoveItemDownOperation( groupUUID, this.getMoveItemDownOperationName() );
		
		this.setLayout( new java.awt.BorderLayout( 8, 0 ) );

		edu.cmu.cs.dennisc.croquet.swing.GridBagPane buttonPane = new edu.cmu.cs.dennisc.croquet.swing.GridBagPane();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		buttonPane.add( edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.addItemOperation ), gbc );
		buttonPane.add( edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.editItemOperation ), gbc );
		buttonPane.add( edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.removeItemOperation ), gbc );
		gbc.insets.top = 8;
		buttonPane.add( edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.moveItemUpOperation ), gbc );
		gbc.insets.top = 0;
		buttonPane.add( edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.moveItemDownOperation ), gbc );
		gbc.weighty = 1.0;
		buttonPane.add( javax.swing.Box.createGlue(), gbc );

		this.add( buttonPane, java.awt.BorderLayout.EAST );

		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( component );
		scrollPane.setBorder( null );
		this.add( scrollPane, java.awt.BorderLayout.CENTER );
	}

	private edu.cmu.cs.dennisc.awt.event.LenientMouseClickAdapter lenientMouseClickAdapter = new edu.cmu.cs.dennisc.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
			if( quoteClickCountUnquote == 2 ) {
				edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( editItemOperation, e, true );
			}
		}
	};
	@Override
	public void addNotify() {
		this.updateOperationsEnabledStates();
		this.component.addMouseListener( this.lenientMouseClickAdapter );
		this.component.addMouseMotionListener( this.lenientMouseClickAdapter );
		super.addNotify();
	}
	@Override
	public void removeNotify() {
		this.component.removeMouseMotionListener( this.lenientMouseClickAdapter );
		this.component.removeMouseListener( this.lenientMouseClickAdapter );
		super.removeNotify();
	}
	
	protected String getAddItemOperationName() {
		return "Add...";
	}
	protected String getEditItemOperationName() {
		return "Edit...";
	}
	protected String getRemoveItemOperationName() {
		return "Remove";
	}
	protected String getMoveItemUpOperationName() {
		return "Move Up";
	}
	protected String getMoveItemDownOperationName() {
		return "Move Down";
	}

	protected boolean isEditItemEnabled( int index ) {
		return index != -1;
	}
	protected boolean isRemoveItemEnabled( int index ) {
		return index != -1;
	}
	protected void updateOperationsEnabledStates() {
		boolean isEnabledAtAll = this.isEnabledAtAll();
		this.addItemOperation.setEnabled( isEnabledAtAll );
		int index = this.getSelectedIndex();
		final int N = this.getListSize();
		this.editItemOperation.setEnabled( isEnabledAtAll && this.isEditItemEnabled( index ) );
		this.removeItemOperation.setEnabled( isEnabledAtAll && this.isRemoveItemEnabled( index ) );
		this.moveItemUpOperation.setEnabled( isEnabledAtAll && index > 0 );
		this.moveItemDownOperation.setEnabled( isEnabledAtAll && index >= 0 && index < N - 1 );
	}
	
}

public abstract class EditableListPane< E > extends AbstractEditableListPane< E > {
	private javax.swing.DefaultListModel defaultListModel;
	private javax.swing.ListSelectionModel listSelectionModel;
	public EditableListPane( java.util.UUID groupUUID, java.awt.Component component, javax.swing.DefaultListModel defaultListModel, javax.swing.ListSelectionModel listSelectionModel ) {
		super( groupUUID, component );
		this.defaultListModel = defaultListModel;
		this.listSelectionModel = listSelectionModel;
	}
	@Override
	protected int getSelectedIndex() {
		return this.listSelectionModel.getMinSelectionIndex();
	}
	@Override
	protected void setSelectedIndex( int index ) {
		this.listSelectionModel.setSelectionInterval( index, index );
	}
	@Override
	protected E getItemAt( int index ) {
		return (E)this.defaultListModel.getElementAt( index );
	}
	@Override
	protected int getListSize() {
		if( this.defaultListModel != null ) {
			return this.defaultListModel.getSize();
		} else {
			return 0;
		}
	}
	@Override
	protected void add( int index, E e ) {
		this.defaultListModel.add( index, e );
	}
	@Override
	protected void remove( int index, E e ) {
		this.defaultListModel.remove( index );
	}
	
	protected abstract void setValueIsAdjusting( boolean isValueAdjusting );
	@Override
	protected void setItemsAt( int index, E e0, E e1 ) {
		this.setValueIsAdjusting( true );
		this.defaultListModel.setElementAt( e0, index );
		this.setValueIsAdjusting( false );
		this.defaultListModel.setElementAt( e1, index+1 );
	}
	
	private javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			updateOperationsEnabledStates();
		}
	};
	@Override
	public void addNotify() {
		this.listSelectionModel.addListSelectionListener( this.listSelectionListener );
		super.addNotify();
	}
	@Override
	public void removeNotify() {
		super.removeNotify();
		this.listSelectionModel.removeListSelectionListener( this.listSelectionListener );
	}
}
