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

package org.alice.stageide.properties;

import org.alice.ide.IDE;
import org.alice.ide.croquet.models.StandardExpressionState;
import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MutableRider;
import org.lgna.story.SThing;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent;
import edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener;

public class MutableRiderVehicleAdapter extends AbstractPropertyAdapter<SThing, MutableRider> {

	private HierarchyListener hierarchyListener;
	private org.lgna.project.virtualmachine.UserInstance sceneInstance;

	public MutableRiderVehicleAdapter( MutableRider instance, StandardExpressionState expressionState, org.lgna.project.virtualmachine.UserInstance sceneInstance )
	{
		super( "Vehicle", instance, expressionState );
		this.sceneInstance = sceneInstance;
		this.initializeExpressionState();
	}

	private void initializeListenersIfNecessary()
	{
		if( this.hierarchyListener == null )
		{
			this.hierarchyListener = new HierarchyListener()
			{
				public void hierarchyChanged( HierarchyEvent hierarchyEvent )
				{
					MutableRiderVehicleAdapter.this.handleHeirarchyChanged();
				}
			};
		}
	}

	@Override
	protected void setExpressionValue( SThing value )
	{
		if( ( this.expressionState != null ) && ( this.sceneInstance != null ) )
		{
			org.lgna.project.ast.Expression expressionValue;
			if( value != null )
			{
				org.lgna.story.SThing entity = value;
				if( entity instanceof org.lgna.story.SJoint ) {
					expressionValue = org.alice.stageide.sceneeditor.SetUpMethodGenerator.getGetterExpressionForJoint( (org.lgna.story.SJoint)entity, this.sceneInstance );
				}
				else {
					org.lgna.project.ast.AbstractField entityField = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( entity );
					org.lgna.project.ast.Expression thisExpression = new org.lgna.project.ast.ThisExpression();
					if( value instanceof org.lgna.story.SScene ) {
						expressionValue = thisExpression;
					}
					else {
						expressionValue = new org.lgna.project.ast.FieldAccess( thisExpression, entityField );
					}
				}
			}
			else
			{
				expressionValue = new NullLiteral();
			}
			this.expressionState.setValueTransactionlessly( expressionValue );
		}
	}

	@Override
	protected void intermediateSetValue( Object value )
	{
		if( value instanceof UserInstance )
		{
			Object instanceInJava = ( (UserInstance)value ).getJavaInstance();
			if( instanceInJava instanceof SThing )
			{
				value = instanceInJava;
			}
		}
		if( value instanceof SThing )
		{
			this.setValue( (SThing)value );
		}
		else {
			Logger.severe( "Trying to set vehicle expression to something other than an Entity.", value );
		}
	}

	@Override
	protected Object evaluateExpression( org.lgna.project.ast.Expression expression )
	{
		org.lgna.project.virtualmachine.VirtualMachine vm = org.alice.stageide.StageIDE.getActiveInstance().getVirtualMachineForSceneEditor();
		Object[] values = vm.ENTRY_POINT_evaluate( this.sceneInstance, new org.lgna.project.ast.Expression[] { expression } );
		assert values.length == 1;
		return values[ 0 ];
	}

	protected void handleHeirarchyChanged()
	{
		this.notifyValueObservers( this.getValue() );
	}

	public static String getNameForVehicle( SThing vehicle )
	{
		if( vehicle != null )
		{
			org.lgna.project.ast.AbstractField field = IDE.getActiveInstance().getSceneEditor().getFieldForInstanceInJavaVM( vehicle );
			if( field != null )
			{
				org.lgna.project.ast.AbstractType<?, ?, ?> valueType = field.getValueType();
				return field.getName();
			}
			else
			{
				return vehicle.getName() + ", " + vehicle.getClass().getSimpleName();
			}
		}
		else
		{
			return "No Vehicle";
		}
	}

	public static javax.swing.Icon getIconForVehicle( SThing vehicle )
	{
		if( vehicle != null )
		{
			org.lgna.project.ast.UserField field = IDE.getActiveInstance().getSceneEditor().getFieldForInstanceInJavaVM( vehicle );
			if( field != null )
			{
				org.lgna.croquet.icon.IconFactory iconFactory = org.alice.stageide.icons.IconFactoryManager.getIconFactoryForField( field );
				return iconFactory.getIcon( new java.awt.Dimension( 24, 18 ) );
			}
		}
		return null;
	}

	@Override
	public void setValue( SThing value )
	{
		super.setValue( value );
		if( this.instance != null )
		{
			this.instance.setVehicle( value );
		}
	}

	@Override
	public Class<SThing> getPropertyType()
	{
		return SThing.class;
	}

	@Override
	public SThing getValue()
	{
		if( this.instance != null )
		{
			return this.instance.getVehicle();
		}
		return null;
	}

	@Override
	public SThing getValueCopyIfMutable()
	{
		return this.getValue();
	}

	@Override
	protected void startPropertyListening()
	{
		super.startPropertyListening();
		if( this.instance != null )
		{
			this.initializeListenersIfNecessary();
			org.lgna.story.implementation.EntityImp imp = ImplementationAccessor.getImplementation( (SThing)this.instance );
			imp.getSgComposite().addHierarchyListener( this.hierarchyListener );
		}
	}

	@Override
	protected void stopPropertyListening()
	{
		super.stopPropertyListening();
		if( this.instance != null )
		{
			org.lgna.story.implementation.EntityImp imp = ImplementationAccessor.getImplementation( (SThing)this.instance );
			imp.getSgComposite().removeHierarchyListener( this.hierarchyListener );
		}
	}

}
