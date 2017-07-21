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
package org.alice.ide.croquet.models.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public class TypeFillIn extends org.lgna.croquet.ImmutableCascadeFillIn<org.lgna.project.ast.AbstractType<?, ?, ?>, Void> {
	private static java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, TypeFillIn> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized TypeFillIn getInstance( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		TypeFillIn rv = map.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = new TypeFillIn( type );
			map.put( type, rv );
		}
		return rv;
	}

	private org.lgna.project.ast.AbstractType<?, ?, ?> type;

	private TypeFillIn( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		super( java.util.UUID.fromString( "8f3e1f74-d1fd-4484-98e0-bc37da452005" ) );
		this.type = type;
	}

	@Override
	public org.lgna.project.ast.AbstractType createValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.AbstractType<?, ?, ?>, Void> node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		return this.type;
	}

	@Override
	public org.lgna.project.ast.AbstractType getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.AbstractType<?, ?, ?>, Void> node ) {
		return this.type;
	}

	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.AbstractType<?, ?, ?>, Void> node ) {
		int depth = org.lgna.project.ast.StaticAnalysisUtilities.getUserTypeDepth( this.type );
		if( depth > 0 ) {
			StringBuilder sb = new StringBuilder();
			for( int i = 0; i < depth; i++ ) {
				sb.append( "+" );
			}
			return new org.lgna.croquet.views.LineAxisPanel( new org.lgna.croquet.views.Label( sb.toString() ), org.alice.ide.common.TypeComponent.createInstance( this.type ) ).getAwtComponent();
		} else {
			throw new AssertionError();
		}
	}

	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.AbstractType<?, ?, ?>, Void> node ) {
		int depth = org.lgna.project.ast.StaticAnalysisUtilities.getUserTypeDepth( this.type );
		if( depth > 0 ) {
			return super.getMenuItemIcon( node );
		} else {
			return org.alice.ide.common.TypeIcon.getInstance( this.type );
		}
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( this.type );
	}

	@Override
	public String getMenuItemText( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.AbstractType<?, ?, ?>, Void> node ) {
		return org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getMenuTextForType( type );
	}
}
