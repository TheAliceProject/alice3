/*******************************************************************************
 * Copyright (c) 2006, 2018, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.story.resourceutilities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.scenegraph.*;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import org.alice.tweedle.file.ImageReference;
import org.alice.tweedle.file.ModelManifest;
import org.lgna.common.resources.ImageResource;
import org.lgna.project.io.JointedModelExporter;
import org.lgna.story.implementation.ImageFactory;
import org.lgna.story.implementation.JointedModelImp.VisualData;
import org.lgna.story.implementation.alice.AliceResourceUtilities;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resourceutilities.exporterutils.collada.*;
import org.lgna.story.resourceutilities.exporterutils.collada.Asset.Unit;
import org.lgna.story.resourceutilities.exporterutils.collada.COLLADA.Scene;
import org.lgna.story.resourceutilities.exporterutils.collada.Geometry;
import org.lgna.story.resourceutilities.exporterutils.collada.Image;
import org.lgna.story.resourceutilities.exporterutils.collada.Mesh;
import org.lgna.story.resourceutilities.exporterutils.collada.Skin.VertexWeights;
import org.lgna.story.resourceutilities.exporterutils.collada.Source.TechniqueCommon;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author Dave Culyba
 */
public class JointedModelColladaExporter implements JointedModelExporter {

  private static final String COLLADA_EXTENSION = "dae";
  private static final String IMAGE_EXTENSION = "png";

  //Controls whether or not we flip the coordinate space during our conversion
  //Collada models are rotated on import to match Alice, so Alice models will be flipped on export.
  private static final boolean FLIP_COORDINATE_SPACE = true;
  private static final boolean SCALE_MODEL = false;
  private static final double MODEL_SCALE = 1.0;

  private final ObjectFactory factory;
  private final SkeletonVisual visual;
  private final ModelManifest.ModelVariant modelVariant;
  private final String modelName;
  private final String resourcePath;
  private final Map<String, String> renamedJoints;

  private final HashMap<edu.cmu.cs.dennisc.scenegraph.Geometry, String> meshNameMap = new HashMap<>();
  private final HashMap<Integer, String> materialNameMap = new HashMap<>();

  public JointedModelColladaExporter(SkeletonVisual sv, ModelManifest.ModelVariant modelVariant, String modelName, String resourcePath, Map<String, String> renamedJoints) {
    this.factory = new ObjectFactory();
    this.visual = sv;
    this.modelVariant = modelVariant;
    this.modelName = modelName;
    this.resourcePath = resourcePath;
    this.renamedJoints = renamedJoints;
    //Go through all the meshes in the sgVisual and find or create names for all of them
    initializeMeshNameMap();
    //Go through all the textures and create names based on the IDs
    initializeMaterialNameMap();
  }

  public JointedModelColladaExporter(SkeletonVisual sv, ModelManifest.ModelVariant modelVariant, String modelName) {
    this(sv, modelVariant, modelName, "", Collections.emptyMap());
  }

  private Asset createAsset() {
    Asset asset = factory.createAsset();

    ZonedDateTime ct = ZonedDateTime.now();
    int zoneOffsetMinutes = ct.getOffset().getTotalSeconds() / 60;
    XMLGregorianCalendar createdDateTime;
    try {
      createdDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(ct.getYear(), ct.getMonth().getValue(), ct.getDayOfMonth(), ct.getHour(), ct.getMinute(), ct.getSecond(), 0, zoneOffsetMinutes);
      asset.setCreated(createdDateTime);
      asset.setModified(createdDateTime);
    } catch (DatatypeConfigurationException e) {
      e.printStackTrace();
    }

    Unit unit = factory.createAssetUnit();
    unit.setMeter(1.0);
    unit.setName("meter");
    asset.setUnit(unit);
    asset.setUpAxis(UpAxisType.Y_UP);

    return asset;
  }

  private Node createNodeForJoint(Joint joint) {
    Node node = factory.createNode();
    String jointIdentifier = getUserJointIdentifier(joint.jointID.getValue());
    node.setName(jointIdentifier);
    node.setId(jointIdentifier);
    node.setSid(jointIdentifier);
    node.setType(NodeType.JOINT);

    Matrix matrix = factory.createMatrix();
    matrix.setSid("matrix");
    AffineMatrix4x4 newTransform = new AffineMatrix4x4(joint.localTransformation.getValue());
    if (SCALE_MODEL) {
      newTransform.translation.multiply(MODEL_SCALE);
    }
    double[] matrixValues = newTransform.getAsRowMajorArray16();
    //Flip the values from Alice space to Collada space
    if (FLIP_COORDINATE_SPACE) {
      matrixValues = ColladaTransformUtilities.createFlippedRowMajorTransform(matrixValues);
    }
    for (double matrixValue : matrixValues) {
      matrix.getValue().add(matrixValue);
    }

    node.getLookatOrMatrixOrRotate().add(matrix);

    for (Component c : joint.getComponents()) {
      if (c instanceof Joint) {
        Node childNode = createNodeForJoint((Joint) c);
        node.getNode().add(childNode);
      }
    }

    return node;
  }

  public String getUserJointIdentifier(String jointIdentifier) {
    return renamedJoints.getOrDefault(jointIdentifier, jointIdentifier);
  }

  private Node createSkeletonNodes() {
    Joint rootJoint = visual.skeleton.getValue();
    if (rootJoint != null) {
      return createNodeForJoint(visual.skeleton.getValue());
    }
    return null;
  }

  private Param createParam(String name, String type) {
    Param param = factory.createParam();
    if (name != null) {
      param.setName(name);
    }
    param.setType(type);
    return param;
  }

