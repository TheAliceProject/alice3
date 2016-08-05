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

package org.alice.stageide.ast;

/**
 * @author Dennis Cosgrove
 */
public class JointedTypeInfo {
	private static final org.lgna.project.ast.JavaType JOINTED_MODEL_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJointedModel.class );

	private static java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, JointedTypeInfo> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static JointedTypeInfo getDeclarationInstance( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( JOINTED_MODEL_TYPE.isAssignableFrom( type ) ) {
			JointedTypeInfo rv = map.get( type );
			if( rv != null ) {
				//pass
			} else {
				rv = new JointedTypeInfo( type );
				JointedTypeInfo.map.put( type, rv );
			}
			if( rv.jointGetters.size() > 0 ) {
				return rv;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static java.util.List<JointedTypeInfo> getInstances( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		java.util.List<JointedTypeInfo> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		org.lgna.project.ast.AbstractType<?, ?, ?> t = type;
		while( JOINTED_MODEL_TYPE.isAssignableFrom( t ) ) {
			JointedTypeInfo jointedTypeInfo = getDeclarationInstance( t );
			if( jointedTypeInfo != null ) {
				rv.add( 0, jointedTypeInfo );
			}
			t = t.getSuperType();
		}
		return rv;
	}

	public static boolean isDeclarationJointed( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		JointedTypeInfo info = getDeclarationInstance( type );
		return ( info != null ) && ( info.jointGetters.size() > 0 );
	}

	public static boolean isJointed( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> t = type;
		while( JOINTED_MODEL_TYPE.isAssignableFrom( t ) ) {
			if( isDeclarationJointed( t ) ) {
				return true;
			}
			t = t.getSuperType();
		}
		return false;
	}

	private final org.lgna.project.ast.AbstractType<?, ?, ?> type;
	private final java.util.List<org.lgna.project.ast.AbstractMethod> jointGetters = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private final java.util.List<JointMethodArrayAccessInfo> jointArrayAccessGetters = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	//Can we add in "joint array accessors"?

	private JointedTypeInfo( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		this.type = type;
		for( org.lgna.project.ast.AbstractMethod method : type.getDeclaredMethods() ) {
			if( JointMethodUtilities.isJointGetter( method ) ) {
				this.jointGetters.add( method );
			} else if( JointMethodUtilities.isJointArrayGetter( method ) ) {
				int length = JointMethodUtilities.getJointArrayLength( method );
				for( int i = 0; i < length; i++ ) {
					this.jointArrayAccessGetters.add( new JointMethodArrayAccessInfo( method, i ) );
				}
			}
		}
	}

	public org.lgna.project.ast.AbstractType<?, ?, ?> getType() {
		return this.type;
	}

	public java.util.List<org.lgna.project.ast.AbstractMethod> getJointGetters() {
		return this.jointGetters;
	}

	public java.util.List<JointMethodArrayAccessInfo> getJointArrayAccessGetters() {
		return this.jointArrayAccessGetters;
	}

	public static class Node {
		private final Node parent;
		private final org.lgna.project.ast.AbstractMethod method;
		private final java.util.List<Node> children = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		public static Node createAndAddToParent( Node parent, org.lgna.project.ast.AbstractMethod method ) {
			Node rv = new Node( parent, method );
			if( parent != null ) {
				parent.children.add( rv );
			}
			return rv;
		}

		private Node( Node parent, org.lgna.project.ast.AbstractMethod method ) {
			this.parent = parent;
			this.method = method;
		}
	}
}
