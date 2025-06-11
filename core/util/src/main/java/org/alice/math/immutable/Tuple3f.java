package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public interface Tuple3f extends BinaryEncodableAndDecodable, Serializable {
  float x();
  float y();
  float z();

  //<editor-fold desc="Condition Checks">
  default boolean isZero() {
    return (x() == 0.0) && (y() == 0.0) && (z() == 0.0);
  }

  default boolean isNaN() {
    return Float.isNaN(x()) || Float.isNaN(y()) || Float.isNaN(z());
  }

  default boolean isWithinEpsilonOf(Tuple3f b, float epsilon) {
    return EpsilonUtilities.isWithinEpsilon(x(), b.x(), epsilon)
        && EpsilonUtilities.isWithinEpsilon(y(), b.y(), epsilon)
        && EpsilonUtilities.isWithinEpsilon(z(), b.z(), epsilon);
  }

  default boolean isWithinReasonableEpsilonOf(Tuple3f b) {
    return isWithinEpsilonOf(b, EpsilonUtilities.REASONABLE_EPSILON_FLOAT);
  }
  //</editor-fold>

  default double distanceSquaredFrom(Tuple3f b) {
    float xDelta = b.x() - x();
    float yDelta = b.y() - y();
    float zDelta = b.z() - z();
    return (xDelta * xDelta) + (yDelta * yDelta) + (zDelta * zDelta);
  }

  default double distanceFrom(Tuple3f b) {
    return Math.sqrt(distanceSquaredFrom(b));
  }

  default List<Float> asFloatList() {
    return Arrays.asList((float) x(), (float) y(), (float) z());
  }
}
