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
package org.alice.ide.issue;

/**
 * @author Dennis Cosgrove
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
	private static final String SILENTLY_FAIL_TEXT = "Silently Fail";
	private static final String CONTINUE_TEXT = "Continue";
	private boolean isBugReportSubmissionPaneDesired = true;
	private int count = 0;
	private String title = "";
	private String applicationName = "Alice";
	public void setTitle( String title ) {
		this.title = title;
	}
	public void setApplicationName( String applicationName ) {
		this.applicationName = applicationName;
	}
	public void uncaughtException( Thread thread, Throwable throwable ) {
		this.count ++;
		throwable.printStackTrace();
		if( this.isBugReportSubmissionPaneDesired ) {
			try {
				org.alice.ide.issue.CaughtExceptionPane bugReportPane = new org.alice.ide.issue.CaughtExceptionPane();
				bugReportPane.setThreadAndThrowable( thread, throwable );
//				javax.swing.JFrame frame = org.alice.ide.IDE.getSingleton().getFrame();
//				if( frame != null ) {
//					//pass
//				} else {
//					frame = new javax.swing.JFrame();
//				}
				java.awt.Component owner = org.alice.ide.IDE.getSingleton().getFrame().getAwtComponent();
//				while( true ) {
					javax.swing.JDialog dialog = edu.cmu.cs.dennisc.javax.swing.JDialogUtilities.createPackedJDialog( bugReportPane, owner, this.title, true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
					dialog.getRootPane().setDefaultButton( bugReportPane.getSubmitButton() );
					dialog.setVisible( true );

					if( bugReportPane.isSubmitAttempted() ) {
						if( bugReportPane.isSubmitBackgrounded() ) {
							//javax.swing.JOptionPane.showMessageDialog( frame, "Thank you for submitting a bug report." );
						} else {
							if( bugReportPane.isSubmitSuccessful() ) {
								javax.swing.JOptionPane.showMessageDialog( owner, "Your bug report has been successfully submitted.  Thank you." );
							} else {
								javax.swing.JOptionPane.showMessageDialog( owner, "Your bug report FAILED to submit.  Thank you for trying." );
							}
						}
//						break;
					} else {
						if( this.count > 1 ) {
							Object[] options = { CONTINUE_TEXT, SILENTLY_FAIL_TEXT };
							String message = "If you are caught in an unending stream of exceptions:\n    1) Press the \"" + SILENTLY_FAIL_TEXT + "\" button,\n    2) Attempt save your project to a different file (use Save As...), and\n    3) Restart " + this.applicationName + ".\nElse\n    1) Press the \"" + CONTINUE_TEXT + "\" button.";
							String title = "Multiple Exceptions Detected";
							int result = javax.swing.JOptionPane.showOptionDialog( owner, message, title, javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, options, CONTINUE_TEXT );
							if( result == javax.swing.JOptionPane.NO_OPTION ) {
								this.isBugReportSubmissionPaneDesired = false;
							}
						}
//						int result = javax.swing.JOptionPane.showConfirmDialog( frame,
//								"NOTE: You do not actually have to fill in any of the fields to submit a bug report.\n\nWould you like to submit this bug?\n\n(If you wish to not see this dialog again during this session, press cancel.  NOTE: Alice may silently fail under these conditions.)",
//								"Bug report NOT submitted", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION );
//						if( result == javax.swing.JOptionPane.YES_OPTION ) {
//							//pass
//						} else {
//							if( result == javax.swing.JOptionPane.CANCEL_OPTION ) {
//								this.isBugReportSubmissionPaneDesired = false;
//							}
//							break;
//						}
					}
//				}
			} catch( Throwable t ) {
				System.err.println( "exception thrown in ExceptionHandler" );
				t.printStackTrace();
			}
		}
	}
	
	public static void main( String[] args ) {
		Thread.setDefaultUncaughtExceptionHandler( new ExceptionHandler() );
		throw new RuntimeException();
	}
}
