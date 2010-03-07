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
public abstract class AbstractEditableListLikeSubstancePane<E> extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
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
					@Override
					protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
						rv.append( "add: " );
						rv.append( e );
						return rv;
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
					@Override
					protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
						rv.append( "remove: " );
						rv.append( e );
						return rv;
					}
				});
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
				@Override
				protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
					rv.append( "swap items" );
					return rv;
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

	protected abstract edu.cmu.cs.dennisc.zoot.ActionOperation createEditOperation( java.util.UUID groupUUID );
	protected abstract E createItem() throws Exception;
	protected abstract void add( int index, E e );
	protected abstract void remove( int index, E e );
	protected abstract E getItemAt( int index );
	protected abstract void setItemsAt( int index, E e0, E e1 );
	protected abstract int getListSize();
	protected abstract int getSelectedIndex();
	protected abstract void setSelectedIndex( int index );
	

	private java.awt.Component listLikeSubstance;
	private AddItemOperation addItemOperation;
	private RemoveItemOperation removeItemOperation;
	private edu.cmu.cs.dennisc.zoot.ActionOperation editItemOperation;
	private MoveItemUpOperation moveItemUpOperation;
	private MoveItemDownOperation moveItemDownOperation;

	public AbstractEditableListLikeSubstancePane( java.util.UUID groupUUID, java.awt.Component listLikeSubstance ) {
		this.listLikeSubstance = listLikeSubstance;
		this.addItemOperation = new AddItemOperation( groupUUID, this.getAddItemOperationName() );
		this.editItemOperation = this.createEditOperation( groupUUID );
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

		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.listLikeSubstance );
		scrollPane.setBorder( null );
		this.add( scrollPane, java.awt.BorderLayout.CENTER );
	}

	public java.awt.Component getListLikeSubstance() {
		return this.listLikeSubstance;
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
		this.listLikeSubstance.addMouseListener( this.lenientMouseClickAdapter );
		this.listLikeSubstance.addMouseMotionListener( this.lenientMouseClickAdapter );
		super.addNotify();
	}
	@Override
	public void removeNotify() {
		this.listLikeSubstance.removeMouseMotionListener( this.lenientMouseClickAdapter );
		this.listLikeSubstance.removeMouseListener( this.lenientMouseClickAdapter );
		super.removeNotify();
	}
	
	protected String getAddItemOperationName() {
		return "Add...";
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

	protected abstract boolean isEditItemEnabled( int index );
	protected abstract boolean isRemoveItemEnabled( int index );
	protected void updateOperationsEnabledStates() {
		boolean isEnabledAtAll = true;
		this.addItemOperation.setEnabled( isEnabledAtAll );
		int index = this.getSelectedIndex();
		final int N = this.getListSize();
		this.editItemOperation.setEnabled( isEnabledAtAll && index != -1 && this.isEditItemEnabled( index ) );
		this.removeItemOperation.setEnabled( isEnabledAtAll && index != -1 && this.isRemoveItemEnabled( index ) );
		this.moveItemUpOperation.setEnabled( isEnabledAtAll && index > 0 );
		this.moveItemDownOperation.setEnabled( isEnabledAtAll && index >= 0 && index < N - 1 );
	}
	
}
