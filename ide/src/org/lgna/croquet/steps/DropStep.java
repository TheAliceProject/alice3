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
package org.lgna.croquet.steps;

/**
 * @author Dennis Cosgrove
 */
public class DropStep extends CompletionStep< edu.cmu.cs.dennisc.croquet.CompletionModel > {
	public static DropStep createAndAddToTransaction( Transaction parent, edu.cmu.cs.dennisc.croquet.CompletionModel model, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor ) {
		return new DropStep( parent, model, dropReceptor );
	}
	private final edu.cmu.cs.dennisc.croquet.CodableResolver< edu.cmu.cs.dennisc.croquet.DropReceptor > dropReceptorResolver;
	private DropStep( Transaction parent, edu.cmu.cs.dennisc.croquet.CompletionModel model, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor ) {
		super( parent, model, null );
		this.dropReceptorResolver = dropReceptor.getCodableResolver();
	}
	public DropStep( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
		this.dropReceptorResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
	}
	@Override
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		super.encode(binaryEncoder);
		binaryEncoder.encode( this.dropReceptorResolver );
	}
	public edu.cmu.cs.dennisc.croquet.DropReceptor getDropReceptor() {
		return this.dropReceptorResolver.getResolved();
	}
	@Override
	public String getTutorialNoteText( edu.cmu.cs.dennisc.croquet.Edit< ? > edit, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		return this.getDropReceptor().getTutorialNoteText( this.getModel(), edit, userInformation );
	}
	@Override
	public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		super.retarget( retargeter );
		if( this.dropReceptorResolver instanceof edu.cmu.cs.dennisc.croquet.RetargetableResolver<?> ) {
			edu.cmu.cs.dennisc.croquet.RetargetableResolver<?> retargetableResolver = (edu.cmu.cs.dennisc.croquet.RetargetableResolver<?>)this.dropReceptorResolver;
			retargetableResolver.retarget( retargeter );
		}
	}
}
