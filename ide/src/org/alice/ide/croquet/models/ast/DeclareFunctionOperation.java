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
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public class DeclareFunctionOperation extends DeclareMethodOperation {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? >, DeclareFunctionOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static DeclareFunctionOperation getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > type ) {
		DeclareFunctionOperation rv = map.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = new DeclareFunctionOperation( type );
			map.put( type, rv );
		}
		return rv;
	}
	private DeclareFunctionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > type ) {
		super( java.util.UUID.fromString( "171164e5-8159-4641-9528-a230ef4d2600" ), type );
		this.setName( "Declare Function..." );
	}
	@Override
	protected org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< DeclareFunctionOperation > createCodableResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< DeclareFunctionOperation >( this, this.getDeclaringType(), edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice.class );
	}
	
	@Override
	protected org.alice.ide.declarationpanes.CreateDeclarationPane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > createCreateMethodPane( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > type ) {
		return new org.alice.ide.declarationpanes.CreateFunctionPane( type );
	}
	@Override
	protected String getMethodDescription( edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		return "Function";
	}
	@Override
	protected StringBuilder appendTutorialFinishNoteText( StringBuilder rv, org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = declareMethodEdit.getMethod();
		rv.append( "a) Select return value type: " );
		rv.append( "<strong>" );
		org.alice.ide.formatter.Formatter formatter = org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem();
		rv.append( formatter.getTextForType( method.getReturnType() ) );
		rv.append( "*</strong>" );
		rv.append( "<br>" );
		rv.append( "b) Enter name: " );
		rv.append( "<strong>" );
		rv.append( method.getName() );
		rv.append( "</strong>" );
		rv.append( "<br>" );
		rv.append( "c) Press <strong>OK</strong>." );
		rv.append( "<br>" );
		rv.append( "<br>" );
		rv.append( "* <em>Required</em>" );
		return rv;
	}
}
