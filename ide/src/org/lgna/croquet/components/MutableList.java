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

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import edu.cmu.cs.dennisc.croquet.BooleanState;
import edu.cmu.cs.dennisc.croquet.ListSelectionState;
import edu.cmu.cs.dennisc.croquet.Operation;

/**
 * @author Dennis Cosgrove
 */
public final class MutableList< E > extends ItemSelectablePanel< E, MutableList.MutableListItemDetails > {
	class MutableListItemDetails extends ItemSelectablePanel.ItemDetails {
		private Component<?> leadingComponent;
		private Component<?> mainComponent;
		private Component<?> trailingComponent;
		public MutableListItemDetails( E item, AbstractButton<?, BooleanState> button, Component<?> leadingComponent, Component<?> mainComponent, Component<?> trailingComponent, java.awt.event.ActionListener actionListener ) {
			super( item, button );
			this.leadingComponent = leadingComponent;
			this.mainComponent = mainComponent;
			this.trailingComponent = trailingComponent;
			
			if( this.leadingComponent != null ) {
				button.internalAddComponent( this.leadingComponent, java.awt.BorderLayout.LINE_START );
			}
			if( this.mainComponent != null ) {
				button.internalAddComponent( this.mainComponent, java.awt.BorderLayout.CENTER );
			}
			if( actionListener != null ) {
				//todo
				assert this.trailingComponent == null;
				edu.cmu.cs.dennisc.javax.swing.components.JCloseButton jCloseButton = new edu.cmu.cs.dennisc.javax.swing.components.JCloseButton(true);
				jCloseButton.addActionListener( actionListener );
				button.getAwtComponent().add( jCloseButton, java.awt.BorderLayout.LINE_END );
			} else {
				if( this.trailingComponent != null ) {
					button.internalAddComponent( this.trailingComponent, java.awt.BorderLayout.LINE_END );
				}
			}

		}
		public Component<?> getLeadingComponent() {
			return this.leadingComponent;
		}
		public Component<?> getMainComponent() {
			return this.mainComponent;
		}
		public Component<?> getTrailingComponent() {
			return this.trailingComponent;
		}
	}

	public static interface Factory< E > {
		public Component<?> createLeadingComponent();
		public Component<?> createMainComponent();
		public Component<?> createTrailingComponent();
		public void update( Component<?> leadingComponent, Component<?> mainComponent, Component<?> trailingComponent, int index, E item );
		public void updateSelection( Component<?> leadingComponent, Component<?> mainComponent, Component<?> trailingComponent, boolean isSelected );
		public Operation<?> getAddItemOperation();
	}
	
	private static java.awt.Color DEFAULT_SELECTED_BACKGROUND = new java.awt.Color( 57, 105, 138 );
	private static java.awt.Color DEFAULT_UNSELECTED_BACKGROUND = new java.awt.Color( 214, 217, 223);
	
	private java.awt.Color selectedBackgroundColor = DEFAULT_SELECTED_BACKGROUND;
	private java.awt.Color unselectedBackgroundColor = DEFAULT_UNSELECTED_BACKGROUND; 
	
	
	private class MutableListButton extends BooleanStateButton< javax.swing.AbstractButton > {
		public MutableListButton( BooleanState booleanState ) {
			super( booleanState );
		}
		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
			javax.swing.JToggleButton rv = new javax.swing.JToggleButton() {
				@Override
				public java.awt.Dimension getMaximumSize() {
					java.awt.Dimension rv = super.getPreferredSize();
					rv.width = Short.MAX_VALUE;
					return rv;
				}
				@Override
				protected void paintComponent(java.awt.Graphics g) {
					//super.paintComponent(g);
					g.setColor( MutableList.this.getUnselectedBackgroundColor() );
					g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
					//g.clearRect( 0, 0, this.getWidth(), this.getHeight() );
					if( this.isSelected() ) {
						java.awt.Color color;
//						if( this.getModel().isRollover() ) {
//							color = SELECTION_ROLLOVER_BACKGROUND;
//						} else {
							color = MutableList.this.getSelectedBackgroundColor();
//						}
						g.setColor( color );
						if (g instanceof Graphics2D)
		                {
		                   ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		                }
						g.fillRoundRect( 0, 0, this.getWidth(), this.getHeight(), 8, 8 );
						if( this.getModel().isRollover() ) {
							color = java.awt.Color.LIGHT_GRAY;
						} else {
							color = java.awt.Color.GRAY;
						}
						g.setColor( color );
						edu.cmu.cs.dennisc.java.awt.KnurlUtilities.paintKnurl5( g, 2, 2, 6, this.getHeight()-5 );
					}
				}
			};
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder(4, 14, 4, 4) );
			rv.setLayout( new java.awt.BorderLayout() );
			rv.setRolloverEnabled( true );
			return rv;
		}
