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
package edu.cmu.cs.dennisc.eula.swing;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.JDialogBuilder;
import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

/**
 * @author Dennis Cosgrove
 */
public class JEulaPane extends JPanel {
  public JEulaPane(String eulaText) {
    JLabel headerLabel = new JLabel("<html>Please read the following license agreement carefully.</html>");

    JTextArea textArea = new JTextArea();
    textArea.setText(eulaText);
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    JCheckBox reject = new JCheckBox();
    this.accept.setText("I accept the terms in the License Agreement");
    reject.setText("I do not accept the terms in the License Agreement");

    ButtonGroup group = new ButtonGroup();
    group.add(this.accept);
    group.add(reject);
    reject.setSelected(true);

    final JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(480, 320));

    headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.accept.setAlignmentX(Component.LEFT_ALIGNMENT);
    reject.setAlignmentX(Component.LEFT_ALIGNMENT);

    this.accept.addChangeListener(this.changeListener);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(scrollPane);
    mainPanel.add(Box.createVerticalStrut(4));
    mainPanel.add(this.accept);
    mainPanel.add(reject);
    mainPanel.add(Box.createVerticalStrut(8));

    String okButtonText = UIManager.getString("OptionPane.okButtonText");
    String cancelButtonText = UIManager.getString("OptionPane.cancelButtonText");
    if ((okButtonText != null) && (okButtonText.length() > 0)) {
      //pass
    } else {
      okButtonText = "OK";
    }
    if ((cancelButtonText != null) && (cancelButtonText.length() > 0)) {
      //pass
    } else {
      cancelButtonText = "Cancel";
    }

    this.okButton = new JButton(new AbstractAction(okButtonText) {
      @Override
      public void actionPerformed(ActionEvent e) {
        handleOkOrCancel(true);
      }
    });
    JButton cancelButton = new JButton(new AbstractAction(cancelButtonText) {
      @Override
      public void actionPerformed(ActionEvent e) {
        handleOkOrCancel(false);
      }
    });

    this.okButton.setEnabled(false);

    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
    if (SystemUtilities.isWindows()) {
      controlPanel.add(okButton);
      controlPanel.add(cancelButton);
    } else {
      controlPanel.add(cancelButton);
      controlPanel.add(okButton);
    }
    //edu.cmu.cs.dennisc.java.util.logging.Logger.outln( controlPanel.getComponentOrientation().isLeftToRight() );

    this.setLayout(new BorderLayout());
    this.add(headerLabel, BorderLayout.PAGE_START);
    this.add(mainPanel, BorderLayout.CENTER);
    this.add(controlPanel, BorderLayout.PAGE_END);

    this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        scrollPane.getVerticalScrollBar().setValue(0);
      }
    });
  }

  @Override
  public void addNotify() {
    super.addNotify();
    this.getRootPane().setDefaultButton(this.okButton);
  }

  private void handleOkOrCancel(boolean isCommitted) {
    this.isCommitted = isCommitted;
    SwingUtilities.getRoot(this).setVisible(false);
  }

  public boolean isAccepted() {
    return this.isCommitted && this.accept.isSelected();
  }

  private final JButton okButton;
  private final ChangeListener changeListener = new ChangeListener() {
    @Override
    public void stateChanged(ChangeEvent e) {
      okButton.setEnabled(accept.isSelected());
    }
  };

  private final JCheckBox accept = new JCheckBox();
  private boolean isCommitted;

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        //        java.util.Locale locale = java.util.Locale.SIMPLIFIED_CHINESE;
        //        java.util.Locale.setDefault( locale );
        //        //javax.swing.JComponent.setDefaultLocale( locale );
        //        //javax.swing.JOptionPane.showConfirmDialog( null, "hello", "title", javax.swing.JOptionPane.OK_CANCEL_OPTION );
        UIManagerUtilities.setLookAndFeel("Nimbus");
        JEulaPane eulaPane = new JEulaPane("eulaText");
        JDialog dialog = new JDialogBuilder().isModal(true).title("title").build();
        dialog.getContentPane().add(eulaPane, BorderLayout.CENTER);
        dialog.pack();
        dialog.setVisible(true);
        Logger.outln(eulaPane.isAccepted());
        System.exit(0);
      }
    });
  }
}
