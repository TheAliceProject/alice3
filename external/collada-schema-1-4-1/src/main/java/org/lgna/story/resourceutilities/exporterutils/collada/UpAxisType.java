
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpAxisType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UpAxisType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="X_UP"/>
 *     &lt;enumeration value="Y_UP"/>
 *     &lt;enumeration value="Z_UP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UpAxisType", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum UpAxisType {

    X_UP,
    Y_UP,
    Z_UP;

    public String value() {
        return name();
    }

    public static UpAxisType fromValue(String v) {
        return valueOf(v);
    }

}
