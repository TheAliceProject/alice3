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
public abstract class AbstractRadioButtons< E > extends Panel {
	private class RadioButton {
		private AbstractButton<?> button;
		private E item;
		private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				AbstractRadioButtons.this.model.setSelectedItem( RadioButton.this.item );
				AbstractRadioButtons.this.fireItemStateChanged( e );
			}
		};
		public RadioButton( AbstractButton<?> button, E item ) {
			this.button = button;
			this.item = item;
		}
		
		public void add() {
			this.button.getAwtComponent().addItemListener( this.itemListener );
			AbstractRadioButtons.this.buttonGroup.add( this.button.getAwtComponent() );
			AbstractRadioButtons.this.addButton( this.button );
		}
		public void remove() {
			//note: should already be removed by removeAllComponents()
			assert this.button.getParent() == null;
			this.button.getAwtComponent().removeItemListener( this.itemListener );
			AbstractRadioButtons.this.buttonGroup.remove( this.button.getAwtComponent() );
		}
		public void setSelected( boolean isSelected ) {
			if( this.button.getAwtComponent().isSelected() != isSelected ) {
				this.button.getAwtComponent().setSelected( isSelected );
			}
		}
	}
	
	private java.util.Map<E, RadioButton > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private javax.swing.ComboBoxModel model;
	private javax.swing.ListSelectionModel listSelectionModel;
	private javax.swing.event.ListDataListener listDataListener = new javax.swing.event.ListDataListener() {
		public void intervalAdded(javax.swing.event.ListDataEvent e) {
			AbstractRadioButtons.this.handleListDataChanged();
		}
		public void intervalRemoved(javax.swing.event.ListDataEvent e) {
			AbstractRadioButtons.this.handleListDataChanged();
		}
		public void contentsChanged(javax.swing.event.ListDataEvent e) {
			AbstractRadioButtons.this.handleListDataChanged();
		}
	};
	private javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			AbstractRadioButtons.this.handleListSelectionChanged();
		}
	};
	private void fireItemStateChanged( java.awt.event.ItemEvent e ) {
		for( java.awt.event.ItemListener itemListener : this.itemListeners ) {
			itemListener.itemStateChanged(e);
		}
	}

	public AbstractRadioButtons() {
	}
	
	protected abstract AbstractButton<?> createButton( E item );
	protected abstract void addPrologue( int count );
	protected abstract void addButton( AbstractButton<?> button );
	protected abstract void addEpilogue();
	
	private void handleListDataChanged() {
		synchronized( this.model ) {
			final int N = this.model.getSize();
			this.removeAllComponents();
			this.addPrologue( N );
			for( int i=0; i<N; i++ ) {
				E item = (E)this.model.getElementAt( i );
				RadioButton radioButton = this.map.get( item );
				if( radioButton != null ) {
					//pass
				} else {
					radioButton = new RadioButton( this.createButton( item ), item );
					this.map.put( item, radioButton );
				}
				radioButton.add();
			}
			this.addEpilogue();
		}
		this.revalidateAndRepaint();
	}
	private void handleListSelectionChanged() {
		E item = (E)this.model.getSelectedItem();
		if( item != null ) {
			RadioButton radioButton = this.map.get( item );
			assert radioButton != null;
			radioButton.setSelected( true );
		} else {
			javax.swing.ButtonModel model = this.buttonGroup.getSelection();
			if( model != null ) {
				this.buttonGroup.setSelected(model, false);
			}
		}
	}
	private java.util.List<java.awt.event.ItemListener> itemListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList(); 
	/*package-private*/ void setModel( javax.swing.ComboBoxModel model ) {
		if( this.model != null ) {
			synchronized( this.model ) {
				this.model.removeListDataListener( this.listDataListener );
			}
		}
		this.model = model;
		this.handleListDataChanged();
		if( this.model != null ) {
			synchronized( this.model ) {
				this.model.addListDataListener( this.listDataListener );
			}
		}
	}
	/*package-private*/ void setSelectionModel( javax.swing.ListSelectionModel listSelectionModel ) {
		if( this.listSelectionModel != null ) {
			synchronized( this.listSelectionModel ) {
				this.listSelectionModel.removeListSelectionListener( this.listSelectionListener );
			}
		}
		this.listSelectionModel = listSelectionModel;
		this.handleListSelectionChanged();
		if( this.listSelectionModel != null ) {
			synchronized( this.listSelectionModel ) {
				this.listSelectionModel.addListSelectionListener( this.listSelectionListener );
			}
		}
	}
	/*package-private*/ void addItemListener(java.awt.event.ItemListener itemListener) {
		this.itemListeners.add( itemListener );
	}
	/*package-private*/ void removeItemListener(java.awt.event.ItemListener itemListener) {
		this.itemListeners.remove( itemListener );
	}
}
