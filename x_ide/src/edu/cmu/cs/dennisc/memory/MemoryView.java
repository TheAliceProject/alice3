package edu.cmu.cs.dennisc.memory;

public class MemoryView extends javax.swing.JComponent {
	private static final long K = 1024;
	//private static final long M = K*K;
	private java.awt.event.MouseListener mouseAdapter = new java.awt.event.MouseListener() {
		public void mouseEntered(java.awt.event.MouseEvent e) {
		}
		public void mouseExited(java.awt.event.MouseEvent e) {
		}
		public void mousePressed(java.awt.event.MouseEvent e) {
			MemoryView.this.repaint();
		}
		public void mouseReleased(java.awt.event.MouseEvent e) {
		}
		public void mouseClicked(java.awt.event.MouseEvent e) {
		}
	};
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.addMouseListener( this.mouseAdapter );
	}
	@Override
	public void removeNotify() {
		this.removeMouseListener( this.mouseAdapter );
		super.removeNotify();
	}
	
	@Override
	protected void paintComponent(java.awt.Graphics g) {
		//super.paintComponent(g);
		java.lang.management.MemoryMXBean memory = java.lang.management.ManagementFactory.getMemoryMXBean();
		java.lang.management.MemoryUsage heapUsage = memory.getHeapMemoryUsage();
		//java.lang.management.MemoryUsage nonHeapUsage = memory.getNonHeapMemoryUsage();
		long maxKB = heapUsage.getMax()/K;
		long usedKB = heapUsage.getUsed()/K;
		double portion = usedKB / (double)maxKB;
	
		//portion = 0.1;

		int w = this.getWidth();
		int h = this.getHeight();

		int xValue = (int)( w * portion );
		int xWarning = 2*w/5;
		int xDanger = 4*w/5;
		
		java.awt.Color fineColor = new java.awt.Color( 0, 191, 0 );
		java.awt.Color warningColor = new java.awt.Color( 255, 255, 0 );
		java.awt.Color dangerColor = new java.awt.Color( 255, 0, 0 );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( fineColor );
		g2.fillRect( 0, 0, xValue, h);
		if( xValue > xWarning ) {
			g2.setPaint( new java.awt.GradientPaint( xWarning, 0, fineColor, xDanger, 0, warningColor ) );
			g2.fillRect( xWarning, 0, xValue-xWarning, h);
			if( xValue > xDanger ) {
				g2.setPaint( new java.awt.GradientPaint( xDanger, 0, warningColor, w, 0, dangerColor ) );
				g2.fillRect( xDanger, 0, xValue-xDanger, h);
			}
		}
		g2.setPaint( java.awt.Color.DARK_GRAY );
		g2.fillRect( xValue, 0, w-xValue, h);
		
		int percent = (int)( portion * 100 );
		String text = "in use: " + percent+"%";
		java.awt.geom.Rectangle2D bounds = g2.getFontMetrics().getStringBounds( text, g2 );
		if( bounds.getWidth() < xValue ) {
			g.drawString( text, xValue-(int)bounds.getWidth()-4, h-(int)bounds.getHeight()/2 );
		}
	}
}
