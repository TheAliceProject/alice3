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

package edu.cmu.cs.dennisc.nebulous.javabased;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.cmu.cs.dennisc.math.AngleInRadians;

public class EulerAngles extends EulerNumbers 
{
    
    public enum Order
    {
        INVALID(0, edu.cmu.cs.dennisc.math.EulerAngles.Order.NOT_APPLICABLE),
        XYZ(1, edu.cmu.cs.dennisc.math.EulerAngles.Order.PITCH_YAW_ROLL),
        YZX(2, edu.cmu.cs.dennisc.math.EulerAngles.Order.YAW_ROLL_PITCH),
        ZXY(3, edu.cmu.cs.dennisc.math.EulerAngles.Order.ROLL_PITCH_YAW),
        XZY(4, edu.cmu.cs.dennisc.math.EulerAngles.Order.PITCH_ROLL_YAW),
        YXZ(5, edu.cmu.cs.dennisc.math.EulerAngles.Order.YAW_PITCH_ROLL),
        ZYX(6, edu.cmu.cs.dennisc.math.EulerAngles.Order.ROLL_YAW_PITCH);
        
        private int val;
        private edu.cmu.cs.dennisc.math.EulerAngles.Order order;
        
        private Order(int val, edu.cmu.cs.dennisc.math.EulerAngles.Order order)
        {
            this.val = val;
            this.order = order;
        }
        
        public int getVal()
        {
            return val;
        }
        
        public edu.cmu.cs.dennisc.math.EulerAngles.Order getAliceOrder()
        {
            return order;
        }
        
        public static Order getOrder(edu.cmu.cs.dennisc.math.EulerAngles.Order order)
        {
            for (Order o : Order.values())
            {
                if (o.getAliceOrder() == order)
                {
                    return o;
                }
            }
            return INVALID;
        }
        
        public static Order getOrder(int val)
        {
            for (Order o : Order.values())
            {
                if (o.getVal() == val)
                {
                    return o;
                }
            }
            return INVALID;
        }
    }
    
    private Order order;
    
    public EulerAngles()
    {
        super();
        this.order = Order.INVALID;
    }
    
    public EulerAngles(float x, float y, float z)
    {
        this(x,y,z, Order.INVALID);
    }
    
    public EulerAngles(float x, float y, float z, Order order)
    {
        super(x,y,z);
        this.order = order;
    }
    
    public void setOrder( Order eOrder ) 
    {
        this.order = eOrder;
    }
    
    public edu.cmu.cs.dennisc.math.EulerAngles createAliceEulerAngles()
    {
        return new edu.cmu.cs.dennisc.math.EulerAngles(new AngleInRadians(getX()), new AngleInRadians(getY()), new AngleInRadians(getZ()), order.getAliceOrder());
    }
    
    @Override
    public void read(InputStream iStream) throws IOException 
    {
        super.read(iStream);
        int orderInt = Utilities.readInt(iStream);
        this.order = Order.getOrder(orderInt);
    }
    
    @Override
    public void write(OutputStream oStream) throws IOException 
    {
        super.write(oStream);
        Utilities.writeInt(this.order.getVal(), oStream);
    }
}
