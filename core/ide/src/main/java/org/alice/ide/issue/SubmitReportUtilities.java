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

import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.jira.JIRAReport;
import org.alice.ide.croquet.models.help.views.AbstractIssueView;
import org.alice.ide.issue.swing.views.ProgressPane;
import org.lgna.project.ProjectVersion;

import javax.swing.JOptionPane;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SubmitReportUtilities {
  public static final String EMAIL_SUBJECT = "Alice %s %s %s";
  public static final String EMAIL_BODY = "Description\n\n%s\n\nSteps\n\n%s\n\nThread\n\n%s\n\nEnvironment\n\n%s";
  public static final String HELP_ADDRESS = "bug_report@alice.org";
  public static final String EMAIL_URI = "mailto:" + HELP_ADDRESS + "?subject=%s&body=%s";

  private SubmitReportUtilities() {
    throw new AssertionError();
  }

  // TODO Restore REST bug submission and then remove the email submission work around
  public static final boolean USE_REST_INTERFACE = false;

  public static <V extends AbstractIssueView> ProgressPane submitReport(JIRAReport report, Component root) {
    if (USE_REST_INTERFACE) {
        return submitBugByRest(report, root);
    }
    return submitBugByMail(report, root);
  }


  private static ProgressPane submitBugByRest(JIRAReport report, Component root) {
    ProgressPane progressPane = new ProgressPane();
    progressPane.initializeAndExecuteWorker(report);

    Component owner = null;
    String title = "Uploading Bug Report";
    String text = "run in background";
    int result = JOptionPane.showOptionDialog(owner, progressPane, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[] {text}, text);
    switch (result) {
    case JOptionPane.OK_OPTION:
      System.out.println("background");
      break;
    case JOptionPane.CLOSED_OPTION:
      System.out.println("closed");
      break;
    }
    if (root == null) {
      return progressPane;
    }
    if (progressPane.isDone()) {
      if (progressPane.isSuccessful()) {
        JOptionPane.showMessageDialog(root, "Your bug report has been successfully submitted.  Thank you.");
        root.setVisible(false);
      } else {
        JOptionPane.showMessageDialog(root, "Your bug report FAILED to submit.  Thank you for trying.");
      }
    } else {
      root.setVisible(false);
    }
    return progressPane;
  }

  private static ProgressPane submitBugByMail(JIRAReport report, Component root) {
    Desktop desktop;
    ProgressPane progressPane = new ProgressPane();
    if (!Desktop.isDesktopSupported() || !(desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
      throw new RuntimeException("Unable to use mail service.");
    }
    try {
      String subject = EMAIL_SUBJECT.formatted(ProjectVersion.getCurrentVersionText(), report.getType(), report.getTruncatedSummary());
      String body = EMAIL_BODY.formatted(report.getDescription(), report.getSteps(), report.getException(), report.getEnvironment());
      subject = URLEncoder.encode(subject, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
      body = URLEncoder.encode(body, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
      URI mailto = new URI(EMAIL_URI.formatted(subject, body));
      Dialogs.showInfo("Add current project", "If your project is relevant please add it to the email.");
      desktop.mail(mailto);
      if (root != null) {
        root.setVisible(false);
      }
      progressPane.done(true);
    } catch (URISyntaxException | IOException e) {
      progressPane.done(false);
      throw new RuntimeException(e);
    }
    return progressPane;
  }
}
