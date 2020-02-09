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
//import static javatest.Main.testConnect; //comment by aarthi

/**
 *
 * @author NVJ438
 */
class nxpEventsListener implements RfidEventsListener
{
     public RFIDReader myReader = null;
    public PrintStream myLogger = null;
    public static TagData tagData=null;
     public nxpEventsListener(RFIDReader reader,PrintStream psLog)
    {
        myReader = reader;
        myLogger = psLog;
    }

    public void eventReadNotify(RfidReadEvents rfidReadEvents)
    {
        //System.out.print("READ EVENT Happened");

        if(rfidReadEvents!=null)
        {
            TagData tagData  = rfidReadEvents.getReadEventData().tagData;

            myLogger.println("<br> tags ID "+tagData.getTagID());
        }
    }


    @Override
    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
    {
        myLogger.println("<br> Event"+rfidStatusEvents.StatusEventData.getStatusEventType());
       // System.out.print(" NXP EAS Event"+rfidStatusEvents.StatusEventData.getStatusEventType());
       // System.out.printf(" antenna ID %d alram Code %d",rfidStatusEvents.StatusEventData.NXPEASAlarmEventData.getantennaID(),rfidStatusEvents.StatusEventData.NXPEASAlarmEventData.getEASAlarmCode());
        //        System.out.print(" Disconnect event "+rfidStatusEvents.StatusEventData.DisconnectionEventData.toString());
        //System.out.print(" GPI event "+rfidStatusEvents.StatusEventData.GPIEventData.getGPIPort());
            myLogger.println(" <br>alram Code "+rfidStatusEvents.StatusEventData.NXPEASAlarmEventData.getEASAlarmCode());
           myLogger.println("<br> antenna ID "+ rfidStatusEvents.StatusEventData.NXPEASAlarmEventData.getantennaID());

//        System.out.print(" antenna ID:"+rfidStatusEvents.StatusEventData.AntennaEventData.getAntennaID()+
//                         "\tantenna event"+rfidStatusEvents.StatusEventData.AntennaEventData.getAntennaEvent());
    }

}
public class ManualTest extends Commonclass {
    int status;
    boolean bSuccess = false;
    public ManualTest()
    {
        
    try {
            mystreamLog = new FileOutputStream("JavaAPI_Manual_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_Manual_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            AppendText();
        }
    catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
    }

    public int TagNum = 0;

    public void Test_EASwait() {
        
        // Setting the antenna power to max
        AntennaPowersettings();

        System.out.print("NXP eas wait test cases");
        nxpEventsListener nxplistener = new nxpEventsListener(reader,psLog);
        reader.Events.addEventsListener(nxplistener);
        reader.Events.setTagReadEvent(true);
        reader.Events.setEASAlarmEvent(true);
        String tagId = "1234123412341234";
         //String tagId = "111122223333444455556666";
         AntennaInfo antInfoNXP = new AntennaInfo();
            short[] antList = new short[]{1};
            OPERATION_QUALIFIER[] opq = { OPERATION_QUALIFIER.NXP_EAS_SCAN};
            antInfoNXP.setAntennaID(antList);
            antInfoNXP.setAntennaOperationQualifier(opq);



        TagAccess tagaccess = new TagAccess();
        NXP nxp = new NXP(tagaccess);
        NXP.SetEASParams setEASParams = nxp.new SetEASParams();
        NXP.ReadProtectParams readProtectParams = nxp.new ReadProtectParams();
        NXP.ResetReadProtectParams resetReadProtectParams = nxp.new ResetReadProtectParams();

        //1. correct access password
        setEASParams.setAccessPassword(00000001);
        psLog.println("getAccessPassword: " + setEASParams.getAccessPassword());
        setEASParams.setEAS(true);
        psLog.println("<br>isEASSet: " + setEASParams.isEASSet());

        try {

            reader.Actions.TagAccess.NXP.SetEASWait(tagId, setEASParams, antInfoNXP);
            Thread.sleep(10000);
            psLog.println("Set EAS done");
            reader.Actions.TagAccess.NXP.performEASScan();
            Thread.sleep(10000);
            reader.Actions.TagAccess.NXP.stopEASScan();
             Thread.sleep(10000);
            readProtectParams.setAccessPassword(00000001);
            reader.Actions.TagAccess.NXP.readProtectWait(tagId, readProtectParams, antInfoNXP);
            psLog.println("<br>Set quiet done");
            Test_SimpleInventory();
            resetReadProtectParams.setAccessPassword(00000001);
            psLog.println("<br>resetReadProtectParams-getAccessPassword"+resetReadProtectParams.getAccessPassword());
            reader.Actions.TagAccess.NXP.resetReadProtectEvent(resetReadProtectParams, antInfoNXP);
            psLog.println("<br>Reset quiet done");

            Test_SimpleInventory();
        } 
       catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
        //2. Incorrect access password
        setEASParams.setAccessPassword(0xABABABAB);
        long access = setEASParams.getAccessPassword();
        //psLog.printf("==========%d",+access);
        psLog.println("getAccessPassword:"+ Integer.toHexString((int)setEASParams.getAccessPassword()));
        setEASParams.setEAS(true);
        try {

            reader.Actions.TagAccess.NXP.SetEASWait(tagId, setEASParams, antInfoNXP);
            Thread.sleep(10000);
            psLog.println("Set EAS done");


            Test_SimpleInventory();
        } 
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }

    }
    public void Test_EASevent() {

        // Setting the antenna power to max
        AntennaPowersettings();

        System.out.print("NXP eas event test cases");
        nxpEventsListener nxplistener = new nxpEventsListener(reader,psLog);
        reader.Events.addEventsListener(nxplistener);
        reader.Events.setTagReadEvent(true);
        reader.Events.setEASAlarmEvent(true);

         AntennaInfo antInfo = new AntennaInfo();
            short[] antList = new short[]{1};
            OPERATION_QUALIFIER[] opq = { OPERATION_QUALIFIER.NXP_EAS_SCAN};
            antInfo.setAntennaID(antList);

        TagAccess tagaccess = new TagAccess();
        NXP nxp = new NXP(tagaccess);
        NXP.SetEASParams setEASParams = nxp.new SetEASParams();
        NXP.ReadProtectParams readProtectParams = nxp.new ReadProtectParams();
        NXP.ResetReadProtectParams resetReadProtectParams = nxp.new ResetReadProtectParams();

        //1. correct access password
        setEASParams.setAccessPassword(0xABCDABCD);
        psLog.println("getAccessPassword: " + setEASParams.getAccessPassword());
        setEASParams.setEAS(true);
        psLog.println("<br>isEASSet: " + setEASParams.isEASSet());

        try {

            reader.Actions.TagAccess.NXP.setEASEvent(setEASParams, null, antInfo);
            Thread.sleep(10000);
            psLog.println("Set EAS done");
            reader.Actions.TagAccess.NXP.performEASScan();
            Thread.sleep(10000);
            reader.Actions.TagAccess.NXP.stopEASScan();
             Thread.sleep(10000);
            readProtectParams.setAccessPassword(0xABCDABCD);
            reader.Actions.TagAccess.NXP.readProtectEvent(readProtectParams, null, antInfo);
            psLog.println("<br>Set quiet done");
            Test_SimpleInventory();
            resetReadProtectParams.setAccessPassword(0xABCDABCD);
            psLog.println("<br>resetReadProtectParams-getAccessPassword"+resetReadProtectParams.getAccessPassword());
            reader.Actions.TagAccess.NXP.resetReadProtectEvent(resetReadProtectParams, antInfo);
            psLog.println("<br>Reset quiet done");

            Test_SimpleInventory();
        } 
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
        //2. Incorrect access password
        setEASParams.setAccessPassword(0xABABABAB);
        long access = setEASParams.getAccessPassword();
        //psLog.printf("==========%d",+access);
        psLog.println("getAccessPassword:"+ Integer.toHexString((int)setEASParams.getAccessPassword()));
        setEASParams.setEAS(true);
        try {

            reader.Actions.TagAccess.NXP.setEASEvent(setEASParams, null, antInfo);
            Thread.sleep(10000);
            psLog.println("Set EAS done");


            Test_SimpleInventory();
        } 
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }

    }
    public void Test_LockEvent()
    {
        psLog.println("<br>Inside Lock Event method");

        TagAccess tagaccess = new TagAccess();
        TagAccess.LockAccessParams lockParams = tagaccess.new LockAccessParams();
        lockParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_USER_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_READ_WRITE);
        
        lockParams.setAccessPassword(0x11112222);
        // lockParams.setAccessPassword(0x44445555);
        String tagId = "0222BBBBCCCCDDDDEEEEFFFF";
        // String tagId = "123456781234567812345678";
        // String tagId = "444455556666444455556666";
        // String tagId = "E2002849112211220990B321";
        psLog.println("<br>getUserMemoryIndex: " + lockParams.getUserMemoryIndex());
        psLog.print("<br>getAccessPassword: " + Integer.toHexString((int) lockParams.getAccessPassword()));

        try {
            // correct access password
            psLog.print("<br>Wrong AccessPassword: " );
            reader.Actions.TagAccess.lockEvent(lockParams, null, null);
            Thread.sleep(10000);
            psLog.println("<br><br>Lock success");
            lockParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_USER_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);
            reader.Actions.TagAccess.lockEvent(lockParams, null, null);
            Thread.sleep(10000);
            psLog.println("<br>UnLock success");
            //incorrect access password
            lockParams.setAccessPassword(0xABCDFFFF);
            psLog.print("<br>getAccessPassword: " + Integer.toHexString((int) lockParams.getAccessPassword()));
            reader.Actions.TagAccess.lockEvent(lockParams, null, null);
            psLog.println("<br>Lock success");
            lockParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_USER_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);


            reader.Actions.TagAccess.lockEvent(lockParams, null, null);
            psLog.println("<br>UnLock success");

        } 
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
    }
    public void Test_LockWait()
    {
        psLog.println("<br>Inside LockWait  method");

        TagAccess tagaccess = new TagAccess();
        LOCK_PRIVILEGE[] lockPrivilege = new LOCK_PRIVILEGE[5];
        TagAccess.LockAccessParams lockParams = tagaccess.new LockAccessParams();
        lockParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_USER_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_READ_WRITE);

        lockParams.setAccessPassword(0x11112222);
        // lockParams.setAccessPassword(0x44445555);
        //String tagId = "ABCDEFABCDEFABCDEFABCDEF";
         String tagId = "123456783333444455556666";
        // String tagId = "444455556666444455556666";
        // String tagId = "E2002849112211220990B321";
        psLog.println("<br>getUserMemoryIndex: " + lockParams.getUserMemoryIndex());
        psLog.print("<br>getAccessPassword: " + Integer.toHexString((int) lockParams.getAccessPassword()));

        try {
            // correct access password
            reader.Actions.TagAccess.lockWait(tagId, lockParams, null);
            Thread.sleep(10000);
            psLog.print("<br>Lock success");
            lockParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_USER_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);
            reader.Actions.TagAccess.lockWait(tagId, lockParams, null);
            Thread.sleep(10000);
            psLog.print("<br>UnLock success");
            //incorrect access password
            lockParams.setAccessPassword(0xABCDFFFF);
            reader.Actions.TagAccess.lockEvent(lockParams, null, null);
            psLog.print("<br>Lock success");
            lockPrivilege[lockParams.getUserMemoryIndex()] = LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK;
            reader.Actions.TagAccess.lockEvent(lockParams, null, null);
            psLog.print("<br>UnLock success");

        } 
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
    }

    public void Test_killWait()
    {
        psLog.println("<br><br>Inside kill Wait method:");
        TagAccess tagaccess = new TagAccess();
        TagAccess.KillAccessParams killParams = tagaccess.new KillAccessParams();
        killParams.setKillPassword(0x00000001);

        //String tagId = "444455556666444455556666";
        //String tagId = "FFFF0000AAAABBBBCCCCDDDD";
        String tagId = "12341234491502151000B2C6";


        psLog.print("getAccessPassword: " +  killParams.getKillPassword());
         //Integer.toHexString((int)
        try {
            reader.Actions.TagAccess.killWait(tagId, killParams, null);
            psLog.println("Kill success");
        } 
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
    }
     public void Test_killEvent()
    {
         psLog.println("<br><br>Inside kill Event method:");
        TagAccess tagaccess = new TagAccess();
        TagAccess.KillAccessParams killParams = tagaccess.new KillAccessParams();
        killParams.setKillPassword(0xFFFFFFFF);

                psLog.print("<br>getAccessPassword: " +  Integer.toHexString((int)killParams.getKillPassword()));
        
        try {
            reader.Actions.TagAccess.killEvent(killParams, null, null);
            Thread.sleep(15000);
            psLog.println("<br>Kill success");
        } 
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
    }
    public void Test_SimpleInventory() {

        try {
            reader.Actions.Inventory.perform();
            Thread.sleep(1000);
            reader.Actions.Inventory.stop();
            TagData tagdata[] = reader.Actions.getReadTags(10);
            
            if (tagdata != null) {
                TagNum = tagdata.length;
                for (int i = 0; i < tagdata.length; i++) {
                    //System.out.print(""+tagdata[i].getTagID());
                    psLog.print("<br>" + tagdata[i].getTagID());
                    //((DefaultTableModel)jTable1.getModel()).insertRow(0, new Object[]{tagdata[i].getTagID(),tagdata[i].getPC(),tagdata[i].getPeakRSSI(),count});

                }
            } else {
                psLog.print("<br>No tags read");
            }

        } 
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
    }
       public void Test_SoftwareUpdate() {
        int status;
        boolean bSuccess = false;
        SubNo = 0;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description:</b> Test_SoftwareUpdate");
        psLog.println("Inside the Reader Management Test_SoftwareUpdate Method:");
        status = RM_Login();
        SoftwareUpdateInfo sofupinfo = new SoftwareUpdateInfo();
        // SoftwareUpdate softupdate = new SoftwareUpdate();

        sofupinfo.setHostName("ftp://157.235.206.150/FTPRoot/FX7500/Build-fx7500_dev-1.1.41.0/");
        psLog.println("getHostName: " + sofupinfo.getHostName());
        sofupinfo.setUserName("rfid");
        psLog.println("getUserName: " + sofupinfo.getUserName());
        sofupinfo.setPassword("rfid");
        psLog.println("getPassword: " + sofupinfo.getPassword());
        try {
            readerMgt.getSoftwareUpdate().Update(sofupinfo);
            while (100 != readerMgt.getSoftwareUpdate().getUpdateStatus().getPercentage()) {
                psLog.println("Percentage Software Update:" + readerMgt.getSoftwareUpdate().getUpdateStatus().getPercentage());
                System.out.println("Percentage Software Update:" + readerMgt.getSoftwareUpdate().getUpdateStatus().getPercentage());
                Thread.sleep(3000);
            }
            psLog.println("Software Update success.");
        } 
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
        status = RM_Logout();
    } // Manual

    public void Test_restartingReader() {
        
        SubNo = 0;
       FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description:</b> Test_restartingReader");
        psLog.println("Inside the Reader Management Test_restartingReader Method:");
        status = RM_Login();
        try
        {
            readerMgt.restart();
        }
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
        status = RM_Logout();

    } //Manual
    
        public void Test_GetCableLossCompensation()    {
        int status = RM_Login();
        SubNo = 1;
        bSuccess = false;
        if (status == success)
           {  
            psLog.println("<br>Inside the Reader Management Test_GetCableLossCompensation Method:");
            try{
                for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++)
               {
                    FormTestID(TestNo, SubNo++, "RM");
                    psLog.println("<br><b>Description: </b>Test_GetCableLossCompensation for Antenna: "+i + "-To get the default values");
                    psLog.println("<br><b>Expected Result:</b>Gets Cable Loss Compensation Info for the given Antenna ID");
                    CableLossCompensation cableLossObj = new CableLossCompensation();
                    cableLossObj = readerMgt.getCableLossCompensation(i);
                    psLog.println("<br><b>Actual Result:</b> CableLossCompensation Class");
                    psLog.println("<br><b>Antenna ID:</b>" + cableLossObj.getAntennaID());
                    psLog.println("<br><b>cablelengthinfeet:</b>" + cableLossObj.getCableLengthInFeet());
                    psLog.println("<br><b>cablelossper100feet:</b>" + cableLossObj.getCableLossPer100Feet());
                    bSuccess = true;     
                    LogStatus( bSuccess, "Test_GetCableLossCompensation",i);
               }   //for loop  
                
              }
            catch(Exception e)
            {
                bSuccess = false;
                AnalyseException(psLog,e);
                //LogStatus( bSuccess, "Test_GetCableLossCompensation",i);
            }
           
                
            } //if(status == success)
            status = RM_Logout();
            TestNo++;
        } 
     
