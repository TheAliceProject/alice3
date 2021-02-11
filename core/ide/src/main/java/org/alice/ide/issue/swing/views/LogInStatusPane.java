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
package org.alice.ide.issue.swing.views;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteUser;
import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.awt.WindowUtilities;
import edu.cmu.cs.dennisc.javax.swing.JDialogUtilities;
import edu.cmu.cs.dennisc.javax.swing.LabelUtilities;
import edu.cmu.cs.dennisc.javax.swing.SpringUtilities;
import edu.cmu.cs.dennisc.javax.swing.components.JCardPane;
import edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane;
import edu.cmu.cs.dennisc.javax.swing.components.JPageAxisPane;
import edu.cmu.cs.dennisc.javax.swing.components.JPane;
import edu.cmu.cs.dennisc.javax.swing.components.JRowsSpringPane;
import edu.cmu.cs.dennisc.login.AccountInformation;
import edu.cmu.cs.dennisc.login.AccountManager;
import org.alice.ide.browser.BrowserOperation;
import org.alice.ide.issue.ReportSubmissionConfiguration;
import org.alice.ide.operations.InconsequentialActionOperation;
import org.lgna.croquet.Operation;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.UUID;

class PasswordPane extends JPageAxisPane {
  private static final String HIDDEN_KEY = "HIDDEN_KEY";
  private static final String EXPOSED_KEY = "EXPOSED_KEY";

  class PasswordCardPane extends JCardPane {
    private JPasswordField hidden = new JPasswordField() {
      @Override
      public Dimension getPreferredSize() {
        return DimensionUtilities.constrainToMinimumWidth(super.getPreferredSize(), 256);
      }
    };
    private JTextField exposed = new JTextField();
    private boolean isExposed = false;

    public PasswordCardPane() {
      this.add(hidden, HIDDEN_KEY);
      this.add(exposed, EXPOSED_KEY);
      this.exposed.setDocument(this.hidden.getDocument());
    }

    public void toggle() {
      if (this.isExposed) {
        this.show(HIDDEN_KEY);
        this.hidden.requestFocus();
      } else {
        this.show(EXPOSED_KEY);
        this.exposed.requestFocus();
      }
      this.isExposed = !this.isExposed;
    }
  }

  private PasswordCardPane passwordCardPane = new PasswordCardPane();
  private JCheckBox checkBox = new JCheckBox("display password");

  public PasswordPane() {
    checkBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        passwordCardPane.toggle();
      }
    });
    this.add(passwordCardPane);
    this.add(checkBox);
  }

  public String getPassword() {
    return passwordCardPane.exposed.getText();
  }
}

class LogInPane extends JPageAxisPane {
  class TestLogInOperation extends InconsequentialActionOperation {
    public TestLogInOperation() {
      super(UUID.fromString("cf700b82-c80b-4fb4-8886-2d170503a253"));
    }

    @Override
    protected void localize() {
      super.localize();
      this.setName("Log In");
    }

