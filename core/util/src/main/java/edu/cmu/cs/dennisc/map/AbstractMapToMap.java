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
package edu.cmu.cs.dennisc.map;

import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractMapToMap<A, B, V> {
	private final InitializingIfAbsentMap<A, InitializingIfAbsentMap<B, V>> outerMap = Maps.newInitializingIfAbsentHashMap();

	public static interface Initializer<A, B, V> {
		public V initialize( A a, B b );
	}

	private final InitializingIfAbsentMap.Initializer<A, InitializingIfAbsentMap<B, V>> mapInitializer = new InitializingIfAbsentMap.Initializer<A, InitializingIfAbsentMap<B, V>>() {
		@Override
		public InitializingIfAbsentMap<B, V> initialize( A key ) {
			return Maps.newInitializingIfAbsentHashMap();
		}
	};

	public V get( A a, B b ) {
		Map<B, V> innerMap = this.outerMap.get( a );
		if( innerMap != null ) {
			return innerMap.get( b );
		} else {
			return null;
		}
	}

	public final synchronized V getInitializingIfAbsent( final A a, B b, final Initializer<A, B, V> initializer ) {
		InitializingIfAbsentMap<B, V> innerMap = this.outerMap.getInitializingIfAbsent( a, this.mapInitializer );
		return innerMap.getInitializingIfAbsent( b, new InitializingIfAbsentMap.Initializer<B, V>() {
			@Override
			public V initialize( B key ) {
				return initializer.initialize( a, key );
			}
		} );
	}

	public final void put( A a, B b, V value ) {
		Map<B, V> innerMap = this.outerMap.getInitializingIfAbsent( a, new InitializingIfAbsentMap.Initializer<A, InitializingIfAbsentMap<B, V>>() {
			@Override
			public InitializingIfAbsentMap<B, V> initialize( A key ) {
				return Maps.newInitializingIfAbsentHashMap();
			}
		} );
		innerMap.put( b, value );
	}

	public final Collection<V> values() {
		List<V> rv = Lists.newLinkedList();
		for( Map<B, V> innerMap : this.outerMap.values() ) {
			rv.addAll( innerMap.values() );
		}
		return rv;
	}
}
