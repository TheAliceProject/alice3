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

	/*package-private*/ static edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava findShortestMethod( Class<?> cls, String methodName ) {
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
	/*package-private*/ static edu.cmu.cs.dennisc.alice.ast.AbstractMethod findShortestMethod( edu.cmu.cs.dennisc.alice.ast.Accessible accessible, String methodName ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > type = accessible.getValueType();
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
	
	
	//todo:
	/*package-private*/ static edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice findFirstLocalNamed( edu.cmu.cs.dennisc.alice.ast.AbstractCode code, final String localName ) {
		if( code instanceof edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeInAlice = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)code;
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice >( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice.class ) {
				@Override
				protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local ) {
					return localName.equals( local.getName() );
				}
			};
			edu.cmu.cs.dennisc.alice.ast.BlockStatement body = codeInAlice.getBodyProperty().getValue();
			body.crawl( crawler, false );
			java.util.List<edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice> list = crawler.getList();
			int index = 0;
			if( index == Short.MAX_VALUE ) {
				index = list.size()-1;
			}
			if( list.size() > index ) {
				return list.get( 0 );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/*package-private*/ static edu.cmu.cs.dennisc.alice.ast.AbstractMethod findFirstMethodInCurrentCode( final String methodName ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = org.alice.ide.IDE.getSingleton().getFocusedCode();
		if( code instanceof edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeInAlice = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)code;
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation >( edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class ) {
				@Override
				protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = methodInvocation.method.getValue();
					return methodName.equals( method.getName() );
				}
			};
			edu.cmu.cs.dennisc.alice.ast.BlockStatement body = codeInAlice.getBodyProperty().getValue();
			body.crawl( crawler, false );
			java.util.List<edu.cmu.cs.dennisc.alice.ast.MethodInvocation> list = crawler.getList();
			int index = 0;
			if( index == Short.MAX_VALUE ) {
				index = list.size()-1;
			}
			if( list.size() > index ) {
				return list.get( index ).method.getValue();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	
	/*package-private*/ static <T extends edu.cmu.cs.dennisc.alice.ast.Node> T getNodeAt( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code, Class<T> cls, int index ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<T> crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<T>( cls ) {
			@Override
			protected boolean isAcceptable( T node ) {
				return true;
			}
		};
		edu.cmu.cs.dennisc.alice.ast.BlockStatement body = code.getBodyProperty().getValue();
		body.crawl( crawler, false );
		java.util.List<T> list = crawler.getList();
		if( index == Short.MAX_VALUE ) {
			index = list.size()-1;
		}
		if( list.size() > index ) {
			return list.get( index );
		} else {
			return null;
		}
	}

	/*package-private*/ static edu.cmu.cs.dennisc.alice.ast.MethodInvocation getMethodInvocationAt( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code, final edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, int index ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation >( edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class ) {
			@Override
			protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation ) {
				return methodInvocation.method.getValue() == method;
			}
		};
		edu.cmu.cs.dennisc.alice.ast.BlockStatement body = code.getBodyProperty().getValue();
		body.crawl( crawler, false );
		java.util.List<edu.cmu.cs.dennisc.alice.ast.MethodInvocation> list = crawler.getList();
		if( index == Short.MAX_VALUE ) {
			index = list.size()-1;
		}
		if( list.size() > index ) {
			return list.get( index );
		} else {
			return null;
		}
	}
	
	public Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > createStatementListPropertyResolver( Class<? extends edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> cls, int index ) {
		return new StatementWithBodyAssignableToStatementListPropertyResolver( cls, index );
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > createFirstStatementListPropertyResolver( Class<? extends edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> cls ) {
		return this.createStatementListPropertyResolver(cls, 0);
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > createLastStatementListPropertyResolver( Class<? extends edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody> cls ) {
		return this.createStatementListPropertyResolver(cls, Short.MAX_VALUE);
	}

	public Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > createIfStatementListPropertyResolver( int index ) {
		return new IfStatementListPropertyResolver( index );
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > createFirstIfStatementListPropertyResolver() {
		return this.createIfStatementListPropertyResolver(0);
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > createLastIfStatementListPropertyResolver() {
		return this.createIfStatementListPropertyResolver(Short.MAX_VALUE);
	}

	public Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > createElseStatementListPropertyResolver( int index ) {
		return new ElseStatementListPropertyResolver( index );
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > createFirstElseStatementListPropertyResolver() {
		return this.createElseStatementListPropertyResolver(0);
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > createLastElseStatementListPropertyResolver() {
		return this.createElseStatementListPropertyResolver(Short.MAX_VALUE);
	}
	
	public StatementListResolver createStatementListResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > statementListPropertyResolver, int index ) {
		return new StatementListResolver(statementListPropertyResolver, index);
	}
	public StatementListResolver createBeginingOfStatementListResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > statementListPropertyResolver ) {
		return this.createStatementListResolver(statementListPropertyResolver, 0);
	}
	public StatementListResolver createEndOfStatementListResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > statementListPropertyResolver ) {
		return this.createStatementListResolver(statementListPropertyResolver, Short.MAX_VALUE);
	}

//	private StatementListResolver createMethodBodyStatementListResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeResolver, int index ) {
//		return this.createStatementListResolver(new CodeBodyStatementListPropertyResolver( codeResolver ), index);
//	}
//	private StatementListResolver createBeginingOfMethodBodyStatementListResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeResolver ) {
//		return this.createBeginingOfStatementListResolver(new CodeBodyStatementListPropertyResolver( codeResolver ));
//	}
//	private StatementListResolver createEndOfMethodBodyStatementListResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeResolver ) {
//		return this.createEndOfStatementListResolver(new CodeBodyStatementListPropertyResolver( codeResolver ));
//	}
	public StatementListResolver createCurrentMethodBodyStatementListResolver( int index ) {
		return this.createStatementListResolver(new CodeBodyStatementListPropertyResolver( this.createCurrentCodeResolver() ), index);
	}
	public StatementListResolver createBeginingOfCurrentMethodBodyStatementListResolver() {
		return this.createBeginingOfStatementListResolver(new CodeBodyStatementListPropertyResolver( this.createCurrentCodeResolver() ));
	}
	public StatementListResolver createEndOfCurrentMethodBodyStatementListResolver() {
		return this.createEndOfStatementListResolver(new CodeBodyStatementListPropertyResolver( this.createCurrentCodeResolver() ));
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

	public Resolver< edu.cmu.cs.dennisc.croquet.PredeterminedTab > getProceduresTab() {
		return new PredeterminedTabResolver( ide.getMembersEditor().getTabbedPaneSelectionState(), java.util.UUID.fromString( "2731d704-1f80-444e-a610-e6e5866c0b9a" ) );
	}

	public Resolver< edu.cmu.cs.dennisc.croquet.PredeterminedTab > getFunctionsTab() {
		return new PredeterminedTabResolver( ide.getMembersEditor().getTabbedPaneSelectionState(), java.util.UUID.fromString( "0f5d1f93-fc67-4109-9aff-0e7b232f201c" ) );
	}

	public Resolver< edu.cmu.cs.dennisc.croquet.PredeterminedTab > getFieldsTab() {
		return new PredeterminedTabResolver( ide.getMembersEditor().getTabbedPaneSelectionState(), java.util.UUID.fromString( "6cb9c5a1-dc60-48e7-9a52-534009a093b8" ) );
	}
	
	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > createCurrentCodeResolver() {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode >() {
			public edu.cmu.cs.dennisc.alice.ast.AbstractCode getResolved() {
				return ide.getFocusedCode();
			}
		};
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > createCodeResolver( final String fieldName, final String methodName) {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractCode >() {
			public edu.cmu.cs.dennisc.alice.ast.AbstractCode getResolved() {
				edu.cmu.cs.dennisc.alice.ast.AbstractField foundField = null;
				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = ide.getSceneField();
				if( sceneField.getName().equals( fieldName ) ) {
					foundField = sceneField;
				} else {
					java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractField > fields = sceneField.getValueType().getDeclaredFields();
					for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : fields ) {
						if( field.getName().equals( fieldName ) ) {
							foundField = field;
							break;
						}
					}
				}
				if( foundField != null ) {
					return findShortestMethod( foundField, methodName );
				} else {
					return null;
				}
			}
		};
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > createCurrentAccessibleMethodResolver( final String methodName ) {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod >() {
			public edu.cmu.cs.dennisc.alice.ast.AbstractMethod getResolved() {
				edu.cmu.cs.dennisc.alice.ast.Accessible accessible = ide.getAccessibleListState().getValue();
				return findShortestMethod( accessible, methodName );
			}
		};
	}
	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > createCurrentCodeMethodResolver( final String methodName ) {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod >() {
			public edu.cmu.cs.dennisc.alice.ast.AbstractMethod getResolved() {
				return findFirstMethodInCurrentCode( methodName );
			}
		};
	}

//	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > createMethodResolver( final edu.cmu.cs.dennisc.alice.ast.AbstractField field, final String methodName ) {
//		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod >() {
//			public edu.cmu.cs.dennisc.alice.ast.AbstractMethod getResolved() {
//				return findShortestMethod( field, methodName );
//			}
//		};
//	}
//	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > createMethodResolver( final Class<?> cls, final String methodName ) {
//		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod >() {
//			public edu.cmu.cs.dennisc.alice.ast.AbstractMethod getResolved() {
//				return findShortestMethod( cls, methodName );
//			}
//		};
//	}

	public Resolver< edu.cmu.cs.dennisc.croquet.ActionOperation > createEditCodeOperationResolver( final Resolver< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeResolver ) {
		return new Resolver< edu.cmu.cs.dennisc.croquet.ActionOperation >() {
			public edu.cmu.cs.dennisc.croquet.ActionOperation getResolved() {
				edu.cmu.cs.dennisc.alice.ast.AbstractCode code = codeResolver.getResolved();
				if( code != null ) {
					edu.cmu.cs.dennisc.croquet.ActionOperation rv = org.alice.ide.operations.ast.FocusCodeOperation.getInstance( code );
					return rv;
				} else {
					return null;
				}
			}
		};
	}
	
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createIfElseStatementConditionResolver( int index ) {
		return new IfElseStatementConditionResolver( index );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createFirstIfElseStatementConditionResolver() {
		return this.createIfElseStatementConditionResolver(0);
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createLastIfElseStatementConditionResolver() {
		return this.createIfElseStatementConditionResolver(Short.MAX_VALUE);
	}

	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createWhileLoopConditionResolver( int index ) {
		return new WhileLoopConditionResolver( index );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createFirstWhileLoopConditionResolver() {
		return this.createWhileLoopConditionResolver(0);
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createLastWhileLoopConditionResolver() {
		return this.createWhileLoopConditionResolver(Short.MAX_VALUE);
	}

	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createForEachInArrayLoopVariableResolver( int index ) {
		return new ForEachInArrayLoopVariableResolver( index );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createFirstForEachInArrayLoopVariableResolver() {
		return this.createForEachInArrayLoopVariableResolver(0);
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createLastForEachInArrayLoopVariableResolver() {
		return this.createForEachInArrayLoopVariableResolver(Short.MAX_VALUE);
	}

	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createParameterResolver( int index ) {
		return new ParameterAtResolver( index );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createFirstParameterResolver() {
		return this.createParameterResolver( 0 );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createLastParameterResolver() {
		return this.createParameterResolver( Short.MAX_VALUE );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createParameterNamedResolver( String paramaterName ) {
		return new ParameterNamedResolver( paramaterName );
	}
	
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createLocalNamedResolver( String paramaterName ) {
		return new LocalNamedResolver( paramaterName );
	}

//	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createInvocationResolver( final Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver, int index ) {
//		return new MethodInvocationStatementResolver(methodResolver, index);
//	}
//	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createFirstInvocationResolver( final Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver ) {
//		return this.createInvocationResolver(methodResolver, 0);
//	}
//	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createLastInvocationResolver( final Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver ) {
//		return this.createInvocationResolver(methodResolver, Short.MAX_VALUE);
//	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createInvocationResolver( String methodName, int index ) {
		return new MethodInvocationStatementResolver(this.createCurrentCodeMethodResolver(methodName), index);
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createFirstInvocationResolver( String methodName ) {
		return this.createInvocationResolver(methodName, 0);
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createLastInvocationResolver( String methodName ) {
		return this.createInvocationResolver(methodName, Short.MAX_VALUE);
	}

//	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createInvocationArgumentResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver, int invocationIndex, int argumentIndex ) {
//		return new InvocationArgumentResolver( methodResolver, invocationIndex, argumentIndex );
//	}
//	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createInvocationInstanceResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver, int invocationIndex ) {
//		return new InvocationInstanceResolver( methodResolver, invocationIndex );
//	}
//	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createFirstInvocationInstanceResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver ) {
//		return this.createInvocationInstanceResolver( methodResolver, 0 );
//	}
//	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createLastInvocationInstanceResolver( Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractMethod > methodResolver ) {
//		return this.createInvocationInstanceResolver( methodResolver, Short.MAX_VALUE );
//	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createInvocationArgumentResolver( String methodName, int invocationIndex, int argumentIndex ) {
		return new InvocationArgumentResolver( this.createCurrentCodeMethodResolver(methodName), invocationIndex, argumentIndex );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createInvocationInstanceResolver( String methodName, int invocationIndex ) {
		return new InvocationInstanceResolver( this.createCurrentCodeMethodResolver(methodName), invocationIndex );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createFirstInvocationInstanceResolver( String methodName ) {
		return this.createInvocationInstanceResolver( methodName, 0 );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createLastInvocationInstanceResolver(String methodName ) {
		return this.createInvocationInstanceResolver( methodName, Short.MAX_VALUE );
	}

	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createInvocationMoreResolver( String methodName, int invocationIndex ) {
		return new InvocationMoreResolver( this.createCurrentCodeMethodResolver(methodName), invocationIndex );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createFirstInvocationMoreResolver( String methodName ) {
		return this.createInvocationMoreResolver( methodName, 0 );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.Operation<?, ?> > createLastInvocationMoreResolver(String methodName ) {
		return this.createInvocationMoreResolver( methodName, Short.MAX_VALUE );
	}
	
	public Resolver< edu.cmu.cs.dennisc.alice.ast.Accessible > createAccessibleResolver( final String name ) {
		return new Resolver< edu.cmu.cs.dennisc.alice.ast.Accessible >() {
			public edu.cmu.cs.dennisc.alice.ast.Accessible getResolved() {
				for( edu.cmu.cs.dennisc.alice.ast.Accessible accessible : ide.getAccessibleListState() ) {
					if( name.equals( accessible.getName() ) ) {
						return accessible;
					}
				}
				return null;
			}
		};
	}
//	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractField > createFieldResolver( final String name ) {
//		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
//			public edu.cmu.cs.dennisc.alice.ast.AbstractField getResolved() {
//				return ide.getSceneType().getDeclaredField( name );
//			}
//		};
//	}
//	public Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractField > createRootFieldResolver() {
//		return new Resolver< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
//			public edu.cmu.cs.dennisc.alice.ast.AbstractField getResolved() {
//				return ide.getSceneField();
//			}
//		};
//	}

	public Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation > createDeclareProcedureOperationResolver() {
		return new DeclareMemberResolver() {
			@Override
			protected edu.cmu.cs.dennisc.croquet.InputDialogOperation getResolved( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > type ) {
				return org.alice.ide.operations.ast.DeclareProcedureOperation.getInstance( type );
			}
		};
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation > createDeclareFunctionOperationResolver() {
		return new DeclareMemberResolver() {
			@Override
			protected edu.cmu.cs.dennisc.croquet.InputDialogOperation getResolved( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > type ) {
				return org.alice.ide.operations.ast.DeclareFunctionOperation.getInstance( type );
			}
		};
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation > createDeclareFieldOperationResolver() {
		return new DeclareMemberResolver() {
			@Override
			protected edu.cmu.cs.dennisc.croquet.InputDialogOperation getResolved( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > type ) {
				return org.alice.ide.operations.ast.DeclareFieldOperation.getInstance( type );
			}
		};
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation > createDeclareMethodParameterOperationResolver() {
		return new Resolver< edu.cmu.cs.dennisc.croquet.InputDialogOperation >() {
			public edu.cmu.cs.dennisc.croquet.InputDialogOperation getResolved() {
				edu.cmu.cs.dennisc.alice.ast.AbstractCode code = ide.getFocusedCode();
				if( code != null ) {
					if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
						edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
						return org.alice.ide.operations.ast.DeclareMethodParameterOperation.getInstance( method );
					} else {
						return null;
					}
				} else {
					return null;
				}
			}
		};
//		return new InputDialogOperationFromIdResolver( java.util.UUID.fromString( "aa3d337d-b409-46ae-816f-54f139b32d86" ) );
	}
	
	
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createProcedureInvocationTemplateResolver( String methodName ) {
		return new ProcedureInvocationTemplateResolver( methodName );
	}
	public Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > createFunctionInvocationTemplateResolver( String methodName ) {
		return new FunctionInvocationTemplateResolver( methodName );
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
		return new TemplateDragComponentResolver( org.alice.ide.ubiquitouspane.templates.DeclareLocalTemplate.class );
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
