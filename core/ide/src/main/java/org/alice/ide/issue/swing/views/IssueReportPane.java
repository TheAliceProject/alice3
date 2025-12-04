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

import edu.cmu.cs.dennisc.issue.*;
import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.lang.ThrowableUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.javax.swing.SpringUtilities;
import edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea;
import edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField;
import edu.cmu.cs.dennisc.jira.JIRAReport;
import org.alice.ide.issue.SubmitReportUtilities;
import org.alice.ide.issue.swing.SubmitReportAction;
import org.lgna.project.ProjectVersion;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class IssueReportPane extends JPanel {
  private static final List<String> systemPropertiesForEnvironmentField = Collections.unmodifiableList(Lists.newArrayList("java.version", "os.name", "os.arch"));

  public static List<String> getSystemPropertiesForEnvironmentField() {
    return systemPropertiesForEnvironmentField;
  }

  public static String getEnvironmentLongDescription() {
    StringBuilder sb = new StringBuilder();
    String intersticial = "";
    for (String propertyName : systemPropertiesForEnvironmentField) {
      sb.append(intersticial);
      sb.append(propertyName);
      sb.append(": ");
      sb.append(System.getProperty(propertyName));
      intersticial = "\n";
    }
    return sb.toString();
  }

  public static String getEnvironmentShortDescription() {
    StringBuilder sb = new StringBuilder();
    String intersticial = "";
    for (String propertyName : systemPropertiesForEnvironmentField) {
      sb.append(intersticial);
      sb.append(System.getProperty(propertyName));
      intersticial = ";";
    }
    if (SystemUtilities.isMac()) {
      sb.append(";");
      sb.append(System.getProperty("os.version"));
    }
    return sb.toString();
  }

  private class SubmitAction extends SubmitReportAction {
    @Override
    public void actionPerformed(ActionEvent e) {
      IssueReportPane.this.submit();
    }
  }

  private final Action submitAction = new SubmitAction();

  private final JButton submitButton = new JButton(submitAction);

  protected abstract int getPreferredDescriptionHeight();

  protected abstract int getPreferredStepsHeight();

  private static final String SUMMARY_SUGGESTIVE_TEXT = "please fill in a one line synopsis";
  private static final String DESCRIPTION_SUGGESTIVE_TEXT = "please fill in a detailed description";
  private static final String STEPS_SUGGESTIVE_TEXT = "please fill in the steps required to reproduce the bug";

  protected Action getSubmitAction() {
    return this.submitAction;
  }

  protected abstract boolean isSummaryRequired();

  private String getSummarySuggestiveText() {
    String rv = SUMMARY_SUGGESTIVE_TEXT;
    if (this.isSummaryRequired()) {
      rv = rv + " (Required)";
    }
    return rv;
  }

  protected static JLabel createLabelForSingleLine(String text) {
    return new JLabel(text, SwingConstants.TRAILING);
  }

  protected static JLabel createLabelForMultiLine(String text) {
    JLabel rv = createLabelForSingleLine(text);
    rv.setVerticalAlignment(SwingConstants.TOP);
    return rv;
  }

  private final JLabel labelSummary = createLabelForSingleLine("summary:");
  private final JSuggestiveTextField textSummary = new JSuggestiveTextField("", this.getSummarySuggestiveText());
  protected Component[] rowSummary = SpringUtilities.createRow(labelSummary, textSummary);

  private final JLabel labelDescription = createLabelForMultiLine("description:");
  private final JSuggestiveTextArea textDescription = new JSuggestiveTextArea("", DESCRIPTION_SUGGESTIVE_TEXT);
  private final JScrollPane scrollDescription = new JScrollPane(this.textDescription, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
    @Override
    public Dimension getPreferredSize() {
      return DimensionUtilities.constrainToMinimumHeight(super.getPreferredSize(), IssueReportPane.this.getPreferredDescriptionHeight());
    }
  };
  protected Component[] rowDescription = SpringUtilities.createRow(labelDescription, scrollDescription);

  private final JLabel labelSteps = createLabelForMultiLine("steps:");
  private final JSuggestiveTextArea textSteps = new JSuggestiveTextArea("", STEPS_SUGGESTIVE_TEXT);
  private final JScrollPane scrollSteps = new JScrollPane(this.textSteps, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
    @Override
    public Dimension getPreferredSize() {
      return DimensionUtilities.constrainToMinimumHeight(super.getPreferredSize(), IssueReportPane.this.getPreferredStepsHeight());
    }
  };
  protected Component[] rowSteps = SpringUtilities.createRow(labelSteps, scrollSteps);

  public IssueReportPane() {
    Font font = this.submitButton.getFont();
    this.submitButton.setFont(font.deriveFont(font.getSize2D() * 1.5f));
    this.submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.labelSummary.setToolTipText(textSummary.getTextForBlankCondition());
    this.labelDescription.setToolTipText(textDescription.getTextForBlankCondition());
    this.labelSteps.setToolTipText(textSteps.getTextForBlankCondition());

    this.scrollDescription.setBorder(BorderFactory.createEmptyBorder());
    this.scrollSteps.setBorder(BorderFactory.createEmptyBorder());

    this.textDescription.setLineWrap(true);
    this.textDescription.setWrapStyleWord(true);
    this.textSteps.setLineWrap(true);
    this.textSteps.setWrapStyleWord(true);

    this.setLayout(new BorderLayout());
    JPanel southPane = new JPanel();
    southPane.add(this.getSubmitButton());
    this.add(southPane, BorderLayout.SOUTH);
  }

  protected abstract String getJIRAProjectKey();

  protected abstract IssueType getIssueType();

  protected String getSummaryText() {
    return this.textSummary.getText();
  }

  protected String getDescriptionText() {
    return this.textDescription.getText();
  }

  protected abstract String getEnvironmentText();

  protected String getStepsText() {
    return this.textSteps.getText();
  }

  protected abstract Thread getThread();

  protected abstract Throwable getThrowable();

  protected abstract String[] getAffectsVersions();

  private String getExceptionText() {
    Throwable throwable = this.getThrowable();
    if (throwable != null) {
      return ThrowableUtilities.getStackTraceAsString(throwable);
    } else {
      return "";
    }
  }

  protected abstract String getSMTPReplyTo();

  protected abstract String getSMTPReplyToPersonal();

  protected abstract boolean isInclusionOfCompleteSystemPropertiesDesired();

  protected AbstractReport addAttachments(AbstractReport rv) {
    Throwable throwable = this.getThrowable();
    if (throwable != null) {
      rv.addAttachment(new StackTraceAttachment(throwable));
    }
    if (this.isInclusionOfCompleteSystemPropertiesDesired()) {
      rv.addAttachment(new SystemPropertiesAttachment());
    }
    return rv;
  }

  private Issue.Builder createIssueBuilder() {
    return new Issue.Builder()
        .type(this.getIssueType())
        .summary(this.getSummaryText())
        .description(this.getDescriptionText())
        .environment(this.getEnvironmentText())
        .steps(this.getStepsText())
        .threadAndThrowable(this.getThread(), this.getThrowable())
        .version(ProjectVersion.getCurrentVersionText())
        .reportedBy(this.getSMTPReplyToPersonal())
        .emailAddress(this.getSMTPReplyTo());
  }

  public JIRAReport generateIssue() {
    JIRAReport report = new JIRAReport(this.createIssueBuilder().build(), this.getJIRAProjectKey());
    this.addAttachments(report);
    return report;
  }

  public JButton getSubmitButton() {
    return this.submitButton;
  }

  private boolean isSubmitAttempted = false;
  private boolean isSubmitSuccessful = false;
  private boolean isSubmitDone = false;

  public boolean isSubmitAttempted() {
    return this.isSubmitAttempted;
  }

  public boolean isSubmitDone() {
    return this.isSubmitDone;
  }

  public boolean isSubmitSuccessful() {
    return this.isSubmitSuccessful;
  }

  protected void submit() {
    this.isSubmitSuccessful = false;
    this.isSubmitDone = false;
    this.isSubmitAttempted = true;
    ProgressPane progressPane = SubmitReportUtilities.submitReport(generateIssue(), SwingUtilities.getRoot(this));
    this.isSubmitSuccessful = progressPane.isSuccessful();
    this.isSubmitDone = progressPane.isDone();
    Component root = SwingUtilities.getRoot(this);
    if (root != null) {
      root.setVisible(false);
    }
  }
}
