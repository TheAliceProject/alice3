/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.scenegraph.builder;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.codec.BufferUtilities;
import edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder;
import edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder;
import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray;
import edu.cmu.cs.dennisc.scenegraph.OldMesh;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import edu.cmu.cs.dennisc.texture.Texture;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;

public class ModelBuilder {
  private ModelPart root;
  private Set<Geometry> geometries;
  private Set<BufferedImageTexture> textures;

  private static final String MAIN_ENTRY_PATH = "main.bin";
  private static final String INDEXED_TRIANGLE_ARRAY_PREFIX = "indexedTriangleArrays/";
  private static final String MESH_PREFIX = "meshes/";
  private static final String GEOMETRY_POSTFIX = ".bin";
  private static final String BUFFERED_IMAGE_TEXTURE_PREFIX = "bufferedImageTextures/";
  private static final String BUFFERED_IMAGE_TEXTURE_POSTFIX = ".png";

  private ModelBuilder() {
  }

  private static void safeEncode(BinaryEncoder encoder, short[] array) {
    boolean isNotNull = array != null;
    encoder.encode(isNotNull);
    if (isNotNull) {
      encoder.encode(array);
    }
  }

  private static short[] safeDecodeShortArray(BinaryDecoder decoder) {
    boolean isNotNull = decoder.decodeBoolean();
    short[] rv;
    if (isNotNull) {
      rv = decoder.decodeShortArray();
    } else {
      rv = null;
    }
    return rv;
  }

  private static Map<File, ModelBuilder> map = Maps.newHashMap();

  public Set<Geometry> getGeometries() {
    return this.geometries;
  }

  public void replaceGeometries(Map<? extends Geometry, ? extends Geometry> map) {
    this.geometries.clear();
    this.geometries.addAll(map.values());
    this.root.replaceGeometries(map);
  }

  public static void forget(File file) {
    map.remove(file);
  }

  public static ModelBuilder getInstance(File file) {
    ModelBuilder rv = map.get(file);
    if (rv != null) {
      //pass
    } else {
      rv = new ModelBuilder();
      try {
        Map<Integer, Geometry> mapIdToGeometry = Maps.newHashMap();
        Map<Integer, BufferedImageTexture> mapIdToTexture = Maps.newHashMap();
        FileInputStream fis = new FileInputStream(file);
        Map<String, byte[]> map = ZipUtilities.extract(fis);

        InputStream isMainEntry = null;
        for (String entryPath : map.keySet()) {
          InputStream is = new ByteArrayInputStream(map.get(entryPath));
          if (entryPath.startsWith(INDEXED_TRIANGLE_ARRAY_PREFIX)) {
            String s = entryPath.substring(INDEXED_TRIANGLE_ARRAY_PREFIX.length(), entryPath.length() - GEOMETRY_POSTFIX.length());
            int id = Integer.parseInt(s);
            BinaryDecoder decoder = new InputStreamBinaryDecoder(is);
            IndexedTriangleArray ita = new IndexedTriangleArray();
            ita.vertices.setValue(decoder.decodeBinaryEncodableAndDecodableArray(Vertex.class));
            ita.polygonData.setValue(decoder.decodeIntArray());
            mapIdToGeometry.put(id, ita);

          } else if (entryPath.startsWith(MESH_PREFIX)) {
            String s = entryPath.substring(MESH_PREFIX.length(), entryPath.length() - GEOMETRY_POSTFIX.length());
            int id = Integer.parseInt(s);
            BinaryDecoder decoder = new InputStreamBinaryDecoder(is);
            OldMesh mesh = new OldMesh();
            mesh.xyzs.setValue(decoder.decodeDoubleArray());
            mesh.ijks.setValue(decoder.decodeFloatArray());
            mesh.uvs.setValue(decoder.decodeFloatArray());
            mesh.xyzTriangleIndices.setValue(safeDecodeShortArray(decoder));
            mesh.ijkTriangleIndices.setValue(safeDecodeShortArray(decoder));
            mesh.uvTriangleIndices.setValue(safeDecodeShortArray(decoder));
            mesh.xyzQuadrangleIndices.setValue(safeDecodeShortArray(decoder));
            mesh.ijkQuadrangleIndices.setValue(safeDecodeShortArray(decoder));
            mesh.uvQuadrangleIndices.setValue(safeDecodeShortArray(decoder));
            mapIdToGeometry.put(id, mesh);
          } else if (entryPath.startsWith(BUFFERED_IMAGE_TEXTURE_PREFIX)) {
            String s = entryPath.substring(BUFFERED_IMAGE_TEXTURE_PREFIX.length(), entryPath.length() - BUFFERED_IMAGE_TEXTURE_POSTFIX.length());

            boolean isPotentiallyAlphaBlended = s.charAt(0) == 't';
            int id = Integer.parseInt(s.substring(1));

            BufferedImage bufferedImage = ImageUtilities.read(ImageUtilities.PNG_CODEC_NAME, is);
            BufferedImageTexture texture = new BufferedImageTexture();
            texture.setBufferedImage(bufferedImage);
            texture.setPotentiallyAlphaBlended(isPotentiallyAlphaBlended);
            mapIdToTexture.put(id, texture);
          } else {
            assert entryPath.equals(MAIN_ENTRY_PATH) : entryPath;
            isMainEntry = is;
          }
        }
        BinaryDecoder decoder = new InputStreamBinaryDecoder(isMainEntry);
        rv.root = decoder.decodeBinaryEncodableAndDecodable(/* ModelPart.class */);
        rv.root.resolve(mapIdToGeometry, mapIdToTexture);

        rv.geometries = Sets.newHashSet(mapIdToGeometry.values());
        rv.textures = Sets.newHashSet(mapIdToTexture.values());
      } catch (IOException ioe) {
        throw new RuntimeException(file.toString(), ioe);
      }
      map.put(file, rv);
    }
    return rv;
  }

