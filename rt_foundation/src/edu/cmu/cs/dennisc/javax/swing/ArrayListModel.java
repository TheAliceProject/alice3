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
public class ArrayListModel extends javax.swing.AbstractListModel {
	private Object array;
	private ArrayListModel( Object array ) {
		if( array != null ) {
			assert array.getClass().isArray();
		}
		this.array = array;
	}
	public ArrayListModel( Object[] array ) {
		this( (Object)array );
	}
	public ArrayListModel( boolean[] array ) {
		this( (Object)array );
	}
	public ArrayListModel( char[] array ) {
		this( (Object)array );
	}
	public ArrayListModel( short[] array ) {
		this( (Object)array );
	}
	public ArrayListModel( int[] array ) {
		this( (Object)array );
	}
	public ArrayListModel( long[] array ) {
		this( (Object)array );
	}
	public ArrayListModel( float[] array ) {
		this( (Object)array );
	}
	public ArrayListModel( double[] array ) {
		this( (Object)array );
	}
	public Object getElementAt( int index ) {
		return java.lang.reflect.Array.get( this.array, index );
	}
	public int getSize() {
		return java.lang.reflect.Array.getLength( this.array );
	}
}
