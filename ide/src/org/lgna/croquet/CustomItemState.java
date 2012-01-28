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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class CustomItemState< T > extends ItemState< T > {
	public static class InternalRootResolver<T> extends IndirectResolver< InternalRoot<T>, CustomItemState<T> > {
		private InternalRootResolver( CustomItemState<T> indirect ) {
			super( indirect );
		}
		public InternalRootResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalRoot<T> getDirect( CustomItemState<T> indirect ) {
			return indirect.getCascadeRoot();
		}
	}

	public static class InternalRoot< T > extends org.lgna.croquet.CascadeRoot< T, org.lgna.croquet.history.StateChangeStep< T > > {
		private final CustomItemState< T > state;
		private InternalRoot( CustomItemState< T > state, CascadeBlank< T >... blanks ) {
			super( java.util.UUID.fromString( "8a973789-9896-443f-b701-4a819fc61d46" ), blanks );
			this.state = state;
		}
		@Override
		protected InternalRootResolver<T> createResolver() {
			return new InternalRootResolver<T>( this.state );
		}
		@Override
		public org.lgna.croquet.history.StateChangeStep< T > createCompletionStep( org.lgna.croquet.triggers.Trigger trigger ) {
			return org.lgna.croquet.history.TransactionManager.addStateChangeStep( this.state, trigger );
		}
		@Override
		public Class< T > getComponentType() {
			return this.state.getItemCodec().getValueClass();
		}
		@Override
		public CustomItemState< T > getCompletionModel() {
			return this.state;
		}
		@Override
		public void prologue() {
		}
		@Override
		public void epilogue() {
		}
		@Override
		protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.StateChangeStep< T > completionStep, T[] values) {
			return this.state.createEdit( completionStep, values[ 0 ] );
		}
	}
	private final InternalRoot<T> root;
	public CustomItemState( org.lgna.croquet.Group group, java.util.UUID id, org.lgna.croquet.ItemCodec< T > itemCodec, CascadeBlank< T >... blanks ) {
		super( group, id, null, itemCodec );
		this.root = new InternalRoot<T>( this, blanks );
	}
	public InternalRoot<T> getCascadeRoot() {
		return this.root;
	}
	@Override
	protected void localize() {
	}
}
