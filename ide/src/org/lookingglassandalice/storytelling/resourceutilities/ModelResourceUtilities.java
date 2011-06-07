/**
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
package org.lookingglassandalice.storytelling.resourceutilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

/**
 * @author dculyba
 *
 */
public class ModelResourceUtilities {

	public static String getName(Class modelResource)
	{
		return modelResource.getSimpleName();
	}
	
	public static BufferedImage getThumbnail(Class modelResource)
	{
		String name = getName(modelResource);
		try
		{
			BufferedImage image = ImageIO.read(modelResource.getResource("resources/"+ name+".png"));
			return image;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private static AxisAlignedBox getBoundingBoxFromXML(Document doc)
	{
		Element bboxElement = XMLUtilities.getSingleChildElementByTagName(doc.getDocumentElement(), "BoundingBox");
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
	
	public static AxisAlignedBox getBoundingBox(Class modelResource)
	{
		String name = getName(modelResource);
		try {
			InputStream is = modelResource.getResourceAsStream("resources/"+name+".xml");
			Document doc = XMLUtilities.read(is);
			return getBoundingBoxFromXML(doc);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
