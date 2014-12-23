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
package edu.cmu.cs.dennisc.property;

/**
 * @author Dennis Cosgrove
 */
public class InstanceProperty<T> {
	public InstanceProperty( InstancePropertyOwner owner, T value ) {
		this.owner = owner;
		this.value = value;
	}

	public String getName() {
		if( this.name != null ) {
			//pass
		} else {
			this.name = this.owner.lookupNameFor( this );
		}
		return this.name;
	}

	public void addPropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		assert propertyListener != null : this;
		this.propertyListeners.add( propertyListener );
	}

	public void removePropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		this.propertyListeners.remove( propertyListener );
	}

	public java.util.Collection<edu.cmu.cs.dennisc.property.event.PropertyListener> getPropertyListeners() {
		return java.util.Collections.unmodifiableCollection( this.propertyListeners );
	}

	private void firePropertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener : this.propertyListeners ) {
			propertyListener.propertyChanging( e );
		}
		InstancePropertyOwner owner = this.getOwner();
		if( owner != null ) {
			owner.firePropertyChanging( e );
		}
	}

	private void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener : this.propertyListeners ) {
			propertyListener.propertyChanged( e );
		}
		InstancePropertyOwner owner = this.getOwner();
		if( owner != null ) {
			owner.firePropertyChanged( e );
		}
	}

	public InstancePropertyOwner getOwner() {
		return this.owner;
	}

	public final T getValue() {
		return this.value;
	}

	public void setValue( T value ) {
		edu.cmu.cs.dennisc.property.event.PropertyEvent e = new edu.cmu.cs.dennisc.property.event.PropertyEvent( this, this.owner, value );
		firePropertyChanging( e );
		this.value = value;
		firePropertyChanged( e );
	}

	protected void writeValue( java.io.ObjectOutputStream oos ) throws java.io.IOException {
		assert ( this.value == null ) || ( this.value instanceof java.io.Serializable );
		oos.writeObject( this.value );
	}

	protected void readValue( java.io.ObjectInputStream ois ) throws java.io.IOException, ClassNotFoundException {
		this.value = (T)ois.readObject();
	}

	private void writeObject( java.io.ObjectOutputStream oos ) throws java.io.IOException {
		oos.defaultWriteObject();
		writeValue( oos );
	}

	private void readObject( java.io.ObjectInputStream ois ) throws java.io.IOException, ClassNotFoundException {
		ois.defaultReadObject();
		readValue( ois );
	}

	protected boolean isToBeIgnored( InstanceProperty<T> other, PropertyFilter filter ) {
		return ( filter != null ) && filter.isToBeIgnored( this, other );
	}

	public boolean valueEquals( InstanceProperty<T> other, PropertyFilter filter ) {
		if( this.isToBeIgnored( other, filter ) ) {
			return true;
		} else {
			T thisValue = this.getValue();
			T otherValue = other.getValue();
			if( thisValue != null ) {
				if( otherValue != null ) {
					return thisValue.equals( otherValue );
				} else {
					return false;
				}
			} else {
				if( otherValue != null ) {
					return false;
				} else {
					return true;
				}
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + "[owner=" + getOwner() + ";name=" + getName() + "]";
	}

	private final java.util.List<edu.cmu.cs.dennisc.property.event.PropertyListener> propertyListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final InstancePropertyOwner owner;
	private T value;
	private String name;
}
