/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.lgna.project.migration;

/**
 * @author Dennis Cosgrove
 */
public class MigrationManager {
	public static final String NO_REPLACEMENT = null;

	private static String createMoreSpecificFieldString( String fieldName, String clsName ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "name=\"" );
		sb.append( fieldName );
		sb.append( "\"><declaringClass name=\"" );
		sb.append( clsName );
		sb.append( "\"" );
		return sb.toString();
	}
	
	private static String createPrevJointString( String prevFieldName, String packageSubName ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "name=\"" );
		sb.append( prevFieldName );
		sb.append( "\"><declaringClass name=\"org\\.lgna\\.story\\.resources." );
		sb.append( packageSubName );
		sb.append( "\\.[A-Za-z]*\"" );
		return sb.toString();
	}
	private static String createNextJointString( String prevFieldName, String clsName ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "name=\"" );
		sb.append( prevFieldName );
		sb.append( "\"><declaringClass name=\"org.lgna.story.resources." );
		sb.append( clsName );
		sb.append( "\"" );
		return sb.toString();
	}
	
	private static String createPrevBipedJointString( String prevFieldName ) {
		return createPrevJointString( prevFieldName, "biped" );
	}
	private static String createNextBipedJointString( String prevFieldName ) {
		return createNextJointString( prevFieldName, "BipedResource" );
	}
	
	private static final TextMigration[] textMigrations = {
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version("3.1.7.0.0"),
					new org.lgna.project.Version("3.1.8.0.0")),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version("3.1.8.0.0"),
					new org.lgna.project.Version("3.1.9.0.0"),

					"ARMOIRE_CLOTHING", NO_REPLACEMENT,

					"org.lgna.story.resources.armoire.ArmoireArtNouveau",
					NO_REPLACEMENT,

					"PINK_POODLE", NO_REPLACEMENT,

					"org.lgna.story.resources.quadruped.Poodle", NO_REPLACEMENT),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version("3.1.9.0.0"),
					new org.lgna.project.Version("3.1.11.0.0")),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version("3.1.11.0.0"),
					new org.lgna.project.Version("3.1.14.0.0"),

					"CAMEL", NO_REPLACEMENT,

					"org.lgna.story.resources.quadruped.Camel", NO_REPLACEMENT,

					"FALCON", NO_REPLACEMENT,

					"org.lgna.story.resources.flyer.Falcon", NO_REPLACEMENT,

					"LION", NO_REPLACEMENT,

					"org.lgna.story.resources.quadruped.Lion", NO_REPLACEMENT,

					"WOLF", NO_REPLACEMENT,

					"org.lgna.story.resources.quadruped.Wolf", NO_REPLACEMENT),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version("3.1.14.0.0"),
					new org.lgna.project.Version("3.1.15.1.0")),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version("3.1.15.1.0"),
					new org.lgna.project.Version("3.1.20.0.0"),

					"org.lgna.story.resources.dresser.DresserCentralAsian",
					"org.lgna.story.resources.prop.DresserCentralAsian",

					"org.lgna.story.resources.dresser.DresserColonial",
					"org.lgna.story.resources.prop.DresserColonial",

					"org.lgna.story.resources.dresser.DresserDesigner",
					"org.lgna.story.resources.prop.DresserDesigner"),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version("3.1.20.0.0"),
					new org.lgna.project.Version("3.1.33.0.0")),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version("3.1.33.0.0"),
					new org.lgna.project.Version("3.1.34.0.0"),

					"org.lgna.story.Program",
					"org.lgna.story.SProgram",

					"org.lgna.story.Entity",
					"org.lgna.story.SThing",

					"org.lgna.story.Ground",
					"org.lgna.story.SGround",

					"org.lgna.story.Room",
					"org.lgna.story.SRoom",

					"org.lgna.story.Scene",
					"org.lgna.story.SScene",

					"org.lgna.story.Turnable",
					"org.lgna.story.STurnable",

					"org.lgna.story.Joint",
					"org.lgna.story.SJoint",

					"org.lgna.story.MovableTurnable",
					"org.lgna.story.SMovableTurnable",

					"org.lgna.story.Axes",
					"org.lgna.story.SAxes",

					"org.lgna.story.Camera",
					"org.lgna.story.SCamera",

					"org.lgna.story.Marker",
					"org.lgna.story.SMarker",

					"org.lgna.story.BookmarkCameraMarker",
					"org.lgna.story.SCameraMarker",

					"org.lgna.story.ObjectMarker",
					"org.lgna.story.SThingMarker",

					"org.lgna.story.Model",
					"org.lgna.story.SModel",

					"org.lgna.story.Billboard",
					"org.lgna.story.SBillboard",

					"org.lgna.story.JointedModel",
					"org.lgna.story.SJointedModel",

					"org.lgna.story.Biped",
					"org.lgna.story.SBiped",

					"org.lgna.story.Flyer",
					"org.lgna.story.SFlyer",

					"org.lgna.story.Prop",
					"org.lgna.story.SProp",

					"org.lgna.story.Quadruped",
					"org.lgna.story.SQuadruped",

					"org.lgna.story.Swimmer",
					"org.lgna.story.SSwimmer",

					"org.lgna.story.Vehicle",
					"org.lgna.story.SVehicle",

					"org.lgna.story.Shape",
					"org.lgna.story.SShape",

					"org.lgna.story.Box",
					"org.lgna.story.SBox",

					"org.lgna.story.Cone",
					"org.lgna.story.SCone",

					"org.lgna.story.Cylinder",
					"org.lgna.story.SCylinder",

					"org.lgna.story.Disc",
					"org.lgna.story.SDisc",

					"org.lgna.story.Sphere",
					"org.lgna.story.SSphere",

					"org.lgna.story.Torus",
					"org.lgna.story.STorus",

					"org.lgna.story.TextModel",
					"org.lgna.story.STextModel",

					"org.lgna.story.Target",
					"org.lgna.story.STarget",

					"org.lgna.story.Sun",
					"org.lgna.story.SSun",

					"ABYSSINIAN",
					"ABYSSINIAN_CAT",

					"org.lgna.story.resources.flyer.MeanChicken",
					"org.lgna.story.resources.flyer.Chicken",

					"org.lgna.story.resources.quadruped.Dalmation",
					"org.lgna.story.resources.quadruped.Dalmatian",

					"BROWN_OGRE",
					"BROWN",

					"PAJAMA_CARDINAL",
					"PAJAMA_FISH",

					"PINK_POODLE",
					"POODLE",

					"SHORT_HAIR",
					"SHORT_HAIR_CAT",

					"ROBOT",
					"ALIEN_ROBOT",

					"ASTEROID1_GRAY",
					"BOULDER1_MOON",

					"ASTEROID1_RED",
					"BOULDER1_MARS",

					"ASTEROID1_BROWN",
					"BOULDER1_DESERT",

					"org.lgna.story.resources.prop.Boulder1",
					"org.lgna.story.resources.prop.Boulder",

					"ASTEROID2_BROWN",
					"BOULDER2_DESERT",

					"ASTEROID2_GRAY",
					"BOULDER2_MOON",

					"ASTEROID2_RED",
					"BOULDER2_MARS",

					"org.lgna.story.resources.prop.Boulder2",
					"org.lgna.story.resources.prop.Boulder",

					"ASTEROID4_BROWN",
					"BOULDER3_DESERT",

					"ASTEROID4_GRAY",
					"BOULDER3_MOON",

					"ASTEROID4_RED",
					"BOULDER3_MARS",

					"org.lgna.story.resources.prop.Boulder3",
					"org.lgna.story.resources.prop.Boulder",

					"ASTEROID5_RED",
					"BOULDER4_MARS",

					"ASTEROID5_GRAY",
					"BOULDER4_MOON",

					"ASTEROID5_BROWN",
					"BOULDER4_DESERT",

					"org.lgna.story.resources.prop.Boulder4",
					"org.lgna.story.resources.prop.Boulder",

					"ASTEROID6_BROWN",
					"BOULDER5_DESERT",

					"ASTEROID6_RED",
					"BOULDER5_MARS",

					"ASTEROID6_GRAY",
					"BOULDER5_MOON",

					"org.lgna.story.resources.prop.Boulder5",
					"org.lgna.story.resources.prop.Boulder",

					"OUTSIDE_SHIP",
					"PIRATE_SHIP",

					"CORAL_SHELF1",
					"SHELF1",

					"org.lgna.story.resources.prop.CoralShelf1",
					"org.lgna.story.resources.prop.CoralShelf",

					"CORAL_SHELF2",
					"SHELF2",

					"org.lgna.story.resources.prop.CoralShelf2",
					"org.lgna.story.resources.prop.CoralShelf",

					"SEA_PLANT1",
					"PLANT1",

					"org.lgna.story.resources.prop.SeaPlant1",
					"org.lgna.story.resources.prop.SeaPlant",

					"SEAPLANT2",
					"PLANT2",

					"org.lgna.story.resources.prop.SeaPlant2",
					"org.lgna.story.resources.prop.SeaPlant",

					"SEA_PLANT3",
					"PLANT3",

					"org.lgna.story.resources.prop.SeaPlant3",
					"org.lgna.story.resources.prop.SeaPlant",

					"org.lgna.story.resources.prop.SeaWeed1",
					"org.lgna.story.resources.prop.Seaweed",

					"org.lgna.story.resources.prop.SeaWeed2",
					"org.lgna.story.resources.prop.Seaweed",

					"org.lgna.story.resources.prop.SeaWeed3",
					"org.lgna.story.resources.prop.Seaweed",

					"MUSHROOM_RED",
					"RED",

					"org.lgna.story.resources.prop.ShortRedMushroom",
					"org.lgna.story.resources.prop.ShortMushroom",

					"org.lgna.story.resources.prop.TallRedMushroom",
					"org.lgna.story.resources.prop.TallMushroom",

					"MUSHROOM_WHITE",
					"WHITE",

					"org.lgna.story.resources.prop.ShortWhiteMushroom",
					"org.lgna.story.resources.prop.ShortMushroom",

					"org.lgna.story.resources.prop.TallWhiteMushroom",
					"org.lgna.story.resources.prop.TallMushroom",

					"TREE_WONDERLAND",
					"WONDERLAND_TREE",

					"ARMOIRE_LOFT_REDFINISH",
					"LOFT_RED_FINISH",

					"ARMOIRE_LOFT_DARK_WOOD",
					"LOFT_DARK_WOOD",

					"ARMOIRE_LOFT_HONEY",
					"LOFT_HONEY",

					"ARMOIRE_LOFT_MAPLE",
					"LOFT_MAPLE",

					"org.lgna.story.resources.armoire.ArmoireLoft",
					"org.lgna.story.resources.armoire.Armoire",

					"org.lgna.story.resources.armoire.ArmoireQuaint",
					"org.lgna.story.resources.armoire.Armoire",

					"ARMOIRE_CENTRAL_ASIAN_GREENFLORAL",
					"CENTRAL_ASIAN_GREEN_FLORAL",

					"ARMOIRE_CENTRAL_ASIAN_LATTICE",
					"CENTRAL_ASIAN_LATTICE",

					"ARMOIRE_CENTRAL_ASIAN_LION",
					"CENTRAL_ASIAN_LION",

					"ARMOIRE_CENTRAL_ASIAN_SIMPLEFLORAL",
					"CENTRAL_ASIAN_SIMPLE_FLORAL",

					"ARMOIRE_CENTRAL_ASIAN_STORY",
					"CENTRAL_ASIAN_STORY",

					"org.lgna.story.resources.armoire.ArmoireColonial",
					"org.lgna.story.resources.armoire.Armoire",

					"_ARMOIRE_MOROCCAN_GREEN",
					"MOROCCAN_GREEN",

					"_ARMOIRE_MOROCCAN_BLUE",
					"MOROCCAN_BLUE",

					"_ARMOIRE_MOROCCAN_RED",
					"MOROCCAN_RED",

					"org.lgna.story.resources.armoire.ArmoireMoroccan",
					"org.lgna.story.resources.armoire.Armoire",

					"UFO__GLOW",
					"UFO",

					"UFO__ZAP2",
					"UFO",

					"CANDY_FACTORY_SURFACE",
					"CANDY_FACTORY",

					"org.lgna.story.resources.prop.ArtNoveauCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD",
					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD",

					"TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK",
					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK",

					"TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED",
					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED",

					"TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG",
					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG",

					"TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD",
					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD",

					"TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH",
					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH",

					"TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK",
					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK",

					"TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE",
					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE",

					"org.lgna.story.resources.prop.SmallClubCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"org.lgna.story.resources.prop.LargeClubCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"TABLE_COFFEE_COLONIAL_GOLDFLORAL",
					"COLONIAL_TABLE_COFFEE_COLONIAL_GOLDFLORAL",

					"TABLE_COFFEE_COLONIAL_PAONAZZETTO",
					"COLONIAL_TABLE_COFFEE_COLONIAL_PAONAZZETTO",

					"TABLE_COFFEE_COLONIAL_PERLINO",
					"COLONIAL_TABLE_COFFEE_COLONIAL_PERLINO",

					"TABLE_COFFEE_COLONIAL_WHITEMARBLE",
					"COLONIAL_TABLE_COFFEE_COLONIAL_WHITEMARBLE",

					"org.lgna.story.resources.prop.ColonialCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"TABLE_COFFEE_END_DESIGNER_WALNUT",
					"DESIGNER_TABLE_COFFEE_END_DESIGNER_WALNUT",

					"org.lgna.story.resources.prop.DesignerCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"_TABLE_COFFEE_LOFT_SHEEN",
					"LOFT_TABLE_COFFEE_LOFT_SHEEN",

					"_TABLE_COFFEE_LOFT_CONCRETE",
					"LOFT_TABLE_COFFEE_LOFT_CONCRETE",

					"_TABLE_COFFEE_LOFT_RED_METAL",
					"LOFT_TABLE_COFFEE_LOFT_RED_METAL",

					"_TABLE_COFFEE_LOFT_PATINA",
					"LOFT_TABLE_COFFEE_LOFT_PATINA",

					"org.lgna.story.resources.prop.LoftCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"TABLE_COFFEE_MOROCCAN_TOP_TABLE_STAR",
					"MOROCCAN_TABLE_COFFEE_MOROCCAN_TOP_TABLE_STAR",

					"TABLE_COFFEE_MOROCCAN_TOP_TABLE_ALADDIN",
					"MOROCCAN_TABLE_COFFEE_MOROCCAN_TOP_TABLE_ALADDIN",

					"TABLE_COFFEE_MOROCCAN_TOP_TABLE_DETAIL",
					"MOROCCAN_TABLE_COFFEE_MOROCCAN_TOP_TABLE_DETAIL",

					"TABLE_COFFEE_MOROCCAN_TOP_TABLE_TILE",
					"MOROCCAN_TABLE_COFFEE_MOROCCAN_TOP_TABLE_TILE",

					"TABLE_COFFEE_MOROCCAN_WOODS_CHERRY",
					"MOROCCAN_TABLE_COFFEE_MOROCCAN_WOODS_CHERRY",

					"TABLE_COFFEE_MOROCCAN_WOODS_MAHOGNY",
					"MOROCCAN_TABLE_COFFEE_MOROCCAN_WOODS_MAHOGNY",

					"TABLE_COFFEE_MOROCCAN_WOODS_YELLOWASPEN",
					"MOROCCAN_TABLE_COFFEE_MOROCCAN_WOODS_YELLOWASPEN",

					"TABLE_COFFEE_MOROCCAN_WOODS_BLACK_LAQUER",
					"MOROCCAN_TABLE_COFFEE_MOROCCAN_WOODS_BLACK_LAQUER",

					"org.lgna.story.resources.prop.MoroccanCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"_TABLE_COFFEE_PINE_CEDAR_WOOD",
					"PINE_TABLE_COFFEE_PINE_CEDAR_WOOD",

					"_TABLE_COFFEE_PINE_BLONDE_WOOD",
					"PINE_TABLE_COFFEE_PINE_BLONDE_WOOD",

					"_TABLE_COFFEE_PINE_HONEY_PINE",
					"PINE_TABLE_COFFEE_PINE_HONEY_PINE",

					"_TABLE_COFFEE_PINE_WALNUT_WOOD",
					"PINE_TABLE_COFFEE_PINE_WALNUT_WOOD",

					"_TABLE_COFFEE_PINE_BIRCH_WOOD",
					"PINE_TABLE_COFFEE_PINE_BIRCH_WOOD",

					"org.lgna.story.resources.prop.PineCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"TABLE_COFFEE_QUAINT_BLUE",
					"QUAINT_TABLE_COFFEE_QUAINT_BLUE",

					"TABLE_COFFEE_QUAINT_GREEN",
					"QUAINT_TABLE_COFFEE_QUAINT_GREEN",

					"TABLE_COFFEE_QUAINT_WHITE",
					"QUAINT_TABLE_COFFEE_QUAINT_WHITE",

					"TABLE_COFFEE_QUAINT_RED",
					"QUAINT_TABLE_COFFEE_QUAINT_RED",

					"org.lgna.story.resources.prop.QuaintCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"TABLE_COFFEE_SPINDLE_WOOD_OLDWOOD",
					"SPINDLE_TABLE_COFFEE_SPINDLE_WOOD_OLDWOOD",

					"TABLE_COFFEE_SPINDLE_WOOD_PAINTED",
					"SPINDLE_TABLE_COFFEE_SPINDLE_WOOD_PAINTED",

					"TABLE_COFFEE_SPINDLE_WOOD_RED",
					"SPINDLE_TABLE_COFFEE_SPINDLE_WOOD_RED",

					"org.lgna.story.resources.prop.SpindleCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"TABLE_DINING_CLUB_ROOT",
					"CLUB_TABLE_DINING_CLUB_ROOT",

					"TABLE_DINING_CLUB_NEDAR",
					"CLUB_TABLE_DINING_CLUB_NEDAR",

					"TABLE_DINING_CLUB_OAK",
					"CLUB_TABLE_DINING_CLUB_OAK",

					"TABLE_DINING_CLUB_RED",
					"CLUB_TABLE_DINING_CLUB_RED",

					"TABLE_DINING_CLUB_REDDARK",
					"CLUB_TABLE_DINING_CLUB_REDDARK",

					"TABLE_DINING_CLUB_WOOD",
					"CLUB_TABLE_DINING_CLUB_WOOD",

					"TABLE_DINING_CLUB_PINE",
					"CLUB_TABLE_DINING_CLUB_PINE",

					"TABLE_DINING_CLUB_SEDAR",
					"CLUB_TABLE_DINING_CLUB_SEDAR",

					"org.lgna.story.resources.prop.ClubDiningTable",
					"org.lgna.story.resources.prop.DiningTable",

					"TABLE_DINING_MOROCCAN_TURQ",
					"MOROCCAN_TABLE_DINING_MOROCCAN_TURQ",

					"TABLE_DINING_MOROCCAN_BLUE",
					"MOROCCAN_TABLE_DINING_MOROCCAN_BLUE",

					"TABLE_DINING_MOROCCAN_BLUE_LIGHT",
					"MOROCCAN_TABLE_DINING_MOROCCAN_BLUE_LIGHT",

					"TABLE_DINING_MOROCCAN_GREEN",
					"MOROCCAN_TABLE_DINING_MOROCCAN_GREEN",

					"org.lgna.story.resources.prop.MoroccanDiningTable",
					"org.lgna.story.resources.prop.DiningTable",

					"TABLE_DINING_ORIENTAL_DRAGON_BROWN",
					"ORIENTAL_TABLE_DINING_ORIENTAL_DRAGON_BROWN",

					"TABLE_DINING_ORIENTAL_FISH_BROWN",
					"ORIENTAL_TABLE_DINING_ORIENTAL_FISH_BROWN",

					"TABLE_DINING_ORIENTAL_DRAGON_RED",
					"ORIENTAL_TABLE_DINING_ORIENTAL_DRAGON_RED",

					"TABLE_DINING_ORIENTAL_FISH_RED",
					"ORIENTAL_TABLE_DINING_ORIENTAL_FISH_RED",

					"TABLE_DINING_ORIENTAL_LOTUS_BLACK",
					"ORIENTAL_TABLE_DINING_ORIENTAL_LOTUS_BLACK",

					"TABLE_DINING_ORIENTAL_LOTUS_ORANGE",
					"ORIENTAL_TABLE_DINING_ORIENTAL_LOTUS_ORANGE",

					"org.lgna.story.resources.prop.OrientalDiningTable",
					"org.lgna.story.resources.prop.DiningTable",

					"TABLE_DINING_OUTDOOR_WOOD_ASH",
					"OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_ASH",

					"TABLE_DINING_OUTDOOR_WOOD_REDOAK",
					"OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_REDOAK",

					"TABLE_DINING_OUTDOOR_WOOD_REDWOOD",
					"OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_REDWOOD",

					"TABLE_DINING_OUTDOOR_WOOD_WHITE",
					"OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_WHITE",

					"TABLE_DINING_OUTDOOR_WOOD_CROSSPINE",
					"OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_CROSSPINE",

					"org.lgna.story.resources.prop.OutdoorDiningTable",
					"org.lgna.story.resources.prop.DiningTable",

					"TABLE_DINING_QUAINT_RED",
					"QUAINT_TABLE_DINING_QUAINT_RED",

					"TABLE_DINING_QUAINT_GREEN",
					"QUAINT_TABLE_DINING_QUAINT_GREEN",

					"TABLE_DINING_QUAINT_WHITE",
					"QUAINT_TABLE_DINING_QUAINT_WHITE",

					"TABLE_DINING_QUAINT_BLUE",
					"QUAINT_TABLE_DINING_QUAINT_BLUE",

					"org.lgna.story.resources.prop.QuaintDiningTable",
					"org.lgna.story.resources.prop.DiningTable",

