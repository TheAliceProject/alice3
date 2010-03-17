package edu.cmu.cs.dennisc.croquet;

public class KList<E> extends KComponent {
	private javax.swing.JList jList = new javax.swing.JList() {
		@Override
		public void addNotify() {
			KList.this.adding();
			super.addNotify();
			KList.this.added();
		}
		@Override
		public void removeNotify() {
			KList.this.removing();
			super.removeNotify();
			KList.this.removed();
		}
	};
	@Override
	protected javax.swing.JComponent getJComponent() {
		return this.jList;
	}
}
