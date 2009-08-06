package edu.cmu.cs.dennisc.progress;

public abstract class ProgressDialog extends javax.swing.JDialog {
	public ProgressDialog( javax.swing.JDialog owner ) {
		super( owner );
		this.initialize();
	}
	public ProgressDialog( javax.swing.JFrame owner ) {
		super( owner );
		this.initialize();
	}
	protected abstract Boolean doInBackground( org.jdesktop.swingworker.SwingWorker< Boolean, String > worker ) throws java.lang.Exception;
	private void initialize() {
		org.jdesktop.swingworker.SwingWorker< Boolean, String > worker = new org.jdesktop.swingworker.SwingWorker< Boolean, String >() {
			@Override
			protected Boolean doInBackground() throws java.lang.Exception {
				return ProgressDialog.this.doInBackground( this );
			}
			@Override
			protected void process( java.util.List< String > texts ) {
				super.process( texts );
				for( String text : texts ) {
					System.out.println( text );
				}
			}
		};
		worker.execute();
		try {
			Boolean isSuccessful = worker.get();
			System.out.println( "isSuccessful: " + isSuccessful );
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	//protected abstract org.jdesktop.swingworker.SwingWorker< T, V > createWorker();
	public static void main( String[] args ) {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		ProgressDialog dialog = new ProgressDialog( frame ) {
			@Override
			protected Boolean doInBackground( org.jdesktop.swingworker.SwingWorker< Boolean, String > worker ) throws java.lang.Exception {
				final int N = 10;
				for( int i = 0; i < N; i++ ) {
//					worker.setProgress( i );
//					worker.publish( Integer.toString( i ) );
					Thread.sleep( 100 );
				}
//				worker.setProgress( N );
				return true;
			}
		};
		dialog.setVisible( true );
	}

}
