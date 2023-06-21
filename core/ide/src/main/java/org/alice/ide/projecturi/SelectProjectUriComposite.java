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

package org.alice.ide.projecturi;

import org.alice.ide.projecturi.views.TabContentPanel;
import org.alice.ide.uricontent.UriProjectLoader;
import org.lgna.croquet.ImmutableDataTabState;
import org.lgna.croquet.SingleValueCreatorInputDialogCoreComposite;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.ComponentManager;
import org.lgna.croquet.views.Dialog;
import org.lgna.croquet.views.Panel;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class SelectProjectUriComposite extends SingleValueCreatorInputDialogCoreComposite<Panel, UriProjectLoader> {
  private final ErrorStatus noSelectionError = this.createErrorStatus("noSelectionError");
  private final StartersTab startersTab = new StartersTab();
  private final TemplatesTab templatesTab = new TemplatesTab();
  private final MyProjectsTab myProjectsTab = new MyProjectsTab();
  private final RecentProjectsTab recentProjectsTab = new RecentProjectsTab();
  private final FileSystemTab fileSystemTab = new FileSystemTab();
  private final ImmutableDataTabState<SelectUriTab> tabState = this.createImmutableTabState("tabState", -1, SelectUriTab.class, this.templatesTab, this.startersTab, this.myProjectsTab, this.recentProjectsTab, this.fileSystemTab);

  private UriProjectLoader getSelectedProject() {
    SelectUriTab tab = tabState.getValue();
    return tab != null ? tab.getSelectedUri() : null;
  }

  private static class SingletonHolder {
    private static SelectProjectUriComposite instance = new SelectProjectUriComposite();
  }

  public static SelectProjectUriComposite getInstance() {
    return SingletonHolder.instance;
  }

  private SelectProjectUriComposite() {
    super(UUID.fromString("3b9ec3fb-3fe5-485c-ac2a-688a5468b0b9"));
  }

  public ImmutableDataTabState<SelectUriTab> getTabState() {
    return this.tabState;
  }

  @Override
  protected UriProjectLoader createValue() {
    return getSelectedProject();
  }

  @Override
  protected Panel createView() {
    BorderPanel view = new BorderPanel(this);
    view.addCenterComponent(getTabState().createFolderTabbedPane());
    view.setBackgroundColor(TabContentPanel.DEFAULT_BACKGROUND_COLOR);
    return view;
  }

  @Override
  protected Status getStatusPreRejectorCheck() {
    return getSelectedProject() != null ? IS_GOOD_TO_GO_STATUS : this.noSelectionError;
  }

  @Override
  protected Integer getWiderGoldenRatioSizeFromWidth() {
    return 800;
  }

  public void selectAppropriateTab(boolean isNew) {
    SelectUriTab tab;
    if (isNew) {
      //tab = this.startersTab != null ? this.startersTab : this.templatesTab;
      tab = this.templatesTab;
    } else {
      tab = this.myProjectsTab; // todo: recentTab?
    }
    this.tabState.setValueTransactionlessly(tab);
    ComponentManager.revalidateAndRepaintAllComponents(this.tabState);
  }

  private void refresh() {
    for (SelectUriTab contentTab : this.tabState) {
      contentTab.refresh();
    }
  }

  @Override
  protected void handlePreShowDialog(Dialog dialog) {
    this.refresh();
    super.handlePreShowDialog(dialog);
  }

  @Override
  protected void refreshStatus() {
    super.refreshStatus();
    tabState.getValue().updateVrCheckbox();
  }
}
