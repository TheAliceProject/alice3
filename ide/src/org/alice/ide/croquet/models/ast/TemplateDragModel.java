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
public abstract class TemplateDragModel extends org.alice.ide.croquet.models.CodeDragModel {
	public TemplateDragModel( java.util.UUID id ) {
		super( id );
	}
	
	protected abstract String getTutorialStepDescription( edu.cmu.cs.dennisc.croquet.UserInformation userInformation );
	
	@Override
	public String getTutorialStepTitle( edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Drag and Drop " );
		sb.append( "<strong>" );
		sb.append( this.getTutorialStepDescription( userInformation ) );
		sb.append( "</strong>" );
		
		edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = modelContext.getSuccessfulCompletionEvent();
		if( successfulCompletionEvent != null ) {
			edu.cmu.cs.dennisc.croquet.Edit< ? > edit = successfulCompletionEvent.getEdit();
			if( edit instanceof org.alice.ide.croquet.edits.ast.InsertStatementEdit ) {
				org.alice.ide.croquet.edits.ast.InsertStatementEdit insertStatementEdit = (org.alice.ide.croquet.edits.ast.InsertStatementEdit)edit;
				edu.cmu.cs.dennisc.alice.ast.Expression[] originalExpressions = insertStatementEdit.getInitialExpressions();
				String prefix = " ";
				for( edu.cmu.cs.dennisc.alice.ast.Expression expression : originalExpressions ) {
					sb.append( prefix );
					edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr( sb, expression, userInformation.getLocale() );
					prefix = ", ";
				}
			}
		}
		return sb.toString();
	}
	@Override
	public String getTutorialDragNoteText( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Drag the " );
		sb.append( "<strong>" );
		sb.append( this.getTutorialStepDescription( userInformation ) );
		sb.append( "</strong>" );
		sb.append( " tile..." );
		return sb.toString();
	}
	@Override
	public String getTutorialDropNoteText( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Drag the " );
		sb.append( "<strong>" );
		sb.append( this.getTutorialStepDescription( userInformation ) );
		sb.append( "</strong>" );
		sb.append( " tile here..." );
		return sb.toString();
	}
	
}
