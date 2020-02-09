/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import com.mot.rfid.api3.*;
import java.io.*;

/**
 *
 * @author NVJ438
 */
public class BlockEraseEvent extends Commonclass {

    ReadEventlistener getEvents;
    byte EraseData[] = {
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,};
    
    String TagId = "0000000000000000000000000000000000000000"
            + "0000000000000000000000000000000000000000"
            + "0000000000000000000000000000000000000000"
            + "00000000";
    String readdata;
    
    public void Test_BlockEraseEvent() {



        try {
            mystreamLog = new FileOutputStream("JavaAPI_BlockEraseEvent_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_BlockEraseEvent_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        } catch (FileNotFoundException e)
        {
            psLog.println("" + e.getMessage());
            System.out.println("e.getMessage()");
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

        accessfilter.setRSSIRangeFilter(false);
        getEvents = new ReadEventlistener(reader, psLog);
        reader.Events.addEventsListener(getEvents);
        reader.Events.setTagReadEvent(true);
        reader.Events.setAttachTagDataWithReadEvent(true);

        //Antenna Power settings
        AntennaPowersettings();

        psLog.println("<br><br>Block Erase Test cases");
        System.out.println(" Block Erase Test cases");



        successCount = 0;
        failureCount = 0;
        TestNo = 106;
        SubNo = 1;

        psLog.println("<br>Block Erase Event Test cases");
        System.out.println(" Block Erase Event Test cases");
        
        //System.out.print("\nBlock Erase Event Test cases");
        Test_BlockEraseEventReserved();
        Test_BlockEraseEventUser();
        reader.Events.removeEventsListener(getEvents);
        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:BlockEraseEvent testcases:" + successCount + ":" + failureCount + ":" + "0");
        System.out.println(" JavaAPI:BlockEraseEvent testcases:" + successCount + ":" + failureCount + ":" + "0");


    }

    public void Test_BlockEraseEventReserved() {

        System.out.println("<br>================================================================");
        psLog.println("<br>varying the pointer , word count = 2 bytes For Reserved Memory Bank");
        psLog.println("<br>================================================================");
        
        System.out.println(" ================================================================");
        System.out.println(" varying the pointer , word count = 2 bytes For Reserved Memory Bank");
        System.out.println(" ================================================================");



        for (int i = 0; i < 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            System.out.println(" Description:</b> BlockEraseEvent on ");
            System.out.println(" Description:</b> BlockEraseEvent on ");
            if (BlockEraseEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, accessfilter) == 0) {
                getLastError();
                ReadEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, accessfilter);
                getLastError();
                
        
                psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(EraseData, i, 2)));
                readdata = getEvents.tagData.getMemoryBankData();
                psLog.println(" Actual Result:" + readdata);
                System.out.println(" Actual Result:" + readdata);
                if (tagCompare(TagId, readdata, 2, 0) == 0) {
                    psResult.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                    psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                    
                    System.out.println("" + TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                    System.out.println(" " + TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                    
                    successCount++;
                } else {
                    psResult.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":FAILED");
                    psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":FAILED");
                    
                    System.out.println("" + TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":FAILED");
                    System.out.println("" + TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":FAILED");
                    
                    failureCount++;
                }

            }
        }//for loop Ending..........
        psLog.println("<br>=================================================================");
        psLog.println("<br> varying the word count and pointer = 0 For Reserved Memory Bank");
        psLog.println("<br>=================================================================");
        
        
        System.out.println(" varying the word count and pointer = 0 For Reserved Memory Bank");
        System.out.println(" =================================================================");