// SetCablelossCompensation test by setting for all antennas at a time.
 public void Test_SetCableLossCompensation(){
    int status = RM_Login();
    SubNo = 1;
    bSuccess = false;
    float[] CableLengthInFeet = {(float)5,(float)15,(float)5,(float)15,(float)5,(float)15,(float)5,(float)15};
    float[] CableLossPer100Feet= {(float)15,(float)30,(float)15,(float)30,(float)15,(float)30,(float)15,(float)30};
    CableLossCompensation cableLossCmpList[] = new CableLossCompensation[8];
    CableLossCompensation cableLossObj = new CableLossCompensation();
    if (status == success)
           {  
            psLog.println("<br>Inside the Reader Management Test_SetCableLossCompensation Method:");
            try{
                for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++)
               {
                    CableLossCompensation obj = new CableLossCompensation();
                    obj.setAntennaID(i);
                    obj.setCableLengthInFeet(CableLengthInFeet[i-1]);
                    obj.setCableLossPer100Feet(CableLossPer100Feet[i-1]);
                    cableLossCmpList[i-1] = obj;  
                    bSuccess = true;                    
               }
                readerMgt.setCableLossCompensation(cableLossCmpList);
                for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++)
               {
                if(bSuccess == true)
                    {
                        FormTestID(TestNo, SubNo++, "RM");
                        psLog.println("<br><b>Description: </b>Test_SetCableLossCompensation for Antenna: "+i);
                        psLog.println("<br><b>Expected Result:</b>Sets Cable Loss Compensation Info with CableLengthInFeet:" + CableLengthInFeet[i-1] + " and CableLossPer100Feet:" + CableLossPer100Feet[i-1]);
                        cableLossObj = readerMgt.getCableLossCompensation(i);
                        psLog.println("<br><b>Actual Result:</b> Get CableLossCompensation Class");
                        psLog.println("<br><b>Antenna ID:</b>" + cableLossObj.getAntennaID());
                        psLog.println("<br><b>cablelengthinfeet:</b>" + cableLossObj.getCableLengthInFeet());
                        psLog.println("<br><b>cablelossper100feet:</b>" + cableLossObj.getCableLossPer100Feet());
                        LogStatus( bSuccess, "Test_SetCableLossCompensation",i);
                    }
               }
                for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++)
               {
                   CableLossCompensation cablelosscompensation = new CableLossCompensation((int)i,(float)0,(float)0);
                    cableLossCmpList[i-1] = cablelosscompensation;  
//               }                
                 for(i=0;i<100;i++)
                        {
               //Thread.sleep(10000);
                      if(i!=0 && !(reader.isConnected()))
                      {
                        reader.reconnect();
                      }
                      if(i==0)
                {
//                    reader.connect();
                GetEventsListener getEvents = new GetEventsListener(reader.getHostName(),reader);
                reader.Events.addEventsListener(getEvents);
                reader.Events.setReaderDisconnectEvent(true);
                psLog.println("reader.Events.isReaderDisconnectEventSet()" + reader.Events.isReaderDisconnectEventSet());   
                if(i==0)readerMgt.setCableLossCompensation(cableLossCmpList); 
               }
                Thread.sleep(10000);
                //evReader.disconnect();
            }       
//                ReadEventlistener getEvents;
//                getEvents = new ReadEventlistener(reader, psLog);
//                try
//                    {
//                        boolean eventread = reader.Events.isReaderDisconnectEventSet();
//                    }
//catch(Exception e)
//        {
//            AnalyseException(psLog,e);
//        }
//                    if(eventread != false)
//                    {
//                 psLog.println("reader.Events.isReaderDisconnectEventSet()" + reader.Events.isReaderDisconnectEventSet());   
//                testConnect();
//                }
              }}
            catch(Exception e)
        {
            bSuccess = false;
            AnalyseException(psLog,e);
            //LogStatus( bSuccess, "Test_SetCableLossCompensation",i);
        }
               }//if(status == success)
            status = RM_Logout();    
            TestNo++;
            
}
 
