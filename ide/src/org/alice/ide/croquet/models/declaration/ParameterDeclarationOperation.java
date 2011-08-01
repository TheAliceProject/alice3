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

package org.alice.ide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public class ParameterDeclarationOperation extends DeclarationOperation< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice >{
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice, ParameterDeclarationOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static ParameterDeclarationOperation getInstance( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code ) {
		synchronized( map ) {
			ParameterDeclarationOperation rv = map.get( code );
			if( rv != null ) {
				//pass
			} else {
				rv = new ParameterDeclarationOperation( code );
				map.put( code, rv );
			}
			return rv;
		}
	}
	private final edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code;
	private ParameterDeclarationOperation( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code ) {
		super( 
				java.util.UUID.fromString( "ebaf5680-4b33-449d-aa07-f96b25b74c02" ), 
				null, false,
				null, true, 
				false, true, 
				"", true,
				null, false
		);
		this.code = code;
	}

	public edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice getCode() {
		return this.code;
	}
	@Override
	protected org.alice.ide.croquet.components.declaration.DeclarationPanel< ? > createMainComponent( org.lgna.croquet.history.InputDialogOperationStep step ) {
		return new org.alice.ide.croquet.components.declaration.ParameterDeclarationPanel( this );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice createPreviewDeclaration() {
		return new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice( this.getDeclarationName(), this.getValueType() );
	}
	@Override
	protected org.lgna.croquet.edits.Edit< ? > createEdit( org.lgna.croquet.history.InputDialogOperationStep step, edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > valueType, java.lang.String declarationName, edu.cmu.cs.dennisc.alice.ast.Expression initializer ) {
		assert declaringType == null;
		assert valueType != null;
		assert declarationName != null;
		assert initializer == null;
		return new org.alice.ide.croquet.edits.ast.ParameterDeclarationEdit( step, new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice( declarationName, valueType ) );
	}
}
