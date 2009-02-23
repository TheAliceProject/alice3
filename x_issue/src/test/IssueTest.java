package test;

class MyCaughtExceptionPane extends edu.cmu.cs.dennisc.issue.AbstractCaughtExceptionPane {
	public static final String JIRA_URI  = "http://bugs.alice.org:8080";
	public static final String RPC_PATH  = "/rpc/xmlrpc";
	public static final String USER_NAME = "alice3_rpc";
	public static final String PASSWORD  = "brokenAnkles";

	
	private String getReplyTo( String reporterEMailAddress ) {
		String address = reporterEMailAddress;
		return address;
	}
	private String getReplyToPersonal( String reporterName ) {
		String address = reporterName;
		return address;
	}

	protected String getBody( edu.cmu.cs.dennisc.issue.Issue issue ) {
		return "detailed decription:\n" + issue.getDescription() + "\n\nsteps to reproduce:\n" + issue.getSteps();
	}

//	@Override
//	protected void uploadToJIRA( edu.cmu.cs.dennisc.issue.Issue issue ) throws Exception {
//		boolean streamMessages = true;
//	    redstone.xmlrpc.XmlRpcClient client = new redstone.xmlrpc.XmlRpcClient( JIRA_URI + RPC_PATH, streamMessages );
//	    Object token = client.invoke( "jira1.login", new Object[] { USER_NAME, PASSWORD } );
//	    try {
//		    redstone.xmlrpc.XmlRpcStruct serverInfo = (redstone.xmlrpc.XmlRpcStruct)client.invoke( "jira1.getServerInfo", new Object[] { token } );
//		    System.out.println( "serverInfo: " + serverInfo );
//		    java.util.List< redstone.xmlrpc.XmlRpcStruct > projects = (java.util.List< redstone.xmlrpc.XmlRpcStruct >)client.invoke( "jira1.getProjectsNoSchemes", new Object[] { token } );
//		    System.out.println( "projects: " );
//		    for( redstone.xmlrpc.XmlRpcStruct project : projects ) {
//			    System.out.println( "\t" + project );
//		    }
//
//		    redstone.xmlrpc.XmlRpcStruct remoteIssue = new redstone.xmlrpc.XmlRpcStruct();
//		    remoteIssue.put( "project", "AIIIP" );
//		    remoteIssue.put( "type", 1 );
//		    remoteIssue.put( "summary", issue.getSummary() );
//		    remoteIssue.put( "description", issue.getDescription() );
//		    remoteIssue.put( "eteps", issue.getSteps() );
//		    remoteIssue.put( "environment", issue.getEnvironment() );
//		    remoteIssue.put( "exception", issue.getException() );
//		    System.out.println( "issue: " + issue );
//			client.invoke( "jira1.createIssue", new Object[] { token, remoteIssue } );
//	    } finally {
//		    client.invoke( "jira1.logout", new Object[] { token } );
//	    }
//	}
//	@Override
//	protected void sendMail( edu.cmu.cs.dennisc.issue.Issue issue, String subject, String reporterEMailAddress, String reporterName ) throws Exception {
//		Exception lastAttemptResult = null;
////		boolean isTransportLayerSecurityDesired = true;
////		for( Integer portOverride : new Integer[] { null, 80 } ) { 
////			try {
////				StringBuffer sb = new StringBuffer();
////				if( isTransportLayerSecurityDesired ) {
////					sb.append( "SECURE: " );
////				} else {
////					sb.append( "unsecure: " );
////				}
////				if( portOverride != null ) {
////					sb.append( "port override: " );
////					sb.append( portOverride );
////				}
////				//sb.append( port );
////				subject = sb.toString() + " " + subject;
////
////				edu.cmu.cs.dennisc.mail.AbstractAuthenticator authenticator = new edu.cmu.cs.dennisc.mail.AbstractAuthenticator() {
////					@Override
////					protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
////						return new javax.mail.PasswordAuthentication( "anonymous-alice-user", "silkySmooth123" );
////					}
////					@Override
////					public String getAnonymousFrom() {
////						return "anonymous-alice-user@haru.pc.cc.cmu.edu";
////					}
////				};
////
////				edu.cmu.cs.dennisc.mail.MailUtilities.sendMail( isTransportLayerSecurityDesired, portOverride, "haru.pc.cc.cmu.edu", authenticator, "dennis.cosgrove@gmail.com", "Dennis Cosgrove", "gfy@andrew.cmu.edu", subject, "gabe rocks", new edu.cmu.cs.dennisc.mail.Attachment[] {} );
////				lastAttemptResult = null;
////				break;
////			} catch( Exception e ) {
////				e.printStackTrace();
////			}
////		}
//		if( lastAttemptResult != null ) {
//			throw lastAttemptResult;
//		}
//	}
}

public class IssueTest {

	public static void main( String[] args ) {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		String title = "title";
		javax.swing.JDialog window = new javax.swing.JDialog( frame, title, true );

		MyCaughtExceptionPane myCaughtExceptionPane = new MyCaughtExceptionPane();
		myCaughtExceptionPane.setThreadAndThrowable( Thread.currentThread(), new RuntimeException( "Four score and seven years ago our fathers brought forth on this continent, a new nation, conceived in Liberty, and dedicated to the proposition that all men are created equal." ) );

		myCaughtExceptionPane.setWindow( window );
		//javax.swing.JFrame window = new javax.swing.JFrame( "Please Submit Bug Report" );
		window.getContentPane().add( myCaughtExceptionPane );
		window.pack();
		window.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		window.getRootPane().setDefaultButton( myCaughtExceptionPane.getSubmitButton() );
		window.setVisible( true );
	}
}
