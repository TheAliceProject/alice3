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
package org.alice.ide.ast.export;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.tree.DefaultNode;
import edu.cmu.cs.dennisc.tree.Node;
import org.lgna.project.Project;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserType;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class ProjectInfo {
	private final Map<UserType<?>, TypeInfo> typeInfoMap;
	private final DefaultNode<TypeInfo> root = DefaultNode.createUnsafeInstance( null, TypeInfo.class );
	private boolean isInTheMidstOfChange;

	public ProjectInfo( Project project ) {
		Set<NamedUserType> types = project.getNamedUserTypes();
		Map<UserType<?>, TypeInfo> map = Maps.newHashMap();
		for( NamedUserType type : types ) {
			map.put( type, new TypeInfo( this, type ) );
		}
		this.typeInfoMap = Collections.unmodifiableMap( map );

		Map<TypeInfo, DefaultNode<TypeInfo>> mapInfoToNode = Maps.newHashMap();
		Collection<TypeInfo> typeInfos = this.getTypeInfos();
		for( TypeInfo typeInfo : typeInfos ) {
			typeInfo.updateDependencies();
			mapInfoToNode.put( typeInfo, DefaultNode.createUnsafeInstance( typeInfo, TypeInfo.class ) );
		}
		mapInfoToNode.put( null, this.root );
		for( TypeInfo typeInfo : typeInfos ) {
			DefaultNode<TypeInfo> node = mapInfoToNode.get( typeInfo );
			DefaultNode<TypeInfo> parent = mapInfoToNode.get( typeInfo.getSuperTypeInfo() );
			parent.addChild( node );
		}

		Collections.sort( this.root.getChildren(), new Comparator<DefaultNode<TypeInfo>>() {
			@Override
			public int compare( DefaultNode<TypeInfo> o1, DefaultNode<TypeInfo> o2 ) {
				return o1.getValue().getDeclaration().getName().compareTo( o2.getValue().getDeclaration().getName() );
			}
		} );
	}

	public Node<TypeInfo> getTypeInfosAsTree() {
		return this.root;
	}

	public Collection<TypeInfo> getTypeInfos() {
		return this.typeInfoMap.values();
	}

	public TypeInfo getInfoForType( UserType<?> type ) {
		return this.typeInfoMap.get( type );
	}

	public boolean isInTheMidstOfChange() {
		return this.isInTheMidstOfChange;
	}

	public void update() {
		this.isInTheMidstOfChange = true;
		try {
			for( TypeInfo typeInfo : this.getTypeInfos() ) {
				typeInfo.resetRequired();
			}
			List<DeclarationInfo<?>> desired = Lists.newLinkedList();
			for( TypeInfo typeInfo : this.getTypeInfos() ) {
				typeInfo.appendDesired( desired );
			}
			Set<DeclarationInfo<?>> set = Sets.newHashSet();
			for( DeclarationInfo<?> declarationInfo : desired ) {
				declarationInfo.updateRequired( set );
			}
			for( TypeInfo typeInfo : this.getTypeInfos() ) {
				typeInfo.updateSwing();
			}
		} finally {
			this.isInTheMidstOfChange = false;
		}
	}
}
