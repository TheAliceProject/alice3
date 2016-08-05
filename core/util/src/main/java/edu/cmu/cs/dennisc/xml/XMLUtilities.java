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

/**
 * @author Dennis Cosgrove
 */
public class XMLUtilities {
	private static javax.xml.parsers.DocumentBuilderFactory s_documentBuilderFactory = null;
	private static javax.xml.parsers.DocumentBuilder s_documentBuilder = null;
	private static javax.xml.transform.TransformerFactory s_transformerFactory = null;
	private static javax.xml.transform.Transformer s_transformer = null;

	private static javax.xml.parsers.DocumentBuilderFactory getDocumentBuilderFactory() {
		if( s_documentBuilderFactory != null ) {
			//pass
		} else {
			s_documentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		}
		return s_documentBuilderFactory;
	}

	private static javax.xml.parsers.DocumentBuilder getDocumentBuilder() {
		if( s_documentBuilder != null ) {
			//pass
		} else {
			try {
				s_documentBuilder = getDocumentBuilderFactory().newDocumentBuilder();
			} catch( javax.xml.parsers.ParserConfigurationException pce ) {
				throw new RuntimeException( pce );
			}
		}
		return s_documentBuilder;
	}

	public static org.w3c.dom.Document createDocument() {
		return getDocumentBuilder().newDocument();
	}

	private static javax.xml.transform.TransformerFactory getTransformerFactory() {
		if( s_transformerFactory != null ) {
			//pass
		} else {
			s_transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
		}
		return s_transformerFactory;
	}

	private static javax.xml.transform.Transformer getTransformer() {
		if( s_transformer != null ) {
			//pass
		} else {
			try {
				s_transformer = getTransformerFactory().newTransformer();
			} catch( javax.xml.transform.TransformerConfigurationException tce ) {
				throw new RuntimeException( tce );
			}
		}
		return s_transformer;
	}

	public static void write( org.w3c.dom.Document xmlDocument, java.io.OutputStream os ) {
		xmlDocument.getDocumentElement().normalize();
		try {
			javax.xml.transform.stream.StreamResult streamResult = new javax.xml.transform.stream.StreamResult( os );
			getTransformer().transform( new javax.xml.transform.dom.DOMSource( xmlDocument ), streamResult );
		} catch( javax.xml.transform.TransformerException te ) {
			throw new RuntimeException( te );
		}
	}

