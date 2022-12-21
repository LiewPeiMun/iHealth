package ihealth;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Email
{
    public static void sendEmail (String recepient, String type, Appointment app) throws MessagingException
    {
        Properties properties = new Properties();
        
        properties.put("mail.smtp.auth","true");                // Setup authentication 
        properties.put("mail.smtp.starttls.enable","true");     // Setup security
        properties.put("mail.smtp.host","smtp.gmail.com");      // Setup mail server (using Gmail's server)
        properties.put("mail.smtp.port","587");                 // Setup port
        
        /** CREDENTIALS **/
        String email = "clinicjava@gmail.com";                   
        String password = "java123_";
        
        /** CREATING SESSION **/
        Session session = Session.getInstance (properties, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(email,password);
            }        
        });
        
        Message message = prepareMessage (session, email, recepient, type, app);
        
        Transport.send(message);           // Send email
    }
 
    private static Message prepareMessage (Session a, String b, String c, String d, Appointment app)
    {
        try
        {
            String subject = null; 
            String text = null; 
            String addition = "";
            if(d.equals("make appointment"))
            {
                subject = "You have requested an appointment!";
                addition = "Thank you for using our system. Your request is currently being processed. \nPlease wait for further notification. \n";
            }
           
            else if(d.equals("status"))
            {
                if(app.getStatus().equals("Approved"))
                {
                    subject = "Your appointment has been approved!";
                    addition = "Congratulation! Your requested appointment has been approved. \nPlease come to the clinic according to the details below. \n";
                }    
                else
                {
                    subject = "Your appointment has been disapproved!";
                    addition = "Unfortunately, your appointment has been disapproved. \nPlease contact the clinic for more inquiry. \n";
                }    
            }
            
            else if(d.equals("time/date"))
            {
                subject = "Your appointments details have been changed!";
                addition = "Due to unforseenable reasons, your appointment has been changed. \nPlease check the details below. \n";
            }
            text = addition + "Details: \nDate: " + app.getDate() + "\nTime: " + app.getTime() + "\nStatus: " + app.getStatus();
            
            Message message = new MimeMessage (a);          // Create MimeMessage object
           
            message.setFrom(new InternetAddress(b));        // Set sender
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(c));     // Set receiver/recepient
            message.setSubject(subject);           // Set subject of the email
            message.setText(text);               // Set content of the email
            
            return message;   
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        
       
        return null;
    }
}
