package org.lgna.story.resourceutilities;

public class ModelClassData extends BaseModelClassData
{
	public final Class superClass;
	public final String packageString;
	
	public ModelClassData(Class superClass, String packageString, BaseModelClassData baseData )
	{
		super(baseData);
		this.superClass = superClass;
		this.packageString = packageString;
	}
	
	//Alice Definitions
	public static final BaseModelClassData BIPED_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Biped.class, org.lgna.story.implementation.BipedImplementation.class, org.lgna.story.resources.alice.BipedImplementationFactory.class);
	public static final BaseModelClassData SWIMMER_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Swimmer.class, org.lgna.story.implementation.SwimmerImplementation.class, org.lgna.story.resources.alice.SwimmerImplementationFactory.class);

	public static final ModelClassData PERSON_CLASS_DATA = new ModelClassData(org.lgna.story.resources.PersonResource.class, "org.lgna.story.resources.people", BIPED_BASE_CLASS_DATA);
	public static final ModelClassData FISH_CLASS_DATA = new ModelClassData(org.lgna.story.resources.FishResource.class, "org.lgna.story.resources.fish", SWIMMER_BASE_CLASS_DATA);
	public static final ModelClassData WHALE_CLASS_DATA = new ModelClassData(org.lgna.story.resources.WhaleResource.class, "org.lgna.story.resources.whale", SWIMMER_BASE_CLASS_DATA);
	
	//Sims Definitions
	public static final BaseModelClassData SIMS_VEHICLE_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Vehicle.class, org.lgna.story.implementation.VehicleImplementation.class, org.lgna.story.resources.sims2.VehicleImplementationFactory.class);
	
	public static final ModelClassData SIMS_VEHICLE_CLASS_DATA = new ModelClassData(org.lgna.story.resources.VehicleResource.class, "org.lgna.story.resources.vehicle", SIMS_VEHICLE_BASE_CLASS_DATA);
	
}