    @Override
    protected void performInternal() {
      try {
        JiraSoapServiceServiceLocator jiraSoapServiceLocator = new JiraSoapServiceServiceLocator();
        JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2(new URL(ReportSubmissionConfiguration.JIRA_SOAP_URL));
        String username = textUsername.getText();
        try {
          String password = passwordPane.getPassword();
          String token = service.login(username, password);
          try {
            RemoteUser remoteUser = service.getUser(token, username);
            AccountManager.logIn(LogInStatusPane.BUGS_ALICE_ORG_KEY, username, password, remoteUser.getFullname());
          } finally {
            service.logout(token);
          }
          SwingUtilities.getRoot(LogInPane.this).setVisible(false);
        } catch (RemoteAuthenticationException rae) {
          JOptionPane.showMessageDialog(null, rae);
          //edu.cmu.cs.dennisc.account.AccountManager.logOut( BUGS_ALICE_ORG_KEY );
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  private JTextField textUsername = new JTextField();
  private PasswordPane passwordPane = new PasswordPane();
  //todo: remove. rely only on operations.
  private JButton logInButton = new TestLogInOperation().createButton().getAwtComponent();

  private Component createLabel(String text) {
    JLabel rv = LabelUtilities.createLabel(text);
    rv.setVerticalAlignment(SwingConstants.TOP);
    rv.setHorizontalAlignment(SwingConstants.TRAILING);
    return rv;
  }

  public LogInPane() {
    JRowsSpringPane rowsPane = new JRowsSpringPane(8, 4) {
      @Override
      protected List<Component[]> addComponentRows(List<Component[]> rv) {
        rv.add(SpringUtilities.createRow(createLabel("Username:"), textUsername));
        rv.add(SpringUtilities.createRow(createLabel("Password:"), passwordPane));
        return rv;
      }
    };

    JPane signUpPane = new JPane();
    signUpPane.add(LabelUtilities.createLabel("Not a member?"));
    Operation signUpOperation = new BrowserOperation(UUID.fromString("450727b2-d86a-4812-a77c-99eb785e10b2"),
                                                     ReportSubmissionConfiguration.JIRA_SIGNUP_URL);
    signUpPane.add(signUpOperation.createHyperlink().getAwtComponent());
    signUpPane.add(LabelUtilities.createLabel("for an account."));

    JPane buttonPane = new JPane();
    buttonPane.add(this.logInButton);

    signUpPane.setAlignmentX(JComponent.CENTER_ALIGNMENT);
    rowsPane.setAlignmentX(JComponent.CENTER_ALIGNMENT);
    buttonPane.setAlignmentX(JComponent.CENTER_ALIGNMENT);

    this.add(signUpPane);
    this.add(Box.createVerticalStrut(32));
    this.add(rowsPane);
    this.add(Box.createVerticalStrut(6));
    this.add(buttonPane);

    this.setBorder(BorderFactory.createEmptyBorder(8, 32, 8, 32));
  }

  public JButton getLogInButton() {
    return this.logInButton;
  }
}

public class LogInStatusPane extends JCardPane {
  public static final String BUGS_ALICE_ORG_KEY = "bugs.alice.org";

  class LogInOperation extends InconsequentialActionOperation {
    public LogInOperation() {
      super(UUID.fromString("f2d620ad-9b18-42e7-8b77-240e7a829b03"));
      this.setName("Log In... (Optional)");
    }

    @Override
    protected void performInternal() {
      LogInPane pane = new LogInPane();
      // TODO work out a way to get an owner or get rid of this code
      Component owner = null;
      JDialog dialog = JDialogUtilities.createPackedJDialog(pane, owner, "Log In", true, WindowConstants.DISPOSE_ON_CLOSE);
      WindowUtilities.setLocationOnScreenToCenteredWithin(dialog, SwingUtilities.getRoot(owner));
      dialog.getRootPane().setDefaultButton(pane.getLogInButton());
      dialog.setVisible(true);
      AccountInformation accountInformation = AccountManager.get(LogInStatusPane.BUGS_ALICE_ORG_KEY);
      if (accountInformation != null) {
        LogInStatusPane.this.onPane.refresh();
        LogInStatusPane.this.show(ON_KEY);
      }
    }
  }

  class LogOutOperation extends InconsequentialActionOperation {
    public LogOutOperation() {
      super(UUID.fromString("73bf08cc-3666-463d-86da-3d483a4d8f2b"));
      this.setName("Log Out");
    }

    @Override
    protected void performInternal() {
      AccountManager.logOut(LogInStatusPane.BUGS_ALICE_ORG_KEY);
      LogInStatusPane.this.show(OFF_KEY);
    }
  }

  private static final String OFF_KEY = "OFF_KEY";
  private static final String ON_KEY = "ON_KEY";
  private JButton logInButton = new LogInOperation().createButton().getAwtComponent();
  private JButton logOutButton = new LogOutOperation().createButton().getAwtComponent();

  class OffPane extends JLineAxisPane {
    public OffPane() {
      this.add(Box.createHorizontalGlue());
      this.add(logInButton);
    }
  }

  class OnPane extends JLineAxisPane {
    private JLabel nameLabel = new JLabel("Full Name") {
      @Override
      public Dimension getPreferredSize() {
        return DimensionUtilities.constrainToMinimumWidth(super.getPreferredSize(), 320);
      }

      @Override
      public Dimension getMaximumSize() {
        return this.getPreferredSize();
      }
    };

    public OnPane() {
      this.refresh();
      this.nameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
      this.nameLabel.setForeground(Color.WHITE);
      this.add(Box.createHorizontalGlue());
      this.add(this.nameLabel);
      this.add(Box.createHorizontalStrut(8));
      this.add(logOutButton);
    }

    public void refresh() {
      AccountInformation accountInformation = AccountManager.get(LogInStatusPane.BUGS_ALICE_ORG_KEY);
      if (accountInformation != null) {
        this.nameLabel.setText(accountInformation.getFullName());
        this.revalidate();
      }
    }
  }

  private OffPane offPane = new OffPane();
  private OnPane onPane = new OnPane();

  public LogInStatusPane() {
    this.add(this.offPane, OFF_KEY);
    this.add(this.onPane, ON_KEY);
    AccountInformation accountInformation = AccountManager.get(LogInStatusPane.BUGS_ALICE_ORG_KEY);
    if (accountInformation != null) {
      LogInStatusPane.this.show(ON_KEY);
    }
  }

  //  @Override
  //  public void show( String key ) {
  //    super.show( key );
  //    this.revalidate();
  //  }
  public static void main(String[] args) {
    LogInStatusPane pane = new LogInStatusPane();
    //LogInPane pane = new LogInPane();
    //PasswordPane pane = new PasswordPane();
    JDialog dialog = JDialogUtilities.createPackedJDialog(pane, null, "", true, WindowConstants.DISPOSE_ON_CLOSE);
    dialog.getContentPane().setBackground(Color.DARK_GRAY);
    dialog.setLocation(200, 200);
    dialog.setVisible(true);
  }
}
