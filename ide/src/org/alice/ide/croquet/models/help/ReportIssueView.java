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

import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.Label;

/**
 * @author Matt May
 */
public class ReportIssueView extends BorderPanel {

	public ReportIssueView( ReportIssueComposite reportIssueComposite ) {

		BorderPanel header = new BorderPanel();
		BorderPanel centerComponent = new BorderPanel();
		
		Label headerLabel = new Label();
		header.setBackgroundColor( Color.BLACK );
		headerLabel.setText( "Alice Bugs" );
		headerLabel.setFontSize( 36 );
		headerLabel.setForegroundColor( Color.YELLOW );
//		headerLabel.setBackgroundColor( Color.BLACK );
		header.addComponent( headerLabel, Constraint.CENTER );
		header.addComponent( reportIssueComposite.getLoginOperation().createButton(), Constraint.LINE_END );

		GridPanel topCenterComponent = GridPanel.createGridPane( 3, 1 );
		GridPanel centerCenterComponent = GridPanel.createGridPane( 3, 1 );
		GridPanel bottomCenterComponent = GridPanel.createGridPane( 1, 1 );

		BorderPanel visibilityRow = new BorderPanel();
		visibilityRow.addComponent( reportIssueComposite.getVisibilityLabel().createImmutableTextArea(), Constraint.LINE_START );
		visibilityRow.addComponent( reportIssueComposite.getVisibilityState().createCheckBox(), Constraint.CENTER );

		BorderPanel typeRow = new BorderPanel();
		typeRow.addComponent( reportIssueComposite.getTypeLabel().createImmutableTextArea(), Constraint.LINE_START );

		BorderPanel summaryRow = new BorderPanel();
		summaryRow.addComponent( reportIssueComposite.getSummaryLabel().createImmutableTextArea(), Constraint.LINE_START );
		summaryRow.addComponent( reportIssueComposite.getSummaryBlank().createTextField(), Constraint.CENTER );

		BorderPanel descriptionRow = new BorderPanel();
		descriptionRow.addComponent( reportIssueComposite.getDescriptionLabel().createImmutableTextArea(), Constraint.LINE_START );
		descriptionRow.addComponent( reportIssueComposite.getDescriptionBlank().createTextArea(), Constraint.CENTER );

		BorderPanel stepsRow = new BorderPanel();
		stepsRow.addComponent( reportIssueComposite.getStepsLabel().createImmutableTextArea(), Constraint.LINE_START );
		stepsRow.addComponent( reportIssueComposite.getStepsBlank().createTextArea(), Constraint.CENTER );

		BorderPanel environmentRow = new BorderPanel();
		environmentRow.addComponent( reportIssueComposite.getEnvironmentLabel().createImmutableTextArea(), Constraint.LINE_START );
		environmentRow.addComponent( reportIssueComposite.getEnvironmentBlank().createTextArea(), Constraint.CENTER );

		BorderPanel attachmentRow = new BorderPanel();
		attachmentRow.addComponent( reportIssueComposite.getAttachmentLabel().createImmutableTextArea(), Constraint.LINE_START );
		attachmentRow.addComponent( reportIssueComposite.getAttachmentState().createCheckBox(), Constraint.CENTER );

		topCenterComponent.addComponent( visibilityRow );
		topCenterComponent.addComponent( typeRow );
		topCenterComponent.addComponent( summaryRow );
		centerCenterComponent.addComponent( descriptionRow );
		centerCenterComponent.addComponent( stepsRow );
		centerCenterComponent.addComponent( environmentRow );
		bottomCenterComponent.addComponent( attachmentRow );

		centerComponent.addComponent( topCenterComponent, Constraint.PAGE_START );
		centerComponent.addComponent( centerCenterComponent, Constraint.CENTER );
		centerComponent.addComponent( bottomCenterComponent, Constraint.PAGE_END );

		this.addComponent( header, Constraint.PAGE_START );
		this.addComponent( centerComponent, Constraint.CENTER );
		this.addComponent( reportIssueComposite.getSubmitBugOperation().createButton(), Constraint.PAGE_END );
	}

}
