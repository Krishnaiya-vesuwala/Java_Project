/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.ejb.Stateless;

/**
 *
 * @author riya vesuwala
 */
@Stateless
public class NotificationBean implements NotificationBeanLocal {

    private static final String ACCOUNT_SID = System.getProperty("TWILIO_ACCOUNT_SID");

    private static final String AUTH_TOKEN = System.getProperty("TWILIO_AUTH_TOKEN");
    
    @Override
    public void sendSMS(String mobile, String message) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
       
        try{
//            System.out.println("SID = " + ACCOUNT_SID);
//            System.out.println("TOKEN = " + AUTH_TOKEN);
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            
            Message MsgObj=Message.creator(
                new PhoneNumber("whatsapp:+91" + mobile),
                new PhoneNumber("whatsapp:+14155238886"),
                message
            ).create();   
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
