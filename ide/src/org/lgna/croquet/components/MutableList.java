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

/*package-private*/class MutableListItemDetails<E, LC extends JComponent<?>, MC extends JComponent<?>, TC extends JComponent<?>> extends ItemDetails<E,MutableListItemDetails<E,LC,MC,TC>,MutableList<E,LC,MC,TC>> {
	private LC leadingComponent;
	private MC mainComponent;
	private TC trailingComponent;

	public MutableListItemDetails( MutableList<E,LC,MC,TC> panel, E item, BooleanStateButton<javax.swing.AbstractButton> button, LC leadingComponent, MC mainComponent, TC trailingComponent, java.awt.event.ActionListener actionListener ) {
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
public abstract class MutableList<E, LC extends JComponent<?>, MC extends JComponent<?>, TC extends JComponent<?>> extends ItemSelectablePanel<E,MutableListItemDetails<E,LC,MC,TC>> implements org.lgna.croquet.DropReceptor {
//	private class MutableListDragModel extends org.lgna.croquet.DragModel {
//		public MutableListDragModel() {
//			super( java.util.UUID.fromString( "a136b615-2e99-4481-a89d-a48d633a4080" ) );
//		}
//		@Override
//		public final java.util.List< ? extends org.lgna.croquet.DropReceptor > createListOfPotentialDropReceptors() {
//			org.lgna.croquet.DropReceptor dropReceptor = MutableList.this;
//			return java.util.Collections.unmodifiableList( edu.cmu.cs.dennisc.java.util.Collections.newArrayList( dropReceptor ) );
//		}
//		@Override
//		public void handleDragStarted( org.lgna.croquet.history.DragStep step ) {
//		}
//		@Override
//		public void handleDragEnteredDropReceptor( org.lgna.croquet.history.DragStep step ) {
//		}
//		@Override
//		public void handleDragExitedDropReceptor( org.lgna.croquet.history.DragStep step ) {
//		}
//		@Override
//		public void handleDragStopped( org.lgna.croquet.history.DragStep step ) {
//		}
//		@Override
//		public org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.croquet.DropSite dropSite ) {
//			return null;
//		}
//	}
//
//	private class MutableListButton extends org.alice.ide.croquet.components.KnurlDragComponent< MutableListDragModel > {
//		public MutableListButton( MutableListDragModel dragModel ) {
//			super( dragModel );
//		}
//		@Override
//		protected java.awt.Shape createShape( int x, int y, int width, int height ) {
//			return null;
//		}
//		@Override
//		protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
//		}
//		@Override
//		protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
//		}
//		@Override
//		protected int getInsetTop() {
//			return 0;
//		}
//		@Override
//		protected int getDockInsetLeft() {
//			return 0;
//		}
//		@Override
//		protected int getInternalInsetLeft() {
//			return 0;
//		}
//		@Override
//		protected int getInsetBottom() {
//			return 0;
//		}
//		@Override
//		protected int getInsetRight() {
//			return 0;
//		}
//	}
	
	private class MutableListButton extends BooleanStateButton<javax.swing.AbstractButton> {
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
				public void updateUI() {
					this.setUI( new javax.swing.plaf.basic.BasicButtonUI() );
				}
				@Override
				protected void paintComponent( java.awt.Graphics g ) {
					//super.paintComponent( g );
					javax.swing.ButtonModel model = this.getModel();
					java.awt.Color color;
					if( model.isSelected() ) {
						if( model.isRollover() ) { 
							color = MutableList.this.selectedRolloverBackgroundColor;
						} else {
							color = MutableList.this.selectedBackgroundColor;
						}
					} else {
						if( model.isRollover() ) { 
							color = MutableList.this.unselectedRolloverBackgroundColor;
						} else {
							color = null;
						}
					}
					int width = this.getWidth();
					int height = this.getHeight();
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					if( color != null ) {
						Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
						g2.setPaint( color );
						
						g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
						g2.fillRoundRect( 0, 0, this.getWidth(), this.getHeight(), 8, 8 );
						if( model.isRollover() ) {
							color = java.awt.Color.LIGHT_GRAY;
						} else {
							color = java.awt.Color.GRAY;
						}
						g2.setPaint( color );
						edu.cmu.cs.dennisc.java.awt.KnurlUtilities.paintKnurl5( g, 2, 2, 6, this.getHeight() - 5 );

						g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
					} else {
//						g2.setPaint( MutableList.this.getUnselectedBackgroundColor() );
//						g.clearRect( 0, 0, width, height );
					}
				}
			};
			rv.setOpaque( false );
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 14, 4, 4 ) );
			rv.setLayout( new java.awt.BorderLayout() );
			rv.setRolloverEnabled( true );
			return rv;
		}
	}

	private static final java.awt.Color DEFAULT_SELECTED_BACKGROUND = new java.awt.Color( 57, 105, 138 );
	private static final java.awt.Color DEFAULT_UNSELECTED_BACKGROUND = java.awt.Color.WHITE;//new java.awt.Color( 214, 217, 223 );

	private java.awt.Color selectedBackgroundColor;
	private java.awt.Color unselectedBackgroundColor;
	private java.awt.Color selectedRolloverBackgroundColor;
	private java.awt.Color unselectedRolloverBackgroundColor;
	
	private java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}
		public void mousePressed( java.awt.event.MouseEvent e ) {
			MutableList.this.handleMousePressed( e );
		}
		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};

	protected abstract LC createLeadingComponent();
	protected abstract MC createMainComponent();
	protected abstract TC createTrailingComponent();
	protected abstract void update( LC leadingComponent, MC mainComponent, TC trailingComponent, int index, E item );
	protected abstract void updateSelection( LC leadingComponent, MC mainComponent, TC trailingComponent, boolean isSelected );

	private int index;
	private final PageAxisPanel pageAxisPanel = new PageAxisPanel();

	public MutableList( org.lgna.croquet.ListSelectionState<E> model ) {
		super( model );
		this.setSelectedBackgroundColor( DEFAULT_SELECTED_BACKGROUND );
		this.setUnselectedBackgroundColor( DEFAULT_UNSELECTED_BACKGROUND );
		this.internalAddComponent( this.pageAxisPanel, java.awt.BorderLayout.CENTER );
	}
	public MutableList( org.lgna.croquet.ListSelectionState<E> model, org.lgna.croquet.PopupPrepModel addPrepModel ) {
		this( model );
		if( addPrepModel != null ) {
			this.internalAddComponent( addPrepModel.createPopupButton(), java.awt.BorderLayout.PAGE_END );
		}
	}
	public MutableList( org.lgna.croquet.ListSelectionState<E> model, org.lgna.croquet.Operation addOperation ) {
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

	private void handleMousePressed( java.awt.event.MouseEvent e ) {
		this.getModel().setSelectedItem( null );
	}

	public java.awt.Color getSelectedBackgroundColor() {
		return this.selectedBackgroundColor;
	}
	public void setSelectedBackgroundColor( java.awt.Color color ) {
		this.selectedBackgroundColor = color;
		this.selectedRolloverBackgroundColor = this.selectedBackgroundColor.darker();
	}

	public java.awt.Color getUnselectedBackgroundColor() {
		return this.unselectedBackgroundColor;
	}
	public void setUnselectedBackgroundColor( java.awt.Color color ) {
		this.unselectedBackgroundColor = color;
		this.unselectedRolloverBackgroundColor = new java.awt.Color( 221, 221, 255 );
		this.setBackgroundColor( this.unselectedBackgroundColor );
	}


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
			boolean isSelected = details.getItem() == item;
			this.updateSelection( details.getLeadingComponent(), details.getMainComponent(), details.getTrailingComponent(), isSelected );
		}
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.addMouseListener( this.mouseListener );
	}
	@Override
	protected void handleUndisplayable() {
		this.removeMouseListener( this.mouseListener );
		super.handleUndisplayable();
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
		ViewController<?,?> button = rv.getButton();
		button.setVisible( false );
		this.pageAxisPanel.addComponent( button );
		return rv;
	}

	private static final javax.swing.KeyStroke DELETE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_DELETE, 0 );
	private static final javax.swing.KeyStroke BACK_SPACE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_BACK_SPACE, 0 );
	//note: ups/downs do not seem to work
	private static final javax.swing.KeyStroke UP_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_UP, 0 );
	private static final javax.swing.KeyStroke KEYPAD_UP_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_KP_UP, 0 );
	private static final javax.swing.KeyStroke DOWN_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_DOWN, 0 );
	private static final javax.swing.KeyStroke KEYPAD_DOWN_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_KP_DOWN, 0 );
	private final java.awt.event.ActionListener removeSelectedListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			System.out.println( "removeSelectedItem" );
			//MutableList.this.removeSelectedItem();
		}
	};
	private final java.awt.event.ActionListener moveSelectionUpListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			System.out.println( "moveSelectionUp" );
		}
	};
	private final java.awt.event.ActionListener moveSelectionDownListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			System.out.println( "moveSelectionDown" );
		}
	};
	public void registerKeyboardActions() {
		this.registerKeyboardAction( this.removeSelectedListener, DELETE_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.removeSelectedListener, BACK_SPACE_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.moveSelectionUpListener, UP_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.moveSelectionUpListener, KEYPAD_UP_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.moveSelectionDownListener, DOWN_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.moveSelectionDownListener, KEYPAD_DOWN_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
	}
	public void unregisterKeyboardActions() {
		this.unregisterKeyboardAction( KEYPAD_DOWN_KEY_STROKE );
		this.unregisterKeyboardAction( DOWN_KEY_STROKE );
		this.unregisterKeyboardAction( KEYPAD_UP_KEY_STROKE );
		this.unregisterKeyboardAction( UP_KEY_STROKE );
		this.unregisterKeyboardAction( BACK_SPACE_KEY_STROKE );
		this.unregisterKeyboardAction( DELETE_KEY_STROKE );
	}

	public org.lgna.croquet.components.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
		return null;
	}
	public boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
		return false;
	}
	public org.lgna.croquet.components.JComponent<?> getViewController() {
		return null;
	}
	public void dragStarted( org.lgna.croquet.history.DragStep step ) {
	}
	public void dragEntered( org.lgna.croquet.history.DragStep step ) {
	}
	public org.lgna.croquet.DropSite dragUpdated( org.lgna.croquet.history.DragStep step ) {
		return null;
	}
	public org.lgna.croquet.Model dragDropped( org.lgna.croquet.history.DragStep step ) {
		return null;
	}
	public void dragExited( org.lgna.croquet.history.DragStep step, boolean isDropRecipient ) {
	}
	public void dragStopped( org.lgna.croquet.history.DragStep step ) {
	}
	public String getTutorialNoteText( org.lgna.croquet.Model model, org.lgna.croquet.edits.Edit<?> edit ) {
		return null;
	}
}
