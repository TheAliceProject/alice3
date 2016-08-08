/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
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

package org.alice.netbeans.palette.items.views;

import java.awt.Color;
import java.awt.Dialog;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

public class CountLoopCustomizer extends javax.swing.JPanel {

    private Dialog dialog = null;
    private DialogDescriptor descriptor = null;
    private boolean dialogOK = false;


    JTextComponent target;

    /** Creates new form CountLoopCustomizer */
    public CountLoopCustomizer(JTextComponent target) {
        this.target = target;

        initComponents();

        this.variableNameTextField.getDocument().addDocumentListener( new DocumentListener() {

            public void insertUpdate(DocumentEvent arg0) {
                evaluateInput();
            }

            public void removeUpdate(DocumentEvent arg0) {
                evaluateInput();
            }

            public void changedUpdate(DocumentEvent arg0) {
                evaluateInput();
            }
        });
    }

    public boolean showDialog() {

		this.variableNameTextField.selectAll();;
		this.variableNameTextField.requestFocusInWindow();
		
        dialogOK = false;

        String displayName = "Counted For Loop";
        Object[] options = {okButton, cancelButton};
        descriptor = new DialogDescriptor
                (this, 
                displayName,
                true,
                options,
                okButton,
                DialogDescriptor.DEFAULT_ALIGN,
                null,
                null);
        dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        repaint();
        return dialogOK;
    }

    private void evaluateInput()
    {
        if (isInputValid())
        {
            this.variableNameTextField.setForeground(Color.black);
            this.okButton.setEnabled(true);
        }
        else
        {
            this.variableNameTextField.setForeground(Color.red);
            this.okButton.setEnabled(false);
        }
    }

    private boolean isInputValid()
    {
        String variableName = variableNameTextField.getText();
        if (variableName != null && variableName.length() > 0)
        {
            if (Character.isJavaIdentifierStart(variableName.charAt(0)))
            {
                boolean isValid = true;
                for (int i=1; i<variableName.length(); i++)
                {
                    if (!Character.isJavaIdentifierPart(variableName.charAt(i)))
                    {
                        isValid = false;
                        break;
                    }
                }
                return isValid;
            }
        }
        return false;
    }


	public String getVariableName() {
		return this.variableNameTextField.getText();
	}
	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        variableNameTextField = new javax.swing.JTextField();

        cancelButton.setText(org.openide.util.NbBundle.getMessage(CountLoopCustomizer.class, "CountLoopCustomizer.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText(org.openide.util.NbBundle.getMessage(CountLoopCustomizer.class, "CountLoopCustomizer.okButton.text")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText(org.openide.util.NbBundle.getMessage(CountLoopCustomizer.class, "CountLoopCustomizer.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 6);
        add(jLabel1, gridBagConstraints);

        variableNameTextField.setColumns(8);
        variableNameTextField.setText(org.openide.util.NbBundle.getMessage(CountLoopCustomizer.class, "CountLoopCustomizer.variableNameTextField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        add(variableNameTextField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        dialogOK = true;
        dialog.dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dialogOK = false;
        dialog.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField variableNameTextField;
    // End of variables declaration//GEN-END:variables

}
