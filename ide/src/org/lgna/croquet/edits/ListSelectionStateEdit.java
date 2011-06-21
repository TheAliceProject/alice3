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
package org.lgna.croquet.edits;

/**
 * @author Dennis Cosgrove
 */
public final class ListSelectionStateEdit<E> extends StateEdit<org.lgna.croquet.ListSelectionState<E>,E> {
	private E prevValue;
	private E nextValue;
	
	public ListSelectionStateEdit( org.lgna.croquet.history.CompletionStep< org.lgna.croquet.ListSelectionState<E> > completionStep, E prevValue, E nextValue ) {
		super( completionStep );
		this.prevValue = prevValue;
		this.nextValue = nextValue;
	}
	public ListSelectionStateEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		org.lgna.croquet.ListSelectionState< E > listSelectionState = this.getModel();
		org.lgna.croquet.ItemCodec<E> codec = listSelectionState.getCodec();
		this.prevValue = codec.decodeValue( binaryDecoder );
		this.nextValue = codec.decodeValue( binaryDecoder );
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		org.lgna.croquet.ListSelectionState< E > listSelectionState = this.getModel();
		org.lgna.croquet.ItemCodec<E> codec = listSelectionState.getCodec();
		codec.encodeValue( binaryEncoder, this.prevValue );
		codec.encodeValue( binaryEncoder, this.nextValue );
	}
	
	@Override
	public E getPreviousValue() {
		return this.prevValue;
	}
	@Override
	public E getNextValue() {
		return this.nextValue;
	}
	@Override
	public boolean canRedo() {
		return this.getModel() != null;
	}
	@Override
	public boolean canUndo() {
		return this.getModel() != null;
	}

	@Override
	public void retarget( org.lgna.croquet.Retargeter retargeter ) {
		super.retarget( retargeter );
		this.prevValue = retargeter.retarget( this.prevValue );
		this.nextValue = retargeter.retarget( this.nextValue );
	}
//	@Override
//	public void addKeyValuePairs( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.croquet.Edit< ? > replacementEdit ) {
//		super.addKeyValuePairs( retargeter, replacementEdit );
//		ListSelectionStateEdit listSelectionStateEdit = (ListSelectionStateEdit)replacementEdit;
//		retargeter.addKeyValuePair( this.prevValue, listSelectionStateEdit.prevValue );
//		retargeter.addKeyValuePair( this.nextValue, listSelectionStateEdit.nextValue );
//	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		this.getModel().setSelectedItem( this.nextValue );
	}
	@Override
	protected final void undoInternal() {
		this.getModel().setSelectedItem( this.prevValue );
	}
	@Override
	public StringBuilder updateTutorialTransactionTitle( StringBuilder rv, org.lgna.croquet.UserInformation userInformation ) {
		rv.append( "select " );
		org.lgna.croquet.ItemCodec< E > itemCodec = this.getModel().getCodec();
		if( itemCodec != null ) {
			itemCodec.appendRepresentation( rv, this.nextValue, userInformation.getLocale() );
		} else {
			rv.append( this.nextValue );
		}
		return rv;
	}
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		rv.append( "select " );
		org.lgna.croquet.ItemCodec< E > itemCodec = this.getModel().getCodec();
		if( itemCodec != null ) {
			itemCodec.appendRepresentation( rv, this.prevValue, locale );
		} else {
			rv.append( this.prevValue );
		}
		rv.append( " ===> " );
		if( itemCodec != null ) {
			itemCodec.appendRepresentation( rv, this.nextValue, locale );
		} else {
			rv.append( this.nextValue );
		}
		return rv;
	}
}
