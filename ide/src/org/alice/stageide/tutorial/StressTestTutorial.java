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
package org.alice.stageide.tutorial;

/**
 * @author Dennis Cosgrove
 */
public class StressTestTutorial {
	private static void createAndShowTutorial( final org.alice.stageide.StageIDE ide ) {
		final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( ide, 1 );
		org.alice.ide.memberseditor.MembersEditor membersEditor = ide.getMembersEditor();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = ide.getSceneField();
		ide.getEmphasizingClassesState().setValue( false );
//		membersEditor.getTabbedPaneSelectionState().setValue( tutorial.getFunctionsTab().getResolved() );
		tutorial.addMessageStep( 
				"Title", 
				"<b><center>Welcome To The Tutorial</center></b><p>This tutorial will introduce you to the basics.<p>" 
		);

		tutorial.addSpotlightStepForModel( 
				"Note Delay",
				"Note the <b>delay</b> template.",
				tutorial.createProcedureInvocationTemplateResolver( "delay" )
		);

		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Delay Procedure",
				"Drag <b>delay</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "delay" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>1.0</b>.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.EPIC_HACK_addDeclareProcedureDialogOpenAndCommitStep( 
				"Declare Procedure Foo", 
				"Declare procedure...", 
				"Type <b>foo</b> and press <i>Ok</i>",
				tutorial.createToDoCompletorValidator(),
				new org.alice.ide.operations.ast.DeclareProcedureOperation.EPIC_HACK_Validator() {
					public String getExplanationIfOkButtonShouldBeDisabled( String name ) {
						if( "foo".equalsIgnoreCase( name ) ) {
							return null;
						} else {
							return "<html>Please enter in the name <b>foo</b> and press <b>Ok</b> button.</html>";
						}
					}
				}
		);

		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Move Procedure",
				"Drag <b>move</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addPopupMenuStep(  
				"Delete Move",
				"Right Click on <b>move</b>.",
				tutorial.createFirstInvocationPopupMenuResolver( "move" ),
				"Select <b>Delete Statement</b.",
				tutorial.createToDoCompletorValidator()
		);
		
		tutorial.addDragAndDropStep( 
				"Drag Do Together",
				"Drag <b>Do Together</b>.",
				tutorial.createDoTogetherTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addSpotlightStepForModel(  
				"Note Do Together",
				"Note <b>Do Together</b>.",
				tutorial.createFirstStatementAssignableToPopupMenuResolver( edu.cmu.cs.dennisc.alice.ast.DoTogether.class )
		);

		tutorial.addPopupMenuStep(  
				"Delete Do Together",
				"Right Click on <b>Do Together</b>.",
				tutorial.createFirstStatementAssignableToPopupMenuResolver( edu.cmu.cs.dennisc.alice.ast.DoTogether.class ),
				"Select <b>Delete Statement</b.",
				tutorial.createToDoCompletorValidator()
		);
		
//		tutorial.addDragAndDropStep( 
//				"Drag Do Together",
//				"Drag <b>Do Together</b>.",
//				tutorial.createDoTogetherTemplateResolver(),
//				"Drop <b>here</b>.",
//				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
//				tutorial.createToDoCompletorValidator()
//		);

		for( int i=0; i<10; i++ ) {
			tutorial.addDragAndDropToPopupMenuStep( 
					"Drag If/Else",
					"Drag <b>If/Else</b>.",
					tutorial.createIfElseTemplateResolver(),
					"Drop <b>here</b>.",
					tutorial.createEndOfCurrentMethodBodyStatementListResolver(), 
					"Select <b>true</b>.",
					tutorial.createToDoCompletorValidator()
			);
		}

		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Move Procedure",
				"Drag <b>move</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Move Procedure",
				"Drag <b>move</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addActionStep( 
				"More",
				"Click <b>more...</b>",
				tutorial.createFirstInvocationMoreResolver( "move" ),
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addActionStep( 
				"More",
				"Click <b>more...</b>",
				tutorial.createLastInvocationMoreResolver( "move" ),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDragAndDropStep( 
				"Drag Do Together",
				"Drag <b>Do Together</b>.",
				tutorial.createDoTogetherTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createCurrentMethodBodyStatementListResolver( 1 ),
				tutorial.createToDoCompletorValidator()
		);
		
		java.awt.Point[] layoutHints = new java.awt.Point[] {
				new java.awt.Point( 100, 100 ),
				new java.awt.Point( 100, -100 ),
				new java.awt.Point( -100, -100 ),
				new java.awt.Point( -100, 100 )
		};
		for( java.awt.Point layoutHint : layoutHints ) {
			edu.cmu.cs.dennisc.tutorial.Step step = tutorial.addMessageStep( 
					"Layout Hint Example", 
					"layoutHint: " + layoutHint 
			);
			step.setLayoutHint( layoutHint );
		}

		tutorial.EPIC_HACK_addForEachInArrayLoopDragAndDropToPopupMenuToInputDialogStep(  
				"Drag For Each In Order",
				"Drag <strong>For Each In Order</strong>.",
				"Drop <strong>here</strong>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <strong>Other Array...</strong>",
				"Select <strong>Model</strong>, add <strong>grassyGround</strong> and <strong>grassyGround</strong>, and press <strong>OK</strong>",
				tutorial.createToDoCompletorValidator(),
				new org.alice.ide.cascade.customfillin.CustomInputDialogOperation.EPIC_HACK_Validator() {
					private boolean isAccessOfFieldNamed( edu.cmu.cs.dennisc.alice.ast.Expression expression, String name ) {
						if (expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess) {
							edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess) expression;
							edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
							if( field != null ) {
								return name.equals( field.getName() );
							}
						}
						return false;
					}
					public String getExplanationIfOkButtonShouldBeDisabled(org.alice.ide.choosers.ValueChooser<?> valueChooser) {
						final String[] desiredFieldNames = { "grassyGround", "grassyGround" };
						final String COMPLETE_INSTRUCTIONS = "<html>Select value type <strong>Model</strong> and add <strong>" + desiredFieldNames[ 0 ] + "</strong> and <strong>" + desiredFieldNames[ 1 ] + "</strong>.</html>";
						if (valueChooser instanceof org.alice.ide.choosers.ArrayChooser) {
							org.alice.ide.choosers.ArrayChooser arrayChooser = (org.alice.ide.choosers.ArrayChooser) valueChooser;
							edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation arrayInstanceCreation = arrayChooser.getValue();
							if( arrayInstanceCreation != null ) {
//								boolean isComponentTypeCorrect = false;
//								edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> arrayType = arrayInstanceCreation.arrayType.getValue();
//								if( arrayType != null ) {
//									if( arrayType.isArray() ) {
//										edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> componentType = arrayType.getComponentType();
//										edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> desiredComponentType = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Model.class );
//										isComponentTypeCorrect = componentType == desiredComponentType;
//									}
//								}
								edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> componentType = arrayChooser.EPIC_HACK_getArrayComponentType();
								edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> desiredComponentType = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Model.class );
								boolean isComponentTypeCorrect = componentType == desiredComponentType;
								boolean areExpressionsCorrect = false;
								java.util.ArrayList< edu.cmu.cs.dennisc.alice.ast.Expression > expressions = arrayInstanceCreation.expressions.getValue();
								if( expressions != null ) {
									if( expressions.size() == desiredFieldNames.length ) {
										final int N = desiredFieldNames.length;
										areExpressionsCorrect = true;
										for( int i=0; i<N; i++ ) {
											if( isAccessOfFieldNamed( expressions.get( i ), desiredFieldNames[ i ] ) ) {
												//pass
											} else {
												areExpressionsCorrect = false;
											}
										}
									}
								}
								if( isComponentTypeCorrect ) {
									if( areExpressionsCorrect ) {
										return null;
									} else {
										return "<html>Add <strong>"+ desiredFieldNames[ 0 ] + "</strong> and <strong>" + desiredFieldNames[ 1 ] + "</strong>.</html>";
									}
								} else {
									if( areExpressionsCorrect ) {
										return "<html>Select value type <strong>Model</strong>.</html>";
									} else {
										return COMPLETE_INSTRUCTIONS;
									}
								}
							}
							return COMPLETE_INSTRUCTIONS;
						} else {
							return "this should not happen.  you have found a bug.  ask for help.";
						}
					}
				} 
		);

		tutorial.EPIC_HACK_addDeclareProcedureDialogOpenAndCommitStep( 
				"Declare Procedure Foo", 
				"Declare procedure...", 
				"Type <b>foo</b> and press <i>Ok</i>",
				tutorial.createToDoCompletorValidator(),
				new org.alice.ide.operations.ast.DeclareProcedureOperation.EPIC_HACK_Validator() {
					public String getExplanationIfOkButtonShouldBeDisabled( String name ) {
						if( "foo".equalsIgnoreCase( name ) ) {
							return null;
						} else {
							return "<html>Please enter in the name <b>foo</b> and press <b>Ok</b> button.</html>";
						}
					}
				}
		);

		tutorial.EPIC_HACK_addDeclareMethodParameterDialogOpenAndCommitStep( 
				"Declare Parameter HowHigh", 
				"Declare parameter...", 
				"1) Select <i>value type:</i> <b>Double</b><p>2) Enter <i>name:</i> <b>howHigh</b><p>and press the <b>OK</b> button.",
				tutorial.createToDoCompletorValidator(),				
				new org.alice.ide.operations.ast.DeclareMethodParameterOperation.EPIC_HACK_Validator() {
					public String getExplanationIfOkButtonShouldBeDisabled( String name, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > valueType ) {
						if( "howHigh".equalsIgnoreCase( name ) ) {
							if( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE == valueType ) {
								return null;
							} else {
								return "<html>Please select <b>Double</b> value type, and press <b>Ok</b>.</html>";
							}
						} else {
							if( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE == valueType ) {
								return "<html>Please enter in name <b>howHigh</b>, and press <b>Ok</b>.</html>";
							} else {
								return "<html>Please select <b>Double</b> value type, enter in name <b>howHigh</b>, and press <b>OK</b>.</html>";
							}
						}
					}
				}
		);


		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag If/Else",
				"Drag <b>If/Else</b>.",
				tutorial.createIfElseTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>true</b>.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDragAndDropStep( 
				"Drag Do In Order",
				"Drag <b>Do In Order</b>.",
				tutorial.createDoInOrderTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( 
						tutorial.createFirstIfStatementListPropertyResolver() 
				),
				tutorial.createToDoCompletorValidator()
		);


		tutorial.addSelectTabStep(   
				"Select Run", 
				"Select run tab.", 
				ide.getEditorsTabSelectionState(),
				tutorial.createCodeResolver( "scene", "run" )
		);