// duplicates
//					"TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD",
//					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD",
//
//					"TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK",
//					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK",
//
//					"TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED",
//					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED",
//
//					"TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG",
//					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG",
//
//					"TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD",
//					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD",
//
//					"TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH",
//					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH",
//
//					"TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK",
//					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK",
//
//					"TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE",
//					"CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE",

					"org.lgna.story.resources.prop.ClubEndTable",
					"org.lgna.story.resources.prop.EndTable",

					"TABLE_END_COLONIAL2_TABLE_LIGHTWOOD",
					"COLONIAL_TABLE_END_COLONIAL2_TABLE_LIGHTWOOD",

					"TABLE_END_COLONIAL2_TABLE_REDWOOD",
					"COLONIAL_TABLE_END_COLONIAL2_TABLE_REDWOOD",

					"TABLE_END_COLONIAL2_TABLE_WOOD",
					"COLONIAL_TABLE_END_COLONIAL2_TABLE_WOOD",

					"TABLE_END_COLONIAL2_TABLE_DARKWOOD",
					"COLONIAL_TABLE_END_COLONIAL2_TABLE_DARKWOOD",

					"TABLE_END_MOROCCAN_END_TABLE_ALADDIN",
					"MOROCCAN_TABLE_END_MOROCCAN_END_TABLE_ALADDIN",

					"TABLE_END_MOROCCAN_END_TABLE_STAR",
					"MOROCCAN_TABLE_END_MOROCCAN_END_TABLE_STAR",

					"TABLE_END_MOROCCAN_END_TABLE_TILE",
					"MOROCCAN_TABLE_END_MOROCCAN_END_TABLE_TILE",

					"TABLE_END_MOROCCAN_END_TABLE_DETAIL",
					"MOROCCAN_TABLE_END_MOROCCAN_END_TABLE_DETAIL",

					"org.lgna.story.resources.prop.MoroccanEndTable",
					"org.lgna.story.resources.prop.EndTable",

					"org.lgna.story.resources.prop.OctagonalEndTable",
					"org.lgna.story.resources.prop.EndTable",

					"TABLE_END_QUAINT__FABRIC_BLUE",
					"QUAINT_TABLE_END_QUAINT_FABRIC_BLUE",

					"TABLE_END_QUAINT__FABRIC_PINK",
					"QUAINT_TABLE_END_QUAINT_FABRIC_PINK",

					"TABLE_END_QUAINT__FABRIC_GREEN",
					"QUAINT_TABLE_END_QUAINT_FABRIC_GREEN",

					"TABLE_END_QUAINT__FABRIC_BEIGE",
					"QUAINT_TABLE_END_QUAINT_FABRIC_BEIGE",

					"TABLE_END_QUAINT__FABRIC_WHITE",
					"QUAINT_TABLE_END_QUAINT_FABRIC_WHITE",

					"TABLE_END_QUAINT__FABRIC_WHITE_FLOWERS",
					"QUAINT_TABLE_END_QUAINT_FABRIC_WHITE_FLOWERS",

					"TABLE_END_UM_PURPLE",
					"UM_TABLE_END_UM_PURPLE",

					"TABLE_END_UM_GREEN",
					"UM_TABLE_END_UM_GREEN",

					"TABLE_END_UM_BLACK",
					"UM_TABLE_END_UM_BLACK",

					"TABLE_END_UM_BLUE",
					"UM_TABLE_END_UM_BLUE",

					"TABLE_END_UM_YELLOW",
					"UM_TABLE_END_UM_YELLOW",

					"TABLE_END_UM_WHITE",
					"UM_TABLE_END_UM_WHITE",

					"TABLE_END_UM_ORANGE",
					"UM_TABLE_END_UM_ORANGE",

					"org.lgna.story.resources.prop.UmEndTable",
					"org.lgna.story.resources.prop.EndTable",

					"LOFT_BOOKCASE_WOOD_DARK",
					"LOFT_LOFT_BOOKCASE_WOOD_DARK",

					"LOFT_BOOKCASE_WOOD_LIGHT",
					"LOFT_LOFT_BOOKCASE_WOOD_LIGHT",

					"LOFT_BOOKCASE_WOOD_MEDIUM",
					"LOFT_LOFT_BOOKCASE_WOOD_MEDIUM",

					"LOFT_BOOKCASE_BRUSHED",
					"LOFT_LOFT_BOOKCASE_BRUSHED",

					"org.lgna.story.resources.prop.BookcaseLoft",
					"org.lgna.story.resources.prop.Bookcase",

					"BOOKCASE_CHEAP_OAK",
					"CHEAP_BOOKCASE_CHEAP_OAK",

					"BOOKCASE_CHEAP_MAHOGANY",
					"CHEAP_BOOKCASE_CHEAP_MAHOGANY",

					"BOOKCASE_CHEAP_PINE",
					"CHEAP_BOOKCASE_CHEAP_PINE",

					"BOOKCASE_CHEAP_BLACK_WASH",
					"CHEAP_BOOKCASE_CHEAP_BLACK_WASH",

					"BOOKCASE_CHEAP_WOOD_PLANK",
					"CHEAP_BOOKCASE_CHEAP_WOOD_PLANK",

					"org.lgna.story.resources.prop.BookcaseCheap",
					"org.lgna.story.resources.prop.Bookcase",

					"BOOKCASE_COLONIAL_REDWOODCURLY",
					"COLONIAL_BOOKCASE_COLONIAL_REDWOODCURLY",

					"BOOKCASE_COLONIAL_BROWNWOODCURLY",
					"COLONIAL_BOOKCASE_COLONIAL_BROWNWOODCURLY",

					"BOOKCASE_COLONIAL_DARK_BROWN_WOODCURLY",
					"COLONIAL_BOOKCASE_COLONIAL_DARK_BROWN_WOODCURLY",

					"org.lgna.story.resources.prop.BookcaseColonial",
					"org.lgna.story.resources.prop.Bookcase",

					"BOOKCASE_VALUE_PRESSEDPINE",
					"VALUE_BOOKCASE_VALUE_PRESSEDPINE",

					"BOOKCASE_VALUE_PINE",
					"VALUE_BOOKCASE_VALUE_PINE",

					"org.lgna.story.resources.prop.BookcaseValue",
					"org.lgna.story.resources.prop.Bookcase",

					"org.lgna.story.resources.prop.LoveseatCamelBack",
					"org.lgna.story.resources.prop.Loveseat",

					"org.lgna.story.resources.prop.LoveseatMoroccan",
					"org.lgna.story.resources.prop.Loveseat",

					"org.lgna.story.resources.prop.LoveseatParkBench",
					"org.lgna.story.resources.prop.Loveseat",

					"org.lgna.story.resources.prop.LoveseatQuaint",
					"org.lgna.story.resources.prop.Loveseat",

					createMoreSpecificFieldString( "SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldString( "MOROCCAN_SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldString( "SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldString( "MOROCCAN_SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldString( "SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldString( "MOROCCAN_SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldString( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldString( "PARK_BENCH_LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldString( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldString( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldString( "PARK_BENCH_LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.Loveseat" ),

					"LOVESEAT_VALUE_RED_CHECKER",
					"VALUE_LOVESEAT_VALUE_RED_CHECKER",

					"LOVESEAT_VALUE_BLUE_CHECKER",
					"VALUE_LOVESEAT_VALUE_BLUE_CHECKER",

					"LOVESEAT_VALUE_BLUE_STRIPE",
					"VALUE_LOVESEAT_VALUE_BLUE_STRIPE",

					"LOVESEAT_VALUE_FLOWER",
					"VALUE_LOVESEAT_VALUE_FLOWER",

					"org.lgna.story.resources.prop.LoveseatValue",
					"org.lgna.story.resources.prop.Loveseat",

					"DESK_CENTRAL_ASIAN_RED",
					"CENTRAL_ASIAN_LION",

					"DESK_CENTRAL_ASIAN_BLACK",
					"CENTRAL_ASIAN_LION",

					"DESK_CLUB__DARKWOOD",
					"CLUB_DESK_CLUB_DARKWOOD",

					"DESK_CLUB__ASH",
					"CLUB_DESK_CLUB_ASH",

					"DESK_CLUB__REDWOOD",
					"CLUB_DESK_CLUB_REDWOOD",

					"org.lgna.story.resources.prop.DeskClub",
					"org.lgna.story.resources.prop.Desk",

					"DESK_QUAINT_GREEN",
					"QUAINT_GREEN",

					"DESK_QUAINT_WHITE",
					"QUAINT_DESK_QUAINT_WHITE",

					"DESK_QUAINT_RED",
					"QUAINT_RED",

					"DESK_QUAINT_BLUE",
					"QUAINT_BLUE",

					"DESK_VALUE_WOODWHITE",
					"VALUE_DESK_VALUE_WOODWHITE",

					"DESK_VALUE_WOODRED",
					"VALUE_DESK_VALUE_WOODRED",

					"DESK_VALUE_WOOD_METAL",
					"VALUE_DESK_VALUE_WOOD_METAL",

					"DESK_VALUE_WOODMAPPLE",
					"VALUE_DESK_VALUE_WOODMAPPLE",

					"org.lgna.story.resources.prop.DeskValue",
					"org.lgna.story.resources.prop.Desk",

					"SOFA_VALUE1_REDCHECKER",
					"VALUE1_SOFA_VALUE1_REDCHECKER",

					"SOFA_VALUE1_BLUE_CHECKER",
					"VALUE1_SOFA_VALUE1_BLUE_CHECKER",

					"SOFA_VALUE1_BLUE_STRIPE",
					"VALUE1_SOFA_VALUE1_BLUE_STRIPE",

					"SOFA_VALUE1_FLOWER",
					"VALUE1_SOFA_VALUE1_FLOWER",

					"org.lgna.story.resources.prop.SofaValue1",
					"org.lgna.story.resources.prop.Sofa",

					createMoreSpecificFieldString( "SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.Sofa" ),
					createMoreSpecificFieldString( "MOROCCAN_SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.Sofa" ),

					createMoreSpecificFieldString( "SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.Sofa" ),
					createMoreSpecificFieldString( "MOROCCAN_SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.Sofa" ),

					createMoreSpecificFieldString( "SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.Sofa" ),
					createMoreSpecificFieldString( "MOROCCAN_SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.Sofa" ),


					"org.lgna.story.resources.prop.SofaQuaint",
					"org.lgna.story.resources.prop.SeaPlant",

					"SOFA_VALUE2_LIGHT_BROWN_FLOWER",
					"VALUE2_SOFA_VALUE2_LIGHT_BROWN_FLOWER",

					"SOFA_VALUE2_RED_CHECKER",
					"VALUE2_SOFA_VALUE2_RED_CHECKER",

					"SOFA_VALUE2_GREEN_FLOWER",
					"VALUE2_SOFA_VALUE2_GREEN_FLOWER",

					"SOFA_VALUE2_BLUE_FLOWER_BORDER",
					"VALUE2_SOFA_VALUE2_BLUE_FLOWER_BORDER",

					"org.lgna.story.resources.prop.SofaValue2",
					"org.lgna.story.resources.prop.Sofa",

					"LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADE",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADE",

					"LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADEON",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADEON",

					"LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADE",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADE",

					"LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADEON",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADEON",

					"LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADE",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADE",

					"LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADEON",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADEON",

					"LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADEON",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADEON",

					"LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADE",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADE",

					"LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADE",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADE",

					"LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADEON",
					"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADEON",

					"LIGHTING_FLOOR_LOFT_LAMP_SHADE_YELLOW_UNLIT",
					"LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_YELLOW_UNLIT",

					"LIGHTING_FLOOR_LOFT_LAMP_SHADE_BLUE_UNLIT",
					"LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_BLUE_UNLIT",

					"LIGHTING_FLOOR_LOFT_LAMP_SHADE_GREEN_UNLIT",
					"LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_GREEN_UNLIT",

					"LIGHTING_FLOOR_LOFT_LAMP_SHADE_ORANGE_UNLIT",
					"LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_ORANGE_UNLIT",

					"LIGHTING_FLOOR_LOFT_LAMP_SHADE_RED_UNLIT",
					"LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_RED_UNLIT",

					"LIGHTING_FLOOR_LOFT_LAMP_SHADE_YELLOW_LIT",
					"LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_YELLOW_LIT",

					"LIGHTING_FLOOR_LOFT_LAMP_SHADE_BLUE_LIT",
					"LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_BLUE_LIT",

					"LIGHTING_FLOOR_LOFT_LAMP_SHADE_RED_LIT",
					"LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_RED_LIT",

					"LIGHTING_FLOOR_LOFT_LAMP_SHADE_GREEN_LIT",
					"LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_GREEN_LIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_BLUE_UNLIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_BLUE_UNLIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_GOLD_BLUE_UNLIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_GOLD_BLUE_UNLIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_ORANGE_UNLIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_ORANGE_UNLIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_RED_UNLIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_RED_UNLIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_YELLOW_UNLIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_YELLOW_UNLIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_BLUES_LIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_BLUES_LIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_GOLD_BLUE_LIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_GOLD_BLUE_LIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_ORANGE_LIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_ORANGE_LIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_RED_LIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_RED_LIT",

					"LIGHTING_FLOOR_MOROCCAN_SHADE_YELLOW_LIT",
					"MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_YELLOW_LIT",

					"LIGHTING_FLOOR_QUAINT_SHADE_PINK_LIT",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_PINK_LIT",

					"LIGHTING_FLOOR_QUAINT_SHADE_BEIGE_LIT",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BEIGE_LIT",

					"LIGHTING_FLOOR_QUAINT_SHADE_BEIGE",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BEIGE",

					"LIGHTING_FLOOR_QUAINT_SHADE_WHITE",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_WHITE",

					"LIGHTING_FLOOR_QUAINT_SHADE_PINK",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_PINK",

					"LIGHTING_FLOOR_QUAINT_SHADE_BLUE",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BLUE",

					"LIGHTING_FLOOR_QUAINT_SHADE_YELLOW",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_YELLOW",

					"LIGHTING_FLOOR_QUAINT_SHADE_GREEN",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_GREEN",

					"LIGHTING_FLOOR_QUAINT_SHADE_BLUE_LIT",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BLUE_LIT",

					"LIGHTING_FLOOR_QUAINT_SHADE_GREEN_LIT",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_GREEN_LIT",

					"LIGHTING_FLOOR_QUAINT_SHADE_WHITE_LIT",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_WHITE_LIT",

					"LIGHTING_FLOOR_QUAINT_SHADE_YELLOW_LIT",
					"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_YELLOW_LIT",

					"LIGHTING_FLOOR_STUDIO_LIGHTS_LIGHTS_LIT",
					"STUDIO_LIGHTING_FLOOR_STUDIO_LIGHTS_LIGHTS_LIT",

					"LIGHTING_FLOOR_STUDIO_LIGHTS_LIGHTS_UNLIT",
					"STUDIO_LIGHTING_FLOOR_STUDIO_LIGHTS_LIGHTS_UNLIT",

					"LIGHTING_FLOOR_VALUE_PAINTED_METAL_BLACKPAINT",
					"VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_BLACKPAINT",

					"LIGHTING_FLOOR_VALUE_PAINTED_METAL_REDPAINT",
					"VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_REDPAINT",

					"LIGHTING_FLOOR_VALUE_PAINTED_METAL_TANPAINT",
					"VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_TANPAINT",

					"LIGHTING_FLOOR_VALUE_PAINTED_METAL_GREEN_PAINT",
					"VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_GREEN_PAINT",

					"LIGHTING_FLOOR_VALUE_PAINTED_METAL_WHITEPAINT",
					"VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_WHITEPAINT",

					"CHAIR_DINING_CLUB_RED_WOOD",
					"CLUB_CHAIR_DINING_CLUB_RED_WOOD",

					"CHAIR_DINING_CLUB_GREENLEATH",
					"CLUB_CHAIR_DINING_CLUB_GREENLEATH",

					"CHAIR_DINING_CLUB_OAKCANE",
					"CLUB_CHAIR_DINING_CLUB_OAKCANE",

					"CHAIR_DINING_CLUB_LTGREENLEATH",
					"CLUB_CHAIR_DINING_CLUB_LTGREENLEATH",

					"org.lgna.story.resources.prop.ClubDiningChair",
					"org.lgna.story.resources.prop.DiningTable",

					"CHAIR_DINING_COLONIAL1_PURPLE",
					"COLONIAL_CHAIR_DINING_COLONIAL1_PURPLE",

					"CHAIR_DINING_COLONIAL1_GOLD_PATTERN",
					"COLONIAL_CHAIR_DINING_COLONIAL1_GOLD_PATTERN",

					"CHAIR_DINING_COLONIAL1_GOLDEN2",
					"COLONIAL_CHAIR_DINING_COLONIAL1_GOLDEN2",

					"CHAIR_DINING_COLONIAL1_STRIPES",
					"COLONIAL_CHAIR_DINING_COLONIAL1_STRIPES",

					"CHAIR_DINING_COLONIAL1_BLUEPATTERN",
					"COLONIAL_CHAIR_DINING_COLONIAL1_BLUEPATTERN",

					"CHAIR_DINING_COLONIAL1_DIAMONDS",
					"COLONIAL_CHAIR_DINING_COLONIAL1_DIAMONDS",

					"org.lgna.story.resources.prop.ParkChair",
					"org.lgna.story.resources.prop.Chair",

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldString( "PARK_LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.Chair" ),

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldString( "PARK_BENCH_LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.Chair" ),

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldString( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.Chair" ),

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldString( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.Chair" ),

					createMoreSpecificFieldString( "LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldString( "PARK_BENCH_LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.Chair" ),

					"LOVESEAT_PARK_BENCH_WALNUT",
					"PARK_LOVESEAT_PARK_BENCH_WALNUT",

					"LOVESEAT_PARK_BENCH_CHESTNUT",
					"PARK_LOVESEAT_PARK_BENCH_CHESTNUT",


					"CHAIR_DINING_MODERATE_BODY_BLACK",
					"MODERATE_CHAIR_DINING_MODERATE_BODY_BLACK",

					"CHAIR_DINING_MODERATE_BODY_WOOD",
					"MODERATE_CHAIR_DINING_MODERATE_BODY_WOOD",

					"CHAIR_DINING_MODERATE_BODY_TEAL",
					"MODERATE_CHAIR_DINING_MODERATE_BODY_TEAL",

					"CHAIR_DINING_MODERATE_BODY_RED",
					"MODERATE_CHAIR_DINING_MODERATE_BODY_RED",

					"CHAIR_DINING_MODERATE_BODY_BLUE",
					"MODERATE_CHAIR_DINING_MODERATE_BODY_BLUE",

					"CHAIR_DINING_MODERATE_SEAT_GRAY",
					"MODERATE_CHAIR_DINING_MODERATE_SEAT_GRAY",

					"CHAIR_DINING_MODERATE_SEAT_BUMBLE",
					"MODERATE_CHAIR_DINING_MODERATE_SEAT_BUMBLE",

					"CHAIR_DINING_MODERATE_SEAT_TEAL",
					"MODERATE_CHAIR_DINING_MODERATE_SEAT_TEAL",

					"CHAIR_DINING_MODERATE_SEAT_STRAWBERRY",
					"MODERATE_CHAIR_DINING_MODERATE_SEAT_STRAWBERRY",

					"CHAIR_DINING_MODERATE_SEAT_YELLOW",
					"MODERATE_CHAIR_DINING_MODERATE_SEAT_YELLOW",

					"CHAIR_DINING_MODERATE_SEAT_BLUE",
					"MODERATE_CHAIR_DINING_MODERATE_SEAT_BLUE",

					"CHAIR_DINING_MOROCCAN_SURFACES_BLUE_ORANGE",
					"MOROCCAN_CHAIR_DINING_MOROCCAN_SURFACES_BLUE_ORANGE",

					"CHAIR_DINING_MOROCCAN_SURFACES_RED_CIRCLES",
					"MOROCCAN_CHAIR_DINING_MOROCCAN_SURFACES_RED_CIRCLES",

					"CHAIR_DINING_MOROCCAN_SURFACES_RED_TAN",
					"MOROCCAN_CHAIR_DINING_MOROCCAN_SURFACES_RED_TAN",

					"CHAIR_DINING_MOROCCAN_SURFACES_BLUE_STRIPES",
					"MOROCCAN_CHAIR_DINING_MOROCCAN_SURFACES_BLUE_STRIPES",

					"CHICKENEYE_CARTOON",
					"CHICKEN",

					"org.lgna.story.resources.whale.Dolphin",
					"org.lgna.story.resources.marinemammal.Dolphin",

					"MANX",
					"MANX_CAT",

					"GREEN_OGRE",
					"GREEN",

					"org.lgna.story.resources.whale.Orca",
					"org.lgna.story.resources.marinemammal.Orca",

					"ADULT_PENGUIN",
					"ADULT",

					"BABY_PENGUIN",
					"BABY",

					"org.lgna.story.resources.biped.FarmerPig",
					"org.lgna.story.resources.biped.Pig",

					"SCOTTY",
					"SCOTTY_DOG",

					"SHOE",
					"TORTOISE",

					"org.lgna.story.resources.quadruped.Robot",
					"org.lgna.story.resources.quadruped.AlienRobot",

					"_SUB",
					"SUBMARINE",

					"SEAWEED_BACK",
					"SEAWEED3",

					"SEAWEED_FRONT",
					"SEAWEED1",

					"SEAWEED_MID",
					"SEAWEED2",

					"QUEEN",
					"QUEEN_OF_HEARTS",

					"PLAYING_CARD_TWO",
					"CARD02",

					"PLAYING_CARD_TEN",
					"CARD10",

					"PLAYING_CARD_THREE",
					"CARD03",

					"PLAYING_CARD_ONE",
					"CARD01",

					"PLAYING_CARD_SEVEN",
					"CARD07",

					"PLAYING_CARD_NINE",
					"CARD09",

					"PLAYING_CARD_SIX",
					"CARD06",

					"PLAYING_CARD",
					"BLANK",

					"PLAYING_CARD_EIGHT",
					"CARD08",

					"PLAYING_CARD_FIVE",
					"CARD05",

					"PLAYING_CARD_FOUR",
					"CARD04",

					"ARMOIRE_LOFT_TRIM_BLACK",
					"LOFT_BLACK_TRIM",

					"ARMOIRE_LOFT_TRIM_HONEY_DARK",
					"LOFT_DARK_HONEY_TRIM",

					"ARMOIRE_LOFT_TRIM_SWIRLY_BROWN",
					"LOFT_SWIRLY_BROWN_TRIM",

					"ARMOIRE_LOFT_TRIM_DARK_RED",
					"LOFT_DARK_RED_TRIM",

					"ARMOIRE_LOFT_TRIM_MEDIUM_BROWN",
					"LOFT_MEDIUM_BROWN_TRIM",

					"ARMOIRE_LOFT_SURFACES",
					"LOFT_OAK",

					"ARMOIRE_QUAINT_ARMOIRE_BLUE",
					"QUAINT_BLUE",

					"ARMOIRE_QUAINT_ARMOIRE_GREEN",
					"QUAINT_GREEN",

					"ARMOIRE_QUAINT_ARMOIR_LEAVES",
					"QUAINT_LEAVES",

					"ARMOIRE_QUAINT_ARMOIRE_RED",
					"QUAINT_RED",

					"ARMOIRE_QUAINT_ARMOIRE_ROSES",
					"QUAINT_ROSES",

					"ARMOIRE_CENTRAL_ASIAN_DRAGONDOOR",
					"CENTRAL_ASIAN_DRAGON",

					"org.lgna.story.resources.armoire.ArmoireCentralAsian",
					"org.lgna.story.resources.armoire.Armoire",

					"ARMOIRE_COLONIAL_WOOD_REDWOODCURLY",
					"COLONIAL_CURLY_REDWOOD",

					"ARMOIRE_COLONIAL_WOOD_DARKWOODQUILTED",
					"COLONIAL_QUILTED_DARK_WOOD",

					"ARMOIRE_COLONIAL_WOOD_LIGHTWOODCURLY",
					"COLONIAL_CURLY_LIGHT_WOOD",

					"DRESSER_CENTRAL_ASIAN_GREEN_FLOWERS",
					"CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_GREEN_FLOWERS",

					"DRESSER_CENTRAL_ASIAN_RED_FLOWERS",
					"CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_RED_FLOWERS",

					"DRESSER_CENTRAL_ASIAN_GREEN",
					"CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_GREEN",

					"DRESSER_CENTRAL_ASIAN_RED",
					"CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_RED",

					"org.lgna.story.resources.prop.DresserCentralAsian",
					"org.lgna.story.resources.prop.Dresser",

					"DRESSER_COLONIAL_WOOD",
					"COLONIAL_DRESSER_COLONIAL_WOOD",

					"DRESSER_COLONIAL_LIGHT_WOOD_CURLY",
					"COLONIAL_DRESSER_COLONIAL_LIGHT_WOOD_CURLY",

					"DRESSER_COLONIAL_RED_WOOD",
					"COLONIAL_DRESSER_COLONIAL_RED_WOOD",

					"DRESSER_COLONIAL_WOOD_STRAIGHT_DARK",
					"COLONIAL_DRESSER_COLONIAL_WOOD_STRAIGHT_DARK",

					"org.lgna.story.resources.prop.DresserColonial",
					"org.lgna.story.resources.prop.Dresser",

					"DRESSER_DESIGNER_BROWN",
					"DESIGNER_DRESSER_DESIGNER_BROWN",

					"DRESSER_DESIGNER_LIGHT_WOOD",
					"DESIGNER_DRESSER_DESIGNER_LIGHT_WOOD",

					"DRESSER_DESIGNER_RED",
					"DESIGNER_DRESSER_DESIGNER_RED",

					"DRESSER_DESIGNER_BLACK",
					"DESIGNER_DRESSER_DESIGNER_BLACK",

					"DRESSER_DESIGNER_BLUE",
					"DESIGNER_DRESSER_DESIGNER_BLUE",

					"org.lgna.story.resources.prop.DresserDesigner",
					"org.lgna.story.resources.prop.Dresser",

					"DRESSER_JAPANESE_TANSU_NORMAL",
					"JAPANESE_DRESSER_JAPANESE_TANSU_NORMAL",

					"DRESSER_JAPANESE_TANSU_BLACK",
					"JAPANESE_DRESSER_JAPANESE_TANSU_BLACK",

					"DRESSER_JAPANESE_TANSU_LIGHT",
					"JAPANESE_DRESSER_JAPANESE_TANSU_LIGHT",

					"DRESSER_JAPANESE_TANSU_RED",
					"JAPANESE_DRESSER_JAPANESE_TANSU_LIGHT",

					"org.lgna.story.resources.prop.DresserJapaneseTansu",
					NO_REPLACEMENT,

					"BARBEQUE_VALUE_METAL_GREEN",
					"GREEN",

					"BARBEQUE_VALUE_METAL_BLACK",
					"BLACK",

					"BARBEQUE_VALUE_METAL_YELLO",
					"YELLOW",

					"BARBEQUE_VALUE_METAL_RED",
					"RED",

					"BARBEQUE_VALUE_METAL_BLUE",
					"BLUE",

					"UFO_MAIN",
					"UFO",

					"UFO_FRAME",
					"UFO",

					"CANDY_FACTORY_ANIMATED_SURFACE",
					"CANDY_FACTORY",

					"CANDY_FACTORY_LIGHT_GREEN",
					"CANDY_FACTORY",

					"CANDY_FACTORY_LIGHT_OFF",
					"CANDY_FACTORY",

					"CANDY_FACTORY_LIGHT_RED",
					"CANDY_FACTORY",

					"TABLE_COFFEE_ART_NOUVEAU_TABLE1",
					"ART_NOVEAU_TABLE_COFFEE_ART_NOUVEAU_TABLE1",

					"TABLE_COFFEE_ART_NOUVEAU_TABLE2",
					"ART_NOVEAU_TABLE_COFFEE_ART_NOUVEAU_TABLE2",

					"TABLE_COFFEE_ART_NOUVEAU_TABLE3",
					"ART_NOVEAU_TABLE_COFFEE_ART_NOUVEAU_TABLE3",

					"TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_RED",
					"CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_RED",

					"TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_CHERRY",
					"CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_CHERRY",

					"TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_BLONDE",
					"CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_BLONDE",

					"TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_DARK",
					"CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_DARK",

					"TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_CHERRY",
					"CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_CHERRY",

					"TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_BLONDE",
					"CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_BLONDE",

					"TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_RED",
					"CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_RED",

					"TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_DARK",
					"CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_DARK",

					"org.lgna.story.resources.prop.CentralAsianCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"TABLE_COFFEE_CLUB_RECTANGLE_WOOD",
					"LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_WOOD",

					"TABLE_COFFEE_CLUB_RECTANGLE_BRIDS_RED",
					"LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_BRIDS_RED",

					"TABLE_COFFEE_CLUB_RECTANGLE_BLEACHED_OAK",
					"LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_BLEACHED_OAK",

					"TABLE_COFFEE_CLUB_RECTANGLE_MAHOG",
					"LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_MAHOG",

					"TABLE_COFFEE_CLUB_RECTANGLE_RED_ASH",
					"LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_RED_ASH",

					"TABLE_COFFEE_CLUB_RECTANGLE_WHITE_OAK",
					"LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_WHITE_OAK",

					"TABLE_COFFEE_CLUB_RECTANGLE_LTBLUE",
					"LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_LTBLUE",

					"TABLE_COFFEE_END_DESIGNER__WHITE",
					"DESIGNER_TABLE_COFFEE_END_DESIGNER_WHITE",

					"TABLE_COFFEE_END_DESIGNER__ASH",
					"DESIGNER_TABLE_COFFEE_END_DESIGNER_ASH",

					"TABLE_END_CENTRAL_ASIAN_WOOD_BLOND_WOOD",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_BLOND_WOOD",

					"TABLE_END_CENTRAL_ASIAN_WOOD_ROUGH",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_ROUGH",

					"TABLE_END_CENTRAL_ASIAN_WOOD_BROWN",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_BROWN",

					"TABLE_END_CENTRAL_ASIAN_WOOD_CHERRY",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_CHERRY",

					"TABLE_END_CENTRAL_ASIAN_WOOD_DARK",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_DARK",

					"TABLE_END_CENTRAL_ASIAN_WOOD_RED_LACQUER",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_RED_LACQUER",

					"TABLE_END_CENTRAL_ASIAN_TABLE_TOP_ROUGH",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_TABLE_TOP_ROUGH",

					"TABLE_END_CENTRAL_ASIAN_TABLE_TOP_BLOND_WOOD",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_TABLE_TOP_BLOND_WOOD",

					"TABLE_END_CENTRAL_ASIAN_TABLE_TOP_CHERRY",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_TABLE_TOP_CHERRY",

					"TABLE_END_CENTRAL_ASIAN_TABLE_TOP_RED_LACQUER",
					"CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_TABLE_TOP_RED_LACQUER",

					"org.lgna.story.resources.prop.CentralAsianEndTable",
					"org.lgna.story.resources.prop.EndTable",

					"org.lgna.story.resources.prop.ColonialEndTable",
					"org.lgna.story.resources.prop.EndTable",

					"TABLE_END_OCTAGONAL_CHERRY",
					"OCTAGONAL_TABLE_END_OCTAGONAL_CHERRY",

					"TABLE_END_OCTAGONAL_WHITE",
					"OCTAGONAL_TABLE_END_OCTAGONAL_WHITE",

					"TABLE_END_OCTAGONAL_DARK",
					"OCTAGONAL_TABLE_END_OCTAGONAL_DARK",

					"TABLE_END_OCTAGONAL_YELLOW",
					"OCTAGONAL_TABLE_END_OCTAGONAL_YELLOW",

					"TABLE_END_OCTAGONAL_GREEN",
					"OCTAGONAL_TABLE_END_OCTAGONAL_GREEN",

					"org.lgna.story.resources.prop.QuaintEndTable",
					"org.lgna.story.resources.prop.EndTable",

					"TABLE_END_TRIANGULAR_TILE_MARBLE_GREEN",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_GREEN",

					"TABLE_END_TRIANGULAR_TILE_MARBLE_CREAM",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_CREAM",

					"TABLE_END_TRIANGULAR_TILE_MARBLE_RED",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_RED",

					"TABLE_END_TRIANGULAR_TILE_MARBLE_WHITE",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_WHITE",

					"TABLE_END_TRIANGULAR_TILE_MARBLE_BLACK",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_BLACK",

					"TABLE_END_TRIANGULAR_TILE_WOOD_SANTA_MARIA",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_SANTA_MARIA",

					"TABLE_END_TRIANGULAR_TILE_WOOD_BLACKWOOD",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_BLACKWOOD",

					"TABLE_END_TRIANGULAR_TILE_WOOD_CHERRY",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_CHERRY",

					"TABLE_END_TRIANGULAR_TILE_WOOD_WHITE",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_WHITE",

					"TABLE_END_TRIANGULAR_TILE_WOOD_RED_OAK",
					"TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_RED_OAK",

					"org.lgna.story.resources.prop.TriangularEndTable",
					"org.lgna.story.resources.prop.EndTable",

					"BOOKCASE_ART_NOUVEAU_SURFACE",
					"ART_NOUVEAU_BOOKCASE_ART_NOUVEAU_SURFACE",

					"org.lgna.story.resources.prop.BookcaseArtNouveau",
					"org.lgna.story.resources.prop.Bookcase",

					"BOOKCASE_CINDERBLOCK_SHELVES_BLACKWASH",
					"CINDER_BLOCK_BOOKCASE_CINDERBLOCK_SHELVES_BLACKWASH",

					"BOOKCASE_CINDERBLOCK_SHELVES_KNOTTYPINE",
					"CINDER_BLOCK_BOOKCASE_CINDERBLOCK_SHELVES_KNOTTYPINE",

					"BOOKCASE_CINDERBLOCK_SHELVES_OLDWOOD",
					"CINDER_BLOCK_BOOKCASE_CINDERBLOCK_SHELVES_OLDWOOD",

					"org.lgna.story.resources.prop.BookcaseCinderblock",
					"org.lgna.story.resources.prop.Bookcase",

					"CHAIR_LIVING_ADIRONDACK_CUSHION_GRAY",
					"ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_GRAY",

					"CHAIR_LIVING_ADIRONDACK_CUSHION_CAMO",
					"ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_CAMO",

					"CHAIR_LIVING_ADIRONDACK_CUSHION_STRIPES",
					"ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_STRIPES",

					"CHAIR_LIVING_ADIRONDACK_CUSHION_POLKA",
					"ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_POLKA",

					"CHAIR_LIVING_ADIRONDACK_CUSHION_PALM",
					"ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_PALM",

					"org.lgna.story.resources.prop.LoveseatAdirondack",
					"org.lgna.story.resources.prop.Loveseat",

					"LOVESEAT_ART_NOUVEAU_FRAME_ANTIQUE",
					"ART_NOUVEAU_LOVESEAT_ART_NOUVEAU_FRAME_ANTIQUE",

					"LOVESEAT_ART_NOUVEAU_FRAME_MOHOGANY",
					"ART_NOUVEAU_LOVESEAT_ART_NOUVEAU_FRAME_MOHOGANY",

					"LOVESEAT_ART_NOUVEAU_FRAME_OAK",
					"ART_NOUVEAU_LOVESEAT_ART_NOUVEAU_FRAME_OAK",

					"org.lgna.story.resources.prop.LoveseatArtNouveau",
					"org.lgna.story.resources.prop.Loveseat",

					"LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_MAHOGONY",
					"CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_MAHOGONY",

					"LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_RED",
					"CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_RED",

					"LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_WHITE",
					"CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_WHITE",

					"LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_LIGHT_WOOD",
					"CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_LIGHT_WOOD",

					"LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_PINK_VELOUR",
					"CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_PINK_VELOUR",

					"LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_BLUE_VELOUR",
					"CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_BLUE_VELOUR",

					"LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_RED_VELOUR",
					"CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_RED_VELOUR",

					"LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_BLACK_VELOUR",
					"CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_BLACK_VELOUR",

					"LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_BEIGE_FABRIC",
					"CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_BEIGE_FABRIC",

					"LOVSEATLOFT_MODERN_FABRIC_BEIGE",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_BEIGE",

					"LOVSEATLOFT_MODERN_FABRIC_ORANGE",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_ORANGE",

					"LOVSEATLOFT_MODERN_FABRIC_WHITE",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_WHITE",

					"LOVSEATLOFT_MODERN_FABRIC_BLUE",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_BLUE",

					"LOVSEATLOFT_MODERN_FABRIC_GREEN",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_GREEN",

					"LOVSEATLOFT_MODERN_CUSHIONS_TAN",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_TAN",

					"LOVSEATLOFT_MODERN_CUSHIONS_GREEN",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_GREEN",

					"LOVSEATLOFT_MODERN_CUSHIONS_ORANGE",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_ORANGE",

					"LOVSEATLOFT_MODERN_CUSHIONS_RED",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_RED",

					"LOVSEATLOFT_MODERN_CUSHIONS_BLUE",
					"MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_BLUE",

					"org.lgna.story.resources.prop.LoveseatLoftModern",
					"org.lgna.story.resources.prop.Loveseat",

					"SOFA_MOROCCAN_BEIGECROSS",
					"MOROCCAN_SOFA_MOROCCAN_BEIGECROSS",

					"LOVESEAT_PARK_BENCH_WOOD",
					"PARK_BENCH_LOVESEAT_PARK_BENCH_WOOD",

					"SOFA_QUAINT_FABRIC_WHITE_FLOWERS",
					"QUAINT_SOFA_QUAINT_FABRIC_WHITE_FLOWERS",

					"SOFA_QUAINT_FABRIC_GREEN_FLOWERS",
					"QUAINT_SOFA_QUAINT_FABRIC_GREEN_FLOWERS",

					"SOFA_QUAINT_FABRIC_BEIGE_FLOWERS",
					"QUAINT_SOFA_QUAINT_FABRIC_BEIGE_FLOWERS",

					"SOFA_QUAINT_FABRIC_BLUE_FLOWERS",
					"QUAINT_SOFA_QUAINT_FABRIC_BLUE_FLOWERS",

					"SOFA_QUAINT_FABRIC_PINK_FLOWERS",
					"QUAINT_SOFA_QUAINT_FABRIC_PINK_FLOWERS",

					"DESK_CENTRAL_ASIAN_WALNUT",
					"CENTRAL_ASIAN_DESK_CENTRAL_ASIAN_WALNUT",

					"DESK_CENTRAL_ASIAN_AVODIRE",
					"CENTRAL_ASIAN_DESK_CENTRAL_ASIAN_AVODIRE",

					"org.lgna.story.resources.prop.DeskCentralAsian",
					"org.lgna.story.resources.prop.Desk",

					"org.lgna.story.resources.prop.DeskQuaint",
					"org.lgna.story.resources.prop.Desk",

					"SOFA_COLONIAL1_FRUITS",
					"COLONIAL1_SOFA_COLONIAL1_FRUITS",

					"SOFA_COLONIAL1_DIAMOND",
					"COLONIAL1_SOFA_COLONIAL1_DIAMOND",

					"SOFA_COLONIAL1_REDPATTERN",
					"COLONIAL1_SOFA_COLONIAL1_REDPATTERN",

					"SOFA_COLONIAL1_BEIGE",
					"COLONIAL1_SOFA_COLONIAL1_BEIGE",

					"SOFA_COLONIAL1_WHITE_DIAMOND",
					"COLONIAL1_SOFA_COLONIAL1_WHITE_DIAMOND",

					"SOFA_COLONIAL1_LINE_CURVES",
					"COLONIAL1_SOFA_COLONIAL1_LINE_CURVES",

					"org.lgna.story.resources.prop.SofaColonial1",
					"org.lgna.story.resources.prop.Sofa",

					"SOFA_COLONIAL2_NEONBLUE",
					"COLONIAL2_SOFA_COLONIAL2_NEONBLUE",

					"SOFA_COLONIAL2_GOLDDIAMOND",
					"COLONIAL2_SOFA_COLONIAL2_GOLDDIAMOND",

					"SOFA_COLONIAL2_GREEN_FLORAL",
					"COLONIAL2_SOFA_COLONIAL2_GREEN_FLORAL",

					"SOFA_COLONIAL2_ORANGE",
					"COLONIAL2_SOFA_COLONIAL2_ORANGE",

					"SOFA_COLONIAL2_RED_STRIPES",
					"COLONIAL2_SOFA_COLONIAL2_RED_STRIPES",

					"SOFA_COLONIAL2_WHITEDIAMOND",
					"COLONIAL2_SOFA_COLONIAL2_WHITEDIAMOND",

					"SOFA_COLONIAL2_BLUE_PATTERN",
					"COLONIAL2_SOFA_COLONIAL2_BLUE_PATTERN",

					"org.lgna.story.resources.prop.SofaColonial2",
					"org.lgna.story.resources.prop.Sofa",

					"SOFA_MODERN_STEEL_FRAME_FABRIC_LEATHER",
					"STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_LEATHER",

					"SOFA_MODERN_STEEL_FRAME_FABRIC_BLACKLEATHER",
					"STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_BLACKLEATHER",

					"SOFA_MODERN_STEEL_FRAME_FABRIC_CORDOROY",
					"STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_CORDOROY",

					"SOFA_MODERN_STEEL_FRAME_FABRIC_STRIPE",
					"STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_STRIPE",

					"SOFA_MODERN_STEEL_FRAME_FABRIC_GATOR",
					"STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_GATOR",

					"org.lgna.story.resources.prop.SofaSteelFrame",
					"org.lgna.story.resources.prop.Sofa",


					"org.lgna.story.resources.prop.SofaMoroccan",
					"org.lgna.story.resources.prop.Sofa",

//duplicates
//					"SOFA_MOROCCAN_BEIGECROSS",
//					"MOROCCAN_SOFA_MOROCCAN_BEIGECROSS",

//					"SOFA_QUAINT_FABRIC_WHITE_FLOWERS",
//					"QUAINT_SOFA_QUAINT_FABRIC_WHITE_FLOWERS",
//
//					"SOFA_QUAINT_FABRIC_GREEN_FLOWERS",
//					"QUAINT_SOFA_QUAINT_FABRIC_GREEN_FLOWERS",
//
//					"SOFA_QUAINT_FABRIC_BEIGE_FLOWERS",
//					"QUAINT_SOFA_QUAINT_FABRIC_BEIGE_FLOWERS",
//
//					"SOFA_QUAINT_FABRIC_BLUE_FLOWERS",
//					"QUAINT_SOFA_QUAINT_FABRIC_BLUE_FLOWERS",
//
//					"SOFA_QUAINT_FABRIC_PINK_FLOWERS",
//					"QUAINT_SOFA_QUAINT_FABRIC_PINK_FLOWERS",

					"SOFA_UM_CUTOUT_BLACK_CREAM",
					"MODERN_CUTOUT_SOFA_UM_CUTOUT_BLACK_CREAM",

					"SOFA_UM_CUTOUT_BLUE",
					"MODERN_CUTOUT_SOFA_UM_CUTOUT_BLUE",

					"SOFA_UM_CUTOUT_LEOPARD",
					"MODERN_CUTOUT_SOFA_UM_CUTOUT_LEOPARD",

					"SOFA_UM_CUTOUT_PURPLE",
					"MODERN_CUTOUT_SOFA_UM_CUTOUT_PURPLE",

					"SOFA_UM_CUTOUT_RED",
					"MODERN_CUTOUT_SOFA_UM_CUTOUT_RED",

					"SOFA_UM_CUTOUT_ZEBRA",
					"MODERN_CUTOUT_SOFA_UM_CUTOUT_ZEBRA",

					"SOFA_UM_CUTOUT_GREEN",
					"MODERN_CUTOUT_SOFA_UM_CUTOUT_GREEN",

					"org.lgna.story.resources.prop.SofaModernCutout",
					"org.lgna.story.resources.prop.Sofa",

					"SOFA_U_M_DIAMOND_CHECK",
					"MODERN_DIAMOND_SOFA_U_M_DIAMOND_CHECK",

					"SOFA_U_M_DIAMOND_BABYBLUE",
					"MODERN_DIAMOND_SOFA_U_M_DIAMOND_BABYBLUE",

					"SOFA_U_M_DIAMOND_BLACK",
					"MODERN_DIAMOND_SOFA_U_M_DIAMOND_BLACK",

					"SOFA_U_M_DIAMOND_YELLOW",
					"MODERN_DIAMOND_SOFA_U_M_DIAMOND_YELLOW",

					"SOFA_U_M_DIAMOND_PURPLE",
					"MODERN_DIAMOND_SOFA_U_M_DIAMOND_PURPLE",

					"SOFA_U_M_DIAMOND_RED",
					"MODERN_DIAMOND_SOFA_U_M_DIAMOND_RED",

					"org.lgna.story.resources.prop.SofaModernDiamond",
					"org.lgna.story.resources.prop.Sofa",

					"LIGHTING_FLOOR_CLUB_LAMP_LAMP",
					"SWING_ARM_LIGHTING_FLOOR_CLUB_LAMP_LAMP",

					"org.lgna.story.resources.prop.FloorLampSwingArm",
					"org.lgna.story.resources.prop.Lamp",

					"org.lgna.story.resources.prop.FloorLampDesigner",
					"org.lgna.story.resources.prop.Lamp",

					"LIGHTING_FLOOR_GARDEN_TIER_LIT",
					"GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER_LIT",

					"LIGHTING_FLOOR_GARDEN_TIER",
					"GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER",

					"LIGHTING_FLOOR_GARDEN_TIER_GREEN_LIT",
					"GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER_GREEN_LIT",

					"LIGHTING_FLOOR_GARDEN_TIER_GREEN",
					"GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER_GREEN",

					"org.lgna.story.resources.prop.FloorLampGardenBollard",
					"org.lgna.story.resources.prop.Lamp",

					"org.lgna.story.resources.prop.FloorLampGardenTier",
					"org.lgna.story.resources.prop.Lamp",

					"org.lgna.story.resources.prop.FloorLampLoft",
					"org.lgna.story.resources.prop.Lamp",

					"org.lgna.story.resources.prop.FloorLampMoroccan",
					"org.lgna.story.resources.prop.Lamp",

					"org.lgna.story.resources.prop.FloorLampQuaint",
					"org.lgna.story.resources.prop.Lamp",

					"org.lgna.story.resources.prop.FloorLampStudio",
					"org.lgna.story.resources.prop.Lamp",

					"org.lgna.story.resources.prop.FloorLampValue",
					"org.lgna.story.resources.prop.Lamp",

					"CHAIR_DINING_ART_NOUVEAU_LIGHT_CLEAN",
					"ART_NOUVEAU_CHAIR_DINING_ART_NOUVEAU_LIGHT_CLEAN",

					"CHAIR_DINING_ART_NOUVEAU_MID_CLEAN",
					"ART_NOUVEAU_CHAIR_DINING_ART_NOUVEAU_MID_CLEAN",

					"CHAIR_DINING_ART_NOUVEAU_DARK_CLEAN",
					"ART_NOUVEAU_CHAIR_DINING_ART_NOUVEAU_DARK_CLEAN",

					"org.lgna.story.resources.prop.ArtNouveauDiningChair",
					"org.lgna.story.resources.prop.Chair",

					"org.lgna.story.resources.prop.ColonialDiningChair",
					"org.lgna.story.resources.prop.Chair",

					"CHAIR_DINING_COLONIAL2_GOLDFLOWER",
					"FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_GOLDFLOWER",

					"CHAIR_DINING_COLONIAL2_SRIPE",
					"FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_SRIPE",

					"CHAIR_DINING_COLONIAL2_GOLDPATTERN",
					"FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_GOLDPATTERN",

					"CHAIR_DINING_COLONIAL2_BEIGE",
					"FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_BEIGE",

					"CHAIR_DINING_COLONIAL2_BLUEPATTERN",
					"FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_BLUEPATTERN",

					"CHAIR_DINING_COLONIAL2_BLUESILK",
					"FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_BLUESILK",

					"CHAIR_DINING_COLONIAL2_DIAMONDPATTERN",
					"FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_DIAMONDPATTERN",

					"org.lgna.story.resources.prop.FancyColonialDiningChair",
					"org.lgna.story.resources.prop.Chair",

					"CHAIR_DINING_DANISH_MODERN_CUSHIONS_GREEN",
					"DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_GREEN",

					"CHAIR_DINING_DANISH_MODERN_CUSHIONS_BABY_BLUE",
					"DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_BABY_BLUE",

					"CHAIR_DINING_DANISH_MODERN_CUSHIONS_POLKA",
					"DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_POLKA",

					"CHAIR_DINING_DANISH_MODERN_CUSHIONS_WHITE",
					"DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_WHITE",

					"CHAIR_DINING_DANISH_MODERN_CUSHIONS_RED",
					"DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_RED",

					"org.lgna.story.resources.prop.DanishModernDiningChair",
					"org.lgna.story.resources.prop.Chair",

					"CHAIR_DINING_LOFT_SEAT_BLUE",
					"LOFT_FORK_CHAIR_DINING_LOFT_SEAT_BLUE",

					"CHAIR_DINING_LOFT_SEAT_GREEN",
					"LOFT_FORK_CHAIR_DINING_LOFT_SEAT_GREEN",

					"CHAIR_DINING_LOFT_SEAT_RED",
					"LOFT_FORK_CHAIR_DINING_LOFT_SEAT_RED",

					"CHAIR_DINING_LOFT_SEAT_TAN",
					"LOFT_FORK_CHAIR_DINING_LOFT_SEAT_TAN",

					"CHAIR_DINING_LOFT_SEAT_ORANGE",
					"LOFT_FORK_CHAIR_DINING_LOFT_SEAT_ORANGE",

					"CHAIR_DINING_LOFT_FORK_BASE_WOOD_LIGHT",
					"LOFT_FORK_CHAIR_DINING_LOFT_FORK_BASE_WOOD_LIGHT",

					"CHAIR_DINING_LOFT_FORK_BASE_WOOD_ORANGE",
					"LOFT_FORK_CHAIR_DINING_LOFT_FORK_BASE_WOOD_ORANGE",

					"CHAIR_DINING_LOFT_FORK_BASE_IRON",
					"LOFT_FORK_CHAIR_DINING_LOFT_FORK_BASE_IRON",

					"CHAIR_DINING_LOFT_FORK_BASE_WOOD_RED",
					"LOFT_FORK_CHAIR_DINING_LOFT_FORK_BASE_WOOD_RED",

					"org.lgna.story.resources.prop.LoftForkDiningChair",
					"org.lgna.story.resources.prop.Chair",

					"org.lgna.story.resources.prop.LoftOfficeDiningChair",
					"org.lgna.story.resources.prop.Chair",

					"org.lgna.story.resources.prop.ModerateDiningChair",
					"org.lgna.story.resources.prop.Chair",

					"org.lgna.story.resources.prop.MoroccanDiningChair",
					"org.lgna.story.resources.prop.Chair",

					createPrevBipedJointString( "LEFT_THUMB_1" ),
					createNextBipedJointString( "LEFT_THUMB" ),

					createPrevBipedJointString( "LEFT_THUMB_2" ),
					createNextBipedJointString( "LEFT_THUMB_KNUCKLE" ),

					createPrevBipedJointString( "LEFT_INDEX_1" ),
					createNextBipedJointString( "LEFT_INDEX_FINGER" ),

					createPrevBipedJointString( "LEFT_INDEX_2" ),
					createNextBipedJointString( "LEFT_INDEX_FINGER_KNUCKLE" ),

					createPrevBipedJointString( "LEFT_MIDDLE_1" ),
					createNextBipedJointString( "LEFT_MIDDLE_FINGER" ),

					createPrevBipedJointString( "LEFT_MIDDLE_2" ),
					createNextBipedJointString( "LEFT_MIDDLE_FINGER_KNUCKLE" ),
					
					createPrevBipedJointString( "LEFT_PINKY_1" ),
					createNextBipedJointString( "LEFT_PINKY_FINGER" ),

					createPrevBipedJointString( "LEFT_PINKY_2" ),
					createNextBipedJointString( "LEFT_PINKY_FINGER_KNUCKLE" ),
					
					createPrevBipedJointString( "RIGHT_THUMB_1" ),
					createNextBipedJointString( "RIGHT_THUMB" ),

					createPrevBipedJointString( "RIGHT_THUMB_2" ),
					createNextBipedJointString( "RIGHT_THUMB_KNUCKLE" ),

					createPrevBipedJointString( "RIGHT_INDEX_1" ),
					createNextBipedJointString( "RIGHT_INDEX_FINGER" ),

					createPrevBipedJointString( "RIGHT_INDEX_2" ),
					createNextBipedJointString( "RIGHT_INDEX_FINGER_KNUCKLE" ),

					createPrevBipedJointString( "RIGHT_MIDDLE_1" ),
					createNextBipedJointString( "RIGHT_MIDDLE_FINGER" ),

					createPrevBipedJointString( "RIGHT_MIDDLE_2" ),
					createNextBipedJointString( "RIGHT_MIDDLE_FINGER_KNUCKLE" ),
					
					createPrevBipedJointString( "RIGHT_PINKY_1" ),
					createNextBipedJointString( "RIGHT_PINKY_FINGER" ),

					createPrevBipedJointString( "RIGHT_PINKY_2" ),
					createNextBipedJointString( "RIGHT_PINKY_FINGER_KNUCKLE" )
			), };
	private static final java.util.List<Migration> versionIndependentMigrations = edu.cmu.cs.dennisc.java.util.concurrent.Collections
			.newCopyOnWriteArrayList();

	private MigrationManager() {
		throw new AssertionError();
	}

	public static boolean isDevoidOfVersionIndependentMigrations() {
		return versionIndependentMigrations.size() == 0;
	}

	public static String migrate(String source, org.lgna.project.Version version) {
		String rv = source;
		for (TextMigration textMigration : textMigrations) {
			if (textMigration.isApplicable(version)) {
				rv = textMigration.migrate(rv);
				version = textMigration.getResultVersion();
			}
		}

		for (Migration versionIndependentMigration : versionIndependentMigrations) {
			rv = versionIndependentMigration.migrate(rv);
		}

		return rv;
	}

	public static void addVersionIndependentMigration(Migration migration) {
		versionIndependentMigrations.add(migration);
	}

	public static void removeVersionIndependentMigration(Migration migration) {
		versionIndependentMigrations.remove(migration);
	}
}
