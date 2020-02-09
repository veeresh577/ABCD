/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

/**
 *
 * @author NVJ438
 */
import com.mot.rfid.api3.*;
import java.io.*;
import static javatest.Commonclass.mystreamLog;
import static javatest.Commonclass.mystreamResult;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.reader;

public class GPIO extends Commonclass {
    
    GPIs gpi = reader.Config.GPI;
    GPOs gpo = reader.Config.GPO;
    ReaderManagement readerMgt;  
    LoginInfo login;
    
    //constructor in which log file is created.
    public GPIO() {
        try {
            mystreamLog = new FileOutputStream("JavaAPI_GPI_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_GPI_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        } catch (FileNotFoundException e) {
            psLog.println("" + e.getMessage());
        }
    }
    public void Test_GPI()
    {
        try{
            // 
            RM_Login();
            readerMgt.setGPIDebounceTimeMilliseconds(60000);
            psLog.println("<br>getGPIDebounceTimeMilliseconds "+readerMgt.getGPIDebounceTimeMilliseconds());
            for(int i=1; i<=gpi.getLength(); i++)
            {
                                
                psLog.println("<br>GPI port state for port number "+ i +" is "+ gpi.getPortState(i));
                psLog.println("<br>Enabling the port number "+ i );
                gpi.enablePort(i, false);
                psLog.println("<br>Is port "+ i +" is enabled "+ gpi.isPortEnabled(i));
                System.out.println(" Short the GPI "+i+" pin");
                System.out.println(" Hit enter once");
                System.in.read();
                gpi.enablePort(i, true);
                psLog.println("<br>Is port "+ i +" is enabled "+ gpi.isPortEnabled(i));
                System.out.println(" Short the GPI "+i+" pin");
                System.out.println(" Hit enter once");
                System.in.read();
//                Thread.sleep(1000);
//                System.in.read();
            }
            
        } catch (InvalidUsageException exp) {
            psLog.println("<br>getVendorMessage" + exp.getVendorMessage());
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
        } catch (OperationFailureException exp) {
            psLog.println("<br>OperationFailureException" + exp.getMessage());
            psLog.println("<br>getVendorMessage" + exp.getVendorMessage());
        } 
//        catch(InterruptedException e) {
////          System.out.print("\nInterruptedException"+e.getMessage());
//            psLog.println("\nInterruptedException"+e.getMessage());
//        } 
        catch(IOException e){
            psLog.println("\nInterruptedException"+e.getMessage());
        }
        
        
    }
    public void Test_GPO()
    {
        try{
               
            for(int i=1; i<=gpo.getLength(); i++)
            {
                psLog.println("<br>GPI port state for port number "+ i +" is "+ gpo.getPortState(i));
                
            }
            
        }catch (InvalidUsageException exp) {
            psLog.println("<br>getVendorMessage" + exp.getVendorMessage());
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
        } catch (OperationFailureException exp) {

            psLog.println("<br>OperationFailureException" + exp.getMessage());
            psLog.println("<br>getVendorMessage" + exp.getVendorMessage());
        }
        
        
    }
    public int RM_Login() {
        login = new LoginInfo();

        psLog.println("<br>Inside the Reader Management Login Method:");
        String s = login.getHostName();
        READER_TYPE type = null;
        String model;
        String user = "admin";
        String password = "change";
        readerMgt = new ReaderManagement();

        if (reader.ReaderCapabilities.getModelName().equals("3190"))
        {
            //  model = "MC";
            login = new LoginInfo("127.0.0.1", user, password, null, true);
            //login = null;
            type = READER_TYPE.MC;
        }

        if (reader.ReaderCapabilities.getModelName().equals("74004") || reader.ReaderCapabilities.getModelName().equals("75002") || reader.ReaderCapabilities.getModelName().equals("75004"))
        {
            login = new LoginInfo(reader.getHostName(), user, password, SECURE_MODE.HTTP, true);
            type = READER_TYPE.FX;
            model = "FX";
            psLog.println("<br>Attempting Login....");
        }
        if (reader.ReaderCapabilities.getModelName().equals("9190"))
        {
             //  model = "MC";
            login = new LoginInfo(reader.getHostName(), user, password, SECURE_MODE.HTTP, true);
            type = READER_TYPE.MC;
            model = "MC";
            psLog.println("<br>Attempting Login....");
        }

        try
        {
            readerMgt.login(login, type);
            psLog.println("<br>Login success");
            System.out.println("<br>Login success");
            readerMgt.setTraceLevel(TRACE_LEVEL.TRACE_LEVEL_ALL);
            return success;
        } 
        catch (InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("\nInvalidUsageException" + exp.getInfo());
            return failure;
        } catch (OperationFailureException exp) {
            System.out.print("\nOperationFailureException" + exp.getStatusDescription());
            psLog.println("\nOperationFailureException" + exp.getStatusDescription());
            return failure;
        } catch (Exception e) {
            System.out.print("\nException:" + e);
            return failure;
        }

    }//RM_Login()
    
}
