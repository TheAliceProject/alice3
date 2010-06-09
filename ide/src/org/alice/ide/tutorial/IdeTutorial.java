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
package org.alice.ide.tutorial;

/**
 * @author Dennis Cosgrove
 */
public class IdeTutorial extends edu.cmu.cs.dennisc.tutorial.Tutorial {
	private org.alice.ide.IDE ide;
	public IdeTutorial( org.alice.stageide.StageIDE ide, final int selectedIndex ) {
		super( new edu.cmu.cs.dennisc.croquet.Group[] { edu.cmu.cs.dennisc.alice.Project.GROUP, org.alice.ide.IDE.IDE_GROUP } );
		assert ide != null;
		this.ide = ide;
		this.ide.getFrame().addWindowListener( new java.awt.event.WindowAdapter() {
			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						IdeTutorial.this.setSelectedIndex( selectedIndex );
					}
				} );
			}
		} );
		
	}
	public org.alice.ide.IDE getIDE() {
		return this.ide;
	}

	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneField() {
		return this.getIDE().getSceneField();
	}
	public <F extends edu.cmu.cs.dennisc.alice.ast.AbstractField> F getFieldDeclaredOnSceneType( String name ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = ide.getSceneType();
		return (F)sceneType.getDeclaredField( name );
	}

	public edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava findShortestMethod( Class<?> cls, String methodName ) {
		Class<?> c = cls;
		while( c != null ) {
			for( java.lang.reflect.Method mthd : c.getMethods() ) {
				int modifiers = mthd.getModifiers();
				if( java.lang.reflect.Modifier.isPublic(modifiers) && java.lang.reflect.Modifier.isStatic( modifiers )==false ) {
					if( mthd.getName().equals( methodName ) ) {
						edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava m = edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava.get( mthd );
						return (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava)m.getShortestInChain();
					}
				}
			}
			c = c.getSuperclass();
		}
		return null;
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractMethod findShortestMethod( edu.cmu.cs.dennisc.alice.ast.AbstractField field, String methodName ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > type = field.getValueType();
		while( type.isDeclaredInAlice() ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > typeInAlice = (edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? >)type;
			for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : typeInAlice.methods ) {
				if( method.getName().equals( methodName ) ) {
					return method;
				}
			}
			type = type.getSuperType();
		}
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava)type;
		Class<?> cls = typeInJava.getClassReflectionProxy().getReification();
		return findShortestMethod(cls, methodName);
	}

	public static abstract class CodeTrackableShapeResolver extends edu.cmu.cs.dennisc.tutorial.TrackableShapeResolver {
		protected abstract edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape( org.alice.ide.codeeditor.CodeEditor codeEditor );
		@Override
		protected final edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape() {
			org.alice.ide.codeeditor.CodeEditor codeEditor = org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().getCodeEditorInFocus();
			if( codeEditor != null ) {
				return this.resolveTrackableShape( codeEditor );
			} else {
				return null;
			}
		}
	}

	public static abstract class CodeDragSourceResolver extends edu.cmu.cs.dennisc.tutorial.DragSourceResolver {
		protected abstract edu.cmu.cs.dennisc.croquet.DragSource resolveDragSource( org.alice.ide.codeeditor.CodeEditor codeEditor );
		@Override
		protected final edu.cmu.cs.dennisc.croquet.DragSource resolveDragSource() {
			org.alice.ide.codeeditor.CodeEditor codeEditor = org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().getCodeEditorInFocus();
			if( codeEditor != null ) {
				return this.resolveDragSource( codeEditor );
			} else {
				return null;
			}
		}
	}

	
	public static class StatementListResolver extends CodeTrackableShapeResolver {
		private edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty;
		private int index;
		public StatementListResolver( edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty, int index ) {
			this.statementListProperty = statementListProperty;
			this.index = index;
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape( org.alice.ide.codeeditor.CodeEditor codeEditor ) {
			return codeEditor.getTrackableShapeAtIndexOf( this.statementListProperty, this.index );
		}
	}

	public static class StatementResolver extends CodeDragSourceResolver {
		private edu.cmu.cs.dennisc.alice.ast.Statement statement;
		public StatementResolver( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
			this.statement = statement;
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.DragSource resolveDragSource(org.alice.ide.codeeditor.CodeEditor codeEditor) {
			return codeEditor.getDragComponent( this.statement );
		}
	}

	public static class ProcedureInvocationResolver extends CodeDragSourceResolver {
		private edu.cmu.cs.dennisc.alice.ast.AbstractMethod method;
		private int index;
		public ProcedureInvocationResolver( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, int index ) {
			this.method = method;
			this.index = index;
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.DragSource resolveDragSource(org.alice.ide.codeeditor.CodeEditor codeEditor) {
			edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)codeEditor.getCode();
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation >( edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class ) {
				@Override
				protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation ) {
					return methodInvocation.method.getValue() == method;
				}
			};
			edu.cmu.cs.dennisc.alice.ast.BlockStatement body = code.getBodyProperty().getValue();
			body.crawl( crawler, false );
			java.util.List<edu.cmu.cs.dennisc.alice.ast.MethodInvocation> list = crawler.getList();
			if( list.size() > this.index ) {
				edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = list.get( this.index );
				edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)methodInvocation.getParent();
				return codeEditor.getDragComponent(statement);
			} else {
				return null;
			}
		}
	}
	public static class StatementAssignableToResolver extends CodeDragSourceResolver {
		private Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls;
		private int index;
		public StatementAssignableToResolver( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls, int index ) {
			this.cls = cls;
			this.index = index;
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.DragSource resolveDragSource(org.alice.ide.codeeditor.CodeEditor codeEditor) {
			edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)codeEditor.getCode();
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler( this.cls ) {
				@Override
				protected boolean isAcceptable( Object statment ) {
					return true;
				}
			};
			edu.cmu.cs.dennisc.alice.ast.BlockStatement body = code.getBodyProperty().getValue();
			body.crawl( crawler, false );
			java.util.List<edu.cmu.cs.dennisc.alice.ast.Statement> list = crawler.getList();
			if( list.size() > this.index ) {
				edu.cmu.cs.dennisc.alice.ast.Statement statement = list.get( this.index );
				return codeEditor.getDragComponent(statement);
			} else {
				return null;
			}
		}
	}
