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

package org.alice.stageide.croquet.models.cascade.adapters;

/**
 * @author Dennis Cosgrove
 */
public class KeyAdapterFillIn extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks< org.lgna.project.ast.InstanceCreation > {
	private static class SingletonHolder {
		private static KeyAdapterFillIn instance = new KeyAdapterFillIn();
	}

	public static KeyAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}
	private final org.lgna.project.ast.InstanceCreation transientValue;
	private KeyAdapterFillIn() {
		super( java.util.UUID.fromString( "58f52823-5d1d-4de2-ae5f-d62f2f6d5dde" ) );
		this.transientValue = this.createValue();
	}
	private org.lgna.project.ast.InstanceCreation createValue() { 
		org.lgna.project.ast.UserParameter[] parameters = new org.lgna.project.ast.UserParameter[] { 
				new org.lgna.project.ast.UserParameter( "e", org.lgna.story.event.KeyEvent.class ) 
		};
		org.lgna.project.ast.BlockStatement body = new org.lgna.project.ast.BlockStatement();
		org.lgna.project.ast.UserMethod method = new org.lgna.project.ast.UserMethod( "keyPressed", Void.TYPE, parameters, body );
		method.isSignatureLocked.setValue( true );
		org.lgna.project.ast.AnonymousUserType type = new org.lgna.project.ast.AnonymousUserType();
		type.superType.setValue( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.event.KeyListener.class ) );
		type.methods.add( method );
		org.lgna.project.ast.AnonymousUserConstructor constructor = org.lgna.project.ast.AnonymousUserConstructor.get( type );
		return new org.lgna.project.ast.InstanceCreation( constructor );
	}
	@Override
	public org.lgna.project.ast.InstanceCreation createValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.InstanceCreation,Void > step ) {
		return this.createValue();
	}
	@Override
	public org.lgna.project.ast.InstanceCreation getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.InstanceCreation,Void > step ) {
		return this.transientValue;
	}
}
