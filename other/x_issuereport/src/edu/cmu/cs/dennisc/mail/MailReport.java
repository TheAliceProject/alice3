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
public class MailReport extends edu.cmu.cs.dennisc.issue.AbstractReport {
	private String replyTo;
	private String replyToPersonal;
	private String subject;
	private String body;
	private java.util.List< edu.cmu.cs.dennisc.issue.Attachment > attachments = new java.util.LinkedList< edu.cmu.cs.dennisc.issue.Attachment >();

	public String getReplyTo() {
		return this.replyTo;
	}
	public void setReplyTo( String replyTo ) {
		this.replyTo = replyTo;
	}
	public String getReplyToPersonal() {
		return this.replyToPersonal;
	}
	public void setReplyToPersonal( String replyToPersonal ) {
		this.replyToPersonal = replyToPersonal;
	}
	public String getSubject() {
		return this.subject;
	}
	public void setSubject( String subject ) {
		this.subject = subject;
	}
	public String getBody() {
		return this.body;
	}
	public void setBody( String body ) {
		this.body = body;
	}
}
