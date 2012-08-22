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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public abstract class Element extends edu.cmu.cs.dennisc.pattern.DefaultInstancePropertyOwner {
	public static class Key<T> {
		public static <T> Key<T> createInstance( String repr ) {
			return new Key<T>( repr );
		}

		private final String repr;

		private Key( String repr ) {
			this.repr = repr;
		}

		@Override
		public java.lang.String toString() {
			return this.repr;
		}
	}

	public static final Key<StackTraceElement[]> DEBUG_CONSTRUCTION_STACK_TRACE_KEY = Key.createInstance( "DEBUG_CONSTRUCTION_STACK_TRACE_KEY" );
	private static boolean isCreationStackTraceDesired = edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "edu.cmu.cs.dennisc.scenegraph.Element.isCreationStackTraceDesired" );

	private final java.util.Map/* < Key<T>, T > */dataMap = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public Element() {
		if( isCreationStackTraceDesired ) {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			this.putBonusDataFor( DEBUG_CONSTRUCTION_STACK_TRACE_KEY, stackTrace );
		}
	}

	@Override
	public boolean isComposedOfGetterAndSetterProperties() {
		return false;
	}

	public <T> boolean containsBonusDataFor( Key<T> key ) {
		return this.dataMap.containsKey( key );
	}

	public <T> T getBonusDataFor( Key<T> key ) {
		return (T)this.dataMap.get( key );
	}

	public <T> void putBonusDataFor( Key<T> key, T value ) {
		this.dataMap.put( key, value );
	}

	public <T> void removeBonusDataFor( Key<T> key ) {
		this.dataMap.remove( key );
	}

	//todo: investigate typing return value with generics
	//todo: support copying referenced elements?
	public Element newCopy() {
		Element rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( this.getClass() );
		rv.setName( this.getName() );
		for( edu.cmu.cs.dennisc.property.Property property : this.getProperties() ) {
			Object value;
			if( property instanceof edu.cmu.cs.dennisc.property.CopyableProperty ) {
				value = ( (edu.cmu.cs.dennisc.property.CopyableProperty)property ).getCopy( this );
			} else {
				value = property.getValue( this );
			}
			edu.cmu.cs.dennisc.property.Property rvProperty = rv.getPropertyNamed( property.getName() );
			rvProperty.setValue( rv, value );
		}
		return rv;
	}

	protected void appendRepr( StringBuilder sb ) {
		sb.append( "name=\"" + getName() + "\"" );
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		this.appendRepr( sb );
		sb.append( "]" );
		return sb.toString();
	}
}
