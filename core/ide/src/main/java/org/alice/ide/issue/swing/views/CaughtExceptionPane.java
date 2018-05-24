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

import edu.cmu.cs.dennisc.issue.AbstractReport;
import edu.cmu.cs.dennisc.java.awt.font.FontUtilities;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import edu.cmu.cs.dennisc.javax.swing.JDialogUtilities;
import edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane;
import edu.cmu.cs.dennisc.javax.swing.components.JMigPane;
import edu.cmu.cs.dennisc.javax.swing.components.JPageAxisPane;
import edu.cmu.cs.dennisc.javax.swing.plaf.HyperlinkUI;
import org.alice.ide.issue.CurrentProjectAttachment;
import org.alice.ide.issue.ReportSubmissionConfiguration;
import org.alice.ide.issue.UserProgramRunningStateUtilities;
import org.alice.ide.issue.swing.CheckForNewAliceVersionAction;
import org.lgna.project.ProjectVersion;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public class CaughtExceptionPane extends AbstractCaughtExceptionPane {
	private static final String APPLICATION_NAME = "Alice";
	private static final float FONT_SCALE = 1.15f;
	private static final Icon MEAN_QUEEN_ICON;

	static {
		Icon icon = null;
		try {
			icon = IconUtilities.createImageIcon( CaughtExceptionPane.class.getResource( "images/meanQueen.png" ) );
		} catch( Throwable t ) {
			t.printStackTrace();
		}
		MEAN_QUEEN_ICON = icon;
	}

	private static JLabel createLabel( String text ) {
		JLabel rv = new JLabel( text );
		rv.setForeground( Color.WHITE );
		FontUtilities.setFontToScaledFont( rv, FONT_SCALE );
		return rv;
	}

	public CaughtExceptionPane() {
		StringBuffer sb = new StringBuffer();
		sb.append( "<html>" );
		sb.append( "An exception has been caught" );

		if( UserProgramRunningStateUtilities.isUserProgramRunning() ) {
			sb.append( " during the running of your program.<p>" );
			sb.append( "<p>While this <em>could</em> be the result of a problem in your code,<br>it is likely a bug in " );
			sb.append( APPLICATION_NAME );
			sb.append( ".<p>" );
		} else {
			sb.append( ".<p>" );
		}
		sb.append( "<p>Please accept our apologies and press the \"" );
		sb.append( this.getSubmitAction().getValue( Action.NAME ) );
		sb.append( "\" button.<p>" );
		sb.append( "<p>We will do our best to fix the problem and make a new release.<p>" );
		sb.append( "</html>" );

		JLabel message = createLabel( sb.toString() );

		JButton hyperlink = new JButton( new CheckForNewAliceVersionAction() ) {
			@Override
			public void updateUI() {
				HyperlinkUI hyperlinkUi = new HyperlinkUI();
				this.setUI( hyperlinkUi );
			}
		};
		FontUtilities.setFontToScaledFont( hyperlink, FONT_SCALE );
		hyperlink.setForeground( Color.WHITE );
		hyperlink.setOpaque( false );
		hyperlink.setBorder( BorderFactory.createEmptyBorder() );

		JPageAxisPane bottomPane = new JPageAxisPane(
				createLabel( "Note: it is possible this bug has already been fixed." ),
				new JLineAxisPane(
						createLabel( "Check:  " ),
						hyperlink,
						createLabel( "  for the latest release." )
				)
				);

		JMigPane pane = new JMigPane( "insets 16 0 0 32", "[]16[]", "[top][bottom]" );
		pane.add( new JLabel( MEAN_QUEEN_ICON ), "span 1 2" );
		pane.add( message, "wrap, growy" );
		pane.add( bottomPane, "wrap" );
		pane.setBackground( Color.DARK_GRAY );
		pane.setOpaque( true );

		this.add( pane, BorderLayout.NORTH );
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
		return new String[] { ProjectVersion.getCurrentVersionText() };
	}

	private boolean isClearedToAttachCurrentProject = false;

	@Override
	protected void submit() {
		int option = JOptionPane.showConfirmDialog( this, "Submitting your current project would greatly help the " + APPLICATION_NAME + " team in diagnosing and fixing this bug.\n\nThis bug report (and your project) will only be viewable by the " + APPLICATION_NAME + " team.\n\nWould you like to submit your project with this bug report?", "Submit project?", JOptionPane.YES_NO_CANCEL_OPTION );
		if( option == JOptionPane.CANCEL_OPTION ) {
			//pass
		} else {
			this.isClearedToAttachCurrentProject = option == JOptionPane.YES_OPTION;
			super.submit();
		}
	}

	@Override
	protected AbstractReport addAttachments( AbstractReport rv ) {
		rv = super.addAttachments( rv );
		if( this.isClearedToAttachCurrentProject ) {
			rv.addAttachment( new CurrentProjectAttachment() );
		}
		return rv;
	}

	public static void main( String[] args ) {
		UserProgramRunningStateUtilities.setUserProgramRunning( true );

		CaughtExceptionPane pane = new CaughtExceptionPane();

		try {
			throw new RuntimeException( "DELETE ME" );
		} catch( RuntimeException re ) {
			pane.setThreadAndThrowable( Thread.currentThread(), re );
		}

		JDialog window = JDialogUtilities.createPackedJDialog( pane, null, "Report Bug", true, WindowConstants.DISPOSE_ON_CLOSE );
		window.getRootPane().setDefaultButton( pane.getSubmitButton() );
		window.setVisible( true );
	}

}
