/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package org.alice.ide.issue.jira.rpc;

/**
 * @author Dennis Cosgrove
 */
public class Authenticator implements edu.cmu.cs.dennisc.jira.rpc.Authenticator {
	public Object login( redstone.xmlrpc.XmlRpcClient client ) throws redstone.xmlrpc.XmlRpcException, redstone.xmlrpc.XmlRpcFault {
		return client.invoke( "jira1.login", new Object[] { "alice3_rpc", "iNw6aFRhNia6SirM" } );
	}
}
