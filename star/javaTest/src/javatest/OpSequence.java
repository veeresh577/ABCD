/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.reader;

/**
 *
 * @author NVJ438
 */
//class accessListener1 implements RfidEventsListener
//{
//    private PrintStream psLog;
//    ManualResetEvent accessStop;
//    public accessListener1( PrintStream fileStream,ManualResetEvent event)
//    {
//         psLog = fileStream;
//         accessStop = event;
//    }
//
//    public void eventReadNotify(RfidReadEvents rfidReadEvents)
//    {
////        psLog.println("<br>\n READ EVENT Happened");
////        if(rfidReadEvents != null)
////        {
////            TagData tagData  = rfidReadEvents.getReadEventData().tagData;
////            psLog.println("<br>\n tags ID :  "+tagData.getTagID()+" membank  :"+tagData.getMemoryBank()+" memBank Data:  "+tagData.getMemoryBankData()+" opCode  :"+tagData.getOpCode()
////                    +" OpCode Status  :"+tagData.getOpStatus());
////        }
//    }
//
//    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
//    {
//        psLog.println("\n Event  :"+rfidStatusEvents.StatusEventData.getStatusEventType()+"<br> \r\n");
//        if( STATUS_EVENT_TYPE.ACCESS_STOP_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
//        {
//            accessStop.set();
//        }
//        else if( STATUS_EVENT_TYPE.NXP_EAS_ALARM_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
//        {
//            System.out.println( "alarmCode "+rfidStatusEvents.StatusEventData.NXPEASAlarmEventData.getEASAlarmCode());
//            accessStop.set();
//        }
//    }
//}

public class OpSequence extends Commonclass {


     TagAccess.Sequence.Operation[] operationArray ;
        int MaxOpSeq;
        TagAccess.Sequence sequance;
        private TagAccess tagAccess;
     private ManualResetEvent accessComplete;
     private TriggerInfo tInfo;
     //private AccessFilter accessfilter;
     private accessListener myaccesslistener1;
     //ACCESS_OPERATION_CODE[] opcodes = null;


    public OpSequence() {

        tInfo = new TriggerInfo();
        accessComplete = new ManualResetEvent(false);
        accessfilter = new AccessFilter();
        tagAccess = new TagAccess( );
        sequance = tagAccess.new Sequence(tagAccess);
        MaxOpSeq = reader.ReaderCapabilities.getMaxNumOperationsInAccessSequence();
        try {
            
            mystreamLog = new FileOutputStream("JavaAPI_OpSequence_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_OpSequence_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        } catch (FileNotFoundException e) {
            psLog.println("" + e.getMessage());

        }
        
        tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
        tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_N_ATTEMPTS_WITH_TIMEOUT);
        tInfo.StopTrigger.NumAttempts.setN((short)1);
        tInfo.StopTrigger.NumAttempts.setTimeout(15000);
        try{

            accessfilter.setRSSIRangeFilter(false);
            TagStorageSettings tgSettings = new TagStorageSettings();
            tgSettings = reader.Config.getTagStorageSettings();
            tgSettings.enableAccessReports(true);
            tgSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            reader.Config.setTagStorageSettings(tgSettings);


            //Register for access start and stop events
            reader.Events.setAccessStartEvent(true);
            reader.Events.setAccessStopEvent(true);
            reader.Events.setAttachTagDataWithReadEvent(true);

            myaccesslistener1 = new accessListener(psLog,accessComplete);
            reader.Events.addEventsListener(myaccesslistener1);

            // initialize the filter for which to do access on
            byte[] tagPattern1 = { (byte)0xBE,(byte)0xDD};
            byte[] tagMask = {(byte)0xFF,(byte)0xFF};
            accessfilter.TagPatternA.setBitOffset(32);
            accessfilter.TagPatternA.setTagPattern(tagPattern1);
            accessfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            accessfilter.TagPatternA.setTagMaskBitCount(16);
            accessfilter.TagPatternA.setTagPatternBitCount(16);
            accessfilter.TagPatternA.setTagMask(tagMask);
            accessfilter.setAccessFilterMatchPattern(FILTER_MATCH_PATTERN.A);
            accessfilter.setRSSIRangeFilter(false);
        }catch(Exception e)
        {
            psLog.println("<br>Exception"+e.getMessage());
        }
    }
    
