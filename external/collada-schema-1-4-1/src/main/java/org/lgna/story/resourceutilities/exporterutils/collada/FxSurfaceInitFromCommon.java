
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * 
 * 				This element is an IDREF which specifies the image to use to initialize a specific mip of a 1D or 2D surface, 3D slice, or Cube face.
 * 			
 * 
 * <p>Java class for fx_surface_init_from_common complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fx_surface_init_from_common">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>IDREF">
 *       &lt;attribute name="mip" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" default="0" />
 *       &lt;attribute name="slice" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" default="0" />
 *       &lt;attribute name="face" type="{http://www.collada.org/2005/11/COLLADASchema}fx_surface_face_enum" default="POSITIVE_X" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fx_surface_init_from_common", namespace = "http://www.collada.org/2005/11/COLLADASchema", propOrder = {
    "value"
})
public class FxSurfaceInitFromCommon {

    @XmlValue
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object value;
    @XmlAttribute(name = "mip")
    @XmlSchemaType(name = "unsignedInt")
    protected Long mip;
    @XmlAttribute(name = "slice")
    @XmlSchemaType(name = "unsignedInt")
    protected Long slice;
    @XmlAttribute(name = "face")
    protected FxSurfaceFaceEnum face;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Gets the value of the mip property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getMip() {
        if (mip == null) {
            return  0L;
        } else {
            return mip;
        }
    }

    /**
     * Sets the value of the mip property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMip(Long value) {
        this.mip = value;
    }

    /**
     * Gets the value of the slice property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getSlice() {
        if (slice == null) {
            return  0L;
        } else {
            return slice;
        }
    }

    /**
     * Sets the value of the slice property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSlice(Long value) {
        this.slice = value;
    }

    /**
     * Gets the value of the face property.
     * 
     * @return
     *     possible object is
     *     {@link FxSurfaceFaceEnum }
     *     
     */
    public FxSurfaceFaceEnum getFace() {
        if (face == null) {
            return FxSurfaceFaceEnum.POSITIVE_X;
        } else {
            return face;
        }
    }

    /**
     * Sets the value of the face property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSurfaceFaceEnum }
     *     
     */
    public void setFace(FxSurfaceFaceEnum value) {
        this.face = value;
    }

}
