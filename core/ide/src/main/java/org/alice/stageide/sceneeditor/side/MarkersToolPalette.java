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
package org.alice.stageide.sceneeditor.side;

import edu.cmu.cs.dennisc.property.event.ListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter;
import org.alice.stageide.project.SceneTypeMetaState;
import org.alice.stageide.sceneeditor.side.views.MarkersView;
import org.lgna.croquet.Operation;
import org.lgna.croquet.RefreshableDataSingleSelectListState;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.UserField;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class MarkersToolPalette extends SideToolPalette<MarkersView> {
  private final RefreshableDataSingleSelectListState<UserField> markerListState;

  private NamedUserType sceneType = null;

  private final SimplifiedListPropertyAdapter<UserField> sceneTypeFieldsListener = new SimplifiedListPropertyAdapter<UserField>() {
    @Override
    protected void changed(ListPropertyEvent<UserField> e) {
      updateTitle();
    }
  };

  private final ValueListener<NamedUserType> sceneTypeListener = new ValueListener<NamedUserType>() {
    @Override
    public void valueChanged(ValueEvent<NamedUserType> e) {
      if (sceneType != null) {
        sceneType.fields.removeListPropertyListener(sceneTypeFieldsListener);
      }
      sceneType = e.getNextValue();
      NodeListProperty<UserField> fieldProperty;
      if (sceneType != null) {
        fieldProperty = sceneType.fields;
      } else {
        fieldProperty = null;
      }

      ((MarkerFieldData) markerListState.getData()).setListProperty(fieldProperty);
      updateTitle();

      if (sceneType != null) {
        sceneType.fields.addListPropertyListener(sceneTypeFieldsListener);
      }
    }
  };

  public MarkersToolPalette(UUID migrationId, MarkerFieldData markerFieldData) {
    super(migrationId, false);
    this.markerListState = this.createRefreshableListState("markerListState", markerFieldData, -1);
    SceneTypeMetaState.getInstance().addAndInvokeValueListener(this.sceneTypeListener);
  }

  public abstract Operation getMoveMarkerToOperation();

  public abstract Operation getMoveToMarkerOperation();

  public RefreshableDataSingleSelectListState<UserField> getMarkerListState() {
    return this.markerListState;
  }

  public abstract Operation getAddOperation();

  @Override
  protected MarkersView createView() {
    return new MarkersView(this);
  }

  @Override
  protected String modifyTextIfNecessary(String text, boolean isExpanded) {
    text = super.modifyTextIfNecessary(text, isExpanded);
    text += " (" + this.markerListState.getItemCount() + ")";
    return text;
  }

  private void updateTitle() {
    this.getOuterComposite().getIsExpandedState().updateNameAndIcon();
  }
}
