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

public class ModelClassData extends BaseModelClassData
{
	public final Class superClass;
	public final String packageString;

	public ModelClassData( Class superClass, String packageString, BaseModelClassData baseData )
	{
		super( baseData );
		this.superClass = superClass;
		this.packageString = packageString;
	}

	//Alice Definitions

	public static final BaseModelClassData PROP_BASE_CLASS_DATA = new BaseModelClassData( org.lgna.story.SJointedModel.class, org.lgna.story.implementation.BasicJointedModelImp.class );
	public static final BaseModelClassData BIPED_BASE_CLASS_DATA = new BaseModelClassData( org.lgna.story.SBiped.class, org.lgna.story.implementation.BipedImp.class );
	public static final BaseModelClassData SWIMMER_BASE_CLASS_DATA = new BaseModelClassData( org.lgna.story.SSwimmer.class, org.lgna.story.implementation.SwimmerImp.class );
	public static final BaseModelClassData FLYER_BASE_CLASS_DATA = new BaseModelClassData( org.lgna.story.SFlyer.class, org.lgna.story.implementation.FlyerImp.class );
	public static final BaseModelClassData QUADRUPED_BASE_CLASS_DATA = new BaseModelClassData( org.lgna.story.SQuadruped.class, org.lgna.story.implementation.QuadrupedImp.class );
	public static final BaseModelClassData VEHICLE_BASE_CLASS_DATA = new BaseModelClassData( org.lgna.story.STransport.class, org.lgna.story.implementation.TransportImp.class );

	public static final ModelClassData BIPED_CLASS_DATA = new ModelClassData( org.lgna.story.resources.BipedResource.class, "org.lgna.story.resources.biped", BIPED_BASE_CLASS_DATA );
	public static final ModelClassData FLYER_CLASS_DATA = new ModelClassData( org.lgna.story.resources.FlyerResource.class, "org.lgna.story.resources.flyer", FLYER_BASE_CLASS_DATA );
	public static final ModelClassData QUADRUPED_CLASS_DATA = new ModelClassData( org.lgna.story.resources.QuadrupedResource.class, "org.lgna.story.resources.quadruped", QUADRUPED_BASE_CLASS_DATA );
	public static final ModelClassData SWIMMER_CLASS_DATA = new ModelClassData( org.lgna.story.resources.SwimmerResource.class, "org.lgna.story.resources.swimmer", SWIMMER_BASE_CLASS_DATA );
	public static final ModelClassData FISH_CLASS_DATA = new ModelClassData( org.lgna.story.resources.FishResource.class, "org.lgna.story.resources.fish", SWIMMER_BASE_CLASS_DATA );
	public static final ModelClassData MARINE_MAMMAL_CLASS_DATA = new ModelClassData( org.lgna.story.resources.MarineMammalResource.class, "org.lgna.story.resources.marinemammal", SWIMMER_BASE_CLASS_DATA );
	public static final ModelClassData PROP_CLASS_DATA = new ModelClassData( org.lgna.story.resources.PropResource.class, "org.lgna.story.resources.prop", PROP_BASE_CLASS_DATA );
	public static final ModelClassData VEHICLE_CLASS_DATA = new ModelClassData( org.lgna.story.resources.TransportResource.class, "org.lgna.story.resources.transport", VEHICLE_BASE_CLASS_DATA );
	public static final ModelClassData AUTOMOBILE_CLASS_DATA = new ModelClassData( org.lgna.story.resources.AutomobileResource.class, "org.lgna.story.resources.automobile", VEHICLE_BASE_CLASS_DATA );
	public static final ModelClassData AIRCRAFT_CLASS_DATA = new ModelClassData( org.lgna.story.resources.AircraftResource.class, "org.lgna.story.resources.aircraft", VEHICLE_BASE_CLASS_DATA );
	public static final ModelClassData WATERCRAFT_CLASS_DATA = new ModelClassData( org.lgna.story.resources.WatercraftResource.class, "org.lgna.story.resources.watercraft", VEHICLE_BASE_CLASS_DATA );
	public static final ModelClassData TRAIN_CLASS_DATA = new ModelClassData( org.lgna.story.resources.TrainResource.class, "org.lgna.story.resources.train", VEHICLE_BASE_CLASS_DATA );

}
