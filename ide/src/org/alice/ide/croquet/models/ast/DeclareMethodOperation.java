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
public abstract class DeclareMethodOperation extends org.alice.ide.croquet.models.InputDialogWithPreviewOperation<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> {
	private edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType;
	public DeclareMethodOperation( java.util.UUID individualId, edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, individualId );
		this.declaringType = declaringType;
	}
//	@Override
//	protected edu.cmu.cs.dennisc.croquet.Edit< ? > createTutorialCompletionEdit( edu.cmu.cs.dennisc.croquet.Edit< ? > originalEdit, edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
//		assert originalEdit instanceof org.alice.ide.croquet.edits.ast.DeclareMethodEdit;
//		org.alice.ide.croquet.edits.ast.DeclareMethodEdit originalDeclareMethodEdit = (org.alice.ide.croquet.edits.ast.DeclareMethodEdit)originalEdit;
//		return originalDeclareMethodEdit.createTutorialCompletionEdit( retargeter );
//	}
	
	//todo: rename
	protected abstract String getMethodDescription( org.lgna.croquet.UserInformation userInformation );
	protected abstract StringBuilder appendTutorialFinishNoteText( StringBuilder rv, org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit, org.lgna.croquet.UserInformation userInformation );

//	@Override
//	protected StringBuilder updateTutorialStepTitle( StringBuilder rv, edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext, edu.cmu.cs.dennisc.croquet.Edit< ? > edit, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
////		edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = modelContext.getSuccessfulCompletionEvent();
////		if( successfulCompletionEvent != null ) {
//			org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit = (org.alice.ide.croquet.edits.ast.DeclareMethodEdit)edit;
//			assert declareMethodEdit != null;
//			assert declareMethodEdit.getMethod() != null;
//			rv.append( "Declare " );
//			rv.append( this.getMethodDescription( userInformation ) );
//			rv.append( " named " );
//			rv.append( declareMethodEdit.getMethod().getName() );
////		}
//		return rv;
//	}
//	
//	@Override
//	public String getTutorialFinishNoteText( org.lgna.croquet.steps.InputDialogOperationStep< ? > step, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
//		StringBuilder sb = new StringBuilder();
//		edu.cmu.cs.dennisc.croquet.Edit< ? > edit = step.getEdit();
//		if( edit instanceof org.alice.ide.croquet.edits.ast.DeclareMethodEdit ) {
//			org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit = (org.alice.ide.croquet.edits.ast.DeclareMethodEdit)edit;
//			this.appendTutorialFinishNoteText( sb, declareMethodEdit, userInformation );
//		}
//		return sb.toString();
//	}

	@Override
	public org.lgna.croquet.edits.Edit< ? > createTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.Retargeter retargeter ) {
		assert edit instanceof org.alice.ide.croquet.edits.ast.DeclareMethodEdit;
		org.alice.ide.croquet.edits.ast.DeclareMethodEdit originalDeclareMethodEdit = (org.alice.ide.croquet.edits.ast.DeclareMethodEdit)edit;
		//todo
		return new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( (org.lgna.croquet.history.OperationStep)step, originalDeclareMethodEdit.getDeclaringType(), originalDeclareMethodEdit.getMethod() );
	}
	protected String getDeclarationName(org.lgna.croquet.history.InputDialogOperationStep step) {
		org.alice.ide.declarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createMethodPane = step.getMainPanel();
		if( createMethodPane != null ) {
			return createMethodPane.getDeclarationName();
		} else {
			return null;
		}
	}
//	@Override
//	public edu.cmu.cs.dennisc.croquet.Edit< ? > EPIC_HACK_createEdit( org.lgna.croquet.steps.InputDialogOperationStep step ) {
//		org.alice.ide.declarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createMethodPane = step.getMainPanel();
//		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = createMethodPane.getInputValue();
//		return new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( declaringType, method );
//	}

	protected abstract org.alice.ide.declarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createCreateMethodPane( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType );
	@Override
	protected org.alice.ide.declarationpanes.CreateDeclarationPane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > prologue(org.lgna.croquet.history.InputDialogOperationStep step) {
		assert this.declaringType != null;
		return this.createCreateMethodPane( this.declaringType );
	}
	@Override
	protected void epilogue(org.lgna.croquet.history.InputDialogOperationStep step, boolean isOk) {
		if( isOk ) {
			org.alice.ide.declarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createMethodPane = step.getMainPanel();
			final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = createMethodPane.getInputValue();
			if( method != null ) {
				final org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
				final edu.cmu.cs.dennisc.alice.ast.AbstractCode prevCode = ide.getFocusedCode();
//				step.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
//					@Override
//					protected final void doOrRedoInternal( boolean isDo ) {
//						declaringType.methods.add( method );
////						assert method.getDeclaringType() == method.body.getValue().getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.AbstractType.class );
//						ide.setFocusedCode( method );
//					}
//					@Override
//					protected final void undoInternal() {
//						int index = declaringType.methods.indexOf( method );
//						if( index != -1 ) {
//							declaringType.methods.remove( index );
//							ide.setFocusedCode( prevCode );
//						} else {
//							throw new javax.swing.undo.CannotUndoException();
//						}
//					}
//					@Override
//					protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
//						rv.append( "declare:" );
//						edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr(rv, method, locale);
//						return rv;
//					}
//				} );
				step.commitAndInvokeDo( new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( step, declaringType, method ) );
			} else {
				step.cancel();
			}
		} else {
			step.cancel();
		}
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> getDeclaringType() {
		return this.declaringType;
	}
	@Override
	protected StringBuilder appendRepr( StringBuilder rv ) {
		super.appendRepr( rv );
		rv.append( "[" );
		if( this.declaringType != null ) {
			rv.append( this.declaringType.getName() );
		}
		rv.append( "]" );
		return rv;
	}	
}
