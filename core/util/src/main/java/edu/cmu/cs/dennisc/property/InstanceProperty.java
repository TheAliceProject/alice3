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
public class InstanceProperty<E> implements Property<E> {
	private InstancePropertyOwner m_owner;
	private String m_name;
	private transient E m_value;
	//private boolean m_isLocked = false;
	private final java.util.List<edu.cmu.cs.dennisc.property.event.PropertyListener> propertyListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	public InstanceProperty( InstancePropertyOwner owner, E value ) {
		m_owner = owner;
		m_value = value;
	}

	@Override
	public String getName() {
		if( m_name != null ) {
			//pass
		} else {
			m_name = m_owner.lookupNameFor( this );
		}
		return m_name;
	}

	public void addPropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		assert propertyListener != null : this;
		this.propertyListeners.add( propertyListener );
	}

	public void removePropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		this.propertyListeners.remove( propertyListener );
	}

	public Iterable<edu.cmu.cs.dennisc.property.event.PropertyListener> accessPropertyListeners() {
		return this.propertyListeners;
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
		return m_owner;
	}

	@Override
	public E getValue( InstancePropertyOwner owner ) {
		assert m_owner == owner : this;
		return m_value;
	}

	@Override
	public void setValue( InstancePropertyOwner owner, E value ) {
		assert m_owner == owner;
		//assert m_isLocked == false;
		edu.cmu.cs.dennisc.property.event.PropertyEvent e = new edu.cmu.cs.dennisc.property.event.PropertyEvent( this, owner, value );
		firePropertyChanging( e );
		m_value = value;
		firePropertyChanged( e );
	}

	//	public boolean isLocked() {
	//		return m_isLocked;
	//	}
	//	public void setLocked( boolean isLocked ) {
	//		m_isLocked = isLocked;
	//	}

	public final E getValue() {
		return getValue( m_owner );
	}

	public final void setValue( E value ) {
		setValue( m_owner, value );
	}

	protected void writeValue( java.io.ObjectOutputStream oos ) throws java.io.IOException {
		assert ( m_value == null ) || ( m_value instanceof java.io.Serializable );
		oos.writeObject( m_value );
	}

	protected void readValue( java.io.ObjectInputStream ois ) throws java.io.IOException, ClassNotFoundException {
		m_value = (E)ois.readObject();
	}

	private void writeObject( java.io.ObjectOutputStream oos ) throws java.io.IOException {
		oos.defaultWriteObject();
		writeValue( oos );
	}

	private void readObject( java.io.ObjectInputStream ois ) throws java.io.IOException, ClassNotFoundException {
		ois.defaultReadObject();
		readValue( ois );
	}

	protected boolean isToBeIgnored( InstanceProperty<E> other, PropertyFilter filter ) {
		return ( filter != null ) && filter.isToBeIgnored( this, other );
	}

	public boolean valueEquals( InstanceProperty<E> other, PropertyFilter filter ) {
		if( this.isToBeIgnored( other, filter ) ) {
			return true;
		} else {
			E thisValue = this.getValue();
			E otherValue = other.getValue();
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
}
