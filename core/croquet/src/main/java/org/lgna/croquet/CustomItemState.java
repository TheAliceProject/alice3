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

import edu.cmu.cs.dennisc.java.util.Lists;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.cascade.RtRoot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class CustomItemState<T> extends ItemState<T> {
  public static class InternalRoot<T> extends CascadeRoot<T, CustomItemState<T>> {
    private final CustomItemState<T> state;

    private InternalRoot(CustomItemState<T> state) {
      super(UUID.fromString("8a973789-9896-443f-b701-4a819fc61d46"));
      this.state = state;
    }

    @Override
    public List<? extends CascadeBlank<T>> getBlanks() {
      return this.state.getBlanks();
    }

    @Override
    public Class<T> getComponentType() {
      return this.state.getItemCodec().getValueClass();
    }

    @Override
    public CustomItemState<T> getCompletionModel() {
      return this.state;
    }

    @Override
    protected void prologue() {
      super.prologue();
      this.state.prologue();
    }

    @Override
    protected void epilogue() {
      this.state.epilogue();
      super.epilogue();
    }

    @Override
    public void handleCompletion(UserActivity userActivity, RtRoot<T, CustomItemState<T>> rtRoot) {
      recordCompletionModel(userActivity);
      T[] values = rtRoot.createValues(getComponentType());
      state.changeValueFromIndirectModel(values[0], userActivity);
      getPopupPrepModel().handleFinally();
    }
  }

  private final InternalRoot<T> root;

  public CustomItemState(Group group, UUID id, T initialValue, ItemCodec<T> itemCodec) {
    super(group, id, initialValue, itemCodec);
    this.root = new InternalRoot<T>(this);
  }

  protected abstract List<? extends CascadeBlank<T>> getBlanks();

  public InternalRoot<T> getCascadeRoot() {
    return this.root;
  }

  protected void appendPrepModelsToCascadeRootPath(List<PrepModel> cascadeRootPath, Edit edit) {
  }

  @Override
  public final List<List<PrepModel>> getPotentialPrepModelPaths(Edit edit) {
    PrepModel rootPrepModel = this.root.getPopupPrepModel();
    ArrayList<PrepModel> cascadeRootPath = Lists.newArrayList(rootPrepModel);
    List<List<PrepModel>> rv = Lists.newArrayList();
    rv.add(cascadeRootPath);
    this.appendPrepModelsToCascadeRootPath(cascadeRootPath, edit);
    return rv;
  }

  @Override
  protected void localize() {
  }

  protected void prologue() {
  }

  protected void epilogue() {
  }

  @Override
  public boolean isEnabled() {
    return this.getCascadeRoot().getPopupPrepModel().isEnabled();
  }

  @Override
  public void setEnabled(boolean isEnabled) {
    this.getCascadeRoot().getPopupPrepModel().setEnabled(isEnabled);
  }
}
