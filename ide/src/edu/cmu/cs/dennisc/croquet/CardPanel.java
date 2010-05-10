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
public class CardPanel extends Panel {
	private java.awt.CardLayout cardLayout;
	public CardPanel() {
		this( 0, 0 );
	}
	public CardPanel( int hgap, int vgap ) {
		this.cardLayout = new java.awt.CardLayout( hgap, vgap );
	}
	@Override
	protected final java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return this.cardLayout;
	}
	
	public class Key {
		private Component<?> child;
		private String cardLayoutKey;
		private Key( Component<?> child, String cardLayoutKey ) {
			this.child = child;
			this.cardLayoutKey = cardLayoutKey;
		}
	}
	
	private Key nullKey;
	public Key createKey( Component<?> child, String cardLayoutKey ) {
		return new Key( child, cardLayoutKey );
	}
//	public Key createKey( Component<?> child, java.util.UUID id ) {
//		return new Key( child, id.toString() );
//	}
	public void addComponent( Key key ) {
		this.internalAddComponent( key.child, key.cardLayoutKey );
	}
	public void removeComponent( Key key ) {
		this.internalRemoveComponent( key.child );
	}
	public void show( Key key ) {
		if( key != null ) {
			//pass
		} else {
			if( this.nullKey != null ) {
				
			} else {
				this.nullKey = this.createKey( new Label( "nothing to see here" ), java.util.UUID.randomUUID().toString() );
			}
			key = this.nullKey;
		}
		this.cardLayout.show( this.getJComponent(), key.cardLayoutKey );
	}
}
