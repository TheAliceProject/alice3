package org.alice.tweedle.file;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import org.lgna.project.annotations.Visibility;

import java.util.*;

public class ModelManifest extends Manifest {

  public String parentClass;
  public List<String> rootJoints = new ArrayList<>();
  public List<JointBounds> jointBounds = new ArrayList<>();
  public List<Joint> additionalJoints = new ArrayList<>();
  public List<JointArray> additionalJointArrays = new ArrayList<>();
  public List<JointArrayId> additionalJointArrayIds = new ArrayList<>();
  public List<Pose> poses = new ArrayList<>();
  public BoundingBox boundingBox;
  public Boolean placeOnGround = true;
  public List<TextureSet> textureSets = new ArrayList<>();
  public List<ModelVariant> models = new ArrayList<>();

  public ModelManifest copyForExport() {
    ModelManifest copy = new ModelManifest();
    super.copyForExport(copy);
    copy.parentClass = parentClass;
    copy.rootJoints = new ArrayList<>(rootJoints);
    copy.additionalJoints = new ArrayList<>(additionalJoints);
    copy.additionalJointArrays = new ArrayList<>(additionalJointArrays);
    copy.additionalJointArrayIds = new ArrayList<>(additionalJointArrayIds);
    copy.poses = new ArrayList<>(poses);
    copy.boundingBox = boundingBox;
    copy.placeOnGround = placeOnGround;
    copy.textureSets = new ArrayList<>();
    for (TextureSet t : textureSets) {
      copy.textureSets.add(t.copyForExport());
    }
    copy.models = new ArrayList<>();
    for (ModelVariant m : models) {
      copy.models.add(m.copyForExport(getName()));
    }
    return copy;
  }

  public static class BoundingBox {
    public List<Float> min;
    public List<Float> max;
  }

  public static class ModelVariant {
    public String name;
    public String structure;
    public String textureSet;
    public String icon;

    public ModelVariant copyForExport(String modelName) {
      final ModelVariant copy = new ModelVariant();
      copy.name = modelName.equals(this.name) ? "DEFAULT" : this.name;
      copy.structure = structure;
      copy.textureSet = textureSet;
      copy.icon = icon;
      return copy;
    }
  }

  public static class TextureSet {
    public String name;
    public Map<Integer, String> idToResourceMap = new HashMap<>();

    TextureSet copyForExport() {
      final TextureSet copy = new TextureSet();
      copy.name = name;
      for (Integer key: idToResourceMap.keySet()) {
        copy.idToResourceMap.put(key, idToResourceMap.get(key));
      }
      return copy;
    }
  }

  public static class Joint {
    public String name;
    public String parent;
    public Visibility visibility;

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Joint) {
        Joint objJoint = (Joint) obj;
        return name.equals(objJoint.name) && (parent == null ? objJoint.parent == null : parent.equals(objJoint.parent));
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, parent);
    }

    @Override
    public String toString() {
      return name + (parent == null ? "" : " (->" + parent + ")");
    }
  }

  public static class JointArray {
    public String name;
    public Visibility visibility;
    public List<String> jointIds = new ArrayList<>();
  }

  public static class JointArrayId {
    public String name;
    public Visibility visibility;
    public String patternId;
    public String rootJoint;
  }

  public static class Pose {
    public String name;
    public List<JointTransform> transforms = new ArrayList<>();
  }

  public static class JointTransform {
    public String jointName;
    public List<Float> orientation;
    public List<Float> position;
  }

  public static class JointBounds {
    public String name;
    public BoundingBox bounds = new BoundingBox();

    public JointBounds(String jointName, AxisAlignedBox box) {
      name = jointName;
      bounds.max = box.getMaximum().getAsFloatList();
      bounds.min = box.getMinimum().getAsFloatList();
    }
  }

  public ModelVariant getModelVariant(String variantName) {
    for (ModelVariant variant : models) {
      if (variant.name.equals(variantName)) {
        return variant;
      }
    }
    return null;
  }

  public AliceTextureReference getAliceTextureReference(String textureName) {
    ResourceReference resource = getResource(textureName);
    if (resource instanceof AliceTextureReference) {
      return (AliceTextureReference) resource;
    }
    return null;
  }

  public StructureReference getStructure(String structureName) {
    ResourceReference resource = getResource(structureName);
    if (resource instanceof StructureReference) {
      return (StructureReference) resource;
    }
    return null;
  }

  public TextureSet getTextureSet(String textureSetName) {
    for (TextureSet textureSet : textureSets) {
      if (textureSet.name.equals(textureSetName)) {
        return textureSet;
      }
    }
    return null;
  }

  public void addBoundsForJoint(String jointName, AxisAlignedBox bounds) {
    if (bounds.isNaN()) {
      return;
    }
    jointBounds.add(new JointBounds(jointName, bounds));
  }
}
