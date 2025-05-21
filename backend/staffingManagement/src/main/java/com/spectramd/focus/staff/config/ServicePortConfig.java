/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.staff.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author raghav.mittal
 */

@Component
@ConfigurationProperties(prefix="service-ports")
public class ServicePortConfig {
    
    private Map<String,Integer> ports;
    
    public Map<String,Integer> getPorts(){
        return ports;
    }
    
    public void setPorts(Map<String,Integer> ports){
        this.ports=ports;
    }
    
    
    public int getPortForService(String service){
        Integer port = ports.get(service);
        if(null==port){
            throw new IllegalArgumentException("Invalid Service: "+service);
        }
        
        return port;
    }

}
