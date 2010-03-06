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
import org.alice.interact.AbstractDragAdapter;
import org.alice.stageide.sceneeditor.viewmanager.ManipulationHandleControlPanel;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;

class IsExpandedCheckBoxUI extends javax.swing.plaf.basic.BasicButtonUI {
	private final static IsExpandedCheckBoxUI singleton = new IsExpandedCheckBoxUI();

	public static javax.swing.plaf.ComponentUI createUI( javax.swing.JComponent c ) {
		return singleton;
	}

	@Override
	protected void paintText( java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle textRect, String text ) {
		//		super.paintText( g, b, textRect, text )

		javax.swing.AbstractButton button = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( c, javax.swing.AbstractButton.class );
		javax.swing.ButtonModel model = button.getModel();
		java.awt.Paint forePaint;
		java.awt.Paint backPaint;
		if( model.isPressed() ) {
			forePaint = new java.awt.Color( 128, 128, 0 );
			backPaint = null;
		} else {
			if( model.isRollover() || model.isArmed() ) {
				forePaint = java.awt.Color.WHITE;
				backPaint = java.awt.Color.BLACK;
			} else {
				//forePaint = new java.awt.Color( 255, 255, 0, 127 );
				forePaint = java.awt.Color.YELLOW;
				backPaint = java.awt.Color.DARK_GRAY;
			}
		}
		java.awt.Graphics2D g2 = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( g, java.awt.Graphics2D.class );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

		java.awt.FontMetrics fm = g.getFontMetrics();
		int x = textRect.x + getTextShiftOffset();
		int y = textRect.y + fm.getAscent() + getTextShiftOffset();
		if( backPaint != null ) {
			g2.setPaint( backPaint );
			g2.drawString( text, x + 2, y + 2 );
		}
		g2.setPaint( forePaint );
		g2.drawString( text, x, y );
	}
	@Override
	public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
		super.paint( g, c );
		javax.swing.AbstractButton button = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( c, javax.swing.AbstractButton.class );
		javax.swing.ButtonModel model = button.getModel();
		java.awt.Graphics2D g2 = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( g, java.awt.Graphics2D.class );
		g2.setStroke( new java.awt.BasicStroke( 3.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ) );
		if( model.isSelected() ) {
			int x = 0;
			int y = 0;
			int minor = 4;
			int MAJOR = 32;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
			minor += 6;
			MAJOR -= 4;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
		} else {
			int x = c.getWidth();
			int y = c.getHeight();
			int minor = -4;
			int MAJOR = -32;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
			minor += -6;
			MAJOR -= -4;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
		}
	}
}

class IsExpandedCheckBox extends edu.cmu.cs.dennisc.zoot.ZCheckBox {
	private final int X_PAD = 16;
	private final int Y_PAD = 10;

	public IsExpandedCheckBox() {
		super( org.alice.ide.IDE.getSingleton().getIsSceneEditorExpandedOperation() );
		this.setOpaque( false );
		this.setFont( this.getFont().deriveFont( 18.0f ) );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( Y_PAD, X_PAD, Y_PAD, X_PAD ) );
	}
	@Override
	public void updateUI() {
		this.setUI( IsExpandedCheckBoxUI.createUI( this ) );
	}
	@Override
	public String getText() {
		if( isSelected() ) {
			return "edit code";
		} else {
			return "edit scene";
		}
	}

	private java.awt.Rectangle innerAreaBuffer = new java.awt.Rectangle();

	@Override
	public boolean contains( int x, int y ) {
		java.awt.Rectangle bounds = javax.swing.SwingUtilities.calculateInnerArea( this, innerAreaBuffer );
		if( this.isSelected() ) {
			bounds.x -= X_PAD;
			bounds.y -= Y_PAD;
		}
		bounds.width += X_PAD;
		bounds.height += Y_PAD;
		return bounds.contains( x, y );
	}
}

class SidePane extends edu.cmu.cs.dennisc.croquet.swing.PageAxisPane {
	private boolean isExpanded = false;
	private ManipulationHandleControlPanel handleControlPanel;

