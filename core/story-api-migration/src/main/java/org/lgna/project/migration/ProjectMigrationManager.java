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
package org.lgna.project.migration;

/**
 * @author Dennis Cosgrove
 */
public class ProjectMigrationManager extends AbstractMigrationManager {
	private static final String PATTERN_WHITESPACE = "\\s*";
	private static final String REPLACEMENT_WHITESPACE = " ";

	// @formatter:off
	private static String createMoreSpecificFieldString( String fieldName, String clsName, String whitespace ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "name=\"" );
		sb.append( fieldName );
		sb.append( "\">" );
		sb.append( whitespace );
		sb.append( "<declaringClass name=\"" );
		sb.append( clsName );
		sb.append( "\"" );
		return sb.toString();
	}

	private static String createMoreSpecificFieldPattern( String fieldName, String clsName ) {
		return createMoreSpecificFieldString( fieldName, clsName, PATTERN_WHITESPACE );
	}

	private static String createMoreSpecificFieldReplacement( String fieldName, String clsName ) {
		return createMoreSpecificFieldString( fieldName, clsName, REPLACEMENT_WHITESPACE );
	}

	private static String createPrevJointString( String prevFieldName, String packageSubName ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "name=\"" );
		sb.append( prevFieldName );
		sb.append( "\">" );
		sb.append( PATTERN_WHITESPACE );
		sb.append( "<declaringClass name=\"org\\.lgna\\.story\\.resources." );
		sb.append( packageSubName );
		sb.append( "\\.[A-Za-z]*\"" );
		return sb.toString();
	}

	private static String createNextJointString( String prevFieldName, String clsName ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "name=\"" );
		sb.append( prevFieldName );
		sb.append( "\">" );
		sb.append( REPLACEMENT_WHITESPACE );
		sb.append( "<declaringClass name=\"org.lgna.story.resources." );
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

	private static String createPrevQuadrupedJointString( String prevFieldName ) {
		return createPrevJointString( prevFieldName, "quadruped" );
	}

	private static String createNextQuadrupedJointString( String prevFieldName ) {
		return createNextJointString( prevFieldName, "QuadrupedResource" );
	}

	private static String createPrevFlyerJointString( String prevFieldName ) {
		return createPrevJointString( prevFieldName, "flyer" );
	}

	private static String createNextFlyerJointString( String prevFieldName ) {
		return createNextJointString( prevFieldName, "FlyerResource" );
	}

	private static String createPrevSwimmerJointString( String prevFieldName ) {
		return createPrevJointString( prevFieldName, "swimmer" );
	}

	private static String createNextSwimmerJointString( String prevFieldName ) {
		return createNextJointString( prevFieldName, "SwimmerResource" );
	}

	private static String createJointAccessorString( String accessorName, String clsName, String whitespace ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "name=\"" );
		sb.append( accessorName );
		sb.append( "\">" );
		sb.append( whitespace );
		sb.append( "<declaringClass name=\"org.lgna.story." );
		sb.append( clsName );
		sb.append( "\"" );
		return sb.toString();
	}

	private static String createJointAccessorPattern( String accessorName, String clsName ) {
		return createJointAccessorString( accessorName, clsName, PATTERN_WHITESPACE );
	}

	private static String createJointAccessorReplacement( String accessorName, String clsName ) {
		return createJointAccessorString( accessorName, clsName, REPLACEMENT_WHITESPACE );
	}

	private static String createJointIdString( String fieldName, String subPackageAndClassName, String whitespace ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "name=\"" );
		sb.append( fieldName );
		sb.append( "\">" );
		sb.append( PATTERN_WHITESPACE );
		sb.append( "<declaringClass name=\"org.lgna.story.resources." );
		sb.append( subPackageAndClassName );
		sb.append( "\"" );
		return sb.toString();
	}

	private static String CACHE_FROM_PREVIOUS_CALL_subPackageAndClassName;

	private static String createJointIdPattern( String prevFieldName, String subPackageAndClassName ) {
		CACHE_FROM_PREVIOUS_CALL_subPackageAndClassName = subPackageAndClassName;
		return createJointIdString( prevFieldName, subPackageAndClassName, PATTERN_WHITESPACE );
	}

	private static String createJointIdReplacement( String nextFieldName ) {
		assert CACHE_FROM_PREVIOUS_CALL_subPackageAndClassName != null : nextFieldName;
		try {
			return createJointIdString( nextFieldName, CACHE_FROM_PREVIOUS_CALL_subPackageAndClassName, PATTERN_WHITESPACE );
		} finally {
			CACHE_FROM_PREVIOUS_CALL_subPackageAndClassName = null;
		}
	}

	//	private static String createJointIdReplacement_NoLongerExistsCondition( String nextFieldNameToUseDespitePotentialProblems ) {
	//		return createJointIdReplacement( nextFieldNameToUseDespitePotentialProblems );
	//	}

	private final TextMigration[] textMigrations = {
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.7.0.0" ),
					new org.lgna.project.Version( "3.1.8.0.0" ) ),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.8.0.0" ),
					new org.lgna.project.Version( "3.1.9.0.0" ),

					"ARMOIRE_CLOTHING", NO_REPLACEMENT,

					"org.lgna.story.resources.armoire.ArmoireArtNouveau",
					NO_REPLACEMENT,

					"PINK_POODLE", NO_REPLACEMENT,

					"org.lgna.story.resources.quadruped.Poodle", NO_REPLACEMENT ),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.9.0.0" ),
					new org.lgna.project.Version( "3.1.11.0.0" ) ),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.11.0.0" ),
					new org.lgna.project.Version( "3.1.14.0.0" ),

					"CAMEL", NO_REPLACEMENT,

					"org.lgna.story.resources.quadruped.Camel", NO_REPLACEMENT,

					"FALCON", NO_REPLACEMENT,

					"org.lgna.story.resources.flyer.Falcon", NO_REPLACEMENT,

					"LION", NO_REPLACEMENT,

					"org.lgna.story.resources.quadruped.Lion", NO_REPLACEMENT,

					"WOLF", NO_REPLACEMENT,

					"org.lgna.story.resources.quadruped.Wolf", NO_REPLACEMENT ),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.14.0.0" ),
					new org.lgna.project.Version( "3.1.15.1.0" ) ),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.15.1.0" ),
					new org.lgna.project.Version( "3.1.20.0.0" ),

					"org.lgna.story.resources.dresser.DresserCentralAsian",
					"org.lgna.story.resources.prop.DresserCentralAsian",

					"org.lgna.story.resources.dresser.DresserColonial",
					"org.lgna.story.resources.prop.DresserColonial",

					"org.lgna.story.resources.dresser.DresserDesigner",
					"org.lgna.story.resources.prop.DresserDesigner" ),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.20.0.0" ),
					new org.lgna.project.Version( "3.1.33.0.0" ) ),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.33.0.0" ),
					new org.lgna.project.Version( "3.1.34.0.0" ),

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

					"org.lgna.story.resources.ArmoireResource",
					"org.lgna.story.resources.prop.Armoire",

					"org.lgna.story.resources.armoire.ArmoireLoft",
					"org.lgna.story.resources.prop.Armoire",

					"org.lgna.story.resources.armoire.ArmoireQuaint",
					"org.lgna.story.resources.prop.Armoire",

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
					"org.lgna.story.resources.prop.Armoire",

					"_ARMOIRE_MOROCCAN_GREEN",
					"MOROCCAN_GREEN",

					"_ARMOIRE_MOROCCAN_BLUE",
					"MOROCCAN_BLUE",

					"_ARMOIRE_MOROCCAN_RED",
					"MOROCCAN_RED",

					"org.lgna.story.resources.armoire.ArmoireMoroccan",
					"org.lgna.story.resources.prop.Armoire",

					"UFO__GLOW",
					"UFO",

					"UFO__ZAP2",
					"UFO",

					"CANDY_FACTORY_SURFACE",
					"CANDY_FACTORY",

					"org.lgna.story.resources.prop.ArtNoveauCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"org.lgna.story.resources.prop.SmallClubCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					"org.lgna.story.resources.prop.LargeClubCoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTable",

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD", "org.lgna.story.resources.prop.CoffeeTable" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD", "org.lgna.story.resources.prop.CoffeeTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK", "org.lgna.story.resources.prop.CoffeeTable" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK", "org.lgna.story.resources.prop.CoffeeTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED", "org.lgna.story.resources.prop.CoffeeTable" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED", "org.lgna.story.resources.prop.CoffeeTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG", "org.lgna.story.resources.prop.CoffeeTable" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG", "org.lgna.story.resources.prop.CoffeeTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD", "org.lgna.story.resources.prop.CoffeeTable" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD", "org.lgna.story.resources.prop.CoffeeTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH", "org.lgna.story.resources.prop.CoffeeTable" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH", "org.lgna.story.resources.prop.CoffeeTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK", "org.lgna.story.resources.prop.CoffeeTable" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK", "org.lgna.story.resources.prop.CoffeeTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE", "org.lgna.story.resources.prop.CoffeeTable" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE", "org.lgna.story.resources.prop.CoffeeTable" ),

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

					"name=\"TABLE_DINING_CLUB_ROOT",
					"name=\"CLUB_TABLE_DINING_CLUB_ROOT",

					"name=\"TABLE_DINING_CLUB_NEDAR",
					"name=\"CLUB_TABLE_DINING_CLUB_NEDAR",

					"name=\"TABLE_DINING_CLUB_OAK",
					"name=\"CLUB_TABLE_DINING_CLUB_OAK",

					"name=\"TABLE_DINING_CLUB_RED",
					"name=\"CLUB_TABLE_DINING_CLUB_RED",

					"name=\"TABLE_DINING_CLUB_REDDARK",
					"name=\"CLUB_TABLE_DINING_CLUB_REDDARK",

					"name=\"TABLE_DINING_CLUB_WOOD",
					"name=\"CLUB_TABLE_DINING_CLUB_WOOD",

					"name=\"TABLE_DINING_CLUB_PINE",
					"name=\"CLUB_TABLE_DINING_CLUB_PINE",

					"name=\"TABLE_DINING_CLUB_SEDAR",
					"name=\"CLUB_TABLE_DINING_CLUB_SEDAR",

					"org.lgna.story.resources.prop.ClubDiningTable",
					"org.lgna.story.resources.prop.DiningTable",

					"name=\"TABLE_DINING_MOROCCAN_TURQ",
					"name=\"MOROCCAN_TABLE_DINING_MOROCCAN_TURQ",

					"name=\"TABLE_DINING_MOROCCAN_BLUE",
					"name=\"MOROCCAN_TABLE_DINING_MOROCCAN_BLUE",

					"name=\"TABLE_DINING_MOROCCAN_BLUE_LIGHT",
					"name=\"MOROCCAN_TABLE_DINING_MOROCCAN_BLUE_LIGHT",

					"name=\"TABLE_DINING_MOROCCAN_GREEN",
					"name=\"MOROCCAN_TABLE_DINING_MOROCCAN_GREEN",

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

					"org.lgna.story.resources.prop.ClubEndTable",
					"org.lgna.story.resources.prop.EndTable",

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD", "org.lgna.story.resources.prop.EndTable" ),
					createMoreSpecificFieldReplacement( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD", "org.lgna.story.resources.prop.EndTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK", "org.lgna.story.resources.prop.EndTable" ),
					createMoreSpecificFieldReplacement( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK", "org.lgna.story.resources.prop.EndTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED", "org.lgna.story.resources.prop.EndTable" ),
					createMoreSpecificFieldReplacement( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED", "org.lgna.story.resources.prop.EndTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG", "org.lgna.story.resources.prop.EndTable" ),
					createMoreSpecificFieldReplacement( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG", "org.lgna.story.resources.prop.EndTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD", "org.lgna.story.resources.prop.EndTable" ),
					createMoreSpecificFieldReplacement( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD", "org.lgna.story.resources.prop.EndTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH", "org.lgna.story.resources.prop.EndTable" ),
					createMoreSpecificFieldReplacement( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH", "org.lgna.story.resources.prop.EndTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK", "org.lgna.story.resources.prop.EndTable" ),
					createMoreSpecificFieldReplacement( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK", "org.lgna.story.resources.prop.EndTable" ),

					createMoreSpecificFieldPattern( "TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE", "org.lgna.story.resources.prop.EndTable" ),
					createMoreSpecificFieldReplacement( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE", "org.lgna.story.resources.prop.EndTable" ),

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

					createMoreSpecificFieldPattern( "SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldPattern( "SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldPattern( "SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.Loveseat" ),

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.Loveseat" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.Loveseat" ),

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
					"CENTRAL_ASIAN_DESK_CENTRAL_ASIAN_RED",

					"DESK_CENTRAL_ASIAN_BLACK",
					"CENTRAL_ASIAN_DESK_CENTRAL_ASIAN_BLACK",

					"DESK_CLUB__DARKWOOD",
					"CLUB_DESK_CLUB_DARKWOOD",

					"DESK_CLUB__ASH",
					"CLUB_DESK_CLUB_ASH",

					"DESK_CLUB__REDWOOD",
					"CLUB_DESK_CLUB_REDWOOD",

					"org.lgna.story.resources.prop.DeskClub",
					"org.lgna.story.resources.prop.Desk",

					"DESK_QUAINT_GREEN",
					"QUAINT_DESK_QUAINT_GREEN",

					"DESK_QUAINT_WHITE",
					"QUAINT_DESK_QUAINT_WHITE",

					"DESK_QUAINT_RED",
					"QUAINT_DESK_QUAINT_RED",

					"DESK_QUAINT_BLUE",
					"QUAINT_DESK_QUAINT_BLUE",

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

					"org.lgna.story.resources.prop.SofaQuaint",
					"org.lgna.story.resources.prop.Sofa",

					"org.lgna.story.resources.prop.SofaValue2",
					"org.lgna.story.resources.prop.Sofa",

					"org.lgna.story.resources.prop.SofaSteelFrame",
					"org.lgna.story.resources.prop.Sofa",

					"org.lgna.story.resources.prop.SofaColonial1",
					"org.lgna.story.resources.prop.Sofa",

					"org.lgna.story.resources.prop.SofaColonial2",
					"org.lgna.story.resources.prop.Sofa",

					"org.lgna.story.resources.prop.SofaMoroccan",
					"org.lgna.story.resources.prop.Sofa",

					"org.lgna.story.resources.prop.SofaModernCutout",
					"org.lgna.story.resources.prop.Sofa",

					"org.lgna.story.resources.prop.SofaModernDiamond",
					"org.lgna.story.resources.prop.Sofa",

					createMoreSpecificFieldPattern( "SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.Sofa" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.Sofa" ),

					createMoreSpecificFieldPattern( "SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.Sofa" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.Sofa" ),

					createMoreSpecificFieldPattern( "SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.Sofa" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.Sofa" ),

					"SOFA_VALUE2_LIGHT_BROWN_FLOWER",
					"VALUE2_SOFA_VALUE2_LIGHT_BROWN_FLOWER",

					"SOFA_VALUE2_RED_CHECKER",
					"VALUE2_SOFA_VALUE2_RED_CHECKER",

					"SOFA_VALUE2_GREEN_FLOWER",
					"VALUE2_SOFA_VALUE2_GREEN_FLOWER",

					"SOFA_VALUE2_BLUE_FLOWER_BORDER",
					"VALUE2_SOFA_VALUE2_BLUE_FLOWER_BORDER",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADE",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADE",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADEON",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADEON",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADE",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADE",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADEON",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADEON",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADE",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADE",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADEON",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADEON",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADEON",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADEON",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADE",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADE",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADE",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADE",

					"name=\"LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADEON",
					"name=\"DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADEON",

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

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_PINK_LIT",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_PINK_LIT",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_BEIGE_LIT",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BEIGE_LIT",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_BEIGE",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BEIGE",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_WHITE",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_WHITE",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_PINK",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_PINK",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_BLUE",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BLUE",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_YELLOW",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_YELLOW",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_GREEN",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_GREEN",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_BLUE_LIT",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BLUE_LIT",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_GREEN_LIT",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_GREEN_LIT",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_WHITE_LIT",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_WHITE_LIT",

					"name=\"LIGHTING_FLOOR_QUAINT_SHADE_YELLOW_LIT",
					"name=\"QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_YELLOW_LIT",

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
					"org.lgna.story.resources.prop.Chair",

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

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldReplacement( "PARK_LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.Chair" ),

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldReplacement( "PARK_LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.Chair" ),

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldReplacement( "PARK_LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.Chair" ),

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldReplacement( "PARK_LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.Chair" ),

					createMoreSpecificFieldPattern( "LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.Chair" ),
					createMoreSpecificFieldReplacement( "PARK_LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.Chair" ),

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

					"PIG_OVERALLS",
					"PIG",

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

					"PLAYING_CARD_EIGHT",
					"CARD08",

					"PLAYING_CARD_FIVE",
					"CARD05",

					"PLAYING_CARD_FOUR",
					"CARD04",

					"name=\"PLAYING_CARD",
					"name=\"BLANK",

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
					"org.lgna.story.resources.prop.Armoire",

					"ARMOIRE_COLONIAL_WOOD_REDWOODCURLY",
					"COLONIAL_CURLY_REDWOOD",

					"ARMOIRE_COLONIAL_WOOD_DARKWOODQUILTED",
					"COLONIAL_QUILTED_DARK_WOOD",

					"ARMOIRE_COLONIAL_WOOD_LIGHTWOODCURLY",
					"COLONIAL_CURLY_LIGHT_WOOD",

					"name=\"DRESSER_CENTRAL_ASIAN_GREEN_FLOWERS",
					"name=\"CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_GREEN_FLOWERS",

					"name=\"DRESSER_CENTRAL_ASIAN_RED_FLOWERS",
					"name=\"CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_RED_FLOWERS",

					"name=\"DRESSER_CENTRAL_ASIAN_GREEN",
					"name=\"CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_GREEN",

					"name=\"DRESSER_CENTRAL_ASIAN_RED",
					"name=\"CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_RED",

					"org.lgna.story.resources.prop.DresserCentralAsian",
					"org.lgna.story.resources.prop.Dresser",

					"name=\"DRESSER_COLONIAL_WOOD",
					"name=\"COLONIAL_DRESSER_COLONIAL_WOOD",

					"name=\"DRESSER_COLONIAL_LIGHT_WOOD_CURLY",
					"name=\"COLONIAL_DRESSER_COLONIAL_LIGHT_WOOD_CURLY",

					"name=\"DRESSER_COLONIAL_RED_WOOD",
					"name=\"COLONIAL_DRESSER_COLONIAL_RED_WOOD",

					"name=\"DRESSER_COLONIAL_WOOD_STRAIGHT_DARK",
					"name=\"COLONIAL_DRESSER_COLONIAL_WOOD_STRAIGHT_DARK",

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
					"org.lgna.story.resources.prop.Dresser",

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

					"LIGHTING_FLOOR_CLUB_LAMP_LAMP",
					"SWING_ARM_LIGHTING_FLOOR_CLUB_LAMP_LAMP",

					"org.lgna.story.resources.prop.FloorLampSwingArm",
					"org.lgna.story.resources.prop.Lamp",

					"org.lgna.story.resources.prop.FloorLampDesigner",
					"org.lgna.story.resources.prop.Lamp",

					"name=\"LIGHTING_FLOOR_GARDEN_TIER_LIT",
					"name=\"GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER_LIT",

					"name=\"LIGHTING_FLOOR_GARDEN_TIER_GREEN_LIT",
					"name=\"GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER_GREEN_LIT",

					"name=\"LIGHTING_FLOOR_GARDEN_TIER_GREEN",
					"name=\"GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER_GREEN",

					"name=\"LIGHTING_FLOOR_GARDEN_TIER",
					"name=\"GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER",

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

					"SAUCER_QUEEN_OF_HEARTS",
					"SAUCER_QUEEN",

					"TEACUP_QUEEN_OF_HEARTS",
					"TEACUP_QUEEN",

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
					createNextBipedJointString( "RIGHT_PINKY_FINGER_KNUCKLE" ),

					createPrevQuadrupedJointString( "TAIL_1" ),
					createNextQuadrupedJointString( "TAIL" ),

					createPrevFlyerJointString( "TAIL_1" ),
					createNextFlyerJointString( "TAIL" ),

					createJointAccessorPattern( "getRightClavicle", "SFlyer" ),
					createJointAccessorReplacement( "getRightWingShoulder", "SFlyer" ),

					createJointAccessorPattern( "getLeftClavicle", "SFlyer" ),
					createJointAccessorReplacement( "getLeftWingShoulder", "SFlyer" ),

					createJointAccessorPattern( "getLeftShoulder", "SFlyer" ),
					createJointAccessorReplacement( "getLeftWingElbow", "SFlyer" ),

					createJointAccessorPattern( "getRightShoulder", "SFlyer" ),
					createJointAccessorReplacement( "getRightWingElbow", "SFlyer" ),

					createJointAccessorPattern( "getRightElbow", "SFlyer" ),
					createJointAccessorReplacement( "getRightWingWrist", "SFlyer" ),

					createJointAccessorPattern( "getLeftElbow", "SFlyer" ),
					createJointAccessorReplacement( "getLeftWingWrist", "SFlyer" ),

					createJointAccessorPattern( "getLeftPectoralFin", "SSwimmer" ),
					createJointAccessorReplacement( "getFrontRightFin", "SSwimmer" ),

					createJointAccessorPattern( "getRightPectoralFin", "SSwimmer" ),
					createJointAccessorReplacement( "getFrontRightFin", "SSwimmer" )
			),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.34.0.0" ),
					new org.lgna.project.Version( "3.1.35.0.0" ),

					"org.lgna.story.resources.biped.Alien",
					"org.lgna.story.resources.biped.AlienResource",

					"org.lgna.story.resources.biped.BabyYeti",
					"org.lgna.story.resources.biped.BabyYetiResource",

					"org.lgna.story.resources.biped.BigBadWolf",
					"org.lgna.story.resources.biped.BigBadWolfResource",

					"org.lgna.story.resources.biped.Bunny",
					"org.lgna.story.resources.biped.BunnyResource",

					"org.lgna.story.resources.biped.CheshireCat",
					"org.lgna.story.resources.biped.CheshireCatResource",

					"org.lgna.story.resources.biped.Hare",
					"org.lgna.story.resources.biped.HareResource",

					"org.lgna.story.resources.biped.MadHatter",
					"org.lgna.story.resources.biped.MadHatterResource",

					"org.lgna.story.resources.biped.MarchHare",
					"org.lgna.story.resources.biped.MarchHareResource",

					"org.lgna.story.resources.biped.Ogre",
					"org.lgna.story.resources.biped.OgreResource",

					"org.lgna.story.resources.biped.Pig",
					"org.lgna.story.resources.biped.PigResource",

					"org.lgna.story.resources.biped.Pixie",
					"org.lgna.story.resources.biped.PixieResource",

					"org.lgna.story.resources.biped.PlayingCard",
					"org.lgna.story.resources.biped.PlayingCardResource",

					"org.lgna.story.resources.biped.QueenOfHearts",
					"org.lgna.story.resources.biped.QueenOfHeartsResource",

					"org.lgna.story.resources.biped.StuffedTiger",
					"org.lgna.story.resources.biped.StuffedTigerResource",

					"org.lgna.story.resources.biped.Tortoise",
					"org.lgna.story.resources.biped.TortoiseResource",

					"org.lgna.story.resources.biped.Troll",
					"org.lgna.story.resources.biped.TrollResource",

					"org.lgna.story.resources.biped.WhiteRabbit",
					"org.lgna.story.resources.biped.WhiteRabbitResource",

					"org.lgna.story.resources.biped.Witch",
					"org.lgna.story.resources.biped.WitchResource",

					"org.lgna.story.resources.biped.Yeti",
					"org.lgna.story.resources.biped.YetiResource",

					"org.lgna.story.resources.flyer.Chicken",
					"org.lgna.story.resources.flyer.ChickenResource",

					"org.lgna.story.resources.flyer.Falcon",
					"org.lgna.story.resources.flyer.FalconResource",

					"org.lgna.story.resources.flyer.Flamingo",
					"org.lgna.story.resources.flyer.FlamingoResource",

					"org.lgna.story.resources.flyer.Owl",
					"org.lgna.story.resources.flyer.OwlResource",

					"org.lgna.story.resources.flyer.Penguin",
					"org.lgna.story.resources.flyer.PenguinResource",

					"org.lgna.story.resources.flyer.Seagull",
					"org.lgna.story.resources.flyer.SeagullResource",

					"org.lgna.story.resources.flyer.Toucan",
					"org.lgna.story.resources.flyer.ToucanResource",

					"org.lgna.story.resources.prop.Armoire",
					"org.lgna.story.resources.prop.ArmoireResource",

					"org.lgna.story.resources.prop.Bookcase",
					"org.lgna.story.resources.prop.BookcaseResource",

					"org.lgna.story.resources.prop.Boulder",
					"org.lgna.story.resources.prop.BoulderResource",

					"org.lgna.story.resources.prop.BowlingPin",
					"org.lgna.story.resources.prop.BowlingPinResource",

					"org.lgna.story.resources.prop.Cake",
					"org.lgna.story.resources.prop.CakeResource",

					"org.lgna.story.resources.prop.CandyFactory",
					"org.lgna.story.resources.prop.CandyFactoryResource",

					"org.lgna.story.resources.prop.CastleGate",
					"org.lgna.story.resources.prop.CastleGateResource",

					"org.lgna.story.resources.prop.CastleTowerBase",
					"org.lgna.story.resources.prop.CastleTowerBaseResource",

					"org.lgna.story.resources.prop.CastleTowerMiddle",
					"org.lgna.story.resources.prop.CastleTowerMiddleResource",

					"org.lgna.story.resources.prop.CastleTowerTop",
					"org.lgna.story.resources.prop.CastleTowerTopResource",

					"org.lgna.story.resources.prop.CastleWall",
					"org.lgna.story.resources.prop.CastleWallResource",

					"declaringClass name=\"org.lgna.story.resources.prop.Cauldron\"",
					"declaringClass name=\"org.lgna.story.resources.prop.CauldronResource\"",

					"declaringClass name=\"org.lgna.story.resources.prop.CauldronLid\"",
					"declaringClass name=\"org.lgna.story.resources.prop.CauldronLidResource\"",

					"org.lgna.story.resources.prop.Cave",
					"org.lgna.story.resources.prop.CaveResource",

					"org.lgna.story.resources.prop.Chair",
					"org.lgna.story.resources.prop.ChairResource",

					"org.lgna.story.resources.prop.CoffeeTable",
					"org.lgna.story.resources.prop.CoffeeTableResource",

					"org.lgna.story.resources.prop.ColaBottle",
					"org.lgna.story.resources.prop.ColaBottleResource",

					"org.lgna.story.resources.prop.CoralShelf",
					"org.lgna.story.resources.prop.CoralShelfResource",

					"org.lgna.story.resources.prop.Desk",
					"org.lgna.story.resources.prop.DeskResource",

					"org.lgna.story.resources.prop.DiningTable",
					"org.lgna.story.resources.prop.DiningTableResource",

					"org.lgna.story.resources.prop.Dresser",
					"org.lgna.story.resources.prop.DresserResource",

					"org.lgna.story.resources.prop.EndTable",
					"org.lgna.story.resources.prop.EndTableResource",

					"org.lgna.story.resources.prop.Grill",
					"org.lgna.story.resources.prop.GrillResource",

					"org.lgna.story.resources.prop.Hedge",
					"org.lgna.story.resources.prop.HedgeResource",

					"org.lgna.story.resources.prop.Helicopter",
					"org.lgna.story.resources.prop.HelicopterResource",

					"org.lgna.story.resources.prop.Iceberg",
					"org.lgna.story.resources.prop.IcebergResource",

					"org.lgna.story.resources.prop.IceFlow",
					"org.lgna.story.resources.prop.IceFlowResource",

					"org.lgna.story.resources.prop.IceMountain",
					"org.lgna.story.resources.prop.IceMountainResource",

					"org.lgna.story.resources.prop.Lamp",
					"org.lgna.story.resources.prop.LampResource",

					"org.lgna.story.resources.prop.Loveseat",
					"org.lgna.story.resources.prop.LoveseatResource",

					"org.lgna.story.resources.prop.MagicSpoon",
					"org.lgna.story.resources.prop.MagicSpoonResource",

					"org.lgna.story.resources.prop.MagicStaff",
					"org.lgna.story.resources.prop.MagicStaffResource",

					"org.lgna.story.resources.prop.MagicStone",
					"org.lgna.story.resources.prop.MagicStoneResource",

					"org.lgna.story.resources.prop.MagicWand",
					"org.lgna.story.resources.prop.MagicWandResource",

					"org.lgna.story.resources.prop.PirateShip",
					"org.lgna.story.resources.prop.PirateShipResource",

					"org.lgna.story.resources.prop.Plateau",
					"org.lgna.story.resources.prop.PlateauResource",

					"org.lgna.story.resources.prop.PocketWatch",
					"org.lgna.story.resources.prop.PocketWatchResource",

					"org.lgna.story.resources.prop.Potion",
					"org.lgna.story.resources.prop.PotionResource",

					"org.lgna.story.resources.prop.PrayerFlags",
					"org.lgna.story.resources.prop.PrayerFlagsResource",

					"org.lgna.story.resources.prop.RedRover",
					"org.lgna.story.resources.prop.RedRoverResource",

					"org.lgna.story.resources.prop.Rose",
					"org.lgna.story.resources.prop.RoseResource",

					"org.lgna.story.resources.prop.Saucer",
					"org.lgna.story.resources.prop.SaucerResource",

					"org.lgna.story.resources.prop.SeaPlant",
					"org.lgna.story.resources.prop.SeaPlantResource",

					"org.lgna.story.resources.prop.Seaweed",
					"org.lgna.story.resources.prop.SeaweedResource",

					"org.lgna.story.resources.prop.ShortMushroom",
					"org.lgna.story.resources.prop.ShortMushroomResource",

					"org.lgna.story.resources.prop.Sled",
					"org.lgna.story.resources.prop.SledResource",

					"org.lgna.story.resources.prop.Snowboard",
					"org.lgna.story.resources.prop.SnowboardResource",

					"org.lgna.story.resources.prop.Sofa",
					"org.lgna.story.resources.prop.SofaResource",

					"org.lgna.story.resources.prop.SpellBook",
					"org.lgna.story.resources.prop.SpellBookResource",

					"org.lgna.story.resources.prop.StoneBridge",
					"org.lgna.story.resources.prop.StoneBridgeResource",

					"org.lgna.story.resources.prop.Stove",
					"org.lgna.story.resources.prop.StoveResource",

					"org.lgna.story.resources.prop.Submarine",
					"org.lgna.story.resources.prop.SubmarineResource",

					"org.lgna.story.resources.prop.Suitcase",
					"org.lgna.story.resources.prop.SuitcaseResource",

					"org.lgna.story.resources.prop.TallMushroom",
					"org.lgna.story.resources.prop.TallMushroomResource",

					"org.lgna.story.resources.prop.Teacup",
					"org.lgna.story.resources.prop.TeacupResource",

					"org.lgna.story.resources.prop.Teapot",
					"org.lgna.story.resources.prop.TeapotResource",

					"org.lgna.story.resources.prop.TeaTable",
					"org.lgna.story.resources.prop.TeaTableResource",

					"org.lgna.story.resources.prop.TeaTray",
					"org.lgna.story.resources.prop.TeaTrayResource",

					"org.lgna.story.resources.prop.Tent",
					"org.lgna.story.resources.prop.TentResource",

					"org.lgna.story.resources.prop.Trashcan",
					"org.lgna.story.resources.prop.TrashcanResource",

					"org.lgna.story.resources.prop.TreasureChest",
					"org.lgna.story.resources.prop.TreasureChestResource",

					"org.lgna.story.resources.prop.UFO",
					"org.lgna.story.resources.prop.UFOResource",

					"org.lgna.story.resources.prop.Volleyball",
					"org.lgna.story.resources.prop.VolleyballResource",

					"org.lgna.story.resources.prop.WeddingCake",
					"org.lgna.story.resources.prop.WeddingCakeResource",

					"org.lgna.story.resources.prop.WonderlandTree",
					"org.lgna.story.resources.prop.WonderlandTreeResource",

					"org.lgna.story.resources.quadruped.AbyssinianCat",
					"org.lgna.story.resources.quadruped.AbyssinianCatResource",

					"org.lgna.story.resources.quadruped.AlienRobot",
					"org.lgna.story.resources.quadruped.AlienRobotResource",

					"org.lgna.story.resources.quadruped.BabyDragon",
					"org.lgna.story.resources.quadruped.BabyDragonResource",

					"org.lgna.story.resources.quadruped.BillyGoat",
					"org.lgna.story.resources.quadruped.BillyGoatResource",

					"org.lgna.story.resources.quadruped.Camel",
					"org.lgna.story.resources.quadruped.CamelResource",

					"org.lgna.story.resources.quadruped.Cow",
					"org.lgna.story.resources.quadruped.CowResource",

					"org.lgna.story.resources.quadruped.Dalmatian",
					"org.lgna.story.resources.quadruped.DalmatianResource",

					"org.lgna.story.resources.quadruped.Dragon",
					"org.lgna.story.resources.quadruped.DragonResource",

					"org.lgna.story.resources.quadruped.Lioness",
					"org.lgna.story.resources.quadruped.LionessResource",

					"org.lgna.story.resources.quadruped.ManxCat",
					"org.lgna.story.resources.quadruped.ManxCatResource",

					"org.lgna.story.resources.quadruped.Poodle",
					"org.lgna.story.resources.quadruped.PoodleResource",

					"org.lgna.story.resources.quadruped.ScottyDog",
					"org.lgna.story.resources.quadruped.ScottyDogResource",

					"org.lgna.story.resources.quadruped.ShortHairCat",
					"org.lgna.story.resources.quadruped.ShortHairCatResource",

					"org.lgna.story.resources.quadruped.Wolf",
					"org.lgna.story.resources.quadruped.WolfResource",

					"org.lgna.story.resources.fish.BlueTang",
					"org.lgna.story.resources.fish.BlueTangResource",

					"org.lgna.story.resources.fish.ClownFish",
					"org.lgna.story.resources.fish.ClownFishResource",

					"org.lgna.story.resources.fish.PajamaFish",
					"org.lgna.story.resources.fish.PajamaFishResource",

					"org.lgna.story.resources.fish.Shark",
					"org.lgna.story.resources.fish.SharkResource",

					"org.lgna.story.resources.marinemammal.BabyWalrus",
					"org.lgna.story.resources.marinemammal.BabyWalrusResource",

					"org.lgna.story.resources.marinemammal.Dolphin",
					"org.lgna.story.resources.marinemammal.DolphinResource",

					"org.lgna.story.resources.marinemammal.Orca",
					"org.lgna.story.resources.marinemammal.OrcaResource",

					"org.lgna.story.resources.marinemammal.Walrus",
					"org.lgna.story.resources.marinemammal.WalrusResource"
			),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.35.0.0" ),
					new org.lgna.project.Version( "3.1.38.0.0" )
			),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.38.0.0" ),
					new org.lgna.project.Version( "3.1.39.0.0" ),

					"org.lgna.story.event.MouseClickOnScreenListener\"/><type name=\"\\[Lorg.lgna.story.AddMouseButtonListener",
					"org.lgna.story.event.MouseClickOnScreenListener\"/><type name=\"\\[Lorg.lgna.story.AddMouseClickOnScreenListener",

					"org.lgna.story.event.MouseClickOnObjectListener\"/><type name=\"\\[Lorg.lgna.story.AddMouseButtonListener",
					"org.lgna.story.event.MouseClickOnObjectListener\"/><type name=\"\\[Lorg.lgna.story.AddMouseClickOnObjectListener"
			),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.39.0.0" ),
					new org.lgna.project.Version( "3.1.48.0.0" ),

					"BILLY_GOAT",
					"BIG_HORNS"
			),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.48.0.0" ),
					new org.lgna.project.Version( "3.1.58.0.0" ),

					"name=\"ICE_FLOW",
					"name=\"ICE_FLOE",

					"name=\"org.lgna.story.resources.prop.IceFlowResource",
					"name=\"org.lgna.story.resources.prop.IceFloeResource",

					"name=\"PIXIE_GREEN",
					"name=\"GREEN",

					"name=\"PIXIE_PINK",
					"name=\"PINK",

					"name=\"PIXIE_BLUE",
					"name=\"BLUE"
			),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.58.0.0" ),
					new org.lgna.project.Version( "3.1.59.0.0" ),

					createMoreSpecificFieldPattern( "PLANT1", "org.lgna.story.resources.prop.SeaPlantResource" ),
					createMoreSpecificFieldReplacement( "DOUBLE", "org.lgna.story.resources.prop.SeaSpongeResource" ),

					createMoreSpecificFieldPattern( "CAULDRON", "org.lgna.story.resources.prop.CauldronResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.CauldronResource" ),

					createMoreSpecificFieldPattern( "YETI", "org.lgna.story.resources.biped.YetiResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.YetiResource" ),

					createMoreSpecificFieldPattern( "PANDA", "org.lgna.story.resources.biped.PandaResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.PandaResource" ),

					createMoreSpecificFieldPattern( "WONDERLAND_TREE", "org.lgna.story.resources.prop.WonderlandTreeResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.WonderlandTreeResource" ),

					createMoreSpecificFieldPattern( "ABYSSINIAN_CAT", "org.lgna.story.resources.quadruped.AbyssinianCatResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.AbyssinianCatResource" ),

					createMoreSpecificFieldPattern( "SMOOTH", "org.lgna.story.resources.marinemammal.ManateeResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.marinemammal.ManateeResource" ),

					createMoreSpecificFieldPattern( "MARCH_HARE", "org.lgna.story.resources.biped.MarchHareResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.MarchHareResource" ),

					createMoreSpecificFieldPattern( "BANANA_TREE", "org.lgna.story.resources.prop.BananaTreeResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.BananaTreeResource" ),

					createMoreSpecificFieldPattern( "BOULDER1_MOON", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER1_GRAY", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER1_DESERT", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER1_BROWN", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER2_MOON", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER2_GRAY", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER2_DESERT", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER2_BROWN", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER3_MOON", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER3_GRAY", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER3_DESERT", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER3_BROWN", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER4_MOON", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER4_GRAY", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER4_DESERT", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER4_BROWN", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER5_MOON", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER5_GRAY", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER5_DESERT", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER5_BROWN", "org.lgna.story.resources.prop.BoulderResource" ),

					"name=\"org.lgna.story.resources.prop.JungleShrubResource",
					"name=\"org.lgna.story.resources.prop.JunglePlantResource",

					createMoreSpecificFieldPattern( "CASTLE_GATE", "org.lgna.story.resources.prop.CastleGateResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.CastleGateResource" ),

					createMoreSpecificFieldPattern( "BANANA", "org.lgna.story.resources.prop.BananaResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.BananaResource" ),

					createMoreSpecificFieldPattern( "WOLF", "org.lgna.story.resources.quadruped.WolfResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.WolfResource" ),

					createMoreSpecificFieldPattern( "STAFF", "org.lgna.story.resources.prop.StaffResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.StaffResource" ),

					"org.lgna.story.resources.prop.LogBridgeResource",
					"org.lgna.story.resources.prop.JungleLogResource",

					createMoreSpecificFieldPattern( "DRAGON_BABY_GREEN", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "GREEN", "org.lgna.story.resources.quadruped.BabyDragonResource" ),

					createMoreSpecificFieldPattern( "DRAGON_BABY_RED", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "RED", "org.lgna.story.resources.quadruped.BabyDragonResource" ),

					createMoreSpecificFieldPattern( "DRAGON_BABY_AQUA", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "AQUA", "org.lgna.story.resources.quadruped.BabyDragonResource" ),

					createMoreSpecificFieldPattern( "DRAGON_BABY_BLUE", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "BLUE", "org.lgna.story.resources.quadruped.BabyDragonResource" ),

					createMoreSpecificFieldPattern( "STONE_BRIDGE", "org.lgna.story.resources.prop.StoneBridgeResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.StoneBridgeResource" ),

					createMoreSpecificFieldPattern( "WHITE_RABBIT", "org.lgna.story.resources.biped.WhiteRabbitResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.WhiteRabbitResource" ),

					createMoreSpecificFieldPattern( "BOWLING_PIN", "org.lgna.story.resources.prop.BowlingPinResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.BowlingPinResource" ),

					createMoreSpecificFieldPattern( "SHRINE", "org.lgna.story.resources.prop.ShrineResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.ShrineResource" ),

					createMoreSpecificFieldPattern( "SCOTTY_DOG", "org.lgna.story.resources.quadruped.ScottyDogResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.ScottyDogResource" ),

					createMoreSpecificFieldPattern( "FISHING_BASKET", "org.lgna.story.resources.prop.FishingBasketResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.FishingBasketResource" ),

					createMoreSpecificFieldPattern( "KITE_SPOOL", "org.lgna.story.resources.prop.KiteSpoolResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.KiteSpoolResource" ),

					createMoreSpecificFieldPattern( "BABY_YETI", "org.lgna.story.resources.biped.BabyYetiResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.BabyYetiResource" ),

					createMoreSpecificFieldPattern( "NO_SCARF", "org.lgna.story.resources.biped.BabyYetiResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.BabyYetiResource" ),

					createMoreSpecificFieldPattern( "BUNNY", "org.lgna.story.resources.biped.BunnyResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.BunnyResource" ),

					createMoreSpecificFieldPattern( "MAPINGUARI", "org.lgna.story.resources.biped.MapinguariResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.MapinguariResource" ),

					createMoreSpecificFieldPattern( "HEDGE", "org.lgna.story.resources.prop.HedgeResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.HedgeResource" ),

					createMoreSpecificFieldPattern( "MANGO", "org.lgna.story.resources.prop.MangoResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.MangoResource" ),

					createMoreSpecificFieldPattern( "SUBMARINE", "org.lgna.story.resources.prop.SubmarineResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.SubmarineResource" ),

					createMoreSpecificFieldPattern( "CASTLE_TOWER_MIDDLE", "org.lgna.story.resources.prop.CastleTowerMiddleResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.CastleTowerMiddleResource" ),

					createMoreSpecificFieldPattern( "TREEHOUSE", "org.lgna.story.resources.prop.TreehouseResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TreehouseResource" ),

					createMoreSpecificFieldPattern( "CASTLE_TOWER_BASE", "org.lgna.story.resources.prop.CastleTowerBaseResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.CastleTowerBaseResource" ),

					createMoreSpecificFieldPattern( "FISHING_BASKET_LID", "org.lgna.story.resources.prop.FishingBasketLidResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.FishingBasketLidResource" ),

					createMoreSpecificFieldPattern( "PLATEAU1_PLATEAU1_BROWN", "org.lgna.story.resources.prop.PlateauResource" ),
					createMoreSpecificFieldReplacement( "TALL_BROWN", "org.lgna.story.resources.prop.PlateauResource" ),

					createMoreSpecificFieldPattern( "PLATEAU1_PLATEAU1_RED", "org.lgna.story.resources.prop.PlateauResource" ),
					createMoreSpecificFieldReplacement( "TALL_RED", "org.lgna.story.resources.prop.PlateauResource" ),

					createMoreSpecificFieldPattern( "PLATEAU1_PLATEAU1_GRAY", "org.lgna.story.resources.prop.PlateauResource" ),
					createMoreSpecificFieldReplacement( "TALL_GRAY", "org.lgna.story.resources.prop.PlateauResource" ),

					createMoreSpecificFieldPattern( "PLATEAU2_PLATEAU2_BROWN", "org.lgna.story.resources.prop.PlateauResource" ),
					createMoreSpecificFieldReplacement( "SHORT_BROWN", "org.lgna.story.resources.prop.PlateauResource" ),

					createMoreSpecificFieldPattern( "PLATEAU2_PLATEAU1_RED", "org.lgna.story.resources.prop.PlateauResource" ),
					createMoreSpecificFieldReplacement( "SHORT_RED", "org.lgna.story.resources.prop.PlateauResource" ),

					createMoreSpecificFieldPattern( "PLATEAU2_PLATEAU2_GRAY", "org.lgna.story.resources.prop.PlateauResource" ),
					createMoreSpecificFieldReplacement( "SHORT_GRAY", "org.lgna.story.resources.prop.PlateauResource" ),

					createMoreSpecificFieldPattern( "RED_ROVER", "org.lgna.story.resources.prop.RedRoverResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.RedRoverResource" ),

					createMoreSpecificFieldPattern( "ALIEN_ROBOT", "org.lgna.story.resources.quadruped.AlienRobotResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.AlienRobotResource" ),

					createMoreSpecificFieldPattern( "SOCCER_BALL", "org.lgna.story.resources.prop.SoccerBallResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.SoccerBallResource" ),

					createMoreSpecificFieldPattern( "YAK", "org.lgna.story.resources.quadruped.YakResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.YakResource" ),

					createMoreSpecificFieldPattern( "CAULDRON_LID", "org.lgna.story.resources.prop.CauldronLidResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.CauldronLidResource" ),

					createMoreSpecificFieldPattern( "TEAPOT", "org.lgna.story.resources.prop.TeapotResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TeapotResource" ),

					createMoreSpecificFieldPattern( "CAIMAN", "org.lgna.story.resources.quadruped.CaimanResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.CaimanResource" ),

					createMoreSpecificFieldPattern( "QUEEN_OF_HEARTS", "org.lgna.story.resources.biped.QueenOfHeartsResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.QueenOfHeartsResource" ),

					createMoreSpecificFieldPattern( "CAMEL", "org.lgna.story.resources.quadruped.CamelResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.CamelResource" ),

					createMoreSpecificFieldPattern( "FALCON", "org.lgna.story.resources.flyer.FalconResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.flyer.FalconResource" ),

					createMoreSpecificFieldPattern( "PIRANHA", "org.lgna.story.resources.fish.PiranhaResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.fish.PiranhaResource" ),

					createMoreSpecificFieldPattern( "BAMBOO1", "org.lgna.story.resources.prop.BambooResource" ),
					createMoreSpecificFieldReplacement( "SHOOT1", "org.lgna.story.resources.prop.BambooResource" ),

					createMoreSpecificFieldPattern( "BAMBOO2", "org.lgna.story.resources.prop.BambooResource" ),
					createMoreSpecificFieldReplacement( "SHOOT2", "org.lgna.story.resources.prop.BambooResource" ),

					createMoreSpecificFieldPattern( "BAMBOO3", "org.lgna.story.resources.prop.BambooResource" ),
					createMoreSpecificFieldReplacement( "SHOOT3", "org.lgna.story.resources.prop.BambooResource" ),

					createMoreSpecificFieldPattern( "BAMBOO4", "org.lgna.story.resources.prop.BambooResource" ),
					createMoreSpecificFieldReplacement( "SHOOT4", "org.lgna.story.resources.prop.BambooResource" ),

					createMoreSpecificFieldPattern( "TORTOISE", "org.lgna.story.resources.biped.TortoiseResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.TortoiseResource" ),

					createMoreSpecificFieldPattern( "GONG", "org.lgna.story.resources.prop.GongResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.GongResource" ),

					createMoreSpecificFieldPattern( "CARD03", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "THREE3", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "CARD10", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "TEN10", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "DOLPHIN", "org.lgna.story.resources.marinemammal.DolphinResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.marinemammal.DolphinResource" ),

					createMoreSpecificFieldPattern( "CAVE", "org.lgna.story.resources.prop.CaveResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_UNDERWATER", "org.lgna.story.resources.prop.CaveResource" ),

					createMoreSpecificFieldPattern( "MAGIC_WAND", "org.lgna.story.resources.prop.MagicWandResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.MagicWandResource" ),

					createMoreSpecificFieldPattern( "GONG_MALLET", "org.lgna.story.resources.prop.GongMalletResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.GongMalletResource" ),

					createMoreSpecificFieldPattern( "CLUSTER1", "org.lgna.story.resources.prop.BambooClusterResource" ),
					createMoreSpecificFieldReplacement( "CLUSTER1", "org.lgna.story.resources.prop.BambooResource" ),

					createMoreSpecificFieldPattern( "CLUSTER2", "org.lgna.story.resources.prop.BambooClusterResource" ),
					createMoreSpecificFieldReplacement( "CLUSTER2", "org.lgna.story.resources.prop.BambooResource" ),

					createMoreSpecificFieldPattern( "CLUSTER3", "org.lgna.story.resources.prop.BambooClusterResource" ),
					createMoreSpecificFieldReplacement( "CLUSTER3", "org.lgna.story.resources.prop.BambooResource" ),

					createMoreSpecificFieldPattern( "CLUSTER4", "org.lgna.story.resources.prop.BambooClusterResource" ),
					createMoreSpecificFieldReplacement( "CLUSTER4", "org.lgna.story.resources.prop.BambooResource" ),

					"name=\"org.lgna.story.resources.prop.BambooClusterResource",
					"name=\"org.lgna.story.resources.prop.BambooResource",

					createMoreSpecificFieldPattern( "OWL", "org.lgna.story.resources.flyer.OwlResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.flyer.OwlResource" ),

					createMoreSpecificFieldPattern( "PECCARY", "org.lgna.story.resources.quadruped.PeccaryResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.PeccaryResource" ),

					createMoreSpecificFieldPattern( "TEA_TRAY", "org.lgna.story.resources.prop.TeaTrayResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TeaTrayResource" ),

					createMoreSpecificFieldPattern( "POCKET_WATCH", "org.lgna.story.resources.prop.PocketWatchResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.PocketWatchResource" ),

					createMoreSpecificFieldPattern( "BABY_WALRUS", "org.lgna.story.resources.marinemammal.BabyWalrusResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.marinemammal.BabyWalrusResource" ),

					createMoreSpecificFieldPattern( "SHELF1", "org.lgna.story.resources.prop.CoralShelfResource" ),
					createMoreSpecificFieldReplacement( "YELLOW", "org.lgna.story.resources.prop.CoralShelfResource" ),

					createMoreSpecificFieldPattern( "SHELF2", "org.lgna.story.resources.prop.CoralShelfResource" ),
					createMoreSpecificFieldReplacement( "YELLOW", "org.lgna.story.resources.prop.CoralShelfResource" ),

					createMoreSpecificFieldPattern( "ORCA", "org.lgna.story.resources.marinemammal.OrcaResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.marinemammal.OrcaResource" ),

					createMoreSpecificFieldPattern( "FISHING_LANTERN", "org.lgna.story.resources.prop.FishingLanternResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.FishingLanternResource" ),

					createMoreSpecificFieldPattern( "TROLL", "org.lgna.story.resources.biped.TrollResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.TrollResource" ),

					createMoreSpecificFieldPattern( "MAD_HATTER", "org.lgna.story.resources.biped.MadHatterResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.MadHatterResource" ),

					createMoreSpecificFieldPattern( "CHICKEN", "org.lgna.story.resources.flyer.ChickenResource" ),
					createMoreSpecificFieldReplacement( "MEAN_CHICKEN", "org.lgna.story.resources.flyer.ChickenResource" ),

					createMoreSpecificFieldPattern( "ARAPAIMA", "org.lgna.story.resources.fish.ArapaimaResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.fish.ArapaimaResource" ),

					createMoreSpecificFieldPattern( "POND", "org.lgna.story.resources.prop.PondResource" ),
					createMoreSpecificFieldReplacement( "LIGHT_BLUE", "org.lgna.story.resources.prop.PondResource" ),

					createMoreSpecificFieldPattern( "PHOENIX", "org.lgna.story.resources.flyer.PhoenixResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.flyer.PhoenixResource" ),

					createMoreSpecificFieldPattern( "ICE_MOUNTAIN", "org.lgna.story.resources.prop.IceMountainResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.IceMountainResource" ),

					createMoreSpecificFieldPattern( "BLUE_TANG", "org.lgna.story.resources.fish.BlueTangResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.fish.BlueTangResource" ),

					createMoreSpecificFieldPattern( "JAPANESE_CYPRESS", "org.lgna.story.resources.prop.JapaneseCypressResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.JapaneseCypressResource" ),

					createMoreSpecificFieldPattern( "LIONESS", "org.lgna.story.resources.quadruped.LionessResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.LionessResource" ),

					createMoreSpecificFieldPattern( "SPELL_BOOK", "org.lgna.story.resources.prop.SpellBookResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.SpellBookResource" ),

					createMoreSpecificFieldPattern( "WALRUS", "org.lgna.story.resources.marinemammal.WalrusResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.marinemammal.WalrusResource" ),

					createMoreSpecificFieldPattern( "PIG", "org.lgna.story.resources.biped.PigResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.PigResource" ),

					createMoreSpecificFieldPattern( "POODLE", "org.lgna.story.resources.quadruped.PoodleResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.PoodleResource" ),

					createMoreSpecificFieldPattern( "COCONUT", "org.lgna.story.resources.prop.CoconutResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.CoconutResource" ),

					createMoreSpecificFieldPattern( "SHORT_HAIR_CAT", "org.lgna.story.resources.quadruped.ShortHairCatResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.ShortHairCatResource" ),

					createMoreSpecificFieldPattern( "TOUCAN", "org.lgna.story.resources.flyer.ToucanResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.flyer.ToucanResource" ),

					createMoreSpecificFieldPattern( "ICE_FLOE", "org.lgna.story.resources.prop.IceFloeResource" ),
					createMoreSpecificFieldReplacement( "ICE_FLOE1", "org.lgna.story.resources.prop.IceFloeResource" ),

					createMoreSpecificFieldPattern( "COLA_BOTTLE", "org.lgna.story.resources.prop.ColaBottleResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.ColaBottleResource" ),

					createMoreSpecificFieldPattern( "WITCH", "org.lgna.story.resources.biped.WitchResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.WitchResource" ),

					createMoreSpecificFieldPattern( "PRAYER_FLAGS", "org.lgna.story.resources.prop.PrayerFlagsResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.PrayerFlagsResource" ),

					createMoreSpecificFieldPattern( "CHESHIRE_CAT", "org.lgna.story.resources.biped.CheshireCatResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.CheshireCatResource" ),

					createMoreSpecificFieldPattern( "BIG_BAD_WOLF", "org.lgna.story.resources.biped.BigBadWolfResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.BigBadWolfResource" ),

					createMoreSpecificFieldPattern( "SHARK", "org.lgna.story.resources.fish.SharkResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.fish.SharkResource" ),

					createMoreSpecificFieldPattern( "TREASURE_CHEST", "org.lgna.story.resources.prop.TreasureChestResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TreasureChestResource" ),

					createMoreSpecificFieldPattern( "CURUPIRA", "org.lgna.story.resources.biped.CurupiraResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.CurupiraResource" ),

					createMoreSpecificFieldPattern( "FLAMINGO", "org.lgna.story.resources.flyer.FlamingoResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.flyer.FlamingoResource" ),

					createMoreSpecificFieldPattern( "FISHING_NET", "org.lgna.story.resources.prop.FishingNetResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.FishingNetResource" ),

					createMoreSpecificFieldPattern( "SAUCER_WHITE_RABBIT", "org.lgna.story.resources.prop.SaucerResource" ),
					createMoreSpecificFieldReplacement( "WHITE_RABBIT", "org.lgna.story.resources.prop.SaucerResource" ),

					createMoreSpecificFieldPattern( "SAUCER_QUEEN", "org.lgna.story.resources.prop.SaucerResource" ),
					createMoreSpecificFieldReplacement( "MARCH_HARE", "org.lgna.story.resources.prop.SaucerResource" ),

					createMoreSpecificFieldPattern( "SAUCER_CHESHIRE", "org.lgna.story.resources.prop.SaucerResource" ),
					createMoreSpecificFieldReplacement( "MARCH_HARE", "org.lgna.story.resources.prop.SaucerResource" ),

					createMoreSpecificFieldPattern( "SAUCER_HATTER", "org.lgna.story.resources.prop.SaucerResource" ),
					createMoreSpecificFieldReplacement( "MAD_HATTER", "org.lgna.story.resources.prop.SaucerResource" ),

					createMoreSpecificFieldPattern( "SAUCER_MARCH_HARE", "org.lgna.story.resources.prop.SaucerResource" ),
					createMoreSpecificFieldReplacement( "MARCH_HARE", "org.lgna.story.resources.prop.SaucerResource" ),

					createMoreSpecificFieldPattern( "SAUCER_PLAYING_CARD", "org.lgna.story.resources.prop.SaucerResource" ),
					createMoreSpecificFieldReplacement( "PLAYING_CARD", "org.lgna.story.resources.prop.SaucerResource" ),

					createMoreSpecificFieldPattern( "TEACUP_CHESHIRE", "org.lgna.story.resources.prop.TeacupResource" ),
					createMoreSpecificFieldReplacement( "MARCH_HARE", "org.lgna.story.resources.prop.TeacupResource" ),

					createMoreSpecificFieldPattern( "TEACUP_HATTER", "org.lgna.story.resources.prop.TeacupResource" ),
					createMoreSpecificFieldReplacement( "MAD_HATTER", "org.lgna.story.resources.prop.TeacupResource" ),

					createMoreSpecificFieldPattern( "TEACUP_MARCH_HARE", "org.lgna.story.resources.prop.TeacupResource" ),
					createMoreSpecificFieldReplacement( "MARCH_HARE", "org.lgna.story.resources.prop.TeacupResource" ),

					createMoreSpecificFieldPattern( "TEACUP_PLAYING_CARD", "org.lgna.story.resources.prop.TeacupResource" ),
					createMoreSpecificFieldReplacement( "PLAYING_CARD", "org.lgna.story.resources.prop.TeacupResource" ),

					createMoreSpecificFieldPattern( "TEACUP_WHITE_RABBIT", "org.lgna.story.resources.prop.TeacupResource" ),
					createMoreSpecificFieldReplacement( "WHITE_RABBIT", "org.lgna.story.resources.prop.TeacupResource" ),

					createMoreSpecificFieldPattern( "TEACUP_QUEEN", "org.lgna.story.resources.prop.TeacupResource" ),
					createMoreSpecificFieldReplacement( "MARCH_HARE", "org.lgna.story.resources.prop.TeacupResource" ),

					createMoreSpecificFieldPattern( "CLOWN_FISH", "org.lgna.story.resources.fish.ClownFishResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.fish.ClownFishResource" ),

					createMoreSpecificFieldPattern( "TENT", "org.lgna.story.resources.prop.TentResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TentResource" ),

					createMoreSpecificFieldPattern( "ICEBERG", "org.lgna.story.resources.prop.IcebergResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.IcebergResource" ),

					createMoreSpecificFieldPattern( "MONKEY_KING", "org.lgna.story.resources.biped.MonkeyKingResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.MonkeyKingResource" ),

					createMoreSpecificFieldPattern( "PAJAMA_FISH", "org.lgna.story.resources.fish.PajamaFishResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.fish.PajamaFishResource" ),

					createMoreSpecificFieldPattern( "SEAGULL", "org.lgna.story.resources.flyer.SeagullResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.flyer.SeagullResource" ),

					createMoreSpecificFieldPattern( "CASTLE_TOWER_TOP", "org.lgna.story.resources.prop.CastleTowerTopResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.CastleTowerTopResource" ),

					createMoreSpecificFieldPattern( "MANX_CAT", "org.lgna.story.resources.quadruped.ManxCatResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.ManxCatResource" ),

					createMoreSpecificFieldPattern( "FISHING_BOAT", "org.lgna.story.resources.prop.FishingBoatResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.FishingBoatResource" ),

					createMoreSpecificFieldPattern( "FOX", "org.lgna.story.resources.quadruped.FoxResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.FoxResource" ),

					createMoreSpecificFieldPattern( "MAGIC_STAFF", "org.lgna.story.resources.prop.MagicStaffResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.MagicStaffResource" ),

					createMoreSpecificFieldPattern( "FISHING_LANTERN_POLE", "org.lgna.story.resources.prop.FishingLanternPoleResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.FishingLanternPoleResource" ),

					createMoreSpecificFieldPattern( "DALMATIAN", "org.lgna.story.resources.quadruped.DalmatianResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.DalmatianResource" ),

					createMoreSpecificFieldPattern( "STUFFED_TIGER", "org.lgna.story.resources.biped.StuffedTigerResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.StuffedTigerResource" ),

					createMoreSpecificFieldPattern( "MAGIC_SPOON", "org.lgna.story.resources.prop.MagicSpoonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.MagicSpoonResource" ),

					createMoreSpecificFieldPattern( "FISHING_BOAT_CANOPY", "org.lgna.story.resources.prop.FishingBoatCanopyResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.FishingBoatCanopyResource" ),

					createMoreSpecificFieldPattern( "SHRINE_LANTERN", "org.lgna.story.resources.prop.ShrineLanternResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.ShrineLanternResource" ),

					createMoreSpecificFieldPattern( "PIRATE_SHIP", "org.lgna.story.resources.prop.PirateShipResource" ),
					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.prop.PirateShipPropResource" ),

					createMoreSpecificFieldPattern( "TEA_TABLE", "org.lgna.story.resources.prop.TeaTableResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TeaTableResource" ),

					createMoreSpecificFieldPattern( "WALL", "org.lgna.story.resources.prop.CastleWallResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.CastleWallResource" ),

					createMoreSpecificFieldPattern( "RED", "org.lgna.story.resources.prop.TallMushroomResource" ),
					createMoreSpecificFieldReplacement( "TALL_RED", "org.lgna.story.resources.prop.MushroomResource" ),

					createMoreSpecificFieldPattern( "WHITE", "org.lgna.story.resources.prop.TallMushroomResource" ),
					createMoreSpecificFieldReplacement( "TALL_WHITE", "org.lgna.story.resources.prop.MushroomResource" ),

					"name=\"org.lgna.story.resources.prop.TallMushroomResource",
					"name=\"org.lgna.story.resources.prop.MushroomResource",

					createMoreSpecificFieldPattern( "TREE_TRUNK", "org.lgna.story.resources.prop.TreeTrunkResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TreeTrunkResource" ),

					createMoreSpecificFieldPattern( "DRAGON_ORANGE", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "ORANGE", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "DRAGON_RED", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "RED", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_GRAY", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ADIRONDACK_GRAY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_CAMO", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ADIRONDACK_CAMOUFLAGE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_PALM", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ADIRONDACK_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_LOVESEAT_ART_NOUVEAU_FRAME_MOHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_LOVESEAT_ART_NOUVEAU_FRAME_OAK", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU_OAK", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_LIGHT_WOOD", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_LIGHT_WOOD", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_BEIGE_FABRIC", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_WHITE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_TAN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "PARK_BENCH_LOVESEAT_PARK_BENCH_WOOD", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_WOOD", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_WHITE_FLOWERS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_WHITE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_GREEN_FLOWERS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_BEIGE_FLOWERS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BROWN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "VALUE_LOVESEAT_VALUE_BLUE_STRIPE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "VALUE_BLUE_STRIPES", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "VALUE1_SOFA_VALUE1_BLUE_STRIPE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "VALUE1_BLUE_STRIPES", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "VALUE1_SOFA_VALUE1_FLOWER", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "VALUE1_FLOWERS", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "VALUE2_SOFA_VALUE2_LIGHT_BROWN_FLOWER", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "VALUE2_BROWN_FLOWERS", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "VALUE2_SOFA_VALUE2_RED_CHECKER", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "VALUE2_RED_SQUARES", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "VALUE2_SOFA_VALUE2_GREEN_FLOWER", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "VALUE2_GREEN_FLOWERS", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "VALUE2_SOFA_VALUE2_BLUE_FLOWER_BORDER", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "VALUE2_BLUE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL1_SOFA_COLONIAL1_FRUITS", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL1_FRUITS", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL1_SOFA_COLONIAL1_DIAMOND", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL1_DIAMONDS", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL1_SOFA_COLONIAL1_REDPATTERN", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL1_RED_SQUARES", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL1_SOFA_COLONIAL1_BEIGE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL1_BROWN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL1_SOFA_COLONIAL1_WHITE_DIAMOND", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL1_DIAMONDS", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL1_SOFA_COLONIAL1_LINE_CURVES", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL1_CURVES", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL2_SOFA_COLONIAL2_GOLDDIAMOND", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL2_GOLD", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL2_SOFA_COLONIAL2_ORANGE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL2_ORANGE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL2_SOFA_COLONIAL2_RED_STRIPES", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL2_RED", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL2_SOFA_COLONIAL2_BLUE_PATTERN", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL2_BLUE_PATTERN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_BLACKLEATHER", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "STEEL_FRAME_BLACK_LEATHER", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_CORDOROY", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "STEEL_FRAME_BRWON_LEATHER", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_STRIPE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "STEEL_FRAME_BRWON_LEATHER", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_GATOR", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "STEEL_FRAME_ALLIGATOR", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_RED", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_SOFA_MOROCCAN_GREEN", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_GREEN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_SOFA_MOROCCAN_BEIGE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_LIGHT_BLUE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_WHITE_FLOWERS", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_WHITE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_GREEN_FLOWERS", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_GREEN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_BLUE_FLOWERS", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BLUE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_PINK_FLOWERS", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_PINK", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_CUTOUT_SOFA_UM_CUTOUT_BLACK_CREAM", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_CUTOUT_BLACK", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_CUTOUT_SOFA_UM_CUTOUT_BLUE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_CUTOUT_BLUE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_CUTOUT_SOFA_UM_CUTOUT_LEOPARD", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_CUTOUT_LEOPARD", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_CUTOUT_SOFA_UM_CUTOUT_PURPLE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_CUTOUT_PURPLE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_CUTOUT_SOFA_UM_CUTOUT_ZEBRA", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_CUTOUT_ZEBRA", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_CUTOUT_SOFA_UM_CUTOUT_GREEN", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_CUTOUT_GREEN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_DIAMOND_SOFA_U_M_DIAMOND_CHECK", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_DIAMOND_BLUE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_DIAMOND_SOFA_U_M_DIAMOND_BLACK", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_DIAMOND_BLACK_AND_WHITE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_DIAMOND_SOFA_U_M_DIAMOND_YELLOW", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_DIAMOND_GREEN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_DIAMOND_SOFA_U_M_DIAMOND_PURPLE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_DIAMOND_PURPLE_AND_GREEN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_DIAMOND_SOFA_U_M_DIAMOND_RED", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_DIAMOND_RED_AND_PURPLE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_GREEN_FLOWERS", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_GREEN_FLOWERS", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_RED_FLOWERS", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_RED_FLOWERS", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_GREEN", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_GREEN", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DRESSER_CENTRAL_ASIAN_RED", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_RED", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_DRESSER_COLONIAL_WOOD", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_WOOD", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_DRESSER_COLONIAL_LIGHT_WOOD_CURLY", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_LIGHT_WOOD", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_DRESSER_COLONIAL_RED_WOOD", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_REDWOOD", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_DRESSER_COLONIAL_WOOD_STRAIGHT_DARK", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_DARK_WOOD", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_DRESSER_DESIGNER_BROWN", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_BROWN", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_DRESSER_DESIGNER_LIGHT_WOOD", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_LIGHT_WOOD", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_DRESSER_DESIGNER_BLACK", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_BLACK", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_DRESSER_DESIGNER_BLUE", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_BLUE", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "JAPANESE_DRESSER_JAPANESE_TANSU_NORMAL", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "JAPANESE_WOOD", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "JAPANESE_DRESSER_JAPANESE_TANSU_BLACK", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "JAPANESE_BLACK", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "JAPANESE_DRESSER_JAPANESE_TANSU_RED", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "JAPANESE_RED", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_RED", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_CHERRY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_CHERRY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_BLONDE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_BLONDE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_REFLECT_CHINESE_DARK", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_DARK_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_CHERRY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_CHERRY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_BLONDE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_BLONDE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_RED", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_COFFEE_CENTRAL_ASIAN_ASIAN_WOOD_CHINESE_DARK", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_DARK_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_OAK", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_CURLY_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_MAHOGANY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_GREEN", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_WHITE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LARGE_CLUB_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_BRIDS_RED", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LARGE_CLUB_CURLY_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_BLEACHED_OAK", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LARGE_CLUB_WHITE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_MAHOG", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LARGE_CLUB_MAHOGANY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_RED_ASH", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LARGE_CLUB_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_WHITE_OAK", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LARGE_CLUB_OAK", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LARGE_CLUB_TABLE_COFFEE_CLUB_RECTANGLE_LTBLUE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LARGE_CLUB_BLUE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_TABLE_COFFEE_COLONIAL_GOLDFLORAL", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_GOLD_FLORAL", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_TABLE_COFFEE_COLONIAL_PAONAZZETTO", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_TABLE_COFFEE_COLONIAL_PERLINO", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_PINK", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_TABLE_COFFEE_COLONIAL_WHITEMARBLE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_WHITE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_TABLE_COFFEE_END_DESIGNER_WHITE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_WHITE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_TABLE_COFFEE_END_DESIGNER_WALNUT", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_WALNUT", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LOFT_TABLE_COFFEE_LOFT_SHEEN", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LOFT_CONCRETE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LOFT_TABLE_COFFEE_LOFT_CONCRETE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LOFT_CONCRETE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LOFT_TABLE_COFFEE_LOFT_PATINA", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LOFT_PATINA", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_COFFEE_MOROCCAN_TOP_TABLE_STAR", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_STARS_INLAY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_COFFEE_MOROCCAN_TOP_TABLE_ALADDIN", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_YELLOW_INLAY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_COFFEE_MOROCCAN_TOP_TABLE_DETAIL", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_FANCY_INLAY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_COFFEE_MOROCCAN_TOP_TABLE_TILE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_TILE_INLAY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_COFFEE_MOROCCAN_WOODS_MAHOGNY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_MAHOGANY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_COFFEE_MOROCCAN_WOODS_YELLOWASPEN", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_YELLOW", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_COFFEE_MOROCCAN_WOODS_BLACK_LAQUER", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_BLACK", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_COFFEE_QUAINT_BLUE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BLUE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_COFFEE_QUAINT_GREEN", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_GREEN", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_COFFEE_QUAINT_WHITE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_WHITE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SPINDLE_TABLE_COFFEE_SPINDLE_WOOD_PAINTED", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SPINDLE_PAINTED", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SPINDLE_TABLE_COFFEE_SPINDLE_WOOD_RED", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SPINDLE_RED", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DESK_CENTRAL_ASIAN_BLACK", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_BLACK", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DESK_CENTRAL_ASIAN_WALNUT", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_WALNUT", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "CLUB_DESK_CLUB_DARKWOOD", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "CLUB_DARK_WOOD", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "QUAINT_DESK_QUAINT_GREEN", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_GREEN", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "QUAINT_DESK_QUAINT_WHITE", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_WHITE", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "QUAINT_DESK_QUAINT_BLUE", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BLUE", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "VALUE_DESK_VALUE_WOODWHITE", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "VALUE_LIGHT_WOOD", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "VALUE_DESK_VALUE_WOODRED", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "VALUE_RED", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "ACCESSORY_LUGGAGE_SURFACE", "org.lgna.story.resources.prop.SuitcaseResource" ),
					createMoreSpecificFieldReplacement( "SUITCASE", "org.lgna.story.resources.prop.SuitcaseResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_DINING_CLUB_NEDAR", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_LIGHT_WOOD", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_DINING_CLUB_OAK", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_OAK", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_DINING_CLUB_SEDAR", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_WOOD", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_DINING_MOROCCAN_TURQ", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_TURQUOISE", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_DINING_MOROCCAN_BLUE", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_BLUE", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_DINING_MOROCCAN_BLUE_LIGHT", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_LIGHT_BLUE", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_TABLE_DINING_ORIENTAL_DRAGON_BROWN", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_DRAGON_BROWN", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_TABLE_DINING_ORIENTAL_DRAGON_RED", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_DRAGON_RED", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_TABLE_DINING_ORIENTAL_LOTUS_BLACK", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_LOTUS_BLACK", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_TABLE_DINING_ORIENTAL_LOTUS_ORANGE", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_LOTUS_ORANGE", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_ASH", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "OUTDOOR_ASH", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_REDOAK", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "OUTDOOR_OAK", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_REDWOOD", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "OUTDOOR_RED", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_WHITE", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "OUTDOOR_WHITE", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_DINING_QUAINT_RED", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_RED", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_DINING_QUAINT_GREEN", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_GREEN", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_DINING_QUAINT_WHITE", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_WHITE", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_DINING_QUAINT_BLUE", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BLUE", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_CURLY_REDWOOD", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_REDWOOD", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_QUILTED_DARK_WOOD", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_DARK_WOOD", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_CURLY_LIGHT_WOOD", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_LIGHT_WOOD", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "BIRTHDAY_CAKE_MATERIALS", "org.lgna.story.resources.prop.CakeResource" ),
					createMoreSpecificFieldReplacement( "BIRTHDAY", "org.lgna.story.resources.prop.CakeResource" ),

					createMoreSpecificFieldPattern( "TRASHCAN_INDOOR_VALUE_CLEAN", "org.lgna.story.resources.prop.TrashcanResource" ),
					createMoreSpecificFieldReplacement( "TRASHCAN", "org.lgna.story.resources.prop.TrashcanResource" ),

					createMoreSpecificFieldPattern( "VEHICLE_HELICOPTER", "org.lgna.story.resources.prop.HelicopterResource" ),
					createMoreSpecificFieldReplacement( "HELICOPTER", "org.lgna.story.resources.prop.HelicopterResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_CHAIR_DINING_ART_NOUVEAU_LIGHT_CLEAN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "CLUB_CHAIR_DINING_CLUB_RED_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "CLUB_DARK_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "CLUB_CHAIR_DINING_CLUB_GREENLEATH", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "CLUB_DARK_GREEN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "CLUB_CHAIR_DINING_CLUB_LTGREENLEATH", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "CLUB_DARK_GREEN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_CHAIR_DINING_COLONIAL1_GOLD_PATTERN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_GOLD_PATTERN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_CHAIR_DINING_COLONIAL1_STRIPES", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_RED_STRIPES", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_CHAIR_DINING_COLONIAL1_BLUEPATTERN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_BLUE_PATTERN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_CHAIR_DINING_COLONIAL1_DIAMONDS", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_DIAMONDS", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_SRIPE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "FANCY_COLONIAL_RED_STRIPES", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_GOLDPATTERN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "FANCY_COLONIAL_GOLD_PATTERN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_BLUEPATTERN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "FANCY_COLONIAL_GOLD_PATTERN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_BLUESILK", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "FANCY_COLONIAL_RED_STRIPES", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_DIAMONDPATTERN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "FANCY_COLONIAL_GOLD_PATTERN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "PARK_LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "PARK_OAK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "PARK_LOVESEAT_PARK_BENCH_WALNUT", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "PARK_WALNUT", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "PARK_LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "PARK_RED", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "PARK_LOVESEAT_PARK_BENCH_OAKBLUE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "PARK_BLUE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "PARK_LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "PARK_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "PARK_LOVESEAT_PARK_BENCH_CHESTNUT", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "PARK_CHESTNUT", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_GREEN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "DANISH_MODERN_GREEN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_BABY_BLUE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "DANISH_MODERN_BLUE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_WHITE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "DANISH_MODERN_WHITE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_CHAIR_DINING_LOFT_SEAT_BLUE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_BLUE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_CHAIR_DINING_LOFT_FORK_BASE_WOOD_LIGHT", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_CHAIR_DINING_LOFT_FORK_BASE_WOOD_ORANGE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_ORANGE_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_CHAIR_DINING_LOFT_FORK_BASE_WOOD_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_RED_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_CHAIR_DINING_LOFT_SEAT_BLUE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_BLUE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_CHAIR_DINING_LOFT_FORK_BASE_WOOD_LIGHT", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_CHAIR_DINING_LOFT_FORK_BASE_WOOD_ORANGE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_ORANGE_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_CHAIR_DINING_LOFT_FORK_BASE_WOOD_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_RED_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_SEAT_GRAY", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_GRAY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_SEAT_TEAL", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_TEAL", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_SEAT_BLUE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_BLUE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_CHAIR_DINING_MOROCCAN_SURFACES_BLUE_ORANGE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_YELLOW", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_CHAIR_DINING_MOROCCAN_SURFACES_RED_CIRCLES", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_RED", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_CHAIR_DINING_MOROCCAN_SURFACES_RED_TAN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_WHITE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_CHAIR_DINING_MOROCCAN_SURFACES_BLUE_STRIPES", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_BLUE_STRIPES", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_CHAIR_DINING_ORIENTAL_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_CHAIR_DINING_ORIENTAL_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_CHAIR_DINING_ORIENTAL_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_RED", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_CHAIR_DINING_ORIENTAL_ORANGE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_ORANGE_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_LOFT_BOOKCASE_BRUSHED", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "LOFT_METAL", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_BOOKCASE_ART_NOUVEAU_SURFACE", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "CHEAP_BOOKCASE_CHEAP_OAK", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "CHEAP_OAK", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "CHEAP_BOOKCASE_CHEAP_MAHOGANY", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "CHEAP_MAHOGANY", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "CHEAP_BOOKCASE_CHEAP_PINE", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "CHEAP_PINE", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "CHEAP_BOOKCASE_CHEAP_BLACK_WASH", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "CHEAP_BLACK", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "CINDER_BLOCK_BOOKCASE_CINDERBLOCK_SHELVES_BLACKWASH", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "CINDER_BLOCK_BLACK", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "CINDER_BLOCK_BOOKCASE_CINDERBLOCK_SHELVES_OLDWOOD", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "CINDER_BLOCK_PLANK", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_BOOKCASE_COLONIAL_REDWOODCURLY", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_REDWOOD", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_BOOKCASE_COLONIAL_BROWNWOODCURLY", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_DARK_WOOD", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_BOOKCASE_COLONIAL_DARK_BROWN_WOODCURLY", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_WOOD", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "VALUE_BOOKCASE_VALUE_PRESSEDPINE", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "VALUE_DARK_PINE", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "VALUE_BOOKCASE_VALUE_PINE", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "VALUE_PINE", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADEON", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_ORANGE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "GARDEN_BOLLARD_LIGHTING_FLOOR_GARDEN_TIER_GREEN", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "GARDEN_BOLLARD_GREEN_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER_GREEN", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "GARDEN_TIER_GREEN_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_YELLOW_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "LOFT_YELLOW_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_BLUE_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "LOFT_BLUE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_GREEN_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "LOFT_GREEN_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_BLUE_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_BLUE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_GOLD_BLUE_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_GOLD_BLUE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_ORANGE_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_ORANGE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_RED_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_RED_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_ORANGE_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_ORANGE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_PINK_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_PINK_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_PINK", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_PINK_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_YELLOW", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_YELLOW_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_GREEN", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_GREEN_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "STUDIO_LIGHTING_FLOOR_STUDIO_LIGHTS_LIGHTS_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "STUDIO_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_BLACKPAINT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "VALUE_BLACK", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_REDPAINT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "VALUE_RED", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_TANPAINT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "VALUE_TAN", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_GREEN_PAINT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "VALUE_GREEN", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DARK_GREEN", "org.lgna.story.resources.prop.BambooThicketResource" ),
					createMoreSpecificFieldReplacement( "THICKET_DARK_GREEN", "org.lgna.story.resources.prop.BambooResource" ),

					createMoreSpecificFieldPattern( "LIGHT_GREEN", "org.lgna.story.resources.prop.BambooThicketResource" ),
					createMoreSpecificFieldReplacement( "THICKET_LIGHT_GREEN", "org.lgna.story.resources.prop.BambooResource" ),

					"name=\"org.lgna.story.resources.prop.BambooThicketResource",
					"name=\"org.lgna.story.resources.prop.BambooResource",

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_BLOND_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_BLONDE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_ROUGH", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_ROUGH", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_RED_LACQUER", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_RED_LAQUER", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_TABLE_TOP_ROUGH", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_TOP_ROUGH", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_TABLE_TOP_BLOND_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_TOP_BLONDE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_TABLE_TOP_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_TOP_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_TABLE_TOP_RED_LACQUER", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_TOP_RED_LAQUER", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIAL_BIRDSRED", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_CURLY_REDWOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_MAHOG", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_MAHOGANY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_GUMWOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_GREEN", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_REDASH", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_REDWOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_BLEACHEDOAK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_TABLE_END_COLONIAL2_TABLE_LIGHTWOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_LIGHT_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_TABLE_END_COLONIAL2_TABLE_REDWOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_REDWOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_TABLE_END_COLONIAL2_TABLE_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_TABLE_END_COLONIAL2_TABLE_DARKWOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_DARK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "OCTAGONAL_TABLE_END_OCTAGONAL_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "OCTAGONAL_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "OCTAGONAL_TABLE_END_OCTAGONAL_DARK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "OCTAGONAL_DARK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "OCTAGONAL_TABLE_END_OCTAGONAL_YELLOW", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "OCTAGONAL_YELLOW", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "OCTAGONAL_TABLE_END_OCTAGONAL_GREEN", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "OCTAGONAL_GREEN", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_END_QUAINT_FABRIC_GREEN", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_GREEN", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "UM_TABLE_END_UM_PURPLE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "UM_PURPLE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "UM_TABLE_END_UM_GREEN", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "UM_GREEN", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "UM_TABLE_END_UM_BLACK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "UM_BLACK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "UM_TABLE_END_UM_YELLOW", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "UM_YELLOW", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "UM_TABLE_END_UM_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "UM_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "UM_TABLE_END_UM_ORANGE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "UM_ORANGE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "VOLLEYBALL_LEATHER", "org.lgna.story.resources.prop.VolleyballResource" ),
					createMoreSpecificFieldReplacement( "VOLLEYBALL", "org.lgna.story.resources.prop.VolleyballResource" ),

					createMoreSpecificFieldPattern( "CAKE_WEDDING_FROSTING", "org.lgna.story.resources.prop.WeddingCakeResource" ),
					createMoreSpecificFieldReplacement( "WEDDING", "org.lgna.story.resources.prop.CakeResource" ),

					createMoreSpecificFieldPattern( "PLANT2", "org.lgna.story.resources.prop.SeaPlantResource" ),
					createMoreSpecificFieldReplacement( "SHORT", "org.lgna.story.resources.prop.SeaSpongeResource" ),

					createMoreSpecificFieldPattern( "PLANT3", "org.lgna.story.resources.prop.SeaPlantResource" ),
					createMoreSpecificFieldReplacement( "TALL", "org.lgna.story.resources.prop.SeaSpongeResource" ),

					"name=\"org.lgna.story.resources.prop.SeaPlantResource",
					"name=\"org.lgna.story.resources.prop.SeaSpongeResource",

					createMoreSpecificFieldPattern( "KITE", "org.lgna.story.resources.prop.KiteResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.KiteResource" ),

					createMoreSpecificFieldPattern( "BOULDER1_MARS", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER1_RED", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER2_MARS", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER2_RED", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER3_MARS", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER3_RED", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER4_MARS", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER4_RED", "org.lgna.story.resources.prop.BoulderResource" ),

					createMoreSpecificFieldPattern( "BOULDER5_MARS", "org.lgna.story.resources.prop.BoulderResource" ),
					createMoreSpecificFieldReplacement( "BOULDER5_RED", "org.lgna.story.resources.prop.BoulderResource" ),

					"name=\"org.lgna.story.resources.prop.JungleShrubResource",
					"name=\"org.lgna.story.resources.prop.JunglePlantResource",

					createMoreSpecificFieldPattern( "DRAGON_BABY", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "PINK", "org.lgna.story.resources.quadruped.BabyDragonResource" ),

					createMoreSpecificFieldPattern( "HARE", "org.lgna.story.resources.biped.HareResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.HareResource" ),

					createMoreSpecificFieldPattern( "RED", "org.lgna.story.resources.prop.ShortMushroomResource" ),
					createMoreSpecificFieldReplacement( "SHORT_RED", "org.lgna.story.resources.prop.MushroomResource" ),

					createMoreSpecificFieldPattern( "WHITE", "org.lgna.story.resources.prop.ShortMushroomResource" ),
					createMoreSpecificFieldReplacement( "SHORT_WHITE", "org.lgna.story.resources.prop.MushroomResource" ),

					"name=\"org.lgna.story.resources.prop.ShortMushroomResource",
					"name=\"org.lgna.story.resources.prop.MushroomResource",

					createMoreSpecificFieldPattern( "CARD01", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "ONE1", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "CARD02", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "TWO2", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "CARD04", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "FOUR4", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "CARD05", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "FIVE5", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "CARD06", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "SIX6", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "CARD07", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "SEVEN7", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "CARD08", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "EIGHT8", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "CARD09", "org.lgna.story.resources.biped.PlayingCardResource" ),
					createMoreSpecificFieldReplacement( "NINE9", "org.lgna.story.resources.biped.PlayingCardResource" ),

					createMoreSpecificFieldPattern( "MANGO_TREE", "org.lgna.story.resources.prop.MangoTreeResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.MangoTreeResource" ),

					createMoreSpecificFieldPattern( "ALIEN", "org.lgna.story.resources.biped.AlienResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.AlienResource" ),

					createMoreSpecificFieldPattern( "SNOWBOARD_YETI", "org.lgna.story.resources.prop.SnowboardResource" ),
					createMoreSpecificFieldReplacement( "ADULT_RED", "org.lgna.story.resources.prop.SnowboardResource" ),

					createMoreSpecificFieldPattern( "SNOWBOARD_YETI2", "org.lgna.story.resources.prop.SnowboardResource" ),
					createMoreSpecificFieldReplacement( "ADULT_GREEN", "org.lgna.story.resources.prop.SnowboardResource" ),

					createMoreSpecificFieldPattern( "SNOWBOARD_YETI_BABY", "org.lgna.story.resources.prop.SnowboardResource" ),
					createMoreSpecificFieldReplacement( "BABY_ORANGE", "org.lgna.story.resources.prop.SnowboardResource" ),

					createMoreSpecificFieldPattern( "SNOWBOARD_YETI_BABY2", "org.lgna.story.resources.prop.SnowboardResource" ),
					createMoreSpecificFieldReplacement( "BABY_PINK", "org.lgna.story.resources.prop.SnowboardResource" ),

					createMoreSpecificFieldPattern( "SPIRE1", "org.lgna.story.resources.prop.RockySpiresResource" ),
					createMoreSpecificFieldReplacement( "LARGE", "org.lgna.story.resources.prop.RockyOutcropResource" ),

					createMoreSpecificFieldPattern( "SPIRE2", "org.lgna.story.resources.prop.RockySpiresResource" ),
					createMoreSpecificFieldReplacement( "MEDIUM", "org.lgna.story.resources.prop.RockyOutcropResource" ),

					createMoreSpecificFieldPattern( "SPIRE3", "org.lgna.story.resources.prop.RockySpiresResource" ),
					createMoreSpecificFieldReplacement( "SMALL", "org.lgna.story.resources.prop.RockyOutcropResource" ),

					"name=\"org.lgna.story.resources.prop.RockySpiresResource",
					"name=\"org.lgna.story.resources.prop.RockyOutcropResource",

					createMoreSpecificFieldPattern( "ROUND", "org.lgna.story.resources.prop.LanternResource" ),
					createMoreSpecificFieldReplacement( "SHORT_AND_ROUND", "org.lgna.story.resources.prop.PaperLanternResource" ),

					createMoreSpecificFieldPattern( "OVAL", "org.lgna.story.resources.prop.LanternResource" ),
					createMoreSpecificFieldReplacement( "TALL_AND_ROUND", "org.lgna.story.resources.prop.PaperLanternResource" ),

					createMoreSpecificFieldPattern( "BOXY", "org.lgna.story.resources.prop.LanternResource" ),
					createMoreSpecificFieldReplacement( "SQUARE_HOURGLASS", "org.lgna.story.resources.prop.PaperLanternResource" ),

					createMoreSpecificFieldPattern( "POINTY", "org.lgna.story.resources.prop.LanternResource" ),
					createMoreSpecificFieldReplacement( "ROUND_HOURGLASS", "org.lgna.story.resources.prop.PaperLanternResource" ),

					"name=\"org.lgna.story.resources.prop.LanternResource",
					"name=\"org.lgna.story.resources.prop.PaperLanternResource",

					createMoreSpecificFieldPattern( "OAR", "org.lgna.story.resources.prop.OarResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.OarResource" ),

					createMoreSpecificFieldPattern( "SLED", "org.lgna.story.resources.prop.SledResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.SledResource" ),

					createMoreSpecificFieldPattern( "COW", "org.lgna.story.resources.quadruped.CowResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.quadruped.CowResource" ),

					createMoreSpecificFieldPattern( "DRAGON", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "PURPLE", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "DRAGON_BLUE", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "BLUE", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "DRAGON_GREEN", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "GREEN", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_STRIPES", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ADIRONDACK_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "ADIRONDACK_CHAIR_LIVING_ADIRONDACK_CUSHION_POLKA", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ADIRONDACK_RED", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_LOVESEAT_ART_NOUVEAU_FRAME_ANTIQUE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU_DARK_WOOD", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_MAHOGONY", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_RED", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_REDWOOD", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_WOOD_WHITE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_WHITE_WOOD", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_PINK_VELOUR", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_PINK", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_BLUE_VELOUR", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_RED_VELOUR", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_RED", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LOVESEAT_EXPENSIVE_CAMEL_BACK_CUSHION_BLACK_VELOUR", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_BLACK", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_BEIGE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_BROWN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_ORANGE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_ORANGE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_WHITE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_WHITE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_BLUE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_FABRIC_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_GREEN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_TAN", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_BROWN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_ORANGE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_ORANGE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_RED", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_RED", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_LOVSEATLOFT_MODERN_CUSHIONS_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_SOFA_MOROCCAN_BEIGECROSS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_LIGHT_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_SOFA_MOROCCAN_RED", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_RED", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "PARK_BENCH_LOVESEAT_PARK_BENCH_OAK", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_OAK", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "PARK_BENCH_LOVESEAT_PARK_BENCH_RED", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_RED", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "PARK_BENCH_LOVESEAT_PARK_BENCH_IVORY", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "PARK_BENCH_WHITE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_BLUE_FLOWERS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_PINK_FLOWERS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_PINK", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "VALUE_LOVESEAT_VALUE_RED_CHECKER", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "VALUE_RED_SQUARES", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "VALUE_LOVESEAT_VALUE_BLUE_CHECKER", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "VALUE_BLUE_SQUARES", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "VALUE_LOVESEAT_VALUE_FLOWER", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "VALUE_WHITE", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "VALUE1_SOFA_VALUE1_REDCHECKER", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "VALUE1_RED_SQUARES", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "VALUE1_SOFA_VALUE1_BLUE_CHECKER", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "VALUE1_BLUE_SQUARES", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL2_SOFA_COLONIAL2_NEONBLUE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL2_BLUE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL2_SOFA_COLONIAL2_GREEN_FLORAL", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL2_GREEN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "COLONIAL2_SOFA_COLONIAL2_WHITEDIAMOND", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL2_GRAY", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "STEEL_FRAME_SOFA_MODERN_STEEL_FRAME_FABRIC_LEATHER", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "STEEL_FRAME_BRWON_LEATHER", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_SOFA_MOROCCAN_BEIGECROSS", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_LIGHT_BLUE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "QUAINT_SOFA_QUAINT_FABRIC_BEIGE_FLOWERS", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_TAN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_CUTOUT_SOFA_UM_CUTOUT_RED", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_CUTOUT_RED", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "MODERN_DIAMOND_SOFA_U_M_DIAMOND_BABYBLUE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "MODERN_DIAMOND_TURQUOISE", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_DRESSER_DESIGNER_RED", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_RED", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "JAPANESE_DRESSER_JAPANESE_TANSU_LIGHT", "org.lgna.story.resources.prop.DresserResource" ),
					createMoreSpecificFieldReplacement( "JAPANESE_LIGHT_WOOD", "org.lgna.story.resources.prop.DresserResource" ),

					createMoreSpecificFieldPattern( "ART_NOVEAU_TABLE_COFFEE_ART_NOUVEAU_TABLE1", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "ART_NOVEAU_LIGHT_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "ART_NOVEAU_TABLE_COFFEE_ART_NOUVEAU_TABLE2", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "ART_NOVEAU_DARK_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "ART_NOVEAU_TABLE_COFFEE_ART_NOUVEAU_TABLE3", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "ART_NOVEAU_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SMALL_CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SMALL_CLUB_BLUE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_TABLE_COFFEE_END_DESIGNER_ASH", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_ASH", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "LOFT_TABLE_COFFEE_LOFT_RED_METAL", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "LOFT_RED", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_COFFEE_MOROCCAN_WOODS_CHERRY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "PINE_TABLE_COFFEE_PINE_CEDAR_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "PINE_CEDAR", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "PINE_TABLE_COFFEE_PINE_BLONDE_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "PINE_BLONDE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "PINE_TABLE_COFFEE_PINE_HONEY_PINE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "PINE_HONEY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "PINE_TABLE_COFFEE_PINE_WALNUT_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "PINE_WALNUT", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "PINE_TABLE_COFFEE_PINE_BIRCH_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "PINE_BIRCH", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_COFFEE_QUAINT_RED", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_RED", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "SPINDLE_TABLE_COFFEE_SPINDLE_WOOD_OLDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "SPINDLE_OLD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DESK_CENTRAL_ASIAN_RED", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_RED", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DESK_CENTRAL_ASIAN_AVODIRE", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_REDWOOD", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "CLUB_DESK_CLUB_ASH", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "CLUB_ASH", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "CLUB_DESK_CLUB_REDWOOD", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "CLUB_REDWOOD", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "QUAINT_DESK_QUAINT_RED", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_RED", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "VALUE_DESK_VALUE_WOOD_METAL", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "VALUE_METAL", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "VALUE_DESK_VALUE_WOODMAPPLE", "org.lgna.story.resources.prop.DeskResource" ),
					createMoreSpecificFieldReplacement( "VALUE_MAPLE", "org.lgna.story.resources.prop.DeskResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_DINING_CLUB_ROOT", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_CURLY_DARK_WOOD", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_DINING_CLUB_RED", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_REDWOOD", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_DINING_CLUB_REDDARK", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_MAHOGANY", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_DINING_CLUB_WOOD", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_DARK_WOOD", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_DINING_CLUB_PINE", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_PINE", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_DINING_MOROCCAN_GREEN", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_GREEN", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_TABLE_DINING_ORIENTAL_FISH_BROWN", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_FISH_BROWN", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_TABLE_DINING_ORIENTAL_FISH_RED", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_FISH_RED", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "OUTDOOR_TABLE_DINING_OUTDOOR_WOOD_CROSSPINE", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "OUTDOOR_PINE", "org.lgna.story.resources.prop.DiningTableResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_CHAIR_DINING_ART_NOUVEAU_MID_CLEAN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_CHAIR_DINING_ART_NOUVEAU_DARK_CLEAN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU_DARK_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "CLUB_CHAIR_DINING_CLUB_OAKCANE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "CLUB_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_CHAIR_DINING_COLONIAL1_PURPLE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_BLUE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "COLONIAL_CHAIR_DINING_COLONIAL1_GOLDEN2", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "COLONIAL_YELLOW", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_GOLDFLOWER", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "FANCY_COLONIAL_GOLD_PATTERN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "FANCY_COLONIAL_CHAIR_DINING_COLONIAL2_BEIGE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "FANCY_COLONIAL_TAN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "PARK_LOVESEAT_PARK_BENCH_OAKGREEN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "PARK_GREEN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_POLKA", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "DANISH_MODERN_PURPLE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "DANISH_MODERN_CHAIR_DINING_DANISH_MODERN_CUSHIONS_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "DANISH_MODERN_RED", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_CHAIR_DINING_LOFT_SEAT_GREEN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_GREEN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_CHAIR_DINING_LOFT_SEAT_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_RED", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_CHAIR_DINING_LOFT_SEAT_TAN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_WHITE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_CHAIR_DINING_LOFT_SEAT_ORANGE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_YELLOW", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_CHAIR_DINING_LOFT_FORK_BASE_IRON", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_CHAIR_DINING_LOFT_SEAT_GREEN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_GREEN", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_CHAIR_DINING_LOFT_SEAT_ORANGE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_YELLOW", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_CHAIR_DINING_LOFT_SEAT_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_RED", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_CHAIR_DINING_LOFT_SEAT_TAN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_WHITE", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_CHAIR_DINING_LOFT_FORK_BASE_IRON", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_BODY_BLACK", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_BODY_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_BODY_TEAL", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_TEAL_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_BODY_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_RED_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_BODY_BLUE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_BLUE_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_SEAT_BUMBLE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_YELLOW_STRIPES", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_SEAT_STRAWBERRY", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_RED", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_CHAIR_DINING_MODERATE_SEAT_YELLOW", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_YELLOW", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_CHAIR_DINING_ORIENTAL_BLACK", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_LOFT_BOOKCASE_WOOD_DARK", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "LOFT_DARK_WOOD", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "LOFT_LOFT_BOOKCASE_WOOD_LIGHT", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "LOFT_LIGHT_WOOD", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "LOFT_LOFT_BOOKCASE_WOOD_MEDIUM", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "LOFT_WOOD", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "CHEAP_BOOKCASE_CHEAP_WOOD_PLANK", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "CHEAP_PLANK", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "CINDER_BLOCK_BOOKCASE_CINDERBLOCK_SHELVES_KNOTTYPINE", "org.lgna.story.resources.prop.BookcaseResource" ),
					createMoreSpecificFieldReplacement( "CINDER_BLOCK_PINE", "org.lgna.story.resources.prop.BookcaseResource" ),

					createMoreSpecificFieldPattern( "SWING_ARM_LIGHTING_FLOOR_CLUB_LAMP_LAMP", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "SWING_ARM", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADE", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_BLUE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_BLUESHADEON", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_BLUE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADE", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_WHITE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_PLAINSHADEON", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_WHITE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADE", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_GREEN_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_OLIVESHADEON", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_GREEN_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_ORANGESHADE", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_ORANGE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADE", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_RED_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "DESIGNER_LIGHTING_FLOOR_DESIGNER_SHADE_REDSHADEON", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "DESIGNER_RED_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "GARDEN_BOLLARD_LIGHTING_FLOOR_GARDEN_TIER_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "GARDEN_BOLLARD_BLACK_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "GARDEN_BOLLARD_LIGHTING_FLOOR_GARDEN_TIER", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "GARDEN_BOLLARD_BLACK_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "GARDEN_BOLLARD_LIGHTING_FLOOR_GARDEN_TIER_GREEN_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "GARDEN_BOLLARD_GREEN_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "GARDEN_TIER_BLACK_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "GARDEN_TIER_BLACK_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "GARDEN_TIER_LIGHTING_FLOOR_GARDEN_TIER_GREEN_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "GARDEN_TIER_GREEN_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_ORANGE_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					NO_REPLACEMENT,

					createMoreSpecificFieldPattern( "LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_RED_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "LOFT_RED_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_YELLOW_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "LOFT_YELLOW_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_BLUE_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "LOFT_BLUE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_RED_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "LOFT_RED_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "LOFT_LIGHTING_FLOOR_LOFT_LAMP_SHADE_GREEN_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "LOFT_GREEN_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_YELLOW_UNLIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_YELLOW_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_BLUES_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_BLUE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_GOLD_BLUE_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_GOLD_BLUE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_RED_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_RED_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_LIGHTING_FLOOR_MOROCCAN_SHADE_YELLOW_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_YELLOW_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BEIGE_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_ORANGE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BEIGE", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_ORANGE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_WHITE", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_WHITE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BLUE", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BLUE_OFF", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_BLUE_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BLUE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_GREEN_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_GREEN_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_WHITE_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_WHITE_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "QUAINT_LIGHTING_FLOOR_QUAINT_SHADE_YELLOW_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_YELLOW_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "STUDIO_LIGHTING_FLOOR_STUDIO_LIGHTS_LIGHTS_LIT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "STUDIO_ON", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "VALUE_LIGHTING_FLOOR_VALUE_PAINTED_METAL_WHITEPAINT", "org.lgna.story.resources.prop.LampResource" ),
					createMoreSpecificFieldReplacement( "VALUE_WHITE", "org.lgna.story.resources.prop.LampResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_BROWN", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TABLE_END_CENTRAL_ASIAN_WOOD_DARK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_DARK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_WHITEOAK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_OAK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CLUB_TABLE_COFFEE_CLUB1_X1_MATERIALS_LTBLUE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CLUB_BLUE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_END_MOROCCAN_END_TABLE_ALADDIN", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_YELLOW_INLAY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_END_MOROCCAN_END_TABLE_STAR", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_STARS_INLAY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_END_MOROCCAN_END_TABLE_TILE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_TILE_INLAY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TABLE_END_MOROCCAN_END_TABLE_DETAIL", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_FANCY_INLAY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "OCTAGONAL_TABLE_END_OCTAGONAL_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "OCTAGONAL_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_END_QUAINT_FABRIC_BLUE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BLUE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_END_QUAINT_FABRIC_PINK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_PINK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_END_QUAINT_FABRIC_BEIGE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_YELLOW", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_END_QUAINT_FABRIC_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_TABLE_END_QUAINT_FABRIC_WHITE_FLOWERS", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_FLOWERS", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_GREEN", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_GREEN_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_CREAM", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_CREAM_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_RED", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_RED_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_WHITE_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_MARBLE_BLACK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_BLACK_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_SANTA_MARIA", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_DARK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_BLACKWOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_BLACK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_WHITE_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_TABLE_END_TRIANGULAR_TILE_WOOD_RED_OAK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRAINGULAR_OAK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "UM_TABLE_END_UM_BLUE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "UM_BLUE", "org.lgna.story.resources.prop.EndTableResource" ),

					"name=\"org.lgna.story.resources.prop.WeddingCakeResource",
					"name=\"org.lgna.story.resources.prop.CakeResource"
			),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.59.0.0" ),
					new org.lgna.project.Version( "3.1.68.0.0" ),

					createMoreSpecificFieldPattern( "STRAIGHT1_RIVERBANK2", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT1", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT1_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT1", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT2_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT2", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT2_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT2", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT3_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT3", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT3_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT3", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT4_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT4", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT4_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT4", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE1_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE1", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE1_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE1", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE2_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE2", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE2_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE2", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE3_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE3", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE3_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE3", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE4_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE4", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE4_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE4", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "BOW1_RIVERBANK3", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW1", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "BOW1_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW1", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "BOW2_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW2", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "BOW2_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW2", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "BOW3_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW3", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "BOW3_BROWN", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW3", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "PINK", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_PINK", "org.lgna.story.resources.quadruped.DragonBabyResource" ),

					createMoreSpecificFieldPattern( "AQUA", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_AQUA", "org.lgna.story.resources.quadruped.DragonBabyResource" ),

					createMoreSpecificFieldPattern( "BLUE", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_BLUE", "org.lgna.story.resources.quadruped.DragonBabyResource" ),

					createMoreSpecificFieldPattern( "GREEN", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_GREEN", "org.lgna.story.resources.quadruped.DragonBabyResource" ),

					createMoreSpecificFieldPattern( "RED", "org.lgna.story.resources.quadruped.BabyDragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_RED", "org.lgna.story.resources.quadruped.DragonBabyResource" ),

					"name=\"org.lgna.story.resources.quadruped.BabyDragonResource",
					"name=\"org.lgna.story.resources.quadruped.DragonBabyResource",

					createMoreSpecificFieldPattern( "WITH_SCARF", "org.lgna.story.resources.biped.BabyYetiResource" ),
					createMoreSpecificFieldReplacement( "WITH_SCARF", "org.lgna.story.resources.biped.YetiBabyResource" ),

					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.biped.BabyYetiResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.YetiBabyResource" ),

					"name=\"org.lgna.story.resources.biped.BabyYetiResource",
					"name=\"org.lgna.story.resources.biped.YetiBabyResource",

					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.marinemammal.BabyWalrusResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.marinemammal.WalrusBabyResource" ),

					"name=\"org.lgna.story.resources.marinemammal.BabyWalrusResource",
					"name=\"org.lgna.story.resources.marinemammal.WalrusBabyResource",

					createMoreSpecificFieldPattern( "ROSE_RED", "org.lgna.story.resources.prop.RoseResource" ),
					createMoreSpecificFieldReplacement( "RED", "org.lgna.story.resources.prop.RoseResource" ),

					createMoreSpecificFieldPattern( "ROSE_WHITE", "org.lgna.story.resources.prop.RoseResource" ),
					createMoreSpecificFieldReplacement( "WHITE", "org.lgna.story.resources.prop.RoseResource" ),

					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.prop.IceFloeResource" ),
					createMoreSpecificFieldReplacement( "ICE_FLOE1", "org.lgna.story.resources.prop.IceFloeResource" ),

					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.prop.CanyonSpiresResource" ),
					createMoreSpecificFieldReplacement( "DESERT", "org.lgna.story.resources.prop.CanyonSpiresResource" ),

					createMoreSpecificFieldPattern( "CANYON_SPIRES", "org.lgna.story.resources.prop.CanyonSpiresResource" ),
					createMoreSpecificFieldReplacement( "DESERT", "org.lgna.story.resources.prop.CanyonSpiresResource" ),

					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.prop.CliffWallResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_DESERT", "org.lgna.story.resources.prop.CliffWallResource" ),

					createMoreSpecificFieldPattern( "CLIFF_WALL", "org.lgna.story.resources.prop.CliffWallResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_DESERT", "org.lgna.story.resources.prop.CliffWallResource" ),

					createMoreSpecificFieldPattern( "PURPLE", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_PURPLE", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "GREEN", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_GREEN", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "BLUE", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_BLUE", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "ORANGE", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_ORANGE", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "RED", "org.lgna.story.resources.quadruped.DragonResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_RED", "org.lgna.story.resources.quadruped.DragonResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_DARK_WOOD", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU_DARK_WOOD_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU_OAK_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "ART_NOUVEAU_OAK", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "ART_NOUVEAU_OAK_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_WHITE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_WHITE_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_BLACK", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_BLACK_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_BLUE_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_PINK", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_PINK_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_RED", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_RED_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_LIGHT_WOOD", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_WHITE_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_BLACK_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_REDWOOD", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_RED_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "CAMEL_BACK_WHITE_WOOD", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "CAMEL_BACK_WHITE_MAHOGANY", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_BLUE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_BLUE_BLUE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_GREEN", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_GREEN_GREEN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_ORANGE", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_ORANGE_GREEN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_RED", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_ORANGE_GREEN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_BROWN", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_ORANGE_GREEN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_BROWN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_BLUE_GREEN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_BLUE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_BLUE_BLUE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_GREEN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_GREEN_GREEN_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_ORANGE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_ORANGE_ORANGE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "MODERN_LOFT_WHITE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),
					createMoreSpecificFieldReplacement( "MODERN_LOFT_BLUE_BLUE_CUSHIONS", "org.lgna.story.resources.prop.LoveseatResource" ),

					createMoreSpecificFieldPattern( "QUAINT_WHITE", "org.lgna.story.resources.prop.SofaResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_TAN", "org.lgna.story.resources.prop.SofaResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_FANCY_BLONDE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_BLONDE_BLONDE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_FANCY_CHERRY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_CHERRY_CHERRY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_FANCY_DARK_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_DARK_WOOD_DARK_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_FANCY_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_REDWOOD_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_BLONDE", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_BLONDE_BLONDE", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_CHERRY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_CHERRY_CHERRY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DARK_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_DARK_WOOD_DARK_WOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_FANCY_REDWOOD_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_YELLOW_INLAY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_YELLOW_INLAY_BLACK", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_FANCY_INLAY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_FANCY_INLAY_BLACK", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_STARS_INLAY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_STARS_INLAY_BLACK", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_TILE_INLAY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_TILE_INLAY_BLACK", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_BLACK", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_TILE_INLAY_BLACK", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_TILE_INLAY_REDWOOD", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_MAHOGANY", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_TILE_INLAY_MAHOGANY", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "MOROCCAN_YELLOW", "org.lgna.story.resources.prop.CoffeeTableResource" ),
					createMoreSpecificFieldReplacement( "MOROCCAN_FANCY_INLAY_YELLOW", "org.lgna.story.resources.prop.CoffeeTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_DRAGON_BROWN", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_DRAGON_BROWN", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_DRAGON_RED", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_DRAGON_RED", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_FISH_BROWN", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_FISH_BROWN", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_FISH_RED", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_FISH_RED", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_LOTUS_BLACK", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_LOTUS_BLACK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "ORIENTAL_LOTUS_ORANGE", "org.lgna.story.resources.prop.DiningTableResource" ),
					createMoreSpecificFieldReplacement( "ORIENTAL_LOTUS_ORANGE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "LOFT_BLACK_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OAK_BLACK_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "LOFT_DARK_HONEY_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OAK_DARK_HONEY_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "LOFT_SWIRLY_BROWN_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_DARK_WOOD_DARK_HONEY_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "LOFT_DARK_RED_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_RED_FINISH_DARK_HONEY_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "LOFT_MEDIUM_BROWN_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_DARK_WOOD_DARK_HONEY_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "LOFT_RED_FINISH", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_RED_FINISH_BLACK_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "LOFT_DARK_WOOD", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_DARK_WOOD_BLACK_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "LOFT_OAK", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OAK_DARK_HONEY_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "LOFT_HONEY", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OAK_DARK_HONEY_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "LOFT_MAPLE", "org.lgna.story.resources.prop.ArmoireResource" ),
					createMoreSpecificFieldReplacement( "LOFT_DARK_WOOD_BLACK_TRIM", "org.lgna.story.resources.prop.ArmoireResource" ),

					createMoreSpecificFieldPattern( "GREEN", "org.lgna.story.resources.prop.GrillResource" ),
					createMoreSpecificFieldReplacement( "GREEN_LIT", "org.lgna.story.resources.prop.GrillResource" ),

					createMoreSpecificFieldPattern( "BLACK", "org.lgna.story.resources.prop.GrillResource" ),
					createMoreSpecificFieldReplacement( "BLACK_LIT", "org.lgna.story.resources.prop.GrillResource" ),

					createMoreSpecificFieldPattern( "YELLOW", "org.lgna.story.resources.prop.GrillResource" ),
					createMoreSpecificFieldReplacement( "YELLOW_LIT", "org.lgna.story.resources.prop.GrillResource" ),

					createMoreSpecificFieldPattern( "RED", "org.lgna.story.resources.prop.GrillResource" ),
					createMoreSpecificFieldReplacement( "RED_LIT", "org.lgna.story.resources.prop.GrillResource" ),

					createMoreSpecificFieldPattern( "BLUE", "org.lgna.story.resources.prop.GrillResource" ),
					createMoreSpecificFieldReplacement( "BLUE_LIT", "org.lgna.story.resources.prop.GrillResource" ),

					createMoreSpecificFieldPattern( "HELICOPTER", "org.lgna.story.resources.prop.HelicopterResource" ),
					createMoreSpecificFieldReplacement( "MILITARY", "org.lgna.story.resources.prop.HelicopterResource" ),

					createMoreSpecificFieldPattern( "CANDY_FACTORY", "org.lgna.story.resources.prop.CandyFactoryResource" ),
					createMoreSpecificFieldReplacement( "LIGHT_OFF", "org.lgna.story.resources.prop.CandyFactoryResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_BLACK", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_WHITE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_RED_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_ORANGE_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_RED_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_RED_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_RED_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_BLUE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_BLUE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_GREEN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_GREEN_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_YELLOW", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_YELLOW_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_RED_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_FORK_WHITE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_WHITE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_WHITE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_WHITE_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_ORANGE_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_YELLOW_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_RED_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_FORK_RED_LIGHT_WOOD", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_BLUE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_BLUE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_GREEN", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_GREEN_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_YELLOW", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_YELLOW_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_RED_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "LOFT_OFFICE_WHITE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "LOFT_OFFICE_WHITE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_BLACK", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_BLUE_BLACK_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_BLUE_BODY", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_BLUE_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_RED_BODY", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_RED_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_TEAL_BODY", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_TEAL_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_WOOD", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_BLUE_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_BLUE", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_BLUE_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_YELLOW_STRIPES", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_YELLOW_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_GRAY", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_GRAY_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_RED", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_RED_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_TEAL", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_TEAL_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "MODERATE_YELLOW", "org.lgna.story.resources.prop.ChairResource" ),
					createMoreSpecificFieldReplacement( "MODERATE_YELLOW_WOOD_BODY", "org.lgna.story.resources.prop.ChairResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TOP_BLONDE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_WOOD_TOP_BLONDE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TOP_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_WOOD_TOP_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TOP_RED_LAQUER", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_DARK_WOOD_TOP_RED_LAQUER", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_TOP_ROUGH", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_ROUGH_TOP_ROUGH", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_BLONDE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_WOOD_TOP_BLONDE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_DARK_WOOD_TOP_BLONDE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_WOOD_TOP_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_DARK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_DARK_WOOD_TOP_BLONDE", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_RED_LAQUER", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_DARK_WOOD_TOP_RED_LAQUER", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "CENTRAL_ASIAN_ROUGH", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "CENTRAL_ASIAN_ROUGH_TOP_ROUGH", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_YELLOW", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_YELLOW_WHITE_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_BLUE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_BLUE_BLUE_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_GREEN", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_GREEN_GREEN_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_PINK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_PINK_RED_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_WHITE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_WHITE_WHITE_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "QUAINT_FLOWERS", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "QUAINT_FLOWERS_WHITE_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_BLACK_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_BLACK_MARBLE_OAK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_CREAM_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_GREEN_MARBLE_OAK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_GREEN_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_GREEN_MARBLE_OAK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_RED_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_GREEN_MARBLE_OAK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_WHITE_MARBLE", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_WHITE_MARBLE_OAK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_BLACK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_BLACK_MARBLE_BLACK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_CHERRY", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_GREEN_MARBLE_WHITE_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_OAK", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_GREEN_MARBLE_OAK", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_DARK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_BLACK_MARBLE_BLACK_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					createMoreSpecificFieldPattern( "TRAINGULAR_WHITE_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),
					createMoreSpecificFieldReplacement( "TRIANGULAR_WHITE_MARBLE_WHITE_WOOD", "org.lgna.story.resources.prop.EndTableResource" ),

					//sims person changes
					createMoreSpecificFieldPattern( "BLACK", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),
					createMoreSpecificFieldReplacement( "BLACK_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),
					createMoreSpecificFieldPattern( "BLOND", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),
					createMoreSpecificFieldReplacement( "BLOND_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),
					createMoreSpecificFieldPattern( "BROWN", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),
					createMoreSpecificFieldReplacement( "BROWN_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),
					createMoreSpecificFieldPattern( "GREY", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),
					createMoreSpecificFieldReplacement( "GREY_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),
					createMoreSpecificFieldPattern( "RED", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),
					createMoreSpecificFieldReplacement( "RED_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairTopHat" ),

					createMoreSpecificFieldPattern( "BLACK", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),
					createMoreSpecificFieldReplacement( "BLACK_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),
					createMoreSpecificFieldPattern( "BLOND", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),
					createMoreSpecificFieldReplacement( "BLOND_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),
					createMoreSpecificFieldPattern( "BROWN", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),
					createMoreSpecificFieldReplacement( "BROWN_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),
					createMoreSpecificFieldPattern( "GREY", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),
					createMoreSpecificFieldReplacement( "GREY_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),
					createMoreSpecificFieldPattern( "RED", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),
					createMoreSpecificFieldReplacement( "RED_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedoraCasual" ),

					//map both KINKY and STRAIGHT to replacement
					createMoreSpecificFieldPattern( "KINKY_BLACK", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "BLACK_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "KINKY_BLOND", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "BLOND_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "KINKY_BROWN", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "BROWN_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "KINKY_GREY", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "GREY_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "KINKY_RED", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "RED_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),

					createMoreSpecificFieldPattern( "STRAIGHT_BLACK", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "BLACK_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "STRAIGHT_BLOND", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "BLOND_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "STRAIGHT_BROWN", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "BROWN_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "STRAIGHT_GREY", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "GREY_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "STRAIGHT_RED", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "RED_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),

					createMoreSpecificFieldPattern( "STRAIGHT_BLACK", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "BLACK_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "STRAIGHT_BLOND", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "BLOND_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "STRAIGHT_BROWN", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "BROWN_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "STRAIGHT_GREY", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "GREY_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldPattern( "STRAIGHT_RED", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),
					createMoreSpecificFieldReplacement( "RED_BLACK_HAT", "org.lgna.story.resources.sims2.MaleAdultHairHatFedora" ),

					createMoreSpecificFieldPattern( "STUBBLE_BLACK", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),
					createMoreSpecificFieldReplacement( "BLACK", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),
					createMoreSpecificFieldPattern( "STUBBLE_BLOND", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),
					createMoreSpecificFieldReplacement( "BLOND", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),
					createMoreSpecificFieldPattern( "STUBBLE_BROWN", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),
					createMoreSpecificFieldReplacement( "BROWN", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),
					createMoreSpecificFieldPattern( "STUBBLE_GREY", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),
					createMoreSpecificFieldReplacement( "GREY", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),
					createMoreSpecificFieldPattern( "STUBBLE_RED", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),
					createMoreSpecificFieldReplacement( "RED", "org.lgna.story.resources.sims2.MaleAdultHairBald" ),

					createMoreSpecificFieldPattern( "BLUEPINSTRIPE", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitOpenCoatLongPants" ),
					createMoreSpecificFieldReplacement( "FORMAL_BLUE_PINSTRIPE", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitOpenCoatLongPants" ),
					createMoreSpecificFieldPattern( "BROWNTWEED", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitOpenCoatLongPants" ),
					createMoreSpecificFieldReplacement( "FORMAL_BROWN_TWEED", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitOpenCoatLongPants" ),
					createMoreSpecificFieldPattern( "GREYPINSTRIPE", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitOpenCoatLongPants" ),
					createMoreSpecificFieldReplacement( "FORMAL_GREY_PINSTRIPE", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitOpenCoatLongPants" ),
					createMoreSpecificFieldPattern( "GREYTWEED", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitOpenCoatLongPants" ),
					createMoreSpecificFieldReplacement( "FORMAL_GREY_TWEED", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitOpenCoatLongPants" ),

					//map removed swimwear to existing swimwear
					createMoreSpecificFieldPattern( "BLUEBIKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLUE_WAVE_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "BLUESINGLE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLUE_DIVING", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "GRAYSINGLE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLUE_DIVING", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "GREENSINGLE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLUE_DIVING", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "LIMEBIKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "LIME_WAVE_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "PINKBIKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "PINK_WAVE_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "PINKSINGLE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "PINK_DIVING", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "REDBIKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "RED_WAVE_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "REDSINGLE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "RED_DIVING", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "YELLOWBIKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "YELLOW_WAVE_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "BLUEPAISLEY", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLUE_FLOWER", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "CLASSICBIKINIBLACK", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLACK_RAINBOW_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "SPORTBIKINIBLUE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLUE_WAVE_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "SPORTBIKINIVIOLET", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLUE_WAVE_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "STRINGBIKINIBROWN", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLACK_RAINBOW_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldPattern( "STRINGBIKINIBURGANDY", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),
					createMoreSpecificFieldReplacement( "BLACK_RAINBOW_TANKINI", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSwimwear" ),

					//map removed dresses to existing dresses
					createMoreSpecificFieldPattern( "REDHOLE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitDressLongTwo" ),
					createMoreSpecificFieldReplacement( "RED", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitDressLongTwo" ),
					createMoreSpecificFieldPattern( "BLACKHOLE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitDressLongTwo" ),
					createMoreSpecificFieldReplacement( "BLACK", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitDressLongTwo" ),
					createMoreSpecificFieldPattern( "LEOPARD", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitDressLongTwo" ),
					createMoreSpecificFieldReplacement( "CREAM", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitDressLongTwo" ),

					//Name change
					createMoreSpecificFieldPattern( "BLUE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitPowerSuit" ),
					createMoreSpecificFieldReplacement( "BLUE_PINSTRIPE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitPowerSuit" ),

					//Name change...Where did SOCIALWORKER come from?
					createMoreSpecificFieldPattern( "SOCIALWORKER", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSuit" ),
					createMoreSpecificFieldReplacement( "BLACK", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitSuit" ),

					//map removed swimwear to existing swimwear
					createMoreSpecificFieldPattern( "TANKINIGREEN", "org.lgna.story.resources.sims2.ChildFullBodyOutfitNaked" ),
					createMoreSpecificFieldReplacement( "TROPIC_SEA_SWIM", "org.lgna.story.resources.sims2.ChildFullBodyOutfitNaked" ),
					createMoreSpecificFieldPattern( "TANKINIPINK", "org.lgna.story.resources.sims2.ChildFullBodyOutfitNaked" ),
					createMoreSpecificFieldReplacement( "TROPIC_BERRY_SWIM", "org.lgna.story.resources.sims2.ChildFullBodyOutfitNaked" ),
					createMoreSpecificFieldPattern( "TANKINISTRIPES", "org.lgna.story.resources.sims2.ChildFullBodyOutfitNaked" ),
					createMoreSpecificFieldReplacement( "TROPIC_FIRE_SWIM", "org.lgna.story.resources.sims2.ChildFullBodyOutfitNaked" ),
					createMoreSpecificFieldPattern( "WHITEUNDER", "org.lgna.story.resources.sims2.ChildFullBodyOutfitNaked" ),
					createMoreSpecificFieldReplacement( "WHITE_CAMISOLE", "org.lgna.story.resources.sims2.ChildFullBodyOutfitNaked" ),

					//Name change
					createMoreSpecificFieldPattern( "FRIED", "org.lgna.story.resources.sims2.ChildHairShocked" ),
					createMoreSpecificFieldReplacement( "BLACK", "org.lgna.story.resources.sims2.ChildHairShocked" ),

					//other fields handled by underscore migration
					createMoreSpecificFieldPattern( "GREENPANTSFLOWERS", "org.lgna.story.resources.sims2.FemaleChildFullBodyOutfitTShirtPants" ),
					createMoreSpecificFieldReplacement( "GREEN_PANTS_SUNFLOWER", "org.lgna.story.resources.sims2.FemaleChildFullBodyOutfitTShirtPants" ),

					//mail delivery changes class name as well as constant
					createMoreSpecificFieldPattern( "STANDARDBLUE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitMailDelivery" ),
					createMoreSpecificFieldReplacement( "BLUE", "org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitDeliveryPerson" ),
					createMoreSpecificFieldPattern( "STANDARDBLUE", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitMailDelivery" ),
					createMoreSpecificFieldReplacement( "BLUE", "org.lgna.story.resources.sims2.MaleAdultFullBodyOutfitDeliveryPerson" )

			),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.68.0.0" ),
					new org.lgna.project.Version( "3.1.69.0.0" ),

					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.prop.CaveResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_UNDERWATER", "org.lgna.story.resources.prop.CaveResource" ),

					createMoreSpecificFieldPattern( "DESERT", "org.lgna.story.resources.prop.CliffWallResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_DESERT", "org.lgna.story.resources.prop.CliffWallResource" ),

					createMoreSpecificFieldPattern( "MARS", "org.lgna.story.resources.prop.CliffWallResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_MARS", "org.lgna.story.resources.prop.CliffWallResource" )
			),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.69.0.0" ),
					new org.lgna.project.Version( "3.1.70.0.0" ),

					createMoreSpecificFieldPattern( "STRAIGHT1", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT1_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT2", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT2_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT3", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT3_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT4", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT4_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE1", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE1_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE2", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE2_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE3", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE3_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "CURVE4", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "CURVE4_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "BOW1", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW1_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "BOW2", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW2_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "BOW3", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW3_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "SHARP_BEND", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "SHARP_BEND_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					//note: possible duplicate
					createMoreSpecificFieldPattern( "BOW1_RIVERBANK3", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "BOW1_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),

					createMoreSpecificFieldPattern( "STRAIGHT1_RIVERBANK2", "org.lgna.story.resources.prop.RiverPieceResource" ),
					createMoreSpecificFieldReplacement( "STRAIGHT1_BLUE", "org.lgna.story.resources.prop.RiverPieceResource" ),
					//

					createMoreSpecificFieldPattern( "WOODEN_BOAT", "org.lgna.story.resources.aircraft.WoodenBoatResource" ),
					createMoreSpecificFieldReplacement( "WOODEN_BOAT", "org.lgna.story.resources.watercraft.WoodenBoatResource" ),

					"name=\"org.lgna.story.resources.aircraft.WoodenBoatResource",
					"name=\"org.lgna.story.resources.watercraft.WoodenBoatResource",

					createMoreSpecificFieldPattern( "SAILBOAT", "org.lgna.story.resources.aircraft.SailboatResource" ),
					createMoreSpecificFieldReplacement( "SAILBOAT", "org.lgna.story.resources.watercraft.SailboatResource" ),

					"name=\"org.lgna.story.resources.aircraft.SailboatResource",
					"name=\"org.lgna.story.resources.watercraft.SailboatResource"
			),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.70.0.0" ),
					new org.lgna.project.Version( "3.1.85.0.0" ),

					//<note: moved from 68 - 69 migration when a world with the UFO discovered with old resource>
					"name=\"org.lgna.story.resources.prop.HelicopterResource",
					"name=\"org.lgna.story.resources.prop.HelicopterPropResource",

					createMoreSpecificFieldPattern( "UFO", "org.lgna.story.resources.prop.UFOResource" ),
					createMoreSpecificFieldReplacement( "U_F_O_PROP", "org.lgna.story.resources.prop.UFOPropResource" ),

					//added for older projects
					//todo: do others require this?
					"name=\"org.lgna.story.resources.prop.UFOResource",
					"name=\"org.lgna.story.resources.prop.UFOPropResource",
					//

					"name=\"org.lgna.story.resources.prop.PirateShipResource",
					"name=\"org.lgna.story.resources.prop.PirateShipPropResource",

					"name=\"org.lgna.story.resources.prop.FishingBoatResource",
					"name=\"org.lgna.story.resources.prop.FishingBoatPropResource",

					"name=\"org.lgna.story.resources.prop.SubmarineResource",
					"name=\"org.lgna.story.resources.prop.SubmarinePropResource",

					//</note>

					"org.lgna.story.event.ComesIntoViewEvent",
					"org.lgna.story.event.EnterViewEvent",

					"org.lgna.story.event.LeavesViewEvent",
					"org.lgna.story.event.ExitViewEvent",

					"getForegroundMovable",
					"getForegroundModel",

					"getBackgroundMovable",
					"getBackgroundModel",

					"edu.cmu.cs.dennisc.matt.EndOcclusionEvent",
					"org.lgna.story.event.EndOcclusionEvent"

			),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.85.0.0" ),
					new org.lgna.project.Version( "3.1.92.0.0" )
			),

			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.92.0.0" ),
					new org.lgna.project.Version( "3.1.93.0.0" ),

					createMoreSpecificFieldPattern( "WALNUT_DOOR_WALNUT_WALNUT", "org.lgna.story.resources.prop.BiotechStationResource" ),
					createMoreSpecificFieldReplacement( "BIOTECH_STATION", "org.lgna.story.resources.prop.BiotechStationResource" ),

					createMoreSpecificFieldPattern( "WALNUT_DOOR_WALNUT_LIGHT_WOOD", "org.lgna.story.resources.prop.BiotechStationResource" ),
					createMoreSpecificFieldReplacement( "BIOTECH_STATION", "org.lgna.story.resources.prop.BiotechStationResource" ),

					createMoreSpecificFieldPattern( "WALNUT_DOOR_WALNUT_ORANGE", "org.lgna.story.resources.prop.BiotechStationResource" ),
					createMoreSpecificFieldReplacement( "BIOTECH_STATION", "org.lgna.story.resources.prop.BiotechStationResource" ),

					createMoreSpecificFieldPattern( "WALNUT_DOOR_WALNUT_BLUE", "org.lgna.story.resources.prop.BiotechStationResource" ),
					createMoreSpecificFieldReplacement( "BIOTECH_STATION", "org.lgna.story.resources.prop.BiotechStationResource" ),

					createMoreSpecificFieldPattern( "WALNUT_DOOR_WALNUT_PINK", "org.lgna.story.resources.prop.BiotechStationResource" ),
					createMoreSpecificFieldReplacement( "BIOTECH_STATION", "org.lgna.story.resources.prop.BiotechStationResource" ),

					createMoreSpecificFieldPattern( "BASIC", "org.lgna.story.resources.prop.BiotechStationResource" ),
					createMoreSpecificFieldReplacement( "BIOTECH_STATION", "org.lgna.story.resources.prop.BiotechStationResource" ),

					createMoreSpecificFieldPattern( "FANCY", "org.lgna.story.resources.prop.BiotechStationResource" ),
					createMoreSpecificFieldReplacement( "BIOTECH_STATION", "org.lgna.story.resources.prop.BiotechStationResource" ),

					//The textbook version needs to not remove these models, so for this branch leave this commented out.
					//					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.biped.BlackCatResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.PandaResource" ),
					//
					//					"name=\"org.lgna.story.resources.biped.BlackCatResource",
					//					"name=\"org.lgna.story.resources.biped.AliceResource",
					//
					//					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.biped.PumpkinHeadResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.PandaResource" ),
					//
					//					createMoreSpecificFieldPattern( "HEADLESS", "org.lgna.story.resources.biped.PumpkinHeadResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.PandaResource" ),
					//
					//					"name=\"org.lgna.story.resources.biped.PumpkinHeadResource",
					//					"name=\"org.lgna.story.resources.biped.PandaResource",
					//

					//note: this is a doomed migration
					////"name=\"org.lgna.story.resources.prop.TrainEngineResource",
					////"name=\"org.lgna.story.resources.train.TrainEngineResource",
					//
					//					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.biped.GhostResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.ThorResource" ),
					//
					//					createMoreSpecificFieldPattern( "SHEET_GHOST", "org.lgna.story.resources.biped.GhostResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.ThorResource" ),
					//
					//					createMoreSpecificFieldPattern( "SHEET_GHOST_SHEET_TRANSPARENT", "org.lgna.story.resources.biped.GhostResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.ThorResource" ),
					//
					//					"name=\"org.lgna.story.resources.biped.GhostResource",
					//					"name=\"org.lgna.story.resources.biped.ThorResource",
					//

					//note: this is a doomed migration
					////"name=\"org.lgna.story.resources.prop.TrainCarResource",
					////"name=\"org.lgna.story.resources.train.TrainCarResource",

					//
					//					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.prop.TunnelResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TentResource" ),
					//
					//					"name=\"org.lgna.story.resources.prop.TunnelResource",
					//					"name=\"org.lgna.story.resources.prop.TentResource",
					//
					//					createMoreSpecificFieldPattern( "WITH_HAT", "org.lgna.story.resources.biped.SkeletonResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.YetiResource" ),
					//
					//					createMoreSpecificFieldPattern( "DEFAULT_SKELETON", "org.lgna.story.resources.biped.SkeletonResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.YetiResource" ),
					//
					//					createMoreSpecificFieldPattern( "DEFAULT_TOP_HAT", "org.lgna.story.resources.biped.SkeletonResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.YetiResource" ),
					//
					//					"name=\"org.lgna.story.resources.biped.SkeletonResource",
					//					"name=\"org.lgna.story.resources.biped.YetiResource",
					//
					//					createMoreSpecificFieldPattern( "DIFFUSE", "org.lgna.story.resources.prop.FirTreeTrunkResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TreeTrunkResource" ),
					//
					//					createMoreSpecificFieldPattern( "SKELETON", "org.lgna.story.resources.prop.FirTreeTrunkResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TreeTrunkResource" ),
					//
					//					createMoreSpecificFieldPattern( "TOP_HAT", "org.lgna.story.resources.prop.FirTreeTrunkResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.TreeTrunkResource" ),
					//
					//					"name=\"org.lgna.story.resources.prop.FirTreeTrunkResource",
					//					"name=\"org.lgna.story.resources.prop.TreeTrunkResource",
					//
					//					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.biped.BatResource" ),
					//					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.YetiResource" ),
					//
					//					"name=\"org.lgna.story.resources.biped.BatResource",
					//					"name=\"org.lgna.story.resources.biped.YetiResource",

					createMoreSpecificFieldPattern( "DEFAULT", "org.lgna.story.resources.biped.AsuraResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.biped.AlienResource" ),

					"name=\"org.lgna.story.resources.biped.AsuraResource",
					"name=\"org.lgna.story.resources.biped.AlienResource",

					createMoreSpecificFieldPattern( "CHEAP", "org.lgna.story.resources.prop.TelevisionRemoteResource" ),
					createMoreSpecificFieldReplacement( "TELEVISION_REMOTE", "org.lgna.story.resources.prop.TelevisionRemoteResource" ),

					createJointIdPattern( "TAIL", "biped.BigBadWolfResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "biped.BigBadWolfResource" ),
					createJointIdReplacement( "TAIL_1" ),
					createJointIdPattern( "TAIL_3", "biped.BigBadWolfResource" ),
					createJointIdReplacement( "TAIL_2" ),
					createJointIdPattern( "TAIL_4", "biped.BigBadWolfResource" ),
					createJointIdReplacement( "TAIL_3" ),

					createJointIdPattern( "TAIL", "biped.BunnyResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "biped.BunnyResource" ),
					createJointIdReplacement( "TAIL_1" ),
					createJointIdPattern( "TAIL_3", "biped.BunnyResource" ),
					createJointIdReplacement( "TAIL_2" ),

					createJointIdPattern( "TAIL", "biped.CheshireCatResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "biped.CheshireCatResource" ),
					createJointIdReplacement( "TAIL_1" ),
					createJointIdPattern( "TAIL_3", "biped.CheshireCatResource" ),
					createJointIdReplacement( "TAIL_2" ),
					createJointIdPattern( "TAIL_4", "biped.CheshireCatResource" ),
					createJointIdReplacement( "TAIL_3" ),
					createJointIdPattern( "TAIL_5", "biped.CheshireCatResource" ),
					createJointIdReplacement( "TAIL_4" ),

					createJointIdPattern( "TAIL", "biped.GoldenMonkeyResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "biped.GoldenMonkeyResource" ),
					createJointIdReplacement( "TAIL_1" ),
					createJointIdPattern( "TAIL_3", "biped.GoldenMonkeyResource" ),
					createJointIdReplacement( "TAIL_2" ),
					createJointIdPattern( "JOINT_4", "biped.GoldenMonkeyResource" ),
					createJointIdReplacement( "TAIL_3" ),

					createJointIdPattern( "TAIL", "biped.HareResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "biped.HareResource" ),
					createJointIdReplacement( "TAIL_1" ),

					createJointIdPattern( "TAIL", "biped.MandrilResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "biped.MandrilResource" ),
					createJointIdReplacement( "TAIL_1" ),

					createJointIdPattern( "TAIL", "biped.MonkeyKingResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "biped.MonkeyKingResource" ),
					createJointIdReplacement( "TAIL_1" ),
					createJointIdPattern( "TAIL_3", "biped.MonkeyKingResource" ),
					createJointIdReplacement( "TAIL_2" ),
					createJointIdPattern( "TAIL_4", "biped.MonkeyKingResource" ),
					createJointIdReplacement( "TAIL_3" ),

					createJointIdPattern( "JAW_1", "biped.PandaResource" ),
					createJointIdReplacement( "MOUTH_TIP" ),

					createJointIdPattern( "TAIL", "biped.StuffedTigerResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "biped.StuffedTigerResource" ),
					createJointIdReplacement( "TAIL_1" ),
					createJointIdPattern( "TAIL_3", "biped.StuffedTigerResource" ),
					createJointIdReplacement( "TAIL_2" ),
					createJointIdPattern( "TAIL_4", "biped.StuffedTigerResource" ),
					createJointIdReplacement( "TAIL_3" ),
					createJointIdPattern( "TAIL_5", "biped.StuffedTigerResource" ),
					createJointIdReplacement( "TAIL_4" ),

					createJointIdPattern( "NECK", "FlyerResource" ),
					createJointIdReplacement( "NECK_0" ),
					createJointIdPattern( "NECK_2", "FlyerResource" ),
					createJointIdReplacement( "NECK_1" ),

					createJointIdPattern( "TAIL", "FlyerResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "FlyerResource" ),
					createJointIdReplacement( "TAIL_1" ),
					createJointIdPattern( "TAIL_3", "FlyerResource" ),
					createJointIdReplacement( "TAIL_2" ),

					createJointIdPattern( "LEFT_PLUMAGE_1", "flyer.PeacockResource" ),
					createJointIdReplacement( "PLUMAGE_LEFT_TIP" ),

					createJointIdPattern( "RIGHT_PLUMAGE_1", "flyer.PeacockResource" ),
					createJointIdReplacement( "PLUMAGE_RIGHT_TIP" ),

					createJointIdPattern( "TAIL", "QuadrupedResource" ),
					createJointIdReplacement( "TAIL_0" ),
					createJointIdPattern( "TAIL_2", "QuadrupedResource" ),
					createJointIdReplacement( "TAIL_1" ),
					createJointIdPattern( "TAIL_3", "QuadrupedResource" ),
					createJointIdReplacement( "TAIL_2" ),
					createJointIdPattern( "TAIL_4", "QuadrupedResource" ),
					createJointIdReplacement( "TAIL_3" ),

					createJointIdPattern( "JAW_1", "quadruped.AbyssinianCatResource" ),
					createJointIdReplacement( "LOWER_LIP" ),
					createJointIdPattern( "JAW_1", "quadruped.AlienRobotResource" ),
					createJointIdReplacement( "MOUTH_TIP" ),
					createJointIdPattern( "JAW_1", "quadruped.CaimanResource" ),
					createJointIdReplacement( "MOUTH_TIP" ),
					createJointIdPattern( "JAW_1", "quadruped.CowResource" ),
					createJointIdReplacement( "MOUTH_TIP" ),
					createJointIdPattern( "JAW_1", "quadruped.DragonResource" ),
					createJointIdReplacement( "MOUTH_TIP" ),
					createJointIdPattern( "JAW_1", "quadruped.DragonBabyResource" ),
					createJointIdReplacement( "MOUTH_TIP" ),
					createJointIdPattern( "JAW_1", "quadruped.PeccaryResource" ),
					createJointIdReplacement( "MOUTH_TIP" ),
					createJointIdPattern( "JAW_1", "quadruped.YaliResource" ),
					createJointIdReplacement( "MOUTH_TIP" ),

					createJointIdPattern( "TONGUE", "quadruped.CoyoteResource" ),
					createJointIdReplacement( "TONGUE_0" ),
					createJointIdPattern( "TONGUE_2", "quadruped.CoyoteResource" ),
					createJointIdReplacement( "TONGUE_1" ),
					createJointIdPattern( "TONGUE_3", "quadruped.CoyoteResource" ),
					createJointIdReplacement( "TONGUE_2" ),
					createJointIdPattern( "TONGUE_4", "quadruped.CoyoteResource" ),
					createJointIdReplacement( "TONGUE_3" ),

					createJointIdPattern( "TRUNK_1", "quadruped.ElephantResource" ),
					createJointIdReplacement( "TRUNK_0" ),
					createJointIdPattern( "TRUNK_2", "quadruped.ElephantResource" ),
					createJointIdReplacement( "TRUNK_1" ),
					createJointIdPattern( "TRUNK_3", "quadruped.ElephantResource" ),
					createJointIdReplacement( "TRUNK_2" ),
					createJointIdPattern( "TRUNK_4", "quadruped.ElephantResource" ),
					createJointIdReplacement( "TRUNK_3" ),
					createJointIdPattern( "TRUNK_5", "quadruped.ElephantResource" ),
					createJointIdReplacement( "TRUNK_4" ),
					createJointIdPattern( "TRUNK_6", "quadruped.ElephantResource" ),
					createJointIdReplacement( "TRUNK_5" ),

					createJointIdPattern( "TONGUE_1", "quadruped.HornedLizardResource" ),
					createJointIdReplacement( "TONGUE_0" ),
					createJointIdPattern( "TONGUE_2", "quadruped.HornedLizardResource" ),
					createJointIdReplacement( "TONGUE_1" ),
					createJointIdPattern( "TONGUE_3", "quadruped.HornedLizardResource" ),
					createJointIdReplacement( "TONGUE_2" ),
					createJointIdPattern( "TONGUE_4", "quadruped.HornedLizardResource" ),
					createJointIdReplacement( "TONGUE_3" ),

					createJointIdPattern( "TONGUE", "quadruped.YakResource" ),
					createJointIdReplacement( "TONGUE_0" ),
					createJointIdPattern( "TONGUE_2", "quadruped.YakResource" ),
					createJointIdReplacement( "TONGUE_1" ),
					createJointIdPattern( "TONGUE_3", "quadruped.YakResource" ),
					createJointIdReplacement( "TONGUE_2" ),
					createJointIdPattern( "TONGUE_4", "quadruped.YakResource" ),
					createJointIdReplacement( "TONGUE_3" ),

					createJointIdPattern( "TRUNK", "quadruped.YaliResource" ),
					createJointIdReplacement( "TRUNK_0" ),
					createJointIdPattern( "TRUNK_2", "quadruped.YaliResource" ),
					createJointIdReplacement( "TRUNK_1" ),
					createJointIdPattern( "TRUNK_3", "quadruped.YaliResource" ),
					createJointIdReplacement( "TRUNK_2" ),
					createJointIdPattern( "TRUNK_4", "quadruped.YaliResource" ),
					createJointIdReplacement( "TRUNK_3" ),
					createJointIdPattern( "TRUNK_5", "quadruped.YaliResource" ),
					createJointIdReplacement( "TRUNK_4" ),
					createJointIdPattern( "TRUNK_6", "quadruped.YaliResource" ),
					createJointIdReplacement( "TRUNK_5" ),

					createJointIdPattern( "LEFT_1", "prop.NavajoBlanketResource" ),
					createJointIdReplacement( "LEFT_0" ),
					createJointIdPattern( "LEFT_2", "prop.NavajoBlanketResource" ),
					createJointIdReplacement( "LEFT_1" ),
					createJointIdPattern( "LEFT_3", "prop.NavajoBlanketResource" ),
					createJointIdReplacement( "LEFT_2" ),
					createJointIdPattern( "RIGHT_1", "prop.NavajoBlanketResource" ),
					createJointIdReplacement( "RIGHT_0" ),
					createJointIdPattern( "RIGHT_2", "prop.NavajoBlanketResource" ),
					createJointIdReplacement( "RIGHT_1" ),
					createJointIdPattern( "RIGHT_3", "prop.NavajoBlanketResource" ),
					createJointIdReplacement( "RIGHT_2" ),

					//createJointIdPattern( "STRING_1", "prop.PrayerFlagsResource" ),
					//createJointIdReplacement( "STRING_1" ),
					createJointIdPattern( "FLAG_5", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_05" ),
					createJointIdPattern( "FLAG_6", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_06" ),
					createJointIdPattern( "FLAG_7", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_07" ),
					createJointIdPattern( "FLAG_8", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_08" ),
					createJointIdPattern( "FLAG_9", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_09" ),

					createJointIdPattern( "STRING_2", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "STRING_0" ),
					createJointIdPattern( "FLAG", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_00" ),
					createJointIdPattern( "FLAG_1", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_01" ),
					createJointIdPattern( "FLAG_2", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_02" ),
					createJointIdPattern( "FLAG_3", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_03" ),
					createJointIdPattern( "FLAG_4", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_04" ),

					createJointIdPattern( "STRING_3", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "STRING_2" ),
					createJointIdPattern( "FLAG_10", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_10" ),
					createJointIdPattern( "FLAG_11", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_11" ),
					createJointIdPattern( "FLAG_12", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_12" ),
					createJointIdPattern( "FLAG_13", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_13" ),
					createJointIdPattern( "FLAG_14", "prop.PrayerFlagsResource" ),
					createJointIdReplacement( "FLAG_14" ),

					createJointIdPattern( "TAIL_5", "quadruped.YaliResource" ),
					createJointIdReplacement( "TAIL_4" ),

					createJointIdPattern( "TAIL_5", "quadruped.DalmatianResource" ),
					createJointIdReplacement( "TAIL_4" )
			),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.1.93.0.0" ),
					new org.lgna.project.Version( "3.2.108.0.0" )
					),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.2.108.0.0" ),
					new org.lgna.project.Version( "3.2.110.0.0" ),

					createMoreSpecificFieldPattern( "OVAL", "org.lgna.story.resources.prop.SandDunesResource" ),
					createMoreSpecificFieldReplacement( "OVAL_DESERT", "org.lgna.story.resources.prop.SandDunesResource" ),

					createMoreSpecificFieldPattern( "CRESCENT", "org.lgna.story.resources.prop.SandDunesResource" ),
					createMoreSpecificFieldReplacement( "CRESCENT_DESERT", "org.lgna.story.resources.prop.SandDunesResource" ),

					createMoreSpecificFieldPattern( "BLOB", "org.lgna.story.resources.prop.SandDunesResource" ),
					createMoreSpecificFieldReplacement( "BLOB_DESERT", "org.lgna.story.resources.prop.SandDunesResource" )
					),
			new org.lgna.project.migration.TextMigration(
					new org.lgna.project.Version( "3.2.110.0.0" ),
					new org.lgna.project.Version( "3.2.111.0.0" ),

					createMoreSpecificFieldPattern( "BLEACHERS", "org.lgna.story.resources.prop.CircusBleachersResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT_BLEACHERS", "org.lgna.story.resources.prop.CircusBleachersResource" ),

					createMoreSpecificFieldPattern( "BONE_PILE", "org.lgna.story.resources.prop.BonesResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.BonePileResource" ),

					"org.lgna.story.resources.prop.BonesResource",
					"org.lgna.story.resources.prop.BonePileResource",

					createMoreSpecificFieldPattern( "SHORT", "org.lgna.story.resources.prop.WychElmResource" ),
					createMoreSpecificFieldReplacement( "DEFAULT", "org.lgna.story.resources.prop.WychElmResource" )
					)

			//			, EventAstMigration.getTextMigration()
	};

	private final AstMigration[] astMigrations = {
			new org.lgna.project.migration.ast.NoOpAstMigrationStandIn(
					new org.lgna.project.Version( "3.1.0.0.0" ),
					new org.lgna.project.Version( "3.1.38.0.0" )
			),
			new org.lgna.project.migration.ast.MouseClickAstMigration(
					new org.lgna.project.Version( "3.1.38.0.0" ),
					new org.lgna.project.Version( "3.1.39.0.0" )
			),
			new org.lgna.project.migration.ast.UnderscoreFieldAccessAstMigration(
					new org.lgna.project.Version( "3.1.39.0.0" ),
					new org.lgna.project.Version( "3.1.68.0.0" )
			),
			new org.lgna.project.migration.ast.EventAstMigration(
					new org.lgna.project.Version( "3.1.68.0.0" ),
					new org.lgna.project.Version( "3.1.70.0.0" )
			),
			new org.lgna.project.migration.ast.RemoveGetMySceneMethodFromProgramTypeAstMigration(
					new org.lgna.project.Version( "3.1.70.0.0" ),
					new org.lgna.project.Version( "3.1.72.0.0" )
			)
	};

	// @formatter:on

	private static class SingletonHolder {
		private static ProjectMigrationManager instance = new ProjectMigrationManager();
	}

	public static ProjectMigrationManager getInstance() {
		return SingletonHolder.instance;
	}

	private ProjectMigrationManager() {
		super( org.lgna.project.ProjectVersion.getCurrentVersion() );
	}

	@Override
	protected org.lgna.project.migration.TextMigration[] getTextMigrations() {
		return this.textMigrations;
	}

	@Override
	protected org.lgna.project.migration.AstMigration[] getAstMigrations() {
		return this.astMigrations;
	}
}
