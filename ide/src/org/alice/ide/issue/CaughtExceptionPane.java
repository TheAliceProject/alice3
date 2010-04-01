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
package org.alice.ide.issue;

/**
 * @author Dennis Cosgrove
 */
public class CaughtExceptionPane extends edu.cmu.cs.dennisc.toolkit.issue.AbstractCaughtExceptionPane {
	public CaughtExceptionPane() {
		StringBuffer sb = new StringBuffer();
		//sb.append( "\n" );
		sb.append( "An exception has been caught:\n\n" );
		sb.append( "  If you were running your program then:\n    it could be either a bug(error) in Alice or your code.\n\n" );
		sb.append( "  If you were building your program then:\n    it is a bug in Alice.\n\n" );
		sb.append( "Please press the \"submit bug report\" button." );
		
		javax.swing.JTextArea message = new javax.swing.JTextArea( sb.toString() ) {
//			@Override
//			public void paint( java.awt.Graphics g ) {
//				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//				g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
//				super.paint( g );
//			}
			@Override
			public java.awt.Dimension getMaximumSize() {
				return this.getPreferredSize();
			}
		};
		message.setEditable( false );
		message.setOpaque( false );
		message.setForeground( java.awt.Color.WHITE );
//		header.setLineWrap( false );
//		header.setWrapStyleWord( true );
		java.awt.Font font = message.getFont();
		font = font.deriveFont( (float)(int)( font.getSize() * 1.15f ) );
		font = font.deriveFont( java.awt.Font.BOLD );
		message.setFont( font );
		message.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 16 ) );
		
		
		javax.swing.ImageIcon icon = new javax.swing.ImageIcon( CaughtExceptionPane.class.getResource( "images/meanQueen.png" ) );
		javax.swing.JLabel meanQueen = new javax.swing.JLabel( icon );
		
		
//		message.setAlignmentY( 0.5f );
//		label.setAlignmentY( 0.5f );
		
		edu.cmu.cs.dennisc.croquet.swing.LineAxisPane pane = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane( meanQueen, javax.swing.Box.createHorizontalStrut( 16 ), message );
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

	private boolean isClearedToAttachCurrentProject = false;
	@Override
	protected boolean isClearedToSubmit() {
		boolean rv = super.isClearedToSubmit();
		if( rv ) {
			int option = javax.swing.JOptionPane.showConfirmDialog( this, "Submitting your current project would greatly help the Alice team in diagnosing and fixing this bug.\n\nThis bug report (and your project) will only be viewable by the Alice team.\n\nWould you like to submit your project with this bug report?", "Submit project?", javax.swing.JOptionPane.YES_NO_OPTION );
			this.isClearedToAttachCurrentProject = option == javax.swing.JOptionPane.YES_OPTION;
		}
		return rv;
	}
	@Override
	protected edu.cmu.cs.dennisc.issue.AbstractReport addAttachments( edu.cmu.cs.dennisc.issue.AbstractReport rv ) {
		rv = super.addAttachments( rv );
		if( this.isClearedToAttachCurrentProject ) {
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
