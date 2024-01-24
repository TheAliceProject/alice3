package org.lgna.project.migration.ast;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.EulerAngles;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.pattern.Crawlable;
import org.lgna.project.ProjectVersion;
import org.lgna.project.Version;
import org.lgna.project.ast.*;
import org.lgna.project.migration.AstMigration;
import org.lgna.project.migration.MigrationManager;
import org.lgna.project.virtualmachine.InstanceCreatingVirtualMachine;
import org.lgna.story.*;

import java.util.ArrayList;
import java.util.function.BiFunction;

/*
 * Used in OptionalMigrationManager when user requests to change SCamera into SVRUser.
 */
public class ReplaceCameraWithVR extends AstMigration {

  AbstractType<?, ?, ?> cameraType = JavaType.getInstance(SCamera.class);
  AbstractType<?, ?, ?> vrUserType = JavaType.getInstance(SVRUser.class);
  private final String sCamera = SCamera.class.getSimpleName();
  private final String getHeadset = "getHeadset";
  private final String setPositionRelativeToVehicle = "setPositionRelativeToVehicle";
  private final String setOrientationRelativeToVehicle = "setOrientationRelativeToVehicle";

  public ReplaceCameraWithVR() {
    super(ProjectVersion.getCurrentVersion());
  }

  // Since this migration is applied upon request it is always applicable
  @Override
  public boolean isApplicable(Version version) {
    return true;
  }

  @Override
  public final void migrate(Node root, final MigrationManager manager) {
    root.crawl(crawlable -> migrateNode(crawlable, manager), CrawlPolicy.COMPLETE, null);
  }

  public void migrateNode(Crawlable node, MigrationManager manager) {
    if (node instanceof UserField) {
      migrateField((UserField) node);
    }
    if (node instanceof UserLocal) {
      migrateType(((UserLocal) node).valueType);
    }
    if (node instanceof UserParameter) {
      migrateType(((UserParameter) node).valueType);
    }
    if (node instanceof MethodInvocation) {
      migrateMethod((MethodInvocation) node, manager);
    }
    // TODO Catch calls to setOrientation and setPosition and split across User and Headset
  }

  protected void migrateField(UserField field) {
    final AbstractType<?, ?, ?> oldFieldType = field.valueType.getValue();
    if (oldFieldType == null || !sCamera.equals(oldFieldType.getName())) {
      return;
    }
    field.valueType.setValue(vrUserType);
    if (field.initializer.getValue() instanceof InstanceCreation) {
      InstanceCreation instantiation = new InstanceCreation(vrUserType.getDeclaredConstructor());
      field.initializer.setValue(instantiation);
    }
    Logger.outln(String.format("Migrated field `%s` type from SCamera to SVRUser", field.getName()));
  }

  private void migrateType(DeclarationProperty<AbstractType<?, ?, ?>> property) {
    final AbstractType<?, ?, ?> oldLocalType = property.getValue();
    if (oldLocalType == null || !sCamera.equals(oldLocalType.getName())) {
      return;
    }
    property.setValue(vrUserType);
    Logger.outln("Converted type reference from SCamera to SVRUser");
  }

  private void migrateMethod(MethodInvocation invocation, MigrationManager manager) {
    AbstractMethod cameraMethod = invocation.method.getValue();
    boolean isVrUserTarget = vrUserType.equals(invocation.expression.getValue().getType());
    if (isVrUserTarget && cameraMethod.getName().equals(setPositionRelativeToVehicle)) {
      splitVrPositionStatement(invocation, manager);
    }
    if (isVrUserTarget && cameraMethod.getName().equals(setOrientationRelativeToVehicle)) {
      splitVrOrientationStatement(invocation, manager);
    }
    if (!(cameraMethod instanceof JavaMethod) || cameraMethod.getDeclaringType() != cameraType) {
      return;
    }
    AbstractType<?, ?, ?>[] paramTypes = getParameterTypes(cameraMethod);
    AbstractMethod vrUserMethod = vrUserType.findMethod(cameraMethod.getName(), paramTypes);
    invocation.method.setValue(vrUserMethod);
    replaceRequiredParamReferences(cameraMethod, vrUserMethod, invocation);
    replaceKeyedParamReferences(cameraMethod, vrUserMethod, invocation);
    Logger.outln(String.format("Changed from SCamera.%s to SVRUser.%s", cameraMethod.getName(), vrUserMethod.getName()));
  }

