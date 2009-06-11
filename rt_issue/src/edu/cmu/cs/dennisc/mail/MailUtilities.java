/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
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
	public static void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride, String host, AbstractAuthenticator authenticator, String replyTo, String replyToPresonal, String to, String subject, String text, Attachment... attachments )
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

		for( Attachment attachment : attachments ) {
			javax.mail.BodyPart attachmentPart = new javax.mail.internet.MimeBodyPart();
			attachmentPart.setDataHandler( new javax.activation.DataHandler( attachment.getDataSource() ) );
			attachmentPart.setFileName( attachment.getFileName() );
			multipart.addBodyPart( attachmentPart );
		}
		message.setContent( multipart );
		javax.mail.Transport.send( message );
	}

	public static void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride, String host, AbstractAuthenticator authenticator, String replyTo, String replyToPresonal, String to, String subject, String text, java.util.List< Attachment > attachments ) throws javax.mail.MessagingException {
		Attachment[] array = new Attachment[ attachments.size() ];
		attachments.toArray( array );
		sendMail( isTransportLayerSecurityDesired, portOverride, host, authenticator, replyTo, replyToPresonal, to, subject, text, array );
	}
}
