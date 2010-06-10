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

import edu.cmu.cs.dennisc.croquet.Resolver;

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

	private static edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava findShortestMethod( Class<?> cls, String methodName ) {
		Class<?> c = cls;
		while( c != null ) {
			for( java.lang.reflect.Method mthd : c.getMethods() ) {
				int modifiers = mthd.getModifiers();
				if( java.lang.reflect.Modifier.isPublic(modifiers) && java.lang.reflect.Modifier.isStatic( modifiers )==false ) {
					if( mthd.getName().equals( methodName ) ) {
						edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava m = edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava.get( mthd );
						edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava rv = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava)m.getShortestInChain();
						assert rv != null : mthd;
						return rv;
					}
				}
			}
			c = c.getSuperclass();
		}
		return null;
	}
	private static edu.cmu.cs.dennisc.alice.ast.AbstractMethod findShortestMethod( edu.cmu.cs.dennisc.alice.ast.AbstractField field, String methodName ) {
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

	public static abstract class CodeTrackableShapeResolver implements Resolver< edu.cmu.cs.dennisc.croquet.TrackableShape > {
		protected abstract edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape( org.alice.ide.codeeditor.CodeEditor codeEditor );
		
		public final edu.cmu.cs.dennisc.croquet.TrackableShape getResolved() {
			org.alice.ide.codeeditor.CodeEditor codeEditor = org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().getCodeEditorInFocus();
			if( codeEditor != null ) {
				return this.resolveTrackableShape( codeEditor );
			} else {
				return null;
			}
		}
	}

	public static abstract class CodeDragComponentResolver implements Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > {
		protected abstract edu.cmu.cs.dennisc.croquet.DragComponent resolveDragComponent( org.alice.ide.codeeditor.CodeEditor codeEditor );
		public final edu.cmu.cs.dennisc.croquet.DragAndDropOperation getResolved() {
			org.alice.ide.codeeditor.CodeEditor codeEditor = org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().getCodeEditorInFocus();
			if( codeEditor != null ) {
				edu.cmu.cs.dennisc.croquet.DragComponent dragComponent = this.resolveDragComponent( codeEditor );
				if( dragComponent != null ) {
					return dragComponent.getDragAndDropOperation();
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	public interface StatementListPropertyResolver {
		public edu.cmu.cs.dennisc.alice.ast.StatementListProperty getStatementListProperty();
	}
	
	private class CodeBodyStatementListPropertyResolver implements StatementListPropertyResolver {
		private Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeResolver;
		public CodeBodyStatementListPropertyResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeResolver ) {
			this.codeResolver = codeResolver;
		}
		public edu.cmu.cs.dennisc.alice.ast.StatementListProperty getStatementListProperty() {
			edu.cmu.cs.dennisc.alice.ast.AbstractCode code = this.codeResolver.getResolved();
			if( code != null ) {
				if( code instanceof edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice ) {
					edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeInAlice = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)code;
					return codeInAlice.getBodyProperty().getValue().statements;
				} else {
					return null;
					}
			} else {
				return null;
			}
		}
	}

	private class DefaultStatementListPropertyResolver implements StatementListPropertyResolver {
		private edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty;
		public DefaultStatementListPropertyResolver( edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody statementWithBody ) {
			this.statementListProperty = statementWithBody.body.getValue().statements;
		}
		public edu.cmu.cs.dennisc.alice.ast.StatementListProperty getStatementListProperty() {
			return this.statementListProperty;
		}
	}
	
	public static class StatementListResolver extends CodeTrackableShapeResolver {
		private StatementListPropertyResolver statementListPropertyResolver;
		private int index;
		public StatementListResolver( StatementListPropertyResolver statementListPropertyResolver, int index ) {
			this.statementListPropertyResolver = statementListPropertyResolver;
			this.index = index;
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape( org.alice.ide.codeeditor.CodeEditor codeEditor ) {
			return codeEditor.getTrackableShapeAtIndexOf( this.statementListPropertyResolver.getStatementListProperty(), this.index );
		}
	}

	public static class StatementResolver extends CodeDragComponentResolver {
		private edu.cmu.cs.dennisc.alice.ast.Statement statement;
		public StatementResolver( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
			this.statement = statement;
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.DragComponent resolveDragComponent(org.alice.ide.codeeditor.CodeEditor codeEditor) {
			return codeEditor.getDragComponent( this.statement );
		}
	}

	public static class ProcedureInvocationResolver extends CodeDragComponentResolver {
		private Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver;
		private int index;
		public ProcedureInvocationResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver, int index ) {
			this.methodResolver = methodResolver;
			this.index = index;
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.DragComponent resolveDragComponent(org.alice.ide.codeeditor.CodeEditor codeEditor) {
			final edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = this.methodResolver.getResolved();
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
			if( this.index == Short.MAX_VALUE ) {
				this.index = list.size()-1;
			}
			if( list.size() > this.index ) {
				edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = list.get( this.index );
				edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)methodInvocation.getParent();
				return codeEditor.getDragComponent(statement);
			} else {
				return null;
			}
		}
	}
	public static class StatementAssignableToResolver extends CodeDragComponentResolver {
		private Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls;
		private int index;
		public StatementAssignableToResolver( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls, int index ) {
			this.cls = cls;
			this.index = index;
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.DragComponent resolveDragComponent(org.alice.ide.codeeditor.CodeEditor codeEditor) {
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
			if( this.index == Short.MAX_VALUE ) {
				this.index = list.size()-1;
			}
			if( list.size() > this.index ) {
				edu.cmu.cs.dennisc.alice.ast.Statement statement = list.get( this.index );
				return codeEditor.getDragComponent(statement);
			} else {
				return null;
			}
		}
	}
	public static class StatementWithBodyAssignableToStatementListPropertyResolver implements StatementListPropertyResolver {
		private Class<? extends edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> cls;
		private int index;
		public StatementWithBodyAssignableToStatementListPropertyResolver( Class<? extends edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> cls, int index ) {
			this.cls = cls;
			this.index = index;
		}
		public edu.cmu.cs.dennisc.alice.ast.StatementListProperty getStatementListProperty() {
			edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().getValue();
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler( this.cls ) {
				@Override
				protected boolean isAcceptable( Object statment ) {
					return true;
				}
			};
			edu.cmu.cs.dennisc.alice.ast.BlockStatement body = code.getBodyProperty().getValue();
			body.crawl( crawler, false );
			java.util.List<edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> list = crawler.getList();
			if( this.index == Short.MAX_VALUE ) {
				this.index = list.size()-1;
			}
			if( list.size() > this.index ) {
				return list.get( this.index ).body.getValue().statements;
			} else {
				return null;
			}
		}
	}
	public static abstract class ConditionalStatementListPropertyResolver implements StatementListPropertyResolver {
		private int index;
		public ConditionalStatementListPropertyResolver( int index ) {
			this.index = index;
		}
		protected abstract edu.cmu.cs.dennisc.alice.ast.BlockStatement getBlockStatement( edu.cmu.cs.dennisc.alice.ast.ConditionalStatement conditionalStatement );
		public edu.cmu.cs.dennisc.alice.ast.StatementListProperty getStatementListProperty() {
			edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().getValue();
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler( edu.cmu.cs.dennisc.alice.ast.ConditionalStatement.class ) {
				@Override
				protected boolean isAcceptable( Object statment ) {
					return true;
				}
			};
			edu.cmu.cs.dennisc.alice.ast.BlockStatement body = code.getBodyProperty().getValue();
			body.crawl( crawler, false );
			java.util.List<edu.cmu.cs.dennisc.alice.ast.ConditionalStatement> list = crawler.getList();
			if( this.index == Short.MAX_VALUE ) {
				this.index = list.size()-1;
			}
			if( list.size() > this.index ) {
				return this.getBlockStatement( list.get( this.index ) ).statements;
			} else {
				return null;
			}
		}
	}
	public static class IfStatementListPropertyResolver extends ConditionalStatementListPropertyResolver {
		public IfStatementListPropertyResolver( int index ) {
			super( index );
		}
		@Override
		protected edu.cmu.cs.dennisc.alice.ast.BlockStatement getBlockStatement( edu.cmu.cs.dennisc.alice.ast.ConditionalStatement conditionalStatement ) {
			return conditionalStatement.booleanExpressionBodyPairs.get( 0 ).body.getValue();
		}
	}
	public static class ElseStatementListPropertyResolver extends ConditionalStatementListPropertyResolver {
		public ElseStatementListPropertyResolver( int index ) {
			super( index );
		}
		@Override
		protected edu.cmu.cs.dennisc.alice.ast.BlockStatement getBlockStatement( edu.cmu.cs.dennisc.alice.ast.ConditionalStatement conditionalStatement ) {
			return conditionalStatement.elseBody.getValue();
		}
	}
	
	public StatementListPropertyResolver createStatementListPropertyResolver( Class<? extends edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> cls, int index ) {
		return new StatementWithBodyAssignableToStatementListPropertyResolver( cls, index );
	}
	public StatementListPropertyResolver createFirstStatementListPropertyResolver( Class<? extends edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> cls ) {
		return this.createStatementListPropertyResolver(cls, 0);
	}
	public StatementListPropertyResolver createLastStatementListPropertyResolver( Class<? extends edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> cls ) {
		return this.createStatementListPropertyResolver(cls, Short.MAX_VALUE);
	}

	public StatementListPropertyResolver createIfStatementListPropertyResolver( int index ) {
		return new IfStatementListPropertyResolver( index );
	}
	public StatementListPropertyResolver createFirstIfStatementListPropertyResolver() {
		return this.createIfStatementListPropertyResolver(0);
	}
	public StatementListPropertyResolver createLastIfStatementListPropertyResolver() {
		return this.createIfStatementListPropertyResolver(Short.MAX_VALUE);
	}

	public StatementListPropertyResolver createElseStatementListPropertyResolver( int index ) {
		return new ElseStatementListPropertyResolver( index );
	}
	public StatementListPropertyResolver createFirstElseStatementListPropertyResolver() {
		return this.createElseStatementListPropertyResolver(0);
	}
	public StatementListPropertyResolver createLastElseStatementListPropertyResolver() {
		return this.createElseStatementListPropertyResolver(Short.MAX_VALUE);
	}
	
	public StatementListResolver createStatementListResolver( StatementListPropertyResolver statementListPropertyResolver, int index ) {
		return new StatementListResolver(statementListPropertyResolver, index);
	}
	public StatementListResolver createBeginingOfStatementListResolver( StatementListPropertyResolver statementListPropertyResolver ) {
		return this.createStatementListResolver(statementListPropertyResolver, 0);
	}
	public StatementListResolver createEndOfStatementListResolver( StatementListPropertyResolver statementListPropertyResolver ) {
		return this.createStatementListResolver(statementListPropertyResolver, Short.MAX_VALUE);
	}


//	public StatementListResolver createStatementListResolver( Class<? extends edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> statementWithBodyCls, int indexA, int indexB ) {
//		return new StatementListResolver(
//				statementListProperty, indexA, 
//		indexB);
//	}


//	public StatementListResolver createStatementListResolver( edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody statementWithBody, int index ) {
//		return this.createStatementListResolver( new DefaultStatementListPropertyResolver( statementWithBody ), index);
//	}
//	public StatementListResolver createBeginingOfStatementListResolver( edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody statementWithBody ) {
//		return this.createBeginingOfStatementListResolver(new DefaultStatementListPropertyResolver( statementWithBody ));
//	}
//	public StatementListResolver createEndOfStatementListResolver( edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody statementWithBody ) {
//		return this.createEndOfStatementListResolver(new DefaultStatementListPropertyResolver( statementWithBody ));
//	}

	public StatementListResolver createStatementListResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeResolver, int index ) {
		return this.createStatementListResolver(new CodeBodyStatementListPropertyResolver( codeResolver ), index);
	}
	public StatementListResolver createBeginingOfStatementListResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeResolver ) {
		return this.createBeginingOfStatementListResolver(new CodeBodyStatementListPropertyResolver( codeResolver ));
	}
	public StatementListResolver createEndOfStatementListResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeResolver ) {
		return this.createEndOfStatementListResolver(new CodeBodyStatementListPropertyResolver( codeResolver ));
	}


	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createStatementAssignableToResolver( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls, int index ) {
		return new StatementAssignableToResolver( cls, index );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createFirstStatementAssignableToResolver( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls ) {
		return this.createStatementAssignableToResolver( cls, 0 );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createLastStatementAssignableToResolver( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls ) {
		return this.createStatementAssignableToResolver( cls, Short.MAX_VALUE );
	}
	
	
	
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createInvocationResolver( final Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver, int index ) {
		return new ProcedureInvocationResolver(methodResolver, index);
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createFirstInvocationResolver( final Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver ) {
		return this.createInvocationResolver(methodResolver, 0);
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createLastInvocationResolver( final Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver ) {
		return this.createInvocationResolver(methodResolver, Short.MAX_VALUE);
	}

	private static class PredeterminedTabResolver implements Resolver< edu.cmu.cs.dennisc.croquet.PredeterminedTab > {
		private edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabSelectionOperation;
		private java.util.UUID id;
		public PredeterminedTabResolver( edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabSelectionOperation, java.util.UUID id ) {
			this.tabSelectionOperation = tabSelectionOperation;
			this.id = id;
		}
		public edu.cmu.cs.dennisc.croquet.PredeterminedTab getResolved() {
			return this.tabSelectionOperation.getItemForId( this.id );
		}
	}
	
	public Resolver< edu.cmu.cs.dennisc.croquet.PredeterminedTab > getProceduresTab() {
		return new PredeterminedTabResolver( ide.getMembersEditor().getTabbedPaneSelectionState(), java.util.UUID.fromString( "2731d704-1f80-444e-a610-e6e5866c0b9a" ) );
	}

	public Resolver< edu.cmu.cs.dennisc.croquet.PredeterminedTab > getFunctionsTab() {
		return new PredeterminedTabResolver( ide.getMembersEditor().getTabbedPaneSelectionState(), java.util.UUID.fromString( "0f5d1f93-fc67-4109-9aff-0e7b232f201c" ) );
	}

	public Resolver< edu.cmu.cs.dennisc.croquet.PredeterminedTab > getFieldsTab() {
		return new PredeterminedTabResolver( ide.getMembersEditor().getTabbedPaneSelectionState(), java.util.UUID.fromString( "6cb9c5a1-dc60-48e7-9a52-534009a093b8" ) );
	}
	
	private static class InputDialogOperationFromIdResolver implements Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation > {
		private java.util.UUID id;
		public InputDialogOperationFromIdResolver( java.util.UUID id ) {
			this.id = id;
		}
		public edu.cmu.cs.dennisc.croquet.InputDialogOperation getResolved() {
			java.util.Set< edu.cmu.cs.dennisc.croquet.Model< ? > > models = org.alice.ide.IDE.getSingleton().lookupModels( this.id );
			for( edu.cmu.cs.dennisc.croquet.Model< ? > model : models ) {
				if( model instanceof edu.cmu.cs.dennisc.croquet.InputDialogOperation ) {
					if( model.isInView() ) {
						return (edu.cmu.cs.dennisc.croquet.InputDialogOperation)model;
					}
				}
			}
			return null;
		}
	}
	
	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > createCurrentCodeResolver() {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode >() {
			public edu.cmu.cs.dennisc.alice.ast.AbstractCode getResolved() {
				return ide.getFocusedCode();
			}
		};
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > createMethodResolver( final edu.cmu.cs.dennisc.alice.ast.AbstractField field, final String methodName ) {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod >() {
			public edu.cmu.cs.dennisc.alice.ast.AbstractMethod getResolved() {
				return findShortestMethod( field, methodName );
			}
		};
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > createMethodResolver( final Class<?> cls, final String methodName ) {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod >() {
			public edu.cmu.cs.dennisc.alice.ast.AbstractMethod getResolved() {
				return findShortestMethod( cls, methodName );
			}
		};
	}
	
	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractField > createFieldResolver( final String name ) {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
			public edu.cmu.cs.dennisc.alice.ast.AbstractField getResolved() {
				return ide.getSceneType().getDeclaredField( name );
			}
		};
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractField > createRootFieldResolver() {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
			public edu.cmu.cs.dennisc.alice.ast.AbstractField getResolved() {
				return ide.getSceneField();
			}
		};
	}
	
	public Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation > createDeclareProcedureOperationResolver() {
		return new InputDialogOperationFromIdResolver( java.util.UUID.fromString( "dcaee920-08ea-4b03-85d1-f2df5f73bfb4" ) );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation > createDeclareFunctionOperationResolver() {
		return new InputDialogOperationFromIdResolver( java.util.UUID.fromString( "171164e5-8159-4641-9528-a230ef4d2600" ) );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation > createDeclareFieldOperationResolver() {
		return new InputDialogOperationFromIdResolver( java.util.UUID.fromString( "5a07b4dc-0bd9-4393-93d2-1cc1a9b48262" ) );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation > createDeclareParameterOperationResolver() {
		return new InputDialogOperationFromIdResolver( java.util.UUID.fromString( "853fb6a3-ea7b-4575-93d6-547f687a7033" ) );
	}
	
	
	public static class TemplateDragComponentResolver implements Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > {
		private Class< ? extends edu.cmu.cs.dennisc.croquet.DragComponent > cls;
		public TemplateDragComponentResolver( Class< ? extends edu.cmu.cs.dennisc.croquet.DragComponent > cls ) {
			assert edu.cmu.cs.dennisc.croquet.Component.class.isAssignableFrom( cls );
			this.cls = cls;
		}
		public edu.cmu.cs.dennisc.croquet.DragComponent getDragComponent() {
			org.alice.ide.ubiquitouspane.UbiquitousPane ubiquitousPane = org.alice.ide.IDE.getSingleton().getUbiquitousPane();
			return this.cls.cast( edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findFirstMatch( ubiquitousPane, this.cls ) );
		}
		public edu.cmu.cs.dennisc.croquet.DragAndDropOperation getResolved() {
			edu.cmu.cs.dennisc.croquet.DragComponent dragComponent = this.getDragComponent();
			if( dragComponent != null ) {
				edu.cmu.cs.dennisc.croquet.DragAndDropOperation rv = dragComponent.getDragAndDropOperation();
				return rv;
			} else {
				return null;
			}
		}
	}
	
	public abstract static class MethodInvocationTemplateResolver implements Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > {
		private edu.cmu.cs.dennisc.alice.ast.AbstractField field;
		private String methodName;
		public MethodInvocationTemplateResolver( edu.cmu.cs.dennisc.alice.ast.AbstractField field, String methodName ) {
			this.field = field;
			this.methodName = methodName;
		}
		protected abstract edu.cmu.cs.dennisc.croquet.DragComponent getDragComponent( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method );
		public edu.cmu.cs.dennisc.croquet.DragAndDropOperation getResolved() {
			edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = findShortestMethod( field, methodName );
			if( method != null ) {
				return this.getDragComponent( method ).getDragAndDropOperation();
			} else {
				return null;
			}
		}
	}
	public static class ProcedureInvocationTemplateResolver extends MethodInvocationTemplateResolver {
		public ProcedureInvocationTemplateResolver( edu.cmu.cs.dennisc.alice.ast.AbstractField field, String methodName ) {
			super( field, methodName );
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.DragComponent getDragComponent( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
			return org.alice.ide.memberseditor.TemplateFactory.getProcedureInvocationTemplate( method );
		}
	}
	public static class FunctionInvocationTemplateResolver extends MethodInvocationTemplateResolver {
		public FunctionInvocationTemplateResolver( edu.cmu.cs.dennisc.alice.ast.AbstractField field, String methodName ) {
			super( field, methodName );
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.DragComponent getDragComponent( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
			return org.alice.ide.memberseditor.TemplateFactory.getFunctionInvocationTemplate( method );
		}
	}
	
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createProcedureInvocationTemplateResolver( edu.cmu.cs.dennisc.alice.ast.AbstractField field, String methodName ) {
		return new ProcedureInvocationTemplateResolver( field, methodName );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createFunctionInvocationTemplateResolver( edu.cmu.cs.dennisc.alice.ast.AbstractField field, String methodName ) {
		return new FunctionInvocationTemplateResolver( field, methodName );
	}
	
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createDoInOrderTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.DoInOrderTemplate.class );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createCountLoopTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.CountLoopTemplate.class );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createWhileLoopTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.WhileLoopTemplate.class );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createForEachInArrayLoopTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.ForEachInArrayLoopTemplate.class );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createIfElseTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.ConditionalStatementTemplate.class );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createDoTogetherTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.DoTogetherTemplate.class );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createEachInArrayTogetherTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.EachInArrayTogetherTemplate.class );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createDoInThreadTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.DoInThreadTemplate.class );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createDeclareLocalTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.CommentTemplate.class );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createCommentTemplateResolver() {
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.CommentTemplate.class );
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
