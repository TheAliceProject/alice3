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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class CardComposite extends AbstractComposite< org.lgna.croquet.components.CardPanel > {
	private final java.util.List< Composite< ? > > cards;
	private Composite<?> showingCard;
	public CardComposite( java.util.UUID id, Composite< ? >... cards ) {
		super( id );
		this.cards = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList( cards );
	}
	public void addCard( Composite<?> card ) {
		this.cards.add( card );
		org.lgna.croquet.components.CardPanel view = this.peekView();
		if( view != null ) {
			view.addComposite( card );
		}
	}
	public void removeCard( Composite<?> card ) {
		this.cards.remove( card );
	}
	public Composite<?> getShowingCard() {
		return this.showingCard;
	}
	
	@Override
	public final boolean contains( org.lgna.croquet.Model model ) {
		if( super.contains( model ) ) {
			return true;
		} else {
			for( Composite< ? > card : this.cards ) {
				//todo
				if( card.contains( model ) ) {
					return true;
				}
			}
			return false;
		}
	}
	public java.util.List< Composite< ? >> getCards() {
		return this.cards;
	}
	@Override
	protected org.lgna.croquet.components.CardPanel createView() {
		return new org.lgna.croquet.components.CardPanel( this );
	}
	@Override
	public void releaseView() {
		for( Composite< ? > card : this.cards ) {
			card.releaseView();
		}
		super.releaseView();
	}
	
	public void showCard( Composite< ? > card ) {
		synchronized( this.getView().getTreeLock() ) {
			if( this.showingCard != null ) {
				this.showingCard.handlePostDeactivation();
			}
			this.showingCard = card;
			if( this.showingCard != null ) {
				this.showingCard.handlePreActivation();
			}
			this.getView().showComposite( this.showingCard );
		}
	}
	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		if( this.showingCard != null ) {
			this.showingCard.handlePreActivation();
		}
	}
	@Override
	public void handlePostDeactivation() {
		if( this.showingCard != null ) {
			this.showingCard.handlePostDeactivation();
		}
		super.handlePostDeactivation();
	}
}
