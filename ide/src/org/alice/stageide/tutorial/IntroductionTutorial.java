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
public class IntroductionTutorial {
	private static void createAndShowTutorial( final org.alice.stageide.StageIDE ide ) {
		final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( ide, 8 );
		final org.alice.ide.memberseditor.MembersEditor membersEditor = ide.getMembersEditor();

		final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = ide.getSceneField();
		final edu.cmu.cs.dennisc.alice.ast.AbstractField cameraField = sceneField.getValueType().getDeclaredField( "camera" );
		final edu.cmu.cs.dennisc.alice.ast.AbstractField iceSkaterField = sceneField.getValueType().getDeclaredField( "iceSkater" );
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice runMethod = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)sceneField.getValueType().getDeclaredMethod( "run" );

		final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice iceSkaterType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)iceSkaterField.getValueType();
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice prepareToSkateMethod = iceSkaterType.getDeclaredMethod( "prepareToSkate" );
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice doSimpleSpinMethod = iceSkaterType.getDeclaredMethod( "doSimpleSpin" );
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice skateMethod = iceSkaterType.getDeclaredMethod( "skate", Integer.class );
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice skateBackwardsMethod = iceSkaterType.getDeclaredMethod( "skateBackwards", Integer.class );
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice jumpMethod = iceSkaterType.getDeclaredMethod( "jump" );
		assert prepareToSkateMethod != null;
		assert doSimpleSpinMethod != null;
		assert skateMethod != null;
		assert skateBackwardsMethod != null;
		assert jumpMethod != null;

		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				ide.getAccessibleListState().setSelectedItem( cameraField );
			}
		} );
		
		tutorial.addMessageStep( 
				"Welcome", 
				"<b><center>Welcome To The Tutorial</center></b><p>This tutorial will introduce you to the basics.<p>" 
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

		tutorial.addMessageStep( 
				"All is well", 
				"Don't worry if you don't fully understand all of the areas of the interface.  We'll be going into more detail over the course of the tutorial." 
		);
		
		tutorial.addMessageStep( 
				"Routine", 
				"In this tutorial, we're going to make the <b>iceSkater</b> perform a routine."
		);

		tutorial.addSpotlightStepForModel( 
				"Prepare To Skate", 
				"Right now, her routine is pretty simple.<p>First she <b>prepares to skate</b>...",
				tutorial.createFirstInvocationResolver( "prepareToSkate" )
				
		);
		tutorial.addSpotlightStepForModel( 
				"Do Simple Spin", 
				"Second, she <b>does a simple spin</b>.",
				tutorial.createFirstInvocationResolver( "doSimpleSpin" )
		);
		
		tutorial.addDialogOpenAndCloseStep( 
				"Run", 
				"Press the <b>Run</b> button", 
				"Press the <b>Close</b> button", 
				ide.getRunOperation() 
		);

		tutorial.addMessageStep( 
				"More Moves", 
				"Now, let's add some more moves to the <b>iceSkater</b>'s routine."
		);

		tutorial.addMessageStep( 
				"Ice Skater Details", 
				"First, we'll need to find out what the <b>iceSkater</b> can do."
		);
		
		tutorial.addListSelectionStep(
				"Select Ice Skater", 
				"By selecting an instance we can see what procedures it knows how to do.<p><p><i>Select the <b>iceSkater</b>.<i>",
				ide.getAccessibleListState(),
				tutorial.createAccessibleResolver( "iceSkater" )
		);
		
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Ice Skater Details", 
				"These are all of the procedures that the <b>iceSkater</b> knows how to do.", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFunctionsTab()
		);

		tutorial.addMessageStep( 
				"Skate", 
				"Let's have the <b>iceSkater</b> skate <b>2</b> strides after she finishes her spin."
		);

		tutorial.addDragAndDropToPopupMenuStep(  
				"Invoke Skate Procedure",
				"Drag <b>skate</b> <i>numberOfStrides</i>...",
				org.alice.ide.memberseditor.templates.TemplateFactory.getProcedureInvocationTemplate( skateMethod ).getDragAndDropOperation(),
				"Drop it <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>2</b> from the menu.",
				new edu.cmu.cs.dennisc.tutorial.DragAndDropOperationCompletorValidator() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragAndDropOperation, edu.cmu.cs.dennisc.croquet.Edit edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit createEdit( edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragAndDropOperation ) {
						return new org.alice.ide.codeeditor.InsertStatementEdit(
								runMethod.body.getValue().statements,
								org.alice.ide.codeeditor.InsertStatementEdit.AT_END, 
								org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( 
										org.alice.ide.ast.NodeUtilities.createFieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), iceSkaterField ), 
										skateMethod, 
										new edu.cmu.cs.dennisc.alice.ast.IntegerLiteral( 2 ) 
								)
						);
					}
				}
		);
		tutorial.addSpotlightStepForModel( 
				"Note iceSkater.skate( 2 )",
				"This line tells the <b>iceSkater</b> to skate <b>2</b> strides.",
				tutorial.createFirstInvocationResolver( "skate" )
		);
		
		tutorial.addDialogOpenAndCloseStep( 
				"Run", 
				"Now, try running your program again.<p>The <b>iceSkater</b> should spin and then skate.", 
				"Press the <b>Close</b> button", 
				ide.getRunOperation() 
		);

		tutorial.addMessageStep( 
				"Change order", 
				"We can change the order of the tricks in the <b>iceSkater</b>'s routine."
		);

		tutorial.addDragAndDropStep(
				"Move Do Simple Spin", 
				"Let's drag and drop iceSkater.doSimpleSpin below the line iceSkater.skate.<p><p>Drag iceSkater.doSimpleSpin...",
				tutorial.createFirstInvocationResolver( "doSimpleSpin" ),
				"...and drop it <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				tutorial.createToDoCompletorValidator()
		);

		//run
		//close
		
		tutorial.addMessageStep( 
				"Skate Backwards", 
				"After her spin, let's have the <b>iceSkater</b> skate backwards."
		);
		
		tutorial.addDragAndDropToPopupMenuStep( 
				"Invoke Skate Backwards Procedure",
				"Drag <b>skateBackwards</b> <i>numberOfStrides</i>...",
				org.alice.ide.memberseditor.templates.TemplateFactory.getProcedureInvocationTemplate( skateBackwardsMethod ).getDragAndDropOperation(),
				"Drop it <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <b>1</b> from the menu.",
				new edu.cmu.cs.dennisc.tutorial.DragAndDropOperationCompletorValidator() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragAndDropOperation, edu.cmu.cs.dennisc.croquet.Edit edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit createEdit( edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragAndDropOperation ) {
						return new org.alice.ide.codeeditor.InsertStatementEdit(
								runMethod.body.getValue().statements,
								org.alice.ide.codeeditor.InsertStatementEdit.AT_END, 
								org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( 
										org.alice.ide.ast.NodeUtilities.createFieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), iceSkaterField ), 
										skateBackwardsMethod, 
										new edu.cmu.cs.dennisc.alice.ast.IntegerLiteral( 1 ) 
								)
						);
					}
				}
		);
		tutorial.addSpotlightStepForModel( 
				"Note iceSkater.skateBackwards( 1 )",
				"This line tells the <b>iceSkater</b> to skate backwards <b>1</b> stride.",
				tutorial.createFirstInvocationResolver( "skateBackwards" )
		);

		tutorial.addMessageStep( 
				"Jump",
				"Finally, we'll have her do a jump at the end of her routine."
		);

		tutorial.addDragAndDropStep( 
				"Invoke Jump Procedure",
				"Drag <b>jump</b>...",
				org.alice.ide.memberseditor.templates.TemplateFactory.getProcedureInvocationTemplate( jumpMethod ).getDragAndDropOperation(),
				"Drop it <b>here</b>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				new edu.cmu.cs.dennisc.tutorial.DragAndDropOperationCompletorValidator() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragAndDropOperation, edu.cmu.cs.dennisc.croquet.Edit edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit createEdit( edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragAndDropOperation ) {
						return new org.alice.ide.codeeditor.InsertStatementEdit(
								runMethod.body.getValue().statements,
								org.alice.ide.codeeditor.InsertStatementEdit.AT_END, 
								org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( 
										org.alice.ide.ast.NodeUtilities.createFieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), iceSkaterField ), 
										jumpMethod 
								)
						);
					}
				}
		);
		tutorial.addSpotlightStepForModel( 
				"Note iceSkater.jump()",
				"This line tells the <b>iceSkater</b> to jump.",
				tutorial.createFirstInvocationResolver("jump")
		);

		//run
		//close

		tutorial.addMessageStep( 
				"Free Skate", 
				"<h1><center>TODO</center></h1><p>resarrange however you want."
		);

		//run
		//close
		
		tutorial.addMessageStep( 
				"The End",
				"<h1><center>TODO: restart</center></h1><p>We have done a quick tour of the IDE, learned how to run programs, and change the <b>iceSkater</b>'s routine.  If you are not comfortable with these topics, please <b>restart</b>.  Otherwise, move on to the next chapter."
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
