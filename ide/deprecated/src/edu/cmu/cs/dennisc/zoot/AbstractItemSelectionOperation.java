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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractItemSelectionOperation<E> extends AbstractOperation implements ItemSelectionOperation< E > {
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private javax.swing.Action[] actions;
	private javax.swing.ButtonModel[] buttonModels;
	private javax.swing.ComboBoxModel comboBoxModel;
	public AbstractItemSelectionOperation( java.util.UUID groupUUID, javax.swing.ComboBoxModel comboBoxModel ) {
		super( groupUUID );
		this.comboBoxModel = comboBoxModel;
		int N = this.comboBoxModel.getSize();
		this.actions = new javax.swing.Action[ N ];
		this.buttonModels = new javax.swing.ButtonModel[ N ];
		E selectedItem = (E)comboBoxModel.getSelectedItem();
		for( int i=0; i<N; i++ ) {
			class Action extends javax.swing.AbstractAction {
				public Action( int i, E item ) {
					this.putValue( NAME, getNameFor( i, item ) );
				}
				public void actionPerformed( java.awt.event.ActionEvent e ) {
				}
			}
			final E item = (E)this.comboBoxModel.getElementAt( i );
			this.actions[ i ] = new Action( i, item ); 
			this.buttonModels[ i ] = new javax.swing.JToggleButton.ToggleButtonModel();
			this.buttonModels[ i ].setGroup( buttonGroup );
			if( item == selectedItem ) {
				this.buttonModels[ i ].setSelected( true );
			}
			this.buttonModels[ i ].addItemListener( new java.awt.event.ItemListener() {
				public void itemStateChanged( java.awt.event.ItemEvent e ) {
					if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
						ZManager.performIfAppropriate( AbstractItemSelectionOperation.this, e, ZManager.CANCEL_IS_FUTILE, null, item );
					}
				}
			} );
		}
	}
	
	protected String getNameFor( int index, E item ) {
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

	protected abstract void handleSelectionChange( E value );
	
	public final void performSelectionChange(edu.cmu.cs.dennisc.zoot.ItemSelectionContext<E> context) {
		class Edit extends AbstractEdit {
			private E prevValue;
			private E nextValue;
			public Edit( E prevValue, E nextValue ) {
				this.prevValue = prevValue;
				this.nextValue = nextValue;
			}
			@Override
			public void doOrRedo( boolean isDo ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: replace w/ listeners" );
				AbstractItemSelectionOperation.this.comboBoxModel.setSelectedItem( this.nextValue );
				AbstractItemSelectionOperation.this.handleSelectionChange( this.nextValue );
			}
			@Override
			public void undo() {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: replace w/ listeners" );
				AbstractItemSelectionOperation.this.comboBoxModel.setSelectedItem( this.prevValue );
				AbstractItemSelectionOperation.this.handleSelectionChange( this.prevValue );
			}
			@Override
			protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
				rv.append( "select " );
				rv.append( this.prevValue );
				rv.append( " ===> " );
				rv.append( this.nextValue );
				return rv;
			}

		}
		
		context.commitAndInvokeDo( new Edit( context.getPreviousSelection(), context.getNextSelection() ) );
	}
}
