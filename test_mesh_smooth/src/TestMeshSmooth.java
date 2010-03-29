public class TestMeshSmooth {
	public static void main( String[] args ) throws Exception {
		org.alice.apis.moveandturn.Program program = new org.alice.apis.moveandturn.Program() {
			private org.alice.apis.moveandturn.Scene scene = new org.alice.apis.moveandturn.Scene();
			private org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera = new org.alice.apis.moveandturn.SymmetricPerspectiveCamera();
			private org.alice.apis.moveandturn.DirectionalLight sun = new org.alice.apis.moveandturn.DirectionalLight();

			private edu.cmu.cs.dennisc.scenegraph.Transformable parent = new edu.cmu.cs.dennisc.scenegraph.Transformable();
			private edu.cmu.cs.dennisc.scenegraph.Transformable[] transformables;
			private edu.cmu.cs.dennisc.scenegraph.Transformable currentTransformable;
			
			private boolean isSolid = true;
			
			private void toggleFillingStyle() {
				final edu.cmu.cs.dennisc.scenegraph.FillingStyle fillingStyle;
				if( isSolid ) {
					fillingStyle = edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME;
				} else {
					fillingStyle = edu.cmu.cs.dennisc.scenegraph.FillingStyle.SOLID;
				}
				for( edu.cmu.cs.dennisc.scenegraph.Transformable transformable : this.transformables ) {
					if( transformable != null ) {
						transformable.accept( new edu.cmu.cs.dennisc.pattern.Visitor() {
							public void visit(edu.cmu.cs.dennisc.pattern.Visitable visitable) {
								if (visitable instanceof edu.cmu.cs.dennisc.scenegraph.Visual) {
									edu.cmu.cs.dennisc.scenegraph.Visual visual = (edu.cmu.cs.dennisc.scenegraph.Visual) visitable;
									visual.frontFacingAppearance.getValue().setFillingStyle( fillingStyle );
								}
							}
						} );
					}
				}
				isSolid = !isSolid;
			}
			private void setModelIndex( int i ) {
				if( this.currentTransformable != null ) {
					this.parent.removeComponent( this.currentTransformable );
				}
				this.currentTransformable = this.transformables[ i ];
				if( this.currentTransformable != null ) {
					this.parent.addComponent( this.currentTransformable );
				}
			}
			@Override
			protected void initialize() {
				String subPath = "/2/Animals/Penguin.zip";
				String[] locations = { "shared", "smoothed_0", "arrayized_1", "arrayized_2", "smoothed_3" };
				this.transformables = new edu.cmu.cs.dennisc.scenegraph.Transformable[ locations.length ];
				int i = 0;
				for( String location : locations ) {
					String path = System.getProperty( "user.home" ) + "/Desktop/gallery_src/full/" + location + subPath;
					java.io.File file = new java.io.File( path );
					if( file.exists() ) {
						try {
							final edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder builder = edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.getInstance( file );
							this.transformables[ i ] = builder.buildTransformable();
							this.transformables[ i ].setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity() );
						} catch( java.io.IOException ioe ) {
							throw new RuntimeException( file.toString(), ioe );
						}
						edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.forget( file );
					} else {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( file + " not found." );
						this.transformables[ i ] = null;
					}
					i++;
				}

				this.currentTransformable = transformables[ 2 ];
				this.parent.addComponent( this.currentTransformable );
				this.parent.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );

				sun.setVehicle( scene );
				sun.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, 0.25 );
				camera.setVehicle( scene );
				camera.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 5.0 );
				camera.move( org.alice.apis.moveandturn.MoveDirection.UP, 2.0 );
				camera.move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.2 );
				camera.pointAt( scene.createOffsetStandIn( org.alice.apis.moveandturn.MoveDirection.UP, 0.75 ) );

				scene.getSGComposite().addComponent( this.parent );
				//scene.getSGComposite().addComponent( new edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes( 2.0 ) );
				this.setScene( scene );
				
				org.alice.interact.CreateASimDragAdapter createASimDragAdapter = new org.alice.interact.CreateASimDragAdapter();
				//createASimDragAdapter.setAnimator( this.getAnimator() );
				createASimDragAdapter.setOnscreenLookingGlass( this.getOnscreenLookingGlass() );
				createASimDragAdapter.setSelectedObject( this.parent );
				
				this.getOnscreenLookingGlass().getAWTComponent().addKeyListener( new java.awt.event.KeyListener() {
					public void keyPressed(java.awt.event.KeyEvent e) {
					}
					public void keyReleased(java.awt.event.KeyEvent e) {
						if( e.getKeyCode() == java.awt.event.KeyEvent.VK_W ) {
							toggleFillingStyle();
						} else {
							switch( e.getKeyCode() ) {
							case java.awt.event.KeyEvent.VK_0:
								setModelIndex( 0 );
								break;
							case java.awt.event.KeyEvent.VK_1:
								setModelIndex( 1 );
								break;
							case java.awt.event.KeyEvent.VK_2:
								setModelIndex( 2 );
								break;
							case java.awt.event.KeyEvent.VK_3:
								setModelIndex( 3 );
								break;
							case java.awt.event.KeyEvent.VK_4:
								setModelIndex( 4 );
								break;
							}
						}
					}
					public void keyTyped(java.awt.event.KeyEvent e) {
					}
				} );
			}
			@Override
			protected void run() {
				while( true ) {
//					this.perform( new edu.cmu.cs.dennisc.animation.Animation() {
//						public void complete( edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
//						}
//						public void reset() {
//						}
//						public double update( double tCurrent, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
//							edu.cmu.cs.dennisc.math.Angle angle = new edu.cmu.cs.dennisc.math.AngleInDegrees( 0.3 );
//							for( int i = 0; i < transformables.length; i++ ) {
//								transformables[ i ].applyRotationAboutYAxis( angle );
//							}
//							return Double.MAX_VALUE;
//						}
//					}, null );
				}
			}
		};
		program.showInJFrame( args, true );
	}
}
