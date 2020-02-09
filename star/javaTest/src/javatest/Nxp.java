package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import java.net.Socket;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.reader;

public class Nxp extends Commonclass {
     
    byte[] tagMask;
    byte[] tagPattern1;
    TagPatternBase tpA;
    TagPatternBase tpB;
    PostFilter postfilter;
    AccessFilter afilter;
    ReadEventlistener getEvents;
    
    NXP.ReadProtectParams setReadProtect;
    NXP.ResetReadProtectParams resetReadProtect;
    NXP.SetEASParams setEAS,resetEAS;
    ManualResetEvent accessComplete;
    AntennaInfo antennaInfo;
    TagData nxpG2ILTag;
    
        TagAccess tagaccess = new TagAccess();
        NXP nxp = new NXP(tagaccess);
    
//Constructor.    
public void Nxp() {
    byte[] tagMask = { (byte)0xFF, (byte)0xFF };
    byte[] tagPattern1 = { (byte)0xF0, (byte)0x0D};
    
//  byte[] tagPattern1 = { (byte)0xE2, (byte)0x00, (byte)0x60, (byte)0x03};
    nxpG2ILTag = new TagData();
    
     //tag Pattern A
        tpA.setBitOffset(0);
        tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_TID);
        tpA.setTagMask(tagMask);
        tpA.setTagMaskBitCount(16);
        tpA.setTagPattern(tagPattern1);
        tpA.setTagPatternBitCount(16);

        //tag pattern B
        tpB.setBitOffset(32);
        tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        tpB.setTagMask(tagMask);
        tpB.setTagMaskBitCount(16);
        tpB.setTagPattern(tagPattern1);
        tpB.setTagPatternBitCount(16);
        
        antInfo = new AntennaInfo();
        antList = new short[]{1,2};
        OPERATION_QUALIFIER[] opq = { OPERATION_QUALIFIER.C1G2_OPERATION,OPERATION_QUALIFIER.C1G2_OPERATION};
        antInfo.setAntennaID(antList);
        antInfo.setAntennaOperationQualifier(opq);
        antList = antInfo.getAntennaID();
        psLog.println("getAntennaID: " + antList);
        

        setEAS = nxp.new SetEASParams(0x00000001, true);
        resetEAS = nxp.new SetEASParams(0x00000001, false);
        accessComplete = new ManualResetEvent(false);
        resetReadProtect = nxp.new ResetReadProtectParams(0x00000001);
        setReadProtect = nxp.new ReadProtectParams(0x00000001);        
        
        //post filter
        postfilter.TagPatternA = tpA;
        postfilter.TagPatternB = tpB;
        postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A_AND_NOTB);
        postfilter.setRSSIRangeFilter(false);
        
        //access filter
        afilter.TagPatternA = tpA;
        afilter.TagPatternB = tpB;
        afilter.setAccessFilterMatchPattern(FILTER_MATCH_PATTERN.A_AND_NOTB);
        afilter.setRSSIRangeFilter(false);
        
}

//public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
//{
//    String getStatusEventType = rfidStatusEvents.StatusEventData.getStatusEventType().toString();
//    switch(getStatusEventType)
//    {
//        case "STATUS_EVENT_TYPE.ACCESS_START_EVENT":
//            System.out.print(" Access Start event: "+rfidStatusEvents.StatusEventData.AccessStartEventData);
//            accessComplete.reset();
//            break;
//        case "STATUS_EVENT_TYPE.ACCESS_STOP_EVENT":
//            System.out.print(" Access Stop event: "+rfidStatusEvents.StatusEventData.AccessStopEventData);
//            accessComplete.set();
//            break;   
//     }
//}

public void eventReadNotify(RfidReadEvents rfidReadEvents)
    {
            TagData tagData  = rfidReadEvents.getReadEventData().tagData;
            System.out.print("TagID: "+tagData.getTagID()+" "+"FirstSeenTimeStamp: "+tagData.SeenTime.getUTCTime().getFirstSeenTimeStamp().ConvertTimetoString()+" "+"TagSeenCount: "+tagData.getTagSeenCount()+" "+"LocationInfo: "+tagData.LocationInfo.getRelativeDistance());
        }
   
