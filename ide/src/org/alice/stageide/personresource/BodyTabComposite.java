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

package org.alice.stageide.personresource;

/**
 * @author Dennis Cosgrove
 */
public class BodyTabComposite extends BodyOrHeadTabComposite<org.alice.stageide.personresource.views.BodyTabView> {
	private static class SingletonHolder {
		private static BodyTabComposite instance = new BodyTabComposite();
	}

	public static BodyTabComposite getInstance() {
		return SingletonHolder.instance;
	}

	private class SetObesityLevelAction implements Action {
		private final double value;

		public SetObesityLevelAction( double value ) {
			this.value = value;
		}

		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			obesityLevelState.setValueTransactionlessly( this.value );
			return null;
		}
	}

	private final org.lgna.croquet.BoundedDoubleState obesityLevelState = this.createBoundedDoubleState( this.createKey( "obesityLevelState" ), new BoundedDoubleDetails() );
	private final org.lgna.croquet.Operation setToInShape = this.createActionOperation( this.createKey( "obesityLevelState(0.0)" ), new SetObesityLevelAction( 0.0 ) );
	private final org.lgna.croquet.Operation setToOutOfShape = this.createActionOperation( this.createKey( "obesityLevelState(1.0)" ), new SetObesityLevelAction( 1.0 ) );

	private BodyTabComposite() {
		super( java.util.UUID.fromString( "10c0d057-a5d7-4a36-8cd7-c30f46f5aac2" ) );
	}

	public org.lgna.croquet.BoundedDoubleState getObesityLevelState() {
		return this.obesityLevelState;
	}

	public org.lgna.croquet.Operation getSetToInShape() {
		return this.setToInShape;
	}

	public org.lgna.croquet.Operation getSetToOutOfShape() {
		return this.setToOutOfShape;
	}

	@Override
	protected org.alice.stageide.personresource.views.BodyTabView createView() {
		return new org.alice.stageide.personresource.views.BodyTabView( this );
	}
}
