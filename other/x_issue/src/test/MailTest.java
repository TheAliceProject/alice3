package test;

public class MailTest {
	public static void main( String[] args ) {
		//java.security.Security.addProvider( new com.sun.net.ssl.internal.ssl.Provider() );
		edu.cmu.cs.dennisc.mail.AbstractAuthenticator authenticator = new edu.cmu.cs.dennisc.mail.AbstractAuthenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication( "anonymous-alice-user", "silkySmooth123" );
			}
			@Override
			public java.lang.String getAnonymousFrom() {
				return "anonymous-alice-user@haru.pc.cc.cmu.edu";
			}
		};
		boolean isTransportLayerSecurityDesired = true;
		//isTransportLayerSecurityDesired = false;
		//Integer portOverride = null;
		//portOverride = 25;
		//portOverride = 80;
		//portOverride = 465;
		
		
		for( Integer portOverride : new Integer[] { null, 80 } ) { 
			try {
				StringBuffer sb = new StringBuffer();
				if( isTransportLayerSecurityDesired ) {
					sb.append( "SECURE: " );
				} else {
					sb.append( "unsecure: " );
				}
				if( portOverride != null ) {
					sb.append( "port override: " );
					sb.append( portOverride );
				}
				//sb.append( port );
				String subject = sb.toString();

				edu.cmu.cs.dennisc.mail.MailUtilities.sendMail( isTransportLayerSecurityDesired, portOverride, "haru.pc.cc.cmu.edu", authenticator, "dennisc@cs.cmu.edu", "Dennis Cosgrove", "dennis.cosgrove@gmail.com", subject, "gabe rocks", new edu.cmu.cs.dennisc.mail.Attachment[] {} );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "gabe does indeed rock" );
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		System.exit( -1 );
	}
}
