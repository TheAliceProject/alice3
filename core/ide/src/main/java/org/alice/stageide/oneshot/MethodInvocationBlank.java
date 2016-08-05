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

package org.alice.stageide.oneshot;

import org.alice.nonfree.NebulousIde;

/**
 * @author Dennis Cosgrove
 */
public class MethodInvocationBlank extends org.lgna.croquet.CascadeBlank<MethodInvocationEditFactory> {
	private static java.util.Map<org.alice.ide.instancefactory.InstanceFactory, MethodInvocationBlank> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static MethodInvocationBlank getInstance( org.alice.ide.instancefactory.InstanceFactory value ) {
		synchronized( map ) {
			MethodInvocationBlank rv = map.get( value );
			if( rv != null ) {
				//pass
			} else {
				rv = new MethodInvocationBlank( value );
				map.put( value, rv );
			}
			return rv;
		}
	}

	private final org.alice.ide.instancefactory.InstanceFactory instanceFactory;

	private MethodInvocationBlank( org.alice.ide.instancefactory.InstanceFactory instanceFactory ) {
		super( java.util.UUID.fromString( "3c5f528b-340b-4bcc-8094-3475867d2f6e" ) );
		this.instanceFactory = instanceFactory;
	}

	@Override
	protected void updateChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> children, org.lgna.croquet.imp.cascade.BlankNode<MethodInvocationEditFactory> blankNode ) {
		org.lgna.project.ast.JavaType turnableType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.STurnable.class );
		org.lgna.project.ast.JavaType movableTurnableType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SMovableTurnable.class );
		org.lgna.project.ast.JavaType jointedModelType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJointedModel.class );
		org.lgna.project.ast.JavaType flyerType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SFlyer.class );
		org.lgna.project.ast.JavaType cameraType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SCamera.class );
		org.lgna.project.ast.JavaType groundType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SGround.class );
		org.lgna.project.ast.JavaType modelType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SModel.class );

		org.lgna.project.ast.AbstractType<?, ?, ?> instanceFactoryValueType = this.instanceFactory.getValueType();
		java.util.List<org.lgna.project.ast.JavaMethod> methods = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.Map<org.lgna.project.ast.MethodInvocation, java.util.List<org.lgna.project.ast.SimpleArgument>> poseMethods = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		if( turnableType.isAssignableFrom( instanceFactoryValueType ) ) {
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.TURN_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.ROLL_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.TURN_TO_FACE_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.POINT_AT_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.ORIENT_TO_UPRIGHT_METHOD );
		}
		if( movableTurnableType.isAssignableFrom( instanceFactoryValueType ) ) {
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.MOVE_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.MOVE_TOWARD_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.MOVE_AWAY_FROM_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.MOVE_TO_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.MOVE_AND_ORIENT_TO_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.PLACE_METHOD );
		}

		if( jointedModelType.isAssignableFrom( instanceFactoryValueType ) ) {
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.STRAIGHTEN_OUT_JOINTS_METHOD );

			if( flyerType.isAssignableFrom( instanceFactoryValueType ) ) {
				methods.add( org.alice.stageide.ast.sort.OneShotSorter.SPREAD_WINGS_METHOD );
				methods.add( org.alice.stageide.ast.sort.OneShotSorter.FOLD_WINGS_METHOD );
			}

			//Search the UserMethods on the type looking for generated pose animations
			//Grab the java method invocation (strikePose) inside the pose animation and add it to the methodInvocations list
			java.util.List<org.lgna.project.ast.AbstractMethod> declaredMethods = org.lgna.project.ast.AstUtilities.getAllMethods( instanceFactoryValueType );
			for( org.lgna.project.ast.AbstractMethod method : declaredMethods ) {
				if( method instanceof org.lgna.project.ast.UserMethod ) {
					org.lgna.project.ast.UserMethod userMethod = (org.lgna.project.ast.UserMethod)method;
					//Pose animations are GENERATED and have no return value
					if( ( userMethod.managementLevel.getValue() == org.lgna.project.ast.ManagementLevel.GENERATED ) && ( userMethod.getReturnType() == org.lgna.project.ast.JavaType.VOID_TYPE ) ) {
						//UserMethod pose animations contain a single JavaMethod in their body called "strikePose"
						//Grab the first statement in the body and check to see if it's actually a pose call
						org.lgna.project.ast.Statement poseStatement = userMethod.body.getValue().statements.get( 0 );
						if( poseStatement instanceof org.lgna.project.ast.ExpressionStatement ) {
							org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)poseStatement;
							org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
							if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
								org.lgna.project.ast.MethodInvocation poseInvocation = (org.lgna.project.ast.MethodInvocation)expression;
								if( "strikePose".equals( poseInvocation.method.getValue().getName() ) ) {
									if( poseInvocation.method.getValue() instanceof org.lgna.project.ast.JavaMethod ) {
										java.util.List<org.lgna.project.ast.SimpleArgument> arguments = poseInvocation.requiredArguments.getValue();
										poseMethods.put( poseInvocation, arguments );
									}
								}
							}
						}
					}
				}
			}

		}
		if( cameraType.isAssignableFrom( instanceFactoryValueType ) ) {
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.MOVE_AND_ORIENT_TO_A_GOOD_VANTAGE_POINT_METHOD );
		}

		if( groundType.isAssignableFrom( instanceFactoryValueType ) ) {
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.GROUND_SET_PAINT_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.GROUND_SET_OPACITY_METHOD );
		}
		NebulousIde.nonfree.addRoomMethods( instanceFactoryValueType, methods );
		if( modelType.isAssignableFrom( instanceFactoryValueType ) ) {
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.MODEL_SET_PAINT_METHOD );
			methods.add( org.alice.stageide.ast.sort.OneShotSorter.MODEL_SET_OPACITY_METHOD );
		}

		java.util.List<org.lgna.project.ast.JavaMethod> sortedMethods = org.alice.stageide.ast.sort.OneShotSorter.SINGLETON.createSortedList( methods );
		for( org.lgna.project.ast.JavaMethod method : sortedMethods ) {
			if( method != null ) {
				org.lgna.croquet.CascadeBlankChild<?> roomFillin = NebulousIde.nonfree.getRoomFillIns( method, this.instanceFactory );
				//todo
				if( method == org.alice.stageide.ast.sort.OneShotSorter.STRAIGHTEN_OUT_JOINTS_METHOD ) {
					children.add( AllJointLocalTransformationsMethodInvocationFillIn.getInstance( this.instanceFactory, method ) );
				} else if( "setPaint".equals( method.getName() ) ) {
					children.add( SetPaintMethodInvocationFillIn.getInstance( this.instanceFactory, method ) );
				} else if( roomFillin != null ) {
					children.add( roomFillin );
				} else if( "setOpacity".equals( method.getName() ) ) {
					children.add( SetOpacityMethodInvocationFillIn.getInstance( this.instanceFactory, method ) );
				} else if( "foldWings".equals( method.getName() ) || "spreadWings".equals( method.getName() ) ) {
					children.add( JavaDefinedStrikePoseMethodInvocationFillIn.getInstance( this.instanceFactory, method ) );
				} else {
					children.add( LocalTransformationMethodInvocationFillIn.getInstance( this.instanceFactory, method ) );
				}
			} else {
				children.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			}
		}
		for( java.util.Map.Entry<org.lgna.project.ast.MethodInvocation, java.util.List<org.lgna.project.ast.SimpleArgument>> poseMethodEntry : poseMethods.entrySet() ) {
			children.add( StrikePoseMethodInvocationFillIn.getInstance( this.instanceFactory, (org.lgna.project.ast.JavaMethod)poseMethodEntry.getKey().method.getValue(), poseMethodEntry.getValue() ) );
		}
	}
}
