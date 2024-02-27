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

package org.lgna.croquet;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.dialog.DialogContentComposite;
import org.lgna.croquet.views.Button;
import org.lgna.croquet.views.CompositeView;
import org.lgna.croquet.views.Dialog;

import javax.swing.JComboBox;
import javax.swing.UIManager;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AdornedDialogCoreComposite<V extends CompositeView<?, ?>, DCC extends DialogContentComposite<?>> extends AbstractDialogComposite<V> {

  protected abstract static class InternalDialogOperation extends Operation {
    private final AdornedDialogCoreComposite coreComposite;

    public InternalDialogOperation(UUID id, AdornedDialogCoreComposite coreComposite) {
      super(DIALOG_IMPLEMENTATION_GROUP, id);
      this.coreComposite = coreComposite;
    }

    public AdornedDialogCoreComposite getDialogCoreComposite() {
      return this.coreComposite;
    }

    @Override
    protected Class<? extends Element> getClassUsedForLocalization() {
      return this.coreComposite.getClassUsedForLocalization();
    }
  }

  private final class InternalCommitOperation extends InternalDialogOperation {
    private InternalCommitOperation(AdornedDialogCoreComposite coreComposite) {
      super(UUID.fromString("8618f47b-8a2b-45e1-ad03-0ff76e2b7e35"), coreComposite);
    }

    @Override
    protected final void performInActivity(UserActivity userActivity) {
      if (getDialogCoreComposite().isClearedForCommit()) {
        userActivity.setCompletionModel(this);
        isCommitted = true;
        dialog.setVisible(false);
        userActivity.finish();
      } else {
        Logger.outln(this);
      }
    }

    @Override
    protected String getSubKeyForLocalization() {
      return "commit";
    }

    @Override
    protected String findDefaultLocalizedText() {
      String rv = super.findDefaultLocalizedText();
      if (rv == null) {
        Locale locale = JComboBox.getDefaultLocale();
        String commitUiKey = this.getDialogCoreComposite().getCommitUiKey();
        if (commitUiKey != null) {
          rv = UIManager.getString(commitUiKey, locale);
        }
        if (rv == null) {
          rv = this.getDialogCoreComposite().getDefaultCommitText();
        }
      }
      return rv;
    }
  }

  private final class InternalCancelOperation extends InternalDialogOperation {
    private InternalCancelOperation(AdornedDialogCoreComposite coreComposite) {
      super(UUID.fromString("c467630e-39ee-49c9-ad07-d20c7a29db68"), coreComposite);
    }

    @Override
    protected final void performInActivity(UserActivity userActivity) {
      userActivity.setCompletionModel(this);
      isCommitted = false;
      dialog.setVisible(false);
      userActivity.finish();
    }

    @Override
    protected String getSubKeyForLocalization() {
      return "cancel";
    }

    @Override
    protected String findDefaultLocalizedText() {
      String rv = super.findDefaultLocalizedText();
      if (rv == null) {
        Locale locale = JComboBox.getDefaultLocale();
        rv = UIManager.getString("OptionPane.cancelButtonText", locale);
        if (rv == null) {
          rv = "Cancel";
        }
      }
      return rv;
    }
  }

  private final InternalCommitOperation commitOperation = new InternalCommitOperation(this);
  private final InternalCancelOperation cancelOperation = new InternalCancelOperation(this);

  public AdornedDialogCoreComposite(UUID migrationId) {
    super(migrationId, IsModal.TRUE);
  }

  protected abstract DCC getDialogContentComposite();

  public final Operation getCommitOperation() {
    return this.commitOperation;
  }

  public final Operation getCancelOperation() {
    return this.cancelOperation;
  }

  protected abstract String getDefaultCommitText();

  protected abstract String getCommitUiKey();

  protected String getCancelUiKey() {
    return "OptionPane.cancelButtonText";
  }

  @Override
  protected void localize() {
    super.localize();
    String commitText = this.findLocalizedText("commit");
    if (commitText == null) {
      Locale locale = JComboBox.getDefaultLocale();
      String commitUiKey = this.getCommitUiKey();
      if (commitUiKey != null) {
        commitText = UIManager.getString(commitUiKey, locale);
      }
      if (commitText == null) {
        commitText = this.getDefaultCommitText();
      }
    }
    this.getCommitOperation().setName(commitText);
    String cancelText = this.findLocalizedText("cancel");
    if (cancelText == null) {
      Locale locale = JComboBox.getDefaultLocale();
      cancelText = UIManager.getString("OptionPane.cancelButtonText", locale);
      if (cancelText == null) {
        cancelText = "Cancel";
      }
    }
    this.getCancelOperation().setName(cancelText);
  }

  @Override
  protected CompositeView<?, ?> allocateView() {
    //todo
    return this.getDialogContentComposite().getView();
  }

  @Override
  protected void releaseView(CompositeView<?, ?> view) {
    //todo
  }

  protected boolean isClearedForCommit() {
    return true;
  }

  @Override
  protected void handlePreShowDialog(Dialog dialog) {
    isCommitted = false;
    this.getDialogContentComposite().handlePreActivation();
    if (this.isDefaultButtonDesired()) {
      Button commitButton = this.getDialogContentComposite().getView().getCommitButton();
      if (commitButton != null) {
        dialog.setDefaultButton(commitButton);
      }
    }
  }

  @Override
  protected void handlePostHideDialog() {
    this.getDialogContentComposite().handlePostDeactivation();
  }

  protected boolean isCommitted = false;
}