  public void encode(File file) throws IOException {
    FileUtilities.createParentDirectoriesIfNecessary(file);

    FileOutputStream fos = new FileOutputStream(file);
    ZipOutputStream zos = new ZipOutputStream(fos);
    for (final Geometry geometry : this.geometries) {
      ZipUtilities.write(zos, new DataSource() {
        @Override
        public String getName() {
          return getEntryPath(geometry);
        }

        @Override
        public void write(OutputStream os) throws IOException {
          BinaryEncoder encoder = new OutputStreamBinaryEncoder(os);
          if (geometry instanceof IndexedTriangleArray) {
            IndexedTriangleArray ita = (IndexedTriangleArray) geometry;
            encoder.encode(ita.vertices.getValue());
            BufferUtilities.encode(encoder, ita.polygonData.getValue(), false);
          } else if (geometry instanceof OldMesh) {
            OldMesh mesh = (OldMesh) geometry;
            encoder.encode(mesh.xyzs.getValue());
            encoder.encode(mesh.ijks.getValue());
            encoder.encode(mesh.uvs.getValue());
            safeEncode(encoder, mesh.xyzTriangleIndices.getValue());
            safeEncode(encoder, mesh.ijkTriangleIndices.getValue());
            safeEncode(encoder, mesh.uvTriangleIndices.getValue());
            safeEncode(encoder, mesh.xyzQuadrangleIndices.getValue());
            safeEncode(encoder, mesh.ijkQuadrangleIndices.getValue());
            safeEncode(encoder, mesh.uvQuadrangleIndices.getValue());
          } else {
            assert false;
          }
          encoder.flush();
        }
      });
    }
    for (final BufferedImageTexture texture : this.textures) {
      ZipUtilities.write(zos, new DataSource() {
        @Override
        public String getName() {
          return getEntryPath(texture);
        }

        @Override
        public void write(OutputStream os) throws IOException {
          ImageUtilities.write(ImageUtilities.PNG_CODEC_NAME, os, texture.getBufferedImage());
        }
      });
    }
    ZipUtilities.write(zos, new DataSource() {
      @Override
      public String getName() {
        return MAIN_ENTRY_PATH;
      }

      @Override
      public void write(OutputStream os) throws IOException {
        BinaryEncoder encoder = new OutputStreamBinaryEncoder(os);
        encoder.encode(root);
        encoder.flush();
      }
    });
    zos.flush();
    zos.close();
  }

  public static ModelBuilder newInstance(Transformable transformable) {
    ModelBuilder rv = new ModelBuilder();
    rv.geometries = Sets.newHashSet();
    rv.textures = Sets.newHashSet();
    rv.root = ModelPart.newInstance(transformable, rv.geometries, rv.textures);
    return rv;
  }

  public Transformable buildTransformable() {
    Transformable rv = this.root.build();
    return rv;
  }

  private static String getEntryPath(Geometry geometry) {
    if (geometry instanceof IndexedTriangleArray) {
      return INDEXED_TRIANGLE_ARRAY_PREFIX + geometry.hashCode() + GEOMETRY_POSTFIX;
    } else if (geometry instanceof OldMesh) {
      return MESH_PREFIX + geometry.hashCode() + GEOMETRY_POSTFIX;
    } else {
      return null;
    }
  }

  private static String getEntryPath(Texture texture) {
    if (texture instanceof BufferedImageTexture) {
      BufferedImageTexture bufferedImageTexture = (BufferedImageTexture) texture;
      char c;
      if (bufferedImageTexture.isPotentiallyAlphaBlended()) {
        c = 't';
      } else {
        c = 'f';
      }
      return BUFFERED_IMAGE_TEXTURE_PREFIX + c + texture.hashCode() + ".png";
    } else {
      return null;
    }
  }
}
