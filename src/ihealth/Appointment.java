package ihealth;

public class Appointment
{
    private String IC;
    private int ID;
    private String date;
    private String time; 
    private String status;
    private String name;
    
    public Appointment ()    // Default Constructor //
    {
        ID = 0;
        date = null;
        time = null;
        status = null;
        IC = null;
        name = null;
    }
    
    public Appointment ( int a, String b, String c, String d, String e, String f )   // Normal Constructor // 
    {
        ID = a;
        date = b;
        time = c;
        status = d;
        IC = e;
        name = f;
    }
    
    public void setID ( int a )    // Mutator to set IC // 
    {
        ID = a;
    }
    
    public void setDate ( String b )    // Mutator to set date // 
    {
        date = b;
    }
    
    public void setTime ( String c )         // Mutator to set time // 
    {
        time = c;
    }
   
    public void setStatus ( String d )         // Mutator to set status // 
    {
        status = d;
    }
    
    public void setIC ( String e )         // Mutator to set IC // 
    {
        IC = e;
    }
    
    public void setName ( String f )         // Mutator to set name // 
    {
        name = f;
    }
    
    public int getID ()       // Retriever to return ID // 
    {
        return ID;
    }
    
    public String getDate ()       // Retriever to return date // 
    {
        return date; 
    }
   
    public String getTime ()        // Retriever to return time // 
    {
        return time;
    }    
    
    public String getStatus ()        // Retriever to return status // 
    {
        return status;
    }  
    
    public String getIC ()        // Retriever to return IC // 
    {
        return IC;
    } 
   
    public String getName ()        // Retriever to return name // 
    {
        return name;
    } 
    
    public String toString (int a)
    {
        String allignment = "| %-4d | %-24s | %-10s | %-19s | %-13s |%n";
        return String.format(allignment,a,name,date,time,status);
    }
}
