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
		final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( ide, 4 );
		final org.alice.ide.memberseditor.MembersEditor membersEditor = ide.getMembersEditor();
		final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = tutorial.getSceneField();
		final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice cameraField = tutorial.getFieldDeclaredOnSceneType( "camera" );
		final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice iceSkaterField = tutorial.getFieldDeclaredOnSceneType( "iceSkater" );
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice runMethod = ide.getSceneType().getDeclaredMethod( "run" );

		final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice iceSkaterType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)iceSkaterField.valueType.getValue();
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
				ide.getFieldSelectionState().setValue( cameraField );
			}
		} );
		
		tutorial.addMessageStep( 
				"Welcome", 
				"<html><b><center>Welcome To The Tutorial</center></b><p>This tutorial will introduce you to the basics.<p></html>" 
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

		tutorial.addMessageStep( 
				"All is well", 
				"<html>Don't worry if you don't fully understand all of the areas of the interface.  We'll be going into more detail over the course of the tutorial.</html>" 
		);
		
		tutorial.addMessageStep( 
				"Routine", 
				"<html>In this tutorial, we're going to make the <b>iceSkater</b> perform a routine.</html>"
		);

		tutorial.addSpotlightStep( 
				"Prepare To Skate", 
				"<html>Right now, her routine is pretty simple.<p>First she <b>prepares to skate</b>...</html>",
//				tutorial.createStatementResolver( runMethod.body.getValue().statements.get( 0 ) )
				tutorial.createInvocationResolver(prepareToSkateMethod, 0) 
				
		);
		tutorial.addSpotlightStep( 
				"Do Simple Spin", 
				"<html>Second, she <b>does a simple spin</b>.</html>",
				//tutorial.createStatementResolver( runMethod.body.getValue().statements.get( 1 ) ) 
				tutorial.createInvocationResolver(doSimpleSpinMethod, 0) 
		);
		
		tutorial.addDialogOpenStep( 
				"Run", 
				"<html>Press the <b>Run</b> button</html>", 
				ide.getRunOperation() 
		);
		tutorial.addDialogCloseStep( 
				"Close", 
				"<html>Press the <b>Close</b> button</html>", 
				ide.getRunOperation() 
		);

		tutorial.addMessageStep( 
				"More Moves", 
				"<html>Now, let's add some more moves to the <b>iceSkater</b>'s routine.</html>"
		);

		tutorial.addMessageStep( 
				"Ice Skater Details", 
				"<html>First, we'll need to find out what the <b>iceSkater</b> can do.</html>"
		);
		
		tutorial.addItemSelectionStep(
				"Select Ice Skater", 
				"<html>By selecting an instance we can see what procedures it knows how to do.<p><p><i>Select the <b>iceSkater</b>.<i></html>",
				ide.getFieldSelectionState(),
				iceSkaterField
		);
		
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Ice Skater Details", 
				"<html>These are all of the procedures that the <b>iceSkater</b> knows how to do.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				tutorial.getFunctionsTab()
		);

		tutorial.addMessageStep( 
				"Skate", 
				"<html>Let's have the <b>iceSkater</b> skate <b>2</b> strides after she finishes her spin.</html>"
		);

		tutorial.addDragAndDropStep( 
				"Invoke Skate Procedure",
				"<html>Drag <b>skate</b> <i>numberOfStrides</i>...</html>",
				org.alice.ide.memberseditor.TemplateFactory.getProcedureInvocationTemplate( skateMethod ).getDragAndDropOperation(),
				"<html>Drop it <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				"<html>Select <b>2</b> from the menu.</html>",
				new edu.cmu.cs.dennisc.tutorial.CompletorValidator() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.Edit edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit getEdit() {
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
		tutorial.addSpotlightStep( 
				"Note iceSkater.skate( 2 )",
				"<html>This line tells the <b>iceSkater</b> to skate <b>2</b> strides.</html>",
				tutorial.createInvocationResolver(skateMethod, 0)
		);
		
		tutorial.addDialogOpenStep( 
				"Run", 
				"<html>Now, try running your program again.<p>The <b>iceSkater</b> should spin and then skate.</html>", 
				ide.getRunOperation() 
		);
		tutorial.addDialogCloseStep( 
				"Run", 
				"<html>Press the <b>Close</b> button</html>", 
				ide.getRunOperation() 
		);

		tutorial.addMessageStep( 
				"Change order", 
				"<html>We can change the order of the tricks in the <b>iceSkater</b>'s routine.</html>"
		);

		tutorial.addDragAndDropStep(
				"Move Do Simple Spin", 
				"<html>Let's drag and drop iceSkater.doSimpleSpin below the line iceSkater.skate.<p><p>Drag iceSkater.doSimpleSpin...</html>",
				tutorial.createInvocationResolver(doSimpleSpinMethod, 0),
				"<html>...and drop it <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				tutorial.createToDoCompletorValidator()
		);

		//run
		//close
		
		tutorial.addMessageStep( 
				"Skate Backwards", 
				"<html>After her spin, let's have the <b>iceSkater</b> skate backwards.</html>"
		);
		
		tutorial.addDragAndDropStep( 
				"Invoke Skate Backwards Procedure",
				"<html>Drag <b>skateBackwards</b> <i>numberOfStrides</i>...</html>",
				org.alice.ide.memberseditor.TemplateFactory.getProcedureInvocationTemplate( skateBackwardsMethod ).getDragAndDropOperation(),
				"<html>Drop it <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				"<html>Select <b>1</b> from the menu.</html>",
				new edu.cmu.cs.dennisc.tutorial.CompletorValidator() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.Edit edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit getEdit() {
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
		tutorial.addSpotlightStep( 
				"Note iceSkater.skateBackwards( 1 )",
				"<html>This line tells the <b>iceSkater</b> to skate backwards <b>1</b> stride.</html>",
				tutorial.createInvocationResolver(skateBackwardsMethod, 0)
		);

		tutorial.addMessageStep( 
				"Jump",
				"<html>Finally, we'll have her do a jump at the end of her routine.</html>"
		);

		tutorial.addDragAndDropStep( 
				"Invoke Jump Procedure",
				"<html>Drag <b>jump</b>...</html>",
				org.alice.ide.memberseditor.TemplateFactory.getProcedureInvocationTemplate( jumpMethod ).getDragAndDropOperation(),
				"<html>Drop it <b>here</b>.</html>",
				tutorial.createEndOfStatementListResolver( runMethod ),
				new edu.cmu.cs.dennisc.tutorial.CompletorValidator() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.Edit edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit getEdit() {
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
		tutorial.addSpotlightStep( 
				"Note iceSkater.jump()",
				"<html>This line tells the <b>iceSkater</b> to jump.</html>",
				tutorial.createInvocationResolver(jumpMethod, 0)
		);

		//run
		//close

		tutorial.addMessageStep( 
				"Free Skate", 
				"<html><h1><center>TODO</center></h1><p>resarrange however you want.</html>"
		);

		//run
		//close
		
		tutorial.addMessageStep( 
				"The End",
				"<html><h1><center>TODO: restart</center></h1><p>We have done a quick tour of the IDE, learned how to run programs, and change the <b>iceSkater</b>'s routine.  If you are not comfortable with these topics, please <b>restart</b>.  Otherwise, move on to the next chapter.</html>"
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
