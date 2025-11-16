package org.alice.tweedle.unlinked;

import org.alice.tweedle.*;
import org.alice.tweedle.ast.AdditionExpression;
import org.alice.tweedle.ast.ReturnStatement;
import org.junit.Test;

import static org.junit.Assert.*;

public class TweedleParseTest {

  private TweedleType parseType(String source) {
    return new TweedleUnlinkedParser().parseType(source);
  }

  @Test
  public void somethingShouldBeCreatedForARootClass() {
    TweedleType tested = parseType("class SThing {}");

    assertNotNull("The parser should have returned something.", tested);
  }

  @Test
  public void aRootClassShouldBeCreated() {
    TweedleType tested = parseType("class SThing {}");

    assertTrue("The parser should have returned a TweedleClass.", tested instanceof TweedleClass);
  }

  @Test
  public void classShouldKnowItsName() {
    TweedleType tested = parseType("class SThing {}");

    assertEquals("The name should be 'SThing'", "SThing", tested.getName());
  }

  @Test(expected = NullPointerException.class)
  public void subclassOfBooleanPrimitiveShouldFail() {
    parseType("class SScene extends Boolean {}");
  }

  @Test(expected = NullPointerException.class)
  public void subclassOfDecimalPrimitiveShouldFail() {
    parseType("class SScene extends DecimalNumber {}");
  }

  @Test(expected = NullPointerException.class)
  public void subclassOfWholePrimitiveShouldFail() {
    parseType("class SScene extends WholeNumber {}");
  }

  @Test(expected = NullPointerException.class)
  public void subclassOfNumberPrimitiveShouldFail() {
    parseType("class SScene extends Number {}");
  }

  @Test(expected = NullPointerException.class)
  public void subclassOfTextStringPrimitiveShouldFail() {
    parseType("class SScene extends TextString {}");
  }

  @Test
  public void subclassOfStringShouldNotFail() {
    TweedleType tested = parseType("class SScene extends String {}");

    assertNotNull("The parser should have returned something.", tested);
  }

  @Test
  public void enumNamedSameAsBooleanPrimitiveShouldCreateSomething() {
    TweedleType tested = parseType("enum Boolean {TRUE, FALSE}");

    assertNotNull("The parser should have returned something.", tested);
  }

  @Test
  public void classNamedSameAsBooleanPrimitiveShouldCreateSomething() {
    TweedleType tested = parseType("class Boolean {}");

    assertNotNull("The parser should have returned something.", tested);
  }

  @Test
  public void somethingShouldBeCreatedForASubclass() {
    TweedleType tested = parseType("class SScene extends SThing {}");

    assertNotNull("The parser should have returned something.", tested);
  }

  @Test
  public void aSubclassShouldBeCreated() {
    TweedleType tested = parseType("class SScene extends SThing {}");

    assertTrue("The parser should have returned a TweedleClass.", tested instanceof TweedleClass);
  }

  @Test
  public void classNameShouldBeReturnedOnSubclass() {
    TweedleType tested = parseType("class SScene extends SThing {}");

    assertEquals("The class name should have been SScene.", "SScene", tested.getName());
  }

  @Test
  public void superclassNameShouldBeReturnedOnSubclass() {
    TweedleClass sScene = (TweedleClass) parseType("class SScene extends SThing {}");

    assertEquals("The class SScene should have a superclass name SThing.", "SThing", sScene.getSuperclassName());
  }

  @Test
  public void somethingShouldBeCreatedForAnEnum() {
    TweedleType tested = parseType("enum Direction {UP, DOWN}");

    assertNotNull("The parser should have returned something.", tested);
  }

  @Test
  public void anEnumShouldBeCreated() {
    TweedleType tested = parseType("enum Direction {UP, DOWN}");

    assertTrue("The parser should have returned an TweedleEnum.", tested instanceof TweedleEnum);
  }

  @Test
  public void nameShouldBeReturnedOnEnum() {
    TweedleType tested = parseType("enum Direction {UP, DOWN}");

    assertEquals("The enum name should have been Direction.", "Direction", tested.getName());
  }

  @Test
  public void enumShouldIncludeTwoValues() {
    TweedleEnum directionEnum = (TweedleEnum) parseType("enum Direction {UP, DOWN}");

    assertEquals("The enum Direction should have two values.", 2, directionEnum.getValues().size());
  }

