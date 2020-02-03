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
package org.alice.ide.recentprojects;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.croquet.models.projecturi.OpenRecentProjectOperation;
import org.lgna.croquet.MenuModel;
import org.lgna.croquet.StandardMenuItemPrepModel;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.AwtContainerView;
import org.lgna.croquet.views.CascadeMenu;
import org.lgna.croquet.views.CascadeMenuItem;
import org.lgna.croquet.views.CheckBoxMenuItem;
import org.lgna.croquet.views.Menu;
import org.lgna.croquet.views.MenuItem;
import org.lgna.croquet.views.MenuItemContainer;
import org.lgna.croquet.views.MenuItemContainerUtilities;
import org.lgna.croquet.views.MenuTextSeparator;
import org.lgna.croquet.views.ViewController;

import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.Component;
import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class RecentProjectsMenuModel extends MenuModel {
  private static class SingletonHolder {
    private static RecentProjectsMenuModel instance = new RecentProjectsMenuModel();
  }

  public static RecentProjectsMenuModel getInstance() {
    return SingletonHolder.instance;
  }

  private RecentProjectsMenuModel() {
    super(UUID.fromString("0a39a07c-d23f-4cf8-a195-5d114b903505"));
  }

  private void setChildren(MenuItemContainer menuItemContainer) {
    IDE ide = IDE.getActiveInstance();
    URI currentUri = ide.getUri();
    URI[] uris = RecentProjectsListData.getInstance().toArray();
    List<StandardMenuItemPrepModel> models = Lists.newLinkedList();
    for (URI uri : uris) {
      if (Objects.equals(uri, currentUri)) {
        //pass
      } else {
        models.add(OpenRecentProjectOperation.getInstance(uri).getMenuItemPrepModel());
      }
    }
    if (models.size() == 0) {
      models.add(NoRecentUrisSeparatorModel.getInstance());
    }
    MenuItemContainerUtilities.setMenuElements(menuItemContainer, models);
  }

  //  @Override
  //  public org.lgna.croquet.components.Menu createMenu() {
  //    org.lgna.croquet.components.Menu rv = super.createMenu();
  //    this.addChildren( rv );
  //    edu.cmu.cs.dennisc.java.util.logging.Logger.testing( rv );
  //    return rv;
  //  }
  //  @Override
  //  public void handlePopupMenuPrologue( org.lgna.croquet.components.PopupMenu popupMenu, org.lgna.croquet.history.StandardPopupPrepStep step ) {
  //    super.handlePopupMenuPrologue( popupMenu, step );
  //    this.addChildren( popupMenu );
  //    edu.cmu.cs.dennisc.java.util.logging.Logger.testing( popupMenu );
  //  }
  @Override
  protected void handleShowing(MenuItemContainer menuItemContainer, PopupMenuEvent e) {
    Object src = e.getSource();
    if (src instanceof JPopupMenu) {
      final JPopupMenu jPopupMenu = (JPopupMenu) e.getSource();
      this.setChildren(new MenuItemContainer() {
        @Override
        public ViewController<?, ?> getViewController() {
          return null;
        }

        @Override
        public void addPopupMenuListener(PopupMenuListener listener) {
        }

        @Override
        public void removePopupMenuListener(PopupMenuListener listener) {
        }

        @Override
        public UserActivity getActivity() {
          return menuItemContainer.getActivity();
        }

        @Override
        public AwtComponentView<?> getMenuComponent(int i) {
          MenuElement menuElement = jPopupMenu.getSubElements()[i];
          if (menuElement instanceof Component) {
            Component awtComponent = (Component) menuElement;
            return AwtComponentView.lookup(awtComponent);
          } else {
            return null;
          }
        }

        @Override
        public int getMenuComponentCount() {
          return jPopupMenu.getSubElements().length;
        }

        @Override
        public synchronized AwtComponentView<?>[] getMenuComponents() {
          final int N = this.getMenuComponentCount();
          AwtComponentView<?>[] rv = new AwtComponentView<?>[N];
          for (int i = 0; i < N; i++) {
            rv[i] = this.getMenuComponent(i);
          }
          return rv;
        }

        @Override
        public AwtContainerView<?> getParent() {
          return null;
        }

        @Override
        public void addMenu(Menu menu) {
          jPopupMenu.add(menu.getAwtComponent());
        }

        @Override
        public void addMenuItem(MenuItem menuItem) {
          jPopupMenu.add(menuItem.getAwtComponent());
        }

        @Override
        public void addCascadeMenu(CascadeMenu cascadeMenu) {
        }

        @Override
        public void addCascadeMenuItem(CascadeMenuItem cascadeMenuItem) {
        }

        @Override
        public void addCascadeCombo(CascadeMenuItem cascadeMenuItem, CascadeMenu cascadeMenu) {
          this.addCascadeMenuItem(cascadeMenuItem);
          this.addCascadeMenu(cascadeMenu);
        }

        @Override
        public void addCheckBoxMenuItem(CheckBoxMenuItem checkBoxMenuItem) {
        }

        @Override
        public void addSeparator() {
          jPopupMenu.addSeparator();
        }

        @Override
        public void addSeparator(MenuTextSeparator menuTextSeparator) {
          jPopupMenu.add(menuTextSeparator.getAwtComponent());
        }

        @Override
        public void forgetAndRemoveAllMenuItems() {
          Logger.todo();
          this.removeAllMenuItems();
        }

        @Override
        public void removeAllMenuItems() {
          jPopupMenu.removeAll();
        }

      });
    }
    super.handleShowing(menuItemContainer, e);
  }
}