public void testNXPReadProtect(RFIDReader Reader)
{
    try
    {
        Reader.connect();
        TagStorageSettings tagStorageSettings = new TagStorageSettings();
        tagStorageSettings.enableAccessReports(true);
        tagStorageSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
        Reader.Config.setTagStorageSettings(tagStorageSettings);
        Reader.Events.setAccessStartEvent(true);
        
        //Set Read Protect
        System.out.print("set NXP tags with read protect");
        getEvents = new ReadEventlistener(reader, psLog);//-----------------------------
        reader.Events.addEventsListener(getEvents);//--------------------------------
        reader.Actions.TagAccess.NXP.setEASEvent(setEAS, null, null);
        Thread.sleep(15000);
        reader.Events.setEASAlarmEvent(true);//----------------------------------
        reader.Actions.TagAccess.NXP.readProtectEvent(setReadProtect, null, null);
        Thread.sleep(15000);
        reader.Events.addEventsListener(getEvents);
        
        System.out.print("Perform NXP scan after read protect,NXP should report EPC as ZERO");
        reader.Actions.Inventory.perform();
        Thread.sleep(5000);
        reader.Actions.Inventory.stop();
//        reader.Actions.TagAccess.NXP.performEASScan(null, antennaInfo);
//        Thread.sleep(5000);
//        reader.Actions.TagAccess.NXP.stopEASScan();
        
        //Reset Read Protect
        System.out.print("set NXP tags with RESET read protect");
        reader.Actions.TagAccess.NXP.resetReadProtectEvent(resetReadProtect, null);
        Thread.sleep(15000);
        System.out.print(" Do Regular Inventory after RESET read protect");
        reader.Actions.Inventory.perform();
        Thread.sleep(5000);
        reader.Actions.Inventory.stop();
        reader.Actions.TagAccess.NXP.setEASEvent(resetEAS, null, null);
        reader.Events.addEventsListener(getEvents);
        reader.disconnect();
    }   
    catch(Exception e)
    {
        System.out.print("Exception Occured"+e.getMessage().toString());
    }
        
}
        
public void DoInventory(RFIDReader reader)
{
    try
    {
    reader.Actions.Inventory.perform(null, null, null);
    Thread.sleep(5000);
    reader.Actions.TagLocationing.Stop();
    TagData[] locatedTags = reader.Actions.getReadTags(1000);
    if( locatedTags != null)
            {
                for(int i=0;i<locatedTags.length;i++)
                {
                    psLog.println(" EPC  :"+locatedTags[i].getTagID()+"Relative Distance"+locatedTags[i].LocationInfo.getRelativeDistance());
                }
            
    }
    }
    catch(Exception e)
    {
        System.out.print("Exception Occured"+e.getMessage().toString());
    }
}

public void SetGetConfigWord(RFIDReader reader,short ConfigWord)
{
 try
 {
    reader.connect();
    NXP nxp = new NXP(tagaccess);
    NXP.ResetReadProtectParams reset = nxp.new ResetReadProtectParams(0x00000001);
    reader.Actions.TagAccess.NXP.resetReadProtectEvent(reset, null);
    Thread.sleep(7000);
    NXP.ChangeConfigParams Config = nxp.new ChangeConfigParams();
    Config.setAccessword(0x00000001);
    Config.setNXPChangeConfigWord(ConfigWord);
    
    
    reader.Actions.TagAccess.NXP.ChangeConfigWait("FACEFACEFACEFACEFACEFACE", Config, null);
    Config.setNXPChangeConfigWord((short)0);
    TagData td = reader.Actions.TagAccess.NXP.ChangeConfigWait("FACEFACEFACEFACEFACEFACE", Config, null);
     System.out.print("Config Word: "+td.AccessOperationResult.NXPChangeConfigResult.getConfigWord());
     reader.disconnect();
 }
     catch(InvalidUsageException exp)
            {
                System.out.println( exp.getVendorMessage()+"    "+exp.getTimeStamp()+"   "+exp.getInfo());
            }
            catch(OperationFailureException opexp)
            {
                System.out.println(opexp.getVendorMessage()+"    "+opexp.getTimeStamp()+"   "+opexp.getStatusDescription());
            }
  catch(Exception e)
  {
    System.out.print("Exception Occured"+e.getMessage().toString());
  }
 
}

