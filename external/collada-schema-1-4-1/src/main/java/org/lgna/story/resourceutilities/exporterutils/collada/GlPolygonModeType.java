
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gl_polygon_mode_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gl_polygon_mode_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="POINT"/>
 *     &lt;enumeration value="LINE"/>
 *     &lt;enumeration value="FILL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gl_polygon_mode_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlPolygonModeType {

    POINT,
    LINE,
    FILL;

    public String value() {
        return name();
    }

    public static GlPolygonModeType fromValue(String v) {
        return valueOf(v);
    }

}
