package test;
public class ProgressTest extends javax.swing.JFrame {
	public static void main( String[] args ) {
		javax.swing.JFrame frame = new javax.swing.JFrame();

		MyProgressPane myProgressPane = new MyProgressPane();
		
		//TODO
		//myProgressPane.initializeAndExecuteWorker( issue, subject, reporterEMailAddress, reporterName );
		
		javax.swing.JDialog dialog = new javax.swing.JDialog( frame, "Uploading Bug Report", true );
		dialog.addWindowListener( new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing( java.awt.event.WindowEvent e ) {
				e.getComponent().setVisible( false );
			}
		} );
		dialog.getContentPane().add( myProgressPane );
		dialog.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		dialog.pack();
		dialog.setVisible( true );

		if( myProgressPane.isDone() ) {
			System.exit( 0 );
		}
		
		//		final javax.swing.ProgressMonitor progressMonitor = new javax.swing.ProgressMonitor( frame, "message", "note", 0, 4 );
		//		progressMonitor.setProgress( 0 );

		//		org.jdesktop.swingworker.SwingWorker< Boolean, String > swingWorker = new org.jdesktop.swingworker.SwingWorker< Boolean, String >() {
		//			@Override
		//			protected void process( java.util.List< String > chunks ) {
		//				for( String chunk : chunks ) {
		//					System.out.print( chunk );
		//				}
		//			}
		//			private void process( String... chunks ) {
		//				this.process( edu.cmu.cs.dennisc.util.CollectionUtilities.createArrayList( chunks ) );
		//			}
		//			@Override
		//			protected Boolean doInBackground() throws Exception {
		//				this.process( "jira... " );
		//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
		//				this.process( "FAILED.\n" );
		//				progressMonitor.setProgress( 1 );
		//				this.process( "mail (on smtp port)... " );
		//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
		//				this.process( "FAILED.\n" );
		//				progressMonitor.setProgress( 2 );
		//				this.process( "mail (on secure smtp port)... " );
		//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
		//				this.process( "FAILED.\n" );
		//				progressMonitor.setProgress( 3 );
		//				this.process( "mail (on http port)... " );
		//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
		//				this.process( "FAILED.\n" );
		//				progressMonitor.setProgress( 4 );
		//				return false;
		//			}
		//			@Override
		//			protected void done() {
		//				try {
		//					Boolean isSuccessful = this.get();
		//				} catch( Exception e ) {
		//					throw new RuntimeException( e );
		//				}
		//			}
		//		};
		//		swingWorker.execute();
	}
}
