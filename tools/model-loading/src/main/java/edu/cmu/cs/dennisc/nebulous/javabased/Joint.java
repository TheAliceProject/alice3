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
 *    "Alice" appear in their name, without prior written permision of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art asets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Asets must also retain the copyright
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

import edu.cmu.cs.dennisc.math.Vector3f;

public class Joint extends Transform
{

    private boolean bIsFreeInX = false;
    private boolean bIsFreeInY = false;
    private boolean bIsFreeInZ = false;
    private EulerNumbers oStiffness = new EulerNumbers();
    private EulerAngles oBoneOrientation = new EulerAngles();
    private EulerAngles oPreferedAngles = new EulerAngles();
    private EulerAngles oLocalRotationAxis = new EulerAngles();
    private EulerNumbers oMinimumDampRange = new EulerNumbers();
    private EulerNumbers oMaximumDampRange = new EulerNumbers();
    private EulerNumbers oMinimumDampStrength = new EulerNumbers();
    private EulerNumbers oMaximumDampStrength = new EulerNumbers();
    
    @Override
    public int getClassID() 
    {
        return Element.JOINT;
    }
    
    @Override
    protected void readInternal(InputStream iStream, int nVersion) throws IOException 
    {
        super.readInternal(iStream, nVersion);
        this.bIsFreeInX = Utilities.readBool(iStream);
        this.bIsFreeInY = Utilities.readBool(iStream);
        this.bIsFreeInZ = Utilities.readBool(iStream);
        this.oStiffness.read(iStream);
        this.oBoneOrientation.read(iStream);
        this.oPreferedAngles.read(iStream);
        this.oLocalRotationAxis.read(iStream);
        this.oMinimumDampRange.read(iStream);
        this.oMaximumDampRange.read(iStream);
        this.oMinimumDampStrength.read(iStream);
        this.oMaximumDampStrength.read(iStream);
    }

    @Override
    protected void writeInternal(OutputStream oStream) throws IOException 
    {
        super.writeInternal(oStream);
        Utilities.writeBool(this.bIsFreeInX, oStream);
        Utilities.writeBool(this.bIsFreeInY, oStream);
        Utilities.writeBool(this.bIsFreeInZ, oStream);
        this.oStiffness.write(oStream);
        this.oBoneOrientation.write(oStream);
        this.oPreferedAngles.write(oStream);
        this.oLocalRotationAxis.write(oStream);
        this.oMinimumDampRange.write(oStream);
        this.oMaximumDampRange.write(oStream);
        this.oMinimumDampStrength.write(oStream);
        this.oMaximumDampStrength.write(oStream);
    }
    
    
    public static edu.cmu.cs.dennisc.nebulous.javabased.Joint createWritableJoint(edu.cmu.cs.dennisc.scenegraph.Joint sgJoint)
    {
        edu.cmu.cs.dennisc.nebulous.javabased.Joint joint = new edu.cmu.cs.dennisc.nebulous.javabased.Joint();
        joint.setLocalTransformation(new AffineMatrix(sgJoint.localTransformation.getValue()));
        joint.setIsFreeInX(sgJoint.isFreeInX.getValue());
        joint.setIsFreeInY(sgJoint.isFreeInY.getValue());
        joint.setIsFreeInZ(sgJoint.isFreeInZ.getValue());
        joint.setStiffness(new edu.cmu.cs.dennisc.nebulous.javabased.EulerNumbers(sgJoint.oStiffness.getValue().x, sgJoint.oStiffness.getValue().x, sgJoint.oStiffness.getValue().x));
        joint.setMinimumDampRange(new edu.cmu.cs.dennisc.nebulous.javabased.EulerNumbers(sgJoint.oMinimumDampRange.getValue().x, sgJoint.oMinimumDampRange.getValue().x, sgJoint.oMinimumDampRange.getValue().x));
        joint.setMaximumDampRange(new edu.cmu.cs.dennisc.nebulous.javabased.EulerNumbers(sgJoint.oMaximumDampRange.getValue().x, sgJoint.oMaximumDampRange.getValue().x, sgJoint.oMaximumDampRange.getValue().x));
        joint.setMinimumDampStrength(new edu.cmu.cs.dennisc.nebulous.javabased.EulerNumbers(sgJoint.oMinimumDampStrength.getValue().x, sgJoint.oMinimumDampStrength.getValue().x, sgJoint.oMinimumDampStrength.getValue().x));
        joint.setMaximumDampStrength(new edu.cmu.cs.dennisc.nebulous.javabased.EulerNumbers(sgJoint.oMaximumDampStrength.getValue().x, sgJoint.oMaximumDampStrength.getValue().x, sgJoint.oMaximumDampStrength.getValue().x));
        joint.setBoneOrientation( new edu.cmu.cs.dennisc.nebulous.javabased.EulerAngles(
                (float)sgJoint.oBoneOrientation.getValue().pitch.getAsRadians(), 
                (float)sgJoint.oBoneOrientation.getValue().yaw.getAsRadians(), 
                (float)sgJoint.oBoneOrientation.getValue().roll.getAsRadians(), 
                edu.cmu.cs.dennisc.nebulous.javabased.EulerAngles.Order.getOrder(sgJoint.oBoneOrientation.getValue().order))
        );
        joint.setPreferedAngles( new edu.cmu.cs.dennisc.nebulous.javabased.EulerAngles(
                (float)sgJoint.oPreferedAngles.getValue().pitch.getAsRadians(), 
                (float)sgJoint.oPreferedAngles.getValue().yaw.getAsRadians(), 
                (float)sgJoint.oPreferedAngles.getValue().roll.getAsRadians(), 
                edu.cmu.cs.dennisc.nebulous.javabased.EulerAngles.Order.getOrder(sgJoint.oPreferedAngles.getValue().order))
        );
        joint.setLocalRotationAxis( new edu.cmu.cs.dennisc.nebulous.javabased.EulerAngles(
                (float)sgJoint.oLocalRotationAxis.getValue().pitch.getAsRadians(), 
                (float)sgJoint.oLocalRotationAxis.getValue().yaw.getAsRadians(), 
                (float)sgJoint.oLocalRotationAxis.getValue().roll.getAsRadians(), 
                edu.cmu.cs.dennisc.nebulous.javabased.EulerAngles.Order.getOrder(sgJoint.oLocalRotationAxis.getValue().order))
        );
        joint.setName(sgJoint.getName());
//        joint.setReference(sgJoint.jointID.getValue().shortValue());
        return joint;
    }
    

