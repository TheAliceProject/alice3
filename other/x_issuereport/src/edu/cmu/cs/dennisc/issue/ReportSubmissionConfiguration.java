package edu.cmu.cs.dennisc.issue;

public interface ReportSubmissionConfiguration {
	public java.net.URL getJIRAViaRPCServer() throws java.net.MalformedURLException;
	public edu.cmu.cs.dennisc.jira.rpc.Authenticator getJIRAViaRPCAuthenticator();

	public java.net.URL getJIRAViaSOAPServer() throws java.net.MalformedURLException;
	public edu.cmu.cs.dennisc.jira.soap.Authenticator getJIRAViaSOAPAuthenticator();

	public String getMailServer();
	public edu.cmu.cs.dennisc.mail.AbstractAuthenticator getMailAuthenticator();
	public String getMailRecipient();
}
