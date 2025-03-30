package org.alice.math.immutable;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public interface Tuple3 extends Serializable {
  double x();
  double y();
  double z();

  default boolean isZero() {
    return (x() == 0.0) && (y() == 0.0) && (z() == 0.0);
  }

  default boolean isNaN() {
    return Double.isNaN(x()) || Double.isNaN(y()) || Double.isNaN(z());
  }

  default boolean isWithinEpsilonOf(Tuple3 b, double epsilon) {
    return EpsilonUtilities.isWithinEpsilon(x(), b.x(), epsilon)
        && EpsilonUtilities.isWithinEpsilon(y(), b.y(), epsilon)
        && EpsilonUtilities.isWithinEpsilon(z(), b.z(), epsilon);
  }

  default boolean isWithinEpsilonOfZero(double epsilon) {
    return isWithinEpsilonOf(Point3.ORIGIN, epsilon);
  }

  default boolean isWithinReasonableEpsilonOf(Tuple3 b) {
    return isWithinEpsilonOf(b, EpsilonUtilities.REASONABLE_EPSILON);
  }

  default boolean isWithinReasonableEpsilonOfZero() {
    return isWithinEpsilonOfZero(EpsilonUtilities.REASONABLE_EPSILON);
  }

  default List<Float> asFloatList() {
    return Arrays.asList((float) x(), (float) y(), (float) z());
  }
}