        for (int i = 2; i <= 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> BlockEraseEvent on ");
            System.out.println(" Description: BlockEraseEvent on ");
            
            if (BlockEraseEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, accessfilter) == 0) {
                getLastError();
                ReadEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, accessfilter);
                getLastError();
                psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(EraseData, i, 2)));
                System.out.println(" Expected Result:" + byte2Hex(read2OffSet(EraseData, i, 2)));
                
                readdata = getEvents.tagData.getMemoryBankData();
                psLog.println(" Actual Result:" + readdata);
                System.out.println(" Actual Result:" + readdata);
                
                if (tagCompare(TagId, readdata, i, 0) == 0) {
                    psResult.println(TestID + ":BlockErase-Reserved" + "count 2 " + " offset " + i + ":PASSED");
                    psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "count 2 " + " offset " + i + ":PASSED");
                    System.out.println("" + TestID + ":BlockErase-Reserved" + "count 2 " + " offset " + i + ":PASSED");
                    System.out.println("" + TestID + ":BlockErase-Reserved" + "count 2 " + " offset " + i + ":PASSED");
                    
                    successCount++;
                } else {
                    psResult.println(TestID + ":BlockErase-Reserved" + "count 2 " + " offset " + i + ":FAILED");
                    psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "count 2 " + " offset " + i + ":FAILED");
                    System.out.println("" + TestID + ":BlockErase-Reserved" + "count 2 " + " offset " + i + ":FAILED");
                    System.out.println("" + TestID + ":BlockErase-Reserved" + "count 2 " + " offset " + i + ":FAILED");
                    failureCount++;
                }
            }//status
        }//for loop Ending............
    }

    public void Test_BlockEraseEventUser() {
        psLog.println("<br>========================================================================================");
        psLog.println("<br> varying the pointer , word count = 2bytes For User Memory Bank");
        psLog.println("<br>==============================================================================");
        
        System.out.println(" varying the pointer , word count = 2bytes For User Memory Bank");
        

        for (int i = 0; i <= 60; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> BlockEraseEvent on ");
            System.out.println(" Description: BlockEraseEvent on ");
            if (BlockEraseEvent(MEMORY_BANK.MEMORY_BANK_USER, i, 2, accessfilter) == 0) {
                getLastError();
                ReadEvent(MEMORY_BANK.MEMORY_BANK_USER, i, 2, accessfilter);
                getLastError();
                psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(EraseData, i, 2)));
                System.out.println("Expected Result:" + byte2Hex(read2OffSet(EraseData, i, 2)));
                
                readdata = getEvents.tagData.getMemoryBankData();
           
                
                psLog.println(" Actual Result:" + readdata);
                System.out.println(" Actual Result:" + readdata);
                if (tagCompare(TagId, readdata, 2, 0) == 0) {
                    psResult.println(TestID + ":BlockEraseEvent-User" + "count 2 " + " offset " + i + ":PASSED");
                    psLog.println("<br>" + TestID + ":BlockEraseEvent-USER" + "count 2 " + " offset " + i + ":PASSED");
                    System.out.println("" + TestID + ":BlockEraseEvent-User" + "count 2 " + " offset " + i + ":PASSED");
                    System.out.println("" + TestID + ":BlockEraseEvent-USER" + "count 2 " + " offset " + i + ":PASSED");
                    successCount++;
                } else {
                    psResult.println(TestID + ":BlockEraseEvent-User" + "count 2 " + " offset " + i + ":FAILED");
                    psLog.println("<br>" + TestID + ":BlockEraseEvent-USER" + "count 2 " + " offset " + i + ":FAILED");
                    System.out.println("" + TestID + ":BlockEraseEvent-User" + "count 2 " + " offset " + i + ":FAILED");
                    System.out.println("" + TestID + ":BlockEraseEvent-USER" + "count 2 " + " offset " + i + ":FAILED");
                    failureCount++;
                }
            }

        int k = i;
        //System.out.println(""+k);
        }
        psLog.println("<br>----------------------------------------------------------------");
        psLog.println("<br><br>Varying the word count and pointer = 0 for USER memory bank");
        psLog.println("<br>-----------------------------------------------------------------");
        System.out.println(" Varying the word count and pointer = 0 for USER memory bank");

        for (int i = 8; i <= 64; i = i + 8) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> BlockEraseEvent on ");
            System.out.println("Description: BlockEraseEvent on ");
            if (BlockEraseEvent(MEMORY_BANK.MEMORY_BANK_USER, 0, i, accessfilter) == 0) {
                getLastError();
                ReadEvent(MEMORY_BANK.MEMORY_BANK_USER, 0, i, accessfilter);
                getLastError();
                psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(EraseData, 0, i)));
                System.out.println("Expected Result:" + byte2Hex(read2OffSet(EraseData, 0, i)));
                readdata = getEvents.tagData.getMemoryBankData();
                psLog.println(" Actual Result:" + readdata);
                System.out.println(" Actual Result:" + readdata);
                if (tagCompare(TagId, readdata, i, 0) == 0) {
                    psResult.println(TestID + ":BlockEraseEvent-User" + "offset 0 " + " count " + i + ":PASSED");
                    psLog.println("<br>" + TestID + ":BlockEraseEvent-User" + "offset 0 " + " count " + i + ":PASSED");
                    System.out.println(TestID + ":BlockEraseEvent-User" + "offset 0 " + " count " + i + ":PASSED");
                    System.out.println(TestID + ":BlockEraseEvent-User" + "offset 0 " + " count " + i + ":PASSED");
                    successCount++;
                } else {
                    psResult.println(TestID + ":BlockEraseEvent-User" + "offset 0 " + " count " + i + ":FAILED");
                    psLog.println("<br>" + TestID + ":BlockEraseEvent-User" + "offset 0 " + " count " + i + ":FAILED");
                    System.out.println(TestID + ":BlockEraseEvent-User" + "offset 0 " + " count " + i + ":FAILED");
                    System.out.println(TestID + ":BlockEraseEvent-User" + "offset 0 " + " count " + i + ":FAILED");
                    failureCount++;
                }
            }
        }

    }
}
