
public class VideoCapturePane extends swing.LineAxisPane {
	
	class RecordOpertaion extends zoot.AbstractActionOperation {
		public RecordOpertaion() {
			this.putValue( javax.swing.Action.NAME, "record" );
		}
		public void perform( zoot.ActionContext actionContext ) {
			VideoCapturePane.this.record();
			actionContext.commit();
		}	
	}
	private class OwnerPane extends javax.swing.JPanel {
		public OwnerPane() {
			setBackground( java.awt.Color.DARK_GRAY );
		}
		@Override
		public java.awt.Dimension getPreferredSize() {
			return new java.awt.Dimension( 320, 240 );
		}
		@Override
		public java.awt.Dimension getMaximumSize() {
			return this.getPreferredSize();
		} 
	}
	private OwnerPane pane = new OwnerPane();
	private java.io.File getProjectFile() {
		return new java.io.File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "a.a3p" );
	}
	private edu.cmu.cs.dennisc.alice.Project project;
	public VideoCapturePane() {
		this.project = edu.cmu.cs.dennisc.alice.io.FileUtilities.readProject( getProjectFile() );
		javax.swing.Box box = javax.swing.Box.createVerticalBox();
		box.add( new zoot.ZButton( new RecordOpertaion() ) );
		box.add( javax.swing.Box.createGlue() );
		this.add( box );
		this.add( this.pane );
		this.add( javax.swing.Box.createGlue() );
	}
	
	private void record() {
		edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.project.getProgramType().getDeclaredFields().get( 0 ).getValueType();
		
		org.alice.stageide.MoveAndTurnRuntimeProgram rtProgram = new org.alice.stageide.MoveAndTurnRuntimeProgram( sceneType, vm ) {
			@Override
			protected java.awt.Component createSpeedMultiplierControlPanel() {
				return null;
			}
			@Override
			protected edu.cmu.cs.dennisc.animation.Animator createAnimator() {
				return new edu.cmu.cs.dennisc.animation.FrameBasedAnimator( 24.0 );
			}
			@Override
			protected void run() {
				super.run();
				this.setMovieEncoder( null );
			}
		};
		
		this.pane.removeAll();
		
		java.io.File directory = new java.io.File( edu.cmu.cs.dennisc.io.FileUtilities.getDefaultDirectory(), "VideoCapture" );
		rtProgram.setMovieEncoder( new edu.cmu.cs.dennisc.movie.seriesofimages.SeriesOfImagesMovieEncoder( directory.getAbsolutePath(), "a", "0000", "png" ) );
		rtProgram.showInAWTContainer( this.pane, new String[] {} );
	}
	
	public static void main( String[] args ) {
		zoot.ZFrame frame = new zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		frame.getContentPane().add( new VideoCapturePane() );
		frame.pack();
		frame.setVisible( true );
	}
}