//		tutorial.addDragAndDropStep( 
//				"Drag Count Loop",
//				"Drag <b>Count Loop</b>.",
//				tutorial.createCountLoopTemplateResolver(),
//				"Drop <b>here</b>.",
//				tutorial.createBeginingOfCurrentMethodBodyStatementListResolver(),
//				"Select <b>2</b>.",
//				tutorial.createToDoCompletorValidator()
//		);
		tutorial.addDragAndDropStep( 
				"Drag Do Together",
				"Drag <b>Do Together</b>.",
				tutorial.createDoTogetherTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( 
						tutorial.createFirstElseStatementListPropertyResolver() 
				),
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Move Procedure",
				"Drag <b>move</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver(
						tutorial.createFirstStatementListPropertyResolver( edu.cmu.cs.dennisc.alice.ast.DoTogether.class )
				),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addActionStep( 
				"More",
				"Click <b>more...</b>",
				tutorial.createLastInvocationMoreResolver( "move" ),
				tutorial.createToDoCompletorValidator()
		);
//		tutorial.addDragAndDropStep( 
//				"Drag Do In Order",
//				"Drag <b>Do In Order</b>.",
//				tutorial.createDoInOrderTemplateResolver(),
//				"Drop <b>here</b>.",
//				tutorial.createEndOfStatementListResolver( 
//						tutorial.createFirstIfStatementListPropertyResolver() 
//				),
//				tutorial.createToDoCompletorValidator()
//		);
		for( int i=0; i<10; i++ ) {
			tutorial.addDragAndDropToPopupMenuStep( 
					"Drag Count Loop",
					"Drag <b>Count Loop</b>.",
					tutorial.createCountLoopTemplateResolver(),
					"Drop <b>here</b>.",
					tutorial.createBeginingOfCurrentMethodBodyStatementListResolver(),
					"Select <b>2</b>.",
					tutorial.createToDoCompletorValidator()
			);
			tutorial.addDragAndDropToPopupMenuStep( 
					"Drag If/Else",
					"Drag <b>If/Else</b>.",
					tutorial.createIfElseTemplateResolver(),
					"Drop <b>here</b>.",
					tutorial.createEndOfStatementListResolver( 
							tutorial.createFirstElseStatementListPropertyResolver() 
					),
					"Select <b>true</b>.",
					tutorial.createToDoCompletorValidator()
			);
			tutorial.addDragAndDropToPopupMenuStep( 
					"Drag Move Procedure",
					"Drag <b>move</b> procedure.",
					tutorial.createProcedureInvocationTemplateResolver( "move" ),
					"Drop <b>here</b>.",
					tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
					"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
					tutorial.createToDoCompletorValidator()
			);
		}

		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag If/Else",
				"Drag <b>If/Else</b>.",
				tutorial.createIfElseTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>true</b>.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addDragAndDropStep( 
				"Drag Do In Order",
				"Drag <b>Do In Order</b>.",
				tutorial.createDoInOrderTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( 
						tutorial.createFirstIfStatementListPropertyResolver() 
				),
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addDragAndDropStep( 
				"Drag Do Together",
				"Drag <b>Do Together</b>.",
				tutorial.createDoTogetherTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( 
						tutorial.createFirstElseStatementListPropertyResolver() 
				),
				tutorial.createToDoCompletorValidator()
		);


		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Move Procedure",
				"Drag <b>move</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver(
						tutorial.createFirstIfStatementListPropertyResolver() 
				),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addSpotlightStepForModel( 
				"Note Move",
				"Note that <b>move</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver( "move" )
		);


		//		tutorial.addDragAndDropStep( 
