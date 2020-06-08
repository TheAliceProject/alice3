package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.*;
import org.lgna.story.implementation.alice.AliceResourceUtilities;
import org.lgna.story.resources.JointId;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.DoubleBuffer;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class AliceModelLoader {

  private static final String destAliceResourceDirPrefix = "assets/alice/aliceModelResources/";

  private static void printJoints(Joint j, String indent) {
    System.out.println(indent + "Joint " + j.jointID.getValue());
    PrintUtilities.print(indent + "    local transform: ", j.localTransformation.getValue().translation, j.localTransformation.getValue().orientation);
    System.out.println();
    AffineMatrix4x4 absoluteTransform = j.getAbsoluteTransformation();
    PrintUtilities.print(indent + " absolute transform: ", absoluteTransform.translation, absoluteTransform.orientation);
    System.out.println();
    for (int i = 0; i < j.getComponentCount(); i++) {
      Component comp = j.getComponentAt(i);
      if (comp instanceof Joint) {
        printJoints((Joint) comp, indent + "  ");
      }
    }
  }

  private static void printMesh(Mesh mesh) {
    System.out.println("Mesh " + mesh.getName());
    System.out.println("  texture id: " + mesh.textureId.getValue());
    double[] vertices = BufferUtilities.convertDoubleBufferToArray(mesh.vertexBuffer.getValue());
    System.out.println("  vertex count " + vertices.length / 3);
    System.out.println("  vertices:");
    for (int i = 0; i < vertices.length; i += 3) {
      System.out.println("   (" + vertices[i] + ", " + vertices[i + 1] + ", " + vertices[i + 2] + ")");
    }

    System.out.println("  normals:");
    float[] normals = BufferUtilities.convertFloatBufferToArray(mesh.normalBuffer.getValue());
    for (int i = 0; i < normals.length; i += 3) {
      System.out.println("   (" + normals[i] + ", " + normals[i + 1] + ", " + normals[i + 2] + ")");
    }
  }

  private static void printWeightInfo(WeightInfo weightInfo) {
    System.out.println("Weight Info:");
    Map<String, InverseAbsoluteTransformationWeightsPair> mapReferencesToInverseAbsoluteTransformationWeightsPairs = weightInfo.getMap();
    for (Entry<String, InverseAbsoluteTransformationWeightsPair> pair : mapReferencesToInverseAbsoluteTransformationWeightsPairs.entrySet()) {
      InverseAbsoluteTransformationWeightsPair iatwp = pair.getValue();
      AffineMatrix4x4 inverseTransform = iatwp.getInverseAbsoluteTransformation();
      AffineMatrix4x4 originalTransform = AffineMatrix4x4.createInverse(inverseTransform);
      System.out.println("  joint: " + pair.getKey());
      System.out.println("  inverse absolute transform:");
      System.out.println("    t: " + inverseTransform.translation);
      System.out.println("    x: " + inverseTransform.orientation.right);
      System.out.println("    y: " + inverseTransform.orientation.up);
      System.out.println("    z: " + inverseTransform.orientation.backward);
      System.out.println("  original absolute transform:");
      System.out.println("    t: " + originalTransform.translation);
      System.out.println("    x: " + originalTransform.orientation.right);
      System.out.println("    y: " + originalTransform.orientation.up);
      System.out.println("    z: " + originalTransform.orientation.backward);
      //      System.out.println( "  weights:" );
      //      System.out.print(   "   ");
      //
      //      iatwp.reset();
      //      while (!iatwp.isDone()) {
      //        System.out.print( "("+iatwp.getIndex()+", "+iatwp.getWeight()+"),");
      //        iatwp.advance();
      //      }
      //      iatwp.reset();
      System.out.println();
    }
  }

  private static void printSkeletonVisual(SkeletonVisual sv) {
    System.out.println("Skeleton: ");
    printJoints(sv.skeleton.getValue(), "");
    System.out.println();
    System.out.println("BoundingBox:");
    System.out.println(sv.baseBoundingBox.getValue());
    //    for (edu.cmu.cs.dennisc.scenegraph.Geometry g : sv.geometries.getValue()) {
    //      if (g instanceof Mesh) {
    //        Mesh m = (Mesh)g;
    //        printMesh(m);
    //        System.out.println();
    //      }
    //    }
    //    for (WeightedMesh wm : sv.weightedMeshes.getValue()) {
    ////      printMesh(wm);
    //      printWeightInfo(wm.weightInfo.getValue());
    //      System.out.println();
    //    }
  }

  public static void translateSkeletonVisual(SkeletonVisual sv, Vector3 translation) {
    if (sv.skeleton.getValue() != null) {
      AffineMatrix4x4 rootTransform = sv.skeleton.getValue().localTransformation.getValue();
      rootTransform.translation.x += translation.x;
      rootTransform.translation.y += translation.y;
      rootTransform.translation.z += translation.z;
      sv.skeleton.getValue().localTransformation.setValue(rootTransform);
    }
    for (Geometry g : sv.geometries.getValue()) {
      if (g instanceof Mesh) {
        Mesh mesh = (Mesh) g;
        DoubleBuffer xyzs = mesh.vertexBuffer.getValue();
        double[] new_xyzs = new double[xyzs.capacity()];
        final int N = xyzs.limit();
        for (int i = 0; i < N; i += 3) {
          new_xyzs[i + 0] = xyzs.get(i + 0) + translation.x;
          new_xyzs[i + 1] = xyzs.get(i + 1) + translation.y;
          new_xyzs[i + 2] = xyzs.get(i + 2) + translation.z;
        }
        mesh.vertexBuffer.setValue(Utilities.createDoubleBuffer(new_xyzs));
      }
    }
    AxisAlignedBox bbox = sv.baseBoundingBox.getValue();
    bbox.setXMaximum(bbox.getXMaximum() + translation.x);
    bbox.setXMinimum(bbox.getXMinimum() + translation.x);
    bbox.setYMaximum(bbox.getYMaximum() + translation.y);
    bbox.setYMinimum(bbox.getYMinimum() + translation.y);
    bbox.setZMaximum(bbox.getZMaximum() + translation.z);
    bbox.setZMinimum(bbox.getZMinimum() + translation.z);
    sv.baseBoundingBox.setValue(bbox);
  }

  public static List<Field> getJointIdFields(Class<?> resourceClass) {
    Field[] fields = resourceClass.getDeclaredFields();
    List<Field> jointFields = new ArrayList<Field>();
    for (Field f : fields) {
      if (JointId.class.isAssignableFrom(f.getType())) {
        jointFields.add(f);
      }
    }
    return jointFields;
  }

  public static SkeletonVisual addMissingJoints(SkeletonVisual sv, Class<?> resourceClass) throws PipelineException {

    List<Field> requiredJointFields = getJointIdFields(resourceClass);

    if (sv.skeleton.getValue() == null) {
      System.err.println("NULL SKELETON!!");

      if (!requiredJointFields.isEmpty()) {
        System.err.println("No skeleton found on " + sv.getName() + " where its base class requires a skeleton. Base class is " + resourceClass);
        throw new PipelineException("No skeleton found on " + sv.getName() + " where its base class requires a skeleton. Base class is " + resourceClass);
      }
      return sv;
    }

    System.out.println("\nCHECKING FOR MISSING JOINTS ON " + sv.getName());

    List<JointId> missingJoints = new LinkedList<JointId>();
    Field[] fields = resourceClass.getDeclaredFields();
    for (Field f : requiredJointFields) {
      String fieldName = f.getName();
      if (sv.skeleton.getValue().getJoint(fieldName) == null) {
        try {
          JointId id = (JointId) f.get(null);
          missingJoints.add(id);
        } catch (IllegalAccessException iae) {
          iae.printStackTrace();
        }
      }
    }
    int index = 0;
    if (missingJoints.isEmpty()) {
      System.out.println("   NO MISSING JOINTS");
    }
    while (!missingJoints.isEmpty()) {
      System.out.println("   MISSING JOINTS:");
      for (JointId joint : missingJoints) {
        System.out.println("     " + joint);
      }
      JointId currentJoint = missingJoints.get(index);
      System.out.println("   TRYING TO AUTOMATICALLY ADD JOINT " + currentJoint);
      JointId parentJointID = currentJoint.getParent();
      if (parentJointID != null) {
        Joint parentJoint = sv.skeleton.getValue().getJoint(parentJointID.toString());
        if (parentJoint != null) {
          Joint newJoint = new Joint();
          newJoint.jointID.setValue(currentJoint.toString());
          newJoint.setParent(parentJoint);
          missingJoints.remove(index);

          System.out.println("   ADDED JOINT " + currentJoint.toString());
        } else {
          System.out.println("   NO JOINT FOUND ON SKELETON FOR " + parentJointID.toString() + ", SKIPPING IT");
          index = (index + 1) % missingJoints.size();
        }
      } else {
        System.out.println("   NO PARENT JOINT FOR " + currentJoint.toString() + ", CAN'T ADD IT");
      }
    }
    return sv;
  }

  public static String getAliceTextureName(String textureName) {
    textureName = FileUtilities.getBaseName(textureName);
    if (textureName.toLowerCase().endsWith("_diffuse")) {
      textureName = textureName.substring(0, textureName.toLowerCase().lastIndexOf("_diffuse"));
    }
    textureName = AliceResourceUtilities.trimName(textureName);
    textureName = textureName.replace(' ', '_');
    while (textureName.contains("__")) {
      textureName = textureName.replace("__", "_");
    }
    return textureName;
  }

  public static String getAliceResourceNameForImageFile(String modelResourceName, String textureName) {
    textureName = getAliceTextureName(textureName);
    return AliceResourceUtilities.getTextureResourceFileName(modelResourceName, textureName);
  }

  private static JPanel showVisual(SkeletonVisual sv, JPanel existingImagePanel) {
    BufferedImage image = AdaptiveRecenteringThumbnailMaker.getInstance(800, 600).createThumbnail(sv, false);
    JPanel imagePanel = existingImagePanel;
    if (imagePanel == null) {
      imagePanel = new JPanel();
    }
    imagePanel.setSize(image.getWidth(), image.getHeight());
    imagePanel.setBackground(Color.BLACK);
    ImageIcon icon = new ImageIcon(image);
    imagePanel.add(new JLabel(icon));
    JFrame frame = new JFrame();
    frame.getContentPane().add(imagePanel);
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    return imagePanel;
  }

  private static List<String> buildJointIDsList(Joint joint, List<String> jointIDs) {
    if (joint != null) {
      jointIDs.add(joint.jointID.getValue());
      for (Component c : joint.getComponents()) {
        if (c instanceof Joint) {
          buildJointIDsList((Joint) c, jointIDs);
        }
      }
    }
    return jointIDs;
  }

  private static void renameJoints(Joint joint, Map<String, String> idToNameMap) {
    if (joint != null) {
      String aliceName = idToNameMap.get(joint.jointID.getValue());
      if (aliceName != null) {
        joint.jointID.setValue(aliceName);
      }
      for (Component c : joint.getComponents()) {
        if (c instanceof Joint) {
          renameJoints((Joint) c, idToNameMap);
        }
      }
    }
  }

  public static void renameJoints(SkeletonVisual sv) {

    //Iterate through the skeleton to get a list of all the joint names for easy iteration
    List<String> jointIDs = buildJointIDsList(sv.skeleton.getValue(), new LinkedList<String>());
    Map<String, String> idToNameMap = new HashMap<>();
    for (String jointID : jointIDs) {
      String aliceJointName = PipelineNamingUtilities.getAliceJointNameForMayaJointName(jointID, false);
      idToNameMap.put(jointID, aliceJointName);
    }

    //Rename all the joints in the skeleton based on the new names
    renameJoints(sv.skeleton.getValue(), idToNameMap);
    //Replace all the references in the weighted meshes with the new joint names
    for (WeightedMesh wm : sv.weightedMeshes.getValue()) {
      WeightInfo oldWeightInfo = wm.weightInfo.getValue();
      Map<String, InverseAbsoluteTransformationWeightsPair> jointToWeightMap = oldWeightInfo.getMap();
      WeightInfo newWeightInfo = new WeightInfo();
      for (Entry<String, InverseAbsoluteTransformationWeightsPair> entry : jointToWeightMap.entrySet()) {
        String aliceJointName = idToNameMap.get(entry.getKey());
        newWeightInfo.addReference(aliceJointName, entry.getValue());
      }
      wm.weightInfo.setValue(newWeightInfo);
    }
  }

  private static List<Tuple2<String, String>> makeJointAndParentListFromSkeleton(Joint joint, List<Tuple2<String, String>> jointAndParentList) {
    if (joint != null) {
      String jointName = joint.jointID.getValue();
      String parentName = null;
      if (joint.getParent() instanceof Joint) {
        parentName = ((Joint) joint.getParent()).jointID.getValue();
      }
      jointAndParentList.add(Tuple2.createInstance(jointName, parentName));
      for (Component c : joint.getComponents()) {
        if (c instanceof Joint) {
          makeJointAndParentListFromSkeleton((Joint) c, jointAndParentList);
        }
      }
    }
    return jointAndParentList;
  }

  public static SkeletonVisual loadAliceModelFromCollada(File colladaModelFile, String modelName) {
    SkeletonVisual sv = null;
    try {
      Logger modelLogger = Logger.getLogger("org.lgna.story.resourceutilities.AliceColladaModelLoader");
      JointedModelColladaImporter colladaImporter = new JointedModelColladaImporter(colladaModelFile, modelLogger);
      sv = colladaImporter.loadSkeletonVisual();
    } catch (ModelLoadingException e) {
      e.printStackTrace();
    }
    return sv;
  }

}
