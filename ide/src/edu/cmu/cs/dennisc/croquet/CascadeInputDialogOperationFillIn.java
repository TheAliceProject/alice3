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
public class CascadeInputDialogOperationFillIn<F,J extends CascadeInputDialogPanel< F > > extends CascadeFillIn< F, Void > {
	private final CascadeInputDialogOperation< F,J > inputDialogOperation;
	public CascadeInputDialogOperationFillIn( CascadeInputDialogOperation< F,J > inputDialogOperation ) {
		super( java.util.UUID.fromString( "f2c75b9f-aa0d-487c-a161-46cb23ff3e76" ) );
		this.inputDialogOperation = inputDialogOperation;
	}
	public static class CascadeInputDialogOperationFillInResolver<F,J extends CascadeInputDialogPanel< F >> implements CodableResolver< CascadeInputDialogOperationFillIn<F,J> > {
		private final CascadeInputDialogOperationFillIn<F,J> model;
		
		public CascadeInputDialogOperationFillInResolver( CascadeInputDialogOperationFillIn<F,J> model ) {
			this.model = model;
		}
		public CascadeInputDialogOperationFillInResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			CodableResolver<CascadeInputDialogOperation<F,J>> resolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
			CascadeInputDialogOperation<F,J> inputDialogOperation = resolver.getResolved();
			this.model = inputDialogOperation.getFillIn();
		}
		public edu.cmu.cs.dennisc.croquet.CascadeInputDialogOperationFillIn<F,J> getResolved() {
			return this.model;
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			CodableResolver<CascadeInputDialogOperation<F,J>> resolver = this.model.inputDialogOperation.getCodableResolver();
			binaryEncoder.encode( resolver );
		}
	}
	public CascadeInputDialogOperation< F, J > getInputDialogOperation() {
		return this.inputDialogOperation;
	}
	@Override
	protected CascadeInputDialogOperationFillInResolver createCodableResolver() {
		return new CascadeInputDialogOperationFillInResolver( this );
	}
	@Override
	protected String getTutorialItemText() {
		return this.inputDialogOperation.getDefaultLocalizedText();
	}
	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( CascadeFillInContext< F, Void > context ) {
		return new javax.swing.JLabel( this.getTutorialItemText() );
	}
	@Override
	public F createValue( CascadeFillInContext< F, Void > context ) {
		InputDialogOperationContext< J > inputDialogContext = this.inputDialogOperation.fire();
		J panel = inputDialogContext.getMainPanel();
		return panel.getInputValue();
	}
	@Override
	public F getTransientValue( CascadeFillInContext< F, Void > context ) {
		return null;
	}
}
