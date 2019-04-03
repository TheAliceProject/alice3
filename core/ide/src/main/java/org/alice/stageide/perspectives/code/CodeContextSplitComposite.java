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

package org.alice.stageide.perspectives.code;

import org.alice.ide.IDE;
import org.alice.ide.ProjectDocumentFrame;
import org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.members.MembersComposite;
import org.alice.ide.sceneeditor.SceneComposite;
import org.alice.ide.typehierarchy.TypeHierarchyComposite;
import org.alice.stageide.run.RunComposite;
import org.alice.stageide.typecontext.NonSceneTypeComposite;
import org.lgna.croquet.CardOwnerComposite;
import org.lgna.croquet.Composite;
import org.lgna.croquet.ImmutableSplitComposite;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.SplitPane;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.SScene;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class CodeContextSplitComposite extends ImmutableSplitComposite {
  public CodeContextSplitComposite(CodePerspectiveComposite codePerspectiveComposite) {
    super(UUID.fromString("c3336f34-9da4-4aaf-86ff-d742f4717d94"));
    this.codePerspectiveComposite = codePerspectiveComposite;
    this.sceneOrNonSceneComposite = this.createCardOwnerCompositeButDoNotRegister(this.sceneComposite, this.nonSceneTypeComposite);
    this.typeOrCodeCardOwnerComposite = this.createCardOwnerCompositeButDoNotRegister(this.typeHierarchyComposite, MembersComposite.getInstance());
  }

  @Override
  public Composite<?> getLeadingComposite() {
    return this.sceneOrNonSceneComposite;
  }

  @Override
  public Composite<?> getTrailingComposite() {
    return this.typeOrCodeCardOwnerComposite;
  }

  private void handleTypeStateChanged(NamedUserType nextValue) {
    Composite<?> composite;
    if (nextValue != null) {
      if (nextValue.isAssignableTo(SScene.class)) {
        composite = this.sceneComposite;
      } else {
        composite = this.nonSceneTypeComposite;
      }
      //      org.lgna.croquet.components.JComponent< ? > view = key.getView();
      //      if( view instanceof NonSceneTypeView ) {
      //        NonSceneTypeView nonSceneTypeView = (NonSceneTypeView)view;
      //        nonSceneTypeView.handlePreShow();
      //      }
    } else {
      composite = null;
    }
    this.sceneOrNonSceneComposite.showCard(composite);
  }

  private void handleDeclarationStateChanged(DeclarationComposite nextValue) {
    Composite<?> composite;
    if (IsEmphasizingClassesState.getInstance().getValue()) {
      if (nextValue != null) {
        if (nextValue.getDeclaration() instanceof AbstractType) {
          composite = this.typeHierarchyComposite;
        } else {
          composite = MembersComposite.getInstance();
        }
      } else {
        composite = null;
      }
    } else {
      composite = MembersComposite.getInstance();
    }
    this.typeOrCodeCardOwnerComposite.showCard(composite);
  }

  public void incrementIgnoreDividerLocationChangeCount() {
    this.ignoreDividerChangeCount++;
  }

  public void decrementIgnoreDividerLocationChangeCount() {
    this.ignoreDividerChangeCount--;
  }

  @Override
  protected SplitPane createView() {
    SplitPane rv = this.createVerticalSplitPane();
    rv.addDividerLocationChangeListener(this.dividerLocationListener);
    rv.setResizeWeight(0.0);
    return rv;
  }

  @Override
  public void handlePreActivation() {
    super.handlePreActivation();
    ProjectDocumentFrame projectDocumentFrame = IDE.getActiveInstance().getDocumentFrame();
    projectDocumentFrame.getTypeMetaState().addAndInvokeValueListener(this.typeListener);
    projectDocumentFrame.getDeclarationsEditorComposite().getTabState().addAndInvokeNewSchoolValueListener(this.declarationListener);
  }

  @Override
  public void handlePostDeactivation() {
    ProjectDocumentFrame projectDocumentFrame = IDE.getActiveInstance().getDocumentFrame();
    projectDocumentFrame.getDeclarationsEditorComposite().getTabState().removeNewSchoolValueListener(this.declarationListener);
    projectDocumentFrame.getTypeMetaState().removeValueListener(this.typeListener);
    super.handlePostDeactivation();
  }

  private final ValueListener<NamedUserType> typeListener = new ValueListener<NamedUserType>() {
    @Override
    public void valueChanged(ValueEvent<NamedUserType> e) {
      CodeContextSplitComposite.this.handleTypeStateChanged(e.getNextValue());
    }
  };
  private final ValueListener<DeclarationComposite<?, ?>> declarationListener = new ValueListener<DeclarationComposite<?, ?>>() {
    @Override
    public void valueChanged(ValueEvent<DeclarationComposite<?, ?>> e) {
      CodeContextSplitComposite.this.handleDeclarationStateChanged(e.getNextValue());
    }
  };

  private final PropertyChangeListener dividerLocationListener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent e) {
      if (ignoreDividerChangeCount > 0) {
        //pass
      } else {
        SplitPane otherSplitPane = codePerspectiveComposite.getView();
        int prevValue = otherSplitPane.getDividerLocation();
        int nextValue = (int) ((Integer) e.getNewValue() * RunComposite.WIDTH_TO_HEIGHT_RATIO);
        if (prevValue != nextValue) {
          codePerspectiveComposite.incrementIgnoreDividerLocationChangeCount();
          try {
            otherSplitPane.setDividerLocation(nextValue);
          } finally {
            codePerspectiveComposite.decrementIgnoreDividerLocationChangeCount();
          }
        }
        //edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "inner:", e.getOldValue(), e.getNewValue() );
      }
    }
  };

  private final CodePerspectiveComposite codePerspectiveComposite;
  private final CardOwnerComposite sceneOrNonSceneComposite;
  private final CardOwnerComposite typeOrCodeCardOwnerComposite;
  private int ignoreDividerChangeCount = 0;

  private final SceneComposite sceneComposite = new SceneComposite();
  private final NonSceneTypeComposite nonSceneTypeComposite = new NonSceneTypeComposite();

  private final TypeHierarchyComposite typeHierarchyComposite = new TypeHierarchyComposite();
}
