package org.openprovenance.prov.sql;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;

/**
 * <p>
 * Java class for Label complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Label">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/ns/prov#>TypedValue">
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Label")
@Entity(name = "Label")
@Table(name = "LABEL")
public class Label extends TypedValue implements
	org.openprovenance.prov.model.Label, Equals, HashCode,
	org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_LABEL_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL;
    private static final QualifiedName PROV_LABEL_QNAME = ProvFactory.getFactory().getName().PROV_LABEL;

    @Transient
    public QualifiedName getElementName() {
	return PROV_LABEL_QNAME;
    }

    @Transient
    public AttributeKind getKind() {
	return PROV_LABEL_KIND;
    }

    public String toNotationString() {
	return DOMProcessing.qualifiedNameToString(getElementName())
		+ " = "
		+ ProvUtilities.valueToNotationString(getValue(),
		                                      getType());
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
			  Object object, EqualsStrategy strategy) {
	if (!(object instanceof Label)) {
	    return false;
	}
	if (this == object) {
	    return true;
	}
	if (!super.equals(thisLocator, thatLocator, object, strategy)) {
	    return false;
	}
	return true;
    }

    public boolean equals(Object object) {
	final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
	return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
	int currentHashCode = super.hashCode(locator, strategy);
	return currentHashCode;
    }

    public int hashCode() {
	final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
	return this.hashCode(null, strategy);
    }

}
