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
package org.alice.stageide.sceneeditor;

import org.alice.apis.moveandturn.gallery.environments.Ground;
import org.alice.apis.moveandturn.gallery.environments.grounds.DirtGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.MoonSurface;
import org.alice.apis.moveandturn.gallery.environments.grounds.SandyGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.SeaSurface;
import org.alice.apis.moveandturn.gallery.environments.grounds.SnowyGround;

/**
 * @author Dennis Cosgrove
 */
public class MoveAndTurnSceneEditor extends org.alice.ide.sceneeditor.AbstractInstantiatingSceneEditor {
	private Program program = this.createProgram();
	private edu.cmu.cs.dennisc.lookingglass.util.CardPane cardPane;
	private org.alice.stageide.sceneeditor.ControlsForOverlayPane controlsForOverlayPane;
	private edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();

	private org.alice.interact.GlobalDragAdapter globalDragAdapter = new org.alice.interact.GlobalDragAdapter();

	public MoveAndTurnSceneEditor() {
		this.setLayout( new java.awt.BorderLayout() );
		this.program.setArgs( new String[] {} );
		this.program.init();
		this.program.start();
		new Thread() {
			@Override
			public void run() {
				edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;
				while( true ) {
					onscreenLookingGlass = MoveAndTurnSceneEditor.this.program.getOnscreenLookingGlass();
					if( onscreenLookingGlass != null ) {
						break;
					} else {
						edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
					}
				}
				MoveAndTurnSceneEditor.this.cardPane = new edu.cmu.cs.dennisc.lookingglass.util.CardPane( onscreenLookingGlass );
				MoveAndTurnSceneEditor.this.add( MoveAndTurnSceneEditor.this.cardPane, java.awt.BorderLayout.CENTER );
				MoveAndTurnSceneEditor.this.cameraNavigationDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
				MoveAndTurnSceneEditor.this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
				edu.cmu.cs.dennisc.animation.Animator animator = MoveAndTurnSceneEditor.this.program.getAnimator();
				while( animator == null ) {
					edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
					animator = MoveAndTurnSceneEditor.this.program.getAnimator();
					//edu.cmu.cs.dennisc.print.PrintUtilities.println( animator );
				}
				MoveAndTurnSceneEditor.this.globalDragAdapter.setAnimator( animator );
				MoveAndTurnSceneEditor.this.globalDragAdapter.addPropertyListener( new org.alice.interact.event.SelectionListener() {
					public void selecting( org.alice.interact.event.SelectionEvent e ) {
					}
					public void selected( org.alice.interact.event.SelectionEvent e ) {
						MoveAndTurnSceneEditor.this.handleSelection( e );
					}
				} );
			}
		}.start();
	}

	private void handleSelection( org.alice.interact.event.SelectionEvent e ) {
		edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = e.getTransformable();
		org.alice.apis.moveandturn.Element element = org.alice.apis.moveandturn.Element.getElement( sgTransformable );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.getFieldForInstanceInJava( element );
		if( field != null ) {
			this.getIDE().setFieldSelection( field );
		}
	}

	private boolean isExpanded = false;

