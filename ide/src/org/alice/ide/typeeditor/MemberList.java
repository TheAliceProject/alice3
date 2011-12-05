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

package org.alice.ide.typeeditor;

/*package-private*/ class MemberItemDetails<E> extends org.lgna.croquet.components.ItemDetails<E,MemberItemDetails<E>,MemberList<E>> {
	public MemberItemDetails( MemberList<E> panel, E item, org.lgna.croquet.components.BooleanStateButton< javax.swing.AbstractButton > button ) {
		super( panel, item, button );
	}
	public org.lgna.croquet.components.Component< ? > getComponent() {
		return this.getButton();
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberList<E> extends org.lgna.croquet.components.ItemSelectablePanel< E, MemberItemDetails<E> > {
	protected final float NAME_FONT_SCALE = 1.5f;
	protected class MemberButton extends org.lgna.croquet.components.BooleanStateButton< javax.swing.AbstractButton > {
		public MemberButton( org.lgna.croquet.BooleanState booleanState, org.lgna.croquet.components.JComponent< ? > lineStart, org.lgna.croquet.components.JComponent< ? > center, org.lgna.croquet.components.JComponent< ? > lineEnd ) {
			super( booleanState );
			if( lineStart != null ) {
				this.addComponent( lineStart, org.lgna.croquet.components.BorderPanel.Constraint.LINE_START );
			}
			if( center != null ) {
				this.addComponent( center, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
			}
			if( lineEnd != null ) {
				this.addComponent( lineEnd, org.lgna.croquet.components.BorderPanel.Constraint.LINE_END );
			}
			
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
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
					java.awt.Color color;

					color = MemberList.this.getBackgroundColor();
					if( this.getModel().isSelected() ) {
						color = color.darker();
					}
					g.setColor( color );
					g.fillRoundRect( 0, 0, this.getWidth()-1, this.getHeight()-1, 8, 8 );
					if( this.getModel().isRollover() ) {
						color = java.awt.Color.DARK_GRAY;
					} else {
						color = MemberList.this.getBackgroundColor().darker();
					}
					g.setColor( color );
					edu.cmu.cs.dennisc.java.awt.KnurlUtilities.paintKnurl5( g, 2, 2, 6, this.getHeight() - 5 );
					g.drawRoundRect( 0, 0, this.getWidth()-1, this.getHeight()-1, 8, 8 );
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
					
				}
			};
			rv.setOpaque( false );
			rv.setLayout( new java.awt.BorderLayout( 8, 0 ) );
			rv.setRolloverEnabled( true );
			return rv;
		}
		public void addComponent( org.lgna.croquet.components.Component< ? > component, org.lgna.croquet.components.BorderPanel.Constraint constraint ) {
			this.internalAddComponent( component, constraint.getInternal() );
		}
	}
	private org.lgna.croquet.components.PageAxisPanel pageAxisPanel = new org.lgna.croquet.components.PageAxisPanel();

	public MemberList( org.lgna.croquet.ListSelectionState< E > model, org.lgna.croquet.Operation< ? >... operations ) {
		super( model );
		this.internalAddComponent( pageAxisPanel );
		for( org.lgna.croquet.Operation< ? > operation : operations ) {
			if( operation != null ) {
				this.internalAddComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 4 ) );
				this.internalAddComponent( operation.createButton() );
			}
		}
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new javax.swing.BoxLayout( jPanel, javax.swing.BoxLayout.PAGE_AXIS );
	}

	protected abstract org.lgna.croquet.components.JComponent< ? > createButtonLineStart( E item );
	protected abstract org.lgna.croquet.components.JComponent< ? > createButtonCenter( E item );
	protected abstract org.lgna.croquet.components.JComponent< ? > createButtonLineEnd( E item );
	
	@Override
	protected final MemberItemDetails<E> createItemDetails(E item, org.lgna.croquet.BooleanState booleanState) {
		MemberButton memberButton = new MemberButton( booleanState,
				this.createButtonLineStart( item ),
				this.createButtonCenter( item ),
				this.createButtonLineEnd( item )
		);
		MemberItemDetails<E> rv = new MemberItemDetails< E >( this, item, memberButton );
		rv.getComponent().setVisible( false );
		this.pageAxisPanel.addComponent( rv.getComponent() );
		return rv;
	}

	@Override
	protected void removeAllDetails() {
		for( MemberItemDetails<E> details : this.getAllItemDetails() ) {
			details.getComponent().setVisible( false );
		}
	}

	private int index;

	@Override
	protected void addPrologue( int count ) {
		//this.pageAxisPanel.internalRemoveAllComponents();
		this.index = 0;
		for( MemberItemDetails<E> details : this.getAllItemDetails() ) {
			details.getComponent().setVisible( false );
		}
	}
	@Override
	protected void addItem( MemberItemDetails<E> itemDetails ) {
		this.index++;
		itemDetails.getComponent().setVisible( true );
	}
	@Override
	protected void addEpilogue() {
	}
}
