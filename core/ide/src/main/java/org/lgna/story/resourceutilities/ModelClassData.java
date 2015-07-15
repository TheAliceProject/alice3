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
	public static final BaseModelClassData SLITHERER_BASE_CLASS_DATA = new BaseModelClassData( org.lgna.story.SSlitherer.class, org.lgna.story.implementation.SlithererImp.class );

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
	public static final ModelClassData SLITHERER_CLASS_DATA = new ModelClassData( org.lgna.story.resources.SlithererResource.class, "org.lgna.story.resources.slitherer", SLITHERER_BASE_CLASS_DATA );

}
