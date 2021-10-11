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
package org.alice.ide.issue.swing;

import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import edu.cmu.cs.dennisc.javax.swing.components.JBrowserHtmlView;
import edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults;
import net.miginfocom.swing.MigLayout;
import org.alice.ide.browser.BrowserOperation;
import org.alice.ide.croquet.models.help.views.GraphicsHelpView;
import org.alice.ide.issue.croquet.views.GlExceptionView;
import org.lgna.issue.ApplicationIssueConfiguration;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public class JGraphicsHeaderPane extends JPanel {
  public JGraphicsHeaderPane(ApplicationIssueConfiguration config) {
    ConformanceTestResults.SharedDetails sharedDetails = ConformanceTestResults.SINGLETON.getSharedDetails();
    ConformanceTestResults.SynchronousPickDetails synchronousPickDetails = ConformanceTestResults.SINGLETON.getSynchronousPickDetails();
    String searchGraphicsDriverUrlSpec = GraphicsHelpView.getRendererSearchUrl();
    String graphicsHelpUrlSpec = BrowserOperation.TROUBLESHOOTING_URL;

    // TODO I18n
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<h1>");
    sb.append(config.getApplicationName());
    sb.append(" has encountered a graphics problem");
    sb.append("</h1>");
    sb.append("<h2>");
    sb.append("The most common way to fix graphics problems is to update your video driver.");
    sb.append("</h2>");
    sb.append("<h3>");
    sb.append("Where to go for help:");
    sb.append("</h3>");
    sb.append("<a href=\"");
    sb.append(searchGraphicsDriverUrlSpec);
    sb.append("\">");
    sb.append(searchGraphicsDriverUrlSpec);
    sb.append("</a> [web]<br>");
    sb.append("<a href=\"");
    sb.append(graphicsHelpUrlSpec);
    sb.append("\">");
    sb.append(graphicsHelpUrlSpec);
    sb.append("</a> [web]<br>");
    sb.append("<h3>");
    sb.append("About your computer:");
    sb.append("</h3>");
    sb.append("Graphics information: <strong>");
    if (sharedDetails != null) {
      sb.append(sharedDetails.getRenderer());
    } else {
      sb.append("unknown");
    }
    sb.append("</strong><br>");
    sb.append("System information: <strong>");
    sb.append(System.getProperty("os.name"));
    sb.append(" ");
    sb.append(System.getProperty("sun.arch.data.model"));
    sb.append("-bit</strong><p>");
    sb.append("FYI: ");
    if (synchronousPickDetails != null) {
      if (synchronousPickDetails.isPickFunctioningCorrectly()) {
        sb.append("Clicking into the scene appears to be functioning correctly");
        if (synchronousPickDetails.isPickActuallyHardwareAccelerated()) {
          sb.append(" in hardware");
        } else {
          sb.append(" in software (updating your video drivers might help)");
          if (synchronousPickDetails.isReportingPickCanBeHardwareAccelerated()) {
            sb.append("(video card reports hardware support but fails)");
          }
        }
      } else {
        sb.append("Clicking into the scene appears to be suboptimal (updating your video drivers might help)");
      }
    } else {
      sb.append("There is no information on clicking into the scene");
    }
    sb.append(".");
    sb.append("</html>");

    this.setLayout(new MigLayout("fill", "[grow 0][grow 0][grow]"));
    this.add(new JLabel(IconUtilities.getErrorIcon()), "aligny top");
    this.add(new JBrowserHtmlView(sb.toString()), "grow");
    this.add(new JLabel(GlExceptionView.ICON), "wrap");

    this.add(new JLabel("<html>If you have updated your video drivers and this problem still persists then please press the \"<em>" + config.getSubmitActionName() + "</em>\" button.<html>"), "span 3");
    this.setBackground(Color.WHITE);

    //    this.add( new org.lgna.croquet.components.Label( "Alice has encountered a graphics problem", javax.swing.UIManager.getIcon( "OptionPane.errorIcon" ), 2.0f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ), "wrap" );
    //    this.addComponent( new org.alice.ide.croquet.models.help.views.GraphicsHelpView(), "wrap" );
    //    this.addComponent( new org.lgna.croquet.components.LineAxisPanel(
    //        new org.lgna.croquet.components.Label( "If you have updated your video drivers and the problem still persists please " ),
    //        new org.lgna.croquet.components.Label( "submit a bug report", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE ),
    //        new org.lgna.croquet.components.Label( "." )
    //        ), "wrap, span 2" );
    //
    //    this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
  }
}
