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

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class Issue {
  public static class Builder {
    public Builder type(IssueType type) {
      this.type = type;
      return this;
    }

    public Builder summary(String summary) {
      this.summary = summary;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder steps(String steps) {
      this.steps = steps;
      return this;
    }

    public Builder environment(String environment) {
      this.environment = environment;
      return this;
    }

    public Builder threadAndThrowable(Thread thread, Throwable throwable) {
      this.thread = thread;
      this.throwable = throwable;
      return this;
    }

    public Builder version(String version) {
      this.version = version;
      return this;
    }

    public Builder reportedBy(String reportedBy) {
      this.reportedBy = reportedBy;
      return this;
    }

    public Builder emailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
      return this;
    }

    public Builder addAttachment(Attachment attachment) {
      this.attachments.add(attachment);
      return this;
    }

    public Issue build() {
      return new Issue(this);
    }

    private IssueType type;
    private String summary;
    private String description;
    private String steps;
    private String environment;
    private Thread thread;
    private Throwable throwable;
    private String version;
    private String reportedBy;
    private String emailAddress;

    private List<Attachment> attachments = Lists.newLinkedList();
  }

  private Issue(Builder builder) {
    this.type = builder.type;
    this.summary = builder.summary;
    this.description = builder.description;
    this.steps = builder.steps;
    this.environment = builder.environment;
    this.thread = builder.thread;
    this.throwable = builder.throwable;
    this.version = builder.version;
    this.reportedBy = builder.reportedBy;
    this.emailAddress = builder.emailAddress;
    this.attachments = ArrayUtilities.createArray(builder.attachments, Attachment.class);
  }

  public IssueType getType() {
    return this.type;
  }

  public String getSummary() {
    return this.summary;
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

  public Thread getThread() {
    return this.thread;
  }

  public Throwable getThrowable() {
    return this.throwable;
  }

  public String getVersion() {
    return this.version;
  }

  public String getReportedBy() {
    return this.reportedBy;
  }

  public String getEmailAddress() {
    return this.emailAddress;
  }

  public Attachment[] getAttachments() {
    return this.attachments;
  }

  private final IssueType type;
  private final String summary;
  private final String description;
  private final String steps;
  private final String environment;
  private final Thread thread;
  private final Throwable throwable;
  private final String version;
  private final String reportedBy;
  private final String emailAddress;
  private final Attachment[] attachments;
}
