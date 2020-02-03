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
package org.alice.ide.youtube.croquet;

import java.awt.Container;
import java.io.File;
import java.util.UUID;

import javax.swing.JDialog;

import edu.cmu.cs.dennisc.javax.swing.option.MessageType;
import org.alice.ide.browser.ImmutableBrowserOperation;
import org.alice.ide.help.HelpBrowserOperation;
import org.alice.ide.youtube.croquet.views.ExecutionPermissionFailedDialogView;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.MessageDialogComposite;
import org.lgna.croquet.Operation;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.edits.Edit;

import edu.cmu.cs.dennisc.java.lang.RuntimeUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.wustl.lookingglass.media.FFmpegProcess;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.simple.SimpleApplication;
import org.lgna.croquet.views.ScrollPane;

/**
 * @author Matt May
 */
public class ExecutionPermissionFailedDialogComposite extends MessageDialogComposite<ExecutionPermissionFailedDialogView> {

  private final StringValue explanation = createStringValue("explanation");
  private boolean isFixed = false;
  private final Operation browserOperation = new ImmutableBrowserOperation(UUID.fromString("06d89886-9433-4b52-85b6-10615412eb0c"), HelpBrowserOperation.HELP_URL_SPEC + "w/page/68664600/FFmpeg_execute_permission");
  private final File ffmpegFile;

  public ExecutionPermissionFailedDialogComposite(File f) {
    super(UUID.fromString("d60cddc2-ec53-40bd-949b-7a445b92b43b"), MessageType.ERROR);
    this.ffmpegFile = f;
  }

  private final ActionOperation troubleShootAction = createActionOperation("troubleShoot", new Action() {

    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      if (SystemUtilities.isMac()) {
        RuntimeUtilities.exec("chmod a+x " + ffmpegFile.getAbsolutePath());
        isFixed = (ffmpegFile != null) && ffmpegFile.canExecute();
        if (isFixed) {
          closeDown();
        }
      } else if (SystemUtilities.isWindows()) {
        // TODO later perhaps? (mmay)
        //        DesktopUtilities.open( ffmpegFile.getParentFile() );
      }
      return null;
    }
  });

  private void closeDown() {
    Container awtComponent = ExecutionPermissionFailedDialogComposite.this.getView().getAwtComponent();
    while ((awtComponent != null) && !(awtComponent instanceof JDialog)) {
      awtComponent = awtComponent.getParent();
    }
    assert awtComponent != null;
    ((JDialog) awtComponent).setVisible(false);
  }

  @Override
  protected ScrollPane createScrollPaneIfDesired() {
    return null;
  }

  @Override
  protected ExecutionPermissionFailedDialogView createView() {
    return new ExecutionPermissionFailedDialogView(this);
  }

  public boolean getIsFixed() {
    return isFixed;
  }

  public StringValue getExplanation() {
    return this.explanation;
  }

  public Operation getBrowserOperation() {
    return this.browserOperation;
  }

  public ActionOperation getTroubleShootAction() {
    return SystemUtilities.isMac() ? this.troubleShootAction : null;
  }

  public static void main(String[] args) {
    SimpleApplication app = new SimpleApplication();
    new ExecutionPermissionFailedDialogComposite(new File(FFmpegProcess.getArchitectureSpecificCommand())).getLaunchOperation().fire();
    System.exit(0);
  }
}