// SetCablelossCompensation test by setting for each antenna one by one.
 public void Test_CableLossCompensation(){

    int status = RM_Login();
    float Compensation;
    short powerindex;
    SubNo = 1;
    bSuccess = false;
    
    CableLossCompensation cableLossCmpList[] = new CableLossCompensation[8];
    float[] CableLengthInFeet = {(float)5,(float)15,(float)5,(float)15,(float)5,(float)15,(float)5,(float)15};
    float[] CableLossPer100Feet= {(float)15,(float)10,(float)15,(float)10,(float)15,(float)10,(float)15,(float)10};
    if (status == success)
           {  
            psLog.println("<br>Inside the Reader Management Test_CableLossCompensation Method for each antenna:");
            try
            {
//               Thread.sleep(60000); 
//            Config.setTransmitPowerIndex((short) ((reader.ReaderCapabilities.getTransmitPowerLevelValues().length)-16));
             for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++)
               {         
//                    Antennas.AntennaRfConfig RfConfig = reader.Config.Antennas.getAntennaRfConfig(i);
//                  Antennas antenna = reader.Config.Antennas;   
//                   System.out.print("isConnected : "+reader.isConnected());

//                   Antennas.Config config = reader.Config.Antennas.getAntennaConfig(i);
//                    int arr[] = reader.ReaderCapabilities.getTransmitPowerLevelValues();
//                    psLog.println("<br>For Antenna: " + i + "PowerIndex before adding compensation: "+config.getTransmitPowerIndex()+ "Powerlevel:  " + arr[config.getTransmitPowerIndex()]);
                    CableLossCompensation cablelosscompensation = new CableLossCompensation((int)i,(float)CableLengthInFeet[i-1],(float)CableLossPer100Feet[i-1]);
                    cableLossCmpList[i-1] = cablelosscompensation;                  
                                     
                }
//                System.out.print("isConnected : "+reader.isConnected());
                readerMgt.setCableLossCompensation(cableLossCmpList);
                bSuccess = true; 
                System.out.print("isConnected : "+reader.isConnected());
                //               System.out.print("isConnected : "+reader.isConnected());
               ReadEventlistener getEvents;
                getEvents = new ReadEventlistener(reader, psLog);
                while(reader.Events.isReaderDisconnectEventSet())
                {    
                //testConnect(); //commented by aarthi
                }
                for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++)
                {
                 if(bSuccess == true)
                    {
                        Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(i);
                    int arr[] = reader.ReaderCapabilities.getTransmitPowerLevelValues();
                        Compensation = CableLengthInFeet[i-1]*CableLossPer100Feet[i-1]/10; 
                    powerindex = ((short)(Compensation+ Config.getTransmitPowerIndex()));
//psLog.println("<br> + ----------------" + Compensation + powerindex);
                    Config.setTransmitPowerIndex(powerindex);
                    reader.Config.Antennas.setAntennaConfig(i, Config);
                    
                        FormTestID(TestNo, SubNo++, "RM");
                        psLog.println("<br><b>Description: </b>Test_CableLossCompensation for Antenna: "+i);
                        psLog.println("<br><b>Expected Result:</b>Sets Cable Loss Compensation Info with CableLengthInFeet:" + CableLengthInFeet[i-1] + " and CableLossPer100Feet:" + CableLossPer100Feet[i-1]);
                        CableLossCompensation cablelosscompensation2 = readerMgt.getCableLossCompensation(i);
                        psLog.println("<br><b>Actual Result:</b> Get CableLossCompensation Class");
                        psLog.println("<br><b>Antenna ID:</b>" + cablelosscompensation2.getAntennaID());
                        psLog.println("<br><b>cablelengthinfeet:</b>" + cablelosscompensation2.getCableLengthInFeet());
                        psLog.println("<br><b>cablelossper100feet:</b>" + cablelosscompensation2.getCableLossPer100Feet());                        
                        psLog.println("<br>For Antenna: " + i + "PowerIndex after adding compensation: "+Config.getTransmitPowerIndex()+ "Powerlevel:  " + arr[Config.getTransmitPowerIndex()]);
                        LogStatus( bSuccess, "Test_CableLossCompensation",i);
                    }                 
               }
                for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++)
               {
                   CableLossCompensation cablelosscompensation = new CableLossCompensation((int)i,(float)0,(float)0);
                    cableLossCmpList[i-1] = cablelosscompensation;  
               }
                readerMgt.setCableLossCompensation(cableLossCmpList); 
            }
                    catch(Exception e)
        {
            bSuccess = false;
            //LogStatus( bSuccess, "Test_CableLossCompensation",i);
            AnalyseException(psLog,e);
        }
               }
               //if(status == success)     
   status = RM_Logout();  
   TestNo++;
   } 
