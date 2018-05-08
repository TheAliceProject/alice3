
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}asset" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}image"/>
 *           &lt;element name="newparam" type="{http://www.collada.org/2005/11/COLLADASchema}common_newparam_type"/>
 *         &lt;/choice>
 *         &lt;element name="technique">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}asset" minOccurs="0"/>
 *                   &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}image"/>
 *                     &lt;element name="newparam" type="{http://www.collada.org/2005/11/COLLADASchema}common_newparam_type"/>
 *                   &lt;/choice>
 *                   &lt;choice>
 *                     &lt;element name="constant">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
 *                               &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="lambert">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="ambient" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="diffuse" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
 *                               &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="phong">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="ambient" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="diffuse" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="specular" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="shininess" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
 *                               &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="blinn">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="ambient" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="diffuse" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="specular" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="shininess" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
 *                               &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
 *                               &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                               &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/choice>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *                 &lt;attribute name="sid" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "asset",
    "imageOrNewparam",
    "technique",
    "extra"
})
public class ProfileCOMMON {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Asset asset;
    @XmlElements({
        @XmlElement(name = "image", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Image.class),
        @XmlElement(name = "newparam", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = CommonNewparamType.class)
    })
    protected List<Object> imageOrNewparam;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected ProfileCOMMON.Technique technique;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the asset property.
     * 
     * @return
     *     possible object is
     *     {@link Asset }
     *     
     */
    public Asset getAsset() {
        return asset;
    }

    /**
     * Sets the value of the asset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Asset }
     *     
     */
    public void setAsset(Asset value) {
        this.asset = value;
    }

    /**
     * Gets the value of the imageOrNewparam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imageOrNewparam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImageOrNewparam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Image }
     * {@link CommonNewparamType }
     * 
     * 
     */
    public List<Object> getImageOrNewparam() {
        if (imageOrNewparam == null) {
            imageOrNewparam = new ArrayList<Object>();
        }
        return this.imageOrNewparam;
    }

    /**
     * Gets the value of the technique property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileCOMMON.Technique }
     *     
     */
    public ProfileCOMMON.Technique getTechnique() {
        return technique;
    }

    /**
     * Sets the value of the technique property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileCOMMON.Technique }
     *     
     */
    public void setTechnique(ProfileCOMMON.Technique value) {
        this.technique = value;
    }