    public edu.cmu.cs.dennisc.scenegraph.Joint createSGJoint()
    {
        edu.cmu.cs.dennisc.scenegraph.Joint sgJoint = new edu.cmu.cs.dennisc.scenegraph.Joint();
        sgJoint.localTransformation.setValue(AffineMatrix.createAliceMatrix(this.getLocalTransformation()));
        sgJoint.isFreeInX.setValue(this.isFreeInX());
        sgJoint.isFreeInY.setValue(this.isFreeInY());
        sgJoint.isFreeInZ.setValue(this.isFreeInZ());
        sgJoint.oStiffness.setValue(new Vector3f(this.getStiffness().getX(), this.getStiffness().getY(), this.getStiffness().getZ()));
        sgJoint.oMinimumDampRange.setValue(new Vector3f(this.getMinimumDampRange().getX(), this.getMinimumDampRange().getY(), this.getMinimumDampRange().getZ()));
        sgJoint.oMaximumDampRange.setValue(new Vector3f(this.getMaximumDampRange().getX(), this.getMaximumDampRange().getY(), this.getMaximumDampRange().getZ()));
        sgJoint.oMinimumDampStrength.setValue(new Vector3f(this.getMinimumDampStrength().getX(), this.getMinimumDampStrength().getY(), this.getMinimumDampStrength().getZ()));
        sgJoint.oMaximumDampStrength.setValue(new Vector3f(this.getMaximumDampStrength().getX(), this.getMaximumDampStrength().getY(), this.getMaximumDampStrength().getZ()));
        sgJoint.oBoneOrientation.setValue(this.getBoneOrientation().createAliceEulerAngles());
        sgJoint.oPreferedAngles.setValue(this.getPreferedAngles().createAliceEulerAngles());
        sgJoint.oLocalRotationAxis.setValue(this.getLocalRotationAxis().createAliceEulerAngles());
        sgJoint.jointID.setValue(this.getName());
        sgJoint.setName(this.getName());
        return sgJoint;
    }
    
    public boolean isFreeInX()
    {
        return bIsFreeInX;
    }

    public void setIsFreeInX(boolean bIsFreeInX)
    {
        this.bIsFreeInX = bIsFreeInX;
    }

    public boolean isFreeInY()
    {
        return bIsFreeInY;
    }

    public void setIsFreeInY(boolean bIsFreeInY)
    {
        this.bIsFreeInY = bIsFreeInY;
    }

    public boolean isFreeInZ()
    {
        return bIsFreeInZ;
    }

    public void setIsFreeInZ(boolean bIsFreeInZ)
    {
        this.bIsFreeInZ = bIsFreeInZ;
    }

    public EulerNumbers getStiffness()
    {
        return oStiffness;
    }

    public void setStiffness(EulerNumbers oStiffness)
    {
        this.oStiffness = oStiffness;
    }

    public EulerAngles getBoneOrientation()
    {
        return oBoneOrientation;
    }

    public void setBoneOrientation(EulerAngles oBoneOrientation)
    {
        this.oBoneOrientation = oBoneOrientation;
    }

    public EulerAngles getPreferedAngles()
    {
        return oPreferedAngles;
    }

    public void setPreferedAngles(EulerAngles oPreferedAngles)
    {
        this.oPreferedAngles = oPreferedAngles;
    }

    public EulerAngles getLocalRotationAxis()
    {
        return oLocalRotationAxis;
    }

    public void setLocalRotationAxis(EulerAngles oLocalRotationAxis)
    {
        this.oLocalRotationAxis = oLocalRotationAxis;
    }

    public EulerNumbers getMinimumDampRange()
    {
        return oMinimumDampRange;
    }

    public void setMinimumDampRange(EulerNumbers oMinimumDampRange)
    {
        this.oMinimumDampRange = oMinimumDampRange;
    }

    public EulerNumbers getMaximumDampRange()
    {
        return oMaximumDampRange;
    }

    public void setMaximumDampRange(EulerNumbers oMaximumDampRange)
    {
        this.oMaximumDampRange = oMaximumDampRange;
    }

    public EulerNumbers getMinimumDampStrength()
    {
        return oMinimumDampStrength;
    }

    public void setMinimumDampStrength(EulerNumbers oMinimumDampStrength)
    {
        this.oMinimumDampStrength = oMinimumDampStrength;
    }

    public EulerNumbers getMaximumDampStrength()
    {
        return oMaximumDampStrength;
    }

    public void setMaximumDampStrength(EulerNumbers oMaximumDampStrength)
    {
        this.oMaximumDampStrength = oMaximumDampStrength;
    }
    
}
