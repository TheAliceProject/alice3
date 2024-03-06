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
package org.alice.ide.member.views;

import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.Theme;
import org.alice.ide.member.AddMethodMenuModel;
import org.alice.ide.member.FunctionsOfReturnTypeSubComposite;
import org.alice.ide.member.MemberTabComposite;
import org.alice.ide.member.MethodsSubComposite;
import org.alice.ide.member.UserMethodsSubComposite;
import org.alice.stageide.icons.PlusIconFactory;
import org.lgna.croquet.views.ComboBox;
import org.lgna.croquet.views.HorizontalTextPosition;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.PopupButton;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.Separator;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.ToolPaletteTitle;
import org.lgna.croquet.views.ToolPaletteView;
import org.lgna.project.ast.Member;

import java.awt.Insets;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberTabView extends MigPanel {
  private final Map<Member, SwingComponentView<?>> map = Maps.newHashMap();

  private final PopupButton popupButton;
  private final ComboBox<String> comboBox;

  MemberTabView(MemberTabComposite<?> composite) {
    super(composite, "insets 0, fill", "[]", "[grow 0][]");
    AddMethodMenuModel addMethodMenuModel = composite.getAddMethodMenuModel();
    if (addMethodMenuModel != null) {
      this.popupButton = addMethodMenuModel.getPopupPrepModel().createPopupButton();
      this.popupButton.setClobberIcon(PlusIconFactory.getInstance().getIconToFit(Theme.EXTRA_SMALL_SQUARE_ICON_SIZE));
      this.popupButton.tightenUpMargin(new Insets(-2, -10, -2, -8));
    } else {
      this.popupButton = null;
    }
    this.comboBox = composite.getSortState().getPrepModel().createComboBoxWithItemCodecListCellRenderer();
  }

  private static SwingComponentView<?> createDragView(Member member) {
    return new Label(member.getName());
  }

  protected SwingComponentView<?> getComponentFor(Member member) {
    synchronized (this.map) {
      SwingComponentView<?> rv = this.map.get(member);
      if (rv == null) {
        rv = createDragView(member);
        this.map.put(member, rv);
      }
      return rv;
    }
  }

  @Override
  protected void internalRefresh() {
    super.internalRefresh();
    MemberTabComposite<?> composite = (MemberTabComposite<?>) this.getComposite();
    this.removeAllComponents();

    AddMethodMenuModel addMethodMenuModel = composite.getAddMethodMenuModel();
    SwingComponentView<?> leftTopComponent;
    if (addMethodMenuModel != null) {
      if (addMethodMenuModel.isRelevant()) {
        leftTopComponent = this.popupButton;
      } else {
        leftTopComponent = null;
      }
    } else {
      leftTopComponent = null;
    }
    String scrollPaneConstraints = "grow";
    if (leftTopComponent != null) {
      this.addComponent(leftTopComponent, "align left");
      scrollPaneConstraints += ", span 2";
    }
    this.addComponent(this.comboBox, "align right, wrap");

    MigPanel scrollPaneView = new MigPanel(null, "insets 0", "[]", "[]0[]");
    for (MethodsSubComposite subComposite : composite.getSubComposites()) {
      if (subComposite != MemberTabComposite.SEPARATOR) {
        if (subComposite.isShowingDesired()) {
          ToolPaletteView view = subComposite.getOuterComposite().getView();
          if (subComposite instanceof FunctionsOfReturnTypeSubComposite) {
            view.getTitle().setHorizontalTextPosition(HorizontalTextPosition.LEADING);
          }
          view.getTitle().changeFont(TextPosture.OBLIQUE);
          if (MemberTabComposite.ARE_TOOL_PALETTES_INERT) {
            view.getTitle().setInert(true);
          } else {
            view.getTitle().setRenderingStyle(ToolPaletteTitle.RenderingStyle.LIGHT_UP_ICON_ONLY);
          }
          view.setBackgroundColor(this.getBackgroundColor());
          if (subComposite instanceof UserMethodsSubComposite) {
            UserMethodsSubComposite userMethodsSubComposite = (UserMethodsSubComposite) subComposite;
            view.getTitle().setSuppressed(!userMethodsSubComposite.isRelevant());
          }
          scrollPaneView.addComponent(view, "wrap");
          subComposite.getView().internalRefresh();
        }
      } else {
        scrollPaneView.addComponent(Separator.createInstanceSeparatingTopFromBottom(), "wrap");
      }
    }
    scrollPaneView.setBackgroundColor(this.getBackgroundColor());
    ScrollPane scrollPane = new ScrollPane(scrollPaneView);
    this.addComponent(scrollPane, scrollPaneConstraints);
  }
}
