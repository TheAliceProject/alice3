package org.lgna.story.resourceutilities;


public class ModelClassData extends BaseModelClassData
{
	public final Class superClass;
	public final String packageString;
	
	public ModelClassData(Class superClass, String packageString, BaseModelClassData baseData)
	{
		super(baseData);
		this.superClass = superClass;
		this.packageString = packageString;
	}
	
	
	//Alice Definitions
	
	public static final BaseModelClassData PROP_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.JointedModel.class, org.lgna.story.implementation.BasicJointedModelImp.class, org.lgna.story.implementation.alice.JointImplementationAndVisualDataFactory.class );
	public static final BaseModelClassData BIPED_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Biped.class, org.lgna.story.implementation.BipedImp.class, org.lgna.story.implementation.alice.JointImplementationAndVisualDataFactory.class );
	public static final BaseModelClassData SWIMMER_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Swimmer.class, org.lgna.story.implementation.SwimmerImp.class, org.lgna.story.implementation.alice.JointImplementationAndVisualDataFactory.class );

	public static final ModelClassData PERSON_CLASS_DATA = new ModelClassData(org.lgna.story.resources.PersonResource.class, "org.lgna.story.resources.people", BIPED_BASE_CLASS_DATA);
	public static final ModelClassData FISH_CLASS_DATA = new ModelClassData(org.lgna.story.resources.FishResource.class, "org.lgna.story.resources.fish", SWIMMER_BASE_CLASS_DATA);
	public static final ModelClassData WHALE_CLASS_DATA = new ModelClassData(org.lgna.story.resources.WhaleResource.class, "org.lgna.story.resources.whale", SWIMMER_BASE_CLASS_DATA);
	public static final ModelClassData PROP_CLASS_DATA = new ModelClassData(org.lgna.story.resources.PropResource.class, "org.lgna.story.resources.prop", PROP_BASE_CLASS_DATA);
	
	//Sims Definitions
	public static final BaseModelClassData SIMS_PROP_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Prop.class, org.lgna.story.implementation.BasicJointedModelImp.class, org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory.class );

	public static final ModelClassData SIMS_PROP_CLASS_DATA = new ModelClassData(org.lgna.story.resources.PropResource.class, "org.lgna.story.resources.prop", SIMS_PROP_BASE_CLASS_DATA);
	
}