//	public edu.cmu.cs.dennisc.croquet.DragSource createStatementResolver( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
//		return new StatementResolver(statement);
//	}

	private edu.cmu.cs.dennisc.croquet.TrackableShape createStatementListResolver( edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty, int index ) {
		return new StatementListResolver(statementListProperty, index);
	}
	private edu.cmu.cs.dennisc.croquet.TrackableShape createBeginingOfStatementListResolver( edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty ) {
		return this.createStatementListResolver(statementListProperty, 0);
	}
	private edu.cmu.cs.dennisc.croquet.TrackableShape createEndOfStatementListResolver( edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty ) {
		return this.createStatementListResolver(statementListProperty, Short.MAX_VALUE);
	}

	public edu.cmu.cs.dennisc.croquet.TrackableShape createStatementListResolver( edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody statementWithBody, int index ) {
		return this.createStatementListResolver(statementWithBody.body.getValue().statements, index);
	}
	public edu.cmu.cs.dennisc.croquet.TrackableShape createBeginingOfStatementListResolver( edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody statementWithBody ) {
		return this.createBeginingOfStatementListResolver(statementWithBody.body.getValue().statements);
	}
	public edu.cmu.cs.dennisc.croquet.TrackableShape createEndOfStatementListResolver( edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody statementWithBody ) {
		return this.createEndOfStatementListResolver(statementWithBody.body.getValue().statements);
	}

	public edu.cmu.cs.dennisc.croquet.TrackableShape createStatementListResolver( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method, int index ) {
		return this.createStatementListResolver(method.body.getValue().statements, index);
	}
	public edu.cmu.cs.dennisc.croquet.TrackableShape createBeginingOfStatementListResolver( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		return this.createBeginingOfStatementListResolver(method.body.getValue().statements);
	}
	public edu.cmu.cs.dennisc.croquet.TrackableShape createEndOfStatementListResolver( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		return this.createEndOfStatementListResolver(method.body.getValue().statements);
	}

	public edu.cmu.cs.dennisc.croquet.DragSource createStatementAssignableToResolver( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls, int index ) {
		return new StatementAssignableToResolver( cls, index );
	}
	public edu.cmu.cs.dennisc.croquet.DragSource createFirstStatementAssignableToResolver( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls ) {
		return this.createStatementAssignableToResolver( cls, 0 );
	}
	public edu.cmu.cs.dennisc.croquet.DragSource createLastStatementAssignableToResolver( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls ) {
		return this.createStatementAssignableToResolver( cls, Short.MAX_VALUE );
	}
	
	
	
	public edu.cmu.cs.dennisc.croquet.DragSource createInvocationResolver( final edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, int index ) {
		return new ProcedureInvocationResolver(method, index);
	}
	public edu.cmu.cs.dennisc.croquet.DragSource createFirstInvocationResolver( final edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		return this.createInvocationResolver(method, 0);
	}
	public edu.cmu.cs.dennisc.croquet.DragSource createLastInvocationResolver( final edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		return this.createInvocationResolver(method, Short.MAX_VALUE);
	}
	

