package edu.cmu.cs.dennisc.browser;

public class BrowserProgressDialog extends edu.cmu.cs.dennisc.progress.ProgressDialog {
	private String url;

	public BrowserProgressDialog(javax.swing.JDialog owner, String url) {
		super(owner);
		this.url = url;
	}

	public BrowserProgressDialog(javax.swing.JFrame owner, String url) {
		super(owner);
		this.url = url;
	}

	@Override
	protected boolean isProgressBarDesired() {
		return false;
	}

	@Override
	protected void handleDone(Boolean result) {
		// if (result) {
		// this.setVisible(false);
		// }
	}

	@Override
	protected void addComponentsToPageAxisContentPane(java.awt.Container contentPane) {
		super.addComponentsToPageAxisContentPane(contentPane);
		
		class CopyToClipboardOperation extends edu.cmu.cs.dennisc.zoot.InconsequentialActionOperation {
			public CopyToClipboardOperation() {
				this.putValue( javax.swing.Action.NAME, "Copy to Clipboard" );
			}
			@Override
			protected void performInternal(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
				edu.cmu.cs.dennisc.clipboard.ClipboardUtilities.setClipboardContents( BrowserProgressDialog.this.url );
			}
		}
		class DismissOperation extends edu.cmu.cs.dennisc.zoot.InconsequentialActionOperation {
			public DismissOperation() {
				this.putValue( javax.swing.Action.NAME, "Dismiss" );
			}
			@Override
			protected void performInternal(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
				BrowserProgressDialog.this.setVisible( false );
			}
		}
		
		javax.swing.JPanel pane = new javax.swing.JPanel();
		pane.add(edu.cmu.cs.dennisc.zoot.ZManager.createButton( new CopyToClipboardOperation() ));
		pane.add(edu.cmu.cs.dennisc.zoot.ZManager.createButton( new DismissOperation() ));
		contentPane.add(pane);
	}

	@Override
	protected edu.cmu.cs.dennisc.progress.ProgressDialog.Worker createWorker() {
		return new Worker() {
			@Override
			protected Boolean doInBackground() throws Exception {
				this.publish("attempting to open web browser to:\n");
				this.publish(BrowserProgressDialog.this.url);
				try {
					edu.cmu.cs.dennisc.browser.BrowserUtilities.browse(BrowserProgressDialog.this.url);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		};
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				javax.swing.JFrame frame = new javax.swing.JFrame();
				BrowserProgressDialog dialog = new BrowserProgressDialog(frame, "http://kenai.com/projects/alice/pages/Help");
				dialog.createAndExecuteWorker();
				dialog.pack();
				dialog.setVisible(true);
			}
		});
	}
}