  private Accessor createAccessorForArray(String arrayId, String arrayType, int arraySize, int stride) {
    Accessor accessor = factory.createAccessor();
    accessor.setSource("#" + arrayId);
    if (stride > 1) {
      accessor.setStride(BigInteger.valueOf(stride));
    }
    accessor.setCount(BigInteger.valueOf(arraySize / stride));

    if (stride == 3) {
      accessor.getParam().add(createParam("X", arrayType));
      accessor.getParam().add(createParam("Y", arrayType));
      accessor.getParam().add(createParam("Z", arrayType));
    } else if (stride == 2) {
      accessor.getParam().add(createParam("S", arrayType));
      accessor.getParam().add(createParam("T", arrayType));
    } else { //Stride of 1 or below is used to create a parameter that just specifies type
      accessor.getParam().add(createParam(null, arrayType));
    }

    return accessor;
  }

  private TechniqueCommon createTechniqueCommonForArray(String arrayId, String arrayType, int arraySize, int stride) {
    TechniqueCommon accessorTechnique = factory.createSourceTechniqueCommon();
    accessorTechnique.setAccessor(createAccessorForArray(arrayId, arrayType, arraySize, stride));
    return accessorTechnique;
  }

  //Helper classes to initialize Collada lists of numbers from Alice data
  //These are used to convert the lists of doubles and floats that represent normals, vertices, etc.
  private interface ListInitializer {
    public void initializeList(List<Double> toInitialize);
  }

  private static class DoubleArrayInitializeList implements ListInitializer {
    private final double[] da;

    public DoubleArrayInitializeList(double[] da) {
      this.da = da;
    }

    @Override
    public void initializeList(List<Double> toInitialize) {
      for (double element : da) {
        toInitialize.add(element);
      }
    }
  }

  private static class DoubleListInitializeList implements ListInitializer {
    private final List<Double> dl;

    public DoubleListInitializeList(List<Double> dl) {
      this.dl = dl;
    }

    @Override
    public void initializeList(List<Double> toInitialize) {
      for (int i = 0; i < dl.size(); i++) {
        toInitialize.add(dl.get(i));
      }
    }
  }

  private static class FloatListInitializeList implements ListInitializer {
    private final List<Float> fl;

    public FloatListInitializeList(List<Float> fl) {
      this.fl = fl;
    }

    @Override
    public void initializeList(List<Double> toInitialize) {
      for (int i = 0; i < fl.size(); i++) {
        toInitialize.add(fl.get(i).doubleValue());
      }
    }
  }

  private static class DoubleBufferInitializeList implements ListInitializer {
    private final DoubleBuffer db;

    public DoubleBufferInitializeList(DoubleBuffer db) {
      this.db = db;
    }

    @Override
    public void initializeList(List<Double> toInitialize) {
      db.rewind();
      final int N = db.limit();
      for (int i = 0; i < N; i++) {
        toInitialize.add(db.get(i));
      }
    }
  }

  private static class FloatBufferInitializeList implements ListInitializer {
    private final FloatBuffer fb;

    public FloatBufferInitializeList(FloatBuffer fb) {
      this.fb = fb;
    }

    @Override
    public void initializeList(List<Double> toInitialize) {
      fb.rewind();
      final int N = fb.limit();
      for (int i = 0; i < N; i++) {
        toInitialize.add(Double.valueOf(fb.get(i)));
      }
    }
  }

  private Source createFloatArraySourceFromInitializer(ListInitializer initializer, String name, int stride, double scale, boolean flipXandZ) {

    Source source = factory.createSource();
    source.setId(name);

    //Build the array of vertices (called "position" in the collada file)
    FloatArray floatArray = factory.createFloatArray();
    floatArray.setId(name + "-array");
    List<Double> values = floatArray.getValue();
    initializer.initializeList(values);

    //Flip X and Z as part of flipping the normals and positions from Alice space to Collada space
    if (flipXandZ || scale != 1.0) {
      //Combine whether or not we're flipping with the scale value so we can do it all in one multiply
      double flipScale = flipXandZ ? scale * -1.0 : scale;
      for (int i = 0; i < values.size(); i += 3) {
        values.set(i, values.get(i) * flipScale); //Only flip X & Z
        values.set(i + 1, values.get(i + 1) * scale);
        values.set(i + 2, values.get(i + 2) * flipScale); //Only flip X & Z
      }
    }

    int valueCount = values.size();
    floatArray.setCount(BigInteger.valueOf(valueCount));

    //Build the accessor
    TechniqueCommon accessorTechnique = createTechniqueCommonForArray(floatArray.getId(), "float", floatArray.getValue().size(), stride);
    source.setFloatArray(floatArray);
    source.setTechniqueCommon(accessorTechnique);

    return source;
  }

  //Mesh name is used both in library_geometries and in library_controllers
  private String getMeshIdForMeshName(String meshName) {
    return meshName + "-id";
  }

  private Triangles createTriangles(edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh, String verticesName, String normalsName, String UVsName, Integer textureId) {
    //Build the triangle data
    Triangles triangles = factory.createTriangles();
    InputLocalOffset vertexInput = createInputLocalOffset("VERTEX", verticesName, 0);
    triangles.getInput().add(vertexInput);

    InputLocalOffset normalInput = createInputLocalOffset("NORMAL", normalsName, 1);
    triangles.getInput().add(normalInput);

    InputLocalOffset texCoordInput = createInputLocalOffset("TEXCOORD", UVsName, 2);
    texCoordInput.setSet(BigInteger.valueOf(0));
    triangles.getInput().add(texCoordInput);
    //Copy over the triangle indices
    List<BigInteger> triangleList = triangles.getP();
    IntBuffer ib = sgMesh.indexBuffer.getValue();
    final int N = sgMesh.indexBuffer.getValue().limit();
    int count = 0;
    for (int i = 0; i < N; i += 3) {
      if (sgMesh.getTextureId(i).equals(textureId)) {
        //Reverse the order of the triangle indices because collada needs the indices in counter clockwise order
        triangleList.add(BigInteger.valueOf(ib.get(i + 0))); //Position 0
        triangleList.add(BigInteger.valueOf(ib.get(i + 0))); //Normal 0
        triangleList.add(BigInteger.valueOf(ib.get(i + 0))); //UV 0

        triangleList.add(BigInteger.valueOf(ib.get(i + 1))); //Position 1
        triangleList.add(BigInteger.valueOf(ib.get(i + 1))); //Normal 1
        triangleList.add(BigInteger.valueOf(ib.get(i + 1))); //UV 1

        triangleList.add(BigInteger.valueOf(ib.get(i + 2))); //Position 2
        triangleList.add(BigInteger.valueOf(ib.get(i + 2))); //Normal 2
        triangleList.add(BigInteger.valueOf(ib.get(i + 2))); //UV 2
        count++;
      }
    }
    triangles.setCount(BigInteger.valueOf(count));

    return triangles;
  }