public void testNXPWait(RFIDReader reader)
{
    try
    {
    reader.connect();
    NXP nxp = new NXP(tagaccess);
    NXP.ResetReadProtectParams reset = nxp.new ResetReadProtectParams(0x00000001);
    reader.Actions.TagAccess.NXP.resetReadProtectEvent(reset, null);
    Thread.sleep(5000);
    NXP.ChangeConfigParams Config = nxp.new ChangeConfigParams();
    Config.setAccessword(0x00000001);
    Config.setNXPChangeConfigWord((short) 0x002);
    reader.Actions.TagAccess.NXP.ChangeConfigWait("FACEFACEFACEFACEFACEFACE", Config, null);
    
    Config.setNXPChangeConfigWord((short)0);
    TagData td = reader.Actions.TagAccess.NXP.ChangeConfigWait("FACEFACEFACEFACEFACEFACE", Config, null);
    System.out.println("Config Word"+td.AccessOperationResult.NXPChangeConfigResult.getConfigWord());
    Config.setNXPChangeConfigWord((short)0x002);
    td = reader.Actions.TagAccess.NXP.ChangeConfigWait("FACEFACEFACEFACEFACEFACE", Config, null);
    System.out.println("Config Word"+td.AccessOperationResult.NXPChangeConfigResult.getConfigWord());
    Config.setNXPChangeConfigWord((short)0);
    td = reader.Actions.TagAccess.NXP.ChangeConfigWait("FACEFACEFACEFACEFACEFACE", Config, null);
    System.out.println("Config Word"+td.AccessOperationResult.NXPChangeConfigResult.getConfigWord());
    reader.disconnect();  
    }
    catch(InvalidUsageException exp)
   {
     System.out.println( exp.getVendorMessage()+"    "+exp.getTimeStamp()+"   "+exp.getInfo());
    }
     catch(OperationFailureException opexp)
     {
         System.out.println(opexp.getVendorMessage()+"    "+opexp.getTimeStamp()+"   "+opexp.getStatusDescription());
     }
  catch(Exception e)
  {
    System.out.print("Exception Occured"+e.getMessage().toString());
  }
    
    
    
}

public void TestProtectMem(RFIDReader reader)
{
    try
    {
    reader.connect();
    postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A);
    int ap = 0;
    TagAccess tagaccess = new TagAccess();
    NXP nxp = new NXP(tagaccess);
    NXP.ChangeConfigParams Config = nxp.new ChangeConfigParams();
    Config.setAccessword(0x01);
    Config.setNXPChangeConfigWord((short)0x006);
    reader.Actions.TagAccess.NXP.ChangeConfigEvent(Config, null,null);
    Thread.sleep(3000);
    TagData[] locatedTags = reader.Actions.getReadTags(1000);
    if( locatedTags != null)
            {
                
                for(int i=0;i<locatedTags.length;i++)
                {
                    psLog.println("EPC :"+locatedTags[i].getTagID());
                    if(locatedTags[i].AccessOperationResult != null)
                        psLog.println(" Config Word  :"+locatedTags[i].AccessOperationResult.NXPChangeConfigResult.getConfigWord());
                }            
    }
    
    Config.setNXPChangeConfigWord((short)0);
    reader.Actions.TagAccess.NXP.ChangeConfigEvent(Config, null, null);
    Thread.sleep(5000);
    
//    postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A);
//    reader.Actions.Inventory.perform(postfilter, null, null);
//    Thread.sleep(5000);
//    reader.Actions.Inventory.stop();
//    TagData[] nxptag = reader.Actions.getReadTags(1);
//    reader.Actions.purgeTags();
    
    TagData[] reportedTags = reader.Actions.getReadTags(1000);
    if( reportedTags != null)
            {
                
                for(int i=0;i<reportedTags.length;i++)
                {
                    psLog.println("EPC :"+reportedTags[i].getTagID());
                    if(reportedTags[i].AccessOperationResult != null)
                        psLog.println(" Config Word  :"+reportedTags[i].AccessOperationResult.NXPChangeConfigResult.getConfigWord());
                }            
    }
    
//    byte[] writeData = {0x00,0x00,0x00,0x01};
//    TagAccess.WriteSpecificFieldAccessParams wspecific = tagaccess.new WriteSpecificFieldAccessParams();
//    wspecific.setAccessPassword(0x12345678);
//    wspecific.setWriteData(writeData);
//    wspecific.setWriteDataLength(4);
    
    //write access pwd
//    reader.Actions.TagAccess.writeAccessPasswordWait(nxptag[0].getTagID(), wspecific, null);
    
    ap = 0x12345678;
    
//    //read protection EPC
//    NXP.ChangeConfigParams Config = new NXP.ChangeConfigParams();
//    Config.setAccessword(ap);
//    Config.setNXPChangeConfigWord((short) 0x02);
////    Config.setNXPChangeConfigWord((short) 0x04);
//    
//    TagData tadata;    
//     tadata = reader.Actions.TagAccess.NXP.ChangeConfigWait(nxptag[0].getTagID(), Config, null);
//    
//    //read back the config word
//     Config.setNXPChangeConfigWord((short)0);
//     tadata = reader.Actions.TagAccess.NXP.ChangeConfigWait(nxptag[0].getTagID(), Config, null);
//     System.out.println("Config Word: "+tadata.AccessOperationResult.NXPChangeConfigResult.getConfigWord());
//     
//     //protect TID
//     Config.setNXPChangeConfigWord((short)0x002);
//     tadata = reader.Actions.TagAccess.NXP.ChangeConfigWait(nxptag[0].getTagID(), Config, null);
//     
//     //read back the config word
//     Config.setNXPChangeConfigWord((short)0);
//     tadata = reader.Actions.TagAccess.NXP.ChangeConfigWait(nxptag[0].getTagID(), Config, null);
//     System.out.println("Config Word: "+tadata.AccessOperationResult.NXPChangeConfigResult.getConfigWord());
     
    reader.disconnect();
    
    }
    catch(InvalidUsageException exp)
   {
     System.out.println( exp.getVendorMessage()+"    "+exp.getTimeStamp()+"   "+exp.getInfo());
    }
     catch(OperationFailureException opexp)
     {
         System.out.println(opexp.getVendorMessage()+"    "+opexp.getTimeStamp()+"   "+opexp.getStatusDescription());
     }
  catch(Exception e)
  {
    System.out.print("Exception Occured"+e.getMessage().toString());
  }
}

