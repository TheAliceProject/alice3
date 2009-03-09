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
package alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractPropertyPane<E extends edu.cmu.cs.dennisc.property.InstanceProperty< ? > > extends edu.cmu.cs.dennisc.moot.ZBoxPane {
	private E property;
	private edu.cmu.cs.dennisc.property.event.PropertyListener propertyAdapter = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
		};
		public void propertyChanged(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
			AbstractPropertyPane.this.refresh();
		};
	};
	public AbstractPropertyPane( int direction ) {
		super( direction );
	}
	public AbstractPropertyPane( int direction, E property ) {
		super( direction );
		this.setProperty( property );
	}
	public E getProperty() {
		return this.property;
	}
	public void setProperty( E property ) {
		if( this.property != null ) {
			this.property.removePropertyListener( this.propertyAdapter );
		}
		this.property = property;
		if( this.property != null ) {
			this.property.addPropertyListener( this.propertyAdapter );
		}
		this.refresh();
	}
	protected abstract void refresh();
}
