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
package org.alice.stageide.modelresource;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.formatter.Formatter;
import org.alice.ide.typemanager.TypeManager;
import org.alice.stageide.icons.IconFactoryManager;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resourceutilities.StorytellingResourcesTreeUtils;

import javax.swing.JComponent;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public final class EnumConstantResourceKey extends InstanceCreatorKey {

	private final Enum<? extends ModelResource> enumConstant;


	public EnumConstantResourceKey( Enum<? extends ModelResource> enumConstant ) {
		this.enumConstant = enumConstant;
	}

	public Enum<? extends ModelResource> getEnumConstant() {
		return this.enumConstant;
	}

	@Override
	public Class<? extends ModelResource> getModelResourceCls() {
		return this.enumConstant.getDeclaringClass();
	}

	public JavaField getField() {
		try {
			return JavaField.getInstance( this.enumConstant.getClass().getField( this.enumConstant.name() ) );
		} catch( NoSuchFieldException nsfe ) {
			throw new RuntimeException( nsfe );
		}
	}

	@Override
	public String getInternalText() {
		return AliceResourceUtilties.getModelClassName( enumConstant.getDeclaringClass(), enumConstant.name(), null );
	}

	@Override
	public String getSearchText() {
		return AliceResourceUtilties.getModelClassName( enumConstant.getDeclaringClass(), enumConstant.name(), JComponent.getDefaultLocale() );
	}

	@Override
	public String getLocalizedDisplayText() {
		String simpleName = AliceResourceUtilties.getModelClassName( enumConstant.getDeclaringClass(), enumConstant.name(), JComponent.getDefaultLocale() );
		String params = this.enumConstant.getDeclaringClass().getEnumConstants().length > 1 ? this.enumConstant.name() : "";

		Formatter formatter = FormatterState.getInstance().getValue();
		return String.format(formatter.getNewFormat(), simpleName, params);
	}

	@Override
	public IconFactory getIconFactory() {
		return IconFactoryManager.getIconFactoryForResourceInstance( (ModelResource)this.enumConstant );
	}

	@Override
	public InstanceCreation createInstanceCreation() {
		JavaField argumentField = this.getField();

		JavaType abstractionType = getAbstractionTypeForResourceType( JavaType.getInstance( this.enumConstant.getClass() ) );
		if( abstractionType != null ) {
			NamedUserType userType = TypeManager.getNamedUserTypeFromArgumentField( abstractionType, argumentField );
			NamedUserConstructor constructor = userType.getDeclaredConstructors().get( 0 );
			Expression[] argumentExpressions;
			if( constructor.getRequiredParameters().size() == 1 ) {
				argumentExpressions = new Expression[] { AstUtilities.createStaticFieldAccess( argumentField )
				};
			} else {
				argumentExpressions = new Expression[] {};
			}
			return AstUtilities.createInstanceCreation( constructor, argumentExpressions );
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
		return AliceResourceUtilties.getTags( enumConstant.getDeclaringClass(), enumConstant.name(), JComponent.getDefaultLocale() );
	}

	@Override
	public String[] getGroupTags() {
		return AliceResourceUtilties.getGroupTags( enumConstant.getDeclaringClass(), enumConstant.name(), JComponent.getDefaultLocale() );
	}

	@Override
	public String[] getThemeTags() {
		return AliceResourceUtilties.getThemeTags( enumConstant.getDeclaringClass(), enumConstant.name(), JComponent.getDefaultLocale() );
	}

	@Override public AxisAlignedBox getBoundingBox() {
		return AliceResourceUtilties.getBoundingBox( enumConstant.getDeclaringClass(), enumConstant.name() );
	}

	@Override public boolean getPlaceOnGround() {
		return AliceResourceUtilties.getPlaceOnGround( enumConstant.getDeclaringClass(), enumConstant.name() );
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
