
public class CheckForScaledSpace {
	public static void main(String[] args) throws Exception {
		java.util.Set< String > doNotExistSet = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( 
//				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.pets.TeddyBear",
//				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.aquarium.OceanFloor"
		);
		java.util.Set< String > scaledSpaceSet = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( 
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.classroom.School",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.factory.Factory",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.factory.FactoryMachine",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.forest.Scenery",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.graveyard.SkeletonArm",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.kennel.Banana_Peel",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.lair.Computer",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.lair.Scenery",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.pigsvillage.LogHouse",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.schoolhallway.School",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.skatepark.KristensSkateboard",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.skatepark.MellysSkateboard",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.skatepark.PhilipsSkateboard",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.skatepark.SceneryAndTricks",
				"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.skatepark.TrevorsSkateboard",
				"org.alice.apis.moveandturn.gallery.amusementpark.Bottlethrow",
				"org.alice.apis.moveandturn.gallery.amusementpark.Whackamole",
				"org.alice.apis.moveandturn.gallery.animals.Flamingo",
				"org.alice.apis.moveandturn.gallery.animals.Rabbit",
				"org.alice.apis.moveandturn.gallery.animals.SleepingCat",
				"org.alice.apis.moveandturn.gallery.animals.bugs.Bee2",
				"org.alice.apis.moveandturn.gallery.animals.bugs.Catepillar",
				"org.alice.apis.moveandturn.gallery.animals.bugs.Mantis",
				"org.alice.apis.moveandturn.gallery.animals.dinosaurs.Mosasaur",
				"org.alice.apis.moveandturn.gallery.buildings.Airport",
				"org.alice.apis.moveandturn.gallery.buildings.ControlTower",
				"org.alice.apis.moveandturn.gallery.buildings.Shack",
				"org.alice.apis.moveandturn.gallery.controls.Throttle",
				"org.alice.apis.moveandturn.gallery.controls.TwoButtonSwitch",
				"org.alice.apis.moveandturn.gallery.environments.CartoonDesert",
				"org.alice.apis.moveandturn.gallery.environments.Hedgemaze",
				"org.alice.apis.moveandturn.gallery.environments.Lake",
				"org.alice.apis.moveandturn.gallery.environments.Oasis",
				"org.alice.apis.moveandturn.gallery.fantasy.DeadFlyer",
				"org.alice.apis.moveandturn.gallery.fantasy.faeries.DuckPrince",
				"org.alice.apis.moveandturn.gallery.fantasy.faeries.ToadStool",
				"org.alice.apis.moveandturn.gallery.farm.Barn",
				"org.alice.apis.moveandturn.gallery.furniture.Coatrack2",
				"org.alice.apis.moveandturn.gallery.furniture.Door",
				"org.alice.apis.moveandturn.gallery.furniture.ToyBox",
				"org.alice.apis.moveandturn.gallery.furniture.ToyboxBox",
				"org.alice.apis.moveandturn.gallery.hawaii.Konana",
				"org.alice.apis.moveandturn.gallery.hawaii.animals.Gecko",
				"org.alice.apis.moveandturn.gallery.hawaii.animals.Rat",
				"org.alice.apis.moveandturn.gallery.hawaii.people.ChiefessHeaddress",
				"org.alice.apis.moveandturn.gallery.hawaii.transportation.CanoeSingle",
				"org.alice.apis.moveandturn.gallery.hawaii.transportation.HoluaSled",
				"org.alice.apis.moveandturn.gallery.highschool.School",
				"org.alice.apis.moveandturn.gallery.highschool.studentsandteachers.Coach",
				"org.alice.apis.moveandturn.gallery.highschool.studentsandteachers.Student1",
				"org.alice.apis.moveandturn.gallery.highschool.studentsandteachers.Teacher",
				"org.alice.apis.moveandturn.gallery.highschool.studentsandteachers.prom.CheerProm",
				"org.alice.apis.moveandturn.gallery.highschool.studentsandteachers.prom.GirlProm",
				"org.alice.apis.moveandturn.gallery.highschool.studentsandteachers.prom.NerdProm",
				"org.alice.apis.moveandturn.gallery.holidays.birthday.Cake",
				"org.alice.apis.moveandturn.gallery.holidays.christmas.ChristmasTree",
				"org.alice.apis.moveandturn.gallery.holidays.christmas.Mistletoe",
				"org.alice.apis.moveandturn.gallery.holidays.christmas.Stocking",
				"org.alice.apis.moveandturn.gallery.kitchen.food.Sushi",
				"org.alice.apis.moveandturn.gallery.medieval.Banner",
				"org.alice.apis.moveandturn.gallery.medieval.Crispy",
				"org.alice.apis.moveandturn.gallery.nature.BirchTree",
				"org.alice.apis.moveandturn.gallery.nature.Bush2",
				"org.alice.apis.moveandturn.gallery.nature.Smallcactus",
				"org.alice.apis.moveandturn.gallery.nature.Volcano",
				"org.alice.apis.moveandturn.gallery.objects.Catapult",
				"org.alice.apis.moveandturn.gallery.objects.Chandelier",
				"org.alice.apis.moveandturn.gallery.objects.ComboLock",
				"org.alice.apis.moveandturn.gallery.objects.FishFood",
				"org.alice.apis.moveandturn.gallery.objects.Flashlight2",
				"org.alice.apis.moveandturn.gallery.objects.GrandfatherClock",
				"org.alice.apis.moveandturn.gallery.objects.LeftGlove",
				"org.alice.apis.moveandturn.gallery.objects.MantleClock",
				"org.alice.apis.moveandturn.gallery.objects.MilitaryRadio",
				"org.alice.apis.moveandturn.gallery.objects.Pinata",
				"org.alice.apis.moveandturn.gallery.objects.Pocketwatch",
				"org.alice.apis.moveandturn.gallery.objects.RightGlove",
				"org.alice.apis.moveandturn.gallery.objects.Shackles",
				"org.alice.apis.moveandturn.gallery.objects.Well",
				"org.alice.apis.moveandturn.gallery.ocean.BigFish2",
				"org.alice.apis.moveandturn.gallery.ocean.Shark2",
				"org.alice.apis.moveandturn.gallery.oldwest.OldWestTerrain",
				"org.alice.apis.moveandturn.gallery.oldwest.Revolver",
				"org.alice.apis.moveandturn.gallery.oldwest.WagonWithOx",
				"org.alice.apis.moveandturn.gallery.park.Grill",
				"org.alice.apis.moveandturn.gallery.people.Bikekid1",
				"org.alice.apis.moveandturn.gallery.people.Bikekid2",
				"org.alice.apis.moveandturn.gallery.people.Bob",
				"org.alice.apis.moveandturn.gallery.people.EvilNinja",
				"org.alice.apis.moveandturn.gallery.people.LittleBrother",
				"org.alice.apis.moveandturn.gallery.people.Magician",
				"org.alice.apis.moveandturn.gallery.people.StreetGirl",
				"org.alice.apis.moveandturn.gallery.roadsandsigns.NoTrucksSign",
				"org.alice.apis.moveandturn.gallery.roadsandsigns.OneWaySign",
				"org.alice.apis.moveandturn.gallery.roadsandsigns.SpeedLimitSign",
				"org.alice.apis.moveandturn.gallery.roadsandsigns.StopHereOnRed",
				"org.alice.apis.moveandturn.gallery.scifi.Ranger",
				"org.alice.apis.moveandturn.gallery.scifi.SpaceDock",
				"org.alice.apis.moveandturn.gallery.scifi.Spaceship",
				"org.alice.apis.moveandturn.gallery.skatepark.Skateboard",
				"org.alice.apis.moveandturn.gallery.space.Lunarlander",
				"org.alice.apis.moveandturn.gallery.space.SpaceShuttle",
				"org.alice.apis.moveandturn.gallery.specialeffects.Fire",
				"org.alice.apis.moveandturn.gallery.spooky.Casket",
				"org.alice.apis.moveandturn.gallery.sports.Bowlingpins",
				"org.alice.apis.moveandturn.gallery.textbookdemos.newstufftemp.Shark",
				"org.alice.apis.moveandturn.gallery.textbookdemos.wandatemp.Airport",
				"org.alice.apis.moveandturn.gallery.textbookdemos.wandatemp.Hedgemaze",
				"org.alice.apis.moveandturn.gallery.textbookdemos.wandatemp.Lake",
				"org.alice.apis.moveandturn.gallery.textbookdemos.wandatemp.Shark",
				"org.alice.apis.moveandturn.gallery.textbookdemos.wandatemp.controltower.A2c",
				"org.alice.apis.moveandturn.gallery.vehicles.Blimp",
				"org.alice.apis.moveandturn.gallery.vehicles.Car",
				"org.alice.apis.moveandturn.gallery.vehicles.Crane",
				"org.alice.apis.moveandturn.gallery.vehicles.Jet",
				"org.alice.apis.moveandturn.gallery.vehicles.LifeBoat",
				"org.alice.apis.moveandturn.gallery.vehicles.Policecar",
				"org.alice.apis.moveandturn.gallery.vehicles.Snowmobile",
				"org.alice.apis.moveandturn.gallery.vehicles.Zamboni"
		);
		java.util.List< String > failedClsNames = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( String projectName : new String[]{ "lg_walkandtouch_gallery_generated", "rt_moveandturn_generated" } ) {
			String rootPath = System.getProperty( "user.home" ) + "/Documents/Workspace/" + projectName + "/src";
			java.io.File root = new java.io.File( rootPath );
			java.io.File[] inFiles = edu.cmu.cs.dennisc.io.FileUtilities.listDescendants( root, "java" );
			for( java.io.File inFile : inFiles ) {
				String path = inFile.getAbsolutePath();
				path = path.substring( root.getAbsolutePath().length()+1, path.length()-".java".length() );
				path = path.replace( '\\', '/' );
				final String clsName = path.replace( '/', '.' );
				if( doNotExistSet.contains( clsName ) ) {
					//pass
				} else {
					//if( scaledSpaceSet.contains( clsName ) ) {
						Class<? extends org.alice.apis.moveandturn.Transformable> cls = (Class<? extends org.alice.apis.moveandturn.Transformable>)Class.forName( clsName );
						try {
							org.alice.apis.moveandturn.Transformable transformable = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( cls );
							transformable.getSGTransformable().accept( new edu.cmu.cs.dennisc.pattern.Visitor() {
								public void visit(edu.cmu.cs.dennisc.pattern.Visitable visitable) {
									if (visitable instanceof edu.cmu.cs.dennisc.scenegraph.Transformable) {
										edu.cmu.cs.dennisc.scenegraph.Transformable transformable = (edu.cmu.cs.dennisc.scenegraph.Transformable) visitable;
										edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = transformable.localTransformation.getValue();
										if( m.orientation.isWithinReasonableEpsilonOfUnitLengthSquared() ) {
											//pass
										} else {
											edu.cmu.cs.dennisc.scenegraph.scale.ScaleUtilities.exorciseTheDemonsOfScaledSpace( transformable );
											assert transformable.localTransformation.getValue().isWithinReasonableEpsilonOfIdentity();
										}
									}
								}
							} );
						} catch( RuntimeException re ) {
							re.printStackTrace();
							failedClsNames.add( clsName );
						}
					//}
				}
			}
		}
		
		for( String failedClsName : failedClsNames ) {
			System.err.println( "\"" + failedClsName + "\"," ); 
		}
	}

}
