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
		//ide.getEmphasizingClassesState().setValue( false );
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().setValue( false ); 

		tutorial.addMessageStep( 
				"Title", 
				"<b><center>Welcome To The Tutorial</center></b><p>This tutorial will introduce you to the basics.<p>" 
		);
		
		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag move action",
				"Click and drag <strong>move</strong> action.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <strong>here</strong>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <strong>UP</strong> from the menu, and then 1.0.",
				new edu.cmu.cs.dennisc.tutorial.DragAndDropOperationCompletorValidator() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropOperation, edu.cmu.cs.dennisc.croquet.Edit<?> edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit<?> createEdit( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropOperation, edu.cmu.cs.dennisc.croquet.TrackableShape dropShape ) {
						org.alice.ide.codeeditor.CodeEditor.StatementListIndexTrackableShape statementListIndexTrackableShape = (org.alice.ide.codeeditor.CodeEditor.StatementListIndexTrackableShape)dropShape;
						edu.cmu.cs.dennisc.croquet.DragComponent dragComponent = (edu.cmu.cs.dennisc.croquet.DragComponent)dragAndDropOperation.getFirstComponent();
						
						
						org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate = (org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate)dragComponent;

						edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression = org.alice.ide.IDE.getSingleton().createInstanceExpression();
						edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = procedureInvocationTemplate.getMethod();
						edu.cmu.cs.dennisc.alice.ast.Statement statement = org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement(
								instanceExpression, 
								method,
								org.alice.ide.ast.NodeUtilities.createStaticFieldAccess( org.alice.apis.moveandturn.MoveDirection.class, "UP" ),
								new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 1.0 )
						);
						return new org.alice.ide.codeeditor.InsertStatementEdit(
								statementListIndexTrackableShape.getStatementListProperty(),
								statementListIndexTrackableShape.getIndex(),
								statement
						);
					}
				}
		);
