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
package org.lgna.story.resourceutilities;

import java.util.LinkedList;
import java.util.List;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;

/**
 * @author alice
 * 
 */
public class ModelSubResourceExporter {
	private final String textureName;
	private final String modelName;
	private AxisAlignedBox bbox = null;
	private final String typeString;
	private final String attributionName;
	private final String attributionYear;
	List<String> tags = new LinkedList<String>();
	List<String> groupTags = new LinkedList<String>();
	List<String> themeTags = new LinkedList<String>();

	//	public ModelSubResourceExporter( String modelName, String textureName, String typeString ) {
	//		this( modelName, textureName, typeString, null );
	//	}

	public ModelSubResourceExporter( String modelName, String textureName, String typeString, String attributionName, String attributionYear ) {
		this.modelName = modelName;
		this.textureName = textureName;
		this.typeString = typeString;
		this.attributionName = attributionName;
		this.attributionYear = attributionYear;
	}

	public AxisAlignedBox getBbox() {
		return bbox;
	}

	public void setBbox( AxisAlignedBox bbox ) {
		this.bbox = bbox;
	}

	public String getModelName() {
		return modelName;
	}

	public String getTextureName() {
		return textureName;
	}

	public String getTypeString() {
		return this.typeString;
	}

	public String getAttributionName() {
		return this.attributionName;
	}

	public String getAttributionYear() {
		return this.attributionYear;
	}

	public List<String> getTags() {
		return tags;
	}

	public List<String> getGroupTags() {
		return groupTags;
	}

	public List<String> getThemeTags() {
		return themeTags;
	}

	public void addTags( String... tags ) {
		for( String tag : tags ) {
			this.tags.add( tag );
		}
	}

	public void addGroupTags( String... tags ) {
		for( String tag : tags ) {
			this.groupTags.add( tag );
		}
	}

	public void addThemeTags( String... tags ) {
		for( String tag : tags ) {
			this.themeTags.add( tag );
		}
	}

}
