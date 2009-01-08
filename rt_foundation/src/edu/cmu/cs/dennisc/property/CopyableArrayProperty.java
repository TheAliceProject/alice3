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
package edu.cmu.cs.dennisc.property;

/**
 * @author Dennis Cosgrove
 */
public abstract class CopyableArrayProperty<E> extends ArrayProperty< E > implements CopyableProperty< E[] > {
	public CopyableArrayProperty( InstancePropertyOwner owner, E... value ) {
		super( owner, value );
	}
	protected abstract E[] createArray( int length );
	protected abstract E createCopy( E e );

	public E[] getCopy( E[] rv, edu.cmu.cs.dennisc.property.PropertyOwner owner ) {
		E[] value = getValue();
		for( int i=0; i<value.length; i++ ) {
			rv[ i ] = createCopy( value[ i ] );
		}
		return rv;
	}
	public E[] getCopy( edu.cmu.cs.dennisc.property.PropertyOwner owner ) {
		return getCopy( createArray( getLength() ), owner );
	}
	public void setCopy( edu.cmu.cs.dennisc.property.PropertyOwner owner, E[] value ) {
		E[] dst = createArray( value.length );
		for( int i=0; i<value.length; i++ ) {
			dst[ i ] = createCopy( value[ i ] );
		}
		setValue( value );
	}
	public E[] getCopy( E[] rv ) {
		return getCopy( rv, getOwner() );
	}
	public E[] getCopy() {
		return getCopy( getOwner() );
	}
	public void setCopy( E[] value ) {
		setCopy( getOwner(), value );
	}
}
