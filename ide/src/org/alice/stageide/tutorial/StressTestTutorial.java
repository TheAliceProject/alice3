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
		final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( ide, 9 );
		org.alice.ide.memberseditor.MembersEditor membersEditor = ide.getMembersEditor();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = tutorial.getSceneField();
		final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice cameraField = tutorial.getFieldDeclaredOnSceneType( "camera" );
		final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice grassyGroundField = tutorial.getFieldDeclaredOnSceneType( "grassyGround" );
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice runMethod = ide.getSceneType().getDeclaredMethod( "run" );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava resizeMethod = tutorial.findShortestMethod( grassyGroundField, "resize" );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava moveMethod = tutorial.findShortestMethod( grassyGroundField, "move" );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava turnMethod = tutorial.findShortestMethod( grassyGroundField, "turn" );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava delayMethod = tutorial.findShortestMethod( grassyGroundField, "delay" );
		assert resizeMethod != null;
		assert moveMethod != null;
		assert turnMethod != null;
		assert delayMethod != null;
		
		tutorial.addMessageStep( 
				"Title", 
				"<html><b><center>Welcome To The Tutorial</center></b><p>This tutorial will introduce you to the basics.<p></html>" 
		);
		tutorial.addSpotlightStep( 
				"For Each In Array", 
				"<html>This is the For Each In Array tile.</html>", 
				tutorial.createForEachInArrayLoopTemplateResolver() 
		);
		tutorial.addDragAndDropStep( 
				"Drag For Each In Array",
				"<html>Drag <b>For Each In Array</b>.</html>",
				tutorial.createForEachInArrayLoopTemplateResolver(),
				"<html>Drop <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				"<html>Select <b>Other Array...</b>.</html>",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addDialogOpenStep( 
				"Run", 
				"<html>Now, try running your program again.<p>The <b>iceSkater</b> should spin and then skate.</html>", 
				ide.getRunOperation() 
		);
		
//		tutorial.addSpotlightStep( 
//				"Dialog Box", 
//				"<html>Note the Dialog Box.</html>", 
//				new edu.cmu.cs.dennisc.tutorial.TrackableShapeResolver() {
//					@Override
//					protected edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape() {
//						return ide.getRunOperation().getActiveDialog();
//					}
//				} 
//		);
		tutorial.addDialogCloseStep( 
				"Run", 
				"<html>Press the <b>Close</b> button</html>", 
				ide.getRunOperation() 
		);
		tutorial.addSpotlightStep( 
				"Do In Order", 
				"<html>This is the Do In Order tile.</html>", 
				tutorial.createDoInOrderTemplateResolver() 
		);
		tutorial.addDragAndDropStep( 
				"Drag Do In Order",
				"<html>Drag <b>Do In Order</b>.</html>",
				tutorial.createDoInOrderTemplateResolver(),
				"<html>Drop <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Count Loop", 
				"<html>This is the Count Loop tile.</html>", 
				tutorial.createCountLoopTemplateResolver() 
		);
		tutorial.addDragAndDropStep( 
				"Drag Count Loop",
				"<html>Drag <b>Count Loop</b>.</html>",
				tutorial.createCountLoopTemplateResolver(),
				"<html>Drop <b>here</b>.</html>",
				tutorial.createBeginingOfStatementListResolver( runMethod ),
				"<html>Select <b>2</b>.</html>",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Do Together", 
				"<html>This is the Do Together tile.</html>", 
				tutorial.createDoTogetherTemplateResolver() 
		);
		tutorial.addDragAndDropStep( 
				"Drag Do Together",
				"<html>Drag <b>Do Together</b>.</html>",
				tutorial.createDoTogetherTemplateResolver(),
				"<html>Drop <b>here</b>.</html>",
				tutorial.createStatementListResolver( runMethod, 1 ),
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Do Together", 
				"<html>Note <b>Do Together</b>.</html>",
				tutorial.createFirstStatementAssignableToResolver( edu.cmu.cs.dennisc.alice.ast.DoTogether.class )
		);

		tutorial.addSpotlightStep( 
				"Scene Editor", 
				"<html>This is the scene editor.</html>", 
				ide.getSceneEditor() 
		);
		tutorial.addSpotlightStep( 
				"Curent Instance", 
				"<html>The current instance is displayed here.</html>", 
				ide.getFieldSelectionState()
		);
		tutorial.addSpotlightStep( 
				"Instance Details", 
				"<html>The currently selected instance methods and fields are displayed here.<p>Right now, it is showing the details of the <b>camera</b>.</html>", 
				membersEditor 
		);

		tutorial.addSpotlightStep( 
				"Instance Details", 
				"<html>This is the code editor.</html>", 
				ide.getEditorsTabSelectionState() 
		);

