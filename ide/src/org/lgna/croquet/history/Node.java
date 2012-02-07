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
package org.lgna.croquet.history;

/**
 * @author Dennis Cosgrove
 */
public abstract class Node<P extends Node<?>> implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private final java.util.List<org.lgna.croquet.history.event.Listener> listeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	
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
	private final java.util.Map/*< Key<T>, T >*/ dataMap = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private P parent;
	public Node( P parent ) {
		this.setParent( parent );
	}
	public Node( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
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
	
	public P getParent() {
		return this.parent;
	}
	/*package-private*/ void setParent( P parent ) {
		this.parent = parent;
	}

	protected abstract void appendContexts( java.util.List< org.lgna.croquet.Context > out );
	
	private <N extends Node<?>> N getFirstAncestorAssignableTo( Class<N> cls, boolean isThisIncludedInSearch ) {
		Node<?> rv;
		if( isThisIncludedInSearch ) {
			rv = this;
		} else {
			rv = this.getParent();
		}
		while( rv != null ) {
			if( cls.isAssignableFrom( rv.getClass() ) ) {
				break;
			}
			rv = rv.getParent();
		}
		return (N)rv;
	}
	public final <N extends Node<?>> N getFirstAncestorAssignableTo( Class<N> cls ) {
		return this.getFirstAncestorAssignableTo( cls, false );
	}
	
	public final <S extends Step<M>, M extends org.lgna.croquet.Model> S getFirstAncestorStepOfEquivalentModel( M model, Class<S> stepCls ) {
		S step = this.getFirstAncestorAssignableTo( stepCls );
		if( step != null ) {
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( step.getModel(), model ) ) {
				return step;
			} else {
				return step.getFirstAncestorStepOfEquivalentModel( model, stepCls );
			}
		} else {
			return null;
		}
	}
	public final <S extends Step<? super M>, M extends org.lgna.croquet.Model> S getFirstAncestorStepOfModelAssignableTo( Class<M> modelCls, Class<S> stepCls ) {
		S step = this.getFirstAncestorAssignableTo( stepCls );
		if( step != null ) {
			org.lgna.croquet.Model m = step.getModel();
			if( m != null ) {
				if( modelCls.isAssignableFrom( m.getClass() ) ) {
					return step;
				} else {
					return step.getFirstAncestorStepOfModelAssignableTo( modelCls, stepCls );
				}
			} else {
				//todo: return null?
				return step.getFirstAncestorStepOfModelAssignableTo( modelCls, stepCls );
			}
		} else {
			return null;
		}
	}
	
	
	public void addListener( org.lgna.croquet.history.event.Listener listener ) {
		this.listeners.add( listener );
	}
	public void removeListener( org.lgna.croquet.history.event.Listener listener ) {
		this.listeners.remove( listener );
	}
	protected void fireChanging( org.lgna.croquet.history.event.Event<?> e ) {
		if( this.parent != null ) {
			this.parent.fireChanging( e );
		}
		for( org.lgna.croquet.history.event.Listener listener : this.listeners ) {
			listener.changing( e );
		}
	}
	protected void fireChanged( org.lgna.croquet.history.event.Event<?> e ) {
		if( this.parent != null ) {
			this.parent.fireChanged( e );
		}
		for( org.lgna.croquet.history.event.Listener listener : this.listeners ) {
			listener.changed( e );
		}
	}
}
	