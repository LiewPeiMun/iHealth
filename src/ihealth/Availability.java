package ihealth;

public class Availability
{
    private String time;
    private int capacity;

    
    public Availability ()      // Default Constructor // 
    {
        time = null; 
        capacity = 0;
    }
     
    public Availability (String a, int b)           // Normal Constructor // 
    {
        time = a; 
        capacity = b;
    }
    
    public void setTime (String a)      // Mutator to set time // 
    {
        time = a;
    }
    
    public void setCapacity (int b)         // Mutator to set capacity // 
    {
        capacity = b; 
    }
    
    public String getTime ()        // Retriever to return time // 
    {
        return time; 
    }
    
    public int getCapacity ()        // Retriever to return capacity // 
    {
        return capacity; 
    }
    
    public String toString (int a)
    {
        String allignment = "| %-4d | %-20s | %-8s |%n";
        return String.format(allignment,a,time,capacity);
    }
}

