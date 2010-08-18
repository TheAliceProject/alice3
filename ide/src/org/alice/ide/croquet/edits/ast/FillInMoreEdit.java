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
public class FillInMoreEdit extends edu.cmu.cs.dennisc.croquet.Edit< org.alice.ide.croquet.models.ast.FillInMoreActionOperation > {
	private edu.cmu.cs.dennisc.alice.ast.Expression argumentExpression;
	public FillInMoreEdit() {
	}
	public FillInMoreEdit( edu.cmu.cs.dennisc.alice.ast.Expression argumentExpression ) {
		this.setArgumentExpression( argumentExpression );
	}
	public void setArgumentExpression(edu.cmu.cs.dennisc.alice.ast.Expression argumentExpression) {
		this.argumentExpression = argumentExpression;
	}
	
	private org.alice.ide.croquet.models.ast.FillInMoreActionOperation EPIC_HACK_actionOperation;
	public void EPIC_HACK_setModel( org.alice.ide.croquet.models.ast.FillInMoreActionOperation actionOperation ) {
		this.EPIC_HACK_actionOperation = actionOperation;
	}
	
	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		assert this.argumentExpression != null;
		org.alice.ide.croquet.models.ast.FillInMoreActionOperation actionOperation;
		if( EPIC_HACK_actionOperation != null ) {
			actionOperation = EPIC_HACK_actionOperation;
		} else {
			actionOperation = this.getModel();
		}
		assert actionOperation != null;
		
		org.alice.ide.croquet.models.ast.FillInMorePopupMenuOperation popupMenuOperation = actionOperation.getFillInMorePopupMenuOperation();
		
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation prevMethodInvocation = popupMenuOperation.getPrevMethodInvocation();
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation nextMethodInvocation = popupMenuOperation.getNextMethodInvocation();

		edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression = prevMethodInvocation.expression.getValue();
		//prevMethodInvocation.expression.setValue( null );
		nextMethodInvocation.expression.setValue( instanceExpression );
		final int N = prevMethodInvocation.arguments.size();
		for( int i=0; i<N; i++ ) {
			edu.cmu.cs.dennisc.alice.ast.Expression expressionI =  prevMethodInvocation.arguments.get( i ).expression.getValue();
			//prevMethodInvocation.arguments.get( i ).expression.setValue( null );
			nextMethodInvocation.arguments.get( i ).expression.setValue( expressionI );
		}
		nextMethodInvocation.arguments.get( N ).expression.setValue( this.argumentExpression );
		popupMenuOperation.getExpressionStatement().expression.setValue( nextMethodInvocation );
//		this.getModel().updateToolTipText();
	}
	@Override
	protected final void undoInternal() {
		org.alice.ide.croquet.models.ast.FillInMoreActionOperation actionOperation;
		if( EPIC_HACK_actionOperation != null ) {
			actionOperation = EPIC_HACK_actionOperation;
		} else {
			actionOperation = this.getModel();
		}
		assert actionOperation != null;
		org.alice.ide.croquet.models.ast.FillInMorePopupMenuOperation popupMenuOperation = actionOperation.getFillInMorePopupMenuOperation();

		edu.cmu.cs.dennisc.alice.ast.MethodInvocation prevMethodInvocation = popupMenuOperation.getPrevMethodInvocation();
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation nextMethodInvocation = popupMenuOperation.getNextMethodInvocation();
		
		edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression = nextMethodInvocation.expression.getValue();
		nextMethodInvocation.expression.setValue( null );
		prevMethodInvocation.expression.setValue( instanceExpression );
		final int N = prevMethodInvocation.arguments.size();
		for( int i=0; i<N; i++ ) {
			edu.cmu.cs.dennisc.alice.ast.Expression expressionI =  nextMethodInvocation.arguments.get( i ).expression.getValue();
			//nextMethodInvocation.arguments.get( i ).expression.setValue( null );
			prevMethodInvocation.arguments.get( i ).expression.setValue( expressionI );
		}
		//nextMethodInvocation.arguments.get( N ).expression.setValue( null );

		popupMenuOperation.getExpressionStatement().expression.setValue( prevMethodInvocation );
		

//		this.getModel().updateToolTipText();
	}
	@Override
	protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
		org.alice.ide.croquet.models.ast.FillInMoreActionOperation actionOperation;
		if( EPIC_HACK_actionOperation != null ) {
			actionOperation = EPIC_HACK_actionOperation;
		} else {
			actionOperation = this.getModel();
		}
		assert actionOperation != null;
		org.alice.ide.croquet.models.ast.FillInMorePopupMenuOperation popupMenuOperation = actionOperation.getFillInMorePopupMenuOperation();
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation nextMethodInvocation = popupMenuOperation.getNextMethodInvocation();
		if( nextMethodInvocation != null ) {
			rv.append( "more: " );
			edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, nextMethodInvocation.method.getValue(), locale );
			rv.append( " " );
			final int N = nextMethodInvocation.arguments.size(); 
			edu.cmu.cs.dennisc.alice.ast.Argument argument = nextMethodInvocation.arguments.get( N-1 );
			edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, argument, locale );
		}
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
