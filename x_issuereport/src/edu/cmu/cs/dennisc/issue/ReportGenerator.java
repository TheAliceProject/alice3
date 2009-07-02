package edu.cmu.cs.dennisc.issue;

import edu.cmu.cs.dennisc.jira.JIRAReport;
import edu.cmu.cs.dennisc.mail.MailReport;

public interface ReportGenerator {
	public JIRAReport generateIssueForSOAP();
	public JIRAReport generateIssueForRPC();
	public MailReport generateIssueForSMTP();
}
