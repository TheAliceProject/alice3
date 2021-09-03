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

import de.javagl.jgltf.impl.v2.*;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.GltfModels;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.impl.creation.BufferStructure;
import de.javagl.jgltf.model.impl.creation.BufferStructureBuilder;
import de.javagl.jgltf.model.impl.creation.BufferStructureGltfV2;
import de.javagl.jgltf.model.impl.creation.BufferStructures;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.GltfModelWriter;
import de.javagl.jgltf.model.io.GltfReference;
import de.javagl.jgltf.model.io.GltfReferenceResolver;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.WeightInfo;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import org.alice.tweedle.file.ModelManifest;
import org.lgna.project.io.JointedModelExporter;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JointedModelGltfExporter implements JointedModelExporter {

  private static final String MODEL_EXTENSION = "glb";
  private static final String IMAGE_EXTENSION = "png";
  private static final int indicesComponentType = GltfConstants.GL_UNSIGNED_SHORT;

  private final SkeletonVisual visual;
  private final Map<String, String> renamedJoints;
  private final String resourcePath;
  private final String fullResourceName;
  private final HashMap<Integer, Integer> textureMaterialMap = new HashMap<>();
  private final HashMap<String, Integer> meshSkinMap = new HashMap<>();


  public JointedModelGltfExporter(SkeletonVisual sv, ModelManifest.ModelVariant modelVariant, String modelName, String resourcePath, Map<String, String> renamedJoints) {
    this.visual = sv;
    this.resourcePath = resourcePath;
    this.renamedJoints = renamedJoints;
    this.fullResourceName = modelVariant == null ? modelName : modelVariant.textureSet;
  }

  @Override
  public DataSource createStructureDataSource() {
    final String name = resourcePath + "/" + fullResourceName + "." + MODEL_EXTENSION;
    return new DataSource() {
      @Override
      public String getName() {
        return name;
      }

      @Override
      public void write(OutputStream os) throws IOException {
        GltfModelWriter writer = new GltfModelWriter();
        final GltfModel model = createModel();
        writer.writeBinary(model, os);
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
  public void addImageDataSources(List<DataSource> dataSources, ModelManifest modelManifest, Map<Integer, String> resourceMap) {
  }

  private static Asset createAssetDetail() {
    Asset asset = new Asset();
    asset.setGenerator("Alice3 Exporter");
    asset.setVersion("2.0");
    return asset;
  }

  protected GltfModel createModel() {
    GlTF gltf = new GlTF();
    gltf.setAsset(createAssetDetail());

    Node origin = new Node();
    origin.setName(fullResourceName);
    int originIndex = Optionals.of(gltf.getNodes()).size();
    gltf.addNodes(origin);

    Map<String, Integer> jointNodes = addSkeletonNodes(gltf);
    // First node after origin will be root joint of skeleton
    origin.addChildren(originIndex + 1);

    Path tempDir;
    try {
      tempDir = Files.createTempDirectory(null);
      createAndAddTextureComponents(tempDir, gltf);
    } catch (IOException e) {
      throw new RuntimeException("Failed to create temp directory and write model textures. Unable to complete export.", e);
    }

    BufferStructureBuilder bufferStructureBuilder = new BufferStructureBuilder();

    final List<Mesh> meshes = createMeshes(bufferStructureBuilder, gltf, jointNodes);

    int bufferCounter = bufferStructureBuilder.getNumBufferModels();
    String bufferName = fullResourceName + "buffer" + bufferCounter;
    String uri = bufferName + ".bin";
    bufferStructureBuilder.createBufferModel(bufferName, uri);

    // Transfer the data from the buffer structure into the glTF
    BufferStructure bufferStructure = bufferStructureBuilder.build();
    gltf.setAccessors(BufferStructureGltfV2.createAccessors(bufferStructure));
    gltf.setBufferViews(BufferStructureGltfV2.createBufferViews(bufferStructure));
    gltf.setBuffers(BufferStructureGltfV2.createBuffers(bufferStructure));

    for (Mesh mesh : meshes) {
      int meshIndex = Optionals.of(gltf.getMeshes()).size();
      int nodeIndex = Optionals.of(gltf.getNodes()).size();
      gltf.addMeshes(mesh);
      Node meshNode = new Node();
      meshNode.setName(mesh.getName());
      meshNode.setMesh(meshIndex);
      if (meshSkinMap.containsKey(mesh.getName())) {
        meshNode.setSkin(meshSkinMap.get(mesh.getName()));
      }
      origin.addChildren(nodeIndex);
      gltf.addNodes(meshNode);
    }

    Scene scene = new Scene();
    scene.setName(fullResourceName);
    scene.addNodes(originIndex);

    int sceneIndex = Optionals.of(gltf.getScenes()).size();
    gltf.addScenes(scene);
    gltf.setScene(sceneIndex);

    GltfAssetV2 gltfAsset = new GltfAssetV2(gltf, null);
    resolveImages(tempDir, gltfAsset);
    resolveBuffers(bufferStructure, gltfAsset);

    return GltfModels.create(gltfAsset);
  }

  private void resolveImages(Path tempDir, GltfAssetV2 _gltfAsset) {
    List<GltfReference> _refList = _gltfAsset.getImageReferences();
    URI _baseUri = tempDir.toAbsolutePath().toUri();
    GltfReferenceResolver.resolveAll(_refList, _baseUri);
  }

  private void resolveBuffers(BufferStructure bufferStructure, GltfAssetV2 gltfAsset) {
    List<GltfReference> bufferReferences = gltfAsset.getBufferReferences();
    BufferStructures.resolve(bufferReferences, bufferStructure);
  }

  private void createAndAddTextureComponents(Path tempDir, GlTF gltf) throws IOException {
    for (TexturedAppearance texture : visual.textures.getValue()) {
      Integer textureId = texture.textureId.getValue();
      MaterialPbrMetallicRoughness pbrMetallicRoughness = new MaterialPbrMetallicRoughness();
      pbrMetallicRoughness.setMetallicFactor(0.0f);

      boolean alphaBlend = false;

      // Embed diffuse texture
      if (texture.diffuseColorTexture.getValue() != null) {
        Image image = new de.javagl.jgltf.impl.v2.Image();
        final String uri = getImageFileName(textureId);
        image.setUri(uri);
        final Path imageFile = tempDir.resolve(uri);
        writeTexture(texture, Files.newOutputStream(imageFile));

        int imageIndex = Optionals.of(gltf.getImages()).size();
        gltf.addImages(image);

        Texture textureOut = new Texture();
        textureOut.setSource(imageIndex);
        int textureIndex = Optionals.of(gltf.getTextures()).size();
        gltf.addTextures(textureOut);

        TextureInfo baseColorTexture = new TextureInfo();
        baseColorTexture.setIndex(textureIndex);
        pbrMetallicRoughness.setBaseColorTexture(baseColorTexture);

        alphaBlend = alphaBlend || texture.isDiffuseColorTextureAlphaBlended.getValue();
      }

      // TODO: Transform bump map to normal map

      float opacity = texture.opacity.getValue() != null ? texture.opacity.getValue() : 1.0f;
      float[] baseColorFactor = null;

      // Apply base diffuse color
      if (texture.diffuseColor.getValue() != null) {
        Color4f baseColor = texture.diffuseColor.getValue();
        // Multiply the texture's opacity with the alpha channel of the base color
        baseColorFactor = new float[]{baseColor.red, baseColor.green, baseColor.blue, baseColor.alpha * opacity};
        alphaBlend = alphaBlend || baseColor.alpha < 1.0f;
      } else if (opacity != 1.0f) {
        // Just apply opacity
        baseColorFactor = new float[]{1.0f, 1.0f, 1.0f, opacity};
      }

      pbrMetallicRoughness.setBaseColorFactor(baseColorFactor);

      Material material = new Material();
      material.setPbrMetallicRoughness(pbrMetallicRoughness);

      // Apply emissive color
      if (texture.emissiveColor.getValue() != null) {
        Color4f emissiveColor = texture.emissiveColor.getValue();
        float[] emissiveFactor = new float[]{emissiveColor.red, emissiveColor.green, emissiveColor.blue};
        material.setEmissiveFactor(emissiveFactor);
      }

      // If we have transparency in our material we need to activate alpha blending which is off by default
      material.setAlphaMode(alphaBlend ? "BLEND" : material.defaultAlphaMode());

      int materialIndex = Optionals.of(gltf.getMaterials()).size();
      gltf.addMaterials(material);
      textureMaterialMap.put(textureId, materialIndex);
    }
  }

  private String getImageFileName(Integer textureId) {
    return fullResourceName + "_material_" + textureId + "_diffuseMap." + IMAGE_EXTENSION;
  }

  private static void writeTexture(TexturedAppearance texture, OutputStream os) throws IOException {
    BufferedImageTexture bufferedTexture = (BufferedImageTexture) texture.diffuseColorTexture.getValue();
    ImageIO.write(bufferedTexture.getBufferedImage(), IMAGE_EXTENSION, os);
  }


  private Map<String, Integer> addSkeletonNodes(GlTF gltf) {
    Joint rootJoint = visual.skeleton.getValue();
    Map<String, Integer> jointIndices = new HashMap<>();
    if (rootJoint != null) {
      addNodeForJoint(rootJoint, gltf, jointIndices);
    }
    return jointIndices;
  }

  private int addNodeForJoint(Joint joint, GlTF gltf, Map<String, Integer> jointIndices) {
    Node node = createJointNode(joint);
    int nodeIndex = Optionals.of(gltf.getNodes()).size();
    gltf.addNodes(node);
    jointIndices.put(node.getName(), nodeIndex);

    for (Component c : joint.getComponents()) {
      if (c instanceof Joint) {
        int childIndex = addNodeForJoint((Joint) c, gltf, jointIndices);
        node.addChildren(childIndex);
      }
    }
    return nodeIndex;
  }

  private Node createJointNode(Joint joint) {
    Node node = new Node();
    node.setName(getUserJointIdentifier(joint.jointID.getValue()));

    final AffineMatrix4x4 jointTransform = joint.localTransformation.getValue();
    float[] matrixValues = new float[16];
    matrixValues = jointTransform.getAsColumnMajorArray16(matrixValues);
    node.setMatrix(matrixValues);
    return node;
  }

  public String getUserJointIdentifier(String jointIdentifier) {
    return renamedJoints.getOrDefault(jointIdentifier, jointIdentifier);
  }

  private List<Mesh> createMeshes(BufferStructureBuilder bufferStructureBuilder, GlTF gltf, Map<String, Integer> jointNodes) {
    List<Mesh> meshes = new ArrayList<>();

    for (Geometry g : visual.geometries.getValue()) {
      if (g instanceof edu.cmu.cs.dennisc.scenegraph.Mesh) {
        edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh = (edu.cmu.cs.dennisc.scenegraph.Mesh) g;
        MeshPrimitive meshPrimitive = createMeshPrimitive(sgMesh, bufferStructureBuilder);
        Mesh mesh = createMesh(sgMesh, meshPrimitive);
        meshes.add(mesh);
      }
    }
    for (WeightedMesh sgWM : visual.weightedMeshes.getValue()) {
      Skin skin = addSkin(sgWM, bufferStructureBuilder, gltf, jointNodes);
      MeshPrimitive meshPrimitive = createWeightedMeshPrimitive(sgWM, bufferStructureBuilder, jointNodes, skin);
      Mesh mesh = createMesh(sgWM, meshPrimitive);
      meshes.add(mesh);
    }
    return meshes;
  }

  private Mesh createMesh(edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh, MeshPrimitive meshPrimitive) {
    Mesh mesh = new Mesh();
    mesh.addPrimitives(meshPrimitive);
    mesh.setName(sgMesh.getName());
    return mesh;
  }

  private MeshPrimitive createMeshPrimitive(edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh, BufferStructureBuilder builder) {
    MeshPrimitive meshPrimitive = new MeshPrimitive();
    meshPrimitive.setMode(GltfConstants.GL_TRIANGLES);

    if (textureMaterialMap.containsKey(sgMesh.textureId.getValue())) {
      meshPrimitive.setMaterial(textureMaterialMap.get(sgMesh.textureId.getValue()));
    } else {
      System.err.println("Missing material for texture id " + sgMesh.textureId);
    }

    // Add the indices data from the mesh to the buffer structure
    int indicesAccessorIndex = builder.getNumAccessorModels();
    IntBuffer indexBuffer = sgMesh.indexBuffer.getValue();
    indexBuffer.rewind();
    builder.createAccessorModel("indicesAccessor_" + indicesAccessorIndex,
                                indicesComponentType, "SCALAR", Buffers.castToShortByteBuffer(indexBuffer));
    meshPrimitive.setIndices(indicesAccessorIndex);
    builder.createArrayElementBufferViewModel(sgMesh.getName() + "_indices_bufferView");

    // Add the vertices (positions) from the mesh to the buffer structure
    int positionsAccessorIndex = builder.getNumAccessorModels();
    DoubleBuffer vertexBuffer = sgMesh.vertexBuffer.getValue();
    vertexBuffer.rewind();
    FloatBuffer objVertices = BufferUtilities.createDirectFloatBuffer(convertToFloatArray(vertexBuffer));
    builder.createAccessorModel("positionsAccessor_" + positionsAccessorIndex,
                                GltfConstants.GL_FLOAT, "VEC3", Buffers.createByteBufferFrom(objVertices));
    meshPrimitive.addAttributes("POSITION", positionsAccessorIndex);
    builder.createArrayBufferViewModel(sgMesh.getName() + "_positions_bufferView");

    // Add the texture coordinates from the mesh to the buffer structure
    FloatBuffer objTexCoords = sgMesh.textCoordBuffer.getValue();
    objTexCoords.rewind();
    if (objTexCoords.hasRemaining()) {
      int texCoordsAccessorIndex = builder.getNumAccessorModels();
      builder.createAccessorModel("texCoordsAccessor_" + texCoordsAccessorIndex,
                                  GltfConstants.GL_FLOAT, "VEC2", Buffers.createByteBufferFrom(objTexCoords));
      meshPrimitive.addAttributes("TEXCOORD_0", texCoordsAccessorIndex);
      builder.createArrayBufferViewModel(sgMesh.getName() + "_textCoords_bufferView");
    }

    // Add the normals from the mesh to the buffer structure
    FloatBuffer normalBuffer = sgMesh.normalBuffer.getValue();
    normalBuffer.rewind();
    FloatBuffer objNormals = BufferUtilities.copyFloatBuffer(normalBuffer);
    if (objNormals.hasRemaining()) {
      normalize(objNormals);
      int normalsAccessorIndex = builder.getNumAccessorModels();
      builder.createAccessorModel("normalsAccessor_" + normalsAccessorIndex,
                                  GltfConstants.GL_FLOAT, "VEC3", Buffers.createByteBufferFrom(objNormals));
      meshPrimitive.addAttributes("NORMAL", normalsAccessorIndex);
      builder.createArrayBufferViewModel(sgMesh.getName() + "_normals_bufferView");
    }
    return meshPrimitive;
  }

  private static class JointWeightQuad {
    int[] jointData;
    float[] weightData;
    int offset;

    public JointWeightQuad(int length, int offset) {
      jointData = new int[4 * length];
      weightData = new float[4 * length];
      this.offset = offset;
    }
  }

  private MeshPrimitive createWeightedMeshPrimitive(WeightedMesh sgWM, BufferStructureBuilder builder, Map<String, Integer> jointNodes, Skin skin) {
    MeshPrimitive meshPrimitive = createMeshPrimitive(sgWM, builder);
    VertexWeights[] vertexWeights = getWeightsByVertex(sgWM, jointNodes, skin);
    int highestJointCount = computeHighestJointCount(vertexWeights);
    int quadCount = (highestJointCount + 3) / 4;
    JointWeightQuad[] quads = new JointWeightQuad[quadCount];
    for (int i = 0; i < quadCount; i++) {
      quads[i] = new JointWeightQuad(vertexWeights.length, i);
    }
    for (int i = 0; i < vertexWeights.length; i++) {
      VertexWeights vertexWeight = vertexWeights[i];
      if (null == vertexWeight) {
        continue;
      }
      List<Integer> joints = vertexWeight.jointIndices;
      List<Float> weights = vertexWeight.weights;
      for (int j = 0; j < joints.size(); j++) {
        final JointWeightQuad quad = quads[j / 4];
        int index = 4 * i + j % 4;
        quad.jointData[index] = joints.get(j);
        quad.weightData[index] = weights.get(j);
      }
    }
    for (int i = 0; i < quadCount; i++) {
      buildJointWeightBuffers(sgWM, builder, meshPrimitive, quads[i]);
    }
    return meshPrimitive;
  }

  private int computeHighestJointCount(VertexWeights[] vertexWeights) {
    int highestJointCount = 0;

    for (VertexWeights vertexWeight : vertexWeights) {
      if (null == vertexWeight) {
        continue;
      }

      int numJoints = vertexWeight.jointIndices.size();

      if (numJoints > highestJointCount) {
        highestJointCount = numJoints;
      }
    }

    return highestJointCount;
  }

  private void buildJointWeightBuffers(WeightedMesh sgWM, BufferStructureBuilder builder, MeshPrimitive meshPrimitive, JointWeightQuad quad) {
    int jointsAccessorIndex = builder.getNumAccessorModels();
    IntBuffer joints = BufferUtilities.createDirectIntBuffer(quad.jointData);
    builder.createAccessorModel("jointsAccessor_" + jointsAccessorIndex,
                                GltfConstants.GL_UNSIGNED_SHORT, "VEC4", Buffers.castToShortByteBuffer(joints));
    meshPrimitive.addAttributes("JOINTS_" + quad.offset, jointsAccessorIndex);
    builder.createArrayBufferViewModel(sgWM.getName() + "_joints_bufferView" + quad.offset);

    int weightsAccessorIndex = builder.getNumAccessorModels();
    FloatBuffer weights = BufferUtilities.createDirectFloatBuffer(quad.weightData);
    builder.createAccessorModel("weightsAccessor_" + weightsAccessorIndex,
                                GltfConstants.GL_FLOAT, "VEC4", Buffers.createByteBufferFrom(weights));
    meshPrimitive.addAttributes("WEIGHTS_" + quad.offset, weightsAccessorIndex);
    builder.createArrayBufferViewModel(sgWM.getName() + "_weights_bufferView" + quad.offset);
  }

  public static class VertexWeights {
    public final List<Integer> jointIndices = new ArrayList<>();
    public final List<Float> weights = new ArrayList<>();

    private void addJointWeight(int jointIndex, float weight) {
      jointIndices.add(jointIndex);
      weights.add(weight);
    }
  }

  public VertexWeights[] getWeightsByVertex(WeightedMesh sgWM, Map<String, Integer> jointNodes, Skin skin) {
    WeightInfo wi = sgWM.weightInfo.getValue();
    int vertexCount = sgWM.vertexBuffer.getValue().limit() / 3;
    VertexWeights[] skinWeights = new VertexWeights[vertexCount];
    final Map<String, InverseAbsoluteTransformationWeightsPair> weightsPairMap = wi.getMap();
    for (Map.Entry<String, Integer> jointEntry : jointNodes.entrySet()) {
      InverseAbsoluteTransformationWeightsPair iatwp = weightsPairMap.get(jointEntry.getKey());
      if (iatwp == null) {
        continue;
      }
      iatwp.reset();
      while (!iatwp.isDone()) {
        int vertexIndex = iatwp.getIndex();
        if (skinWeights[vertexIndex] == null) {
          skinWeights[vertexIndex] = new VertexWeights();
        }
        final Integer jointIndex = jointEntry.getValue();
        int skinJointIndex = skin.getJoints().indexOf(jointIndex);
        skinWeights[vertexIndex].addJointWeight(skinJointIndex, iatwp.getWeight());
        iatwp.advance();
      }
    }
    return skinWeights;
  }

  private Skin addSkin(WeightedMesh sgWM, BufferStructureBuilder builder, GlTF gltf, Map<String, Integer> jointNodes) {
    Skin skin = new Skin();
    // Add the Inverse Bind Matrices from the mesh to the buffer structure
    int ibmIndex = builder.getNumAccessorModels();
    final Set<Map.Entry<String, InverseAbsoluteTransformationWeightsPair>> entries = sgWM.weightInfo.getValue().getMap().entrySet();
    float[] matrices = new float[16 * entries.size()];
    float[] matrix = new float[16];
    int index = 0;
    for (Map.Entry<String, InverseAbsoluteTransformationWeightsPair> entry : entries) {
      skin.addJoints(jointNodes.get(entry.getKey()));
      AffineMatrix4x4 inverseBindMatrix = entry.getValue().getInverseAbsoluteTransformation();
      inverseBindMatrix.getAsColumnMajorArray16(matrix);
      System.arraycopy(matrix, 0, matrices, index, 16);
      index += 16;
    }

    FloatBuffer ibmBuffer = BufferUtilities.createDirectFloatBuffer(matrices);
    builder.createAccessorModel("ibmsAccessor_" + ibmIndex, GltfConstants.GL_FLOAT, "MAT4", Buffers.createByteBufferFrom(ibmBuffer));

    builder.createBufferViewModel(sgWM.getName() + "_skin_bufferView", null);

    skin.setInverseBindMatrices(ibmIndex);
    meshSkinMap.put(sgWM.getName(), Optionals.of(gltf.getSkins()).size());
    gltf.addSkins(skin);
    return skin;
  }

  private static final double EPSILON = 1e-6;

  // Borrowed from JglTF/ObjNormals.java
  public static void normalize(FloatBuffer normals) {
    int numZeroLengthNormals = 0;
    int numNaNNormals = 0;
    int n = normals.remaining() / 3;
    for (int i = 0; i < n; i++) {
      float x = normals.get(i * 3);
      float y = normals.get(i * 3 + 1);
      float z = normals.get(i * 3 + 2);
      float nx = 1.0f;
      float ny = 0.0f;
      float nz = 0.0f;
      if (Float.isNaN(x) || Float.isNaN(y) || Float.isNaN(z)) {
        numNaNNormals++;
      } else {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length < EPSILON) {
          numZeroLengthNormals++;
        } else {
          float invLength = (float) (1.0 / length);
          nx = x * invLength;
          ny = y * invLength;
          nz = z * invLength;
        }
      }
      normals.put(i * 3, nx);
      normals.put(i * 3 + 1, ny);
      normals.put(i * 3 + 2, nz);
    }
    if (numZeroLengthNormals > 0) {
      System.out.println("There have been " + numZeroLengthNormals + " normals with zero length. Using (1,0,0) as a default.");
    }
    if (numNaNNormals > 0) {
      System.out.println("There have been " + numNaNNormals + " normals with NaN components. Using (1,0,0) as a default.");
    }
  }

  public static float[] convertToFloatArray(DoubleBuffer buf) {
    if (buf == null) {
      return new float[0];
    }
    float[] array = new float[buf.remaining()];
    int index = 0;
    while (buf.hasRemaining()) {
      array[index++] = (float) buf.get();
      array[index++] = (float) buf.get();
      array[index++] = (float) buf.get();
    }

    buf.rewind();
    return array;
  }

}
