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
public class WriteWait extends Commonclass {
    // RFIDReader myreader = reader;
    //static String tagId = beddTag;

    String tagId = "BEDD11112222333344445555";
    String TagId = "1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF";
    String readdata;
    boolean retrycount;
    //short writeData[] = {0x11,0x11,0x22,0x22,0x33,0x33,0x44,0x44};
    //byte writeData[] = {(byte)0xAA,(byte)0xAA,(byte)0xBB,(byte)0xBB,(byte)0xCC,(byte)0xCC,(byte)0xDD,(byte)0xDD,(byte)0xDD,(byte)0xDD,(byte)0xDD,(byte)0xDD,(byte)0xDD,(byte)0xDD,(byte)0xDD,(byte)0xDD};
    byte writeData[] = {(byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x33, (byte) 0x33, (byte) 0x44, (byte) 0x44, (byte) 0x55, (byte) 0x55, (byte) 0x66, (byte) 0x66, (byte) 0x77, (byte) 0x77, (byte) 0x88, (byte) 0x88, (byte) 0x99, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0xAA, (byte) 0xAA, (byte) 0xBB, (byte) 0xBB, (byte) 0xCC, (byte) 0xCC, (byte) 0xDD, (byte) 0xDD, (byte) 0xEE, (byte) 0xEE, (byte) 0xFF, (byte) 0xFF, (byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x33, (byte) 0x33, (byte) 0x44, (byte) 0x44, (byte) 0x55, (byte) 0x55, (byte) 0x66, (byte) 0x66, (byte) 0x77, (byte) 0x77, (byte) 0x88, (byte) 0x88, (byte) 0x99, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0xAA, (byte) 0xAA, (byte) 0xBB, (byte) 0xBB, (byte) 0xCC, (byte) 0xCC, (byte) 0xDD, (byte) 0xDD, (byte) 0xEE, (byte) 0xEE, (byte) 0xFF, (byte) 0xFF};

    public WriteWait() {
        try {
            mystreamLog = new FileOutputStream("JavaAPI_WriteWait_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_WriteWait_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        } catch (FileNotFoundException e) {
            psLog.println("" + e.getMessage());
        }
    }

    public void test_Writewait() {
        psLog.println("<html><br>");
        psLog.println("<body><br>");

        //Antenna Power settings
        AntennaPowersettings();

        successCount = 0;
        failureCount = 0;
        TestNo = 103;
        SubNo = 1;
        retrycount = false;

        psLog.println("<br>Write Wait Test cases");

        Test_WriteWaitReserved();
        //Test_WriteWaitEPC();
        Test_WriteWaitUser();

        // execute this only on the need basis, since more writes will reduce the life of the tag
        //
        //TestWriteSuccessRate();
        //Print into the summary sheet
        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:WriteWait test cases:" + successCount + ":" + failureCount + ":" + "0");
        psLog.println("</html>\n");
        psLog.println("</body>\n");
    }

    public void Test_WriteWaitReserved() {
        psLog.println("<br>-----------------------------------------------------------------------------------------");
        psLog.println("<br>varying the pointer(byteoffset) , word count byte = 2 For Reserve Memory Bank............");
        psLog.println("<br>-----------------------------------------------------------------------------------------");


        for (int i = 0; i < 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteWait on ");

            for (int retry = 0; retry < 3; retry++) {


                if (WriteWait(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, writeData, tagId) == 0) {
                    ReadWait(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, tagId);
                    retrycount = true;
                    psLog.println("<br>Expected Result: " + byte2Hex(read2OffSet(writeData, i, writeParams.getWriteDataLength())));
                    psLog.println(" Actual Result:" + readAccessTag.getMemoryBankData());
                    readdata = readAccessTag.getMemoryBankData();
                    if (tagCompare(TagId, readdata, 2, 0) == 0) {
                        psLog.println("<br>" + TestID + ":Memory Reserved,Pointer=" + i + ",Count=1:PASSED");
                        psResult.println("" + TestID + ":Memory Reserved,Pointer=" + i + ",Count=1:PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + ":Memory Reserved,Pointer=" + i + ",Count=1:FAILED");
                        psResult.println("" + TestID + ":Memory Reserved,Pointer=" + i + ",Count=1:FAILED");
                        failureCount++;
                    }

                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psLog.println("<br>" + TestID + ":Memory Reserved,Pointer=" + i + ",Count=1:FAILED");
                    psResult.println("" + TestID + ":Memory Reserved,Pointer=" + i + ",Count=1:FAILED");
                    failureCount++;
                    break;
                }


            }

        }

        psLog.println("<br>--------------------------------------------------------------------");
        psLog.println("<br><br>Varying the word count and pointer = 0 for Reserved memory bank");
        psLog.println("<br>--------------------------------------------------------------------");

        for (int i = 2; i <= 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteWait on ");

            for (int retry = 0; retry < 3; retry++) {

                if (WriteWait(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, writeData, tagId) == 0) {
                    ReadWait(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, tagId);
                    psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(writeData, 0, i)));
                    psLog.println(" Actual Result:" + readAccessTag.getMemoryBankData());
                    readdata = readAccessTag.getMemoryBankData();
                    if (tagCompare(TagId, readdata, i, 0) == 0) {
                        psLog.println("<br>" + TestID + ":Memory RESERVED,Count=" + i + ",Pointer=0:PASSED");
                        psResult.println("" + TestID + ":Memory RESERVED,Count=" + i + ",Pointer=0:PASSED");
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + ":Memory RESERVED,Count=" + i + ",Pointer=0:FAILED");
                        psResult.println("" + TestID + ":Memory RESERVED,Count=" + i + ",Pointer=0:FAILED");
                        failureCount++;
                    }
                    successCount++;
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }

                }
                if (retry == 2) {
                    psLog.println("<br>" + TestID + ":Memory USER,Count=" + i + ",Pointer=0:FAILED");
                    psResult.println("" + TestID + ":Memory USER,Count=" + i + ",Pointer=0:FAILED");
                    failureCount++;
                    break;
                }
            } //retry loop
        }//for
    }

