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
package org.alice.ide.issue.swing;

/**
 * @author Dennis Cosgrove
 */
public abstract class JSubmitDialog extends javax.swing.JFrame {
	private class SubmitAction extends javax.swing.AbstractAction {
		public SubmitAction() {
			super( "submit bug report" );
		}

		//public void setAwtWindow( java.awt.Window awtWindow ) {
		//	this.awtWindow = awtWindow;
		//}

		public void actionPerformed( java.awt.event.ActionEvent e ) {
			//		protected abstract IssueWorker createIssueWorker( Thread t, Throwable e );
			//		IssueWorker worker = this.createIssueWorker( t, e );
			//		worker.execute();
			submit();
		}

		//private java.awt.Window awtWindow;
	}

	private final javax.swing.text.Document summaryDocument;
	private final javax.swing.text.Document descriptionDocument;
	private final javax.swing.text.Document stepsDocument;

	public JSubmitDialog( String applicationName, javax.swing.Icon logoIcon ) {
		this.setModalExclusionType( java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE );
		StringBuilder sbTitle = new StringBuilder();
		sbTitle.append( "Please Submit Bug Report: " );
		sbTitle.append( applicationName );
		this.setTitle( sbTitle.toString() );

		SubmitAction submitAction = new SubmitAction();

		StringBuilder sbHeader = new StringBuilder();
		sbHeader.append( "<html>" );
		sbHeader.append( "An exception has been caught" );

		if( org.alice.ide.issue.UserProgramRunningStateUtilities.isUserProgramRunning() ) {
			sbHeader.append( " during the running of your program.<p>" );
			sbHeader.append( "<p>While this <em>could</em> be the result of a problem in your code,<br>it is likely a bug in " );
			sbHeader.append( applicationName );
			sbHeader.append( ".<p>" );
		} else {
			sbHeader.append( ".<p>" );
		}
		sbHeader.append( "<p>Please accept our apologies and press the \"" );
		sbHeader.append( submitAction.getValue( javax.swing.Action.NAME ) );
		sbHeader.append( "\" button.<p>" );
		sbHeader.append( "<p>We will do our best to fix the problem and make a new release.<p>" );
		sbHeader.append( "</html>" );
		javax.swing.JLabel headerLabel = new javax.swing.JLabel( sbHeader.toString() );
		headerLabel.setForeground( java.awt.Color.WHITE );

		javax.swing.JLabel logoLabel = new javax.swing.JLabel( logoIcon );
		javax.swing.JButton submitButton = new javax.swing.JButton( submitAction );

		javax.swing.JPanel headerPanel = new javax.swing.JPanel();
		headerPanel.setLayout( new java.awt.BorderLayout() );
		headerPanel.add( logoLabel, java.awt.BorderLayout.LINE_START );
		headerPanel.add( headerLabel, java.awt.BorderLayout.CENTER );
		headerPanel.setBackground( java.awt.Color.DARK_GRAY );

		this.setLayout( new java.awt.BorderLayout() );

		javax.swing.JTextField summaryTextField = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField();
		javax.swing.JTextArea descriptionTextArea = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea();
		javax.swing.JTextArea stepsTextArea = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea();

		this.summaryDocument = summaryTextField.getDocument();
		this.descriptionDocument = descriptionTextArea.getDocument();
		this.stepsDocument = stepsTextArea.getDocument();

		javax.swing.JPanel panel = new javax.swing.JPanel();
		panel.setLayout( new net.miginfocom.swing.MigLayout( "fill", "[align right, grow 0][]" ) );
		panel.add( new javax.swing.JLabel( "summary:" ) );
		panel.add( summaryTextField, "grow, wrap" );
		panel.add( new javax.swing.JLabel( "description:" ) );
		panel.add( descriptionTextArea, "grow, wrap" );
		panel.add( new javax.swing.JLabel( "steps:" ) );
		panel.add( stepsTextArea, "grow, wrap" );
		panel.add( new javax.swing.JLabel( "exception:" ) );
		panel.add( new JExceptionSubPane(), "wrap" );
		panel.add( new javax.swing.JLabel( "environment:" ) );
		panel.add( new JEnvironmentSubPane(), "wrap" );

		javax.swing.JPanel panelA = new javax.swing.JPanel();
		panelA.setLayout( new java.awt.BorderLayout() );
		panelA.add( panel, java.awt.BorderLayout.CENTER );
		panelA.add( submitButton, java.awt.BorderLayout.PAGE_END );

		this.getContentPane().add( headerPanel, java.awt.BorderLayout.PAGE_START );
		this.getContentPane().add( panelA, java.awt.BorderLayout.PAGE_END );
	}

	protected abstract void submit();
}
