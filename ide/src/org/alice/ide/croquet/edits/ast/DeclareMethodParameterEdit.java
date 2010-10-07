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
public class DeclareMethodParameterEdit extends edu.cmu.cs.dennisc.croquet.OperationEdit< org.alice.ide.croquet.models.ast.DeclareMethodParameterOperation > {
	public static class DeclareMethodParameterEditMemento extends Memento<org.alice.ide.croquet.models.ast.DeclareMethodParameterOperation> {
		private edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter;
		public DeclareMethodParameterEditMemento( DeclareMethodParameterEdit edit ) {
			super( edit );
			this.parameter = edit.parameter;
		}
		public DeclareMethodParameterEditMemento( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.Edit<org.alice.ide.croquet.models.ast.DeclareMethodParameterOperation> createEdit() {
			return new DeclareMethodParameterEdit( this );
		}
		@Override
		protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			this.parameter = org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice.class ).decode( binaryDecoder );
		}
		@Override
		protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice.class ).encode( binaryEncoder, this.parameter );
		}
	}

	private edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter;

	private transient java.util.Map< edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Argument > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private transient int index;

	public DeclareMethodParameterEdit( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter ) {
		this.parameter = parameter;
	}
	private DeclareMethodParameterEdit( DeclareMethodParameterEditMemento memento ) {
		super( memento );
		this.parameter = memento.parameter;
	}
	@Override
	public Memento<org.alice.ide.croquet.models.ast.DeclareMethodParameterOperation> createMemento() {
		return new DeclareMethodParameterEditMemento( this );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = this.getModel().getMethod();
		this.index = method.parameters.size();
		org.alice.ide.ast.NodeUtilities.addParameter( map, method, this.parameter, this.index, org.alice.ide.IDE.getSingleton().getMethodInvocations( method ) );
	}
	@Override
	protected final void undoInternal() {
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = this.getModel().getMethod();
		org.alice.ide.ast.NodeUtilities.removeParameter( map, method, this.parameter, this.index, org.alice.ide.IDE.getSingleton().getMethodInvocations( method ) );
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
