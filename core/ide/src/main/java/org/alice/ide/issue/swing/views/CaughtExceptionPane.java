/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.issue.swing.views;

import org.alice.ide.issue.CurrentProjectAttachment;
import org.alice.ide.issue.ReportSubmissionConfiguration;

/**
 * @author Dennis Cosgrove
 */
public class CaughtExceptionPane extends org.alice.ide.issue.swing.views.AbstractCaughtExceptionPane {
	private static final String APPLICATION_NAME = "Alice";
	private static final float FONT_SCALE = 1.15f;
	private static final javax.swing.Icon MEAN_QUEEN_ICON;

	static {
		javax.swing.Icon icon = null;
		try {
			icon = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( CaughtExceptionPane.class.getResource( "images/meanQueen.png" ) );
		} catch( Throwable t ) {
			t.printStackTrace();
		}
		MEAN_QUEEN_ICON = icon;
	}

	private static javax.swing.JLabel createLabel( String text ) {
		javax.swing.JLabel rv = new javax.swing.JLabel( text );
		rv.setForeground( java.awt.Color.WHITE );
		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToScaledFont( rv, FONT_SCALE );
		return rv;
	}

	public CaughtExceptionPane() {
		StringBuffer sb = new StringBuffer();
		sb.append( "<html>" );
		sb.append( "An exception has been caught" );

		if( org.alice.ide.issue.UserProgramRunningStateUtilities.isUserProgramRunning() ) {
			sb.append( " during the running of your program.<p>" );
			sb.append( "<p>While this <em>could</em> be the result of a problem in your code,<br>it is likely a bug in " );
			sb.append( APPLICATION_NAME );
			sb.append( ".<p>" );
		} else {
			sb.append( ".<p>" );
		}
		sb.append( "<p>Please accept our apologies and press the \"" );
		sb.append( this.getSubmitAction().getValue( javax.swing.Action.NAME ) );
		sb.append( "\" button.<p>" );
		sb.append( "<p>We will do our best to fix the problem and make a new release.<p>" );
		sb.append( "</html>" );

		javax.swing.JLabel message = createLabel( sb.toString() );

		javax.swing.JButton hyperlink = new javax.swing.JButton( new org.alice.ide.issue.swing.CheckForNewAliceVersionAction() ) {
			@Override
			public void updateUI() {
				edu.cmu.cs.dennisc.javax.swing.plaf.HyperlinkUI hyperlinkUi = new edu.cmu.cs.dennisc.javax.swing.plaf.HyperlinkUI();
				this.setUI( hyperlinkUi );
			}
		};
		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToScaledFont( hyperlink, FONT_SCALE );
		hyperlink.setForeground( java.awt.Color.WHITE );
		hyperlink.setOpaque( false );
		hyperlink.setBorder( javax.swing.BorderFactory.createEmptyBorder() );

		edu.cmu.cs.dennisc.javax.swing.components.JPageAxisPane bottomPane = new edu.cmu.cs.dennisc.javax.swing.components.JPageAxisPane(
				createLabel( "Note: it is possible this bug has already been fixed." ),
				new edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane(
						createLabel( "Check:  " ),
						hyperlink,
						createLabel( "  for the latest release." )
				)
				);

		edu.cmu.cs.dennisc.javax.swing.components.JMigPane pane = new edu.cmu.cs.dennisc.javax.swing.components.JMigPane( "insets 16 0 0 32", "[]16[]", "[top][bottom]" );
		pane.add( new javax.swing.JLabel( MEAN_QUEEN_ICON ), "span 1 2" );
		pane.add( message, "wrap, growy" );
		pane.add( bottomPane, "wrap" );
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
		return new String[] { org.lgna.project.ProjectVersion.getCurrentVersionText() };
	}

	private boolean isClearedToAttachCurrentProject = false;

	@Override
	protected void submit() {
		int option = javax.swing.JOptionPane.showConfirmDialog( this, "Submitting your current project would greatly help the " + APPLICATION_NAME + " team in diagnosing and fixing this bug.\n\nThis bug report (and your project) will only be viewable by the " + APPLICATION_NAME + " team.\n\nWould you like to submit your project with this bug report?", "Submit project?", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION );
		if( option == javax.swing.JOptionPane.CANCEL_OPTION ) {
			//pass
		} else {
			this.isClearedToAttachCurrentProject = option == javax.swing.JOptionPane.YES_OPTION;
			super.submit();
		}
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
		org.alice.ide.issue.UserProgramRunningStateUtilities.setUserProgramRunning( true );

		CaughtExceptionPane pane = new CaughtExceptionPane();

		try {
			throw new RuntimeException( "DELETE ME" );
		} catch( RuntimeException re ) {
			pane.setThreadAndThrowable( Thread.currentThread(), re );
		}

		javax.swing.JDialog window = edu.cmu.cs.dennisc.javax.swing.JDialogUtilities.createPackedJDialog( pane, null, "Report Bug", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		window.getRootPane().setDefaultButton( pane.getSubmitButton() );
		window.setVisible( true );
	}

}
