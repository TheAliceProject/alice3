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
public class CaughtExceptionPane extends edu.cmu.cs.dennisc.toolkit.issue.AbstractCaughtExceptionPane {
	public CaughtExceptionPane() {
		StringBuffer sb = new StringBuffer();
		sb.append( "An exception has been caught:\n\n" );
		sb.append( "  If you were running your program then:\n    it could be either a bug(error) in Alice or your code.\n\n" );
		sb.append( "  If you were building your program then:\n    it is a bug in Alice.\n\n" );
		sb.append( "Please press the \"submit bug report\" button." );
		javax.swing.JTextArea header = new javax.swing.JTextArea( sb.toString() ) {
			@Override
			public void paint( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
				super.paint( g );
			}
		};
		header.setEditable( false );
		header.setOpaque( false );
		header.setForeground( java.awt.Color.WHITE );
		header.setLineWrap( true );
		header.setWrapStyleWord( true );
		java.awt.Font font = header.getFont();
		font = font.deriveFont( font.getSize2D() * 1.15f );
		font = font.deriveFont( java.awt.Font.BOLD );
		header.setFont( font );
		header.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 16 ) );
		
		javax.swing.ImageIcon icon = new javax.swing.ImageIcon( CaughtExceptionPane.class.getResource( "images/meanQueen.png" ) );
		javax.swing.JLabel label = new javax.swing.JLabel( icon );
		swing.LineAxisPane pane = new swing.LineAxisPane( label, header );
		pane.setBackground( java.awt.Color.DARK_GRAY );
		pane.setOpaque( true );
		this.add( pane, java.awt.BorderLayout.NORTH );
	}
	@Override
	protected edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration getReportSubmissionConfiguration() {
		return new ReportSubmissionConfiguration();
	}
	@Override
	protected String getJIRAProjectKey() {
		return "AIIIP";
	}
	@Override
	protected String[] getAffectsVersions() {
		return new String[] { edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() };
	}

	@Override
	protected edu.cmu.cs.dennisc.issue.AbstractReport addAttachments( edu.cmu.cs.dennisc.issue.AbstractReport rv ) {
		rv = super.addAttachments( rv );
		if( javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( this, "Is your current project relevant to this issue and are you willing to attach your project with this report?", "Submit project?", javax.swing.JOptionPane.YES_NO_OPTION ) ) {
			rv.addAttachment( new CurrentProjectAttachment() );
		}
		return rv;
	}
	public static void main( String[] args ) {
		CaughtExceptionPane pane = new CaughtExceptionPane();
		
		try {
			throw new RuntimeException( "DELETE ME" );
		} catch( RuntimeException re ) {
			pane.setThreadAndThrowable( Thread.currentThread(), re );
		}
		
		javax.swing.JDialog window = edu.cmu.cs.dennisc.swing.JDialogUtilities.createPackedJDialog( pane, null, "Report Bug", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		window.getRootPane().setDefaultButton( pane.getSubmitButton() );
		window.setVisible( true );
	}

}
