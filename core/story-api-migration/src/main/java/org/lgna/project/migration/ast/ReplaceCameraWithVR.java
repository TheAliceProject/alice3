package org.lgna.project.migration.ast;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.Angle;
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
import org.lgna.story.Orientation;

import java.util.ArrayList;
import java.util.function.BiFunction;

/*
 * Used in OptionalMigrationManager when user requests to change SCamera into SVRUser.
 */
public class ReplaceCameraWithVR extends AstMigration {

  AbstractType<?, ?, ?> cameraType = JavaType.getInstance(SCamera.class);
  AbstractType<?, ?, ?> vrUserType = JavaType.getInstance(SVRUser.class);
  InstanceCreatingVirtualMachine vm = new InstanceCreatingVirtualMachine();
  private final String sCamera = SCamera.class.getSimpleName();
  private final String getHeadset = "getHeadset";
  private final String setPositionRelativeToVehicle = "setPositionRelativeToVehicle";
  private final String setOrientationRelativeToVehicle = "setOrientationRelativeToVehicle";
  private final AngleInRadians zero = new AngleInRadians(0);

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

  private void splitVrOrientationStatement(MethodInvocation setOrientationCall, MigrationManager manager) {
    // Get the ExpressionStatement holding the setOrientationCall
    Node stmt = setOrientationCall.getParent();
    // Get the block containing the statement
    Node grandparent = stmt.getParent();
    if (grandparent instanceof BlockStatement) {
      BlockStatement block = (BlockStatement) grandparent;
      Expression orientationExp = setOrientationCall.requiredArguments.get(0).expression.getValue();
      if (orientationExp instanceof InstanceCreation) {
        InstanceCreation creation = (InstanceCreation) orientationExp;
        final Object ori = vm.createInstance(creation);
        if (ori instanceof Orientation) {
          Orientation cameraOrientation = (Orientation) ori;

          UnitQuaternion vrUserOrientation = getVrUserOrientation(cameraOrientation);
          replaceOrientationArgs(creation, vrUserOrientation);

          UnitQuaternion headsetOrientation = getHeadsetOrientation(cameraOrientation);
          ExpressionStatement setHeadsetOrientation =
              setHeadsetOrientationStatement(setOrientationCall.expression.getValue(), headsetOrientation);
          manager.addFinalization(() -> block.statements.add(setHeadsetOrientation));

          Logger.outln("Moved orientation from SCamera to SVRUser headset");
        }
      }
    }

  }

  private UnitQuaternion getVrUserOrientation(Orientation cameraOrientation) {
    OrthogonalMatrix3x3 matrix = EmployeesOnly.getOrthogonalMatrix3x3(cameraOrientation);
    EulerAngles angles = new EulerAngles(matrix);

    Angle flatPitch = new AngleInRadians(nearestPi(angles.pitch));
    Angle flatRoll = new AngleInRadians(nearestPi(angles.roll));
    EulerAngles vrUserAngles = new EulerAngles(flatPitch, angles.yaw, flatRoll, angles.order);
    return vrUserAngles.createUnitQuaternion();
  }

  private double nearestPi(Angle angle) {
    double radians = angle.getAsRadians();

    int halfTurns = (int) (radians / Math.PI);
    // Set to absolute lower bound
    halfTurns = (radians < 0) ? halfTurns - 1 : halfTurns;

    // Pick closest half turn, below or above
    if (radians > (0.5 + halfTurns) * Math.PI) {
      halfTurns++;
    }
    return Math.PI * halfTurns;
  }

  private static void replaceOrientationArgs(InstanceCreation creation, UnitQuaternion newOrientation) {
    SimpleArgumentListProperty args = creation.requiredArguments;
    args.get(0).expression.setValue(new DoubleLiteral(newOrientation.x));
    args.get(1).expression.setValue(new DoubleLiteral(newOrientation.y));
    args.get(2).expression.setValue(new DoubleLiteral(newOrientation.z));
    args.get(3).expression.setValue(new DoubleLiteral(newOrientation.w));
  }

  private UnitQuaternion getHeadsetOrientation(Orientation cameraOrientation) {
    EulerAngles angles = new EulerAngles(EmployeesOnly.getOrthogonalMatrix3x3(cameraOrientation));
    Angle flatPitchOffset = new AngleInRadians(angles.pitch.getAsRadians() - nearestPi(angles.pitch));
    Angle flatRollOffset = new AngleInRadians(angles.roll.getAsRadians() - nearestPi(angles.roll));
    EulerAngles headsetAngles = new EulerAngles(flatPitchOffset, zero, flatRollOffset, angles.order);
    return headsetAngles.createUnitQuaternion();
  }

  private ExpressionStatement setHeadsetOrientationStatement(Expression userExpression, UnitQuaternion headsetOrientation) {
    AbstractMethod headsetMethod = vrUserType.findMethod(getHeadset);
    Expression getHeadsetExpression = new MethodInvocation(userExpression, headsetMethod);
    AbstractMethod setOrientation = AstUtilities.lookupMethod(SVRHeadset.class, setOrientationRelativeToVehicle, Orientation.class, SetOrientationRelativeToVehicle.Detail[].class);

    JavaConstructor constructor = JavaConstructor.getInstance(Orientation.class, Number.class, Number.class, Number.class, Number.class);
    InstanceCreation headOrientation = AstUtilities.createInstanceCreation(constructor, new DoubleLiteral(headsetOrientation.x), new DoubleLiteral(headsetOrientation.y), new DoubleLiteral(headsetOrientation.z), new DoubleLiteral(headsetOrientation.w));

    return AstUtilities.createMethodInvocationStatement(getHeadsetExpression, setOrientation, headOrientation);
  }

  private void splitVrPositionStatement(MethodInvocation invocation, MigrationManager manager) {
    // Individual ExpressionStatement holding the invocation
    Node stmt = invocation.getParent();

    Node grandparent = stmt.getParent();
    if (grandparent instanceof BlockStatement) {
      BlockStatement block = (BlockStatement) grandparent;
      Expression positionExp = invocation.requiredArguments.get(0).expression.getValue();
      if (positionExp instanceof InstanceCreation) {
        InstanceCreation creation = (InstanceCreation) positionExp;
        final Object pos = vm.createInstance(creation);
        Runnable result;
        if (pos instanceof Position) {
          Position cameraPosition = (Position) pos;

          SimpleArgument arg = creation.requiredArguments.get(1);
          double defaultHeight = 1.56;
          arg.expression.setValue(new DoubleLiteral(cameraPosition.getUp() - defaultHeight));

          AbstractMethod headsetMethod = vrUserType.findMethod(getHeadset);
          Expression getHeadsetExpression = new MethodInvocation(invocation.expression.getValue(), headsetMethod);
          AbstractMethod setPosition = AstUtilities.lookupMethod(SVRHeadset.class, setPositionRelativeToVehicle, Position.class, SetPositionRelativeToVehicle.Detail[].class);

          JavaConstructor constructor = JavaConstructor.getInstance(Position.class, Number.class, Number.class, Number.class);
          InstanceCreation headPosition = AstUtilities.createInstanceCreation(constructor, new DoubleLiteral(0.0), new DoubleLiteral(defaultHeight), new DoubleLiteral(0.0));

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
