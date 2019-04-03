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
package org.alice.ide.warning.components;

import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import edu.cmu.cs.dennisc.javax.swing.components.JBrowserHyperlink;
import edu.cmu.cs.dennisc.javax.swing.SpringUtilities;
import edu.cmu.cs.dennisc.javax.swing.components.JRowsSpringPane;
import org.alice.ide.issue.ReportSubmissionConfiguration;
import org.alice.ide.warning.WarningDialogComposite;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.FolderTabbedPane;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.PageAxisPanel;
import org.lgna.croquet.views.PlainMultiLineLabel;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.util.List;

public class WarningView extends PageAxisPanel {
  public WarningView(WarningDialogComposite composite) {
    super(composite);
    this.addComponent(new Label(IconUtilities.createImageIcon(WarningView.class.getResource("images/toxic.png"))));
    this.addComponent(BoxUtilities.createVerticalSliver(8));

    PlainMultiLineLabel descriptionLabel = new PlainMultiLineLabel(getLocalizedStringByKey("content"));
    descriptionLabel.scaleFont(1.4f);
    descriptionLabel.changeFont(TextWeight.BOLD);
    this.addComponent(descriptionLabel);
    this.addComponent(BoxUtilities.createVerticalSliver(8));
    class FurtherInfoPane extends JRowsSpringPane {
      public FurtherInfoPane() {
        super(8, 4);
        this.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
      }

      private Component createLabel(String key) {
        JLabel rv = new JLabel(getLocalizedStringByKey(key));
        rv.setHorizontalAlignment(SwingConstants.TRAILING);
        return rv;
      }

      @Override
      protected List<Component[]> addComponentRows(List<Component[]> rv) {
        rv.add(SpringUtilities.createRow(createLabel("updates"), new JBrowserHyperlink("http://www.alice.org/get-alice/alice-3")));
        rv.add(SpringUtilities.createRow(createLabel("blog"), new JBrowserHyperlink("https://www.alice.org/news/")));
        rv.add(SpringUtilities.createRow(createLabel("community"), new JBrowserHyperlink("http://www.alice.org/community/")));
        rv.add(SpringUtilities.createRow(createLabel("bugReports"), new JBrowserHyperlink(ReportSubmissionConfiguration.JIRA_URL)));
        return rv;
      }
    }
    this.getAwtComponent().add(new FurtherInfoPane());

    this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    this.setBackgroundColor(FolderTabbedPane.DEFAULT_BACKGROUND_COLOR);
  }

  private String getLocalizedStringByKey(String key) {
    return ResourceBundleUtilities.getStringForKey(key, WarningDialogComposite.class);
  }
}
