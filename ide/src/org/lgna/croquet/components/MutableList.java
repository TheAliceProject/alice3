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

/*package-private*/ class MutableListItemDetails<E, LC extends JComponent< ? >,MC extends JComponent< ? >,TC extends JComponent< ? > > extends ItemDetails<E, MutableListItemDetails< E,LC,MC,TC >, MutableList<E,LC,MC,TC>> {
	private LC leadingComponent;
	private MC mainComponent;
	private TC trailingComponent;

	public MutableListItemDetails( MutableList<E,LC,MC,TC> panel, E item, BooleanStateButton< javax.swing.AbstractButton > button, LC leadingComponent, MC mainComponent, TC trailingComponent, java.awt.event.ActionListener actionListener ) {
		super( panel, item, button );
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
			edu.cmu.cs.dennisc.javax.swing.components.JCloseButton jCloseButton = new edu.cmu.cs.dennisc.javax.swing.components.JCloseButton( true );
			jCloseButton.addActionListener( actionListener );
			button.getAwtComponent().add( jCloseButton, java.awt.BorderLayout.LINE_END );
		} else {
			if( this.trailingComponent != null ) {
				button.internalAddComponent( this.trailingComponent, java.awt.BorderLayout.LINE_END );
			}
		}

	}
	public LC getLeadingComponent() {
		return this.leadingComponent;
	}
	public MC getMainComponent() {
		return this.mainComponent;
	}
	public TC getTrailingComponent() {
		return this.trailingComponent;
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class MutableList<E, LC extends JComponent< ? >,MC extends JComponent< ? >,TC extends JComponent< ? > > extends ItemSelectablePanel< E, MutableListItemDetails<E,LC,MC,TC> > {
	private static final java.awt.Color DEFAULT_SELECTED_BACKGROUND = new java.awt.Color( 57, 105, 138 );
	private static final java.awt.Color DEFAULT_UNSELECTED_BACKGROUND = new java.awt.Color( 214, 217, 223 );

	private java.awt.Color selectedBackgroundColor = DEFAULT_SELECTED_BACKGROUND;
	private java.awt.Color unselectedBackgroundColor = DEFAULT_UNSELECTED_BACKGROUND;

	protected abstract LC createLeadingComponent();
	protected abstract MC createMainComponent();
	protected abstract TC createTrailingComponent();
	protected abstract void update( LC leadingComponent, MC mainComponent, TC trailingComponent, int index, E item );
	protected abstract void updateSelection( LC leadingComponent, MC mainComponent, TC trailingComponent, boolean isSelected );

	private class MutableListButton extends BooleanStateButton< javax.swing.AbstractButton > {
		public MutableListButton( org.lgna.croquet.BooleanState booleanState ) {
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
				protected void paintComponent( java.awt.Graphics g ) {
					//super.paintComponent(g);
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
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
						g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
						g.fillRoundRect( 0, 0, this.getWidth(), this.getHeight(), 8, 8 );
						if( this.getModel().isRollover() ) {
							color = java.awt.Color.LIGHT_GRAY;
						} else {
							color = java.awt.Color.GRAY;
						}
						g.setColor( color );
						edu.cmu.cs.dennisc.java.awt.KnurlUtilities.paintKnurl5( g, 2, 2, 6, this.getHeight() - 5 );
					}
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
				}
			};
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 14, 4, 4 ) );
			rv.setLayout( new java.awt.BorderLayout() );
			rv.setRolloverEnabled( true );
			return rv;
		}
	}
	private final PageAxisPanel pageAxisPanel = new PageAxisPanel();
	public MutableList( org.lgna.croquet.ListSelectionState< E > model ) {
		super( model );
		this.internalAddComponent( this.pageAxisPanel, java.awt.BorderLayout.CENTER );
	}
	public MutableList( org.lgna.croquet.ListSelectionState< E > model, org.lgna.croquet.PopupPrepModel addPrepModel ) {
		this( model );
		if( addPrepModel != null ) {
			this.internalAddComponent( addPrepModel.createPopupButton(), java.awt.BorderLayout.PAGE_END );
		}
	}
	public MutableList( org.lgna.croquet.ListSelectionState< E > model, org.lgna.croquet.Operation addOperation ) {
		this( model );
		if( addOperation != null ) {
			this.internalAddComponent( addOperation.createButton(), java.awt.BorderLayout.PAGE_END );
		}
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
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
	protected void addPrologue( int count ) {
		//this.pageAxisPanel.internalRemoveAllComponents();
		this.index = 0;
		for( MutableListItemDetails details : this.getAllItemDetails() ) {
			details.getButton().setVisible( false );
		}
	}
	@Override
	protected void addItem( MutableListItemDetails<E,LC,MC,TC> itemDetails ) {
		this.update( itemDetails.getLeadingComponent(), itemDetails.getMainComponent(), itemDetails.getTrailingComponent(), this.index, itemDetails.getItem() );
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
		for( MutableListItemDetails<E,LC,MC,TC> details : this.getAllItemDetails() ) {
			this.updateSelection( details.getLeadingComponent(), details.getMainComponent(), details.getTrailingComponent(), details.getItem() == item );
		}
	}
	@Override
	protected final MutableListItemDetails<E,LC,MC,TC> createItemDetails( final E item, org.lgna.croquet.BooleanState booleanState ) {
		java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				MutableList.this.getModel().removeItem( item );
			}
		};

		MutableListButton mutableListButton = new MutableListButton( booleanState );
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( booleanState );
		MutableListItemDetails<E,LC,MC,TC> rv = new MutableListItemDetails<E,LC,MC,TC>( this, item, mutableListButton, this.createLeadingComponent(), this.createMainComponent(), this.createTrailingComponent(), actionListener );
		AbstractButton< ?, ? > button = rv.getButton();
		button.setVisible( false );
		this.pageAxisPanel.addComponent( button );
		return rv;
	}

	public void setSelectedBackgroundColor( java.awt.Color color ) {
		this.selectedBackgroundColor = color;
	}

	public java.awt.Color getSelectedBackgroundColor() {
		return this.selectedBackgroundColor;
	}

	public void setUnselectedBackgroundColor( java.awt.Color color ) {
		this.unselectedBackgroundColor = color;
	}

	public java.awt.Color getUnselectedBackgroundColor() {
		return this.unselectedBackgroundColor;
	}
}