	public SidePane() {
		this.handleControlPanel = new ManipulationHandleControlPanel();
		this.add( this.handleControlPanel );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
	}
	public boolean isExpanded() {
		return this.isExpanded;
	}
	public void setExpanded( boolean isExpanded ) {
		this.isExpanded = isExpanded;
		this.revalidate();
		this.repaint();
		//this.doLayout();
	}
	public void setDragAdapter( AbstractDragAdapter dragAdapter ) {
		this.handleControlPanel.setDragAdapter( dragAdapter );
	}

}

/**
 * @author Dennis Cosgrove
 */
public class MoveAndTurnSceneEditor extends org.alice.ide.sceneeditor.AbstractInstantiatingSceneEditor {
	private static final int INSET = 8;
	private javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private org.alice.interact.GlobalDragAdapter globalDragAdapter = new org.alice.interact.GlobalDragAdapter();
	private org.alice.apis.moveandturn.Scene scene;
	private edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass onscreenLookingGlass;
	private edu.cmu.cs.dennisc.animation.Animator animator;
	private SidePane sidePane;
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField;
	private java.util.List< FieldTile > fieldTiles = new java.util.LinkedList< FieldTile >();
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > fieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.refreshFields();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.refreshFields();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.refreshFields();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.refreshFields();
		}
	};

	public MoveAndTurnSceneEditor() {
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.sidePane = new SidePane();
		this.splitPane.setResizeWeight( 1.0 );
		this.splitPane.setDividerLocation( 1.0 );
		new Thread() {
			@Override
			public void run() {
				MoveAndTurnSceneEditor.this.initializeProgramAndDragAdapter();
			}
		}.start();
	}

	private javax.swing.SpringLayout getSpringLayout() {
		return (javax.swing.SpringLayout)this.onscreenLookingGlass.getJPanel().getLayout();
	}

	private void initializeProgramAndDragAdapter() {
		if( this.animator != null ) {
			//pass
		} else {
			this.animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();
			edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().addAutomaticDisplayListener( new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
				public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
					animator.update();
				}
			} );
			this.onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createLightweightOnscreenLookingGlass();
			this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );

			javax.swing.JPanel lgPanel = this.onscreenLookingGlass.getJPanel();
			javax.swing.SpringLayout springLayout = new javax.swing.SpringLayout();
			lgPanel.setLayout( springLayout );

			this.splitPane.setLeftComponent( lgPanel );

			final org.alice.interact.CameraNavigatorWidget cameraNavigatorWidget = new org.alice.interact.CameraNavigatorWidget( this.globalDragAdapter );
			//final org.alice.interact.CameraNavigatorWidget cameraNavigatorWidget = null;

			final IsExpandedCheckBox isSceneEditorExpandedCheckBox = new IsExpandedCheckBox();
			//final javax.swing.JCheckBox isSceneEditorExpandedCheckBox = edu.cmu.cs.dennisc.zoot.ZManager.createCheckBox( this.getIDE().getIsSceneEditorExpandedOperation() );
			if( cameraNavigatorWidget != null ) {
				cameraNavigatorWidget.setExpanded( isSceneEditorExpandedCheckBox.isSelected() );
			}
			isSceneEditorExpandedCheckBox.addItemListener( new java.awt.event.ItemListener() {
				public void itemStateChanged( java.awt.event.ItemEvent e ) {
					boolean isExpanded = e.getStateChange() == java.awt.event.ItemEvent.SELECTED;
					if( cameraNavigatorWidget != null ) {
						cameraNavigatorWidget.setExpanded( isExpanded );
					}
				}
			} );

			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( lgPanel, isSceneEditorExpandedCheckBox, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal.EAST, -INSET, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Vertical.SOUTH, -INSET );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( lgPanel, edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.getIDE().getRunOperation() ), edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal.EAST, -INSET, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Vertical.NORTH, INSET );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( lgPanel, cameraNavigatorWidget, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal.CENTER, INSET, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Vertical.SOUTH, -INSET );

			//			this.setSouthEastComponent( isSceneEditorExpandedCheckBox );
			//			this.setNorthEastComponent( edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.getIDE().getRunOperation() ) );
			//			this.setSouthComponent( cameraNavigatorWidget );

			this.add( this.splitPane );

			//			this.add( panel );

			//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: remove forcing repaint of scene editor" );
			//			
			//			animator.invokeLater( new edu.cmu.cs.dennisc.animation.Animation() {
			//				public double update( double tCurrent, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
			//					program.getOnscreenLookingGlass().repaint();
			//					edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
			////					if( scene != null ) {
			////						edu.cmu.cs.dennisc.scenegraph.Composite sgComposite = scene.getSGComposite();
			////						if( sgComposite instanceof edu.cmu.cs.dennisc.scenegraph.Scene ) {
			////							edu.cmu.cs.dennisc.scenegraph.Scene sgScene = (edu.cmu.cs.dennisc.scenegraph.Scene)sgComposite;
			////							double t = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
			////							double portion = t - Math.floor( t );
			////							portion *= 2;
			////							portion -= 1;
			////							portion = Math.abs( portion );
			////							sgScene.background.getValue().color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 1.0f, (float)portion, (float)portion, 1.0f ) );
			////							//PrintUtilities.println( portion );
			////						}
			////					}
			//					return 1.0;
			//				}
			//				public void complete( edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
			//				}
			//				public void reset() {
			//				}
			//			}, null );

			this.globalDragAdapter.setAnimator( animator );
			this.globalDragAdapter.addPropertyListener( new org.alice.interact.event.SelectionListener() {
				public void selecting( org.alice.interact.event.SelectionEvent e ) {
				}
				public void selected( org.alice.interact.event.SelectionEvent e ) {
					MoveAndTurnSceneEditor.this.handleSelection( e );
				}
			} );

			this.sidePane.setDragAdapter( this.globalDragAdapter );
		}
	}
	@Override
	public void handleExpandContractChange( boolean isExpanded ) {
		this.sidePane.setExpanded( isExpanded );
		if( isExpanded ) {
			this.splitPane.setRightComponent( this.sidePane );
			this.splitPane.setDividerSize( 10 );
		} else {
			this.splitPane.setRightComponent( null );
			this.splitPane.setDividerSize( 0 );
		}
		this.updateSceneBasedOnScope();
	}

	@Override
	public void handleFieldCreation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance ) {
		assert instance instanceof org.alice.apis.moveandturn.Transformable;
		final org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instance;
		//declaringType.fields.add( field );
		this.putInstanceForField( field, transformable );
		this.getIDE().setFieldSelection( field );
		final org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera = this.scene.findFirstMatch( org.alice.apis.moveandturn.SymmetricPerspectiveCamera.class );
		if( camera != null && transformable instanceof org.alice.apis.moveandturn.Model ) {
			new Thread() {
				@Override
				public void run() {
					MoveAndTurnSceneEditor.this.getGoodLookAtShowInstanceAndReturnCamera( camera, (org.alice.apis.moveandturn.Model)transformable );
				}
			}.start();
		} else {
			this.scene.addComponent( transformable );
		}
		//getIDE().markChanged( "scene program addInstance" );
	}
	private void getGoodLookAtShowInstanceAndReturnCamera( org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera, org.alice.apis.moveandturn.Model model ) {
		this.pushAndSetCameraNavigationDragAdapterEnabled( false );
		try {
			model.setOpacity( 0.0, org.alice.apis.moveandturn.Composite.RIGHT_NOW );
			org.alice.apis.moveandturn.PointOfView pov = camera.getPointOfView( this.scene );
			camera.getGoodLookAt( model, 0.5 );
			this.scene.addComponent( model );
			model.setOpacity( 1.0 );
			camera.moveAndOrientTo( this.scene.createOffsetStandIn( pov.getInternal() ), 0.5 );
		} finally {
			this.popCameraNavigationDragAdapterEnabled();
		}
	}
	private void handleSelection( org.alice.interact.event.SelectionEvent e ) {
		edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = e.getTransformable();
		org.alice.apis.moveandturn.Element element = org.alice.apis.moveandturn.Element.getElement( sgTransformable );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.getFieldForInstanceInJava( element );
		if( field != null ) {
			this.getIDE().setFieldSelection( field );
		}
	}

	protected void updateSceneBasedOnScope() {
		//		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = this.getIDE().getFocusedCode();
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getIDE().getTypeInScope();
		if( type != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
			if( sceneField != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
				boolean isSceneScope = type.isAssignableFrom( sceneType ) || this.sidePane.isExpanded();

				java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractField > fields = sceneType.getDeclaredFields();

				try {
					edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera = this.getOnscreenLookingGlass().getCameraAt( 0 );
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
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "force repaint" );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				if( onscreenLookingGlass != null ) {
					onscreenLookingGlass.repaint();
				}
			}
		} );
	}

	@Override
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		super.focusedCodeChanged( e );
		this.updateSceneBasedOnScope();
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = e.getNextValue();
		if( code != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = code.getDeclaringType();
			if( type != null ) {
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
		}
		this.updateFieldLabels();
	}

	public void updateFieldLabels() {
		for( FieldTile fieldTile : this.fieldTiles ) {
			fieldTile.updateLabel();
		}
	}

	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getRootTypeDeclaredInAlice() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.rootField.valueType.getValue();
	}
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getRootField() {
		return this.rootField;
	}
	public void setRootField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField ) {
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.removeListPropertyListener( this.fieldsAdapter );
		}
		this.rootField = rootField;
		if( this.getRootField() != null ) {
			getRootTypeDeclaredInAlice().fields.addListPropertyListener( this.fieldsAdapter );
		}
		this.refreshFields();
	}
	protected FieldTile createFieldTile( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		assert field != null;
		return new FieldTile( field );
	}

	private void refreshFields() {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		java.awt.Container lgPanel = this.onscreenLookingGlass.getJPanel();
		for( FieldTile fieldTile : this.fieldTiles ) {
			springLayout.removeLayoutComponent( fieldTile );
			this.onscreenLookingGlass.getJPanel().remove( fieldTile );
		}
		this.fieldTiles.clear();
		if( this.rootField != null ) {
			FieldTile rootFieldTile = this.createFieldTile( this.rootField );
			//rootFieldTile.setOpaque( true );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( lgPanel, rootFieldTile, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal.WEST, INSET, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Vertical.NORTH, INSET );
			this.fieldTiles.add( rootFieldTile );
			java.awt.Component prev = rootFieldTile;
			if( rootField != null ) {
				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : rootField.valueType.getValue().getDeclaredFields() ) {
					FieldTile fieldTile = createFieldTile( field );
					//fieldTile.setOpaque( true );
					this.onscreenLookingGlass.getJPanel().add( fieldTile );
					this.fieldTiles.add( fieldTile );
					springLayout.putConstraint( javax.swing.SpringLayout.NORTH, fieldTile, 1, javax.swing.SpringLayout.SOUTH, prev );
					springLayout.putConstraint( javax.swing.SpringLayout.WEST, fieldTile, 10, javax.swing.SpringLayout.WEST, rootFieldTile );
					prev = fieldTile;
				}
			}
		}
		revalidate();
		repaint();
	}

	private static boolean isGround( org.alice.apis.moveandturn.AbstractModel model ) {
		Class[] clses = { Ground.class, GrassyGround.class, DirtGround.class, MoonSurface.class, SandyGround.class, SeaSurface.class, SnowyGround.class };
		for( Class< ? extends org.alice.apis.moveandturn.PolygonalModel > cls : clses ) {
			if( cls.isAssignableFrom( model.getClass() ) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void putFieldForInstanceInJava( Object instanceInJava, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super.putFieldForInstanceInJava( instanceInJava, field );
		if( instanceInJava instanceof org.alice.apis.moveandturn.Transformable ) {
			org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instanceInJava;
			transformable.realizeIfNecessary();
			edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = transformable.getSGTransformable();
			if( instanceInJava instanceof org.alice.apis.moveandturn.AbstractModel ) {
				org.alice.apis.moveandturn.AbstractModel model = (org.alice.apis.moveandturn.AbstractModel)instanceInJava;
				if( MoveAndTurnSceneEditor.isGround( model ) ) {
					sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.GROUND );
				} else {
					sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );
					edu.cmu.cs.dennisc.math.AxisAlignedBox box = model.getAxisAlignedMinimumBoundingBox();
					edu.cmu.cs.dennisc.math.Matrix3x3 scale = model.getOriginalScale();
					if( scale != null && scale.isNaN() == false && scale.isIdentity() == false ) {
						edu.cmu.cs.dennisc.math.Matrix3x3 scaleInv = new edu.cmu.cs.dennisc.math.Matrix3x3( scale );
						scaleInv.invert();
						box.scale( scaleInv );
					}
					sgTransformable.putBonusDataFor( org.alice.interact.GlobalDragAdapter.BOUNDING_BOX_KEY, box );
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
	@Override
	public void addNotify() {
		this.initializeProgramAndDragAdapter();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
		super.addNotify();
	}

	@Override
	public void removeNotify() {
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
		super.removeNotify();
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
		this.refreshFields();
	}

	private edu.cmu.cs.dennisc.scenegraph.Background cameraBackground = new edu.cmu.cs.dennisc.scenegraph.Background();

	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = null;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > listPropertyAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removing( RemoveListPropertyEvent< FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.handleRemoved( e );
		}
		public void removed( RemoveListPropertyEvent< FieldDeclaredInAlice > e ) {
		}
		public void set( SetListPropertyEvent< FieldDeclaredInAlice > e ) {
		}
		public void setting( SetListPropertyEvent< FieldDeclaredInAlice > e ) {
		};
	};

	private void handleRemoved( RemoveListPropertyEvent< FieldDeclaredInAlice > e ) {
		for( FieldDeclaredInAlice fieldDeclaredInAlice : e.getElements() ) {
			org.alice.apis.moveandturn.Transformable transformable = this.getInstanceInJavaForField( fieldDeclaredInAlice, org.alice.apis.moveandturn.Transformable.class );
			if( transformable != null ) {
				this.scene.removeComponent( transformable );
			}
		}
	}

	@Override
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		if( onscreenLookingGlass.getCameraCount() > 0 ) {
			onscreenLookingGlass.clearCameras();
			this.scene = null;
		}
		if( this.sceneType != null ) {
			this.sceneType.fields.removeListPropertyListener( this.listPropertyAdapter );
		}

		Object rv = super.createScene( sceneField );
		this.sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)sceneField.getValueType();
		if( this.sceneType != null ) {
			this.sceneType.fields.addListPropertyListener( this.listPropertyAdapter );
		}
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = getIDE().getPerformEditorGeneratedSetUpMethod();
		this.getVM().invokeEntryPoint( method, rv );
		this.scene = (org.alice.apis.moveandturn.Scene)((edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)rv).getInstanceInJava();

		this.scene.setOwner( new org.alice.apis.moveandturn.SceneOwner() {
			private double simulationSpeedFactor = 1.0;

			public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
				return onscreenLookingGlass;
			}
			public void setSimulationSpeedFactor( Number simulationSpeedFactor ) {
				this.simulationSpeedFactor = simulationSpeedFactor.doubleValue();
			}
			public Double getSimulationSpeedFactor() {
				return this.simulationSpeedFactor;
			}
			public void perform( edu.cmu.cs.dennisc.animation.Animation animation, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
				try {
					animator.invokeAndWait( animation, animationObserver );
				} catch( InterruptedException ie ) {
					throw new RuntimeException( ie );
				} catch( java.lang.reflect.InvocationTargetException ite ) {
					throw new RuntimeException( ite );
				}
			}
		} );

		this.setRootField( sceneField );
		//this.cameraNavigationDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
		return rv;
	}
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

	//	@Override
	//	protected void setSceneField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
	//		super.setSceneField( sceneField );
	//		this.controlsForOverlayPane.setRootField( sceneField );
	//	}

	//	public void setDragInProgress( boolean isDragInProgress ) {
	//		if( isDragInProgress ) {
	//			this.showSnapshotIfAppropriate();
	//		} else {
	//			this.showLiveIfAppropriate();
	//		}
	//	}

	//	public void showSnapshotIfAppropriate() {
	//		this.cardPane.showSnapshot();
	//	}
	//	public void showLiveIfAppropriate() {
	//		this.cardPane.showLive();
	//	}

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
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( Enum e ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( e.getClass() );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = type.getDeclaredField( type, e.name() );
		return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.TypeExpression( type ), field );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.font.Attribute attribute ) {
		if( attribute instanceof Enum ) {
			return createExpression( (Enum)attribute );
		} else {
			throw new RuntimeException( "todo handle org.alice.apis.moveandturn.font.Attribute that is not an Enum" );
		}
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.ImageSource imageSource ) {
		if( imageSource != null ) {
			edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( org.alice.apis.moveandturn.ImageSource.class, org.alice.virtualmachine.resources.ImageResource.class );
			edu.cmu.cs.dennisc.alice.ast.Expression arg0Expression;
			org.alice.virtualmachine.resources.ImageResource imageResource = imageSource.getImageResource();
			if( imageResource != null ) {
				arg0Expression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( org.alice.virtualmachine.resources.ImageResource.class, imageResource );
			} else {
				arg0Expression = new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
			}
			return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression );
		} else {
			return new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
		}
	}

	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Color color ) {
		edu.cmu.cs.dennisc.alice.ast.Expression rv = null;
		Class< ? > cls = org.alice.apis.moveandturn.Color.class;
		for( java.lang.reflect.Field fld : edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getPublicStaticFinalFields( cls, cls ) ) {
			if( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( fld, null ).equals( color ) ) {
				edu.cmu.cs.dennisc.alice.ast.TypeExpression typeExpression = new edu.cmu.cs.dennisc.alice.ast.TypeExpression( cls );
				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field = edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField.get( fld );
				rv = new edu.cmu.cs.dennisc.alice.ast.FieldAccess( typeExpression, field );
				break;
			}
		}
		if( rv != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cls, Number.class, Number.class, Number.class );
			rv = org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( color.getRed() ), createExpression( color.getGreen() ), createExpression( color.getBlue() ) );
		}
		return rv;
	}

	private static edu.cmu.cs.dennisc.alice.ast.Expression createPositionExpression( edu.cmu.cs.dennisc.math.Point3 translation ) {
		Class< ? > cls = org.alice.apis.moveandturn.Position.class;
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cls, Number.class, Number.class, Number.class );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( translation.x ), createExpression( translation.y ), createExpression( translation.z ) );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createQuaternionExpression( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation ) {
		edu.cmu.cs.dennisc.math.UnitQuaternion q = new edu.cmu.cs.dennisc.math.UnitQuaternion( orientation );
		Class< ? > cls = org.alice.apis.moveandturn.Quaternion.class;
		java.lang.reflect.Constructor< ? > cnstrctr = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( cls, Number.class, Number.class, Number.class, Number.class );
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cnstrctr );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( q.x ), createExpression( q.y ), createExpression( q.z ), createExpression( q.w ) );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.PointOfView pointOfView ) {
		Class< ? > cls = org.alice.apis.moveandturn.PointOfView.class;
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cls, org.alice.apis.moveandturn.Orientation.class, org.alice.apis.moveandturn.Position.class );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createQuaternionExpression( pointOfView.getInternal().orientation ), createPositionExpression( pointOfView.getInternal().translation ) );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Font font ) {
		Class< ? > cls = org.alice.apis.moveandturn.Font.class;
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cls, org.alice.apis.moveandturn.font.Attribute[].class );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( font.getFamily() ), createExpression( font.getWeight() ), createExpression( font.getPosture() ) );
	}

	private static edu.cmu.cs.dennisc.alice.ast.ExpressionStatement createStatement( Class< ? > declarationCls, String methodName, Class< ? > parameterCls, edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression, edu.cmu.cs.dennisc.alice.ast.Expression argumentExpression ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = org.alice.ide.ast.NodeUtilities.lookupMethod( declarationCls, methodName, parameterCls );
		return org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpression );
	}
	private static edu.cmu.cs.dennisc.alice.ast.ExpressionStatement createStatement( Class< ? > declarationCls, String methodName, Class< ? >[] parameterClses, edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression, edu.cmu.cs.dennisc.alice.ast.Expression... argumentExpressions ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = org.alice.ide.ast.NodeUtilities.lookupMethod( declarationCls, methodName, parameterClses );
		return org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpressions );
	}
	private void fillInAutomaticSetUpMethod( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		Object instance = this.getInstanceInJavaForField( field );
		if( instance instanceof org.alice.apis.moveandturn.Element ) {
			org.alice.apis.moveandturn.Element element = (org.alice.apis.moveandturn.Element)instance;
			bodyStatementsProperty.add( createStatement( edu.cmu.cs.dennisc.pattern.AbstractElement.class, "setName", String.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( field.getName() ) ) );
			if( instance instanceof org.alice.apis.moveandturn.Transformable ) {
				org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)element;
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.AbstractTransformable.class, "setLocalPointOfView", org.alice.apis.moveandturn.PointOfView.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( transformable.getLocalPointOfView() ) ) );
				if( instance instanceof org.alice.apis.moveandturn.AbstractModel ) {
					org.alice.apis.moveandturn.AbstractModel abstractModel = (org.alice.apis.moveandturn.AbstractModel)transformable;

					double widthFactor = abstractModel.getResizeWidthAmount();
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( widthFactor, 1 ) ) {
						//pass
					} else {
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Transformable.class, "resizeWidth", new Class< ? >[] { Number.class, Number.class, org.alice.apis.moveandturn.ResizePolicy.class }, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), createExpression( widthFactor ), createExpression( 0.0 ), createExpression( org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING ) ) );
					}
					double heightFactor = abstractModel.getResizeHeightAmount();
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( heightFactor, 1 ) ) {
						//pass
					} else {
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Transformable.class, "resizeHeight", new Class< ? >[] { Number.class, Number.class, org.alice.apis.moveandturn.ResizePolicy.class }, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), createExpression( heightFactor ), createExpression( 0.0 ), createExpression( org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING ) ) );
					}
					double depthFactor = abstractModel.getResizeDepthAmount();
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( depthFactor, 1 ) ) {
						//pass
					} else {
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Transformable.class, "resizeDepth", new Class< ? >[] { Number.class, Number.class, org.alice.apis.moveandturn.ResizePolicy.class }, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), createExpression( depthFactor ), createExpression( 0.0 ), createExpression( org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING ) ) );
					}
					if( instance instanceof org.alice.apis.moveandturn.Model ) {
						org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)transformable;

						if( model instanceof org.alice.apis.moveandturn.Text ) {
							org.alice.apis.moveandturn.Text text = (org.alice.apis.moveandturn.Text)model;
							bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setValue", String.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( text.getValue() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setFont", org.alice.apis.moveandturn.Font.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( text.getFont() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setLetterHeight", Number.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( text.getLetterHeight() ) ) );
						} else if( model instanceof org.alice.apis.stage.Person ) {
							org.alice.apis.stage.Person person = (org.alice.apis.stage.Person)model;
							if( person instanceof org.alice.apis.stage.MaleAdult || person instanceof org.alice.apis.stage.FemaleAdult ) {
								//pass
							} else {
								bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setGender", org.alice.apis.stage.Gender.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( (Enum)person.getGender() ) ) );
							}

							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setHair", org.alice.apis.stage.Hair.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( (Enum)person.getHair() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setEyeColor", org.alice.apis.stage.EyeColor.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( (Enum)person.getEyeColor() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setSkinTone", org.alice.apis.stage.SkinTone.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( (Enum)person.getSkinTone() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setOutfit", org.alice.apis.stage.Outfit.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( (Enum)person.getOutfit() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setFitnessLevel", Number.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( person.getFitnessLevel() ) ) );

						}
					} else if( instance instanceof org.alice.apis.moveandturn.Billboard ) {
						org.alice.apis.moveandturn.Billboard billboard = (org.alice.apis.moveandturn.Billboard)transformable;
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Billboard.class, "setFrontImageSource", org.alice.apis.moveandturn.ImageSource.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( billboard.getFrontImageSource() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Billboard.class, "setBackImageSource", org.alice.apis.moveandturn.ImageSource.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( billboard.getBackImageSource() ) ) );
					}
				}
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Composite.class, "addComponent", org.alice.apis.moveandturn.Transformable.class, new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ) ) );
			} else if( instance instanceof org.alice.apis.moveandturn.Scene ) {
				org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)element;
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Scene.class, "setAtmosphereColor", org.alice.apis.moveandturn.Color.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( scene.getAtmosphereColor() ) ) );
			}
		}
	}

	private static final String GENERATED_CODE_WARNING = "DO NOT EDIT\nDO NOT EDIT\nDO NOT EDIT\n\nThis code is automatically generated.  Any work you perform in this method will be overwritten.\n\nDO NOT EDIT\nDO NOT EDIT\nDO NOT EDIT";

	@Override
	public void generateCodeForSetUp() {
		edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
		//		edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
		//		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = sceneType.getDeclaredMethod( "performSceneEditorGeneratedSetUp" );
		//		if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
		//			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method;
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = this.getIDE().getPerformEditorGeneratedSetUpMethod();
		edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty = methodDeclaredInAlice.body.getValue().statements;
		bodyStatementsProperty.clear();
		bodyStatementsProperty.add( new edu.cmu.cs.dennisc.alice.ast.Comment( GENERATED_CODE_WARNING ) );
		this.fillInAutomaticSetUpMethod( bodyStatementsProperty, true, sceneField );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
			this.fillInAutomaticSetUpMethod( bodyStatementsProperty, false, field );
		}
		//bodyStatementsProperty.add( new edu.cmu.cs.dennisc.alice.ast.Comment( GENERATED_CODE_WARNING ) );
		//		} else {
		//			throw new RuntimeException();
		//		}
	}
	@Override
	public void preserveProjectProperties() {
		//this.preserveCameraNavigationProperties( this.cameraNavigationDragAdapter );
	}
	@Override
	public void restoreProjectProperties() {
		//this.restoreCameraNavigationProperties( this.cameraNavigationDragAdapter );
	}

	//private java.util.Stack< Boolean > isCameraNavigationDragAdapterEnabledStack = new java.util.Stack< Boolean >();

	protected void pushAndSetCameraNavigationDragAdapterEnabled( Boolean isCameraNavigationDragAdapterEnabled ) {
		//this.isCameraNavigationDragAdapterEnabledStack.push( this.cameraNavigationDragAdapter.isEnabled() );
		//this.cameraNavigationDragAdapter.setEnabled( isCameraNavigationDragAdapterEnabled );
	}
	protected void popCameraNavigationDragAdapterEnabled() {
		//this.cameraNavigationDragAdapter.setEnabled( this.isCameraNavigationDragAdapterEnabledStack.pop() );
	}

	@Override
	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
		this.updateFieldLabels();
	}

	private boolean isRenderingEnabled = true;

	@Override
	public void setRenderingEnabled( boolean isRenderingEnabled ) {
		this.isRenderingEnabled = isRenderingEnabled;
		this.onscreenLookingGlass.setRenderingEnabled( isRenderingEnabled );
	}

	//	public void editPerson( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
	//		org.alice.apis.stage.Person person = this.getInstanceInJavaForField( field, org.alice.apis.stage.Person.class );
	//		if( person != null ) {
	//			org.alice.stageide.personeditor.PersonEditorInputPane inputPane = new org.alice.stageide.personeditor.PersonEditorInputPane( person );
	//			org.alice.apis.stage.Person result = inputPane.showInJDialog( this.getIDE() );
	//			edu.cmu.cs.dennisc.print.PrintUtilities.println( result );
	//		}
	//	}

	@Override
	public void update( java.awt.Graphics g ) {
		if( isRenderingEnabled ) {
			super.update( g );
		} else {
//			java.awt.Dimension size = this.getSize();
//			g.setColor( java.awt.Color.GRAY );
//			g.fillRect( 0, 0, size.width, size.height );
//			String text = "rendering disabled for performance considerations";
//			g.setColor( java.awt.Color.BLACK );
//			edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, text, size );
//			g.setColor( java.awt.Color.YELLOW );
//			g.translate( -1, -1 );
//			edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, text, size );
//			g.translate( 1, 1 );
		}
	}

	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return this.onscreenLookingGlass;
	}
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCameraForCreatingThumbnails() {
		return this.onscreenLookingGlass.getCameraAt( 0 );
	}
}
