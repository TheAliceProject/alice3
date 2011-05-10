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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class InputDialogOperation extends GatedCommitDialogOperation<org.lgna.croquet.steps.InputDialogOperationStep> {
	protected static class OkOperation extends CompleteOperation {
		private static class SingletonHolder {
			private static OkOperation instance = new OkOperation();
		}
		public static OkOperation getInstance() {
			return SingletonHolder.instance;
		}
		private OkOperation() {
			super( java.util.UUID.fromString( "fc908f6f-4b72-48b6-9b65-352dc9f2e18b" ) );
		}
		@Override
		protected void localize() {
			super.localize();
			this.setName( "OK" );
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.GatedCommitDialogOperation.CompleteOperation getCompleteOperation() {
		return OkOperation.getInstance();
	}
	private boolean isCancelDesired;
	public InputDialogOperation(Group group, java.util.UUID individualId, boolean isCancelDesired) {
		super(group, individualId);
		this.isCancelDesired = isCancelDesired;
	}
	public InputDialogOperation(Group group, java.util.UUID individualId) {
		this(group, individualId, true);
	}

	@Override
	public org.lgna.croquet.steps.InputDialogOperationStep createAndPushStep( org.lgna.croquet.Trigger trigger ) {
		return org.lgna.croquet.steps.TransactionManager.addInputDialogOperationStep( this, trigger );
	}

	
	public String getTutorialFinishNoteText( org.lgna.croquet.steps.InputDialogOperationStep inputDialogOperationContext, UserInformation userInformation ) {
		return "When finished press the <strong>OK</strong> button.";
	}

	public static interface ExternalCommitButtonDisabler {
		public String getExplanationIfCommitButtonShouldBeDisabled( org.lgna.croquet.steps.InputDialogOperationStep step );
	}
	private ExternalCommitButtonDisabler externalCommitButtonDisabler;
	public ExternalCommitButtonDisabler getExternalCommitButtonDisabler() {
		return this.externalCommitButtonDisabler;
	}
	public void setExternalCommitButtonDisabler( ExternalCommitButtonDisabler externalCommitButtonDisabler ) {
		this.externalCommitButtonDisabler = externalCommitButtonDisabler;
	}
	
	protected String getInternalExplanation( org.lgna.croquet.steps.InputDialogOperationStep step ) {
		return null;
	}
	
	@Override
	protected final String getExplanation( org.lgna.croquet.steps.InputDialogOperationStep step ) {
		//org.lgna.croquet.steps.InputDialogOperationStep step = (org.lgna.croquet.steps.InputDialogOperationStep)child.findContextFor( InputDialogOperation.this );
//		String text;
		if( step != null ) {
			String explanation = this.getInternalExplanation( step );
			if( this.externalCommitButtonDisabler != null ) {
				String externalExplanation = this.externalCommitButtonDisabler.getExplanationIfCommitButtonShouldBeDisabled( step );
				if( externalExplanation != null ) {
					explanation = externalExplanation;
				}
			}
			return explanation;
//			if( explanation != null ) {
//				text = explanation;
//			} else {
//				text = NULL_EXPLANATION;
//			}
//			this.getCompleteOperation().setEnabled( text == NULL_EXPLANATION );
//			this.explanationLabel.setText( text );
		} else {
//			this.explanationLabel.setText( "todo: updateOperationAndExplanation step==null" );
			this.getCompleteOperation().setEnabled( true );
			return "todo: updateOperationAndExplanation step==null";
		}
	}
	protected abstract JComponent< ? > prologue( org.lgna.croquet.steps.InputDialogOperationStep step );
	protected abstract void epilogue( org.lgna.croquet.steps.InputDialogOperationStep step, boolean isCommit );


	@Deprecated
	public Edit< ? > EPIC_HACK_createEdit( org.lgna.croquet.steps.InputDialogOperationStep inputDialogOperationContext ) {
		//todo
		return null;
	}
	
	@Override
	protected Component< ? > createControlsPanel( org.lgna.croquet.steps.InputDialogOperationStep step, Dialog dialog ) {
		Button okButton = this.getCompleteOperation().createButton();
		LineAxisPanel rv = new LineAxisPanel();
		rv.addComponent( BoxUtilities.createHorizontalGlue() );
		rv.addComponent( okButton );
		
		//todo: use isCancelDesired?
		rv.addComponent( BoxUtilities.createHorizontalSliver( 4 ) );
		rv.addComponent( this.getCancelOperation().createButton() );

		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
		dialog.setDefaultButton( okButton );
		return rv;
	}
	
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createMainPanel( org.lgna.croquet.steps.InputDialogOperationStep step, edu.cmu.cs.dennisc.croquet.Dialog dialog, edu.cmu.cs.dennisc.croquet.Label explanationLabel ) {
		JComponent< ? > child = this.prologue( step );
		assert child != null;
		step.setMainPanel( child );
		BorderPanel rv = new BorderPanel();
		rv.setBackgroundColor( child.getBackgroundColor() );
		rv.addComponent( child, BorderPanel.Constraint.CENTER );
		rv.addComponent( explanationLabel, BorderPanel.Constraint.PAGE_END );
		return rv;
	}
	@Override
	protected void release( org.lgna.croquet.steps.InputDialogOperationStep step, edu.cmu.cs.dennisc.croquet.Dialog dialog, boolean isCompleted ) {
		this.epilogue( step, isCompleted );
	}
}
