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
package edu.cmu.cs.dennisc.property.event;

/**
 * @author Dennis Cosgrove
 */
public abstract class SimplifiedListPropertyAdapter<E> implements ListPropertyListener<E> {

	protected abstract void changing( ListPropertyEvent<E> e );
	protected abstract void changed( ListPropertyEvent<E> e );
	
	public final void adding( AddListPropertyEvent< E > e ) {
		this.changing( e );
	}
	public final void added( AddListPropertyEvent< E > e ) {
		this.changed( e );
	}


	public final void clearing( ClearListPropertyEvent< E > e ) {
		this.changing( e );
	}
	public final void cleared( ClearListPropertyEvent< E > e ) {
		this.changed( e );
	}


	public final void removing( RemoveListPropertyEvent< E > e ) {
		this.changing( e );
	}
	public final void removed( RemoveListPropertyEvent< E > e ) {
		this.changed( e );
	}


	public final void setting( SetListPropertyEvent< E > e ) {
		this.changing( e );
	}
	public final void set( SetListPropertyEvent< E > e ) {
		this.changed( e );
	}

}
