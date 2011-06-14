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
public class ExpressionPropertyEdit extends org.lgna.croquet.edits.Edit {
	private final edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty;
	private final edu.cmu.cs.dennisc.alice.ast.Expression nextExpression;
	private final edu.cmu.cs.dennisc.alice.ast.Expression prevExpression;

	public ExpressionPropertyEdit( org.lgna.croquet.history.CompletionStep completionStep, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression, edu.cmu.cs.dennisc.alice.ast.Expression nextExpression ) {
		super( completionStep );
		this.expressionProperty = expressionProperty;
		this.prevExpression = prevExpression;
		this.nextExpression = nextExpression;
	}
	public ExpressionPropertyEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		java.util.UUID expressionPropertyNodeId = binaryDecoder.decodeId();
		String propertyName = binaryDecoder.decodeString();
		java.util.UUID prevExpressionId = binaryDecoder.decodeId();
		java.util.UUID nextExpressionId = binaryDecoder.decodeId();

		
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
		edu.cmu.cs.dennisc.alice.ast.AbstractNode node = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, expressionPropertyNodeId );
		this.expressionProperty = (edu.cmu.cs.dennisc.alice.ast.ExpressionProperty)node.getPropertyNamed( propertyName );
		this.prevExpression = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, prevExpressionId );
		this.nextExpression = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, nextExpressionId );
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		edu.cmu.cs.dennisc.alice.ast.Node node = (edu.cmu.cs.dennisc.alice.ast.Node)this.expressionProperty.getOwner();
		binaryEncoder.encode( node.getUUID() );
		binaryEncoder.encode( this.expressionProperty.getName() );
		
		binaryEncoder.encode( this.prevExpression.getUUID() );
		binaryEncoder.encode( this.nextExpression.getUUID() );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		this.expressionProperty.setValue( this.nextExpression );
	}
	@Override
	protected final void undoInternal() {
		this.expressionProperty.setValue( this.prevExpression );
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
	public void addKeyValuePairs( org.lgna.croquet.Retargeter retargeter, org.lgna.croquet.edits.Edit edit ) {
		super.addKeyValuePairs( retargeter, edit );
		ExpressionPropertyEdit replacementEdit = (ExpressionPropertyEdit)edit;
		retargeter.addKeyValuePair( this.expressionProperty, replacementEdit.expressionProperty );
		retargeter.addKeyValuePair( this.prevExpression, replacementEdit.prevExpression );
		retargeter.addKeyValuePair( this.nextExpression, replacementEdit.nextExpression );
	}
}