    public void Test_WriteWaitUser() {
        // byte WriteDataUser[] = {0x11,0x11,0x22,0x22,0x33,0x33,0x44,0x44,0x55,0x55,0x66,0x66,0x77,0x77,0x88,0x88,0x99,0x99,(byte)0xAA};//,0xAA,0xBB,0xBB,0xCC,0xCC,0xDD,0xDD,0xEE,0xEE,0xFF,0xFF,0x11,0x11,0x22,0x22,0x33,0x33,0x44,0x44,0x55,0x55,0x66,0x66,0x77,0x77,0x88,0x88,0x99,0x99,0xAA,0xAA,0xBB,0xBB,0xCC,0xCC,0xDD,0xDD,0xEE,0xEE,0xFF,0xFF,0x11,0x11,0x22,0x22);

        psLog.println("<br>-----------------------------------------------------------------------------------------");
        psLog.println("<br>varying the pointer(byteoffset) , word count byte = 2 For USER Memory Bank............");
        psLog.println("<br>-----------------------------------------------------------------------------------------");


        for (int i = 0; i < 64; i = i + 4) {
            FormTestID(TestNo, SubNo++, "FUN");

            psLog.println("<br><b>Description:</b> WriteWait on :");

            for (int retry = 0; retry < 3; retry++) {


                if (WriteWait(MEMORY_BANK.MEMORY_BANK_USER, i, 2, writeData, tagId) == 0) {
                    ReadWait(MEMORY_BANK.MEMORY_BANK_USER, i, 2, tagId);
                    psLog.println("<br>Expected Result: 1111");
                    psLog.println(" Actual Result:" + readAccessTag.getMemoryBankData());
                    readdata = readAccessTag.getMemoryBankData();
                    if (tagCompare(TagId, readdata, 2, 0) == 0) {
                        psLog.println("<br>" + TestID + ":Memory USER,Pointer=" + i + ",Count=2:PASSED");
                        psResult.println("" + TestID + ":Memory USER,Pointer=" + i + ",Count=2:PASSED");
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + ":Memory USER,Pointer=" + i + ",Count=2:FAILED");
                        psResult.println("" + TestID + ":Memory USER,Pointer=" + i + ",Count=2:FAILED");
                        failureCount++;
                    }
                    successCount++;
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }
                if (retry == 2) {
                    psLog.println("<br>" + TestID + ":Memory USER,Pointer=" + i + ",Count=1:FAILED");
                    psResult.println("" + TestID + ":Memory USER,Pointer=" + i + ",Count=1:FAILED");
                    failureCount++;
                    break;
                }




            }//retry loop
        }

        // varying the word count and pointer = 0
        psLog.println("<br>--------------------------------------------------------------------");
        psLog.println("<br><br>Varying the word count and pointer = 0 for USER memory bank");
        psLog.println("<br>--------------------------------------------------------------------");