	private void updateSceneBasedOnScope() {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = this.getIDE().getFocusedCode();
		if( code != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = code.getDeclaringType();
			edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
			if( sceneField != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
				boolean isSceneScope = type.isAssignableFrom( sceneType ) || this.isExpanded;
				java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractField > fields = sceneType.getDeclaredFields();

				try {
					edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera = this.getProgram().getOnscreenLookingGlass().getCameraAt( 0 );
					if( isSceneScope ) {
						camera.background.setValue( null );
					} else {
						cameraBackground.color.setValue( edu.cmu.cs.dennisc.color.Color4f.BLACK );
						camera.background.setValue( cameraBackground );
					}
				} catch( Throwable t ) {
					//pass
				}
				//				Object sceneInstanceInJava = this.getInstanceInJavaForField( sceneField );
				//				if( sceneInstanceInJava instanceof org.alice.apis.moveandturn.Scene ) {
				//					org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)sceneInstanceInJava;
				//					if( isSceneScope ) {
				//						scene.setAtmosphereColor( new org.alice.apis.moveandturn.Color( 0.75f, 0.75f, 1.0f ), org.alice.apis.moveandturn.Scene.RIGHT_NOW );
				//					} else {
				//						scene.setAtmosphereColor( org.alice.apis.moveandturn.Color.BLACK, org.alice.apis.moveandturn.Scene.RIGHT_NOW );
				//					}
				//				}
				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : fields ) {
					Object instanceInJava = this.getInstanceInJavaForField( field );
					if( instanceInJava instanceof org.alice.apis.moveandturn.Model ) {
						org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instanceInJava;
						if( isSceneScope || type.isAssignableTo( model.getClass() ) ) {
							model.setColor( org.alice.apis.moveandturn.Color.WHITE, org.alice.apis.moveandturn.Model.RIGHT_NOW );
							//							model.setOpacity( 1.0f, org.alice.apis.moveandturn.Model.RIGHT_NOW );
						} else {
							model.setColor( new org.alice.apis.moveandturn.Color( 0.25, 0.25, 0.25 ), org.alice.apis.moveandturn.Model.RIGHT_NOW );
							//							model.setOpacity( 0.125f, org.alice.apis.moveandturn.Model.RIGHT_NOW );
						}
					}
				}
			}
		}
	}

	@Override
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		super.focusedCodeChanged( e );
		this.updateSceneBasedOnScope();
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = e.getNextValue();
		if( code != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = code.getDeclaringType();

			edu.cmu.cs.dennisc.alice.ast.AbstractField selectedField = this.getIDE().getFieldSelection();
			if( selectedField != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType selectedType = selectedField.getValueType();
				if( type.isAssignableFrom( selectedType ) ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
					if( sceneField != null ) {
						edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
						if( type.isAssignableFrom( sceneType ) ) {
							//pass
						} else {
							for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
								if( type.isAssignableFrom( field.getValueType() ) ) {
									this.getIDE().setFieldSelection( field );
									break;
								}
							}
						}
					}
				}
			}
		}
		this.controlsForOverlayPane.focusedCodeChanged( e );
	}
	public void handleExpandContractChange( boolean isExpanded ) {
		this.isExpanded = isExpanded;
		this.updateSceneBasedOnScope();
	}

	private static boolean isGround( org.alice.apis.moveandturn.Model model ) {
		Class[] clses = { Ground.class, GrassyGround.class, DirtGround.class, MoonSurface.class, SandyGround.class, SeaSurface.class, SnowyGround.class };
		for( Class< ? extends org.alice.apis.moveandturn.PolygonalModel > cls : clses ) {
			if( cls.isAssignableFrom( model.getClass() ) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void putFieldForInstanceInJava( java.lang.Object instanceInJava, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super.putFieldForInstanceInJava( instanceInJava, field );
		if( instanceInJava instanceof org.alice.apis.moveandturn.Transformable ) {
			org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instanceInJava;
			edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = transformable.getSGTransformable();
			if( instanceInJava instanceof org.alice.apis.moveandturn.Model ) {
				org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instanceInJava;
				if( MoveAndTurnSceneEditor.isGround( model ) ) {
					sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.GROUND );
				} else {
					sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );
					sgTransformable.putBonusDataFor( org.alice.interact.GlobalDragAdapter.BOUNDING_BOX_KEY, model.getAxisAlignedMinimumBoundingBox() );
					//edu.cmu.cs.dennisc.print.PrintUtilities.println( sgTransformable.getBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY ) );
				}
			} else if( transformable instanceof org.alice.apis.moveandturn.Light ) {
				org.alice.apis.moveandturn.Light light = (org.alice.apis.moveandturn.Light)instanceInJava;
				sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.LIGHT );
			} else if( transformable instanceof org.alice.apis.moveandturn.AbstractCamera ) {
				org.alice.apis.moveandturn.AbstractCamera camera = (org.alice.apis.moveandturn.AbstractCamera)instanceInJava;
				sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.CAMERA );
			}
		}
	}
	protected org.alice.stageide.sceneeditor.ControlsForOverlayPane createControlsForOverlayPane() {
		return new org.alice.stageide.sceneeditor.ControlsForOverlayPane( null );
	}
	protected Program createProgram() {
		return new Program( this );
	}
	protected Program getProgram() {
		return this.program;
	}
	private org.alice.stageide.sceneeditor.ControlsForOverlayPane getControlsForOverlayPane() {
		if( this.controlsForOverlayPane != null ) {
			//pass
		} else {
			this.controlsForOverlayPane = this.createControlsForOverlayPane();
		}
		return this.controlsForOverlayPane;
	}
	public void initializeLightweightOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass lightweightOnscreenLookingGlass ) {
		javax.swing.JPanel panel = lightweightOnscreenLookingGlass.getJPanel();
		panel.setLayout( new java.awt.BorderLayout() );
		panel.add( this.getControlsForOverlayPane() );
	}

	@Override
	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		super.fieldSelectionChanged( e );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = e.getNextValue();
		Object instance = this.getInstanceForField( field );
		if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
			edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
			instance = instanceInAlice.getInstanceInJava();
		}
		if( instance instanceof org.alice.apis.moveandturn.Model ) {
			org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instance;
			this.globalDragAdapter.setSelectedObject( model.getSGTransformable() );
		}
		this.controlsForOverlayPane.fieldSelectionChanged( e );
	}

	private edu.cmu.cs.dennisc.scenegraph.Background cameraBackground = new edu.cmu.cs.dennisc.scenegraph.Background();

	@Override
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		Object rv = super.createScene( sceneField );
		edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = sceneType.getDeclaredMethod( "performSetUp" );
		this.getVM().invokeEntryPoint( method, rv );
		org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)((edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)rv).getInstanceInJava();
		this.program.setScene( scene );
		this.getControlsForOverlayPane().setRootField( sceneField );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( sceneField );
		return rv;
	}

	//	@Override
	//	protected void setSceneField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
	//		super.setSceneField( sceneField );
	//		this.controlsForOverlayPane.setRootField( sceneField );
	//	}

	public void setDragInProgress( boolean isDragInProgress ) {
		if( isDragInProgress ) {
			this.showSnapshotIfAppropriate();
		} else {
			this.showLiveIfAppropriate();
		}
	}

	public void showSnapshotIfAppropriate() {
		this.cardPane.showSnapshot();
	}
	public void showLiveIfAppropriate() {
		this.cardPane.showLive();
	}

	public Object createInstance( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return this.getVM().createInstanceEntryPoint( type );
	}

	private static edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression( boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		edu.cmu.cs.dennisc.alice.ast.Expression thisExpression = new edu.cmu.cs.dennisc.alice.ast.ThisExpression();
		if( isThis ) {
			return thisExpression;
		} else {
			return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( thisExpression, field );
		}
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( Double d ) {
		return new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( d );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( String s ) {
		return new edu.cmu.cs.dennisc.alice.ast.StringLiteral( s );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Color color ) {
		return new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Font font ) {
		return new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.PointOfView pointOfView ) {
		return new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
	}
	
	private static edu.cmu.cs.dennisc.alice.ast.ExpressionStatement createStatement( Class<?> declarationCls, String methodName, Class<?> parameterCls, edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression, edu.cmu.cs.dennisc.alice.ast.Expression argumentExpression ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = org.alice.ide.ast.NodeUtilities.lookupMethod( declarationCls, methodName, parameterCls );
		return org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpression );
	}
	private void fillInAutomaticSetUpMethod( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		Object instance = this.getInstanceInJavaForField( field );
		if( instance instanceof org.alice.apis.moveandturn.Element ) {
			org.alice.apis.moveandturn.Element element = (org.alice.apis.moveandturn.Element)instance;
			bodyStatementsProperty.add( createStatement( edu.cmu.cs.dennisc.pattern.AbstractElement.class, "setName", String.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( element.getName() ) ) );
			if( instance instanceof org.alice.apis.moveandturn.Transformable ) {
				org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)element;
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.AbstractTransformable.class, "setLocalPointOfView", org.alice.apis.moveandturn.PointOfView.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( transformable.getLocalPointOfView() ) ) );
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Composite.class, "addComponent", org.alice.apis.moveandturn.Transformable.class, new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ) ) );
				if( instance instanceof org.alice.apis.moveandturn.Model ) {
					org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)transformable;
					//todo: handle size
					if( instance instanceof org.alice.apis.moveandturn.Text ) {
						org.alice.apis.moveandturn.Text text = (org.alice.apis.moveandturn.Text)model;
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setValue", String.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( text.getValue() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setFont", org.alice.apis.moveandturn.Font.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( text.getFont() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setLetterHeight", Number.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( text.getLetterHeight() ) ) );
						//todo: handle size
					}
				}
			} else if( instance instanceof org.alice.apis.moveandturn.Scene ) {
				org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)element;
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Scene.class, "setAtmosphereColor", org.alice.apis.moveandturn.Color.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( scene.getAtmosphereColor() ) ) );
			}
		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( field );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( instance );
	}

	@Override
	public void generateCodeForSetUp() {
		edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
		edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = sceneType.getDeclaredMethod( "performSceneEditorGeneratedSetUp" );
		if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method;
			edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty = methodDeclaredInAlice.body.getValue().statements;
			bodyStatementsProperty.clear();
			bodyStatementsProperty.add( new edu.cmu.cs.dennisc.alice.ast.Comment( "DO NOT EDIT.  This code is automatically generated." ) );
			this.fillInAutomaticSetUpMethod( bodyStatementsProperty, true, sceneField );
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
				this.fillInAutomaticSetUpMethod( bodyStatementsProperty, false, field );
			}
		} else {
			throw new RuntimeException();
		}
	}
	@Override
	public void preserveProjectProperties() {
		this.preserveCameraNavigationProperties( this.cameraNavigationDragAdapter );
	}
	@Override
	public void restoreProjectProperties() {
		this.restoreCameraNavigationProperties( this.cameraNavigationDragAdapter );
	}

	private java.util.Stack< Boolean > isCameraNavigationDragAdapterEnabledStack = new java.util.Stack< Boolean >();

	protected void pushAndSetCameraNavigationDragAdapterEnabled( Boolean isCameraNavigationDragAdapterEnabled ) {
		this.isCameraNavigationDragAdapterEnabledStack.push( this.cameraNavigationDragAdapter.isEnabled() );
		this.cameraNavigationDragAdapter.setEnabled( isCameraNavigationDragAdapterEnabled );
	}
	protected void popCameraNavigationDragAdapterEnabled() {
		this.cameraNavigationDragAdapter.setEnabled( this.isCameraNavigationDragAdapterEnabledStack.pop() );
	}

	public void orientToUpright( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		org.alice.apis.moveandturn.AbstractTransformable transformable = this.getInstanceInJavaForField( field, org.alice.apis.moveandturn.AbstractTransformable.class );
		if( transformable != null ) {
			transformable.orientToUpright();
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "orientToUpright", field );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "orientToUpright", this.getInstanceInJavaForField( field ) );
		}
	}
	public void placeOnTopOfGround( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		org.alice.apis.moveandturn.Model model = this.getInstanceInJavaForField( field, org.alice.apis.moveandturn.Model.class );
		if( model != null ) {
			org.alice.apis.moveandturn.Scene scene = model.getScene();
			edu.cmu.cs.dennisc.math.AxisAlignedBox bb = model.getAxisAlignedMinimumBoundingBox();
			edu.cmu.cs.dennisc.math.Point3 position = model.getPosition( scene );
			position.y = -bb.getYMinimum();
			model.moveTo( scene.createOffsetStandIn( position ) );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "placeOnTopOfGround", field );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "placeOnTopOfGround", this.getInstanceInJavaForField( field ) );
		}
	}

	//	def performOrientToUpright(self, field):
	//		instanceInJava = self.getInstanceInJavaForField( field )
	//		instanceInJava.orientToUpright()
	//		
	//	def performPlaceOnTopOfGround(self, field):
	//		instanceInJava = self.getInstanceInJavaForField( field )
	//		asSeenBy = org.alice.apis.moveandturn.AsSeenBy.SCENE
	//		bb = instanceInJava.getAxisAlignedMinimumBoundingBox()
	//		position = instanceInJava.getPosition( asSeenBy )
	//		position.y = -bb.minimum.y
	//		instanceInJava.moveTo( instanceInJava.acquireStandIn( asSeenBy, position ) )
	//	
	//	def getScene(self):
	//		return self.getSceneInstanceInJava()
	//	
	//	def getSceneType( self ):
	//		return self.getSceneField().getValueType()
	//
	//	def _isFieldNameFree( self, name ):
	//		sceneType = self.getSceneType()
	//		if sceneType:
	//			for field in sceneType.fields.iterator():
	//				if field.getName() == name:
	//					return False
	//		return True 
	//
	//	def _getAvailableFieldName( self, superClassBaseName ):
	//		name = superClassBaseName[ 0 ].lower() + superClassBaseName[ 1: ]
	//		rv = name
	//		i = 2
	//		while not self._isFieldNameFree( rv ):
	//			rv = name + `i`
	//			i += 1
	//		return rv
	//
	//	def _handleCreateInstance( self, type ):
	//		#todo
	//		t = type.getFirstTypeEncounteredDeclaredInJava()
	//		cls = t.getCls()
	//		clsName = cls.__name__
	//		thumbnailsRoot = self._api._getGalleryThumbnailsRoot()
	//		
	//		prefixList = []
	//		prefixList.append( "org.alice.apis.moveandturn.gallery" )
	//		prefixList.append( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery" )
	//		for prefix in prefixList:
	//			if clsName.startswith( prefix ):
	//				file = java.io.File( thumbnailsRoot, prefix + clsName[ len( prefix ): ].replace( ".", "/" ) + ".png" )
	//
	//		inputPane = ecc.dennisc.alice.ide.moveandturn.editors.gallery.CreateInstancePane( self, file, thumbnailsRoot, type )
	//		owner = alice.ide.IDE.getSingleton()
	//		instance = inputPane.showInJDialog( owner, "Create Instance", True )
	//		if instance:
	//			self.addInstance( instance )
	//
	//#	def _select(self, nextField ):
	//#		prevField = self._currSelectedField
	//#		operation = self._createSelectionOperation( nextField, prevField )
	//#		event = None
	//#		alice.ide.IDE.getSingleton().performIfAppropriate( operation, event )
	//
	//	def addASTFieldFor( self, instance ):
	//		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
	//		name = instanceInJava.getName()
	//		#programType = alice.ast.TypeDeclaredInJava.get( nameable.__class__ )
	//		type = instance.getType()
	//		astField = alice.ast.FieldDeclaredInAlice( name, type, alice.ast.InstanceCreation( type.getDeclaredConstructor( [] ), [] ) )
	//		astField.finalVolatileOrNeither.setValue( alice.ast.FieldModifierFinalVolatileOrNeither.FINAL )
	//		astField.access.setValue( alice.ast.Access.PRIVATE )
	//		self.getSceneType().fields.add( [ astField ] )
	//
	//		self.putInstanceForField( astField, instance )
	//		self.getIDE().setFieldSelection( astField )
	//#		self._select( astField )
	//
	//	def addInstance( self, instance ):
	//		self.addASTFieldFor( instance )
	//		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
	//		if isinstance( instanceInJava, apis.moveandturn.Model ):
	//			camera = self.getScene().findFirstMatch( apis.moveandturn.AbstractCamera )
	//			if camera:
	//				java.lang.Thread( ecc.dennisc.lang.ApplyRunnable( self._getGoodLookAtShowInstanceAndReturnCamera, ( camera, instanceInJava, ) ) ).start()
	//			else:
	//				self.getScene().addComponent( instanceInJava )
	//		else:
	//			self.getScene().addComponent( instanceInJava )
	//		alice.ide.IDE.getSingleton().markChanged( "scene program addInstance" )
	//

	//	def createScene( self, sceneField ):
	//		program = self.getProgram()
	//		if program:
	//			lg = program.getOnscreenLookingGlass()
	//			lg.clearCameras()
	//			program.setScene( None )
	//
	//		sceneInstance = org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor.createScene( self, sceneField )
	//		if sceneInstance:
	//			self.restoreProjectProperties()
	//		return sceneInstance
	//
	//	def getSceneAutomaticSetUpMethod( self ):
	//		return self.getSceneType().getDeclaredMethod( "performSceneEditorGeneratedSetUp", [] )
	//
	//	def getFilledInSceneAutomaticSetUpMethod( self, fillerInner ):
	//		rv = self.getSceneAutomaticSetUpMethod()
	//		map = {}
	//		for key in self.mapFieldToInstance.keySet():
	//			map[ key ] = self.mapFieldToInstance.get( key )
	//		fillerInner.fillInSceneAutomaticSetUpMethod( rv, self.getSceneField(), map )
	//		return rv
	//
	//	def generateCodeForSceneSetUp( self, setUpMethodGenerator ):
	//		self.getFilledInSceneAutomaticSetUpMethod( setUpMethodGenerator )
	//
	//	def _createSelectionOperation(self, nextField, prevField ):
	//		return BogusSelectionOperation( nextField, prevField )
	//
	//	def _getGoodLookAtShowInstanceAndReturnCamera( self, camera, instance ):
	//		self.pushAndSetCameraNavigationDragAdapterEnabled( False )
	//		try:
	//			instance.setOpacity( 0.0, apis.moveandturn.Composite.RIGHT_NOW )
	//			pov = camera.getPointOfView( self.getScene() )
	//			camera.getGoodLookAt( instance, 0.5 )
	//			self.getScene().addComponent( instance )
	//			instance.setOpacity( 1.0 )
	//			camera.moveAndOrientTo( self.getScene().createOffsetStandIn( pov.getInternal() ), 0.5 ) 
	//		finally:
	//			self.popCameraNavigationDragAdapterEnabled()
	//
	//	def handleDelete(self, node):
	//		instance = self.mapFieldToInstance.get( node )
	//		if instance:
	//			self.getScene().removeComponent( ecc.dennisc.alice.vm.getInstanceInJava( instance ) )

	//	public static void main( String[] args ) {
	//
	//		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
	//
	//		MoveAndTurnSceneEditor moveAndTurnSceneEditor = new MoveAndTurnSceneEditor();
	//
	//		ide.loadProjectFrom( new java.io.File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "a.a3p" ) );
	//
	//		javax.swing.JFrame frame = new javax.swing.JFrame();
	//		frame.getContentPane().add( moveAndTurnSceneEditor );
	//		frame.setSize( 1024, 768 );
	//		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
	//		frame.setVisible( true );
	//	}
}
