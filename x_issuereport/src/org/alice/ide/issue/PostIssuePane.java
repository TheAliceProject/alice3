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
package org.alice.ide.issue;

/**
 * @author Dennis Cosgrove
 */
public class PostIssuePane extends edu.cmu.cs.dennisc.toolkit.issue.AbstractPostIssuePane {
	public PostIssuePane( edu.cmu.cs.dennisc.jira.JIRAReport.Type issueType ) {
		super( issueType );
		HeaderPane headerPane = new HeaderPane();
		this.add( headerPane, java.awt.BorderLayout.NORTH );
	}
	@Override
	protected edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration getReportSubmissionConfiguration() {
		return new ReportSubmissionConfiguration();
	}
	@Override
	protected String getJIRAPublicProjectKey() {
		return "AIII";
	}
	@Override
	protected String getJIRAPrivateProjectKey() {
		return "AIIIP";
	}
	
	@Override
	protected String[] getAffectsVersions() {
		return new String[] { "3.beta.0058" };
	}
	

//	@Override
//	protected java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > updateCriticalAttachments( java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > rv ) {
//		rv = super.updateCriticalAttachments( rv );
//		if( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( this, "Is your current project relevant to this issue and are you willing to attach your project with this report?", "Submit project?", javax.swing.JOptionPane.YES_NO_OPTION ) ) {
//			rv.add( new CurrentProjectAttachment() );
//		}
//		return rv;
//
//	}
//	@Override
//	protected edu.cmu.cs.dennisc.issue.Issue updateIssue( edu.cmu.cs.dennisc.issue.Issue rv ) {
//		rv.setAffectsVersions( edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() );
//		return super.updateIssue( rv );
//	}

	public static void main( String[] args ) {
		PostIssuePane pane = new PostIssuePane( edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG );
		javax.swing.JDialog window = edu.cmu.cs.dennisc.swing.JDialogUtilities.createPackedJDialog( pane, null, "Report Issue", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
//		pane.setWindow( window );
		window.getRootPane().setDefaultButton( pane.getSubmitButton() );
		window.setVisible( true );
	}

}
