
package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.server.model.ObjectFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "BdcDataSynServerService", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface BdcDataSynServerService {


    /**
     * 
     * @param jsonParam
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "DoSynchron", action = "http://tempuri.org/IBDCDataSynServer/DoSynchron")
    @WebResult(name = "DoSynchronResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "DoSynchron", targetNamespace = "http://tempuri.org/", className = "cn.gtmap.estateplat.server.core.service.impl.DoSynchron")
    @ResponseWrapper(localName = "DoSynchronResponse", targetNamespace = "http://tempuri.org/", className = "cn.gtmap.estateplat.server.core.service.impl.DoSynchronResponse")
    public String doSynchron(
            @WebParam(name = "jsonParam", targetNamespace = "http://tempuri.org/")
            String jsonParam);

}
