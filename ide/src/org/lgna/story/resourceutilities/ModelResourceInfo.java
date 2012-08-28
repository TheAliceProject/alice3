/*
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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
package org.lgna.story.resourceutilities;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

/**
 * @author dculyba
 * 
 */
public class ModelResourceInfo {
	private final AxisAlignedBox boundingBox;
	private final int creationYear;
	private final String creator;
	private final String resourceName;
	private final String modelName;
	private final String textureName;
	private final ModelResourceInfo parentInfo;
	private final java.util.List<ModelResourceInfo> subResources = new java.util.LinkedList<ModelResourceInfo>();

	private final String[] tags;

	private static AxisAlignedBox getBoundingBoxFromXML( Element bboxElement )
	{
		if( bboxElement != null )
		{
			Element min = (Element)bboxElement.getElementsByTagName( "Min" ).item( 0 );
			Element max = (Element)bboxElement.getElementsByTagName( "Max" ).item( 0 );

			double minX = Double.parseDouble( min.getAttribute( "x" ) );
			double minY = Double.parseDouble( min.getAttribute( "y" ) );
			double minZ = Double.parseDouble( min.getAttribute( "z" ) );

			double maxX = Double.parseDouble( max.getAttribute( "x" ) );
			double maxY = Double.parseDouble( max.getAttribute( "y" ) );
			double maxZ = Double.parseDouble( max.getAttribute( "z" ) );

			AxisAlignedBox bbox = new AxisAlignedBox( minX, minY, minZ, maxX, maxY, maxZ );

			return bbox;
		}

		return null;
	}

	private static ModelResourceInfo getSubResourceFromXML( Element resourceElement, ModelResourceInfo parent )
	{
		if( resourceElement != null )
		{
			AxisAlignedBox bbox = null;
			NodeList bboxNodeList = resourceElement.getElementsByTagName( "BoundingBox" );
			if( bboxNodeList.getLength() > 0 ) {
				bbox = getBoundingBoxFromXML( (Element)bboxNodeList.item( 0 ) );
			}
			String modelName = null;
			if( resourceElement.hasAttribute( "modelName" ) ) {
				modelName = resourceElement.getAttribute( "modelName" );
			}
			String textureName = null;
			if( resourceElement.hasAttribute( "textureName" ) ) {
				textureName = resourceElement.getAttribute( "textureName" );
			}
			String resourceName = null;
			if( resourceElement.hasAttribute( "resourceName" ) ) {
				resourceName = resourceElement.getAttribute( "resourceName" );
			}
			LinkedList<String> tagList = new LinkedList<String>();
			NodeList tagNodeList = resourceElement.getElementsByTagName( "Tag" );
			for( int i = 0; i < tagNodeList.getLength(); i++ ) {
				tagList.add( tagNodeList.item( i ).getTextContent() );
			}
			String[] tags = tagList.toArray( new String[ tagList.size() ] );
			ModelResourceInfo resource = new ModelResourceInfo( parent, resourceName, null, -1, bbox, tags, modelName, textureName );
			return resource;
		}

		return null;
	}

	public ModelResourceInfo( ModelResourceInfo parent, String resourceName, String creator, int creationYear, AxisAlignedBox boundingBox, String[] tags, String modelName, String textureName ) {
		this.parentInfo = parent;
		this.resourceName = resourceName;
		this.creator = creator;
		this.creationYear = creationYear;
		this.boundingBox = boundingBox;
		this.tags = tags;
		this.textureName = textureName;
		this.modelName = modelName;
	}

	private static java.util.List<Element> getImmediateChildElementsByTagName( Element node, String tagName ) {
		java.util.List<Element> elements = new LinkedList<Element>();
		NodeList children = node.getChildNodes();
		for( int i = 0; i < children.getLength(); i++ ) {
			org.w3c.dom.Node child = children.item( i );
			if( ( child instanceof Element ) && child.getNodeName().equals( tagName ) ) {
				elements.add( (Element)child );
			}
		}
		return elements;
	}

