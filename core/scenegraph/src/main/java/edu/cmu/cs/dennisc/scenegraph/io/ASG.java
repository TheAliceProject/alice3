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
package edu.cmu.cs.dennisc.scenegraph.io;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Tuple2f;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.math.Tuple3f;
import edu.cmu.cs.dennisc.math.Vector2f;
import edu.cmu.cs.dennisc.math.Vector3f;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.texture.TextureCoordinate2f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.cmu.cs.dennisc.scenegraph.Vertex;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

//todo: handle Affector[] ?

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractPropertyReference {
  private edu.cmu.cs.dennisc.scenegraph.Element m_element;
  private InstanceProperty m_property;

  public AbstractPropertyReference(edu.cmu.cs.dennisc.scenegraph.Element element, InstanceProperty property) {
    m_element = element;
    m_property = property;
  }

  public abstract void resolve(HashMap<Integer, edu.cmu.cs.dennisc.scenegraph.Element> map);

  protected edu.cmu.cs.dennisc.scenegraph.Element getElement() {
    return m_element;
  }

  protected String getPropertyName() {
    return m_property.getName();
  }

  protected void setPropertyValue(Object value) {
    m_property.setValue(value);
  }
}

/**
 * @author Dennis Cosgrove
 */
class PropertyReferenceToElement extends AbstractPropertyReference {
  private Integer m_key;

  public PropertyReferenceToElement(edu.cmu.cs.dennisc.scenegraph.Element element, InstanceProperty<?> property, Integer key) {
    super(element, property);
    m_key = key;
  }

  @Override
  public void resolve(HashMap<Integer, edu.cmu.cs.dennisc.scenegraph.Element> map) {
    edu.cmu.cs.dennisc.scenegraph.Element value = map.get(m_key);
    if (value != null) {
      setPropertyValue(value);
    } else {
      throw new RuntimeException("could not resolve reference- element: " + getElement() + "; propertyName: " + getPropertyName() + "; key: " + m_key);
    }
  }
}

/**
 * @author Dennis Cosgrove
 */
class MatrixUtilities {
  public static double[] getRow(double[] rv, AffineMatrix4x4 m, int row) {
    switch (row) {
    case 0:
      rv[0] = m.orientation.right.x;
      rv[1] = m.orientation.up.x;
      rv[2] = m.orientation.backward.x;
      rv[3] = m.translation.x;
      break;
    case 1:
      rv[0] = m.orientation.right.y;
      rv[1] = m.orientation.up.y;
      rv[2] = m.orientation.backward.y;
      rv[3] = m.translation.y;
      break;
    case 2:
      rv[0] = m.orientation.right.z;
      rv[1] = m.orientation.up.z;
      rv[2] = m.orientation.backward.z;
      rv[3] = m.translation.z;
      break;
    case 3:
      rv[0] = 0.0;
      rv[1] = 0.0;
      rv[2] = 0.0;
      rv[3] = 1.0;
      break;
    default:
      throw new IllegalArgumentException();
    }
    return rv;
  }

  public static void setRow(AffineMatrix4x4 m, int row, double a, double b, double c, double d) {
    switch (row) {
    case 0:
      m.orientation.right.x = a;
      m.orientation.up.x = b;
      m.orientation.backward.x = c;
      m.translation.x = d;
      break;
    case 1:
      m.orientation.right.y = a;
      m.orientation.up.y = b;
      m.orientation.backward.y = c;
      m.translation.y = d;
      break;
    case 2:
      m.orientation.right.z = a;
      m.orientation.up.z = b;
      m.orientation.backward.z = c;
      m.translation.z = d;
      break;
    case 3:
      assert (a == 0.0);
      assert (b == 0.0);
      assert (c == 0.0);
      assert (d == 1.0);
      break;
    default:
      throw new IllegalArgumentException();
    }
  }

  public static void setRow(AffineMatrix4x4 m, int row, double[] abcd) {
    setRow(m, row, abcd[0], abcd[1], abcd[2], abcd[3]);
  }

  public static double[] getRow(double[] rv, Matrix3x3 m, int row) {
    switch (row) {
    case 0:
      rv[0] = m.right.x;
      rv[1] = m.up.x;
      rv[2] = m.backward.x;
      break;
    case 1:
      rv[0] = m.right.y;
      rv[1] = m.up.y;
      rv[2] = m.backward.y;
      break;
    case 2:
      rv[0] = m.right.z;
      rv[1] = m.up.z;
      rv[2] = m.backward.z;
      break;
    default:
      throw new IllegalArgumentException();
    }
    return rv;
  }

  public static void setRow(Matrix3x3 m, int row, double a, double b, double c) {
    switch (row) {
    case 0:
      m.right.x = a;
      m.up.x = b;
      m.backward.x = c;
      break;
    case 1:
      m.right.y = a;
      m.up.y = b;
      m.backward.y = c;
      break;
    case 2:
      m.right.y = a;
      m.up.y = b;
      m.backward.y = c;
      break;
    default:
      throw new IllegalArgumentException();
    }
  }

  public static void setRow(Matrix3x3 m, int row, double[] abc) {
    setRow(m, row, abc[0], abc[1], abc[2]);
  }

}

/**
 * @author Dennis Cosgrove
 */
public class ASG {
  private static final int SMALL_ENOUGH_PRIMATIVE_ARRAY_LENGTH_TO_ENCODE_AS_TEXT = 32;
  private static final String ROOT_FILENAME = "root.xml";
  private static Set<String> s_deadProperties = null;

  private static boolean isDeadProperty(String property) {
    if (s_deadProperties == null) {
      s_deadProperties = new HashSet<String>();
      s_deadProperties.add("IsFirstClass");
      s_deadProperties.add("OpacityMap");
      s_deadProperties.add("EmissiveColorMap");
      s_deadProperties.add("SpecularHighlightColorMap");
      s_deadProperties.add("BumpMap");
      s_deadProperties.add("DetailMap");
      s_deadProperties.add("Format");
      s_deadProperties.add("VertexLowerBound");
      s_deadProperties.add("VertexUpperBound");
    }
    return s_deadProperties.contains(property);
  }

