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

/**
 * @author Dennis Cosgrove
 */
public class AliceIssueReportingHub implements org.lgna.issue.IssueReportingHub {
	@Override
	public boolean isLoginSupported() {
		return true;
	}

	@Override
	public void checkRemoteUser( String username, String password, org.lgna.issue.IssueReportingRemoteUserObserver observer ) {
		com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator jiraSoapServiceLocator = new com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator();
		com.atlassian.jira.rpc.soap.client.JiraSoapService service = null;
		try {
			java.net.URL url = new java.net.URL( "http://bugs.alice.org:8080/rpc/soap/jirasoapservice-v2" );
			service = jiraSoapServiceLocator.getJirasoapserviceV2( url );
		} catch( java.net.MalformedURLException murle ) {
			// error
			murle.printStackTrace();
		} catch( javax.xml.rpc.ServiceException se ) {
			se.printStackTrace();
		}
		boolean isConnectionSeeminglyPossible;
		org.lgna.issue.IssueReportRemoteUser user = null;
		if( service != null ) {
			isConnectionSeeminglyPossible = true;
			try {
				String token = service.login( username, password );
				try {
					com.atlassian.jira.rpc.soap.client.RemoteUser jiraRemoteUser = service.getUser( token, username );
					if( jiraRemoteUser != null ) {
						user = new AliceIssueReportRemoteUser( jiraRemoteUser );
						//todo?
						//edu.cmu.cs.dennisc.login.AccountManager.logIn( LogInStatusPane.BUGS_ALICE_ORG_KEY, username, password, jiraRemoteUser.getFullname() );
					}
				} catch( com.atlassian.jira.rpc.soap.client.RemotePermissionException rpe ) {
					rpe.printStackTrace();
				} catch( java.rmi.RemoteException re ) {
					re.printStackTrace();
				} finally {
					try {
						service.logout( token );
					} catch( java.rmi.RemoteException re ) {
						//todo?
						re.printStackTrace();
					}
				}
			} catch( com.atlassian.jira.rpc.soap.client.RemoteException jirare ) {
				//could not log in
			} catch( java.rmi.RemoteException re ) {
				//could not connect
				isConnectionSeeminglyPossible = false;
			}
		} else {
			isConnectionSeeminglyPossible = false;
		}
		observer.remoteUserAttemptCompleted( isConnectionSeeminglyPossible, user );
	}
}
