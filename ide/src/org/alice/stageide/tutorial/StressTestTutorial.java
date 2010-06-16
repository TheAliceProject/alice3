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
		final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( ide, 0 );
		org.alice.ide.memberseditor.MembersEditor membersEditor = ide.getMembersEditor();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = ide.getSceneField();
		ide.getEmphasizingClassesState().setValue( false );
//		membersEditor.getTabbedPaneSelectionState().setValue( tutorial.getFunctionsTab().getResolved() );
		tutorial.addMessageStep( 
				"Title", 
				"<b><center>Welcome To The Tutorial</center></b><p>This tutorial will introduce you to the basics.<p>" 
		);

//		tutorial.addDialogOpenStep( 
//				"Declare Procedure", 
//				"Declare a procedure.", 
//				tutorial.createDeclareProcedureOperationResolver()
//		);
		tutorial.EPIC_HACK_addDeclareProcedureOperationStep( 
				"Declare Procedure", 
				"Declare a procedure.", 
				new org.alice.ide.operations.ast.DeclareProcedureOperation.EPIC_HACK_Validator() {
					public String getExplanationIfOkButtonShouldBeDisabled( String name ) {
						if( name.equals( "foo" ) ) {
							return null;
						} else {
							return "<html>Please type in the name <b>foo</b> and press <b>Ok</b> button.</html>";
						}
					}
				}
		);

		tutorial.addInputDialogCommitStep( 
				"Name Foo", 
				"Type <b>foo</b> and press <i>Ok</i>",
				tutorial.createDeclareProcedureOperationResolver(),
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
		tutorial.addDragAndDropStep( 
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
		tutorial.addMessageStep( 
				"Title", 
				"Note" 
		);

		tutorial.addDragAndDropStep( 
				"Drag If/Else",
				"Drag <b>If/Else</b>.",
				tutorial.createIfElseTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>true</b>.",
				tutorial.createToDoCompletorValidator()
		);
		for( int i=0; i<10; i++ ) {
			tutorial.addDragAndDropStep( 
					"Drag Count Loop",
					"Drag <b>Count Loop</b>.",
					tutorial.createCountLoopTemplateResolver(),
					"Drop <b>here</b>.",
					tutorial.createBeginingOfCurrentMethodBodyStatementListResolver(),
					"Select <b>2</b>.",
					tutorial.createToDoCompletorValidator()
			);
			tutorial.addDragAndDropStep( 
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
			tutorial.addDragAndDropStep( 
					"Drag Move Procedure",
					"Drag <b>move</b> procedure.",
					tutorial.createProcedureInvocationTemplateResolver( "move" ),
					"Drop <b>here</b>.",
					tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
					"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
					tutorial.createToDoCompletorValidator()
			);
		}

		tutorial.addDragAndDropStep( 
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


		tutorial.addDragAndDropStep( 
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

		tutorial.addSpotlightStep( 
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
		tutorial.addDragAndDropStep( 
				"Declared Variable I",
				"Drag <b>Local</b>.",
				tutorial.createDeclareLocalTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Enter <b>i</b> and <b>Integer</b>.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDragAndDropStep( 
				"Drag Resize Procedure",
				"Drag <b>resize</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "resize" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>2.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addDragAndDropStep( 
				"Drag Local I",
				"Drag <b>i</b> local.",
				tutorial.createLocalNamedResolver( "i" ),
				"Drop <b>here</b>.",
				tutorial.createInvocationArgumentResolver( "resize", 0, 0 ),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDialogOpenStep( 
				"Declare Procedure", 
				"Declare a procedure.", 
				tutorial.createDeclareProcedureOperationResolver()
		);

		tutorial.addInputDialogCommitStep( 
				"Name Foo", 
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
		tutorial.addDialogOpenStep( 
				"Declare Parameter", 
				"Declare Parameter.", 
				tutorial.createDeclareMethodParameterOperationResolver() 
		);

		tutorial.addInputDialogCommitStep( 
				"Name HowHigh", 
				"Type <b>howHigh</b> and select <b>Double</b> and press <i>Ok</i>",
				tutorial.createDeclareMethodParameterOperationResolver(),
				tutorial.createToDoCompletorValidator()				
		);
		
		tutorial.addDragAndDropStep( 
				"Drag Roll Procedure",
				"Drag <b>roll</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "roll" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>LEFT</b> and <b>0.25</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDragAndDropStep( 
				"Drag Parameter",
				"Drag <b>howHigh</b> parameter.",
				tutorial.createFirstParameterResolver(),
// also an option:
//				tutorial.createParameterNamedResolver( "howHigh" ),
				"Drop <b>here</b>.",
				tutorial.createInvocationArgumentResolver( "roll", 0, 1 ),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDragAndDropStep( 
				"Drag For Each In Order",
				"Drag <b>For Each In Order</b>.",
				tutorial.createForEachInArrayLoopTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addSpotlightStep( 
				"Note For Each Variable",
				"Note For Each Variable.",
				tutorial.createFirstForEachInArrayLoopVariableResolver()
		);

		tutorial.addDragAndDropStep( 
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

		tutorial.addSpotlightStep( 
				"Note Move",
				"Note that <b>move</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver( "move" )
		);

		tutorial.addDragAndDropStep( 
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
		tutorial.addSpotlightStep( 
				"Note isWithinThresholdOf",
				"Note <b>isWithinThresholdOf</b>.",
				tutorial.createFunctionInvocationTemplateResolver( "isWithinThresholdOf" )
		);
		tutorial.addDragAndDropStep( 
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

		tutorial.addDragAndDropStep( 
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

		
		tutorial.addItemSelectionStep(
				"Select Scene", 
				"Select the <b>scene</b>.<i>",
				ide.getFieldSelectionState(),
				tutorial.createRootFieldResolver()
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

		tutorial.addSpotlightStep( 
				"For Each In Array", 
				"This is the For Each In Array tile.", 
				tutorial.createForEachInArrayLoopTemplateResolver() 
		);
		tutorial.addDragAndDropStep( 
				"Drag For Each In Array",
				"Drag <b>For Each In Array</b>.",
				tutorial.createForEachInArrayLoopTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>Other Array...</b>.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
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

		tutorial.addDialogOpenStep( 
				"Run", 
				"Now, try running your program again.<p>The <b>iceSkater</b> should spin and then skate.", 
				ide.getRunOperation() 
		);
		
//		tutorial.addSpotlightStep( 
//				"Dialog Box", 
//				"Note the Dialog Box.", 
//				new edu.cmu.cs.dennisc.tutorial.TrackableShapeResolver() {
//					@Override
//					protected edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape() {
//						return ide.getRunOperation().getActiveDialog();
//					}
//				} 
//		);
		tutorial.addDialogCloseStep( 
				"Run", 
				"Press the <b>Close</b> button", 
				ide.getRunOperation() 
		);
		tutorial.addSpotlightStep( 
				"Count Loop", 
				"This is the Count Loop tile.", 
				tutorial.createCountLoopTemplateResolver() 
		);
		tutorial.addDragAndDropStep( 
				"Drag Count Loop",
				"Drag <b>Count Loop</b>.",
				tutorial.createCountLoopTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createBeginingOfCurrentMethodBodyStatementListResolver(),
				"Select <b>2</b>.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
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
		tutorial.addSpotlightStep( 
				"Note Do Together", 
				"Note <b>Do Together</b>.",
				tutorial.createFirstStatementAssignableToResolver( edu.cmu.cs.dennisc.alice.ast.DoTogether.class )
		);

		tutorial.addSpotlightStep( 
				"Scene Editor", 
				"This is the scene editor.", 
				ide.getSceneEditor() 
		);
		tutorial.addSpotlightStep( 
				"Curent Instance", 
				"The current instance is displayed here.", 
				ide.getFieldSelectionState()
		);
		tutorial.addSpotlightStep( 
				"Instance Details", 
				"The currently selected instance methods and fields are displayed here.<p>Right now, it is showing the details of the <b>camera</b>.", 
				membersEditor 
		);

		tutorial.addSpotlightStep( 
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
		tutorial.addDragAndDropStep( 
				"Drag Delay",
				"Drag <b>delay</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "delay" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>1.0</b> from the menu.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Delay",
				"Note that <b>delay</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver("delay")
		);
		tutorial.addDragAndDropStep( 
				"Drag Resize Procedure",
				"Drag <b>resize</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "resize" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>2.0</b> from the menu.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Resize",
				"Note that <b>resize</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver("resize")
		);

		tutorial.addDragAndDropStep( 
				"Drag Move Procedure",
				"Drag <b>move</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Move",
				"Note that <b>move</b> has been added to your run method.",
				tutorial.createFirstInvocationResolver("move")
		);

		tutorial.addDragAndDropStep( 
				"Drag Turn Procedure",
				"Drag <b>turn</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( "turn" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>LEFT</b> and <b>0.25</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
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
