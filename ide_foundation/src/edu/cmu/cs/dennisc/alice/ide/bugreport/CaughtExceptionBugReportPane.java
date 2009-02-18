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
package edu.cmu.cs.dennisc.alice.ide.bugreport;

/**
 * @author Dennis Cosgrove
 */
public class CaughtExceptionBugReportPane extends edu.cmu.cs.dennisc.ui.bugreport.AbstractCaughtExceptionBugReportPane {
	private javax.swing.JCheckBox vcUploadProject = null;
	@Override
	protected java.util.ArrayList< java.awt.Component[] > addBugPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
		rv = super.addBugPaneRows( rv );
		assert this.vcUploadProject == null;
		this.vcUploadProject = new javax.swing.JCheckBox( "upload last-saved and current versions of project and interaction history" );
		this.vcUploadProject.setSelected( true );
		javax.swing.JLabel yourProjectLabel = new javax.swing.JLabel( "your project:", javax.swing.SwingConstants.TRAILING );
		rv.add( new java.awt.Component[] { yourProjectLabel, this.vcUploadProject } ); 
		return rv;
	}
	
	@Override
	protected java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > updateBonusAttachments( java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > rv ) {
		rv = super.updateBonusAttachments( rv );
//		rv.add( new edu.cmu.cs.dennisc.mail.Attachment() {
//			public javax.activation.DataSource getDataSource() {
//				//todo
//			}
//			public String getFileName() {
//				//todo
//			}
//		} );
		return rv;
	}

	@Override
	protected StringBuffer updateSubject( StringBuffer rv ) {
		rv.append( edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() );
		rv.append( " " );
		return super.updateSubject( rv );
	}
	@Override
	protected boolean isTransportLayerSecurityDesired() {
		return true;
	}
	@Override
	protected String getHost() {
		//return "smtp.gmail.com";
		return "haru.pc.cc.cmu.edu";
	}
	@Override
	protected String getTo() {
		return "alice.bugs.3.beta.xxxx@gmail.com";
	}
	@Override
	protected edu.cmu.cs.dennisc.mail.AbstractAuthenticator getAuthenticator() {
		return new edu.cmu.cs.dennisc.alice.ide.bugreport.mail.Authenticator();
	}
}
