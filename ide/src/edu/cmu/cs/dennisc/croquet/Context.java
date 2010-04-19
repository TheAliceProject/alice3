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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class Context< O extends Operation > {
	private CompositeContext parent;
	private O operation;
	private java.util.EventObject event;
	private CancelEffectiveness cancelEffectiveness;

	public enum State {
		COMMITTED,
		SKIPPED,
		CANCELLED,
		PENDING
	}
//	public static class Key<V> {
//		private String name;
//		private Key( String name ) {
//			this.name = name;
//		}
//		@Override
//		public String toString() {
//			return this.name;
//		}
//	};
//	public static <V> Key<V> createKey( String name ) {
//		return new Key( name );
//	}
//	private java.util.Map< Key, Object > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
//	public <V> V get( Key<V> key ) {
//		return (V)this.map.get( key );
//	}
//	public <V> void put( Key<V> key, V value ) {
//		this.map.put( key, value );
//	}

	public Context( CompositeContext parent, O operation, java.util.EventObject event, CancelEffectiveness cancelEffectiveness ) {
		this.parent = parent;
		this.operation = operation;
		this.event = event;
		this.cancelEffectiveness = cancelEffectiveness;
	}
	public Context<?> getParent() {
		return this.parent;
	}
	public O getOperation() {
		return this.operation;
	}
	public java.util.EventObject getEvent() {
		return event;
	}
	public boolean isCancelWorthwhile() {
		return this.cancelEffectiveness == CancelEffectiveness.WORTHWHILE;
	}

	public abstract State getState();
	public final boolean isCommitted() {
		return this.getState() == State.COMMITTED;
	}
	public final boolean isCancelled() {
		return this.getState() == State.CANCELLED;
	}
}
