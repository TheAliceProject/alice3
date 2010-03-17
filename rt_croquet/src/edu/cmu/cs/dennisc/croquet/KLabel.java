package edu.cmu.cs.dennisc.croquet;

public class KLabel extends KComponent {
	private javax.swing.JLabel jLabel = new javax.swing.JLabel() {
		@Override
		public void addNotify() {
			KLabel.this.adding();
			super.addNotify();
			KLabel.this.added();
		}
		@Override
		public void removeNotify() {
			KLabel.this.removing();
			super.removeNotify();
			KLabel.this.removed();
		}
	};
	@Override
	protected javax.swing.JComponent getJComponent() {
		return this.jLabel;
	}
}
