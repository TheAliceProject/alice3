/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.issue.swing;

/**
 * @author Dennis Cosgrove
 */
public class JInsightPane extends javax.swing.JPanel {
	private static final String SUMMARY_SUGGESTIVE_TEXT = "please fill in a one line synopsis";
	private static final String DESCRIPTION_SUGGESTIVE_TEXT = "please fill in a detailed description";
	private static final String STEPS_SUGGESTIVE_TEXT = "please fill in the steps required to reproduce the bug";
	private static final String NAME_SUGGESTIVE_TEXT = "please fill in your name (optional)";
	private static final String EMAIL_SUGGESTIVE_TEXT = "please fill in your e-mail address (optional)";
	private final javax.swing.text.Document summaryDocument;
	private final javax.swing.text.Document descriptionDocument;
	private final javax.swing.text.Document stepsDocument;
	private final javax.swing.text.Document reportedByDocument;
	private final javax.swing.text.Document emailDocument;

	private boolean isExpanded;

	public JInsightPane() {
		javax.swing.JTextField summaryTextField = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField( "", SUMMARY_SUGGESTIVE_TEXT );
		javax.swing.JTextArea descriptionTextArea = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea( "", DESCRIPTION_SUGGESTIVE_TEXT );
		javax.swing.JTextArea stepsTextArea = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea( "", STEPS_SUGGESTIVE_TEXT );
		javax.swing.JTextField reportedByTextField = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField( "", NAME_SUGGESTIVE_TEXT );
		javax.swing.JTextField emailAddressTextField = new edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField( "", EMAIL_SUGGESTIVE_TEXT );

		this.summaryDocument = summaryTextField.getDocument();
		this.descriptionDocument = descriptionTextArea.getDocument();
		this.stepsDocument = stepsTextArea.getDocument();
		this.reportedByDocument = reportedByTextField.getDocument();
		this.emailDocument = emailAddressTextField.getDocument();

		javax.swing.border.Border border = summaryTextField.getBorder();
		descriptionTextArea.setBorder( border );
		stepsTextArea.setBorder( border );

		this.setLayout( new net.miginfocom.swing.MigLayout( "fill, insets 0", "[align right, grow 0][]", "4[grow 0][grow, shrink][grow, shrink][grow 0][grow 0][grow 0][grow 0]" ) );
		this.add( new javax.swing.JLabel( "summary:" ) );
		this.add( summaryTextField, "growx, wrap" );
		this.add( new javax.swing.JLabel( "description:" ) );
		this.add( descriptionTextArea, "grow, wrap" );
		this.add( new javax.swing.JLabel( "steps:" ) );
		this.add( stepsTextArea, "grow, wrap" );
		this.add( new javax.swing.JLabel( "exception:" ) );
		this.add( new JExceptionSubPane(), "wrap" );
		this.add( new javax.swing.JLabel( "environment:" ) );
		this.add( new JEnvironmentSubPane(), "wrap" );
		this.add( new javax.swing.JLabel( "reported by:" ) );
		this.add( reportedByTextField, "growx, wrap" );
		this.add( new javax.swing.JLabel( "e-mail address:" ) );
		this.add( emailAddressTextField, "growx, wrap" );
	}

	public boolean isExpanded() {
		return this.isExpanded;
	}

	public void setExpanded( boolean isExpanded ) {
		this.isExpanded = isExpanded;
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
}