        for (int i = 2; i <= 64; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteWait on ");

            for (int retry = 0; retry < 3; retry++) {

                if (WriteWait(MEMORY_BANK.MEMORY_BANK_USER, 0, i, writeData, tagId) == 0) {
                    ReadWait(MEMORY_BANK.MEMORY_BANK_USER, 0, i, tagId);
                    psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(writeData, 0, i)));
                    psLog.println(" Actual Result:" + readAccessTag.getMemoryBankData());
                    readdata = readAccessTag.getMemoryBankData();
                    if (tagCompare(TagId, readdata, i, 0) == 0) {
                        psLog.println("<br>" + TestID + ":Memory USER,Count=" + i + ",Pointer=0:PASSED");
                        psResult.println("" + TestID + ":Memory USER,Count=" + i + ",Pointer=0:PASSED");
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + ":Memory USER,Count=" + i + ",Pointer=0:FAILED");
                        psResult.println("" + TestID + ":Memory USER,Count=" + i + ",Pointer=0:FAILED");
                        failureCount++;
                    }
                    successCount++;
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                    // Thread.sleep(1000);
                }
                if (retry == 2) {
                    psLog.println("<br>" + TestID + ":Memory USER,Count=" + i + ",Pointer=0:FAILED");
                    psResult.println("" + TestID + ":Memory USER,Count=" + i + ",Pointer=0:FAILED");
                    failureCount++;
                    break;
                }
            } //retry loop
        }//for

    }

    public void Test_WriteWaitEPC() {
        String tagId = "XXXXYYYYZZZZ1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF";

        psLog.println("<br>--------------------------------------------------------------------");
        psLog.println("<br><br>Varying the pointer and word count = 2bytes for EPC memory bank");
        psLog.println("<br>--------------------------------------------------------------------");

        for (int i = 6; i <= 12; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> WriteWait on ");

            for (int retry = 0; retry < 3; retry++) {
                if (WriteWait(MEMORY_BANK.MEMORY_BANK_USER, i, 2, writeData, tagId) == 0) {
                    ReadWait(MEMORY_BANK.MEMORY_BANK_USER, i, 2, tagId);
                    retrycount = true;
                    psLog.println("<br>Expected Result:" + byte2Hex(read2OffSet(writeData, i, 2)));
                    psLog.println(" Actual Result:" + readAccessTag.getMemoryBankData());
                    readdata = readAccessTag.getMemoryBankData();
                    if (tagCompare(tagId, readdata, 2, i) == 0) {
                        psLog.println("<br>" + TestID + ":Memory Reserved,Pointer=" + i + ",Count=1:PASSED");
                        psResult.println("" + TestID + ":Memory EPC,Pointer=" + i + ",Count=1:PASSED");
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + ":Memory EPC,Pointer=" + i + ",Count=1:FAILED");
                        psResult.println("" + TestID + ":Memory EPC,Pointer=" + i + ",Count=1:FAILED");
                        failureCount++;
                    }
                    successCount++;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psLog.println("<br>" + TestID + ":Memory EPC,Pointer=" + i + ",Count=1:FAILED");
                    psResult.println("" + TestID + ":Memory EPC,Pointer=" + i + ",Count=1:FAILED");
                    failureCount++;
                    break;
                }




            }//retry loop

        }//for

    }

    public void TestWriteSuccessRate() {
        byte[] writeData = {(byte) 0x44, (byte) 0x44, (byte) 0x44, (byte) 0x44, (byte) 0x88, (byte) 0x88, (byte) 0x88, (byte) 0x88};
        FormTestID(TestNo, SubNo++, "FUN");
        psLog.println("<br><b>Description:</b> WriteSuccessRate for 1000 writes on the tag" + tagId);
        writeParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
        writeParams.setByteOffset(0);
        writeParams.setWriteData(writeData);
        writeParams.setWriteDataLength(8);
        int SuccessCount = 0;
        int failureCount = 0;
        Java_getBEDDTag();
        tagId = beddTag;
        for (int i = 0; i < 100; i++) {
            try {
                reader.Actions.TagAccess.writeWait(tagId, writeParams, null);
                SuccessCount++;
            } catch (InvalidUsageException exp) {
                System.out.println("" + exp.getVendorMessage());
            } catch (OperationFailureException opexp) {
                failureCount++;
                System.out.println("" + opexp.getStatusDescription());
            }

        }
        psLog.println(" Actual Result: Total WriteCount: 1000" + "SuccessCount:" + SuccessCount + "failureCount:" + failureCount);
        psResult.println("Total WriteCount: 1000" + "SuccessCount:" + SuccessCount + "failureCount:" + failureCount);
    }
}