//				"Drag Turn Procedure",
//				"Drag <b>turn</b> procedure.",
//				tutorial.createProcedureInvocationTemplateResolver( "turn" ),
//				"Drop <b>here</b>.",
//				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
//				"Select <b>LEFT</b> and <b>0.25</b> from the menus.",
//				tutorial.createToDoCompletorValidator()
//		);
//
		tutorial.addDragAndDropToInputDialogStep(  
				"Declared Variable I",
				"Drag <b>Local</b>.",
				tutorial.createDeclareLocalTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Enter <b>i</b> and <b>Integer</b>.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Resize Procedure",
				"Drag <b>resize</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "resize" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>2.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addDragAndDropStepForModel( 
				"Drag Local I",
				"Drag <b>i</b> local.",
				tutorial.createLocalNamedResolver( "i" ),
				"Drop <b>here</b>.",
				tutorial.createInvocationArgumentResolver( "resize", 0, 0 ),
				tutorial.createToDoCompletorValidator()
		);


		tutorial.addInputDialogOpenAndCommitStep( 
				"Declare Procedure Foo", 
				"Declare a procedure...", 
				"Type <b>foo</b> and press <i>Ok</i>",
				tutorial.createDeclareProcedureOperationResolver(),
				tutorial.createToDoCompletorValidator()
		);
