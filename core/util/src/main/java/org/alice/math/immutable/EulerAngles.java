package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;

public record EulerAngles(Angle yaw, Angle pitch, Angle roll, EulerAngles.Order order) implements Orientation, BinaryEncodableAndDecodable {
  public static final EulerAngles IDENTITY = new EulerAngles(Angle.ZERO, Angle.ZERO, Angle.ZERO, Order.YAW_PITCH_ROLL);

  @Override
  public boolean isNaN() {
    return yaw.isNaN() || pitch.isNaN() || roll.isNaN();
  }

  @Override
  public boolean isIdentity() {
    return yaw.isZero() && pitch.isZero() && roll.isZero();
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    binaryEncoder.encodeRecord(this);
  }

  private enum CardinalRotation {
    PITCH() {
      @Override
      public Matrix3x3 applyRotation(Matrix3x3 m, EulerAngles ea) {
        return m.applyRotationAboutArbitraryAxis(Vector3.POSITIVE_X_AXIS, ea.pitch);
      }
    },
    YAW() {
      @Override
      public Matrix3x3 applyRotation(Matrix3x3 m, EulerAngles ea) {
        return m.applyRotationAboutArbitraryAxis(Vector3.POSITIVE_Y_AXIS, ea.yaw);
      }
    },
    ROLL() {
      @Override
      public Matrix3x3 applyRotation(Matrix3x3 m, EulerAngles ea) {
        return m.applyRotationAboutArbitraryAxis(Vector3.POSITIVE_Z_AXIS, ea.roll);
      }
    };
    public abstract Matrix3x3 applyRotation(Matrix3x3 m, EulerAngles ea);
  }

  public enum Order {
    PITCH_YAW_ROLL(CardinalRotation.PITCH, CardinalRotation.YAW, CardinalRotation.ROLL),
    YAW_ROLL_PITCH(CardinalRotation.YAW, CardinalRotation.ROLL, CardinalRotation.PITCH),
    ROLL_PITCH_YAW(CardinalRotation.ROLL, CardinalRotation.PITCH, CardinalRotation.YAW),
    PITCH_ROLL_YAW(CardinalRotation.PITCH, CardinalRotation.ROLL, CardinalRotation.YAW),
    YAW_PITCH_ROLL(CardinalRotation.YAW, CardinalRotation.PITCH, CardinalRotation.ROLL),
    ROLL_YAW_PITCH(CardinalRotation.ROLL, CardinalRotation.YAW, CardinalRotation.PITCH),
    NOT_APPLICABLE();

    CardinalRotation primary;
    CardinalRotation secondary;
    CardinalRotation tertiary;

    Order(CardinalRotation primary, CardinalRotation secondary, CardinalRotation tertiary) {
      this.primary = primary;
      this.secondary = secondary;
      this.tertiary = tertiary;
    }

    Order() {
    }

    public Matrix3x3 matrixFrom(EulerAngles ea) {
      Matrix3x3 m = Matrix3x3.IDENTITY;
      m = primary.applyRotation(m, ea);
      m = secondary.applyRotation(m, ea);
      return tertiary.applyRotation(m, ea);
    }
  }

  @Override
  public Matrix3x3 asMatrix3x3() {
    return order.matrixFrom(this);
  }

  @Override
  public UnitQuaternion asUnitQuaternion() {
    return asMatrix3x3().asUnitQuaternion();
  }

  @Override
  public AxisRotation asAxisRotation() {
    return asMatrix3x3().asAxisRotation();
  }

  @Override
  public EulerAngles asEulerAngles() {
    return this;
  }

  @Override
  public ForwardAndUpGuide asForwardAndUpGuide() {
    return asMatrix3x3().asForwardAndUpGuide();
  }

  public EulerAngles interpolate(EulerAngles ea0, EulerAngles ea1, double portion) {
    return new EulerAngles(
        ea0.yaw.interpolateToward(ea1.yaw, portion),
        ea0.pitch.interpolateToward(ea1.pitch, portion),
        ea0.roll.interpolateToward(ea1.roll, portion),
        ea0.order);
  }
}
