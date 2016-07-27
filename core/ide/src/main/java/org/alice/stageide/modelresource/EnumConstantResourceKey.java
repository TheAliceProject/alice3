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
package org.alice.stageide.modelresource;

/**
 * @author Dennis Cosgrove
 */
public final class EnumConstantResourceKey extends InstanceCreatorKey {
	private static java.util.Map<org.lgna.project.ast.JavaType, org.lgna.project.ast.JavaType> mapResourceTypeToAbstractionType;

	private final Enum<? extends org.lgna.story.resources.ModelResource> enumConstant;

	public static org.lgna.project.ast.JavaType getAbstractionTypeForResourceType( org.lgna.project.ast.AbstractType<?, ?, ?> assignableFromResourceType ) {
		if( mapResourceTypeToAbstractionType != null ) {
			//pass
		} else {
			mapResourceTypeToAbstractionType = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			java.util.List<org.lgna.project.ast.JavaType> abstractionTypes = org.lgna.story.resourceutilities.StorytellingResourcesTreeUtils.INSTANCE.getTopLevelGalleryTypes();
			for( org.lgna.project.ast.JavaType abstractionType : abstractionTypes ) {
				org.lgna.project.ast.JavaType resourceType = (org.lgna.project.ast.JavaType)abstractionType.getDeclaredConstructors().get( 0 ).getRequiredParameters().get( 0 ).getValueType();
				mapResourceTypeToAbstractionType.put( resourceType, abstractionType );
			}
		}
		org.lgna.project.ast.JavaType abstractionType = null;
		for( org.lgna.project.ast.JavaType resourceType : mapResourceTypeToAbstractionType.keySet() ) {
			if( resourceType.isAssignableFrom( assignableFromResourceType ) ) {
				abstractionType = mapResourceTypeToAbstractionType.get( resourceType );
				break;
			}
		}
		return abstractionType;
	}

	public EnumConstantResourceKey( Enum<? extends org.lgna.story.resources.ModelResource> enumConstant ) {
		this.enumConstant = enumConstant;
	}

	public Enum<? extends org.lgna.story.resources.ModelResource> getEnumConstant() {
		return this.enumConstant;
	}

	@Override
	public Class<? extends org.lgna.story.resources.ModelResource> getModelResourceCls() {
		return this.enumConstant.getDeclaringClass();
	}

	public org.lgna.project.ast.JavaField getField() {
		try {
			return org.lgna.project.ast.JavaField.getInstance( this.enumConstant.getClass().getField( this.enumConstant.name() ) );
		} catch( NoSuchFieldException nsfe ) {
			throw new RuntimeException( nsfe );
		}
	}

	@Override
	public String getInternalText() {
		return IdeAliceResourceUtilities.getModelClassName( this, null );
	}

	@Override
	public String getSearchText() {
		return IdeAliceResourceUtilities.getModelClassName( this, javax.swing.JComponent.getDefaultLocale() );
	}

	@Override
	public String getLocalizedDisplayText() {
		String simpleName = IdeAliceResourceUtilities.getModelClassName( this, javax.swing.JComponent.getDefaultLocale() );
		StringBuilder sb = new StringBuilder();

		//TODO: Localize
		sb.append( "new " );
		sb.append( simpleName );
		if( this.enumConstant.getDeclaringClass().getEnumConstants().length > 1 ) {
			sb.append( "( " );
			sb.append( this.enumConstant.name() );
			sb.append( " )" );
		} else {
			sb.append( "()" );
		}
		return sb.toString();
	}

	@Override
	public org.lgna.croquet.icon.IconFactory getIconFactory() {
		return org.alice.stageide.icons.IconFactoryManager.getIconFactoryForResourceInstance( (org.lgna.story.resources.ModelResource)this.enumConstant );
	}

	@Override
	public org.lgna.project.ast.InstanceCreation createInstanceCreation() {
		org.lgna.project.ast.JavaField argumentField = this.getField();

		org.lgna.project.ast.JavaType abstractionType = getAbstractionTypeForResourceType( org.lgna.project.ast.JavaType.getInstance( this.enumConstant.getClass() ) );
		if( abstractionType != null ) {
			org.lgna.project.ast.NamedUserType userType = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromArgumentField( abstractionType, argumentField );
			org.lgna.project.ast.NamedUserConstructor constructor = userType.getDeclaredConstructors().get( 0 );
			org.lgna.project.ast.Expression[] argumentExpressions;
			if( constructor.getRequiredParameters().size() == 1 ) {
				argumentExpressions = new org.lgna.project.ast.Expression[] { org.lgna.project.ast.AstUtilities.createStaticFieldAccess( argumentField )
				};
			} else {
				argumentExpressions = new org.lgna.project.ast.Expression[] {};
			}
			return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, argumentExpressions );
		} else {
			return null;
		}
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public String[] getTags() {
		return IdeAliceResourceUtilities.getTags( this, javax.swing.JComponent.getDefaultLocale() );
	}

	@Override
	public String[] getGroupTags() {
		return IdeAliceResourceUtilities.getGroupTags( this, javax.swing.JComponent.getDefaultLocale() );
	}

	@Override
	public String[] getThemeTags() {
		return IdeAliceResourceUtilities.getThemeTags( this, javax.swing.JComponent.getDefaultLocale() );
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) {
			return true;
		}
		if( o instanceof EnumConstantResourceKey ) {
			EnumConstantResourceKey other = (EnumConstantResourceKey)o;
			return this.enumConstant == other.enumConstant;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.enumConstant.hashCode();
	}

	@Override
	protected void appendRep( StringBuilder sb ) {
		sb.append( this.enumConstant );
	}
}
