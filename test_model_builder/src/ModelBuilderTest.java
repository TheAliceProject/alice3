public class ModelBuilderTest {
	public static void main( String[] args ) throws Exception {
		String path;
		if( args.length > 0 ) {
			path = args[ 0 ];
			args = new String[] {};
		} else {
			path = System.getProperty( "user.home" ) + "/Desktop/gallery_src/full/smoothed3Gallery_2/2/Animals/penguin.zip";
		}
		java.io.File file = new java.io.File( path );
		final edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder builder = edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.getInstance( file );

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
				camera.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 10.0 );
				camera.move( org.alice.apis.moveandturn.MoveDirection.UP, 4.0 );
				camera.move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.2 );
				camera.pointAt( scene.createOffsetStandIn( org.alice.apis.moveandturn.MoveDirection.UP, 0.25 ) );

				//scene.getSGComposite().addComponent( new edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes( 2.0 ) );
				
				for( int i = 0; i < N; i++ ) {
					for( int j = 0; j < N; j++ ) {
						edu.cmu.cs.dennisc.scenegraph.Transformable transformable = builder.buildTransformable();
						transformable.setName( "penguins[" + i + "][" + j + "]" );
						transformable.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity() );
						transformable.applyTranslation( -N/2 + i * 1.0, 0, -N/2 + j * 1.0 );
						penguins[ i ][ j ] = transformable;
						scene.getSGComposite().addComponent( transformable );
						edu.cmu.cs.dennisc.print.PrintUtilities.println( transformable.getName() );
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
							edu.cmu.cs.dennisc.math.Angle angle = new edu.cmu.cs.dennisc.math.AngleInDegrees( 0.3 );
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
