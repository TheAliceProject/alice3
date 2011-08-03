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
public class FocusCodeOperation extends org.alice.ide.operations.ActionOperation {
	private org.lgna.project.ast.AbstractCode nextCode;
	
	private static java.util.Map< org.lgna.project.ast.AbstractCode, FocusCodeOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static FocusCodeOperation getInstance( org.lgna.project.ast.AbstractCode nextCode ) {
		FocusCodeOperation rv = map.get( nextCode );
		if( rv != null ) {
			//pass
		} else {
			rv = new FocusCodeOperation( nextCode );
			map.put( nextCode, rv );
		}
		return rv;
	}
	
	private FocusCodeOperation( org.lgna.project.ast.AbstractCode nextCode ) {
		super( org.alice.ide.ProjectApplication.UI_STATE_GROUP, java.util.UUID.fromString( "82bf4d2a-f1ff-4df5-a5dc-80f981181ba5" ) );
		this.nextCode = nextCode;
		String name;
		if( nextCode instanceof org.lgna.project.ast.ConstructorDeclaredInAlice ) {
			name = "Edit Constructor";
		} else {
			name = "Edit";
		}
		this.setName( name );
	}
	@Override
	protected final void perform(org.lgna.croquet.history.ActionOperationStep step) {
		final org.lgna.project.ast.AbstractCode prevCode = getIDE().getFocusedCode();
		step.commitAndInvokeDo( new org.alice.ide.ToDoEdit( step ) {
			@Override
			protected final void doOrRedoInternal( boolean isDo ) {
				getIDE().setFocusedCode( nextCode );
			}
			@Override
			protected final void undoInternal() {
				getIDE().setFocusedCode( prevCode );
			}
			@Override
			protected StringBuilder updatePresentation(StringBuilder rv, java.util.Locale locale) {
				rv.append( "focus: " );
				rv.append( org.lgna.project.ast.NodeUtilities.safeAppendRepr(rv, nextCode, locale) );
				return rv;
			}
		} );
	}
}
