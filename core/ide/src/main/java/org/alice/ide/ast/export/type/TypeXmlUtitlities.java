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
package org.alice.ide.ast.export.type;

/**
 * @author Dennis Cosgrove
 */
public class TypeXmlUtitlities {
	private static final String ROOT_TAG = "typeSummary";
	private static final String TYPE_ELEMENT_TAG = "type";
	private static final String RESOURCE_ELEMENT_TAG = "resource";
	private static final String HIERARCHY_ELEMENT_TAG = "hierarchy";
	private static final String PROCEDURE_ELEMENT_TAG = "procedure";
	private static final String FUNCTION_ELEMENT_TAG = "function";
	private static final String FIELD_ELEMENT_TAG = "field";

	private static final String VERSION_ATTRIBUTE = "version";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String CLASS_NAME_ATTRIBUTE = "className";
	private static final String RESOURCE_FIELD_NAME_ATTRIBUTE = "fieldName";
	private static final String RETURN_CLASS_NAME_ATTRIBUTE = "returnClassName";
	private static final String VALUE_CLASS_NAME_ATTRIBUTE = "valueClassName";

	public static final TypeSummary decode( org.w3c.dom.Document xmlDocument ) throws org.lgna.project.VersionNotSupportedException {
		org.w3c.dom.Element xmlElement = xmlDocument.getDocumentElement();
		double typeSummaryVersion = Double.parseDouble( xmlElement.getAttribute( VERSION_ATTRIBUTE ) );
		if( typeSummaryVersion >= TypeSummary.MINIMUM_ACCEPTABLE_VERSION ) {
			org.w3c.dom.NodeList typeElementList = xmlElement.getElementsByTagName( TYPE_ELEMENT_TAG );
			org.w3c.dom.Element typeElement = (org.w3c.dom.Element)typeElementList.item( 0 );
			String typeName = typeElement.getAttribute( NAME_ATTRIBUTE );

			ResourceInfo resourceInfo;
			org.w3c.dom.NodeList resourceElementList = xmlElement.getElementsByTagName( RESOURCE_ELEMENT_TAG );
			if( resourceElementList.getLength() > 0 ) {
				org.w3c.dom.Element resourceElement = (org.w3c.dom.Element)resourceElementList.item( 0 );
				String resourceClassName = resourceElement.getAttribute( CLASS_NAME_ATTRIBUTE );
				String resourceFieldName;
				if( resourceElement.hasAttribute( RESOURCE_FIELD_NAME_ATTRIBUTE ) ) {
					resourceFieldName = resourceElement.getAttribute( RESOURCE_FIELD_NAME_ATTRIBUTE );
				} else {
					resourceFieldName = null;
				}
				resourceInfo = new ResourceInfo( resourceClassName, resourceFieldName );
			} else {
				resourceInfo = null;
			}
			java.util.List<String> hierarchyClassNames = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			org.w3c.dom.NodeList hierarchyElementList = xmlElement.getElementsByTagName( HIERARCHY_ELEMENT_TAG );
			for( int i = 0; i < hierarchyElementList.getLength(); i++ ) {
				org.w3c.dom.Element procedureElement = (org.w3c.dom.Element)hierarchyElementList.item( i );
				hierarchyClassNames.add( procedureElement.getAttribute( NAME_ATTRIBUTE ) );
			}

			java.util.List<String> procedureNames = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			org.w3c.dom.NodeList procedureElementList = xmlElement.getElementsByTagName( PROCEDURE_ELEMENT_TAG );
			for( int i = 0; i < procedureElementList.getLength(); i++ ) {
				org.w3c.dom.Element procedureElement = (org.w3c.dom.Element)procedureElementList.item( i );
				procedureNames.add( procedureElement.getAttribute( NAME_ATTRIBUTE ) );
			}
			java.util.List<FunctionInfo> functionInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			org.w3c.dom.NodeList functionElementList = xmlElement.getElementsByTagName( FUNCTION_ELEMENT_TAG );
			for( int i = 0; i < functionElementList.getLength(); i++ ) {
				org.w3c.dom.Element functionElement = (org.w3c.dom.Element)functionElementList.item( i );
				functionInfos.add( new FunctionInfo( functionElement.getAttribute( RETURN_CLASS_NAME_ATTRIBUTE ), functionElement.getAttribute( NAME_ATTRIBUTE ) ) );
			}
			java.util.List<FieldInfo> fieldInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			org.w3c.dom.NodeList fieldElementList = xmlElement.getElementsByTagName( FIELD_ELEMENT_TAG );
			for( int i = 0; i < fieldElementList.getLength(); i++ ) {
				org.w3c.dom.Element fieldElement = (org.w3c.dom.Element)fieldElementList.item( i );
				fieldInfos.add( new FieldInfo( fieldElement.getAttribute( VALUE_CLASS_NAME_ATTRIBUTE ), fieldElement.getAttribute( NAME_ATTRIBUTE ) ) );
			}
			return new TypeSummary( typeSummaryVersion, typeName, hierarchyClassNames, resourceInfo, procedureNames, functionInfos, fieldInfos );
		} else {
			throw new org.lgna.project.VersionNotSupportedException( TypeSummary.MINIMUM_ACCEPTABLE_VERSION, typeSummaryVersion );
		}
	}

