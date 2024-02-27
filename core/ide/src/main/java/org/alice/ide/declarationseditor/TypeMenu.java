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
package org.alice.ide.declarationseditor;

import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Sets;
import org.alice.ide.IDE;
import org.alice.ide.ast.declaration.AddFunctionComposite;
import org.alice.ide.ast.declaration.AddProcedureComposite;
import org.alice.ide.ast.declaration.AddUnmanagedFieldComposite;
import org.alice.ide.ast.declaration.ManagedEditFieldComposite;
import org.alice.ide.ast.declaration.UnmanagedEditFieldComposite;
import org.alice.ide.common.TypeIcon;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingConstructors;
import org.alice.stageide.ast.declaration.AddResourceKeyManagedFieldComposite;
import org.alice.stageide.showme.ShowMeHowToAddGalleryModelsIteratingOperation;
import org.lgna.croquet.MenuModel;
import org.lgna.croquet.Operation;
import org.lgna.croquet.StandardMenuItemPrepModel;
import org.lgna.croquet.data.ListData;
import org.lgna.croquet.views.MenuItemContainer;
import org.lgna.croquet.views.MenuItemContainerUtilities;
import org.lgna.croquet.views.ViewController;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;

import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class TypeMenu extends MenuModel {
  private static final Font TYPE_FONT;
  private static final Font BONUS_FONT;

  static {
    Font defaultFont = UIManager.getFont("defaultFont");
    if (defaultFont == null) {
      defaultFont = new Font("SansSerif", Font.PLAIN, 12);
    }
    TYPE_FONT = defaultFont.deriveFont(18.0f);
    BONUS_FONT = defaultFont.deriveFont(Font.ITALIC);
  }

  private static Map<NamedUserType, TypeMenu> map = Maps.newHashMap();

  public static synchronized TypeMenu getInstance(NamedUserType type) {
    TypeMenu rv = map.get(type);
    if (rv == null) {
      rv = new TypeMenu(type);
      map.put(type, rv);
    }
    return rv;
  }

  private final NamedUserType type;

  private TypeMenu(NamedUserType type) {
    super(UUID.fromString("d4ea32ce-d9d6-452e-8d99-2d8078c01251"));
    this.type = type;
  }

  @Override
  protected void localize() {
    super.localize();
    this.setSmallIcon(new TypeIcon(this.type, true, TYPE_FONT, BONUS_FONT));
  }

  @Override
  protected void handleShowing(MenuItemContainer menuItemContainer, PopupMenuEvent e) {
    DeclarationTabState declarationTabState = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();

    List<StandardMenuItemPrepModel> procedureModels = Lists.newLinkedList();
    List<StandardMenuItemPrepModel> functionModels = Lists.newLinkedList();
    List<StandardMenuItemPrepModel> managedFieldModels = Lists.newLinkedList();
    List<StandardMenuItemPrepModel> unmanagedFieldModels = Lists.newLinkedList();

    ListData<DeclarationComposite<?, ?>> data = declarationTabState.getData();
    final Set<StandardMenuItemPrepModel> set = Sets.newHashSet();
    for (UserMethod method : this.type.methods) {
      if (method.managementLevel.getValue() == ManagementLevel.NONE) {
        StandardMenuItemPrepModel model = declarationTabState.getItemSelectionOperationForMethod(method).getMenuItemPrepModel();
        if (data.contains(DeclarationComposite.getInstance(method))) {
          set.add(model);
        }
        if (method.isProcedure()) {
          procedureModels.add(model);
        } else {
          functionModels.add(model);
        }
      }
    }

    final boolean EDIT = false;
    for (UserField field : this.type.fields) {
      if (field.managementLevel.getValue() == ManagementLevel.MANAGED) {
        if (EDIT) {
          managedFieldModels.add(ManagedEditFieldComposite.getInstance(field).getLaunchOperation().getMenuItemPrepModel());
        } else {
          managedFieldModels.add(HighlightFieldOperation.getInstance(field).getMenuItemPrepModel());
        }
      } else {
        if (EDIT) {
          unmanagedFieldModels.add(UnmanagedEditFieldComposite.getInstance(field).getLaunchOperation().getMenuItemPrepModel());
        } else {
          unmanagedFieldModels.add(HighlightFieldOperation.getInstance(field).getMenuItemPrepModel());
        }
      }
    }

    if (procedureModels.size() > 0) {
      procedureModels.add(0, ProceduresSeparator.getInstance());
    }
    if (functionModels.size() > 0) {
      functionModels.add(0, FunctionsSeparator.getInstance());
    }

    procedureModels.add(AddProcedureComposite.getInstance(this.type).getLaunchOperation().getMenuItemPrepModel());
    functionModels.add(AddFunctionComposite.getInstance(this.type).getLaunchOperation().getMenuItemPrepModel());

    List<StandardMenuItemPrepModel> models = Lists.newLinkedList();

    Operation operation = declarationTabState.getItemSelectionOperationForType(type);
    operation.setName(type.getName());

    if (data.contains(DeclarationComposite.getInstance(type))) {
      set.add(operation.getMenuItemPrepModel());
    }
    models.add(operation.getMenuItemPrepModel());

    if (IsIncludingConstructors.getInstance().getValue()) {
      models.add(SEPARATOR);
      for (NamedUserConstructor constructor : type.getDeclaredConstructors()) {
        StandardMenuItemPrepModel model = declarationTabState.getItemSelectionOperationForConstructor(constructor).getMenuItemPrepModel();
        if (data.contains(DeclarationComposite.getInstance(constructor))) {
          set.add(model);
        }
        models.add(model);
      }
    }

    models.add(SEPARATOR);
    models.addAll(procedureModels);
    models.add(SEPARATOR);
    models.addAll(functionModels);

    if (IDE.getActiveInstance().getApiConfigurationManager().isDeclaringTypeForManagedFields(type)) {
      models.add(SEPARATOR);
      if (managedFieldModels.size() > 0) {
        models.add(ManagedFieldsSeparator.getInstance());
        models.addAll(managedFieldModels);
      }
      final boolean IS_SHOW_ME_HOW_PREFERRED = false;
      if (IS_SHOW_ME_HOW_PREFERRED) {
        models.add(ShowMeHowToAddGalleryModelsIteratingOperation.getInstance().getMenuItemPrepModel());
      } else {
        models.add(AddResourceKeyManagedFieldComposite.getInstance().getLaunchOperation().getMenuItemPrepModel());
      }
    }

    models.add(SEPARATOR);
    if ((unmanagedFieldModels.size() > 0) || (managedFieldModels.size() > 0)) {
      if (managedFieldModels.size() > 0) {
        models.add(UnmanagedFieldsSeparator.getInstance());
      } else {
        models.add(FieldsSeparator.getInstance());
      }
      models.addAll(unmanagedFieldModels);
    }
    models.add(AddUnmanagedFieldComposite.getInstance(type).getLaunchOperation().getMenuItemPrepModel());
    //    models.add( new PoserInputDialogComposite( type ).getOperation().getMenuItemPrepModel() );

    MenuItemContainerUtilities.MenuElementObserver observer = new MenuItemContainerUtilities.MenuElementObserver() {
      @Override
      public void update(MenuItemContainer menuItemContainer, StandardMenuItemPrepModel model, ViewController<?, ?> menuElement) {
        if (menuElement != null) {
          TextWeight textWeight = set.contains(model) ? TextWeight.BOLD : TextWeight.LIGHT;
          menuElement.changeFont(textWeight);
        }
      }
    };
    MenuItemContainerUtilities.setMenuElements(menuItemContainer, models, observer);

    super.handleShowing(menuItemContainer, e);
  }
}
