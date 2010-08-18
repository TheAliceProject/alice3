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
public class FillInExpressionPropertyEdit extends edu.cmu.cs.dennisc.croquet.Edit< org.alice.ide.croquet.models.ast.FillInExpressionPropertyActionOperation > {
	private edu.cmu.cs.dennisc.alice.ast.Expression prevExpression;
	private edu.cmu.cs.dennisc.alice.ast.Expression nextExpression;
	public FillInExpressionPropertyEdit() {
	}
	public FillInExpressionPropertyEdit( edu.cmu.cs.dennisc.alice.ast.Expression nextExpression ) {
		this.nextExpression = nextExpression;
	}
	
	private org.alice.ide.croquet.models.ast.FillInExpressionPropertyActionOperation EPIC_HACK_actionOperation;
	public void EPIC_HACK_setModel( org.alice.ide.croquet.models.ast.FillInExpressionPropertyActionOperation actionOperation ) {
		this.EPIC_HACK_actionOperation = actionOperation;
	}
	
	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		org.alice.ide.croquet.models.ast.FillInExpressionPropertyActionOperation actionOperation;
		if( EPIC_HACK_actionOperation != null ) {
			actionOperation = EPIC_HACK_actionOperation;
		} else {
			actionOperation = this.getModel();
		}
		assert actionOperation != null;
		
		org.alice.ide.croquet.models.ast.FillInExpressionPropertyPopupMenuOperation popupMenuOperation = actionOperation.getFillInExpressionPropertyPopupMenuOperation();

		edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty = popupMenuOperation.getExpressionProperty();
		if( isDo ) {
			this.prevExpression = popupMenuOperation.getPreviousExpression();
		}
		expressionProperty.setValue( this.nextExpression );
	}
	@Override
	protected final void undoInternal() {
		org.alice.ide.croquet.models.ast.FillInExpressionPropertyActionOperation actionOperation;
		if( EPIC_HACK_actionOperation != null ) {
			actionOperation = EPIC_HACK_actionOperation;
		} else {
			actionOperation = this.getModel();
		}
		assert actionOperation != null;
		
		org.alice.ide.croquet.models.ast.FillInExpressionPropertyPopupMenuOperation popupMenuOperation = actionOperation.getFillInExpressionPropertyPopupMenuOperation();
		edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty = popupMenuOperation.getExpressionProperty();
		expressionProperty.setValue( this.prevExpression );
	}
	@Override
	protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
		rv.append( "set: " );
		edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, this.prevExpression, locale );
		rv.append( " ===> " );
		edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, this.nextExpression, locale );
		return rv;
	}

	
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		throw new RuntimeException( "todo" );
	}
	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		throw new RuntimeException( "todo" );
	}
	@Override
	public boolean canRedo() {
		return true;
	}
	@Override
	public boolean canUndo() {
		return true;
	}
}
