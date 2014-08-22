/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.poser;

import java.util.ArrayList;

import org.alice.ide.ProjectStack;
import org.alice.stageide.type.croquet.TypeNode;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ConstructorInvocationStatement;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.UserField;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.biped.OgreResource;

import edu.cmu.cs.dennisc.java.util.Lists;

/**
 * @author Matt May
 */
public class FieldFinder {

	private FieldFinder() {
	}

	private static class SingletonHolder {
		static final FieldFinder instance = new FieldFinder();
	}

	public static FieldFinder getInstance() {
		return SingletonHolder.instance;
	}

	private NamedUserType sceneType;
	private final org.lgna.project.virtualmachine.ReleaseVirtualMachine vm = new org.lgna.project.virtualmachine.ReleaseVirtualMachine();

	public void refreshScene() {
		sceneType = org.alice.stageide.ast.StoryApiSpecificAstUtilities.getSceneTypeFromProject( ProjectStack.peekProject() );
	}

	public ArrayList<JointedModelResource> getResourcesForType( NamedUserType type ) {
		ArrayList<JointedModelResource> rv = Lists.newArrayList();
		refreshScene();
		if( sceneType == null ) {
			JointedModelResource ogre = OgreResource.GREEN;
			return Lists.newArrayList( ogre );
		}
		java.util.List<UserField> fields = sceneType.getDeclaredFields();
		for( UserField field : fields ) {
			if( field.getManagementLevel().isManaged() ) {
				if( field.getValueType().isAssignableTo( type ) ) {
					InstanceCreation creation = (InstanceCreation)field.initializer.getValue();
					if( creation.requiredArguments.size() > 0 ) {
						SimpleArgument simpleArgument = creation.requiredArguments.get( 0 );
						Expression[] arr = new Expression[ 1 ];
						arr[ 0 ] = simpleArgument.expression.getValue();
						Object[] evaluated = vm.ENTRY_POINT_evaluate( null, arr );
						Object resource = evaluated[ 0 ];
						rv.add( (JointedModelResource)resource );
					} else {
						NamedUserConstructor constructor = (NamedUserConstructor)creation.constructor.getValue();
						ConstructorInvocationStatement constructorInvocationStatement = constructor.body.getValue().constructorInvocationStatement.getValue();
						SimpleArgument simpleArgument = constructorInvocationStatement.requiredArguments.get( 0 );

						Expression[] arr = new Expression[ 1 ];
						arr[ 0 ] = simpleArgument.expression.getValue();
						Object[] evaluated = vm.ENTRY_POINT_evaluate( null, arr );
						Object resource = evaluated[ 0 ];
						assert resource instanceof JointedModelResource : resource;
						rv.add( (JointedModelResource)resource );
					}
				}
			}
		}
		return rv;
	}

	public static TypeNode populateList( AbstractType<?, ?, ?> rootType ) {
		TypeNode rootNode = new TypeNode( rootType );
		org.lgna.project.Project project = org.alice.ide.ProjectStack.peekProject();
		Iterable<org.lgna.project.ast.NamedUserType> types = project.getNamedUserTypes();
		for( org.lgna.project.ast.NamedUserType type : types ) {
			if( type.isAssignableTo( rootType ) ) {
				TypeNode newNode = new TypeNode( type );
				insert( newNode, rootNode );
			}
		}
		return rootNode;
	}

	private static void insert( TypeNode newNode, TypeNode rootNode ) {
		for( int i = 0; i != rootNode.getChildCount(); ++i ) {
			TypeNode child = (TypeNode)rootNode.getChildAt( i );
			if( child.getType().isAssignableTo( newNode.getType() ) ) {
				rootNode.add( newNode );
				newNode.add( child );
			} else if( newNode.getType().isAssignableTo( child.getType() ) ) {
				insert( newNode, child );
			}
		}
		if( newNode.getParent() == null ) {
			rootNode.add( newNode );
		}
	}

	/**
	 * HACK FOR NOW
	 */
	public boolean isSceneTypeNull() {
		refreshScene();
		return sceneType != null;
	}
}
