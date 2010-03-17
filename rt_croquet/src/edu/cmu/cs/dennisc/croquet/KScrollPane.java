package edu.cmu.cs.dennisc.croquet;

public class KScrollPane extends KComponent {
	private javax.swing.JScrollPane jScrollPane = new javax.swing.JScrollPane()	{
		@Override
		public void addNotify() {
			KScrollPane.this.adding();
			super.addNotify();
			KScrollPane.this.added();
		}
		@Override
		public void removeNotify() {
			KScrollPane.this.removing();
			super.removeNotify();
			KScrollPane.this.removed();
		}
	};
	public enum KVerticalScrollbarPolicy {
		NEVER( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER ),
		AS_NEEDED( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ),
		ALWAYS( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		private int internal;
		private KVerticalScrollbarPolicy( int internal ) {
			this.internal = internal;
		}
	}
	public enum KHorizontalScrollbarPolicy {
		NEVER( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),
		AS_NEEDED( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ),
		ALWAYS( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
		private int internal;
		private KHorizontalScrollbarPolicy( int internal ) {
			this.internal = internal;
		}
	}
	public KScrollPane() {
	}
	public KScrollPane( KComponent viewportView ) {
		this.setViewportView( viewportView );
	}
	public KScrollPane( KComponent viewportView, KVerticalScrollbarPolicy verticalScrollbarPolicy, KHorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		this.setViewportView( viewportView );
		this.setVerticalScrollbarPolicy( verticalScrollbarPolicy );
		this.setHorizontalScrollbarPolicy( horizontalScrollbarPolicy );
	}
	public KScrollPane( KVerticalScrollbarPolicy verticalScrollbarPolicy, KHorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		this.setVerticalScrollbarPolicy( verticalScrollbarPolicy );
		this.setHorizontalScrollbarPolicy( horizontalScrollbarPolicy );
	}
	@Override
	protected javax.swing.JComponent getJComponent() {
		return this.jScrollPane;
	}
	
	public void setViewportView( KComponent view ) {
		assert view != null;
		this.jScrollPane.setViewportView( view.getJComponent() );
	}
	public void setVerticalScrollbarPolicy( KVerticalScrollbarPolicy verticalScrollbarPolicy ) {
		assert verticalScrollbarPolicy != null;
		this.jScrollPane.setVerticalScrollBarPolicy( verticalScrollbarPolicy.internal );
	}
	public void setHorizontalScrollbarPolicy( KHorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		assert horizontalScrollbarPolicy != null;
		this.jScrollPane.setHorizontalScrollBarPolicy( horizontalScrollbarPolicy.internal );
	}
}
