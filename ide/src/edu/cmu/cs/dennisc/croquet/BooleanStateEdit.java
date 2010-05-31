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
public final class BooleanStateEdit extends Edit {
	private BooleanState operation;
	private java.util.UUID operationId;
	//can't really imagine this values being the same, but it doesn't seem likely to hurt to track both values
	private boolean previousValue;
	private boolean nextValue;

	public BooleanStateEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public BooleanStateEdit( ModelContext context, java.awt.event.ItemEvent e, BooleanState operation ) {
		super( context );
		this.operation = operation;
		this.operationId = operation.getIndividualUUID();
		if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
			this.previousValue = false;
			this.nextValue = true;
		} else {
			this.previousValue = true;
			this.nextValue = false;
		}
	}

	private BooleanState getOperation() {
		if( this.operation != null ) {
			//pass
		} else {
			this.operation = Application.getSingleton().lookupOperation( this.operationId );
		}
		return this.operation;
	}
	@Override
	protected void decodeInternal(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		this.previousValue = binaryDecoder.decodeBoolean();
		this.nextValue = binaryDecoder.decodeBoolean();
	}
	@Override
	protected void encodeInternal(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode( this.previousValue );
		binaryEncoder.encode( this.nextValue );
	}
	
	@Override
	public boolean canRedo() {
		return this.getOperation() != null;
	}
	@Override
	public boolean canUndo() {
		return this.getOperation() != null;
	}

	@Override
	public void doOrRedo(boolean isDo) {
		this.getOperation().internalSetValue(this.nextValue);
	}

	@Override
	public void undo() {
		this.getOperation().internalSetValue(this.previousValue);
	}

	@Override
	protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
		rv.append("boolean: ");
		rv.append(this.nextValue);
		return rv;
	}
}
