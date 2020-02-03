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

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteUser;
import org.lgna.issue.IssueReportRemoteUser;
import org.lgna.issue.IssueReportingHub;
import org.lgna.issue.IssueReportingRemoteUserObserver;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * @author Dennis Cosgrove
 */
public class AliceIssueReportingHub implements IssueReportingHub {
  @Override
  public boolean isLoginSupported() {
    return true;
  }

  @Override
  public void checkRemoteUser(String username, String password, IssueReportingRemoteUserObserver observer) {
    JiraSoapServiceServiceLocator jiraSoapServiceLocator = new JiraSoapServiceServiceLocator();
    JiraSoapService service = null;
    try {
      URL url = new URL(ReportSubmissionConfiguration.JIRA_SOAP_URL);
      service = jiraSoapServiceLocator.getJirasoapserviceV2(url);
    } catch (MalformedURLException murle) {
      // error
      murle.printStackTrace();
    } catch (ServiceException se) {
      se.printStackTrace();
    }
    boolean isConnectionSeeminglyPossible;
    IssueReportRemoteUser user = null;
    if (service != null) {
      isConnectionSeeminglyPossible = true;
      try {
        String token = service.login(username, password);
        try {
          RemoteUser jiraRemoteUser = service.getUser(token, username);
          if (jiraRemoteUser != null) {
            user = new AliceIssueReportRemoteUser(jiraRemoteUser);
            //todo?
            //edu.cmu.cs.dennisc.login.AccountManager.logIn( LogInStatusPane.BUGS_ALICE_ORG_KEY, username, password, jiraRemoteUser.getFullname() );
          }
        } catch (RemotePermissionException rpe) {
          rpe.printStackTrace();
        } catch (RemoteException re) {
          re.printStackTrace();
        } finally {
          try {
            service.logout(token);
          } catch (RemoteException re) {
            //todo?
            re.printStackTrace();
          }
        }
      } catch (com.atlassian.jira.rpc.soap.client.RemoteException jirare) {
        //could not log in
      } catch (RemoteException re) {
        //could not connect
        isConnectionSeeminglyPossible = false;
      }
    } else {
      isConnectionSeeminglyPossible = false;
    }
    observer.remoteUserAttemptCompleted(isConnectionSeeminglyPossible, user);
  }
}
