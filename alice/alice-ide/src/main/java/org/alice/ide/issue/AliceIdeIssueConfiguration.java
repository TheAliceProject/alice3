/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.issue;

/**
 * @author Dennis Cosgrove
 */
public class AliceIdeIssueConfiguration extends IdeIssueConfiguration {
	@Override
	public String getApplicationName() {
		return "Alice";
	}

	@Override
	public String getDownloadUrlSpec() {
		return "http://www.alice.org/get-alice/alice-3";
	}

	@Override
	public String getDownloadUrlText() {
		return this.getDownloadUrlSpec();
	}

	@Override
	public void submit( org.lgna.issue.swing.JSubmitPane jSubmitPane ) {
		org.lgna.issue.ApplicationIssueConfiguration config = jSubmitPane.getConfig();
		int option = javax.swing.JOptionPane.showConfirmDialog( jSubmitPane, "Submitting your current project might greatly help the " + config.getApplicationName() + " team in diagnosing and fixing this bug.\n\nThis bug report (and your project) will only be viewable by the " + config.getApplicationName() + " team.\n\nWould you like to submit your project with this bug report?", "Submit project?", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION );
		if( option == javax.swing.JOptionPane.CANCEL_OPTION ) {
			//pass
		} else {
			jSubmitPane.setSubmitAttempted( true );
			new org.alice.ide.issue.AliceIssueSubmissionProgressWorker( jSubmitPane, option == javax.swing.JOptionPane.YES_OPTION ).execute();
		}
	}
}