    /**
     * 
     * 							The extra element may appear any number of times.
     * 						Gets the value of the extra property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extra property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtra().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Extra }
     * 
     * 
     */
    public List<Extra> getExtra() {
        if (extra == null) {
            extra = new ArrayList<Extra>();
        }
        return this.extra;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}asset" minOccurs="0"/>
     *         &lt;choice maxOccurs="unbounded" minOccurs="0">
     *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}image"/>
     *           &lt;element name="newparam" type="{http://www.collada.org/2005/11/COLLADASchema}common_newparam_type"/>
     *         &lt;/choice>
     *         &lt;choice>
     *           &lt;element name="constant">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
     *                     &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *           &lt;element name="lambert">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="ambient" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="diffuse" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
     *                     &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *           &lt;element name="phong">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="ambient" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="diffuse" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="specular" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="shininess" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
     *                     &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *           &lt;element name="blinn">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="ambient" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="diffuse" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="specular" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="shininess" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
     *                     &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
     *                     &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                     &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *         &lt;/choice>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
     *       &lt;attribute name="sid" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "asset",
        "imageOrNewparam",
        "constant",
        "lambert",
        "phong",
        "blinn",
        "extra"
    })
    public static class Technique {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected Asset asset;
        @XmlElements({
            @XmlElement(name = "image", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Image.class),
            @XmlElement(name = "newparam", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = CommonNewparamType.class)
        })
        protected List<Object> imageOrNewparam;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected ProfileCOMMON.Technique.Constant constant;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected ProfileCOMMON.Technique.Lambert lambert;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected ProfileCOMMON.Technique.Phong phong;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected ProfileCOMMON.Technique.Blinn blinn;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<Extra> extra;
        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected String id;
        @XmlAttribute(name = "sid", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String sid;

        /**
         * 
         * 										The technique element may contain an asset element.
         * 									
         * 
         * @return
         *     possible object is
         *     {@link Asset }
         *     
         */
        public Asset getAsset() {
            return asset;
        }

        /**
         * Sets the value of the asset property.
         * 
         * @param value
         *     allowed object is
         *     {@link Asset }
         *     
         */
        public void setAsset(Asset value) {
            this.asset = value;
        }

        /**
         * Gets the value of the imageOrNewparam property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the imageOrNewparam property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getImageOrNewparam().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Image }
         * {@link CommonNewparamType }
         * 
         * 
         */
        public List<Object> getImageOrNewparam() {
            if (imageOrNewparam == null) {
                imageOrNewparam = new ArrayList<Object>();
            }
            return this.imageOrNewparam;
        }

        /**
         * Gets the value of the constant property.
         * 
         * @return
         *     possible object is
         *     {@link ProfileCOMMON.Technique.Constant }
         *     
         */
        public ProfileCOMMON.Technique.Constant getConstant() {
            return constant;
        }

        /**
         * Sets the value of the constant property.
         * 
         * @param value
         *     allowed object is
         *     {@link ProfileCOMMON.Technique.Constant }
         *     
         */
        public void setConstant(ProfileCOMMON.Technique.Constant value) {
            this.constant = value;
        }

        /**
         * Gets the value of the lambert property.
         * 
         * @return
         *     possible object is
         *     {@link ProfileCOMMON.Technique.Lambert }
         *     
         */
        public ProfileCOMMON.Technique.Lambert getLambert() {
            return lambert;
        }

        /**
         * Sets the value of the lambert property.
         * 
         * @param value
         *     allowed object is
         *     {@link ProfileCOMMON.Technique.Lambert }
         *     
         */
        public void setLambert(ProfileCOMMON.Technique.Lambert value) {
            this.lambert = value;
        }

        /**
         * Gets the value of the phong property.
         * 
         * @return
         *     possible object is
         *     {@link ProfileCOMMON.Technique.Phong }
         *     
         */
        public ProfileCOMMON.Technique.Phong getPhong() {
            return phong;
        }

        /**
         * Sets the value of the phong property.
         * 
         * @param value
         *     allowed object is
         *     {@link ProfileCOMMON.Technique.Phong }
         *     
         */
        public void setPhong(ProfileCOMMON.Technique.Phong value) {
            this.phong = value;
        }

        /**
         * Gets the value of the blinn property.
         * 
         * @return
         *     possible object is
         *     {@link ProfileCOMMON.Technique.Blinn }
         *     
         */
        public ProfileCOMMON.Technique.Blinn getBlinn() {
            return blinn;
        }

        /**
         * Sets the value of the blinn property.
         * 
         * @param value
         *     allowed object is
         *     {@link ProfileCOMMON.Technique.Blinn }
         *     
         */
        public void setBlinn(ProfileCOMMON.Technique.Blinn value) {
            this.blinn = value;
        }

        /**
         * 
         * 										The extra element may appear any number of times.
         * 									Gets the value of the extra property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the extra property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getExtra().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Extra }
         * 
         * 
         */
        public List<Extra> getExtra() {
            if (extra == null) {
                extra = new ArrayList<Extra>();
            }
            return this.extra;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
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


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="ambient" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="diffuse" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="specular" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="shininess" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
         *         &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "emission",
            "ambient",
            "diffuse",
            "specular",
            "shininess",
            "reflective",
            "reflectivity",
            "transparent",
            "transparency",
            "indexOfRefraction"
        })
        public static class Blinn {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType emission;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType ambient;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType diffuse;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType specular;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType shininess;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType reflective;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType reflectivity;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonTransparentType transparent;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType transparency;
            @XmlElement(name = "index_of_refraction", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType indexOfRefraction;

            /**
             * Gets the value of the emission property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getEmission() {
                return emission;
            }

            /**
             * Sets the value of the emission property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setEmission(CommonColorOrTextureType value) {
                this.emission = value;
            }

            /**
             * Gets the value of the ambient property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getAmbient() {
                return ambient;
            }

            /**
             * Sets the value of the ambient property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setAmbient(CommonColorOrTextureType value) {
                this.ambient = value;
            }

            /**
             * Gets the value of the diffuse property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getDiffuse() {
                return diffuse;
            }

            /**
             * Sets the value of the diffuse property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setDiffuse(CommonColorOrTextureType value) {
                this.diffuse = value;
            }

            /**
             * Gets the value of the specular property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getSpecular() {
                return specular;
            }

            /**
             * Sets the value of the specular property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setSpecular(CommonColorOrTextureType value) {
                this.specular = value;
            }

            /**
             * Gets the value of the shininess property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getShininess() {
                return shininess;
            }

            /**
             * Sets the value of the shininess property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setShininess(CommonFloatOrParamType value) {
                this.shininess = value;
            }

            /**
             * Gets the value of the reflective property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getReflective() {
                return reflective;
            }

            /**
             * Sets the value of the reflective property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setReflective(CommonColorOrTextureType value) {
                this.reflective = value;
            }

            /**
             * Gets the value of the reflectivity property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getReflectivity() {
                return reflectivity;
            }

            /**
             * Sets the value of the reflectivity property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setReflectivity(CommonFloatOrParamType value) {
                this.reflectivity = value;
            }

            /**
             * Gets the value of the transparent property.
             * 
             * @return
             *     possible object is
             *     {@link CommonTransparentType }
             *     
             */
            public CommonTransparentType getTransparent() {
                return transparent;
            }

            /**
             * Sets the value of the transparent property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonTransparentType }
             *     
             */
            public void setTransparent(CommonTransparentType value) {
                this.transparent = value;
            }

            /**
             * Gets the value of the transparency property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getTransparency() {
                return transparency;
            }

            /**
             * Sets the value of the transparency property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setTransparency(CommonFloatOrParamType value) {
                this.transparency = value;
            }

            /**
             * Gets the value of the indexOfRefraction property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getIndexOfRefraction() {
                return indexOfRefraction;
            }

            /**
             * Sets the value of the indexOfRefraction property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setIndexOfRefraction(CommonFloatOrParamType value) {
                this.indexOfRefraction = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
         *         &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "emission",
            "reflective",
            "reflectivity",
            "transparent",
            "transparency",
            "indexOfRefraction"
        })
        public static class Constant {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType emission;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType reflective;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType reflectivity;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonTransparentType transparent;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType transparency;
            @XmlElement(name = "index_of_refraction", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType indexOfRefraction;

            /**
             * Gets the value of the emission property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getEmission() {
                return emission;
            }

            /**
             * Sets the value of the emission property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setEmission(CommonColorOrTextureType value) {
                this.emission = value;
            }

            /**
             * Gets the value of the reflective property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getReflective() {
                return reflective;
            }

            /**
             * Sets the value of the reflective property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setReflective(CommonColorOrTextureType value) {
                this.reflective = value;
            }

            /**
             * Gets the value of the reflectivity property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getReflectivity() {
                return reflectivity;
            }

            /**
             * Sets the value of the reflectivity property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setReflectivity(CommonFloatOrParamType value) {
                this.reflectivity = value;
            }

            /**
             * Gets the value of the transparent property.
             * 
             * @return
             *     possible object is
             *     {@link CommonTransparentType }
             *     
             */
            public CommonTransparentType getTransparent() {
                return transparent;
            }

            /**
             * Sets the value of the transparent property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonTransparentType }
             *     
             */
            public void setTransparent(CommonTransparentType value) {
                this.transparent = value;
            }

            /**
             * Gets the value of the transparency property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getTransparency() {
                return transparency;
            }

            /**
             * Sets the value of the transparency property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setTransparency(CommonFloatOrParamType value) {
                this.transparency = value;
            }

            /**
             * Gets the value of the indexOfRefraction property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getIndexOfRefraction() {
                return indexOfRefraction;
            }

            /**
             * Sets the value of the indexOfRefraction property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setIndexOfRefraction(CommonFloatOrParamType value) {
                this.indexOfRefraction = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="ambient" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="diffuse" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
         *         &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "emission",
            "ambient",
            "diffuse",
            "reflective",
            "reflectivity",
            "transparent",
            "transparency",
            "indexOfRefraction"
        })
        public static class Lambert {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType emission;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType ambient;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType diffuse;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType reflective;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType reflectivity;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonTransparentType transparent;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType transparency;
            @XmlElement(name = "index_of_refraction", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType indexOfRefraction;

            /**
             * Gets the value of the emission property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getEmission() {
                return emission;
            }

            /**
             * Sets the value of the emission property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setEmission(CommonColorOrTextureType value) {
                this.emission = value;
            }

            /**
             * Gets the value of the ambient property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getAmbient() {
                return ambient;
            }

            /**
             * Sets the value of the ambient property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setAmbient(CommonColorOrTextureType value) {
                this.ambient = value;
            }

            /**
             * Gets the value of the diffuse property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getDiffuse() {
                return diffuse;
            }

            /**
             * Sets the value of the diffuse property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setDiffuse(CommonColorOrTextureType value) {
                this.diffuse = value;
            }

            /**
             * Gets the value of the reflective property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getReflective() {
                return reflective;
            }

            /**
             * Sets the value of the reflective property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setReflective(CommonColorOrTextureType value) {
                this.reflective = value;
            }

            /**
             * Gets the value of the reflectivity property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getReflectivity() {
                return reflectivity;
            }

            /**
             * Sets the value of the reflectivity property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setReflectivity(CommonFloatOrParamType value) {
                this.reflectivity = value;
            }

            /**
             * Gets the value of the transparent property.
             * 
             * @return
             *     possible object is
             *     {@link CommonTransparentType }
             *     
             */
            public CommonTransparentType getTransparent() {
                return transparent;
            }

            /**
             * Sets the value of the transparent property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonTransparentType }
             *     
             */
            public void setTransparent(CommonTransparentType value) {
                this.transparent = value;
            }

            /**
             * Gets the value of the transparency property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getTransparency() {
                return transparency;
            }

            /**
             * Sets the value of the transparency property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setTransparency(CommonFloatOrParamType value) {
                this.transparency = value;
            }

            /**
             * Gets the value of the indexOfRefraction property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getIndexOfRefraction() {
                return indexOfRefraction;
            }

            /**
             * Sets the value of the indexOfRefraction property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setIndexOfRefraction(CommonFloatOrParamType value) {
                this.indexOfRefraction = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="emission" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="ambient" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="diffuse" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="specular" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="shininess" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="reflective" type="{http://www.collada.org/2005/11/COLLADASchema}common_color_or_texture_type" minOccurs="0"/>
         *         &lt;element name="reflectivity" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="transparent" type="{http://www.collada.org/2005/11/COLLADASchema}common_transparent_type" minOccurs="0"/>
         *         &lt;element name="transparency" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *         &lt;element name="index_of_refraction" type="{http://www.collada.org/2005/11/COLLADASchema}common_float_or_param_type" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "emission",
            "ambient",
            "diffuse",
            "specular",
            "shininess",
            "reflective",
            "reflectivity",
            "transparent",
            "transparency",
            "indexOfRefraction"
        })
        public static class Phong {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType emission;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType ambient;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType diffuse;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType specular;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType shininess;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonColorOrTextureType reflective;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType reflectivity;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonTransparentType transparent;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType transparency;
            @XmlElement(name = "index_of_refraction", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected CommonFloatOrParamType indexOfRefraction;

            /**
             * Gets the value of the emission property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getEmission() {
                return emission;
            }

            /**
             * Sets the value of the emission property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setEmission(CommonColorOrTextureType value) {
                this.emission = value;
            }

            /**
             * Gets the value of the ambient property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getAmbient() {
                return ambient;
            }

            /**
             * Sets the value of the ambient property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setAmbient(CommonColorOrTextureType value) {
                this.ambient = value;
            }

            /**
             * Gets the value of the diffuse property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getDiffuse() {
                return diffuse;
            }

            /**
             * Sets the value of the diffuse property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setDiffuse(CommonColorOrTextureType value) {
                this.diffuse = value;
            }

            /**
             * Gets the value of the specular property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getSpecular() {
                return specular;
            }

            /**
             * Sets the value of the specular property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setSpecular(CommonColorOrTextureType value) {
                this.specular = value;
            }

            /**
             * Gets the value of the shininess property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getShininess() {
                return shininess;
            }

            /**
             * Sets the value of the shininess property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setShininess(CommonFloatOrParamType value) {
                this.shininess = value;
            }

            /**
             * Gets the value of the reflective property.
             * 
             * @return
             *     possible object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public CommonColorOrTextureType getReflective() {
                return reflective;
            }

            /**
             * Sets the value of the reflective property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonColorOrTextureType }
             *     
             */
            public void setReflective(CommonColorOrTextureType value) {
                this.reflective = value;
            }

            /**
             * Gets the value of the reflectivity property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getReflectivity() {
                return reflectivity;
            }

            /**
             * Sets the value of the reflectivity property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setReflectivity(CommonFloatOrParamType value) {
                this.reflectivity = value;
            }

            /**
             * Gets the value of the transparent property.
             * 
             * @return
             *     possible object is
             *     {@link CommonTransparentType }
             *     
             */
            public CommonTransparentType getTransparent() {
                return transparent;
            }

            /**
             * Sets the value of the transparent property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonTransparentType }
             *     
             */
            public void setTransparent(CommonTransparentType value) {
                this.transparent = value;
            }

            /**
             * Gets the value of the transparency property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getTransparency() {
                return transparency;
            }

            /**
             * Sets the value of the transparency property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setTransparency(CommonFloatOrParamType value) {
                this.transparency = value;
            }

            /**
             * Gets the value of the indexOfRefraction property.
             * 
             * @return
             *     possible object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public CommonFloatOrParamType getIndexOfRefraction() {
                return indexOfRefraction;
            }

            /**
             * Sets the value of the indexOfRefraction property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommonFloatOrParamType }
             *     
             */
            public void setIndexOfRefraction(CommonFloatOrParamType value) {
                this.indexOfRefraction = value;
            }

        }

    }

}
