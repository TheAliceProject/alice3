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
package org.lgna.ik.poser.animation.composites;

import java.util.UUID;

import org.alice.stageide.modelresource.ClassHierarchyBasedResourceNode;
import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.modelresource.EnumConstantResourceKey;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.modelresource.ResourceNodeTreeState;
import org.alice.stageide.modelresource.RootResourceKey;
import org.alice.stageide.modelresource.UpdatableRootResourceNodeTreeSelectionState;
import org.alice.stageide.type.croquet.TypeNode;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Model;
import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.ik.core.IKCore;
import org.lgna.ik.core.IKCore.Limb;
import org.lgna.ik.poser.FieldFinder;
import org.lgna.ik.poser.animation.composites.drops.TypeNodeSelectionState;
import org.lgna.ik.poser.controllers.PoserControllerAdapter;
import org.lgna.ik.poser.croquet.view.AbstractPoserControlView;
import org.lgna.ik.poser.input.AbstractPoserOrAnimatorInputDialogComposite;
import org.lgna.ik.poser.jselection.JointSelectionSphere;
import org.lgna.ik.poser.jselection.JointSelectionSphereState;
import org.lgna.ik.poser.scene.AbstractPoserScene;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserType;
import org.lgna.story.Color;
import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SJointedModel;
import org.lgna.story.SQuadruped;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointedModelResource;

import edu.cmu.cs.dennisc.property.Property;

/**
 * @author Matt May
 */
public abstract class AbstractPoserControlComposite<T extends AbstractPoserControlView> extends SimpleComposite<T> {

	private final JointSelectionSphereState rightArmAnchor;
	private final JointSelectionSphereState leftArmAnchor;
	private final JointSelectionSphereState rightLegAnchor;
	private final JointSelectionSphereState leftLegAnchor;
	private final StringValue rightArmLabel = this.createStringValue( "rightArm" );
	private final StringValue leftArmLabel = this.createStringValue( "leftArm" );
	private final StringValue rightLegLabel = this.createStringValue( "rightLeg" );
	private final StringValue leftLegLabel = this.createStringValue( "leftLeg" );
	private final StringValue typeSelectionLabel = this.createStringValue( "typeSelectionLabel" );
	private final BooleanState isUsingIK = createBooleanState( "isUsingIK", true );
	private final UpdatableRootResourceNodeTreeSelectionState resourceTree;
	//			createListSelectionState( "chooseResource" ), new RefreshableListData<JointedModelResource>( DefaultItemCodec.createInstance( JointedModelResource.class ) )
	private final BooleanState jointRotationHandleVisibilityState = createBooleanState( "showHandles", false );
	protected AbstractPoserOrAnimatorInputDialogComposite parent;
	private final PoserControllerAdapter adapter;
	private final TypeNode typeSelectionRoot;
	private final TypeNodeSelectionState typeSelectionState;
	public final boolean typeSwitchingEnabled;

