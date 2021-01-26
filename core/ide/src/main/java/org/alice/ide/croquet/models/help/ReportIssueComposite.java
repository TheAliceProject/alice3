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

import edu.cmu.cs.dennisc.issue.IssueType;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult;
import org.alice.ide.browser.ImmutableBrowserOperation;
import org.alice.ide.croquet.models.help.views.ReportIssueView;
import org.alice.ide.issue.ReportSubmissionConfiguration;
import org.lgna.croquet.CardOwnerComposite;
import org.lgna.croquet.ImmutableDataSingleSelectListState;
import org.lgna.croquet.Initializer;
import org.lgna.croquet.Operation;
import org.lgna.croquet.StringState;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;

import java.util.UUID;

/**
 * @author Matt May
 */
public final class ReportIssueComposite extends AbstractIssueComposite<ReportIssueView> {
  private static class IssueTypeInitializer implements Initializer<ReportIssueComposite> {
    public IssueTypeInitializer(IssueType initialReportTypeValue) {
      this.initialReportTypeValue = initialReportTypeValue;
    }

    @Override
    public void initialize(ReportIssueComposite value) {
      value.reportTypeState.setValueTransactionlessly(this.initialReportTypeValue);
    }

    private final IssueType initialReportTypeValue;
  }

  public ReportIssueComposite() {
    super(UUID.fromString("96e23d44-c8b1-4da1-8d59-aea9f7ee7b42"), IsModal.FALSE);
    this.reportTypeState = createImmutableListStateForEnum("reportTypeState", IssueType.class, null);
    this.registerSubComposite(logInOutComposite);
    this.reportBugLaunchOperation = this.getImp().createAndRegisterLaunchOperation("reportBug", new IssueTypeInitializer(IssueType.BUG));
  }

  @Override
  protected String getDefaultTitleText() {
    //todo
    return null;
  }

  @Override
  protected Thread getThread() {
    return null;
  }

  @Override
  protected Throwable getThrowable() {
    return null;
  }

  @Override
  protected boolean isPublic() {
    return this.getVisibilityState().getValue().equals(BugSubmitVisibility.PUBLIC);
  }

  public ImmutableDataSingleSelectListState<BugSubmitVisibility> getVisibilityState() {
    return this.visibilityState;
  }

  @Override
  protected IssueType getReportType() {
    return this.reportTypeState.getValue();
  }

  public ImmutableDataSingleSelectListState<IssueType> getReportTypeState() {
    return this.reportTypeState;
  }

  @Override
  protected String getSummaryText() {
    return this.summaryState.getValue();
  }

  public StringState getSummaryState() {
    return this.summaryState;
  }

  @Override
  protected String getDescriptionText() {
    return this.descriptionState.getValue();
  }

  public StringState getDescriptionState() {
    return this.descriptionState;
  }

  public ImmutableDataSingleSelectListState<BugSubmitAttachment> getAttachmentState() {
    return this.attachmentState;
  }

  public CardOwnerComposite getLogInOutCardComposite() {
    return this.logInOutComposite;
  }

  @Override
  protected ReportIssueView createView() {
    return new ReportIssueView(this);
  }

  public Operation getBrowserOperation() {
    return this.browserOperation;
  }

  @Override
  protected boolean isProjectAttachmentDesired() {
    return this.attachmentState.getValue().equals(BugSubmitAttachment.YES);
  }

  @Override
  protected boolean isClearedToSubmitBug() {
    boolean rv;
    if (this.attachmentState.getValue() != null) {
      rv = true;
    } else {
      YesNoCancelResult result = Dialogs.confirmOrCancel("Attach current project?", "Is your current project relevant to this issue report?");
      if (result == YesNoCancelResult.YES) {
        this.attachmentState.setValueTransactionlessly(BugSubmitAttachment.YES);
        rv = true;
      } else if (result == YesNoCancelResult.NO) {
        this.attachmentState.setValueTransactionlessly(BugSubmitAttachment.NO);
        rv = true;
      } else {
        rv = false;
      }
    }
    return rv;
  }

  @Override
  public void handlePreActivation() {
    this.summaryState.setValueTransactionlessly("");
    this.descriptionState.setValueTransactionlessly("");
    this.visibilityState.setValueTransactionlessly(BugSubmitVisibility.PUBLIC);
    this.attachmentState.setValueTransactionlessly(null);

    this.summaryState.addAndInvokeNewSchoolValueListener(this.adapter);
    super.handlePreActivation();
  }

  @Override
  public void handlePostDeactivation() {
    super.handlePostDeactivation();
    this.summaryState.removeNewSchoolValueListener(this.adapter);
  }

  public Operation getReportBugLaunchOperation() {
    return this.reportBugLaunchOperation;
  }

  private final ImmutableDataSingleSelectListState<BugSubmitVisibility> visibilityState = createImmutableListStateForEnum("visibilityState", BugSubmitVisibility.class, BugSubmitVisibility.PRIVATE);

  private final ImmutableDataSingleSelectListState<IssueType> reportTypeState;
  private final StringState summaryState = createStringState("summaryState");
  private final StringState descriptionState = createStringState("descriptionState");
  private final ImmutableDataSingleSelectListState<BugSubmitAttachment> attachmentState = createImmutableListStateForEnum("attachmentState", BugSubmitAttachment.class, null);
  private final Operation browserOperation = new ImmutableBrowserOperation(UUID.fromString("55806b33-8b8a-43e0-ad5a-823d733be2f8"), ReportSubmissionConfiguration.JIRA_URL);
  private final LogInOutComposite logInOutComposite = new LogInOutComposite(UUID.fromString("079f108d-c3bb-4581-b107-f21b8d7286ca"), BugLoginComposite.getInstance());
  private final Operation reportBugLaunchOperation;

  private final ValueListener<String> adapter = new ValueListener<String>() {
    @Override
    public void valueChanged(ValueEvent<String> e) {
      getSubmitBugOperation().setEnabled(summaryState.getValue().length() > 0);
    }
  };
}
