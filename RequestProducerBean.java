/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ode.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import obe.model.ReservationRequest;

/**
 *
 * @author K
 */
@Named(value = "request")
@Dependent
public class RequestProducerBean implements Serializable {
    
    @Resource(mappedName = "jms/MyTopic")
    private Topic myTopic;
    @Resource(mappedName = "jms/myTopicFactory")
    private ConnectionFactory myTopicFactory;

    /**
     * Creates a new instance of RequestProducerBean
     */
    public RequestProducerBean() {
    }
    
    public void send(ReservationRequest request, String region) throws JMSException{
        Connection connection = null;
        Session session = null;
        try{
            connection = myTopicFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(myTopic);
            MapMessage mm = session.createMapMessage();
            mm.setString("FirstName", request.getFirstName());
            mm.setString("LastName", request.getLastName());
            mm.setString("Check-in", request.getCheckIn());
            mm.setString("Check-out", request.getCheckOut());
            mm.setInt("NumberOfRooms", request.getNumberOfRooms());
            mm.setStringProperty("Region", region);
            messageProducer.send(mm);
        } finally {
            if(session != null){
                try{
                    session.close();
                } catch (JMSException e){
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if(connection != null){
                connection.close();
            }
        }
    }
    
}
