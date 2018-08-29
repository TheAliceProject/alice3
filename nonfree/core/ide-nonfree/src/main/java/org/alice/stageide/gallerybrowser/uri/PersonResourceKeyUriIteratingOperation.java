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
package org.alice.stageide.gallerybrowser.uri;

import org.alice.stageide.ast.declaration.AddPersonResourceManagedFieldComposite;
import org.alice.stageide.modelresource.PersonResourceKey;
import org.alice.stageide.personresource.PersonResourceComposite;
import org.lgna.croquet.Model;
import org.lgna.croquet.ValueCreator;
import org.lgna.croquet.history.Step;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.story.resources.sims2.AdultPersonResource;
import org.lgna.story.resources.sims2.ChildPersonResource;
import org.lgna.story.resources.sims2.ElderPersonResource;
import org.lgna.story.resources.sims2.LifeStage;
import org.lgna.story.resources.sims2.TeenPersonResource;
import org.lgna.story.resources.sims2.ToddlerPersonResource;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class PersonResourceKeyUriIteratingOperation extends ResourceKeyUriIteratingOperation {
	private static class SingletonHolder {
		private static PersonResourceKeyUriIteratingOperation instance = new PersonResourceKeyUriIteratingOperation();
	}

	public static PersonResourceKeyUriIteratingOperation getInstance() {
		return SingletonHolder.instance;
	}

	private PersonResourceKeyUriIteratingOperation() {
		super( UUID.fromString( "587d7ef6-a811-43ff-9e73-7fd4bb198ce6" ) );
	}

	@Override
	protected int getStepCount() {
		return 3;
	}

	@Override
	protected Model getNext( List<UserActivity> subSteps, Iterator<Model> iteratingData ) {
		PersonResourceKey personResourceKey = (PersonResourceKey)this.resourceKey;
		switch( subSteps.size() ) {
		case 0:
			Class<?> resourceCls = personResourceKey.getModelResourceCls();
			LifeStage lifeStage;
			if( ToddlerPersonResource.class.isAssignableFrom( resourceCls ) ) {
				lifeStage = LifeStage.TODDLER;
			} else if( ChildPersonResource.class.isAssignableFrom( resourceCls ) ) {
				lifeStage = LifeStage.CHILD;
			} else if( TeenPersonResource.class.isAssignableFrom( resourceCls ) ) {
				lifeStage = LifeStage.TEEN;
			} else if( AdultPersonResource.class.isAssignableFrom( resourceCls ) ) {
				lifeStage = LifeStage.ADULT;
			} else if( ElderPersonResource.class.isAssignableFrom( resourceCls ) ) {
				lifeStage = LifeStage.ELDER;
			} else {
				lifeStage = null;
			}

			PersonResourceComposite personResourceComposite = PersonResourceComposite.getInstance();
			if( lifeStage != null ) {
				personResourceComposite.EPIC_HACK_disableLifeStageStateOneTime();
			}
			return personResourceComposite.getRandomPersonExpressionValueConverter( lifeStage );
		case 1:
			UserActivity prevSubStep = subSteps.get( 0 );
			if( prevSubStep.getProducedValue() != null ) {
				Object value = prevSubStep.getProducedValue();
				if( value instanceof InstanceCreation ) {
					InstanceCreation instanceCreation = (InstanceCreation)value;
					AddPersonResourceManagedFieldComposite addPersonResourceManagedFieldComposite = AddPersonResourceManagedFieldComposite.getInstance();
					addPersonResourceManagedFieldComposite.setInitialPersonResourceInstanceCreation( instanceCreation );
					return addPersonResourceManagedFieldComposite.getLaunchOperation();
				} else {
					return null;
				}
			} else {
				return null;
			}
		case 2:
			return this.getMergeTypeOperation();
		default:
			return null;
		}
	}
}
