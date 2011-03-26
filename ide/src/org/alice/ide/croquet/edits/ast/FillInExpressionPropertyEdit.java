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
public class FillInExpressionPropertyEdit extends edu.cmu.cs.dennisc.croquet.Edit< org.alice.ide.croquet.models.ast.expression.ExpressionPropertyOperation > {
	public static class FillInExpressionPropertyEditMemento extends Memento<org.alice.ide.croquet.models.ast.expression.ExpressionPropertyOperation> {
		private edu.cmu.cs.dennisc.alice.ast.Expression prevExpression;
		private edu.cmu.cs.dennisc.alice.ast.Expression nextExpression;
		public FillInExpressionPropertyEditMemento( FillInExpressionPropertyEdit edit ) {
			super( edit );
			this.prevExpression = edit.prevExpression;
			this.nextExpression = edit.nextExpression;
		}
		public FillInExpressionPropertyEditMemento( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.Edit< org.alice.ide.croquet.models.ast.expression.ExpressionPropertyOperation > createEdit() {
			return new FillInExpressionPropertyEdit( this );
		}
		@Override
		protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
			edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
			java.util.UUID prevExpressionId = binaryDecoder.decodeId();
			this.prevExpression = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, prevExpressionId );
			java.util.UUID nextExpressionId = binaryDecoder.decodeId();
			this.nextExpression = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, nextExpressionId );
		}
		@Override
		protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			binaryEncoder.encode( this.prevExpression.getUUID() );
			binaryEncoder.encode( this.nextExpression.getUUID() );
		}
	}

	private final edu.cmu.cs.dennisc.alice.ast.Expression nextExpression;
	private final edu.cmu.cs.dennisc.alice.ast.Expression prevExpression;

	public FillInExpressionPropertyEdit( edu.cmu.cs.dennisc.alice.ast.Expression prevExpression, edu.cmu.cs.dennisc.alice.ast.Expression nextExpression ) {
		this.prevExpression = prevExpression;
		this.nextExpression = nextExpression;
	}
	private FillInExpressionPropertyEdit( FillInExpressionPropertyEditMemento memento ) {
		super( memento );
		this.prevExpression = memento.prevExpression;
		this.nextExpression = memento.nextExpression;
	}
	@Override
	public Memento<org.alice.ide.croquet.models.ast.expression.ExpressionPropertyOperation> createMemento() {
		return new FillInExpressionPropertyEditMemento( this );
	}

	private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty getExpressionProperty() {
		org.alice.ide.croquet.models.ast.expression.ExpressionPropertyOperation expressionPropertyOperation = this.getModel();
		return expressionPropertyOperation.getExpressionProperty();
	}
	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		this.getExpressionProperty().setValue( this.nextExpression );
	}
	@Override
	protected final void undoInternal() {
		this.getExpressionProperty().setValue( this.prevExpression );
	}
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		rv.append( "set: " );
		edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr( rv, this.prevExpression, locale );
		rv.append( " ===> " );
		edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr( rv, this.nextExpression, locale );
		return rv;
	}

	@Override
	public void addKeyValuePairs( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		super.addKeyValuePairs( retargeter, edit );
		FillInExpressionPropertyEdit replacementEdit = (FillInExpressionPropertyEdit)edit;
		retargeter.addKeyValuePair( this.prevExpression, replacementEdit.prevExpression );
		retargeter.addKeyValuePair( this.nextExpression, replacementEdit.nextExpression );
	}
}
