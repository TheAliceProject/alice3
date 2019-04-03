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
package org.alice.ide.ast.type.preview.croquet.views;

import org.alice.ide.Theme;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.ast.type.merge.croquet.AddMembersPage;
import org.alice.ide.ast.type.merge.croquet.MemberHub;
import org.alice.ide.ast.type.preview.croquet.PreviewPage;
import org.alice.ide.common.TypeIcon;
import org.lgna.croquet.views.HorizontalTextPosition;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class PreviewPane extends MigPanel {
  public PreviewPane(PreviewPage page) {
    super(page, "fillx", "", "[grow 0][grow]");
    this.setBackgroundColor(ThemeUtilities.getActiveTheme().getTypeColor());
  }

  @Override
  protected void internalRefresh() {
    super.internalRefresh();
    this.forgetAndRemoveAllComponents();
    PreviewPage page = (PreviewPage) this.getComposite();
    AddMembersPage addMembersPage = page.getOwner().getAddMembersPage();

    MigPanel panel = new MigPanel(null, "fillx, insets 0");

    Theme theme = ThemeUtilities.getActiveTheme();

    List<MemberHub<UserMethod>> procedureHubs = addMembersPage.getPreviewProcedureHubs();
    if (procedureHubs.size() > 0) {
      panel.addComponent(new MembersSubPane<UserMethod>("procedures", theme.getProcedureColor(), procedureHubs), "gap 8, grow, shrink, wrap");
    }

    List<MemberHub<UserMethod>> functionHubs = addMembersPage.getPreviewFunctionHubs();
    if (functionHubs.size() > 0) {
      panel.addComponent(new MembersSubPane<UserMethod>("functions", theme.getFunctionColor(), functionHubs), "gap 8, grow, shrink, wrap");
    }

    List<MemberHub<UserField>> fieldHubs = addMembersPage.getPreviewFieldHubs();
    if (fieldHubs.size() > 0) {
      panel.addComponent(new MembersSubPane<UserField>("properties", theme.getFieldColor(), fieldHubs), "gap 8, grow, shrink, wrap");
    }

    Label classLabel = new Label("class", TypeIcon.getInstance(addMembersPage.getDstType()));
    //classLabel.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
    classLabel.scaleFont(1.2f);
    classLabel.setHorizontalTextPosition(HorizontalTextPosition.LEADING);
    this.addComponent(classLabel, "split 3, grow, shrink, push");
    this.addComponent(page.getIsIncludingAllState().getSidekickLabel().createLabel());
    this.addComponent(page.getIsIncludingAllState().createHorizontalToggleButtons(false), "wrap");

    ScrollPane scrollPane = new ScrollPane(panel);
    panel.setBackgroundColor(this.getBackgroundColor());
    this.addComponent(scrollPane, "grow, shrink, wrap");
  }
}
