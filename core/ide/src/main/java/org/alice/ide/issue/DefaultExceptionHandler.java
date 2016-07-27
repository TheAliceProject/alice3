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
package org.alice.ide.issue;

import org.alice.ide.issue.croquet.LgnaExceptionComposite;

/**
 * @author Dennis Cosgrove
 */
public class DefaultExceptionHandler extends ExceptionHandler {
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

	@Override
	protected boolean handleLgnaRuntimeException( Thread thread, org.lgna.common.LgnaRuntimeException lgnare ) {
		org.lgna.croquet.Application application = org.lgna.croquet.Application.getActiveInstance();
		if( application != null ) {
			new LgnaExceptionComposite( thread, lgnare ).getLaunchOperation().fire();
			return true;
		} else {
			return false;
		}
	}

	private boolean isInTheMidstOfHandlingAThrowable = false;

	@Override
	protected synchronized void handleThrowable( Thread thread, Throwable throwable ) {
		try {
			edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( throwable );
		} catch( Throwable t ) {
			t.printStackTrace();
		}
		if( isInTheMidstOfHandlingAThrowable ) {
			//pass
		} else {
			this.isInTheMidstOfHandlingAThrowable = true;
			try {
				this.count++;
				if( this.isBugReportSubmissionPaneDesired ) {
					try {
						org.alice.ide.issue.swing.views.AbstractCaughtExceptionPane exceptionPane;
						if( throwable instanceof com.jogamp.opengl.GLException ) {
							exceptionPane = new org.alice.ide.issue.swing.views.CaughtGlExceptionPane();
						} else {
							exceptionPane = new org.alice.ide.issue.swing.views.CaughtExceptionPane();
						}

						exceptionPane.setThreadAndThrowable( thread, throwable );

						java.awt.Component owner;
						org.lgna.croquet.Application application = org.lgna.croquet.Application.getActiveInstance();
						if( application != null ) {
							org.lgna.croquet.views.Frame frame = application.getDocumentFrame().getFrame();
							if( frame != null ) {
								owner = frame.getAwtComponent();
							} else {
								owner = null;
							}
						} else {
							owner = null;
						}

						javax.swing.JDialog dialog = edu.cmu.cs.dennisc.javax.swing.JDialogUtilities.createPackedJDialog( exceptionPane, owner, this.title, true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
						dialog.getRootPane().setDefaultButton( exceptionPane.getSubmitButton() );
						dialog.setVisible( true );

						if( exceptionPane.isSubmitAttempted() ) {
							if( exceptionPane.isSubmitDone() ) {
								if( exceptionPane.isSubmitSuccessful() ) {
									javax.swing.JOptionPane.showMessageDialog( owner, "Your bug report has been successfully submitted.  Thank you." );
								} else {
									javax.swing.JOptionPane.showMessageDialog( owner, "Your bug report FAILED to submit.  Thank you for trying." );
								}
							} else {
								//backgrounded
							}
						} else {
							if( this.count > 1 ) {
								Object[] options = { CONTINUE_TEXT, SILENTLY_FAIL_TEXT };
								String message = "If you are caught in an unending stream of exceptions:\n    1) Press the \"" + SILENTLY_FAIL_TEXT + "\" button,\n    2) Attempt save your project to a different file (use Save As...), and\n    3) Restart " + this.applicationName + ".\nElse\n    1) Press the \"" + CONTINUE_TEXT + "\" button.";
								String title = "Multiple Exceptions Detected";
								int resultIgnore = javax.swing.JOptionPane.showOptionDialog( owner, message, title, javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, options, CONTINUE_TEXT );
								if( resultIgnore == javax.swing.JOptionPane.NO_OPTION ) {
									this.isBugReportSubmissionPaneDesired = false;
								}
							}
						}
						//					}
					} catch( Throwable t ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t );
					}
				}
			} finally {
				boolean isSystemExitDesired = true;
				org.lgna.croquet.Application application = org.lgna.croquet.Application.getActiveInstance();
				if( application != null ) {
					org.lgna.croquet.views.Frame frame = application.getDocumentFrame().getFrame();
					if( frame != null ) {
						if( frame.isVisible() ) {
							isSystemExitDesired = false;
						}
					}
				}
				this.isInTheMidstOfHandlingAThrowable = false;
				if( isSystemExitDesired ) {
					javax.swing.JOptionPane.showMessageDialog( null, "Exception occurred before application was able to show window.  Exiting." );
					System.exit( -1 );
				}
			}
		}
	}

	public static void main( String[] args ) throws Exception {
		org.lgna.croquet.simple.SimpleApplication application = new org.lgna.croquet.simple.SimpleApplication();
		Thread.setDefaultUncaughtExceptionHandler( new DefaultExceptionHandler() );
		while( true ) {
			new Thread() {
				@Override
				public void run() {
					super.run();
					throw new com.jogamp.opengl.GLException( "DELETE ME" );
					//throw new RuntimeException( "DELETE ME" );
					//throw new org.lgna.common.LgnaIllegalArgumentException( "DELETE ME", 0, null );
				}
			}.start();
			Thread.sleep( 100 );
		}
	}
}
