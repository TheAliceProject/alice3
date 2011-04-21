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
public class PlainDialogCloseOperation extends SingleThreadOperation< PlainDialogCloseOperationContext > {
	private static final Group DIALOG_CLOSE_OPERATION_GROUP = Group.getInstance( java.util.UUID.fromString( "e9b1fda4-6668-4d23-980d-ab1610ffd2d0" ), "DIALOG_CLOSE_OPERATION_GROUP" );
	private final PlainDialogOperation plainDialogOperation;
	/*package-private*/ PlainDialogCloseOperation( PlainDialogOperation plainDialogOperation ) {
		super( DIALOG_CLOSE_OPERATION_GROUP, java.util.UUID.fromString( "2a116435-9536-4590-8294-c4050ea65a4e" ) );
		assert plainDialogOperation != null;
		this.plainDialogOperation = plainDialogOperation;
	}
	public static class PlainDialogCloseOperationResolver implements CodableResolver< PlainDialogCloseOperation > {
		private PlainDialogCloseOperation model;
		
		public PlainDialogCloseOperationResolver( PlainDialogCloseOperation model ) {
			this.model = model;
		}
		public PlainDialogCloseOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			this.decode( binaryDecoder );
		}
		public PlainDialogCloseOperation getResolved() {
			return this.model;
		}
		public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			CodableResolver<PlainDialogOperation> resolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
			PlainDialogOperation plainDialogOperation = resolver.getResolved();
			this.model = plainDialogOperation.getCloseOperation();
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			CodableResolver<PlainDialogOperation> resolver = this.model.plainDialogOperation.getCodableResolver();
			binaryEncoder.encode( resolver );
		}
	}
	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, Edit< ? > edit, UserInformation userInformation ) {
		rv.append( "Press the <strong>Close</strong> button when you are ready." );
		return rv;
	}
	
	public PlainDialogOperation getPlainDialogOperation() {
		return this.plainDialogOperation;
	}
	@Override
	protected PlainDialogCloseOperationResolver createCodableResolver() {
		return new PlainDialogCloseOperationResolver( this );
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.PlainDialogCloseOperationContext createAndPushContext( java.util.EventObject e, edu.cmu.cs.dennisc.croquet.ViewController< ?, ? > viewController ) {
		return ContextManager.createAndPushPlainDialogCloseOperationContext( this, e, viewController );
	}
	@Override
	protected void perform( edu.cmu.cs.dennisc.croquet.PlainDialogCloseOperationContext context ) {
		context.finish();
	}
}
