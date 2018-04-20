package org.alice.tweedle.unlinked;

import org.alice.tweedle.TweedleClass;
import org.alice.tweedle.TweedleEnum;
import org.alice.tweedle.TweedleMethod;
import org.alice.tweedle.TweedleNull;
import org.alice.tweedle.TweedleStatement;
import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.ast.AdditionExpression;
import org.alice.tweedle.ast.ReturnStatement;
import org.junit.Test;

import static org.junit.Assert.*;

public class TweedleParseTest {

	private TweedleType parseType( String source) {
		return new TweedleUnlinkedParser().parseType( source );
	}

	@Test
	public void somethingShouldBeCreatedForARootClass() {
		TweedleType tested = parseType( "class SThing {}" );

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aRootClassShouldBeCreated() {
		TweedleType tested = parseType( "class SThing {}" );

		assertTrue("The parser should have returned a TweedleClass.", tested instanceof TweedleClass );
	}

	@Test
	public void classShouldKnowItsName() {
		TweedleType tested = parseType( "class SThing {}" );

		assertEquals( "The name should be 'SThing'", "SThing", tested.getName() );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfBooleanPrimitiveShouldFail() {
		parseType( "class SScene extends Boolean {}" );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfDecimalPrimitiveShouldFail() {
		parseType( "class SScene extends DecimalNumber {}" );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfWholePrimitiveShouldFail() {
		parseType( "class SScene extends WholeNumber {}" );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfNumberPrimitiveShouldFail() {
		parseType( "class SScene extends Number {}" );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfTextStringPrimitiveShouldFail() {
		parseType( "class SScene extends TextString {}" );
	}

	@Test
	public void subclassOfStringShouldNotFail() {
		TweedleType tested = parseType( "class SScene extends String {}" );

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void enumNamedSameAsBooleanPrimitiveShouldCreateSomething() {
		TweedleType tested = parseType( "enum Boolean {TRUE, FALSE}" );

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void classNamedSameAsBooleanPrimitiveShouldCreateSomething() {
		TweedleType tested = parseType( "class Boolean {}" );

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void somethingShouldBeCreatedForASubclass() {
		TweedleType tested = parseType( "class SScene extends SThing {}" );

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aSubclassShouldBeCreated() {
		TweedleType tested = parseType( "class SScene extends SThing {}" );

		assertTrue("The parser should have returned a TweedleClass.", tested instanceof TweedleClass);
	}

	@Test
	public void classNameShouldBeReturnedOnSubclass() {
		TweedleType tested = parseType( "class SScene extends SThing {}" );

		assertEquals("The class name should have been SScene.",
								 "SScene",
									tested.getName() );
	}

	@Test
	public void superclassNameShouldBeReturnedOnSubclass() {
		TweedleClass sScene = (TweedleClass) parseType( "class SScene extends SThing {}" );

		assertEquals("The class SScene should have a superclass name SThing.",
						     "SThing",
						      sScene.getSuperclassName() );
	}

	@Test
	public void somethingShouldBeCreatedForAnEnum() {
		TweedleType tested = parseType( "enum Direction {UP, DOWN}" );

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void anEnumShouldBeCreated() {
		TweedleType tested = parseType( "enum Direction {UP, DOWN}" );

		assertTrue("The parser should have returned an TweedleEnum.", tested instanceof TweedleEnum );
	}

	@Test
	public void nameShouldBeReturnedOnEnum() {
		TweedleType tested = parseType( "enum Direction {UP, DOWN}" );

		assertEquals("The enum name should have been Direction.",
						"Direction",
						tested.getName() );
	}

	@Test
	public void enumShouldIncludeTwoValues() {
		TweedleEnum directionEnum = (TweedleEnum) parseType( "enum Direction {UP, DOWN}" );

		assertEquals("The enum Direction should have two values.",
						2,
						directionEnum.getValues().size() );
	}

	@Test
	public void enumShouldIncludeUpValue() {
		TweedleEnum directionEnum = (TweedleEnum) parseType( "enum Direction {UP, DOWN}" );

		assertTrue("The enum Direction should include UP.",
						directionEnum.getValues().contains( "UP" ) );
	}

	@Test
	public void enumShouldIncludeDownValue() {
		TweedleEnum directionEnum = (TweedleEnum) parseType( "enum Direction {UP, DOWN}" );

		assertTrue("The enum Direction should include DOWN.",
						directionEnum.getValues().contains( "DOWN" ) );
	}

	@Test
	public void enumShouldNotIncludeLeftValue() {
		TweedleEnum directionEnum = (TweedleEnum) parseType( "enum Direction {UP, DOWN}" );

		assertFalse("The enum Direction should not include LEFT.",
						directionEnum.getValues().contains( "LEFT" ) );
	}

	@Test
	public void somethingShouldBeCreatedForClassWithConstuctor() {
		String scene = "class Scene extends SScene models Scene {\n"
						+ "  Scene() {\n"
						+ "    super();\n"
						+ "  }\n"
						+ "}";
		TweedleType tested = parseType( scene);

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void classWithMethodShouldHaveMethod() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);

		assertFalse("The class should have a method.", tested.methods.isEmpty() );
	}

	@Test
	public void classMethodShouldHaveReturnType() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );

		assertEquals( "The method should return a WholeNumber.", TweedleTypes.WHOLE_NUMBER, sumThing.getType() );
	}

	@Test
	public void classMethodShouldBeNamed() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );

		assertEquals( "The method should be named.", "sumThing", sumThing.getName() );
	}

	@Test
	public void classMethodShouldHaveNoRequiredParams() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );

		assertTrue( "The method should have no params.", sumThing.getRequiredParameters().isEmpty() );
	}

	@Test
	public void classMethodShouldHaveNoOptionalParams() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );

		assertTrue( "The method should have no params.", sumThing.getOptionalParameters().isEmpty() );
	}

	@Test
	public void classMethodShouldHaveAStatement() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );

		assertEquals( "The method should have one statement.", 1, sumThing.getBody().size() );
	}

