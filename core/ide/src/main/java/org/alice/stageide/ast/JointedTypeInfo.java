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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaType;
import org.lgna.story.SJointedModel;

import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class JointedTypeInfo {
	private static final JavaType JOINTED_MODEL_TYPE = JavaType.getInstance( SJointedModel.class );

	private static Map<AbstractType<?, ?, ?>, JointedTypeInfo> map = Maps.newHashMap();

	public static JointedTypeInfo getDeclarationInstance( AbstractType<?, ?, ?> type ) {
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

	public static List<JointedTypeInfo> getInstances( AbstractType<?, ?, ?> type ) {
		List<JointedTypeInfo> rv = Lists.newLinkedList();
		AbstractType<?, ?, ?> t = type;
		while( JOINTED_MODEL_TYPE.isAssignableFrom( t ) ) {
			JointedTypeInfo jointedTypeInfo = getDeclarationInstance( t );
			if( jointedTypeInfo != null ) {
				rv.add( 0, jointedTypeInfo );
			}
			t = t.getSuperType();
		}
		return rv;
	}

	public static boolean isDeclarationJointed( AbstractType<?, ?, ?> type ) {
		JointedTypeInfo info = getDeclarationInstance( type );
		return ( info != null ) && ( info.jointGetters.size() > 0 );
	}

	public static boolean isJointed( AbstractType<?, ?, ?> type ) {
		AbstractType<?, ?, ?> t = type;
		while( JOINTED_MODEL_TYPE.isAssignableFrom( t ) ) {
			if( isDeclarationJointed( t ) ) {
				return true;
			}
			t = t.getSuperType();
		}
		return false;
	}

	private final AbstractType<?, ?, ?> type;
	private final List<AbstractMethod> jointGetters = Lists.newLinkedList();
	private final List<JointMethodArrayAccessInfo> jointArrayAccessGetters = Lists.newLinkedList();

	//Can we add in "joint array accessors"?

	private JointedTypeInfo( AbstractType<?, ?, ?> type ) {
		this.type = type;
		for( AbstractMethod method : type.getDeclaredMethods() ) {
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

	public AbstractType<?, ?, ?> getType() {
		return this.type;
	}

	public List<AbstractMethod> getJointGetters() {
		return this.jointGetters;
	}

	public List<JointMethodArrayAccessInfo> getJointArrayAccessGetters() {
		return this.jointArrayAccessGetters;
	}

	public static class Node {
		private final Node parent;
		private final AbstractMethod method;
		private final List<Node> children = Lists.newLinkedList();

		public static Node createAndAddToParent( Node parent, AbstractMethod method ) {
			Node rv = new Node( parent, method );
			if( parent != null ) {
				parent.children.add( rv );
			}
			return rv;
		}

		private Node( Node parent, AbstractMethod method ) {
			this.parent = parent;
			this.method = method;
		}
	}
}
