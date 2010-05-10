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

/**
* @author Dennis Cosgrove
*/
public abstract class EditMethodsPane extends EditMembersPane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
	private java.util.Set< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > referencedMethods;
	public EditMethodsPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		super( declaringType, declaringType.methods );
		this.referencedMethods = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
		if( project != null ) {
			ide.ensureProjectCodeUpToDate();
			edu.cmu.cs.dennisc.alice.ast.AbstractType programType = project.getProgramType();
			for( final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : declaringType.methods ) {
				edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation >( edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class ) {
					@Override
					protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation ) {
						return methodInvocation.method.getValue() == method;
					}
				};
				programType.crawl( crawler, true );
				java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > methodInvocations = crawler.getList();
				if( methodInvocations.size() > 0 ) {
					referencedMethods.add( method );
				}
			}
		}
	}
	@Override
	protected java.awt.Component createCellRendererComponent( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice e ) {
		return new org.alice.ide.codeeditor.MethodHeaderPane( e, null ).getJComponent();
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.AbstractActionOperation createEditOperation( java.util.UUID groupUUID, String name ) {
		return new org.alice.ide.operations.ast.AbstractRenameNodeOperation( groupUUID, java.util.UUID.fromString( "16fe1479-558c-4386-925c-0d9eb5aaf280" ), name ) {
			@Override
			protected edu.cmu.cs.dennisc.property.StringProperty getNameProperty() {
				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = getSelectedItem();
				if( method != null ) {
					return method.name;
				} else {
					return null;
				}
			}
			@Override
			protected org.alice.ide.name.validators.NodeNameValidator getNodeNameValidator() {
				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = getSelectedItem();
				return new org.alice.ide.name.validators.MethodNameValidator( method );
			}
//			@Override
//			protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton< ? > button ) {
//				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = getSelectedItem();
//				if( method != null ) {
//					this.perform( context, e, button, method.name, new org.alice.ide.name.validators.MethodNameValidator( method ) );
//					getIDE().setFocusedCode( method );
//				} else {
//					context.cancel();
//				}
//			}
		};
	}
	protected abstract org.alice.ide.declarationpanes.CreateMethodPane createCreateMethodPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType );
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice createMember( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		org.alice.ide.declarationpanes.CreateMethodPane createMethodPane = this.createCreateMethodPane( declaringType );
		return createMethodPane.showInJDialog();
	}
	@Override
	protected boolean isEditItemEnabledFor( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice e ) {
		return e.isSignatureLocked.getValue() == false;
	}
	@Override
	protected boolean isRemoveItemEnabledFor( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice e ) {
		return e.isDeletionAllowed.getValue()  && this.referencedMethods.contains( e ) == false;
	}
}