  private Vertices createVertices(String verticesName, String positionsName) {
    Vertices vertices = factory.createVertices();
    vertices.setId(verticesName);
    InputLocal positionInput = createInputLocal("POSITION", positionsName);
    vertices.getInput().add(positionInput);
    return vertices;
  }

  private void addGeometriesForMesh(List<Geometry> geometries, edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh) {
    //Make a unique geometry for each triangle set using a distinct textureId in the mesh
    for (Integer textureId : sgMesh.getReferencedTextureIds()) {
      geometries.add(geometryForMeshAndTexture(sgMesh, textureId));
    }
  }

  private Geometry geometryForMeshAndTexture(edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh, Integer textureId) {
    Geometry geometry = factory.createGeometry();
    String meshName = getMeshTextureId(sgMesh, textureId);

    geometry.setId(getMeshIdForMeshName(meshName));
    geometry.setName(meshName + "mesh");
    //Geometry consists of a Geometry node with a single Mesh node inside
    geometry.setMesh(createMesh(sgMesh, textureId, meshName));

    return geometry;
  }

  private Mesh createMesh(edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh, Integer textureId, String meshName) {
    //Set up the Mesh node
    Mesh mesh = factory.createMesh();
    //Build the Position source. This will hold the array of vertices and a accessor that tells the file the array is of stride 3
    String positionName = meshName + "-POSITION";
    double scale = SCALE_MODEL ? MODEL_SCALE : 1.0;
    //Pass flipXandZ = TRUE to flip Alice coordinate space to Collada coordinate space
    //Only pass scale to position. Normals and UVs should not be scaled
    Source positionSource = createFloatArraySourceFromInitializer(new DoubleBufferInitializeList(sgMesh.vertexBuffer.getValue()), positionName, 3, scale, FLIP_COORDINATE_SPACE);
    mesh.getSource().add(positionSource);

    String normalName = meshName + "-NORMAL";
    //Pass flipXandZ = TRUE to flip Alice coordinate space to Collada coordinate space
    Source normalSource = createFloatArraySourceFromInitializer(new FloatBufferInitializeList(sgMesh.normalBuffer.getValue()), normalName, 3, 1.0, FLIP_COORDINATE_SPACE);
    mesh.getSource().add(normalSource);

    String uvName = meshName + "-UV";
    Source uvSource = createFloatArraySourceFromInitializer(new FloatBufferInitializeList(sgMesh.textCoordBuffer.getValue()), uvName, 2, 1.0, false);
    mesh.getSource().add(uvSource);
    //Create and add vertices
    String vertexName = meshName + "-VERTEX";
    Vertices vertices = createVertices(vertexName, positionName);
    mesh.setVertices(vertices);

    //Create and add the triangles
    Triangles triangles = createTriangles(sgMesh, vertexName, normalName, uvName, textureId);
    // Sets a reference to an instance_material by its symbol
    triangles.setMaterial(getInstanceMaterialSymbolForIndex(textureId));
    mesh.getLinesOrLinestripsOrPolygons().add(triangles);
    return mesh;
  }

  private double[] getBindShapeMatrix(WeightedMesh sgWeightedMesh) {
    //All Alice models have a bind shape of the identity
    AffineMatrix4x4 bindShapeMatrix = AffineMatrix4x4.createIdentity();
    return bindShapeMatrix.getAsRowMajorArray16();
  }

  private Source createJointSource(WeightInfo wi, String jointSourceName) {
    Source jointSource = factory.createSource();
    jointSource.setId(jointSourceName);
    NameArray jointNameArray = factory.createNameArray();
    jointNameArray.setId(jointSourceName + "-array");
    for (Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet()) {
      jointNameArray.getValue().add(getUserJointIdentifier(entry.getKey()));
    }
    jointNameArray.setCount(BigInteger.valueOf(jointNameArray.getValue().size()));
    jointSource.setNameArray(jointNameArray);
    jointSource.setTechniqueCommon(createTechniqueCommonForArray(jointNameArray.getId(), "name", jointNameArray.getValue().size(), 1));
    return jointSource;
  }

  private Source createMatrixSource(WeightInfo wi, String matricesSourceName) {
    //Build the Matrix Source
    Source matricesSource = factory.createSource();
    matricesSource.setId(matricesSourceName);
    FloatArray matricesArray = factory.createFloatArray();
    matricesArray.setId(matricesSourceName + "-array");
    for (Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet()) {
      InverseAbsoluteTransformationWeightsPair iatwp = entry.getValue();
      AffineMatrix4x4 inverseBindMatrix = iatwp.getInverseAbsoluteTransformation();
      if (SCALE_MODEL) {
        inverseBindMatrix.translation.multiply(MODEL_SCALE);
      }
      double[] matrix = inverseBindMatrix.getAsRowMajorArray16();
      if (FLIP_COORDINATE_SPACE) {
        //Invert matrix, flip its values from Alice space to Collada space and then invert it back
        matrix = ColladaTransformUtilities.createFlippedRowMajorTransform(matrix);
      }
      for (double element : matrix) {
        matricesArray.getValue().add(element);
      }
    }
    int matrixCount = matricesArray.getValue().size();
    matricesArray.setCount(BigInteger.valueOf(matrixCount));
    matricesSource.setFloatArray(matricesArray);
    matricesSource.setTechniqueCommon(createTechniqueCommonForArray(matricesArray.getId(), "float4x4", matrixCount, 16));

    return matricesSource;
  }

