/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class CardPanel extends Panel {
	private class CustomPreferredSizeCardLayout extends java.awt.CardLayout {
		public CustomPreferredSizeCardLayout( int hgap, int vgap ) {
			super( hgap, vgap );
		}

		@Override
		public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
			synchronized( parent.getTreeLock() ) {
				org.lgna.croquet.CardOwnerComposite cardOwner = (org.lgna.croquet.CardOwnerComposite)getComposite();

				int widthMax = 0;
				int heightMax = 0;

				for( org.lgna.croquet.Composite<?> card : cardOwner.getCards() ) {
					if( cardOwner.isCardAccountedForInPreferredSizeCalculation( card ) ) {
						java.awt.Component awtChild = card.getRootComponent().getAwtComponent();
						java.awt.Dimension awtChildPreferredSize = awtChild.getPreferredSize();
						widthMax = Math.max( widthMax, awtChildPreferredSize.width );
						heightMax = Math.max( heightMax, awtChildPreferredSize.height );
					}
				}
				java.awt.Insets insets = parent.getInsets();
				int hgap = this.getHgap();
				int vgap = this.getVgap();
				return new java.awt.Dimension(
						hgap + insets.left + widthMax + insets.right + hgap,
						vgap + insets.top + heightMax + insets.bottom + vgap );
			}
		}
	}

	private final java.awt.CardLayout cardLayout;

	public CardPanel( org.lgna.croquet.CardOwnerComposite composite, int hgap, int vgap ) {
		super( composite );
		this.cardLayout = new CustomPreferredSizeCardLayout( hgap, vgap );
		java.awt.Color color = FolderTabbedPane.DEFAULT_BACKGROUND_COLOR;
		if( composite != null ) {
			java.util.List<org.lgna.croquet.Composite<?>> cards = composite.getCards();
			for( org.lgna.croquet.Composite<?> card : cards ) {
				this.addComposite( card );
			}
			if( cards.size() > 0 ) {
				color = cards.get( 0 ).getView().getBackgroundColor();
			}
		}
		this.setBackgroundColor( color );
	}

	public CardPanel( org.lgna.croquet.CardOwnerComposite composite ) {
		this( composite, 0, 0 );
	}

	@Override
	protected final java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return this.cardLayout;
	}

	private static final String NULL_KEY = "null";

	private Label nullLabel;

	private static String getKey( org.lgna.croquet.Composite<?> composite ) {
		return composite != null ? composite.getCardId().toString() : NULL_KEY;
	}

	public void addComposite( org.lgna.croquet.Composite<?> composite ) {
		assert composite != null : this;
		synchronized( this.getTreeLock() ) {
			this.internalAddComponent( composite.getRootComponent(), getKey( composite ) );
		}
	}

	public void removeComposite( org.lgna.croquet.Composite<?> composite ) {
		assert composite != null : this;
		synchronized( this.getTreeLock() ) {
			this.internalRemoveComponent( composite.getRootComponent() );
		}
	}

	public void showComposite( org.lgna.croquet.Composite<?> composite ) {
		if( composite != null ) {
			//pass
		} else {
			if( this.nullLabel != null ) {
				//pass
			} else {
				this.nullLabel = new Label();
				this.internalAddComponent( this.nullLabel, NULL_KEY );
			}
		}
		this.cardLayout.show( this.getAwtComponent(), getKey( composite ) );
	}
}
