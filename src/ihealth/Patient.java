package ihealth;
public class Patient
{
    private String IC;
    private String name;
    private String gender; 
    private String phone;
    private String email;
    
    public Patient ()    // Default Constructor //
    {
        IC = null;
        name = null;
        gender = null;
        phone = null;
        email = null;
    }
    
    public Patient ( String a, String b, String c, String d, String e)   // Normal Constructor // 
    {
        IC = a;
        name = b;
        gender = c;
        phone = d;
        email = e;
    }
    
    public void setIC ( String a )    // Mutator to set IC // 
    {
        IC = a;
    }
    
    public void setName ( String b )    // Mutator to set name // 
    {
        name = b;
    }
    
    public void setGender ( String c )         // Mutator to set gender // 
    {
        gender = c;
    }
   
    public void setPhone ( String d )         // Mutator to set phone number // 
    {
        phone = d;
    }
    
    public void setEmail ( String e )         // Mutator to set email address // 
    {
        email = e;
    }
    
    public String getIC ()       // Retriever to return IC // 
    {
        return IC;
    }
    
    public String getName ()       // Retriever to return name // 
    {
        return name; 
    }
   
    public String getGender ()        // Retriever to return gender // 
    {
        return gender;
    }    
    
    public String getPhone ()        // Retriever to return phone number // 
    {
        return phone;
    }  
    
    public String getEmail ()        // Retriever to return email address // 
    {
        return email;
    }  
    
    public String toString ()
    {
        String allignment = "| %-24s | %-20s | %-6s | %-12s | %-22s |%n";
        return String.format(allignment, IC, name, gender, phone, email); 
    }
}

