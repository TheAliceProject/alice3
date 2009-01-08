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
	public static void sendMail( String protocol, String host, String from, String to, String subject, String text, Attachment... attachments ) throws javax.mail.MessagingException {
		java.util.Properties props = new java.util.Properties();
		props.put( "mail." + protocol + ".host", host );
		javax.mail.Authenticator authenticator = new javax.mail.Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return super.getPasswordAuthentication();
			}
		};
		javax.mail.Session session = javax.mail.Session.getDefaultInstance( props, authenticator );
		javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage( session );
		message.setSubject( subject );
		javax.mail.Address fromAddress = new javax.mail.internet.InternetAddress( from );
		javax.mail.Address toAddress = new javax.mail.internet.InternetAddress( to );
		message.setFrom( fromAddress );
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
	public static void sendMail( String protocol, String host, String from, String to, String subject, String text, java.util.ArrayList< Attachment > attachments ) throws javax.mail.MessagingException {
		Attachment[] array = new Attachment[ attachments.size() ];
		attachments.toArray( array );
		sendMail( protocol, host, from, to, subject, text, array );
	}
}
