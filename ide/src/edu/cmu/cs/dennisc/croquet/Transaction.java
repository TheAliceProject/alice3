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
public class Transaction implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private TransactionHistory parent;
	private final java.util.List< PrepStep<?> > prepSteps;
	private CompletionStep<?> completionStep;
	/*package-private*/ Transaction( TransactionHistory parent ) {
		this.setParent( parent );
		this.prepSteps = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
		this.completionStep = null;
	}
	public Transaction( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.prepSteps = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList( (PrepStep<?>[])binaryDecoder.decodeBinaryEncodableAndDecodableArray( PrepStep.class ) );
		for( PrepStep< ? > prepStep : this.prepSteps ) {
			prepStep.setParent( this );
		}
		this.completionStep = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.completionStep.setParent( this );
	}
	public TransactionHistory getParent() {
		return this.parent;
	}
	/*package-private*/ void setParent( TransactionHistory parent ) {
		this.parent = parent;
	}
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		throw new AssertionError();
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( (java.util.List)this.prepSteps, PrepStep.class ) );
		binaryEncoder.encode( this.completionStep );
	}

	public void addDragStep( DragAndDropModel model ) {
		this.addPrepStep( new DragStep( this, model ) ); 
	}
	public void addDropStep( CompletionModel model, Edit< CompletionModel > edit, DropReceptor dropReceptor ) {
		this.setCompletionStep( new DropStep( this, model, edit, dropReceptor ) );
	}
	public void addBooleanStateChangeStep( BooleanState model, BooleanStateEdit edit ) {
		this.setCompletionStep( new BooleanStateChangeStep( this, model, edit ) );
	}
	public <E> void addListSelectionStateChangeStep( ListSelectionState< E > model, ListSelectionStateEdit< E > edit ) {
		this.setCompletionStep( new ListSelectionStateChangeStep<E>( this, model, edit ) );
	}
	
	public java.util.ListIterator< PrepStep<?> > getPrepSteps() {
		return this.prepSteps.listIterator();
	}
	public int getIndexOfPrepStep( PrepStep<?> prepStep ) {
		return this.prepSteps.indexOf( prepStep );
	}	
	public PrepStep<?> getPrepStepAt( int i ) {
		return this.prepSteps.get( i );
	}
	public int getPrepStepCount() {
		return this.prepSteps.size();
	}

	private void addPrepStep( PrepStep< ? > step ) {
		TransactionManager.fireAddingStep( step );
		this.prepSteps.add( step );
		TransactionManager.fireAddedStep( step );
	}
//	public void removePrepStep( PrepStep< ? > step ) {
//		this.prepSteps.remove( step );
//	}
	public CompletionStep< ? > getCompletionStep() {
		return this.completionStep;
	}
	private void setCompletionStep( CompletionStep<?> step ) {
		TransactionManager.fireAddingStep( step );
		this.completionStep = step;
		TransactionManager.fireAddedStep( step );
	}
	public boolean isActive() {
		if( this.completionStep != null ) {
			return this.completionStep.isActive();
		} else {
			return true;
		}
	}
}