  private InputLocal createInputLocal(String semantic, String sourceName) {
    InputLocal inputLocal = factory.createInputLocal();
    inputLocal.setSemantic(semantic);
    inputLocal.setSource("#" + sourceName);
    return inputLocal;
  }

  private InputLocalOffset createInputLocalOffset(String semantic, String sourceName, int offset) {
    InputLocalOffset inputLocalOffset = factory.createInputLocalOffset();
    inputLocalOffset.setSemantic(semantic);
    inputLocalOffset.setSource("#" + sourceName);
    inputLocalOffset.setOffset(BigInteger.valueOf(offset));
    return inputLocalOffset;
  }

  private List<Float> createWeightList(WeightedMesh sgWeightedMesh) {
    WeightInfo wi = sgWeightedMesh.weightInfo.getValue();
    //Array that all the individual weights stored in it. The weight indices in the skinWeights array will index into this
    List<Float> weightArray = new ArrayList<Float>();
    //Iterate through the WeightInfo and add the weights in order to the weightArray
    for (Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet()) {
      InverseAbsoluteTransformationWeightsPair.WeightIterator weightIterator = entry.getValue().getIterator();
      while (weightIterator.hasNext()) {
        weightArray.add(weightIterator.next());
      }
    }
    return weightArray;
  }

  //Helper class for storing weights as lists of indices
  private static class ColladaSkinWeights {
    public final List<Integer> weightIndices = new ArrayList<Integer>();
    public final List<Integer> jointIndices = new ArrayList<Integer>();

  }

  private ColladaSkinWeights[] createSkinWeights(WeightedMesh sgWeightedMesh) {
    WeightInfo wi = sgWeightedMesh.weightInfo.getValue();
    int vertexCount = sgWeightedMesh.vertexBuffer.getValue().limit() / 3;
    ColladaSkinWeights[] skinWeights = new ColladaSkinWeights[vertexCount];
    int jointIndex = 0;
    //Counter for tracking the appropriate index value to the weightIndices array
    int weightIndex = 0;
    //Iterate through the WeightInfo and get the joint indices and weight indices
    //ColladaSkinWeights is an array that uses a vertex index to index to a ColladaSkinWeights
    //The ColladaSkinWeights object contains the joint indices the vertex is mapped to and the weight indices for those joints
    for (Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet()) {
      InverseAbsoluteTransformationWeightsPair.WeightIterator weightIterator = entry.getValue().getIterator();
      while (weightIterator.hasNext()) {
        int vertexIndex = weightIterator.getIndex();
        if (skinWeights[vertexIndex] == null) {
          skinWeights[vertexIndex] = new ColladaSkinWeights();
        }
        ColladaSkinWeights vertexWeights = skinWeights[vertexIndex];
        vertexWeights.jointIndices.add(jointIndex);
        //weightIndex is the sequential count of the weights. It ends up mapping to the weight list created in createWeightList()
        vertexWeights.weightIndices.add(weightIndex++);
        weightIterator.next();
      }
      jointIndex++;
    }
    return skinWeights;
  }

  //  Here is an example of a more complete <vertex_weights> element. Note that the <vcount> element
  //  says that the first vertex has 3 bones, the second has 2, etc. Also, the <v> element says that the first
  //  vertex is weighted with weights[0] towards the bind shape, weights[1] towards bone 0, and
  //  weights[2] towards bone 1:
  //  <skin>
  //   <source id="joints"/>
  //   <source id="weights"/>
  //   <vertex_weights count="4">
  //     <input semantic="JOINT" source="#joints"/>
  //     <input semantic="WEIGHT" source="#weights"/>
  //     <vcount>3 2 2 3</vcount>
  //     <v>
  //       -1 0 0 1 1 2
  //       -1 3 1 4
  //       -1 3 2 4
  //       -1 0 3 1 2 2
  //     </v>
  //   </vertex_weights>
  //  </skin>
  private Skin createSkin(WeightedMesh sgWeightedMesh, String controllerName) {
    Skin skin = factory.createSkin();

    //Set the bind shape matrix
    double[] bindShapeMatrix = getBindShapeMatrix(sgWeightedMesh);
    for (double element : bindShapeMatrix) {
      skin.getBindShapeMatrix().add(element);
    }

    WeightInfo wi = sgWeightedMesh.weightInfo.getValue();
    String jointSourceName = controllerName + "-Joints";
    Source jointSource = createJointSource(wi, jointSourceName);
    skin.getSourceAttribute2().add(jointSource);

    String matricesSourceName = controllerName + "-Matrices";
    Source matricesSource = createMatrixSource(wi, matricesSourceName);
    skin.getSourceAttribute2().add(matricesSource);

    //Build the VertexWeights
    int vertexCount = sgWeightedMesh.vertexBuffer.getValue().limit() / 3;
    VertexWeights vw = factory.createSkinVertexWeights();
    vw.setCount(BigInteger.valueOf(vertexCount));
    //Array that contains the skin weight info (joint indices and weight indices) for each vertex
    ColladaSkinWeights[] skinWeights = createSkinWeights(sgWeightedMesh);
    //Array that all the individual weights stored in it. The weight indices in the skinWeights array will index into this
    List<Float> weightArray = createWeightList(sgWeightedMesh);
    //Add the ColladaSkinWeights to the VertexWeights structure by adding the indices to the V element
    //See the Skin structure comment at the top for details
    for (ColladaSkinWeights vertexWeights : skinWeights) {
      if (vertexWeights == null) {
        // This vertex is not weighted to any joints. It is probably weighted to hidden joints. It will not be displayed.
        vw.getVcount().add(BigInteger.valueOf(0));
        continue;
      }

      vw.getVcount().add(BigInteger.valueOf(vertexWeights.jointIndices.size()));
      for (int j = 0; j < vertexWeights.jointIndices.size(); j++) {
        vw.getV().add(vertexWeights.jointIndices.get(j).longValue());
        vw.getV().add(vertexWeights.weightIndices.get(j).longValue());
      }
    }

    //Build the weights source.
    String weightsSourceName = controllerName + "-Weights";
    Source weightsSource = createFloatArraySourceFromInitializer(new FloatListInitializeList(weightArray), weightsSourceName, 1, 1.0, false);
    skin.getSourceAttribute2().add(weightsSource);

    vw.getInput().add(createInputLocalOffset("JOINT", jointSourceName, 0));
    vw.getInput().add(createInputLocalOffset("WEIGHT", weightsSourceName, 1));

    //Create and add Joints object
    Skin.Joints joints = factory.createSkinJoints();
    joints.getInput().add(createInputLocal("JOINT", jointSourceName));
    joints.getInput().add(createInputLocal("INV_BIND_MATRIX", matricesSourceName));

    skin.setJoints(joints);
    skin.setVertexWeights(vw);

    return skin;
  }

