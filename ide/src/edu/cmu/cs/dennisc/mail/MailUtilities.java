/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package edu.cmu.cs.dennisc.mail;

/**
 * @author Dennis Cosgrove
 */
public class MailUtilities {
	private static void setPersonal( javax.mail.internet.InternetAddress address, String personal ) {
		try {
			address.setPersonal( personal );
		} catch( java.io.UnsupportedEncodingException uee ) {
			uee.printStackTrace();
		}
	}
	public static void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride, String host, AbstractAuthenticator authenticator, String replyTo, String replyToPresonal, String to, String subject, String text, edu.cmu.cs.dennisc.issue.Attachment... attachments )
			throws javax.mail.MessagingException {
		java.util.Properties props = new java.util.Properties();
		props.put( "mail.transport.protocol", "smtp" );
		props.put( "mail.smtp.host", host );
		String tlsValue;
		
		int port;
		if( portOverride != null ) {
			port = portOverride;
		} else {
			if( isTransportLayerSecurityDesired ) {
				port = 465;
			} else {
				port = 25;
			}
		}
		props.put( "mail.smtp.port", Integer.toString( port ) );
		if( isTransportLayerSecurityDesired ) {
			props.put( "mail.smtp.auth", "true" );
			props.put( "mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory" );
			props.put( "mail.smtp.socketFactory.fallback", "false" );
//			props.put( "mail.smtp.ssl.protocols", "SSLv3 TLSv1" );
			tlsValue = "true";
		} else {
			tlsValue = "false";
		}
		

		props.put( "mail.smtp.starttls.enable", tlsValue );

		String authValue;
		if( authenticator != null ) {
			authValue = "true";
		} else {
			authValue = "false";
		}
		props.put( "mail.smtp.auth", authValue );
		props.put( "mail.smtp.quitwait", "false" );

		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "sendMail", props );
		javax.mail.Session session = javax.mail.Session.getInstance( props, authenticator );
		//session.setDebug( true );

		javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage( session );
		message.setSubject( subject );

		javax.mail.internet.InternetAddress fromAddress;
		if( authenticator != null ) {
			fromAddress = new javax.mail.internet.InternetAddress( authenticator.getAnonymousFrom() );
		} else {
			fromAddress = new javax.mail.internet.InternetAddress( replyTo );
		}
		if( replyToPresonal != null && replyToPresonal.length() > 0 ) {
			setPersonal( fromAddress, replyToPresonal );
		} else {
			if( replyTo != null && replyTo.length() > 0 ) {
				setPersonal( fromAddress, replyTo );
			}
		}
		message.setFrom( fromAddress );
		if( replyTo != null && replyTo.length() > 0 ) {
			javax.mail.internet.InternetAddress replyToAddress = new javax.mail.internet.InternetAddress( replyTo );
			if( replyToPresonal != null && replyToPresonal.length() > 0 ) {
				setPersonal( replyToAddress, replyToPresonal );
			}
			message.setReplyTo( new javax.mail.Address[] { replyToAddress } );
		}

		javax.mail.Address toAddress = new javax.mail.internet.InternetAddress( to );
		message.addRecipient( javax.mail.Message.RecipientType.TO, toAddress );

		javax.mail.BodyPart messagePart = new javax.mail.internet.MimeBodyPart();
		messagePart.setText( text );

		javax.mail.Multipart multipart = new javax.mail.internet.MimeMultipart();
		multipart.addBodyPart( messagePart );

		for( edu.cmu.cs.dennisc.issue.Attachment attachment : attachments ) {
			javax.mail.BodyPart attachmentPart = new javax.mail.internet.MimeBodyPart();
			javax.activation.DataSource dataSource = new javax.mail.util.ByteArrayDataSource( attachment.getBytes(), attachment.getMIMEType() );
			attachmentPart.setDataHandler( new javax.activation.DataHandler( dataSource ) );
			attachmentPart.setFileName( attachment.getFileName() );
			multipart.addBodyPart( attachmentPart );
		}
		message.setContent( multipart );
		javax.mail.Transport.send( message );
	}

	public static void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride, String host, AbstractAuthenticator authenticator, String replyTo, String replyToPresonal, String to, String subject, String text, java.util.List< edu.cmu.cs.dennisc.issue.Attachment > attachments ) throws javax.mail.MessagingException {
		edu.cmu.cs.dennisc.issue.Attachment[] array = new edu.cmu.cs.dennisc.issue.Attachment[ attachments.size() ];
		attachments.toArray( array );
		sendMail( isTransportLayerSecurityDesired, portOverride, host, authenticator, replyTo, replyToPresonal, to, subject, text, array );
	}
}
