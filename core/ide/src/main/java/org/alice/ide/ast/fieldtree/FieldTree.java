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
package org.alice.ide.ast.fieldtree;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.io.IoUtilities;
import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SProp;
import org.lgna.story.SQuadruped;
import org.lgna.story.SShape;
import org.lgna.story.SSwimmer;
import org.lgna.story.SThing;

import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class FieldTree {
  public static class TypeCollapseThresholdData {
    private final AbstractType<?, ?, ?> type;
    private final int collapseThreshold;
    private final int collapseThresholdForDescendants;

    public TypeCollapseThresholdData(AbstractType<?, ?, ?> type, int collapseThreshold, int collapseThresholdForDescendants) {
      this.type = type;
      this.collapseThreshold = collapseThreshold;
      this.collapseThresholdForDescendants = collapseThresholdForDescendants;
    }

    public TypeCollapseThresholdData(Class<?> cls, int collapseThreshold, int collapseThresholdForDescendants) {
      this(JavaType.getInstance(cls), collapseThreshold, collapseThresholdForDescendants);
    }

    public AbstractType<?, ?, ?> getType() {
      return this.type;
    }

    public int getCollapseThreshold() {
      return this.collapseThreshold;
    }

    public int getCollapseThresholdForDescendants() {
      return this.collapseThresholdForDescendants;
    }
  }

  private static class Data {
    private final Map<AbstractType, TypeNode> map = Maps.newHashMap();
    private final RootNode root = new RootNode();

    public Data(TypeCollapseThresholdData[] topLevels) {
      //map.put( null, this.root );
      for (TypeCollapseThresholdData typeCollapseThresholdData : topLevels) {
        TypeNode typeNode = TypeNode.createAndAddToParent(root, typeCollapseThresholdData.getType(), typeCollapseThresholdData.getCollapseThreshold(), typeCollapseThresholdData.getCollapseThresholdForDescendants());
        map.put(typeCollapseThresholdData.getType(), typeNode);
      }
    }

    private TypeNode getTypeNode(AbstractType<?, ?, ?> type) {
      if (type != null) {
        TypeNode typeNode = map.get(type);
        if (typeNode != null) {
          //pass
        } else {
          TypeNode superTypeNode = getTypeNode(type.getSuperType());
          int collapseThreshold = superTypeNode.getCollapseThresholdForDescendants();
          typeNode = TypeNode.createAndAddToParent(superTypeNode, type, collapseThreshold, collapseThreshold);
          map.put(type, typeNode);
        }
        return typeNode;
      } else {
        return null;
      }
    }

    public void insertField(UserField field) {
      FieldNode.createAndAddToParent(getTypeNode(field.getValueType()), field);
    }

    public RootNode getRoot() {
      return this.root;
    }
  }

  private FieldTree() {
    throw new AssertionError();
  }

  public static RootNode createTreeFor(Iterable<UserField> fields, TypeCollapseThresholdData... topLevels) {
    Data data = new Data(topLevels);
    for (UserField field : fields) {
      data.insertField(field);
    }
    RootNode root = data.getRoot();
    root.collapseIfAppropriate();
    root.removeEmptyTypeNodes();
    root.sort();

    return root;
  }

  private static final int COLLAPSE_THRESHOLD_FOR_FIRST_CLASS = 10;
  private static final int COLLAPSE_THRESHOLD_FOR_DESCENDANTS_OF_FIRST_CLASS = COLLAPSE_THRESHOLD_FOR_FIRST_CLASS;
  private static final int COLLAPSE_THRESHOLD_FOR_SECOND_CLASS = COLLAPSE_THRESHOLD_FOR_FIRST_CLASS / 2;
  private static final int COLLAPSE_THRESHOLD_FOR_DESCENDANTS_OF_SECOND_CLASS = COLLAPSE_THRESHOLD_FOR_SECOND_CLASS;

  public static TypeCollapseThresholdData createFirstClassThreshold(Class<?> cls) {
    return new TypeCollapseThresholdData(cls, COLLAPSE_THRESHOLD_FOR_FIRST_CLASS, COLLAPSE_THRESHOLD_FOR_DESCENDANTS_OF_FIRST_CLASS);
  }

  public static TypeCollapseThresholdData createSecondClassThreshold(Class<?> cls) {
    return new TypeCollapseThresholdData(cls, COLLAPSE_THRESHOLD_FOR_SECOND_CLASS, COLLAPSE_THRESHOLD_FOR_DESCENDANTS_OF_SECOND_CLASS);
  }

  public static void main(String[] args) throws Exception {
    Project project = IoUtilities.readProject(args[0]);
    NamedUserType sceneType = (NamedUserType) project.getProgramType().fields.get(0).getValueType();

    RootNode root = createTreeFor(sceneType.fields, createFirstClassThreshold(SBiped.class), createFirstClassThreshold(SQuadruped.class), createFirstClassThreshold(SSwimmer.class), createFirstClassThreshold(SFlyer.class),

                                  createSecondClassThreshold(SProp.class), createSecondClassThreshold(SShape.class), createSecondClassThreshold(SThing.class), createSecondClassThreshold(Object.class));
    StringBuilder sb = new StringBuilder();
    root.append(sb, 0);
    System.out.println(sb.toString());
  }
}
