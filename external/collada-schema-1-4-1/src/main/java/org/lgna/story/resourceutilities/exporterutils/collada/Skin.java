
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="bind_shape_matrix" type="{http://www.collada.org/2005/11/COLLADASchema}float4x4" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}source" maxOccurs="unbounded" minOccurs="3"/>
 *         &lt;element name="joints">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="input" type="{http://www.collada.org/2005/11/COLLADASchema}InputLocal" maxOccurs="unbounded" minOccurs="2"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="vertex_weights">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="input" type="{http://www.collada.org/2005/11/COLLADASchema}InputLocalOffset" maxOccurs="unbounded" minOccurs="2"/>
 *                   &lt;element name="vcount" type="{http://www.collada.org/2005/11/COLLADASchema}ListOfUInts" minOccurs="0"/>
 *                   &lt;element name="v" type="{http://www.collada.org/2005/11/COLLADASchema}ListOfInts" minOccurs="0"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="count" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}uint" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bindShapeMatrix",
    "sourceAttribute2",
    "joints",
    "vertexWeights",
    "extra"
})
@XmlRootElement(name = "skin", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class Skin {

    @XmlList
    @XmlElement(name = "bind_shape_matrix", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
    protected List<Double> bindShapeMatrix;
    @XmlElement(name = "source", namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected List<Source> sourceAttribute2;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected Skin.Joints joints;
    @XmlElement(name = "vertex_weights", namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected Skin.VertexWeights vertexWeights;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "source", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String sourceAttribute;

    /**
     * Gets the value of the bindShapeMatrix property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bindShapeMatrix property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBindShapeMatrix().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getBindShapeMatrix() {
        if (bindShapeMatrix == null) {
            bindShapeMatrix = new ArrayList<Double>();
        }
        return this.bindShapeMatrix;
    }

    /**
     * 
     * 							The skin element must contain at least three source elements.
     * 						Gets the value of the sourceAttribute2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourceAttribute2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceAttribute2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Source }
     * 
     * 
     */
    public List<Source> getSourceAttribute2() {
        if (sourceAttribute2 == null) {
            sourceAttribute2 = new ArrayList<Source>();
        }
        return this.sourceAttribute2;
    }

    /**
     * Gets the value of the joints property.
     * 
     * @return
     *     possible object is
     *     {@link Skin.Joints }
     *     
     */
    public Skin.Joints getJoints() {
        return joints;
    }

    /**
     * Sets the value of the joints property.
     * 
     * @param value
     *     allowed object is
     *     {@link Skin.Joints }
     *     
     */
    public void setJoints(Skin.Joints value) {
        this.joints = value;
    }

    /**
     * Gets the value of the vertexWeights property.
     * 
     * @return
     *     possible object is
     *     {@link Skin.VertexWeights }
     *     
     */
    public Skin.VertexWeights getVertexWeights() {
        return vertexWeights;
    }

    /**
     * Sets the value of the vertexWeights property.
     * 
     * @param value
     *     allowed object is
     *     {@link Skin.VertexWeights }
     *     
     */
    public void setVertexWeights(Skin.VertexWeights value) {
        this.vertexWeights = value;
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
     * Gets the value of the sourceAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceAttribute() {
        return sourceAttribute;
    }

    /**
     * Sets the value of the sourceAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceAttribute(String value) {
        this.sourceAttribute = value;
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
     *         &lt;element name="input" type="{http://www.collada.org/2005/11/COLLADASchema}InputLocal" maxOccurs="unbounded" minOccurs="2"/>
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
        "input",
        "extra"
    })
    public static class Joints {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
        protected List<InputLocal> input;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<Extra> extra;

        /**
         * Gets the value of the input property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the input property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInput().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link InputLocal }
         * 
         * 
         */
        public List<InputLocal> getInput() {
            if (input == null) {
                input = new ArrayList<InputLocal>();
            }
            return this.input;
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
     *         &lt;element name="input" type="{http://www.collada.org/2005/11/COLLADASchema}InputLocalOffset" maxOccurs="unbounded" minOccurs="2"/>
     *         &lt;element name="vcount" type="{http://www.collada.org/2005/11/COLLADASchema}ListOfUInts" minOccurs="0"/>
     *         &lt;element name="v" type="{http://www.collada.org/2005/11/COLLADASchema}ListOfInts" minOccurs="0"/>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="count" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}uint" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "input",
        "vcount",
        "v",
        "extra"
    })
    public static class VertexWeights {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
        protected List<InputLocalOffset> input;
        @XmlList
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<BigInteger> vcount;
        @XmlList
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Long.class)
        protected List<Long> v;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<Extra> extra;
        @XmlAttribute(name = "count", required = true)
        protected BigInteger count;

        /**
         * Gets the value of the input property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the input property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInput().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link InputLocalOffset }
         * 
         * 
         */
        public List<InputLocalOffset> getInput() {
            if (input == null) {
                input = new ArrayList<InputLocalOffset>();
            }
            return this.input;
        }

        /**
         * Gets the value of the vcount property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the vcount property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVcount().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BigInteger }
         * 
         * 
         */
        public List<BigInteger> getVcount() {
            if (vcount == null) {
                vcount = new ArrayList<BigInteger>();
            }
            return this.vcount;
        }

        /**
         * Gets the value of the v property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the v property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getV().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Long }
         * 
         * 
         */
        public List<Long> getV() {
            if (v == null) {
                v = new ArrayList<Long>();
            }
            return this.v;
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
         * Gets the value of the count property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getCount() {
            return count;
        }

        /**
         * Sets the value of the count property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setCount(BigInteger value) {
            this.count = value;
        }

    }

}
