
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}source" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}vertices"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}lines"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}linestrips"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}polygons"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}polylist"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}triangles"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}trifans"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}tristrips"/>
 *         &lt;/choice>
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
    "source",
    "vertices",
    "linesOrLinestripsOrPolygons",
    "extra"
})
@XmlRootElement(name = "mesh", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class Mesh {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected List<Source> source;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected Vertices vertices;
    @XmlElements({
        @XmlElement(name = "lines", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Lines.class),
        @XmlElement(name = "linestrips", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Linestrips.class),
        @XmlElement(name = "polygons", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Polygons.class),
        @XmlElement(name = "polylist", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Polylist.class),
        @XmlElement(name = "triangles", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Triangles.class),
        @XmlElement(name = "trifans", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Trifans.class),
        @XmlElement(name = "tristrips", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Tristrips.class)
    })
    protected List<Object> linesOrLinestripsOrPolygons;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;

    /**
     * 
     * 							The mesh element must contain one or more source elements.
     * 						Gets the value of the source property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the source property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Source }
     * 
     * 
     */
    public List<Source> getSource() {
        if (source == null) {
            source = new ArrayList<Source>();
        }
        return this.source;
    }

    /**
     * 
     * 							The mesh element must contain one vertices element.
     * 						
     * 
     * @return
     *     possible object is
     *     {@link Vertices }
     *     
     */
    public Vertices getVertices() {
        return vertices;
    }

    /**
     * Sets the value of the vertices property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vertices }
     *     
     */
    public void setVertices(Vertices value) {
        this.vertices = value;
    }

    /**
     * Gets the value of the linesOrLinestripsOrPolygons property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linesOrLinestripsOrPolygons property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinesOrLinestripsOrPolygons().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lines }
     * {@link Linestrips }
     * {@link Polygons }
     * {@link Polylist }
     * {@link Triangles }
     * {@link Trifans }
     * {@link Tristrips }
     * 
     * 
     */
    public List<Object> getLinesOrLinestripsOrPolygons() {
        if (linesOrLinestripsOrPolygons == null) {
            linesOrLinestripsOrPolygons = new ArrayList<Object>();
        }
        return this.linesOrLinestripsOrPolygons;
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

}
