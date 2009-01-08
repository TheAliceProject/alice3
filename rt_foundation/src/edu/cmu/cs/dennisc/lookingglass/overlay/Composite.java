/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.lookingglass.overlay;

/**
 * @author Dennis Cosgrove
 */
public class Composite extends Component {
	private java.util.List< Component > m_components = new java.util.LinkedList< Component >();
	private boolean m_isLayoutRequired = true;
	private LayoutManager m_layoutManager = null;

	@Override
	void setOverlay( Overlay overlay ) {
		super.setOverlay( overlay );
		for( Component component : m_components ) {
			component.setOverlay( overlay );
		}
		setLayoutRequired( true );
	}

	//todo
	private float m_width = Float.NaN;
	private float m_height = Float.NaN;

	@Override
	public float getWidth() {
		return m_width;
	}
	@Override
	public float getHeight() {
		return m_height;
	}

	public LayoutManager getLayoutManager() {
		return m_layoutManager;
	}
	public void setLayoutManager( LayoutManager layoutManager ) {
		if( m_layoutManager != null ) {
			m_layoutManager.removeComposite( this );
		}
		m_layoutManager = layoutManager;
		if( m_layoutManager != null ) {
			m_layoutManager.addComposite( this );
		}
		setLayoutRequired( true );
	}

	public boolean isLayoutRequired() {
		return m_isLayoutRequired;
	}
	public void setLayoutRequired( boolean isLayoutRequired ) {
		m_isLayoutRequired = isLayoutRequired;
		if( m_isLayoutRequired ) {
			repaint();
		}
	}

	public void addComponent( Component component ) {
		synchronized( m_components ) {
			m_components.add( component );
			component.setOverlay( getOverlay() );
			setLayoutRequired( true );
		}
	}
	public void removeComponent( Component component ) {
		synchronized( m_components ) {
			repaint();
			component.setOverlay( null );
			m_components.remove( component );
		}
	}

	public Iterable< Component > getComponents() {
		return m_components;
	}

	@Override
	public boolean isPointContainedWithin( int x, int y ) {
		synchronized( m_components ) {
			for( Component component : m_components ) {
				if( component.isVisible() ) {
					if( component.isPointContainedWithin( x, y ) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public boolean isEventIntercepted( java.util.EventObject e ) {
		if( e instanceof java.awt.event.MouseEvent ) {
			java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
			layoutIfNecessary( me );
			return isPointContainedWithin( me.getX(), me.getY() );
		}
		return false;
	}

	@Override
	public void computePreferredSize( java.awt.Graphics g, int width, int height ) {
		synchronized( m_components ) {
			for( Component component : m_components ) {
				if( component.isVisible() ) {
					component.computePreferredSize( g, width, height );
				}
			}
		}
	}

	@Override
	public void paint( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e, java.awt.Graphics2D g2 ) {
		synchronized( m_components ) {
			for( Component component : m_components ) {
				if( component.isVisible() ) {
					component.paint( e, g2 );
				}
			}
		}
	}
	public void layoutIfNecessary( java.awt.Graphics g, int width, int height ) {
		if( m_isLayoutRequired ) {
			synchronized( m_components ) {
				for( Component component : m_components ) {
					if( component.isVisible() ) {
					}
				}
			}
			if( m_layoutManager != null ) {
				m_layoutManager.computePreferredSizes( this, g, width, height );
				m_layoutManager.computeLocations( this, width, height );
			}
			m_isLayoutRequired = false;
		}
		synchronized( m_components ) {
			for( Component component : m_components ) {
				if( component instanceof Composite ) {
					((Composite)component).layoutIfNecessary( g, width, height );
				}
			}
		}
	}
	private void layoutIfNecessary( java.awt.event.MouseEvent e ) {
		Object source = e.getSource();
		if( source instanceof java.awt.Component ) {
			java.awt.Component awtComponent = (java.awt.Component)source;
			layoutIfNecessary( awtComponent.getGraphics(), awtComponent.getWidth(), awtComponent.getHeight() );
		}
	}

	public void handleRendered( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e, java.awt.Graphics2D g2 ) {
		layoutIfNecessary( g2, e.getTypedSource().getWidth(), e.getTypedSource().getHeight() );
		paint( e, g2 );
	}
	public void handleComponentShown( java.awt.event.ComponentEvent e ) {
		setLayoutRequired( true );
	}
	public void handleComponentResized( java.awt.event.ComponentEvent e ) {
		setLayoutRequired( true );
	}

	private void handleMousePressed( int x, int y ) {
		synchronized( m_components ) {
			for( Component component : m_components ) {
				if( component.isVisible() ) {
					if( component instanceof Control ) {
						Control control = (Control)component;
						control.setPressed( control.isPointContainedWithin( x, y ) );
					} else if( component instanceof Composite ) {
						Composite composite = (Composite)component;
						composite.handleMousePressed( x-(int)getX(), y-(int)getY() );
					}
				}
			}
		}
	}

	public void handleMousePressed( java.awt.event.MouseEvent e ) {
		layoutIfNecessary( e );
		handleMousePressed( e.getX(), e.getY() );
	}

	private void handleMouseReleased( int x, int y, java.awt.event.MouseEvent e ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleMouseReleased", x, y, e );
		synchronized( m_components ) {
			for( Component component : m_components ) {
				if( component.isVisible() ) {
					if( component instanceof Control ) {
						Control control = (Control)component;
						boolean wasPressed = control.isPressed();
						if( e.isPopupTrigger() || e.isMetaDown() ) {
							if( wasPressed ) {
								control.firePopupListeners( e );
							}
						} else {
							control.setPressed( false );
							if( control instanceof AbstractSelectableControl ) {
								AbstractSelectableControl selectableControl = (AbstractSelectableControl)control;
								if( wasPressed ) {
									Group< AbstractSelectableControl > group = selectableControl.getGroup();
									assert group != null;
									for( AbstractSelectableControl sc : group.getGroupComponents() ) {
										sc.setSelected( sc == selectableControl );
									}
								}
							} else if( control instanceof AbstractButton ) {
								AbstractButton button = (AbstractButton)control;
								if( button.isEnabled() && button.isPointContainedWithin( x, y ) ) {
									button.fireActionListeners();
								}
							}
						}
					} else if( component instanceof Composite ) {
						Composite composite = (Composite)component;
						composite.handleMouseReleased( x-(int)getX(), y-(int)getY(), e );
					}
				}
			}
		}
	}

	public void handleMouseReleased( java.awt.event.MouseEvent e ) {
		layoutIfNecessary( e );
		handleMouseReleased( e.getX(), e.getY(), e );
	}

	private void handleMouseMoved( int x, int y ) {
		synchronized( m_components ) {
			for( Component component : m_components ) {
				if( component.isVisible() ) {
					if( component instanceof Control ) {
						Control control = (Control)component;
						control.setHighlighted( control.isPointContainedWithin( x, y ) );
					} else if( component instanceof Composite ) {
						Composite composite = (Composite)component;
						composite.handleMouseMoved( x-(int)getX(), y-(int)getY() );
					}
				}
			}
		}
	}

	public void handleMouseMoved( java.awt.event.MouseEvent e ) {
		layoutIfNecessary( e );
		handleMouseMoved( e.getX(), e.getY() );
	}

}
