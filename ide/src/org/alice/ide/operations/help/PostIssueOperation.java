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
package org.alice.ide.operations.help;

/**
 * @author Dennis Cosgrove
 */
public abstract class PostIssueOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	public PostIssueOperation( java.util.UUID individualId ) {
		super( individualId );
	}
	protected abstract edu.cmu.cs.dennisc.jira.JIRAReport.Type getIssueType();
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		final org.alice.ide.issue.PostIssuePane pane = new org.alice.ide.issue.PostIssuePane( this.getIssueType() );

		javax.swing.JFrame window = edu.cmu.cs.dennisc.javax.swing.JFrameUtilities.createPackedJFrame( pane, "Report Issue", javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		edu.cmu.cs.dennisc.java.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( window, context.getComponent().getAwtComponent() );
		window.getRootPane().setDefaultButton( pane.getSubmitButton() );
		window.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentHidden( java.awt.event.ComponentEvent e ) {
				if( pane.isSubmitAttempted() ) {
					if( pane.isSubmitBackgrounded() ) {
						//pass
					} else {
						if( pane.isSubmitSuccessful() ) {
							
							java.net.URL urlResult = pane.getURLResult();
							final String MESSAGE = "Your issue report has been successfully submitted.  Thank you.";
							Object message;
							String title = "Report Successfully Submitted";
							if( urlResult != null ) {
								edu.cmu.cs.dennisc.ui.html.HTMLPane htmlPane = new edu.cmu.cs.dennisc.ui.html.HTMLPane();
								StringBuffer sb = new StringBuffer();
								sb.append( "<html>" );
								sb.append( MESSAGE );
								sb.append( "<br>" );
								sb.append( "<br>" );
								//sb.append( "You can view your report (if it is public) at: " );
								sb.append( "View your report: " );
								sb.append( "<a href=\"" );
								sb.append( urlResult.toExternalForm() );
								sb.append( "\">" );
								sb.append( urlResult.toExternalForm() );
								sb.append( "</a>" );
								sb.append( "</html>" );
								htmlPane.setText( sb.toString() );
								htmlPane.setOpaque( false );
								message = htmlPane;
							} else {
								message = MESSAGE;
							}
							org.alice.ide.IDE.getSingleton().showMessageDialog( message, title, edu.cmu.cs.dennisc.croquet.MessageType.PLAIN );
						} else {
							org.alice.ide.IDE.getSingleton().showMessageDialog( "Your issue report FAILED to submit.  Thank you for trying.", "Report Submission Failed", edu.cmu.cs.dennisc.croquet.MessageType.ERROR );
						}
					}
				}
			}

			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}

			public void componentResized( java.awt.event.ComponentEvent e ) {
			}

			public void componentShown( java.awt.event.ComponentEvent e ) {
			}

		} );
		window.setVisible( true );
	}
}
