
package cn.gtmap.estateplat.server.core.service.impl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


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
 *         &lt;element name="DoSynchronResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "doSynchronResult"
})
@XmlRootElement(name = "DoSynchronResponse")
public class DoSynchronResponse {

    @XmlElementRef(name = "DoSynchronResult", namespace = "http://tempuri.org/", type = JAXBElement.class)
    protected JAXBElement<String> doSynchronResult;

    /**
     * Gets the value of the doSynchronResult property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDoSynchronResult() {
        return doSynchronResult;
    }

    /**
     * Sets the value of the doSynchronResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDoSynchronResult(JAXBElement<String> value) {
        this.doSynchronResult = value;
    }

}
