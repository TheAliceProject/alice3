
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 				Defines a set of texturing commands that will be converted into multitexturing operations using glTexEnv in regular and combiner mode.
 * 			
 * 
 * <p>Java class for gles_texture_pipeline complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gles_texture_pipeline">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="texcombiner" type="{http://www.collada.org/2005/11/COLLADASchema}gles_texcombiner_command_type"/>
 *         &lt;element name="texenv" type="{http://www.collada.org/2005/11/COLLADASchema}gles_texenv_command_type"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra"/>
 *       &lt;/choice>
 *       &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gles_texture_pipeline", namespace = "http://www.collada.org/2005/11/COLLADASchema", propOrder = {
    "texcombinerOrTexenvOrExtra"
})
public class GlesTexturePipeline {

    @XmlElements({
        @XmlElement(name = "texcombiner", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = GlesTexcombinerCommandType.class),
        @XmlElement(name = "texenv", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = GlesTexenvCommandType.class),
        @XmlElement(name = "extra", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Extra.class)
    })
    protected List<Object> texcombinerOrTexenvOrExtra;
    @XmlAttribute(name = "sid")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sid;

    /**
     * Gets the value of the texcombinerOrTexenvOrExtra property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the texcombinerOrTexenvOrExtra property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTexcombinerOrTexenvOrExtra().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GlesTexcombinerCommandType }
     * {@link GlesTexenvCommandType }
     * {@link Extra }
     * 
     * 
     */
    public List<Object> getTexcombinerOrTexenvOrExtra() {
        if (texcombinerOrTexenvOrExtra == null) {
            texcombinerOrTexenvOrExtra = new ArrayList<Object>();
        }
        return this.texcombinerOrTexenvOrExtra;
    }

    /**
     * Gets the value of the sid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSid() {
        return sid;
    }

    /**
     * Sets the value of the sid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSid(String value) {
        this.sid = value;
    }

}
