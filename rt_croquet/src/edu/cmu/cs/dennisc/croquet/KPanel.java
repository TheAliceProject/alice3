package edu.cmu.cs.dennisc.croquet;

public abstract class KPanel extends KComponent {
	private javax.swing.JPanel jPanel = new javax.swing.JPanel() {
		@Override
		public void addNotify() {
			KPanel.this.adding();
			super.addNotify();
			KPanel.this.added();
		}
		@Override
		public void removeNotify() {
			KPanel.this.removing();
			super.removeNotify();
			KPanel.this.removed();
		}
	};
	private java.awt.LayoutManager layoutManager;
	@Override
	protected javax.swing.JComponent getJComponent() {
		return this.jPanel;
	}
	
	protected abstract java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel );
	@Override
	protected void adding() {
		super.adding();
		if( this.layoutManager != null ) {
			//pass
		} else {
			this.layoutManager = this.createLayoutManager( this.jPanel );
			this.jPanel.setLayout( this.layoutManager );
		}
	}
	
	protected void internalAddComponent( KComponent component ) {
		assert component != null;
		component.adding();
		this.jPanel.add( component.getJComponent() );
		component.added();
	}
}
