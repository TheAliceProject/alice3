/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.issue;

/**
 * @author Dennis Cosgrove
 */
public abstract class IssueSubmissionProgressWorker extends edu.cmu.cs.dennisc.worker.WorkerWithProgress<Boolean, String> {
	private static final String START_MESSAGE = "START_MESSAGE";
	private static final String END_MESSAGE = "END_MESSAGE";

	public IssueSubmissionProgressWorker( org.lgna.issue.swing.JSubmitPane owner ) {
		this.owner = owner;
	}

	protected abstract Boolean doInternal_onBackgroundThread( edu.cmu.cs.dennisc.issue.Issue.Builder issueBuilder ) throws Exception;

	@Override
	protected final Boolean do_onBackgroundThread() throws Exception {
		this.publish( START_MESSAGE );
		edu.cmu.cs.dennisc.issue.Issue.Builder issueBuilder = this.owner.createIssueBuilder();
		boolean rv = this.doInternal_onBackgroundThread( issueBuilder );
		this.publish( END_MESSAGE );
		return rv;
	}

	@Override
	protected final void handleProcess_onEventDispatchThread( java.util.List<String> chunks ) {
		for( String message : chunks ) {
			if( START_MESSAGE.equals( message ) ) {
				javax.swing.JDialog dialog = new edu.cmu.cs.dennisc.javax.swing.JDialogBuilder()
						.owner( this.owner )
						.title( "Uploading Bug Report" )
						.build();
				dialog.add( this.progressPane, java.awt.BorderLayout.CENTER );
				dialog.pack();
				dialog.setVisible( true );
			} else if( END_MESSAGE.equals( message ) ) {
				javax.swing.SwingUtilities.getRoot( this.progressPane ).setVisible( false );
			} else {
				this.progressPane.addMessage( message );
			}
		}
	}

	@Override
	protected final void handleDone_onEventDispatchThread( Boolean value ) {
		if( this.progressPane.isBackgrounded() || ( javax.swing.SwingUtilities.getRoot( this.owner ).isVisible() == false ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "issue submission result:", value );
		} else {
			if( value ) {
				javax.swing.JOptionPane.showMessageDialog( this.owner, "Your bug report has been successfully submitted.  Thank you." );
			} else {
				javax.swing.JOptionPane.showMessageDialog( this.owner, "Your bug report FAILED to submit.  Thank you for trying." );
			}
			this.hideOwnerDialog();
		}
	}

	public void hideOwnerDialog() {
		javax.swing.SwingUtilities.getRoot( this.owner ).setVisible( false );
	}

	private final org.lgna.issue.swing.JSubmitPane owner;
	private final org.lgna.issue.swing.JProgressPane progressPane = new org.lgna.issue.swing.JProgressPane( this );
}
