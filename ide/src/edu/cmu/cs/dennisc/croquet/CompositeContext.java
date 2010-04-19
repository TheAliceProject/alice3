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
public class CompositeContext extends Context< CompositeOperation > {
	private java.util.List< Context<?> > children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	public CompositeContext( CompositeContext parent, CompositeOperation operation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness ) {
		super( parent, operation, e, cancelEffectiveness );
	}
	@Override
	public State getState() {
		final int N = this.children.size();
		if( N > 0 ) {
			return this.children.get( N-1 ).getState();
		} else {
			return null;
		}
	}
	public CompositeContext getCurrentCompositeActionContext() {
		if( this.children.size() > 0 ) {
			Context<?> lastChildContext = this.children.get( this.children.size()-1 );
			if( lastChildContext instanceof CompositeContext ) {
				CompositeContext lastChildCompositeContext = (CompositeContext)lastChildContext;
				State state = lastChildContext.getState();
				if( state != null && state != State.PENDING ) {
					return this;
				} else {
					return lastChildCompositeContext.getCurrentCompositeActionContext();
				}
			} else {
				return this;
			}
		} else {
			return this;
		}
	}
	
	private boolean isGoodToGo() {
		if( this.children.size() > 0 ) {
			Context<?> lastChildContext = this.children.get( this.children.size()-1 );
			State state = lastChildContext.getState();
			return state != null && state != State.PENDING;
		} else {
			return true;
		}
	}

	private static boolean isGoodToReturn( Context<?> context ) {
		State state = context.getState();
		if( state != null ) {
			if( state == State.PENDING ) {
				//todo? handle pend
			}
			return true;
		} else {
			return false;
		}
		
	}
	public CompositeContext performInChildContext( CompositeOperation compositeOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness ) {
		assert compositeOperation != null;
		assert this.isGoodToGo();
		CompositeContext rv = new CompositeContext( this, compositeOperation, e, cancelEffectiveness);
		this.children.add( rv );
		compositeOperation.perform(rv);
		assert isGoodToReturn( rv );
		return rv;
	}
	public ActionContext performInChildContext( ActionOperation actionOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness ) {
		assert actionOperation != null;
		assert this.isGoodToGo();
		ActionContext rv = new ActionContext( this, actionOperation, e, cancelEffectiveness);
		this.children.add( rv );
		actionOperation.perform(rv);
		assert isGoodToReturn( rv );
		return rv;
	}

	public BooleanStateContext performInChildContext(BooleanStateOperation stateOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness, Boolean previousValue, Boolean nextValue) {
		assert stateOperation != null;
		assert this.isGoodToGo();
		BooleanStateContext rv = new BooleanStateContext(this, stateOperation, e, cancelEffectiveness, previousValue, nextValue);
		this.children.add( rv );
		stateOperation.performStateChange(rv);
		assert isGoodToReturn( rv );
		return rv;
	}
	
	public <E> ItemSelectionContext<E> performInChildContext(ItemSelectionOperation<E> itemSelectionOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness, E previousSelection, E nextSelection) {
		assert itemSelectionOperation != null;
		assert this.isGoodToGo();
		ItemSelectionContext<E> rv = new ItemSelectionContext<E>(this, itemSelectionOperation, e, cancelEffectiveness, previousSelection, nextSelection);
		this.children.add( rv );
		itemSelectionOperation.performSelectionChange(rv);
		assert isGoodToReturn( rv );
		return rv;
	}
	
	public BoundedRangeContext performInChildContext(BoundedRangeOperation boundedRangeOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness) {
		assert boundedRangeOperation != null;
		assert this.isGoodToGo();
		BoundedRangeContext rv = new BoundedRangeContext(this, boundedRangeOperation, e, cancelEffectiveness);
		this.children.add( rv );
		boundedRangeOperation.perform(rv);
		assert isGoodToReturn( rv );
		return rv;
	}
	
	//todo
	@Deprecated
	public void commit() {
		throw new AssertionError();
	}
	//todo
	@Deprecated
	public void cancel() {
		throw new AssertionError();
	}
}