  private void addControllersForMesh(List<Controller> controllers, WeightedMesh sgWeightedMesh) {
    for (Integer usedTextureId : sgWeightedMesh.getReferencedTextureIds()) {
      String meshName = getMeshTextureId(sgWeightedMesh, usedTextureId);
      Controller controller = factory.createController();
      String controllerName = meshName + "Controller";
      controller.setId(controllerName);

      Skin skin = createSkin(sgWeightedMesh, controllerName);
      skin.setSourceAttribute("#" + getMeshIdForMeshName(meshName));
      controller.setSkin(skin);

      controllers.add(controller);
    }
  }

  private void addMeshToNameMap(edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh) {
    String meshName = sgMesh.getName();
    if ((meshName == null) || (meshName.length() == 0)) {
      meshName = "mesh" + meshNameMap.size();
    }
    meshNameMap.put(sgMesh, meshName);
  }

  private void initializeMeshNameMap() {
    meshNameMap.clear();
    for (edu.cmu.cs.dennisc.scenegraph.Geometry g : visual.geometries.getValue()) {
      if (g instanceof edu.cmu.cs.dennisc.scenegraph.Mesh) {
        addMeshToNameMap((edu.cmu.cs.dennisc.scenegraph.Mesh) g);
      }
    }
    for (WeightedMesh wm : visual.weightedMeshes.getValue()) {
      addMeshToNameMap(wm);
    }
  }

  private void initializeMaterialNameMap() {
    materialNameMap.clear();
    //Make names for all the texture IDs
    //There's no naming convention. Naming is geared toward human readability--material_0 is fine
    for (TexturedAppearance texture : visual.textures.getValue()) {
      materialNameMap.put(texture.textureId.getValue(), "material_" + texture.textureId.getValue());
    }
  }

  private String getImageNameForIndex(Integer index) {
    return materialNameMap.get(index) + "_diffuseMap";
  }

  //Image file name must be unique to the model.
  //We don't know if models share textures, so we must construct a unique file name to avoid collisions
  //To support sharing textures, we will need to have a way to connect the texture id (a number) to a unique filename
  private String getExternallyUniqueImageNameForID(Integer id) {
    return getFullResourceName() + "_" + getImageNameForIndex(id);
  }

  private String getImageFileNameForIndex(Integer index) {
    return getExternallyUniqueImageNameForID(index) + "." + IMAGE_EXTENSION;
  }

  private String getImageIDForIndex(Integer index) {
    return getImageNameForIndex(index) + "-image";
  }

  private String getMaterialIDForIndex(Integer index) {
    return materialNameMap.get(index) + "_shader";
  }

  private String getInstanceMaterialSymbolForIndex(Integer index) {
    // This must be the same as material id because Alice currently bypasses instance_material, requiring the
    // instance_material's symbol, which identifies it within an instance_controller to be the same as the
    // library material's id, which is unique in the entire collada file.
    return getMaterialIDForIndex(index);
  }

  private String getEffectIDForIndex(Integer index) {
    return materialNameMap.get(index) + "_fx";
  }

  private BindMaterial createBindMaterialForMaterialIndex(Integer materialIndex) {
    BindMaterial bindMaterial = factory.createBindMaterial();

    BindMaterial.TechniqueCommon techniqueCommon = factory.createBindMaterialTechniqueCommon();
    InstanceMaterial instanceMaterial = factory.createInstanceMaterial();
    // Symbol uniquely identifies the instance_material.
    // It is referred to from a geometry sub element (e.g. triangle) by material="anInstanceMaterialSymbol"
    instanceMaterial.setSymbol(getInstanceMaterialSymbolForIndex(materialIndex));
    // Target is the id of the material to use does not have #
    instanceMaterial.setTarget(getMaterialIDForIndex(materialIndex));
    InstanceMaterial.BindVertexInput bindVertexInput = factory.createInstanceMaterialBindVertexInput();
    bindVertexInput.setSemantic("UVMap");
    bindVertexInput.setInputSet(BigInteger.ZERO);
    bindVertexInput.setInputSemantic("TEXCOORD");
    instanceMaterial.getBindVertexInput().add(bindVertexInput);
    techniqueCommon.getInstanceMaterial().add(instanceMaterial);

    bindMaterial.setTechniqueCommon(techniqueCommon);
    return bindMaterial;
  }

  private Node createVisualSceneNode(String name) {
    Node visualSceneNode = factory.createNode();
    visualSceneNode.setName(name);
    visualSceneNode.setId(name);
    visualSceneNode.setSid(name);
    return visualSceneNode;
  }

  private void addVisualSceneNodesForMesh(List<Node> sceneNodes, edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh) {
    for (Integer usedTextureId : sgMesh.getReferencedTextureIds()) {
      String meshName = getMeshTextureId(sgMesh, usedTextureId);
      Node visualSceneNode = createVisualSceneNode(meshName);

      String geometryURL = "#" + getMeshIdForMeshName(meshName);
      InstanceGeometry instanceGeometry = factory.createInstanceGeometry();
      instanceGeometry.setUrl(geometryURL);

      instanceGeometry.setBindMaterial(createBindMaterialForMaterialIndex(usedTextureId));
      visualSceneNode.getInstanceGeometry().add(instanceGeometry);

      sceneNodes.add(visualSceneNode);
    }
  }

