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
public class PersonResourceKey extends InstanceCreatorKey {

	private static final org.lgna.croquet.icon.IconFactory ICON_FACTORY = new org.lgna.croquet.icon.ImageIconFactory( org.alice.stageide.gallerybrowser.ResourceBasedTab.CREATE_PERSON_LARGE_ICON.getImage() );

	private static class SingletonHolder {
		private static PersonResourceKey instance = new PersonResourceKey();
	}

	public static PersonResourceKey getInstance() {
		return SingletonHolder.instance;
	}

	private PersonResourceKey() {
	}

	@Override
	public Class<? extends org.lgna.story.resources.ModelResource> getModelResourceCls() {
		return org.lgna.story.resources.sims2.PersonResource.class;
	}

	@Override
	public String getDisplayText() {
		return "Person";
	}

	@Override
	public org.lgna.croquet.icon.IconFactory getIconFactory() {
		return ICON_FACTORY;
	}

	@Override
	public org.lgna.project.ast.InstanceCreation createInstanceCreation() {
		org.lgna.story.resources.sims2.PersonResource personResource = org.alice.stageide.personresource.RandomPersonResourceComposite.getInstance().getValueCreator().fireAndGetValue( org.lgna.croquet.triggers.NullTrigger.createUserInstance() );
		org.lgna.project.ast.NamedUserType userType = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromPersonResource( personResource );
		org.lgna.project.ast.NamedUserConstructor constructor = userType.getDeclaredConstructors().get( 0 );
		try {
			org.lgna.project.ast.InstanceCreation argumentExpression = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSims2PersonRecourseInstanceCreation( personResource );
			return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, argumentExpression );
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
			throw new RuntimeException( ccee );
		}
	}

	@Override
	public String[] getTags() {
		return null;
	}

	@Override
	public String[] getGroupTags() {
		return null;
	}

	@Override
	public String[] getThemeTags() {
		return null;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	protected void appendRep( StringBuilder sb ) {
		sb.append( this.getDisplayText() );
	}
}