    public void Test_MultipleOpSequence() {

        psLog.println("<html><br>");
        psLog.println("<body><br>");

        //Antenna Power settings
//        AntennaPowersettings();

        successCount = 0;
        failureCount = 0;

        TestNo = 65;
        SubNo = 0;
        
        psLog.println("<br>Multiple OpSequence Test cases");
        System.out.print("\nMultiple OpSequence Test cases..Started");
        
        
//        
////        TC 1
//        
//            FormTestID(TestNo, SubNo++, "OpSeq");
//            psLog.println("<br><br><br><b>Description:</b> Op Sequence with 9 operations ");
//            psLog.println("<br>Expected Result: All operation should be a success");
//            ACCESS_OPERATION_CODE[] opcodes = {ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ};
//            
//            if(TestMultiSequence(opcodes) == reader.ReaderCapabilities.getMaxNumOperationsInAccessSequence())
//            {
//                psLog.println("<br>" + TestID + ":Op sequence " + ":PASSED");
//                psResult.println("<br>" + TestID + ":Op sequence " + ":PASSED");
//                successCount++;
//            }else{
//                psLog.println("<br>" + TestID + ":Op sequence " + ":FAILED");
//                psResult.println("<br>" + TestID + ":Op sequence " + ":FAILED");
//                failureCount++;
//            }

            
        
        //TC 2
//        ACCESS_OPERATION_CODE[] opcodewrite= {ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE};
        ACCESS_OPERATION_CODE[] opcodesAll = {ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE};
        //ACCESS_OPERATION_CODE[] opcodesAll = {ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_PERMALOCK};
        //ACCESS_OPERATION_CODE[] opcodesAll = {ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ};
//        ACCESS_OPERATION_CODE[] opcodesAll = {ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE};
        int ind = MaxOpSeq;
        for(int j=0; j<opcodesAll.length; j++)
        {
//            try {
//            reader.Config.resetFactoryDefaults();
//            psLog.println("<br>Inside Antenna Power setting Method");
//            Antennas antenna = reader.Config.Antennas;
//            
//            Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(1);
////            Config.setTransmitPowerIndex((short) ((reader.ReaderCapabilities.getTransmitPowerLevelValues().length)-16));
////            for(int i=1; i<=reader.ReaderCapabilities.getNumAntennaSupported();i++)
////            {
////                reader.Config.Antennas.setAntennaConfig(i, Config);
////            Config = reader.Config.Antennas.getAntennaConfig(1);
//            psLog.print("<br>getTransmitPowerIndex for antenna "+1+" is "+Config.getTransmitPowerIndex());
////            }
//
//        } catch (InvalidUsageException exp) {
//            psLog.println("<br>getVendorMessage" + exp.getVendorMessage());
//            psLog.println("<br>InvalidUsageException" + exp.getInfo());
//        } catch (OperationFailureException exp) {
//
//            psLog.println("<br>OperationFailureException" + exp.getMessage());
//            psLog.println("<br>getVendorMessage" + exp.getVendorMessage());
//        }
            for(int i=1; i<ind; i++)
            {
                ACCESS_OPERATION_CODE[] op = new ACCESS_OPERATION_CODE[i];
                Arrays.fill(op, opcodesAll[j]);
                FormTestID(TestNo, SubNo++, "OpSeq");
                psLog.println("<br><br><b>Description:</b> Op Sequence with "+" "+i+" "+opcodesAll[j]+"  operations ");
                psLog.println("<br>Expected Result: All "+i+" operation should be a success");
                if(TestMultiSequence(op) == i)
                {
                    psLog.println("<br>" + TestID + ":Op sequence " + ":PASSED");
                    psResult.println("<br>" + TestID + ":Op sequence " + ":PASSED");
                    successCount++;

                }else{
                    psLog.println("<br>" + TestID + ":Op sequence " + ":FAILED");
                    psResult.println("<br>" + TestID + ":Op sequence " + ":FAILED");
                    failureCount++;
                }


            }
        }
        

        //TC 3
                TestNo = 3;
                FormTestID(TestNo, SubNo++, "OpSeq");
                psLog.println("<br><br><b>Description:</b> Op Sequence with ACCESS_OPERATION_NXP_SET_EAS operation ");
                psLog.println("<br>Expected Result: All operation should be a success");
                ACCESS_OPERATION_CODE[] opcodes1 = {ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_SET_EAS,ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_READ_PROTECT,ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_RESET_READ_PROTECT};
                if(TestMultiSequence(opcodes1) == 3)
                {
                    psLog.println("<br>" + TestID + ":Op sequence " + ":PASSED");
                    psResult.println("<br>" + TestID + ":Op sequence " + ":PASSED");
                    successCount++;
                }else{
                    psLog.println("<br>" + TestID + ":Op sequence " + ":FAILED");
                    psResult.println("<br>" + TestID + ":Op sequence " + ":FAILED");
                    failureCount++;
                }

                psSummary.println("JavaAPI:Op Sequence:" + successCount + ":" + failureCount + ":" + "0");
                System.out.print("\n    Multiple OpSequence Test cases..Complete");
              
    }

