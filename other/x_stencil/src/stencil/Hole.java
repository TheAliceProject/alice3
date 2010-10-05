package stencil;

public class Hole {
	private Proxy proxy = new Proxy();
	private java.awt.Component component;

	private java.awt.Component leadingDecorator;
	private java.awt.Component trailingDecorator;

	public Hole( java.awt.Component component ) {
		this.component = component;
	}

	public int getPad() {
		return 8;
	}

	public java.awt.Component getComponent() {
		return this.component;
	}

	public Proxy getProxy() {
		return this.proxy;
	}

	public java.awt.Component getLeadingDecorator() {
		return this.leadingDecorator;
	}

	public void setLeadingDecorator( java.awt.Component leadingDecorator ) {
		this.leadingDecorator = leadingDecorator;
	}

	public java.awt.Component getTrailingDecorator() {
		return this.trailingDecorator;
	}

	public void setTrailingDecorator( java.awt.Component trailingDecorator ) {
		this.trailingDecorator = trailingDecorator;
	}
	
	public void showDecorators() {
		if (trailingDecorator != null) trailingDecorator.setVisible(true);
		if (leadingDecorator != null) leadingDecorator.setVisible(true);

	}
	
	public void hideDecorators() {
		if (trailingDecorator != null) trailingDecorator.setVisible(false);
		if (leadingDecorator != null) leadingDecorator.setVisible(false);
	}
}
