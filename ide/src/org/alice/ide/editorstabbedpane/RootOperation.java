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
package org.alice.ide.editorstabbedpane;

class TypeFillIn extends edu.cmu.cs.dennisc.cascade.MenuFillIn< edu.cmu.cs.dennisc.zoot.ActionOperation > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;

	public TypeFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( type.getName() );
		this.type = type;
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return org.alice.ide.common.TypeComponent.createInstance( this.type ).getAwtComponent();
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		if( this.type != null ) {
			blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.EditTypeOperation( this.type ) ) );
			blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.RenameTypeOperation( this.type ) ) );
			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
			if( ide.isInstanceCreationAllowableFor( this.type ) ) {
				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType = ide.getSceneType();
				blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.DeclareFieldOfPredeterminedTypeOperation( ownerType, this.type ) ) );
			}
			blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.file.SaveAsTypeOperation( this.type ) ) );
			blank.addSeparator();
			blank.addFillIn( new OperatorFillIn( new EditConstructorOperation( this.type.getDeclaredConstructor() ) ) );
			blank.addSeparator();
			blank.addFillIn( new OperatorFillIn( org.alice.ide.operations.ast.DeclareProcedureOperation.getInstance( this.type ) ) );
			for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.type.methods ) {
				if( method.isProcedure() ) {
					blank.addFillIn( new OperatorFillIn( new EditMethodOperation( method ) ) );
				}
			}
			blank.addSeparator();
			for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.type.methods ) {
				if( method.isFunction() ) {
					blank.addFillIn( new OperatorFillIn( new EditMethodOperation( method ) ) );
				}
			}
			blank.addFillIn( new OperatorFillIn( org.alice.ide.operations.ast.DeclareFunctionOperation.getInstance( this.type ) ) );
			blank.addSeparator();
			blank.addFillIn( new OperatorFillIn( org.alice.ide.operations.ast.DeclareFieldOperation.getInstance( this.type ) ) );
			for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : this.type.fields ) {
				blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.EditFieldOperation( field ) ) );
			}
		} else {
			blank.addFillIn( new edu.cmu.cs.dennisc.cascade.CancelFillIn( "type is not set.  canceling." ) );
		}
	}
}

class ProjectBlank extends edu.cmu.cs.dennisc.cascade.Blank {
	private edu.cmu.cs.dennisc.alice.Project project;

	public ProjectBlank( edu.cmu.cs.dennisc.alice.Project project ) {
		assert project != null;
		this.project = project;
	}
	@Override
	protected void addChildren() {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice >( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice.class );
		final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> programType = this.project.getProgramType();
		programType.crawl( crawler, true );
		for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type : crawler.getList() ) {
			if( type == programType ) {
				//pass
			} else {
				this.addFillIn( new TypeFillIn( type ) );
			}
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class RootOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	public RootOperation() {
		super( java.util.UUID.fromString( "259dfcc5-dd20-4890-8104-a34a075734d0" ) );
	}
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		edu.cmu.cs.dennisc.croquet.ViewController<?,?> viewController = context.getViewController();
		int x = 0;
		int y = viewController.getHeight();
		edu.cmu.cs.dennisc.alice.Project project = getIDE().getProject();
		if( project != null ) {
			new ProjectBlank( project ).showPopupMenu( viewController.getAwtComponent(), x, y, new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.croquet.Operation< ?,? > >() {
				public void handleCompletion( edu.cmu.cs.dennisc.croquet.Operation< ?,? > operation ) {
					operation.fire();
				}
				public void handleCancelation() {
				}
			} );
		} else {
			this.getIDE().showMessageDialog( "Open a project first (via the File Menu)", "No Project", edu.cmu.cs.dennisc.croquet.MessageType.INFORMATION );
		}
	}
}
