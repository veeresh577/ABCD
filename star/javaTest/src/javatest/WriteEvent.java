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
public class WriteEvent extends Commonclass {

    String readdata;
    String TagId = "1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF";
    boolean retrycount;
    byte writeData[] = {(byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x33, (byte) 0x33, (byte) 0x44, (byte) 0x44, (byte) 0x55, (byte) 0x55, (byte) 0x66, (byte) 0x66, (byte) 0x77, (byte) 0x77, (byte) 0x88, (byte) 0x88, (byte) 0x99, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0xAA, (byte) 0xAA, (byte) 0xBB, (byte) 0xBB, (byte) 0xCC, (byte) 0xCC, (byte) 0xDD, (byte) 0xDD, (byte) 0xEE, (byte) 0xEE, (byte) 0xFF, (byte) 0xFF, (byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x33, (byte) 0x33, (byte) 0x44, (byte) 0x44, (byte) 0x55, (byte) 0x55, (byte) 0x66, (byte) 0x66, (byte) 0x77, (byte) 0x77, (byte) 0x88, (byte) 0x88, (byte) 0x99, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0xAA, (byte) 0xAA, (byte) 0xBB, (byte) 0xBB, (byte) 0xCC, (byte) 0xCC, (byte) 0xDD, (byte) 0xDD, (byte) 0xEE, (byte) 0xEE, (byte) 0xFF, (byte) 0xFF};
    ReadEventlistener getEvents;

    public WriteEvent() {
        try {
            mystreamLog = new FileOutputStream("JavaAPI_WriteEvent_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_WriteEvent_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            reader.Config.resetFactoryDefaults();
        } catch (FileNotFoundException e) {
            psLog.println("" + e.getMessage());
        }
        catch(InvalidUsageException e){
            psLog.println(""+e.getMessage());
        }
        catch(OperationFailureException e){
            psLog.println(""+e.getMessage());
        }
        byte[] tagPattern1 = {(byte) 0xBE, (byte) 0xDD};
        byte[] tagMask = {(byte) 0xFF, (byte) 0xFF};
        accessfilter.TagPatternA.setBitOffset(32);
        accessfilter.TagPatternA.setTagPattern(tagPattern1);
        accessfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        accessfilter.TagPatternA.setTagMaskBitCount(tagPattern1.length * 8);
        accessfilter.TagPatternA.setTagPatternBitCount((2 * 8));
        accessfilter.TagPatternA.setTagMask(tagMask);
        accessfilter.setAccessFilterMatchPattern(FILTER_MATCH_PATTERN.A);
    }

    public void Test_WriteEvent() {
        //Antenna Power settings
        
        AntennaPowersettings();

        getEvents = new ReadEventlistener(reader, psLog);
        reader.Events.addEventsListener(getEvents);
        reader.Events.setTagReadEvent(true);
        reader.Events.setAccessStartEvent(true);
        reader.Events.setAccessStopEvent(true);

        accessfilter.setRSSIRangeFilter(false);
        successCount = 0;
        failureCount = 0;
        TestNo = 104;
        SubNo = 1;
        retrycount = false;

        psLog.println("<br>Write Event Test cases");
        //System.out.print("\nWrite Event Test cases");

        reader.Events.setAttachTagDataWithReadEvent(true);
//        Test_WriteEventReserved();
        Test_WriteEventUser();
        //Test_WriteEventEPC();
        reader.Events.removeEventsListener(getEvents);

        //Print into the summary sheet
        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:WriteEvent test cases:" + successCount + ":" + failureCount + ":" + "0");
    }

    public void Test_WriteEventReserved() {


        psLog.println("<br>-----------------------------------------------------------------------------------------");
        psLog.println("<br>varying the pointer(byteoffset) , word count byte = 2 For Reserve Memory Bank............");
        psLog.println("<br>-----------------------------------------------------------------------------------------");




        for (int i = 0; i < 8; i = i + 2) {

            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteEvent on ");

            for (int retry = 0; retry < 3; retry++) {
                psLog.println("<br>Retry " + retry);


                if (WriteEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, writeData, accessfilter) == 0) {
//                    getLastError();
                    
                    ReadEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, accessfilter);
                    
                    psLog.println("<br>Expected Result: 1111");
                    try{
                        readdata = getEvents.tagData.getMemoryBankData();
                    }
                    catch(Exception e){
                        psLog.println(""+e.getMessage());
                    }
        
//                    getLastError();
                    psLog.println(" Actual Result:" + readdata);
                    if (tagCompare(TagId, readdata, 2, 0) == 0) {
                        psLog.println("<br>" + TestID + ",Memory:Reserved,Pointer=" + i + ",Count=1:PASSED");
                        psResult.println("" + TestID + ",Memory:Reserved,Pointer=" + i + ",Count=1:PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + ",Memory:Reserved,Pointer=" + i + ",Count=1:FAILED");
                        psResult.println("" + TestID + ",Memory:Reserved,Pointer=" + i + ",Count=1:FAILED");
                        failureCount++;
                    }
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psLog.println("<br>" + TestID + ",Memory:Reserved,Pointer=" + i + ",Count=1:FAILED");
                    psResult.println("" + TestID + ",Memory:Reserved,Pointer=" + i + ",Count=1:FAILED");
                    failureCount++;
                    break;
                }


            }//retry loop
        }//FOR LOOP
        psLog.println("<br>--------------------------------------------");
        psLog.println("<br><br>Varying the word count and pointer = 0");
        psLog.println("<br>--------------------------------------------");

        for (int i = 2; i <= 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteEvent on ");

            for (int retry = 0; retry < 3; retry++) {

                if (WriteEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, writeData, accessfilter) == 0) {
//                    getLastError();
                    ReadEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, accessfilter);

                    psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(writeData, 0, i)));
                    try{
                        readdata = getEvents.tagData.getMemoryBankData();
                    }
                    catch(Exception e){
                        psLog.println(""+e.getMessage());
                    }
//                    getLastError();
                    psLog.println(" Actual Result:" + readdata);
                    if (tagCompare(TagId, readdata, i, 0) == 0) {
                        psLog.println("<br>" + TestID + ",Memory:RESERVED,Count=" + i + ",Pointer=0:PASSED");
                        psResult.println("" + TestID + ",Memory:RESERVED,Count=" + i + ",Pointer=0:PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>" + TestID + ",Memory:RESERVED,Count=" + i + ",Pointer=0:FAILED");
                        psResult.println("" + TestID + ",Memory:RESERVED,Count=" + i + ",Pointer=0:FAILED");
                        failureCount++;
                    }
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }


                if (retry == 2) {
                    psLog.println("<br>" + TestID + ",Memory:RESERVED,Count=" + i + ",Pointer=0:FAILED");
                    psResult.println("" + TestID + ",Memory:RESERVED,Count=" + i + ",Pointer=0:FAILED");
                    failureCount++;
                    break;
                }



            }//retry loop
        }
    }

    public void Test_WriteEventEPC() {
        //=========================================================================================================================
        byte WriteDataEPC[] = {0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55, 0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55};


        psLog.println("<br>-------------------------------------------------");
        psLog.println("<br><br>Varying the pointer and word count = 2 bytes");
        psLog.println("<br>-------------------------------------------------");

        for (int i = 6; i <= 12; i = i + 2) {

            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteEvent on ");

            for (int retry = 0; retry < 3; retry++) {
                if (WriteEvent(MEMORY_BANK.MEMORY_BANK_EPC, i, 2, writeData, accessfilter) == 0) {
                    ReadEvent(MEMORY_BANK.MEMORY_BANK_EPC, i, 2, accessfilter);
                    psLog.println("<br>Expected Result: 1111");
                    try{
                        readdata = getEvents.tagData.getMemoryBankData();
                    }
                    catch(Exception e){
                        psLog.println(""+e.getMessage());
                    }
                    psLog.println(" Actual Result:" + readdata);

                    if (tagCompare(TagId, readdata, 2, 0) == 0) {
                        psLog.println("<br>" + TestID + ",Memory:Reserved,Pointer=" + i + ",Count=1:PASSED");
                        psResult.println("" + TestID + ",Memory:EPC,Pointer=" + i + ",Count=1:PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + ",Memory:EPC,Pointer=" + i + ",Count=1:FAILED");
                        psResult.println("" + TestID + ",Memory:EPC,Pointer=" + i + ",Count=1:FAILED");
                        failureCount++;
                    }
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psLog.println("<br>" + TestID + ",Memory:EPC,Pointer=" + i + ",Count=1:FAILED");
                    psResult.println("" + TestID + ",Memory:EPC,Pointer=" + i + ",Count=1:FAILED");
                    failureCount++;
                    break;
                }


            }//retry loop
        }//for loop
        psLog.println("<br>-------------------------------------------------");
        psLog.println("<br><br>Varying the word count and pointer = 0");
        psLog.println("<br>-------------------------------------------------");


        psLog.println("<br>getWriteDataLength: " + writeParams.getWriteDataLength());
        for (int i = 2; i <= 12; i = i + 2) {

            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteEvent on ");


            for (int retry = 0; retry < 3; retry++) {

                if (WriteEvent(MEMORY_BANK.MEMORY_BANK_EPC, 6, i, writeData, accessfilter) == 0) {
                    ReadEvent(MEMORY_BANK.MEMORY_BANK_EPC, 6, i, accessfilter);
                    psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(writeData, 0, i + 2)));
                    try{
                        readdata = getEvents.tagData.getMemoryBankData();
                    }
                    catch(Exception e){
                        psLog.println(""+e.getMessage());
                    }
                    psLog.println(" Actual Result:" + readdata);

                    if (tagCompare(TagId, readdata, i, 0) == 0) {
                        psLog.println("<br>" + TestID + ",Memory:Reserved,Pointer=0 Count=" + i + ":PASSED");
                        psResult.println("" + TestID + ",Memory:EPC,Pointer=0 Count=" + i + ":PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + ",Memory:Reserved,Pointer=0 Count=" + i + ":FAILED");
                        psResult.println("" + TestID + ",Memory:EPC,Pointer=0 Count=" + i + ":FAILED");
                        failureCount++;
                    }
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psLog.println("<br>" + TestID + ",Memory:Reserved,Pointer=0 Count=" + i + ":FAILED");
                    psResult.println("" + TestID + ",Memory:EPC,Pointer=0 Count=" + i + ":FAILED");
                    failureCount++;
                    break;
                }


            }//retry loop
        }//for loop
    }

    public void Test_WriteEventUser() {




        psLog.println("<br>-------------------------------------------------");
        psLog.println("<br><br>Varying the pointer and word count = 2 bytes");
        psLog.println("<br>-------------------------------------------------");


        for (int i = 0; i <= 56; i = i + 2) {

            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteEvent on ");
            for (int retry = 0; retry < 3; retry++) {


                if (WriteEvent(MEMORY_BANK.MEMORY_BANK_USER, i, 2, writeData, accessfilter) == 0) {
//                    getLastError();
                    ReadEvent(MEMORY_BANK.MEMORY_BANK_USER, i, 2, accessfilter);
//                    getLastError();
                    psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(writeData, 0, 2)));
                    try{
                        readdata = getEvents.tagData.getMemoryBankData();
                    }
                    catch(Exception e){
                        psLog.println(""+e.getMessage());
                    }
                    psLog.println(" Actual Result:" + readdata);
                    if (tagCompare(TagId, readdata, 2, 0) == 0) {
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + ",Memory:USER,Pointer=" + i + ",Count=2:PASSED");
                        psResult.println("" + TestID + ",Memory:USER,Pointer=" + i + ",Count=2:PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>" + TestID + ",Memory:USER,Pointer=" + i + ",Count=1:FAILED");
                        psResult.println("" + TestID + ",Memory:USER,Pointer=" + i + ",Count=1:FAILED");
                        failureCount++;
                    }
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psLog.println("<br>" + TestID + ",Memory:USER,Pointer=" + i + ",Count=1:FAILED");
                    psResult.println("" + TestID + ",Memory:USER,Pointer=" + i + ",Count=1:FAILED");
                    failureCount++;
                    break;
                }



            }//retry loop
        }//for loop

        psLog.println("<br>--------------------------------------------");
        psLog.println("<br><br>Varying the word count and pointer = 0");
        psLog.println("<br>--------------------------------------------");

        for (int i = 2; i <= 64; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteEvent on ");

            for (int retry = 0; retry < 3; retry++) {

                if (WriteEvent(MEMORY_BANK.MEMORY_BANK_USER, 0, i, writeData, accessfilter) == 0) {
//                    getLastError();
                    ReadEvent(MEMORY_BANK.MEMORY_BANK_USER, 0, i, accessfilter);
//                    getLastError();
                    psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(writeData, 0, i)));
                    try{
                        readdata = getEvents.tagData.getMemoryBankData();
                    }
                    catch(Exception e){
                        psLog.println(""+e.getMessage());
                    }
                    psLog.println(" Actual Result:" + readdata);
                    if (tagCompare(TagId, readdata, i, 0) == 0) {
                        psLog.println("<br>" + TestID + ",Memory:USER,Count=" + i + ",Pointer=0:PASSED");
                        psResult.println("" + TestID + ",Memory:USER,Count=" + i + ",Pointer=0:PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>" + TestID + ",Memory:USER,Count=" + i + ",Pointer=0:FAILED");
                        psResult.println("" + TestID + ",Memory:USER,Count=" + i + ",Pointer=0:FAILED");
                        failureCount++;
                    }
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }


                if (retry == 2) {
                    psLog.println("<br>" + TestID + ",Memory:USER,Count=" + i + ",Pointer=0:FAILED");
                    psResult.println("" + TestID + ",Memory:USER,Count=" + i + ",Pointer=0:FAILED");
                    failureCount++;
                    break;
                }



            }//retry loop
        }
    }
    public void getLastError()
   {
       int[] pass = new int[1]; int[] fail = new int[1];
       try{
           reader.Actions.TagAccess.getLastAccessResult(pass, fail);
           psLog.println();
           psLog.println("\n\n PassCount = "+pass[0]+"     FailCount = "+fail[0]);
       }
       catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }

   }
}
