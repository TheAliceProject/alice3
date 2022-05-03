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
package edu.cmu.cs.dennisc.xml;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class XMLUtilities {

  private static DocumentBuilder getDocumentBuilder() {
    try {
      DocumentBuilderFactory s_documentBuilderFactory = DocumentBuilderFactory.newInstance();
      return s_documentBuilderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      throw new RuntimeException(pce);
    }
  }

  public static Document createDocument() {
    return getDocumentBuilder().newDocument();
  }

  private static Transformer getTransformer() {
    try {
      TransformerFactory s_transformerFactory = TransformerFactory.newInstance();
      Transformer s_transformer = s_transformerFactory.newTransformer();
      // for encoding surrogate character, e.g., emojis
      s_transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-16");
      s_transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      return s_transformer;
    } catch (TransformerConfigurationException tce) {
      throw new RuntimeException(tce);
    }
  }

  public static void write(Document xmlDocument, OutputStream os) {
    xmlDocument.getDocumentElement().normalize();
    try {
      StreamResult streamResult = new StreamResult(os);
      getTransformer().transform(new DOMSource(xmlDocument), streamResult);
    } catch (TransformerException te) {
      throw new RuntimeException(te);
    }
  }

  public static void write(Document xmlDocument, File file) {
    FileUtilities.createParentDirectoriesIfNecessary(file);
    try {
      FileOutputStream fos = new FileOutputStream(file);
      write(xmlDocument, fos);
      fos.flush();
      fos.close();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static void write(Document xmlDocument, String path) {
    write(xmlDocument, new File(path));
  }

  public static Document read(InputStream is) {
    try {
      //todo?
      DocumentBuilder documentBuilder = getDocumentBuilder();
      synchronized (documentBuilder) {
        Document rv = documentBuilder.parse(is);
        // TODO: we should validate this document with a schema and call DocumentBuilderFactory.setIgnoringElementContentWhitespace() instead.
        // but this work-around will at least allow us to load files that have empty white space.
        removeWhitespaceNodes(rv.getDocumentElement());
        return rv;
      }
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } catch (SAXException saxe) {
      throw new RuntimeException(saxe);
    }
  }

  public static Document read(File file) {
    try {
      return read(new FileInputStream(file));
    } catch (IOException ioe) {
      throw new RuntimeException(file.toString(), ioe);
    }
    //    try {
    //      //todo?
    //      javax.xml.parsers.DocumentBuilder documentBuilder = getDocumentBuilder();
    //      synchronized( documentBuilder ) {
    //        org.w3c.dom.Document rv = documentBuilder.parse( file );
    //        return rv;
    //      }
    //    } catch( java.io.IOException ioe ) {
    //      throw new RuntimeException( file.toString(), ioe );
    //    } catch( org.xml.sax.SAXException saxe ) {
    //      throw new RuntimeException( file.toString(), saxe );
    //    }
  }

  public static Document read(String path) {
    return read(new File(path));
  }

  // TODO: This removes the indent whitespace from the xml. This is a hack. We should define
  // an xml schema for project files and validate the document. This is the right way to do this
  // anyway... not just because of whitespace.
  @Deprecated
  private static void removeWhitespaceNodes(Element e) {
    NodeList children = e.getChildNodes();
    for (int i = children.getLength() - 1; i >= 0; i--) {
      Node child = children.item(i);
      if ((child instanceof Text) && (((Text) child).getData().trim().length() == 0)) {
        e.removeChild(child);
      } else if (child instanceof Element) {
        removeWhitespaceNodes((Element) child);
      }
    }
  }

  //  //WARNING: this method returns all decendants.  remove.
  //  @Deprecated
  //  public static org.w3c.dom.Element[] getDescendantElementsByTagNameAsArray( org.w3c.dom.Element xmlParent, String tagName ) {
  //    org.w3c.dom.NodeList nodeList = xmlParent.getElementsByTagName( tagName );
  //    org.w3c.dom.Element[] rv = new org.w3c.dom.Element[ nodeList.getLength() ];
  //    for( int i=0; i<nodeList.getLength(); i++ ) {
  //      rv[ i ] = (org.w3c.dom.Element)nodeList.item( i );
  //    }
  //    return rv;
  //  }
  //  //WARNING: this method returns all decendants.  remove.
  //  @Deprecated
  //  public static Iterable< org.w3c.dom.Element > getDescendantElementsByTagNameAsIterable( org.w3c.dom.Element xmlParent, String tagName ) {
  //    return new ElementIterable( xmlParent.getElementsByTagName( tagName ) );
  //  }
  //  //WARNING: this method returns all decendants.  remove.
  //  @Deprecated
  //  public static org.w3c.dom.Element getSingleDescendantElementByTagName( org.w3c.dom.Element xmlParent, String tagName ) {
  //    org.w3c.dom.NodeList nodeList = xmlParent.getElementsByTagName( tagName );
  //    assert nodeList.getLength() == 1 : tagName;
  //    org.w3c.dom.Node node0 = nodeList.item( 0 );
  //    assert node0 instanceof org.w3c.dom.Element : node0;
  //    return (org.w3c.dom.Element)node0;
  //  }

  public static List<Element> getChildElementsByTagName(Element xmlParent, String tagName) {
    List<Element> rv = new LinkedList<Element>();
    NodeList nodeList = xmlParent.getChildNodes();
    final int N = nodeList.getLength();
    for (int i = 0; i < N; i++) {
      Node node = nodeList.item(i);
      if (node instanceof Element) {
        Element element = (Element) node;
        if (tagName.equals(element.getTagName())) {
          rv.add(element);
        }
      }
    }
    return rv;
  }

  public static Element getSingleChildElementByTagName(Element xmlParent, String tagName) {
    NodeList nodeList = xmlParent.getChildNodes();
    final int N = nodeList.getLength();
    for (int i = 0; i < N; i++) {
      Node node = nodeList.item(i);
      if (node instanceof Element) {
        Element element = (Element) node;
        if (tagName.equals(element.getTagName())) {
          return element;
        }
      }
    }
    return null;
  }
}
