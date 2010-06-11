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

import edu.cmu.cs.dennisc.croquet.Resolver;
/**
 * @author Dennis Cosgrove
 */
public class StressTestTutorial {
	private static void createAndShowTutorial( final org.alice.stageide.StageIDE ide ) {
		final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( ide, 0 );
		org.alice.ide.memberseditor.MembersEditor membersEditor = ide.getMembersEditor();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = tutorial.getSceneField();
		final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice cameraField = tutorial.getFieldDeclaredOnSceneType( "camera" );
		final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice grassyGroundField = tutorial.getFieldDeclaredOnSceneType( "grassyGround" );
		//final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice runMethod = ide.getSceneType().getDeclaredMethod( "run" );
		Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > resizeMethod = tutorial.createMethodResolver( grassyGroundField, "resize" );
		Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > moveMethod = tutorial.createMethodResolver( grassyGroundField, "move" );
		Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > turnMethod = tutorial.createMethodResolver( grassyGroundField, "turn" );
		Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > delayMethod = tutorial.createMethodResolver( grassyGroundField, "delay" );
		assert resizeMethod.getResolved() != null;
		assert moveMethod.getResolved() != null;
		assert turnMethod.getResolved() != null;
		assert delayMethod.getResolved() != null;
		
		ide.getEmphasizingClassesState().setValue( false );
//		membersEditor.getTabbedPaneSelectionState().setValue( tutorial.getFunctionsTab().getResolved() );
		
		tutorial.addMessageStep( 
				"Title", 
				"<b><center>Welcome To The Tutorial</center></b><p>This tutorial will introduce you to the basics.<p>" 
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
						tutorial.createMethodResolver( sceneField, "performMySetUp" )
				),
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDialogOpenStep( 
				"Declare Procedure", 
				"Declare Procedure.", 
				tutorial.createDeclareProcedureOperationResolver() 
		);

		tutorial.addInputDialogCommitStep( 
				"Name Hop", 
				"Type <b>hop</b> and press <i>Ok</i>",
				tutorial.createDeclareProcedureOperationResolver()				
		);

		
		tutorial.addDragAndDropStep( 
				"Drag If/Else",
				"Drag <b>If/Else</b>.",
				tutorial.createIfElseTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( tutorial.createCurrentCodeResolver() ),
				"Select <b>true</b>.",
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
				tutorial.createFunctionInvocationTemplateResolver( grassyGroundField, "isWithinThresholdOf" )
		);
		tutorial.addDragAndDropStep( 
				"Drag isWithinThresholdOf",
				"Drag <b>isWithinThresholdOf</b>.",
				tutorial.createFunctionInvocationTemplateResolver( grassyGroundField, "isWithinThresholdOf" ),
				"Drop <b>here</b>.",
				tutorial.createFirstIfConditionResolver(),
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addDragAndDropStep( 
				"Drag isWithinThresholdOf",
				"Drag <b>getDistanceTo</b>.",
				tutorial.createFunctionInvocationTemplateResolver( grassyGroundField, "getDistanceTo" ),
				"Drop <b>here</b>.",
				tutorial.createInvocationArgumentResolver( tutorial.createMethodResolver(grassyGroundField, "move"), 0, 1),
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
				"Select Run", 
				"Select run tab.", 
				ide.getEditorsTabSelectionState(),
				tutorial.createMethodResolver( sceneField, "run" )
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
				tutorial.createEndOfStatementListResolver( tutorial.createCurrentCodeResolver() ),
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
				tutorial.createBeginingOfStatementListResolver( tutorial.createCurrentCodeResolver() ),
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
				tutorial.createStatementListResolver( tutorial.createCurrentCodeResolver(), 1 ),
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
				tutorial.createProcedureInvocationTemplateResolver( grassyGroundField, "delay" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( tutorial.createCurrentCodeResolver() ),
				"Select <b>1.0</b> from the menu.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Delay",
				"Note that <b>delay</b> has been added to your run method.",
				tutorial.createInvocationResolver(delayMethod, 0)
		);
		tutorial.addDragAndDropStep( 
				"Drag Resize Procedure",
				"Drag <b>resize</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( grassyGroundField, "resize" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( tutorial.createCurrentCodeResolver() ),
				"Select <b>2.0</b> from the menu.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Resize",
				"Note that <b>resize</b> has been added to your run method.",
				tutorial.createInvocationResolver(resizeMethod, 0)
		);

		tutorial.addDragAndDropStep( 
				"Drag Move Procedure",
				"Drag <b>move</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( grassyGroundField, "move" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( tutorial.createCurrentCodeResolver() ),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Move",
				"Note that <b>move</b> has been added to your run method.",
				tutorial.createInvocationResolver( moveMethod, 0)
		);

		tutorial.addDragAndDropStep( 
				"Drag Turn Procedure",
				"Drag <b>turn</b> procedure.",
				tutorial.createProcedureInvocationTemplateResolver( grassyGroundField, "turn" ),
				"Drop <b>here</b>.",
				tutorial.createEndOfStatementListResolver( tutorial.createCurrentCodeResolver() ),
				"Select <b>LEFT</b> and <b>0.25</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Turn",
				"Note that <b>turn</b> has been added to your run method.",
				tutorial.createInvocationResolver(turnMethod, 0)
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
