package edu.cmu.cs.dennisc.croquet;

public class KButton extends KComponent {
	private javax.swing.JButton jButton = new javax.swing.JButton() {
		@Override
		public void addNotify() {
			KButton.this.adding();
			super.addNotify();
			KButton.this.added();
		}
		@Override
		public void removeNotify() {
			KButton.this.removing();
			super.removeNotify();
			KButton.this.removed();
		}
	};
	@Override
	protected javax.swing.JComponent getJComponent() {
		return this.jButton;
	}
}
