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

package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class ReflectionProxy<E> {
	private boolean isReificationNecessary;
	private E reification;
	public ReflectionProxy() {
		isReificationNecessary = true;
	}
	public ReflectionProxy( E reification ) {
		this.reification = reification;
		isReificationNecessary = false;
	}
	protected abstract E reify();
	public E getReification() {
		if( isReificationNecessary ) {
			this.reification = this.reify();
			isReificationNecessary = false;
		}
		return this.reification;
	}
	
	protected abstract int hashCodeNonReifiable(); 
	protected abstract boolean equalsInstanceOfSameClassButNonReifiable( ReflectionProxy< ? > o ); 
	
	@Override
	public final int hashCode() {
		E e = this.getReification();
		if( e != null ) {
			return e.hashCode();
		} else {
			return this.hashCodeNonReifiable();
		}
	}
	@Override
	public final boolean equals( Object o ) {
		if( this == o ) {
			return true;
		} else {
			if( o instanceof ReflectionProxy< ? > ) {
				ReflectionProxy< ? > other = (ReflectionProxy< ? >)o;
				E e = this.getReification();
				if( e != null ) {
					return e.equals( other.getReification() );
				} else {
					if( this.getClass().equals( other.getClass() ) ) {
						return this.equalsInstanceOfSameClassButNonReifiable( other );
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		}
	}
}