//
 public void Test_RadioIdleTimeOut() {
        int status = RM_Login();
        SubNo = 0;
        boolean caught = false;
        int timeOut = 5000;
        //if logged in
        if (status == success)
        {
             System.out.print("Inside Test_RadioIdleTimeOut()");
             //readerMgt.turnOffRadiowhenIdle(timeOut);
             System.out.print("Radio Idle time out is" +timeOut);
             FormTestID(TestNo, SubNo++, "RM");
             if(timeOut == 0)
             {
                
                psLog.println("<br><b>Description: </b>Test_RadioIdleTimeOut - Gettng Radio Timeout");
                psLog.println("<br><b>Expected Result: </b>RadioIdleTimeOut should be 0");
                psLog.println("<br><b>Actual Result: </b>RadioIdleTimeOut is " +timeOut);
                psLog.println("<br>"+TestID + "ReaderManagement_Test_RadioIdleTimeOut:PASSED");
                psResult.println(""+TestID + "ReaderManagement_Test_RadioIdleTimeOut:PASSED \n");
                
               successCount++;
               
             }
             else
            {
                psLog.println("<br><b>Description: </b>Test_RadioIdleTimeOut - Gettng Radio Timeout");
                psLog.println("<br><b>Expected Result: </b>RadioIdleTimeOut should be 0");
                psLog.println("<br><b>Actual Result: </b>RadioIdleTimeOut is" +timeOut);
                psLog.println("<br>"+TestID+ "ReaderManagement_Test_RadioIdleTimeOut:FAILED");
                psResult.println(""+TestID + "ReaderManagement_Test_RadioIdleTimeOut:FAILED \n");
                failureCount++;
            }
//             
//              try
//            {
//                 
//                int count_1 = 0;
//                readerMgt.turnOffRadiowhenIdle(count_1);
//                 System.out.print("\n timeout " +readerMgt.getRadioIdleTimeout());
//                int c = 1;
//                FormTestID(TestNo, SubNo++, "RM");
//                for(int i=1;i<=20;i++)
//                {
//                   Thread.sleep(1000);
//                  System.out.print("Iteration ----  "+reader.Config.getRadioPowerState()+"\n");
//                   c++;
////                  while(c==20)
////                  {
////                      if(reader.Config.getRadioPowerState().toString().equalsIgnoreCase("off"))
////                      {
////                          psLog.println("<br><b>Description: </b>Test_RadioIdleTimeOut - Setting timeout to 20");
////                          psLog.println("<br><b>Expected Result: </b>RadioIdleTimeOut should be 20");
////                          psLog.println("<br><b>Actual Result: </b>RadioIdleTimeOut is " +timeOut);
////                          psLog.println("<br>"+TestID + "ReaderManagement_Test_RadioIdleTimeOut:PASSED");
////                          psResult.println(""+TestID + "ReaderManagement_Test_RadioIdleTimeOut:PASSED \n");
////                          successCount++;
////                      }
////                      else
////                      {
////                          psLog.println("<br><b>Description: </b>Test_RadioIdleTimeOut - Gettng Radio Timeout");
////                             psLog.println("<br><b>Expected Result: </b>RadioIdleTimeOut should be 0");
////                             psLog.println("<br><b>Actual Result: </b>RadioIdleTimeOut is" +timeOut);
////                             psLog.println("<br>"+TestID+ "ReaderManagement_Test_RadioIdleTimeOut:FAILED");
////                          psResult.println(""+TestID + "ReaderManagement_Test_RadioIdleTimeOut:FAILED \n");
////                             failureCount++;
////                      }
////                      
////                  }
//                }
//          }
//                    catch(Exception e)
//        {
//            AnalyseException(psLog,e);
//        }

             
            try
            {
                 timeOut = readerMgt.getRadioIdleTimeout(); 
                int count_1 = -20;
                readerMgt.turnOffRadiowhenIdle(count_1);
            }
                     catch(Exception e)
        {
            caught = true;
            AnalyseException(psLog,e);
        }
            if(caught)
             {
                FormTestID(TestNo, SubNo++, "RM");
                psLog.println("<br><b>Description: </b>Test_SetReadPoingStatus - Set Radio TimeOout to -20");
                psLog.println("<br><b>Expected Result: </b>RadioIdleTimeOut should throw an exception");
                psLog.println("<br><b>Actual Result: </b>RadioIdleTimeOut exception is" );
                psLog.println("<br>"+TestID + "ReaderManagement_Test_RadioIdleTimeOut:PASSED");
                psResult.println(""+TestID + "ReaderManagement_Test_RadioIdleTimeOut:PASSED \n");
               successCount++;
               
             }
             else
            {
                psLog.println("<br><b>Description: </b>Test_SetReadPoingStatus");
                psLog.println("<br><b>Expected Result: </b>RadioIdleTimeOut should be 0");
                psLog.println("<br><b>Actual Result: </b>RadioIdleTimeOut is" +timeOut);
                psLog.println("<br>"+TestID +"ReaderManagement_Test_RadioIdleTimeOut:FAILED");
                psResult.println(""+TestID +"ReaderManagement_Test_RadioIdleTimeOut:FAILED \n");
                failureCount++;
            }
            
            
            try 
            {
                int count_1 = 0;
                readerMgt.turnOffRadiowhenIdle(count_1);
                timeOut = readerMgt.getRadioIdleTimeout();
             System.out.print("Radio Idle time out is" +timeOut);
             FormTestID(TestNo, SubNo++, "RM");
             if(timeOut == 0)
             {
                
                psLog.println("<br><b>Description: </b>Test_SetReadPoingStatus");
                psLog.println("<br><b>Expected Result: </b>RadioIdleTimeOut should be 0");
                psLog.println("<br><b>Actual Result: </b>RadioIdleTimeOut is" +timeOut);
                psLog.println("<br>"+TestID +"ReaderManagement_Test_RadioIdleTimeOut:PASSED");
                psResult.println(""+TestID +"ReaderManagement_Test_RadioIdleTimeOut:PASSED \n");
               successCount++;
               
             }
             else
            {
                psLog.println("<br><b>Description: </b>Test_SetReadPoingStatus");
                psLog.println("<br><b>Expected Result: </b>RadioIdleTimeOut should be 0");
                psLog.println("<br><b>Actual Result: </b>RadioIdleTimeOut is" +timeOut);
                psLog.println("<br>"+TestID +"ReaderManagement_Test_RadioIdleTimeOut:FAILED");
                psResult.println(""+TestID +"ReaderManagement_Test_RadioIdleTimeOut:FAILED \n");
                failureCount++;
            }
            }
        catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
             
             status = RM_Logout();
        }
        
        }      
