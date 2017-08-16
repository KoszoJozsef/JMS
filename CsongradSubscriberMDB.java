/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obe.mdb;


import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author K
 */
@MessageDriven(mappedName="jms/Topic", activationConfig = {
    @ActivationConfigProperty(propertyName="messageSelector", propertyValue = "Region = 'Csongrad'")
    ,
    
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "CsongradSubscriberMDB")
    ,
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/MyTopic")
    ,
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable")
    ,
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "CsongradSubscriberMDB")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class CsongradSubscriberMDB implements MessageListener {
    
    public CsongradSubscriberMDB() {
    }
    
    @Override
    public void onMessage(Message message) {
        
        if (message instanceof MapMessage) {
            MapMessage mm = (MapMessage) message;
            String request = null;
            try {
                Enumeration<String> mapNames = mm.getMapNames();
                while (mapNames.hasMoreElements()) {
                    String name = mapNames.nextElement();
                    request += name + ": " + mm.getString(name) + " | ";
                }
                System.out.println(this.getClass().getName() + ": Received a request for " + message.getStringProperty("Region"));
                System.out.println(request);
            } catch (JMSException ex) {
                System.out.println("Failed to get request message");
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
