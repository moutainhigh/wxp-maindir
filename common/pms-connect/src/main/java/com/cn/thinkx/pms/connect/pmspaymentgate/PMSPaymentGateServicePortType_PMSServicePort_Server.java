
package com.cn.thinkx.pms.connect.pmspaymentgate;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 2.5.2
 * 2016-02-25T15:48:21.754+08:00
 * Generated source version: 2.5.2
 * 
 */
 
public class PMSPaymentGateServicePortType_PMSServicePort_Server{

    protected PMSPaymentGateServicePortType_PMSServicePort_Server() throws java.lang.Exception {
        System.out.println("Starting Server");
        Object implementor = new PMSPaymentGateServicePortTypeImpl();
        String address = "http://";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws java.lang.Exception { 
        new PMSPaymentGateServicePortType_PMSServicePort_Server();
        System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);
    }
}