// 
 public void Test_UserApp() {
   System.out.print("User App Installation testcase started...");  
   
   try
   {
      
   //Declaring the reader login details
   int status = RM_Login();
   //if logged in
   if (status == success)
   {
////       UserAppInfo[] userappinfo = readerMgt.UserApp.list();
////       for(short i=0;i<userappinfo.length;i++)
////    {
////    System.out.print("\nUser Appliaction RunStatus: "+ userappinfo[i].getRunStatus());
////    }
       
    
       // System.out.print("\nUser Appliaction RunStatus from readerMgt: "+ readerMgt.UserApp.getRunStatus("package-1"));
//readerMgt.UserApp.stop("package-1");
//readerMgt.UserApp.stop("testpackage");
//readerMgt.UserApp.stop("javatestpackage1");
//readerMgt.UserApp.stop("samplepackage");
//        readerMgt.UserApp.uninstall("package-1");
//        readerMgt.UserApp.uninstall("package-1");
//        readerMgt.UserApp.uninstall("leelapackage");
//       readerMgt.UserApp.uninstall("javatestpackage1");
// readerMgt.UserApp.uninstall("package-1");
        String currentDir = new StringBuilder().append(System.getProperty("user.dir")).append("\\package-1_3.6_all.deb").toString();
        readerMgt.UserApp.install(currentDir); //Autostart false
         currentDir = new StringBuilder().append(System.getProperty("user.dir")).append("\\samplepackage_1.2_all.deb").toString();
         readerMgt.UserApp.install(currentDir); //Autostart false
//         readerMgt.UserApp.install((String) "samplepackage",(String) currentDir,(boolean) true); //Autostart false
        currentDir = new StringBuilder().append(System.getProperty("user.dir")).append("\\javatestpackage1_1.2.0_all.deb").toString();
        readerMgt.UserApp.install(currentDir); //Autostart false
//        readerMgt.UserApp.install((String) "javatestpackage1_1.2.0_all",(String) currentDir,(boolean) true); //Autostart true
        currentDir = new StringBuilder().append(System.getProperty("user.dir")).append("\\testpackage_1.2.7_all.deb").toString();
        readerMgt.UserApp.install(currentDir); //Autostart false
//        readerMgt.UserApp.install((String) "testpackage_1.2.7_all",(String) currentDir,(boolean) true); //Autostart true  
//        
        
//        System.out.println("Current dir using System:" +currentDir);
//        readerMgt.UserApp.install((String) "javatestpackage1_1.2.0_all",(String) currentDir,(boolean) false); //Autostart false
          
        //
        
        UserAppInfo[] userappinfo = readerMgt.UserApp.list();
//          System.out.print("\nUser Appliaction RunStatus: "+ userappinfo[0].getRunStatus());
//        System.out.print("\nUser Appliaction RunStatus from readerMgt: "+ readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
//        System.out.print("\nUser Appliaction RunStatus: "+ userappinfo[1].getRunStatus());
//        System.out.print("\nUser Appliaction RunStatus from readerMgt: "+ readerMgt.UserApp.getRunStatus(userappinfo[1].getAppName()));

//        readerMgt.UserApp.install((String) "javatestpackage1_1.2.0_all",(String) "C:\\javatestpackage1_1.2.0_all.deb",(boolean) true); //Wrongpath
//        readerMgt.UserApp.install((String) "leela",(String) currentDir,(boolean) true); //Wrong Package name.
        //readerMgt.UserApp.autoStart("javatestpackage", true);

//        readerMgt.UserApp.uninstall("package-1");
//        readerMgt.UserApp.uninstall("testpackage");
//        readerMgt.UserApp.uninstall("samplepackage");
//        readerMgt.UserApp.uninstall("leelapackage");
//          readerMgt.UserApp.uninstall("javatestpackage1");
       
//        System.out.print("\nHow Many Apps installed: "+ userappinfo.length);
//           for(i=0;i<userappinfo.length;i++)
//    {
//       
//        readerMgt.UserApp.start(userappinfo[i].getAppName());
////        readerMgt.UserApp.start("leela1_1.2.0_all");
//        boolean run = userappinfo[i].getRunStatus();
//        System.out.println("" + "userappinfo[i].getRunStatus()" + run);
//        readerMgt.UserApp.stop(userappinfo[i].getAppName());
////        readerMgt.UserApp.stop("leela1_1.2.0_all");
//    }
//        readerMgt.UserApp.uninstall("javatestpackage");
//        readerMgt.UserApp.uninstall("samplepackage");
//        readerMgt.UserApp.uninstall("package-1");
//        readerMgt.UserApp.uninstall("testpackage");
        //readerMgt.UserApp.install((String) "javatestpackage",(String) currentDir,(boolean) true);
//        reader.disconnect();
//    Thread.sleep(5000);
//    reader.connect();
    
    

    for(short i=0;i<userappinfo.length;i++)
    {
        readerMgt.UserApp.start(userappinfo[i].getAppName());
        Thread.sleep(2000);
        System.out.print("\nUser Appliaction Number: "+ i);
        System.out.print("\nUser Appliaction Name: "+ userappinfo[i].getAppName());
        System.out.print("\nUser Appliaction MetaData: "+ userappinfo[i].getMetaData());
        System.out.print("\nUser Appliaction AutoStart: "+ userappinfo[i].getAutoStart());
//        readerMgt.UserApp.start("package-1");
//        readerMgt.UserApp.stop(userappinfo[i].getAppName());
//        readerMgt.UserApp.start(userappinfo[i].getAppName());
//        Thread.sleep(5000);
//        boolean run = userappinfo[i].getRunStatus();
//        
//        System.out.println("" + "userappinfo[i].getRunStatus()" + run);
         UserAppInfo[] myuserappinfo = readerMgt.UserApp.list();
         System.out.print("\nMy User Appliaction RunStatus: "+ myuserappinfo[i].getRunStatus());
        System.out.print("\nUser Appliaction RunStatus: "+ userappinfo[i].getRunStatus());
        System.out.print("\nUser Appliaction RunStatus from readerMgt: "+ readerMgt.UserApp.getRunStatus(userappinfo[i].getAppName()));
        readerMgt.UserApp.stop(userappinfo[i].getAppName());
        Thread.sleep(2000);
        
        System.out.print("\nUser Appliaction RunStatus: "+ userappinfo[i].getRunStatus());
        System.out.print("\nUser Appliaction RunStatus from readerMgt: "+ readerMgt.UserApp.getRunStatus(userappinfo[i].getAppName()));
       // readerMgt.UserApp.uninstall(userappinfo[i].getAppName());
//        readerMgt.UserApp.stop(userappinfo[i].getAppName());
    }
//    readerMgt.UserApp.uninstall("package-1");
//        if((userappinfo[i].getAppName().equalsIgnoreCase("package")) && ((userappinfo[i].getAutoStart()) == false) && ((userappinfo[i].getRunStatus()) == false) && (userappinfo[i].getRunStatus()) == (readerMgt.UserApp.getRunStatus("package")))
////            if((userappinfo[i].getAppName().equalsIgnoreCase("package")) && ((userappinfo[i].getAutoStart()) == false) && ((userappinfo[i].getRunStatus()) == false))
//                 System.out.println("Application Install Passed");
////          if((userappinfo[i].getRunStatus()) == (readerMgt.UserApp.getRunStatus("javatestpackage1"))) 
////                  {
////                  System.out.println("1");
////                  if((userappinfo[i].getAutoStart()) == true) 
////                  {
////                      System.out.println("2");
////                      if((userappinfo[i].getRunStatus()) == false) 
////                      {    System.out.println("3");
////                          if(userappinfo[i].getAppName().equalsIgnoreCase("javatestpackage1"))
////                          {
////                          System.out.println("4");
////                          System.out.println("Application Install Passed");
////                          }}}}
//        else
//          {
//                System.out.println(""+userappinfo[i].getAppName().toString());
//                System.out.println(""+userappinfo[i].getAutoStart());
//                System.out.println("" + userappinfo[i].getRunStatus());
//                System.out.println("" + readerMgt.UserApp.getRunStatus("package"));
//                System.out.println("Application Install Failed");
//          }
      
      }
   
   }
                     catch(Exception e)
        {
            AnalyseException(psLog,e);
        }
 }    
    
 public void Test_RadioFirmwareUpdate() {
        
        int status;bSuccess = false;
        SubNo = 0;
//        TestNo = 162;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description:</b> Test_RadioFirmwareUpdate");
        psLog.println("Inside the Reader Management Test_RadioFirmwareUpdate Method:");
        status = RM_Login();
        try {
            RadioFirmwareUpdate radioFirmUpd = readerMgt.getRadioFirmwareUpdate();
            radioFirmUpd.update("D:/Projects/JavaEMDK/sailfish.a79");
            while (100 != radioFirmUpd.getUpdateStatus().getPercentage()) {
                psLog.println("Percentage Firmware Update:" + radioFirmUpd.getUpdateStatus().getPercentage());

            }
            bSuccess = false;
            psLog.println("Firmware Update success.");
        } 
        catch(Exception e)
        {
            bSuccess = false;
            AnalyseException(psLog,e);
        }
       // LogStatus( bSuccess, "setReaderLocalTime",i);
        status = RM_Logout();
    }
 
 public void Test_OEMconfigUpdate() {
        int status;
        boolean bSuccess = false;
        SubNo = 0;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description:</b> Test_OEMconfigUpdate");
        psLog.println("Inside the Reader Management Test_OEMconfigUpdate Method:");
        status = RM_Login();
        try {
            RadioConfigUpdate radioConfigUpd = readerMgt.getRadioConfigUpdate();
            radioConfigUpd.update("D:/Projects/JavaEMDK/sailfish.a79");
            while (100 != radioConfigUpd.getUpdateStatus().getPercentage()) {
                psLog.println("Percentage Firmware Update:" + radioConfigUpd.getUpdateStatus().getPercentage());

            }
            psLog.println("OEM config Update success.");
            bSuccess = false;
        } 
                catch(Exception e)
        {
            bSuccess = false;
            AnalyseException(psLog,e);
        }
       // LogStatus( bSuccess, "OEMconfigUpdate",i);
        status = RM_Logout();
    }
 
 public void Test_SaveLlrpConfig() {
        int status;
        boolean bSuccess = false;
        System.out.println("Test Save LLRP Config");
        try {
            //Antenna Power
            Antennas.Config config = reader.Config.Antennas.getAntennaConfig(1);
            int arr[] = reader.ReaderCapabilities.getTransmitPowerLevelValues();
            System.out.println("<br>For Antenna: " + 1 + "PowerIndex before adding compensation: " + config.getTransmitPowerIndex() + "Powerlevel:  " + arr[config.getTransmitPowerIndex()]);
            config.setTransmitPowerIndex((short)150);
            reader.Config.Antennas.setAntennaConfig(1, config);
            
            //Antenna RF Mode
            Antennas.RFMode antennaRFMode = reader.Config.Antennas.getRFMode(1);
            System.out.println(" GetTableIndex: " + antennaRFMode.getTableIndex() + "Tari : " + antennaRFMode.getTari());
            antennaRFMode.setTableIndex(1);
            antennaRFMode.setTari(6250);
            reader.Config.Antennas.setRFMode(1, antennaRFMode);
            
            //Antenna Singulation
            Antennas.SingulationControl singControl = reader.Config.Antennas.getSingulationControl(1);
            System.out.println("getTagPopulation : " + singControl.getTagPopulation() + "\ngetSession : " + 
                    singControl.getSession() + "\ngetTagTransitTime : " + singControl.getTagTransitTime());
            
            singControl.setSession(SESSION.SESSION_S3);
            singControl.setTagPopulation((short)420);
            singControl.setTagTransitTime((short)840);
            reader.Config.Antennas.setSingulationControl(1, singControl);
            
            //GPIO Settings
            reader.Config.GPI.isPortEnabled(4);
            System.out.println("GPI Port state 4 : " + reader.Config.GPI.isPortEnabled(4));
            
            reader.Config.GPI.enablePort(4, false);
            
            System.out.println("GPO Port state 2 : " + reader.Config.GPO.getPortState(3));
            reader.Config.GPO.setPortState(2, GPO_PORT_STATE.TRUE);
            
            boolean saveCfgStatus = reader.Config.getSaveLlrpConfigStatus();
            System.out.println("Save Config Status : " + saveCfgStatus);
            //reader.Config.resetFactoryDefaults();
            reader.Config.saveLlrpConfig();
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
    }
 
 public void Test_CableLossCompensationNegative()
    {
        
        psLog.println("<br> Inside Test_CableLossCompensationNegative method");
        psLog.println("<br>=================================================");
       
        RdrManagement rm = new RdrManagement();
        int status = rm.RM_Login();
        
//        //1.  Invalid Antenna ID.(-ve values) by using CableLossCompensation Methods
//        FormTestID(TestNo, SubNo++, "NEG");
//        psLog.println("<br><b>Description:</b> CableLossCompensation with Negativevalue as Antenna ID");
//        psLog.println("<br>Expected Result: Unable to Set the Antenna ID");
//        psLog.print("<br>Calling SetCableLossCompensation Method");
//        psLog.println("<br>Actual Result:");        
//        if (status == success)
//           {  
////            try {
                CableLossCompensation obj = new CableLossCompensation();
//                obj.setAntennaID(-20);
//                 psLog.println("<br><b>Antenna ID:</b>" + obj.getAntennaID());
//                if(obj.getAntennaID() == -20)
//                       {
//                          
//                           psResult.println(TestID+": CableLossCompensation with Negativevalue as Antenna ID:FAILED");
//                           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as Antenna ID:FAILED");
//                           failureCount++;
//                       }
//                else
//                {                          
//           psResult.println(TestID+": CableLossCompensation with Negativevalue as Antenna ID:PASSED");
//           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as Antenna ID:PASSED");
//           successCount++;
//              }
            
           
           //2. Invalid CableLossPer100Feet.(-ve values) by using CableLossCompensation Methods
//           FormTestID(TestNo, SubNo++, "NEG");
//           psLog.println("<br><b>Description:</b> CableLossCompensation with Negative value as CableLossPer100Feet");
//           psLog.println("<br>Expected Result: Unable to Set the CableLossPer100Feet");
//           psLog.print("<br>Calling SetCableLossCompensation Method");
//           psLog.println("<br>Actual Result:");
//           obj.setCableLengthInFeet(-20);
//           psLog.println("<br><b>CableLossPer100Feet:</b>" + obj.getCableLossPer100Feet());
//           if(obj.getCableLossPer100Feet() == -20)
//                       {                        
//                           psResult.println(TestID+": CableLossCompensation with Negativevalue as CableLossPer100Feet:FAILED");
//                           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as CableLossPer100Feet:FAILED");
//                           failureCount++;
//                       } 
//           else
//           {
//           psResult.println(TestID+": CableLossCompensation with Negativevalue as CableLossPer100Feet:PASSED");
//           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as CableLossPer100Feet:PASSED");
//           successCount++;
//              }
//           
//           //3. Invalid cablelengthinfeet.(-ve values)by using CableLossCompensation Methods.
//           FormTestID(TestNo, SubNo++, "NEG");
//           psLog.println("<br><b>Description:</b> CableLossCompensation with Negative value as CableLengthInFeet");
//           psLog.println("<br>Expected Result: Unable to Set the CableLengthInFeet");
//           psLog.print("<br>Calling SetCableLossCompensation Method");
//           psLog.println("<br>Actual Result:");
//           obj.setCableLengthInFeet(-20);
//           psLog.println("<br><b>CableLengthInFeet:</b>" + obj.getCableLengthInFeet());
//           if(obj.getCableLengthInFeet() == -20)
//                       {
//                           psResult.println(TestID+": CableLossCompensation with Negativevalue as CableLengthInFeet:FAILED");
//                           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as CableLengthInFeet:FAILED");
//                           failureCount++;
//                       } 
//           else
//           {
//           psResult.println(TestID+": CableLossCompensation with Negativevalue as CableLengthInFeet:PASSED");
//           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as CableLengthInFeet:PASSED");
//           successCount++;
//              }
           
           //4.  Invalid Antenna ID.(-ve values) by using CableLossCompensation Class
//           FormTestID(TestNo, SubNo++, "NEG");
//           psLog.println("<br><b>Description:</b> CableLossCompensation with Negativevalue as Antenna ID");
//           psLog.println("<br>Expected Result: Unable to Set the Antenna ID");
//           psLog.print("<br>Calling SetCableLossCompensation Method");
//           psLog.println("<br>Actual Result:");
           CableLossCompensation cablelosscompensation = new CableLossCompensation(-30,(float)15.00,(float)0.11);
//           psLog.println("<br><b>Antenna ID:</b>" + cablelosscompensation.getAntennaID());
//           if(cablelosscompensation.getAntennaID() == -30)
//                       {
//                           psResult.println(TestID+": CableLossCompensation with Negativevalue as Antenna ID:FAILED");
//                           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as Antenna ID:FAILED");
//                           failureCount++;
//                       }
//                else
//                {                          
//           psResult.println(TestID+": CableLossCompensation with Negativevalue as Antenna ID:PASSED");
//           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as Antenna ID:PASSED");
//           successCount++;
//              }
            
           
           //5. Invalid CableLossPer100Feet.(-ve values) by using CableLossCompensation class
//           FormTestID(TestNo, SubNo++, "NEG");
//           psLog.println("<br><b>Description:</b> CableLossCompensation with Negative value as CableLossPer100Feet");
//           psLog.println("<br>Expected Result: Unable to Set the CableLossPer100Feet");
//           psLog.print("<br>Calling SetCableLossCompensation Method");
//           psLog.println("<br>Actual Result:");
//           cablelosscompensation = new CableLossCompensation(2,(float)-15.00,(float)0.11);
//           psLog.println("<br><b>CableLossPer100Feet:</b>" + cablelosscompensation.getCableLengthInFeet());
//           if(cablelosscompensation.getCableLengthInFeet() == -15)
//                       {
//                           
//                           psResult.println(TestID+": CableLossCompensation with Negativevalue as CableLossPer100Feet:FAILED");
//                           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as CableLossPer100Feet:FAILED");
//                           failureCount++;
//                       } 
//           else
//           {
//           psResult.println(TestID+": CableLossCompensation with Negativevalue as CableLossPer100Feet:PASSED");
//           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as CableLossPer100Feet:PASSED");
//           successCount++;
//              }
//           
//           //6. Invalid cablelengthinfeet.(-ve values)by using CableLossCompensation class.
//           FormTestID(TestNo, SubNo++, "NEG");
//           psLog.println("<br><b>Description:</b> CableLossCompensation with Negative value as CableLengthInFeet");
//           psLog.println("<br>Expected Result: Unable to Set the CableLengthInFeet");
//           psLog.print("<br>Calling SetCableLossCompensation Method");
//           psLog.println("<br>Actual Result:");
//           cablelosscompensation = new CableLossCompensation(2,(float)15.00,(float)-0.15);
//           psLog.println("<br><b>CableLengthInFeet:</b>" + cablelosscompensation.getCableLossPer100Feet());
//           if(cablelosscompensation.getCableLossPer100Feet() == (float)(-0.15))
//                       {
//                           
//                           psResult.println(TestID+": CableLossCompensation with Negativevalue as CableLengthInFeet:FAILED");
//                           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as CableLengthInFeet:FAILED");
//                           failureCount++;
//                       } 
//           else
//           {
//           psResult.println(TestID+": CableLossCompensation with Negativevalue as CableLengthInFeet:PASSED");
//           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as CableLengthInFeet:PASSED");
//           successCount++;
//              }
           
            //7. Invalid Antenna ID.(=9) by using CableLossCompensation Methods
           FormTestID(TestNo, SubNo++, "NEG");
           psLog.println("<br><b>Description:</b> CableLossCompensation with Antenna ID=9");
           psLog.println("<br>Expected Result: Unable to Set the CableLossPer100Feet");
           psLog.print("<br>Calling SetCableLossCompensation Method");
           psLog.println("<br>Actual Result:");
           status = rm.RM_Login();
           if(status == success){
           try
            {
           cablelosscompensation = new CableLossCompensation(9,(float)55.00,(float)5.55);
           CableLossCompensation cableLossCmpList[] = new CableLossCompensation[1];
           cableLossCmpList[0] = cablelosscompensation;                             
           readerMgt.setCableLossCompensation(cableLossCmpList);
            }
            catch(Exception e)
            {
                AnalyseException(psLog,e);
            }
 }
           Test_GetCableLossCompensation();
           psLog.println("<br><b>Antenna ID:</b>" + obj.getAntennaID());
           if(obj.getAntennaID() == 9)
                       {
                           
                           psResult.println(TestID+": CableLossCompensation with Negativevalue as Antenna ID:FAILED");
                           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as Antenna ID:FAILED");
                           failureCount++;
                       } 
           else
           {
           psResult.println(TestID+": CableLossCompensation with Negativevalue as Antenna ID:PASSED");
           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as Antenna ID:PASSED");
           successCount++;
              }
//           
//           //8. Invalid Antenna ID.(=100) by using CableLossCompensation Methods
//           FormTestID(TestNo, SubNo++, "NEG");
//           psLog.println("<br><b>Description:</b> CableLossCompensation with Antenna ID=100");
//           psLog.println("<br>Expected Result: Unable to Set the Antenna ID");
//           psLog.print("<br>Calling SetCableLossCompensation Method");
//           psLog.println("<br>Actual Result:");
//           obj.setAntennaID(100);
//           psLog.println("<br><b>Antenna ID:</b>" + obj.getAntennaID());
//           if(obj.getAntennaID() == 100)
//                       {
//                           
//                           psResult.println(TestID+": CableLossCompensation with Negativevalue as Antenna ID:FAILED");
//                           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as Antenna ID:FAILED");
//                           failureCount++;
//                       } 
//           else
//           {
//           psResult.println(TestID+": CableLossCompensation with Negativevalue as Antenna ID:PASSED");
//           psLog.println("<br>"+TestID+": CableLossCompensation with Negativevalue as Antenna ID:PASSED");
//           successCount++;
//              }
////            } catch (InvalidUsageException ex) {
////                Logger.getLogger(NegativeTest.class.getName()).log(Level.SEVERE, null, ex);
////            } catch (OperationFailureException ex) {
////                Logger.getLogger(NegativeTest.class.getName()).log(Level.SEVERE, null, ex);
////            }
//       
//    }
    }
 
}
