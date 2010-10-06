package test;

public class JIRATest {
	public static final String JIRA_URI  = "http://bugs.alice.org:8080";
	public static final String RPC_PATH  = "/rpc/xmlrpc";
//	public static final String USER_NAME = "alice3_rpc";
//	public static final String PASSWORD  = "brokenAnkles";
	public static final String USER_NAME = "dennis";
	public static final String PASSWORD  = "shaqDrains2";

	public static void main( String[] args ) throws Exception {
		boolean streamMessages = true;
//		Jira jira = ( Jira ) redstone.xmlrpc.XmlRpcProxy.createProxy( new java.net.URL( JIRA_URI + RPC_PATH ), new Class[] { Jira.class }, streamMessages );
//	    String token = jira.login( USER_NAME, PASSWORD );
//	    jira.logout( token );

	    redstone.xmlrpc.XmlRpcClient client = new redstone.xmlrpc.XmlRpcClient( JIRA_URI + RPC_PATH, streamMessages );
	    Object token = client.invoke( "jira1.login", new Object[] { USER_NAME, PASSWORD } );
	    try {
		    System.out.println( "token: " + token );
	
		    redstone.xmlrpc.XmlRpcStruct serverInfo = (redstone.xmlrpc.XmlRpcStruct)client.invoke( "jira1.getServerInfo", new Object[] { token } );
		    System.out.println( "serverInfo: " + serverInfo );
		    java.util.List< redstone.xmlrpc.XmlRpcStruct > projects = (java.util.List< redstone.xmlrpc.XmlRpcStruct >)client.invoke( "jira1.getProjectsNoSchemes", new Object[] { token } );
		    System.out.println( "projects: " );
		    for( redstone.xmlrpc.XmlRpcStruct project : projects ) {
			    System.out.println( "\t" + project );
		    }

		    edu.cmu.cs.dennisc.issue.Issue issue = new edu.cmu.cs.dennisc.issue.Issue() {
		    	@Override
		    	public String getJIRASummary() {
		    		return this.getSummary();
		    	}
		    	@Override
		    	public String getMailSubject() {
		    		return this.getSummary();
		    	}
		    	@Override
		    	public String getMailBody() {
		    		return this.getDescription();
		    	}
		    };
		    issue.setType( edu.cmu.cs.dennisc.issue.Issue.Type.BUG );
		    issue.setSummary( new java.util.Date().toString() );
		    issue.setDescription( "the current date string" );
		    issue.setSteps( "1 2 3 4" );
		    issue.setThrowable( null );
		    
//		    redstone.xmlrpc.XmlRpcStruct remoteIn = JIRAUtilites.createIssue( issue );
//		    System.out.println( "\tin: " + remoteIn );
//		    redstone.xmlrpc.XmlRpcStruct remoteOut = (redstone.xmlrpc.XmlRpcStruct)client.invoke( "jira1.createIssue", new Object[] { token, remoteIn } );
//		    System.out.println( "\tout: " + remoteOut );

		    redstone.xmlrpc.XmlRpcStruct old = (redstone.xmlrpc.XmlRpcStruct)client.invoke( "jira1.getIssue", new Object[] { token, "AIIIP-35" } );
		    System.out.println( "\told:" );
		    for( Object key : old.keySet() ) {
			    System.out.println( "\t\t" + key + ": " + old.get( key ) );
		    }
		    
		    
	//	    java.util.Hashtable< String, String > issue = new java.util.Hashtable< String, String >();
	//	    issue.put( "summary", "gabe is the man" );
	//	    client.invoke( "jira1.createIssue", new Object[] { token, issue } );
	    } finally {
		    client.invoke( "jira1.logout", new Object[] { token } );
		    System.out.println( "done." );
	    }
	}
}
