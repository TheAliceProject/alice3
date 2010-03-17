package edu.cmu.cs.dennisc.croquet;

public class KCheckBox extends KComponent {
	private javax.swing.JCheckBox jCheckBox = new javax.swing.JCheckBox() {
		@Override
		public void addNotify() {
			KCheckBox.this.adding();
			super.addNotify();
			KCheckBox.this.added();
		}
		@Override
		public void removeNotify() {
			KCheckBox.this.removing();
			super.removeNotify();
			KCheckBox.this.removed();
		}
	};
	@Override
	protected javax.swing.JComponent getJComponent() {
		return this.jCheckBox;
	}
}
