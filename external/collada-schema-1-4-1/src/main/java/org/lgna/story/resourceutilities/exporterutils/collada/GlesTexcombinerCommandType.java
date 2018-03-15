
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gles_texcombiner_command_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gles_texcombiner_command_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="constant" type="{http://www.collada.org/2005/11/COLLADASchema}gles_texture_constant_type" minOccurs="0"/>
 *         &lt;element name="RGB" type="{http://www.collada.org/2005/11/COLLADASchema}gles_texcombiner_commandRGB_type" minOccurs="0"/>
 *         &lt;element name="alpha" type="{http://www.collada.org/2005/11/COLLADASchema}gles_texcombiner_commandAlpha_type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gles_texcombiner_command_type", namespace = "http://www.collada.org/2005/11/COLLADASchema", propOrder = {
    "constant",
    "rgb",
    "alpha"
})
public class GlesTexcombinerCommandType {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected GlesTextureConstantType constant;
    @XmlElement(name = "RGB", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected GlesTexcombinerCommandRGBType rgb;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected GlesTexcombinerCommandAlphaType alpha;

    /**
     * Gets the value of the constant property.
     * 
     * @return
     *     possible object is
     *     {@link GlesTextureConstantType }
     *     
     */
    public GlesTextureConstantType getConstant() {
        return constant;
    }

    /**
     * Sets the value of the constant property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlesTextureConstantType }
     *     
     */
    public void setConstant(GlesTextureConstantType value) {
        this.constant = value;
    }

    /**
     * Gets the value of the rgb property.
     * 
     * @return
     *     possible object is
     *     {@link GlesTexcombinerCommandRGBType }
     *     
     */
    public GlesTexcombinerCommandRGBType getRGB() {
        return rgb;
    }

    /**
     * Sets the value of the rgb property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlesTexcombinerCommandRGBType }
     *     
     */
    public void setRGB(GlesTexcombinerCommandRGBType value) {
        this.rgb = value;
    }

    /**
     * Gets the value of the alpha property.
     * 
     * @return
     *     possible object is
     *     {@link GlesTexcombinerCommandAlphaType }
     *     
     */
    public GlesTexcombinerCommandAlphaType getAlpha() {
        return alpha;
    }

    /**
     * Sets the value of the alpha property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlesTexcombinerCommandAlphaType }
     *     
     */
    public void setAlpha(GlesTexcombinerCommandAlphaType value) {
        this.alpha = value;
    }

}
