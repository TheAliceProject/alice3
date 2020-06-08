package org.lgna.story.resources;

import org.alice.serialization.tweedle.Encoder;
import org.lgna.project.annotations.Visibility;

public class DynamicJointId extends JointId {

  DynamicResource<?, ?> resource;
  private final String name;
  private Visibility visibility;

  public DynamicJointId(DynamicResource<?, ?> resource, String jointName, JointId parent, Visibility visibility) {
    super(parent, null);
    this.resource = resource;
    this.name = jointName;
    this.visibility = visibility;
  }

  public DynamicJointId(DynamicResource<?, ?> resource, String jointName, JointId parent) {
    this(resource, jointName, parent, Visibility.PRIME_TIME);
  }

  public void setVisibility(Visibility visibility) {
    this.visibility = visibility;
  }

  @Override
  public Visibility getVisibility() {
    return visibility;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public String getJointName(Encoder encoder) {
    return encoder.getUserJointIdentifier(name);
  }

  @Override
  public String getCodeIdentifier(Encoder encoder) {
    return encoder.getFieldReference(resource.getModelVariantName() + "Resource", getJointName(encoder));
  }
}