public void ReadAlarmBit(RFIDReader reader)
{
    try
    {       
    reader.connect();
    byte[] tp = {(byte)0xE2,(byte)0x00,(byte)0x68,(byte)0x05};
    PreFilters pfs = new PreFilters();
    PreFilters.PreFilter pf1  = pfs.new PreFilter();
    pf1.setAntennaID((short)0);
    pf1.setBitOffset((int)32);
    pf1.setFilterAction(FILTER_ACTION.FILTER_ACTION_STATE_AWARE);
    pf1.StateAwareAction.setStateAwareAction(STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A_NOT_INV_B);
    pf1.StateAwareAction.setTarget(TARGET.TARGET_INVENTORIED_STATE_S0);
    pf1.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
    pf1.setTagPattern(tp);
    pf1.setTagPatternBitCount((int) 32);
    reader.Actions.PreFilters.add(pf1);
    
    reader.Actions.Inventory.perform(null,null,null);
    Thread.sleep(5000);
    reader.Actions.Inventory.stop();
    TagData[] nxptag = reader.Actions.getReadTags(1);
    reader.Actions.purgeTags();
    
    NXP.ChangeConfigParams Config =nxp.new ChangeConfigParams();
    Config.setAccessword((long) 0);
    Config.setNXPChangeConfigWord((short) 0);
    
    TagData tadata = reader.Actions.TagAccess.NXP.ChangeConfigWait(nxptag[0].getTagID(), Config, null);
    System.out.println("Config Word: "+tadata.AccessOperationResult.NXPChangeConfigResult.getConfigWord());
    TagAccess.ReadAccessParams rparams = tagaccess.new ReadAccessParams(MEMORY_BANK.MEMORY_BANK_EPC,(int)64,(int)2,(long)0);
    tadata = reader.Actions.TagAccess.readWait(nxptag[0].getTagID(), rparams, null);
    System.out.println("Config Word: "+tadata.getMemoryBankData());
    reader.disconnect();
    
    }
    catch(InvalidUsageException exp)
   {
     System.out.println( exp.getVendorMessage()+"    "+exp.getTimeStamp()+"   "+exp.getInfo());
    }
     catch(OperationFailureException opexp)
     {
         System.out.println(opexp.getVendorMessage()+"    "+opexp.getTimeStamp()+"   "+opexp.getStatusDescription());
     }
  catch(Exception e)
  {
    System.out.print("Exception Occured"+e.getMessage().toString());
  }
    
}

