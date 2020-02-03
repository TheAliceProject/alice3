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
package org.alice.media.youtube.croquet;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Crawlable;
import edu.cmu.cs.dennisc.pattern.Crawler;
import org.alice.media.youtube.croquet.views.EventRecordView;
import org.alice.media.youtube.croquet.views.icons.IsPlayingIcon;
import org.alice.stageide.StageIDE;
import org.alice.stageide.ast.StoryApiSpecificAstUtilities;
import org.alice.stageide.program.RunProgramContext;
import org.lgna.common.RandomUtilities;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.MutableDataSingleSelectListState;
import org.lgna.croquet.WizardPageComposite;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserMethod;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.ast.EventListenerMethodUtilities;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.animation.FrameObserver;
import edu.cmu.cs.dennisc.matt.eventscript.EventScript;
import edu.cmu.cs.dennisc.matt.eventscript.events.EventScriptEvent;
import edu.cmu.cs.dennisc.matt.eventscript.events.EventScriptListener;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Matt May
 */
public class EventRecordComposite extends WizardPageComposite<EventRecordView, ExportToYouTubeWizardDialogComposite> {

  private static final List<JavaMethod> interactiveMethods = Collections.unmodifiableList(EventListenerMethodUtilities.ALL_MOUSE_CLICK_EVENT_METHODS);
  private final ErrorStatus cannotAdvanceBecauseRecording = this.createErrorStatus("cannotAdvanceBecauseRecording");
  private final BooleanState isRecordingState = this.createBooleanState("isRecordingState", false);
  private RunProgramContext programContext;
  private EventScript eventScript;

  public EventRecordComposite(ExportToYouTubeWizardDialogComposite owner) {
    super(UUID.fromString("35d34417-8c0c-4f06-b919-5945b336b596"), owner);
    this.isRecordingState.setIconForBothTrueAndFalse(new IsPlayingIcon());
    isRecordingState.addNewSchoolValueListener(isRecordingListener);
  }

  private final ValueListener<Boolean> isRecordingListener = new ValueListener<Boolean>() {
    @Override
    public void valueChanged(ValueEvent<Boolean> e) {
      if (isRecordingState.getValue()) {
        programContext.getProgramImp().startAnimator();
        programContext.getProgramImp().getAnimator().setSpeedFactor(1);
        restartRecording.setEnabled(true);
      } else {
        programContext.getProgramImp().getAnimator().setSpeedFactor(0);
      }
    }
  };

