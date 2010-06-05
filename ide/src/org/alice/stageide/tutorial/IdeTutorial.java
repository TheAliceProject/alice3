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
public class IdeTutorial extends edu.cmu.cs.dennisc.tutorial.Tutorial {
	private org.alice.stageide.StageIDE ide;
	public IdeTutorial( org.alice.stageide.StageIDE ide ) {
		super( new edu.cmu.cs.dennisc.croquet.Group[] { edu.cmu.cs.dennisc.alice.Project.GROUP, org.alice.ide.IDE.IDE_GROUP } );
		assert ide != null;
		this.ide = ide;
	}
	public org.alice.stageide.StageIDE getIDE() {
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
			c = cls.getSuperclass();
		}
		return null;
	}
	public edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava findShortestMethod( edu.cmu.cs.dennisc.alice.ast.AbstractField field, String methodName ) {
		Class<?> cls = field.getValueType().getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getReification();
		return findShortestMethod(cls, methodName);
	}

	public static abstract class Resolver implements edu.cmu.cs.dennisc.croquet.TrackableShape {
		private edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape;
		protected abstract edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape();
		private edu.cmu.cs.dennisc.croquet.TrackableShape getTrackableShape() {
			if( this.trackableShape != null ) {
				//pass
			} else {
				this.trackableShape = this.resolveTrackableShape();
			}
			return this.trackableShape;
		}
		public final java.awt.Shape getShape( edu.cmu.cs.dennisc.croquet.Component< ? > asSeenBy, java.awt.Insets insets ) {
			edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape = this.getTrackableShape();
			if( this.trackableShape != null ) {
				return trackableShape.getShape( asSeenBy, insets );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: getShape" );
				return null;
			}
		}
		public final java.awt.Shape getVisibleShape( edu.cmu.cs.dennisc.croquet.Component< ? > asSeenBy, java.awt.Insets insets ) {
			edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape = this.getTrackableShape();
			if( this.trackableShape != null ) {
				return trackableShape.getVisibleShape( asSeenBy, insets );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: getVisibleShape" );
				return null;
			}
		}
	}
	
	public abstract class CodeResolver extends Resolver {
		protected abstract edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape( org.alice.ide.codeeditor.CodeEditor codeEditor );
		@Override
		protected final edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape() {
			return this.resolveTrackableShape( ide.getEditorsTabSelectionState().getCodeEditorInFocus() );
		}
	}
	

	public class BlockStatementResolver extends CodeResolver {
		private edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement;
		public BlockStatementResolver( edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody statementWithBody ) {
			this.blockStatement = statementWithBody.body.getValue();
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape( org.alice.ide.codeeditor.CodeEditor codeEditor ) {
			return codeEditor.getTrackableShape( this.blockStatement, org.alice.ide.codeeditor.CodeEditor.Location.AT_END );
		}
	}

	public class ProcedureInvocationLoopResolver extends CodeResolver {
		private edu.cmu.cs.dennisc.alice.ast.AbstractMethod method;
		private int index;
		public ProcedureInvocationLoopResolver( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, int index ) {
			this.method = method;
			this.index = index;
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.TrackableShape resolveTrackableShape( org.alice.ide.codeeditor.CodeEditor codeEditor ) {
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
				return codeEditor.getTrackableShape(statement);
			} else {
				return null;
			}
		}
	}
	
	public edu.cmu.cs.dennisc.croquet.TrackableShape createBlockStatementResolver( edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody statementWithBody ) {
		return new BlockStatementResolver(statementWithBody);
	}
	public edu.cmu.cs.dennisc.croquet.TrackableShape findProcedureInvocationStatement( final edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, int index ) {
		return new ProcedureInvocationLoopResolver(method, index);
	}
	public edu.cmu.cs.dennisc.croquet.DragComponent getDoInOrderTemplate() {
		return (edu.cmu.cs.dennisc.croquet.DragComponent)this.getIDE().getUbiquitousPane().getComponent( 0 );
	}
	
	@Deprecated
	public edu.cmu.cs.dennisc.croquet.Edit createToDoEdit() {
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
}