//	private final static java.util.UUID PROCEDURES_TAB_ID = java.util.UUID.fromString( "2731d704-1f80-444e-a610-e6e5866c0b9a" );
//	private final static java.util.UUID FUNCTIONS_TAB_ID = java.util.UUID.fromString( "0f5d1f93-fc67-4109-9aff-0e7b232f201c" );
//	private final static java.util.UUID FIELDS_TAB_ID = java.util.UUID.fromString( "6cb9c5a1-dc60-48e7-9a52-534009a093b8" );
	
	public edu.cmu.cs.dennisc.croquet.PredeterminedTab getProceduresTab() {
		return this.getIDE().getMembersEditor().getTabbedPaneSelectionState().getItemAt( 0 );
	}

	public edu.cmu.cs.dennisc.croquet.PredeterminedTab getFunctionsTab() {
		return this.getIDE().getMembersEditor().getTabbedPaneSelectionState().getItemAt( 1 );
	}

	public edu.cmu.cs.dennisc.croquet.PredeterminedTab getFieldsTab() {
		return this.getIDE().getMembersEditor().getTabbedPaneSelectionState().getItemAt( 2 );
	}
	
	public static class TemplateDragSourceResolver extends edu.cmu.cs.dennisc.tutorial.DragSourceResolver {
		private Class< ? extends edu.cmu.cs.dennisc.croquet.DragSource > cls;
		public TemplateDragSourceResolver( Class< ? extends edu.cmu.cs.dennisc.croquet.DragSource > cls ) {
			assert edu.cmu.cs.dennisc.croquet.Component.class.isAssignableFrom( cls );
			this.cls = cls;
		}
		@Override
		protected final edu.cmu.cs.dennisc.croquet.DragSource resolveDragSource() {
			org.alice.ide.ubiquitouspane.UbiquitousPane ubiquitousPane = org.alice.ide.IDE.getSingleton().getUbiquitousPane();
			return this.cls.cast( edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findFirstMatch( ubiquitousPane, (Class<edu.cmu.cs.dennisc.croquet.Component>)this.cls ) );
		}
	}
	
	
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createDoInOrderTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.DoInOrderTemplate.class );
	}
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createCountLoopTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.CountLoopTemplate.class );
	}
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createWhileLoopTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.WhileLoopTemplate.class );
	}
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createForEachInArrayLoopTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.ForEachInArrayLoopTemplate.class );
	}
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createIfElseTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.ConditionalStatementTemplate.class );
	}
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createDoTogetherTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.DoTogetherTemplate.class );
	}
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createEachInArrayTogetherTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.EachInArrayTogetherTemplate.class );
	}
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createDoInThreadTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.DoInThreadTemplate.class );
	}
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createDeclareLocalTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.CommentTemplate.class );
	}
	public edu.cmu.cs.dennisc.tutorial.DragSourceResolver createCommentTemplateResolver() {
		return new TemplateDragSourceResolver( org.alice.ide.ubiquitouspane.templates.CommentTemplate.class );
	}

	@Deprecated
	public edu.cmu.cs.dennisc.tutorial.CompletorValidator createToDoCompletorValidator() {
		return new edu.cmu.cs.dennisc.tutorial.CompletorValidator() {
			public Result checkValidity(edu.cmu.cs.dennisc.croquet.Edit edit) {
				return Result.TO_BE_HONEST_I_DIDNT_EVEN_CHECK;
			}
			public edu.cmu.cs.dennisc.croquet.Edit getEdit() {
				return new org.alice.ide.ToDoEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
					}
					@Override
					public void undo() {
					}
					@Override
					protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
						return rv;
					}
				};
			}
		};
	}
}
