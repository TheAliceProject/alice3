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

package org.lgna.croquet.components;

/*package-private*/abstract class TabItemDetails<E extends org.lgna.croquet.TabComposite<?>> extends ItemDetails<E> {
	private ScrollPane scrollPane;

	public TabItemDetails( org.lgna.croquet.ItemState<E> state, E item, AbstractTabbedPane<E, ?> tabbedPane, ScrollPane scrollPane ) {
		super( state, item, tabbedPane );
		this.scrollPane = scrollPane;
		View<?, ?> mainView = this.getMainView();
		this.getButton().setBackgroundColor( mainView.getBackgroundColor() );
		if( this.scrollPane != null ) {
			this.scrollPane.setViewportView( mainView );
		}
	}

	public java.util.UUID getTabId() {
		return this.getItem().getTabId();
	}

	public View<?, ?> getMainView() {
		return this.getItem().getView();
	}

	public ScrollPane getScrollPane() {
		return this.scrollPane;
	}

	public JComponent<?> getRootComponent() {
		if( this.scrollPane != null ) {
			return this.scrollPane;
		} else {
			return this.getMainView();
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractTabbedPane<E extends org.lgna.croquet.TabComposite<?>, TID extends TabItemDetails<E>> extends ItemSelectablePanel<E, TID> {
	public AbstractTabbedPane( org.lgna.croquet.ListSelectionState<E> model ) {
		super( model );
	}

	@Override
	public void setFont( java.awt.Font font ) {
		super.setFont( font );
		for( TID itemDetails : this.getAllItemDetails() ) {
			itemDetails.getButton().setFont( font );
		}
	}

	protected void customizeTitleComponent( org.lgna.croquet.BooleanState booleanState, BooleanStateButton<?> button, E item ) {
		item.customizeTitleComponent( booleanState, button );
	}

	protected void releaseTitleComponent( org.lgna.croquet.BooleanState booleanState, BooleanStateButton<?> button, E item ) {
		item.releaseTitleComponent( booleanState, button );
	}

	protected ScrollPane createScrollPane( E item ) {
		return item.createScrollPane();
	}

	protected abstract BooleanStateButton<? extends javax.swing.AbstractButton> createTitleButton( E item, org.lgna.croquet.BooleanState itemSelectedState, java.awt.event.ActionListener closeButtonActionListener );

	protected abstract TID createTabItemDetails( E item, ScrollPane scrollPane );

	@Override
	protected final TID createItemDetails( final E item ) {
		return createTabItemDetails( item, this.createScrollPane( item ) );
	}

	@Override
	protected org.lgna.croquet.components.BooleanStateButton<?> createButtonForItemSelectedState( final E item, org.lgna.croquet.BooleanState itemSelectedState ) {
		java.awt.event.ActionListener closeButtonActionListener;
		if( item.isCloseable() ) {
			closeButtonActionListener = new java.awt.event.ActionListener() {
				public void actionPerformed( java.awt.event.ActionEvent e ) {
					AbstractTabbedPane.this.getModel().removeItemAndSelectAppropriateReplacement( item );
				}
			};
		} else {
			closeButtonActionListener = null;
		}
		BooleanStateButton<?> rv = this.createTitleButton( item, itemSelectedState, closeButtonActionListener );
		item.customizeTitleComponent( itemSelectedState, rv );
		return rv;

	}

	public JComponent<?> getMainComponentFor( E item ) {
		TabItemDetails<E> tabItemDetails = (TabItemDetails<E>)this.getItemDetails( item );
		if( tabItemDetails != null ) {
			return tabItemDetails.getMainView();
		} else {
			return null;
		}
	}

	public ScrollPane getScrollPaneFor( E item ) {
		TabItemDetails<E> tabItemDetails = (TabItemDetails<E>)this.getItemDetails( item );
		if( tabItemDetails != null ) {
			return tabItemDetails.getScrollPane();
		} else {
			return null;
		}
	}

	public JComponent<?> getRootComponentFor( E item ) {
		TabItemDetails<E> tabItemDetails = (TabItemDetails<E>)this.getItemDetails( item );
		if( tabItemDetails != null ) {
			return tabItemDetails.getRootComponent();
		} else {
			return null;
		}
	}
}