public void textNXPChangeConfig(RFIDReader Reader, boolean isap)
{
    try
    {
        int ap = 0;
        //reader.connect();
        //postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A);
        //reader.Actions.Inventory.perform(postfilter, null, null);
        //Thread.sleep(5000);
        //reader.Actions.Inventory.stop();
        //TagData[] nxptag = reader.Actions.getReadTags(1);
        //reader.Actions.purgeTags();
//        TagStorageSettings tagStorageSettings = new TagStorageSettings();
//        tagStorageSettings = reader.Config.getTagStorageSettings();
//        tagStorageSettings.enableAccessReports(true);
//        tagStorageSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
//        reader.Config.setTagStorageSettings(tagStorageSettings);
//        reader.Events.setAccessStartEvent(true);
//        reader.Events.setAccessStopEvent(true);
        reader.Events.setEASAlarmEvent(true);
        //AntennaInfo aInfo = new AntennaInfo();
        //ushort[] alist = new ushort[2]{1,2};
        //OPERATION_QUALIFER[] opq = new OPERATION_QUALIFER[2]{ OPERATION_QUALIFER.NXP_EAS_SCAN,OPERATION_QUALIFER.NXP_EAS_SCAN};
        //aInfo.AntennaID = alist;
        //aInfo.OperationQualifier = opq;
//        byte[] writeData = { 0x12, 0x34, 0x56, 0x78 };
//        TagAccess.WriteSpecificFieldAccessParams writeSpecificAccessParam = tagaccess.new WriteSpecificFieldAccessParams();
//        writeSpecificAccessParam.setAccessPassword(0);
//        writeSpecificAccessParam.setWriteData(writeData);
//        writeSpecificAccessParam.setWriteDataLength(4);
//
//        if (isap)
//        {
//        // write access password
//        reader.Actions.TagAccess.writeAccessPasswordWait(nxptag[0].getTagID(), writeSpecificAccessParam, null);
//        ap = 0x12345678;
//        }

        NXP.ChangeConfigParams config = nxp.new ChangeConfigParams();
        config.setAccessword(1);
        System.out.println("Config:"+config.getNXPChangeConfigWord());
        config.setNXPChangeConfigWord((short) 0x0004);
////
//////        
        TagData tadata = reader.Actions.TagAccess.NXP.ChangeConfigWait("E20068070000000000000000", config, null);
        System.out.println(" Config Word Value is "+tadata.AccessOperationResult.NXPChangeConfigResult.getConfigWord());
        TagAccess.ReadAccessParams rParams = tagaccess.new ReadAccessParams();
        rParams.setAccessPassword(1);
        rParams.setByteCount(2);
        rParams.setByteOffset(64);
        rParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);

        

            tadata = reader.Actions.TagAccess.readWait("E20068070000000000000000", rParams, null);
        System.out.println("Config Word: "+tadata.getMemoryBankData());
//        if (isap)
//        {
//        writeData[0] = 0; writeData[1] = 0; writeData[2] = 0; writeData[3] = 0;
//        writeSpecificAccessParam.setWriteData(writeData);
//        //reset password
//        reader.Actions.TagAccess.writeAccessPasswordWait(nxptag[0].getTagID(), writeSpecificAccessParam, null);
//        }
//        reader.disconnect();
            reader.Actions.TagAccess.NXP.performEASScan();
            Thread.sleep(10000);
            reader.Actions.TagAccess.NXP.stopEASScan();
           

    }
    catch(InvalidUsageException exp)
   {
     System.out.println( exp.getVendorMessage()+"    "+exp.getTimeStamp()+"   "+exp.getInfo());
    }
     catch(OperationFailureException opexp)
     {
         System.out.println(opexp.getVendorMessage()+"    "+opexp.getTimeStamp()+"   "+opexp.getStatusDescription());
     }
    catch(NullPointerException opexp)
     {
         System.out.println(opexp.getMessage());
     }
  catch(Exception e)
  {
    System.out.print("Exception Occured"+e.getMessage().toString());
  }
}

