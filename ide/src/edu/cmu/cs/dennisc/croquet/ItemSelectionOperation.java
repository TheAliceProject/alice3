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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class ItemSelectionOperation<E> extends Operation {
	public static interface ValueObserver<E> {
		public void changed( E nextValue );
	};
	private java.util.List< ValueObserver<E> > valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public void addValueObserver( ValueObserver<E> valueObserver ) {
		this.valueObservers.add( valueObserver );
	}
	public void removeValueObserver( ValueObserver<E> valueObserver ) {
		this.valueObservers.remove( valueObserver );
	}
	private void fireValueChanged( E nextValue ) {
		for( ValueObserver<E> valueObserver : this.valueObservers ) {
			valueObserver.changed( nextValue );
		}
	}

//	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
//	private javax.swing.Action[] actions;
//	private javax.swing.ButtonModel[] buttonModels;
//	private java.awt.event.ItemListener[] itemListeners;
	private javax.swing.DefaultComboBoxModel comboBoxModel;
	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			Application application = Application.getSingleton();
			Context parentContext = application.getCurrentContext();
			Context childContext = parentContext.createChildContext();
			childContext.addChild( new ItemSelectionEvent< E >( childContext, ItemSelectionOperation.this, e ) );
			ItemSelectionOperation.this.perform( childContext, e );
		}
	};
	private javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			if( e.getValueIsAdjusting() ) {
				//pass
			} else {
				Object item;
				int index = e.getFirstIndex();
				if( index >= 0 ) {
					item = ItemSelectionOperation.this.comboBoxModel.getElementAt( index );
				} else {
					item = null;
				}
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "valueChanged", index, item, ItemSelectionOperation.this.comboBoxModel.getSize() );
				ItemSelectionOperation.this.comboBoxModel.setSelectedItem( item );
				
				Application application = Application.getSingleton();
				Context parentContext = application.getCurrentContext();
				Context childContext = parentContext.createChildContext();
				childContext.addChild( new ItemSelectionEvent< E >( childContext, ItemSelectionOperation.this, e ) );
				ItemSelectionOperation.this.perform( childContext, e );
			}
		}
	};
	private E previousSelection;

	private ItemSelectionOperation( java.util.UUID groupUUID, java.util.UUID individualUUID, javax.swing.DefaultComboBoxModel comboBoxModel ) {
		super( groupUUID, individualUUID );
		this.comboBoxModel = comboBoxModel;
//		int N = this.comboBoxModel.getSize();
//		this.actions = new javax.swing.Action[ N ];
//		this.buttonModels = new javax.swing.ButtonModel[ N ];
//		this.itemListeners = new java.awt.event.ItemListener[ N ];
//		this.previousSelection = (E)comboBoxModel.getSelectedItem();
//		for( int i=0; i<N; i++ ) {
//			class Action extends javax.swing.AbstractAction {
//				public Action( int i, E item ) {
//					this.putValue( NAME, getNameFor( i, item ) );
//				}
//				public void actionPerformed( java.awt.event.ActionEvent e ) {
//				}
//			}
//			E item = (E)this.comboBoxModel.getElementAt( i );
//			this.actions[ i ] = new Action( i, item ); 
//			this.buttonModels[ i ] = new javax.swing.JToggleButton.ToggleButtonModel();
//			this.buttonModels[ i ].setGroup( buttonGroup );
//			if( item == this.previousSelection ) {
//				this.buttonModels[ i ].setSelected( true );
//			}
//			this.itemListeners[ i ] = new java.awt.event.ItemListener() {
//				public void itemStateChanged( java.awt.event.ItemEvent e ) {
//					Application application = Application.getSingleton();
//					Context parentContext = application.getCurrentContext();
//					Context childContext = parentContext.createChildContext();
//					childContext.addChild( new ItemSelectionEvent< E >( childContext, ItemSelectionOperation.this, e ) );
//					ItemSelectionOperation.this.perform( childContext, e );
//				}
//			};
//			this.buttonModels[ i ].addItemListener( this.itemListeners[ i ] );
//		}
	}
	public ItemSelectionOperation( java.util.UUID groupUUID, java.util.UUID individualUUID, int selectedIndex, E... items ) {
		this( groupUUID, individualUUID, new javax.swing.DefaultComboBoxModel(items) );
		E item;
		if( selectedIndex >= 0 ) {
			item = items[ selectedIndex ];
		} else {
			item = null;
		}
		this.comboBoxModel.setSelectedItem( item );
	}
	
	
