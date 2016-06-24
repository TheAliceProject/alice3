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
package org.alice.nonfree;

import java.util.List;
import java.util.Map;

import org.alice.ide.croquet.models.StandardExpressionState;
import org.alice.ide.croquet.models.declaration.GalleryPersonResourceFillIn;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.alice.stageide.SimsStoryApiConfigurationManager;
import org.alice.stageide.StoryApiConfigurationManager;
import org.alice.stageide.ast.ExpressionCreator;
import org.alice.stageide.ast.SimsBootstrapUtilties;
import org.alice.stageide.ast.SimsExpressionCreator;
import org.alice.stageide.ast.sort.SimsOneShotSorter;
import org.alice.stageide.cascade.ExpressionCascadeManager;
import org.alice.stageide.cascade.SimsExpressionCascadeManager;
import org.alice.stageide.gallerybrowser.uri.PersonResourceKeyUriIteratingOperation;
import org.alice.stageide.gallerybrowser.uri.ResourceKeyUriIteratingOperation;
import org.alice.stageide.icons.SimsIconFactoryManager;
import org.alice.stageide.modelresource.ClassHierarchyBasedResourceNode;
import org.alice.stageide.modelresource.InstanceCreatorKey;
import org.alice.stageide.modelresource.PersonResourceKey;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.oneshot.SetCeilingPaintMethodInvocationFillIn;
import org.alice.stageide.oneshot.SetFloorPaintMethodInvocationFillIn;
import org.alice.stageide.oneshot.SetWallPaintMethodInvocationFillIn;
import org.alice.stageide.openprojectpane.models.TemplateUriState;
import org.alice.stageide.properties.RoomCeilingPaintPropertyAdapter;
import org.alice.stageide.properties.RoomFloorPaintPropertyAdapter;
import org.alice.stageide.properties.RoomWallPaintPropertyAdapter;
import org.lgna.croquet.Model;
import org.lgna.croquet.Operation;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaMethod;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.RoomImp;
import org.lgna.story.resources.ModelResource;

import edu.cmu.cs.dennisc.eula.LicenseRejectedException;

/**
 * @author Kyle J. Harms
 */
public class IdeNonfree extends NebulousIde {