  private String getMeshTextureId(edu.cmu.cs.dennisc.scenegraph.Mesh mesh, Integer usedTextureId) {
    String meshName = meshNameMap.get(mesh);
    if (mesh.getReferencedTextureIds().size() > 1) {
      meshName = meshName + "-" + usedTextureId;
    }
    return meshName;
  }

  private void addVisualSceneNodesForWeightedMesh(List<Node> sceneNodes, WeightedMesh sgWeightedMesh) {
    for (Integer usedTextureId : sgWeightedMesh.getReferencedTextureIds()) {
      String meshName = getMeshTextureId(sgWeightedMesh, usedTextureId);
      Node visualSceneNode = createVisualSceneNode(meshName);

      String controllerURL = "#" + meshName + "Controller";
      InstanceController instanceController = factory.createInstanceController();
      instanceController.setUrl(controllerURL);

      instanceController.setBindMaterial(createBindMaterialForMaterialIndex(usedTextureId));
      visualSceneNode.getInstanceController().add(instanceController);

      sceneNodes.add(visualSceneNode);
    }
  }

  private CommonColorOrTextureType.Color createCommonColor(String sid, double r, double g, double b, double a) {
    CommonColorOrTextureType.Color color = factory.createCommonColorOrTextureTypeColor();
    color.setSid(sid);
    color.getValue().add(r);
    color.getValue().add(g);
    color.getValue().add(b);
    color.getValue().add(a);
    return color;
  }

  private CommonColorOrTextureType createCommonColorType(String sid, double r, double g, double b, double a) {
    CommonColorOrTextureType colorType = factory.createCommonColorOrTextureType();
    colorType.setColor(createCommonColor(sid, r, g, b, a));
    return colorType;
  }

  private CommonNewparamType createSurfaceParam(Integer materialIndex) {
    //Create the surface param
    String surfaceParamId = getImageNameForIndex(materialIndex) + "-surface";
    CommonNewparamType surfaceParam = factory.createCommonNewparamType();
    surfaceParam.setSid(surfaceParamId);
    FxSurfaceCommon surface = factory.createFxSurfaceCommon();
    surface.setType("2D");
    FxSurfaceInitFromCommon surfaceInit = factory.createFxSurfaceInitFromCommon();
    //This "setValue" needs something that has an ID.
    //We're using the same pattern we used for making the Image entries for the library_images section
    Image image = factory.createImage();
    image.setName(getImageNameForIndex(materialIndex));
    image.setId(getImageIDForIndex(materialIndex));
    surfaceInit.setValue(image);

    surface.getInitFrom().add(surfaceInit);
    surfaceParam.setSurface(surface);
    return surfaceParam;
  }

  private CommonNewparamType createSamplerParam(Integer materialIndex, String surfaceParamId) {
    //Create the sampler param
    CommonNewparamType samplerParam = factory.createCommonNewparamType();
    final String samplerParamSid = getImageNameForIndex(materialIndex) + "-sampler";
    samplerParam.setSid(samplerParamSid);
    FxSampler2DCommon sampler = factory.createFxSampler2DCommon();
    sampler.setSource(surfaceParamId);
    samplerParam.setSampler2D(sampler);
    return samplerParam;
  }


  private Effect createEffect(TexturedAppearance texturedAppearance) {
    Integer materialIndex = texturedAppearance.textureId.getValue();
    Effect effect = factory.createEffect();
    final String effectId = getEffectIDForIndex(materialIndex);
    effect.setId(effectId);
    // Id is required. Name is not, but we will repeat the ID.
    effect.setName(effectId);
    ProfileCOMMON profile = factory.createProfileCOMMON();
    ProfileCOMMON.Technique technique = factory.createProfileCOMMONTechnique();
    technique.setSid("standard");

    ProfileCOMMON.Technique.Lambert lambert = factory.createProfileCOMMONTechniqueLambert();
    //TODO: Should we set these colors based on the textured appearance?
    // We don't use these values in the textured appearance, so the values may not translate well
    lambert.setEmission(createCommonColorType("emission", 0, 0, 0, 1));
    lambert.setAmbient(createCommonColorType("ambient", 0, 0, 0, 1));
    if (texturedAppearance.diffuseColorTexture.getValue() != null) {
      CommonNewparamType surfaceParam = createSurfaceParam(materialIndex);
      profile.getImageOrNewparam().add(surfaceParam);
      CommonNewparamType samplerParam = createSamplerParam(materialIndex, surfaceParam.getSid());
      profile.getImageOrNewparam().add(samplerParam);
      CommonColorOrTextureType diffuse = factory.createCommonColorOrTextureType();
      CommonColorOrTextureType.Texture texture = factory.createCommonColorOrTextureTypeTexture();
      texture.setTexture(samplerParam.getSid());
      texture.setTexcoord("UVMap"); //Based on generated example collada file
      diffuse.setTexture(texture);
      lambert.setDiffuse(diffuse);
      //If the texture has alpha, add a transparent node to and set the texture to be the value
      if (texturedAppearance.isDiffuseColorTextureAlphaBlended.getValue()) {
        CommonTransparentType transparent = factory.createCommonTransparentType();
        transparent.setTexture(texture);
        lambert.setTransparent(transparent);
      }
    } else {
      Color4f diffuseColor = texturedAppearance.diffuseColor.getValue();
      lambert.setDiffuse(createCommonColorType("diffuse", diffuseColor.red, diffuseColor.green, diffuseColor.blue, diffuseColor.alpha));
    }
    technique.setLambert(lambert);
    profile.setTechnique(technique);
    JAXBElement<ProfileCOMMON> profileCOMMONJAXBElement = new JAXBElement<ProfileCOMMON>(new QName("http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON"), ProfileCOMMON.class, profile);
    effect.getFxProfileAbstract().add(profileCOMMONJAXBElement);

    return effect;
  }

