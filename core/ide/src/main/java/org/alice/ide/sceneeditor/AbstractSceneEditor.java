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
package org.alice.ide.sceneeditor;

import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.ProjectApplication;
import org.alice.ide.ProjectDocument;
import org.alice.ide.ReasonToDisableSomeAmountOfRendering;
import org.alice.ide.ast.AstEventManager;
import org.alice.ide.perspectives.ProjectPerspective;
import org.alice.ide.project.ProjectDocumentState;
import org.lgna.common.ComponentExecutor;
import org.lgna.croquet.State;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.project.virtualmachine.VirtualMachine;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SProgram;
import org.lgna.story.SScene;
import org.lgna.story.SThing;
import org.lgna.story.implementation.EntityImp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSceneEditor extends BorderPanel {

  private VirtualMachine vm;

  // This map and selection were put in to support possible future projects with multiple scenes
  private Map<UserField, UserInstance> mapSceneFieldToInstance = new HashMap<UserField, UserInstance>();
  private final SceneFieldState sceneFieldListSelectionState = new SceneFieldState();

  private Map<UserField, Statement> mapSceneFieldToInitialCodeState = new HashMap<UserField, Statement>();

  private NamedUserType programType;
  private UserInstance programInstance;
  private UserField selectedField;

  private final State.ValueListener<ProjectDocument> projectListener = new State.ValueListener<ProjectDocument>() {
    @Override
    public void changing(State<ProjectDocument> state, ProjectDocument prevValue, ProjectDocument nextValue) {
    }

    @Override
    public void changed(State<ProjectDocument> state, ProjectDocument prevValue, ProjectDocument nextValue) {
      AbstractSceneEditor.this.handleProjectOpened(nextValue != null ? nextValue.getProject() : null);
    }
  };

  private ValueListener<UserField> selectedSceneObserver = new ValueListener<UserField>() {
    @Override
    public void valueChanged(ValueEvent<UserField> e) {
      UserField nextValue = e.getNextValue();
      AbstractSceneEditor.this.setActiveScene(nextValue);
    }
  };

  private final ValueListener<ProjectPerspective> perspectiveListener = new ValueListener<ProjectPerspective>() {
    @Override
    public void valueChanged(ValueEvent<ProjectPerspective> e) {
      ProjectPerspective prevValue = e.getPreviousValue();
      ProjectPerspective nextValue = e.getNextValue();
      if (prevValue != nextValue) {
        AbstractSceneEditor.this.handleExpandContractChange(nextValue == IDE.getActiveInstance().getDocumentFrame().getSetupScenePerspective());
      }
    }
  };

  public abstract void disableRendering(ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering);

  public abstract void enableRendering(ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering);

  public abstract void generateCodeForSetUp(StatementListProperty bodyStatementsProperty);

  public abstract Statement[] getDoStatementsForAddField(UserField field, AffineMatrix4x4 initialTransform);

  public abstract Statement[] getDoStatementsForCopyField(UserField fieldToCopy, UserField newField, AffineMatrix4x4 initialTransform);

  public abstract Statement[] getUndoStatementsForAddField(UserField field);

  public abstract Statement[] getDoStatementsForRemoveField(UserField field);

  public abstract Statement[] getUndoStatementsForRemoveField(UserField field);

  public abstract void preScreenCapture();

  public abstract void postScreenCapture();

  protected abstract void handleExpandContractChange(boolean isExpanded);

  //Initialization
  private boolean isInitialized = false;

  private void initializeIfNecessary() {
    if (!this.isInitialized) {
      initializeComponents();
      initializeObservers();
      this.isInitialized = true;
    }
  }

  protected void initializeComponents() {

  }

  protected void initializeObservers() {
    IDE.getActiveInstance().getDocumentFrame().getPerspectiveState().addAndInvokeNewSchoolValueListener(this.perspectiveListener);
  }

  protected void setInitialCodeStateForField(UserField field, Statement code) {
    this.mapSceneFieldToInitialCodeState.put(field, code);
  }

  protected Statement getInitialCodeforField(UserField field) {
    return this.mapSceneFieldToInitialCodeState.get(field);
  }

  public UserField getActiveSceneField() {
    return this.sceneFieldListSelectionState.getValue();
  }

  public UserField getFieldForInstanceInJavaVM(Object javaInstance) {
    return getActiveSceneInstance().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava(javaInstance);
  }

  public Object getInstanceInJavaVMForField(AbstractField field) {
    if (field == null) {
      return null;
    }
    assert field instanceof UserField;
    if (field == this.getActiveSceneField()) {
      return getActiveSceneInstance().getJavaInstance();
    } else {
      return getActiveSceneInstance().getFieldValueInstanceInJava((UserField) field);
    }
  }

  public Object getInstanceForExpression(Expression expression) {
    if (expression == null) {
      return null;
    }
    try {
      Object[] values = this.getVirtualMachine().ENTRY_POINT_evaluate(getActiveSceneInstance(), new Expression[] {expression});
      if (values.length > 0) {
        return values[0];
      } else {
        return null;
      }
    } catch (Throwable t) {
      Logger.throwable(t);
      return null;
    }
  }

  public Object getInstanceInJavaVMForExpression(Expression expression) {
    return UserInstance.getJavaInstanceIfNecessary(getInstanceForExpression(expression));
  }

  public <E> E getInstanceInJavaVMForField(AbstractField field, Class<E> cls) {
    return ClassUtilities.getInstance(getInstanceInJavaVMForField(field), cls);
  }

  public <E> E getInstanceInJavaVMForExpression(Expression expression, Class<E> cls) {
    return ClassUtilities.getInstance(getInstanceInJavaVMForExpression(expression), cls);
  }

  public <T extends EntityImp> T getImplementation(AbstractField field) {
    if (field == null) {
      return null;
    }
    SThing entity = getInstanceInJavaVMForField(field, SThing.class);
    if (entity != null) {
      return EmployeesOnly.getImplementation(entity);
    } else {
      return null;
    }
  }

  public UserField getSelectedField() {
    return this.selectedField;
  }

  public void setSelectedField(UserType<?> declaringType, UserField field) {
    assert (declaringType == this.getActiveSceneType()) || (field == this.getActiveSceneField());
    this.selectedField = field;
  }

  public void executeStatements(Statement... statements) {
    for (Statement statement : statements) {
      this.getVirtualMachine().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_executeStatement(this.getActiveSceneInstance(), statement);
    }
  }

  public void addField(UserType<?> declaringType, UserField field, int index, Statement... statements) {
    assert declaringType == this.getActiveSceneType() : declaringType;
    this.getVirtualMachine().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_initializeField(this.getActiveSceneInstance(), field);
    SProgram program = this.getProgramInstanceInJava();
    double prevSimulationSpeedFactor = program.getSimulationSpeedFactor();
    program.setSimulationSpeedFactor(Double.POSITIVE_INFINITY);
    try {
      this.executeStatements(statements);
    } finally {
      program.setSimulationSpeedFactor(prevSimulationSpeedFactor);
    }
    this.getActiveSceneType().fields.add(index, field);
    AstEventManager.fireTypeHierarchyListeners();
    this.setSelectedField(declaringType, field);

  }

  public abstract Statement getCurrentStateCodeForField(UserField field);

  public void setFieldToState(UserField field, final Statement... statements) {
    new ComponentExecutor(new Runnable() {
      @Override
      public void run() {
        executeStatements(statements);
      }
    }, "SetFieldToState(" + field.getName() + ")").start();

  }

  public void revertFieldToInitialState(UserField field) {
    Statement code = this.getInitialCodeforField(field);
    if (code == null) {
      Logger.severe("No initial state code found for field " + field);
      return;
    }
    this.setFieldToState(field, code);
  }

  public void removeField(UserType<?> declaringType, UserField field, Statement... statements) {
    assert declaringType == this.getActiveSceneType() : declaringType + " " + field;
    for (int i = 0; i < this.getActiveSceneType().fields.size(); i++) {
      if (this.getActiveSceneType().fields.get(i) == field) {
        this.getActiveSceneType().fields.remove(i);
        break;
      }
    }
    this.executeStatements(statements);
    if (this.selectedField == field) {
      UserField uf = this.getActiveSceneField();
      this.setSelectedField(uf.getDeclaringType(), uf);
    }
  }

  public NamedUserType getActiveSceneType() {
    UserField field = this.getActiveSceneField();
    if (field != null) {
      AbstractType<?, ?, ?> type = field.getValueType();
      if (type instanceof NamedUserType) {
        return (NamedUserType) type;
      }
    }
    return null;
  }

  public UserInstance getActiveSceneInstance() {
    return this.mapSceneFieldToInstance.get(this.getActiveSceneField());
  }

  public <T extends EntityImp> T getActiveSceneImplementation() {
    SThing entity = getInstanceInJavaVMForField(getActiveSceneField(), SThing.class);
    if (entity != null) {
      return EmployeesOnly.getImplementation(entity);
    } else {
      return null;
    }
  }

  public final VirtualMachine getVirtualMachine() {
    if (this.vm != null) {
      //pass
    } else {
      this.vm = IDE.getActiveInstance().createRegisteredVirtualMachineForSceneEditor();
    }
    return this.vm;
  }

  private void addScene(UserField sceneField) {
    Object sceneObject = programInstance.getFieldValue(sceneField);
    assert sceneObject != null;
    assert sceneObject instanceof UserInstance;
    UserInstance sceneInstance = (UserInstance) sceneObject;
    sceneInstance.ensureInverseMapExists();
    mapSceneFieldToInstance.put(sceneField, sceneInstance);
    sceneFieldListSelectionState.addItem(sceneField);
  }

  protected void setActiveScene(UserField sceneField) {
    //note: added by dennisc
    this.initializeIfNecessary();
    //

    this.sceneFieldListSelectionState.setValueTransactionlessly(sceneField);

    //Run the "setActiveScene" call on the program to get the active scene set in the right state
    //    edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice sceneAliceInstance = getActiveSceneInstance();
    //    org.lookingglassandalice.storytelling.Scene sceneJavaInstance = (org.lookingglassandalice.storytelling.Scene)sceneAliceInstance.getInstanceInJava();
    //    getProgramInstanceInJava().setActiveScene(sceneJavaInstance);
  }

  protected UserInstance getUserProgramInstance() {
    return this.programInstance;
  }

  protected SProgram getProgramInstanceInJava() {
    return (SProgram) this.programInstance.getJavaInstance();
  }

  protected void setProgramInstance(UserInstance programInstance) {
    this.programInstance = programInstance;
  }

  protected UserInstance createProgramInstance() {
    return getVirtualMachine().ENTRY_POINT_createInstance(this.programType);
  }

  protected void setProgramType(NamedUserType programType) {
    if (this.programType != programType) {
      if (this.programType != null) {
        this.sceneFieldListSelectionState.removeNewSchoolValueListener(this.selectedSceneObserver);
        this.sceneFieldListSelectionState.clear();
      }
      this.programType = programType;
      mapSceneFieldToInstance.clear();
      if (this.programType != null) {
        setProgramInstance(this.createProgramInstance());
        for (AbstractField programField : this.programType.getDeclaredFields()) {
          if (programField.getValueType().isAssignableTo(SScene.class)) {
            this.addScene((UserField) programField);
          }
        }
      } else {
        setProgramInstance(null);
      }
      if (this.sceneFieldListSelectionState.getItemCount() > 0) {
        this.sceneFieldListSelectionState.setSelectedIndex(0);
      }
      this.sceneFieldListSelectionState.addAndInvokeNewSchoolValueListener(this.selectedSceneObserver);
    }
  }

  protected void handleProjectOpened(Project nextProject) {
    AbstractSceneEditor.this.setProgramType(nextProject.getProgramType());
    AbstractSceneEditor.this.revalidateAndRepaint();
  }

  //  @Override
  //  protected void handleDisplayable() {
  //    super.handleDisplayable();
  //    initializeIfNecessary();
  //  }

  private boolean EPIC_HACK_isFirstAddedTo = true;

  @Override
  protected void handleAddedTo(AwtComponentView<?> parent) {
    if (EPIC_HACK_isFirstAddedTo) {
      ProjectDocument projectDocument = ProjectApplication.getActiveInstance().getDocumentFrame().getDocument();
      if (projectDocument != null) {
        this.projectListener.changed(null, null, projectDocument);
        Logger.todo("remove firing changed", projectDocument);
      }
      EPIC_HACK_isFirstAddedTo = false;
    }
    this.initializeIfNecessary();
    ProjectDocumentState.getInstance().addValueListener(this.projectListener);
    super.handleAddedTo(parent);
  }

  @Override
  protected void handleRemovedFrom(AwtComponentView<?> parent) {
    super.handleRemovedFrom(parent);
    ProjectDocumentState.getInstance().removeValueListener(this.projectListener);
  }

}