public void testNXPFunctionality(RFIDReader reader)
{
          try
        {
            reader.connect();
            TagStorageSettings tagStorageSettings = new TagStorageSettings();
            tagStorageSettings = reader.Config.getTagStorageSettings();
            tagStorageSettings.enableAccessReports(true);
            tagStorageSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            reader.Config.setTagStorageSettings(tagStorageSettings);
            reader.Events.setAccessStartEvent(true);
            reader.Events.setEASAlarmEvent(true);
            reader.Events.setAccessStopEvent(true);
            
             byte[] ap = new byte[] { 0x12, 0x34, 0x56, 0x78 };
             TagAccess.WriteAccessParams wParams = tagaccess.new WriteAccessParams();
             
             
             wParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
             wParams.setByteOffset((int) 4);
             wParams.setWriteDataLength((int) 4);
             wParams.setAccessPassword((long)0);
             wParams.setWriteData(ap);                  
                     
             reader.Actions.TagAccess.writeEvent(wParams, accessfilter, null);
             Thread.sleep(15000);
             getEvents = new ReadEventlistener(reader, psLog);//-----------------------------
        reader.Events.addEventsListener(getEvents);//--------------------------------
        reader.Actions.TagAccess.NXP.setEASEvent(setEAS, null, null);
        Thread.sleep(15000);
       
         psResult.println("Perform NXP EAS Scan for Alarm Events");
         reader.Actions.TagAccess.NXP.setEASEvent(setEAS,null,null);
          Thread.sleep(15000);
          
         psResult.println("Perform NXP EAS Scan for Alarm Events");
         reader.Actions.TagAccess.NXP.performEASScan();
         Thread.sleep(5000);
          reader.Actions.TagAccess.NXP.stopEASScan();
           reader.Actions.TagAccess.NXP.setEASEvent(resetEAS, null, null);
          reader.disconnect();
            
        }
       catch(InvalidUsageException exp)
   {
     System.out.println( exp.getVendorMessage()+"    "+exp.getTimeStamp()+"   "+exp.getInfo());
    }
     catch(OperationFailureException opexp)
     {
         System.out.println(opexp.getVendorMessage()+"    "+opexp.getTimeStamp()+"   "+opexp.getStatusDescription());
     }
  catch(Exception e)
  {
    System.out.print("Exception Occured"+e.getMessage().toString());
  }
}


        



        

        
        
        
    
    
    
    
    
    

//        //Events 
//        GetEvents getEvents = new GetEvents(reader, psLog);
//        reader.Events.addEventsListener(getEvents);
//        reader.Events.setTagReadEvent(true);
//        boolean isTagReadEventSet = reader.Events.isTagReadEventSet();
//        psLog.println("isTagReadEventSet: " + isTagReadEventSet);
//        String tagId = "1234123412341234";
//
//        
//        TagAccess tagaccess = new TagAccess();
//        NXP nxp = new NXP(tagaccess);
//        NXP.ChangeConfigParams ChangeConfigParams = nxp.new ChangeConfigParams();
//        NXP.ReadProtectParams readProtectParams = nxp.new ReadProtectParams();
//        NXP.ResetReadProtectParams resetReadProtectParams = nxp.new ResetReadProtectParams();
//        
//        ChangeConfigParams.setAccessword(00000001);
//        psLog.println("getAccessPassword from ChangeConfigParams: " + ChangeConfigParams.getAccessword());


//        Inventory in = new Inventory();
//        try {
//
//
//            reader.Actions.TagAccess.NXP.SetEASWait(tagId, setEASParams, null);
//            psLog.println("Set EAS done");
//            reader.Actions.TagAccess.NXP.performEASScan();
//            Thread.sleep(10000);
//            reader.Actions.TagAccess.NXP.stopEASScan();
//            readProtectParams.setAccessPassword(00000001);
//            reader.Actions.TagAccess.NXP.readProtectWait(tagId, readProtectParams, null);
//            psLog.println("Set quiet done");
//            Test_SimpleInventory();
//            resetReadProtectParams.setAccessPassword(00000001);
//            reader.Actions.TagAccess.NXP.resetReadProtectEvent(resetReadProtectParams, null);
//            psLog.println("Reset quiet done");
//
//            Test_SimpleInventory();
//        } catch (InvalidUsageException exp) {
//            System.out.print("InvalidUsageException" + exp.getInfo());
//            psLog.println("InvalidUsageException" + exp.getInfo());
//            psLog.println("VendorMessage" + exp.getVendorMessage());
//        } catch (OperationFailureException exp) {
//
//            System.out.print("OperationFailureException" + exp.getMessage());
//            psLog.println("OperationFailureException" + exp.getMessage());
//            psLog.println("VendorMessage" + exp.getVendorMessage());
//        } catch (InterruptedException e) {
//        }
//
   
