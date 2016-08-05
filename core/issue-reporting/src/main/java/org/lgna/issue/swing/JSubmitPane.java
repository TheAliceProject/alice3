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
package org.lgna.issue.swing;

/**
 * @author Dennis Cosgrove
 */
public final class JSubmitPane extends javax.swing.JPanel {
	private static final String CONTRACTED_TEXT = "Provide details";
	private static final String EXPANDED_TEXT = "Please describe the problem and what steps you took that lead you to this bug:";

	public JSubmitPane( Thread thread, Throwable originalThrowable, Throwable originalThrowableOrTarget, org.lgna.issue.ApplicationIssueConfiguration config ) {
		this.config = config;
		this.insightPane = new JInsightPane( thread, originalThrowable, originalThrowableOrTarget );
		this.toggleButton.setMargin( new java.awt.Insets( 0, 0, 0, 0 ) );
		javax.swing.Action submitAction = new javax.swing.AbstractAction( config.getSubmitActionName() ) {
			@Override
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				getConfig().submit( JSubmitPane.this );
			}
		};
		this.submitButton = new javax.swing.JButton( submitAction );

		javax.swing.JPanel headerPane = config.createHeaderPane( thread, originalThrowable, originalThrowableOrTarget );

		javax.swing.JPanel insightHeaderPanel = new javax.swing.JPanel();
		insightHeaderPanel.setLayout( new java.awt.BorderLayout() );
		insightHeaderPanel.add( toggleButton, java.awt.BorderLayout.LINE_START );

		javax.swing.JPanel mainPanel = new javax.swing.JPanel();
		mainPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		mainPanel.setLayout( new java.awt.BorderLayout() );
		mainPanel.add( insightHeaderPanel, java.awt.BorderLayout.PAGE_START );
		mainPanel.add( insightPane, java.awt.BorderLayout.CENTER );

		javax.swing.JPanel submitPanel = new javax.swing.JPanel();
		submitPanel.setLayout( new java.awt.FlowLayout() );
		submitPanel.add( submitButton );

		this.setLayout( new java.awt.BorderLayout() );
		this.add( headerPane, java.awt.BorderLayout.PAGE_START );
		this.add( mainPanel, java.awt.BorderLayout.CENTER );
		this.add( submitPanel, java.awt.BorderLayout.PAGE_END );

		this.toggleButton.addChangeListener( this.changeListener );

		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( submitButton, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToScaledFont( submitButton, 1.6f );

		this.toggleButton.setIcon( new edu.cmu.cs.dennisc.javax.swing.icons.AbstractArrowIcon( 12 ) {
			@Override
			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
				javax.swing.ButtonModel buttonModel = button.getModel();
				Heading heading = buttonModel.isSelected() ? Heading.SOUTH : Heading.EAST;
				java.awt.Shape shape = this.createPath( x, y, heading );
				g2.setPaint( java.awt.Color.DARK_GRAY );
				g2.fill( shape );
			}
		} );
		this.toggleButton.setIconTextGap( 12 );
		this.toggleButton.setHorizontalTextPosition( javax.swing.SwingConstants.LEADING );
		this.toggleButton.setFocusable( false );
		final boolean IS_INSIGHT_EXPANDED_BY_DEFAULT = false;
		if( IS_INSIGHT_EXPANDED_BY_DEFAULT ) {
			this.toggleButton.setSelected( IS_INSIGHT_EXPANDED_BY_DEFAULT );
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.getRootPane().setDefaultButton( this.submitButton );
	}

	public org.lgna.issue.ApplicationIssueConfiguration getConfig() {
		return this.config;
	}

	public edu.cmu.cs.dennisc.issue.Issue.Builder createIssueBuilder() {
		return this.insightPane.createIssueBuilder();
	}

	public boolean isSubmitAttempted() {
		return this.isSubmitAttempted;
	}

	public void setSubmitAttempted( boolean isSubmitAttempted ) {
		this.isSubmitAttempted = isSubmitAttempted;
	}

	private final JInsightPane insightPane;

	private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		@Override
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			Object src = e.getSource();
			if( src instanceof javax.swing.JToggleButton ) {
				javax.swing.JToggleButton button = (javax.swing.JToggleButton)src;
				insightPane.setExpanded( button.isSelected() );
				toggleButton.setText( button.isSelected() ? EXPANDED_TEXT : CONTRACTED_TEXT );
				( (java.awt.Window)javax.swing.SwingUtilities.getRoot( button ) ).pack();
			}
		}
	};
	private final javax.swing.JToggleButton toggleButton = new javax.swing.JToggleButton( CONTRACTED_TEXT );
	private final javax.swing.JButton submitButton;

	private final org.lgna.issue.ApplicationIssueConfiguration config;

	private boolean isSubmitAttempted;
}
