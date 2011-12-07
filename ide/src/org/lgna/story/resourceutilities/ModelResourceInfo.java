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
	private final String name;
	
	private final String[] tags;
	
	private static AxisAlignedBox getBoundingBoxFromXML(Element bboxElement)
	{
		if (bboxElement != null)
		{
			Element min = (Element)bboxElement.getElementsByTagName("Min").item(0);
			Element max = (Element)bboxElement.getElementsByTagName("Max").item(0);
			
			double minX = Double.parseDouble(min.getAttribute("x"));
			double minY = Double.parseDouble(min.getAttribute("y"));
			double minZ = Double.parseDouble(min.getAttribute("z"));
			
			double maxX = Double.parseDouble(max.getAttribute("x"));
			double maxY = Double.parseDouble(max.getAttribute("y"));
			double maxZ = Double.parseDouble(max.getAttribute("z"));
			
			AxisAlignedBox bbox = new AxisAlignedBox(minX, minY, minZ, maxX, maxY, maxZ);
			
			return bbox;
		}
		
		return null;
	}
	
	public ModelResourceInfo(String name, String creator, int creationYear, AxisAlignedBox boundingBox, String[] tags) {
		this.name = name;
		this.creator = creator;
		this.creationYear = creationYear;
		this.boundingBox = boundingBox;
		this.tags = tags;
	}
	
	public ModelResourceInfo(Document xmlDoc) {
		Element modelElement = xmlDoc.getDocumentElement();
		if (!modelElement.getNodeName().equals("AliceModel")) {
			modelElement = XMLUtilities.getSingleChildElementByTagName(xmlDoc.getDocumentElement(), "AliceModel");
		}
		assert modelElement != null;
		NodeList bboxNodeList = modelElement.getElementsByTagName("BoundingBox");
		if (bboxNodeList.getLength() > 0) {
			this.boundingBox = getBoundingBoxFromXML((Element)bboxNodeList.item(0));
		}
		else {
			this.boundingBox = new AxisAlignedBox();
		}
		this.name = modelElement.getAttribute("name");
		this.creator = modelElement.getAttribute("creator");
		int creationYearTemp = -1;
		try {
			creationYearTemp = Integer.parseInt(modelElement.getAttribute("creationYear"));
		}
		catch (Exception e) { }
		this.creationYear = creationYearTemp;
		
		LinkedList<String> tagList = new LinkedList<String>();
		NodeList tagNodeList = modelElement.getElementsByTagName("Tag");
		for (int i=0; i<tagNodeList.getLength(); i++) {
			tagList.add(tagNodeList.item(i).getTextContent());
		}
		this.tags = tagList.toArray(new String[tagList.size()]);
	}

	/**
	 * @return the boundingBox
	 */
	public AxisAlignedBox getBoundingBox() {
		return boundingBox;
	}

	/**
	 * @return the creationYear
	 */
	public int getCreationYear() {
		return creationYear;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}
	

}