//
//    public void Test_SimpleInventory() {
//
//        try {
//            reader.Actions.Inventory.perform();
//            Thread.sleep(1000);
//            reader.Actions.Inventory.stop();
//            TagData tagdata[] = reader.Actions.getReadTags(10);
//            TagNum = tagdata.length;
//            if (tagdata != null) {
//                for (int i = 0; i < tagdata.length; i++) {
//                    //System.out.print(""+tagdata[i].getTagID());
//                    psLog.print("<br>" + tagdata[i].getTagID());
//                    //((DefaultTableModel)jTable1.getModel()).insertRow(0, new Object[]{tagdata[i].getTagID(),tagdata[i].getPC(),tagdata[i].getPeakRSSI(),count});
//
//                }
//            } else {
//                psLog.print("<br>No tags read");
//            }
//
//        } catch (InvalidUsageException exp) {
//            System.out.print("InvalidUsageException" + exp.getInfo());
//            psLog.println("InvalidUsageException" + exp.getInfo());
//        } catch (OperationFailureException exp) {
//
//            System.out.print("OperationFailureException" + exp.getMessage());
//            psLog.println("OperationFailureException" + exp.getMessage());
//
//        } catch (InterruptedException e) {
//            System.out.print("InterruptedException" + e.getMessage());
//            psLog.println("InterruptedException" + e.getMessage());
//        }
//    }
//    
//    public void SetGetConfigWord(short ConfigWord)
//        {
//        TagAccess tagaccess = new TagAccess();
//        NXP nxp = new NXP(tagaccess);   
//        NXP.ResetReadProtectParams resetReadProtectParams = nxp.new ResetReadProtectParams((long) 00000001);
//        try {
//
//        reader.Actions.TagAccess.NXP.resetReadProtectEvent(resetReadProtectParams, null);  
//        Thread.sleep(7000);
//        NXP.ChangeConfigParams Config = nxp.new ChangeConfigParams ();
//        } catch (InvalidUsageException exp) {
//            System.out.print("InvalidUsageException" + exp.getInfo());
//            psLog.println("InvalidUsageException" + exp.getInfo());
//            psLog.println("VendorMessage" + exp.getVendorMessage());
//        } catch (OperationFailureException exp) {
//
//            System.out.print("OperationFailureException" + exp.getMessage());
//            psLog.println("OperationFailureException" + exp.getMessage());
//            psLog.println("VendorMessage" + exp.getVendorMessage());
//        } catch (InterruptedException e) {
//        }
//       
////            Thread.sleep(7000);
////            NXP.ChangeConfigParams Config = new NXP.ChangeConfigParams { AccessPassword = 0x01, NXPChangeConfigWord = ConfigWord };
////            Reader.Actions.TagAccess.NXP.ResetReadProtectEvent(reset, null);
////            Thread.Sleep(7000);
////            NXP.ChangeConfigParams Config = new NXP.ChangeConfigParams { AccessPassword = 0x01, NXPChangeConfigWord = ConfigWord };
////            try
////            {
////                Reader.Actions.TagAccess.NXP.ChangeConfigWait("FACEFACEFACEFACEFACEFACE", Config, null);
////                Config.NXPChangeConfigWord = 0;
////                TagData td = Reader.Actions.TagAccess.NXP.ChangeConfigWait("FACEFACEFACEFACEFACEFACE", Config, null);
////                Console.WriteLine("Config Word is {0:X}", td.AccessOperationResult.NXPChangeConfigResult.ConfigWord);
////
////            }
////            catch (InvalidUsageException e)
////            {
////                Console.WriteLine("Exception Occured {0}{1}", e.Message.ToString(), e.Info);
////            }
////            catch (OperationFailureException e)
////            {
////                Console.WriteLine("Exception Occured {0} {1}", e.Message.ToString(), e.StatusDescription);
////            }
////            Reader.Disconnect();
//       
//}
}