	private static final org.lgna.project.ast.JavaType PERSON_RESOURCE_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.resources.sims2.PersonResource.class );

	@Override
	public boolean isNonFreeEnabled() {
		return true;
	}

	@Override
	public void promptForLicenseAgreements( String licenseKey ) throws LicenseRejectedException {
		edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.nebulous.License.class, licenseKey, "License Agreement (Part 2 of 2): The Sims (TM) 2 Art Assets", edu.cmu.cs.dennisc.nebulous.License.TEXT,
				"The Sims (TM) 2 Art Assets" );
	}

	@Override
	public Operation newSimsArtEulaDialogLaunchOperation() {
		return org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createNoArgumentConstructorInstance(
				org.alice.stageide.about.SimsArtAssetsEulaComposite.class,
				org.lgna.croquet.Application.INFORMATION_GROUP ).getLaunchOperation();
	}

	@Override
	public org.lgna.project.ast.NamedUserType createProgramType( TemplateUriState.Template template ) {
		return SimsBootstrapUtilties.createProgramType( template.getFloorAppearance(), template.getWallAppearance(), template.getCeilingAppearance(), template.getAtmospherColor(), template.getFogDensity(), template.getAboveLightColor(),
				template.getBelowLightColor() );
	}

	@Override
	public AbstractPropertyAdapter<?, ?> getPropertyAdapterForGetter( JavaMethod setter, StandardExpressionState state, EntityImp entityImp ) {
		if( setter.getName().equalsIgnoreCase( "setWallPaint" ) ) {
			if( entityImp instanceof RoomImp ) {
				return new RoomWallPaintPropertyAdapter( (RoomImp)entityImp, state );
			}
		} else if( setter.getName().equalsIgnoreCase( "setFloorPaint" ) ) {
			if( entityImp instanceof RoomImp ) {
				return new RoomFloorPaintPropertyAdapter( (RoomImp)entityImp, state );
			}
		} else if( setter.getName().equalsIgnoreCase( "setCeilingPaint" ) ) {
			if( entityImp instanceof RoomImp ) {
				return new RoomCeilingPaintPropertyAdapter( (RoomImp)entityImp, state );
			}
		}
		return null;
	}

	@Override
	public void addBipedResourceResourceNodes( List<ResourceNode> childNodes, List<ResourceNode> emptyList ) {
		childNodes.add( new ClassHierarchyBasedResourceNode( PersonResourceKey.getElderInstance(), emptyList ) );
		childNodes.add( new ClassHierarchyBasedResourceNode( PersonResourceKey.getAdultInstance(), emptyList ) );
		childNodes.add( new ClassHierarchyBasedResourceNode( PersonResourceKey.getTeenInstance(), emptyList ) );
		childNodes.add( new ClassHierarchyBasedResourceNode( PersonResourceKey.getChildInstance(), emptyList ) );
		childNodes.add( new ClassHierarchyBasedResourceNode( PersonResourceKey.getToddlerInstance(), emptyList ) );
	}

	@Override
	public void unloadNebulousModelData() {
		edu.cmu.cs.dennisc.nebulous.Manager.unloadNebulousModelData();
	}

	@Override
	public void unloadPerson() {
		org.alice.stageide.personresource.PersonResourceComposite.getInstance().getPreviewComposite().unloadPerson();
	}

	@Override
	public IconFactory createIconFactory( ModelResource instance ) {
		return SimsIconFactoryManager.createIconFactory( instance );
	}

	@Override
	public double setOneShotSortValues( Map<JavaMethod, Double> map, double value, double INCREMENT ) {
		return super.setOneShotSortValues( map, value, INCREMENT );
	}

	@Override
	public boolean isInstanceOfPersonResourceKey( ResourceKey resourceKey ) {
		return ( resourceKey instanceof PersonResourceKey );
	}

	@Override
	public Model getPersonResourceDropModel( ResourceKey resourceKey ) {
		if( resourceKey instanceof PersonResourceKey ) {
			PersonResourceKey personResourceKey = (PersonResourceKey)resourceKey;
			return org.alice.stageide.croquet.models.gallerybrowser.DeclareFieldFromPersonResourceIteratingOperation.getInstanceForLifeStage( personResourceKey.getLifeStage() );
			//todo
			//		if( ( this.resourceKey instanceof EnumConstantResourceKey ) || ( this.resourceKey instanceof PersonResourceKey ) ) {
			//			return new org.alice.stageide.ast.declaration.AddResourceKeyManagedFieldComposite( this.resourceKey ).getOperation();
		} else {
			return null;
		}
	}

	@Override
	public void addRoomMethods( org.lgna.project.ast.AbstractType<?, ?, ?> instanceFactoryValueType, java.util.List<org.lgna.project.ast.JavaMethod> methods ) {
		org.lgna.project.ast.JavaType roomType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SRoom.class );
		if( roomType.isAssignableFrom( instanceFactoryValueType ) ) {
			methods.add( SimsOneShotSorter.ROOM_SET_CEILING_PAINT_METHOD );
			methods.add( SimsOneShotSorter.ROOM_SET_WALL_PAINT_METHOD );
			methods.add( SimsOneShotSorter.ROOM_SET_FLOOR_PAINT_METHOD );
			methods.add( SimsOneShotSorter.ROOM_SET_OPACITY_METHOD );
		}
	}

	@Override
	public org.lgna.croquet.CascadeBlankChild<?> getRoomFillIns( JavaMethod method, InstanceFactory instanceFactory ) {
		if( "setCeilingPaint".equals( method.getName() ) ) {
			return SetCeilingPaintMethodInvocationFillIn.getInstance( instanceFactory, method );
		} else if( "setWallPaint".equals( method.getName() ) ) {
			return SetWallPaintMethodInvocationFillIn.getInstance( instanceFactory, method );
		} else if( "setFloorPaint".equals( method.getName() ) ) {
			return SetFloorPaintMethodInvocationFillIn.getInstance( instanceFactory, method );
		} else {
			return null;
		}
	}

	@Override
	public ResourceKeyUriIteratingOperation getPersonResourceKeyUriIteratingOperation() {
		return PersonResourceKeyUriIteratingOperation.getInstance();
	}

	@Override
	public ExpressionCascadeManager newExpressionCascadeManager() {
		return new SimsExpressionCascadeManager();
	}

	@Override
	public StoryApiConfigurationManager newStoryApiConfigurationManager() {
		return new SimsStoryApiConfigurationManager();
	}

	@Override
	public boolean isAssignableToPersonResource( AbstractType<?, ?, ?> type ) {
		return ( type.isAssignableTo( org.lgna.story.resources.sims2.PersonResource.class ) );
	}

	@Override
	public org.lgna.story.Paint getFloorApperanceRedwood() {
		return org.lgna.story.SRoom.FloorAppearance.REDWOOD;
	}

	@Override
	public org.lgna.story.Paint getWallApperanceYellow() {
		return org.lgna.story.SRoom.WallAppearance.YELLOW;
	}

	@Override
	public org.lgna.croquet.CascadeBlankChild<?> getGalleryPersonResourceFillInInstance( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return GalleryPersonResourceFillIn.getInstance( type );
	}

	@Override
	public boolean isPersonResourceAssignableFrom( Class<?> cls ) {
		return ( org.lgna.story.resources.sims2.PersonResource.class.isAssignableFrom( cls ) );
	}

	@Override
	public InstanceCreatorKey getPersonResourceKeyInstanceForResourceClass( Class<? extends org.lgna.story.resources.ModelResource> resourceCls ) {
		return org.alice.stageide.modelresource.PersonResourceKey.getInstanceForResourceClass( resourceCls );
	}

	@Override
	public ExpressionCreator newExpressionCreator() {
		return new SimsExpressionCreator();
	}

	@Override
	public boolean isPersonResourceTypeAssingleFrom( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return PERSON_RESOURCE_TYPE.isAssignableFrom( type );
	}
}
