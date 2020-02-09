/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.reader;
import static javatest.Commonclass.readerMgt;


/**
 *
 * @author knf483
 */
public class ConnectReader extends Commonclass{
    boolean invalidCertificate = false;
    boolean noCertificate = false;

     public void ConnectReader()
    {
    
        try
        {
            mystreamLog = new FileOutputStream("JavaAPI_ConnectReader_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_ConnectReader_Log.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            AppendText();
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
        psLog.println("<html><br>");
        psLog.println("<body><br>");
    }
     
     
    private String readFileContent(String strFilePath) {
        String strContent = "";
        String line;
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new FileReader(strFilePath));
            while ((line = reader.readLine()) != null) {
                strContent += "\n" + line;
            }
            // Cut of the first newline;
            strContent = strContent.substring(1);
            // Close the reader
            reader.close();
            return strContent;
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
        return null;
    }

    private void initSecureConnection(String PhraseInfo, boolean SecureMode, boolean ValidatePeerCerticate) {
        reader = new RFIDReader(ip, 5085, 5000);
        psLog.println("<br> Secure Mode Parameters -:");
        psLog.println("<br>"+ "SecureMode : "+SecureMode + "   ValidatePeerCerticate :" + ValidatePeerCerticate);

        reader.SecureConnectionInfo = new SecureConnectionInfo();
        reader.SecureConnectionInfo.setSecureMode(SecureMode);
        reader.SecureConnectionInfo.setValidatePeerCerticate(ValidatePeerCerticate);

        String CurrentDirectory;
        if (invalidCertificate) {
            invalidCertificate = false;
            CurrentDirectory = "C:\\Program Files\\Motorola EMDK for Java\\RFID\\Integrator";
            String ClientCertPath = CurrentDirectory + "/EMDKJavaRFID_SetupDLL.txt";
            psLog.println("<br> Invalid Certificate:  " + ClientCertPath.toString());
            
            String output = readFileContent(ClientCertPath);

            reader.SecureConnectionInfo.setClientCertificate(output);

            //Setting Client Private Key.
            String ClientKeyPath = CurrentDirectory + "/EMDKJavaRFID_SetupDLL.txt";
            output = readFileContent(ClientKeyPath);
            reader.SecureConnectionInfo.setClientPrivateKey(output);

            //Setting Phrase Info
            reader.SecureConnectionInfo.setPhraseInfo(PhraseInfo);

            //Setting Root Certificate.
            String rootCertPath = CurrentDirectory + "/EMDKJavaRFID_SetupDLL.txt";
            output = readFileContent(rootCertPath);
            reader.SecureConnectionInfo.setRootCertificate(output);     
           
        } else {
            if (noCertificate) { 
                noCertificate = false;
                CurrentDirectory = "C:\\Program Files\\Motorola EMDK for Java\\RFID\\Samples\\netbeans\\emdk\\J_RFIDHostSample1";
                psLog.println("<br> Certificates are not available in path:  " + CurrentDirectory.toString());
            } else {
                CurrentDirectory = System.getProperty("user.dir");
                psLog.println("<br> Current Directroy: " + CurrentDirectory.toString());
            }
            
            String ClientCertPath = CurrentDirectory + "/client_crt.pem";

            String output = readFileContent(ClientCertPath);

            reader.SecureConnectionInfo.setClientCertificate(output);

            //Setting Client Private Key.
            String ClientKeyPath = CurrentDirectory + "/client_key.pem";
            output = readFileContent(ClientKeyPath);
            reader.SecureConnectionInfo.setClientPrivateKey(output);

            //Setting Phrase Info
            reader.SecureConnectionInfo.setPhraseInfo(PhraseInfo);

            //Setting Root Certificate.
            String rootCertPath = CurrentDirectory + "/cacert.pem";
            output = readFileContent(rootCertPath);
            reader.SecureConnectionInfo.setRootCertificate(output);
        }
    }
     
   public void connectReaderUnSecureMode() {
        String OS = System.getProperty("os.name");
        System.out.print("OS: " + OS.toString() + "\n");
        if (OS.compareTo("Windows CE") == 0) {
            ip = "127.0.0.1";
        }
        
        reader = new RFIDReader(ip, 0, 5000);
        try {        
            System.out.print("IP: address" + ip + "\n");
            reader.connect();
            System.out.print("Reader SuccessFully Connected");
            System.out.println("" + " C dll version Info : " + reader.versionInfo().getVersion() + "\n" + "Reader IP :" + reader.getHostName());
            System.out.println("isConnected " + reader.isConnected());
            
            psSummary.println("C dll version Info : "+reader.versionInfo().getVersion() + "    Reader IP: "+reader.getHostName());
            psSummary.println("Reader Model : " + reader.ReaderCapabilities.getModelName());
            psSummary.println("FW Version : " + reader.ReaderCapabilities.getFirwareVersion());
            psSummary.println("isConnected : "+reader.isConnected());
            psSummary.println("Secure Connection : " + reader.SecureConnectionInfo.isSecureModeEnabled());
            psSummary.println();
            psSummary.println();

        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
    }
    
    public void connectReaderInSecureMode() {
        System.out.print("Secure Connection Started...");

        try {
            initSecureConnection("abcd12345", true, false);
            
            String OS = System.getProperty("os.name");
            System.out.print("OS: " + OS.toString() + "\n");
            psSummary.println("OS: " + OS.toString() + "\n");
            System.out.print("IP: address" + ip + "\n");
            reader.connect();
            System.out.print("Reader SuccessFully Connected");
            System.out.println("" + " C dll version Info : " + reader.versionInfo().getVersion() + "\n" + "Reader IP :" + reader.getHostName());
            System.out.println("isConnected " + reader.isConnected());
            System.out.print("Reader SuccessFully Connected");

            psSummary.println("C dll version Info : "+reader.versionInfo().getVersion() + "    Reader IP: "+reader.getHostName());
            psSummary.println("Reader Model : " + reader.ReaderCapabilities.getModelName());
            psSummary.println("FW Version : " + reader.ReaderCapabilities.getFirwareVersion());
            psSummary.println("isConnected : "+reader.isConnected());
            psSummary.println("Secure Connection : " + reader.SecureConnectionInfo.isSecureModeEnabled());
            psSummary.println();
            psSummary.println();
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }    
    }

    public void testDisConnect() {
        try {
            reader.disconnect();
            System.out.println("Reader SuccessFully DisConnected");
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }   
    }
    
    public void testSecureConnection() {

        System.out.print("\nTest Secure Connection Started...");
        
        TestNo = 107;
        SubNo = 0;

        try {
            if (reader.isConnected()) {
                reader.disconnect();
            }
        } catch (Exception e) {
            AnalyseException(null, e);
        }
        
        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            initSecureConnection("abcd12345", true, true);
            reader.connect();
            reader.disconnect();
            LogStatus( TestID, "connect", true);
        } catch (Exception e) {
            LogStatus( TestID, "connect", false);
            AnalyseException(null, e);
        }
        

        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            invalidCertificate = true;
            initSecureConnection("abcd12345", true, true);
            reader.connect();
            reader.disconnect();
            LogStatus( TestID, "secure connect", true);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", false);
            AnalyseException(null, e);
        }


        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            noCertificate = true;
            initSecureConnection("abcd12345", true, true);
            reader.connect();

            reader.disconnect();
            LogStatus( TestID, "secure connect", true);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", false);
            AnalyseException(null, e);
        }
        
