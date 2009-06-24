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
public interface PropertyOwner {
	public Iterable< Property< ? > > getProperties();
	public Property< ? > getPropertyNamed( String name );
	public void firePropertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e );
	public void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e );
	public void fireAdding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<?> e );
	public void fireAdded( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<?> e );
	public void fireClearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<?> e );
	public void fireCleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<?> e );
	public void fireRemoving( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<?> e );
	public void fireRemoved( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<?> e );
	public void fireSetting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<?> e );
	public void fireSet( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<?> e );
}
