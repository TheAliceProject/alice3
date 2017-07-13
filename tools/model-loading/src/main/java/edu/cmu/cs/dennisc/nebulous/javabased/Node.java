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
import java.util.LinkedList;
import java.util.List;

import org.alice.ide.IDE;
import org.alice.interact.debug.DebugInteractUtilities;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
public abstract class Node extends Named {
    protected Node parent;
    protected List<Node> children;

    public Node() {
        this.parent = null;
        this.children = new LinkedList<Node>();
    }

    public Node getParent() {
        return this.parent;
    }

    public int getChildCount() {
        return this.children.size();
    }

    public Node getChildAt(int index) {
        return this.children.get(index);
    }

    public Node getDescendant(short reference) {
        if (this.getReference() == reference) {
            return this;
        }
        for (Node child : this.children) {
            Node descendant = child.getDescendant(reference);
            if (descendant != null) {
                return descendant;
            }
        }
        return null;
    }
    
    public Node getDescendantNamed(String name) {
        if (this.getName() != null && this.getName().equals(name)) {
            return this;
        }
        for (Node child : this.children) {
            Node descendant = child.getDescendantNamed(name);
            if (descendant != null) {
                return descendant;
            }
        }
        return null;
    }
    
    public boolean isDescendantOf( Node possibleAncestor ) {
        if( possibleAncestor == null ) {
            return false;
        }
        if( this.parent == possibleAncestor ) {
            return true;
        } else {
            if( this.parent == null ) {
                return false;
            } else {
                return this.parent.isDescendantOf( possibleAncestor );
            }
        }
    }
    
    public boolean isAncestorOf( Node component ) {
        if( component == null ) {
            return false;
        } else {
            return component.isDescendantOf( this );
        }
    }

    public boolean isSceneOf( Node other ) {
        return false;
    }
    public boolean isParentOf( Node other ) {
        return this == other.getParent();
    }
    public boolean isLocalOf( Node other ) {
        return this == other;
    }

    public Node resolveReference(short reference) {
        return this.getDescendant(reference);
    }

    @Override
    public int getClassID() {
        return Element.NODE;
    }

    public void addChild(Node child) {
        child.parent = this;
        this.children.add(child);
    }

    public void renderVisualization(edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc)
    {
//        if (DebugInteractUtilities.isDebugEnabled())
//        {
//            rc.gl.glPushMatrix();
//            renderSelfVisualization(rc);
//            renderChildrenVisualization(rc);
//            rc.gl.glPopMatrix();
//        }
    }
    
    protected void renderChildrenVisualization(edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc)
    {
        if (DebugInteractUtilities.isDebugEnabled())
        {
            for (Node child : this.children)
            {
                if (child != null)
                {
                    child.renderVisualization(rc);
                }
            }
        }
    }
    
    protected abstract void renderSelfVisualization(edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc); 

    @Override
    protected void readInternal(InputStream iStream, int nVersion)
            throws IOException {
        super.readInternal(iStream, nVersion);
        int numChildren = Utilities.readInt(iStream);
        this.children = new LinkedList<Node>();
        for (int i = 0; i < numChildren; i++) {
            Element child = Element.construct(iStream, nVersion);
            assert child instanceof Node;
            Node nodeChild = (Node) child;
            nodeChild.parent = this;
            this.children.add(nodeChild);
        }
    }

    @Override
    protected void writeInternal(OutputStream oStream) throws IOException {
        super.writeInternal(oStream);
        Utilities.writeInt(this.children.size(), oStream);
        for (Node child : this.children) {
            child.writeWithoutVersion(oStream);
        }
    }

}
