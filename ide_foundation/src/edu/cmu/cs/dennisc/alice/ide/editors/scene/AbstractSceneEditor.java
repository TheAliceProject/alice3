/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.alice.ide.editors.scene;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSceneEditor extends edu.cmu.cs.dennisc.alice.ide.IDEListenerPane implements edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = null;
	
	private static final String CAMERA_NAVIGATION_DISTANCE_KEY = AbstractSceneEditor.class.getName() + ".CAMERA_NAVIGATION_DISTANCE";
	private static final String CAMERA_NAVIGATION_YAW_IN_RADIANS_KEY = AbstractSceneEditor.class.getName() + ".CAMERA_NAVIGATION_YAW_IN_RADIANS_KEY";
	private static final String CAMERA_NAVIGATION_TARGET_X_KEY = AbstractSceneEditor.class.getName() + ".CAMERA_NAVIGATION_TARGET_X_KEY";
	private static final String CAMERA_NAVIGATION_TARGET_Y_KEY = AbstractSceneEditor.class.getName() + ".CAMERA_NAVIGATION_TARGET_Y_KEY";
	private static final String CAMERA_NAVIGATION_TARGET_Z_KEY = AbstractSceneEditor.class.getName() + ".CAMERA_NAVIGATION_TARGET_Z_KEY";
//	private static final String CAMERA_NAVIGATION_PITCH_IN_RADIANS_KEY = AbstractSceneEditor.class.getName() + ".CAMERA_NAVIGATION_PITCH_IN_RADIANS_KEY";
	protected void restoreCameraNavigationProperties( edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter ) {
		edu.cmu.cs.dennisc.alice.Project project = getIDE().getProject();
		if( project != null ) {
			edu.cmu.cs.dennisc.alice.Project.Properties properties = project.getProperties();
			if( properties != null ) {
				double distance = properties.getDouble( CAMERA_NAVIGATION_DISTANCE_KEY, 16.0 );
				double yawInRadians = properties.getDouble( CAMERA_NAVIGATION_YAW_IN_RADIANS_KEY, Math.PI );
				//double pitchInRadians = project.getProperties().getDouble( CAMERA_NAVIGATION_PITCH_IN_RADIANS_KEY, 0.0 );
				edu.cmu.cs.dennisc.math.Point3 target = new edu.cmu.cs.dennisc.math.Point3();
				target.x = properties.getDouble( CAMERA_NAVIGATION_TARGET_X_KEY, 0.0 );
				target.y = properties.getDouble( CAMERA_NAVIGATION_TARGET_Y_KEY, 0.0 );
				target.z = properties.getDouble( CAMERA_NAVIGATION_TARGET_Z_KEY, 0.0 );
				cameraNavigationDragAdapter.requestDistance( distance );
				cameraNavigationDragAdapter.requestYaw( new edu.cmu.cs.dennisc.math.AngleInRadians( yawInRadians ) );
				cameraNavigationDragAdapter.requestTarget( target );
			}
		}
	}
	protected void preserveCameraNavigationProperties( edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter ) {
		if( cameraNavigationDragAdapter != null ) {
			edu.cmu.cs.dennisc.alice.Project project = getIDE().getProject();
			if( project != null ) {
				edu.cmu.cs.dennisc.alice.Project.Properties properties = project.getProperties();
				if( properties != null ) {
					properties.putDouble( CAMERA_NAVIGATION_DISTANCE_KEY, cameraNavigationDragAdapter.getDistanceRequested() );
					properties.putDouble( CAMERA_NAVIGATION_YAW_IN_RADIANS_KEY, cameraNavigationDragAdapter.getYawRequested().getAsRadians() );
					
					edu.cmu.cs.dennisc.math.Point3 target = cameraNavigationDragAdapter.getTargetRequested();
					if( target != null ) {
						properties.putDouble( CAMERA_NAVIGATION_TARGET_X_KEY, target.x );
						properties.putDouble( CAMERA_NAVIGATION_TARGET_Y_KEY, target.y );
						properties.putDouble( CAMERA_NAVIGATION_TARGET_Z_KEY, target.z );
					}
				} else {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: preserveCameraNavigationProperties: properties == null" );
				}
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: preserveCameraNavigationProperties: project == null" );
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: preserveCameraNavigationProperties: cameraNavigationDragAdapter == null" );
		}
	}
	
	protected edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVM() {
		return getIDE().getVirtualMachineForSceneEditor();
	}
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getSceneTypeDeclaredInAlice() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.sceneField.getDeclaringType();
	}
	public void addFieldToSceneType( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		getSceneTypeDeclaredInAlice().fields.add( field );
	}
	protected void setSceneField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		if( this.sceneField != null ) {
			this.sceneField.removeListPropertyListener( this );
		}
		this.sceneField = sceneField;
		if( this.sceneField != null ) {
			this.sceneField.addListPropertyListener( this );
		}
	}
	protected void setProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType programType ) {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldInAlice;
		if( programType != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = programType.getDeclaredFields().get( 0 );
			if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
				fieldInAlice = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field;
			} else {
				fieldInAlice = null;
			}
		} else {
			fieldInAlice = null;
		}
		setSceneField( fieldInAlice );
	}
	@Override
	public void projectOpened( edu.cmu.cs.dennisc.alice.ide.event.ProjectOpenEvent e ) {
		super.projectOpened( e );
		edu.cmu.cs.dennisc.alice.Project project = e.getNextValue();
		this.setProgramType( project.getProgramType() );
		this.revalidate();
		this.repaint();
	}
	public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
}
