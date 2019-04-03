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

import edu.cmu.cs.dennisc.java.lang.ThreadUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.ThisFieldAccessFactory;
import org.alice.ide.instancefactory.croquet.InstanceFactoryFillIn;
import org.alice.ide.instancefactory.croquet.InstanceFactoryState;
import org.alice.ide.properties.adapter.croquet.edits.PropertyValueEdit;
import org.alice.stageide.sceneeditor.interact.croquet.AbstractFieldBasedManipulationActionOperation;
import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.Group;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.edits.StateEdit;
import org.lgna.croquet.history.event.EditCommittedEvent;
import org.lgna.croquet.history.event.ActivityEvent;
import org.lgna.croquet.history.event.Listener;
import org.lgna.croquet.undo.UndoHistory;
import org.lgna.croquet.views.ComponentManager;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.UserField;

import java.util.Map;

/**
 * @author Kyle J. Harms
 */
public class ProjectHistoryManager {

  private Listener listener;
  private Map<Group, UndoHistory> map;

  public ProjectHistoryManager(ProjectDocument projectDocument) {
    this.listener = new Listener() {
      @Override
      public void changed(ActivityEvent e) {
        if (e instanceof EditCommittedEvent) {
          EditCommittedEvent editCommittedEvent = (EditCommittedEvent) e;
          ProjectHistoryManager.this.handleEditCommitted(editCommittedEvent.getEdit());
        }
      }
    };
    projectDocument.getUserActivity().addListener(this.listener);
    this.map = Maps.newHashMap();
  }

  public UndoHistory getGroupHistory(Group group) {
    UndoHistory rv;
    if (group != null) {
      rv = this.map.get(group);
      if (rv != null) {
        //pass
      } else {
        rv = new UndoHistory(group);
        this.map.put(group, rv);
      }
    } else {
      Logger.warning("group==null");
      rv = null;
    }
    return rv;
  }

  private static int IS_POSSIBLY_OPENING_SCENE = 0x1;
  private static int IS_ANIMATED = 0x2;
  private static int IS_POSSIBLY_OPENING_SCENE_AND_ANIMATED = IS_POSSIBLY_OPENING_SCENE | IS_ANIMATED;

  private int isPossiblyOpeningSceneEdit(Edit edit) {
    if (edit instanceof PropertyValueEdit) {
      return IS_POSSIBLY_OPENING_SCENE_AND_ANIMATED;
    }

    if (edit instanceof StateEdit<?>) {
      StateEdit<?> stateEdit = (StateEdit<?>) edit;
      if (stateEdit.getGroup() == IDE.PROJECT_GROUP) {
        return IS_POSSIBLY_OPENING_SCENE_AND_ANIMATED;
      }
    }

    CompletionModel completionModel = ((AbstractEdit<?>) edit).getModel();
    if (completionModel instanceof AbstractFieldBasedManipulationActionOperation) {
      return IS_POSSIBLY_OPENING_SCENE;
    }

    return 0;
  }

  private void markDirty(ProjectDocumentFrame projectDocumentFrame, ThisFieldAccessFactory thisFieldAccessFactory) {
    UserField userField = thisFieldAccessFactory.getField();
    projectDocumentFrame.getIconFactoryManager().markIconFactoryForFieldDirty(userField);
    InstanceFactoryFillIn.getInstance(thisFieldAccessFactory).markDirty();
    for (SwingComponentView<?> component : ComponentManager.getComponents(projectDocumentFrame.getInstanceFactoryState().getCascadeRoot().getPopupPrepModel())) {
      //note: rendering artifact for faux combo boxes when only invoking repaint.
      //component.repaint();
      component.revalidateAndRepaint();
    }
  }

  private void handleEditCommitted(Edit edit) {
    assert edit != null;
    UndoHistory projectHistory = this.getGroupHistory(edit.getGroup());
    if (projectHistory != null) {
      projectHistory.push(edit);
    }

    int value = this.isPossiblyOpeningSceneEdit(edit);
    if ((value & IS_POSSIBLY_OPENING_SCENE) != 0) {
      final ProjectDocumentFrame projectDocumentFrame = IDE.getActiveInstance().getDocumentFrame();
      if (projectDocumentFrame != null) {
        final InstanceFactoryState instanceFactoryState = projectDocumentFrame.getInstanceFactoryState();
        InstanceFactory instanceFactory = instanceFactoryState.getValue();
        if (instanceFactory instanceof ThisFieldAccessFactory) {
          final ThisFieldAccessFactory thisFieldAccessFactory = (ThisFieldAccessFactory) instanceFactory;
          if ((value & IS_ANIMATED) != 0) {
            new Thread() {
              @Override
              public void run() {
                ThreadUtilities.sleep(1100);
                markDirty(projectDocumentFrame, thisFieldAccessFactory);
              }
            }.start();
          } else {
            this.markDirty(projectDocumentFrame, thisFieldAccessFactory);
          }
        }
      }
    }
  }
}