	public ModelResourceInfo( Document xmlDoc ) {
		Element modelElement = xmlDoc.getDocumentElement();
		if( !modelElement.getNodeName().equals( "AliceModel" ) ) {
			modelElement = XMLUtilities.getSingleChildElementByTagName( xmlDoc.getDocumentElement(), "AliceModel" );
		}
		assert modelElement != null;
		java.util.List<Element> bboxNodeList = getImmediateChildElementsByTagName( modelElement, "BoundingBox" );
		if( bboxNodeList.size() > 0 ) {
			this.boundingBox = getBoundingBoxFromXML( bboxNodeList.get( 0 ) );
		}
		else {
			this.boundingBox = new AxisAlignedBox();
		}
		this.modelName = modelElement.getAttribute( "name" );
		this.creator = modelElement.getAttribute( "creator" );
		int creationYearTemp = -1;
		try {
			creationYearTemp = Integer.parseInt( modelElement.getAttribute( "creationYear" ) );
		} catch( Exception e ) {
		}
		this.creationYear = creationYearTemp;

		LinkedList<String> tagList = new LinkedList<String>();
		java.util.List<Element> tagsElementList = getImmediateChildElementsByTagName( modelElement, "Tags" );
		for( Element tagsElement : tagsElementList ) {
			java.util.List<Element> tagElementList = getImmediateChildElementsByTagName( tagsElement, "Tag" );
			for( Element tagElement : tagElementList ) {
				tagList.add( tagElement.getTextContent() );
			}
		}
		this.tags = tagList.toArray( new String[ tagList.size() ] );

		java.util.List<Element> subResourcesList = getImmediateChildElementsByTagName( modelElement, "Resource" );
		for( Element subResourceElement : subResourcesList ) {
			ModelResourceInfo subResource = getSubResourceFromXML( subResourceElement, this );
			if( subResource != null ) {
				subResources.add( subResource );
			}
			else {
				Logger.severe( "Failed to make sub resource for " + subResourceElement );
			}
		}
		this.textureName = null;
		this.resourceName = null;
		this.parentInfo = null;
	}

	public AxisAlignedBox getBoundingBox() {
		if( ( this.boundingBox == null ) && ( parentInfo != null ) ) {
			return this.parentInfo.boundingBox;
		}
		return boundingBox;
	}

	public int getCreationYear() {
		if( ( this.creationYear == -1 ) && ( parentInfo != null ) ) {
			return this.parentInfo.creationYear;
		}
		return creationYear;
	}

	public String getCreator() {
		if( ( this.creator == null ) && ( parentInfo != null ) ) {
			return this.parentInfo.creator;
		}
		return creator;
	}

	public String getResourceName() {
		if( ( this.resourceName == null ) && ( parentInfo != null ) ) {
			return this.parentInfo.resourceName;
		}
		return resourceName;
	}

	public String getModelName() {
		if( ( this.modelName == null ) && ( parentInfo != null ) ) {
			return this.parentInfo.modelName;
		}
		return modelName;
	}

	public String getTextureName() {
		if( ( this.textureName == null ) && ( parentInfo != null ) ) {
			return this.parentInfo.textureName;
		}
		return textureName;
	}

	public String[] getTags() {
		if( this.parentInfo != null ) {
			String[] allTags = new String[ this.tags.length + this.parentInfo.tags.length ];
			if( this.parentInfo.tags.length > 0 ) {
				System.arraycopy( this.parentInfo.tags, 0, allTags, 0, this.parentInfo.tags.length );
			}
			if( this.tags.length > 0 ) {
				System.arraycopy( this.tags, 0, allTags, this.parentInfo.tags.length, this.tags.length );
			}
			return allTags;
		}
		return tags;
	}

	public ModelResourceInfo getSubResource( String modelName, String textureName ) {
		for( ModelResourceInfo mri : this.subResources ) {
			String subModel = mri.getModelName();
			String subTexture = mri.getTextureName();
			if( ( subModel != null ) && subModel.equalsIgnoreCase( modelName ) &&
					( subTexture != null ) && subTexture.equalsIgnoreCase( textureName ) ) {
				return mri;
			}
		}
		return null;
	}

	public ModelResourceInfo getSubResource( String resourceName ) {
		for( ModelResourceInfo mri : this.subResources ) {
			String subResource = mri.getResourceName();
			if( ( subResource != null ) && subResource.equalsIgnoreCase( resourceName ) ) {
				return mri;
			}
		}
		return null;
	}

}
