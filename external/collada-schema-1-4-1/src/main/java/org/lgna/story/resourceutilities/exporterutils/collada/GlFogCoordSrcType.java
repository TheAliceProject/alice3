
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gl_fog_coord_src_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gl_fog_coord_src_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FOG_COORDINATE"/>
 *     &lt;enumeration value="FRAGMENT_DEPTH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gl_fog_coord_src_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlFogCoordSrcType {

    FOG_COORDINATE,
    FRAGMENT_DEPTH;

    public String value() {
        return name();
    }

    public static GlFogCoordSrcType fromValue(String v) {
        return valueOf(v);
    }

}
