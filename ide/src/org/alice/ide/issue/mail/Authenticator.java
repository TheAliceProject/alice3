/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package org.alice.ide.issue.mail;

/**
 * @author Dennis Cosgrove
 */
public class Authenticator extends edu.cmu.cs.dennisc.mail.AbstractAuthenticator {
	@Override
	protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
		return new javax.mail.PasswordAuthentication( "anonymous-alice-user", "silkySmooth123" );
	}
	@Override
	public String getAnonymousFrom() {
		return "anonymous-alice-user@haru.pc.cc.cmu.edu";
	}
//	@Override
//	public String getAnonymousFrom() {
//		return "anonymous.alice.bugs.3.beta.xxxx@gmail.com";
//	}
//	@Override
//	protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//		String anonymousPassword = "pleaseDoNotStealThisAccount";
//		return new javax.mail.PasswordAuthentication( getAnonymousFrom(), anonymousPassword );
//	}
}
