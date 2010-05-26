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

import edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver;

/**
 * @author Dennis Cosgrove
 */
public class TabSelectionOperation extends Operation {
	public interface SelectionObserver {
		public void selected( TabStateOperation next );
	}
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private final java.util.Map<javax.swing.ButtonModel, TabStateOperation> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
				java.awt.ItemSelectable itemSelectable = e.getItemSelectable();
				TabStateOperation tabStateOperation;
				if( itemSelectable instanceof javax.swing.ButtonModel ) {
					tabStateOperation = map.get( (javax.swing.ButtonModel)itemSelectable );
					assert tabStateOperation != null;
				} else {
					assert false;
					tabStateOperation = null;
				}
				TabSelectionOperation.this.selectTab( tabStateOperation );
			} else {
				//pass
			}
		}
	};
	private void addTab( TabStateOperation tabStateOperation ) {
		AbstractButton<?> tabTitle = tabStateOperation.getSingletonTabTitle( this.singletonSingleSelectionPane );
		this.map.put( tabTitle.getAwtComponent().getModel(), tabStateOperation);
		this.buttonGroup.add(tabTitle.getAwtComponent());
		tabTitle.getAwtComponent().getModel().addItemListener( this.itemListener );
		this.singletonSingleSelectionPane.addTab( tabStateOperation );
	}

	private void removeTab( TabStateOperation tabStateOperation ) {
		AbstractButton<?> tabTitle = tabStateOperation.getSingletonTabTitle( this.singletonSingleSelectionPane );
		tabTitle.getAwtComponent().getModel().removeItemListener( this.itemListener );
		this.buttonGroup.remove(tabTitle.getAwtComponent());
		this.map.remove( tabTitle.getAwtComponent().getModel() );
		this.singletonSingleSelectionPane.removeTab( tabStateOperation );
	}
	
	private void selectTab( TabStateOperation tabStateOperation ) {
		this.singletonSingleSelectionPane.selectTab( tabStateOperation );
		for( SelectionObserver selectionObserver : this.selectionObservers ) {
			selectionObserver.selected( tabStateOperation );
		}
	}

	private final java.util.List< SelectionObserver > selectionObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private final java.util.List< TabStateOperation > tabStateOperations;
	public TabSelectionOperation( Group group, java.util.UUID individualUUID, TabStateOperation... operations ) {
		super( group, individualUUID );
		this.tabStateOperations = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList( operations );
	}
	public void addTabStateOperation( TabStateOperation operation ) {
		this.tabStateOperations.add( operation );
		if( this.singletonSingleSelectionPane != null ) {
			this.addToTabbedPane( operation );
		}
	}
	public void removeStateOperation( TabStateOperation operation ) {
		if( this.singletonSingleSelectionPane != null ) {
			this.removeFromTabbedPane( operation );
		}
		this.tabStateOperations.remove( operation );
	}
	public Iterable< TabStateOperation > getTabStateOperations() {
		return this.tabStateOperations;
	}
	public void removeAllTabStateOperations() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: removeAllTabStateOperations" );
		this.tabStateOperations.clear();
	}
	public TabStateOperation getCurrentTabStateOperation() {
		javax.swing.ButtonModel model = this.buttonGroup.getSelection();
		if( model != null ) {
			return this.map.get( model );
		} else {
			return null;
		}
	}
//	public void setCurrentTabStateOperation( TabStateOperation operation ) {
//		operation.setValue( true );
//	}
	
	public void addSelectionObserver( SelectionObserver selectionObserver ) { 
		this.selectionObservers.add( selectionObserver );
	}
	public void addAndInvokeSelectionObserver( SelectionObserver selectionObserver ) {
		this.addSelectionObserver( selectionObserver );
		selectionObserver.selected( this.getCurrentTabStateOperation() );
	}
	public void removeSelectionObserver( SelectionObserver selectionObserver ) { 
		this.selectionObservers.remove( selectionObserver );
	}
	
	private void addToTabbedPane( TabStateOperation tabState ) {
		ScrollPane scrollPane = tabState.getSingletonScrollPane();
		Component<?> mainComponent = tabState.getSingletonView();
		AbstractButton<?> titleButton = tabState.getSingletonTabTitle( this.singletonSingleSelectionPane );
		titleButton.setBackgroundColor( mainComponent.getBackgroundColor() );
		scrollPane.setBackgroundColor( mainComponent.getBackgroundColor() );
		this.addTab( tabState );
		titleButton.setFont( this.singletonSingleSelectionPane.getFont() );
		if( tabState.getState() ) {
			this.selectTab( tabState );
		}
//		tabState.addValueObserver( new TabIsSelectedObserver( tabState, key) );
	}
	private void removeFromTabbedPane( TabStateOperation tabState ) {
		throw new RuntimeException( "todo" );
	}

	private AbstractTabbedPane singletonSingleSelectionPane;
	public AbstractTabbedPane getSingletonSingleSelectionPane() {
		assert this.singletonSingleSelectionPane != null;
		return this.singletonSingleSelectionPane;
	}
	private void setSingletonSingleSelectionPane( AbstractTabbedPane singleSelectionPane ) {
		assert this.singletonSingleSelectionPane == null;
		this.singletonSingleSelectionPane = singleSelectionPane;
		this.addComponent( this.singletonSingleSelectionPane );
		for( TabStateOperation tabState : this.tabStateOperations ) {
			this.addToTabbedPane(tabState);
		}
	}
	public FolderTabbedPane createFolderTabbedPane() {
		FolderTabbedPane rv = new FolderTabbedPane();
		this.setSingletonSingleSelectionPane(rv);
		return rv;
	}
	public ToolPaletteTabbedPane createToolPaletteTabbedPane() {
		ToolPaletteTabbedPane rv = new ToolPaletteTabbedPane();
		this.setSingletonSingleSelectionPane(rv);
		return rv;
	}
}
