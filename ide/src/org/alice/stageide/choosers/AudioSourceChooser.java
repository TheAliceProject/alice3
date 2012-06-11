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
package org.alice.stageide.choosers;

class TimeSlider extends javax.swing.JSlider {
	private javax.swing.event.ChangeListener changeAdapter = new javax.swing.event.ChangeListener() {
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			TimeSlider.this.updateTime();
		}
	};
	private double duration;
	private double time;
	public TimeSlider() {
		this.setMaximum( 1000 );
		this.setDuration( Double.NaN );
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.addChangeListener( this.changeAdapter );
	}
	@Override
	public void removeNotify() {
		this.removeChangeListener( this.changeAdapter );
		super.removeNotify();
	}
	private void updateValue() {
		if( Double.isNaN( this.time ) || Double.isNaN( this.duration ) ) {
			this.setValue( this.getMaximum() );
		} else {
			this.setValue( (int)((this.time/this.duration)*this.getMaximum()) );
		}
	}
	private void updateTime() {
		if( Double.isNaN( this.duration ) ) {
			//pass
		} else {
			this.time = this.getValue()*this.duration/this.getMaximum();
		}
	}
	
	public double getDuration() {
		return this.duration;
	}
	public void setDuration( double duration ) {
		this.duration = duration;
		this.setEnabled( Double.isNaN( this.duration )==false );
		this.updateValue();
	}
	public double getTime() {
		if( Double.isNaN( this.time ) ) {
			return this.time;
		} else {
			return edu.cmu.cs.dennisc.java.lang.DoubleUtilities.round( this.time, 2 );
		}
	}
	public void setTime( double time ) {
		this.time = time;
		this.updateValue();
	}
}
