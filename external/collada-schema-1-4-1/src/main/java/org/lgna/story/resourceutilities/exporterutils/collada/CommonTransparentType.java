
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for common_transparent_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="common_transparent_type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type">
 *       &lt;attribute name="opaque" type="{http://www.collada.org/2005/11/COLLADASchema}fx_opaque_enum" default="A_ONE" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "common_transparent_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class CommonTransparentType
    extends CommonColorOrTextureType
{

    @XmlAttribute(name = "opaque")
    protected FxOpaqueEnum opaque;

    /**
     * Gets the value of the opaque property.
     * 
     * @return
     *     possible object is
     *     {@link FxOpaqueEnum }
     *     
     */
    public FxOpaqueEnum getOpaque() {
        if (opaque == null) {
            return FxOpaqueEnum.A_ONE;
        } else {
            return opaque;
        }
    }

    /**
     * Sets the value of the opaque property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxOpaqueEnum }
     *     
     */
    public void setOpaque(FxOpaqueEnum value) {
        this.opaque = value;
    }

}
