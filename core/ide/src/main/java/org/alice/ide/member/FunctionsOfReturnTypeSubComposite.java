/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.member;

/**
 * @author Dennis Cosgrove
 */
public class FunctionsOfReturnTypeSubComposite extends MethodsSubComposite {
	private static java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, FunctionsOfReturnTypeSubComposite> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized FunctionsOfReturnTypeSubComposite getInstance( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( type != null ) {
			FunctionsOfReturnTypeSubComposite rv = map.get( type );
			if( rv != null ) {
				//pass
			} else {
				rv = new FunctionsOfReturnTypeSubComposite( type );
				map.put( type, rv );
			}
			return rv;
		} else {
			return null;
		}
	}

	private java.util.List<org.lgna.project.ast.AbstractMethod> methods = java.util.Collections.emptyList();
	private final org.lgna.project.ast.AbstractType<?, ?, ?> returnType;

	private FunctionsOfReturnTypeSubComposite( org.lgna.project.ast.AbstractType<?, ?, ?> returnType ) {
		super( java.util.UUID.fromString( "76b131c5-133c-43a0-9592-e200b9cd1f25" ), returnType != org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJoint.class ) );
		this.returnType = returnType;

		this.getOuterComposite().getIsExpandedState().setIconForBothTrueAndFalse( new org.alice.ide.common.TypeIcon( this.returnType ) );
		//this.getOuterComposite().getIsExpandedState().setTextForBothTrueAndFalse( org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getMenuTextForType( this.returnType ) );
		this.getOuterComposite().getIsExpandedState().setTextForBothTrueAndFalse( "Functions returning" );
	}

	public org.lgna.project.ast.AbstractType<?, ?, ?> getReturnType() {
		return this.returnType;
	}

	@Override
	public java.util.List<? extends org.lgna.project.ast.AbstractMethod> getMethods() {
		return this.methods;
	}

	public void setMethods( java.util.List<org.lgna.project.ast.AbstractMethod> methods ) {
		this.methods = java.util.Collections.unmodifiableList( methods );
		this.getView().refreshLater();
	}

	@Override
	protected org.alice.ide.member.views.MethodsSubView<FunctionsOfReturnTypeSubComposite> createView() {
		return new org.alice.ide.member.views.MethodsSubView<FunctionsOfReturnTypeSubComposite>( this );
	}
}
