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
public class JInsightPane extends javax.swing.JPanel {
	//private static final String SUMMARY_SUGGESTIVE_TEXT = "please fill in a one line synopsis";
	private static final String DESCRIPTION_SUGGESTIVE_TEXT = "please fill in a detailed description";
	private static final String STEPS_SUGGESTIVE_TEXT = "please fill in the steps required to reproduce the bug";
	private static final String NAME_SUGGESTIVE_TEXT = "(optional)";//"please fill in your name (optional)";
	private static final String EMAIL_SUGGESTIVE_TEXT = "(optional)";//"please fill in your e-mail address (optional)";

	private static javax.swing.text.JTextComponent createTextArea( String text, String suggestiveText ) {
		javax.swing.JTextArea rv = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea( text, suggestiveText );
		rv.setLineWrap( true );
		rv.setWrapStyleWord( true );
		return rv;
	}

	private static javax.swing.JScrollPane createScrollPane( javax.swing.text.JTextComponent view ) {
		javax.swing.JScrollPane rv = new javax.swing.JScrollPane( view, javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ) {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), 100 );
			}
		};
		return rv;
	}

	public JInsightPane( Thread thread, Throwable originalThrowable, Throwable originalThrowableOrTarget ) {
		//javax.swing.JTextField summaryTextField = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField( "", SUMMARY_SUGGESTIVE_TEXT );
		this.exceptionSubPane = new JExceptionSubPane( thread, originalThrowable, originalThrowableOrTarget );
		this.descriptionTextArea = createTextArea( "", DESCRIPTION_SUGGESTIVE_TEXT );
		this.stepsTextArea = createTextArea( "", STEPS_SUGGESTIVE_TEXT );
		this.reportedByTextField = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField( "", NAME_SUGGESTIVE_TEXT );
		this.emailAddressTextField = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField( "", EMAIL_SUGGESTIVE_TEXT );

		String textAreaConstraint = "aligny top, gaptop 8";
		String panelConstraint = "aligny top";
		this.setLayout( new net.miginfocom.swing.MigLayout( "fill, insets 0", "[align right, grow 0][]", "4[grow, shrink][grow, shrink][grow 0][grow 0][grow 0][grow 0][grow 0]" ) );
		//this.add( new javax.swing.JLabel( "summary:" ) );
		//this.add( summaryTextField, "growx, wrap" );
		this.add( new javax.swing.JLabel( "description:" ), textAreaConstraint );
		this.add( createScrollPane( descriptionTextArea ), "grow, wrap" );
		this.add( new javax.swing.JLabel( "steps:" ), textAreaConstraint );
		this.add( createScrollPane( stepsTextArea ), "grow, wrap" );
		this.add( new javax.swing.JLabel( "reported by:" ) );
		this.add( reportedByTextField, "growx, wrap" );
		this.add( new javax.swing.JLabel( "e-mail address:" ) );
		this.add( emailAddressTextField, "growx, wrap" );
		this.add( new javax.swing.JSeparator( javax.swing.SwingConstants.HORIZONTAL ), "growx, span 2, wrap" );
		this.add( new javax.swing.JLabel( "exception:" ), panelConstraint );
		this.add( this.exceptionSubPane, "wrap" );
		this.add( new javax.swing.JLabel( "environment:" ), panelConstraint );
		this.add( this.environmentSubPane, "wrap" );
	}

	public edu.cmu.cs.dennisc.issue.Issue.Builder createIssueBuilder() {
		edu.cmu.cs.dennisc.issue.Issue.Builder rv = new edu.cmu.cs.dennisc.issue.Issue.Builder();
		rv.type( edu.cmu.cs.dennisc.issue.IssueType.BUG );
		rv.description( this.descriptionTextArea.getText() );
		rv.steps( this.stepsTextArea.getText() );
		rv.reportedBy( this.reportedByTextField.getText() );
		rv.emailAddress( this.emailAddressTextField.getText() );
		rv.threadAndThrowable( this.exceptionSubPane.getThread(), this.exceptionSubPane.getOriginalThrowable() );
		return rv;
	}

	public boolean isExpanded() {
		return this.isExpanded;
	}

	public void setExpanded( boolean isExpanded ) {
		this.isExpanded = isExpanded;
		this.descriptionTextArea.requestFocus();
		this.revalidate();
		this.repaint();
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		if( isExpanded ) {
			return super.getPreferredSize();
		} else {
			return new java.awt.Dimension( 0, 0 );
		}
	}

	private final javax.swing.text.JTextComponent descriptionTextArea;
	private final javax.swing.text.JTextComponent stepsTextArea;
	private final javax.swing.text.JTextComponent reportedByTextField;
	private final javax.swing.text.JTextComponent emailAddressTextField;

	private final JExceptionSubPane exceptionSubPane;
	private final JEnvironmentSubPane environmentSubPane = new JEnvironmentSubPane( edu.cmu.cs.dennisc.issue.IssueUtilities.getSystemPropertiesForEnvironmentField() );
	private boolean isExpanded;
}
