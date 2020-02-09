/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
import java.io.*;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.reader;
/**
 *
 * @author NVJ438
 */
class WriteEventlistener implements RfidEventsListener {

    int test;
    int count;
   
    public RFIDReader myReader = null;
    public PrintStream myLogger = null;
    public static TagData tagData=null;
     boolean  accessStop= false;
    ACCESS_OPERATION_STATUS status=ACCESS_OPERATION_STATUS.ACCESS_SUCCESS;
    public int accessSuccesscount=0;
    public  int accessFailurecount=0;
    
    //RFIDReader reader;
    public WriteEventlistener(RFIDReader reader,PrintStream psLog)
    {
        myReader = reader;
        myLogger = psLog;
    }
     public void eventReadNotify(RfidReadEvents rfidReadEvents)
     {
          myLogger.println("<br>READ EVENT Happened");

         if(rfidReadEvents!=null)
         {
             tagData  = rfidReadEvents.getReadEventData().tagData;
              myLogger.println("<br> tags ID :  "+tagData.getTagID()+"<br>membank  :"+tagData.getMemoryBank()+" memBank Data:  "+tagData.getMemoryBankData()+"<br>opCode  :"+tagData.getOpCode()
                    +"<br>OpCode Status  :"+tagData.getOpStatus());
              if(tagData.getOpStatus()==status)
              {accessSuccesscount++;}
              else{accessFailurecount++;}
             }

     }