    public int TestMultiSequence(ACCESS_OPERATION_CODE[] opCodes )
    {

            boolean equalArguments = false;
            int SuccessCount = 0;
            ACCESS_OPERATION_CODE[] opCode = opCodes;
            //for(ACCESS_OPERATION_CODE opCode:opCodes)
            for(int i =0; i<opCode.length; i++)
            {
                if (opCodes[0] == opCodes[i])
                {
                    equalArguments = true;
                }
                else
                {
                    equalArguments = false;
                }
            }


            try
            {
                reader.Actions.TagAccess.OperationSequence.deleteAll();
                //psLog.println("<br>getMaxNumOperationsInAccessSequence"+MaxOpSeq);
                if(opCodes.length > MaxOpSeq)
                {
                    MaxOpSeq = MaxOpSeq + 1;
                }
                operationArray = new TagAccess.Sequence.Operation[MaxOpSeq];
            }catch(Exception e)
            {
                psLog.println("<br>Exception"+e.getMessage());
            }

            // a Case where only Reads are give, more flexible way is to exapnd the logic below for equal arguments case where the Opcode
            // is other than Read.
            //operationArray = new TagAccess.Sequence.Operation[MaxOpSeq];
//            if (equalArguments)
//            {
                int index = 0;

                try{
                    reader.Actions.purgeTags();
                    
                for(int i =0; i<opCodes.length; i++)
                {
                    
                    operationArray[index] = sequance.new Operation();
                    switch(opCodes[index].getValue())
                    {

                        case 0:
                        {
                                                         
                                //ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ
                                psLog.println("<br>ACCESS_OPERATION_READ");
                                TagAccess.ReadAccessParams rParams = tagaccess.new ReadAccessParams(MEMORY_BANK.MEMORY_BANK_USER,0,2,0);
                                operationArray[index].ReadAccessParams = rParams;
                                operationArray[index].setAccessOperationCode(opCodes[i]);
                                break;
                        }
                        case 1:
                                //ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE
                                psLog.println("<br>ACCESS_OPERATION_WRITE");
                                byte[] writeData = { 0x12, 0x34, 0x56, 0x78 };
                                TagAccess.WriteAccessParams wParams = tagaccess.new WriteAccessParams(MEMORY_BANK.MEMORY_BANK_USER,0,2,0,writeData);
                                operationArray[index].WriteAccessParams = wParams;
                                operationArray[index].setAccessOperationCode(opCodes[i]);
                                break;
                        case 2:
                            //ACCESS_OPERATION_CODE.ACCESS_OPERATION_LOCK
                            break;
                        case 3:
                            //ACCESS_OPERATION_CODE.ACCESS_OPERATION_KILL
                            break;
                        case 4:
                                //ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE
                                psLog.println("<br>ACCESS_OPERATION_BLOCK_WRITE");
                                byte[] bwriteData = { 0x12, 0x34, 0x56, 0x78 };
                                TagAccess.WriteAccessParams blwParams = tagaccess.new WriteAccessParams(MEMORY_BANK.MEMORY_BANK_USER,0,4,0,bwriteData);
                                operationArray[index].BlockWriteAccessParams = blwParams;
                                operationArray[index].setAccessOperationCode(opCodes[i]);
                                break;
                        case 5:
                                //ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE
                                psLog.println("<br>ACCESS_OPERATION_BLOCK_ERASE");
                                TagAccess.BlockEraseAccessParams EraseParams = tagaccess.new BlockEraseAccessParams(MEMORY_BANK.MEMORY_BANK_USER,0,12,0);
                                operationArray[index].BlockEraseAccessParams = EraseParams;
                                operationArray[index].setAccessOperationCode(opCodes[i]);
                                break;
                        case 6:
                            //ACCESS_OPERATION_CODE.ACCESS_OPERATION_RECOMMISSION
                            break;
                        case 7:
                                //ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_PERMALOCK
                                psLog.println("<br>ACCESS_OPERATION_BLOCK_PERMALOCK");
                                byte[] Mask = { 0x40, 0x00 };
                                TagAccess.BlockPermalockAccessParams bPermaLockParams = tagaccess.new BlockPermalockAccessParams(MEMORY_BANK.MEMORY_BANK_TID,true,0,1,0x12345678,2,Mask);
                                operationArray[index].BlockPermaLockAccessParams = bPermaLockParams;
                                operationArray[index].setAccessOperationCode(opCodes[i]);
                            break;
                        case 8:
                            //ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_SET_EAS
                            psLog.println("<br>ACCESS_OPERATION_NXP_SET_EAS");
                            operationArray[index].SetEASParams.setAccessPassword(0x00000001);
                            operationArray[index].SetEASParams.setEAS(true);
                            operationArray[index].setAccessOperationCode(opCodes[i]);
                            break;
                        case 9:
                            //ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_READ_PROTECT
                            psLog.println("<br>ACCESS_OPERATION_NXP_READ_PROTECT");
                            operationArray[index].ReadProtectParams.setAccessPassword(0x00000001);
                            operationArray[index].setAccessOperationCode(opCodes[i]);
                            break;
                        case 10:
                            //ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_RESET_READ_PROTECT
                            psLog.println("<br>ACCESS_OPERATION_NXP_RESET_READ_PROTECT");
                            operationArray[index].ResetReadProtectParams.setAccessPassword(0x00000001);
                            operationArray[index].setAccessOperationCode(opCodes[i]);
                            break;
                        case 255:
                            //ACCESS_OPERATION_CODE.ACCESS_OPERATION_NONE
                            break;
                        default:
                            break;

                    }
                    reader.Actions.TagAccess.OperationSequence.add(operationArray[index]);
                    index++;

                }
                }
                catch(InvalidUsageException exp)
                {
                    CleanupPendingSequence();
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());

                }

//            }
//            else
//            {
//
//            }
            // Call perform sequence and count the success..if correct sequence added the count should match the length of 
            // op sequences sent. if not it is a failure.Note: The sequence is applied on a single tag using access filter.

