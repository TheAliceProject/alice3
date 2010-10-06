package test;

class MyUploadWorker extends edu.cmu.cs.dennisc.issue.UploadWorker {
	private static final String USER_NAME = "alice3_rpc";
	private static final String PASSWORD = "brokenAnkles";

	public MyUploadWorker( edu.cmu.cs.dennisc.issue.ProgressPane progressPane ) {
		super( progressPane );
	}
	private java.net.URL getJIRAServer() {
		final String RPC_PATH = "/rpc/xmlrpc";
		try {
			return new java.net.URL( "http://bugs.alice.org:8080" + RPC_PATH );
		} catch( java.net.MalformedURLException murle ) {
			throw new RuntimeException( murle );
		}
	}
	@Override
	protected void uploadToJIRA() throws Exception {
		//		final boolean STREAM_MESSAGES = true;
		//	    redstone.xmlrpc.XmlRpcClient client = new redstone.xmlrpc.XmlRpcClient( this.getJIRAServer(), STREAM_MESSAGES );
		//	    Object token = client.invoke( "jira1.login", new Object[] { USER_NAME, PASSWORD } );
		//	    try {
		//		    System.out.println( "token: " + token );
		//
		//		    redstone.xmlrpc.XmlRpcStruct serverInfo = (redstone.xmlrpc.XmlRpcStruct)client.invoke( "jira1.getServerInfo", new Object[] { token } );
		//		    System.out.println( "serverInfo: " + serverInfo );
		//		    java.util.List< redstone.xmlrpc.XmlRpcStruct > projects = (java.util.List< redstone.xmlrpc.XmlRpcStruct >)client.invoke( "jira1.getProjectsNoSchemes", new Object[] { token } );
		//		    System.out.println( "projects: " );
		//		    for( redstone.xmlrpc.XmlRpcStruct project : projects ) {
		//			    System.out.println( "\t" + project );
		//		    }
		//		    redstone.xmlrpc.XmlRpcStruct issue = new redstone.xmlrpc.XmlRpcStruct();
		//		    issue.put( "project", "AIIIP" );
		//		    issue.put( "type", 1 );
		//		    issue.put( "summary", new java.util.Date().toString() );
		//		    issue.put( "description", "the current date string" );
		//		    System.out.println( "\t" + issue );
		//			client.invoke( "jira1.createIssue", new Object[] { token, issue } );
		//	    } finally {
		//		    client.invoke( "jira1.logout", new Object[] { token } );
		//		    System.out.println( "done." );
		//	    }
		edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
		throw new Exception();
	}

	@Override
	protected void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride ) throws Exception {
		//		edu.cmu.cs.dennisc.mail.AbstractAuthenticator authenticator = new edu.cmu.cs.dennisc.mail.AbstractAuthenticator() {
		//			@Override
		//			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
		//				return new javax.mail.PasswordAuthentication( "anonymous-alice-user", "silkySmooth123" );
		//			}
		//			@Override
		//			public java.lang.String getAnonymousFrom() {
		//				return "anonymous-alice-user@haru.pc.cc.cmu.edu";
		//			}
		//		};
		//		StringBuffer sb = new StringBuffer();
		//		if( isTransportLayerSecurityDesired ) {
		//			sb.append( "SECURE: " );
		//		} else {
		//			sb.append( "unsecure: " );
		//		}
		//		if( portOverride != null ) {
		//			sb.append( "port override: " );
		//			sb.append( portOverride );
		//		}
		//		String subject = sb.toString();
		//
		//		edu.cmu.cs.dennisc.mail.MailUtilities.sendMail( isTransportLayerSecurityDesired, portOverride, "haru.pc.cc.cmu.edu", authenticator, "dennis.cosgrove@gmail.com", "Dennis Cosgrove", "gfy@andrew.cmu.edu", subject, "gabe rocks", new edu.cmu.cs.dennisc.mail.Attachment[] {} );
		edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
		throw new Exception();
	}
}
