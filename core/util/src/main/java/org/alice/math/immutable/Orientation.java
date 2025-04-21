package org.alice.math.immutable;

import java.io.Serializable;

public interface Orientation extends Serializable {
    boolean isNaN();
    boolean isIdentity();
    boolean isAlignedWith(Orientation other);
    OrthogonalMatrix3x3 asMatrix3x3();
    UnitQuaternion asUnitQuaternion();
    AxisRotation asAxisRotation();
    EulerAngles asEulerAngles();
    // This one was not used
    ForwardAndUpGuide asForwardAndUpGuide();
}