	public AbstractPoserControlComposite( AbstractPoserOrAnimatorInputDialogComposite parent, UUID uid ) {
		super( uid );
		this.parent = parent;
		parent.getJointSelectionSheres();
		AbstractPoserScene scene = parent.getScene();
		rightArmAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKCore.Limb.RIGHT_ARM ), scene.getJointsForLimb( IKCore.Limb.RIGHT_ARM ) );
		leftArmAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKCore.Limb.LEFT_ARM ), scene.getJointsForLimb( IKCore.Limb.LEFT_ARM ) );
		rightLegAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKCore.Limb.RIGHT_LEG ), scene.getJointsForLimb( IKCore.Limb.RIGHT_LEG ) );
		leftLegAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKCore.Limb.LEFT_LEG ), scene.getJointsForLimb( IKCore.Limb.LEFT_LEG ) );
		rightArmAnchor.getValue().setPaint( Color.GREEN );
		leftArmAnchor.getValue().setPaint( Color.GREEN );
		rightLegAnchor.getValue().setPaint( Color.GREEN );
		leftLegAnchor.getValue().setPaint( Color.GREEN );
		adapter = new PoserControllerAdapter( this );
		parent.setAdapter( adapter );
		typeSwitchingEnabled = FieldFinder.getInstance().isSceneTypeNull();
		if( typeSwitchingEnabled ) {
			typeSelectionRoot = initializeRootTypeNode();
			TypeNode initialValue = getInitialValue( typeSelectionRoot );
			typeSelectionState = new TypeNodeSelectionState( AnimatorControlComposite.GROUP, initialValue, typeSelectionRoot );
			typeSelectionState.addNewSchoolValueListener( typeChangedListener );
			//		resourceList.addNewSchoolValueListener( resourceChangeListener );
			ResourceNode updateRoot = updateRoot( org.alice.stageide.modelresource.ClassHierarchyBasedResourceNodeTreeState.getInstance().getTreeModel().getRoot() );
			resourceTree = new UpdatableRootResourceNodeTreeSelectionState( updateRoot );
		} else {
			resourceTree = null;
			typeSelectionState = null;
			typeSelectionRoot = null;
		}
	}

	private org.lgna.croquet.event.ValueListener<JointedModelResource> resourceChangeListener = new org.lgna.croquet.event.ValueListener<JointedModelResource>() {

		public void valueChanged( ValueEvent<JointedModelResource> e ) {
			( (JointedModelImp)org.lgna.story.EmployeesOnly.getImplementation( parent.getModel() ) ).setNewResource( e.getNextValue() );
		}
	};

	private org.lgna.croquet.event.ValueListener<TypeNode> typeChangedListener = new org.lgna.croquet.event.ValueListener<TypeNode>() {

		public void valueChanged( ValueEvent<TypeNode> e ) {
			NamedUserType type = (NamedUserType)e.getNextValue().getType();
			parent.setType( type );
		}
	};

	private TypeNode initializeRootTypeNode() {
		SJointedModel model = parent.getModel();
		org.lgna.project.ast.JavaType rootType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJointedModel.class );
		if( model instanceof SBiped ) {
			rootType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SBiped.class );
		} else if( model instanceof SQuadruped ) {
			rootType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SQuadruped.class );
		} else if( model instanceof SFlyer ) {
			rootType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SFlyer.class );
		}
		TypeNode javaRoot = FieldFinder.populateList( rootType );
		assert !javaRoot.isLeaf();
		return (TypeNode)javaRoot.getChildAt( 0 );
	}

	private TypeNode getInitialValue( TypeNode root ) {
		if( root.getType().equals( parent.getDeclaringType() ) ) {
			return root;
		}
		for( int i = 0; i != root.getChildCount(); ++i ) {
			TypeNode child = getInitialValue( (TypeNode)root.getChildAt( i ) );
			if( child != null ) {
				return child;
			}
		}
		return null;
	}

	protected ActionOperation straightenJointsOperation = createActionOperation( "straightenJoints", new Action() {

		public AbstractEdit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			parent.getModel().straightenOutJoints();
			return null;
		}

	} );

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
	};

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
	}

	public BooleanState getIsUsingIK() {
		return this.isUsingIK;
	}

	public ActionOperation getStraightenJointsOperation() {
		return this.straightenJointsOperation;
	}

	public void addRightArmAnchorListener( ValueListener<JointSelectionSphere> listener ) {
		rightArmAnchor.addValueListener( listener );
	}

	public void addRightLegAnchorListener( ValueListener<JointSelectionSphere> listener ) {
		rightLegAnchor.addValueListener( listener );
	}

	public void addLeftArmAnchorListener( ValueListener<JointSelectionSphere> listener ) {
		leftArmAnchor.addValueListener( listener );
	}

	public void addLeftLegAnchorListener( ValueListener<JointSelectionSphere> listener ) {
		leftLegAnchor.addValueListener( listener );
	}

	public StringValue getRightArmLabel() {
		return this.rightArmLabel;
	}

	public StringValue getLeftArmLabel() {
		return this.leftArmLabel;
	}

	public StringValue getRightLegLabel() {
		return this.rightLegLabel;
	}

	public StringValue getLeftLegLabel() {
		return this.leftLegLabel;
	}

	public JointSelectionSphereState getRightArmAnchor() {
		return this.rightArmAnchor;
	}

	public JointSelectionSphereState getRightLegAnchor() {
		return this.rightLegAnchor;
	}

	public JointSelectionSphereState getLeftArmAnchor() {
		return this.leftArmAnchor;
	}

	public JointSelectionSphereState getLeftLegAnchor() {
		return this.leftLegAnchor;
	}

	public void updateSphere( Limb limb, JointSelectionSphere sphere ) {
		switch( limb ) {
		case LEFT_ARM:
			leftArmAnchor.setValueTransactionlessly( sphere );
			break;
		case LEFT_LEG:
			leftLegAnchor.setValueTransactionlessly( sphere );
			break;
		case RIGHT_ARM:
			rightArmAnchor.setValueTransactionlessly( sphere );
			break;
		case RIGHT_LEG:
			rightLegAnchor.setValueTransactionlessly( sphere );
			break;
		}
	}

	private ResourceNode updateRoot( ResourceNode node ) {
		ResourceNode rv = null;
		if( node instanceof ClassHierarchyBasedResourceNode ) {
			ClassHierarchyBasedResourceNode chbrNode = (ClassHierarchyBasedResourceNode)node;
			for( Model o : chbrNode.getChildren() ) {
				System.out.println( "child " + o.getClass() );
			}
		}
		node.getChildren();
		ResourceKey resourceKey = node.getResourceKey();
		if( resourceKey instanceof RootResourceKey ) {
			for( ResourceNode child : node.getNodeChildren() ) {
				rv = updateRoot( child );
				if( rv != null ) {
					return rv;
				}
			}
		} else if( resourceKey instanceof ClassResourceKey ) {
			JavaType resourceType = ( (ClassResourceKey)resourceKey ).getType();

			AbstractType<?, ?, ?> type = typeSelectionState.getValue().getType();
			for( Property<?> o : resourceType.getProperties() ) {
			}
			System.out.println( type.getPropertyNamed( "superType" ) );
			if( type.isAssignableTo( resourceType ) ) {
				for( ResourceNode child : node.getNodeChildren() ) {
					rv = updateRoot( child );
					if( rv != null ) {
						return rv;
					}
				}
				return node;
			}
		} else if( resourceKey instanceof EnumConstantResourceKey ) {
			//do nothing leafs
			return null;
		} else {
			System.out.println( " ERROR: " + resourceKey.getClass() );
		}
		return node;
	}

	public UserType<?> getDeclaringType() {
		return parent.getDeclaringType();
	}

	public PoserControllerAdapter getAdapter() {
		return this.adapter;
	}

	public BooleanState getJointRotationHandleVisibilityState() {
		return jointRotationHandleVisibilityState;
	}

	public ResourceNodeTreeState getResourceList() {
		return this.resourceTree;
	}

	private void updateResourceRoot() {
	}

	public TypeNodeSelectionState getTypeSelectionState() {
		return this.typeSelectionState;
	}

	public StringValue getTypeSelectionLabel() {
		return this.typeSelectionLabel;
	}
}
