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

package org.alice.stageide.about.views;

import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import org.alice.stageide.about.AboutComposite;
import org.alice.stageide.about.MainSiteBrowserOperation;
import org.lgna.croquet.Operation;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.FormPanel;
import org.lgna.croquet.views.HtmlMultiLineLabel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LabeledFormRow;
import org.lgna.croquet.views.PageAxisPanel;
import org.lgna.project.ProjectVersion;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.AbstractBorder;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class AboutView extends BorderPanel {
  private static final ImageIcon ICON = IconUtilities.createImageIcon(AboutView.class.getResource("images/about.png"));

  private static class IconBorder extends AbstractBorder {
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
      insets.top = 200;
      insets.left = 150;
      insets.bottom = 16;
      insets.right = 16;
      return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      g.drawImage(ICON.getImage(), x, y, c);
    }
  }

  public AboutView(final AboutComposite composite) {
    super(composite);

    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append(getLocalizedStringByKey("aboutSupport"));
    sb.append("<br><ul>");
    for (String sponsor : new String[] {"Sun Foundation", "Oracle", "Electronic Arts Foundation", "The National Science Foundation", "Defense Advanced Research Projects Agency", "Hearst Foundations", "Heinz Endowments", "Google", "Disney and Hyperion"}) {
      sb.append("<li><strong>");
      sb.append(sponsor);
      sb.append("</strong></li>");
    }
    sb.append("</ul>");
    sb.append(getLocalizedStringByKey("aboutSimsDonation"));
    sb.append("</html>");

    Label supportedByLabel = new Label(sb.toString());
    supportedByLabel.setBorder(new IconBorder());
    this.addPageStartComponent(supportedByLabel);

    PageAxisPanel otherPanel = new PageAxisPanel();

    FormPanel formPanel = new FormPanel() {
      @Override
      protected void appendRows(List<LabeledFormRow> rows) {
        for (Operation operation : composite.getEulaDialogLaunchOperations()) {
          rows.add(new LabeledFormRow(operation.getSidekickLabel(), operation.createButton()));
        }
      }
    };
    otherPanel.addComponent(formPanel);
    otherPanel.addComponent(BoxUtilities.createVerticalSliver(16));

    //    org.lgna.croquet.components.LineAxisPanel lineAxisPanel = new org.lgna.croquet.components.LineAxisPanel(
    //        org.alice.stageide.about.MainSiteBrowserOperation.getInstance().createHyperlink(),
    //        org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 24 ),
    //        composite.getVersionLabel().createImmutableEditorPane()
    //    );
    //    otherPanel.addComponent( lineAxisPanel );
    otherPanel.addComponent(MainSiteBrowserOperation.getInstance().createHyperlink());
    otherPanel.addComponent(composite.getCreditsDialogLaunchOperation().createHyperlink());
    otherPanel.addComponent(new HtmlMultiLineLabel(String.format(getLocalizedStringByKey("aboutVersion"), ProjectVersion.getCurrentVersionText())));
    otherPanel.addComponent(BoxUtilities.createVerticalSliver(16));
    otherPanel.addComponent(new HtmlMultiLineLabel(getLocalizedStringByKey("aboutDedication")));

    otherPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    this.addCenterComponent(otherPanel);
  }

  private String getLocalizedStringByKey(String key) {
    return ResourceBundleUtilities.getStringForKey(key, AboutComposite.class);
  }
}
