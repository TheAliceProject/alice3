/*
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
package edu.cmu.cs.dennisc.croquet.guide;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ class ForwardingFromActionOperationToInputDialogOperationNote extends RequirementNote {
	public static ForwardingFromActionOperationToInputDialogOperationNote createInstance( ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.ActionOperationContext actionOperationContext, edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent actionCompletionEvent, edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > inputDialogOperationContext, edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent inputDialogCompletionEvent ) {
		ForwardingFromActionOperationToInputDialogOperationNote rv = new ForwardingFromActionOperationToInputDialogOperationNote( parentContextCriterion, actionOperationContext, actionCompletionEvent, inputDialogOperationContext, inputDialogCompletionEvent );
		rv.addRequirement( new IsChildOfAndInstanceOf( parentContextCriterion, edu.cmu.cs.dennisc.croquet.InputDialogOperationContext.class ) );
		rv.addRequirement( new IsChildOfAndInstanceOf( rv.getAcceptedContextAt( 0 ), edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent.class ) );
		//rv.addRequirement( new IsChildOfAndInstanceOf( rv.getAcceptedContextAt( 0 ), edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosingEvent.class ) );
		rv.addRequirement( new IsAcceptableSuccessfulCompletionOf( rv.getAcceptedContextAt( 0 ), inputDialogCompletionEvent ) );
		rv.addRequirement( new IsAcceptableSuccessfulCompletionOf( parentContextCriterion, actionCompletionEvent ) );
		return rv;
	}
	private ForwardingFromActionOperationToInputDialogOperationNote( ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.ActionOperationContext actionOperationContext, edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent actionCompletionEvent, edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > inputDialogOperationContext, edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent inputDialogCompletionEvent ) {
		edu.cmu.cs.dennisc.croquet.InputDialogOperation< ? > inputDialogOperation = inputDialogOperationContext.getModel();
		String text = inputDialogOperation.getTutorialNoteText( inputDialogOperationContext, ConstructionGuide.getInstance().getUserInformation() );
		edu.cmu.cs.dennisc.croquet.Edit< ? > edit = actionCompletionEvent.getEdit();
		if( edit instanceof org.alice.ide.croquet.edits.ast.InsertStatementEdit ) {
			org.alice.ide.croquet.edits.ast.InsertStatementEdit insertStatementEdit = (org.alice.ide.croquet.edits.ast.InsertStatementEdit)edit;
			edu.cmu.cs.dennisc.alice.ast.Expression[] originalExpressions = insertStatementEdit.getOriginalExpressions();
			if( originalExpressions.length > 0 ) {
				StringBuilder sb = new StringBuilder();
				edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr( sb, originalExpressions[ 0 ], java.util.Locale.getDefault() );
				text = text.replaceAll( "fill_in_expression_value_here", sb.toString() );
			}
		}
		this.setText( text );
		final ModelFromContextResolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation<?> > inputDialogOperationResolver = new ModelFromContextResolver( inputDialogOperationContext );
		this.addFeature( new edu.cmu.cs.dennisc.tutorial.InputDialogCommitFeature( new edu.cmu.cs.dennisc.croquet.RuntimeResolver< edu.cmu.cs.dennisc.croquet.TrackableShape >() {
			public edu.cmu.cs.dennisc.croquet.TrackableShape getResolved() {
				edu.cmu.cs.dennisc.croquet.InputDialogOperation<?> inputDialogOperation = inputDialogOperationResolver.getResolved();
				if( inputDialogOperation != null ) {
					edu.cmu.cs.dennisc.croquet.Dialog activeDialog = inputDialogOperation.getActiveDialog();
					if( activeDialog != null ) {
						//todo
						return activeDialog;
					} else {
						return null;
					}
				} else {
					return null;
				}
			}
		} ) );
		
	}
}