  private String getFullResourceName() {
    return modelVariant == null ? modelName : modelVariant.textureSet;
  }

  private void createAndAddTextureComponents(COLLADA collada) {
    LibraryImages libraryImages = factory.createLibraryImages();
    LibraryMaterials libraryMaterials = factory.createLibraryMaterials();
    LibraryEffects libraryEffects = factory.createLibraryEffects();
    for (TexturedAppearance texture : visual.textures.getValue()) {
      Integer materialIndex = texture.textureId.getValue();
      if (texture.diffuseColorTexture.getValue() != null) {
        Image image = factory.createImage();
        image.setName(getImageNameForIndex(materialIndex));
        image.setId(getImageIDForIndex(materialIndex));
        image.setInitFrom(getImageFileNameForIndex(materialIndex));
        libraryImages.getImage().add(image);
      }

      Material material = factory.createMaterial();
      final String materialId = getMaterialIDForIndex(materialIndex);
      material.setId(materialId);
      // Material name is not required, but we will repeat the ID
      material.setName(materialId);
      InstanceEffect instanceEffect = factory.createInstanceEffect();
      instanceEffect.setUrl("#" + getEffectIDForIndex(materialIndex));
      material.setInstanceEffect(instanceEffect);
      libraryMaterials.getMaterial().add(material);

      Effect effect = createEffect(texture);
      libraryEffects.getEffect().add(effect);
    }
    collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(libraryImages);
    collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(libraryMaterials);
    collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(libraryEffects);
  }

  private void createAndAddMeshComponents(COLLADA collada, VisualScene visualScene) {
    //Geometry
    LibraryGeometries lg = factory.createLibraryGeometries();
    //Grab the static geometry and add them to the scene
    for (edu.cmu.cs.dennisc.scenegraph.Geometry g : visual.geometries.getValue()) {
      if (g instanceof edu.cmu.cs.dennisc.scenegraph.Mesh) {
        edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh = (edu.cmu.cs.dennisc.scenegraph.Mesh) g;
        addGeometriesForMesh(lg.getGeometry(), sgMesh);
        addVisualSceneNodesForMesh(visualScene.getNode(), sgMesh);
      }
    }
    //Grab the weighted meshes and add their geometry to the scene
    for (WeightedMesh sgWM : visual.weightedMeshes.getValue()) {
      addGeometriesForMesh(lg.getGeometry(), sgWM);
    }
    collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(lg);

    //Weighted Mesh Controllers
    LibraryControllers lc = factory.createLibraryControllers();
    //Create controllers for all the weighted meshes
    for (WeightedMesh sgWM : visual.weightedMeshes.getValue()) {
      addControllersForMesh(lc.getController(), sgWM);
      addVisualSceneNodesForWeightedMesh(visualScene.getNode(), sgWM);
    }
    collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(lc);
  }

  protected COLLADA createCollada() {
    COLLADA collada = factory.createCOLLADA();

    //Required part of a collada file
    collada.setVersion("1.4.1");
    Asset asset = createAsset();
    collada.setAsset(asset);

    //Create the Visual Scene, but don't add it yet because it needs to be at the end
    String sceneName = getFullResourceName();
    VisualScene visualScene = factory.createVisualScene();
    visualScene.setId(sceneName);
    visualScene.setName(sceneName);

    //Add the skeleton to the visual scene
    Node skeletonNodes = createSkeletonNodes();
    visualScene.getNode().add(skeletonNodes);

    //Create the scene and add it
    Scene scene = factory.createCOLLADAScene();
    InstanceWithExtra sceneInstance = factory.createInstanceWithExtra();
    sceneInstance.setUrl("#" + visualScene.getId());
    scene.setInstanceVisualScene(sceneInstance);
    collada.setScene(scene);

    //Images, Materials, and Effects
    createAndAddTextureComponents(collada);
    //Meshes, Weighted Meshes, and Controllers
    createAndAddMeshComponents(collada, visualScene);

    //Finally add visual scene last so it's at the end
    LibraryVisualScenes lvs = factory.createLibraryVisualScenes();
    lvs.getVisualScene().add(visualScene);
    collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(lvs);

    return collada;
  }

  public void writeCollada(OutputStream os) throws IOException {
    COLLADA collada = createCollada();

    JAXBContext jc;
    try {
      jc = JAXBContext.newInstance("org.lgna.story.resourceutilities.exporterutils.collada");
      final Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(collada, os);
    } catch (JAXBException e) {
      throw new IOException(e);
    }
  }

