
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
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}lookat"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}matrix"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}scale"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}skew"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_camera" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_controller" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_geometry" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_light" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_node" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}node" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="type" type="{http://www.collada.org/2005/11/COLLADASchema}NodeType" default="NODE" />
 *       &lt;attribute name="layer" type="{http://www.collada.org/2005/11/COLLADASchema}ListOfNames" />
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
    "lookatOrMatrixOrRotate",
    "instanceCamera",
    "instanceController",
    "instanceGeometry",
    "instanceLight",
    "instanceNode",
    "node",
    "extra"
})
@XmlRootElement(name = "node", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class Node {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Asset asset;
    @XmlElementRefs({
        @XmlElementRef(name = "skew", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Skew.class, required = false),
        @XmlElementRef(name = "rotate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Rotate.class, required = false),
        @XmlElementRef(name = "lookat", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Lookat.class, required = false),
        @XmlElementRef(name = "matrix", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Matrix.class, required = false),
        @XmlElementRef(name = "scale", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "translate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = JAXBElement.class, required = false)
    })
    protected List<Object> lookatOrMatrixOrRotate;
    @XmlElement(name = "instance_camera", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<InstanceWithExtra> instanceCamera;
    @XmlElement(name = "instance_controller", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<InstanceController> instanceController;
    @XmlElement(name = "instance_geometry", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<InstanceGeometry> instanceGeometry;
    @XmlElement(name = "instance_light", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<InstanceWithExtra> instanceLight;
    @XmlElement(name = "instance_node", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<InstanceWithExtra> instanceNode;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Node> node;
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
    @XmlAttribute(name = "sid")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sid;
    @XmlAttribute(name = "type")
    protected NodeType type;
    @XmlAttribute(name = "layer")
    protected List<String> layer;

    /**
     * 
     * 							The node element may contain an asset element.
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
     * Gets the value of the lookatOrMatrixOrRotate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lookatOrMatrixOrRotate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLookatOrMatrixOrRotate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lookat }
     * {@link Matrix }
     * {@link JAXBElement }{@code <}{@link TargetableFloat3 }{@code >}
     * {@link JAXBElement }{@code <}{@link TargetableFloat3 }{@code >}
     * {@link Skew }
     * {@link Rotate }
     * 
     * 
     */
    public List<Object> getLookatOrMatrixOrRotate() {
        if (lookatOrMatrixOrRotate == null) {
            lookatOrMatrixOrRotate = new ArrayList<Object>();
        }
        return this.lookatOrMatrixOrRotate;
    }

    /**
     * 
     * 							The node element may instance any number of camera objects.
     * 						Gets the value of the instanceCamera property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instanceCamera property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstanceCamera().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstanceWithExtra }
     * 
     * 
     */
    public List<InstanceWithExtra> getInstanceCamera() {
        if (instanceCamera == null) {
            instanceCamera = new ArrayList<InstanceWithExtra>();
        }
        return this.instanceCamera;
    }

    /**
     * 
     * 							The node element may instance any number of controller objects.
     * 						Gets the value of the instanceController property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instanceController property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstanceController().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstanceController }
     * 
     * 
     */
    public List<InstanceController> getInstanceController() {
        if (instanceController == null) {
            instanceController = new ArrayList<InstanceController>();
        }
        return this.instanceController;
    }

    /**
     * 
     * 							The node element may instance any number of geometry objects.
     * 						Gets the value of the instanceGeometry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instanceGeometry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstanceGeometry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstanceGeometry }
     * 
     * 
     */
    public List<InstanceGeometry> getInstanceGeometry() {
        if (instanceGeometry == null) {
            instanceGeometry = new ArrayList<InstanceGeometry>();
        }
        return this.instanceGeometry;
    }

    /**
     * 
     * 							The node element may instance any number of light objects.
     * 						Gets the value of the instanceLight property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instanceLight property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstanceLight().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstanceWithExtra }
     * 
     * 
     */
    public List<InstanceWithExtra> getInstanceLight() {
        if (instanceLight == null) {
            instanceLight = new ArrayList<InstanceWithExtra>();
        }
        return this.instanceLight;
    }

    /**
     * 
     * 							The node element may instance any number of node elements or hierarchies objects.
     * 						Gets the value of the instanceNode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instanceNode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstanceNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstanceWithExtra }
     * 
     * 
     */
    public List<InstanceWithExtra> getInstanceNode() {
        if (instanceNode == null) {
            instanceNode = new ArrayList<InstanceWithExtra>();
        }
        return this.instanceNode;
    }

    /**
     * 
     * 							The node element may be hierarchical and be the parent of any number of other node elements.
     * 						Gets the value of the node property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the node property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Node }
     * 
     * 
     */
    public List<Node> getNode() {
        if (node == null) {
            node = new ArrayList<Node>();
        }
        return this.node;
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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link NodeType }
     *     
     */
    public NodeType getType() {
        if (type == null) {
            return NodeType.NODE;
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link NodeType }
     *     
     */
    public void setType(NodeType value) {
        this.type = value;
    }

    /**
     * Gets the value of the layer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the layer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLayer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLayer() {
        if (layer == null) {
            layer = new ArrayList<String>();
        }
        return this.layer;
    }

}
