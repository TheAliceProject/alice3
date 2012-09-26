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

class ExceptionPane extends edu.cmu.cs.dennisc.toolkit.issue.AbstractCaughtExceptionPane {
	@Override
	protected edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration getReportSubmissionConfiguration() {
		return new org.alice.ide.issue.ReportSubmissionConfiguration() {
			@Override
			public edu.cmu.cs.dennisc.jira.rpc.Authenticator getJIRAViaRPCAuthenticator() {
				final edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( edu.cmu.cs.dennisc.toolkit.login.LogInStatusPane.BUGS_ALICE_ORG_KEY );
				if( accountInformation != null ) {
					return new edu.cmu.cs.dennisc.jira.rpc.Authenticator() {
						public Object login( redstone.xmlrpc.XmlRpcClient client ) throws redstone.xmlrpc.XmlRpcException, redstone.xmlrpc.XmlRpcFault {
							return edu.cmu.cs.dennisc.jira.rpc.RPCUtilities.logIn( client, accountInformation.getID(), accountInformation.getPassword() );
						}
					};
				} else {
					return super.getJIRAViaRPCAuthenticator();
				}
			}

			@Override
			public edu.cmu.cs.dennisc.jira.soap.Authenticator getJIRAViaSOAPAuthenticator() {
				final edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( edu.cmu.cs.dennisc.toolkit.login.LogInStatusPane.BUGS_ALICE_ORG_KEY );
				if( accountInformation != null ) {
					return new edu.cmu.cs.dennisc.jira.soap.Authenticator() {
						public String login( com.atlassian.jira.rpc.soap.client.JiraSoapService service ) throws java.rmi.RemoteException {
							return service.login( accountInformation.getID(), accountInformation.getPassword() );
						}
					};
				} else {
					return super.getJIRAViaSOAPAuthenticator();
				}
			}
		};
	}

	@Override
	protected String getJIRAProjectKey() {
		return "AIIIP";
	}

	@Override
	protected String[] getAffectsVersions() {
		return new String[] { org.lgna.project.Version.getCurrentVersionText() };
	}
}

class ExceptionHandler implements Thread.UncaughtExceptionHandler {
	public void uncaughtException( Thread thread, Throwable throwable ) {
		throwable.printStackTrace();
		int result = javax.swing.JOptionPane.showConfirmDialog( null, "please upload bug report", "", javax.swing.JOptionPane.OK_CANCEL_OPTION );
		switch( result ) {
		case javax.swing.JOptionPane.OK_OPTION:
			ExceptionPane bugReportPane = new ExceptionPane();
			bugReportPane.setThreadAndThrowable( thread, throwable );
			bugReportPane.getSubmitButton().doClick();
			break;
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class FieldTest {
	static {
		ExceptionHandler exceptionHandler = new ExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler( exceptionHandler );
	}
	public static void main( String[] args ) {
		
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.setSize( 640, 480 );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
//		java.util.List<edu.cmu.cs.dennisc.java.lang.Property> properties = edu.cmu.cs.dennisc.java.lang.SystemUtilities.getPropertyList();
//		java.util.Collections.sort( properties );
//		for( edu.cmu.cs.dennisc.java.lang.Property property : properties ) {
//			System.out.println( property );
//		}
		throw new RuntimeException();
	}
}