  private static BufferedImage createFlippedImage(BufferedImage image) {
    AffineTransform at = new AffineTransform();
    at.concatenate(AffineTransform.getScaleInstance(1, -1));
    at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));

    BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = flippedImage.createGraphics();
    g.transform(at);
    g.drawImage(image, 0, 0, null);
    g.dispose();

    return flippedImage;
  }

  private static void writeTexture(TexturedAppearance texture, OutputStream os) throws IOException {
    BufferedImageTexture bufferedTexture = (BufferedImageTexture) texture.diffuseColorTexture.getValue();
    //Flip the textures because Alice uses flipped textures and things outside of Alice don't
    BufferedImage flippedImage = createFlippedImage(bufferedTexture.getBufferedImage());
    ImageIO.write(flippedImage, IMAGE_EXTENSION, os);
  }

  private String getColladaFileName() {
    return getFullResourceName() + "." + COLLADA_EXTENSION;
  }

  public List<String> getTextureFileNames() {
    List<String> textureFileNames = new ArrayList<>();
    for (TexturedAppearance texture : visual.textures.getValue()) {
      Integer materialIndex = texture.textureId.getValue();
      textureFileNames.add(getImageFileNameForIndex(materialIndex));
    }
    return textureFileNames;
  }

  public Map<Integer, String> createTextureIdToImageMap() {
    Map<Integer, String> textureIdToFileMap = new HashMap<>();
    for (TexturedAppearance texture : visual.textures.getValue()) {
      Integer textureID = texture.textureId.getValue();
      textureIdToFileMap.put(textureID, getExternallyUniqueImageNameForID(textureID));
    }
    return textureIdToFileMap;
  }

  private TexturedAppearance getTextureAppearance(Integer textureId) {
    for (TexturedAppearance texture : visual.textures.getValue()) {
      if (textureId.equals(texture.textureId.getValue())) {
        return texture;
      }
    }
    return null;
  }

  public ImageResource createImageResourceForTexture(Integer textureId) throws IOException {
    TexturedAppearance texturedAppearance = getTextureAppearance(textureId);
    BufferedImageTexture bufferedTexture = (BufferedImageTexture) texturedAppearance.diffuseColorTexture.getValue();
    return ImageFactory.createImageResource(bufferedTexture.getBufferedImage(), getImageFileNameForIndex(textureId));
  }

  public Integer getTextureIdForName(String textureName) {
    //Strip off any extension or path from the text name
    String baseTextureName = FileUtilities.getBaseName(textureName);
    for (TexturedAppearance texture : visual.textures.getValue()) {
      Integer textureID = texture.textureId.getValue();
      String toCheck = getExternallyUniqueImageNameForID(textureID);
      if (baseTextureName.equals(toCheck)) {
        return textureID;
      }
    }
    return -1;
  }

  @Override
  public DataSource createStructureDataSource() {
    final String name = resourcePath + "/" + getColladaFileName();
    return new DataSource() {
      @Override
      public String getName() {
        return name;
      }

      @Override
      public void write(OutputStream os) throws IOException {
        writeCollada(os);
      }
    };
  }

  @Override
  public String getStructureFileName(DataSource structureDataSource) {
    //Strip the base and model path from the name to make it relative to the manifest
    return structureDataSource.getName().substring(resourcePath.length() + 1);
  }

  @Override
  public String getStructureExtension() {
    return IMAGE_EXTENSION;
  }

  @Override
  public void addImageDataSources(List<DataSource> dataSources, ModelManifest modelManifest, Map<Integer, String> resourceMap) throws IOException {
    addTextureIds(resourceMap);
    for (DataSource imageDataSource : createImageDataSources()) {
      //For each new image, create an image reference and add it to the manifest
      if (!dataSources.contains(imageDataSource)) {
        Integer imageId = getTextureIdForName(imageDataSource.getName());
        ImageReference imageReference = new ImageReference(createImageResourceForTexture(imageId));
        //ResourceReference name is initialized to the source filename. Replacing with the imageName instead.
        imageReference.name = resourceMap.get(imageId);
        modelManifest.resources.add(imageReference);
        dataSources.add(imageDataSource);
      }
    }
  }

  private void addTextureIds(Map<Integer, String> resourceMap) {
    for (TexturedAppearance texture : visual.textures.getValue()) {
      Integer textureID = texture.textureId.getValue();
      resourceMap.put(textureID, getExternallyUniqueImageNameForID(textureID));
    }
  }

  //Get all the textures from the model as data sources
  public List<DataSource> createImageDataSources() {
    List<DataSource> dataSources = new ArrayList<>();
    for (TexturedAppearance texture : visual.textures.getValue()) {
      if (texture.diffuseColorTexture.getValue() != null) {
        Integer materialIndex = texture.textureId.getValue();
        final String textureName = resourcePath + "/" + getImageFileNameForIndex(materialIndex);
        DataSource dataSource = new DataSource() {
          @Override
          public String getName() {
            return textureName;
          }

          @Override
          public void write(OutputStream os) throws IOException {
            writeTexture(texture, os);
          }
        };
        dataSources.add(dataSource);
      }
    }
    return dataSources;
  }

  public List<File> saveTexturesToDirectory(File directory) {
    List<File> textureFiles = new ArrayList<>();
    for (TexturedAppearance texture : visual.textures.getValue()) {
      File textureFile = new File(directory, getImageFileNameForIndex(texture.textureId.getValue()));
      try {
        FileUtilities.createParentDirectoriesIfNecessary(textureFile);
        textureFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(textureFile);
        writeTexture(texture, fos);
        textureFiles.add(textureFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return textureFiles;
  }

  //Local testing code
  private File saveColladaToDirectory(File directory) throws IOException {
    File colladaOutputFile = new File(directory, getColladaFileName());
    writeCollada(new FileOutputStream(colladaOutputFile));
    return colladaOutputFile;
  }

  private static List<File> exportAliceModelToDir(JointedModelColladaExporter exporter, File rootDir) throws IOException {
    List<File> outputFiles = new ArrayList<>();

    outputFiles.add(exporter.saveColladaToDirectory(rootDir));
    outputFiles.addAll(exporter.saveTexturesToDirectory(rootDir));

    return outputFiles;
  }

  private static SkeletonVisual loadAliceModel(JointedModelResource resource) {
    VisualData<JointedModelResource> v = ImplementationAndVisualType.ALICE.getFactory(resource).createVisualData();
    SkeletonVisual sv = (SkeletonVisual) v.getSgVisuals()[0];
    return sv;
  }

  private static List<File> exportAliceModelResourceToDir(JointedModelResource modelResource, File rootDir) throws IOException {
    ModelResourceInfo modelInfo = AliceResourceUtilities.getModelResourceInfo(modelResource.getClass(), modelResource.toString());
    SkeletonVisual sgSkeletonVisual = loadAliceModel(modelResource);
    ModelManifest modelManifest = modelInfo.createModelManifest();
    JointedModelColladaExporter exporter = new JointedModelColladaExporter(sgSkeletonVisual, modelManifest.models.get(0), modelManifest.description.name);
    return exportAliceModelToDir(exporter, rootDir);
  }
}
