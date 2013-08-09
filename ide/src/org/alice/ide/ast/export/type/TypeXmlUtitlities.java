/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.ide.ast.export.type;

/**
 * @author Dennis Cosgrove
 */
public class TypeXmlUtitlities {
	private static final String ROOT_TAG = "TypeDetails";
	private static final String TYPE_ELEMENT_TAG = "type";
	private static final String RESOURCE_ELEMENT_TAG = "resource";
	private static final String PROCEDURE_ELEMENT_TAG = "procedure";
	private static final String FUNCTION_ELEMENT_TAG = "function";
	private static final String FIELD_ELEMENT_TAG = "field";

	private static final String VERSION_ATTRIBUTE = "version";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String CLASS_NAME_ATTRIBUTE = "className";

	public static final TypeDetails decode( org.w3c.dom.Document xmlDocument ) throws org.lgna.project.VersionNotSupportedException {
		org.w3c.dom.Element xmlElement = xmlDocument.getDocumentElement();
		double typeDetailsVersion = Double.parseDouble( xmlElement.getAttribute( VERSION_ATTRIBUTE ) );
		if( typeDetailsVersion >= TypeDetails.MINIMUM_ACCEPTABLE_VERSION ) {
			org.w3c.dom.NodeList typeElementList = xmlElement.getElementsByTagName( TYPE_ELEMENT_TAG );
			org.w3c.dom.Element typeElement = (org.w3c.dom.Element)typeElementList.item( 0 );
			String typeName = typeElement.getAttribute( NAME_ATTRIBUTE );

			String resourceClassName;
			org.w3c.dom.NodeList resourceElementList = xmlElement.getElementsByTagName( RESOURCE_ELEMENT_TAG );
			if( resourceElementList.getLength() > 0 ) {
				org.w3c.dom.Element resourceElement = (org.w3c.dom.Element)resourceElementList.item( 0 );
				resourceClassName = resourceElement.getAttribute( CLASS_NAME_ATTRIBUTE );
			} else {
				resourceClassName = null;
			}
			java.util.List<String> procedureNames = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			org.w3c.dom.NodeList procedureElementList = xmlElement.getElementsByTagName( PROCEDURE_ELEMENT_TAG );
			for( int i = 0; i < procedureElementList.getLength(); i++ ) {
				org.w3c.dom.Element procedureElement = (org.w3c.dom.Element)procedureElementList.item( i );
				procedureNames.add( procedureElement.getAttribute( NAME_ATTRIBUTE ) );
			}
			java.util.List<String> functionNames = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			org.w3c.dom.NodeList functionElementList = xmlElement.getElementsByTagName( FUNCTION_ELEMENT_TAG );
			for( int i = 0; i < functionElementList.getLength(); i++ ) {
				org.w3c.dom.Element functionElement = (org.w3c.dom.Element)functionElementList.item( i );
				functionNames.add( functionElement.getAttribute( NAME_ATTRIBUTE ) );
			}
			java.util.List<String> fieldNames = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			org.w3c.dom.NodeList fieldElementList = xmlElement.getElementsByTagName( FIELD_ELEMENT_TAG );
			for( int i = 0; i < fieldElementList.getLength(); i++ ) {
				org.w3c.dom.Element fieldElement = (org.w3c.dom.Element)fieldElementList.item( i );
				fieldNames.add( fieldElement.getAttribute( NAME_ATTRIBUTE ) );
			}
			return new TypeDetails( typeDetailsVersion, typeName, resourceClassName, procedureNames, functionNames, fieldNames );
		} else {
			throw new org.lgna.project.VersionNotSupportedException( TypeDetails.MINIMUM_ACCEPTABLE_VERSION, typeDetailsVersion );
		}
	}

	public static final org.w3c.dom.Document encode( TypeDetails typeDetails ) {
		org.w3c.dom.Document rv = edu.cmu.cs.dennisc.xml.XMLUtilities.createDocument();
		org.w3c.dom.Element xmlTypeDetails = rv.createElement( ROOT_TAG );
		xmlTypeDetails.setAttribute( VERSION_ATTRIBUTE, Double.toString( typeDetails.getVersion() ) );

		org.w3c.dom.Element xmlType = rv.createElement( TYPE_ELEMENT_TAG );
		xmlType.setAttribute( NAME_ATTRIBUTE, typeDetails.getTypeName() );
		xmlTypeDetails.appendChild( xmlType );

		String resourceClassName = typeDetails.getResourceClassName();
		if( resourceClassName != null ) {
			org.w3c.dom.Element xmlResource = rv.createElement( RESOURCE_ELEMENT_TAG );
			xmlResource.setAttribute( CLASS_NAME_ATTRIBUTE, resourceClassName );
			xmlTypeDetails.appendChild( xmlResource );
		}

		for( String procedureName : typeDetails.getProcedureNames() ) {
			org.w3c.dom.Element xmlProcedure = rv.createElement( PROCEDURE_ELEMENT_TAG );
			xmlProcedure.setAttribute( NAME_ATTRIBUTE, procedureName );
			xmlTypeDetails.appendChild( xmlProcedure );
		}

		for( String functionName : typeDetails.getFunctionNames() ) {
			org.w3c.dom.Element xmlFunction = rv.createElement( FUNCTION_ELEMENT_TAG );
			xmlFunction.setAttribute( NAME_ATTRIBUTE, functionName );
			xmlTypeDetails.appendChild( xmlFunction );
		}

		for( String fieldName : typeDetails.getFieldNames() ) {
			org.w3c.dom.Element xmlField = rv.createElement( FIELD_ELEMENT_TAG );
			xmlField.setAttribute( NAME_ATTRIBUTE, fieldName );
			xmlTypeDetails.appendChild( xmlField );
		}

		rv.appendChild( xmlTypeDetails );
		xmlTypeDetails.normalize();
		return rv;
	}

	public static void main( String[] args ) throws Exception {
		TypeDetails typeDetails = new TypeDetails(
				TypeDetails.CURRENT_VERSION,
				"Bunny",
				"org.lgna.story.resources.biped.BunnyResource",
				edu.cmu.cs.dennisc.java.util.Collections.newArrayList( "hop", "skip", "jump" ),
				edu.cmu.cs.dennisc.java.util.Collections.newArrayList( "isHappy", "getClosestHurdle" ),
				edu.cmu.cs.dennisc.java.util.Collections.newArrayList( "coinCount" )
				);
		org.w3c.dom.Document xmlDocument = encode( typeDetails );
		edu.cmu.cs.dennisc.xml.XMLUtilities.write( xmlDocument, System.out );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();

		TypeDetails decodedTypeDetails = decode( xmlDocument );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( decodedTypeDetails );

		org.w3c.dom.Document decodedXmlDocument = encode( decodedTypeDetails );
		edu.cmu.cs.dennisc.xml.XMLUtilities.write( decodedXmlDocument, System.out );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
	}
}
