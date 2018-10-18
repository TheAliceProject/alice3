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

import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.modelresource.EnumConstantResourceKey;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.tweedle.file.ModelManifest;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.SModel;
import org.lgna.story.resources.DynamicResource;
import org.lgna.story.resources.ModelResource;

/**
 * @author dculyba
 * 
 */
public class TypedDefinedGalleryTreeNode extends GalleryResourceTreeNode {
	TypedDefinedGalleryTreeNode( NamedUserType aliceClass, Class<? extends ModelResource> resourceClass,
								 Class<? extends SModel> modelClass ) {
		super(aliceClass != null ? aliceClass.getName() : null);
		this.userType = aliceClass;
		if( this.userType != null ) {
			this.name = this.userType.getName();
		}
		this.modelClass = modelClass;
		this.resourceJavaType = JavaType.getInstance( resourceClass );
	}

	public void setUserType( NamedUserType type ) {
		userType = type;
	}

	public NamedUserType getUserType() {
		return userType;
	}

	JavaType getResourceJavaType() {
		return this.resourceJavaType;
	}

	void setJavaField( JavaField field ) {
		this.resourceJavaField = field;
	}

	JavaField getJavaField() {
		return this.resourceJavaField;
	}

	void setDynamicResource( Class<? extends DynamicResource> dynamicClass ) {
		this.dynamicClass = dynamicClass;
	}

	@Override
	public String toString() {
		JavaType value = resourceJavaType;
		return value != null ? value.getName() : null;
	}

	private TypedDefinedGalleryTreeNode getChildWithJavaType( AbstractType<?, ?, ?> type ) {
		for( GalleryResourceTreeNode child : this.children ) {
			if (child instanceof TypedDefinedGalleryTreeNode) {
				if ((((TypedDefinedGalleryTreeNode)child).resourceJavaType != null) && type.isAssignableFrom(((TypedDefinedGalleryTreeNode)child).resourceJavaType)) {
					return (TypedDefinedGalleryTreeNode)child;
				}
			}
		}
		return null;
	}

	TypedDefinedGalleryTreeNode getDescendantOfJavaType( AbstractType<?, ?, ?> type ) {
		TypedDefinedGalleryTreeNode rv = this.getChildWithJavaType( type );
		if( rv != null ) {
			return rv;
		}
		if( this.getChildCount() > 0 ) {
			for( GalleryResourceTreeNode child : this.children ) {
				if (child instanceof TypedDefinedGalleryTreeNode) {
					TypedDefinedGalleryTreeNode result = ((TypedDefinedGalleryTreeNode)child).getDescendantOfJavaType(type);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	private NamedUserType userType;
	private JavaType resourceJavaType;
	private final Class<? extends SModel> modelClass;
	private Class<? extends DynamicResource> dynamicClass;
	private JavaField resourceJavaField = null;

	@Override
	public ResourceKey createResourceKey() {
		ResourceKey resourceKey = null;
		if (resourceJavaField != null) {
			try {
				resourceKey = new EnumConstantResourceKey((Enum<? extends ModelResource>) resourceJavaField.getFieldReflectionProxy().getReification().get(null));
			} catch (IllegalAccessException iae) {
				throw new RuntimeException(iae);
			}
		} else {
			resourceKey = new ClassResourceKey((Class<? extends ModelResource>) resourceJavaType.getClassReflectionProxy().getReification());
		}
		return resourceKey;
	}
}
