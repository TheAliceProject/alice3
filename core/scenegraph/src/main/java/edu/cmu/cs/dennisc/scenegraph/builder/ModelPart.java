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
import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.scenegraph.Appearance;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import edu.cmu.cs.dennisc.texture.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModelPart implements BinaryEncodableAndDecodable {
  private String name;
  private AffineMatrix4x4 localTransformation;
  private Geometry geometry;
  private BufferedImageTexture texture;
  private List<ModelPart> children;

  private Integer geometryID = null;
  private Integer textureID = null;

  private ModelPart() {
  }

  public ModelPart(BinaryDecoder binaryDecoder) {
    this.name = binaryDecoder.decodeString();
    this.localTransformation = binaryDecoder.decodeBinaryEncodableAndDecodable(/* edu.cmu.cs.dennisc.math.AffineMatrix4x4.class */);
    this.geometryID = binaryDecoder.decodeInt();
    this.textureID = binaryDecoder.decodeInt();
    final int N = binaryDecoder.decodeInt();
    ArrayList<ModelPart> arrayList = Lists.newArrayList();
    arrayList.ensureCapacity(N);
    for (int i = 0; i < N; i++) {
      ModelPart modelPart = binaryDecoder.decodeBinaryEncodableAndDecodable(/* ModelPart.class */);
      arrayList.add(modelPart);
    }
    this.children = arrayList;
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    binaryEncoder.encode(this.name);
    binaryEncoder.encode(this.localTransformation);
    binaryEncoder.encode(getID(this.geometry));
    binaryEncoder.encode(getID(this.texture));
    binaryEncoder.encode(this.children.size());
    for (ModelPart child : this.children) {
      binaryEncoder.encode(child);
    }
  }

  public static ModelPart newInstance(Transformable parent, Set<Geometry> geometries, Set<BufferedImageTexture> textures) {
    ModelPart rv = new ModelPart();
    rv.name = parent.getName();
    rv.localTransformation = parent.localTransformation.getValue();
    rv.children = Lists.newLinkedList();

    for (Component component : parent.getComponents()) {
      if (component instanceof Visual) {
        Visual visual = (Visual) component;
        Appearance front = visual.frontFacingAppearance.getValue();
        if (front instanceof TexturedAppearance) {
          TexturedAppearance singleAppearance = (TexturedAppearance) front;
          Texture texture = singleAppearance.diffuseColorTexture.getValue();
          if (texture != null) {
            if (texture instanceof BufferedImageTexture) {
              rv.texture = (BufferedImageTexture) texture;
              textures.add(rv.texture);
            } else {
              assert false;
            }
          } else {
            Logger.warning("no texture for ", rv.name);
          }
        } else {
          assert false;
        }
        rv.geometry = visual.getGeometry();
        if (rv.geometry != null) {
          Matrix3x3 scale = visual.scale.getValue();
          if (rv.geometry instanceof IndexedTriangleArray) {
            IndexedTriangleArray sgITA = (IndexedTriangleArray) rv.geometry;
            if (scale.isIdentity()) {
              //pass
            } else {
              //              System.err.println( "fixing scale for: " + rv.name + " " + scale.right.x + " " + scale.up.y + " " + scale.backward.z );
              for (Vertex v : sgITA.vertices.getValue()) {
                v.position.x *= scale.right.x;
                v.position.y *= scale.up.y;
                v.position.z *= scale.backward.z;
              }
            }
          } else {
            assert false;
          }
          geometries.add(rv.geometry);
        } else {
          Logger.warning("no geometry for ", rv.name);
        }
      } else if (component instanceof Transformable) {
        Transformable transformable = (Transformable) component;
        rv.children.add(newInstance(transformable, geometries, textures));
      }
    }
    return rv;
  }

  public Transformable build() {
    Transformable rv = new Transformable();
    Visual visual = new Visual();
    TexturedAppearance appearance = new TexturedAppearance();
    appearance.setDiffuseColorTexture(this.texture);
    visual.frontFacingAppearance.setValue(appearance);
    assert this.geometry != null;
    visual.setGeometry(this.geometry);
    rv.addComponent(visual);
    rv.localTransformation.setValue(new AffineMatrix4x4(this.localTransformation));
    for (ModelPart child : this.children) {
      rv.addComponent(child.build());
    }
    return rv;
  }

  public void resolve(Map<Integer, Geometry> mapIdToGeometry, Map<Integer, BufferedImageTexture> mapIdToTexture) {
    if (this.geometryID != 0) {
      assert mapIdToGeometry.containsKey(this.geometryID) : geometryID;
      this.geometry = mapIdToGeometry.get(this.geometryID);
      assert this.geometry != null : this.name;
    }
    if (this.textureID != 0) {
      assert mapIdToTexture.containsKey(this.textureID) : textureID;
      this.texture = mapIdToTexture.get(this.textureID);
      assert this.texture != null : this.name;
    }

    this.geometryID = null;
    this.textureID = null;
    for (ModelPart child : this.children) {
      child.resolve(mapIdToGeometry, mapIdToTexture);
    }
  }

  public void replaceGeometries(Map<? extends Geometry, ? extends Geometry> map) {
    if (this.geometry != null) {
      this.geometry = map.get(this.geometry);
    }
    for (ModelPart child : this.children) {
      child.replaceGeometries(map);
    }
  }

  private static int getID(Object o) {
    int rv;
    if (o != null) {
      rv = o.hashCode();
      assert rv != 0;
    } else {
      rv = 0;
    }
    return rv;
  }
}
