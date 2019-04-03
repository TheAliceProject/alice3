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

package edu.cmu.cs.dennisc.jira;

import edu.cmu.cs.dennisc.issue.AbstractReport;
import edu.cmu.cs.dennisc.issue.Issue;
import edu.cmu.cs.dennisc.issue.IssueType;
import edu.cmu.cs.dennisc.issue.IssueUtilities;

/**
 * @author Dennis Cosgrove
 */
public class JIRAReport extends AbstractReport {
  private static final int SUMMARY_MAX = 254;

  private final String projectKey;
  private final IssueType type;
  private final String summary;
  private final String description;
  private final String steps;
  private final String environment;
  private final String exception;
  private final String[] affectsVersions;
  private final String reportedBy;
  private final String emailAddress;

  public JIRAReport(Issue issue, String projectKey) {
    this.projectKey = projectKey;
    this.type = issue.getType();
    this.summary = issue.getSummary();
    this.description = issue.getDescription();
    this.steps = issue.getSteps();
    this.environment = issue.getEnvironment();
    this.exception = IssueUtilities.getThrowableText(issue.getThrowable());
    String versionText = issue.getVersion();
    if (versionText != null) {
      this.affectsVersions = new String[] {versionText};
    } else {
      this.affectsVersions = new String[] {};
    }
    this.reportedBy = issue.getReportedBy();
    this.emailAddress = issue.getEmailAddress();
  }

  public String getProjectKey() {
    return this.projectKey;
  }

  public IssueType getType() {
    return this.type;
  }

  public int getTypeID() {
    switch (type) {
    case BUG:
      return 1;
    case NEW_FEATURE:
      return 2;
    case IMPROVEMENT:
      return 4;
    default:
      throw new RuntimeException();
    }
  }

  public String getTruncatedSummary() {
    return summary.length() < SUMMARY_MAX ? summary : summary.substring(0, SUMMARY_MAX);
  }

  public String getDescription() {
    return this.description;
  }

  public String getSteps() {
    return this.steps;
  }

  public String getEnvironment() {
    return this.environment;
  }

  public String[] getAffectsVersions() {
    return this.affectsVersions;
  }

  public String getException() {
    return this.exception;
  }

  public String getReportedBy() {
    return this.reportedBy;
  }

  public String getEmailAddress() {
    return this.emailAddress;
  }

  public String getAffectsVersionText() {
    if ((this.affectsVersions != null) && (this.affectsVersions.length > 0)) {
      return affectsVersions[0];
    } else {
      return "";
    }
  }

  public String getCreditedDescription() {
    StringBuilder sb = new StringBuilder();
    sb.append(getDescription());
    sb.append("\n\n");
    if ((reportedBy != null) && (reportedBy.length() > 0)) {
      sb.append("\nReported by: ");
      sb.append(reportedBy);
    }
    if ((emailAddress != null) && (emailAddress.length() > 0)) {
      sb.append("\nEmail address: ");
      sb.append(emailAddress);
    }
    sb.append("\nAffects version: ");
    sb.append(getAffectsVersionText());
    return sb.toString();
  }
}
