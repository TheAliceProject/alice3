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
package org.alice.ide.common;


/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractListPropertyPane< E extends edu.cmu.cs.dennisc.property.ListProperty > extends AbstractPropertyPane< E > {
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > listPropertyAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< E >() {

		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
			AbstractListPropertyPane.this.refresh();
		}


		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
			AbstractListPropertyPane.this.refresh();
		}


		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
			AbstractListPropertyPane.this.refresh();
		}


		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
			AbstractListPropertyPane.this.refresh();
		}
		
	}; 
	public AbstractListPropertyPane( Factory factory, int axis, E property ) {
		super( factory, axis, property );
	}
	protected abstract edu.cmu.cs.dennisc.croquet.Component< ? > createComponent( Object instance );
	protected void addPrefixComponents() {
	}
	protected void addPostfixComponents() {
	}
	
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		this.getProperty().addListPropertyListener( this.listPropertyAdapter );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		this.getProperty().removeListPropertyListener( this.listPropertyAdapter );
		super.handleRemovedFrom( parent );
	}
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createInterstitial( int i, final int N ) {
		return null;
	}
	
	@Override
	protected void refresh() {
		this.removeAllComponents();
		this.addPrefixComponents();
		final int N = getProperty().size();
		int i = 0;
		for( Object o : getProperty() ) {
			edu.cmu.cs.dennisc.croquet.Component< ? > component;
			if( o != null ) {
				component = this.createComponent( o );
			} else {
				component = new edu.cmu.cs.dennisc.croquet.Label( "null" );
			}
			this.addComponent( component );
			edu.cmu.cs.dennisc.croquet.Component< ? > interstitial = this.createInterstitial( i, N );
			if( interstitial != null ) {
				this.addComponent( interstitial );
			}
			i++;
		}
		this.addPostfixComponents();
		this.revalidateAndRepaint();
	}
}

