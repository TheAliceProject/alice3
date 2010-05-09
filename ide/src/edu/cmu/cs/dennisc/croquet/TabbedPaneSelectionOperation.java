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
public class TabbedPaneSelectionOperation extends Operation {
	public interface SelectionObserver {
		public void selecting( TabIsSelectedStateOperation prev, TabIsSelectedStateOperation next );
		public void selected( TabIsSelectedStateOperation prev, TabIsSelectedStateOperation next );
	}
	private class TabIsSelectedObserver implements BooleanStateOperation.ValueObserver {
		private TabbedPane tabbedPane;
		private edu.cmu.cs.dennisc.croquet.TabbedPane.Key key;
		public TabIsSelectedObserver( TabbedPane tabbedPane, edu.cmu.cs.dennisc.croquet.TabbedPane.Key key ) {
			this.tabbedPane = tabbedPane;
			this.key = key;
		}
		public void changing(boolean nextValue) {
		}
		public void changed(boolean nextValue) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( nextValue );
			if( nextValue ) {
				this.tabbedPane.selectTab( this.key );
			}
		}
	};

	private final java.util.List< SelectionObserver > selectionObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private final java.util.List< TabIsSelectedStateOperation > tabIsSelectedStateOperations;
	private TabIsSelectedStateOperation currentSelection;
	private float headerFontScalar = 1.0f;
	
	public TabbedPaneSelectionOperation( java.util.UUID groupUUID, java.util.UUID individualUUID, TabIsSelectedStateOperation... operations ) {
		super( groupUUID, individualUUID );
		this.tabIsSelectedStateOperations = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList( operations );
	}
	public void addTabIsSelectedStateOperation( TabIsSelectedStateOperation operation ) {
		this.tabIsSelectedStateOperations.add( operation );
	}
	public void removeTabIsSelectedStateOperation( TabIsSelectedStateOperation operation ) {
		this.tabIsSelectedStateOperations.remove( operation );
	}
	public Iterable< TabIsSelectedStateOperation > getTabIsSelectedStateOperations() {
		return this.tabIsSelectedStateOperations;
	}
	public void removeAllTabIsSelectedStateOperations() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: removeAllTabIsSelectedOperations" );
		this.tabIsSelectedStateOperations.clear();
	}
	
	protected void setHeaderFontScalar( float headerFontScalar ) {
		this.headerFontScalar = headerFontScalar;
	}
	
	public TabIsSelectedStateOperation getCurrentSelectedTabOperation() {
		return this.currentSelection;
	}
	public void setCurrentSelectedTabOperation( TabIsSelectedStateOperation tabIsSelectedOperation ) {
		this.currentSelection = tabIsSelectedOperation;
		this.currentSelection.setValue( true );
	}
	
	public void addSelectionObserver( SelectionObserver selectionObserver ) { 
		this.selectionObservers.add( selectionObserver );
	}
	public void removeSelectionObserver( SelectionObserver selectionObserver ) { 
		this.selectionObservers.remove( selectionObserver );
	}

	private TabbedPane singletonTabbedPane;
	public TabbedPane getSingletonTabbedPane() {
		if( this.singletonTabbedPane != null ) {
			//pass
		} else {
			this.singletonTabbedPane = new TabbedPane();
			for( TabIsSelectedStateOperation tabIsSelectedOperation : this.tabIsSelectedStateOperations ) {
				ScrollPane scrollPane = tabIsSelectedOperation.getSingletonScrollPane();
				Component<?> mainComponent = tabIsSelectedOperation.getSingletonView();
				AbstractButton<?> header = tabIsSelectedOperation.createTabTitle();
				header.scaleFont( this.headerFontScalar );
				header.setBackgroundColor( mainComponent.getBackgroundColor() );
				scrollPane.setBackgroundColor( mainComponent.getBackgroundColor() );
				TabbedPane.Key key = this.singletonTabbedPane.createKey(header, scrollPane, tabIsSelectedOperation.getIndividualUUID().toString() );
				this.singletonTabbedPane.addTab( key );
				if( tabIsSelectedOperation.getState() ) {
					this.singletonTabbedPane.selectTab( key );
				}
				tabIsSelectedOperation.addValueObserver( new TabIsSelectedObserver(this.singletonTabbedPane, key) );
			}
		}
		return this.singletonTabbedPane;
	}
}
