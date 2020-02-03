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

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

/**
 * @author Dennis Cosgrove
 */
public class EventLayerManager extends LinkedList<EventLayer> implements ComponentListener, MouseListener, MouseMotionListener, KeyListener {
  private Component m_awtComponent;

  public Component getComponent() {
    return m_awtComponent;
  }

  public void setComponent(Component awtComponent) {
    if (m_awtComponent != null) {
      m_awtComponent.removeComponentListener(this);
      m_awtComponent.removeMouseListener(this);
      m_awtComponent.removeMouseMotionListener(this);
      m_awtComponent.removeKeyListener(this);
    }
    m_awtComponent = awtComponent;
    if (m_awtComponent != null) {
      m_awtComponent.addComponentListener(this);
      m_awtComponent.addMouseListener(this);
      m_awtComponent.addMouseMotionListener(this);
      m_awtComponent.addKeyListener(this);
    }
  }

  @Override
  public void componentShown(ComponentEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof ComponentListener) {
        ((ComponentListener) eventInterceptor).componentShown(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void componentHidden(ComponentEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof ComponentListener) {
        ((ComponentListener) eventInterceptor).componentHidden(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void componentMoved(ComponentEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof ComponentListener) {
        ((ComponentListener) eventInterceptor).componentMoved(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void componentResized(ComponentEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof ComponentListener) {
        ((ComponentListener) eventInterceptor).componentResized(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof MouseListener) {
        ((MouseListener) eventInterceptor).mousePressed(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof MouseListener) {
        ((MouseListener) eventInterceptor).mouseReleased(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof MouseListener) {
        ((MouseListener) eventInterceptor).mouseClicked(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof MouseListener) {
        ((MouseListener) eventInterceptor).mouseEntered(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void mouseExited(MouseEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof MouseListener) {
        ((MouseListener) eventInterceptor).mouseExited(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof MouseMotionListener) {
        ((MouseMotionListener) eventInterceptor).mouseMoved(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof MouseMotionListener) {
        ((MouseMotionListener) eventInterceptor).mouseDragged(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof KeyListener) {
        ((KeyListener) eventInterceptor).keyPressed(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof KeyListener) {
        ((KeyListener) eventInterceptor).keyReleased(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    for (EventLayer eventInterceptor : this) {
      if (eventInterceptor instanceof KeyListener) {
        ((KeyListener) eventInterceptor).keyTyped(e);
        if (eventInterceptor.isEventIntercepted(e)) {
          break;
        }
      }
    }
  }

}
