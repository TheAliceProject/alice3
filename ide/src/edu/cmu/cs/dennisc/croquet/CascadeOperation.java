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

class RtNode<M extends Model, C extends ModelContext< M > > {
	private M model;
	private C context;
	public RtNode( M model, C context ) {
		this.model = model;
		this.context = context;
	}
	public M getModel() {
		return this.model;
	}
	public C getContext() {
		return this.context;
	}
}

class RtBlank< T > extends RtNode< CascadeBlank< T >, CascadeBlankContext< T > > {
	private final RtFillIn< T >[] rtFillIns;
	private RtFillIn< T > rtSelectedFillIn;
	public RtBlank( CascadeBlank< T > model ) {
		super( model, ContextManager.createCascadeBlankContext( model ) );
		java.util.List< RtFillIn< T > > allRtFillIns = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		for( CascadeFillIn< T > fillIn : this.getModel().getFillIns() ) {
			allRtFillIns.add( ( new RtFillIn< T >( fillIn ) ) );
		}
		
		java.util.ListIterator< RtFillIn< T > > listIterator = allRtFillIns.listIterator();
		while( listIterator.hasNext() ) {
			RtFillIn< T > rtFillIn = listIterator.next();
			if( rtFillIn.isInclusionDesired() ) {
				//pass
			} else {
				listIterator.remove();
			}
		}
	
		this.rtFillIns = (RtFillIn< T >[])edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( (java.util.List)allRtFillIns, RtFillIn.class );
	}
	
	private static <T> boolean isEmptySeparator( RtFillIn< T > rtFillIn ) {
		 if( rtFillIn.getModel() instanceof SeparatorFillIn ) {
			 SeparatorFillIn separatorFillIn = (SeparatorFillIn)rtFillIn.getModel();
			 return separatorFillIn.isEmpty();
		 } else {
			 return false;
		 }
	}
	private static <T> void cleanUpSeparators( java.util.List< RtFillIn< T > > rtFillIns ) {
		 java.util.ListIterator< RtFillIn< T > > listIterator = rtFillIns.listIterator();
		 boolean isSeparatorAcceptable = false;
		 while( listIterator.hasNext() ) {
			 RtFillIn< T > rtFillIn = listIterator.next();
			 if( isEmptySeparator( rtFillIn ) ) {
				 if( isSeparatorAcceptable ) {
					//pass 
				 } else {
					 listIterator.remove();
				 }
				 isSeparatorAcceptable = false;
			 } else {
				 isSeparatorAcceptable = true;
			 }
		 }
		 
		 //remove separators at the end
		 //there should be a maximum of only 1 but we loop anyway 
		 final int N = rtFillIns.size();
		 for( int i=0; i<N; i++ ) {
			 int index = N-i-1;
			 if( isEmptySeparator( rtFillIns.get( index ) ) ) {
				 rtFillIns.remove( index );
			 } else {
				 break;
			 }
		 }
	}
	public boolean isFillInAlreadyDetermined() {
		return this.rtSelectedFillIn != null;
	}
	public T createValue() {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.createValue();
		} else {
			throw new RuntimeException();
		}
	}
}

class RtBlankOwner<T, M extends CascadeBlankOwner< T >, C extends CascadeBlankOwnerContext< M, T > > extends RtNode<M, C> {
	private RtBlank< T >[] rtBlanks;
	public RtBlankOwner( M model, C context ) {
		super( model, context );
		CascadeBlank< T >[] blanks = model.getBlanks();
		this.rtBlanks = new RtBlank[ blanks.length ];
		for( int i=0; i<this.rtBlanks.length; i++ ) {
			this.rtBlanks[ i ] = new RtBlank< T >( blanks[ i ] );
		}
	}
	protected RtBlank< T >[] getRtBlanks() {
		return this.rtBlanks;
	}
	protected boolean isGoodToGo() {
		if( this.rtBlanks.length > 0 ) {
			for( RtBlank< T > rtBlank : this.rtBlanks ) {
				if( rtBlank.isFillInAlreadyDetermined() ) {
					//pass
				} else {
					return false;
				}
			}
		}
		return true;
	}
}

class RtFillIn< T > extends RtBlankOwner< T, CascadeFillIn< T >, CascadeFillInContext< T > > {
	public RtFillIn( CascadeFillIn< T > model ) {
		super( model, ContextManager.createCascadeFillInContext( model ) );
	}
	public boolean isInclusionDesired() {
		return this.getModel().isInclusionDesired( this.getContext() );
	}
	public T createValue() {
		return this.getModel().createValue( this.getContext() );
	}
}

class RtOperation< T > extends RtBlankOwner< T, CascadeOperation< T >, CascadeOperationContext< T > > {
	private final Operation.PerformObserver performObserver;
	public RtOperation( CascadeOperation< T > model, CascadeOperationContext<T> context, Operation.PerformObserver performObserver ) {
		super( model, context );
		this.performObserver = performObserver;
	}

	protected T[] createValues( Class<T> componentType ) {
		RtBlank< T >[] rtBlanks = this.getRtBlanks();
		T[] rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newTypedArrayInstance( componentType, rtBlanks.length );
		for( int i=0; i<rtBlanks.length; i++ ) {
			rv[ i ] = rtBlanks[ i ].createValue();
		}
		return rv;
	}

	public void perform() {
		if( this.isGoodToGo() ) {
			T[] values = this.createValues( this.getModel().getComponentType() );
			this.getModel().handleCompletion( this.getContext(), performObserver, values );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: perform ", this );
		}
	}
}


/**
 * @author Dennis Cosgrove
 */
public abstract class CascadeOperation< T > extends Operation< CascadeOperationContext< T > > implements CascadeBlankOwner< T > {
	private final Class<T> componentType;
	public CascadeOperation( Group group, java.util.UUID id, Class<T> componentType ) {
		super( group, id );
		this.componentType = componentType;
	}
	@Override
	public CascadeOperationContext< T > createAndPushContext( java.util.EventObject e, ViewController< ?, ? > viewController ) {
		return ContextManager.createAndPushCascadeOperationContext( this, e, viewController );
	}
	
	public Class< T > getComponentType() {
		return this.componentType;
	}
	@Override
	protected void localize() {
	}
	@Override
	public boolean isAlreadyInState( Edit< ? > edit ) {
		//todo?
		return false;
	}
	
	public abstract CascadeBlank< T >[] getBlanks();
	protected abstract Edit< CascadeOperation< T > > createEdit( T[] values );
	
	/*package-private*/ void handleCompletion( CascadeOperationContext<T> context, PerformObserver performObserver, T[] values ) {
		try {
			Edit< CascadeOperation< T > > edit = this.createEdit( values );
			context.commitAndInvokeDo( edit );
		} finally {
			performObserver.handleFinally();
		}
	}

	@Override
	protected void perform( CascadeOperationContext<T> context, PerformObserver performObserver ) {
		RtOperation< T > rt = new RtOperation< T >( this, context, performObserver );
		rt.perform();
	}
}
