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
public abstract class AbstractPropertyPane< E extends edu.cmu.cs.dennisc.property.InstanceProperty > extends edu.cmu.cs.dennisc.croquet.swing.BoxPane {
	private Factory factory;
	private E property;
	private edu.cmu.cs.dennisc.property.event.PropertyListener propertyAdapter = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
		};
		public void propertyChanged(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
			AbstractPropertyPane.this.refresh();
		};
	};
	public AbstractPropertyPane( Factory factory, int direction, E property ) {
		super( direction );
		assert property != null;
		this.factory = factory;
		this.property = property;
		this.refresh();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.property.addPropertyListener( this.propertyAdapter );
	}
	@Override
	public void removeNotify() {
		this.property.removePropertyListener( this.propertyAdapter );
		super.removeNotify();
	}
	
	protected Factory getFactory() {
		return this.factory;
	}
	
	public E getProperty() {
		return this.property;
	}
	protected abstract void refresh();
}