//	public final void performSelectionChange(ItemSelectionContext<T> context) {
//		context.commitAndInvokeDo( new ItemSelectionEdit< T >( this, context.getPreviousSelection(), context.getNextSelection() ) {
//			@Override
//			protected T decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
//				return ItemSelectionOperation.this.decodeValue(binaryDecoder);
//			}
//			@Override
//			protected void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, T value) {
//				ItemSelectionOperation.this.encodeValue(binaryEncoder, value);
//			}
//		} );
//	}

	
//	@Override
//	protected ItemSelectionContext createContext( CompositeContext parentContext, java.util.EventObject e, CancelEffectiveness cancelEffectiveness ) {
//		assert e instanceof java.awt.event.ItemEvent;
//		java.awt.event.ItemEvent itemEvent = (java.awt.event.ItemEvent)e;
//		T nextSelection = (T)this.comboBoxModel.getSelectedItem();
//		ItemSelectionContext rv = new ItemSelectionContext( parentContext, this, e, cancelEffectiveness, this.previousSelection, nextSelection );
//		this.previousSelection = nextSelection;
//		return rv;
//	}

	protected abstract E decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder);
	protected abstract void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, E value);

	/*package-private*/ final void perform( Context context, java.util.EventObject e ) {
		E nextSelection = (E)this.comboBoxModel.getSelectedItem();
		context.commitAndInvokeDo( new ItemSelectionEdit< E >( context, e, this.previousSelection, nextSelection, this ) );
		this.fireValueChanged( nextSelection );
		this.previousSelection = nextSelection;
	}

//	protected String getNameFor( int index, E item ) {
//		if( item != null ) {
//			return item.toString();
//		} else {
//			return "null";
//		}
//	}
//	public javax.swing.KeyStroke getAcceleratorForConfiguringSwing( int index ) {
//		return null;
//	}
//	public javax.swing.Action getActionForConfiguringSwing( int index ) {
//		return this.actions[ index ];
//	}
//	public javax.swing.ButtonModel getButtonModelForConfiguringSwing( int index ) {
//		return this.buttonModels[ index ];
//	}
//	public javax.swing.ComboBoxModel getComboBoxModel() {
//		return this.comboBoxModel;
//	}
//	public void handleKeyPressed( java.awt.event.KeyEvent e ) {
//		int N = this.comboBoxModel.getSize();
//		for( int i=0; i<N; i++ ) {
//			javax.swing.KeyStroke acceleratorI = this.getAcceleratorForConfiguringSwing( i );
//			if( acceleratorI != null ) {
//				if( e.getKeyCode() == acceleratorI.getKeyCode() && e.getModifiersEx() == acceleratorI.getModifiers() ) {
//					this.getButtonModelForConfiguringSwing( i ).setSelected( true );
//				}
//			}
//		}
//	}
	
	public E getValue() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "this.comboBoxModel.getSelectedItem()", this.comboBoxModel.getSelectedItem() );
		return (E)this.comboBoxModel.getSelectedItem();
	}
	
	/*package-private*/ void internalSetValue(E value) {
//		int N = this.buttonModels.length;
//		for( int i=0; i<N; i++ ) {
//			this.buttonModels[ i ].removeItemListener( this.itemListeners[ i ] );
//		}
		this.comboBoxModel.setSelectedItem(value);
//		for( int i=0; i<N; i++ ) {
//			this.buttonModels[ i ].addItemListener( this.itemListeners[ i ] );
//		}
	}
	
	public void setValue( E value ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: ItemSelectionOperation setValue" );
		this.internalSetValue(value);
	}

	public void randomize() {
		final int N = this.comboBoxModel.getSize();
		int i;
		if( N > 0 ) {
			i = org.alice.random.RandomUtilities.nextIntegerFrom0ToNExclusive( N );
		} else {
			i = -1;
		}
		this.comboBoxModel.setSelectedItem( this.comboBoxModel.getElementAt( i ) );
	}
	
	public void setListData( int selectedIndex, E... items ) {
		synchronized( this.comboBoxModel ) {
			this.comboBoxModel.removeAllElements();
			for( E item : items ) {
				this.comboBoxModel.addElement( item );
			}
			this.comboBoxModel.setSelectedItem( null );
		}
	}

	public < L extends ItemSelectable<E,?> > L register( final L rv ) {
		Application.getSingleton().register( this );
		rv.setModel( this.comboBoxModel );
		return rv;
	}
	public ComboBox<E> createComboBox() {
		return register( new ComboBox< E >() {
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo(parent);
				ItemSelectionOperation.this.addComponent( this );
				this.addItemListener( ItemSelectionOperation.this.itemListener );
			};
			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				this.removeItemListener( ItemSelectionOperation.this.itemListener );
				ItemSelectionOperation.this.removeComponent( this );
				super.handleRemovedFrom(parent);
			}
		});
	}
	public List<E> createList() {
		return register( new List< E >() {
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo(parent);
				ItemSelectionOperation.this.addComponent( this );
				this.addListSelectionListener( ItemSelectionOperation.this.listSelectionListener );
			};
			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				this.removeListSelectionListener( ItemSelectionOperation.this.listSelectionListener );
				ItemSelectionOperation.this.removeComponent( this );
				super.handleRemovedFrom(parent);
			}
		} );
	}
}
