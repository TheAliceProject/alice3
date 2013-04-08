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
package edu.cmu.cs.dennisc.matt;

import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.components.BorderPanel;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class EventScript extends SimpleComposite<BorderPanel> {

	public EventScript() {
		super( java.util.UUID.fromString( "8c5c4cef-9ca6-42f6-8879-7bb5830f1032" ) );
	}

	List<EventWithTime> eventList = Collections.newLinkedList();
	List<EventWithTime> list = Collections.newLinkedList();
	private boolean isVirgin = true;
	private List<EventScriptListener> listeners = Collections.newLinkedList();
	private StringValue mouseEventName = createStringValue( createKey( "mouseEvent" ) );
	private StringValue keyBoardEventName = createStringValue( createKey( "keyboardEvent" ) );

	public void record( double currentTime, Object e ) {
		EventWithTime event = new EventWithTime( currentTime, e );
		eventList.add( event );
		fireChanged( event );
	}

	private void fireChanged( EventWithTime event ) {
		for( EventScriptListener listener : listeners ) {
			listener.fireChanged( event );
		}
	}

	public List<Object> getEventsForTime( double time ) {
		if( isVirgin ) {
			isVirgin = false;
			refresh();
		}
		List<Object> rv = Collections.newLinkedList();
		while( ( list.size() > 0 ) && ( list.get( 0 ).getTime() < time ) ) {
			rv.add( list.remove( 0 ).getEvent() );
		}
		return rv;
	}

	private void refresh() {
		list = Collections.newLinkedList( eventList );
	}

	public class EventWithTime {
		Object event;
		double time;

		public EventWithTime( double time, Object event ) {
			this.time = time;
			this.event = event;
		}

		public Object getEvent() {
			return this.event;
		}

		public double getTime() {
			return this.time;
		}

		@Override
		public String toString() {
			String eventType = "";
			String timeString = "";
			if( event instanceof MouseEventWrapper ) {
				eventType = mouseEventName.getText();
			} else if( event instanceof KeyEvent ) {
				eventType = keyBoardEventName.getText();
			} else {
				eventType = "UNKNOWN EVENT TYPE: " + event.getClass().getSimpleName();
			}
			Date date = new Date( (long)( time * 1000 ) );
			timeString = new SimpleDateFormat( "mm:ss.SS" ).format( date );
			return eventType + ": " + timeString;
		}
	}

	public int size() {
		return eventList.size();
	}

	public void addListener( EventScriptListener listener ) {
		this.listeners.add( listener );
	}

	public List<EventWithTime> getEventList() {
		return eventList;
	}

	@Override
	@Deprecated
	protected BorderPanel createView() {
		return null;
	}
}
