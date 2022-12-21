package ihealth;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;

public class iHealth_Main
{
    public static void main (String [] args)
    {
        try
        {
            Scanner in = new Scanner (System.in);
            
            Connection conn = DBConnect.getConnection();            // Establish database connection
            Statement st = conn.createStatement();                  // Create the java statement
            String query = null;                
            ResultSet rs;
            
            String user = null;
            int found = 0,repeat = 1, staff_ID, app_ID, HA_ID = 0, recordNum;
            String password, choice = "0", IC, fullName = null, phone, gender, email = "", app_date = null, time_interval = null, app_status = null;
            String opening = "", closing = "";
            char flag = 'y';
            int count = 0, capacity, skip = 0;
            
            /*** CREATE AVAILABLE TIME FOR APPOINTMENT ***/
            query = "SELECT opening, closing FROM businesshour";
            rs = st.executeQuery(query); 
            while (rs.next())
            {
                opening = rs.getString(1);
                closing = rs.getString(2);
            }
            
            int size = createArraySize(opening,closing);        // Initializing the variable size using method 
            String[] interval = new String[size];
            ArrayList <Availability> listA = new ArrayList<Availability>();     // Create arraylist of Availability 
            createInterval(opening,closing,interval);
            
            for (int x = 0; x < interval.length; x++)
            {
                Availability newA = new Availability (interval[x], 0);
                listA.add(newA);
            }
           
            
            /** DETERMINE TYPE OF USER **/
            System.out.println("~~~~~   iHealth Clinic v0.0.1 ~~~~~~~~");
            System.out.println("1. Staff");
            System.out.println("2. Patient\n");
            System.out.print("Choose type of user: ");
            user = in.next();
            
            while (!user.equals("1") && !user.equals("2"))
            {
                System.out.println("Wrong input.");
                System.out.print("Choose type of user: ");
                user = in.next();
            }
            
            
            
            /** STAFF SECTION **/
            if (user.equals("1"))
            {
                /** CHECK THE CREDENTIALS OF ADMIN/STAFF. IT WILL LOOP UNTIL THE ID AND PASSWORD ARE CORRECT **/
                while (found == 0)
                {
                    System.out.print("Staff ID: ");
                    staff_ID = in.nextInt();
                    
                    System.out.print("Password: ");
                    password = in.next();
                    
                    /** CHECK WHETHER THERE EXIST A RECORD OF INSERTED STAFF ID AND PASSWORD **/
                    query = "SELECT COUNT(*) FROM staff WHERE staff_ID = '"+staff_ID+"' AND staff_password = '"+password+"'";
                    rs = st.executeQuery(query);
                    
                    while (rs.next())
                    {
                        found = rs.getInt(1);
                    }
                    
                    /** IF THERE IS NO RECORD FOUND, IT WILL LOOP AGAIN **/
                    if (found == 0)
                    {
                        System.out.println("Staff ID or password is not correct. Please try again. \n");
                    }
                }
                
                /** LOOP THIS WHILE USER DOESNT DECIDE TO END THE PROGRAM  **/
                while (!choice.equals("2"))
                {
                    ArrayList<Appointment> listAppointment = new ArrayList<Appointment>();
                    ArrayList<Patient> listPatients = new ArrayList<Patient>();
                    /** Initializing both list using method **/
                    initiateList(listPatients,listAppointment);
                    
                   
                    System.out.println("");
                    if (repeat == 1)
                    {
                        staffMenu();
                        choice = in.next();
                        repeat = 0;
                    }
                    
                     
                    while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3"))
                    {
                        System.out.println("Wrong input. Please try again.");
                        System.out.print("Please enter your choice: ");
                        choice = in.next();
                    }
                    
                    /***    DISPLAY LIST OF APPOINTMENTS ***/
                    if(choice.equals("1"))
                    {
                        /** DISPLAY OPTIONS FOR USER TO CHOOSE **/
                        displayListAppointments(listAppointment);
                        System.out.println("\nWhat do you wish to do?");
                        System.out.println("1 - Edit record");
                        System.out.println("2 - Delete record");
                        System.out.println("3 - Return to menu");
                        System.out.print("\nEnter your choice: ");
                        choice = in.next();
                        
                        /** ERROR CHECKING TO MAKE SURE THAT USER ENTER THE RIGHT INPUT **/
                        while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3"))
                        {
                            System.out.println("Wrong input. Please try again.");
                            System.out.print("Enter your choice: ");
                            choice = in.next();
                        }
                        
                        /** IF USER CHOOSES TO EDIT APPOINTMENT **/
                        if(choice.equals("1"))
                        {
                            flag = 'y';     // Reset the value of flag so that the loop will remain true 
                            
                            
                            
                            System.out.println("Which record you would like to choose?");
                            System.out.print("Enter record no. : ");
                            recordNum = in.nextInt();
                                
                            /** LOOP TO MAKE SURE THE USER DOESN'T INSERT A NUMBER OUT OF THE LENGTH OF THE ARRAYLIST **/
                            while (recordNum > listAppointment.size() || recordNum < 0)
                            {
                                if(recordNum > listAppointment.size())
                                {
                                    System.out.println("Input is out of bound. Please try again.");
                                    System.out.print("Enter your choice: ");
                                    recordNum = in.nextInt();
                                }
                                    
                                else 
                                {
                                    System.out.println("Input is less than 1. Please try again.");
                                    System.out.print("Enter your choice: ");
                                    recordNum = in.nextInt();
                                }
                            }
                            
                                recordNum = recordNum - 1;
                                /** EDIT APPOINTMENT **/
                            while (flag == 'y' || flag == 'Y')
                            {        
                                /** DISPLAY THE ACTIONS THE USER CAN PERFORM **/
                                System.out.println("Which field you would like to edit?");
                                System.out.format("+-----+---------+%n");
                                System.out.format("| No. |  Field  |%n");
                                System.out.format("+-----+---------+%n");        
                                System.out.format("|  1  |   Date  |%n");
                                System.out.format("|  2  |   Time  |%n");
                                System.out.format("|  3  |  Status |%n");
                                System.out.format("+-----+---------+%n");
                                System.out.print("Enter field no. : ");
                                choice = in.next();
                            
                                /** LOOP TO MAKE SURE THE USER DOESN'T ENTER THE WRONG INPUT **/
                                while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3"))
                                {
                                    System.out.println("Wrong input. Please try again.");
                                    System.out.print("Enter field no. : ");
                                    choice = in.next();
                                }
                            
                            
                            
                                app_ID = listAppointment.get(recordNum).getID();        // Obtaining the appointment ID fromm the arrayList
                                
                                /** IF USER DECIDE TO EDIT DATE OR TIME OF THE APPOINTMENT **/
                                if (choice.equals("1") || choice.equals("2") )
                                {
                                    /** IF THE USER WANTS TO EDIT THE DATE OF APPOINTMENT **/
                                    if (choice.equals("1"))
                                    {
                                        System.out.print("Please enter a new date of appointment (DD/MM/YYYY): ");
                                        app_date = in.next();
                                        
                                        /**  UPDATE THE DATE OF THE APPOINTMENT INSIDE OF THE DATABASE **/
                                        query = "UPDATE appointment SET app_Date = '"+app_date+"' WHERE app_ID ='"+app_ID+"'";
                                        st.executeUpdate(query);
                                        
                                        /** SET VALUE OF THE DATE OF APPOINTMENT INSIDE THE ARRAYLIST **/
                                        listAppointment.get(recordNum).setDate(app_date);   
                                    }
                                    
                                    
                                    /** IF USER WANTS TO CHANGE THE TIME, NOT THE DATE **/
                                    if(choice.equals("2"))
                                    {
                                        app_date = listAppointment.get(recordNum).getDate();
                                    }
                                    
                                    /** DISPLAY THE AVAILABLE TIME FOR NEWLY SELECTED DATE OR EXISTING DATE OF THE APPOINTMENT **/
                                    System.out.println("Here are the list of available time for an appointment on " + app_date);
                                    count = 0; 
                                    for (int x = 0; x < listAppointment.size(); x++)
                                    {
                                        Appointment a = listAppointment.get(x); 
                                        
                                        /** CHECK THE CAPACITY OF EVERY TIME INTERVAL  **/
                                        if (a.getDate().equals(app_date))
                                        {
                                            checkCapacity(listA, a.getTime());
                                        }
                                    }
                                    
                                    /** DISLAY THE AVAILABLE TIME USING METHOD CALLING **/
                                    displayAvailability(listA);
                                    System.out.println("Please choose the time for the appointment.");
                                    System.out.print("Enter no (e.g 1, 2 or 3): ");
                                    
                                    
                                    while (!in.hasNextInt()) 
                                    {        
                                        in.next(); // Read and discard offending non-int input
                                        System.out.println("Wrong input. Please try again.\n");   //Re-prompt
                                        System.out.println("Please choose the time for the appointment.  ");
                                        System.out.print("Enter no (e.g 1, 2 or 3): ");
                                    }
                            
                                    int index = in.nextInt();   // Get the integer
                            
                                    while (index > interval.length || index < 1 ) // Making sure user do not enter index greater the array size or less than 1
                                    {
                                        System.out.println("Wrong input. Please try again.\n");   //Re-prompt
                                        System.out.println("Please choose the time for the appointment.  ");
                                        System.out.print("Enter no (e.g 1, 2 or 3): ");
                                        while (!in.hasNextInt()) 
                                        {        
                                            in.next(); // Read and discard offending non-int input
                                            System.out.println("Wrong input. Please try again.\n");   //Re-prompt
                                            System.out.println("Please choose the time for the appointment.  ");
                                            System.out.print("Enter no (e.g 1, 2 or 3): ");
                                        }

                                        index = in.nextInt();   // Get the integer
                                    }
                        
                        
                                     index = index - 1; 
                                    
                                  
                                    time_interval = listA.get(index).getTime();
                                    
                                    
                                    /** UPDATE THE TIME OF APPOINTMENT INSIDE OF DATABASE **/
                                    query = "UPDATE hasappointment SET time_interval = '"+time_interval+"' WHERE app_ID ='"+app_ID+"'";
                                    st.executeUpdate(query);
                                    
                                    /** UPDATE THE TIME OF APPOINTMENT INSIDE THE ARRAYLIST **/
                                    listAppointment.get(recordNum).setTime(time_interval);
                                    
                                    System.out.println("Changes have been made.");
                                    
                                    /** OBTAININING THE EMAIL OF THE PATIENT OF THE EDITED APPOINTMENT RECORD **/
                                    email = getPatientEmail(listPatients, listAppointment.get(recordNum).getIC());
                                    
                                    /** NOTIFYING THE PATIENT ABOUT THE CHANGES MADE THROUGH EMAIL **/
                                    Email.sendEmail(email, "time/date",listAppointment.get(recordNum));
                                }
                                
                                /** IF USER DECIDES TO CHANGE THE APPROVAL OF THE APPOINTMENT **/
                                else if (choice.equals("3"))
                                {
                                    /**  CHECK IF THE EXISTING APPROVAL IS "NOT APPROVED" **/
                                    if(listAppointment.get(recordNum).getStatus().equals("Not Approved"))
                                    {
                                        System.out.println("Do you wish to approve this appointment?");
                                        System.out.print("Enter [YES/NO]: ");
                                        choice = in.next();
                                        
                                        
                                        while(!choice.equalsIgnoreCase("YES") && !choice.equalsIgnoreCase("NO"))
                                        {
                                            System.out.println("Wrong input. Please try again.");
                                            System.out.print("Enter [YES/NO]:");
                                            choice = in.next();
                                        }
                                        
                                        if(choice.equalsIgnoreCase("YES"))
                                        {
                                            app_status = "Approved";
                                        }
                                    }
                                    
                                    /**  CHECK IF THE EXISTING APPROVAL IS "APPROVED" **/
                                    else
                                    {
                                        System.out.println("Do you wish to disapprove this appointment?");
                                        System.out.print("Enter [YES/NO]: ");
                                        choice = in.next();
                                        
                                        while(!choice.equalsIgnoreCase("YES") && !choice.equalsIgnoreCase("NO"))
                                        {
                                            System.out.println("Wrong input. Please try again.");
                                            System.out.print("Enter [YES/NO]:");
                                            choice = in.next();
                                        }
                                        
                                        if(choice.equalsIgnoreCase("YES"))
                                        {
                                            app_status = "Not Approved";
                                        }
                                    }
                                    
                                    /**  IF USER AGREES TO APPROVE/DISAPPROVE, UPDATE THE VALUE OF THE APPROVAL **/
                                    if(choice.equalsIgnoreCase("YES"))
                                    {
                                        listAppointment.get(recordNum).setStatus(app_status);
                                        query = "UPDATE appointment SET app_status = '"+app_status+"' WHERE app_ID ='"+app_ID+"'";
                                        st.executeUpdate(query);
                                        System.out.println("Changes have been made.");
                                        
                                        /** OBTAINING THE EMAIL OF THE PATIENT WHOSE APPROVAL HAS JUST BEEN EDITED **/
                                        email = getPatientEmail(listPatients, listAppointment.get(recordNum).getIC());
                                        
                                        /** NOTIFYING THE PATIENT ABOUT THE CHANGES MADE THROUGH EMAIL **/
                                        Email.sendEmail(email, "status",listAppointment.get(recordNum));
                                    }
                                }
                                
                                /** ASK USER IF THEY WANT TO CONTINUE EDITING. IF NOT, EXIST LOOPING **/
                                System.out.println("\nDo you want to change another field? ");
                                System.out.print("Enter [Y/N]: ");
                                flag = in.next().charAt(0);
                                
                                while(flag != ('Y') && flag != ('y') && flag != ('N') && flag != ('n'))
                                {
                                    System.out.println("Wrong input. Please try again.");
                                    System.out.print("Enter [Y/N]: ");
                                    flag = in.next().charAt(0);
                                }
                            }
                            
                        }
                        
                        /** IF USER DECIDES TO DELETE RECORD OF APPOINTMENT **/
                        else if(choice.equals("2"))
                        {
                            System.out.println("Which record you would like to choose?");
                            System.out.print("Enter record no. : ");
                            
                            
                            while (!in.hasNextInt()) 
                            {        
                                in.next(); // Read and discard offending non-int input
                                System.out.println("Wrong input. Please try again.");   //Re-prompt
                                System.out.print("Enter field no. : ");
                            }
                            
                            recordNum = in.nextInt();   // Get the integer
                            
                            while (recordNum > listAppointment.size() || recordNum < 1 )
                            {
                                System.out.println("Wrong input. Please try again.");
                                System.out.print("Enter field no. : ");
                                while (!in.hasNextInt()) 
                                {        
                                    in.next(); // Read and discard offending non-int input
                                    System.out.println("Wrong input. Please try again.");   //Re-prompt
                                    System.out.print("Enter field no. : ");
                                }
                                recordNum = in.nextInt();   // Get the integer
                            }
                            
                            recordNum =  recordNum - 1;      // Getting the index of the arrayList appointment
                            
                            
                            app_ID = listAppointment.get(recordNum).getID();
                            
                            /** OBTAINING THE HA_ID FROM TABLE hasappointment WHICH IS RELATED TO SELECTED RECORD **/
                            query = "SELECT HA_ID from hasappointment WHERE app_ID = '"+app_ID+"'";
                            rs = st.executeQuery(query);
                            while (rs.next())
                            {
                                HA_ID = rs.getInt("HA_ID");
                            }
                            /** DELETE THE ROW FROM TABLE hasappointment WHICH RELATED TO THE CHOOSEN APPOINTMENT **/
                            query = "DELETE from hasappointment WHERE HA_ID = '"+HA_ID+"'";
                            st.executeUpdate(query);
                            
                            /** DELETE THE APPOINTMENT **/
                            query = "DELETE from appointment WHERE app_ID = '"+app_ID+"'";
                            st.executeUpdate(query);
                            
                            System.out.println("Appointment has been deleted!\n");
                            
                        }
                        
                        /** IF USER DECIDES TO BACK TO MAIN MENU **/
                        else if(choice.equals("3"))
                        {
                            skip = 1;
                            choice = "1";
                        }
                        repeat = 1;
                        
                    }
                    
                    /***    DISPLAY LIST OF PATIENTS ***/
                    else if(choice.equals("2"))
                    {
                        displayListPatients(listPatients);
                        repeat = 1;
                    }
                    
                    else if(choice.equals("3"))
                    {
                        /** DISPLAY THE CURRENT OPENING AND CLOSING HOUR **/
                        
                        query = "SELECT * from businesshour";
                        rs = st.executeQuery(query);                // Execute the query and get a java resultset
                                        
                        while (rs.next())
                        {
                            opening = rs.getString("opening");
                            closing = rs.getString("closing");
                        }
                        String allignment = "| %-7s | %-7s |%n";
                        System.out.format("+---------+---------+%n");
                        System.out.format("| Opening | Closing |%n");
                        System.out.format("+---------+---------+%n");
                        System.out.format(allignment, opening,closing);
                        System.out.format("+---------+---------+%n");
                        
                        System.out.println("1 - Change Opening/Closing time");
                        System.out.println("2 - Back");    // back to the staff menu
                        System.out.print("Enter your choice: ");
                        choice = in.next();
                        
                        while (!choice.equals("1") && !choice.equals("2"))
                        {
                            System.out.println("Wrong input. Please try again.");
                            System.out.print("Enter your choice: ");
                            choice = in.next();
                        }
                        
                        if (choice.equals("1"))
                        {
                            System.out.print("Enter new opening hour: ");
                            opening = in.next();
                            System.out.print("Enter new closing hour: ");
                            closing = in.next();
                           
                            int tempO = Integer.parseInt(opening);
                            int tempC = Integer.parseInt(closing);
                            
                            while (tempO > tempC)
                            {
                                System.out.println("Error! Opening must be earlier than closing. Please try again.\n");
                                System.out.print("Enter new opening hour: ");
                                opening = in.next();
                                System.out.print("Enter new closing hour: ");
                                closing = in.next();
                                tempO = Integer.parseInt(opening);
                                tempC = Integer.parseInt(closing);
                            }    
                            
                            
                            /** CHANGE THE OPENING/CLOSING BASED ON USER'S INPUTS **/
                            query = "UPDATE businesshour SET opening = '"+opening+"', closing = '"+closing+"' WHERE bh_ID =1";
                            st.executeUpdate(query);            //execute the update query
                            
                            System.out.println("Changes have been made.");
                        }
                        
                        else if (choice.equals("2"))
                        {
                            skip = 1;
                            choice = "1";
                        }   
                            
                        
                        
                        repeat = 1;
                    }
                                 
                    
                    
                    /** ASK USER IF THEY STILL WANT TO CONTINUE USING THE SYSTEM **/
                    if (skip == 0)
                    {
                        System.out.println("\nDo you wish to continue?");
                        System.out.println("1. Continue");
                        System.out.println("2. Exit");
                        System.out.print("Enter your choice: ");
                        choice = in.next();
                        while (!choice.equals("1") && !choice.equals("2"))
                        {
                            System.out.println("Wrong input. Please try again.");
                            System.out.print("Enter your choice: ");
                            choice = in.next();
                        }
                        
                       
                    }
                    
                    skip = 0;
                }
                
                
            }
            
            /** Patient Section **/
            else if ( user.equals("2"))
            {
                System.out.print("Please enter your IC: ");
                IC = in.next();
                
                
                while (!choice.equals("2"))
                {
                    ArrayList<Appointment> listAppointment = new ArrayList<Appointment>();
                    ArrayList<Patient> listPatients = new ArrayList<Patient>();
                    /** Initializing both list using method **/
                    initiateList(listPatients,listAppointment);
                
                    
                    System.out.println("");
                    if (repeat == 1)
                    {
                        patientMenu();
                        choice = in.next();
                        repeat = 0;
                    }
                    
                    while (!choice.equals("1") && !choice.equals("2"))
                    {
                        System.out.println("Wrong input. Please try again.");
                        System.out.print("Please Enter your choice: ");
                        choice = in.next();
                    }
                    /***  MAKE AN APPOINTMENT  ***/
                    if(choice.equals("1"))
                    {
                        int check = 0;
 
                        for (int x = 0; x < listPatients.size(); x++)
                        {
                            if (listPatients.get(x).getIC().equals(IC))
                            {
                                check = 1;
                                break;
                            }
                        }
                        
                        /** IF PATIENT HAS NOT REGISTERED, THE SYSTEM WILL ASK THEM TO REGISTER FIRST **/
                        if (check == 0)
                        {
                            Scanner sc = new Scanner (System.in);   // New scanner to avoid skipping
                            System.out.println("It looks like that you have not registered in the system.\n");
                            
                            System.out.print("Enter your full name: ");
                            fullName = sc.nextLine();
                           
                            System.out.print("Enter your gender (Male/Female): ");
                            gender = in.next();
                            
                            System.out.print("Enter your phone number: ");
                            phone = in.next();
                            
                            System.out.print("Enter your email address: ");
                            email = in.next();
                            
                            /** ADD A NEW RECORD INSIDE THE TABLE Patient **/
                            query = "INSERT INTO patient (IC, full_name, phone_number, gender, email) VALUES ('"+IC+"', '"+fullName+"', '"+phone+"','"+gender+"', '"+email+"')";
                            st.executeUpdate(query);
                            System.out.println("Congratulation! You have been registered. Now, proceed with making an appointment.\n");
                        }
                        
                        System.out.print("Please enter the date of appointment you would like to have (DD/MM/YYYY): ");
                        String date = in.next();
                        
                        /** CHECK THE AVAILABILITY OF THE SELECTED DATE **/
                        System.out.println("Here are the list of available time for an appointment on " + date);
                        count = 0; 
                        for (int x = 0; x < listAppointment.size(); x++)
                        {
                            Appointment a = listAppointment.get(x); 
                            
                            if (a.getDate().equals(date))
                            {
                                checkCapacity(listA, a.getTime());
                            }
                        }
                        
                        displayAvailability(listA);
                        System.out.println("Please choose the time for the appointment.  ");
                        System.out.print("Enter no (e.g 1, 2 or 3): ");
                        
                                    
                        while (!in.hasNextInt()) 
                        {        
                            in.next(); // Read and discard offending non-int input
                            System.out.println("Wrong input. Please try again.\n");   //Re-prompt
                            System.out.println("Please choose the time for the appointment.  ");
                            System.out.print("Enter no (e.g 1, 2 or 3): ");
                        }
                            
                        int index = in.nextInt();   // Get the integer
                            
                        while (index > interval.length || index < 1 ) // Making sure user do not enter index greater the array size or less than 1
                        {
                            System.out.println("Wrong input. Please try again.\n");   //Re-prompt
                            System.out.println("Please choose the time for the appointment.  ");
                            System.out.print("Enter no (e.g 1, 2 or 3): ");
                            
                            while (!in.hasNextInt()) 
                            {        
                                in.next(); // Read and discard offending non-int input
                                System.out.println("Wrong input. Please try again.\n");   //Re-prompt
                                System.out.println("Please choose the time for the appointment.  ");
                                System.out.print("Enter no (e.g 1, 2 or 3): ");
                            }

                            index = in.nextInt();   // Get the integer
                        }
                        
                        
                        index = index - 1; 
                        time_interval = listA.get(index).getTime();
                        app_status = "Not Approved";

                        /** INSERT NEW RECORD INTO THE TABLE OF Appointment INSIDE OF THE DATABASE **/
                        query = "INSERT INTO appointment (app_date, app_status) VALUES ('"+date+"', '"+app_status+"')"; 
                        st.executeUpdate(query);
                        
                        /** OBTAIN APPOINTMENT ID OF THE LATEST RECORD FROM THE TABLE Appointment **/
                        query = "SELECT app_ID FROM appointment ORDER BY app_ID DESC LIMIT 1";
                        rs = st.executeQuery(query); 
                        app_ID = 0;
                        while (rs.next())
                        {
                            app_ID = rs.getInt(1);
                        }
                        
                        /** INSERT NEW RECORD INTO THE TABLE OF hasAppointment THAT HAS THE FOREIGN KEY OF Patient(IC) AND Appointment (app_ID) **/
                        query = "INSERT INTO hasAppointment (time_interval, app_ID, IC) VALUES ('"+time_interval+"', '"+app_ID+"', '"+IC+"')"; 
                        st.executeUpdate(query);
                        
                        System.out.println("You have made an appointment. The request will be reviewed by the Clinic soon.");
                        
                        /** NOTIFYING PATIENT OF THE NEWLY REQUESTED APPOINTMENT THROUGH EMAIL **/
                        email = getPatientEmail(listPatients, IC);
                        
                        
                        /*** GETTING THE DETAILS OF NEWLY CREATED APPOINTMENT FROM DATABASE ***/
                        query = "SELECT a.app_ID, a.app_date, h.time_interval, a.app_status, h.IC, p.full_name FROM hasAppointment h, appointment a, patient p WHERE h.IC = p.IC AND h.app_ID = a.app_ID ORDER BY a.app_ID = '"+app_ID+"'";    
                        rs = st.executeQuery(query);                // Execute the query and get a java resultset
                                    
                                     
                        while (rs.next())
                        {
                           app_ID = rs.getInt(1);
                           app_date = rs.getString(2);
                           time_interval = rs.getString(3);
                           app_status = rs.getString(4);
                           IC = rs.getString(5);
                           fullName = rs.getString(6);
                                                    
                           Appointment newAdded = new Appointment (app_ID, app_date, time_interval, app_status, IC, fullName);
                           
                           Email.sendEmail(email, "make appointment", newAdded);
                        }
                        repeat = 1;
                    }
                    
                    /***  CHECK APPOINTMENT  ***/
                    else if(choice.equals("2"))
                    {
                        count = 0;
                        
                        /** CHECK IF THERE USER HAS ANY APPOINTMENT**/
                        for (int x = 0; x < listAppointment.size(); x++)
                        {
                            String temp = listAppointment.get(x).getIC();
                            
                            if (temp.equals(IC))
                            {
                                count = count + 1;
                            }
                        }
                        
                        /** IF USER HAS APPOINTMENT, DISPLAY IT **/
                        if(count > 0)
                        {
                            System.out.println("You have " + count + " appointment(s).\n");
                            
                            System.out.println("List of Appointments");
                            String allignment = "| %-10s | %-19s | %-13s |%n";
                            System.out.format("+------------+---------------------+---------------+%n");
                            System.out.format("| Date       | Time                | Status        |%n");
                            System.out.format("+--------- --+---------------------+---------------+%n");
                            
                            for (int x = 0; x < listAppointment.size(); x++)
                            {
                                Appointment tempA = listAppointment.get(x);
                                if (tempA.getIC().equals(IC))
                                {
                                    System.out.printf(allignment, tempA.getDate(), tempA.getTime(), tempA.getStatus());
                                }
                            }
                            System.out.format("+--------- --+---------------------+---------------+%n");
                        }
                        
                        /** IF THERE IS NO APPOINTMENT, DISPLAY THIS **/
                        else 
                        {
                            System.out.println("There is no record found.");
                        }
                        repeat = 1;
                    }
                    
                    
       
                    /** ASK USER WHETHER THEY WANT TO CONTINUE USING THE SYSTEM OR NOT **/
                    System.out.println("\nDo you wish to continue?");
                    System.out.println("1. Continue");
                    System.out.println("2. Exit");
                    System.out.print("Enter your choice: ");
                    choice = in.next();
                   
                    while (!choice.equals("1") && !choice.equals("2"))
                    {
                        System.out.println("Wrong input. Please try again.");
                        System.out.print("Enter your choice: ");
                        choice = in.next();
                    }
                }    
            
            }
            
            
            System.out.println("Thank you for using our system!");
            conn.close();
        }
        
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public static void staffMenu ()         // Method to display the menu for Staff
    {
        System.out.println("~~~~~   iHealth Clinic Admin v0.0.1 ~~~~~~~~");
        System.out.println("How can I help you today?\n");
        System.out.println("1. List of Appointments");
        System.out.println("2. List of Patients");
        System.out.println("3. Set Opening/Closing Time\n");
        System.out.print("Please enter your choice: ");
    }
    
    public static void displayListPatients (ArrayList <Patient> a)          // Method to display the list of patients
    {
        System.out.println("List of Patients");
        String allignment = "| %-24s | %-20s | %-6s | %-12s | %-22s |%n";
        System.out.format("+--------------------------+----------------------+--------+--------------+------------------------+%n");
        System.out.format("| Identification Card (IC) | Name                 | Gender | Phone Number | Email                  |%n");
        System.out.format("+--------------------------+----------------------+--------+--------------+------------------------+%n");
        for(int x = 0; x < a.size(); x++)
        {
            System.out.print(a.get(x).toString());
        }
        System.out.format("+--------------------------+----------------------+--------+--------------+------------------------+%n");
    }
    
    public static void displayListAppointments (ArrayList <Appointment> a)      // Method to display the list of appointments
    {
        System.out.println("List of Appointments");
        String allignment = "| %-4d | %-24s | %-10s | %-19s | %-13s |%n";
        System.out.format("+------+--------------------------+------------+---------------------+---------------+%n");
        System.out.format("|No.   | Name                     | Date       | Time                | Status        |%n");
        System.out.format("+------+--------------------------+------------+---------------------+---------------+%n");
        for(int x = 0; x < a.size(); x++)
        {
            int no = x + 1;
            System.out.print(a.get(x).toString(no));
        }
        System.out.format("+------+--------------------------+------------+---------------------+---------------+%n");
    }
    
    public static void displayAvailability (ArrayList <Availability> a)         // Method to display the list of available time of certain date 
    {
        String allignment = "| %-4d | %-20s | %-8s |%n";
        System.out.format("+------+----------------------+----------+%n");
        System.out.format("|No.   | Time                 | Capacity |%n");
        System.out.format("+------|----------------------+----------+%n");
        for(int x = 0; x < a.size(); x++)
        {
            if (a.get(x).getCapacity() < 3)                // Only time interval with less than 3 patients will be displayed 
            {
                int no = x + 1;
                System.out.print(a.get(x).toString(no));
            }    
            
        }
        System.out.format("+------|----------------------+----------+%n");
    }
    
    public static void patientMenu ()      // Method to display the menu for patients
    {
        System.out.println("~~~~~   iHealth Clinic v0.0.1 ~~~~~~~~");
        System.out.println("How can I help you today?\n");
        System.out.println("1. Make Appointment");
        System.out.println("2. Check My Appointment\n");
        System.out.print("Please enter your choice: ");
    }
    
   
    public static void createInterval ( String a , String b, String[] e )           // Method to create time interval for appointment based on opening and closing time
    {
       int c = Integer.parseInt(a);
       int d = Integer.parseInt(b);
      
       int size = (d - c)/100; 
       
       
       String add = "";
       String add_2 = "";
       for(int x = 0; x < size; x++)
       {   
           if ( c < 1000)
           {
               add = "0";
           }
           
           if ( c+100 < 1000)
           {
               add_2 = "0";
           }
           e[x] = add + Integer.toString(c) + " - " + add_2 + Integer.toString(c+100);
           
           add = "";
           add_2 = "";
           c = c + 100;
       } 
    }
                                                          
    public static void checkCapacity (ArrayList <Availability> a, String b)     // Method to check the capacity of every time interval for certain date
    {
        int kira = 0;
       
        for ( int x = 0; x < a.size(); x++ )
        {
            kira = a.get(x).getCapacity();
            
            if (a.get(x).getTime().equals(b))
            {
                kira = kira + 1; 
                a.get(x).setCapacity(kira);
            }
        }
        
    }
                                    
    public static int createArraySize ( String a , String b)         // Method to return size of array from opening and closing time
    {
       int c = Integer.parseInt(a);
       int d = Integer.parseInt(b);
       
       int size = (d - c)/100; 
       
       return size;
    }
                                            
    public static String getPatientEmail (ArrayList <Patient> a, String b)  /*** GETTING THE EMAILS OF THE PATIENT ***/
    {
        String email = "";
        for (int x  = 0; x < a.size(); x++)
        {
            if(a.get(x).getIC().equals(b))
            {
                email = a.get(x).getEmail();
                break;
            }
        }
        return email;
    }
   
    public static void initiateList (ArrayList <Patient> a, ArrayList <Appointment> b)
    {
        try
        {
            // Query to get all rows in table hasAppointment and related data in table patient and appointment         
            Connection conn = DBConnect.getConnection();            // Establish database connection
            Statement st = conn.createStatement();   
            
            /*** STORE LIST OF PATIENT FROM DATABASE ***/
            String query = "SELECT * FROM patient";            // Query to display all patients
            ResultSet rs = st.executeQuery(query);                // Execute the query and get a java resultset
                                    
            while (rs.next())
            {
                String IC = rs.getString("IC");
                String fullName = rs.getString("full_name");
                String phone = rs.getString("phone_number");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                                        
                Patient newPatient = new Patient (IC, fullName, gender, phone, email);
                a.add(newPatient);
            }
            
            /*** STORE LIST OF APPOINTMENT FROM DATABASE ***/
            query = "SELECT a.app_ID, a.app_date, h.time_interval, a.app_status, h.IC, p.full_name FROM hasAppointment h, appointment a, patient p WHERE h.IC = p.IC AND h.app_ID = a.app_ID ORDER BY a.app_date, h.time_interval";    
            rs = st.executeQuery(query);                // Execute the query and get a java resultset
                                    
                                     
            while (rs.next())
            {
                int app_ID = rs.getInt(1);
                String app_date = rs.getString(2);
                String time_interval = rs.getString(3);
                String app_status = rs.getString(4);
                String IC = rs.getString(5);
                String fullName = rs.getString(6);
                                        
                Appointment newAppointment = new Appointment (app_ID, app_date, time_interval, app_status, IC, fullName);
                b.add(newAppointment);
            }
        }
        
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    
    }
}
