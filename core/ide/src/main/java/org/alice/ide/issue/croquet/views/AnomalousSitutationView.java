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
package org.alice.ide.issue.croquet.views;

import edu.cmu.cs.dennisc.javax.swing.icons.ScaledIcon;
import org.alice.ide.croquet.models.help.views.AbstractIssueView;
import org.alice.ide.croquet.models.information.RestartRequiredOperation;
import org.alice.ide.issue.croquet.AnomalousSituationComposite;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.VerticalAlignment;

import javax.swing.Icon;

/**
 * @author Dennis Cosgrove
 */
public class AnomalousSitutationView extends AbstractIssueView {
  private static final Icon ICON = new ScaledIcon(RestartRequiredOperation.TWEEDLEDUM_AND_TWEEDLEDEE_ICON, 0.5f);

  public AnomalousSitutationView(AnomalousSituationComposite composite) {
    super(composite);
    Label iconLabel = new Label(ICON);
    iconLabel.setVerticalAlignment(VerticalAlignment.TOP);
    this.addLineStartComponent(iconLabel);

    Throwable throwable = composite.getThrowable();
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<body>");
    sb.append("<h2>");
    sb.append(throwable.getMessage());
    sb.append("</h2>");
    sb.append("<h3>");
    // TODO I18n
    sb.append("We are aware that this problem exists.  However, we are unable to reproduce it in the lab.");
    sb.append("</h3>");
    sb.append("<h3>");
    sb.append("If you can, please describe what you were doing when this bug was triggered.");
    sb.append("</h3>");
    sb.append("</body>");
    sb.append("<html>");

    MigPanel centerPanel = new MigPanel();
    centerPanel.addComponent(new Label(sb.toString()), "wrap");
    centerPanel.addComponent(createScrollPaneTextArea(composite.getStepsState()), "wrap, growx");
    centerPanel.addComponent(new LineAxisPanel(composite.getAreProjectAndImageAttachmentsDesired().createCheckBox(), BoxUtilities.createHorizontalSliver(8), composite.getShowApplicationContentPanelImageOperation().createHyperlink()), "wrap");
    centerPanel.addComponent(new Label("<html><h3>We apologize for the inconvenience and will try to fix this bug as soon as possible.</h3></html>"));

    this.addCenterComponent(centerPanel);
  }
}
