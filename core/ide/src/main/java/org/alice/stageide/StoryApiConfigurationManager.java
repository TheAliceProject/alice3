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

package org.alice.stageide;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.ApiConfigurationManager;
import org.alice.ide.ast.CurrentThisExpression;
import org.alice.ide.ast.ExpressionCreator;
import org.alice.ide.ast.components.DeclarationNameLabel;
import org.alice.ide.common.BeveledShapeForType;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingImportAndExportType;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingProgramType;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState;
import org.alice.ide.custom.PortionCustomExpressionCreatorComposite;
import org.alice.ide.iconfactory.IconFactoryManager;
import org.alice.ide.icons.Icons;
import org.alice.ide.identifier.IdentifierNameGenerator;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.ThisFieldAccessMethodInvocationFactory;
import org.alice.ide.instancefactory.croquet.InstanceFactoryFillIn;
import org.alice.ide.member.FilteredMethodsSubComposite;
import org.alice.ide.typemanager.ConstructorArgumentUtilities;
import org.alice.ide.typemanager.TypeManager;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.ast.BootstrapUtilities;
import org.alice.stageide.ast.JointedTypeInfo;
import org.alice.stageide.custom.VolumeLevelCustomExpressionCreatorComposite;
import org.alice.stageide.iconfactory.StoryIconFactoryManager;
import org.alice.stageide.icons.*;
import org.alice.stageide.instancefactory.croquet.joint.all.LocalAccessJointedTypeMenuModel;
import org.alice.stageide.instancefactory.croquet.joint.all.ParameterAccessJointedTypeMenuModel;
import org.alice.stageide.instancefactory.croquet.joint.all.ParameterAccessMethodInvocationJointedTypeMenuModel;
import org.alice.stageide.instancefactory.croquet.joint.all.ThisFieldAccessJointedTypeMenuModel;
import org.alice.stageide.instancefactory.croquet.joint.all.ThisJointedTypeMenuModel;
import org.alice.stageide.member.AddListenerProceduresComposite;
import org.alice.stageide.member.AppearanceFunctionsComposite;
import org.alice.stageide.member.AppearanceProceduresComposite;
import org.alice.stageide.member.AtmosphereFunctionsComposite;
import org.alice.stageide.member.AtmosphereProceduresComposite;
import org.alice.stageide.member.AudioProceduresComposite;
import org.alice.stageide.member.FieldOfViewFunctionsComposite;
import org.alice.stageide.member.FieldOfViewProceduresComposite;
import org.alice.stageide.member.JointFunctionsComposite;
import org.alice.stageide.member.OrientationProceduresComposite;
import org.alice.stageide.member.PositionAndOrientationProceduresComposite;
import org.alice.stageide.member.PositionProceduresComposite;
import org.alice.stageide.member.PromptUserFunctionsComposite;
import org.alice.stageide.member.SayThinkProceduresComposite;
import org.alice.stageide.member.SizeFunctionsComposite;
import org.alice.stageide.member.SizeProceduresComposite;
import org.alice.stageide.member.TextProceduresComposite;
import org.alice.stageide.member.TimingProceduresComposite;
import org.alice.stageide.member.VehicleProceduresComposite;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeItem;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.icon.ImageIconFactory;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.ast.*;
import org.lgna.story.*;
import org.lgna.story.annotation.PortionDetails;
import org.lgna.story.annotation.VolumeLevelDetails;
import org.lgna.story.resources.*;
import org.lgna.story.resourceutilities.StorytellingResourcesTreeUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class StoryApiConfigurationManager extends ApiConfigurationManager {
  public static final JavaMethod SET_ACTIVE_SCENE_METHOD = JavaMethod.getInstance(SProgram.class, "setActiveScene", SScene.class);
  private CascadeMenuModel<InstanceFactory> cameraFieldsMenuModel;
  private CascadeMenuModel<InstanceFactory> vrUserFieldsMenuModel;

  private static class SingletonHolder {
    private static StoryApiConfigurationManager instance = NebulousIde.nonfree.newStoryApiConfigurationManager();
  }

  public static StoryApiConfigurationManager getInstance() {
    return SingletonHolder.instance;
  }

  private final org.alice.stageide.ast.ExpressionCreator expressionCreator = NebulousIde.nonfree.newExpressionCreator();
  private final List<FilteredMethodsSubComposite> categoryProcedureSubComposites;
  private final List<FilteredMethodsSubComposite> categoryFunctionSubComposites;
  private final List<FilteredMethodsSubComposite> categoryOrAlphabeticalProcedureSubComposites;
  private final List<FilteredMethodsSubComposite> categoryOrAlphabeticalFunctionSubComposites;

  private static List<FilteredMethodsSubComposite> createUnmodifiableSubCompositeList(FilteredMethodsSubComposite... subComposites) {
    return Collections.unmodifiableList(Lists.newLinkedList(subComposites));
  }

  public StoryApiConfigurationManager() {
    BeveledShapeForType.addRoundType(SThing.class);
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SSphere.class, SceneIconFactory.getInstance());
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SCylinder.class, new ShapeIconFactory(CylinderIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SCone.class, new ShapeIconFactory(ConeIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SDisc.class, new ShapeIconFactory(DiscIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SSphere.class, new ShapeIconFactory(SphereIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(STorus.class, new ShapeIconFactory(TorusIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SAxes.class, new ShapeIconFactory(AxesIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(STextModel.class, new ShapeIconFactory(TextModelIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SBillboard.class, new ShapeIconFactory(BillboardIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SBox.class, new ShapeIconFactory(BoxIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SGround.class, new ShapeIconFactory(GroundIcon.class));

    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SJoint.class, new ShapeIconFactory(JointIcon.class));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SCamera.class, new ImageIconFactory(Icons.class.getResource("images/160x120/Camera.png")));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SVRHand.class, new ImageIconFactory(Icons.class.getResource("images/256x256/LeftHand.png")));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SVRHeadset.class, new ImageIconFactory(Icons.class.getResource("images/256x256/VRHeadset.png")));
    org.alice.stageide.icons.IconFactoryManager.registerIconFactory(SVRUser.class, new ImageIconFactory(Icons.class.getResource("images/VRUser.png")));

    this.categoryProcedureSubComposites = createUnmodifiableSubCompositeList(TextProceduresComposite.getInstance(), AtmosphereProceduresComposite.getInstance(), SayThinkProceduresComposite.getInstance(), PositionProceduresComposite.getInstance(), OrientationProceduresComposite.getInstance(), PositionAndOrientationProceduresComposite.getInstance(), SizeProceduresComposite.getInstance(), AppearanceProceduresComposite.getInstance(), FieldOfViewProceduresComposite.getInstance(), VehicleProceduresComposite.getInstance(), AudioProceduresComposite.getInstance(), TimingProceduresComposite.getInstance());

    this.categoryFunctionSubComposites = createUnmodifiableSubCompositeList(AtmosphereFunctionsComposite.getInstance(), AppearanceFunctionsComposite.getInstance(), SizeFunctionsComposite.getInstance(), PromptUserFunctionsComposite.getInstance(), FieldOfViewFunctionsComposite.getInstance());

    this.categoryOrAlphabeticalProcedureSubComposites = createUnmodifiableSubCompositeList(AddListenerProceduresComposite.getInstance());

    this.categoryOrAlphabeticalFunctionSubComposites = createUnmodifiableSubCompositeList(JointFunctionsComposite.getInstance());
  }

  private static enum TypeComparator implements Comparator<AbstractType<?, ?, ?>> {
    SINGLETON;
    private static final double DEFAULT_VALUE = 50.0;
    private final Map<AbstractType<?, ?, ?>, Double> mapTypeToValue = Maps.newHashMap();

    TypeComparator() {
      mapTypeToValue.put(JavaType.BOOLEAN_OBJECT_TYPE, 1.1);
      mapTypeToValue.put(JavaType.DOUBLE_OBJECT_TYPE, 1.2);
      mapTypeToValue.put(JavaType.INTEGER_OBJECT_TYPE, 1.3);
      mapTypeToValue.put(JavaType.STRING_TYPE, 1.4);

      mapTypeToValue.put(JavaType.getInstance(SThing.class), 10.1);

      mapTypeToValue.put(JavaType.getInstance(Color.class), 20.1);
      mapTypeToValue.put(JavaType.getInstance(Paint.class), 20.2);

      mapTypeToValue.put(JavaType.getInstance(Position.class), 30.1);
      mapTypeToValue.put(JavaType.getInstance(Orientation.class), 30.2);
      mapTypeToValue.put(JavaType.getInstance(VantagePoint.class), 30.3);

      mapTypeToValue.put(JavaType.getInstance(SJoint.class), 99.9);
    }

    private double getValue(AbstractType<?, ?, ?> type) {
      Double value = mapTypeToValue.get(type);
      if (value != null) {
        return value;
      } else {
        return DEFAULT_VALUE;
      }
    }

    @Override
    public int compare(AbstractType<?, ?, ?> typeA, AbstractType<?, ?, ?> typeB) {
      double valueA = getValue(typeA);
      double valueB = getValue(typeB);
      if (valueA == valueB) {
        return typeA.getName().compareTo(typeB.getName());
      } else {
        return Double.compare(valueA, valueB);
      }
    }
  }

  @Override
  public Comparator<AbstractType<?, ?, ?>> getTypeComparator() {
    return TypeComparator.SINGLETON;
  }

  @Override
  protected boolean isNamedUserTypesAcceptableForGallery(NamedUserType type) {
    return type.isAssignableTo(SModel.class);
  }

  @Override
  protected boolean isNamedUserTypesAcceptableForSelection(NamedUserType type) {
    return (type.isAssignableTo(SProgram.class) == false) || IsIncludingProgramType.getInstance().getValue();
  }

  @Override
  public List<FilteredMethodsSubComposite> getCategoryProcedureSubComposites() {
    return this.categoryProcedureSubComposites;
  }

  @Override
  public List<FilteredMethodsSubComposite> getCategoryFunctionSubComposites() {
    return this.categoryFunctionSubComposites;
  }

  @Override
  public List<FilteredMethodsSubComposite> getCategoryOrAlphabeticalProcedureSubComposites() {
    return this.categoryOrAlphabeticalProcedureSubComposites;
  }

  @Override
  public List<FilteredMethodsSubComposite> getCategoryOrAlphabeticalFunctionSubComposites() {
    return this.categoryOrAlphabeticalFunctionSubComposites;
  }

  @Override
  public boolean isDeclaringTypeForManagedFields(UserType<?> type) {
    return type.isAssignableTo(SScene.class);
  }

  @Override
  public boolean isInstanceFactoryDesiredForType(AbstractType<?, ?, ?> type) {
    return type != null
        && (type.isAssignableTo(SThing.class) && !type.isAssignableTo(SMarker.class)
        || type.isAssignableTo(SProgram.class));
  }

  protected static final JavaType BIPED_RESOURCE_TYPE = JavaType.getInstance(BipedResource.class);

  @Override
  public JavaType getGalleryResourceParentFor(JavaType type) {
    return StorytellingResourcesTreeUtils.INSTANCE.getGalleryResourceParentFor(type);
  }

  @Override
  public List<AbstractDeclaration> getGalleryResourceChildrenFor(AbstractType<?, ?, ?> type) {
    return StorytellingResourcesTreeUtils.INSTANCE.getGalleryResourceChildrenFor(type);
  }

  @Override
  public CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForThis(AbstractType<?, ?, ?> type) {
    if (JointedTypeInfo.isJointed(type)) {
      return ThisJointedTypeMenuModel.getInstance(type);
    } else {
      return null;
    }
  }

  @Override
  public CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForThisFieldAccess(UserField field) {
    AbstractType<?, ?, ?> type = field.getValueType();
    if (JointedTypeInfo.isJointed(type)) {
      return ThisFieldAccessJointedTypeMenuModel.getInstance(field);
    }
    if (JavaType.getInstance(SCamera.class).isAssignableFrom(type)) {
      return getCameraFieldsMenu(field);
    }
    if (JavaType.getInstance(SVRUser.class).isAssignableFrom(type)) {
      return getVrUserFieldsMenu(field);
    }
    return null;
  }

  private CascadeMenuModel<InstanceFactory> getCameraFieldsMenu(UserField cameraField) {
    if (cameraFieldsMenuModel == null) {
      cameraFieldsMenuModel = new CascadeMenuModel<InstanceFactory>(UUID.fromString("2b2c901a-22f3-4080-8a28-381f3d3b26c8")) {
        @Override
        protected void updateBlankChildren(List<CascadeBlankChild> blankChildren, BlankNode<InstanceFactory> blankNode) {
          for (AbstractMethod method : SCamera.getHandMethods(cameraField.getValueType())) {
            blankChildren.add(InstanceFactoryFillIn.getInstance(ThisFieldAccessMethodInvocationFactory.getInstance(cameraField, method)));
          }
        }
      };
    }
    return cameraFieldsMenuModel;
  }

  private CascadeMenuModel<InstanceFactory> getVrUserFieldsMenu(UserField vrField) {
    if (vrUserFieldsMenuModel == null) {
      vrUserFieldsMenuModel = new CascadeMenuModel<InstanceFactory>(UUID.fromString("2b2c901a-22f3-4050-8a28-331f32bb26a8")) {
        @Override
        protected void updateBlankChildren(List<CascadeBlankChild> blankChildren, BlankNode<InstanceFactory> blankNode) {
          for (AbstractMethod method : SVRUser.getDeviceMethods(vrField.getValueType())) {
            blankChildren.add(InstanceFactoryFillIn.getInstance(ThisFieldAccessMethodInvocationFactory.getInstance(vrField, method)));
          }
        }
      };
    }
    return vrUserFieldsMenuModel;
  }

  @Override
  public CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForParameterAccess(UserParameter parameter) {
    AbstractType<?, ?, ?> type = parameter.getValueType();
    if (JointedTypeInfo.isJointed(type)) {
      return ParameterAccessJointedTypeMenuModel.getInstance(parameter);
    } else {
      return null;
    }
  }

  @Override
  public CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForLocalAccess(UserLocal local) {
    AbstractType<?, ?, ?> type = local.getValueType();
    if (JointedTypeInfo.isJointed(type)) {
      return LocalAccessJointedTypeMenuModel.getInstance(local);
    } else {
      return null;
    }
  }

  @Override
  public CascadeMenuModel<InstanceFactory> getInstanceFactorySubMenuForParameterAccessMethodInvocation(UserParameter parameter, AbstractMethod method) {
    AbstractType<?, ?, ?> type = method.getReturnType();
    if (JointedTypeInfo.isJointed(type)) {
      return ParameterAccessMethodInvocationJointedTypeMenuModel.getInstance(parameter, method);
    } else {
      return null;
    }
  }

  @Override
  public AbstractConstructor getGalleryResourceConstructorFor(AbstractType<?, ?, ?> argumentType) {
    List<NamedUserType> types = TypeManager.getNamedUserTypesFromSuperTypes(StorytellingResourcesTreeUtils.INSTANCE.getTopLevelGalleryTypes());
    for (AbstractType<?, ?, ?> type : types) {
      AbstractConstructor constructor = type.getDeclaredConstructors().get(0);
      List<? extends AbstractParameter> parameters = constructor.getRequiredParameters();
      if (parameters.size() == 1) {
        if (parameters.get(0).getValueType().isAssignableFrom(argumentType)) {
          return constructor;
        }
      }
    }
    return null;
  }

  private DeclarationNameLabel createDeclarationNameLabel(AbstractField field) {
    //todo: better name
    class ThisFieldAccessNameLabel extends DeclarationNameLabel {
      private ThisFieldAccessNameLabel(AbstractField field) {
        super(field);
      }

      @Override
      protected String getNameText() {
        if (IsIncludingThisForFieldAccessesState.getInstance().getValue()) {
          return FormatterState.getInstance().getValue().getTextForThis() + "." + super.getNameText();
        } else {
          return super.getNameText();
        }
      }
    }
    return new ThisFieldAccessNameLabel(field);
  }

  @Override
  public SwingComponentView<?> createReplacementForFieldAccessIfAppropriate(FieldAccess fieldAccess) {
    Expression fieldExpression = fieldAccess.expression.getValue();
    if ((fieldExpression instanceof ThisExpression) || (fieldExpression instanceof CurrentThisExpression)) {
      AbstractField field = fieldAccess.field.getValue();
      AbstractType<?, ?, ?> declaringType = field.getDeclaringType();
      if ((declaringType != null) && declaringType.isAssignableTo(SScene.class)) {
        if (field.getValueType().isAssignableTo(SThing.class)) {
          return this.createDeclarationNameLabel(field);
        }
      }
    }
    return null;
  }

  @Override
  public CascadeItem<?, ?> getCustomFillInFor(ValueDetails<?> valueDetails) {
    if (valueDetails instanceof PortionDetails) {
      return PortionCustomExpressionCreatorComposite.getInstance().getValueCreator().getFillIn();
    } else if (valueDetails instanceof VolumeLevelDetails) {
      //      return org.alice.stageide.croquet.models.custom.CustomVolumeLevelInputDialogOperation.getInstance().getFillIn();
      return VolumeLevelCustomExpressionCreatorComposite.getInstance().getValueCreator().getFillIn();
    } else {
      return null;
    }
  }

  @Override
  public ExpressionCreator getExpressionCreator() {
    return this.expressionCreator;
  }

  @Override
  public boolean isSignatureLocked(Code code) {
    //todo: check to see if only referenced from Program and Program type is hidden
    return super.isSignatureLocked(code) || BootstrapUtilities.MY_FIRST_PROCEDURE_NAME.equalsIgnoreCase(code.getName());
  }

  @Override
  public boolean isTabClosable(AbstractCode code) {
    return !BootstrapUtilities.MY_FIRST_PROCEDURE_NAME.equalsIgnoreCase(code.getName());
  }

  @Override
  protected List<? super JavaType> addPrimeTimeJavaTypes(List<? super JavaType> rv) {
    rv = super.addPrimeTimeJavaTypes(rv);
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Model.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Biped.class ) );
    return rv;
  }

  @Override
  protected List<? super JavaType> addSecondaryJavaTypes(List<? super JavaType> rv) {
    super.addSecondaryJavaTypes(rv);

    rv.add(JavaType.getInstance(SJoint.class));
    rv.add(null);
    if (StageIDE.getActiveInstance().getSceneEditor().isVrActive()) {
      rv.add(JavaType.getInstance(SVRHand.class));
      rv.add(JavaType.getInstance(SVRHeadset.class));
      rv.add(null);
    }
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SThing.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.STurnable.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SMovableTurnable.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SModel.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJointedModel.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SBillboard.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SAxes.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SShape.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SSphere.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SCone.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SDisc.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SMarker.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SThingMarker.class ) );
    //    rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SCameraMarker.class ) );
    rv.add(JavaType.getInstance(Paint.class));
    rv.add(JavaType.getInstance(Color.class));
    rv.add(null);
    rv.add(JavaType.getInstance(MoveDirection.class));
    rv.add(JavaType.getInstance(TurnDirection.class));
    rv.add(JavaType.getInstance(RollDirection.class));
    rv.add(JavaType.getInstance(Key.class));
    rv.add(null);
    rv.add(JavaType.getInstance(AudioSource.class));
    return rv;
  }

  private static final JavaType JOINTED_MODEL_TYPE = JavaType.getInstance(SJointedModel.class);

  private static String getFieldMethodNameHint(AbstractField field) {
    if (field instanceof JavaField) {
      Field fld = ((JavaField) field).getFieldReflectionProxy().getReification();
      if (fld != null) {
        if (fld.isAnnotationPresent(FieldTemplate.class)) {
          FieldTemplate propertyFieldTemplate = fld.getAnnotation(FieldTemplate.class);
          String methodNameHint = propertyFieldTemplate.methodNameHint();
          if (methodNameHint.length() > 0) {
            return methodNameHint;
          }
        }
      }
    }
    return null;
  }

  private void addMethodsToType(UserType<?> userType, DynamicResource dynamicResource) {
    //Get the string based "getJoint" method since we're working with dynamic resources and dynamic joints
    JavaMethod getJointMethod = JOINTED_MODEL_TYPE.getDeclaredMethod("getJoint", String.class);
    for (JointId joint : dynamicResource.getModelSpecificJoints()) {
      if (joint.getVisibility() != Visibility.COMPLETELY_HIDDEN) {
        String methodName = IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName(joint.toString(), "get");
        UserMethod method = AstUtilities.createFunction(methodName, SJoint.class);
        method.managementLevel.setValue(ManagementLevel.GENERATED);
        BlockStatement body = method.body.getValue();
        Expression expression = AstUtilities.createMethodInvocation(new ThisExpression(), getJointMethod, new StringLiteral(joint.toString()));
        body.statements.add(AstUtilities.createReturnStatement(SJoint.class, expression));
        userType.methods.add(method);
      }
    }
  }

  private void addMethodsToType(UserType<?> userType, AbstractType<?, ?, ?> resourceType) {
    JavaMethod getJointArrayMethod = JOINTED_MODEL_TYPE.getDeclaredMethod("getJointArray", JointId[].class);
    JavaMethod getJointArrayIdMethod = JOINTED_MODEL_TYPE.getDeclaredMethod("getJointArray", JointArrayId.class);
    JavaMethod getJointMethod = JOINTED_MODEL_TYPE.getDeclaredMethod("getJoint", JointId.class);
    JavaMethod strikePoseMethod = JOINTED_MODEL_TYPE.getDeclaredMethod("strikePose", Pose.class, StrikePose.Detail[].class);

    for (AbstractField field : resourceType.getDeclaredFields()) {
      if (field.isStatic()) {
        if (field.getValueType().isAssignableTo(JointId.class) && (field.getVisibility() != Visibility.COMPLETELY_HIDDEN)) {
          String methodName = getFieldMethodNameHint(field);
          if (methodName == null) {
            methodName = IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName(field.getName(), "get");
          }
          UserMethod method = AstUtilities.createFunction(methodName, SJoint.class);
          method.managementLevel.setValue(ManagementLevel.GENERATED);
          BlockStatement body = method.body.getValue();
          Expression expression = AstUtilities.createMethodInvocation(new ThisExpression(), getJointMethod, AstUtilities.createStaticFieldAccess(field));
          body.statements.add(AstUtilities.createReturnStatement(SJoint.class, expression));
          userType.methods.add(method);
        } else if (field.getValueType().isAssignableTo(JointId[].class) && (field.getVisibility() != Visibility.COMPLETELY_HIDDEN)) {
          String methodName = getFieldMethodNameHint(field);
          if (methodName == null) {
            methodName = IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName(field.getName(), "get");
          }
          UserMethod method = AstUtilities.createFunction(methodName, SJoint[].class);
          method.managementLevel.setValue(ManagementLevel.GENERATED);
          BlockStatement body = method.body.getValue();
          Expression expression = AstUtilities.createMethodInvocation(new ThisExpression(), getJointArrayMethod, AstUtilities.createStaticFieldAccess(field));
          body.statements.add(AstUtilities.createReturnStatement(SJoint[].class, expression));
          userType.methods.add(method);
        } else if (field.getValueType().isAssignableTo(JointArrayId.class) && (field.getVisibility() != Visibility.COMPLETELY_HIDDEN)) {
          String methodName = getFieldMethodNameHint(field);
          if (methodName == null) {
            methodName = IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName(field.getName(), "get");
          }
          UserMethod method = AstUtilities.createFunction(methodName, SJoint[].class);
          method.managementLevel.setValue(ManagementLevel.GENERATED);
          BlockStatement body = method.body.getValue();
          Expression expression = AstUtilities.createMethodInvocation(new ThisExpression(), getJointArrayIdMethod, AstUtilities.createStaticFieldAccess(field));
          body.statements.add(AstUtilities.createReturnStatement(SJoint[].class, expression));
          userType.methods.add(method);
        } else if (field.getValueType().isAssignableTo(Pose.class) && (field.getVisibility() != Visibility.COMPLETELY_HIDDEN)) {
          String methodName = getFieldMethodNameHint(field);
          if (methodName == null) {
            methodName = IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName(field.getName());
          }
          UserMethod method = AstUtilities.createProcedure(methodName);
          method.managementLevel.setValue(ManagementLevel.GENERATED);
          //                UserParameter detailsParameter = new UserParameter( "details", StrikePose.Detail.class );
          //                method.getVariableLengthParameter().
          //                method.requiredParameters.add( detailsParameter );
          BlockStatement body = method.body.getValue();
          MethodInvocation mi = AstUtilities.createMethodInvocation(new ThisExpression(), strikePoseMethod, AstUtilities.createStaticFieldAccess(field));
          //mi.variableArguments.add( new SimpleArgument( strikePoseMethod.getVariableLengthParameter(), new ParameterAccess( detailsParameter ) ) );
          body.statements.add(new ExpressionStatement(mi));
          userType.methods.add(method);
        }
      }
    }
  }

  @Override
  public UserType<?> augmentTypeIfNecessary(UserType<?> rv) {
    if (JOINTED_MODEL_TYPE.isAssignableFrom(rv)) {
      AbstractConstructor constructor0 = rv.getFirstDeclaredConstructor();
      //We have multiple different types of constructors to consider:
      // No parameters:
      // public Alien() { super(AlienResource.DEFAULT); }
      // public Alien2() { super(new DynamicBipedResource("Alien2", "Alien2")); }
      //
      // 1 parameter:
      // public Alice(AliceResource resource) { super(resource); }
      // public Biped(BipedResource resource) { super(resource); }
      Object firstArgument = constructor0.instantiateFirstArgumentPassedToSuperConstructor();
      AbstractType<?, ?, ?> constructorParameterType = ConstructorArgumentUtilities.getParameter0Type(constructor0);
      AbstractType<?, ?, ?> inferredResourceType = constructorParameterType;
      if (inferredResourceType == null) {
        JavaField field = ConstructorArgumentUtilities.getArgumentField(constructor0);
        if (field != null) {
          inferredResourceType = field.getValueType();
        }
      }
      JavaType ancestorType = rv.getFirstEncounteredJavaType();
      if (constructorParameterType != ConstructorArgumentUtilities.getContructor0Parameter0Type(ancestorType)) {
        if (inferredResourceType != null) {
          addMethodsToType(rv, inferredResourceType);
        } else if (firstArgument instanceof DynamicResource) {
          addMethodsToType(rv, (DynamicResource) firstArgument);
        } else {
          Logger.severe("Failed to augment type " + rv + ". Unable to find model resource type.");
        }
      }
    }
    return rv;
  }

  @Override
  public boolean isExportTypeDesiredFor(NamedUserType type) {
    if (IsIncludingImportAndExportType.getValue()) {
      return (type.isAssignableTo(SScene.class) == false) && (type.isAssignableTo(SProgram.class) == false);
    } else {
      return false;
    }
  }

  private AbstractType<?, ?, ?> getSpecificPoseBuilderType(Expression expression) {
    if (expression instanceof MethodInvocation) {
      MethodInvocation methodInvocation = (MethodInvocation) expression;
      return getSpecificPoseBuilderType(methodInvocation.expression.getValue());
    } else if (expression instanceof InstanceCreation) {
      InstanceCreation instanceCreation = (InstanceCreation) expression;
      return instanceCreation.getType();
    } else {
      return null;
    }
  }

  private AbstractType<?, ?, ?> getBuildMethodPoseBuilderType(MethodInvocation methodInvocation, boolean isSpecificPoseBuilderTypeRequired) {
    AbstractMethod method = methodInvocation.method.getValue();
    if ("build".equals(method.getName())) {
      AbstractType<?, ?, ?> type = methodInvocation.expression.getExpressionType();
      if (type.isAssignableTo(PoseBuilder.class)) {
        if (isSpecificPoseBuilderTypeRequired) {
          return getSpecificPoseBuilderType(methodInvocation.expression.getValue());
        } else {
          return type;
        }
      }
    }
    return null;
  }

  public AbstractType<?, ?, ?> getBuildMethodPoseBuilderType(MethodInvocation methodInvocation) {
    return getBuildMethodPoseBuilderType(methodInvocation, true);
  }

  public boolean isBuildMethod(MethodInvocation methodInvocation) {
    return getBuildMethodPoseBuilderType(methodInvocation, false) != null;
  }

  @Override
  public IconFactoryManager createIconFactoryManager() {
    return new StoryIconFactoryManager();
  }
}
