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
package org.alice.ide;

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.util.DStack;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Stacks;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Lazy;
import org.alice.ide.capture.ImageCaptureComposite;
import org.alice.ide.croquet.models.AliceMenuBar;
import org.alice.ide.croquet.models.history.RedoOperation;
import org.alice.ide.croquet.models.history.UndoOperation;
import org.alice.ide.croquet.models.project.find.croquet.FindComposite;
import org.alice.ide.croquet.models.project.stats.croquet.StatisticsFrameComposite;
import org.alice.ide.croquet.models.projecturi.NewProjectOperation;
import org.alice.ide.croquet.models.projecturi.OpenProjectOperation;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.DeclarationTabState;
import org.alice.ide.declarationseditor.DeclarationsEditorComposite;
import org.alice.ide.formatter.Formatter;
import org.alice.ide.highlight.IdeHighlightStencil;
import org.alice.ide.iconfactory.IconFactoryManager;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.croquet.InstanceFactoryState;
import org.alice.ide.perspectives.ProjectPerspective;
import org.alice.ide.perspectives.noproject.NoProjectPerspective;
import org.alice.ide.project.ProjectDocumentState;
import org.alice.ide.resource.manager.ResourceManagerComposite;
import org.alice.stageide.perspectives.CodePerspective;
import org.alice.stageide.perspectives.PerspectiveState;
import org.alice.stageide.perspectives.SetupScenePerspective;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.ItemState;
import org.lgna.croquet.Operation;
import org.lgna.croquet.PerspectiveDocumentFrame;
import org.lgna.croquet.State;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.imp.frame.LazyIsFrameShowingState;
import org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory;
import org.lgna.croquet.meta.MetaState;
import org.lgna.croquet.meta.StateTrackingMetaState;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.NamedUserType;

