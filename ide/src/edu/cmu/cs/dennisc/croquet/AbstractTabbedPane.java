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
	public AbstractTabbedPane( ListSelectionState<E> model, TabSelectionState.TabCreator< E > tabCreator ) {
		super( model );
		this.tabCreator = tabCreator;
	}

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

	private TabSelectionState.TabCreator< E > tabCreator;
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
	protected final D createItemDetails( final E item, BooleanState booleanState ) {
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
		AbstractButton< ?, BooleanState > titleButton = this.createTitleButton( booleanState, closeButtonActionListener );
		this.tabCreator.customizeTitleComponent( booleanState, titleButton, item );
		return createTabItemDetails( item, id, titleButton, this.tabCreator.createScrollPane( item ), this.tabCreator.createMainComponent( item ) );
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
}
