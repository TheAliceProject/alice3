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
public class ParameterDeclarationEdit extends org.lgna.croquet.edits.Edit< org.alice.ide.croquet.models.declaration.ParameterDeclarationOperation > {
	private edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter;
	private transient java.util.Map< edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty, edu.cmu.cs.dennisc.alice.ast.Argument > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private transient int index;

	public ParameterDeclarationEdit( org.lgna.croquet.history.CompletionStep completionStep, edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter ) {
		super( completionStep );
		this.parameter = parameter;
	}
	public ParameterDeclarationEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.parameter = org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice.class ).decodeValue( binaryDecoder );
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice.class ).encodeValue( binaryEncoder, this.parameter );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code = this.getModel().getCode();
		this.index = code.getParamtersProperty().size();
		org.alice.ide.ast.NodeUtilities.addParameter( map, code, this.parameter, this.index, org.alice.ide.IDE.getActiveInstance().getArgumentLists( code ) );
	}
	@Override
	protected final void undoInternal() {
		edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code = this.getModel().getCode();
		org.alice.ide.ast.NodeUtilities.removeParameter( map, code, this.parameter, this.index, org.alice.ide.IDE.getActiveInstance().getArgumentLists( code ) );
	}
	@Override
	protected StringBuilder updatePresentation(StringBuilder rv, java.util.Locale locale) {
		rv.append( "declare:" );
		edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr(rv, parameter, locale);
		return rv;
	}
	@Override
	public boolean canUndo() {
		return true;
	}
	@Override
	public boolean canRedo() {
		return true;
	}
}
