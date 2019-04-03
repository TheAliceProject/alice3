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
package org.alice.ide.croquet.models.menubar;

import org.alice.ide.IdeApp;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.croquet.models.ui.locale.LocaleState;
import org.alice.ide.croquet.models.ui.preferences.IsExposingReassignableStatusState;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingConstructors;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingProgramType;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState;
import org.alice.ide.croquet.models.ui.preferences.IsJavaCodeOnTheSideState;
import org.lgna.croquet.MenuModel;
import org.lgna.croquet.PredeterminedMenuModel;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class PreferencesMenuModel extends PredeterminedMenuModel {
  private static class SingletonHolder {
    private static PreferencesMenuModel instance = new PreferencesMenuModel();
  }

  public static PreferencesMenuModel getInstance() {
    return SingletonHolder.instance;
  }

  private PreferencesMenuModel() {
    super(UUID.fromString("e8f8a5b3-83be-4519-8956-3ef2b9546e23"), FormatterState.getInstance().getMenuModel(), LocaleState.getInstance().getMenuModel(), MenuModel.SEPARATOR, IsJavaCodeOnTheSideState.getInstance().getMenuItemPrepModel(), MenuModel.SEPARATOR, IsIncludingThisForFieldAccessesState.getInstance().getMenuModel(),
          //org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getMenuItemPrepModel(),
          //org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().getMenuItemPrepModel(),
          MenuModel.SEPARATOR, IdeApp.INSTANCE.getIsRecursionAllowedPreferenceDialogLaunchOperation().getMenuItemPrepModel(), IsExposingReassignableStatusState.getInstance().getMenuItemPrepModel(), IsIncludingProgramType.getInstance().getMenuItemPrepModel(), IsIncludingConstructors.getInstance().getMenuItemPrepModel(), MenuModel.SEPARATOR, AllowNullMenuModel.getInstance(), MenuModel.SEPARATOR, GalleryMenuModel.getInstance());
  }
}