//
//		tutorial.addSpotlightTabTitleStep(  
//				"Note Foo Tab", 
//				"Note the foo folder tab.", 
//				ide.getEditorsTabSelectionState(),
//				tutorial.createCurrentCodeResolver()
//		);
//
		tutorial.addInputDialogOpenAndCommitStep( 
				"Declare Parameter How High", 
				"Declare parameter....", 
				"Type <b>howHigh</b> and select <b>Double</b> and press <i>Ok</i>",
				tutorial.createDeclareMethodParameterOperationResolver(),
				tutorial.createToDoCompletorValidator()				
		);
		
		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Roll Procedure",
				"Drag <b>roll</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "roll" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>LEFT</b> and <b>0.25</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDragAndDropStepForModel( 
				"Drag Parameter",
				"Drag <b>howHigh</b> parameter.",
				tutorial.createFirstParameterResolver(),
// also an option:
//				tutorial.createParameterNamedResolver( "howHigh" ),
				"Drop <b>here</b>.",
				tutorial.createInvocationArgumentResolver( "roll", 0, 1 ),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addSpotlightStepForModel( 
				"Note For Each Variable",
				"Note For Each Variable.",
				tutorial.createFirstForEachInArrayLoopVariableResolver()
		);

		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Move Procedure",
				"Drag <b>move</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver(
						tutorial.createFirstStatementListPropertyResolver( edu.cmu.cs.dennisc.alice.ast.ForEachInArrayLoop.class )
				),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addSpotlightStepForModel( 
				"Note Move",
				"Note that <b>move</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver( "move" )
		);

		tutorial.addDragAndDropToPopupMenuStepForModel( 
				"Drag For Each In Array Variable",
				"Drag For Each In Array Variable.",
				tutorial.createFirstForEachInArrayLoopVariableResolver(),
				"Drop <b>here</b>.",
				tutorial.createInvocationArgumentResolver( "move", 0, 1 ),
				"Select <b>true</b>.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addActionStep(
				"Change Instance", 
				"change instance to <b>sunLight</b>",
				tutorial.createFirstInvocationInstanceResolver( "move" ),
				tutorial.createToDoCompletorValidator()
		);
		

		tutorial.addActionStep(
				"Change If Condition", 
				"change if condition to <b>false</b>",
				tutorial.createFirstIfElseStatementConditionResolver(),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addSelectTabStep( 
				"Select Functions Tab", 
				"Select the <b>Functions</b> tab.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFunctionsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Functions Tab", 
				"Now the functions are now displayed.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFunctionsTab()
		);
		tutorial.addSpotlightStepForModel( 
				"Note isWithinThresholdOf",
				"Note <b>isWithinThresholdOf</b>.",
				tutorial.createFunctionInvocationTemplateResolver( "isWithinThresholdOf" )
		);
		tutorial.addDragAndDropStepForModel( 
				"Drag isWithinThresholdOf",
				"Drag <b>isWithinThresholdOf</b>.",
				tutorial.createFunctionInvocationTemplateResolver( "isWithinThresholdOf" ),
				"Drop <b>here</b>.",
				tutorial.createFirstIfElseStatementConditionResolver(),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addActionStep(
				"Change Argument", 
				"change threshold argument to <b>1.0</b>",
				tutorial.createInvocationArgumentResolver( "isWithinThresholdOf", 0, 0 ),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDragAndDropStepForModel( 
				"Drag getDistanceTo",
				"Drag <b>getDistanceTo</b>.",
				tutorial.createFunctionInvocationTemplateResolver( "getDistanceTo" ),
				"Drop <b>here</b>.",
				tutorial.createInvocationArgumentResolver( "move", 0, 1),
				tutorial.createToDoCompletorValidator()
		);
		

		tutorial.addSelectTabStep( 
				"Procedures Tab", 
				"Select the <b>Procedures</b> tab.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"Now the procedures are now displayed.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);

		
		tutorial.addListSelectionStep(
				"Select Scene", 
				"Select the <b>scene</b>.<i>",
				ide.getAccessibleListState(),
				tutorial.createAccessibleResolver( "scene" )
		);

		tutorial.addActionStep( 
				"Edit Perform My Setup", 
				"edit performMySetup", 
				tutorial.createEditCodeOperationResolver( 
						tutorial.createCurrentAccessibleMethodResolver( "performMySetUp" )
				),
				tutorial.createToDoCompletorValidator()
		);


		tutorial.addSelectTabStep( 
				"Procedures Tab", 
				"Select the <b>Procedures</b> tab.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"Now the procedures are now displayed.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);

		tutorial.addActionStep( 
				"Declare Procedure Hop", 
				"Declare a procedure named <b>hop</b>.", 
				tutorial.createDeclareProcedureOperationResolver(),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addSpotlightTabTitleStep(  
				"Note Hop Tab", 
				"Note the folder tab.", 
				ide.getEditorsTabSelectionState(),
				tutorial.createCurrentCodeResolver()
		);

		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Properies Tab", 
				"Note the area to implement hop.", 
				ide.getEditorsTabSelectionState(),
				tutorial.createCurrentCodeResolver()
		);

		
		
		tutorial.addSelectTabStep( 
				"Procedures Tab", 
				"Select the <b>Procedures</b> tab.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"Now the procedures are now displayed.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);

		tutorial.addSpotlightStepForModel( 
				"For Each In Array", 
				"This is the For Each In Array tile.", 
				tutorial.createForEachInArrayLoopTemplateResolver() 
		);
		tutorial.addDragAndDropToPopupMenuStep(  
				"Drag For Each In Array",
				"Drag <b>For Each In Array</b>.",
				tutorial.createForEachInArrayLoopTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>Other Array...</b>.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStepForModel( 
				"Do In Order", 
				"This is the Do In Order tile.", 
				tutorial.createDoInOrderTemplateResolver() 
		);
		tutorial.addDragAndDropStep( 
				"Drag Do In Order",
				"Drag <b>Do In Order</b>.",
				tutorial.createDoInOrderTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( 
						tutorial.createFirstStatementListPropertyResolver( edu.cmu.cs.dennisc.alice.ast.ForEachInArrayLoop.class ) 
				),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDialogOpenAndCloseStep( 
				"Run", 
				"Now, try running your program again.<p>The <b>iceSkater</b> should spin and then skate.", 
				"Press the <b>Close</b> button", 
				ide.getRunOperation() 
		);
		
		tutorial.addSpotlightStepForModel( 
				"Count Loop", 
				"This is the Count Loop tile.", 
				tutorial.createCountLoopTemplateResolver() 
		);
		tutorial.addDragAndDropToPopupMenuStep(  
				"Drag Count Loop",
				"Drag <b>Count Loop</b>.",
				tutorial.createCountLoopTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createBeginingOfCurrentMethodBodyStatementListResolver(),
				"Select <b>2</b>.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStepForModel( 
				"Do Together", 
				"This is the Do Together tile.", 
				tutorial.createDoTogetherTemplateResolver() 
		);
		tutorial.addDragAndDropStep( 
				"Drag Do Together",
				"Drag <b>Do Together</b>.",
				tutorial.createDoTogetherTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createCurrentMethodBodyStatementListResolver( 1 ),
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStepForModel( 
				"Note Do Together", 
				"Note <b>Do Together</b>.",
				tutorial.createFirstStatementAssignableToResolver( edu.cmu.cs.dennisc.alice.ast.DoTogether.class )
		);

		tutorial.addSpotlightStep( 
				"Scene Editor", 
				"This is the scene editor.", 
				ide.getSceneEditor() 
		);
		tutorial.addSpotlightStepForModel( 
				"Curent Instance", 
				"The current instance is displayed here.", 
				ide.getAccessibleListState()
		);
		tutorial.addSpotlightStep( 
				"Instance Details", 
				"The currently selected instance methods and fields are displayed here.<p>Right now, it is showing the details of the <b>camera</b>.", 
				membersEditor 
		);

		tutorial.addSpotlightStepForModel( 
				"Instance Details", 
				"This is the code editor.", 
				ide.getEditorsTabSelectionState() 
		);

//		tutorial.addItemSelectionStep(
//				"Select Scene",
//				"Select the <b>scene</b> instance.",
//				ide.getFieldSelectionState(),
//				sceneField
//		);
//		
//		tutorial.addSpotlightStep( 
//				"Note Scene Details", 
//				"Now the scene instance details are displayed.", 
//				membersEditor 
//		);


		tutorial.addSpotlightStep( 
				"Constructs",
				"This where loops and locals live.", 
				ide.getUbiquitousPane() 
		);


		tutorial.addSelectTabStep( 
				"Select Functions Tab", 
				"Select the <b>Functions</b> tab.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFunctionsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Functions Tab", 
				"Now the functions are now displayed.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFunctionsTab()
		);
		tutorial.addSelectTabStep( 
				"Properies Tab", 
				"Select the <b>Properies</b> tab.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFieldsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Properies Tab", 
				"Now the properties are now displayed.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFieldsTab()
		);
		tutorial.addSelectTabStep( 
				"Procedures Tab", 
				"Select the <b>Procedures</b> tab.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"Now the procedures are now displayed.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);

		tutorial.addBooleanStateStep( 
				"Edit Scene", 
				"Press the <b>Edit Scene</b> button", 
				ide.getIsSceneEditorExpandedState(),
				true
		);
		tutorial.addMessageStep( 
				"Note Edit Scene",
				"Note you are now editing the scene." 
		);
		tutorial.addBooleanStateStep( 
				"Edit Code", 
				"Press the <b>Edit Code</b> button", 
				ide.getIsSceneEditorExpandedState(),
				false
		);
		tutorial.addMessageStep( 
				"Note Edit Code",
				"Note you are now editing the code." 
		);

//		tutorial.addSpotlightStep( 
//				"Note Resize",
//				"Note that <b>Do in order</b> has been added to your count loop.",
//				tutorial.createInvocationResolver(resizeMethod, 0)
//		);
		tutorial.addDragAndDropToPopupMenuStep(  
				"Drag Delay",
				"Drag <b>delay</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "delay" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>1.0</b> from the menu.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStepForModel( 
				"Note Delay",
				"Note that <b>delay</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver("delay")
		);
		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Resize Procedure",
				"Drag <b>resize</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "resize" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>2.0</b> from the menu.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStepForModel( 
				"Note Resize",
				"Note that <b>resize</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver("resize")
		);

		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Move Procedure",
				"Drag <b>move</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStepForModel( 
				"Note Move",
				"Note that <b>move</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver("move")
		);

		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Turn Procedure",
				"Drag <b>turn</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "turn" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>LEFT</b> and <b>0.25</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStepForModel( 
				"Note Turn",
				"Note that <b>turn</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver("turn")
		);
		tutorial.setVisible( true );
		ide.getFrame().setVisible( true );
	}
	public static void main( final String[] args ) throws Exception {
		final org.alice.stageide.tutorial.TutorialIDE ide = org.alice.ide.LaunchUtilities.launchAndWait( org.alice.stageide.tutorial.TutorialIDE.class, null, args, false );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				createAndShowTutorial( ide );
			} 
		} );
	}
}
