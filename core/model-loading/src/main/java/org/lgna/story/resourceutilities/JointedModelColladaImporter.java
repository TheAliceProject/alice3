package org.lgna.story.resourceutilities;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.controller.Controller;
import com.dddviewr.collada.controller.Skin;
import com.dddviewr.collada.controller.VertexWeights;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.NewParam;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.geometry.Primitives;
import com.dddviewr.collada.geometry.Triangles;
import com.dddviewr.collada.images.Image;
import com.dddviewr.collada.materials.InstanceEffect;
import com.dddviewr.collada.materials.LibraryMaterials;
import com.dddviewr.collada.materials.Material;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.*;
import com.jogamp.common.nio.Buffers;
import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.math.*;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.*;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import edu.cmu.cs.dennisc.texture.Texture;
import org.lgna.story.implementation.JointedModelImp.VisualData;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointedModelResource;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JointedModelColladaImporter {
  private final File colladaModelFile;
  private final Logger modelLoadingLogger;
  private final File rootPath;
  private Orientation orientation;

  public JointedModelColladaImporter(File colladaModelFile, Logger modelLoadingLogger) {
    this.colladaModelFile = colladaModelFile;
    this.modelLoadingLogger = modelLoadingLogger;
    this.rootPath = colladaModelFile.getParentFile();
  }

  private static boolean nodeIsJoint(Node n) {
    return (n.getType() != null && n.getType().equals("JOINT"));
  }

  private Node findNodeNamedRoot(List<Node> nodes) {
    for (Node n : nodes) {
      if (nodeIsJoint(n) && getJointIdentifier(n).equalsIgnoreCase("root")) {
        return n;
      }
    }
    for (Node n : nodes) {
      Node childRoot = findNodeNamedRoot(n.getChildNodes());
      if (childRoot != null) {
        return childRoot;
      }
    }
    return null;
  }

  private static Node findFirstJoint(List<Node> nodes) {
    for (Node n : nodes) {
      if (nodeIsJoint(n)) {
        return n;
      }
    }
    for (Node n : nodes) {
      Node childRoot = findFirstJoint(n.getChildNodes());
      if (childRoot != null) {
        return childRoot;
      }
    }
    return null;
  }

  private AffineMatrix4x4 collapsedRootTransform(List<Node> nodes, Node root) throws ModelLoadingException {
    if (nodes.contains(root)) {
      return AffineMatrix4x4.createIdentity();
    }
    for (Node n : nodes) {
      AffineMatrix4x4 childTransform = collapsedRootTransform(n.getChildNodes(), root);
      if (childTransform != null) {
        final AffineMatrix4x4 nodeTransform = getNodeTransform(n);
        nodeTransform.multiply(childTransform);
        return nodeTransform;
      }
    }
    return null;
  }

  private Node findRootNode(List<Node> nodes) {
    Node rootNode = findNodeNamedRoot(nodes);
    if (rootNode != null) {
      return rootNode;
    }
    rootNode = findFirstJoint(nodes);
    if (rootNode != null) {
      return rootNode;
    }
    //No joints are found, so return null
    return null;
  }

  private AffineMatrix4x4 floatArrayToAliceMatrix(float[] floatData) throws ModelLoadingException {
    double[] doubleData = new double[floatData.length];
    for (int i = 0; i < floatData.length; i++) {
      doubleData[i] = floatData[i];
    }
    AffineMatrix4x4 srcMatrix;
    if (doubleData.length == 12) {
      srcMatrix = AffineMatrix4x4.createFromRowMajorArray12(doubleData);
    } else if (doubleData.length == 16) {
      srcMatrix = AffineMatrix4x4.createFromRowMajorArray16(doubleData);
    } else {
      throw new ModelLoadingException("Error converting collada matrix to Alice matrix. Expected array of size 12 or 16, instead got " + floatData.length);
    }
    return orientation.orientMatrixToAlice(srcMatrix);
  }

  private AffineMatrix4x4 colladaMatrixToAliceMatrix(Matrix m) throws ModelLoadingException {
    return floatArrayToAliceMatrix(m.getData());
  }

  private Joint createAliceSkeletonFromNode(Node node) throws ModelLoadingException {
    Joint j = new Joint();
    j.jointID.setValue(getJointIdentifier(node));
    j.setName(getJointIdentifier(node));

    j.localTransformation.setValue(getNodeTransform(node));
    for (Node child : node.getChildNodes()) {
      if (nodeIsJoint(child)) {
        Joint childJoint = createAliceSkeletonFromNode(child);
        childJoint.setParent(j);
      }
    }
    return j;
  }

  private String getJointIdentifier(Node node) {
    String sid = node.getSid();
    String name = node.getName();
    if (sid == null || "".equals(sid)) {
      return name;
    }
    if (name != null && !"".equals(name) && !name.equals(sid)) {
      modelLoadingLogger.log(Level.WARNING, "Incoming Joint has scoped ID (sid) '" + sid + "' that differs from name '" + name + "'. Using sid as identifier. This may conflict with export assumptions.");
    }
    return sid;
  }

  private AffineMatrix4x4 getNodeTransform(Node node) throws ModelLoadingException {
    AffineMatrix4x4 aliceMatrix = AffineMatrix4x4.createIdentity();
    for (int i = 0; i < node.getXforms().size(); i++) {
      BaseXform xform = node.getXforms().get(i);
      if (xform instanceof Matrix) {
        aliceMatrix = colladaMatrixToAliceMatrix((Matrix) xform);
      } else if (xform instanceof Translate) {
        Translate translate = (Translate) xform;
        // TODO orient to Alice
        aliceMatrix.translation.set(translate.getX(), translate.getY(), translate.getZ());
      } else if (xform instanceof Scale) {
        Scale scale = (Scale) xform;
        // TODO orient to Alice
        OrthogonalMatrix3x3 scaleMatrix = new OrthogonalMatrix3x3(new Vector3(scale.getX(), 0, 0), new Vector3(0, scale.getY(), 0), new Vector3(0, 0, scale.getZ()));
        aliceMatrix.orientation.applyMultiplication(scaleMatrix);
      } else if (xform instanceof Rotate) {
        Rotate rotate = (Rotate) xform;
        // TODO orient to Alice
        Vector3 axis = new Vector3(rotate.getX(), rotate.getY(), rotate.getZ());
        aliceMatrix.orientation.applyRotationAboutArbitraryAxis(axis, new AngleInDegrees(rotate.getAngle()));
      }
    }
    return aliceMatrix;
  }

  private static int getMaterialIndex(String materialId, Collada colladaModel) {
    if (materialId == null) {
      return -1;
    }
    int index = 0;
    for (Material material : colladaModel.getLibraryMaterials().getMaterials()) {
      if (materialId.equals(material.getId())) {
        return index;
      }
      index++;
    }
    return -1;
  }

  private static Controller getControllerForGeometry(Geometry geometry, Collada colladaModel) {
    if (colladaModel.getLibraryControllers() != null && colladaModel.getLibraryControllers().getControllers() != null) {
      for (Controller controller : colladaModel.getLibraryControllers().getControllers()) {
        Geometry foundGeometry = colladaModel.findGeometry(controller.getSkin().getSource());
        if (foundGeometry == geometry) {
          return controller;
        }
      }
    }
    return null;
  }

  private WeightInfo createWeightInfoForController(Controller meshController) throws ModelLoadingException {
    /*
     *  Here is an example of a more complete <vertex_weights> element. Note that the <vcount> element
      says that the first vertex has 3 bones, the second has 2, etc. Also, the <v> element says that the first
      vertex is weighted with weights[0] towards the bind shape, weights[1] towards bone 0, and
      weights[2] towards bone 1:
     *  From the Collada 1.4.1 spec: (Alice, MAYA, and Blender use the 1.4.1 schema for export)
      <controller id="skin">
        <skin source="#base_mesh">
          <source id="Joints">
            <Name_array count="4"> Root Spine1 Spine2 Head </Name_array>
            ...
          </source>
          <source id="Weights">
            <float_array count="4"> 0.0 0.33 0.66 1.0 </float_array>
            ...
          </source>
          <source id="Inv_bind_mats">
            <float_array count="64"> ... </float_array>
            ...
          </source>
          <joints>
            <input semantic="JOINT" source="#Joints"/>
            <input semantic="INV_BIND_MATRIX" source="#Inv_bind_mats"/>
          </joints>
          <vertex_weights count="4">
            <input semantic="JOINT" source="#Joints"/>
            <input semantic="WEIGHT" source="#Weights"/>
            <vcount>3 2 2 3</vcount>
            <v>
            -1 0 0 1 1 2
            -1 3 1 4
            -1 3 2 4
            -1 0 3 1 2 2
            </v>
          </vertex_weights>
        </skin>
      </controller>
     */
    Skin skin = meshController.getSkin();
    Map<Integer, float[]> jointWeightMap = getJointWeightMap(skin);
    //Take the weight data and convert it to WeightInfo
    String[] jointData = skin.getJointData();
    if (jointData == null) {
      throw new ModelLoadingException("Error converting mesh " + meshController.getName() + ", no joint data found on mesh skin.");
    }

    //Array of inverse bind matrices for all the referenced joints. Is indexed into by the joint index.
    float[] inverseBindMatrixData = skin.getInvBindMatrixData();
    if (inverseBindMatrixData == null) {
      throw new ModelLoadingException("Error converting mesh " + meshController.getName() + ", no inverse bind matrix data found on mesh skin.");
    }
    WeightInfo weightInfo = new WeightInfo();
    for (Entry<Integer, float[]> jointAndWeights : jointWeightMap.entrySet()) {
      int jointIndex = jointAndWeights.getKey();
      String jointId = jointData[jointIndex];
      // The Inverse Bind Matrix for jointIndex i. IBMi in the Collada spec.
      float[] inverseBindMatrix = Arrays.copyOfRange(inverseBindMatrixData, 16 * jointIndex, 16 * jointIndex + 16);
      AffineMatrix4x4 aliceInverseBindMatrix = floatArrayToAliceMatrix(inverseBindMatrix);
      InverseAbsoluteTransformationWeightsPair iawp = InverseAbsoluteTransformationWeightsPair.createInverseAbsoluteTransformationWeightsPair(jointAndWeights.getValue(), aliceInverseBindMatrix);
      if (iawp != null) {
        weightInfo.addReference(jointId, iawp);
      }
    }
    return weightInfo;
  }

  private static Map<Integer, float[]> getJointWeightMap(Skin skin) {
    VertexWeights vertexWeights = skin.getVertexWeights();
    float[] weightData = skin.getWeightData();
    int[] vertexWeightData = vertexWeights.getData();
    Map<Integer, float[]> jointWeightMap = new HashMap<>();
    int entryCount = 0;
    //Loop through the vertex weights and build a map of joints (stored as their indices to the joint data) to
    // arrays of weight info.
    //The weight info is stored as arrays of weights where the index of the entry is the index of the joint and
    // the weight values are the weighting of that vertex to the joint
    for (int vertex = 0; vertex < vertexWeights.getCount(); vertex++) {
      int boneCount = vertexWeights.getVcount().getData()[vertex];
      for (int bone = 0; bone < boneCount; bone++) {
        int jointIndex = vertexWeightData[entryCount * 2];
        int weightIndex = vertexWeightData[entryCount * 2 + 1];

        float[] weightArray;
        if (jointWeightMap.containsKey(jointIndex)) {
          weightArray = jointWeightMap.get(jointIndex);
        } else {
          weightArray = new float[vertexWeights.getCount()];
          jointWeightMap.put(jointIndex, weightArray);
        }
        weightArray[vertex] = weightData[weightIndex];
        entryCount++;
      }
    }
    return jointWeightMap;
  }

  private Mesh createAliceSGMeshFromGeometry(Geometry geometry, Collada colladaModel) throws ModelLoadingException {

    Controller meshController = getControllerForGeometry(geometry, colladaModel);
    Mesh sgMesh;
    if (meshController != null) {
      sgMesh = new WeightedMesh();
    } else {
      sgMesh = new Mesh();
    }
    sgMesh.setName(geometry.getName());
    final float[] normals = geometry.getMesh().getNormalData();
    if (normals == null) {
      throw new ModelLoadingException("No normal data found in model.");
    }
    orientation.orientNormals(normals, sgMesh.normalBuffer);

    float[] colladaVertices = geometry.getMesh().getPositionData();
    double[] doubleVertexData = orientation.orientVertices(colladaVertices, sgMesh.vertexBuffer);
    final float[] coordData = geometry.getMesh().getTexCoordData();
    if (coordData == null) {
      throw new ModelLoadingException("No texture coordinate data found in model.");
    }
    sgMesh.textCoordBuffer.setValue(Buffers.newDirectFloatBuffer(coordData));

    //Find the triangle data and use it to set the index data
    Triangles tris = null;
    for (Primitives p : geometry.getMesh().getPrimitives()) {
      if (p instanceof Triangles) {
        if (tris == null) {
          tris = (Triangles) p;
        } else {
          modelLoadingLogger.log(Level.WARNING, "Converting mesh '" + geometry.getName() + "': Unsupported primitive count: Found extra triangle primitives, only processing the first.");
        }
      } else {
        modelLoadingLogger.log(Level.WARNING, "Converting mesh '" + geometry.getName() + "': Unsupported primitive type " + p.getClass() + ". Skipping this geometry.");
      }
    }
    if (tris == null) {
      modelLoadingLogger.log(Level.WARNING, "Error converting mesh " + geometry.getName() + ". No triangle primitive data found. Skipping entire mesh.");
      return null;
    }
    sgMesh.indexBuffer.setValue(Buffers.newDirectIntBuffer(tris.getData()));
    // TODO Remove this early binding. The material on a piece of geometry references an instance_material by symbol.
    // The instance_material, which is specific to an instance_controller on a node, uses target to reference the
    // material in library_materials by id. The following line bypasses the instance_material, requiring it to have
    // identical symbol and target and forcing all nodes to use the same material.
    // TODO Stop using the index as the ID.
    sgMesh.textureId.setValue(getMaterialIndex(tris.getMaterial(), colladaModel));

    if (sgMesh instanceof WeightedMesh) {
      recordWeights((WeightedMesh) sgMesh, meshController, doubleVertexData);
    }
    return sgMesh;
  }

  private void recordWeights(WeightedMesh sgMesh, Controller meshController, double vertices[]) throws ModelLoadingException {
    //Since this is a weighted mesh, we need to transform the mesh data into the bind space
    float[] bindMatrixData = meshController.getSkin().getBindShapeMatrix();
    if (bindMatrixData != null) {
      AffineMatrix4x4 bindMatrix = floatArrayToAliceMatrix(bindMatrixData);
      double[] bindSpaceVertices = new double[vertices.length];
      for (int i = 0; i < vertices.length; i += 3) {
        bindMatrix.transformVertex(bindSpaceVertices, i, vertices, i);
      }
      sgMesh.vertexBuffer.setValue(Buffers.newDirectDoubleBuffer(bindSpaceVertices));
    }
    sgMesh.weightInfo.setValue(createWeightInfoForController(meshController));
  }

  private List<Mesh> createAliceMeshesFromCollada(Collada colladaModel) throws ModelLoadingException {
    List<Geometry> geometries = colladaModel.getLibraryGeometries().getGeometries();
    List<Mesh> meshes = new ArrayList<Mesh>();
    for (Geometry geometry : geometries) {
      Mesh mesh = createAliceSGMeshFromGeometry(geometry, colladaModel);
      if (mesh != null) {
        meshes.add(mesh);
      }
    }
    return meshes;
  }

  public static SkeletonVisual loadAliceModel(JointedModelResource resource) {
    VisualData<JointedModelResource> v = ImplementationAndVisualType.ALICE.getFactory(resource).createVisualData();
    SkeletonVisual sv = (SkeletonVisual) v.getSgVisuals()[0];
    return sv;
  }

  private static Texture getAliceTexture(BufferedImage image) {
    boolean flipImage = true;
    BufferedImageTexture aliceTexture = new BufferedImageTexture();
    BufferedImage tex;
    if (flipImage) {
      try {
        int type;
        if (image.getTransparency() == BufferedImage.OPAQUE) {
          type = BufferedImage.TYPE_3BYTE_BGR;
        } else {
          type = BufferedImage.TYPE_4BYTE_ABGR;
        }
        tex = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        return null;
      }
      image.getWidth(null);
      image.getHeight(null);

      if (image instanceof BufferedImage) {
        int imageWidth = image.getWidth(null);
        int[] tmpData = new int[imageWidth];
        int row = 0;
        BufferedImage bufferedImage = ((BufferedImage) image);
        for (int y = image.getHeight(null) - 1; y >= 0; y--) {
          bufferedImage.getRGB(0, (flipImage ? row++ : y), imageWidth, 1, tmpData, 0, imageWidth);
          tex.setRGB(0, y, imageWidth, 1, tmpData, 0, imageWidth);
        }
      } else {
        AffineTransform tx = null;
        if (flipImage) {
          tx = AffineTransform.getScaleInstance(1, -1);
          tx.translate(0, -image.getHeight(null));
        }
        Graphics2D g = (Graphics2D) tex.getGraphics();
        g.drawImage(image, tx, null);
        g.dispose();
      }

    } else {
      tex = (BufferedImage) image;
    }
    aliceTexture.setBufferedImage(tex);
    return aliceTexture;
  }

  private List<TexturedAppearance> createAliceMaterialsFromCollada(Collada colladaModel, File rootPath, List<Mesh> aliceMeshes) throws ModelLoadingException {
    List<TexturedAppearance> textureAppearances = new LinkedList<TexturedAppearance>();
    final LibraryMaterials libraryMaterials = colladaModel.getLibraryMaterials();
    if (libraryMaterials != null) {
      List<Material> materials = libraryMaterials.getMaterials();
      for (Material material : materials) {
        int index = getMaterialIndex(material.getId(), colladaModel);
        boolean isUsed = false;
        for (Mesh aliceMesh : aliceMeshes) {
          if (aliceMesh.textureId.getValue() == index) {
            isUsed = true;
            break;
          }
        }
        if (isUsed) {
          Image image = getColladaImageForMaterial(material, colladaModel);
          if (image == null) {
            throw new ModelLoadingException("Error loading material " + material.getId() + ": No valid image found.");
          }
          BufferedImage bufferedImage;
          try {
            File imageFile = resolveTextureFileName(image.getInitFrom(), rootPath);
            if (imageFile == null) {
              throw new ModelLoadingException("Error loading texture: File '" + image.getInitFrom() + "' not found.");
            }
            bufferedImage = ImageUtilities.read(imageFile);
            if (bufferedImage == null) {
              throw new ModelLoadingException("Error loading texture: File '" + image.getInitFrom() + "' not readable.");
            }
            bufferedImage = ImageUtilities.stretchToPowersOfTwo(bufferedImage);
          } catch (IOException e) {
            throw new ModelLoadingException("Error loading texture: " + image.getInitFrom() + " not found.", e);
          } catch (RuntimeException e) {
            throw new ModelLoadingException("Error loading texture: " + image.getInitFrom() + ".\n" + e.getMessage(), e);
          }
          TexturedAppearance m_sgAppearance = new TexturedAppearance();
          m_sgAppearance.diffuseColorTexture.setValue(getAliceTexture(bufferedImage));
          m_sgAppearance.textureId.setValue(index);
          textureAppearances.add(m_sgAppearance);
        } else {
          modelLoadingLogger.log(Level.WARNING, "Loading materials: Skipping unreferenced material " + material.getId());
        }
      }
    }
    if (textureAppearances.size() == 0) {
      throw new ModelLoadingException("No supported materials found. Alice models must have a texture.");
    }
    return textureAppearances;
  }

  private static File resolveTextureFileName(String textureFileName, File rootPath) {
    //Strip URI info. This seems to be inconsistent with java.net.URI and therefore it's easier to discard it
    if (textureFileName.startsWith("file://")) {
      textureFileName = textureFileName.substring(7);
    } else if (textureFileName.startsWith("file:///")) {
      textureFileName = textureFileName.substring(8);
    }

    //Check for texture file
    File textureFile = new File(textureFileName);
    if (textureFile.exists()) {
      return textureFile;
    }
    //If texture file isn't found, check in the root directory (the directory where the collada file is loaded from)
    File localFile = new File(rootPath, textureFile.getName());
    if (localFile.exists()) {
      return localFile;
    }
    return null;
  }

  private Image getColladaImageForMaterial(Material material, Collada colladaModel) {
    InstanceEffect ie = material.getInstanceEffect();
    Effect effect = colladaModel.findEffect(ie.getUrl());
    if (effect == null) {
      modelLoadingLogger.warning("Error loading material '" + material.getId() + "': No effect found for url '" + ie.getUrl() + "'");
      return null;
    }
    if (effect.getEffectMaterial() == null) {
      modelLoadingLogger.warning("Error loading material '" + material.getId() + "': No effect material found for '" + effect.getId() + "'");
      return null;
    } else if (effect.getEffectMaterial().getDiffuse() == null) {
      modelLoadingLogger.warning("Error loading material '" + material.getId() + "': No diffuse value found for effect '" + effect.getId() + "'");
      return null;
    } else if (effect.getEffectMaterial().getDiffuse().getTexture() == null) {
      modelLoadingLogger.warning("Error loading material '" + material.getId() + "': No diffuse texture found for effect '" + effect.getId() + "'");
      return null;
    } else if (effect.getEffectMaterial().getDiffuse().getTexture().getTexture() == null) {
      modelLoadingLogger.warning("Error loading material '" + material.getId() + "': No diffuse texture value found for effect '" + effect.getId() + "'");
      return null;
    }
    String textureId = effect.getEffectMaterial().getDiffuse().getTexture().getTexture();

    NewParam textureParam = effect.findNewParam(textureId);
    while (textureParam != null) {
      if (textureParam.getSurface() != null) {
        textureId = textureParam.getSurface().getInitFrom();
        textureParam = null;
        break;
      } else if (textureParam.getSampler2D() != null) {
        textureParam = effect.findNewParam(textureParam.getSampler2D().getSource());
      } else {
        textureParam = null;
      }
    }

    return colladaModel.findImage(textureId);
  }

  private Collada readColladaModel() throws ModelLoadingException {
    Collada colladaModel;
    try {
      colladaModel = Collada.readFile(colladaModelFile.getAbsolutePath());
    } catch (SAXException | IOException e) {
      throw new ModelLoadingException("Failed to load collada file " + colladaModelFile, e);
    } catch (ClassCastException e) {
      throw new ModelLoadingException("Failed to load collada file " + colladaModelFile + ".\nIf there are nested animations they should be removed.", e);
    }
    orientation = Orientation.forUpAxis(colladaModel.getUpAxis());
    colladaModel.deindexMeshes();
    return colladaModel;
  }

  public SkeletonVisual loadSkeletonVisual() throws ModelLoadingException {
    assert modelLoadingLogger != null;

    Collada colladaModel = readColladaModel();

    VisualScene scene = colladaModel.getLibraryVisualScenes().getScene(colladaModel.getScene().getInstanceVisualScene().getUrl());
    if (scene == null) {
      throw new ModelLoadingException("Error processing model: No scene found.");
    }
    //Find and build the skeleton first
    Node rootNode = findRootNode(scene.getNodes());
    Joint aliceSkeleton = null;
    if (rootNode != null) {
      aliceSkeleton = createAliceSkeletonFromNode(rootNode);
      AffineMatrix4x4 rootTransform = collapsedRootTransform(scene.getNodes(), rootNode);
      if (rootTransform != null && !rootTransform.isIdentity()) {
        rootTransform.multiply(aliceSkeleton.getLocalTransformation());
        aliceSkeleton.setLocalTransformation(rootTransform);
      }
    }

    //Find and build meshes (both static and weighted) from the collada model
    List<Mesh> aliceMeshes = createAliceMeshesFromCollada(colladaModel);
    if (aliceMeshes.size() == 0) {
      throw new ModelLoadingException("Error processing model: No valid meshes found.");
    }

    SkeletonVisual skeletonVisual = new SkeletonVisual();
    skeletonVisual.setName(baseFileName(colladaModelFile.getName()));
    skeletonVisual.frontFacingAppearance.setValue(new SimpleAppearance());
    skeletonVisual.skeleton.setValue(aliceSkeleton);
    List<Mesh> aliceGeometry = new ArrayList<Mesh>();
    List<WeightedMesh> aliceWeightedMeshes = new ArrayList<WeightedMesh>();
    //Loop through the meshes and divide them into lists of regular meshes and weighted meshes
    for (Mesh mesh : aliceMeshes) {
      if (mesh instanceof WeightedMesh) {
        WeightedMesh weightedMesh = (WeightedMesh) mesh;
        //Link the weighted mesh to the skeleton
        weightedMesh.skeleton.setValue(aliceSkeleton);
        aliceWeightedMeshes.add(weightedMesh);
      } else {
        aliceGeometry.add(mesh);
      }
    }

    skeletonVisual.geometries.setValue(aliceGeometry.toArray(new Mesh[aliceGeometry.size()]));
    skeletonVisual.weightedMeshes.setValue(aliceWeightedMeshes.toArray(new WeightedMesh[aliceWeightedMeshes.size()]));

    float extraScale = colladaModel.getUnit().getMeter();
    if (extraScale != 1.0f) {
      skeletonVisual.scale(new Vector3(extraScale, extraScale, extraScale));
    }

    List<TexturedAppearance> sgTextureAppearances = createAliceMaterialsFromCollada(colladaModel, rootPath, aliceMeshes);
    skeletonVisual.textures.setValue(sgTextureAppearances.toArray(new TexturedAppearance[sgTextureAppearances.size()]));

    UtilitySkeletonVisualAdapter skeletonVisualAdapter = new UtilitySkeletonVisualAdapter();
    skeletonVisualAdapter.initialize(skeletonVisual);
    skeletonVisualAdapter.processWeightedMesh();
    skeletonVisualAdapter.initializeJointBoundingBoxes();
    AxisAlignedBox absoluteBBox = skeletonVisualAdapter.getAbsoluteBoundingBox();
    if (skeletonVisual.geometries.getValue() != null) {
      for (edu.cmu.cs.dennisc.scenegraph.Geometry g : skeletonVisual.geometries.getValue()) {
        absoluteBBox.union(g.getAxisAlignedMinimumBoundingBox());
      }
    }
    skeletonVisual.baseBoundingBox.setValue(absoluteBBox);
    skeletonVisualAdapter.handleReleased();
    skeletonVisual.setTracker(null);

    return skeletonVisual;
  }

  private String baseFileName(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      return "Model";
    }
    int pos = fileName.lastIndexOf(".");
    if (pos < 0) {
      return  fileName;
    }
    return fileName.substring(0, pos);
  }

  /*
   *  Convenience methods for debugging
   */

  private static void printGeometryInfo(Geometry geometry) {
    float[] normalData = geometry.getMesh().getNormalData();
    int normalCount = normalData.length / 3;
    float[] vertexData = geometry.getMesh().getPositionData();
    int vertexCount = vertexData.length / 3;
    float[] uvData = geometry.getMesh().getTexCoordData();
    int uvCount = uvData.length / 2;
    Triangles tris = (Triangles) geometry.getMesh().getPrimitives().get(0);
    int triCount = tris.getCount();

    System.out.println("Tris:  " + triCount + ", normals: " + normalCount + ", vertices: " + vertexCount + ", uvs: " + uvCount);
  }

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

  private static void printWeightInfo(WeightInfo wi) {
    for (Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet()) {
      InverseAbsoluteTransformationWeightsPair iatwp = entry.getValue();
      Point3 t = iatwp.getInverseAbsoluteTransformation().translation;
      OrthogonalMatrix3x3 o = iatwp.getInverseAbsoluteTransformation().orientation;

      System.out.println(entry.getKey() + ":");
      System.out.println(" inverse transform = (" + t.x + ", " + t.y + ", " + t.z + "), [[" + o.right.x + ", " + o.right.y + ", " + o.right.z + "], [" + o.up.x + ", " + o.up.y + ", " + o.up.z + "], [" + o.backward.x + ", " + o.backward.y + ", " + o.backward.z + "]]");
      List<Float> weights = new ArrayList<Float>();
      List<Integer> indices = new ArrayList<Integer>();
      while (!iatwp.isDone()) {
        weights.add(iatwp.getWeight());
        indices.add(iatwp.getIndex());
        iatwp.advance();
      }
      System.out.println("  weight count = " + weights.size());
      System.out.println("  weights: " + weights);
      System.out.println(" indices: " + indices);
    }
  }
}
