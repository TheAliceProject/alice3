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

import edu.cmu.cs.dennisc.issue.Issue;
import edu.cmu.cs.dennisc.java.awt.font.FontUtilities;
import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.javax.swing.icons.AbstractArrowIcon;
import org.lgna.issue.ApplicationIssueConfiguration;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ActionEvent;

/**
 * @author Dennis Cosgrove
 */
public final class JSubmitPane extends JPanel {
  private static final String CONTRACTED_TEXT = "Provide details";
  private static final String EXPANDED_TEXT = "Please describe the problem and what steps you took that lead you to this bug:";

  public JSubmitPane(Thread thread, Throwable originalThrowable, Throwable originalThrowableOrTarget, ApplicationIssueConfiguration config) {
    this.config = config;
    this.insightPane = new JInsightPane(thread, originalThrowable, originalThrowableOrTarget);
    this.toggleButton.setMargin(new Insets(0, 0, 0, 0));
    Action submitAction = new AbstractAction(config.getSubmitActionName()) {
      @Override
      public void actionPerformed(ActionEvent e) {
        getConfig().submit(JSubmitPane.this);
      }
    };
    this.submitButton = new JButton(submitAction);

    JPanel headerPane = config.createHeaderPane(thread, originalThrowable, originalThrowableOrTarget);

    JPanel insightHeaderPanel = new JPanel();
    insightHeaderPanel.setLayout(new BorderLayout());
    insightHeaderPanel.add(toggleButton, BorderLayout.LINE_START);

    JPanel mainPanel = new JPanel();
    mainPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(insightHeaderPanel, BorderLayout.PAGE_START);
    mainPanel.add(insightPane, BorderLayout.CENTER);

    JPanel submitPanel = new JPanel();
    submitPanel.setLayout(new FlowLayout());
    submitPanel.add(submitButton);

    this.setLayout(new BorderLayout());
    this.add(headerPane, BorderLayout.PAGE_START);
    this.add(mainPanel, BorderLayout.CENTER);
    this.add(submitPanel, BorderLayout.PAGE_END);

    this.toggleButton.addChangeListener(this.changeListener);

    FontUtilities.setFontToDerivedFont(submitButton, TextWeight.BOLD);
    FontUtilities.setFontToScaledFont(submitButton, 1.6f);

    this.toggleButton.setIcon(new AbstractArrowIcon(12) {
      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        AbstractButton button = (AbstractButton) c;
        ButtonModel buttonModel = button.getModel();
        Heading heading = buttonModel.isSelected() ? Heading.SOUTH : Heading.EAST;
        Shape shape = this.createPath(x, y, heading);
        g2.setPaint(Color.DARK_GRAY);
        g2.fill(shape);
      }
    });
    this.toggleButton.setIconTextGap(12);
    this.toggleButton.setHorizontalTextPosition(SwingConstants.LEADING);
    this.toggleButton.setFocusable(false);
    final boolean IS_INSIGHT_EXPANDED_BY_DEFAULT = false;
    if (IS_INSIGHT_EXPANDED_BY_DEFAULT) {
      this.toggleButton.setSelected(IS_INSIGHT_EXPANDED_BY_DEFAULT);
    }
  }

  @Override
  public void addNotify() {
    super.addNotify();
    this.getRootPane().setDefaultButton(this.submitButton);
  }

  public ApplicationIssueConfiguration getConfig() {
    return this.config;
  }

  public Issue.Builder createIssueBuilder() {
    return this.insightPane.createIssueBuilder();
  }

  public boolean isSubmitAttempted() {
    return this.isSubmitAttempted;
  }

  public void setSubmitAttempted(boolean isSubmitAttempted) {
    this.isSubmitAttempted = isSubmitAttempted;
  }

  private final JInsightPane insightPane;

  private final ChangeListener changeListener = new ChangeListener() {
    @Override
    public void stateChanged(ChangeEvent e) {
      Object src = e.getSource();
      if (src instanceof JToggleButton) {
        JToggleButton button = (JToggleButton) src;
        insightPane.setExpanded(button.isSelected());
        toggleButton.setText(button.isSelected() ? EXPANDED_TEXT : CONTRACTED_TEXT);
        ((Window) SwingUtilities.getRoot(button)).pack();
      }
    }
  };
  private final JToggleButton toggleButton = new JToggleButton(CONTRACTED_TEXT);
  private final JButton submitButton;

  private final ApplicationIssueConfiguration config;

  private boolean isSubmitAttempted;
}