import javax.swing.JLayeredPane;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class ProjectDocumentFrame extends PerspectiveDocumentFrame {
  public ProjectDocumentFrame(IdeConfiguration ideConfiguration, ApiConfigurationManager apiConfigurationManager) {
    this.apiConfigurationManager = apiConfigurationManager;

    this.noProjectPerspective = new NoProjectPerspective(this);
    AliceMenuBar aliceMenuBar = new AliceMenuBar(this);
    this.codePerspective = new CodePerspective(this, aliceMenuBar);
    this.setupScenePerspective = new SetupScenePerspective(this, aliceMenuBar);
    this.perspectiveState = new PerspectiveState(this.codePerspective, this.setupScenePerspective);

    this.metaDeclarationFauxState = new MetaDeclarationFauxState(this);
    this.instanceFactoryState = new InstanceFactoryState(this);
    this.findComposite = new FindComposite(this);
    this.iconFactoryManager = apiConfigurationManager.createIconFactoryManager();
  }

  private static final KeyStroke CAPTURE_ENTIRE_WINDOW_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_F12, InputEvent.SHIFT_MASK);
  private static final KeyStroke CAPTURE_ENTIRE_CONTENT_PANE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_F12, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK);
  private static final KeyStroke CAPTURE_RECTANGLE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);

  private void registerScreenCaptureKeyStrokes(AbstractWindow<?> window) {
    ImageCaptureComposite imageCaptureComposite = ImageCaptureComposite.getInstance();
    window.getContentPane().registerKeyboardAction(imageCaptureComposite.getCaptureEntireContentPaneOperation().getImp().getSwingModel().getAction(), CAPTURE_ENTIRE_CONTENT_PANE_KEY_STROKE, SwingComponentView.Condition.WHEN_IN_FOCUSED_WINDOW);
    window.getContentPane().registerKeyboardAction(imageCaptureComposite.getCaptureEntireWindowOperation().getImp().getSwingModel().getAction(), CAPTURE_ENTIRE_WINDOW_KEY_STROKE, SwingComponentView.Condition.WHEN_IN_FOCUSED_WINDOW);
    if (window == this.getFrame()) {
      //pass
    } else {
      window.getContentPane().registerKeyboardAction(imageCaptureComposite.getCaptureRectangleOperation().getImp().getSwingModel().getAction(), CAPTURE_RECTANGLE_KEY_STROKE, SwingComponentView.Condition.WHEN_IN_FOCUSED_WINDOW);
    }
  }

  private void unregisterScreenCaptureKeyStrokes(AbstractWindow<?> window) {
    window.getContentPane().unregisterKeyboardAction(CAPTURE_ENTIRE_WINDOW_KEY_STROKE);
    window.getContentPane().unregisterKeyboardAction(CAPTURE_ENTIRE_CONTENT_PANE_KEY_STROKE);
    window.getContentPane().unregisterKeyboardAction(CAPTURE_RECTANGLE_KEY_STROKE);
  }

  /*package-private*/void initialize() {
    this.registerScreenCaptureKeyStrokes(this.getFrame());
    this.getInstanceFactoryState().addAndInvokeNewSchoolValueListener(this.instanceFactoryListener);
    FormatterState.getInstance().addNewSchoolValueListener(this.formatterListener);
  }

  @Override
  public void pushWindow(final AbstractWindow<?> window) {
    this.registerScreenCaptureKeyStrokes(window);
    super.pushWindow(window);
  }

  @Override
  public AbstractWindow<?> popWindow() {
    AbstractWindow<?> window = super.popWindow();
    this.unregisterScreenCaptureKeyStrokes(window);
    return window;
  }

  public void disableRendering(ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering) {
    this.stack.push(reasonToDisableSomeAmountOfRendering);
    StorytellingSceneEditor.getInstance().disableRendering(reasonToDisableSomeAmountOfRendering);
  }

  public void enableRendering() {
    if (this.stack.isEmpty()) {
      Logger.severe(this);
    } else {
      ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering = this.stack.pop();
      StorytellingSceneEditor.getInstance().enableRendering(reasonToDisableSomeAmountOfRendering);
    }
  }

  @Override
  public ProjectDocument getDocument() {
    return ProjectDocumentState.getInstance().getValue();
  }

  public ApiConfigurationManager getApiConfigurationManager() {
    return this.apiConfigurationManager;
  }

  public InstanceFactoryState getInstanceFactoryState() {
    return this.instanceFactoryState;
  }

  public MetaDeclarationFauxState getMetaDeclarationFauxState() {
    return this.metaDeclarationFauxState;
  }

  public NoProjectPerspective getNoProjectPerspective() {
    return this.noProjectPerspective;
  }

  public CodePerspective getCodePerspective() {
    return this.codePerspective;
  }

  public SetupScenePerspective getSetupScenePerspective() {
    return this.setupScenePerspective;
  }

  public ItemState<ProjectPerspective> getPerspectiveState() {
    return this.perspectiveState;
  }

  public boolean isInCodePerspective() {
    return this.getPerspectiveState().getValue() == this.getCodePerspective();
  }

  public boolean isInSetupScenePerspective() {
    return this.getPerspectiveState().getValue() == this.getSetupScenePerspective();
  }

  public Operation getSetToCodePerspectiveOperation() {
    return this.getPerspectiveState().getItemSelectionOperation(this.getCodePerspective());
  }

  public Operation getSetToSetupScenePerspectiveOperation() {
    return this.getPerspectiveState().getItemSelectionOperation(this.getSetupScenePerspective());
  }

  public void setToCodePerspectiveTransactionlessly() {
    this.getPerspectiveState().setValueTransactionlessly(this.getCodePerspective());
  }

  public void setToSetupScenePerspectiveTransactionlessly() {
    this.getPerspectiveState().setValueTransactionlessly(this.getSetupScenePerspective());
  }

  public FindComposite getFindComposite() {
    return this.findComposite;
  }

  public MetaState<NamedUserType> getTypeMetaState() {
    if (this.typeMetaState != null) {
      //pass
    } else {
      DeclarationTabState declarationTabState = this.declarationsEditorComposite.getTabState();
      this.typeMetaState = new StateTrackingMetaState<NamedUserType, DeclarationComposite<?, ?>>(declarationTabState) {
        @Override
        protected NamedUserType getValue(State<DeclarationComposite<?, ?>> state) {
          DeclarationComposite<?, ?> declarationComposite = state.getValue();
          if (declarationComposite != null) {
            return ClassUtilities.getInstance(declarationComposite.getType(), NamedUserType.class);
          } else {
            return null;
          }
        }
      };
    }
    return this.typeMetaState;
  }

  public IconFactoryManager getIconFactoryManager() {
    return this.iconFactoryManager;
  }

  public DeclarationsEditorComposite getDeclarationsEditorComposite() {
    return this.declarationsEditorComposite;
  }

  public Operation getResourcesDialogLaunchOperation() {
    return this.resourcesDialogLaunchOperation;
  }

  public BooleanState getStatisticsFrameIsShowingState() {
    return this.stasticsFrameIsShowingState;
  }

  private static final Integer HIGHLIGHT_STENCIL_LAYER = JLayeredPane.POPUP_LAYER - 2;

  public IdeHighlightStencil getHighlightStencil() {
    if (this.highlightStencil != null) {
      //pass
    } else {
      this.highlightStencil = new IdeHighlightStencil(this.getFrame(), HIGHLIGHT_STENCIL_LAYER);
    }
    return this.highlightStencil;
  }

  public AbstractCode getFocusedCode() {
    AbstractDeclaration declaration = this.getMetaDeclarationFauxState().getValue();
    if (declaration instanceof AbstractCode) {
      return (AbstractCode) declaration;
    } else {
      return null;
    }
  }

  public void setFocusedCode(AbstractCode nextFocusedCode) {
    this.selectDeclaration(nextFocusedCode);
  }

  public void selectDeclarationComposite(DeclarationComposite declarationComposite) {
    if (declarationComposite != null) {
      AbstractDeclaration declaration = declarationComposite.getDeclaration();
      //      org.lgna.project.ast.AbstractType<?, ?, ?> type;
      //      if( declaration instanceof org.lgna.project.ast.AbstractType<?, ?, ?> ) {
      //        type = (org.lgna.project.ast.AbstractType<?, ?, ?>)declaration;
      //      } else if( declaration instanceof org.lgna.project.ast.AbstractCode ) {
      //        org.lgna.project.ast.AbstractCode code = (org.lgna.project.ast.AbstractCode)declaration;
      //        type = code.getDeclaringType();
      //      } else {
      //        type = null;
      //      }
      //      if( type instanceof org.lgna.project.ast.NamedUserType ) {
      //        org.alice.ide.declarationseditor.TypeState.getInstance().setValueTransactionlessly( (org.lgna.project.ast.NamedUserType)type );
      //      }
      DeclarationTabState tabState = this.getDeclarationsEditorComposite().getTabState();
      //      if( tabState.containsItem( declarationComposite ) ) {
      //        //pass
      //      } else {
      //        tabState.addItem( declarationComposite );
      //      }
      tabState.setValueTransactionlessly(declarationComposite);
    }
  }

  private void selectDeclaration(AbstractDeclaration declaration) {
    this.selectDeclarationComposite(DeclarationComposite.getInstance(declaration));
  }

  public Operation getNewProjectOperation() {
    return this.newProjectOperation;
  }

  public Operation getOpenProjectOperation() {
    return this.openProjectOperation;
  }

  public Operation getUndoOperation() {
    return this.undoOperation;
  }

  public Operation getRedoOperation() {
    return this.redoOperation;
  }

  private final ApiConfigurationManager apiConfigurationManager;

  private MetaState<NamedUserType> typeMetaState;

  private final FindComposite findComposite;

  private final NoProjectPerspective noProjectPerspective;
  private final CodePerspective codePerspective;
  private final SetupScenePerspective setupScenePerspective;

  private final PerspectiveState perspectiveState;

  private final MetaDeclarationFauxState metaDeclarationFauxState;

  private final InstanceFactoryState instanceFactoryState;

  private final IconFactoryManager iconFactoryManager;

  private final DeclarationsEditorComposite declarationsEditorComposite = new DeclarationsEditorComposite();

  private final Operation resourcesDialogLaunchOperation = LazySimpleLaunchOperationFactory.createInstance(ResourceManagerComposite.class, new Lazy<ResourceManagerComposite>() {
    @Override
    protected ResourceManagerComposite create() {
      return new ResourceManagerComposite(ProjectDocumentFrame.this);
    }
  }, Application.DOCUMENT_UI_GROUP).getLaunchOperation();

  private final BooleanState stasticsFrameIsShowingState = LazyIsFrameShowingState.createInstance(Application.INFORMATION_GROUP, StatisticsFrameComposite.class, new Lazy<StatisticsFrameComposite>() {
    @Override
    protected StatisticsFrameComposite create() {
      return new StatisticsFrameComposite(ProjectDocumentFrame.this);
    }
  });
  private final DStack<ReasonToDisableSomeAmountOfRendering> stack = Stacks.newStack();

  private final Map<AbstractCode, InstanceFactory> mapCodeToInstanceFactory = Maps.newHashMap();
  private final ValueListener<InstanceFactory> instanceFactoryListener = new ValueListener<InstanceFactory>() {
    @Override
    public void valueChanged(ValueEvent<InstanceFactory> e) {
      InstanceFactory nextValue = e.getNextValue();
      if (nextValue != null) {
        AbstractCode code = getFocusedCode();
        if (code != null) {
          mapCodeToInstanceFactory.put(code, nextValue);
        }
      }
    }
  };

  private final ValueListener<Formatter> formatterListener = new ValueListener<Formatter>() {
    @Override
    public void valueChanged(ValueEvent<Formatter> e) {
      ComponentUtilities.revalidateTree(getFrame().getAwtComponent());
    }
  };

  private IdeHighlightStencil highlightStencil;

  private final Operation newProjectOperation = new NewProjectOperation();
  private final Operation openProjectOperation = new OpenProjectOperation();

  private final Operation undoOperation = new UndoOperation(this);
  private final Operation redoOperation = new RedoOperation(this);
}
