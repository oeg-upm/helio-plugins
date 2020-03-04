package oeg.helio.valkyrie.valkyr.ie.connector;

import helio.framework.Connector;
import helio.framework.exceptions.NotReachableEndpointException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.upm.oeg.valkyr.ie.execution.Valkyr_IE;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pablo
 */
public class ValkyrConnector implements Connector {

    
    private String resourcesPath;
    private Valkyr_IE val;
    private String source;
    
    public ValkyrConnector(List <String> args){
    
        if(args.size() != 2){throw new IllegalArgumentException();}
    
        resourcesPath=args.get(0);
        source=args.get(1);
    }
    
    @Override
    public String retrieveData() throws NotReachableEndpointException {
    
        
        
        try {
            if(val==null) val = new Valkyr_IE(resourcesPath);
            
         } catch (Exception ex) {
            Logger.getLogger(ValkyrConnector.class.getName()).log(Level.SEVERE, null, ex);
            throw new NotReachableEndpointException(ValkyrConnector.class.toString(),"Valkyr could no intialize");
        }
        
        
        
        try {
            
           return  val.recognitionOfNamedEntities(source);
        } catch (Exception ex) {
            Logger.getLogger(ValkyrConnector.class.getName()).log(Level.SEVERE, null, ex);
            throw new NotReachableEndpointException(ValkyrConnector.class.toString(),"Valkyr could not execute document");
        }
        
        
      //  return null;
    }
    
}
