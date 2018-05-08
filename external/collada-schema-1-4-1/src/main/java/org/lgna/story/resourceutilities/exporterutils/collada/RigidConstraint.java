
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
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
 *         &lt;element name="ref_attachment">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/choice>
 *                 &lt;attribute name="rigid_body" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="attachment">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/choice>
 *                 &lt;attribute name="rigid_body" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="technique_common">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="enabled" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
 *                           &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="interpenetrate" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
 *                           &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="limits" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="swing_cone_and_twist" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="min" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
 *                                       &lt;element name="max" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="linear" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="min" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
 *                                       &lt;element name="max" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="spring" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="angular" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="stiffness" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                                       &lt;element name="damping" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                                       &lt;element name="target_value" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="linear" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="stiffness" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                                       &lt;element name="damping" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                                       &lt;element name="target_value" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}technique" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="sid" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
    "refAttachment",
    "attachment",
    "techniqueCommon",
    "technique",
    "extra"
})
@XmlRootElement(name = "rigid_constraint", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class RigidConstraint {

    @XmlElement(name = "ref_attachment", namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected RigidConstraint.RefAttachment refAttachment;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected RigidConstraint.Attachment attachment;
    @XmlElement(name = "technique_common", namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected RigidConstraint.TechniqueCommon techniqueCommon;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Technique> technique;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "sid", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sid;
    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;

    /**
     * Gets the value of the refAttachment property.
     * 
     * @return
     *     possible object is
     *     {@link RigidConstraint.RefAttachment }
     *     
     */
    public RigidConstraint.RefAttachment getRefAttachment() {
        return refAttachment;
    }

    /**
     * Sets the value of the refAttachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link RigidConstraint.RefAttachment }
     *     
     */
    public void setRefAttachment(RigidConstraint.RefAttachment value) {
        this.refAttachment = value;
    }

    /**
     * Gets the value of the attachment property.
     * 
     * @return
     *     possible object is
     *     {@link RigidConstraint.Attachment }
     *     
     */
    public RigidConstraint.Attachment getAttachment() {
        return attachment;
    }

    /**
     * Sets the value of the attachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link RigidConstraint.Attachment }
     *     
     */
    public void setAttachment(RigidConstraint.Attachment value) {
        this.attachment = value;
    }

    /**
     * Gets the value of the techniqueCommon property.
     * 
     * @return
     *     possible object is
     *     {@link RigidConstraint.TechniqueCommon }
     *     
     */
    public RigidConstraint.TechniqueCommon getTechniqueCommon() {
        return techniqueCommon;
    }

    /**
     * Sets the value of the techniqueCommon property.
     * 
     * @param value
     *     allowed object is
     *     {@link RigidConstraint.TechniqueCommon }
     *     
     */
    public void setTechniqueCommon(RigidConstraint.TechniqueCommon value) {
        this.techniqueCommon = value;
    }

    /**
     * 
     * 							This element may contain any number of non-common profile techniques.
     * 						Gets the value of the technique property.
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
     *       &lt;choice maxOccurs="unbounded" minOccurs="0">
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/choice>
     *       &lt;attribute name="rigid_body" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "translateOrRotateOrExtra"
    })
    public static class Attachment {

        @XmlElements({
            @XmlElement(name = "translate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = TargetableFloat3 .class),
            @XmlElement(name = "rotate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Rotate.class),
            @XmlElement(name = "extra", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Extra.class)
        })
        protected List<Object> translateOrRotateOrExtra;
        @XmlAttribute(name = "rigid_body")
        @XmlSchemaType(name = "anyURI")
        protected String rigidBody;

        /**
         * Gets the value of the translateOrRotateOrExtra property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the translateOrRotateOrExtra property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTranslateOrRotateOrExtra().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TargetableFloat3 }
         * {@link Rotate }
         * {@link Extra }
         * 
         * 
         */
        public List<Object> getTranslateOrRotateOrExtra() {
            if (translateOrRotateOrExtra == null) {
                translateOrRotateOrExtra = new ArrayList<Object>();
            }
            return this.translateOrRotateOrExtra;
        }

        /**
         * Gets the value of the rigidBody property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRigidBody() {
            return rigidBody;
        }

        /**
         * Sets the value of the rigidBody property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRigidBody(String value) {
            this.rigidBody = value;
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
     *       &lt;choice maxOccurs="unbounded" minOccurs="0">
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/choice>
     *       &lt;attribute name="rigid_body" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "translateOrRotateOrExtra"
    })
    public static class RefAttachment {

        @XmlElements({
            @XmlElement(name = "translate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = TargetableFloat3 .class),
            @XmlElement(name = "rotate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Rotate.class),
            @XmlElement(name = "extra", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Extra.class)
        })
        protected List<Object> translateOrRotateOrExtra;
        @XmlAttribute(name = "rigid_body")
        @XmlSchemaType(name = "anyURI")
        protected String rigidBody;

        /**
         * Gets the value of the translateOrRotateOrExtra property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the translateOrRotateOrExtra property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTranslateOrRotateOrExtra().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TargetableFloat3 }
         * {@link Rotate }
         * {@link Extra }
         * 
         * 
         */
        public List<Object> getTranslateOrRotateOrExtra() {
            if (translateOrRotateOrExtra == null) {
                translateOrRotateOrExtra = new ArrayList<Object>();
            }
            return this.translateOrRotateOrExtra;
        }

        /**
         * Gets the value of the rigidBody property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRigidBody() {
            return rigidBody;
        }

        /**
         * Sets the value of the rigidBody property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRigidBody(String value) {
            this.rigidBody = value;
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
     *         &lt;element name="enabled" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
     *                 &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="interpenetrate" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
     *                 &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="limits" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="swing_cone_and_twist" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="min" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
     *                             &lt;element name="max" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="linear" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="min" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
     *                             &lt;element name="max" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="spring" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="angular" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="stiffness" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                             &lt;element name="damping" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                             &lt;element name="target_value" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="linear" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="stiffness" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                             &lt;element name="damping" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                             &lt;element name="target_value" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "enabled",
        "interpenetrate",
        "limits",
        "spring"
    })
    public static class TechniqueCommon {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "true")
        protected RigidConstraint.TechniqueCommon.Enabled enabled;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "false")
        protected RigidConstraint.TechniqueCommon.Interpenetrate interpenetrate;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected RigidConstraint.TechniqueCommon.Limits limits;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected RigidConstraint.TechniqueCommon.Spring spring;

        /**
         * Gets the value of the enabled property.
         * 
         * @return
         *     possible object is
         *     {@link RigidConstraint.TechniqueCommon.Enabled }
         *     
         */
        public RigidConstraint.TechniqueCommon.Enabled getEnabled() {
            return enabled;
        }

        /**
         * Sets the value of the enabled property.
         * 
         * @param value
         *     allowed object is
         *     {@link RigidConstraint.TechniqueCommon.Enabled }
         *     
         */
        public void setEnabled(RigidConstraint.TechniqueCommon.Enabled value) {
            this.enabled = value;
        }

        /**
         * Gets the value of the interpenetrate property.
         * 
         * @return
         *     possible object is
         *     {@link RigidConstraint.TechniqueCommon.Interpenetrate }
         *     
         */
        public RigidConstraint.TechniqueCommon.Interpenetrate getInterpenetrate() {
            return interpenetrate;
        }

        /**
         * Sets the value of the interpenetrate property.
         * 
         * @param value
         *     allowed object is
         *     {@link RigidConstraint.TechniqueCommon.Interpenetrate }
         *     
         */
        public void setInterpenetrate(RigidConstraint.TechniqueCommon.Interpenetrate value) {
            this.interpenetrate = value;
        }

        /**
         * Gets the value of the limits property.
         * 
         * @return
         *     possible object is
         *     {@link RigidConstraint.TechniqueCommon.Limits }
         *     
         */
        public RigidConstraint.TechniqueCommon.Limits getLimits() {
            return limits;
        }

        /**
         * Sets the value of the limits property.
         * 
         * @param value
         *     allowed object is
         *     {@link RigidConstraint.TechniqueCommon.Limits }
         *     
         */
        public void setLimits(RigidConstraint.TechniqueCommon.Limits value) {
            this.limits = value;
        }

        /**
         * Gets the value of the spring property.
         * 
         * @return
         *     possible object is
         *     {@link RigidConstraint.TechniqueCommon.Spring }
         *     
         */
        public RigidConstraint.TechniqueCommon.Spring getSpring() {
            return spring;
        }

        /**
         * Sets the value of the spring property.
         * 
         * @param value
         *     allowed object is
         *     {@link RigidConstraint.TechniqueCommon.Spring }
         *     
         */
        public void setSpring(RigidConstraint.TechniqueCommon.Spring value) {
            this.spring = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
         *       &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class Enabled {

            @XmlValue
            protected boolean value;
            @XmlAttribute(name = "sid")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NCName")
            protected String sid;

            /**
             * Gets the value of the value property.
             * 
             */
            public boolean isValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             */
            public void setValue(boolean value) {
                this.value = value;
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


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
         *       &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class Interpenetrate {

            @XmlValue
            protected boolean value;
            @XmlAttribute(name = "sid")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NCName")
            protected String sid;

            /**
             * Gets the value of the value property.
             * 
             */
            public boolean isValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             */
            public void setValue(boolean value) {
                this.value = value;
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
         *         &lt;element name="swing_cone_and_twist" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="min" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
         *                   &lt;element name="max" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="linear" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="min" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
         *                   &lt;element name="max" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "swingConeAndTwist",
            "linear"
        })
        public static class Limits {

            @XmlElement(name = "swing_cone_and_twist", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected RigidConstraint.TechniqueCommon.Limits.SwingConeAndTwist swingConeAndTwist;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected RigidConstraint.TechniqueCommon.Limits.Linear linear;

            /**
             * Gets the value of the swingConeAndTwist property.
             * 
             * @return
             *     possible object is
             *     {@link RigidConstraint.TechniqueCommon.Limits.SwingConeAndTwist }
             *     
             */
            public RigidConstraint.TechniqueCommon.Limits.SwingConeAndTwist getSwingConeAndTwist() {
                return swingConeAndTwist;
            }

            /**
             * Sets the value of the swingConeAndTwist property.
             * 
             * @param value
             *     allowed object is
             *     {@link RigidConstraint.TechniqueCommon.Limits.SwingConeAndTwist }
             *     
             */
            public void setSwingConeAndTwist(RigidConstraint.TechniqueCommon.Limits.SwingConeAndTwist value) {
                this.swingConeAndTwist = value;
            }

            /**
             * Gets the value of the linear property.
             * 
             * @return
             *     possible object is
             *     {@link RigidConstraint.TechniqueCommon.Limits.Linear }
             *     
             */
            public RigidConstraint.TechniqueCommon.Limits.Linear getLinear() {
                return linear;
            }

            /**
             * Sets the value of the linear property.
             * 
             * @param value
             *     allowed object is
             *     {@link RigidConstraint.TechniqueCommon.Limits.Linear }
             *     
             */
            public void setLinear(RigidConstraint.TechniqueCommon.Limits.Linear value) {
                this.linear = value;
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
             *         &lt;element name="min" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
             *         &lt;element name="max" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
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
                "min",
                "max"
            })
            public static class Linear {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0 0.0 0.0")
                protected TargetableFloat3 min;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0 0.0 0.0")
                protected TargetableFloat3 max;

                /**
                 * Gets the value of the min property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat3 }
                 *     
                 */
                public TargetableFloat3 getMin() {
                    return min;
                }

                /**
                 * Sets the value of the min property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat3 }
                 *     
                 */
                public void setMin(TargetableFloat3 value) {
                    this.min = value;
                }

                /**
                 * Gets the value of the max property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat3 }
                 *     
                 */
                public TargetableFloat3 getMax() {
                    return max;
                }

                /**
                 * Sets the value of the max property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat3 }
                 *     
                 */
                public void setMax(TargetableFloat3 value) {
                    this.max = value;
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
             *         &lt;element name="min" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
             *         &lt;element name="max" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
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
                "min",
                "max"
            })
            public static class SwingConeAndTwist {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0 0.0 0.0")
                protected TargetableFloat3 min;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0 0.0 0.0")
                protected TargetableFloat3 max;

                /**
                 * Gets the value of the min property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat3 }
                 *     
                 */
                public TargetableFloat3 getMin() {
                    return min;
                }

                /**
                 * Sets the value of the min property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat3 }
                 *     
                 */
                public void setMin(TargetableFloat3 value) {
                    this.min = value;
                }

                /**
                 * Gets the value of the max property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat3 }
                 *     
                 */
                public TargetableFloat3 getMax() {
                    return max;
                }

                /**
                 * Sets the value of the max property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat3 }
                 *     
                 */
                public void setMax(TargetableFloat3 value) {
                    this.max = value;
                }

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
         *         &lt;element name="angular" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="stiffness" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *                   &lt;element name="damping" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *                   &lt;element name="target_value" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="linear" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="stiffness" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *                   &lt;element name="damping" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *                   &lt;element name="target_value" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "angular",
            "linear"
        })
        public static class Spring {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected RigidConstraint.TechniqueCommon.Spring.Angular angular;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected RigidConstraint.TechniqueCommon.Spring.Linear linear;

            /**
             * Gets the value of the angular property.
             * 
             * @return
             *     possible object is
             *     {@link RigidConstraint.TechniqueCommon.Spring.Angular }
             *     
             */
            public RigidConstraint.TechniqueCommon.Spring.Angular getAngular() {
                return angular;
            }

            /**
             * Sets the value of the angular property.
             * 
             * @param value
             *     allowed object is
             *     {@link RigidConstraint.TechniqueCommon.Spring.Angular }
             *     
             */
            public void setAngular(RigidConstraint.TechniqueCommon.Spring.Angular value) {
                this.angular = value;
            }

            /**
             * Gets the value of the linear property.
             * 
             * @return
             *     possible object is
             *     {@link RigidConstraint.TechniqueCommon.Spring.Linear }
             *     
             */
            public RigidConstraint.TechniqueCommon.Spring.Linear getLinear() {
                return linear;
            }

            /**
             * Sets the value of the linear property.
             * 
             * @param value
             *     allowed object is
             *     {@link RigidConstraint.TechniqueCommon.Spring.Linear }
             *     
             */
            public void setLinear(RigidConstraint.TechniqueCommon.Spring.Linear value) {
                this.linear = value;
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
             *         &lt;element name="stiffness" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
             *         &lt;element name="damping" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
             *         &lt;element name="target_value" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
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
                "stiffness",
                "damping",
                "targetValue"
            })
            public static class Angular {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "1.0")
                protected TargetableFloat stiffness;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
                protected TargetableFloat damping;
                @XmlElement(name = "target_value", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
                protected TargetableFloat targetValue;

                /**
                 * Gets the value of the stiffness property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public TargetableFloat getStiffness() {
                    return stiffness;
                }

                /**
                 * Sets the value of the stiffness property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public void setStiffness(TargetableFloat value) {
                    this.stiffness = value;
                }

                /**
                 * Gets the value of the damping property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public TargetableFloat getDamping() {
                    return damping;
                }

                /**
                 * Sets the value of the damping property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public void setDamping(TargetableFloat value) {
                    this.damping = value;
                }

                /**
                 * Gets the value of the targetValue property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public TargetableFloat getTargetValue() {
                    return targetValue;
                }

                /**
                 * Sets the value of the targetValue property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public void setTargetValue(TargetableFloat value) {
                    this.targetValue = value;
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
             *         &lt;element name="stiffness" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
             *         &lt;element name="damping" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
             *         &lt;element name="target_value" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
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
                "stiffness",
                "damping",
                "targetValue"
            })
            public static class Linear {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "1.0")
                protected TargetableFloat stiffness;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
                protected TargetableFloat damping;
                @XmlElement(name = "target_value", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
                protected TargetableFloat targetValue;

                /**
                 * Gets the value of the stiffness property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public TargetableFloat getStiffness() {
                    return stiffness;
                }

                /**
                 * Sets the value of the stiffness property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public void setStiffness(TargetableFloat value) {
                    this.stiffness = value;
                }

                /**
                 * Gets the value of the damping property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public TargetableFloat getDamping() {
                    return damping;
                }

                /**
                 * Sets the value of the damping property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public void setDamping(TargetableFloat value) {
                    this.damping = value;
                }

                /**
                 * Gets the value of the targetValue property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public TargetableFloat getTargetValue() {
                    return targetValue;
                }

                /**
                 * Sets the value of the targetValue property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TargetableFloat }
                 *     
                 */
                public void setTargetValue(TargetableFloat value) {
                    this.targetValue = value;
                }

            }

        }

    }

}
