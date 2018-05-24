/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.animation.DurationBasedAnimation;
import edu.cmu.cs.dennisc.animation.Style;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class Property<T> {
	public static interface Listener<T> {
		public void propertyChanged( Property<T> property, T prevValue, T nextValue );
	}

	public static String getPropertyNameForGetter( Method method ) {
		Class<?> valueClass = method.getReturnType();
		boolean isBoolean = valueClass.equals( Boolean.TYPE ) || valueClass.equals( Boolean.class );
		String prefix;
		if( isBoolean ) {
			prefix = "is";
		} else {
			prefix = "get";
		}
		String name = method.getName();
		if( name.startsWith( prefix ) ) {
			if( isBoolean ) {
				return name;
			} else {
				return name.substring( prefix.length() );
			}
		} else {
			return null;
		}
	}

	private final List<Listener<T>> listeners = Lists.newCopyOnWriteArrayList();
	private final PropertyOwnerImp owner;
	private final Class<T> valueCls;

	public Property( PropertyOwnerImp owner, Class<T> valueCls ) {
		this.owner = owner;
		this.valueCls = valueCls;
	}

	public PropertyOwnerImp getOwner() {
		return this.owner;
	}

	public Class<T> getValueCls() {
		return this.valueCls;
	}

	public abstract T getValue();

	protected abstract void handleSetValue( T value );

	protected abstract T interpolate( T a, T b, double portion );

	public final void setValue( T value ) {
		T prevValue = this.getValue();
		this.handleSetValue( value );
		this.fireChanged( prevValue, value );
	}

	public void animateValue( final T value, double duration, Style style ) {
		duration = this.owner.adjustDurationIfNecessary( duration );
		if( EpsilonUtilities.isWithinReasonableEpsilon( duration, EntityImp.RIGHT_NOW ) ) {
			this.setValue( value );
		} else {
			final T value0 = this.getValue();
			this.owner.perform( new DurationBasedAnimation( duration, style ) {
				@Override
				protected void prologue() {
				}

				@Override
				protected void setPortion( double portion ) {
					Property.this.setValue( Property.this.interpolate( value0, value, portion ) );
				}

				@Override
				protected void epilogue() {
					Property.this.setValue( value );
				}
			} );
		}
	}

	public void animateValue( T value, double duration ) {
		this.animateValue( value, duration, EntityImp.DEFAULT_STYLE );
	}

	public void animateValue( T value ) {
		this.animateValue( value, EntityImp.DEFAULT_DURATION );
	}

	protected void fireChanged( T prevValue, T nextValue ) {
		for( Listener<T> listener : listeners ) {
			listener.propertyChanged( this, prevValue, nextValue );
		}
	}

	public void addPropertyListener( Listener<T> listener ) {
		this.listeners.add( listener );
	}

	public void removePropertyListener( Listener<T> listener ) {
		this.listeners.remove( listener );
	}
}
