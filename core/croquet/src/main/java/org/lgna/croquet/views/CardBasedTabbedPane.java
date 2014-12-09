/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.croquet.views;

import org.lgna.croquet.TabState;

/**
 * @author Dennis Cosgrove
 */
public abstract class CardBasedTabbedPane<E extends org.lgna.croquet.TabComposite<?>> extends TabbedPane<E> {
	private static final class FolderTabbedPaneCardOwner extends org.lgna.croquet.CardOwnerComposite {
		public FolderTabbedPaneCardOwner() {
			super( java.util.UUID.fromString( "31cf52f4-80ea-49f9-9875-7ea942d241e7" ) );
		}
	}

	private final FolderTabbedPaneCardOwner cardOwner = new FolderTabbedPaneCardOwner();

	public CardBasedTabbedPane( TabState<E, ?> model ) {
		super( model );
		for( org.lgna.croquet.TabComposite<?> card : model ) {
			if( card != null ) {
				this.cardOwner.addCard( card );
			}
		}
	}

	public org.lgna.croquet.CardOwnerComposite getCardOwner() {
		return this.cardOwner;
	}

	@Override
	protected void handleValueChanged( final E card ) {
		if( this.cardOwner.getShowingCard() == card ) {
			//pass
		} else {
			if( this.cardOwner.getCards().contains( card ) ) {
				this.cardOwner.showCardRefrainingFromActivation( card );
				this.repaint();
			} else {
				if( card != null ) {
					this.cardOwner.addCard( card );
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "note invoke later showCard", card );
					javax.swing.SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							cardOwner.showCardRefrainingFromActivation( card );
							repaint();
						}
					} );
				}
			}
		}
		if( card != null ) {
			BooleanStateButton<?> button = this.getItemDetails( card );
			if( button != null ) {
				button.scrollToVisible();
			}
		}
	}

	@Override
	protected void removeAllDetails() {
		this.cardOwner.getView().removeAllComponents();
	}

	@Override
	protected void addPrologue( int count ) {
	}

	@Override
	protected void addItem( E item, BooleanStateButton<?> button ) {
		this.cardOwner.getView().addComposite( item );
	}

	@Override
	protected void addEpilogue() {
		this.cardOwner.showCardRefrainingFromActivation( this.cardOwner.getShowingCard() );
	}
}
