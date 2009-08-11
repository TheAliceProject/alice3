package edu.cmu.cs.dennisc.progress;

public abstract class ProgressDialog extends javax.swing.JDialog {
	private javax.swing.JTextArea textArea = new javax.swing.JTextArea();
	private javax.swing.JProgressBar progressBar = new javax.swing.JProgressBar();
	protected abstract class Worker extends org.jdesktop.swingworker.SwingWorker<Boolean, String> {
		protected void setPortionCompleted(double portionCompleted) {
			this.setProgress((int) (100 * portionCompleted));
		}

		@Override
		protected void process(java.util.List<String> texts) {
			super.process(texts);
			for (String text : texts) {
				ProgressDialog.this.textArea.append(text);
			}
		}
	}
	private java.beans.PropertyChangeListener propertyChangeListener = new java.beans.PropertyChangeListener() {
		public void propertyChange(java.beans.PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();
			Object propertyValue = e.getNewValue();
			if ("progress".equals( propertyName )) {
				ProgressDialog.this.handleProgressChange((Integer)propertyValue);
			} else if ("state".equals( propertyName )) {
				ProgressDialog.this.handleStateChange( (Worker)e.getSource(), (org.jdesktop.swingworker.SwingWorker.StateValue)propertyValue);
			} else {
				System.out.println(propertyName + " " + propertyValue);
			}
		}
	};
	public ProgressDialog(javax.swing.JDialog owner) {
		super(owner);
	}
	public ProgressDialog(javax.swing.JFrame owner) {
		super(owner);
	}

	protected void addComponentsToPageAxisContentPane( java.awt.Container contentPane ) {
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(this.textArea) {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return new java.awt.Dimension(320, 240);
			}
		};
		contentPane.add(scrollPane);
		if (this.isProgressBarDesired()) {
			contentPane.add(this.progressBar);
		}
	}
	@Override
	public void addNotify() {
		super.addNotify();
		java.awt.Container contentPane = this.getContentPane();
		if( contentPane.getComponentCount() == 0 ) {
			contentPane.setLayout( new javax.swing.BoxLayout( contentPane, javax.swing.BoxLayout.PAGE_AXIS ) );
			addComponentsToPageAxisContentPane( contentPane );
		}
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "addNotify" );
	}
	@Override
	public void removeNotify() {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "removeNotify" );
		super.removeNotify();
	}

	protected abstract Worker createWorker();
	protected abstract void handleDone( Boolean result );
	protected abstract boolean isProgressBarDesired();

	protected void createAndExecuteWorker() {
		Worker worker = createWorker();
		worker.addPropertyChangeListener(this.propertyChangeListener);
		worker.execute();
	}
	protected void handleProgressChange( Integer progress ) {
		this.progressBar.setValue(progress);
	}
	protected void handleStateChange( Worker worker, org.jdesktop.swingworker.SwingWorker.StateValue state ) {
		if( org.jdesktop.swingworker.SwingWorker.StateValue.DONE.equals( state ) ) {
			try {
				worker.removePropertyChangeListener( this.propertyChangeListener );
				Boolean result = worker.get();
				if( result ) {
					this.progressBar.setValue(100);
				}
				this.handleDone(result);
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				javax.swing.JFrame frame = new javax.swing.JFrame();
				ProgressDialog dialog = new ProgressDialog(frame) {
					@Override
					protected boolean isProgressBarDesired() {
						return true;
					}
					
					@Override
					protected void handleDone(Boolean result) {
						if( result ) {
							this.setVisible(false);
						}
					}
		
					@Override
					protected edu.cmu.cs.dennisc.progress.ProgressDialog.Worker createWorker() {
						return new Worker() {
							@Override
							protected Boolean doInBackground() throws Exception {
								final int N = 10;
								for (int i = 0; i < N; i++) {
									this.setPortionCompleted(i / (double)N);
									this.publish(Integer.toString(i) + "\n");
									Thread.sleep(100);
								}
								this.publish("complete");
								return true;
							}
						};
					}
				};
				dialog.createAndExecuteWorker();
				dialog.pack();
				dialog.setVisible(true);
			}
		} );
	}
}
