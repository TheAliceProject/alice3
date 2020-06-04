package org.lgna.story.resources;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import org.alice.tweedle.file.AliceTextureReference;
import org.alice.tweedle.file.ModelManifest;
import org.alice.tweedle.file.StructureReference;
import org.lgna.story.SThing;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.implementation.alice.JointImplementationAndVisualDataFactory;
import org.lgna.story.resourceutilities.StorytellingResources;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class DynamicResource<I extends JointedModelImp, T extends SThing> implements ModelStructure<I, T> {

  private final ModelManifest modelManifest;
  private final ModelManifest.ModelVariant modelVariant;

  private String[] tags;
  private String[] groupTags;
  private String[] themeTags;
  private JointId[] resourceSpecificJoints;
  private AxisAlignedBox bbox;

  public static DynamicResource createDynamicResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
    switch (modelManifest.parentClass) {
    case "Biped":
      return new DynamicBipedResource(modelManifest, modelVariant);
    case "Quadruped":
      return new DynamicQuadrupedResource(modelManifest, modelVariant);
    case "Flyer":
      return new DynamicFlyerResource(modelManifest, modelVariant);
    case "Slitherer":
      return new DynamicSlithererResource(modelManifest, modelVariant);
    case "Fish":
      return new DynamicFishResource(modelManifest, modelVariant);
    case "MarineMammal":
      return new DynamicMarineMammalResource(modelManifest, modelVariant);
    case "Prop":
      return new DynamicPropResource(modelManifest, modelVariant);
    case "Train":
      return new DynamicTrainResource(modelManifest, modelVariant);
    case "Automobile":
      return new DynamicAutomobileResource(modelManifest, modelVariant);
    case "Aircraft":
      return new DynamicAircraftResource(modelManifest, modelVariant);
    case "Watercraft":
      return new DynamicWatercraftResource(modelManifest, modelVariant);
    default:
      Logger.severe("Unknown dynamic class type: " + modelManifest.parentClass);
      return null;
    }
  }

  public DynamicResource(String modelName, String resourceName) {
    this.modelManifest = StorytellingResources.INSTANCE.getModelManifest(modelName);
    this.modelVariant = this.modelManifest.getModelVariant(resourceName);
    initialize();
  }

  public DynamicResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
    this.modelManifest = modelManifest;
    this.modelVariant = modelVariant;
    initialize();
  }

   @Override
   public String identifierFor(String resourceName) {
    return resourceName;
  }

  @Override
  public URL getThumbnailUrl(String variant) {
    try {
      return getIconURI().toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void initialize() {
    this.tags = modelManifest.description.tags.toArray(new String[modelManifest.description.tags.size()]);
    this.groupTags = modelManifest.description.groupTags.toArray(new String[modelManifest.description.groupTags.size()]);
    this.themeTags = modelManifest.description.themeTags.toArray(new String[modelManifest.description.themeTags.size()]);
    this.resourceSpecificJoints = createJointArray(modelManifest.additionalJoints);
    this.bbox = createBoundingBox();
  }

  private JointId[] createJointArray(List<ModelManifest.Joint> manifestJoints) {
    List<JointId> newJoints = new ArrayList<>();

    //Get all the base joints from the parent class. These will be used where the model specific joints reference parent joints
    //  that are defined on the parent class
    Class<? extends ModelResource> parentClass = AliceResourceClassUtilities.getResourceClassForAliceName(modelManifest.parentClass);
    List<JointId> baseJoints = AliceResourceClassUtilities.getJoints(parentClass);
    Map<String, JointId> jointMap = new HashMap<>();
    for (JointId jointId : baseJoints) {
      jointMap.put(jointId.toString(), jointId);
    }

    //Loop through a list of the joints and build new JointIds from them
    LinkedList<ModelManifest.Joint> jointsToProcess = new LinkedList<>(manifestJoints);
    while (jointsToProcess.size() > 0) {
      ModelManifest.Joint currentJoint = jointsToProcess.pop();
      //If we already have a JointId for the parent or the parent is null, make a new JointId and add it to the newJoints list and the jointMap
      if (currentJoint.parent == null || jointMap.containsKey(currentJoint.parent)) {
        JointId parent = currentJoint.parent != null ? jointMap.get(currentJoint.parent) : null;
        JointId newJoint = new DynamicJointId(currentJoint.name, parent, currentJoint.visibility);
        newJoints.add(newJoint);
        jointMap.put(newJoint.toString(), newJoint);
      } else {
        //If no parent is found, add it to the back of the queue
        jointsToProcess.push(currentJoint);
      }
    }

    return newJoints.toArray(new JointId[newJoints.size()]);
  }

  private AxisAlignedBox createBoundingBox() {
    StructureReference structure = modelManifest.getStructure(modelVariant.structure);
    ModelManifest.BoundingBox manifestBBox = null;
    if (structure != null && structure.boundingBox != null) {
      manifestBBox = structure.boundingBox;
    } else if (modelManifest.boundingBox != null) {
      manifestBBox = modelManifest.boundingBox;
    }
    if (manifestBBox != null) {
      return new AxisAlignedBox(manifestBBox.min.get(0), manifestBBox.min.get(1), manifestBBox.min.get(2), manifestBBox.max.get(0), manifestBBox.max.get(1), manifestBBox.max.get(2));
    }
    return AxisAlignedBox.createNaN();
  }

  @Override
  public JointedModelImp.JointImplementationAndVisualDataFactory getImplementationAndVisualFactory() {
    return JointImplementationAndVisualDataFactory.getInstance(this);
  }

  @Override
  public JointId[] getModelSpecificJoints() {
    return resourceSpecificJoints;
  }

  @Deprecated
  public JointId[] getRootJointIds() {
    return new JointId[0];
  }

  @Override
  public String[] getTags() {
    return tags;
  }

  @Override
  public String[] getGroupTags() {
    return groupTags;
  }

  @Override
  public String getModelClassName() {
    return modelManifest.description.name;
  }

  public String getModelVariantName() {
    return this.modelVariant.name;
  }

  @Override
  public String getInternalModelClassName() {
    return modelManifest.description.name;
  }

  @Override
  public String[] getThemeTags() {
    return themeTags;
  }

  @Override
  public AxisAlignedBox getBoundingBox() {
    return bbox;
  }

  @Override
  public boolean getPlaceOnGround() {
    return modelManifest.placeOnGround;
  }

  @Override
  public URI getIconURI() {
    File iconFile = modelManifest.getFile(modelVariant.icon);
    return iconFile.toURI();
  }

  @Override
  public URI getVisualURI() {
    StructureReference structureReference = modelManifest.getStructure(modelVariant.structure);
    File visualFile = modelManifest.getFile(structureReference.file);
    return visualFile.toURI();
  }

  @Override
  public URI getTextureURI() {
    ModelManifest.TextureSet textureSet = modelManifest.getTextureSet(modelVariant.textureSet);
    String textureResourceName = textureSet.idToResourceMap.values().iterator().next();
    AliceTextureReference textureReference = modelManifest.getAliceTextureReference(textureResourceName);
    File textureFile = modelManifest.getFile(textureReference.file);
    return textureFile.toURI();
  }

}