	public static void write( org.w3c.dom.Document xmlDocument, java.io.File file ) {
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		try {
			java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
			write( xmlDocument, fos );
			fos.flush();
			fos.close();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static void write( org.w3c.dom.Document xmlDocument, String path ) {
		write( xmlDocument, new java.io.File( path ) );
	}

	public static org.w3c.dom.Document read( java.io.InputStream is ) {
		try {
			//todo?
			javax.xml.parsers.DocumentBuilder documentBuilder = getDocumentBuilder();
			synchronized( documentBuilder ) {
				org.w3c.dom.Document rv = documentBuilder.parse( is );
				// TODO: we should validate this document with a schema and call DocumentBuilderFactory.setIgnoringElementContentWhitespace() instead.
				// but this work-around will at least allow us to load files that have empty white space.
				removeWhitespaceNodes( rv.getDocumentElement() );
				return rv;
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		} catch( org.xml.sax.SAXException saxe ) {
			throw new RuntimeException( saxe );
		}
	}

	public static org.w3c.dom.Document read( java.io.File file ) {
		try {
			return read( new java.io.FileInputStream( file ) );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.toString(), ioe );
		}
		//		try {
		//			//todo?
		//			javax.xml.parsers.DocumentBuilder documentBuilder = getDocumentBuilder();
		//			synchronized( documentBuilder ) {
		//				org.w3c.dom.Document rv = documentBuilder.parse( file );
		//				return rv;
		//			}
		//		} catch( java.io.IOException ioe ) {
		//			throw new RuntimeException( file.toString(), ioe );
		//		} catch( org.xml.sax.SAXException saxe ) {
		//			throw new RuntimeException( file.toString(), saxe );
		//		}
	}

	public static org.w3c.dom.Document read( String path ) {
		return read( new java.io.File( path ) );
	}

	// TODO: This removes the indent whitespace from the xml. This is a hack. We should define
	// an xml schema for project files and validate the document. This is the right way to do this 
	// anyway... not just because of whitespace.
	@Deprecated
	private static void removeWhitespaceNodes( org.w3c.dom.Element e ) {
		org.w3c.dom.NodeList children = e.getChildNodes();
		for( int i = children.getLength() - 1; i >= 0; i-- ) {
			org.w3c.dom.Node child = children.item( i );
			if( ( child instanceof org.w3c.dom.Text ) && ( ( (org.w3c.dom.Text)child ).getData().trim().length() == 0 ) ) {
				e.removeChild( child );
			}
			else if( child instanceof org.w3c.dom.Element ) {
				removeWhitespaceNodes( (org.w3c.dom.Element)child );
			}
		}
	}

	//	//WARNING: this method returns all decendants.  remove.
	//	@Deprecated
	//	public static org.w3c.dom.Element[] getDescendantElementsByTagNameAsArray( org.w3c.dom.Element xmlParent, String tagName ) {
	//		org.w3c.dom.NodeList nodeList = xmlParent.getElementsByTagName( tagName );
	//		org.w3c.dom.Element[] rv = new org.w3c.dom.Element[ nodeList.getLength() ];
	//		for( int i=0; i<nodeList.getLength(); i++ ) {
	//			rv[ i ] = (org.w3c.dom.Element)nodeList.item( i );
	//		}
	//		return rv;
	//	}
	//	//WARNING: this method returns all decendants.  remove.
	//	@Deprecated
	//	public static Iterable< org.w3c.dom.Element > getDescendantElementsByTagNameAsIterable( org.w3c.dom.Element xmlParent, String tagName ) {
	//		return new ElementIterable( xmlParent.getElementsByTagName( tagName ) );
	//	}
	//	//WARNING: this method returns all decendants.  remove.
	//	@Deprecated
	//	public static org.w3c.dom.Element getSingleDescendantElementByTagName( org.w3c.dom.Element xmlParent, String tagName ) {
	//		org.w3c.dom.NodeList nodeList = xmlParent.getElementsByTagName( tagName );
	//		assert nodeList.getLength() == 1 : tagName;
	//		org.w3c.dom.Node node0 = nodeList.item( 0 );
	//		assert node0 instanceof org.w3c.dom.Element : node0;
	//		return (org.w3c.dom.Element)node0;
	//	}

	public static java.util.List<org.w3c.dom.Element> getChildElementsByTagName( org.w3c.dom.Element xmlParent, String tagName ) {
		java.util.List<org.w3c.dom.Element> rv = new java.util.LinkedList<org.w3c.dom.Element>();
		org.w3c.dom.NodeList nodeList = xmlParent.getChildNodes();
		final int N = nodeList.getLength();
		for( int i = 0; i < N; i++ ) {
			org.w3c.dom.Node node = nodeList.item( i );
			if( node instanceof org.w3c.dom.Element ) {
				org.w3c.dom.Element element = (org.w3c.dom.Element)node;
				if( tagName.equals( element.getTagName() ) ) {
					rv.add( element );
				}
			}
		}
		return rv;
	}

	public static org.w3c.dom.Element getSingleChildElementByTagName( org.w3c.dom.Element xmlParent, String tagName ) {
		org.w3c.dom.NodeList nodeList = xmlParent.getChildNodes();
		final int N = nodeList.getLength();
		for( int i = 0; i < N; i++ ) {
			org.w3c.dom.Node node = nodeList.item( i );
			if( node instanceof org.w3c.dom.Element ) {
				org.w3c.dom.Element element = (org.w3c.dom.Element)node;
				if( tagName.equals( element.getTagName() ) ) {
					return element;
				}
			}
		}
		return null;
	}
}