	public static final org.w3c.dom.Document encode( TypeSummary typeSummary ) {
		org.w3c.dom.Document rv = edu.cmu.cs.dennisc.xml.XMLUtilities.createDocument();
		org.w3c.dom.Element xmlTypeSummary = rv.createElement( ROOT_TAG );
		xmlTypeSummary.setAttribute( VERSION_ATTRIBUTE, Double.toString( typeSummary.getVersion() ) );

		org.w3c.dom.Element xmlType = rv.createElement( TYPE_ELEMENT_TAG );
		xmlType.setAttribute( NAME_ATTRIBUTE, typeSummary.getTypeName() );
		xmlTypeSummary.appendChild( xmlType );

		for( String hierarchyClassName : typeSummary.getHierarchyClassNames() ) {
			org.w3c.dom.Element xmlHeirarchy = rv.createElement( HIERARCHY_ELEMENT_TAG );
			xmlHeirarchy.setAttribute( NAME_ATTRIBUTE, hierarchyClassName );
			xmlTypeSummary.appendChild( xmlHeirarchy );
		}

		ResourceInfo resourceInfo = typeSummary.getResourceInfo();
		if( resourceInfo != null ) {
			org.w3c.dom.Element xmlResource = rv.createElement( RESOURCE_ELEMENT_TAG );
			xmlResource.setAttribute( CLASS_NAME_ATTRIBUTE, resourceInfo.getClassName() );
			String fieldName = resourceInfo.getFieldName();
			if( fieldName != null ) {
				xmlResource.setAttribute( RESOURCE_FIELD_NAME_ATTRIBUTE, resourceInfo.getFieldName() );
			}
			xmlTypeSummary.appendChild( xmlResource );
		}

		for( String procedureName : typeSummary.getProcedureNames() ) {
			org.w3c.dom.Element xmlProcedure = rv.createElement( PROCEDURE_ELEMENT_TAG );
			xmlProcedure.setAttribute( NAME_ATTRIBUTE, procedureName );
			xmlTypeSummary.appendChild( xmlProcedure );
		}

		for( FunctionInfo functionInfo : typeSummary.getFunctionInfos() ) {
			org.w3c.dom.Element xmlFunction = rv.createElement( FUNCTION_ELEMENT_TAG );
			xmlFunction.setAttribute( RETURN_CLASS_NAME_ATTRIBUTE, functionInfo.getReturnClassName() );
			xmlFunction.setAttribute( NAME_ATTRIBUTE, functionInfo.getName() );
			xmlTypeSummary.appendChild( xmlFunction );
		}

		for( FieldInfo fieldInfo : typeSummary.getFieldInfos() ) {
			org.w3c.dom.Element xmlField = rv.createElement( FIELD_ELEMENT_TAG );
			xmlField.setAttribute( VALUE_CLASS_NAME_ATTRIBUTE, fieldInfo.getValueClassName() );
			xmlField.setAttribute( NAME_ATTRIBUTE, fieldInfo.getName() );
			xmlTypeSummary.appendChild( xmlField );
		}

		rv.appendChild( xmlTypeSummary );
		xmlTypeSummary.normalize();
		return rv;
	}

	public static void main( String[] args ) throws Exception {
		TypeSummary typeSummary = new TypeSummary(
				TypeSummary.CURRENT_VERSION,
				"Bunny",
				edu.cmu.cs.dennisc.java.util.Lists.newArrayList( "Biped", "org.lgna.story.SBiped" ),
				new ResourceInfo( "org.lgna.story.resources.biped.BunnyResource", "DEFAULT" ),
				edu.cmu.cs.dennisc.java.util.Lists.newArrayList( "hop", "skip", "jump" ),
				edu.cmu.cs.dennisc.java.util.Lists.newArrayList( new FunctionInfo( "java.lang.Boolean", "isHappy" ), new FunctionInfo( "Hurdle", "getClosestHurdle" ) ),
				edu.cmu.cs.dennisc.java.util.Lists.newArrayList( new FieldInfo( "java.lang.Integer", "coinCount" ) )
				);
		org.w3c.dom.Document xmlDocument = encode( typeSummary );
		edu.cmu.cs.dennisc.xml.XMLUtilities.write( xmlDocument, System.out );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();

		TypeSummary decodedTypeSummary = decode( xmlDocument );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( decodedTypeSummary );

		org.w3c.dom.Document decodedXmlDocument = encode( decodedTypeSummary );
		edu.cmu.cs.dennisc.xml.XMLUtilities.write( decodedXmlDocument, System.out );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
	}
}
