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

/**
 * @author Dennis Cosgrove
 */
public class JIRAReport extends edu.cmu.cs.dennisc.issue.AbstractReport {
	private final String projectKey;
	private final edu.cmu.cs.dennisc.issue.IssueType type;
	private final String summary;
	private final String description;
	private final String steps;
	private final String environment;
	private final String exception;
	private final String[] affectsVersions;
	private final String reportedBy;
	private final String emailAddress;

	public JIRAReport( edu.cmu.cs.dennisc.issue.Issue issue, String projectKey ) {
		this.projectKey = projectKey;
		this.type = issue.getType();
		this.summary = issue.getSummary();
		this.description = issue.getDescription();
		this.steps = issue.getSteps();
		this.environment = issue.getEnvironment();
		this.exception = edu.cmu.cs.dennisc.issue.IssueUtilities.getThrowableText( issue.getThrowable() );
		String versionText = issue.getVersion();
		if( versionText != null ) {
			this.affectsVersions = new String[] { versionText };
		} else {
			this.affectsVersions = new String[] {};
		}
		this.reportedBy = issue.getReportedBy();
		this.emailAddress = issue.getEmailAddress();
	}

	public String getProjectKey() {
		return this.projectKey;
	}

	public edu.cmu.cs.dennisc.issue.IssueType getType() {
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
		String rv;
		if( ( this.affectsVersions != null ) && ( this.affectsVersions.length > 0 ) ) {
			rv = this.affectsVersions[ 0 ];
		} else {
			rv = null;
		}
		if( rv != null ) {
			//pass
		} else {
			rv = "";
		}
		return rv;
	}
}