            try
                {
                    reader.Actions.purgeTags();
                    psLog.println("<br>Length of OperationSequence "+reader.Actions.TagAccess.OperationSequence.getLength()+"\n");
                    accessComplete.reset();
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
                    //accessComplete.waitOne();
                    Thread.sleep(15000);
                    reader.Actions.TagAccess.OperationSequence.deleteAll();

                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                catch(InterruptedException e)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+e.getMessage());
                }
                SuccessCount = 0;
            TagData[] tags = reader.Actions.getReadTags(1000);
            if( tags != null )
            {
                for (int iteration = 0; iteration < opCodes.length; iteration++)
                        {

                            for(int i=0;i<tags.length;i++)
                            {
                                if (tags[i].getOpCode() == opCodes[iteration])
                                {
                                    //System.out.print("\n TagID: {0} opCode : {1} Access Result {2}"+ tag.getTagID()+ tag.getOpCode()+ tag.getOpStatus());
                                    psLog.println("<br>TagID: "+ tags[i].getTagID()+"opCode :"+tags[i].getOpCode()+" Access Result " + tags[i].getOpStatus()+" Antenna ID: "+tags[i].getAntennaID()+" Offset: "+tags[i].getMemoryBankDataOffset());
                                    
                                    if (tags[i].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
                                    {
                                        SuccessCount++;
                                        if (tags[i].getMemoryBankData() != null)
                                        {
                                            psLog.println("\n Operation: "+tags[i].getOpCode()+" MemBank: "+tags[i].getMemoryBank()+"Data: " + tags[i].getMemoryBankData()+"Antenna ID: "+tags[i].getAntennaID()+" Offset: "+tags[i].getMemoryBankDataOffset());
                                        }
                                        break;
                                    }
                                    if(tags[i].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_PERMALOCK)
                                    {
                                        if (tags[i].getOpStatus() != ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
                                        {
                                            SuccessCount++;
                                            if (tags[i].getMemoryBankData() != null)
                                            {
                                                psLog.println("\n Operation: "+tags[i].getOpCode()+" MemBank: "+tags[i].getMemoryBank()+"Data: " + tags[i].getMemoryBankData()+"Antenna ID: "+tags[i].getAntennaID()+" Offset: "+tags[i].getMemoryBankDataOffset());
                                            }
                                            break;
                                        }
                                    }
                                    if(tags[i].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_SET_EAS ||
                                            tags[i].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_READ_PROTECT ||
                                                tags[i].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_RESET_READ_PROTECT)
                                    {
                                        if (tags[i].getOpStatus() != ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
                                        {
                                            SuccessCount++;
                                            if (tags[i].getMemoryBankData() != null)
                                            {
                                                psLog.println("\n Operation: "+tags[i].getOpCode()+" MemBank: "+tags[i].getMemoryBank()+"Data: " + tags[i].getMemoryBankData()+"Antenna ID: "+tags[i].getAntennaID()+" Offset: "+tags[i].getMemoryBankDataOffset());
                                            }
                                            break;
                                        }
                                    }
                                    
                                }
                                
                            }
                        }

//                for(int tagCount = 0; tagCount < tags.length;tagCount++)
//                {
//                    if( tags[tagCount].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
//                    {
//                        psLog.println("\n EPC  :"+tags[tagCount].getTagID()+" memBank "+tags[tagCount].getMemoryBank()+"  memoryBank Data :"+ tags[tagCount].getMemoryBankData()+" length  :"+
//                            tags[tagCount].getMemoryBankDataAllocatedSize()+"getOpStatus "+tags[tagCount].getOpStatus()+"getOpCode "+tags[tagCount].getOpCode()+"<br>");
//                    }
//                }
                if(index == reader.ReaderCapabilities.getMaxNumOperationsInAccessSequence())
                                    {
                                        if(SuccessCount>reader.ReaderCapabilities.getMaxNumOperationsInAccessSequence())
                                        {
                                            SuccessCount = 0;
                                        }

                                    }
                reader.Actions.purgeTags();

            }
            else
            {
                psLog.println("\nTag Not found");
                reader.Actions.purgeTags();

            }

        return SuccessCount;
         
        
    }

}