  @Test
  public void enumShouldIncludeUpValue() {
    TweedleEnum directionEnum = (TweedleEnum) parseType("enum Direction {UP, DOWN}");

    assertNotNull("The enum Direction should include UP.", directionEnum.getValue("UP"));
  }

  @Test
  public void enumShouldIncludeDownValue() {
    TweedleEnum directionEnum = (TweedleEnum) parseType("enum Direction {UP, DOWN}");

    assertNotNull("The enum Direction should include DOWN.", directionEnum.getValue("DOWN"));
  }

  @Test
  public void enumShouldNotIncludeLeftValue() {
    TweedleEnum directionEnum = (TweedleEnum) parseType("enum Direction {UP, DOWN}");

    assertNull("The enum Direction should not include LEFT.", directionEnum.getValue("LEFT"));
  }

  @Test
  public void somethingShouldBeCreatedForClassWithConstructor() {
    String scene = "class Scene extends SScene models Scene {\n" + "  Scene() {\n" + "    super();\n" + "  }\n" + "}";
    TweedleType tested = parseType(scene);

    assertNotNull("The parser should have returned something.", tested);
  }

  @Test
  public void classWithMethodShouldHaveMethod() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);

    assertFalse("The class should have a method.", tested.getMethods().isEmpty());
  }

  @Test
  public void classMethodShouldHaveReturnType() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);

    assertEquals("The method should return a WholeNumber.", TweedleTypes.WHOLE_NUMBER, sumThing.getType());
  }

  @Test
  public void classMethodShouldBeNamed() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);

    assertEquals("The method should be named.", "sumThing", sumThing.getName());
  }

  @Test
  public void classMethodShouldHaveNoRequiredParams() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);

    assertTrue("The method should have no params.", sumThing.getRequiredParameters().isEmpty());
  }

  @Test
  public void classMethodShouldHaveNoOptionalParams() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);

    assertTrue("The method should have no params.", sumThing.getOptionalParameters().isEmpty());
  }

  @Test
  public void classMethodShouldHaveAStatement() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);

    assertEquals("The method should have one statement.", 1, sumThing.getBody().size());
  }

  @Test
  public void classMethodWithEmptyReturnShouldHaveANonNullStatement() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);
    TweedleStatement stmt = sumThing.getBody().get(0);

    assertNotNull("The method statement should not be null.", stmt);
  }

  @Test
  public void classMethodWithEmptyReturnShouldHaveReturnStatement() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);
    TweedleStatement stmt = sumThing.getBody().get(0);

    assertTrue("The method statement should be a return.", stmt instanceof ReturnStatement);
  }

  @Test
  public void classMethodWithEmptyReturnReturnStatementShouldHaveAnExpression() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);
    ReturnStatement stmt = (ReturnStatement) sumThing.getBody().get(0);

    assertNotNull("The return statement should hold an expression.", stmt.getExpression());
  }

  @Test
  public void classMethodWithEmptyReturnReturnStatementShouldHaveTweedleNull() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);
    ReturnStatement stmt = (ReturnStatement) sumThing.getBody().get(0);

    assertEquals("The return statement should hold NULL.", TweedleNull.NULL, stmt.getExpression());
  }

  @Test
  public void classMethodShouldHaveANonNullStatement() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);
    TweedleStatement stmt = sumThing.getBody().get(0);

    assertNotNull("The method statement should not be null.", stmt);
  }

  @Test
  public void classMethodShouldHaveReturnStatement() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);
    TweedleStatement stmt = sumThing.getBody().get(0);

    assertTrue("The method statement should be a return.", stmt instanceof ReturnStatement);
  }

  @Test
  public void classMethodReturnStatementShouldHaveAnExpression() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);
    ReturnStatement stmt = (ReturnStatement) sumThing.getBody().get(0);

    assertNotNull("The method statement should hold an expression.", stmt.getExpression());
  }

  @Test
  public void classMethodReturnStatementShouldHaveAnAdditionExpression() {
    String scene = "class Scene extends SScene {\n" + "  WholeNumber sumThing() {\n" + "    return 3 + 4;\n" + "  }\n" + "}";
    TweedleClass tested = (TweedleClass) parseType(scene);
    TweedleMethod sumThing = tested.getMethods().get(0);
    ReturnStatement stmt = (ReturnStatement) sumThing.getBody().get(0);

    assertTrue("The method statement should hold an addition expression.", stmt.getExpression() instanceof AdditionExpression);
  }

  @Test
  public void somethingShouldBeCreatedForClassWithListener() {
    String scene = "class Scene extends SScene {\n" + "  Scene() {\n" + "    super();\n" + "  }\n" + "  void initializeEventListeners() {\n" + "    this.addSceneActivationListener(listener: (SceneActivationEvent event) -> {\n" + "      this.myFirstMethod();\n" + "    });\n" + "  }\n" + "}";
    TweedleType tested = parseType(scene);

    assertNotNull("The parser should have returned something.", tested);
  }

  @Test
  public void somethingShouldBeCreatedForGeneratedScene() {
    String generatedScene =
        """
        class Scene extends SScene models Scene {
          Scene() {
            super();
          }
        
          void initializeEventListeners() {
            this.addSceneActivationListener(listener: (SceneActivationEvent event)-> {
              this.myFirstMethod();
            });
          }
        
          void myFirstMethod() {
            this.sphere.jump();
            this.sphere.jump();
            doTogether {
              this.walrus.moveToward(target: this.sphere,amount: 2.0);
              this.walrus.moveToward(target: this.cylinder,amount: 2.0);
            }
            doTogether {
              this.sphere.setPaint(paint: Color.GREEN);
              this.sphere.setPaint(paint: Color.RED);
            }
            this.walrus.say(text: "hello \\"Ralph\\" How are you? \\\\\\"/\\" today?");
            doTogether {
              this.walrus.turn(direction: TurnDirection.LEFT,amount: 1.0);
            }
        *<  this.walrus.roll(direction: RollDirection.RIGHT,amount: 1.0); >*
            this.walrus.turn(direction: TurnDirection.LEFT,amount: 1.0);
            doInOrder {
              doInOrder {
              }
              // So much to say
              // And I can use multiple lines
              // Nicer if the other side updated as I typed, but what can you do?
        *<    doInOrder {
        *<      this.walrus.turnToFace(target: this.cylinder,details: TurnToFace.duration(unknown: 2.0)); >*
                this.walrus.turnToFace(target: this.sphere,details: TurnToFace.duration(unknown: 2.0));
              } >*
            }
            doInOrder {
              doTogether {
                forEach(SModel x in new SModel[]{this.sphere, this.walrus}) {
                  doTogether {
                  }
                }
              }
              SModel[] muddles <- new SModel[]{};
              doInOrder {
                doTogether {
                  this.walrus.turnToFace(target: this.cylinder,details: TurnToFace.duration(unknown: 2.0));
                  this.walrus.turnToFace(target: this.sphere,details: TurnToFace.duration(unknown: 2.0));
                }
              }
            }
        *<  countUpTo( indexA < 2 ) {
            } >*
            countUpTo( indexB < 2 ) {
            }
        *<  while (false) {
            } >*
            while (false) {
            }
        *<  forEach(SModel x in new SModel[]{this.sphere, this.walrus}) {
              doTogether {
              }
            } >*
            forEach(SModel x in new SModel[]{this.sphere, this.walrus}) {
              doTogether {
              }
            }
        *<  if(true) {
            } else {
            } >*
            if(true) {
            } else {
            }
        *<  doTogether {
            } >*
            doTogether {
            }
        *<  eachTogether(TextString msg in new TextString[]{"hello", "hello"}) {
              this.walrus.say(text: msg);
            } >*
            eachTogether(TextString msg in new TextString[]{"hello", "hello"}) {
              this.walrus.say(text: msg);
            }
        *<  WholeNumber a <- 2; >*
            WholeNumber a <- 2;
        *<  a <- 2; >*
            a <- 2;
          }
        
          void doInfix() {
            WholeNumber v <- 1+2+(2-1)*3;
            if((true||false)&&false) {
            } else {
            }
            if(false&&false||0.5<=1.0) {
            } else {
            }
            if((false||false)&&(true||true)) {
            } else {
            }
            if(false&&false||true&&true) {
            } else {
            }
          }
          SGround ground <- new SGround();
          SCamera camera <- new SCamera();
          Walrus walrus <- new Walrus();
          Sphere sphere <- new Sphere();
          Cylinder cylinder <- new Cylinder();
        
          void performCustomSetup() {
            // Make adjustments to the starting scene, in a way not available in the Scene editor
          }
        
          void performGeneratedSetUp() {
            // DO NOT EDIT
            // This code is automatically generated.  Any work you perform in this method will be overwritten.
            // DO NOT EDIT
            this.setAtmosphereColor(color: new Color(red: 0.588,green: 0.886,blue: 0.988));
            this.setFromAboveLightColor(color: Color.WHITE);
            this.setFromBelowLightColor(color: Color.BLACK);
            this.setFogDensity(density: 0.0);
            this.setName(name: "myScene");
            this.ground.setPaint(paint: SurfaceAppearance.GRASS);
            this.ground.setOpacity(opacity: 1.0);
            this.ground.setName(name: "ground");
            this.ground.setVehicle(vehicle: this);
            this.camera.setName(name: "camera");
            this.camera.setVehicle(vehicle: this);
            this.camera.setOrientationRelativeToVehicle(orientation: new Orientation(x: 0.0,y: 0.995185,z: 0.0980144,w: 6.12323E-17));
            this.camera.setPositionRelativeToVehicle(position: new Position(right: 9.61E-16,up: 1.56,backward: -7.85));
            this.walrus.setPaint(paint: Color.WHITE);
            this.walrus.setOpacity(opacity: 1.0);
            this.walrus.setName(name: "walrus");
            this.walrus.setVehicle(vehicle: this);
            this.walrus.setOrientationRelativeToVehicle(orientation: new Orientation(x: 0.0,y: 0.0,z: 0.0,w: 1.0));
            this.walrus.setPositionRelativeToVehicle(position: new Position(right: 0.618,up: 0.0111,backward: -0.877));
            this.sphere.setRadius(radius: 0.5);
            this.sphere.setPaint(paint: Color.WHITE);
            this.sphere.setOpacity(opacity: 1.0);
            this.sphere.setName(name: "sphere");
            this.sphere.setVehicle(vehicle: this);
            this.sphere.setOrientationRelativeToVehicle(orientation: new Orientation(x: 0.0,y: 0.0,z: 0.0,w: 1.0));
            this.sphere.setPositionRelativeToVehicle(position: new Position(right: -7.34,up: 0.5,backward: 18.9));
            this.cylinder.setRadius(radius: 0.5);
            this.cylinder.setLength(length: 1.0);
            this.cylinder.setPaint(paint: Color.WHITE);
            this.cylinder.setOpacity(opacity: 1.0);
            this.cylinder.setName(name: "cylinder");
            this.cylinder.setVehicle(vehicle: this);
            this.cylinder.setOrientationRelativeToVehicle(orientation: new Orientation(x: 0.0,y: 0.0,z: 0.0,w: 1.0));
            this.cylinder.setPositionRelativeToVehicle(position: new Position(right: 7.63,up: 0.0,backward: 19.7));
          }
        
          void handleActiveChanged(Boolean isActive,WholeNumber activationCount) {
            if(isActive) {
              if(activationCount==1) {
                this.performGeneratedSetUp();
                this.performCustomSetup();
                this.initializeEventListeners();
              } else {
                this.restoreStateAndEventListeners();
              }
            } else {
              this.preserveStateAndEventListeners();
            }
          }
          SGround getGround() {
            return this.ground;
          }
          SCamera getCamera() {
            return this.camera;
          }
          Walrus getWalrus() {
            return this.walrus;
          }
          Sphere getSphere() {
            return this.sphere;
          }
          Cylinder getCylinder() {
            return this.cylinder;
          }
        }\
        """;
    TweedleType tested = parseType(generatedScene);

    assertNotNull("The parser should have returned something.", tested);
  }

  @Test
  public void somethingShouldBeCreatedForClassToString() {
    String scene = "class SThing {\n" + "  SThing() {\n" + "  }\n" + "  TextString toString() {\n" + "    if( this.name != null ) {\n" + "      return this.name;\n" + "    } else {\n" + "      return \"unnamed \" .. Primitive.getClassName( instance: this );\n" + "    }\n" + "  }\n" + "}";
    TweedleType tested = parseType(scene);

    assertNotNull("The parser should have returned something.", tested);
  }
}