     public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
        {
           myLogger.println("<br>"+rfidStatusEvents.StatusEventData.getStatusEventType());
           
           if((accessStop = myReader.Events.isAccessStopEventSet()))
            {
                System.out.println("access Stop : "+accessStop);
            }

}
}
public class TagStorage extends Commonclass
{
    public int len=0;
    TagAccess tagaccess=new TagAccess();
    TagAccess.ReadAccessParams readParams = tagaccess.new ReadAccessParams();
    public boolean status1 = true;
    public TagStorage()
    {

    }
    public void Test_TagStorageSettings()
    {
        try
        {
            mystreamLog=new FileOutputStream("JavaAPI_TagStorage_Log.html");
            mystreamResult=new FileOutputStream("JavaAPI_TagStorage_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            reader.Config.resetFactoryDefaults();
        }
        catch(FileNotFoundException e)
        {
            psLog.println(""+e.getMessage());
        }
        catch(InvalidUsageException e){
            psLog.println(""+e.getMessage());
        }
        catch(OperationFailureException e){
            psLog.println(""+e.getMessage());
        }
        int antennas=1;
        successCount = 0;  failureCount = 0;
        TestNo=285;        SubNo=0;
        
        TagStorageSettings tagStorageSettings=new TagStorageSettings();
        psLog.println("\nTag Storage test cases");
        //    //Max tag count
        int[] tagCount = {100,1000,2000,3000,4096,5000,10000};
        // TagStorageSettings tagStorageSettings=null;
        for (int i = 0; i < 6; i++)
        {
            FormTestID(TestNo++, SubNo, "CONFIG");
            psLog.println("<br><b>Description:</b>");
            psLog.println("Setting the tag count to " + tagCount[i] + " through TagStorageSettings constructor");
            tagStorageSettings = new TagStorageSettings(tagCount[i], 64, 12);
            psLog.println("<br>Succuessfully set");
            psLog.println("<br>Do simple inventroy");
            Test_SimpleInventory();
            psLog.println("<br>" + TestID + ":Tag count " + tagCount[i] + ":PASSED");
            psResult.println(TestID + ":Tag count " + tagCount[i] + ":PASSED");
            successCount++;
        }
        //setMaxMemoryBankByteCount
        //TestNo++;
        SubNo = 1;
        psLog.println("<br>---------------------------------------------------------------------------------------------------");
        int[] MemBankbyteCt = {8,12,24,64};
        for(int i=0; i<4; i++)//memory banks
        {

            for (int j = 2; j < MemBankbyteCt[i]; j = j * 2)//max memory bank count
            {
                tagStorageSettings.setMaxMemoryBankByteCount(j);
                // tagStorageSettings.setMaxTagIDLength(MemBankbyteCt[i]);
                try
                {
                    for (int k = 2; k <= MemBankbyteCt[i]; k = k + 2)
                    {
                        FormTestID(TestNo, SubNo++, "CONFIG");
                        psLog.println("<br><b>Description: </b>");
                        reader.Config.setTagStorageSettings(tagStorageSettings);
                        psLog.println("Setting the MaxMemoryBankByteCount " + j + " through TagStorageSettings");
                        psLog.println("<br><br>Getting the MaxMemoryBankByteCount to " + tagStorageSettings.getMaxMemoryBankByteCount());
                        psLog.println("<br>Succuessfully set");

                        readParams.setMemoryBank(MEMORY_BANK.GetMemoryBankValue(i));
                        MEMORY_BANK mem = readParams.getMemoryBank();
                        psLog.print("<br>Memory Bank " + readParams.getMemoryBank());

                        psLog.println("<br>Set the memory bank byte count to  " + k);
                        psLog.println("<br>Expected Result: Not more than " +j+" bytes should be read");

                        ReadMemoryBank(mem, 0, k);
                        if(len <= j)
                        {
                           psLog.println("<br>" + TestID + ":MaxMemoryBankByteCount " + j + ":PASSED");
                           psResult.println(TestID + ":MaxMemoryBankByteCount " + j + ":PASSED");
                           successCount++;
                        }
                        else
                        {
                           psLog.println("<br>" + TestID + ":MaxMemoryBankByteCount " + j + ":FAILED");
                           psResult.println(TestID + ":MaxMemoryBankByteCount " + j + ":FAILED");
                           failureCount++;
                        }
                               
                    }//k loop
                }
                catch (InvalidUsageException exp)
                {

                    psLog.println("<br>" + TestID + ":MaxMemoryBankByteCount " + j + ":FAILED");
                    psResult.println(TestID + ":MaxMemoryBankByteCount " + j + ":FAILED");
                    failureCount++;
                    psLog.println("<br>InvalidUsageException: " + exp.getInfo());
                    psLog.println("<br>getVendorMessage: " + exp.getVendorMessage());
                }
                catch (OperationFailureException exp)
                {
                    psLog.println("<br>" + TestID + ":MaxMemoryBankByteCount " + j + ":FAILED");
                    psResult.println( TestID + ":MaxMemoryBankByteCount " + j + ":FAILED");
                    failureCount++;
                    psLog.println("<br>nOperationFailureException: " + exp.getStatusDescription());
                    psLog.println("<br>getVendorMessage: " + exp.getVendorMessage());
                }
            }//j loop
            TestNo++;SubNo = 1;
        }// i loop


        SubNo = 1;
        //setMaxTagIDLength
        
        for(int j=2; j<12; j=j+2)//max memory bank count
        {
            
            psLog.println("<br><br>"+TestID);
            tagStorageSettings.setMaxTagIDLength(j);
            tagStorageSettings.enableAccessReports(true);
            // tagStorageSettings.setMaxTagIDLength(MemBankbyteCt[i]);
            try
            {
                
                reader.Config.setTagStorageSettings(tagStorageSettings);
                psLog.println("<br>Setting the setMaxTagIDLength "+j+ " through TagStorageSettings");
                psLog.println("<br>Getting the setMaxTagIDLength to "+tagStorageSettings.getMaxTagIDLength());
                psLog.println("<br>Succuessfully set");
                for(int k=2; k<12; k++)
                {
                    FormTestID(TestNo, SubNo++, "CONFIG");
                    ReadMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC, 0,k);
                    psLog.println("<br>"+TestID+":setMaxTagIDLength "+j+":PASSED");
                    psResult.println("<br>"+TestID+":setMaxTagIDLength "+j+":PASSED");
                    successCount++;
                }//k loop
                TestNo++; SubNo = 1;
            }
            catch(InvalidUsageException exp)
            {
                psLog.println("<br>"+TestID+":setMaxTagIDLength "+j+":FAILED");
                psResult.println("<br>"+TestID+":setMaxTagIDLength "+j+":FAILED");
                failureCount++;
                psLog.println("<br>InvalidUsageException: "+exp.getInfo());
                psLog.println("<br>getVendorMessage: "+exp.getVendorMessage());
            }
            catch(OperationFailureException exp)
            {
                psLog.println("<br>"+TestID+":setMaxTagIDLength "+j+":FAILED");
                psResult.println("<br>"+TestID+":setMaxTagIDLength "+j+":FAILED");
                failureCount++;
                psLog.println("<br>nOperationFailureException: "+exp.getStatusDescription());
                psLog.println("<br>getVendorMessage: "+exp.getVendorMessage());
            }
        }//j loop
        SubNo = 0;
        //discardTagsOnInventoryStop
        try
        {
             FormTestID(TestNo++, SubNo, "CONFIG");
            tagStorageSettings=reader.Config.getTagStorageSettings();
            tagStorageSettings.discardTagsOnInventoryStop(true);
            tagStorageSettings.setMaxTagCount(5);
            reader.Config.setTagStorageSettings(tagStorageSettings);
            tagStorageSettings=reader.Config.getTagStorageSettings();
            TriggerInfo tInfo = new TriggerInfo();
            tInfo.setTagReportTrigger(1);
            psLog.println("<br>isTagsOnInventoryStopDiscarded: "+tagStorageSettings.isTagsOnInventoryStopDiscarded());
            reader.Actions.Inventory.perform();

            Thread.sleep(1000);
            reader.Actions.Inventory.stop();
            TagData tagdata[]=reader.Actions.getReadTags(100);
            if (tagdata != null)
            {
                psLog.println("<br>"+TestID+":discardTagsOnInventoryStop "+":FAILED");
                psResult.println("<br>"+TestID+":discardTagsOnInventoryStop "+":FAILED");
                failureCount++;
            }
            else
            {
                psLog.println("<br>"+TestID+":discardTagsOnInventoryStop "+":PASSED");
                psResult.println("<br>"+TestID+":discardTagsOnInventoryStop "+":PASSED");
                successCount++;
            }
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+TestID+":discardTagsOnInventoryStop "+":FAILED");
            psResult.println("<br>"+TestID+":discardTagsOnInventoryStop "+":FAILED");
            failureCount++;
            psLog.println("<br>InvalidUsageException: "+exp.getInfo());
            psLog.println("<br>getVendorMessage: "+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>"+TestID+":discardTagsOnInventoryStop "+":FAILED");
            psResult.println("<br>"+TestID+":discardTagsOnInventoryStop "+":FAILED");
            failureCount++;
            psLog.println("<br>nOperationFailureException: "+exp.getStatusDescription());
            psLog.println("<br>getVendorMessage: "+exp.getVendorMessage());
        }
        catch(InterruptedException e)
        {
            //System.out.print("\nInterruptedException"+e.getMessage());
            psLog.println("\nInterruptedException"+e.getMessage());
        }
        //enableAccessReports
        try
        {
            FormTestID(TestNo++, SubNo, "CONFIG");
            tagStorageSettings=reader.Config.getTagStorageSettings();
            tagStorageSettings.enableAccessReports(false);
            reader.Config.setTagStorageSettings(tagStorageSettings);
            tagStorageSettings=reader.Config.getTagStorageSettings();
            psLog.println("<br>enableAccessReports: "+tagStorageSettings.isAccessReportsEnabled());
            ReadEvent(MEMORY_BANK.MEMORY_BANK_EPC, 4, 2, null);
            int[] pass = new int[1]; int[] fail = new int[1];
            reader.Actions.TagAccess.getLastAccessResult(pass, fail);
            psLog.println("\nPass "+pass[0]+" Fail: "+fail[0]);
            if(pass[0]>0 && fail[0]>=0)
            {
                psLog.println("<br>"+TestID+":enableAccessReports "+":PASSED");
                psResult.println("<br>"+TestID+":enableAccessReports "+":PASSED");
                successCount++;

            }
            else
            {
                psLog.println("<br>"+TestID+":enableAccessReports "+":FAILED");
                psResult.println("<br>"+TestID+":enableAccessReports "+":FAILED");
                failureCount++;
            }
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+TestID+":enableAccessReports "+":FAILED");
            psResult.println("<br>"+TestID+":enableAccessReports "+":FAILED");
            failureCount++;
            psLog.println("<br>enableAccessReports: "+exp.getInfo());
            psLog.println("<br>enableAccessReports: "+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>"+TestID+":enableAccessReports "+":FAILED");
            psResult.println("<br>"+TestID+":enableAccessReports "+":FAILED");
            failureCount++;
            psLog.println("<br>nOperationFailureException: "+exp.getStatusDescription());
            psLog.println("<br>getVendorMessage: "+exp.getVendorMessage());
        }
        
        //setTagFields
        SubNo = 0; 
        
        //Setting the array of tag fields one by one
        try
        {
            
            tagStorageSettings=reader.Config.getTagStorageSettings();
            TAG_FIELD[] arrTagFields = {TAG_FIELD.ANTENNA_ID,TAG_FIELD.CHANNEL_INDEX,TAG_FIELD.CRC,TAG_FIELD.FIRST_SEEN_TIME_STAMP,TAG_FIELD.LAST_SEEN_TIME_STAMP,TAG_FIELD.PC,TAG_FIELD.PEAK_RSSI,TAG_FIELD.PHASE_INFO,TAG_FIELD.TAG_SEEN_COUNT,TAG_FIELD.XPC};
            psLog.println("\nTag Storage array length" + arrTagFields.length);
//            tagStorageSettings.setTagFields(arrTagFields);
            //passing each TAG_FIELD to td
            for(TAG_FIELD td : arrTagFields)
            {
                FormTestID(TestNo++, SubNo, "CONFIG");
                psLog.println("<br><b>Description:</b> Setting the  "+td+" is the tag field and checking only that particular field is reported in the tag <br><br>");
                psLog.println("<br>");
                int g=0;
                //System.out.println(""+td+"is the tag field\n");
                tagStorageSettings.setTagFields(td);
                reader.Config.setTagStorageSettings(tagStorageSettings);
                //System.out.println("/nsuccessful/n");
                reader.Actions.purgeTags();
                reader.Actions.Inventory.perform();
                Thread.sleep(1000);
                reader.Actions.Inventory.stop();
                TagData[] tagdata = reader.Actions.getReadTags(3);
                ArryTagField(td, tagdata, g);
                
                //Validating the respected tag field is not null and the rest are 0 or null 
                //and displaying it is passed if it is true
                if((ValidateTagField(td, tagdata) == true) && (status1 = true))
                {
                    psResult.println(TestID + "setTagFields:" +td+ ":PASSED\n");
                    psLog.println(TestID + "setTagFields:" +td+ " :PASSED\n");
                    successCount++;
                } else
                {
                    psResult.println(TestID + "setTagFields:" +td+ " :FAILED\n");
                    psLog.println(TestID + "setTagFields:" +td+ " :FAILED\n");
                    failureCount++;
                     
                }
                
            }
            }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+TestID+":setTagFields "+":FAILED");
            psResult.println("<br>"+TestID+":setTagFields "+":FAILED");
            failureCount++;
            psLog.println("<br>setTagFields: "+exp.getInfo());
            psLog.println("<br>setTagFields: "+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>"+TestID+":setTagFields "+":FAILED");
            psResult.println("<br>"+TestID+":setTagFields "+":FAILED");
            failureCount++;
            psLog.println("<br>nOperationFailureException: "+exp.getStatusDescription());
            psLog.println("<br>getVendorMessage: "+exp.getVendorMessage());
        }
        catch(InterruptedException e)
        {
            //System.out.print("\nInterruptedException"+e.getMessage());
            psLog.println("\nInterruptedException"+e.getMessage());
        }
        
