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
	
	public static final BaseModelClassData PROP_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.JointedModel.class, org.lgna.story.implementation.BasicJointedModelImp.class);
	public static final BaseModelClassData BIPED_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Biped.class, org.lgna.story.implementation.BipedImp.class);
	public static final BaseModelClassData SWIMMER_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Swimmer.class, org.lgna.story.implementation.SwimmerImp.class);
	public static final BaseModelClassData FLYER_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Flyer.class, org.lgna.story.implementation.FlyerImp.class);
	public static final BaseModelClassData QUADRUPED_BASE_CLASS_DATA = new BaseModelClassData(org.lgna.story.Quadruped.class, org.lgna.story.implementation.QuadrupedImp.class);
	
	
	public static final ModelClassData PERSON_CLASS_DATA = new ModelClassData(org.lgna.story.resources.PersonResource.class, "org.lgna.story.resources.people", BIPED_BASE_CLASS_DATA);
	public static final ModelClassData CHARACTER_CLASS_DATA = new ModelClassData(org.lgna.story.resources.CharacterResource.class, "org.lgna.story.resources.character", BIPED_BASE_CLASS_DATA);
	public static final ModelClassData MONSTER_CLASS_DATA = new ModelClassData(org.lgna.story.resources.MonsterResource.class, "org.lgna.story.resources.monster", BIPED_BASE_CLASS_DATA);
	public static final ModelClassData BIRD_CLASS_DATA = new ModelClassData(org.lgna.story.resources.BirdResource.class, "org.lgna.story.resources.bird", FLYER_BASE_CLASS_DATA);
	public static final ModelClassData FISH_CLASS_DATA = new ModelClassData(org.lgna.story.resources.FishResource.class, "org.lgna.story.resources.fish", SWIMMER_BASE_CLASS_DATA);
	public static final ModelClassData WHALE_CLASS_DATA = new ModelClassData(org.lgna.story.resources.WhaleResource.class, "org.lgna.story.resources.whale", SWIMMER_BASE_CLASS_DATA);
	public static final ModelClassData PROP_CLASS_DATA = new ModelClassData(org.lgna.story.resources.PropResource.class, "org.lgna.story.resources.prop", PROP_BASE_CLASS_DATA);
	public static final ModelClassData CAT_CLASS_DATA = new ModelClassData(org.lgna.story.resources.CatResource.class, "org.lgna.story.resources.cat", QUADRUPED_BASE_CLASS_DATA);
	public static final ModelClassData DOG_CLASS_DATA = new ModelClassData(org.lgna.story.resources.DogResource.class, "org.lgna.story.resources.dog", QUADRUPED_BASE_CLASS_DATA);
	public static final ModelClassData CAMEL_CLASS_DATA = new ModelClassData(org.lgna.story.resources.CamelResource.class, "org.lgna.story.resources.camel", QUADRUPED_BASE_CLASS_DATA);
	public static final ModelClassData HORSE_CLASS_DATA = new ModelClassData(org.lgna.story.resources.HorseResource.class, "org.lgna.story.resources.horse", QUADRUPED_BASE_CLASS_DATA);
	
}