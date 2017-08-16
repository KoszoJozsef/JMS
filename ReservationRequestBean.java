package obe.beans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import obe.model.ReservationRequest;
import ode.beans.RequestProducerBean;

@Named(value = "reserve")
@SessionScoped
public class ReservationRequestBean implements Serializable {

    private String firstName;
    private String lastName;
    private String checkIn;
    private String checkOut;
    private int numberOfRooms;
    private String region;
    
    @Inject
    private RequestProducerBean producer;

    public ReservationRequestBean() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    

    public void sendRequest(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ReservationRequest request = new ReservationRequest(firstName, lastName, checkIn, checkOut, numberOfRooms);
        try{
            producer.send(request, region);
            FacesMessage facesMessage = new FacesMessage("Request sent successfully!");
            facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
            facesContext.addMessage(null, facesMessage);
        } catch (JMSException e){
            FacesMessage facesMessage = new FacesMessage("Request NOT sent. Error: " + e);
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(null, facesMessage);
        }
    
    }
}
