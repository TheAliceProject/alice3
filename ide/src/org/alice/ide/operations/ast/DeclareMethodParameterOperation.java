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
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class DeclareMethodParameterOperation extends org.alice.ide.operations.InputDialogWithPreviewOperation<org.alice.ide.declarationpanes.CreateMethodParameterPane> {
	@Deprecated
//	public interface EPIC_HACK_Validator {
//		public String getExplanationIfOkButtonShouldBeDisabled( String name, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > valueType );
//	}
//	private EPIC_HACK_Validator validator = null;
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice, DeclareMethodParameterOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static DeclareMethodParameterOperation getInstance( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		DeclareMethodParameterOperation rv = map.get( method );
		if( rv != null ) {
			//pass
		} else {
			rv = new DeclareMethodParameterOperation( method );
			map.put( method, rv );
		}
		return rv;
	}
	private edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method;
	private DeclareMethodParameterOperation( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "aa3d337d-b409-46ae-816f-54f139b32d86" ) );
		this.method = method;
		this.setName( "Add Parameter..." );
	}
//	public EPIC_HACK_Validator getValidator() {
//		return this.validator;
//	}
//	public void setValidator( EPIC_HACK_Validator validator ) {
//		this.validator = validator;
//	}
//	@Override
//	protected String getExplanationIfOkButtonShouldBeDisabled(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateMethodParameterPane> context) {
//		String rv = super.getExplanationIfOkButtonShouldBeDisabled( context );
//		if( this.validator != null ) {
//			String explanation = this.validator.getExplanationIfOkButtonShouldBeDisabled( this.getDeclarationName( context ), this.getValueType( context ) );
//			if( explanation != null ) {
//				rv = explanation;
//			}
//		}
//		return rv;
//	}

	private String getDeclarationName(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateMethodParameterPane> context) {
		org.alice.ide.declarationpanes.CreateMethodParameterPane createMethodParameterPane = context.getMainPanel();
		if( createMethodParameterPane != null ) {
			return createMethodParameterPane.getDeclarationName();
		} else {
			return null;
		}
	}
	private edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > getValueType(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateMethodParameterPane> context) {
		org.alice.ide.declarationpanes.CreateMethodParameterPane createMethodParameterPane = context.getMainPanel();
		if( createMethodParameterPane != null ) {
			return createMethodParameterPane.getValueType();
		} else {
			return null;
		}
	}

	
	@Override
	protected org.alice.ide.declarationpanes.CreateMethodParameterPane prologue(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateMethodParameterPane> context) {
		//todo: create before hand and refresh at this point
		return new org.alice.ide.declarationpanes.CreateMethodParameterPane( method, org.alice.ide.IDE.getSingleton().getMethodInvocations( method ) );
	}
	@Override
	protected void epilogue(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateMethodParameterPane> context, boolean isOk) {
		if( isOk ) {
			org.alice.ide.declarationpanes.CreateMethodParameterPane createMethodParameterPane = context.getMainPanel();
			final edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter = createMethodParameterPane.getActualInputValue();
			if( parameter != null ) {
				final int index = method.parameters.size();
				final java.util.Map< edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Argument > map = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Argument >();
				context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						org.alice.ide.ast.NodeUtilities.addParameter( map, method, parameter, index, org.alice.ide.IDE.getSingleton().getMethodInvocations( method ) );
					}
					@Override
					public void undo() {
						org.alice.ide.ast.NodeUtilities.removeParameter( map, method, parameter, index, org.alice.ide.IDE.getSingleton().getMethodInvocations( method ) );
					}
					@Override
					protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
						rv.append( "declare:" );
						edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, parameter, locale);
						return rv;
					}
				} );
			} else {
				context.cancel();
			}
		} else {
			context.cancel();
		}
	}
}
