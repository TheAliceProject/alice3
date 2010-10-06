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
public abstract class AbstractTabbedPane<E,D extends AbstractTabbedPane.TabItemDetails> extends ItemSelectablePanel<E, D> {
	class TabItemDetails extends ItemDetails {
		private java.util.UUID id;
		private JComponent<?> mainComponent;
		private ScrollPane scrollPane;
		public TabItemDetails( E item, AbstractButton< ?,BooleanState > titleButton, java.util.UUID id, ScrollPane scrollPane, JComponent<?> mainComponent ) {
			super( item, titleButton );
			assert id != null;
			this.id = id;
			this.mainComponent = mainComponent;
			this.scrollPane = scrollPane;
			titleButton.setBackgroundColor( this.mainComponent.getBackgroundColor() );
			if( this.scrollPane != null ) {
				this.scrollPane.setViewportView( this.mainComponent );
			}
		}
		public java.util.UUID getId() {
			return this.id;
		}
		public JComponent< ? > getMainComponent() {
			return this.mainComponent;
		}
		public ScrollPane getScrollPane() {
			return this.scrollPane;
		}
		public JComponent< ? > getRootComponent() {
			if( this.scrollPane != null ) {
				return this.scrollPane;
			} else {
				return this.mainComponent;
			}
		}
	}

	private ListSelectionState.TabCreator< E > tabCreator;
	@Override
	public void setFont(java.awt.Font font) {
		super.setFont( font );
		for( D tabItemDetails : this.getAllItemDetails() ) {
			tabItemDetails.getButton().setFont( font );
		}
	}

	protected abstract AbstractButton< ?, BooleanState > createTitleButton( BooleanState booleanState, java.awt.event.ActionListener closeButtonActionListener );
	protected abstract D createTabItemDetails( E item, java.util.UUID id, AbstractButton< ?, BooleanState > titleButton, ScrollPane scrollPane, JComponent<?> mainComponent );
	@Override
	protected final D createItemDetails( final E item ) {
		java.util.UUID id = this.tabCreator.getId( item );
		assert id != null : item;
		java.awt.event.ActionListener closeButtonActionListener;
		if( this.tabCreator.isCloseable( item ) ) {
			closeButtonActionListener = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					AbstractTabbedPane.this.getModel().removeItem( item );
				}
			};
		} else {
			closeButtonActionListener = null;
		}
		BooleanState booleanState = new BooleanState( Application.INHERIT_GROUP, java.util.UUID.fromString( "a6ed465d-39f4-4604-a5d0-e6c9463606b0" ), false );
		AbstractButton< ?, BooleanState > titleButton = this.createTitleButton( booleanState, closeButtonActionListener );
		this.tabCreator.customizeTitleComponent( booleanState, titleButton, item );
		return createTabItemDetails( item, id, titleButton, this.tabCreator.createScrollPane( item ), this.tabCreator.createMainComponent( item ) );
	}
	
	public AbstractTabbedPane( ListSelectionState<E> model, ListSelectionState.TabCreator< E > tabCreator ) {
		super( model );
		this.tabCreator = tabCreator;
	}
	
	/*package-private*/ JComponent< ? > getMainComponentFor( E item ) {
		TabItemDetails tabItemDetails = this.getItemDetails( item );
		if( tabItemDetails != null ) {
			return tabItemDetails.getMainComponent();
		} else {
			return null;
		}
	}
	/*package-private*/ ScrollPane getScrollPaneFor( E item ) {
		TabItemDetails tabItemDetails = this.getItemDetails( item );
		if( tabItemDetails != null ) {
			return tabItemDetails.getScrollPane();
		} else {
			return null;
		}
	}
	/*package-private*/ JComponent< ? > getRootComponentFor( E item ) {
		TabItemDetails tabItemDetails = this.getItemDetails( item );
		if( tabItemDetails != null ) {
			return tabItemDetails.getRootComponent();
		} else {
			return null;
		}
	}
	
		
//	protected abstract Tab<E> createTab( E item, ItemSelectionOperation.TabCreator< E > tabCreator );
//	protected abstract void addTab( Tab<E> tab );
//	protected abstract void removeTab( Tab<E> tab );
//	@Override
//	protected AbstractButton<?> createButton(E item) {
//		Tab< E > tab = createTab( item, this.tabCreator );
//		AbstractButton<?> rv = tab.getOuterTitleComponent();
//		map.put(rv, tab);
//		return rv;
//	}
//	@Override
//	protected void removeAllButtons() {
//		for( Tab<E> tab : this.map.values() ) {
//			this.removeTab( tab );
//		}
//	}
//	@Override
//	protected void addPrologue( int count ) {
//	}
//	@Override
//	protected final void addButton(edu.cmu.cs.dennisc.croquet.AbstractButton<?> button) {
//		Tab< E > tab = this.map.get( button );
//		assert tab != null;
//		this.addTab( tab );
//	}
//	@Override
//	protected void addEpilogue() {
//	}
//	
//	@Override
//	protected void handleItemSelected(E item) {
//		javax.swing.JOptionPane.showMessageDialog( null, "todo: handleItemSelected" );
//	}
	
//	/* package-private */ void addTab( E item ) {
//		Tab<E> tab = this.map.get( item );
//		if( tab != null ) {
//			//pass
//		} else {
//			tab = this.createTab( item, this.tabCreator );
//			this.map.put( item, tab );
//		}
//		this.revalidateAndRepaint();
//	}
//	/* package-private */ void removeTab( E item ) {
//		this.revalidateAndRepaint();
//	}
//	/* package-private */ void selectTab( E item ) {
//		Tab<E> tab = this.map.get( item );
//		assert tab != null;
//		tab.select();
//	}
}