	@Test
	public void classMethodWithEmtpyReturnShouldHaveANonNullStatement() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );
		TweedleStatement stmt = sumThing.getBody().get( 0 );

		assertNotNull( "The method statement should not be null.", stmt );
	}

	@Test
	public void classMethodWithEmtpyReturnShouldHaveReturnStatement() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );
		TweedleStatement stmt = sumThing.getBody().get( 0 );

		assertTrue( "The method statement should be a return.", stmt instanceof ReturnStatement );
	}

	@Test
	public void classMethodWithEmtpyReturnReturnStatementShouldHaveAnExpression() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );
		ReturnStatement stmt = (ReturnStatement) sumThing.getBody().get( 0 );

		assertNotNull( "The return statement should hold an expression.", stmt.getExpression() );
	}

	@Test
	public void classMethodWithEmtpyReturnReturnStatementShouldHaveTweedleNull() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );
		ReturnStatement stmt = (ReturnStatement) sumThing.getBody().get( 0 );

		assertEquals( "The return statement should hold NULL.", TweedleNull.NULL, stmt.getExpression() );
	}

	@Test
	public void classMethodShouldHaveANonNullStatement() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );
		TweedleStatement stmt = sumThing.getBody().get( 0 );

		assertNotNull( "The method statement should not be null.", stmt );
	}

	@Test
	public void classMethodShouldHaveReturnStatement() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );
		TweedleStatement stmt = sumThing.getBody().get( 0 );

		assertTrue( "The method statement should be a return.", stmt instanceof ReturnStatement );
	}

	@Test
	public void classMethodReturnStatementShouldHaveAnExpression() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );
		ReturnStatement stmt = (ReturnStatement) sumThing.getBody().get( 0 );

		assertNotNull( "The method statement should hold an expression.", stmt.getExpression() );
	}

	@Test
	public void classMethodReturnStatementShouldHaveAnAdditionExpression() {
		String scene = "class Scene extends SScene {\n"
						+ "  WholeNumber sumThing() {\n"
						+ "    return 3 + 4;\n"
						+ "  }\n"
						+ "}";
		TweedleClass tested = (TweedleClass) parseType( scene);
		TweedleMethod sumThing = tested.methods.get( 0 );
		ReturnStatement stmt = (ReturnStatement) sumThing.getBody().get( 0 );

		assertTrue( "The method statement should hold an addition expression.", stmt.getExpression() instanceof AdditionExpression );
	}

	@Test
	public void somethingShouldBeCreatedForClassWithListener() {
		String scene = "class Scene extends SScene {\n"
						+ "  Scene() {\n"
						+ "    super();\n"
						+ "  }\n"
						+ "  void initializeEventListeners() {\n"
						+ "    this.addSceneActivationListener(listener: (SceneActivationEvent event) -> {\n"
						+ "      this.myFirstMethod();\n"
						+ "    });\n"
						+ "  }\n"
						+ "}";
		TweedleType tested = parseType( scene);

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void somethingShouldBeCreatedForGeneratedScene() {
		String generatedScene = "class Scene extends SScene models Scene {\n" + "  Scene() {\n" + "    super();\n" + "  }\n"
						+ "\n" + "  void initializeEventListeners() {\n"
						+ "    this.addSceneActivationListener(listener: (SceneActivationEvent event)-> {\n"
						+ "      this.myFirstMethod();\n" + "    });\n" + "  }\n" + "\n" + "  void myFirstMethod() {\n"
						+ "    this.sphere.jump();\n" + "    this.sphere.jump();\n" + "    doTogether {\n"
						+ "      this.walrus.moveToward(target: this.sphere,amount: 2.0);\n"
						+ "      this.walrus.moveToward(target: this.cylinder,amount: 2.0);\n" + "    }\n" + "    doTogether {\n"
						+ "      this.sphere.setPaint(paint: Color.GREEN);\n" + "      this.sphere.setPaint(paint: Color.RED);\n"
						+ "    }\n" + "    this.walrus.say(text: \"hello \\\"Ralph\\\" How are you? \\\\\\\"/\\\" today?\");\n"
						+ "    doTogether {\n" + "      this.walrus.turn(direction: TurnDirection.LEFT,amount: 1.0);\n" + "    }\n"
						+ "*<  this.walrus.roll(direction: RollDirection.RIGHT,amount: 1.0); >*\n"
						+ "    this.walrus.turn(direction: TurnDirection.LEFT,amount: 1.0);\n" + "    doInOrder {\n"
						+ "      doInOrder {\n" + "      }\n" + "      // So much to say\n"
						+ "      // And I can use multiple lines\n"
						+ "      // Nicer if the other side updated as I typed, but what can you do?\n" + "*<    doInOrder {\n"
						+ "*<      this.walrus.turnToFace(target: this.cylinder,details: TurnToFace.duration(unknown: 2.0)); >*\n"
						+ "        this.walrus.turnToFace(target: this.sphere,details: TurnToFace.duration(unknown: 2.0));\n"
						+ "      } >*\n" + "    }\n" + "    doInOrder {\n" + "      doTogether {\n"
						+ "        forEach(SModel x in new SModel[]{this.sphere, this.walrus}) {\n" + "          doTogether {\n"
						+ "          }\n" + "        }\n" + "      }\n" + "      SModel[] muddles <- new SModel[]{};\n"
						+ "      doInOrder {\n" + "        doTogether {\n"
						+ "          this.walrus.turnToFace(target: this.cylinder,details: TurnToFace.duration(unknown: 2.0));\n"
						+ "          this.walrus.turnToFace(target: this.sphere,details: TurnToFace.duration(unknown: 2.0));\n"
						+ "        }\n" + "      }\n" + "    }\n" + "*<  countUpTo( indexA < 2 ) {\n" + "    } >*\n"
						+ "    countUpTo( indexB < 2 ) {\n" + "    }\n" + "*<  while (false) {\n" + "    } >*\n"
						+ "    while (false) {\n" + "    }\n"
						+ "*<  forEach(SModel x in new SModel[]{this.sphere, this.walrus}) {\n" + "      doTogether {\n"
						+ "      }\n" + "    } >*\n" + "    forEach(SModel x in new SModel[]{this.sphere, this.walrus}) {\n"
						+ "      doTogether {\n" + "      }\n" + "    }\n" + "*<  if(true) {\n" + "    } else {\n" + "    } >*\n"
						+ "    if(true) {\n" + "    } else {\n" + "    }\n" + "*<  doTogether {\n" + "    } >*\n"
						+ "    doTogether {\n" + "    }\n"
						+ "*<  eachTogether(TextString msg in new TextString[]{\"hello\", \"hello\"}) {\n"
						+ "      this.walrus.say(text: msg);\n" + "    } >*\n"
						+ "    eachTogether(TextString msg in new TextString[]{\"hello\", \"hello\"}) {\n"
						+ "      this.walrus.say(text: msg);\n" + "    }\n" + "*<  WholeNumber a <- 2; >*\n" + "    WholeNumber a <- 2;\n"
						+ "*<  a <- 2; >*\n" + "    a <- 2;\n" + "  }\n" + "\n" + "  void doInfix() {\n"
						+ "    WholeNumber v <- 1+2+(2-1)*3;\n" + "    if((true||false)&&false) {\n" + "    } else {\n" + "    }\n"
						+ "    if(false&&false||0.5<=1.0) {\n" + "    } else {\n" + "    }\n"
						+ "    if((false||false)&&(true||true)) {\n" + "    } else {\n" + "    }\n"
						+ "    if(false&&false||true&&true) {\n" + "    } else {\n" + "    }\n" + "  }\n"
						+ "  SGround ground <- new SGround();\n" + "  SCamera camera <- new SCamera();\n"
						+ "  Walrus walrus <- new Walrus();\n" + "  Sphere sphere <- new Sphere();\n"
						+ "  Cylinder cylinder <- new Cylinder();\n" + "\n" + "  void performCustomSetup() {\n"
						+ "    // Make adjustments to the starting scene, in a way not available in the Scene editor\n" + "  }\n"
						+ "\n" + "  void performGeneratedSetUp() {\n" + "    // DO NOT EDIT\n"
						+ "    // This code is automatically generated.  Any work you perform in this method will be overwritten.\n"
						+ "    // DO NOT EDIT\n"
						+ "    this.setAtmosphereColor(color: new Color(red: 0.588,green: 0.886,blue: 0.988));\n"
						+ "    this.setFromAboveLightColor(color: Color.WHITE);\n"
						+ "    this.setFromBelowLightColor(color: Color.BLACK);\n" + "    this.setFogDensity(density: 0.0);\n"
						+ "    this.setName(name: \"myScene\");\n" + "    this.ground.setPaint(paint: SurfaceAppearance.GRASS);\n"
						+ "    this.ground.setOpacity(opacity: 1.0);\n" + "    this.ground.setName(name: \"ground\");\n"
						+ "    this.ground.setVehicle(vehicle: this);\n" + "    this.camera.setName(name: \"camera\");\n"
						+ "    this.camera.setVehicle(vehicle: this);\n"
						+ "    this.camera.setOrientationRelativeToVehicle(orientation: new Orientation(x: 0.0,y: 0.995185,z: 0.0980144,w: 6.12323E-17));\n"
						+ "    this.camera.setPositionRelativeToVehicle(position: new Position(right: 9.61E-16,up: 1.56,backward: -7.85));\n"
						+ "    this.walrus.setPaint(paint: Color.WHITE);\n" + "    this.walrus.setOpacity(opacity: 1.0);\n"
						+ "    this.walrus.setName(name: \"walrus\");\n" + "    this.walrus.setVehicle(vehicle: this);\n"
						+ "    this.walrus.setOrientationRelativeToVehicle(orientation: new Orientation(x: 0.0,y: 0.0,z: 0.0,w: 1.0));\n"
						+ "    this.walrus.setPositionRelativeToVehicle(position: new Position(right: 0.618,up: 0.0111,backward: -0.877));\n"
						+ "    this.sphere.setRadius(radius: 0.5);\n" + "    this.sphere.setPaint(paint: Color.WHITE);\n"
						+ "    this.sphere.setOpacity(opacity: 1.0);\n" + "    this.sphere.setName(name: \"sphere\");\n"
						+ "    this.sphere.setVehicle(vehicle: this);\n"
						+ "    this.sphere.setOrientationRelativeToVehicle(orientation: new Orientation(x: 0.0,y: 0.0,z: 0.0,w: 1.0));\n"
						+ "    this.sphere.setPositionRelativeToVehicle(position: new Position(right: -7.34,up: 0.5,backward: 18.9));\n"
						+ "    this.cylinder.setRadius(radius: 0.5);\n" + "    this.cylinder.setLength(length: 1.0);\n"
						+ "    this.cylinder.setPaint(paint: Color.WHITE);\n" + "    this.cylinder.setOpacity(opacity: 1.0);\n"
						+ "    this.cylinder.setName(name: \"cylinder\");\n" + "    this.cylinder.setVehicle(vehicle: this);\n"
						+ "    this.cylinder.setOrientationRelativeToVehicle(orientation: new Orientation(x: 0.0,y: 0.0,z: 0.0,w: 1.0));\n"
						+ "    this.cylinder.setPositionRelativeToVehicle(position: new Position(right: 7.63,up: 0.0,backward: 19.7));\n"
						+ "  }\n" + "\n" + "  void handleActiveChanged(Boolean isActive,WholeNumber activationCount) {\n"
						+ "    if(isActive) {\n" + "      if(activationCount==1) {\n" + "        this.performGeneratedSetUp();\n"
						+ "        this.performCustomSetup();\n" + "        this.initializeEventListeners();\n" + "      } else {\n"
						+ "        this.restoreStateAndEventListeners();\n" + "      }\n" + "    } else {\n"
						+ "      this.preserveStateAndEventListeners();\n" + "    }\n" + "  }\n" + "  SGround getGround() {\n"
						+ "    return this.ground;\n" + "  }\n" + "  SCamera getCamera() {\n" + "    return this.camera;\n"
						+ "  }\n" + "  Walrus getWalrus() {\n" + "    return this.walrus;\n" + "  }\n" + "  Sphere getSphere() {\n"
						+ "    return this.sphere;\n" + "  }\n" + "  Cylinder getCylinder() {\n" + "    return this.cylinder;\n"
						+ "  }\n" + "}";
		TweedleType tested = parseType( generatedScene);

		assertNotNull("The parser should have returned something.", tested );
	}
}