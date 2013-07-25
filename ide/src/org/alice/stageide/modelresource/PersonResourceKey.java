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
	private static org.lgna.croquet.icon.IconFactory createIconFactory( String subPath ) {
		return new org.lgna.croquet.icon.TrimmedImageIconFactory( PersonResourceKey.class.getResource( "images/" + subPath + ".png" ), 160, 120 );
	}

	private static final org.lgna.croquet.icon.IconFactory ELDER_ICON_FACTORY = createIconFactory( "elder" );
	private static final org.lgna.croquet.icon.IconFactory ADULT_ICON_FACTORY = createIconFactory( "adult" );
	private static final org.lgna.croquet.icon.IconFactory TEEN_ICON_FACTORY = createIconFactory( "teen" );
	private static final org.lgna.croquet.icon.IconFactory CHILD_ICON_FACTORY = createIconFactory( "child" );
	private static final org.lgna.croquet.icon.IconFactory TODDLER_ICON_FACTORY = createIconFactory( "toddler" );

	private static class SingletonHolder {
		private static PersonResourceKey elderInstance = new PersonResourceKey( org.lgna.story.resources.sims2.LifeStage.ELDER );
		private static PersonResourceKey adultInstance = new PersonResourceKey( org.lgna.story.resources.sims2.LifeStage.ADULT );
		private static PersonResourceKey teenInstance = new PersonResourceKey( org.lgna.story.resources.sims2.LifeStage.TEEN );
		private static PersonResourceKey childInstance = new PersonResourceKey( org.lgna.story.resources.sims2.LifeStage.CHILD );
		private static PersonResourceKey toddlerInstance = new PersonResourceKey( org.lgna.story.resources.sims2.LifeStage.TODDLER );
	}

	public static PersonResourceKey getElderInstance() {
		return SingletonHolder.elderInstance;
	}

	public static PersonResourceKey getAdultInstance() {
		return SingletonHolder.adultInstance;
	}

	public static PersonResourceKey getTeenInstance() {
		return SingletonHolder.teenInstance;
	}

	public static PersonResourceKey getChildInstance() {
		return SingletonHolder.childInstance;
	}

	public static PersonResourceKey getToddlerInstance() {
		return SingletonHolder.toddlerInstance;
	}

	private final org.lgna.story.resources.sims2.LifeStage lifeStage;

	private PersonResourceKey( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		this.lifeStage = lifeStage;
	}

	public org.lgna.story.resources.sims2.LifeStage getLifeStage() {
		return this.lifeStage;
	}

	@Override
	public Class<? extends org.lgna.story.resources.ModelResource> getModelResourceCls() {
		return org.lgna.story.resources.sims2.PersonResource.class;
	}

	private org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> getPersonResourceValueCreator() {
		return org.alice.stageide.personresource.PersonResourceComposite.getInstance().getRandomPersonExpressionValueConverter( this.lifeStage );
	}

	@Override
	public String getDisplayText() {
		StringBuilder sb = new StringBuilder();
		sb.append( "new " );
		sb.append( this.lifeStage.name().charAt( 0 ) );
		sb.append( this.lifeStage.name().substring( 1 ).toLowerCase() );
		sb.append( "( ... )" );
		return sb.toString();
	}

	@Override
	public org.lgna.croquet.icon.IconFactory getIconFactory() {
		if( this.lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) {
			return ELDER_ICON_FACTORY;
		} else if( this.lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) {
			return ADULT_ICON_FACTORY;
		} else if( this.lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) {
			return TEEN_ICON_FACTORY;
		} else if( this.lifeStage == org.lgna.story.resources.sims2.LifeStage.CHILD ) {
			return CHILD_ICON_FACTORY;
		} else {
			return TODDLER_ICON_FACTORY;
		}
	}

	@Override
	public org.lgna.project.ast.InstanceCreation createInstanceCreation() {
		return this.getPersonResourceValueCreator().fireAndGetValue( org.lgna.croquet.triggers.NullTrigger.createUserInstance() );
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