  private void splitVrOrientationStatement(MethodInvocation invocation, MigrationManager manager) {
    // Individual ExpressionStatement holding the invocation
    Node stmt = invocation.getParent();
    Node grandparent = stmt.getParent();
    if (grandparent instanceof BlockStatement) {
      BlockStatement block = (BlockStatement) grandparent;
      Expression orientationExp = invocation.requiredArguments.get(0).expression.getValue();
      if (orientationExp instanceof InstanceCreation) {
        InstanceCreatingVirtualMachine vm = new InstanceCreatingVirtualMachine();
        InstanceCreation creation = (InstanceCreation) orientationExp;
        final Object ori = vm.createInstance(creation);
        if (ori instanceof Orientation) {
          Orientation cameraOrientation = (Orientation) ori;
          OrthogonalMatrix3x3 matrix = EmployeesOnly.getOrthogonalMatrix3x3(cameraOrientation);
          EulerAngles angles = new EulerAngles(matrix);

          EulerAngles newCamAngles = new EulerAngles(new AngleInRadians(0), angles.yaw, new AngleInRadians(0), angles.order);
          UnitQuaternion newCamQuat = newCamAngles.createUnitQuaternion();
          SimpleArgumentListProperty args = creation.requiredArguments;
          args.get(0).expression.setValue(new DoubleLiteral(newCamQuat.x));
          args.get(1).expression.setValue(new DoubleLiteral(newCamQuat.y));
          args.get(2).expression.setValue(new DoubleLiteral(newCamQuat.z));
          args.get(3).expression.setValue(new DoubleLiteral(newCamQuat.w));

          EulerAngles headAngles = new EulerAngles(angles.pitch, new AngleInRadians(0), angles.roll, angles.order);
          UnitQuaternion headQuat = headAngles.createUnitQuaternion();

          AbstractMethod headsetMethod = vrUserType.findMethod(getHeadset);
          Expression getHeadsetExpression = new MethodInvocation(invocation.expression.getValue(), headsetMethod);
          AbstractMethod setOrientation = AstUtilities.lookupMethod(SVRHeadset.class, setOrientationRelativeToVehicle, Orientation.class, SetOrientationRelativeToVehicle.Detail[].class);

          JavaConstructor constructor = JavaConstructor.getInstance(Orientation.class, Number.class, Number.class, Number.class, Number.class);
          InstanceCreation headOrientation = AstUtilities.createInstanceCreation(constructor, new DoubleLiteral(headQuat.x), new DoubleLiteral(headQuat.y), new DoubleLiteral(headQuat.z), new DoubleLiteral(headQuat.w));

          manager.addFinalization(() -> block.statements.add(AstUtilities.createMethodInvocationStatement(getHeadsetExpression, setOrientation, headOrientation)));
          Logger.outln("Moved orientation from SCamera to SVRUser headset");
        }
      }
    }

  }

  private void splitVrPositionStatement(MethodInvocation invocation, MigrationManager manager) {
    // Individual ExpressionStatement holding the invocation
    Node stmt = invocation.getParent();

    Node grandparent = stmt.getParent();
    if (grandparent instanceof BlockStatement) {
      BlockStatement block = (BlockStatement) grandparent;
      Expression positionExp = invocation.requiredArguments.get(0).expression.getValue();
      if (positionExp instanceof InstanceCreation) {
        InstanceCreatingVirtualMachine vm = new InstanceCreatingVirtualMachine();
        InstanceCreation creation = (InstanceCreation) positionExp;
        final Object pos = vm.createInstance(creation);
        Runnable result;
        if (pos instanceof Position) {
          Position cameraPosition = (Position) pos;

          SimpleArgument arg = creation.requiredArguments.get(1);
          arg.expression.setValue(new DoubleLiteral(cameraPosition.getUp() - 1.56));

          AbstractMethod headsetMethod = vrUserType.findMethod(getHeadset);
          Expression getHeadsetExpression = new MethodInvocation(invocation.expression.getValue(), headsetMethod);
          AbstractMethod setPosition = AstUtilities.lookupMethod(SVRHeadset.class, setPositionRelativeToVehicle, Position.class, SetPositionRelativeToVehicle.Detail[].class);

          JavaConstructor constructor = JavaConstructor.getInstance(Position.class, Number.class, Number.class, Number.class);
          InstanceCreation headPosition = AstUtilities.createInstanceCreation(constructor, new DoubleLiteral(0.0), new DoubleLiteral(1.56), new DoubleLiteral(0.0));

          result = () -> block.statements.add(AstUtilities.createMethodInvocationStatement(getHeadsetExpression, setPosition, headPosition));
          manager.addFinalization(result);
          Logger.outln("Split position on SCamera between SVRUser and headset");
        }
      }
    }
  }

  private static AbstractType<?, ?, ?>[] getParameterTypes(AbstractMethod method) {
    AbstractParameter[] parameters = method.getAllParameters();
    AbstractType<?, ?, ?>[] paramTypes = new AbstractType<?, ?, ?>[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      paramTypes[i] = parameters[i].getValueType();
    }
    return paramTypes;
  }

  private void replaceRequiredParamReferences(AbstractMethod oldMethod, AbstractMethod newMethod, MethodInvocation invocation) {
    replaceParamReferences(oldMethod, newMethod, invocation.getRequiredArgumentsProperty().getValue(), this::findNewRequiredParam);
  }

  private void replaceKeyedParamReferences(AbstractMethod oldMethod, AbstractMethod newMethod, MethodInvocation invocation) {
    replaceParamReferences(oldMethod, newMethod, invocation.getKeyedArgumentsProperty().getValue(), this::findNewKeyedParam);
  }

  private void replaceParamReferences(AbstractMethod oldMethod, AbstractMethod newMethod, ArrayList<? extends AbstractArgument> args, BiFunction<JavaMethodParameter, AbstractMethod, AbstractParameter> filter) {
    for (AbstractArgument argument : args) {
      AbstractParameter param = argument.parameter.getValue();
      if (param instanceof JavaMethodParameter) {
        JavaMethodParameter javaParam = (JavaMethodParameter) param;
        if (oldMethod == javaParam.getCode()) {
          argument.parameter.setValue(filter.apply(javaParam, newMethod));
        }
      }
    }
  }

  private JavaMethodParameter findNewRequiredParam(JavaMethodParameter oldJavaParam, AbstractMethod newMethod) {
    for (AbstractParameter parameter : newMethod.getRequiredParameters()) {
      final JavaMethodParameter newJavaParam = (JavaMethodParameter) parameter;
      if (oldJavaParam.getIndex() == newJavaParam.getIndex()) {
        return newJavaParam;
      }
    }
    return null;
  }

  private AbstractParameter findNewKeyedParam(JavaMethodParameter oldJavaParam, AbstractMethod newMethod) {
    AbstractParameter keyParam = newMethod.getKeyedParameter();
    if (keyParam.getValueType() == oldJavaParam.getValueType()) {
      return keyParam;
    }
    return null;
  }
}
