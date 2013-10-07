/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package org.alice.ide.issue.jira.soap;

/**
 * @author Dennis Cosgrove
 */
public class Authenticator implements edu.cmu.cs.dennisc.jira.soap.Authenticator {
	public String login( com.atlassian.jira.rpc.soap.client.JiraSoapService service ) throws java.rmi.RemoteException {
		return service.login( "alice3_soap", "BW@U~1.455g45u" );
	}
}
