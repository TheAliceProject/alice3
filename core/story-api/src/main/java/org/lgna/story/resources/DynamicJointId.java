package org.lgna.story.resources;

import org.lgna.project.annotations.Visibility;

public class DynamicJointId extends JointId {

  private final String name;
  private Visibility visibility;

  public DynamicJointId(String jointName, JointId parent, Visibility visibility) {
    super(parent, null);
    this.name = jointName;
    this.visibility = visibility;
  }

  public DynamicJointId(String jointName, JointId parent) {
    this(jointName, parent, Visibility.PRIME_TIME);
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
}
