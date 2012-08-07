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
package org.lgna.croquet;

import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.BoxUtilities;
import org.lgna.croquet.components.Button;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.Dialog;
import org.lgna.croquet.components.JComponent;
import org.lgna.croquet.components.LineAxisPanel;

/**
 * @author Dennis Cosgrove
 */
public abstract class InputDialogOperation<T> extends GatedCommitDialogOperation {
	public static final org.lgna.croquet.history.Step.Key< org.lgna.croquet.components.Container< ? > > INPUT_PANEL_KEY = org.lgna.croquet.history.Step.Key.createInstance( "InputDialogOperation.INPUT_PANEL_KEY" );

	public InputDialogOperation(Group group, java.util.UUID individualId) {
		super(group, individualId);
	}
	@Override
	protected Component< ? > createControlsPanel( org.lgna.croquet.history.CompletionStep<?> step, Dialog dialog ) {
		Button okButton = this.getCompleteOperation().createButton();
		Button cancelButton; 
		if( this.isCancelDesired() ) {
			cancelButton = this.getCancelOperation().createButton();
		} else {
			cancelButton = null;
		}
		
		LineAxisPanel rv = new LineAxisPanel();
		rv.addComponent( BoxUtilities.createHorizontalGlue() );
		
		if( cancelButton != null ) {
			Button leadingButton;
			Button trailingButton;
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				leadingButton = okButton;
				trailingButton = cancelButton;
			} else {
				leadingButton = cancelButton;
				trailingButton = okButton;
			}
			rv.addComponent( leadingButton );
			rv.addComponent( BoxUtilities.createHorizontalSliver( 4 ) );
			rv.addComponent( trailingButton );
		} else {
			rv.addComponent( okButton );
		}
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
		dialog.setDefaultButton( okButton );
		return rv;
	}

	protected boolean isCancelDesired() {
		return true;
	}

	protected abstract JComponent< ? > prologue( org.lgna.croquet.history.CompletionStep<?> step );
	protected abstract void epilogue( org.lgna.croquet.history.CompletionStep<?> step, boolean isCommit );
	
	@Override
	protected org.lgna.croquet.components.Component< ? > createMainPanel( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.JComponent< javax.swing.JLabel > explanationLabel ) {
		JComponent< ? > child = this.prologue( step );
		if( child != null ) {
			step.putEphemeralDataFor( INPUT_PANEL_KEY, child );
			BorderPanel rv = new BorderPanel.Builder()
				.center( child )
				.pageEnd( explanationLabel )
			.build();
			rv.setBackgroundColor( child.getBackgroundColor() );
			return rv;
		} else {
			return null;
		}
	}
	@Override
	protected void release( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog, boolean isCompleted ) {
		this.epilogue( step, isCompleted );
	}

	protected String getInternalExplanation( org.lgna.croquet.history.CompletionStep<?> step ) {
		return null;
	}
	public static interface ExternalCommitButtonDisabler {
		public String getExplanationIfCommitButtonShouldBeDisabled( org.lgna.croquet.history.CompletionStep<?> step );
	}
	private ExternalCommitButtonDisabler externalCommitButtonDisabler;
	public ExternalCommitButtonDisabler getExternalCommitButtonDisabler() {
		return this.externalCommitButtonDisabler;
	}
	public void setExternalCommitButtonDisabler( ExternalCommitButtonDisabler externalCommitButtonDisabler ) {
		this.externalCommitButtonDisabler = externalCommitButtonDisabler;
	}

	@Override
	protected final String getExplanation( org.lgna.croquet.history.CompletionStep<?> step ) {
		String explanation = this.getInternalExplanation( step );
		if( this.externalCommitButtonDisabler != null ) {
			String externalExplanation = this.externalCommitButtonDisabler.getExplanationIfCommitButtonShouldBeDisabled( step );
			if( externalExplanation != null ) {
				explanation = externalExplanation;
			}
		}
		return explanation;
	}

	public String getTutorialFinishNoteText( org.lgna.croquet.history.CompletionStep<?> step ) {
		return "When finished press the <strong>OK</strong> button.";
	}
}
