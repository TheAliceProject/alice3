package edu.cmu.cs.dennisc.croquet;

public abstract class KWidget extends KComponent {
	private javax.swing.JComponent jComponent = new javax.swing.JComponent() {
		@Override
		public void addNotify() {
			KWidget.this.adding();
			super.addNotify();
			KWidget.this.added();
		}
		@Override
		public void removeNotify() {
			KWidget.this.removing();
			super.removeNotify();
			KWidget.this.removed();
		}
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			assert g instanceof java.awt.Graphics2D;
			KWidget.this.paintComponent( (java.awt.Graphics2D)g );
		}
	};

	@Override
	protected javax.swing.JComponent getJComponent() {
		return this.jComponent;
	}
	protected abstract void paintComponent( java.awt.Graphics2D g2 );
}
