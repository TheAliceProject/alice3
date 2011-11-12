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


/**
 * @author Dennis Cosgrove
 */
public class CardPanel extends Panel {
	private final java.awt.CardLayout cardLayout;
	public CardPanel() {
		this( null );
	}
	public CardPanel( int hgap, int vgap ) {
		this( null, hgap, vgap );
	}
	public CardPanel( org.lgna.croquet.CardComposite composite ) {
		this( composite, 0, 0 );
	}
	public CardPanel( org.lgna.croquet.CardComposite composite, int hgap, int vgap ) {
		super( composite );
		this.cardLayout = new java.awt.CardLayout( hgap, vgap );
		if( composite != null ) {
			for( org.lgna.croquet.Composite< ? > card : composite.getCards() ) {
				Key key = this.createKey( card );
				this.addComponent( key );
			}
		}
		this.show( null );
	}
	@Override
	protected final java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return this.cardLayout;
	}
	
	public static final class Key {
		private final JComponent< ? > view;
		private final java.util.UUID id;
		private Key( JComponent< ? > view, java.util.UUID id ) {
			this.view = view;
			this.id = id;
		}
		public java.util.UUID getId() {
			return this.id;
		}	
		public JComponent< ? > getView() {
			return this.view;
		}
	}
	private java.util.Map< java.util.UUID, Key > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private Key nullKey;
	@Deprecated
	public Key createKey( JComponent< ? > child, java.util.UUID id ) {
		if( map.containsKey( id ) ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: CardPanel replacing key" );
		}
		Key rv = new Key( child, id );
		this.map.put( id, rv );
		return rv;
	}
	@Deprecated
	public Key getKey( java.util.UUID id ) {
		return this.map.get( id );
	}

	public Key getKey( org.lgna.croquet.Composite< ? > composite ) {
		return this.getKey( composite.getId() );
	}
	public Key createKey( org.lgna.croquet.Composite< ? > composite ) {
		return this.createKey( composite.createView(), composite.getId() );
	}
	
	public void addComponent( Key key ) {
		this.internalAddComponent( key.view, key.id.toString() );
	}
	public void removeComponent( Key key ) {
		this.internalRemoveComponent( key.view );
	}
	public void show( Key key ) {
		if( key != null ) {
			//pass
		} else {
			if( this.nullKey != null ) {
				//pass
			} else {
				Label label = new Label();
				//label.setText( "unset" );
				label.setBackgroundColor( null );
				label.setHorizontalAlignment( HorizontalAlignment.CENTER );
				this.nullKey = this.createKey( label, java.util.UUID.randomUUID() );
				this.addComponent( this.nullKey );
			}
			key = this.nullKey;
		}
		this.cardLayout.show( this.getAwtComponent(), key.id.toString() );
	}
}
