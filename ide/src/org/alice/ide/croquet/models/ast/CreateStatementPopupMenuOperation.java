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
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public class CreateStatementPopupMenuOperation extends FillInExpressionsPopupMenuOperation {
	public CreateStatementPopupMenuOperation( java.util.UUID id ) {
		super( id );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
		return null;
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.ActionOperation createActionOperation( edu.cmu.cs.dennisc.cascade.FillIn< ? > fillIn ) {
		return null;
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.Edit< ? extends edu.cmu.cs.dennisc.croquet.ActionOperation > createEdit( java.lang.Object value, edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		return null;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? >[] getDesiredValueTypes() {
		return null;
	}
	
//	class DropEdit extends org.alice.ide.ToDoEdit {
//		private edu.cmu.cs.dennisc.alice.ast.Statement statement;
//		@Override
//		protected final void doOrRedoInternal( boolean isDo ) {
//			statementListPropertyPane.getProperty().add( index, statement );
//			CodeEditor.this.refresh();
//			CodeEditor.this.resetScrollPane( viewPosition );
//		}
//
//		@Override
//		protected final void undoInternal() {
//			if( statementListPropertyPane.getProperty().get( index ) == statement ) {
//				statementListPropertyPane.getProperty().remove( index );
//				CodeEditor.this.refresh();
//				CodeEditor.this.resetScrollPane( viewPosition );
//			} else {
//				throw new javax.swing.undo.CannotUndoException();
//			}
//		}
//		
//		@Override
//		protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
//			//super.updatePresentation( rv, locale );
//			rv.append( "drop: " );
//			edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, statement, locale );
//			return rv;
//		}
//	}
//	context.pend( new edu.cmu.cs.dennisc.croquet.PendResolver< DropEdit, edu.cmu.cs.dennisc.alice.ast.Statement >() {
//		public DropEdit createEdit() {
//			return new DropEdit();
//		}
//		public DropEdit initialize(DropEdit rv, edu.cmu.cs.dennisc.croquet.ModelContext context, java.util.UUID id, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Statement> taskObserver) {
//			edu.cmu.cs.dennisc.property.PropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
//			if( propertyOwner instanceof edu.cmu.cs.dennisc.alice.ast.BlockStatement ) {
//				edu.cmu.cs.dennisc.alice.ast.BlockStatement block = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)propertyOwner;
//				statementTemplate.createStatement( mouseEvent, block, index, taskObserver );
//			}
//			return rv;
//		}
//		
//		public DropEdit handleCompletion(DropEdit rv, edu.cmu.cs.dennisc.alice.ast.Statement statement) {
//			rv.statement = statement;
//			source.hideDropProxyIfNecessary();
//			return rv;
//		}
//		public void handleCancelation() {
//			source.hideDropProxyIfNecessary();
//		}
//	} );

}