         //Setting the array of tag fields at the same time 
        try
        {
            tagStorageSettings=reader.Config.getTagStorageSettings();
            TAG_FIELD[] arrTagFields = {TAG_FIELD.ANTENNA_ID,TAG_FIELD.CHANNEL_INDEX,TAG_FIELD.CRC,TAG_FIELD.FIRST_SEEN_TIME_STAMP,TAG_FIELD.LAST_SEEN_TIME_STAMP,TAG_FIELD.PC,TAG_FIELD.PEAK_RSSI,TAG_FIELD.PHASE_INFO,TAG_FIELD.TAG_SEEN_COUNT,TAG_FIELD.XPC};
            
            psLog.println("\nTag Storage data -  array length" +arrTagFields.length);
            tagStorageSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            reader.Config.setTagStorageSettings(tagStorageSettings);
            reader.Actions.Inventory.perform();
            Thread.sleep(1000);
            reader.Actions.Inventory.stop();
            TagData[] tagdata = reader.Actions.getReadTags(10);
            
            for(TAG_FIELD td : arrTagFields)
            {
                
                FormTestID(TestNo++, SubNo, "CONFIG");
                psLog.println("<br><b>Description:</b> Setting the  "+td+" is the tag field and checking only that particular field is reported in the tag <br><br>");
                psLog.println("<br>");
                int g=0;
                ArryTagField(td, tagdata, g );
                
                //Validating the respected tag field is not null and the rest are 0 or null 
                //and displaying it is passed if it is true
                if((ValidateTagField(td, tagdata) == true) && (status1 = true))
                {
                 
                    psResult.println(TestID + "setTagFields:" +td+ ":PASSED\n");
                    psLog.println(TestID + "setTagFields:" +td+ " :PASSED\n");
                    successCount++;
                } 
                else
                {
                
                    psResult.println(TestID + "setTagFields:" +td+ " :FAILED\n");
                    psLog.println(TestID + "setTagFields:" +td+ " :FAILED\n");
                    failureCount++;
                     
                }
                
            }
            for(int i=0;i<tagdata.length;i++)
            {
                psLog.println("getTagID value is:"+tagdata[i].getTagID()+"<br>");        
                psLog.println("getAntennaID value is:"+tagdata[i].getAntennaID()+"<br>");
                psLog.println("getCRC value is:"+tagdata[i].getCRC()+"<br>");
                psLog.println("getChannelIndex value is:"+tagdata[i].getChannelIndex()+"<br>");
                psLog.println("getPC value is:"+tagdata[i].getPC()+"<br>");
                psLog.println("getPeakRSSI value is:"+tagdata[i].getPeakRSSI()+"<br>");
                psLog.println("getPhase value is:"+tagdata[i].getPhase()+"<br>");
                psLog.println("getTagSeenCount value is:"+tagdata[i].getTagSeenCount()+"<br>");
                psLog.println("getFirstSeenTimeStamp value is:"+tagdata[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek+"<br>");
                psLog.println("getLastSeenTimeStamp value is:"+tagdata[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek+"<br>");
                psLog.println("getXPC1 value is:"+tagdata[i].getXPC_W1()+"<br>");
                psLog.println("getXPC2 value is:"+tagdata[i].getXPC_W2()+"<br>");
                psLog.println("<br>");
            }
    
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+TestID+":setTagFields "+":FAILED");
            psResult.println("<br>"+TestID+":setTagFields "+":FAILED");
            failureCount++;
            psLog.println("<br>setTagFields: "+exp.getInfo());
            psLog.println("<br>setTagFields: "+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>"+TestID+":setTagFields "+":FAILED");
            psResult.println("<br>"+TestID+":setTagFields "+":FAILED");
            failureCount++;
            psLog.println("<br>nOperationFailureException: "+exp.getStatusDescription());
            psLog.println("<br>getVendorMessage: "+exp.getVendorMessage());
        }
        catch(InterruptedException e)
        {
            //System.out.print("\nInterruptedException"+e.getMessage());
            psLog.println("\nInterruptedException"+e.getMessage());
        }
        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:TagStorage testcases:" + successCount + ":" + failureCount + ":" + "0");

    }
    /**Checking weather the get tag field function value  
     * if there is some value returns TRUE
     * or else it returns FALSE
     
     */
    public void result(int g, TAG_FIELD tagfield )
    {
        if(tagfield == TAG_FIELD.XPC)
        {
            if(g == 0) 
            {
                status1 = true;

            }else
            {
                status1 = false;
            }
        }
        else
        {
            if(g > 0) 
            {
                status1 = true;

            }else
            {
                status1 = false;
            }
        }

    
    }
    /**
     * Checking for respected TAG_FIELD and for every tag calling the respected 
     * get function and checking weather the value is a value or null
     
     */
    public void ArryTagField(TAG_FIELD td, TagData[] tagdata, int g)
    {
        for(int i=0;i<tagdata.length;i++)
                {
                    if(td==TAG_FIELD.ANTENNA_ID)
                    {         
                        psLog.println("\n");
                        psLog.println("\n getTagID is:"+tagdata[i].getTagID()+"\n");                   
                        g = tagdata[i].getAntennaID();
                        psLog.println("getAntennaID is:"+tagdata[i].getAntennaID()+"\n");
                        psLog.println("<br><br>\n");
                        Display(tagdata[i]);
                        result(g, td);
                    }
                    else if(td==TAG_FIELD.CRC)
                    {
                        psLog.println("\ngetTagID is: "+tagdata[i].getTagID()+"\n");                  
                        g = tagdata[i].getCRC();
                        psLog.println("n getCRC is:"+tagdata[i].getCRC()+"\n");
                        psLog.println("<br><br>\n");
                        Display(tagdata[i]);
                        result(g, td);
                        
                    }
                    else if(td==TAG_FIELD.CHANNEL_INDEX)
                    {
                        psLog.println("\ngetTagID is: "+tagdata[i].getTagID()+"\n");                   
                        g = tagdata[i].getChannelIndex();
                        psLog.println("getChannelIndex is :"+tagdata[i].getChannelIndex()+"\n");
                        psLog.println("<br><br>\n");
                        Display(tagdata[i]);
                        result(g, td);
                        
                    }
                    else if(td==TAG_FIELD.FIRST_SEEN_TIME_STAMP)
                    {
                        psLog.println("\ngetTagID is: "+tagdata[i].getTagID()+"\n");                  
                        g = tagdata[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek;
                        psLog.println("getFirstSeenTimeStamp is:"+tagdata[i].SeenTime.getUTCTime().getFirstSeenTimeStamp()+"\n");
                        psLog.println("<br><br>\n");
                        Display(tagdata[i]);
                        result(g, td);
                        
                    }
                    else if(td==TAG_FIELD.LAST_SEEN_TIME_STAMP)
                    {
                        psLog.println("\ngetTagID is: "+tagdata[i].getTagID()+"\n");             
                        g = tagdata[i].SeenTime.getUTCTime().getLastSeenTimeStamp().Second;
                        psLog.println("getLastSeenTimeStamp is:"+tagdata[i].SeenTime.getUTCTime().getLastSeenTimeStamp()+"\n");
                        psLog.println("<br><br>\n");
                        Display(tagdata[i]);
                        result(g, td);
                        
                    }
                    else if(td==TAG_FIELD.PC)
                    {                        
                        psLog.println("\ngetTagID is:"+tagdata[i].getTagID()+"\n");                   
                        g = tagdata[i].getPC();
                        psLog.println("\n getPC is:"+tagdata[i].getPC()+"\n");
                        psLog.println("<br><br>\n");
                        Display(tagdata[i]);
                        result(g, td);
                        
                    }
                    else if(td==TAG_FIELD.PEAK_RSSI)
                    {                        
                        psLog.println("\ngetTagID is:"+tagdata[i].getTagID()+"\n");                   
                        g = tagdata[i].getPeakRSSI();
                        psLog.println("getPeakRSSI"+tagdata[i].getPeakRSSI()+"\n");
                        psLog.println("<br><br>\n");
                        Display(tagdata[i]);
                        result(g, td);
                        
                    }
                    else if(td==TAG_FIELD.PHASE_INFO)
                    {                        
                        psLog.println("\ngetTagID is:"+tagdata[i].getTagID()+"\n");                   
                        g = tagdata[i].getPhase();
                        psLog.println("getPhase is:"+tagdata[i].getPhase()+"\n");
                        psLog.println("<br><br>\n");
                        Display(tagdata[i]);
                        result(g, td);
                        
                    }
                    else if(td==TAG_FIELD.TAG_SEEN_COUNT)
                    {                        
                        psLog.println("\ngetTagID is:"+tagdata[i].getTagID()+"\n");                   
                        g = tagdata[i].getTagSeenCount();
                        psLog.println("getTagSeenCount is:"+tagdata[i].getTagSeenCount()+"\n");
                        psLog.println("<br><br>\n");
                        Display(tagdata[i]);
                        result(g, td);
                        
                    }
                    else if(td==TAG_FIELD.XPC)
                    {                        
                        psLog.println("\ngetTagID is: "+tagdata[i].getTagID()+"\n");                   
                        g = tagdata[i].getXPC_W1();
                        psLog.println("getXPC is:"+tagdata[i].getXPC_W1()+"\n");
                        psLog.println("<br><br>");
                        Display(tagdata[i]);
                        result(g, td);
                        
                    }
            
                }
    }
    
//    Displaying data in the Tag Fields for each tag
    public void Display(TagData tagData)
    {
        psLog.println("getTagID value is:"+tagData.getTagID()+"<br>");        
        psLog.println("getAntennaID value is:"+tagData.getAntennaID()+"<br>");
        psLog.println("getCRC value is:"+tagData.getCRC()+"<br>");
        psLog.println("getChannelIndex value is:"+tagData.getChannelIndex()+"<br>");
        psLog.println("getPC value is:"+tagData.getPC()+"<br>");
        psLog.println("getPeakRSSI value is:"+tagData.getPeakRSSI()+"<br>");
        psLog.println("getPhase value is:"+tagData.getPhase()+"<br>");
        psLog.println("getTagSeenCount value is:"+tagData.getTagSeenCount()+"<br>");
        psLog.println("getFirstSeenTimeStamp value is:"+tagData.SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek+"<br>");
        psLog.println("getLastSeenTimeStamp value is:"+tagData.SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek+"<br>");
        psLog.println("getXPC value is:"+tagData.getXPC_W1()+"<br");
        
    }

    /** Validates the Tag Field present in the Tag Data or not 
        Returns TRUE if tag field is correctly verified or returns FALSE
    **/
    public boolean ValidateTagField( TAG_FIELD tagfield, TagData[] tagData )
    {
        // Validating Antenna ID tag field
        if( tagfield == TAG_FIELD.ANTENNA_ID)
        {
            for(int i=0; i<tagData.length; i++)
            {
                SYSTEMTIME sy = tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp();
                psLog.print("<br>The first seen time is----------> "+sy);
                if( tagData[i].getAntennaID() == 0 &&( tagData[i].getCRC() != 0 || tagData[i].getChannelIndex() !=0 || tagData[i].getPC() != 0 || tagData[i].getPeakRSSI() != 0 || tagData[i].getPhase() != 0 || tagData[i].getXPC_W1() != 0 || tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek != 0 || tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek != 0 || tagData[i].getTagSeenCount() != 0 ) )
                {
                    return false;
                }
            }
        }
        //Validating CHANNEL_INDEX tag field
        else if( tagfield == TAG_FIELD.CHANNEL_INDEX)
        {
            for(int i=0; i<tagData.length; i++)
            {
                SYSTEMTIME sy = tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp();
                psLog.print("<br>The first seen time is----------> "+sy);
                if( tagData[i].getChannelIndex()== 0 &&( tagData[i].getCRC() != 0 || tagData[i].getAntennaID()!=0 || tagData[i].getPC() != 0 || tagData[i].getPeakRSSI() != 0 || tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek != 0 || tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek != 0 || tagData[i].getPhase() != 0 || tagData[i].getXPC_W1() != 0 || tagData[i].getTagSeenCount() != 0 ) )
                {
                    return false;
                }
            }
        }
        //Validating CRC tag field
        else if( tagfield == TAG_FIELD.CRC)
        {
            for(int i=0; i<tagData.length; i++)
            {
                psLog.print("<br>The first seen time is "+tagData[i].SeenTime.getUpTime().getFirstSeenTimeStamp());
                if( tagData[i].getCRC()== 0 &&( tagData[i].getAntennaID() != 0 || tagData[i].getChannelIndex() !=0 || tagData[i].getPC() != 0 ||tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek != 0 || tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek != 0|| tagData[i].getPeakRSSI() != 0 || tagData[i].getPhase() != 0 || tagData[i].getXPC_W1() != 0 || tagData[i].getTagSeenCount() != 0 ) )
                {
                    return false;
                }
            }
        }
        //Validating FIRST_SEEN_TIME_STAMP tag field
        else if( tagfield == TAG_FIELD.FIRST_SEEN_TIME_STAMP)
        {
            for(int i=0; i<tagData.length; i++)
            {
                
                psLog.print("<br>The first seen time is "+tagData[i].SeenTime.getUpTime().getFirstSeenTimeStamp());
                if( tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 0 &&( tagData[i].getCRC() != 0 || tagData[i].getChannelIndex() !=0 || tagData[i].getPC() != 0 || tagData[i].getPeakRSSI() != 0 || tagData[i].getPhase() != 0 || tagData[i].getXPC_W1() != 0 || tagData[i].getTagSeenCount() != 0 || tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek != 0 ) )
                {
                    return false;
                }
            }
        }
        //Validating LAST_SEEN_TIME_STAMP tag field
        else if( tagfield == TAG_FIELD.LAST_SEEN_TIME_STAMP)
        {
            for(int i=0; i<tagData.length; i++)
            {
                psLog.print("<br>The last seen time is "+tagData[i].SeenTime.getUpTime().getLastSeenTimeStamp());
                if( tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek == 0 &&( tagData[i].getCRC() != 0 || tagData[i].getChannelIndex() !=0 || tagData[i].getPC() != 0 || tagData[i].getPeakRSSI() != 0 || tagData[i].getPhase() != 0 || tagData[i].getXPC_W1() != 0 || tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek != 0 || tagData[i].getAntennaID() != 0 || tagData[i].getTagSeenCount() != 0 ) )
                {
                    return false;
                }
            }
        }
        //Validating PC tag field
        else if( tagfield == TAG_FIELD.PC)
        {
            for(int i=0; i<tagData.length; i++)
            {
                psLog.print("<br>The pc is "+tagData[i].getPC());
                if( tagData[i].getPC() == 0 &&( tagData[i].getCRC() != 0 || tagData[i].getChannelIndex() !=0 || tagData[i].getAntennaID() != 0 || tagData[i].getPeakRSSI() != 0 || tagData[i].getPhase() != 0 || tagData[i].getXPC_W1() != 0 || tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek != 0 || tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek != 0 || tagData[i].getTagSeenCount() != 0 ) )
                {
                    return false;
                }
            }
        }
        //Validating PEAK_RSSI tag field
        else if( tagfield == TAG_FIELD.PEAK_RSSI)
        {
            for(int i=0; i<tagData.length; i++)
            {
                psLog.print("<br>The peak RSSI is "+tagData[i].getPeakRSSI());
                if( tagData[i].getPeakRSSI() == 0 &&( tagData[i].getCRC() != 0 || tagData[i].getChannelIndex() !=0 || tagData[i].getPC() != 0 || tagData[i].getAntennaID() != 0 || tagData[i].getPhase() != 0 || tagData[i].getXPC_W1() != 0 || tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek != 0 || tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek != 0 || tagData[i].getTagSeenCount() != 0 ) )
                {
                    return false;
                }
            }
        }
        //Validating PHASE_INFO tag field
        else if( tagfield == TAG_FIELD.PHASE_INFO)
        {
            for(int i=0; i<tagData.length; i++)
            {
                psLog.print("<br>The phase info is "+tagData[i].getPhase());
                if( tagData[i].getPhase() == 0 &&( tagData[i].getCRC() != 0 || tagData[i].getChannelIndex() !=0 || tagData[i].getPC() != 0 || tagData[i].getPeakRSSI() != 0 || tagData[i].getAntennaID() != 0 || tagData[i].getXPC_W1() != 0 || tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek != 0 || tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek != 0 || tagData[i].getTagSeenCount() != 0 ) )
                {
                    return false;
                }
            }
        }
        //Validating TAG_SEEN_COUNT tag field
        else if( tagfield == TAG_FIELD.TAG_SEEN_COUNT)
        {
            for(int i=0; i<tagData.length; i++)
            {
                psLog.print("<br>The tag seen count is "+tagData[i].getTagSeenCount());
                if( tagData[i].getTagSeenCount() == 0 &&( tagData[i].getCRC() != 0 || tagData[i].getChannelIndex() !=0 || tagData[i].getPC() != 0 || tagData[i].getPeakRSSI() != 0 || tagData[i].getPhase() != 0 || tagData[i].getXPC_W1() != 0 || tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek != 0 || tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek != 0 || tagData[i].getAntennaID() != 0 ) )
                {
                    return false;
                }
            }
        }
        //Validating XPC tag field
        else if( tagfield == TAG_FIELD.XPC)
        {
            for(int i=0; i<tagData.length; i++)
            {
                psLog.print("<br>The XPC is "+tagData[i].getXPC_W1());
                if( tagData[i].getXPC_W1() == 0 &&( tagData[i].getCRC() != 0 || tagData[i].getChannelIndex() !=0 || tagData[i].getPC() != 0 || tagData[i].getPeakRSSI() != 0 || tagData[i].getPhase() != 0 || tagData[i].getAntennaID() != 0 || tagData[i].SeenTime.getUTCTime().getFirstSeenTimeStamp().DayOfWeek != 0 || tagData[i].SeenTime.getUTCTime().getLastSeenTimeStamp().DayOfWeek != 0 || tagData[i].getTagSeenCount() != 0 ) )
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    public void Test_SimpleInventory()
    {
        try
        {
            reader.Actions.Inventory.perform();
            Thread.sleep(2000);
            reader.Actions.Inventory.stop();
            TagData tagdata[]=reader.Actions.getReadTags(5000);
            if( tagdata == null)
            {
               psLog.print("<br>No.of Tags read:"+0); 
            }
            else
            {
                //
                psLog.print("<br>No.of Tags read:"+tagdata.length);
                for(int i=0;i<tagdata.length;i++)
                {
                    //System.out.print("\n"+tagdata[i].getTagID());
                   psLog.print("\n"+tagdata[i].getTagID());
                }
            }
        }
        catch(InvalidUsageException exp) 
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo());
            psLog.println("\nInvalidUsageException"+exp.getInfo());
        }
        catch(OperationFailureException exp)
        {
            System.out.print("\nOperationFailureException"+exp.getMessage());
            psLog.println("\nOperationFailureException"+exp.getMessage());
        }
        catch(InterruptedException e)
        {
            System.out.print("\nInterruptedException"+e.getMessage());
            psLog.println("\nInterruptedException"+e.getMessage());
        }
    }

    public void ReadMemoryBank(MEMORY_BANK mBank, int offset,int dataLen)
    {
        readParams.setAccessPassword(0);
        readParams.setByteCount(dataLen);
        readParams.setByteOffset(offset);
        readParams.setMemoryBank(mBank);
   
        try
        {
            TagData membankData = new TagData();
            
            membankData = reader.Actions.TagAccess.readWait(a22fTag, readParams, null);
            if(membankData.getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
            {
                psLog.println("<br> memBank data\t"+membankData.getMemoryBank()+"    "+membankData.getMemoryBankData()+"\tmemBank allocated  :"+
                        membankData.getMemoryBankDataAllocatedSize());
                len = (membankData.getMemoryBankData().length())/2;
                psLog.println("<br>Actual Result: "+len+" bytes are read");
            }

        }

        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
        }

    }
}
