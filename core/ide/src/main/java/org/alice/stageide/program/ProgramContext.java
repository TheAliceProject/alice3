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

package org.alice.stageide.program;

import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import org.alice.ide.IDE;
import org.alice.ide.ReasonToDisableSomeAmountOfRendering;
import org.alice.ide.issue.UserProgramRunningStateUtilities;
import org.alice.stageide.StageIDE;
import org.alice.stageide.StoryApiConfigurationManager;
import org.alice.stageide.apis.story.event.ArrowKeyAdapter;
import org.alice.stageide.apis.story.event.ComesIntoViewEventAdapter;
import org.alice.stageide.apis.story.event.ComesOutOfViewEventAdapter;
import org.alice.stageide.apis.story.event.EndCollisionAdapter;
import org.alice.stageide.apis.story.event.EndOcclusionEventAdapter;
import org.alice.stageide.apis.story.event.EnterProximityAdapter;
import org.alice.stageide.apis.story.event.ExitProximityAdapter;
import org.alice.stageide.apis.story.event.KeyAdapter;
import org.alice.stageide.apis.story.event.MouseClickOnObjectAdapter;
import org.alice.stageide.apis.story.event.MouseClickOnScreenAdapter;
import org.alice.stageide.apis.story.event.NumberKeyAdapter;
import org.alice.stageide.apis.story.event.SceneActivationAdapter;
import org.alice.stageide.apis.story.event.StartCollisionAdapter;
import org.alice.stageide.apis.story.event.StartOcclusionEventAdapter;
import org.alice.stageide.apis.story.event.TimerEventAdapter;
import org.alice.stageide.apis.story.event.TransformationEventAdapter;
import org.alice.stageide.ast.SceneAdapter;
import org.lgna.common.ProgramClosedException;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.virtualmachine.ReleaseVirtualMachine;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.project.virtualmachine.VirtualMachine;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SJointedModel;
import org.lgna.story.SProgram;
import org.lgna.story.SScene;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;
import org.lgna.story.event.NumberKeyPressListener;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.implementation.ProgramImp;
import org.lgna.story.resources.JointedModelResource;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProgramContext {
  protected static NamedUserType getUpToDateProgramTypeFromActiveIde() {
    final StageIDE ide = StageIDE.getActiveInstance();
    if (ide != null) {
      return ide.getUpToDateProgramType();
    } else {
      return null;
    }
  }

  private final UserInstance programInstance;
  private final VirtualMachine vm;

  public ProgramContext(NamedUserType programType) {
    assert programType != null;
    this.vm = this.createVirtualMachine();
    this.vm.registerAbstractClassAdapter(SScene.class, SceneAdapter.class);
    this.vm.registerAbstractClassAdapter(SceneActivationListener.class, SceneActivationAdapter.class);
    this.vm.registerAbstractClassAdapter(MouseClickOnScreenListener.class, MouseClickOnScreenAdapter.class);
    this.vm.registerAbstractClassAdapter(MouseClickOnObjectListener.class, MouseClickOnObjectAdapter.class);
    this.vm.registerAbstractClassAdapter(KeyPressListener.class, KeyAdapter.class);
    this.vm.registerAbstractClassAdapter(ArrowKeyPressListener.class, ArrowKeyAdapter.class);
    this.vm.registerAbstractClassAdapter(NumberKeyPressListener.class, NumberKeyAdapter.class);
    this.vm.registerAbstractClassAdapter(PointOfViewChangeListener.class, TransformationEventAdapter.class);
    this.vm.registerAbstractClassAdapter(ViewEnterListener.class, ComesIntoViewEventAdapter.class);
    this.vm.registerAbstractClassAdapter(ViewExitListener.class, ComesOutOfViewEventAdapter.class);
    this.vm.registerAbstractClassAdapter(CollisionStartListener.class, StartCollisionAdapter.class);
    this.vm.registerAbstractClassAdapter(CollisionEndListener.class, EndCollisionAdapter.class);
    this.vm.registerAbstractClassAdapter(ProximityEnterListener.class, EnterProximityAdapter.class);
    this.vm.registerAbstractClassAdapter(ProximityExitListener.class, ExitProximityAdapter.class);
    this.vm.registerAbstractClassAdapter(OcclusionStartListener.class, StartOcclusionEventAdapter.class);
    this.vm.registerAbstractClassAdapter(OcclusionEndListener.class, EndOcclusionEventAdapter.class);
    this.vm.registerAbstractClassAdapter(TimeListener.class, TimerEventAdapter.class);

    vm.registerProtectedMethodAdapter(ReflectionUtilities.getDeclaredMethod(SJointedModel.class, "setJointedModelResource", JointedModelResource.class), ReflectionUtilities.getDeclaredMethod(EmployeesOnly.class, "invokeSetJointedModelResource", SJointedModel.class, JointedModelResource.class));

    UserProgramRunningStateUtilities.setUserProgramRunning(true);
    this.programInstance = this.createProgramInstance(programType);
  }

  protected UserInstance createProgramInstance(NamedUserType programType) {
    return this.vm.ENTRY_POINT_createInstance(programType);
  }

  protected VirtualMachine createVirtualMachine() {
    return new ReleaseVirtualMachine();
  }

  public UserInstance getProgramInstance() {
    return this.programInstance;
  }

  public SProgram getProgram() {
    return this.programInstance.getJavaInstance(SProgram.class);
  }

  public ProgramImp getProgramImp() {
    return EmployeesOnly.getImplementation(this.getProgram());
  }

  public VirtualMachine getVirtualMachine() {
    return this.vm;
  }

  public OnscreenRenderTarget<?> getOnscreenRenderTarget() {
    ProgramImp programImp = this.getProgramImp();
    return programImp != null ? programImp.getOnscreenRenderTarget() : null;
  }

  private ReasonToDisableSomeAmountOfRendering rendering;

  void disableRendering() {
    this.rendering = ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN;
    IDE ide = IDE.getActiveInstance();
    if (ide != null) {
      ide.getDocumentFrame().disableRendering(rendering);
    }
  }

  private void enableRendering() {
    if (this.rendering != null) {
      IDE ide = IDE.getActiveInstance();
      if (ide != null) {
        ide.getDocumentFrame().enableRendering();
      }
      this.rendering = null;
    }
  }

  public void setActiveScene() {
    ProgramClosedException.invokeAndCatchProgramClosedException(new Runnable() {
      @Override
      public void run() {
        UserField sceneField = null;
        for (UserField field : programInstance.getType().fields) {
          if (field.valueType.getValue().isAssignableTo(SScene.class)) {
            sceneField = field;
          }
        }
        assert sceneField != null;
        UserInstance programInstance = ProgramContext.this.getProgramInstance();
        ProgramContext.this.getVirtualMachine().ENTRY_POINT_invoke(programInstance, StoryApiConfigurationManager.SET_ACTIVE_SCENE_METHOD, programInstance.getFieldValue(sceneField));
      }
    });
  }

  public void cleanUpProgram() {
    UserProgramRunningStateUtilities.setUserProgramRunning(false);
    this.getProgramImp().shutDown();
    vm.stopExecution();
    enableRendering();
  }
}
