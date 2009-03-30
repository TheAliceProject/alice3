/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.common;


/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractListPropertyPane< E extends edu.cmu.cs.dennisc.property.ListProperty > extends AbstractPropertyPane< E > {
	public AbstractListPropertyPane( Factory factory, int axis, E property ) {
		super( factory, axis, property );
		property.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.ListPropertyListener< E >() {

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
			
		} );
	}
	protected abstract javax.swing.JComponent createComponent( Object instance );
	protected void addPrefixComponents() {
	}
	protected void addPostfixComponents() {
	}
	
	protected java.awt.Component createInterstitial( int i, final int N ) {
		return null;
	}
	
	@Override
	protected void refresh() {
		this.removeAll();
		this.addPrefixComponents();
		final int N = getProperty().size();
		int i = 0;
		for( Object o : getProperty() ) {
			javax.swing.JComponent component;
			if( o != null ) {
				component = this.createComponent( o );
			} else {
				component = new zoot.ZLabel( "null" );
			}
			this.add( component );
			java.awt.Component interstitial = this.createInterstitial( i, N );
			if( interstitial != null ) {
				this.add( interstitial );
			}
			i++;
		}
		this.addPostfixComponents();
		this.revalidate();
		this.repaint();
	}
}

