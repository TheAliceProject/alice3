public class ModelBuilderTest {
	public static void main( String[] args ) throws Exception {
		java.io.File file = new java.io.File( System.getProperty( "user.home" ) + "/Desktop/penguin.zip" );

		if( file.exists() ) {
			//pass
		} else {
			org.alice.apis.moveandturn.gallery.animals.Penguin penguin = new org.alice.apis.moveandturn.gallery.animals.Penguin();
			edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder treeBuilder0 = edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.newInstance( penguin.getSGTransformable() );
			treeBuilder0.encode( file );
		}

		final edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder treeBuilder = edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.getInstance( file );

		final int N = 11;
		org.alice.apis.moveandturn.Program program = new org.alice.apis.moveandturn.Program() {
			private org.alice.apis.moveandturn.Scene scene = new org.alice.apis.moveandturn.Scene();
			private org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera = new org.alice.apis.moveandturn.SymmetricPerspectiveCamera();
			private org.alice.apis.moveandturn.DirectionalLight sun = new org.alice.apis.moveandturn.DirectionalLight();

			private edu.cmu.cs.dennisc.scenegraph.Transformable[][] penguins = new edu.cmu.cs.dennisc.scenegraph.Transformable[ N ][ N ];
			@Override
			protected void initialize() {
				sun.setVehicle( scene );
				sun.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, 0.25 );
				camera.setVehicle( scene );
				camera.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 20.0 );
				camera.move( org.alice.apis.moveandturn.MoveDirection.UP, 10.0 );
				camera.move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 10.0 );
				camera.pointAt( scene );

				//scene.getSGComposite().addComponent( new edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes( 2.0 ) );
				
				for( int i = 0; i < N; i++ ) {
					for( int j = 0; j < N; j++ ) {
						edu.cmu.cs.dennisc.scenegraph.Transformable transformable = treeBuilder.buildTransformable();
						transformable.setName( "penguins[" + i + "]["+ j + "]" );
						transformable.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity() );
						transformable.applyTranslation( -5.0 + i * 1.0, 0, -5.0 + j * 1.0 );
						penguins[ i ][ j ] = transformable;
						scene.getSGComposite().addComponent( transformable );
						//edu.cmu.cs.dennisc.print.PrintUtilities.println( transformable.getName() );
					}
				}
				this.setScene( scene );
			}
			@Override
			protected void run() {
				while( true ) {
					this.perform( new edu.cmu.cs.dennisc.animation.Animation() {
						public void complete( edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
						}
						public void reset() {
						}
						public double update( double tCurrent, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
							edu.cmu.cs.dennisc.math.Angle angle = new edu.cmu.cs.dennisc.math.AngleInDegrees( 1.0 );
							for( int i = 0; i < N; i++ ) {
								for( int j = 0; j < N; j++ ) {
									penguins[ i ][ j ].applyRotationAboutYAxis( angle );
								}
							}
							return Double.MAX_VALUE;
						}
					}, null );
				}
			}
		};
		program.showInJFrame( args, true );
	}
}
