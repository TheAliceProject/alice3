package test;

class MyProgressPane extends edu.cmu.cs.dennisc.issue.ProgressPane {
	@Override
	protected edu.cmu.cs.dennisc.issue.UploadWorker createUploadWorker() {
		return new MyUploadWorker( MyProgressPane.this );
	}
}

