package test;

class ProgressPane extends javax.swing.JPanel {
	private javax.swing.JTextPane console = new javax.swing.JTextPane();
	private org.jdesktop.swingworker.SwingWorker< Boolean, String > swingWorker = new org.jdesktop.swingworker.SwingWorker< Boolean, String >() {
		@Override
		protected void process( java.util.List< String > chunks ) {
			for( String chunk : chunks ) {
				javax.swing.text.Document document = ProgressPane.this.console.getDocument();
				try {
					document.insertString( document.getLength(), chunk, null );
				} catch (javax.swing.text.BadLocationException ble ) {
					throw new RuntimeException( ble );
				}
				System.out.print( chunk );
			}
		}
		private void process( String... chunks ) {
			this.process( edu.cmu.cs.dennisc.util.CollectionUtilities.createArrayList( chunks ) );
		}


		public static final String JIRA_URI  = "http://bugs.alice.org:8080";
		public static final String RPC_PATH  = "/rpc/xmlrpc";
		public static final String USER_NAME = "alice3_rpc";
		public static final String PASSWORD  = "brokenAnkles";

		public void uploadToJIRA() throws Exception {
			boolean streamMessages = true;
//			Jira jira = ( Jira ) redstone.xmlrpc.XmlRpcProxy.createProxy( new java.net.URL( JIRA_URI + RPC_PATH ), new Class[] { Jira.class }, streamMessages );
//		    String token = jira.login( USER_NAME, PASSWORD );
//		    jira.logout( token );

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
//				    for( String key : new String[] { "key", "name", "projectUrl" } ) {
//					    issue.put( key, project.getString( key ) );
//				    }
				    //issue.put( "project", project.getString( "key" ) );
				    
			    }
			    redstone.xmlrpc.XmlRpcStruct issue = new redstone.xmlrpc.XmlRpcStruct();
			    issue.put( "project", "AIIIP" );
			    issue.put( "type", 1 );
			    issue.put( "summary", new java.util.Date().toString() );
			    issue.put( "description", "the current date string" );
			    System.out.println( "\t" + issue );
				client.invoke( "jira1.createIssue", new Object[] { token, issue } );
		//	    java.util.Hashtable< String, String > issue = new java.util.Hashtable< String, String >();
		//	    issue.put( "summary", "gabe is the man" );
		//	    client.invoke( "jira1.createIssue", new Object[] { token, issue } );
		    } finally {
			    client.invoke( "jira1.logout", new Object[] { token } );
			    System.out.println( "done." );
		    }
//			edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
//			throw new Exception();
		}
		
		public void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride ) throws Exception {
//			edu.cmu.cs.dennisc.mail.AbstractAuthenticator authenticator = new edu.cmu.cs.dennisc.mail.AbstractAuthenticator() {
//				@Override
//				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//					return new javax.mail.PasswordAuthentication( "anonymous-alice-user", "silkySmooth123" );
//				}
//				@Override
//				public java.lang.String getAnonymousFrom() {
//					return "anonymous-alice-user@haru.pc.cc.cmu.edu";
//				}
//			};
//			StringBuffer sb = new StringBuffer();
//			if( isTransportLayerSecurityDesired ) {
//				sb.append( "SECURE: " );
//			} else {
//				sb.append( "unsecure: " );
//			}
//			if( portOverride != null ) {
//				sb.append( "port override: " );
//				sb.append( portOverride );
//			}
//			String subject = sb.toString();
//
//			edu.cmu.cs.dennisc.mail.MailUtilities.sendMail( isTransportLayerSecurityDesired, portOverride, "haru.pc.cc.cmu.edu", authenticator, "dennis.cosgrove@gmail.com", "Dennis Cosgrove", "gfy@andrew.cmu.edu", subject, "gabe rocks", new edu.cmu.cs.dennisc.mail.Attachment[] {} );
			edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
			throw new Exception();
		}
		
		@Override
		protected Boolean doInBackground() throws Exception {

			this.process( "Attempting to upload bug report to JIRA server... " );
			try {
				this.uploadToJIRA();
				this.process( "SUCCEEDED.\n" );
				return true;
			} catch( Exception e ) {
				this.process( "FAILED.\n" );
			}
			
			this.process( "Attempting to send mail bug report (on smtp port)... " );
			try {
				this.sendMail( false, null );
				this.process( "SUCCEEDED.\n" );
				return true;
			} catch( Exception e ) {
				this.process( "FAILED.\n" );
			}
			
			this.process( "Attempting to send secure mail bug report (on secure smtp port)... " );
			try {
				this.sendMail( true, null );
				this.process( "SUCCEEDED.\n" );
				return true;
			} catch( Exception e ) {
				this.process( "FAILED.\n" );
			}
			
			
			this.process( "Attempting to send secure mail bug report (on http port)... " );
			try {
				this.sendMail( true, 80 );
				this.process( "SUCCEEDED.\n" );
				return true;
			} catch( Exception e ) {
				this.process( "FAILED.\n" );
			}

			return false;
		}
		@Override
		protected void done() {
			try {
				Boolean isSuccessful = this.get();
			} catch( Exception e ) {
				throw new RuntimeException( e );
			}
		}
	};
	public ProgressPane() {
		this.console.setPreferredSize( new java.awt.Dimension( 640, 480 ) );
		this.swingWorker.execute();
		
		this.setLayout( new java.awt.BorderLayout() );
		this.add( new javax.swing.JScrollPane( this.console ), java.awt.BorderLayout.CENTER );
	}
	
}

public class ProgressTest extends javax.swing.JFrame {
	
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( new ProgressPane() );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.pack();
		frame.setVisible( true );
		
//		final javax.swing.ProgressMonitor progressMonitor = new javax.swing.ProgressMonitor( frame, "message", "note", 0, 4 );
//		progressMonitor.setProgress( 0 );
		
//		org.jdesktop.swingworker.SwingWorker< Boolean, String > swingWorker = new org.jdesktop.swingworker.SwingWorker< Boolean, String >() {
//			@Override
//			protected void process( java.util.List< String > chunks ) {
//				for( String chunk : chunks ) {
//					System.out.print( chunk );
//				}
//			}
//			private void process( String... chunks ) {
//				this.process( edu.cmu.cs.dennisc.util.CollectionUtilities.createArrayList( chunks ) );
//			}
//			@Override
//			protected Boolean doInBackground() throws Exception {
//				this.process( "jira... " );
//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
//				this.process( "FAILED.\n" );
//				progressMonitor.setProgress( 1 );
//				this.process( "mail (on smtp port)... " );
//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
//				this.process( "FAILED.\n" );
//				progressMonitor.setProgress( 2 );
//				this.process( "mail (on secure smtp port)... " );
//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
//				this.process( "FAILED.\n" );
//				progressMonitor.setProgress( 3 );
//				this.process( "mail (on http port)... " );
//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
//				this.process( "FAILED.\n" );
//				progressMonitor.setProgress( 4 );
//				return false;
//			}
//			@Override
//			protected void done() {
//				try {
//					Boolean isSuccessful = this.get();
//				} catch( Exception e ) {
//					throw new RuntimeException( e );
//				}
//			}
//		};
//		swingWorker.execute();
	}
}
