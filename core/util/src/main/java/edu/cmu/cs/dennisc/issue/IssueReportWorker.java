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

package edu.cmu.cs.dennisc.issue;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.jira.JIRAReport;
import edu.cmu.cs.dennisc.jira.rest.RestUtilities;
import net.rcarz.jiraclient.Issue;

import javax.swing.SwingWorker;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Dennis Cosgrove
 */
public final class IssueReportWorker extends SwingWorker<Boolean, String> {
  private final WorkerListener workerListener;
  private final ReportGenerator issueReportGenerator;
  private final ReportSubmissionConfiguration reportSubmissionConfiguration;
  private String key = null;

  public IssueReportWorker(WorkerListener workerListener, ReportGenerator issueReportGenerator, ReportSubmissionConfiguration reportSubmissionConfiguration) {
    assert workerListener != null;
    this.workerListener = workerListener;
    this.issueReportGenerator = issueReportGenerator;
    this.reportSubmissionConfiguration = reportSubmissionConfiguration;
  }

  @Override
  protected void process(List<String> chunks) {
    this.workerListener.process(chunks);
  }

  private void process(String... chunks) {
    this.process(Lists.newArrayList(chunks));
  }

  private void uploadToJiraViaRest() throws Exception {
    JIRAReport jiraReport = issueReportGenerator.generateIssue();
    if (jiraReport != null) {
      Issue issue = RestUtilities.createIssue(reportSubmissionConfiguration.getJIRAViaRestServer(), jiraReport);
      this.key = issue.getKey();
      List<Attachment> attachments = jiraReport.getAttachments();
      if (attachments != null && !attachments.isEmpty()) {
        this.process("\n");
        for (Attachment attachment : attachments) {
          this.process("\t" + attachment.getFileName() + "... ");
          issue.addAttachment(new File(attachment.getFileName()));
          this.process("done.\n");
        }
      }
    } else {
      throw new Exception("pass");
    }
  }

  @Override
  protected Boolean doInBackground() throws Exception {
    this.process("attempting to submit bug report...\n");

    this.process("* uploading directly to database via REST... ");
    try {
      uploadToJiraViaRest();
      this.process("SUCCEEDED.\n");
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      this.process("FAILED.\n");
      return false;
    }
  }

  @Override
  protected void done() {
    try {
      Boolean isSuccessful = this.get();
      if (isSuccessful != null) {
        this.workerListener.done(isSuccessful, null);
      } else {
        System.out.println("IssueReportWorker: isSuccessful is null.");
      }
    } catch (ExecutionException ee) {
      ee.printStackTrace();
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }
  }
}
