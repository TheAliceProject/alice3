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
package org.alice.stageide.type.croquet;

import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import org.alice.ide.ProjectStack;
import org.alice.stageide.type.croquet.data.SceneFieldListData;
import org.alice.stageide.type.croquet.views.OtherTypeDialogPane;
import org.lgna.croquet.Element;
import org.lgna.croquet.HtmlStringValue;
import org.lgna.croquet.ImmutableDataTabState;
import org.lgna.croquet.MultipleSelectionListState;
import org.lgna.croquet.SingleSelectTreeState;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.TabState;
import org.lgna.croquet.ValueCreator;
import org.lgna.croquet.ValueCreatorInputDialogCoreComposite;
import org.lgna.croquet.data.ListData;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.simple.SimpleApplication;
import org.lgna.croquet.triggers.NullTrigger;
import org.lgna.croquet.views.Panel;
import org.lgna.project.Project;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMember;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.io.IoUtilities;
import org.lgna.story.SModel;
import org.lgna.story.SThing;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class OtherTypeDialog extends ValueCreatorInputDialogCoreComposite<Panel, AbstractType> {
  private static class SingletonHolder {
    private static OtherTypeDialog instance = new OtherTypeDialog();
  }

  public static OtherTypeDialog getInstance() {
    return SingletonHolder.instance;
  }

  private class ValueCreatorForRootFilterType extends ValueCreator<AbstractType<?, ?, ?>> {
    public ValueCreatorForRootFilterType(JavaType rootFilterType) {
      super(UUID.fromString("84922129-0658-47af-8e32-f2476f030e41"));
      this.rootFilterType = rootFilterType;
    }

    @Override
    protected Class<? extends Element> getClassUsedForLocalization() {
      return OtherTypeDialog.class;
    }

    @Override
    protected AbstractType<?, ?, ?> createValue(UserActivity userActivity) {
      OtherTypeDialog.this.initializeRootFilterType(this.rootFilterType);
      return OtherTypeDialog.this.createValue(userActivity);
    }

    private final JavaType rootFilterType;
  }

  private final InitializingIfAbsentMap<JavaType, ValueCreator<AbstractType<?, ?, ?>>> mapTypeToValueCreator = Maps.newInitializingIfAbsentHashMap();

  private final Map<AbstractType<?, ?, ?>, TypeNode> map = Maps.newHashMap();

  private final TypeTreeState typeTreeState = new TypeTreeState();
  private final StringValue descriptionText = new HtmlStringValue(UUID.fromString("5417d9ee-bbe5-457b-aa63-1e5d0958ae1f")) {
  };
  private final SceneFieldListData sceneFieldListData = new SceneFieldListData();
  private final MultipleSelectionListState<UserField> sceneFieldsState = new SceneFieldsState(sceneFieldListData);

  private final AssignableTab assignableTab = new AssignableTab(this);
  private final ContainsTab containsTab = new ContainsTab(this);
  private final ImmutableDataTabState<?> tabState = this.createImmutableTabState("tabState", 0, this.assignableTab, this.containsTab);

  private final ErrorStatus noSelectionError = this.createErrorStatus("noSelectionError");
  private final Status notAssignableError = new Status() {
    @Override
    public boolean isGoodToGo() {
      return false;
    }

    @Override
    public String getText() {
      StringBuilder sb = new StringBuilder();
      sb.append("Select class assignable to ");
      if (rootFilterType != null) {
        sb.append(rootFilterType.getName());
      }
      return sb.toString();
    }
  };

  private JavaType rootFilterType;

  private boolean isInTheMidstOfLowestCommonAncestorSetting;
  private final ValueListener<TypeNode> typeListener = new ValueListener<TypeNode>() {
    @Override
    public void valueChanged(ValueEvent<TypeNode> e) {
      TypeNode nextValue = e.getNextValue();
      handleTypeChange(nextValue != null ? nextValue.getType() : null);
    }
  };

  private final ValueListener<List<UserField>> sceneFieldListener = new ValueListener<List<UserField>>() {
    @Override
    public void valueChanged(ValueEvent<List<UserField>> e) {
      List<UserField> fields = e.getNextValue();
      TypeNode sharedNode = null;
      if (fields.size() > 0) {
        for (UserField field : fields) {
          TypeNode typeNode = map.get(field.getValueType());
          if (sharedNode != null) {
            sharedNode = (TypeNode) sharedNode.getSharedAncestor(typeNode);
          } else {
            sharedNode = typeNode;
          }
        }
      }
      isInTheMidstOfLowestCommonAncestorSetting = true;
      try {
        typeTreeState.setValueTransactionlessly(sharedNode);
      } finally {
        isInTheMidstOfLowestCommonAncestorSetting = false;
      }
    }
  };

  private OtherTypeDialog() {
    super(UUID.fromString("58d24fb6-a6f5-4ad9-87b0-dfb5e9e4de41"));
  }

  public ValueCreator<AbstractType<?, ?, ?>> getValueCreator(JavaType rootType) {
    return this.mapTypeToValueCreator.getInitializingIfAbsent(rootType, new InitializingIfAbsentMap.Initializer<JavaType, ValueCreator<AbstractType<?, ?, ?>>>() {
      @Override
      public ValueCreator<AbstractType<?, ?, ?>> initialize(JavaType key) {
        return new ValueCreatorForRootFilterType(key);
      }
    });
  }

  public ValueCreator<AbstractType<?, ?, ?>> getValueCreator(Class<? extends SThing> rootCls) {
    return this.getValueCreator(JavaType.getInstance(rootCls));
  }

  private void initializeRootFilterType(JavaType rootFilterType) {
    this.rootFilterType = rootFilterType;
  }

  @Override
  protected Integer getWiderGoldenRatioSizeFromHeight() {
    return 600;
  }

  public TabState getTabState() {
    return this.tabState;
  }

  public MultipleSelectionListState<UserField> getSceneFieldsState() {
    return this.sceneFieldsState;
  }

  public SingleSelectTreeState<TypeNode> getTypeTreeState() {
    return this.typeTreeState;
  }

  public StringValue getDescriptionText() {
    return this.descriptionText;
  }

  @Override
  protected AbstractType createValue() {
    TypeNode typeNode = this.typeTreeState.getValue();
    if (typeNode != null) {
      return typeNode.getType();
    } else {
      return null;
    }
  }

  @Override
  protected Status getStatusPreRejectorCheck() {
    TypeNode typeNode = this.typeTreeState.getValue();
    if (typeNode != null) {
      AbstractType<?, ?, ?> type = typeNode.getType();
      //todo assert this.rootFilterType != null;
      if ((this.rootFilterType == null) || this.rootFilterType.isAssignableFrom(type)) {
        return IS_GOOD_TO_GO_STATUS;
      } else {
        return this.notAssignableError;
      }
    } else {
      return this.noSelectionError;
    }
  }

  private static TypeNode build(AbstractType<?, ?, ?> type, Map<AbstractType<?, ?, ?>, TypeNode> map) {
    assert type != null;
    TypeNode typeNode = map.get(type);
    if (typeNode == null) {
      typeNode = new TypeNode(type);
      map.put(type, typeNode);
      AbstractType<?, ?, ?> superType = type.getSuperType();
      TypeNode superTypeNode = map.get(superType);
      if (superTypeNode == null) {
        superTypeNode = build(superType, map);
      }
      superTypeNode.add(typeNode);
    }
    return typeNode;
  }

  @Override
  public void handlePreActivation() {
    Project project = ProjectStack.peekProject();
    Iterable<NamedUserType> types = project.getNamedUserTypes();
    map.clear();

    JavaType rootType = JavaType.getInstance(SThing.class);
    TypeNode rootNode = new TypeNode(rootType);
    map.put(rootType, rootNode);
    for (NamedUserType type : types) {
      if (this.rootFilterType.isAssignableFrom(type)) {
        build(type, map);
      }
    }
    this.sceneFieldListData.refresh();

    // handle JavaType scene fields
    synchronized (this.sceneFieldListData) {
      final int N = this.sceneFieldListData.getItemCount();
      for (int i = 0; i < N; i++) {
        UserField field = this.sceneFieldListData.getItemAt(i);
        AbstractType<?, ?, ?> valueType = field.getValueType();
        if (valueType instanceof JavaType) {
          JavaType javaValueType = (JavaType) valueType;
          build(javaValueType, map);
        }
      }
    }

    this.typeTreeState.setRoot(rootNode);
    this.typeTreeState.addAndInvokeNewSchoolValueListener(this.typeListener);
    this.sceneFieldsState.addNewSchoolValueListener(this.sceneFieldListener);

    this.containsTab.getMemberListData().connect(rootNode);
    super.handlePreActivation();
  }

  @Override
  public void handlePostDeactivation() {
    this.containsTab.getMemberListData().disconnect();
    this.sceneFieldsState.removeNewSchoolValueListener(this.sceneFieldListener);
    this.typeTreeState.removeNewSchoolValueListener(this.typeListener);
    super.handlePostDeactivation();
  }

  @Override
  protected Panel createView() {
    return new OtherTypeDialogPane(this);
  }

  private static boolean isInclusionDesired(AbstractMember member) {
    if (member instanceof AbstractMethod) {
      AbstractMethod method = (AbstractMethod) member;
      if (method.isStatic()) {
        return false;
      }
    } else if (member instanceof AbstractField) {
      AbstractField field = (AbstractField) member;
      if (field.isStatic()) {
        return false;
      }
    }
    if (member.isPublicAccess() || member.isUserAuthored()) {
      Visibility visibility = member.getVisibility();
      return (visibility == null) || visibility.equals(Visibility.PRIME_TIME);
    } else {
      return false;
    }
  }

  private static void appendMembers(StringBuilder sb, AbstractType<?, ?, ?> type, boolean isSelected) {
    if (isSelected) {
      sb.append("<h2>");
    } else {
      sb.append("<h2>");
    }
    sb.append("class ");
    sb.append(type.getName());
    if (isSelected) {
      sb.append("</h2>");
    } else {
      sb.append(" <em>(inherit)</em></h2>");
    }

    List<? extends AbstractMethod> methods = type.getDeclaredMethods();

    boolean isFirst = true;
    for (AbstractMethod method : methods) {
      if (isInclusionDesired(method)) {
        if (method.isProcedure()) {
          if (isFirst) {
            sb.append("<em>procedures</em>");
            sb.append("<ul>");
            isFirst = false;
          }
          sb.append("<li>");
          sb.append(method.getName());
          sb.append("</li>");
        }
      }
    }
    if (isFirst) {
      //pass
    } else {
      sb.append("</ul>");
    }
    isFirst = true;
    for (AbstractMethod method : methods) {
      if (isInclusionDesired(method)) {
        if (method.isFunction()) {
          if (isFirst) {
            sb.append("<em>functions</em>");
            sb.append("<ul>");
            isFirst = false;
          }
          sb.append("<li>");
          sb.append(method.getName());
          sb.append("</li>");
        }
      }
    }
    if (isFirst) {
      //pass
    } else {
      sb.append("</ul>");
    }

    isFirst = true;
    for (AbstractField field : type.getDeclaredFields()) {
      if (isInclusionDesired(field)) {
        if (isFirst) {
          sb.append("<em>properties</em>");
          sb.append("<ul>");
          isFirst = false;
        }
        sb.append("<li>");
        sb.append(field.getName());
        sb.append("</li>");
      }
    }
    if (isFirst) {
      //pass
    } else {
      sb.append("</ul>");
    }

    if (type.isFollowToSuperClassDesired()) {
      appendMembers(sb, type.getSuperType(), false);
    }
  }

  private void handleTypeChange(AbstractType<?, ?, ?> type) {
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<body bgcolor=\"#FFFFFF\">");
    if (type != null) {
      appendMembers(sb, type, true);
    } else {
      sb.append("<em>no class selected</em>");
    }
    sb.append("</body>");
    sb.append("</html>");
    descriptionText.setText(sb.toString());

    ListData<UserField> data = sceneFieldsState.getData();

    this.getView().repaint();
    if (this.isInTheMidstOfLowestCommonAncestorSetting) {
      //
    } else {
      List<UserField> fields = Lists.newLinkedList();
      if (type != null) {
        synchronized (data) {
          final int N = data.getItemCount();
          for (int i = 0; i < N; i++) {
            UserField item = data.getItemAt(i);
            if (type.isAssignableFrom(item.getValueType())) {
              fields.add(item);
            }
          }
        }
      }
      this.sceneFieldsState.setValue(fields);
    }
  }

  public TypeNode getTypeNodeFor(AbstractType<?, ?, ?> nextValue) {
    return nextValue != null ? map.get(nextValue) : null;
  }

  public static void main(String[] args) throws Exception {
    UIManagerUtilities.setLookAndFeel("Nimbus");

    new SimpleApplication();

    Project project = IoUtilities.readProject(args[0]);
    ProjectStack.pushProject(project);
    OtherTypeDialog.getInstance().getValueCreator(SModel.class).fire(NullTrigger.createUserActivity());
    System.exit(0);
  }
}