  private static String convertPropertyIfNecessary(String property) {
    if (property.equals("DiffuseColorMap")) {
      property = "DiffuseColorTexture";
    }
    if (property.equals("Indices")) {
      property = "TriangleData";
    }
    return property;
  }

  private static final String OLD_PACKAGE = "edu.cmu.cs.stage3.";

  private static String convertClassnameIfNecessary(String className) {
    // System.err.print( className + " " );
    if (className.equals("edu.cmu.cs.stage3.alice.scenegraph.Color")) {
      className = "edu.cmu.cs.dennisc.color.Color4f";
    }
    if (className.startsWith(OLD_PACKAGE)) {
      //todo
      className = "edu.cmu.cs.dennisc." + className.substring(OLD_PACKAGE.length());
    }
    if (className.startsWith("[L" + OLD_PACKAGE)) {
      className = "[Lorg." + className.substring(2 + OLD_PACKAGE.length());
    }
    if (className.endsWith("Vertex3d;")) {
      className = className.substring(0, className.length() - 3) + ';';
    }
    if (className.endsWith("Vertex3d")) {
      className = className.substring(0, className.length() - 2);
    }
    if (className.endsWith("TextureMap")) {
      className = className.substring(0, className.length() - 3);
    }
    return className;
  }

  public static void encodeVertexArrayInBinary(Vertex[] vertices, OutputStream os) {
    BufferedOutputStream bos = new BufferedOutputStream(os);
    DataOutputStream dos = new DataOutputStream(bos);
    try {
      dos.writeInt(3);
      dos.writeInt(vertices.length);
      for (Vertex vertice : vertices) {
        int format = vertice.getFormat();
        dos.writeInt(format);
        if ((format & Vertex.FORMAT_POSITION) != 0) {
          dos.writeDouble(vertice.position.x);
          dos.writeDouble(vertice.position.y);
          dos.writeDouble(vertice.position.z);
        }
        if ((format & Vertex.FORMAT_NORMAL) != 0) {
          dos.writeDouble(vertice.normal.x);
          dos.writeDouble(vertice.normal.y);
          dos.writeDouble(vertice.normal.z);
        }
        if ((format & Vertex.FORMAT_DIFFUSE_COLOR) != 0) {
          dos.writeFloat(vertice.diffuseColor.red);
          dos.writeFloat(vertice.diffuseColor.green);
          dos.writeFloat(vertice.diffuseColor.blue);
          dos.writeFloat(vertice.diffuseColor.alpha);
        }
        if ((format & Vertex.FORMAT_SPECULAR_HIGHLIGHT_COLOR) != 0) {
          dos.writeFloat(vertice.specularHighlightColor.red);
          dos.writeFloat(vertice.specularHighlightColor.green);
          dos.writeFloat(vertice.specularHighlightColor.blue);
          dos.writeFloat(vertice.specularHighlightColor.alpha);
        }
        if ((format & Vertex.FORMAT_TEXTURE_COORDINATE_0) != 0) {
          dos.writeFloat(vertice.textureCoordinate0.u);
          dos.writeFloat(vertice.textureCoordinate0.v);
        }
      }
      dos.flush();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static Vertex[] decodeVertexArrayInBinary(InputStream is) {
    Vertex[] vertices = null;
    BufferedInputStream bis = new BufferedInputStream(is);
    DataInputStream dis = new DataInputStream(bis);
    try {
      int version = dis.readInt();
      if (version == 1) {
        int vertexCount = dis.readInt();
        vertices = new Vertex[vertexCount];
        for (int index = 0; index < vertices.length; index++) {
          double x = dis.readDouble();
          double y = dis.readDouble();
          double z = dis.readDouble();
          float i = (float) dis.readDouble();
          float j = (float) dis.readDouble();
          float k = (float) dis.readDouble();
          float u = (float) dis.readDouble();
          float v = (float) dis.readDouble();
          vertices[index] = Vertex.createXYZIJKUV(x, y, z, i, j, k, u, v);
        }
      } else if (version == 2) {
        int vertexCount = dis.readInt();
        vertices = new Vertex[vertexCount];
        for (int index = 0; index < vertices.length; index++) {
          int format = dis.readInt();
          final Point3 position = Point3.createNaN();
          if ((format & Vertex.FORMAT_POSITION) != 0) {
            position.x = dis.readDouble();
            position.y = dis.readDouble();
            position.z = dis.readDouble();
          }
          final Vector3f normal = Vector3f.createNaN();
          if ((format & Vertex.FORMAT_NORMAL) != 0) {
            normal.x = (float) dis.readDouble();
            normal.y = (float) dis.readDouble();
            normal.z = (float) dis.readDouble();
          }
          final Color4f diffuseColor;
          if ((format & Vertex.FORMAT_DIFFUSE_COLOR) != 0) {
            float red = (float) dis.readDouble();
            float green = (float) dis.readDouble();
            float blue = (float) dis.readDouble();
            float alpha = (float) dis.readDouble();
            diffuseColor = new Color4f(red, green, blue, alpha);
          } else {
            diffuseColor = null;
          }
          if ((format & Vertex.FORMAT_SPECULAR_HIGHLIGHT_COLOR) != 0) {
          }
          final TextureCoordinate2f textureCoordinate0;
          if ((format & Vertex.FORMAT_TEXTURE_COORDINATE_0) != 0) {
            float u = (float) dis.readDouble();
            float v = (float) dis.readDouble();
            textureCoordinate0 = new TextureCoordinate2f(u, v);
          } else {
            textureCoordinate0 = null;
          }
          vertices[index] = new Vertex(position, normal, diffuseColor, null, textureCoordinate0);
        }
      } else if (version == 3) {
        int vertexCount = dis.readInt();
        vertices = new Vertex[vertexCount];
        for (int index = 0; index < vertices.length; index++) {
          int format = dis.readInt();
          final Point3 position = Point3.createNaN();
          if ((format & Vertex.FORMAT_POSITION) != 0) {
            position.x = dis.readDouble();
            position.y = dis.readDouble();
            position.z = dis.readDouble();
          }
          final Vector3f normal = Vector3f.createNaN();
          if ((format & Vertex.FORMAT_NORMAL) != 0) {
            normal.x = (float) dis.readDouble();
            normal.y = (float) dis.readDouble();
            normal.z = (float) dis.readDouble();
          }
          final Color4f diffuseColor;
          if ((format & Vertex.FORMAT_DIFFUSE_COLOR) != 0) {
            float red = dis.readFloat();
            float green = dis.readFloat();
            float blue = dis.readFloat();
            float alpha = dis.readFloat();
            diffuseColor = new Color4f(red, green, blue, alpha);
          } else {
            diffuseColor = null;
          }
          final Color4f specularHighlightColor;
          if ((format & Vertex.FORMAT_SPECULAR_HIGHLIGHT_COLOR) != 0) {
            float red = dis.readFloat();
            float green = dis.readFloat();
            float blue = dis.readFloat();
            float alpha = dis.readFloat();
            specularHighlightColor = new Color4f(red, green, blue, alpha);
          } else {
            specularHighlightColor = null;
          }
          final TextureCoordinate2f textureCoordinate0;
          if ((format & Vertex.FORMAT_TEXTURE_COORDINATE_0) != 0) {
            float u = dis.readFloat();
            float v = dis.readFloat();
            textureCoordinate0 = new TextureCoordinate2f(u, v);
          } else {
            textureCoordinate0 = null;
          }
          vertices[index] = new Vertex(position, normal, diffuseColor, specularHighlightColor, textureCoordinate0);
        }
      } else {
        throw new RuntimeException("invalid file version: " + version);
      }
      return vertices;
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static void encodeIntArrayInBinary(int[] array, OutputStream os) {
    BufferedOutputStream bos = new BufferedOutputStream(os);
    DataOutputStream dos = new DataOutputStream(bos);
    try {
      dos.writeInt(2);
      dos.writeInt(array.length);
      for (int element : array) {
        dos.writeInt(element);
      }
      dos.flush();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static int[] decodeIntArrayInBinary(InputStream is) {
    int[] array = null;
    BufferedInputStream bis = new BufferedInputStream(is);
    DataInputStream dis = new DataInputStream(bis);
    try {
      int version = dis.readInt();
      if (version == 1) {
        int faceCount = dis.readInt();
        /* unused int verticesPerFace = */
        dis.readInt();
        array = new int[faceCount * 3];
        for (int i = 0; i < array.length; i++) {
          array[i] = dis.readInt();
        }
      } else if (version == 2) {
        int count = dis.readInt();
        array = new int[count];
        for (int i = 0; i < array.length; i++) {
          array[i] = dis.readInt();
        }
        for (int i = 0; i < array.length; i += 3) {
          int temp = array[i];
          array[i] = array[i + 2];
          array[i + 2] = temp;
        }
      } else {
        throw new RuntimeException("invalid file version: " + version);
      }
      return array;
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static void encodeDoubleArrayInBinary(double[] array, OutputStream os) {
    BufferedOutputStream bos = new BufferedOutputStream(os);
    DataOutputStream dos = new DataOutputStream(bos);
    try {
      dos.writeInt(2);
      dos.writeInt(array.length);
      for (double element : array) {
        dos.writeDouble(element);
      }
      dos.flush();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static double[] decodeDoubleArrayInBinary(InputStream is) {
    double[] array = null;
    BufferedInputStream bis = new BufferedInputStream(is);
    DataInputStream dis = new DataInputStream(bis);
    try {
      int version = dis.readInt();
      if (version == 1) {
        // there was no version 1
      } else if (version == 2) {
        int count = dis.readInt();
        array = new double[count];
        for (int i = 0; i < array.length; i++) {
          array[i] = dis.readDouble();
        }
      } else {
        throw new RuntimeException("invalid file version: " + version);
      }
      return array;
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private static String encodeIntArray(int[] array, int offset, int length, boolean isHexadecimal) {
    StringBuilder buffer = new StringBuilder();
    int index = offset;
    for (int lcv = 0; lcv < length; lcv++) {
      String s;
      int value = array[index++];
      if (isHexadecimal) {
        s = Integer.toHexString(value).toUpperCase();
      } else {
        s = Integer.toString(value);
      }
      buffer.append(s);
      if (lcv < (length - 1)) {
        buffer.append(' ');
      }
    }
    return buffer.toString();
  }

  private static void decodeIntArray(String s, int[] array, int offset, int length, boolean isHexadecimal) {
    int index = offset;
    int begin = 0;
    for (int lcv = 0; lcv < length; lcv++) {
      int end = s.indexOf(' ', begin);
      if (end == -1) {
        end = s.length();
      }
      String substr = s.substring(begin, end);
      int value;
      if (isHexadecimal) {
        value = (int) Long.parseLong(substr, 16);
      } else {
        value = Integer.parseInt(substr);
      }
      array[index++] = value;
      begin = end + 1;
    }
  }

  private static String encodeIntArray(int[] array, boolean isHexadecimal) {
    return encodeIntArray(array, 0, array.length, isHexadecimal);
  }

  private static void decodeIntArray(String s, int[] array, boolean isHexadecimal) {
    decodeIntArray(s, array, 0, array.length, isHexadecimal);
  }

  private static String encodeDoubleArray(double[] array, int offset, int length) {
    StringBuilder buffer = new StringBuilder();
    int index = offset;
    for (int lcv = 0; lcv < length; lcv++) {
      buffer.append(array[index++]);
      if (lcv < (length - 1)) {
        buffer.append(' ');
      }
    }
    return buffer.toString();
  }

  private static void decodeDoubleArray(String s, double[] array, int offset, int length) {
    int index = offset;
    int begin = 0;
    for (int lcv = 0; lcv < length; lcv++) {
      int end = s.indexOf(' ', begin);
      if (end == -1) {
        end = s.length();
      }
      String substr = s.substring(begin, end);
      array[index++] = Double.parseDouble(substr);
      begin = end + 1;
    }
  }

  private static String encodeDoubleArray(double[] array) {
    return encodeDoubleArray(array, 0, array.length);
  }

  private static void decodeDoubleArray(String s, double[] array) {
    decodeDoubleArray(s, array, 0, array.length);
  }

  private static String encodeTuple3d(Tuple3 tuple3d) {
    return Double.toString(tuple3d.x) + ' ' + tuple3d.y + ' ' + tuple3d.z;
  }

  private static void decodeTuple3d(String s, Tuple3 tuple3d) {
    int begin = 0;
    int end = s.indexOf(' ', begin);
    tuple3d.x = Double.parseDouble(s.substring(begin, end));
    begin = end + 1;
    end = s.indexOf(' ', begin);
    tuple3d.y = Double.parseDouble(s.substring(begin, end));
    begin = end + 1;
    end = s.length();
    tuple3d.z = Double.parseDouble(s.substring(begin, end));
  }

  private static String encodeTuple3f(Tuple3f tuple3d) {
    return Double.toString(tuple3d.x) + ' ' + Double.toString(tuple3d.y) + ' ' + Double.toString(tuple3d.z);
  }

  private static void decodeTuple3f(String s, Tuple3f tuple3d) {
    int begin = 0;
    int end = s.indexOf(' ', begin);
    tuple3d.x = Float.parseFloat(s.substring(begin, end));
    begin = end + 1;
    end = s.indexOf(' ', begin);
    tuple3d.y = Float.parseFloat(s.substring(begin, end));
    begin = end + 1;
    end = s.length();
    tuple3d.z = Float.parseFloat(s.substring(begin, end));
  }

  private static void decodeTuple2f(String s, Tuple2f tuple2f) {
    int begin = 0;
    int end = s.indexOf(' ', begin);
    tuple2f.x = Float.parseFloat(s.substring(begin, end));
    begin = end + 1;
    end = s.length();
    tuple2f.y = Float.parseFloat(s.substring(begin, end));
  }

  private static String encodeTexCoord2f(TextureCoordinate2f tc2f) {
    return String.valueOf(tc2f.u) + ' ' + tc2f.v;
  }

  private static TextureCoordinate2f decodeTexCoord2f(String s) {
    int begin = 0;
    int end = s.indexOf(' ', begin);
    float u = Float.parseFloat(s.substring(begin, end));
    begin = end + 1;
    end = s.length();
    float v = Float.parseFloat(s.substring(begin, end));
    return new TextureCoordinate2f(u, v);
  }

  private static String encodeColor4f(Color4f color4f) {
    return String.valueOf(color4f.red) + ' ' + color4f.green + ' ' + color4f.blue + ' ' + color4f.alpha;
  }

  private static Color4f decodeColor4f(String s) {
    int begin = 0;
    int end = s.indexOf(' ', begin);
    float red = Float.parseFloat(s.substring(begin, end));
    begin = end + 1;
    end = s.indexOf(' ', begin);
    float green = Float.parseFloat(s.substring(begin, end));
    begin = end + 1;
    end = s.indexOf(' ', begin);
    float blue = Float.parseFloat(s.substring(begin, end));
    begin = end + 1;
    end = s.length();
    float alpha = Float.parseFloat(s.substring(begin, end));
    return new Color4f(red, green, blue, alpha);
  }

  public static final double VERSION = 1.0;

  private static String getKey(edu.cmu.cs.dennisc.scenegraph.Element element) {
    return Integer.toString(element.hashCode());
  }

  private static Element encodeElement(edu.cmu.cs.dennisc.scenegraph.Element element, Document document, String s, HashMap<String, ByteArrayOutputStream> filenameToStreamMap, HashMap<String, edu.cmu.cs.dennisc.scenegraph.Element> keyToElementToBeEncodedMap, boolean isTextAlwaysDesired) {
    Element xmlElement = document.createElement(s);
    Class<? extends edu.cmu.cs.dennisc.scenegraph.Element> elementClass = element.getClass();
    xmlElement.setAttribute("class", elementClass.getName());
    xmlElement.setAttribute("key", getKey(element));
    for (InstanceProperty<?> property : element.getProperties()) {
      String propertyName = property.getName();
      if (propertyName.equals("Parent")) {
        // pass
      } else if (propertyName.equals("Bonus")) {
        // pass
      } else {
        Element xmlProperty = document.createElement("property");
        xmlProperty.setAttribute("name", propertyName);
        Object value = property.getValue();
        if (value != null) {
          Class<?> propertyValueClass = value.getClass();
          if (edu.cmu.cs.dennisc.scenegraph.Element.class.isAssignableFrom(propertyValueClass)) {
            String key = Integer.toString(value.hashCode());
            xmlProperty.setAttribute("key", key);
            if (Component.class.isAssignableFrom(propertyValueClass)) {
            } else {
              keyToElementToBeEncodedMap.put(key, (edu.cmu.cs.dennisc.scenegraph.Element) value);
            }
          } else {
            if (AffineMatrix4x4.class.isAssignableFrom(propertyValueClass)) {
              xmlProperty.setAttribute("class", "edu.cmu.cs.dennisc.math.Matrix4d");
              AffineMatrix4x4 m = (AffineMatrix4x4) value;
              double[] row = new double[4];
              for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
                Element xmlRow = document.createElement("row");
                MatrixUtilities.getRow(row, m, rowIndex);
                xmlRow.appendChild(document.createTextNode(encodeDoubleArray(row)));
                xmlProperty.appendChild(xmlRow);
              }
            } else if (Matrix3x3.class.isAssignableFrom(propertyValueClass)) {
              xmlProperty.setAttribute("class", "edu.cmu.cs.dennisc.math.Matrix3d");
              Matrix3x3 m = (Matrix3x3) value;
              double[] row = new double[3];
              for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
                Element xmlRow = document.createElement("row");
                MatrixUtilities.getRow(row, m, rowIndex);
                xmlRow.appendChild(document.createTextNode(encodeDoubleArray(row)));
                xmlProperty.appendChild(xmlRow);
              }
            } else if (Image.class.isAssignableFrom(propertyValueClass)) {
              Image image = (Image) value;
              xmlProperty.setAttribute("class", "java.awt.Image");
              if (isTextAlwaysDesired) {
                int width = ImageUtilities.getWidth(image);
                int height = ImageUtilities.getHeight(image);
                int[] pixels = ImageUtilities.getPixels(image, width, height);
                xmlProperty.setAttribute("width", Integer.toString(width));
                xmlProperty.setAttribute("height", Integer.toString(width));
                int pixelIndex = 0;
                for (int rowIndex = 0; rowIndex < height; rowIndex++) {
                  Element xmlRow = document.createElement("row");
                  // for( int colIndex=0; colIndex<width;
                  // colIndex++ ) {
                  // int pixel = pixels[ pixelIndex++ ];
                  // //int alpha = (pixel >> 24) & 0xff;
                  // //int red = (pixel >> 16) & 0xff;
                  // //int green = (pixel >> 8) & 0xff;
                  // //int blue = (pixel ) & 0xff;
                  //
                  // org.w3c.dom.Element itemNode =
                  // document.createElement( "item" );
                  // itemNode.appendChild(
                  // document.createTextNode(
                  // Integer.toHexString( pixel
                  // ).toUpperCase() ) );
                  // rowNode.appendChild( itemNode );
                  // }
                  xmlRow.appendChild(document.createTextNode(encodeIntArray(pixels, pixelIndex, width, true)));
                  pixelIndex += width;
                  xmlProperty.appendChild(xmlRow);
                }
              } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                  ImageUtilities.write(ImageUtilities.PNG_CODEC_NAME, baos, image);
                  String filename = image.hashCode() + ".png";
                  xmlProperty.setAttribute("filename", filename);
                  filenameToStreamMap.put(filename, baos);
                } catch (IOException ioe) {
                  throw new RuntimeException(ioe);
                }
              }
            } else if (Color4f.class.isAssignableFrom(propertyValueClass)) {
              xmlProperty.setAttribute("class", "edu.cmu.cs.dennisc.color.Color4f");
              Color4f color = (Color4f) value;
              Element xmlRed = document.createElement("red");
              xmlRed.appendChild(document.createTextNode(Float.toString(color.red)));
              xmlProperty.appendChild(xmlRed);
              Element xmlGreen = document.createElement("green");
              xmlGreen.appendChild(document.createTextNode(Float.toString(color.green)));
              xmlProperty.appendChild(xmlGreen);
              Element xmlBlue = document.createElement("blue");
              xmlBlue.appendChild(document.createTextNode(Float.toString(color.blue)));
              xmlProperty.appendChild(xmlBlue);
              Element xmlAlpha = document.createElement("alpha");
              xmlAlpha.appendChild(document.createTextNode(Float.toString(color.alpha)));
              xmlProperty.appendChild(xmlAlpha);
            } else if (int[].class.isAssignableFrom(propertyValueClass)) {
              int[] array = (int[]) value;
              xmlProperty.setAttribute("class", "[I");
              if (isTextAlwaysDesired || (array.length < SMALL_ENOUGH_PRIMATIVE_ARRAY_LENGTH_TO_ENCODE_AS_TEXT)) {
                xmlProperty.setAttribute("length", Integer.toString(array.length));
                xmlProperty.appendChild(document.createTextNode(encodeIntArray(array, false)));
              } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                encodeIntArrayInBinary(array, baos);
                String filename = "int array " + array.hashCode() + ".bin";
                xmlProperty.setAttribute("filename", filename);
                filenameToStreamMap.put(filename, baos);
              }
            } else if (double[].class.isAssignableFrom(propertyValueClass)) {
              double[] array = (double[]) value;
              xmlProperty.setAttribute("class", "[D");
              if (isTextAlwaysDesired || (array.length < SMALL_ENOUGH_PRIMATIVE_ARRAY_LENGTH_TO_ENCODE_AS_TEXT)) {
                xmlProperty.setAttribute("length", Integer.toString(array.length));
                xmlProperty.appendChild(document.createTextNode(encodeDoubleArray(array)));
              } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                encodeDoubleArrayInBinary(array, baos);
                String filename = "double array " + array.hashCode() + ".bin";
                xmlProperty.setAttribute("filename", filename);
                filenameToStreamMap.put(filename, baos);
              }
            } else if (Vertex[].class.isAssignableFrom(propertyValueClass)) {
              Vertex[] array = (Vertex[]) value;
              xmlProperty.setAttribute("class", "[Ledu.cmu.cs.dennisc.scenegraph.Vertex;");
              if (isTextAlwaysDesired || (array.length == 0)) {
                for (Vertex vertex : array) {
                  Element xmlVertex = document.createElement("vertex");
                  if (vertex.position != null) {
                    Element xmlPosition = document.createElement("position");
                    xmlPosition.appendChild(document.createTextNode(encodeTuple3d(vertex.position)));
                    xmlVertex.appendChild(xmlPosition);
                  }
                  if (vertex.normal != null) {
                    Element xmlNormal = document.createElement("normal");
                    xmlNormal.appendChild(document.createTextNode(encodeTuple3f(vertex.normal)));
                    xmlVertex.appendChild(xmlNormal);
                  }
                  if (vertex.diffuseColor != null) {
                    Element xmlDiffuseColor = document.createElement("diffuseColor");
                    xmlDiffuseColor.appendChild(document.createTextNode(encodeColor4f(vertex.diffuseColor)));
                    xmlVertex.appendChild(xmlDiffuseColor);
                  }
                  if (vertex.textureCoordinate0 != null) {
                    Element xmlTextureCoordinate0 = document.createElement("textureCoordinate0");
                    xmlTextureCoordinate0.appendChild(document.createTextNode(encodeTexCoord2f(vertex.textureCoordinate0)));
                    xmlVertex.appendChild(xmlTextureCoordinate0);
                  }
                  xmlProperty.appendChild(xmlVertex);
                }
              } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                encodeVertexArrayInBinary(array, baos);
                String filename = "Vertex array " + array.hashCode() + ".bin";
                xmlProperty.setAttribute("filename", filename);
                filenameToStreamMap.put(filename, baos);
              }
            } else {
              xmlProperty.setAttribute("class", propertyValueClass.getName());
              xmlProperty.appendChild(document.createTextNode(value.toString()));
            }
          }
        }
        xmlElement.appendChild(xmlProperty);
      }
    }
    return xmlElement;
  }

  private static Element encodeComponent(Component component, Document document, String s, HashMap<String, ByteArrayOutputStream> filenameToStreamMap, HashMap<String, edu.cmu.cs.dennisc.scenegraph.Element> keyToElementToBeEncodedMap, boolean isTextAlwaysDesired) {
    Element xmlComponent = encodeElement(component, document, s, filenameToStreamMap, keyToElementToBeEncodedMap, isTextAlwaysDesired);
    if (component instanceof Composite) {
      Composite sgComposite = (Composite) component;
      for (Component sgComponent : sgComposite.getComponents()) {
        xmlComponent.appendChild(encodeComponent(sgComponent, document, "child", filenameToStreamMap, keyToElementToBeEncodedMap, isTextAlwaysDesired));
      }
    }
    return xmlComponent;
  }

  private static void encodeInternal(Component component, OutputStream os, HashMap<String, ByteArrayOutputStream> filenameToStreamMap, boolean isTextAlwaysDesired) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.newDocument();
      HashMap<String, edu.cmu.cs.dennisc.scenegraph.Element> keyToElementToBeEncodedMap = new HashMap<String, edu.cmu.cs.dennisc.scenegraph.Element>();
      Element rootNode = encodeComponent(component, document, "root", filenameToStreamMap, keyToElementToBeEncodedMap, isTextAlwaysDesired);
      rootNode.setAttribute("version", Double.toString(VERSION));
      while (keyToElementToBeEncodedMap.size() > 0) {
        HashMap<String, edu.cmu.cs.dennisc.scenegraph.Element> tempCopy = new HashMap<String, edu.cmu.cs.dennisc.scenegraph.Element>(keyToElementToBeEncodedMap);
        for (String key : tempCopy.keySet()) {
          edu.cmu.cs.dennisc.scenegraph.Element element = tempCopy.get(key);
          rootNode.appendChild(encodeElement(element, document, "element", filenameToStreamMap, keyToElementToBeEncodedMap, isTextAlwaysDesired));
          keyToElementToBeEncodedMap.remove(key);
        }
      }
      document.appendChild(rootNode);
      document.getDocumentElement().normalize();
      try {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(document), new StreamResult(os));
      } catch (TransformerException te) {
        throw new RuntimeException(te);
      }
    } catch (ParserConfigurationException pce) {
      throw new RuntimeException(pce);
    }
  }

  public static void encode(Component component, OutputStream os) {
    HashMap<String, ByteArrayOutputStream> filenameToStreamMap = new HashMap<String, ByteArrayOutputStream>();
    ByteArrayOutputStream rootBAOS = new ByteArrayOutputStream();
    encodeInternal(component, rootBAOS, filenameToStreamMap, false);
    filenameToStreamMap.put(ROOT_FILENAME, rootBAOS);
    ZipOutputStream zos;
    if (os instanceof ZipOutputStream) {
      zos = (ZipOutputStream) os;
    } else {
      zos = new ZipOutputStream(os);
    }
    CRC32 crc32 = new CRC32();
    try {
      for (String filename : filenameToStreamMap.keySet()) {
        ByteArrayOutputStream baos = filenameToStreamMap.get(filename);
        baos.flush();
        byte[] ba = baos.toByteArray();
        ZipEntry zipEntry = new ZipEntry(filename);
        int method;
        if (filename.endsWith(".png")) {
          crc32.reset();
          crc32.update(ba);
          zipEntry.setCrc(crc32.getValue());
          zipEntry.setSize(ba.length);
          method = ZipOutputStream.STORED;
        } else {
          method = ZipOutputStream.DEFLATED;
        }
        zos.setMethod(method);
        zos.putNextEntry(zipEntry);
        zos.write(ba, 0, ba.length);
        zos.closeEntry();
      }
      zos.flush();
      zos.finish();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static void encode(Component component, File file) {
    try {
      OutputStream os = new FileOutputStream(file);
      encode(component, os);
      os.close();
    } catch (FileNotFoundException fnfe) {
      throw new RuntimeException(fnfe);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static void encode(Component component, String path) {
    encode(component, new File(path));
  }

  private static Element getFirstChild(Node node, String tag) {
    Node childNode = node.getFirstChild();
    while (childNode != null) {
      if (childNode instanceof Element) {
        if (childNode.getNodeName().equals(tag)) {
          return (Element) childNode;
        }
      }
      childNode = childNode.getNextSibling();
    }
    return null;
  }

  private static Element[] getChildren(Node node, String tag) {
    Vector<Element> vector = new Vector<Element>();
    Node childNode = node.getFirstChild();
    while (childNode != null) {
      if (childNode instanceof Element) {
        if (childNode.getNodeName().equals(tag)) {
          vector.addElement((Element) childNode);
        }
      }
      childNode = childNode.getNextSibling();
    }
    Element[] array = new Element[vector.size()];
    vector.copyInto(array);
    return array;
  }

  private static String getNodeText(Node node) {
    StringBuilder propertyTextBuffer = new StringBuilder();
    NodeList children = node.getChildNodes();
    for (int j = 0; j < children.getLength(); j++) {
      Text textNode = (Text) children.item(j);
      propertyTextBuffer.append(textNode.getData().trim());
    }
    return propertyTextBuffer.toString();
  }

  private static Object valueOf(Class<?> cls, String text) {
    if (String.class.isAssignableFrom(cls)) {
      return text;
    } else if (cls.equals(Double.class) && text.equals("Infinity")) {
      return new Double(Double.POSITIVE_INFINITY);
    } else if (cls.equals(Double.class) && text.equals("NaN")) {
      return new Double(Double.NaN);
    } else {
      Class<?>[] parameterTypes = {String.class};
      try {
        Method valueOfMethod = cls.getMethod("valueOf", parameterTypes);
        int modifiers = valueOfMethod.getModifiers();
        if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
          Object[] parameters = {text};
          return valueOfMethod.invoke(null, parameters);
        } else {
          throw new RuntimeException("valueOf method not public static.");
        }
      } catch (NoSuchMethodException nsme) {
        throw new RuntimeException("NoSuchMethodException: class[" + cls.getName() + "]; method[" + text + "]");
      } catch (IllegalAccessException iae) {
        throw new RuntimeException("IllegalAccessException: " + cls + " " + text);
      } catch (InvocationTargetException ite) {
        throw new RuntimeException("java.lang.reflect.InvocationTargetException: " + cls + " " + text);
      }
    }
  }

  private static edu.cmu.cs.dennisc.scenegraph.Element decodeElement(Element xmlElement, HashMap<String, InputStream> filenameToStreamMap, HashMap<Integer, edu.cmu.cs.dennisc.scenegraph.Element> keyToElementMap, Vector<AbstractPropertyReference> referencesToBeResolved) {
    String className = xmlElement.getAttribute("class");
    Integer elementKey = Integer.parseInt(xmlElement.getAttribute("key"));
    String elementName = xmlElement.getAttribute("name");
    className = convertClassnameIfNecessary(className);
    edu.cmu.cs.dennisc.scenegraph.Element sgElement = (edu.cmu.cs.dennisc.scenegraph.Element) ReflectionUtilities.newInstance(className);
    sgElement.setName(elementName);
    keyToElementMap.put(elementKey, sgElement);
    Element[] xmlProperties = getChildren(xmlElement, "property");
    for (Element xmlProperty : xmlProperties) {
      String propertyName = xmlProperty.getAttribute("name");
      propertyName = convertPropertyIfNecessary(propertyName);
      if (isDeadProperty(propertyName)) {
        continue;
      }
      InstanceProperty property = sgElement.getPropertyNamed(propertyName);
      if (xmlProperty.hasAttribute("class")) {
        String propertyValueClassname = xmlProperty.getAttribute("class");
        propertyValueClassname = convertClassnameIfNecessary(propertyValueClassname);
        Class<?> propertyValueClass = ReflectionUtilities.getClassForName(propertyValueClassname);
        Object value;
        if (xmlProperty.hasAttribute("filename")) {
          String filename = xmlProperty.getAttribute("filename");
          InputStream is = filenameToStreamMap.get(filename);
          if (is != null) {
            if (Image.class.isAssignableFrom(propertyValueClass)) {
              String ext = FileUtilities.getExtension(filename);
              String codecName = ImageUtilities.getCodecNameForExtension(ext);
              try {
                value = ImageUtilities.read(codecName, is);
              } catch (IOException ioe) {
                throw new RuntimeException(filename, ioe);
              }
            } else if (Vertex[].class.isAssignableFrom(propertyValueClass)) {
              value = decodeVertexArrayInBinary(is);
            } else if (int[].class.isAssignableFrom(propertyValueClass)) {
              value = decodeIntArrayInBinary(is);
            } else if (double[].class.isAssignableFrom(propertyValueClass)) {
              value = decodeDoubleArrayInBinary(is);
            } else {
              throw new RuntimeException();
            }
          } else {
            throw new RuntimeException();
          }
        } else {
          if (AffineMatrix4x4.class.isAssignableFrom(propertyValueClass)) {
            AffineMatrix4x4 m = AffineMatrix4x4.createNaN();
            Element[] xmlRows = getChildren(xmlProperty, "row");
            double[] row = new double[4];
            for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
              decodeDoubleArray(getNodeText(xmlRows[rowIndex]), row);
              MatrixUtilities.setRow(m, rowIndex, row);
            }
            value = m;
          } else if (Matrix3x3.class.isAssignableFrom(propertyValueClass)) {
            Matrix3x3 m = Matrix3x3.createNaN();
            Element[] xmlRows = getChildren(xmlProperty, "row");
            double[] row = new double[3];
            for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
              decodeDoubleArray(getNodeText(xmlRows[rowIndex]), row);
              MatrixUtilities.setRow(m, rowIndex, row);
            }
            value = m;
          } else if (Image.class.isAssignableFrom(propertyValueClass)) {
            int width = Integer.parseInt(xmlProperty.getAttribute("width"));
            int height = Integer.parseInt(xmlProperty.getAttribute("height"));
            Element[] xmlRows = getChildren(xmlProperty, "row");
            int[] pixels = new int[width * height];
            int pixelIndex = 0;
            for (int rowIndex = 0; rowIndex < height; rowIndex++) {
              String s = getNodeText(xmlRows[rowIndex]);
              decodeIntArray(s, pixels, pixelIndex, width, true);
              pixelIndex += width;
            }
            value = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, pixels, 0, width));
          } else if (Color4f.class.isAssignableFrom(propertyValueClass)) {
            float red = Float.parseFloat(getNodeText(getFirstChild(xmlProperty, "red")));
            float green = Float.parseFloat(getNodeText(getFirstChild(xmlProperty, "green")));
            float blue = Float.parseFloat(getNodeText(getFirstChild(xmlProperty, "blue")));
            float alpha = Float.parseFloat(getNodeText(getFirstChild(xmlProperty, "alpha")));
            value = new Color4f(red, green, blue, alpha);
          } else if (int[].class.isAssignableFrom(propertyValueClass)) {
            int length = Integer.parseInt(xmlProperty.getAttribute("length"));
            int[] array = new int[length];
            decodeIntArray(getNodeText(xmlProperty), array, false);
            value = array;
          } else if (double[].class.isAssignableFrom(propertyValueClass)) {
            int length = Integer.parseInt(xmlProperty.getAttribute("length"));
            double[] array = new double[length];
            decodeDoubleArray(getNodeText(xmlProperty), array);
            value = array;
          } else if (Point3[].class.isAssignableFrom(propertyValueClass)) {
            Element[] xmlPoints = getChildren(xmlProperty, "point");
            Point3[] array = new Point3[xmlPoints.length];
            for (int tupleIndex = 0; tupleIndex < xmlPoints.length; tupleIndex++) {
              Point3 point = new Point3();
              decodeTuple3d(getNodeText(xmlPoints[tupleIndex]), point);
              array[tupleIndex] = point;
            }
            value = array;
          } else if (Vector3f[].class.isAssignableFrom(propertyValueClass)) {
            Element[] xmlNormals = getChildren(xmlProperty, "normal");
            Vector3f[] array = new Vector3f[xmlNormals.length];
            for (int tupleIndex = 0; tupleIndex < xmlNormals.length; tupleIndex++) {
              Vector3f v = new Vector3f();
              decodeTuple3f(getNodeText(xmlNormals[tupleIndex]), v);
              array[tupleIndex] = v;
            }
            value = array;
          } else if (Vector2f[].class.isAssignableFrom(propertyValueClass)) {
            Element[] xmlTextureCoords = getChildren(xmlProperty, "textureCoordinate");
            Vector2f[] array = new Vector2f[xmlTextureCoords.length];
            for (int tupleIndex = 0; tupleIndex < xmlTextureCoords.length; tupleIndex++) {
              Vector2f v = new Vector2f();
              decodeTuple2f(getNodeText(xmlTextureCoords[tupleIndex]), v);
              array[tupleIndex] = v;
            }
            value = array;
          } else if (Vertex[].class.isAssignableFrom(propertyValueClass)) {
            Element[] xmlVertices = getChildren(xmlProperty, "vertex");
            Vertex[] array = new Vertex[xmlVertices.length];
            for (int vertexIndex = 0; vertexIndex < xmlVertices.length; vertexIndex++) {
              Element xmlVertex = xmlVertices[vertexIndex];
              Element xmlPosition = getFirstChild(xmlVertex, "position");
              Point3 position = Point3.createNaN();
              if (xmlPosition != null) {
                decodeTuple3d(getNodeText(xmlPosition), position);
              }
              Element xmlNormal = getFirstChild(xmlVertex, "normal");
              Vector3f normal = Vector3f.createNaN();
              if (xmlNormal != null) {
                decodeTuple3f(getNodeText(xmlNormal), normal);
              }
              Element xmlDiffuseColor = getFirstChild(xmlVertex, "diffuseColor");
              final Color4f diffuseColor;
              if (xmlDiffuseColor != null) {
                diffuseColor = decodeColor4f(getNodeText(xmlDiffuseColor));
              } else {
                diffuseColor = null;
              }
              Element xmlTextureCoordinate0 = getFirstChild(xmlVertex, "textureCoordinate0");
              final TextureCoordinate2f textureCoordinate0;
              if (xmlTextureCoordinate0 != null) {
                textureCoordinate0 = decodeTexCoord2f(getNodeText(xmlTextureCoordinate0));
              } else {
                textureCoordinate0 = null;
              }
              Vertex vertex = new Vertex(position, normal, diffuseColor, null, textureCoordinate0);
              array[vertexIndex] = vertex;
            }
            value = array;
          } else {
            value = valueOf(propertyValueClass, getNodeText(xmlProperty));
          }
        }
        property.setValue(value);
      } else if (xmlProperty.hasAttribute("key")) {
        Integer key = Integer.parseInt(xmlProperty.getAttribute("key"));
        referencesToBeResolved.addElement(new PropertyReferenceToElement(sgElement, property, key));
      } else {
        property.setValue(null);
      }
    }
    return sgElement;
  }

  private static Component decodeComponent(Element xmlComponent, HashMap<String, InputStream> filenameToStreamMap, HashMap<Integer, edu.cmu.cs.dennisc.scenegraph.Element> keyToElementMap, Vector<AbstractPropertyReference> referencesToBeResolved) {
    Component sgComponent = (Component) decodeElement(xmlComponent, filenameToStreamMap, keyToElementMap, referencesToBeResolved);
    Element[] xmlChildren = getChildren(xmlComponent, "child");
    for (Element element : xmlChildren) {
      decodeComponent(element, filenameToStreamMap, keyToElementMap, referencesToBeResolved).setParent((Composite) sgComponent);
    }
    return sgComponent;
  }

  private static Component decodeInternal(InputStream is, HashMap<String, InputStream> filenameToStreamMap) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(is);
      Element xmlRoot = document.getDocumentElement();
      // double version = Double.parseDouble( elementNode.getAttribute(
      // "version" ) );
      HashMap<Integer, edu.cmu.cs.dennisc.scenegraph.Element> keyToElementMap = new HashMap<Integer, edu.cmu.cs.dennisc.scenegraph.Element>();
      Vector<AbstractPropertyReference> referencesToBeResolved = new Vector<AbstractPropertyReference>();
      Component sgRoot = decodeComponent(xmlRoot, filenameToStreamMap, keyToElementMap, referencesToBeResolved);
      Element[] xmlElements = getChildren(xmlRoot, "element");
      for (Element xmlElement : xmlElements) {
        decodeElement(xmlElement, filenameToStreamMap, keyToElementMap, referencesToBeResolved);
      }
      for (AbstractPropertyReference propertyReference : referencesToBeResolved) {
        propertyReference.resolve(keyToElementMap);
      }
      return sgRoot;
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } catch (SAXException saxe) {
      throw new RuntimeException(saxe);
    } catch (ParserConfigurationException pce) {
      throw new RuntimeException(pce);
    }
  }

  public static Component decode(InputStream is, HashMap<String, InputStream> filenameToStreamMap) {
    BufferedInputStream bis;
    if (is instanceof BufferedInputStream) {
      bis = (BufferedInputStream) is;
    } else {
      bis = new BufferedInputStream(is);
    }
    return decodeInternal(bis, filenameToStreamMap);
  }

  public static Component decodeZip(InputStream is) {
    ZipInputStream zis;
    if (is instanceof ZipInputStream) {
      zis = (ZipInputStream) is;
    } else {
      zis = new ZipInputStream(is);
    }
    HashMap<String, InputStream> filenameToStreamMap = new HashMap<String, InputStream>();
    ZipEntry zipEntry;
    try {
      while ((zipEntry = zis.getNextEntry()) != null) {
        String name = zipEntry.getName();
        if (zipEntry.isDirectory()) {
          // pass
        } else {
          final int BUFFER_SIZE = 2048;
          byte[] buffer = new byte[BUFFER_SIZE];
          ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
          int count;
          while ((count = zis.read(buffer, 0, BUFFER_SIZE)) != -1) {
            baos.write(buffer, 0, count);
          }
          zis.closeEntry();
          ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
          filenameToStreamMap.put(name, bais);
        }
      }
      InputStream rootIS = filenameToStreamMap.get(ROOT_FILENAME);
      if (rootIS == null) {
        throw new RuntimeException(ROOT_FILENAME);
      }
      filenameToStreamMap.remove(ROOT_FILENAME);
      return decode(rootIS, filenameToStreamMap);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static Component decode(File file) {
    try {
      try {
        ZipFile zipFile = new ZipFile(file);
        zipFile.close();
        return decodeZip(new ZipInputStream(new FileInputStream(file)));
      } catch (ZipException ze) {
        // empty map
        // todo: use null instead?
        HashMap<String, InputStream> filenameToStreamMap = new HashMap<String, InputStream>();
        return decode(new FileInputStream(file), filenameToStreamMap);
      }
    } catch (FileNotFoundException fnfe) {
      throw new RuntimeException(fnfe);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static Component decode(String path) {
    return decode(new File(path));
  }
}
