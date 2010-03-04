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
package edu.cmu.cs.dennisc.javax.swing;

/**
 * @author Dennis Cosgrove
 */
public class ListPropertyListModel< E > extends javax.swing.AbstractListModel {
	private edu.cmu.cs.dennisc.property.ListProperty< E > listProperty;
	private ListPropertyListModel( edu.cmu.cs.dennisc.property.ListProperty< E > listProperty ) {
		this.listProperty = listProperty;
	}
	public static <E> ListPropertyListModel< E > createInstance( edu.cmu.cs.dennisc.property.ListProperty< E > listProperty ) {
		return new ListPropertyListModel( listProperty );
	}
	public int getSize() {
		return this.listProperty.size();
	}
	public E getElementAt( int index ) {
		return this.listProperty.get( index );
	}
	public void add( int index, E e ) {
		this.listProperty.add( index, e );
		this.fireIntervalAdded( this, index, index );
	}
	public void remove( int index, E e ) {
		assert this.listProperty.get( index ) == e;
		this.listProperty.remove( index );
		this.fireIntervalRemoved( this, index, index );
	}
	public void set( int index, E... es ) {
		if( es.length > 0 ) {
			this.listProperty.set( index, es );
			this.fireContentsChanged( this, index, index+es.length-1 );
		}
	}
}
