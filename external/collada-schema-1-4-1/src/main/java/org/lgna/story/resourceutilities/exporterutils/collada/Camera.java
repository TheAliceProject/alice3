
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="optics">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="technique_common">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice>
 *                             &lt;element name="orthographic">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;choice>
 *                                         &lt;sequence>
 *                                           &lt;element name="xmag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                           &lt;choice minOccurs="0">
 *                                             &lt;element name="ymag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                             &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                           &lt;/choice>
 *                                         &lt;/sequence>
 *                                         &lt;sequence>
 *                                           &lt;element name="ymag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                           &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                                         &lt;/sequence>
 *                                       &lt;/choice>
 *                                       &lt;element name="znear" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                       &lt;element name="zfar" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="perspective">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;choice>
 *                                         &lt;sequence>
 *                                           &lt;element name="xfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                           &lt;choice minOccurs="0">
 *                                             &lt;element name="yfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                             &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                           &lt;/choice>
 *                                         &lt;/sequence>
 *                                         &lt;sequence>
 *                                           &lt;element name="yfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                           &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                                         &lt;/sequence>
 *                                       &lt;/choice>
 *                                       &lt;element name="znear" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                       &lt;element name="zfar" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/choice>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}technique" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="imager" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}technique" maxOccurs="unbounded"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
    "optics",
    "imager",
    "extra"
})
@XmlRootElement(name = "camera", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class Camera {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Asset asset;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected Camera.Optics optics;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Camera.Imager imager;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;

    /**
     * 
     * 							The camera element may contain an asset element.
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
     * Gets the value of the optics property.
     * 
     * @return
     *     possible object is
     *     {@link Camera.Optics }
     *     
     */
    public Camera.Optics getOptics() {
        return optics;
    }

    /**
     * Sets the value of the optics property.
     * 
     * @param value
     *     allowed object is
     *     {@link Camera.Optics }
     *     
     */
    public void setOptics(Camera.Optics value) {
        this.optics = value;
    }

    /**
     * Gets the value of the imager property.
     * 
     * @return
     *     possible object is
     *     {@link Camera.Imager }
     *     
     */
    public Camera.Imager getImager() {
        return imager;
    }

    /**
     * Sets the value of the imager property.
     * 
     * @param value
     *     allowed object is
     *     {@link Camera.Imager }
     *     
     */
    public void setImager(Camera.Imager value) {
        this.imager = value;
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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}technique" maxOccurs="unbounded"/>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
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
        "technique",
        "extra"
    })
    public static class Imager {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
        protected List<Technique> technique;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<Extra> extra;

        /**
         * 
         * 										This element may contain any number of non-common profile techniques.
         * 										There is no common technique for imager.
         * 									Gets the value of the technique property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the technique property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTechnique().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Technique }
         * 
         * 
         */
        public List<Technique> getTechnique() {
            if (technique == null) {
                technique = new ArrayList<Technique>();
            }
            return this.technique;
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
     *         &lt;element name="technique_common">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;element name="orthographic">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;choice>
     *                               &lt;sequence>
     *                                 &lt;element name="xmag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                                 &lt;choice minOccurs="0">
     *                                   &lt;element name="ymag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                                   &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                                 &lt;/choice>
     *                               &lt;/sequence>
     *                               &lt;sequence>
     *                                 &lt;element name="ymag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                                 &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                               &lt;/sequence>
     *                             &lt;/choice>
     *                             &lt;element name="znear" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                             &lt;element name="zfar" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="perspective">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;choice>
     *                               &lt;sequence>
     *                                 &lt;element name="xfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                                 &lt;choice minOccurs="0">
     *                                   &lt;element name="yfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                                   &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                                 &lt;/choice>
     *                               &lt;/sequence>
     *                               &lt;sequence>
     *                                 &lt;element name="yfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                                 &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                               &lt;/sequence>
     *                             &lt;/choice>
     *                             &lt;element name="znear" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                             &lt;element name="zfar" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}technique" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
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
        "techniqueCommon",
        "technique",
        "extra"
    })
    public static class Optics {

        @XmlElement(name = "technique_common", namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
        protected Camera.Optics.TechniqueCommon techniqueCommon;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<Technique> technique;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<Extra> extra;

        /**
         * Gets the value of the techniqueCommon property.
         * 
         * @return
         *     possible object is
         *     {@link Camera.Optics.TechniqueCommon }
         *     
         */
        public Camera.Optics.TechniqueCommon getTechniqueCommon() {
            return techniqueCommon;
        }

        /**
         * Sets the value of the techniqueCommon property.
         * 
         * @param value
         *     allowed object is
         *     {@link Camera.Optics.TechniqueCommon }
         *     
         */
        public void setTechniqueCommon(Camera.Optics.TechniqueCommon value) {
            this.techniqueCommon = value;
        }

        /**
         * 
         * 										This element may contain any number of non-common profile techniques.
         * 									Gets the value of the technique property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the technique property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTechnique().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Technique }
         * 
         * 
         */
        public List<Technique> getTechnique() {
            if (technique == null) {
                technique = new ArrayList<Technique>();
            }
            return this.technique;
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;choice>
         *         &lt;element name="orthographic">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;choice>
         *                     &lt;sequence>
         *                       &lt;element name="xmag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                       &lt;choice minOccurs="0">
         *                         &lt;element name="ymag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                         &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                       &lt;/choice>
         *                     &lt;/sequence>
         *                     &lt;sequence>
         *                       &lt;element name="ymag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                       &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *                     &lt;/sequence>
         *                   &lt;/choice>
         *                   &lt;element name="znear" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                   &lt;element name="zfar" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="perspective">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;choice>
         *                     &lt;sequence>
         *                       &lt;element name="xfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                       &lt;choice minOccurs="0">
         *                         &lt;element name="yfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                         &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                       &lt;/choice>
         *                     &lt;/sequence>
         *                     &lt;sequence>
         *                       &lt;element name="yfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                       &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *                     &lt;/sequence>
         *                   &lt;/choice>
         *                   &lt;element name="znear" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                   &lt;element name="zfar" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/choice>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "orthographic",
            "perspective"
        })
        public static class TechniqueCommon {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected Camera.Optics.TechniqueCommon.Orthographic orthographic;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected Camera.Optics.TechniqueCommon.Perspective perspective;

            /**
             * Gets the value of the orthographic property.
             * 
             * @return
             *     possible object is
             *     {@link Camera.Optics.TechniqueCommon.Orthographic }
             *     
             */
            public Camera.Optics.TechniqueCommon.Orthographic getOrthographic() {
                return orthographic;
            }

            /**
             * Sets the value of the orthographic property.
             * 
             * @param value
             *     allowed object is
             *     {@link Camera.Optics.TechniqueCommon.Orthographic }
             *     
             */
            public void setOrthographic(Camera.Optics.TechniqueCommon.Orthographic value) {
                this.orthographic = value;
            }

            /**
             * Gets the value of the perspective property.
             * 
             * @return
             *     possible object is
             *     {@link Camera.Optics.TechniqueCommon.Perspective }
             *     
             */
            public Camera.Optics.TechniqueCommon.Perspective getPerspective() {
                return perspective;
            }

            /**
             * Sets the value of the perspective property.
             * 
             * @param value
             *     allowed object is
             *     {@link Camera.Optics.TechniqueCommon.Perspective }
             *     
             */
            public void setPerspective(Camera.Optics.TechniqueCommon.Perspective value) {
                this.perspective = value;
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
             *         &lt;choice>
             *           &lt;sequence>
             *             &lt;element name="xmag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *             &lt;choice minOccurs="0">
             *               &lt;element name="ymag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *               &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *             &lt;/choice>
             *           &lt;/sequence>
             *           &lt;sequence>
             *             &lt;element name="ymag" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *             &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
             *           &lt;/sequence>
             *         &lt;/choice>
             *         &lt;element name="znear" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *         &lt;element name="zfar" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
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
                "content"
            })
            public static class Orthographic {

                @XmlElementRefs({
                    @XmlElementRef(name = "xmag", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false),
                    @XmlElementRef(name = "znear", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false),
                    @XmlElementRef(name = "zfar", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false),
                    @XmlElementRef(name = "aspect_ratio", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false),
                    @XmlElementRef(name = "ymag", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false)
                })
                protected List<JAXBElement<TargetableFloat>> content;

                /**
                 * Gets the rest of the content model. 
                 * 
                 * <p>
                 * You are getting this "catch-all" property because of the following reason: 
                 * The field name "Ymag" is used by two different parts of a schema. See: 
                 * line 2235 of file:/C:/Users/dculyba/IdeaProjects/Collada%20XSD%20code%20generation/src/collada_schema_1_4_1.xsd
                 * line 2215 of file:/C:/Users/dculyba/IdeaProjects/Collada%20XSD%20code%20generation/src/collada_schema_1_4_1.xsd
                 * <p>
                 * To get rid of this property, apply a property customization to one 
                 * of both of the following declarations to change their names: 
                 * Gets the value of the content property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the content property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getContent().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * 
                 * 
                 */
                public List<JAXBElement<TargetableFloat>> getContent() {
                    if (content == null) {
                        content = new ArrayList<JAXBElement<TargetableFloat>>();
                    }
                    return this.content;
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
             *         &lt;choice>
             *           &lt;sequence>
             *             &lt;element name="xfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *             &lt;choice minOccurs="0">
             *               &lt;element name="yfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *               &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *             &lt;/choice>
             *           &lt;/sequence>
             *           &lt;sequence>
             *             &lt;element name="yfov" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *             &lt;element name="aspect_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
             *           &lt;/sequence>
             *         &lt;/choice>
             *         &lt;element name="znear" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
             *         &lt;element name="zfar" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat"/>
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
                "content"
            })
            public static class Perspective {

                @XmlElementRefs({
                    @XmlElementRef(name = "yfov", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false),
                    @XmlElementRef(name = "aspect_ratio", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false),
                    @XmlElementRef(name = "znear", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false),
                    @XmlElementRef(name = "zfar", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false),
                    @XmlElementRef(name = "xfov", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false)
                })
                protected List<JAXBElement<TargetableFloat>> content;

                /**
                 * Gets the rest of the content model. 
                 * 
                 * <p>
                 * You are getting this "catch-all" property because of the following reason: 
                 * The field name "Yfov" is used by two different parts of a schema. See: 
                 * line 2295 of file:/C:/Users/dculyba/IdeaProjects/Collada%20XSD%20code%20generation/src/collada_schema_1_4_1.xsd
                 * line 2276 of file:/C:/Users/dculyba/IdeaProjects/Collada%20XSD%20code%20generation/src/collada_schema_1_4_1.xsd
                 * <p>
                 * To get rid of this property, apply a property customization to one 
                 * of both of the following declarations to change their names: 
                 * Gets the value of the content property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the content property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getContent().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}
                 * 
                 * 
                 */
                public List<JAXBElement<TargetableFloat>> getContent() {
                    if (content == null) {
                        content = new ArrayList<JAXBElement<TargetableFloat>>();
                    }
                    return this.content;
                }

            }

        }

    }

}