        SubNo = 0;
        
        try {
            FormTestID(++TestNo, ++SubNo, "SECURE");
            initSecureConnection("abcd12345", true, false);
            reader.connect();
            Thread.sleep(100);
            reader.disconnect();
            LogStatus( TestID, "secure connect", true);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", false);
            AnalyseException(null, e);
        }
        

        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            invalidCertificate = true;
            initSecureConnection("abcd12345", true, false);
            reader.connect();
            reader.disconnect();
            LogStatus( TestID, "secure connect", true);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", false);
            AnalyseException(null, e);
        }


        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            noCertificate = true;
            initSecureConnection("abcd12345", true, false);
            reader.connect();

            reader.disconnect();
            LogStatus( TestID, "secure connect", true);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", false);
            AnalyseException(null, e);
        }
        
        SubNo = 0;
        
         try {
            FormTestID(++TestNo, SubNo, "SECURE");
            noCertificate = true;
            initSecureConnection("abcd12345", false, true);
            reader.connect();

            reader.disconnect();
            LogStatus( TestID, "secure connect", false);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", true);
            AnalyseException(null, e);
        }
         
         try {
            FormTestID(++TestNo, SubNo, "SECURE");
            noCertificate = true;
            initSecureConnection("abcd12345", false, false);
            reader.connect();

            reader.disconnect();
            LogStatus( TestID, "secure connect", false);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", true);
            AnalyseException(null, e);
        }

        try {
            System.out.print("\n Invalid Pass code...");
            FormTestID(++TestNo, SubNo, "SECURE");
            noCertificate = true;
            initSecureConnection("abcd1234", true, true);
            psLog.println("<br> Invalid Pass code.. ");
            reader.connect();

            reader.disconnect();
            LogStatus( TestID, "secure connect", false);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", true);
            AnalyseException(null, e);
        }
        
        try {
            System.out.print("\n Invalid Pass code...");
            FormTestID(++TestNo, SubNo, "SECURE");
            noCertificate = true;
            initSecureConnection("abcd12", false, false);
            psLog.println("<br> Invalid Pass code.. "); 
            reader.connect();

            reader.disconnect();
            LogStatus( TestID, "secure connect", false);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", true);
            AnalyseException(null, e);
        }
        System.out.print("\nTest SecureConnection Completed...");        
    }

    public void testSelfSecureConnection() {
        System.out.print("\nTest SlelfSecureConnection Started...");
        try {
            if (reader.isConnected()) {
                reader.disconnect();
            }
        } catch (Exception e) {
            AnalyseException(null, e);
        }
        TestNo = 113;
        SubNo = 0;   
        try {
            FormTestID(TestNo, SubNo, "SECURE");
            initSecureConnection("abcd12345", true, false);
            reader.connect();

            reader.disconnect();
            LogStatus( TestID, "secure connect", true);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", false);
            AnalyseException(null, e);
        }
        System.out.print("\nTest SlelfSecureConnectio Completed...");
    }
  
    public void testSecureConnectionWithPeercheck() {

        System.out.print("\nTest SecureConnectionWithPercheck Started...");
        
        TestNo = 114;
        SubNo = 0;

        try {
            if (reader.isConnected()) {
                reader.disconnect();
            }
        } catch (Exception e) {
            AnalyseException(null, e);
        }
        
        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            initSecureConnection("abcd12345", true, false);
            reader.connect();
            reader.disconnect();
            LogStatus( TestID, "secure connect", true);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", false);
            AnalyseException(null, e);
        }
        

        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            invalidCertificate = true;
            initSecureConnection("abcd12345", true, true);
            reader.connect();
            reader.disconnect();
            LogStatus( TestID, "secure connect", false);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", true);
            AnalyseException(null, e);
        }


        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            noCertificate = true;
            initSecureConnection("abcd12345", true, true);
            reader.connect();

            reader.disconnect();
            LogStatus( TestID, "secure connect", false);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", true);
            AnalyseException(null, e);
        }
        
        SubNo = 0;
        
        try {
            FormTestID(++TestNo, ++SubNo, "SECURE");
            initSecureConnection("abcd12345", true, true);
            reader.connect();
            reader.disconnect();
            LogStatus( TestID, "secure connect", true);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", false);
            AnalyseException(null, e);
        }
        

        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            invalidCertificate = true;
            initSecureConnection("abcd12345", true, true);
            reader.connect();
            reader.disconnect();
            LogStatus( TestID, "secure connect", false);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", true);
            AnalyseException(null, e);
        }

        try {
            FormTestID(TestNo, ++SubNo, "SECURE");
            noCertificate = true;
            initSecureConnection("abcd12345", true, true);
            reader.connect();

            reader.disconnect();
            LogStatus( TestID, "secure connect", false);
        } catch (Exception e) {
            LogStatus( TestID, "secure connect", true);
            AnalyseException(null, e);
        }
        
        System.out.print("\nTest SecureConnectionWithPercheck Completed...");
    }    
    
}