//		private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
//			public void itemStateChanged(java.awt.event.ItemEvent e) {
//				boolean isSelected = e.getStateChange() == java.awt.event.ItemEvent.SELECTED;
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( isSelected, mainComponent.hashCode() );
//				MutableList.this.factory.updateSelection(leadingComponent, mainComponent, trailingComponent, isSelected );
//				if( isSelected ) {
//					requestFocus();
//				}
//			}
//		};
//		@Override
//		protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
//			super.handleAddedTo( parent );
//			this.getAwtComponent().addItemListener( this.itemListener );
//		}
//		@Override
//		protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
//			this.getAwtComponent().removeItemListener( this.itemListener );
//			super.handleRemovedFrom( parent );
//		}
	}
	private Factory<E> factory;
	private PageAxisPanel pageAxisPanel = new PageAxisPanel();
	public MutableList( ListSelectionState<E> model, Factory<E> factory ) {
		super( model );
		this.factory = factory;
		this.internalAddComponent( pageAxisPanel, java.awt.BorderLayout.CENTER );
		Operation<?> operation = this.factory.getAddItemOperation();
		if( operation != null ) {
			this.internalAddComponent( operation.createButton(), java.awt.BorderLayout.PAGE_END );
		}
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager(javax.swing.JPanel jPanel) {
		return new javax.swing.BoxLayout( jPanel, javax.swing.BoxLayout.PAGE_AXIS );
	}
	@Override
	protected void removeAllDetails() {
		for( MutableListItemDetails details : this.getAllItemDetails() ) {
			details.getButton().setVisible( false );
		}
	}
	private int index;
	@Override
	protected void addPrologue(int count) {
		//this.pageAxisPanel.internalRemoveAllComponents();
		this.index = 0;
		for( MutableListItemDetails details : this.getAllItemDetails() ) {
			details.getButton().setVisible( false );
		}
	}
	@Override
	protected void addItem(MutableList.MutableListItemDetails itemDetails) {
		factory.update(itemDetails.getLeadingComponent(), itemDetails.getMainComponent(), itemDetails.getTrailingComponent(), this.index, (E)itemDetails.getItem() );
		this.index++;
		//this.pageAxisPanel.internalAddComponent( itemDetails.getButton() );
		itemDetails.getButton().setVisible( true );
	}
	@Override
	protected void addEpilogue() {
	}

	@Override
	protected void handleItemSelected( E item ) {
		super.handleItemSelected( item );
		for( MutableListItemDetails details : this.getAllItemDetails() ) {
			this.factory.updateSelection( details.getLeadingComponent(), details.getMainComponent(), details.getTrailingComponent(), details.getItem() == item );
		}
	}
	@Override
	protected final MutableList.MutableListItemDetails createItemDetails( final E item, BooleanState booleanState ) {
		java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				MutableList.this.getModel().removeItem( item );
			}
		};

		MutableListButton mutableListButton = new MutableListButton( booleanState );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: MUTABLE_LIST_BOOLEAN_STATE" );
		MutableListItemDetails rv = new MutableListItemDetails( item, mutableListButton, factory.createLeadingComponent(), factory.createMainComponent(), factory.createTrailingComponent(), actionListener );
		AbstractButton<?, ?> button = rv.getButton();
		button.setVisible( false );
		this.pageAxisPanel.addComponent( button );
		return rv;
	}
	
	public void setSelectedBackgroundColor(java.awt.Color color)
	{
	    this.selectedBackgroundColor = color;
	}
	
	public java.awt.Color getSelectedBackgroundColor()
	{
	    return this.selectedBackgroundColor;
	}
	
	public void setUnselectedBackgroundColor(java.awt.Color color)
    {
        this.unselectedBackgroundColor = color;
    }
    
    public java.awt.Color getUnselectedBackgroundColor()
    {
        return this.unselectedBackgroundColor;
    }
}