  private final EventScriptListener listener = new EventScriptListener() {

    @Override
    public void eventAdded(EventScriptEvent event) {
      getEventList().addItem(event);
    }
  };
  private final FrameObserver frameListener = new FrameObserver() {

    @Override
    public void update(double tCurrent) {
      getView().updateTime(tCurrent);
    }

    @Override
    public void complete() {
    }
  };
  private final ActionOperation restartRecording = this.createActionOperation("restart", new Action() {

    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      isRecordingState.setValueTransactionlessly(false);
      resetData();
      return null;
    }

  });

  @Override
  public void handlePostDeactivation() {
    isRecordingState.setValueTransactionlessly(false);
    this.getView().disableLookingGlassContainer();
    super.handlePostDeactivation();
  }

  @Override
  public void handlePreActivation() {
    super.handlePreActivation();
    if (programContext == null) {
      restartProgramContext();
    }
    this.getView().enableLookingGlassContainer();
  }

  private void restartProgramContext() {
    ExportToYouTubeWizardDialogComposite owner = this.getOwner();
    restartRecording.setEnabled(false);
    if ((programContext != null)) {
      programContext.getProgramImp().getAnimator().removeFrameObserver(frameListener);
    }
    if (containsRandom()) {
      this.getOwner().setRandomSeed(System.currentTimeMillis());
    }
    programContext = new RunProgramContext(owner.getProject().getProgramType());
    programContext.getProgramImp().setControlPanelDesired(false);
    programContext.initializeInContainer(getView().getLookingGlassContainer().getAwtComponent(), 640, 360);
    programContext.getProgramImp().getAnimator().addFrameObserver(frameListener);
    programContext.getProgramImp().stopAnimator();
    programContext.setActiveScene();
    eventScript = ((SceneImp) EmployeesOnly.getImplementation(programContext.getProgram().getActiveScene())).getTranscript();
    owner.setEventScript(eventScript);
    getEventList().clear();
    eventScript.addListener(this.listener);
    getView().updateTime(0);
  }

  public BooleanState getPlayRecordedOperation() {
    return this.isRecordingState;
  }

  public ActionOperation getRestartRecording() {
    return this.restartRecording;
  }

  public EventScript getScript() {
    return this.eventScript;
  }

  @Override
  protected EventRecordView createView() {
    return new EventRecordView(this);
  }

  @Override
  public Status getPageStatus() {
    return isRecordingState.getValue() ? cannotAdvanceBecauseRecording : IS_GOOD_TO_GO_STATUS;
  }

  @Override
  protected boolean isOptional() {
    return true;
  }

  @Override
  protected boolean isAutoAdvanceWorthAttempting() {
    return true;
  }

  @Override
  protected boolean isClearedForAutoAdvance() {
    return (!this.containsInputEvents() && !this.containsRandom());
  }

  private boolean containsRandom() {
    StageIDE ide = StageIDE.getActiveInstance();
    if (ide != null) {
      RandomUtilitiesMethodInvocationCrawler crawler = new RandomUtilitiesMethodInvocationCrawler();
      ide.crawlFilteredProgramType(crawler);
      return crawler.containsRandom;
    } else {
      Logger.errln("containsRandom check skipped due to lack of ide");
      return false;
    }
  }

  private static class RandomUtilitiesMethodInvocationCrawler implements Crawler {
    private boolean containsRandom;

    @Override
    public void visit(Crawlable crawlable) {
      if (crawlable instanceof MethodInvocation) {
        MethodInvocation methodInvocation = (MethodInvocation) crawlable;
        Statement statement = methodInvocation.getFirstAncestorAssignableTo(Statement.class);
        if ((statement != null) && statement.isEnabled.getValue()) {
          AbstractMethod method = methodInvocation.method.getValue();
          if (method.isFunction()) {
            if (method.getDeclaringType() instanceof JavaType) {
              JavaType jType = (JavaType) method.getDeclaringType();
              this.containsRandom = jType.contentEquals(RandomUtilities.class);
            }
          }
        }
      }
    }
  }

  private boolean containsInputEvents() {
    Project project = this.getOwner().getProject();
    NamedUserType sceneType = StoryApiSpecificAstUtilities.getSceneTypeFromProject(project);
    UserMethod initializeEventListeners = sceneType.getDeclaredMethod(StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME);
    BlockStatement body = initializeEventListeners.body.getValue();
    for (Statement statement : body.statements) {
      if (statement.isEnabled.getValue()) {
        if (statement instanceof ExpressionStatement) {
          ExpressionStatement expressionStatement = (ExpressionStatement) statement;
          if (expressionStatement.expression.getValue() instanceof MethodInvocation) {
            AbstractMethod method = ((MethodInvocation) expressionStatement.expression.getValue()).method.getValue();
            if (interactiveMethods.contains(method)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public MutableDataSingleSelectListState<EventScriptEvent> getEventList() {
    return getOwner().getEventList();
  }

  @Override
  public void resetData() {
    BorderPanel lookingGlassContainer = getView().getLookingGlassContainer();
    if (lookingGlassContainer != null) {
      synchronized (lookingGlassContainer.getTreeLock()) {
        lookingGlassContainer.removeAllComponents();
      }
    }
    restartProgramContext();
  }

  @Override
  public void handlePostHideDialog() {
    programContext.cleanUpProgram();
    super.handlePostHideDialog();
  }
}
