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
package org.alice.ide.croquet.edits.ast;

/**
 * @author Dennis Cosgrove
 */
public class DeclareMethodEdit extends edu.cmu.cs.dennisc.croquet.Edit<org.alice.ide.croquet.models.ast.DeclareMethodOperation> {
	private edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> type;
	private edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method;
	
	private edu.cmu.cs.dennisc.alice.ast.AbstractCode prevFocusedCode;

	public DeclareMethodEdit() {
	}
	public DeclareMethodEdit( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> type, edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		this.type = type;
		this.method = method;
	}
	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.type = org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice.class ).decode( binaryDecoder );
		this.method = org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class ).decode( binaryDecoder );
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice.class ).encode( binaryEncoder, this.type );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class ).encode( binaryEncoder, this.method );
	}
	
	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		this.type.methods.add( this.method );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		this.prevFocusedCode = ide.getFocusedCode();
		ide.setFocusedCode( method );
	}
	@Override
	protected final void undoInternal() {
		int index = type.methods.indexOf( method );
		if( index != -1 ) {
			type.methods.remove( index );
			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
			ide.setFocusedCode( this.prevFocusedCode );
		} else {
			throw new javax.swing.undo.CannotUndoException();
		}
	}
	@Override
	protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
		rv.append( "declare:" );
		edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr(rv, method, locale);
		return rv;
	}
	
}
