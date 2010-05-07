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
package org.alice.ide.editorstabbedpane;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractEditableListLikeSubstancePane<E> extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	class AddItemOperation extends edu.cmu.cs.dennisc.croquet.AbstractActionOperation {
		public AddItemOperation( java.util.UUID groupUUID, String name ) {
			super( groupUUID, java.util.UUID.fromString( "118ab33a-8c1a-4207-80d1-88edd555ca61" ) );
			this.setName( name );
		}
		@Override
		protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton< ? > button ) {
			try {
				final E item = createItem();
				final int index = getListSize();
				context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						add( index, item );
					}
					@Override
					public void undo() {
						remove( index, item );
					}
					@Override
					protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
						rv.append( "add: " );
						rv.append( item );
						return rv;
					}
				});
			} catch( Exception exception ) {
				context.todo();
			}
		}
	}

	class RemoveItemOperation extends edu.cmu.cs.dennisc.croquet.AbstractActionOperation {
		public RemoveItemOperation( java.util.UUID groupUUID, String name ) {
			super( groupUUID, java.util.UUID.fromString( "230c56bc-6735-486c-a6f7-0451ee3714e7" ) );
			this.setName( name );
		}
		@Override
		protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton< ? > button ) {
			final int index = getSelectedIndex();
			if( index >= 0 ) {
				final E item = getItemAt( index );
				context.commitAndInvokeDo(new org.alice.ide.ToDoEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						remove( index, item );
					}
					@Override
					public void undo() {
						add( index, item );
					}
					@Override
					protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
						rv.append( "remove: " );
						rv.append( item );
						return rv;
					}
				});
			} else {
				throw new AssertionError();
			}
		}
	}

	abstract class AbstractMoveItemOperation extends edu.cmu.cs.dennisc.croquet.AbstractActionOperation {
		public AbstractMoveItemOperation( java.util.UUID groupId, java.util.UUID individualId, String name ) {
			super( groupId, individualId );
			this.setName( name );
		}
		protected abstract int getIndex( int selectedIndex );
		protected abstract int getRedoSelectionIndexDelta();
		protected abstract int getUndoSelectionIndexDelta();
		@Override
		protected final void perform( edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton< ? > button ) {
			final int index = this.getIndex( getSelectedIndex() );
			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
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
			super( groupUUID, java.util.UUID.fromString( "d0174767-da0b-42ed-9174-1c3a1e6ed09d" ), name );
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
			super( groupUUID, java.util.UUID.fromString( "2443e57e-f8ff-4a80-8fcb-9d8bf72f13d7" ), name );
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

	protected abstract edu.cmu.cs.dennisc.croquet.AbstractActionOperation createEditOperation( java.util.UUID groupUUID );
	protected abstract E createItem() throws Exception;
	protected abstract void add( int index, E e );
	protected abstract void remove( int index, E e );
	protected abstract E getItemAt( int index );
	protected abstract void setItemsAt( int index, E e0, E e1 );
	protected abstract int getListSize();
	protected abstract int getSelectedIndex();
	protected abstract void setSelectedIndex( int index );
	

	private edu.cmu.cs.dennisc.croquet.Component< ? > listLikeSubstance;
	private AddItemOperation addItemOperation;
	private RemoveItemOperation removeItemOperation;
	private edu.cmu.cs.dennisc.croquet.AbstractActionOperation editItemOperation;
	private MoveItemUpOperation moveItemUpOperation;
	private MoveItemDownOperation moveItemDownOperation;

	public AbstractEditableListLikeSubstancePane( java.util.UUID groupUUID, edu.cmu.cs.dennisc.croquet.Component< ? > listLikeSubstance ) {
		super( 8, 0 );
		this.listLikeSubstance = listLikeSubstance;
		this.addItemOperation = new AddItemOperation( groupUUID, this.getAddItemOperationName() );
		this.editItemOperation = this.createEditOperation( groupUUID );
		this.removeItemOperation = new RemoveItemOperation( groupUUID, this.getRemoveItemOperationName() );
		this.moveItemUpOperation = new MoveItemUpOperation( groupUUID, this.getMoveItemUpOperationName() );
		this.moveItemDownOperation = new MoveItemDownOperation( groupUUID, this.getMoveItemDownOperationName() );
		

		edu.cmu.cs.dennisc.croquet.GridBagPanel buttonPane = new edu.cmu.cs.dennisc.croquet.GridBagPanel();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		buttonPane.addComponent( edu.cmu.cs.dennisc.croquet.Application.getSingleton().createButton( this.addItemOperation ), gbc );
		buttonPane.addComponent( edu.cmu.cs.dennisc.croquet.Application.getSingleton().createButton( this.editItemOperation ), gbc );
		buttonPane.addComponent( edu.cmu.cs.dennisc.croquet.Application.getSingleton().createButton( this.removeItemOperation ), gbc );
		gbc.insets.top = 8;
		buttonPane.addComponent( edu.cmu.cs.dennisc.croquet.Application.getSingleton().createButton( this.moveItemUpOperation ), gbc );
		gbc.insets.top = 0;
		buttonPane.addComponent( edu.cmu.cs.dennisc.croquet.Application.getSingleton().createButton( this.moveItemDownOperation ), gbc );
		gbc.weighty = 1.0;
		buttonPane.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue(), gbc );

		this.addComponent( buttonPane, CardinalDirection.EAST );

		edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( this.listLikeSubstance );
		scrollPane.setBorder( null );
		this.addComponent( scrollPane, CardinalDirection.CENTER );
	}

	public edu.cmu.cs.dennisc.croquet.Component< ? > getListLikeSubstance() {
		return this.listLikeSubstance;
	}
	
	private edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter lenientMouseClickAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
			if( quoteClickCountUnquote == 2 ) {
				editItemOperation.fire();
			}
		}
	};
	
	@Override
	protected void adding() {
		super.adding();
		this.updateOperationsEnabledStates();
		this.listLikeSubstance.addMouseListener( this.lenientMouseClickAdapter );
		this.listLikeSubstance.addMouseMotionListener( this.lenientMouseClickAdapter );
	}
	
	@Override
	protected void removed() {
		this.listLikeSubstance.removeMouseMotionListener( this.lenientMouseClickAdapter );
		this.listLikeSubstance.removeMouseListener( this.lenientMouseClickAdapter );
		super.removed();
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
