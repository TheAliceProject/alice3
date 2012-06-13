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
package org.alice.ide.croquet.models.help;

import java.awt.Color;
import java.util.List;

import javax.swing.Icon;

import org.alice.ide.issue.HeaderPane;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.Hyperlink;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.LabeledSpringRow;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.PageAxisPanel;
import org.lgna.croquet.components.FlowPanel;
import org.lgna.croquet.components.RowSpringPanel;
import org.lgna.croquet.components.SpringRow;
import org.lgna.croquet.components.VerticalAlignment;

import edu.cmu.cs.dennisc.javax.swing.IconUtilities;

/**
 * @author Matt May
 */
public class ReportIssueView extends BorderPanel {
	private final static Icon headerIcon = IconUtilities.createImageIcon( HeaderPane.class.getResource( "images/logo.png" ) );
	public ReportIssueView( final ReportIssueComposite reportIssueComposite ) {
		BorderPanel header = new BorderPanel();
		RowSpringPanel centerComponent = new RowSpringPanel() {
			@Override
			protected void appendRows( List<SpringRow> rows ) {
				rows.add( new LabeledSpringRow( reportIssueComposite.getVisibilityLabel(), reportIssueComposite.getVisibilityList().createHorizontalDefaultRadioButtons() ) );
				rows.add( new LabeledSpringRow( reportIssueComposite.getTypeLabel(), reportIssueComposite.getTypeList().getPrepModel().createComboBox(), VerticalAlignment.CENTER, false ) );
				rows.add( new LabeledSpringRow( reportIssueComposite.getSummaryLabel(), reportIssueComposite.getSummaryBlank().createTextField() ) );
				rows.add( new LabeledSpringRow( reportIssueComposite.getDescriptionLabel(), reportIssueComposite.getDescriptionBlank().createTextArea(), VerticalAlignment.TOP ) );
				rows.add( new LabeledSpringRow( reportIssueComposite.getStepsLabel(), reportIssueComposite.getStepsBlank().createTextArea(), VerticalAlignment.TOP ) );
				rows.add( new LabeledSpringRow( reportIssueComposite.getEnvironmentLabel(), reportIssueComposite.getEnvironmentBlank().createTextArea(), VerticalAlignment.TOP ) );
				rows.add( new LabeledSpringRow( reportIssueComposite.getAttachmentLabel(), reportIssueComposite.getAttachmentList().createVerticalDefaultRadioButtons(), VerticalAlignment.TOP ) );
			}
		};

		Color backgroundColor = Color.DARK_GRAY;
		Label headerLabel = new Label();
		headerLabel.setIcon( headerIcon );
		headerLabel.setAlignmentX( 0.5f );
		Hyperlink link = reportIssueComposite.getBrowserOperation().createHyperlink();
		link.setForegroundColor( Color.LIGHT_GRAY );
		link.getAwtComponent().setBackground( backgroundColor );
		link.setAlignmentX( 0.5f );
		
		PageAxisPanel lineStartPanel = new PageAxisPanel( headerLabel, link );
				
		LineAxisPanel rightOfHeaderPanel = new LineAxisPanel( reportIssueComposite.getLoginOperation().createButton() );

		header.addComponent( lineStartPanel, Constraint.LINE_START );
		header.addComponent( rightOfHeaderPanel, Constraint.LINE_END );
		header.setBackgroundColor( backgroundColor );
		
		header.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
		centerComponent.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
		
		org.lgna.croquet.components.Button submitButton = reportIssueComposite.getSubmitBugOperation().createButton();
		submitButton.scaleFont( 1.6f );
		submitButton.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		
		FlowPanel submitPanel = new FlowPanel( FlowPanel.Alignment.CENTER );
		submitPanel.addComponent( submitButton );

		PageAxisPanel pageEndPanel = new PageAxisPanel( new org.lgna.croquet.components.HorizontalSeparator(), org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 ), submitPanel );
		pageEndPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		
		this.addComponent( header, Constraint.PAGE_START );
		this.addComponent( centerComponent, Constraint.CENTER );
		this.addComponent( pageEndPanel, Constraint.PAGE_END );
	}

}
