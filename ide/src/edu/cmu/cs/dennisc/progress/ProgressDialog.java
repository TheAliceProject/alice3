/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
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

	public void createAndExecuteWorker() {
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
				throw new RuntimeException( e );
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
