/*******************************************************************************
 * Copyright (c) 2019 Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.croquet.models.html;

import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import org.alice.ide.IDE;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.code.ProcessableNode;
import org.lgna.project.io.ProjectIo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;

public class HtmlProjectWriter implements ProjectIo.ProjectWriter {
  public HtmlProjectWriter() {
  }

  @Override
  public void writeType(OutputStream os, NamedUserType type, DataSource... dataSources) throws IOException {
    writeXml(os, type.getName(), (classDiv, coder) -> {
      classDiv.setAttribute("class", "alice-class");
      coder.encode(type, classDiv);
    });
  }

  @Override
  public void writeProject(OutputStream os, final Project project, DataSource... dataSources) throws IOException {
    String windowTitle = IDE.getActiveInstance().getDocumentFrame().getFrame().getTitle();
    writeXml(os, windowTitle, (classesDiv, coder) -> {
      classesDiv.setAttribute("class", "alice-classes");
      for (NamedUserType userType : project.getNamedUserTypes()) {
        coder.encode(userType, classesDiv);
      }
    });
  }

  public void writeDeclaration(OutputStream os, AbstractDeclaration declaration) throws IOException {
    writeXml(os, declaration.getName(), (classDiv, coder) -> {
      classDiv.setAttribute("class", "alice-method");
      if (declaration instanceof ProcessableNode) {
        coder.encode((ProcessableNode) declaration, classDiv);
      }
    });
  }

  interface Content {
    void addToBody(Element body, HtmlEncoder coder);
  }

  private void writeXml(OutputStream os, String documentTitle, Content content) throws IOException {
    Element html = asXhtml(documentTitle, content);
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    try {
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      DOMSource domSource = new DOMSource(html);
      StreamResult streamResult = new StreamResult(os);

      transformer.transform(domSource, streamResult);
    } catch (TransformerException e) {
      e.printStackTrace();
    }
  }

  private Element asXhtml(String documentTitle, Content content) throws IOException {
    Document document = createDocument();

    Element html = document.createElement("html");
    document.appendChild(html);

    final Element head = document.createElement("head");

    final Element title = document.createElement("title");
    title.setTextContent(documentTitle);
    head.appendChild(title);

    Element style = document.createElement("style");
    style.setAttribute("type", "text/css");
    style.setAttribute("media", "all");
    style.setTextContent(cssStyle);
    head.appendChild(style);

    html.appendChild(head);

    Element body = document.createElement("body");
    html.appendChild(body);

    Element bodyContent = document.createElement("div");
    body.appendChild(bodyContent);

    content.addToBody(bodyContent, new HtmlEncoder(document));

    return html;
  }

  private Document createDocument() throws IOException {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    try {
      return documentFactory.newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
      throw new IOException("Unable to serialize data");
    }
  }

  private static final String cssStyle = ".alice-class {\nbackground: #D3D7F0;\nborder: 1px solid #999999;\nmargin:5px 0;}\n"
      + ".alice-generated-svg {\ndisplay: inline-block;\n}\n"
      + ".alice-class-header {\nfont-size: x-large;\n}\n"
      + ".alice-code-header-detail {\nfont-style: italic;\n}\n"
      + ".alice-method-name, .alice-code-header-detail, .alice-class-name, .alice-class-superType, .alice-assignment, .alice-field-name {\nmargin: 0 5px 0 0;\n}\n"
      + ".alice-parameter-label {\nmargin: 0 5px 0 2px;\n}\n"
      + ".alice-method-name {\nfont-size: large;\nfont-weight: bold;\n}\n"
      + ".alice-method {\npadding: 10px;\n}\n"
      + ".alice-listener-addition {\nborder: 1px solid #999999;\nmargin: 5px 5px 10px 20px;\nposition:relative;\n}\n"
      + ".alice-listener-declaration {\nbackground: #ADA7D1;\npadding: 5px;\nmargin: 5px;\n}\n"
      // This works in the html view, but does not get properly rendered into the pdf
      + ".alice-disabled {\nheight: 100%;\nwidth: 100%;\ntop: 0;left: 0;\nposition: absolute;\nbackground: repeating-linear-gradient( 135deg, #d3d7f000, #D3D7F000 6px, #9396A5FF 6px, #9396A5FF 7px);}\n";
}