//		tutorial.addItemSelectionStep(
//				"Select Scene",
//				"<html>Select the <b>scene</b> instance.</html>",
//				ide.getFieldSelectionState(),
//				sceneField
//		);
//		
//		tutorial.addSpotlightStep( 
//				"Note Scene Details", 
//				"<html>Now the scene instance details are displayed.</html>", 
//				membersEditor 
//		);


		tutorial.addSpotlightStep( 
				"Constructs",
				"<html>This where loops and locals live.</html>", 
				ide.getUbiquitousPane() 
		);


		tutorial.addSelectTabStep( 
				"Select Functions Tab", 
				"<html>Select the <b>Functions</b> tab.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFunctionsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Functions Tab", 
				"<html>Now the functions are now displayed.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFunctionsTab()
		);
		tutorial.addSelectTabStep( 
				"Properies Tab", 
				"<html>Select the <b>Properies</b> tab.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFieldsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Properies Tab", 
				"<html>Now the properties are now displayed.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFieldsTab()
		);
		tutorial.addSelectTabStep( 
				"Procedures Tab", 
				"<html>Select the <b>Procedures</b> tab.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"<html>Now the procedures are now displayed.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getProceduresTab()
		);

		tutorial.addBooleanStateStep( 
				"Edit Scene", 
				"<html>Press the <b>Edit Scene</b> button</html>", 
				ide.getIsSceneEditorExpandedState(),
				true
		);
		tutorial.addMessageStep( 
				"Note Edit Scene",
				"<html>Note you are now editing the scene.</html>" 
		);
		tutorial.addBooleanStateStep( 
				"Edit Code", 
				"<html>Press the <b>Edit Code</b> button</html>", 
				ide.getIsSceneEditorExpandedState(),
				false
		);
		tutorial.addMessageStep( 
				"Note Edit Code",
				"<html>Note you are now editing the code.</html>" 
		);

//		tutorial.addSpotlightStep( 
//				"Note Resize",
//				"<html>Note that <b>Do in order</b> has been added to your count loop.</html>",
//				tutorial.createInvocationResolver(resizeMethod, 0)
//		);
		tutorial.addDragAndDropStep( 
				"Drag Delay",
				"<html>Drag <b>delay</b> procedure.</html>",
				org.alice.ide.memberseditor.TemplateFactory.getProcedureInvocationTemplate( delayMethod ),
				"<html>Drop <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				"<html>Select <b>1.0</b> from the menu.</html>",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Delay",
				"<html>Note that <b>delay</b> has been added to your run method.</html>",
				tutorial.createInvocationResolver(delayMethod, 0)
		);
		tutorial.addDragAndDropStep( 
				"Drag Resize Procedure",
				"<html>Drag <b>resize</b> procedure.</html>",
				org.alice.ide.memberseditor.TemplateFactory.getProcedureInvocationTemplate( resizeMethod ),
				"<html>Drop <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				"<html>Select <b>2.0</b> from the menu.</html>",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Resize",
				"<html>Note that <b>resize</b> has been added to your run method.</html>",
				tutorial.createInvocationResolver(resizeMethod, 0)
		);

		tutorial.addDragAndDropStep( 
				"Drag Move Procedure",
				"<html>Drag <b>move</b> procedure.</html>",
				org.alice.ide.memberseditor.TemplateFactory.getProcedureInvocationTemplate( moveMethod ),
				"<html>Drop <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				"<html>Select <b>FORWARD</b> and <b>1.0</b> from the menus.</html>",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Move",
				"<html>Note that <b>move</b> has been added to your run method.</html>",
				tutorial.createInvocationResolver( moveMethod, 0)
		);

		tutorial.addDragAndDropStep( 
				"Drag Turn Procedure",
				"<html>Drag <b>turn</b> procedure.</html>",
				org.alice.ide.memberseditor.TemplateFactory.getProcedureInvocationTemplate( turnMethod ),
				"<html>Drop <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				"<html>Select <b>LEFT</b> and <b>0.25</b> from the menus.</html>",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addSpotlightStep( 
				"Note Turn",
				"<html>Note that <b>turn</b> has been added to your run method.</html>",
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
