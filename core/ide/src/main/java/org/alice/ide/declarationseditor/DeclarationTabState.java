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

import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.DefaultTheme;
import org.alice.ide.IDE;
import org.alice.ide.croquet.codecs.typeeditor.DeclarationCompositeCodec;
import org.alice.ide.icons.TabIcon;
import org.alice.ide.project.ProjectChangeOfInterestManager;
import org.alice.ide.project.events.ProjectChangeOfInterestListener;
import org.lgna.croquet.MutableDataTabState;
import org.lgna.croquet.Operation;
import org.lgna.croquet.data.ListData;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;

import javax.swing.Icon;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationTabState extends MutableDataTabState<DeclarationComposite<?, ?>> {
  private final ProjectChangeOfInterestListener projectChangeOfInterestListener = new ProjectChangeOfInterestListener() {
    @Override
    public void projectChanged() {
      handleAstChangeThatCouldBeOfInterest();
    }
  };

  public DeclarationTabState() {
    super(IDE.DOCUMENT_UI_GROUP, UUID.fromString("7b3f95a0-c188-43bf-9089-21ec77c99a69"), DeclarationCompositeCodec.SINGLETON);
    ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener(this.projectChangeOfInterestListener);
  }

  @Override
  protected void setCurrentTruthAndBeautyValue(DeclarationComposite<?, ?> declarationComposite) {
    if (declarationComposite != null) {
      ListData<DeclarationComposite<?, ?>> data = this.getData();
      if (data.contains(declarationComposite)) {
        //pass
      } else {
        class TypeListPair {
          private final NamedUserType type;
          private final List<DeclarationComposite<?, ?>> list = Lists.newLinkedList();

          public TypeListPair(NamedUserType type) {
            this.type = type;
          }

          public void addDeclarationComposite(DeclarationComposite<?, ?> declarationComposite) {
            if (declarationComposite instanceof TypeComposite) {
              this.list.add(0, declarationComposite);
            } else {
              this.list.add(declarationComposite);
            }
          }

          public void update(List<DeclarationComposite<?, ?>> updatee, boolean isTypeRequired) {
            if (isTypeRequired) {
              TypeComposite typeComposite = TypeComposite.getInstance(this.type);
              if (this.list.contains(typeComposite)) {
                //pass
              } else {
                updatee.add(typeComposite);
              }
            }
            updatee.addAll(this.list);
          }
        }

        InitializingIfAbsentMap<NamedUserType, TypeListPair> map = Maps.newInitializingIfAbsentHashMap();
        List<TypeListPair> typeListPairs = Lists.newLinkedList();

        List<DeclarationComposite<?, ?>> prevItems = Lists.newArrayList(data.toArray());
        prevItems.add(declarationComposite);

        List<DeclarationComposite<?, ?>> orphans = Lists.newLinkedList();
        for (DeclarationComposite<?, ?> item : prevItems) {
          if (item != null) {
            NamedUserType namedUserType = (NamedUserType) item.getType();
            if (namedUserType != null) {
              TypeListPair typeListPair = map.getInitializingIfAbsent(namedUserType, new InitializingIfAbsentMap.Initializer<NamedUserType, TypeListPair>() {
                @Override
                public TypeListPair initialize(NamedUserType key) {
                  return new TypeListPair(key);
                }
              });
              typeListPair.addDeclarationComposite(item);
              if (typeListPairs.contains(typeListPair)) {
                //pass
              } else {
                typeListPairs.add(typeListPair);
              }
            } else {
              orphans.add(item);
            }
          }
        }
        List<DeclarationComposite<?, ?>> nextItems = Lists.newLinkedList();
        boolean isTypeRequired = true; //typeListPairs.size() > 1;
        boolean isSeparatorDesired = false;
        for (TypeListPair typeListPair : typeListPairs) {
          if (isSeparatorDesired) {
            nextItems.add(null);
          }
          typeListPair.update(nextItems, isTypeRequired);
          isSeparatorDesired = true;
        }

        if (orphans.size() > 0) {
          nextItems.add(null);
          nextItems.addAll(orphans);
        }
        data.internalSetAllItems(nextItems);
      }
    }
    super.setCurrentTruthAndBeautyValue(declarationComposite);
  }

  private static final Dimension ICON_SIZE = new Dimension(16, 16);
  private static final Icon TYPE_ICON = new TabIcon(ICON_SIZE, DefaultTheme.DEFAULT_TYPE_COLOR);
  private static final Icon FIELD_ICON = new TabIcon(ICON_SIZE, DefaultTheme.DEFAULT_TYPE_COLOR) {
    @Override
    protected void paintIcon(Component c, Graphics2D g2, int width, int height, Paint fillPaint, Paint drawPaint) {
      super.paintIcon(c, g2, width, height, fillPaint, drawPaint);
      g2.setPaint(DefaultTheme.DEFAULT_FIELD_COLOR);
      g2.fill(new Rectangle2D.Float(0.3f * width, 0.7f * height, 0.6f * width, 0.1f * height));
    }
  };

  private static final Icon PROCEDURE_ICON = new TabIcon(ICON_SIZE, DefaultTheme.DEFAULT_PROCEDURE_COLOR);
  private static final Icon FUNCTION_ICON = new TabIcon(ICON_SIZE, DefaultTheme.DEFAULT_FUNCTION_COLOR);
  private static final Icon CONSTRUCTOR_ICON = new TabIcon(ICON_SIZE, DefaultTheme.DEFAULT_CONSTRUCTOR_COLOR);

  public static Icon getProcedureIcon() {
    return PROCEDURE_ICON;
  }

  public static Icon getFunctionIcon() {
    return FUNCTION_ICON;
  }

  public static Icon getFieldIcon() {
    return FIELD_ICON;
  }

  public static Icon getConstructorIcon() {
    return CONSTRUCTOR_ICON;
  }

  public Operation getItemSelectionOperationForType(NamedUserType type) {
    Operation rv = this.getItemSelectionOperation(TypeComposite.getInstance(type));
    rv.setSmallIcon(TYPE_ICON);
    return rv;
  }

  public Operation getItemSelectionOperationForMethod(AbstractMethod method) {
    final Operation rv = this.getItemSelectionOperation(CodeComposite.getInstance(method));
    if (method.isProcedure()) {
      rv.setSmallIcon(PROCEDURE_ICON);
    } else {
      rv.setSmallIcon(FUNCTION_ICON);
    }
    if (method instanceof UserMethod) {
      UserMethod userMethod = (UserMethod) method;
      userMethod.name.addPropertyListener(e -> rv.setName((String) e.getValue()));
      //todo: release?
    }
    return rv;
  }

  public Operation getItemSelectionOperationForConstructor(AbstractConstructor constructor) {
    Operation rv = this.getItemSelectionOperation(CodeComposite.getInstance(constructor));
    rv.setSmallIcon(CONSTRUCTOR_ICON);
    return rv;
  }

  public Operation getItemSelectionOperationForCode(AbstractCode code) {
    if (code instanceof AbstractMethod) {
      return this.getItemSelectionOperationForMethod((AbstractMethod) code);
    } else if (code instanceof AbstractConstructor) {
      return this.getItemSelectionOperationForConstructor((AbstractConstructor) code);
    } else {
      return null;
    }
  }

  private void handleAstChangeThatCouldBeOfInterest() {
    DeclarationComposite<?, ?> declarationComposite = this.getValue();
    if (declarationComposite instanceof CodeComposite) {
      CodeComposite codeComposite = (CodeComposite) declarationComposite;
      codeComposite.handleAstChangeThatCouldBeOfInterest();
    }
  }

  public void removeAllOrphans() {
    List<DeclarationComposite<?, ?>> orphans = Lists.newLinkedList();
    for (DeclarationComposite<?, ?> composite : this) {
      if (composite != null) {
        AbstractDeclaration declaration = composite.getDeclaration();
        if (declaration instanceof UserCode) {
          UserCode code = (UserCode) declaration;
          UserType<?> declaringType = code.getDeclaringType();
          if (declaringType != null) {
            //pass
          } else {
            orphans.add(composite);
          }
        }
      }
    }

    for (DeclarationComposite<?, ?> orphan : orphans) {
      this.removeItem(orphan);
    }
  }
}
