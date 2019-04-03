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
package org.alice.ide.croquet.models.help;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteUser;
import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import edu.cmu.cs.dennisc.login.AccountManager;
import org.alice.ide.croquet.models.help.views.LoginView;
import org.alice.ide.issue.ReportSubmissionConfiguration;
import org.alice.ide.issue.swing.views.LogInStatusPane;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.simple.SimpleApplication;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * @author Matt May
 */
public class BugLoginComposite extends AbstractLoginComposite<LoginView> {
  private static class SingletonHolder {
    private static BugLoginComposite instance = new BugLoginComposite();
  }

  public static BugLoginComposite getInstance() {
    return SingletonHolder.instance;
  }

  private BugLoginComposite() {
    super(UUID.fromString("e73910c0-ee70-4e48-899d-52ca96d21c9f"), ReportIssueComposite.ISSUE_GROUP);
  }

  private RemoteUser remoteUser;

  public RemoteUser getRemoteUser() {
    return this.remoteUser;
  }

  @Override
  protected boolean tryToLogin() {
    try {
      JiraSoapServiceServiceLocator jiraSoapServiceLocator = new JiraSoapServiceServiceLocator();
      JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2(new URL(ReportSubmissionConfiguration.JIRA_SOAP_URL));
      String username = userNameState.getValue();
      String password = passwordState.getValue();
      String token;
      try {
        token = service.login(username, password);
      } catch (com.atlassian.jira.rpc.soap.client.RemoteException e) {
        //could not log in
        return false;
      } catch (RemoteException e) {
        //could not connect
        this.setConnectionFailed(true);
        return false;
      }
      this.setConnectionFailed(false);
      try {
        try {
          remoteUser = service.getUser(token, username);
        } catch (RemotePermissionException e) {
          e.printStackTrace();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        AccountManager.logIn(LogInStatusPane.BUGS_ALICE_ORG_KEY, username, password, remoteUser.getFullname());
      } finally {
        try {
          service.logout(token);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
      return true;
    } catch (MalformedURLException e1) {
      e1.printStackTrace();
    } catch (ServiceException e1) {
      e1.printStackTrace();
    }
    return false;
  }

  @Override
  public void logout() {
    AccountManager.logOut(LogInStatusPane.BUGS_ALICE_ORG_KEY);
    isLoggedIn.setValueTransactionlessly(false);
  }

  @Override
  public String updateUserNameForWelcomeString() {
    //    this.getParent().getLogOutCard().getUsernameLabel().getAwtComponent().setForeground( Color.WHITE );
    if (getRemoteUser() != null) {
      return getRemoteUser().getFullname();
    } else {
      return "";
    }
  }

  public static void main(String[] args) throws Exception {
    UIManagerUtilities.setLookAndFeel("Nimbus");
    SimpleApplication app = new SimpleApplication();
    //new org.alice.stageide.StageIDE();
    try {
      new BugLoginComposite().getLaunchOperation().fire();
    } catch (CancelException ce) {
      //pass
    }
  }
}
