/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

/**
 *
 * @author K
 */
@Named(value = "receiverBean")
@Dependent
public class ReceiverBean {
    
    @Inject
    private JMSContext context;
    @Resource(lookup = "jms/MyQueue")
    private Queue queue;

    /**
     * Creates a new instance of ReceiverBean
     */
    public ReceiverBean() {
    }
    
    public void getMessage(){
        try{
            JMSConsumer receiver = context.createConsumer(queue);
            String text = receiver.receiveBody(String.class, 1000);
            
            if(text != null){
                FacesMessage facesMessage = new FacesMessage("Reading message: " + text);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            } else {
                FacesMessage facesMessage = new FacesMessage("No message received after 1 second.");
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        } catch(JMSRuntimeException e){
            System.out.println(e.toString());
        }
    }
}
