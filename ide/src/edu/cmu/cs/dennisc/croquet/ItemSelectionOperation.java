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
public abstract class ItemSelectionOperation<T> extends Operation {
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private javax.swing.Action[] actions;
	private javax.swing.ButtonModel[] buttonModels;
	private java.awt.event.ItemListener[] itemListeners;
	private javax.swing.ComboBoxModel comboBoxModel;
	
	private T previousSelection;

	public ItemSelectionOperation( java.util.UUID groupUUID, java.util.UUID individualUUID, javax.swing.ComboBoxModel comboBoxModel ) {
		super( groupUUID, individualUUID );
		this.comboBoxModel = comboBoxModel;
		int N = this.comboBoxModel.getSize();
		this.actions = new javax.swing.Action[ N ];
		this.buttonModels = new javax.swing.ButtonModel[ N ];
		this.itemListeners = new java.awt.event.ItemListener[ N ];
		this.previousSelection = (T)comboBoxModel.getSelectedItem();
		for( int i=0; i<N; i++ ) {
			class Action extends javax.swing.AbstractAction {
				public Action( int i, T item ) {
					this.putValue( NAME, getNameFor( i, item ) );
				}
				public void actionPerformed( java.awt.event.ActionEvent e ) {
				}
			}
			T item = (T)this.comboBoxModel.getElementAt( i );
			this.actions[ i ] = new Action( i, item ); 
			this.buttonModels[ i ] = new javax.swing.JToggleButton.ToggleButtonModel();
			this.buttonModels[ i ].setGroup( buttonGroup );
			if( item == this.previousSelection ) {
				this.buttonModels[ i ].setSelected( true );
			}
			this.itemListeners[ i ] = new java.awt.event.ItemListener() {
				public void itemStateChanged( java.awt.event.ItemEvent e ) {
					Application application = Application.getSingleton();
					Context parentContext = application.getCurrentContext();
					Context childContext = parentContext.open();
					childContext.handleItemStateChanged( ItemSelectionOperation.this, e );
					childContext.closeIfNotPending();
				}
			};
			this.buttonModels[ i ].addItemListener( this.itemListeners[ i ] );
		}
	}

	protected abstract ItemSelectionEdit< T > createItemSelectionEdit( Context context, java.awt.event.ItemEvent e, T previousSelection, T nextSelection );
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

	/*package-private*/ final void perform( Context context, java.awt.event.ItemEvent e ) {
		T nextSelection = (T)this.comboBoxModel.getSelectedItem();
		context.commitAndInvokeDo( this.createItemSelectionEdit( context, e, this.previousSelection, nextSelection ) );
		this.previousSelection = nextSelection;
	}

	protected String getNameFor( int index, T item ) {
		if( item != null ) {
			return item.toString();
		} else {
			return "null";
		}
	}
	public javax.swing.KeyStroke getAcceleratorForConfiguringSwing( int index ) {
		return null;
	}
	public javax.swing.Action getActionForConfiguringSwing( int index ) {
		return this.actions[ index ];
	}
	public javax.swing.ButtonModel getButtonModelForConfiguringSwing( int index ) {
		return this.buttonModels[ index ];
	}
	public javax.swing.ComboBoxModel getComboBoxModel() {
		return this.comboBoxModel;
	}
	public void handleKeyPressed( java.awt.event.KeyEvent e ) {
		int N = this.comboBoxModel.getSize();
		for( int i=0; i<N; i++ ) {
			javax.swing.KeyStroke acceleratorI = this.getAcceleratorForConfiguringSwing( i );
			if( acceleratorI != null ) {
				if( e.getKeyCode() == acceleratorI.getKeyCode() && e.getModifiersEx() == acceleratorI.getModifiers() ) {
					this.getButtonModelForConfiguringSwing( i ).setSelected( true );
				}
			}
		}
	}
	/*package-private*/ void setValue(T value) {
		int N = this.buttonModels.length;
		for( int i=0; i<N; i++ ) {
			this.buttonModels[ i ].removeItemListener( this.itemListeners[ i ] );
		}
		this.comboBoxModel.setSelectedItem(value);
		for( int i=0; i<N; i++ ) {
			this.buttonModels[ i ].addItemListener( this.itemListeners[ i ] );
		}
	}
}
