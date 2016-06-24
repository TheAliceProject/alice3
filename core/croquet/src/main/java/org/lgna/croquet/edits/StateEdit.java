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
package org.lgna.croquet.edits;

/**
 * @author Dennis Cosgrove
 */
public final class StateEdit<T> extends org.lgna.croquet.edits.AbstractEdit<org.lgna.croquet.State<T>> {
	private T prevValue;
	private T nextValue;

	public StateEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.State<T>> completionStep, T prevValue, T nextValue ) {
		super( completionStep );
		this.prevValue = prevValue;
		this.nextValue = nextValue;
	}

	public StateEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		org.lgna.croquet.State<T> state = this.getModel();
		this.prevValue = state.decodeValue( binaryDecoder );
		this.nextValue = state.decodeValue( binaryDecoder );
	}

	@Override
	public final void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		org.lgna.croquet.State<T> state = this.getModel();
		state.encodeValue( binaryEncoder, this.prevValue );
		state.encodeValue( binaryEncoder, this.nextValue );
	}

	public final T getPreviousValue() {
		return this.prevValue;
	}

	public final T getNextValue() {
		return this.nextValue;
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "select " );
		org.lgna.croquet.State<T> state = this.getModel();
		if( state != null ) {
			state.appendRepresentation( rv, this.prevValue );
		} else {
			rv.append( this.prevValue );
		}
		rv.append( " ===> " );
		if( state != null ) {
			state.appendRepresentation( rv, this.nextValue );
		} else {
			rv.append( this.nextValue );
		}
	}

	@Override
	public final boolean canRedo() {
		return this.getModel() != null;
	}

	@Override
	public final boolean canUndo() {
		return this.getModel() != null;
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		if( isDo ) {
			//pass
		} else {
			this.getModel().changeValueFromEdit( this.getNextValue() );
		}
	}

	@Override
	protected final void undoInternal() {
		this.getModel().changeValueFromEdit( this.getPreviousValue() );
	}
}
