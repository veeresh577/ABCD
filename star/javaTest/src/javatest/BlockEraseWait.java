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
public class BlockEraseWait extends Commonclass {

    String readdata;
    String tagId = "BEDD11112222333344445555";
    String TagId = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    byte EraseData[] = {
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,};
    boolean retrycount = false;

    public void Test_BlockEraseWait() {
        try {
            mystreamLog = new FileOutputStream("JavaAPI_BlockEraseWait_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_BlockEraseWait_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        } catch (FileNotFoundException e) 
        {
            psLog.println("" + e.getMessage());
            System.out.println(e.getMessage());
        }

        //Antenna Power settings
        AntennaPowersettings();
        successCount = 0;
        failureCount = 0;

        TestNo = 105;
        SubNo = 1;
        psLog.println("<br><br>Block Write Test cases");
        System.out.println("Block Write Test cases");
        



        psLog.println("<br>Block Erase Wait Test cases");
        //System.out.print("\nBlock Erase Wait Test cases");

        Test_BlockEraseWaitReserved();
        Test_BlockEraseWaitUser();

        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:BlockEraseWait testcases:" + successCount + ":" + failureCount + ":" + "0");
        System.out.println("JavaAPI:BlockEraseWait testcases:" + successCount + ":" + failureCount + ":" + "0");


    }

    public void Test_BlockEraseWaitReserved() {



        psLog.println("<br>==============================================================================");
        psLog.println("<br>Varying the  pointer and word count = 2 bytes for Reserved Bank");
        psLog.println("<br>==============================================================================");
        
        System.out.println("Varying the  pointer and word count = 2 bytes for Reserved Bank");

        for (int i = 0; i < 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> BlockEraseWait on ");
            System.out.println("Description:</b> BlockEraseWait on ");

            for (int retry = 0; retry < 3; retry++) {

                if (BlockEraseWait(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, tagId) == 0) {
                    readdata = readAccessTag.getMemoryBankData();
                    getLastError();
                    ReadWait(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, tagId);
                    getLastError();
                    
                    psLog.println("<br>Expected Result:0000");
                    readdata = readAccessTag.getMemoryBankData();
                    psLog.println(" Actual Result: " + readdata);
                    System.out.println(" Actual Result: " + readdata);
                    
                    if (tagCompare(TagId, readdata, 2, 0) == 0)
                    {
                        psResult.println(TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                        psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                        
                        System.out.println(TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                        System.out.println(TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                        
                        successCount++;
                    } 
                    else
                    {
                        psResult.println(TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                        psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                        
                        System.out.println(TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                        System.out.println(TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                        
                        failureCount++;
                    }
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }
                if (retry == 2)
                {
                    psResult.println(TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                    psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                    
                    System.out.println(TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                    System.out.println(TestID + ":BlockErase-Reserved" + "count 2 " + " Offset " + i + ":PASSED");
                    
                    failureCount++;
                }


            }// retry loop
        }//for loop Ending..........

        psLog.println("<br>============================================================================");
        psLog.println("<br> varying the word count and pointer = 0 For Reserved Memory Bank");
        psLog.println("<br>=============================================================================");
        
        System.out.println("varying the word count and pointer = 0 For Reserved Memory Bank");

        for (int i = 2; i <= 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> BlockEraseWait on ");
            System.out.println("Description:</b> BlockEraseWait on ");

            for (int retry = 0; retry < 3; retry++) {

                if (BlockEraseWait(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, tagId) == 0) {
                    getLastError();
                    ReadWait(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, tagId);
                    getLastError();
                    psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(EraseData, 0, i)));
                    readdata = readAccessTag.getMemoryBankData();
                    psLog.println(" Actual Result: " + readdata);
                    System.out.println("Actual Result: " + readdata);
                    if (tagCompare(TagId, readdata, i, 0) == 0)
                    {
                        psResult.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                        psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                        
                        System.out.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                        System.out.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                        
                        successCount++;
                    } 
                    else
                    {
                        psResult.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                        psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                        
                        System.out.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                        System.out.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");

                        failureCount++;
                    }
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }
                if (retry == 2)
                {
                    psResult.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                    psLog.println("<br>" + TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                    
                    System.out.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                    System.out.println(TestID + ":BlockErase-Reserved" + "offset 0 " + " count " + i + ":PASSED");
                    
                    failureCount++;
                }


            }//retry loop
        }
    }

    public void Test_BlockEraseWaitUser() {
        psLog.println("<br>=================================================================");
        psLog.println("<br> Block Erase For User Memory Bank.....................");
        psLog.println("<br> varying the pointer , word count = 2bytes For User Memory Bank");
        psLog.println("<br>===============================================================");

        System.out.println("Block Erase For User Memory Bank.....................");
        
        for (int i = 0; i <= 60; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> BlockEraseWait on ");


            for (int retry = 0; retry < 3; retry++) {
                boolean bSuccess = false;

                if (BlockEraseWait(MEMORY_BANK.MEMORY_BANK_USER, i, 2, tagId) == 0) {
                    getLastError();
                    ReadWait(MEMORY_BANK.MEMORY_BANK_USER, i, 2, tagId);
                    getLastError();
                    psLog.println("<br>Expected Result:Erased Memory Banks with Zero data. i.e 0000");
                    readdata = readAccessTag.getMemoryBankData();
                    psLog.println(" Actual Result: " + readdata);
                    System.out.println(" Actual Result: " + readdata);
                    if (tagCompare(TagId, readdata, 2, 0) == 0) {
                        psResult.println(TestID + ":BlockErase-User" + "count 2bytes " + " offset " + i + ":PASSED");
                        psLog.println("<br>" + TestID + ":BlockErase-USER" + "count 2bytes " + " offset " + i + ":PASSED");
                        System.out.println(TestID + ":BlockErase-USER" + "count 2bytes " + " offset " + i + ":PASSED");
                        
                        successCount++;
                    } else {
                        psResult.println(TestID + ":BlockErase-User" + "count 2bytes " + " offset " + i + ":FAILED");
                        psLog.println("<br>" + TestID + ":BlockErase-USER" + "count 2bytes " + " offset " + i + ":FAILED");
                        System.out.println(TestID + ":BlockErase-USER" + "count 2bytes " + " offset " + i + ":FAILED");
                        failureCount++;
                    }

                    bSuccess = true;

                    if (bSuccess == true) {
                        break;
                    }
                } //status
                if (retry == 2) {
                    psResult.println(TestID + ":BlockErase-User" + "count 2bytes " + " offset " + i + ":FAILED");
                    psLog.println("<br>" + TestID + ":BlockErase-USER" + "count 2bytes " + " offset " + i + ":FAILED");
                    System.out.println(TestID + ":BlockErase-USER" + "count 2bytes " + " offset " + i + ":FAILED");
                    failureCount++;
                }


            }//retry loop
        }

        psLog.println("<br>============================================================================");
        psLog.println("<br> varying the word count and pointer = 0 For User Memory Bank");
        psLog.println("<br>=============================================================================");


        for (int i = 2; i <= 64; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> BlockEraseWait on ");

            psLog.println("<br><br>getByteCount: " + EraseAccessParams.getByteCount());
            for (int retry = 0; retry < 3; retry++) {

                if (BlockEraseWait(MEMORY_BANK.MEMORY_BANK_USER, 0, i, tagId) == 0) {
                    getLastError();
                    ReadWait(MEMORY_BANK.MEMORY_BANK_USER, 0, i, tagId);
                    getLastError();

                    psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(EraseData, 0, i)));
                    readdata = readAccessTag.getMemoryBankData();
                    psLog.println(" Actual Result: " + readdata);
                    if (tagCompare(TagId, readdata, i, 0) == 0) {
                        psResult.println(TestID + ":BlockErase-User" + "offset 0 " + " count " + i + ":PASSED");
                        psLog.println("<br>" + TestID + ":BlockErase-User" + "offset 0 " + " count " + i + ":PASSED");
                        successCount++;
                    } else {
                        psResult.println(TestID + ":BlockErase-USER" + "offset 0 " + " count " + i + ":FAILED");
                        psLog.println("<br>" + TestID + ":BlockErase-USER" + "offset 0 " + " count " + i + ":FAILED");
                        failureCount++;
                    }
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }
                if (retry == 2) {
                    psResult.println(TestID + ":BlockErase-USER" + "offset 0 " + " count " + i + ":FAILED");
                    psLog.println("<br>" + TestID + ":BlockErase-USER" + "offset 0 " + " count " + i + ":FAILED");
                    failureCount++;
                }


            }//retry loop
        }

    }
      public void getLastError()
   {
       int[] pass = new int[1]; int[] fail = new int[1];
       try{
           TagData[] tags = reader.Actions.getReadTags(10);
           if( tags != null )
        {
            for(int tagCount = 0; tagCount < tags.length;tagCount++)
            {
                if( tags[tagCount].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
                {
                    psLog.println("\n EPC  :"+tags[tagCount].getTagID()+" memBank "+tags[tagCount].getMemoryBank()+"  memoryBank Data :"+ tags[tagCount].getMemoryBankData()+" length  :"+
                        tags[tagCount].getMemoryBankDataAllocatedSize()+ "Op code: "+tags[tagCount].getOpCode()+ "Op Status: "+tags[tagCount].getOpStatus());
                    psLog.println("<br>");
                    //isAccessSuccess = true;
                }
            }
            reader.Actions.purgeTags();
            //return isAccessSuccess;
        }
        else
        {
            reader.Actions.purgeTags();
            //return isAccessSuccess;
        }
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
