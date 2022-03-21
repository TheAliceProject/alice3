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

package org.alice.stageide.properties;

import org.alice.ide.croquet.models.StandardExpressionState;
import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.lgna.story.implementation.ModelImp;
import org.lgna.story.implementation.Property;
import org.lgna.story.implementation.Property.Listener;

public class ModelOpacityAdapter extends AbstractPropertyAdapter<Double, ModelImp> {

  private Listener<Float> propertyListener;
  private Property<Float> property;

  private void initializeListenersIfNecessary() {
    if (this.propertyListener == null) {
      this.propertyListener = () -> handleInternalValueChanged();
    }
  }

  @Override
  protected void startPropertyListening() {
    super.startPropertyListening();
    if (this.instance != null) {
      this.initializeListenersIfNecessary();
      this.addPropertyListener(this.propertyListener);
    }
  }

  @Override
  protected void stopPropertyListening() {
    super.stopPropertyListening();
    if (this.instance != null) {
      this.removePropertyListener(this.propertyListener);
    }
  }

  public ModelOpacityAdapter(ModelImp instance, StandardExpressionState expressionState) {
    super("Opacity", instance, expressionState);
    this.property = instance.opacity;
    this.startPropertyListening();
    this.initializeExpressionState();
  }

  @Override
  public Double getValue() {
    if (this.property != null) {
      return this.property.getValue().doubleValue();
    } else {
      return null;
    }
  }

  @Override
  public Class<Double> getPropertyType() {
    return Double.class;
  }

  @Override
  public void setValue(final Double value) {
    super.setValue(value);
    if (this.property != null) {
      new Thread() {
        @Override
        public void run() {
          ModelOpacityAdapter.this.property.setValue(value.floatValue());
        }
      }.start();
    }
  }

  protected void addPropertyListener(Listener<Float> propertyListener) {
    if (this.property != null) {
      property.addPropertyListener(propertyListener);
    }
  }

  protected void removePropertyListener(Listener<Float> propertyListener) {
    if (this.property != null) {
      property.removePropertyListener(propertyListener);
    }
  }

  protected void handleInternalValueChanged() {
    Double newValue = this.getValue();
    this.notifyValueObservers(newValue);
  }

  @Override
  public Double getValueCopyIfMutable() {
    return new Double(this.getValue());
  }

}
