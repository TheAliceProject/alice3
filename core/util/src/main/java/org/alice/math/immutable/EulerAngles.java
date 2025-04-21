package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;

public record EulerAngles(Angle pitch, Angle yaw, Angle roll, EulerAngles.Order order) implements Orientation, BinaryEncodableAndDecodable {
  public static final EulerAngles IDENTITY = new EulerAngles(Angle.ZERO, Angle.ZERO, Angle.ZERO, Order.YAW_PITCH_ROLL);

  //<editor-fold desc="Condition Checks">
  @Override
  public boolean isNaN() {
    return yaw.isNaN() || pitch.isNaN() || roll.isNaN();
  }

  @Override
  public boolean isIdentity() {
    return yaw.isZero() && pitch.isZero() && roll.isZero();
  }
  //</editor-fold>

  //<editor-fold desc="Comparisons">
  @Override
  public boolean isAlignedWith(Orientation other) {
    EulerAngles b = other.asEulerAngles();
    return order == b.order
        && yaw.isCloseTo(b.yaw)
        && pitch.isCloseTo(b.pitch)
        && roll.isCloseTo(b.roll);
  }
  //</editor-fold>

  //<editor-fold desc="Representation">
  private enum CardinalRotation {
    PITCH() {
      @Override
      public OrthogonalMatrix3x3 applyRotation(OrthogonalMatrix3x3 m, EulerAngles ea) {
        return m.applyRotationAboutArbitraryAxis(Vector3.POSITIVE_X_AXIS, ea.pitch);
      }
    },
    YAW() {
      @Override
      public OrthogonalMatrix3x3 applyRotation(OrthogonalMatrix3x3 m, EulerAngles ea) {
        return m.applyRotationAboutArbitraryAxis(Vector3.POSITIVE_Y_AXIS, ea.yaw);
      }
    },
    ROLL() {
      @Override
      public OrthogonalMatrix3x3 applyRotation(OrthogonalMatrix3x3 m, EulerAngles ea) {
        return m.applyRotationAboutArbitraryAxis(Vector3.POSITIVE_Z_AXIS, ea.roll);
      }
    };
    public abstract OrthogonalMatrix3x3 applyRotation(OrthogonalMatrix3x3 m, EulerAngles ea);
  }

  public enum Order {
    PITCH_YAW_ROLL(CardinalRotation.PITCH, CardinalRotation.YAW, CardinalRotation.ROLL),
    YAW_ROLL_PITCH(CardinalRotation.YAW, CardinalRotation.ROLL, CardinalRotation.PITCH),
    ROLL_PITCH_YAW(CardinalRotation.ROLL, CardinalRotation.PITCH, CardinalRotation.YAW),
    PITCH_ROLL_YAW(CardinalRotation.PITCH, CardinalRotation.ROLL, CardinalRotation.YAW),
    YAW_PITCH_ROLL(CardinalRotation.YAW, CardinalRotation.PITCH, CardinalRotation.ROLL) {
      @Override
      public OrthogonalMatrix3x3 matrixFrom(EulerAngles ea) {
        double theta = ea.yaw.getAsRadians();
        double phi = ea.pitch.getAsRadians();
        double psi = ea.roll.getAsRadians();
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);
        double cosPsi = Math.cos(psi);
        double sinPsi = Math.sin(psi);

        Vector3 right = new Vector3(cosPsi * cosTheta, sinPsi * cosTheta, -sinTheta);
        Vector3 up = new Vector3((cosPsi * sinTheta * sinPhi) - (sinPsi * cosPhi), (sinPsi * sinTheta * sinPhi) + (cosPsi * cosPhi), cosTheta * sinPhi);
        Vector3 backward = new Vector3((cosPsi * sinTheta * cosPhi) + (sinPsi * sinPhi), (sinPsi * sinTheta * cosPhi) - (cosPsi * sinPhi), cosTheta * cosPhi);
        return new OrthogonalMatrix3x3(right, up, backward);
      }
    },
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

    public OrthogonalMatrix3x3 matrixFrom(EulerAngles ea) {
      OrthogonalMatrix3x3 m = OrthogonalMatrix3x3.IDENTITY;
      m = primary.applyRotation(m, ea);
      m = secondary.applyRotation(m, ea);
      return tertiary.applyRotation(m, ea);
    }
  }
  //</editor-fold>

  //<editor-fold desc="Operations">
  public EulerAngles interpolate(EulerAngles ea0, EulerAngles ea1, double portion) {
    return new EulerAngles(
        ea0.pitch.interpolateToward(ea1.pitch, portion),
        ea0.yaw.interpolateToward(ea1.yaw, portion),
        ea0.roll.interpolateToward(ea1.roll, portion),
        ea0.order);
  }
  //</editor-fold>

  //<editor-fold desc="Orientation Conversions">
  @Override
  public OrthogonalMatrix3x3 asMatrix3x3() {
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
  //</editor-fold>

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    binaryEncoder.encodeRecord(this);
  }
}