//		final double desiredDuration = 0.5;
//		tutorial.addPopupMenuStep( 
//				"More (Duration)",
//				"Click <b>more...</b> and select 0.5",
//				tutorial.createFirstInvocationMoreResolver( "move" ),
//				"text",
//				new edu.cmu.cs.dennisc.tutorial.PopupMenuOperationCompletorValidator() {
//					public Result checkValidity(edu.cmu.cs.dennisc.croquet.PopupMenuOperation operation, edu.cmu.cs.dennisc.croquet.Edit<?> edit) {
//						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
//					}
//					public edu.cmu.cs.dennisc.croquet.Edit<?> createEdit(edu.cmu.cs.dennisc.croquet.PopupMenuOperation operation) {
//						org.alice.ide.croquet.models.ast.FillInMorePopupMenuOperation fillInMoreOperation = (org.alice.ide.croquet.models.ast.FillInMorePopupMenuOperation)operation;
//						org.alice.ide.croquet.edits.ast.FillInMoreEdit rv = new org.alice.ide.croquet.edits.ast.FillInMoreEdit( new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( desiredDuration ) );
//						rv.EPIC_HACK_setModel( fillInMoreOperation );
//						return rv;
//					}
//				}
//		);
//		
//		final String fieldName = "grassyGround";
//		tutorial.addPopupMenuStep( 
//				"More (As Seen By)",
//				"Click <b>more...</b> and select <b>" + fieldName + "</b>",
//				tutorial.createFirstInvocationMoreResolver( "move" ),
//				"text",
//				new edu.cmu.cs.dennisc.tutorial.PopupMenuOperationCompletorValidator() {
//					public Result checkValidity(edu.cmu.cs.dennisc.croquet.PopupMenuOperation operation, edu.cmu.cs.dennisc.croquet.Edit<?> edit) {
//						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
//					}
//					public edu.cmu.cs.dennisc.croquet.Edit<?> createEdit(edu.cmu.cs.dennisc.croquet.PopupMenuOperation operation) {
//						org.alice.ide.croquet.models.ast.FillInMorePopupMenuOperation fillInMoreOperation = (org.alice.ide.croquet.models.ast.FillInMorePopupMenuOperation)operation;
//						
//						edu.cmu.cs.dennisc.alice.ast.AbstractField field = ide.getSceneType().getDeclaredField( fieldName );
//						edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess(
//								new edu.cmu.cs.dennisc.alice.ast.ThisExpression(),
//								field
//						);
//						org.alice.ide.croquet.edits.ast.FillInMoreEdit rv = new org.alice.ide.croquet.edits.ast.FillInMoreEdit( fieldAccess );
//						rv.EPIC_HACK_setModel( fillInMoreOperation );
//						return rv;
//					}
//				}
//		);

		//formerly known as EPIC_HACK_addDeclareProcedureDialogOpenAndCommitStep
		final String requiredProcedureName = "skate";
		tutorial.addInputDialogOpenAndCommitStep( 
				"Declare Procedure", 
				"Declare procedure...", 
				"Type <b>" + requiredProcedureName + "</b> and press <i>Ok</i>",
				tutorial.createDeclareProcedureOperationResolver(), 
				new edu.cmu.cs.dennisc.tutorial.InputDialogOperationCompletorValidatorOkButtonDisabler<org.alice.ide.declarationpanes.CreateProcedurePane>() {
					public Result checkValidity(edu.cmu.cs.dennisc.croquet.InputDialogOperation inputDialogOperation, edu.cmu.cs.dennisc.croquet.Edit edit) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit<?> createEdit(edu.cmu.cs.dennisc.croquet.InputDialogOperation inputDialogOperation) {
						org.alice.ide.croquet.models.ast.DeclareProcedureOperation declareProcedureOperation = (org.alice.ide.croquet.models.ast.DeclareProcedureOperation)inputDialogOperation;
						edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> declaringType = declareProcedureOperation.getDeclaringType();
						edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = org.alice.ide.ast.NodeUtilities.createProcedure( requiredProcedureName );
						return new org.alice.ide.croquet.edits.ast.DeclareMethodEdit(declaringType, method);
					}
					private String getExplanationIfOkButtonShouldBeDisabled( String name ) {
						if( requiredProcedureName.equalsIgnoreCase( name ) ) {
							return null;
						} else {
							return "<html>Please enter in the name <b>" + requiredProcedureName + "</b> and press <b>Ok</b> button.</html>";
						}
					}
					public String getExplanationIfOkButtonShouldBeDisabled(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateProcedurePane> context) {
						org.alice.ide.declarationpanes.CreateProcedurePane createProcedurePane = context.getMainPanel();
						return this.getExplanationIfOkButtonShouldBeDisabled( createProcedurePane.getDeclarationName() );
					}
				}
		);

		final int count = 2;
		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag Count Loop",
				"Drag <b>Count Loop</b>.",
				tutorial.createCountLoopTemplateResolver(),
				"Drop <b>here</b>.",
				tutorial.createBeginingOfCurrentMethodBodyStatementListResolver(),
				"Select <b>" + count + "</b>.",
				new edu.cmu.cs.dennisc.tutorial.DragAndDropOperationCompletorValidator() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropOperation, edu.cmu.cs.dennisc.croquet.Edit edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit createEdit( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropOperation, edu.cmu.cs.dennisc.croquet.TrackableShape dropShape ) {
						org.alice.ide.codeeditor.CodeEditor.StatementListIndexTrackableShape statementListIndexTrackableShape = (org.alice.ide.codeeditor.CodeEditor.StatementListIndexTrackableShape)dropShape;
						edu.cmu.cs.dennisc.alice.ast.Statement statement = org.alice.ide.ast.NodeUtilities.createCountLoop( new edu.cmu.cs.dennisc.alice.ast.IntegerLiteral( count ) ); 
						return new org.alice.ide.codeeditor.InsertStatementEdit(
								statementListIndexTrackableShape.getStatementListProperty(),
								statementListIndexTrackableShape.getIndex(),
								statement
						);
					}
				}
		);

		tutorial.addDragAndDropToPopupMenuStep( 
				"Drag move action",
				"Click and drag <strong>move</strong> action.",
				tutorial.createProcedureInvocationTemplateResolver( "move" ),
				"Drop <strong>here</strong>.",
				tutorial.createEndOfStatementListResolver( 
						tutorial.createFirstStatementListPropertyResolver( edu.cmu.cs.dennisc.alice.ast.CountLoop.class ) 
				),
				"Select <strong>UP</strong> from the menu, and then 1.0.",
				new edu.cmu.cs.dennisc.tutorial.DragAndDropOperationCompletorValidator() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropOperation, edu.cmu.cs.dennisc.croquet.Edit<?> edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit<?> createEdit( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropOperation, edu.cmu.cs.dennisc.croquet.TrackableShape dropShape ) {
						org.alice.ide.codeeditor.CodeEditor.StatementListIndexTrackableShape statementListIndexTrackableShape = (org.alice.ide.codeeditor.CodeEditor.StatementListIndexTrackableShape)dropShape;
						edu.cmu.cs.dennisc.croquet.DragComponent dragComponent = (edu.cmu.cs.dennisc.croquet.DragComponent)dragAndDropOperation.getFirstComponent();
						
						
						org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate = (org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate)dragComponent;

						edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression = org.alice.ide.IDE.getSingleton().createInstanceExpression();
						edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = procedureInvocationTemplate.getMethod();
						edu.cmu.cs.dennisc.alice.ast.ExpressionStatement statement = org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement(
								instanceExpression, 
								method,
								org.alice.ide.ast.NodeUtilities.createStaticFieldAccess( org.alice.apis.moveandturn.MoveDirection.class, "UP" ),
								new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 1.0 )
						);
						
						assert statement.expression.getValue() instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation;
						edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)statement.expression.getValue();
						if( methodInvocation.isValid() ) {
							//pass
						} else {
							edu.cmu.cs.dennisc.print.PrintUtilities.println( "createEdit", methodInvocation );
						}
						return new org.alice.ide.codeeditor.InsertStatementEdit(
								statementListIndexTrackableShape.getStatementListProperty(),
								statementListIndexTrackableShape.getIndex(),
								statement
						);
					}
				}
		);
		

		tutorial.addSpotlightStepForModel( 
				"Note Count Loop", 
				"note count loop", 
				tutorial.createFirstStatementAssignableToResolver( edu.cmu.cs.dennisc.alice.ast.CountLoop.class ) 
		);

		tutorial.addSpotlightStepForModel( 
				"Note Count Loop Count", 
				"note count is 2", 
				tutorial.createFirstCountLoopCountResolver() 
		);

		//formerly known as EPIC_HACK_addForEachInArrayLoopDragAndDropToPopupMenuToInputDialogStep
		final String requiredTypeName = "PolygonalModel";
		final String[] desiredFieldNames = { "grassyGround", "grassyGround" };
		tutorial.addDragAndDropToPopupMenuToInputDialogStep( 
				"Drag For Each In Order",
				"Drag <strong>For Each In Order</strong>.",
				tutorial.createForEachInArrayLoopTemplateResolver(),
				"Drop <strong>here</strong>.",
				tutorial.createEndOfCurrentMethodBodyStatementListResolver(),
				"Select <strong>Other Array...</strong>",
				"Select <strong>" + requiredTypeName + "</strong>, add <strong>" + desiredFieldNames[ 0 ] + "</strong> and <strong>" + desiredFieldNames[ 1 ] + "</strong>, and press <strong>OK</strong>",
				new edu.cmu.cs.dennisc.tutorial.DragAndDropOperationCompletorValidatorOkButtonDisabler<org.alice.ide.cascade.customfillin.CustomInputPane<?>>() {
					public edu.cmu.cs.dennisc.tutorial.Validator.Result checkValidity( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropOperation, edu.cmu.cs.dennisc.croquet.Edit edit ) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit createEdit( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragAndDropOperation, edu.cmu.cs.dennisc.croquet.TrackableShape dropShape ) {
						return tutorial.createToDoEdit();
					}
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
					private String getExplanationIfOkButtonShouldBeDisabled(org.alice.ide.choosers.ValueChooser<?> valueChooser) {
						final String COMPLETE_INSTRUCTIONS = "<html>Select value type <strong>" + requiredTypeName + "</strong> and add <strong>" + desiredFieldNames[ 0 ] + "</strong> and <strong>" + desiredFieldNames[ 1 ] + "</strong>.</html>";
						if (valueChooser instanceof org.alice.ide.choosers.ArrayChooser) {
							org.alice.ide.choosers.ArrayChooser arrayChooser = (org.alice.ide.choosers.ArrayChooser) valueChooser;
							edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation arrayInstanceCreation = arrayChooser.getValue();
							if( arrayInstanceCreation != null ) {
								edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> componentType = arrayChooser.EPIC_HACK_getArrayComponentType();
								if( componentType != null ) {
									boolean isComponentTypeCorrect = requiredTypeName.equals( componentType.getName() );
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
										}
									}
								}
							}
							return COMPLETE_INSTRUCTIONS;
						} else {
							return "this should not happen.  you have found a bug.  ask for help.";
						}
					}
					public String getExplanationIfOkButtonShouldBeDisabled(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.cascade.customfillin.CustomInputPane<?>> context) {
						org.alice.ide.cascade.customfillin.CustomInputPane<?> customInputPane = context.getMainPanel();
						return this.getExplanationIfOkButtonShouldBeDisabled( customInputPane.getValueChooser() );
					}
				}
		);

		//formerly known as EPIC_HACK_addDeclareMethodParameterDialogOpenAndCommitStep
		final String requiredParameterName = "howHigh";
		tutorial.addInputDialogOpenAndCommitStep( 
				"Declare Parameter", 
				"Declare parameter...", 
				"1) Select <i>value type:</i> <b>Double</b><p>2) Enter <i>name:</i> <b>" + requiredParameterName + "</b><p>and press the <b>OK</b> button.",
				tutorial.createDeclareMethodParameterOperationResolver(),
				new edu.cmu.cs.dennisc.tutorial.InputDialogOperationCompletorValidatorOkButtonDisabler<org.alice.ide.declarationpanes.CreateMethodParameterPane>() {
					public Result checkValidity(edu.cmu.cs.dennisc.croquet.InputDialogOperation inputDialogOperation, edu.cmu.cs.dennisc.croquet.Edit edit) {
						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
					}
					public edu.cmu.cs.dennisc.croquet.Edit<?> createEdit(edu.cmu.cs.dennisc.croquet.InputDialogOperation inputDialogOperation) {
						return tutorial.createToDoEdit();
					}
					private String getExplanationIfOkButtonShouldBeDisabled( String name, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > valueType ) {
						if( requiredParameterName.equalsIgnoreCase( name ) ) {
							if( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE == valueType ) {
								return null;
							} else {
								return "<html>Please select <b>RealNumber</b> value type, and press <b>Ok</b>.</html>";
							}
						} else {
							if( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE == valueType ) {
								return "<html>Please enter in name <b>" + requiredParameterName + "</b>, and press <b>Ok</b>.</html>";
							} else {
								return "<html>Please select <b>RealNumber</b> value type, enter in name <b>" + requiredParameterName + "</b>, and press <b>OK</b>.</html>";
							}
						}
					}
					public String getExplanationIfOkButtonShouldBeDisabled(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateMethodParameterPane> context) {
						org.alice.ide.declarationpanes.CreateMethodParameterPane createMethodParameterPane = context.getMainPanel();
						return this.getExplanationIfOkButtonShouldBeDisabled( createMethodParameterPane.getDeclarationName(), createMethodParameterPane.getValueType() );
					}
				}
		);
		

		tutorial.addSelectTabStep( 
				"Select Run", 
				"Select run tab.", 
				org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance(),
				tutorial.createCodeResolver( "scene", "run" )
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

//		tutorial.addDeclareProcedureDialogOpenAndCommitStep( 
//				"Declare Procedure Foo", 
//				"Declare procedure...", 
//				"Type <b>foo</b> and press <i>Ok</i>",
//				new edu.cmu.cs.dennisc.tutorial.InputDialogOperationCompletorValidatorOkButtonDisabler() {
//					public Result checkValidity(edu.cmu.cs.dennisc.croquet.InputDialogOperation inputDialogOperation, edu.cmu.cs.dennisc.croquet.Edit edit) {
//						return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
//					}
//					public edu.cmu.cs.dennisc.croquet.Edit<?> createEdit(edu.cmu.cs.dennisc.croquet.InputDialogOperation inputDialogOperation) {
//						return tutorial.createToDoEdit();
//					}
//					private String getExplanationIfOkButtonShouldBeDisabled( String name ) {
//						if( "foo".equalsIgnoreCase( name ) ) {
//							return null;
//						} else {
//							return "<html>Please enter in the name <b>foo</b> and press <b>Ok</b> button.</html>";
//						}
//					}
//					public String getExplanationIfOkButtonShouldBeDisabled(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext context) {
//						return null;
//					}
//				}
//		);

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

		tutorial.addPopupMenuStep( 
				"More",
				"Click <b>more...</b>",
				tutorial.createFirstInvocationMoreResolver( "move" ),
				"text",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addPopupMenuStep( 
				"More",
				"Click <b>more...</b>",
				tutorial.createLastInvocationMoreResolver( "move" ),
				"text",
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
				org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance(),
				tutorial.createCodeResolver( "scene", "run" )
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
						tutorial.createFirstStatementListPropertyResolver( edu.cmu.cs.dennisc.alice.ast.DoTogether.class )
				),
				"Select <b>FORWARD</b> and <b>1.0</b> from the menus.",
				tutorial.createToDoCompletorValidator()
		);
		tutorial.addPopupMenuStep( 
				"More",
				"Click <b>more...</b>",
				tutorial.createLastInvocationMoreResolver( "move" ),
				"text",
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


//		tutorial.addInputDialogOpenAndCommitStep( 
//				"Declare Procedure Foo", 
//				"Declare a procedure...", 
//				"Type <b>foo</b> and press <i>Ok</i>",
//				tutorial.createDeclareProcedureOperationResolver(),
//				tutorial.createToDoCompletorValidator()
//		);
//
//		tutorial.addSpotlightTabTitleStep(  
//				"Note Foo Tab", 
//				"Note the foo folder tab.", 
//				org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance(),
//				tutorial.createCurrentCodeResolver()
//		);
//
//		tutorial.addInputDialogOpenAndCommitStep( 
//				"Declare Parameter How High", 
//				"Declare parameter....", 
//				"Type <b>howHigh</b> and select <b>Double</b> and press <i>Ok</i>",
//				tutorial.createDeclareMethodParameterOperationResolver(),
//				tutorial.createToDoCompletorValidator()				
//		);
		
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

		tutorial.addPopupMenuStep(
				"Change Instance", 
				"change instance to <b>sunLight</b>",
				tutorial.createFirstInvocationInstanceResolver( "move" ),
				"text",
				tutorial.createToDoCompletorValidator()
		);
		

		tutorial.addPopupMenuStep(
				"Change If Condition", 
				"change if condition to <b>false</b>",
				tutorial.createFirstIfElseStatementConditionResolver(),
				"select <b>false</b>",
				tutorial.createToDoCompletorValidator()
		);

		tutorial.addSelectTabStep( 
				"Select Functions Tab", 
				"Select the <b>Functions</b> tab.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getFunctionsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Functions Tab", 
				"Now the functions are now displayed.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
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

		tutorial.addPopupMenuStep(
				"Change Argument", 
				"change threshold argument to <b>1.0</b>",
				tutorial.createInvocationArgumentResolver( "isWithinThresholdOf", 0, 0 ),
				"text",
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
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"Now the procedures are now displayed.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getProceduresTab()
		);

		
		tutorial.addListSelectionStep(
				"Select Scene", 
				"Select the <b>scene</b>.<i>",
				org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance(),
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
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"Now the procedures are now displayed.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getProceduresTab()
		);

//		tutorial.addActionStep( 
//				"Declare Procedure Hop", 
//				"Declare a procedure named <b>hop</b>.", 
//				tutorial.createDeclareProcedureOperationResolver(),
//				tutorial.createToDoCompletorValidator()
//		);

		tutorial.addSpotlightTabTitleStep(  
				"Note Hop Tab", 
				"Note the folder tab.", 
				org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance(),
				tutorial.createCurrentCodeResolver()
		);

		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Properies Tab", 
				"Note the area to implement hop.", 
				org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance(),
				tutorial.createCurrentCodeResolver()
		);

		
		
		tutorial.addSelectTabStep( 
				"Procedures Tab", 
				"Select the <b>Procedures</b> tab.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"Now the procedures are now displayed.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
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
				org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance()
		);
		tutorial.addSpotlightStep( 
				"Instance Details", 
				"The currently selected instance methods and fields are displayed here.<p>Right now, it is showing the details of the <b>camera</b>.", 
				ide.getMembersEditor() 
		);

		tutorial.addSpotlightStepForModel( 
				"Instance Details", 
				"This is the code editor.", 
				org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance() 
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
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getFunctionsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Functions Tab", 
				"Now the functions are now displayed.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getFunctionsTab()
		);
		tutorial.addSelectTabStep( 
				"Properies Tab", 
				"Select the <b>Properies</b> tab.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getFieldsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Properies Tab", 
				"Now the properties are now displayed.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getFieldsTab()
		);
		tutorial.addSelectTabStep( 
				"Procedures Tab", 
				"Select the <b>Procedures</b> tab.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"Now the procedures are now displayed.", 
				org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance(),
				tutorial.getProceduresTab()
		);

		tutorial.addBooleanStateStep( 
				"Edit Scene", 
				"Press the <b>Edit Scene</b> button", 
				org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance(),
				true
		);
		tutorial.addMessageStep( 
				"Note Edit Scene",
				"Note you are now editing the scene." 
		);
		tutorial.addBooleanStateStep( 
				"Edit Code", 
				"Press the <b>Edit Code</b> button", 
				org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance(),
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
