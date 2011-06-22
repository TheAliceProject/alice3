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
	private class CustomBlank extends org.lgna.croquet.CascadeBlank<T> {
		public CustomBlank() {
			super( java.util.UUID.fromString( "3fa6c08f-550d-4d80-b4a9-71c35c0fd186" ) );
		}
		@Override
		protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateChildren( java.util.List< org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode< T > blankNode ) {
			return CustomItemState.this.updateBlankChildren( rv, blankNode );
		}
	}
	public class CascadeCustomRoot extends org.lgna.croquet.CascadeRoot< T, org.lgna.croquet.history.CustomItemStateChangeStep< T > > {
		public CascadeCustomRoot( CascadeBlank< T >[] blanks ) {
			super( java.util.UUID.fromString( "8a973789-9896-443f-b701-4a819fc61d46" ), blanks );
		}
		@Override
		public org.lgna.croquet.history.CustomItemStateChangeStep< T > createCompletionStep( org.lgna.croquet.triggers.Trigger trigger ) {
			return org.lgna.croquet.history.TransactionManager.addCustomItemStateChangeStep( CustomItemState.this, trigger );
		}
		@Override
		public java.lang.Class< T > getComponentType() {
			return CustomItemState.this.getItemCodec().getValueClass();
		}
		@Override
		public CustomItemState< T > getCompletionModel() {
			return CustomItemState.this;
		}
		@Override
		public void prologue() {
		}
		@Override
		public void epilogue() {
		}
		@Override
		protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CustomItemStateChangeStep< T > completionStep, T[] values) {
			return new org.lgna.croquet.edits.CustomItemStateEdit( completionStep, CustomItemState.this.getValue(), values[ 0 ] );
		}
	}
	private final CascadeCustomRoot root;
	public CustomItemState( org.lgna.croquet.Group group, java.util.UUID id, org.lgna.croquet.ItemCodec< T > itemCodec ) {
		super( group, id, itemCodec );
		this.root = new CascadeCustomRoot( new org.lgna.croquet.CascadeBlank[] { new CustomBlank() } );
	}
	public CascadeCustomRoot getCascadeRoot() {
		return this.root;
	}
	@Override
	protected void localize() {
	}
	protected abstract java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode< T > blankNode );
	public abstract void setValue( T value );
}
