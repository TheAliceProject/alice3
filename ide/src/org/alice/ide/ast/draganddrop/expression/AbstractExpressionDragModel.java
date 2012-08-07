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

package org.alice.ide.ast.draganddrop.expression;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractExpressionDragModel extends org.alice.ide.ast.draganddrop.CodeDragModel {
	public AbstractExpressionDragModel( java.util.UUID id ) {
		super( id );
	}
//	@Override
//	public java.util.List< ? extends org.lgna.croquet.DropReceptor > createListOfPotentialDropReceptors() {
//		java.util.List< org.lgna.croquet.DropReceptor > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
//		if( ide != null ) {
//			org.alice.ide.codedrop.CodeDropReceptor codeEditor = ide.getCodeEditorInFocus();
//			if( codeEditor != null ) {
//				codeEditor.addPotentialDropReceptors( rv, this.getExpressionType() );
//				//codeEditor.addPotentialDropReceptors( rv, org.lgna.project.ast.JavaType.VOID_TYPE );
//			} else {
//				//todo: investigate
//			}
//		}
//		return rv;
//	}
	public abstract boolean isPotentialStatementCreator();
	protected abstract org.lgna.croquet.Model getDropModel( org.lgna.project.ast.ExpressionProperty expressionProperty );
	protected abstract org.lgna.croquet.Model getDropModel( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair );
	@Override
	public final org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.croquet.DropSite dropSite ) {
		if( dropSite instanceof org.alice.ide.ast.draganddrop.ExpressionPropertyDropSite ) {
			org.alice.ide.ast.draganddrop.ExpressionPropertyDropSite expressionPropertyDropSite = (org.alice.ide.ast.draganddrop.ExpressionPropertyDropSite)dropSite;
			return this.getDropModel( expressionPropertyDropSite.getExpressionProperty() );
		} else if( dropSite instanceof org.alice.ide.ast.draganddrop.BlockStatementIndexPair ) {
			org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair = (org.alice.ide.ast.draganddrop.BlockStatementIndexPair)dropSite;
			return this.getDropModel( blockStatementIndexPair );
		} else {
			return null;
		}
	}
}
