/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class EventLayerManager extends java.util.LinkedList<EventLayer> implements java.awt.event.ComponentListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener, java.awt.event.KeyListener {
	private java.awt.Component m_awtComponent;

	public java.awt.Component getComponent() {
		return m_awtComponent;
	}

	public void setComponent( java.awt.Component awtComponent ) {
		if( m_awtComponent != null ) {
			m_awtComponent.removeComponentListener( this );
			m_awtComponent.removeMouseListener( this );
			m_awtComponent.removeMouseMotionListener( this );
			m_awtComponent.removeKeyListener( this );
		}
		m_awtComponent = awtComponent;
		if( m_awtComponent != null ) {
			m_awtComponent.addComponentListener( this );
			m_awtComponent.addMouseListener( this );
			m_awtComponent.addMouseMotionListener( this );
			m_awtComponent.addKeyListener( this );
		}
	}

	@Override
	public void componentShown( java.awt.event.ComponentEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.ComponentListener ) {
				( (java.awt.event.ComponentListener)eventInterceptor ).componentShown( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void componentHidden( java.awt.event.ComponentEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.ComponentListener ) {
				( (java.awt.event.ComponentListener)eventInterceptor ).componentHidden( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void componentMoved( java.awt.event.ComponentEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.ComponentListener ) {
				( (java.awt.event.ComponentListener)eventInterceptor ).componentMoved( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void componentResized( java.awt.event.ComponentEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.ComponentListener ) {
				( (java.awt.event.ComponentListener)eventInterceptor ).componentResized( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void mousePressed( java.awt.event.MouseEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.MouseListener ) {
				( (java.awt.event.MouseListener)eventInterceptor ).mousePressed( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void mouseReleased( java.awt.event.MouseEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.MouseListener ) {
				( (java.awt.event.MouseListener)eventInterceptor ).mouseReleased( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void mouseClicked( java.awt.event.MouseEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.MouseListener ) {
				( (java.awt.event.MouseListener)eventInterceptor ).mouseClicked( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void mouseEntered( java.awt.event.MouseEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.MouseListener ) {
				( (java.awt.event.MouseListener)eventInterceptor ).mouseEntered( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void mouseExited( java.awt.event.MouseEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.MouseListener ) {
				( (java.awt.event.MouseListener)eventInterceptor ).mouseExited( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void mouseMoved( java.awt.event.MouseEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.MouseMotionListener ) {
				( (java.awt.event.MouseMotionListener)eventInterceptor ).mouseMoved( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void mouseDragged( java.awt.event.MouseEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.MouseMotionListener ) {
				( (java.awt.event.MouseMotionListener)eventInterceptor ).mouseDragged( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void keyPressed( java.awt.event.KeyEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.KeyListener ) {
				( (java.awt.event.KeyListener)eventInterceptor ).keyPressed( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void keyReleased( java.awt.event.KeyEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.KeyListener ) {
				( (java.awt.event.KeyListener)eventInterceptor ).keyReleased( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

	@Override
	public void keyTyped( java.awt.event.KeyEvent e ) {
		for( EventLayer eventInterceptor : this ) {
			if( eventInterceptor instanceof java.awt.event.KeyListener ) {
				( (java.awt.event.KeyListener)eventInterceptor ).keyTyped( e );
				if( eventInterceptor.isEventIntercepted( e ) ) {
					break;
				}
			}
		}
	}

}
