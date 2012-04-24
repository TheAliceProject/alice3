/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.media.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.alice.media.RecordComposite;
import org.alice.media.VideoPlayer;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.Button;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.Label;

/**
 * @author Matt May
 */
public class RecordView extends BorderPanel {

	private GridPanel bottom;
	private Button recordSlashStop;
	private ChangeListener recordAdapter = new ChangeListener() {

		//		public void actionPerformed( ActionEvent e ) {
		//			System.out.println("a " + bottom.getComponentCount());
		//			bottom.removeComponent( recordSlashStop );
		//			System.out.println("b " + bottom.getComponentCount());
		//			recordSlashStop.getAwtComponent().removeActionListener( recordAdapter );
		//			System.out.println("c " + bottom.getComponentCount());
		//			recordSlashStop = composite.getStopOperation().createButton();
		//			System.out.println("d " + bottom.getComponentCount());
		//			recordSlashStop.getAwtComponent().addActionListener( stopAdapter );
		//			System.out.println("e " + bottom.getComponentCount());
		//			bottom.addComponent( recordSlashStop );
		//			System.out.println("f " + bottom.getComponentCount());
		//		}
		int count = 0;

		public void stateChanged( ChangeEvent e ) {
			++count;
			System.out.println( e.getSource().getClass() + " " + count );
//			System.out.println( "a " + bottom.getComponentCount() );
//			bottom.removeComponent( recordSlashStop );
//			System.out.println( "b " + bottom.getComponentCount() );
//			recordSlashStop.getAwtComponent().removeChangeListener( recordAdapter );
//			System.out.println( "c " + bottom.getComponentCount() );
//			recordSlashStop = composite.getStopOperation().createButton();
//			System.out.println( "d " + bottom.getComponentCount() );
//			recordSlashStop.getAwtComponent().addActionListener( stopAdapter );
//			System.out.println( "e " + bottom.getComponentCount() );
//			bottom.addComponent( recordSlashStop );
//			System.out.println( "f " + bottom.getComponentCount() );
		}
	};
	private ActionListener stopAdapter = new ActionListener() {

		public void actionPerformed( ActionEvent e ) {
			bottom.removeComponent( recordSlashStop );
			recordSlashStop.getAwtComponent().addActionListener( stopAdapter );
			recordSlashStop = getComposite().getRecordOperation().createButton();
			recordSlashStop.getAwtComponent().removeChangeListener( recordAdapter );
			bottom.addComponent( recordSlashStop );
		}
	};
	private ComponentListener adapter = new ComponentListener() {
		
		public void componentShown( ComponentEvent e ) {
		}
		
		public void componentResized( ComponentEvent e ) {
		}
		
		public void componentMoved( ComponentEvent e ) {
		}
		
		public void componentHidden( ComponentEvent e ) {
		}
	};

	public RecordView( RecordComposite recordComposite ) {
		super( recordComposite );
		addComponent( new VideoPlayer(), Constraint.CENTER );
		bottom = GridPanel.createGridPane( 1, 3 );
		recordSlashStop = recordComposite.getRecordOperation().createButton();
		bottom.addComponent( recordSlashStop );
		bottom.addComponent( recordComposite.getPlayRecordedOperation().createButton() );
		bottom.addComponent( new Label() );
		addComponent( bottom, Constraint.PAGE_END );
		recordSlashStop.getAwtComponent().addChangeListener( recordAdapter );
		recordSlashStop.addComponentListener( adapter  );
	}
	@Override
	public RecordComposite getComposite() {
		return (RecordComposite)super.getComposite();
	}

}
