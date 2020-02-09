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
public class Readwait extends Commonclass {

    //private TagData[] tagData;
//    TagAccess tagaccess = new TagAccess();
//    TagAccess.ReadAccessParams readParams = tagaccess.new ReadAccessParams();
    String TagID = "111122223333444455556666";
    String tagId = "1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF";
    String readdata;
    //short TagID[] = {0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55, 0x66, 0x66, 0x77, 0x77, 0x88, 0x88, 0x99, 0x99, 0x00, 0x00, 0xaa, 0xaa, 0xbb, 0xbb, 0xcc, 0xcc, 0xdd, 0xdd, 0xee, 0xee, 0xff, 0xff, 0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55, 0x66, 0x66, 0x77, 0x77, 0x88, 0x88, 0x99, 0x99, 0x00, 0x00, 0xaa, 0xaa, 0xbb, 0xbb, 0xcc, 0xcc, 0xdd, 0xdd, 0xee, 0xee, 0xff, 0xff};
    boolean retrycount;
    int Status;

    public Readwait() {
        try {
            mystreamLog = new FileOutputStream("JavaAPI_ReadWait_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_ReadWait_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        } catch (FileNotFoundException e) {
            psLog.println("" + e.getMessage());
        }
    }

    public void test_Readwait() {
        psLog.println("<html><br>");
        psLog.println("<body><br>");

        AntennaPowersettings();
        successCount = 0;
        failureCount = 0;
        TestNo = 101;
        SubNo = 1;
        retrycount = false;

        psLog.println("<br>Read Wait Test cases");
        //System.out.print("\nRead Wait Test cases");
        String tagId = "111122223333444455556666";

        //Test_ReadWaitReserved();
        Test_ReadWaitUser();
//        Test_ReadWaitTID();
//        Test_ReadWaitEPC();

        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:ReadWait_Testcases" + ":" + successCount + ":" + failureCount + ":0");
        psLog.println("</html>\n");
        psLog.println("</body>\n");
    }

    public void Test_ReadWaitReserved() {
        psLog.println("<br>-------------------------------------------------");
        psLog.println("<br>varying the pointer and wordcount = 2 For Reserve Memory Bank");
        psLog.println("<br>-------------------------------------------------");
        for (int i = 0; i < 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadWait on ");
            for (int retry = 0; retry < 3; retry++) {

                Status = ReadWait(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, TagID);
                retrycount = true;
                psLog.println("<br>Expected Result: " + TagID.substring(i * 2, (i * 2) + 4));
                if (Status == 0) {
                    readdata = readAccessTag.getMemoryBankData();
                    psLog.println("<br>Actual Result: " + readdata);

                    if (tagCompare(TagID, readdata, 2, i) == 0) {

                        psResult.println(TestID + "ReserveMemoryBank" + "offset: " + i + " " + ":count " + 2 + ":PASSED");
                        psLog.println("<br>" + TestID + "ReserveMemoryBank" + "offset: " + i + " " + ":count " + 2 + ":PASSED");
                        successCount++;


                    } else {
                        psResult.println(TestID + "ReserveMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                        psLog.println("<br>Read data is not matching");
                        psLog.println("<br>" + TestID + " ReserveMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                        failureCount++;
                    }

                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }
                if (retry == 2) {

                    psResult.println(TestID + "ReserveMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                    psLog.println("<br>" + TestID + " ReserveMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                    failureCount++;
                    break;
                }

            }//retry loop
        }

        psLog.println("<br>---------------------------------------------------");
        psLog.println("<br><br>varying the word count and pointer = 0 For Reserve Memory Bank");

        for (int i = 2; i <= 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadWait on ");
            for (int retry = 0; retry < 3; retry++) {


                Status = ReadWait(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, TagID);
                psLog.println("<br>Expected Result: Read " + readParams.getByteCount() + "bytes from offset " + readParams.getByteOffset() + " from " + readParams.getMemoryBank());
                if (Status == 0) {
                    readdata = readAccessTag.getMemoryBankData();
                    psLog.println("<br>i.e" + TagID.substring(0, (i * 2)));
                    psLog.println("<br>Actual Result: " + readAccessTag.getMemoryBankData());
                    if (tagCompare(TagID, readdata, i, 0) == 0) {
                        psResult.println(TestID + " ReserveMemoryBank " + "Count: " + i + ":offset " + 0 + ":PASSED");
                        psLog.println("<br>");
                        psLog.println(TestID + "ReserveMemoryBank" + "Count: " + i + ":offset " + 0 + ":PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psResult.println(TestID + " ReserveMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                        psLog.println("<br>" + TestID + "ReserveMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                        failureCount++;
                    }

                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {

                    psResult.println(TestID + "ReserveMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                    psLog.println("<br>" + TestID + "ReserveMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                    failureCount++;
                    break;
                }

            }//retry
        }//for loop
    }

    public void Test_ReadWaitUser() {
        // Readwait FOR USER MEMORY BANK...........................................

        psLog.println("<br>---------------------------------------------------");
        psLog.println("<br>varying the pointer , word count = 2 For User Memory  Bank.");
        psLog.println("<br>---------------------------------------------------");

        for (int i = 0; i < 64; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadWait on ");
            for (int retry = 0; retry < 3; retry++) {


                Status = ReadWait(MEMORY_BANK.MEMORY_BANK_USER, i, 2, TagID);
                psLog.println("<br>Expected Result: Read " + " bytes from offset " + readParams.getByteOffset() + " from " + readParams.getMemoryBank());
                psLog.println("<br>i.e" + tagId.substring(i * 2, (i * 2) + 4));
                if (Status == 0) {
                    psLog.println("Actual Result: " + readAccessTag.getMemoryBankData());
                    readdata = readAccessTag.getMemoryBankData();
                    if (tagCompare(tagId, readdata, 2, i) == 0) {
                        psResult.println(TestID + " UserMemoryBank" + "offset: " + i + " " + ":count " + 2 + ":PASSED");
                        psLog.println("<br>");
                        psLog.println(TestID + "UserMemoryBank" + "offset: " + i + " " + ":count " + 2 + ":PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psResult.println(TestID + "UserMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                        psLog.println("<br>" + TestID + "UserMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                        failureCount++;
                    }

                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }


                if (retry == 2) {
                    psResult.println(TestID + "UserMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                    psLog.println("<br>" + TestID + "UserMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                    failureCount++;
                    break;

                }



            }//retry
        }//for

//varying the word count and pointer = 0 For User Memory Bank......................

        psLog.println("<br>---------------------------------------------------------");
        psLog.println("<br>varying the word count , pointer = 0 For User Memory  Bank.");
        psLog.println("<br>---------------------------------------------------");

        for (int i = 2; i <= 64; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadWait on ");

            for (int retry = 0; retry < 3; retry++) {



                psLog.println("<br>Expected Result: Read " + readParams.getByteCount() + "bytes from off-set" + readParams.getByteOffset() + "in the MemoryBank" + readParams.getMemoryBank());
                psLog.println("<br>i.e" + tagId.substring(0, (i * 2)));

                Status = ReadWait(MEMORY_BANK.MEMORY_BANK_USER, 0, i, TagID);
                if (Status == 0) {
                    psLog.println("<br>Actual Result: " + readAccessTag.getMemoryBankData());
                    readdata = readAccessTag.getMemoryBankData();

                    if (tagCompare(tagId, readdata, i, 0) == 0) {
                        psResult.println(TestID + "UserMemoryBank " + "Count: " + i + ":offset " + 0 + ":PASSED");
                        psLog.println("<br>");
                        psLog.println(TestID + "UserMemoryBank" + "Count: " + i + ":offset " + 0 + ":PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psResult.println(TestID + " UserMemoryBank" + "Count: " + i + ":offset " + 0 + ":FAILED");
                        psLog.println("<br>");
                        psLog.println(TestID + " UserMemoryBank" + "Count: " + i + ":offset " + 0 + ":FAILED");
                        failureCount++;
                    }

                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psResult.println(TestID + " UserMemoryBank" + "Count: " + i + ":offset " + 0 + ":FAILED");
                    psLog.println("<br>");
                    psLog.println(TestID + "UserMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                    failureCount++;
                    break;
                }


            }//retry
        }//for

    }

    public void Test_ReadWaitTID() {

//varying the pointer , word count = 1 For TID Memory Bank..............

        psLog.println("<br>-------------------------------------------------");
        psLog.println("<br>varying the pointer and wordcount = 2 For TID Memory Bank");
        psLog.println("<br>-------------------------------------------------");


        for (int i = 0; i < 12; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadWait on ");
            for (int retry = 0; retry < 3; retry++) {



                Status = ReadWait(MEMORY_BANK.MEMORY_BANK_TID, i, 2, TagID);
                psLog.println("<br>Expected Result: Read " + readParams.getByteCount() + "bytes from off-set" + readParams.getByteOffset() + "in the MemoryBank" + readParams.getMemoryBank());
                if (Status == 0) {
                    psLog.println("<br>Actual Result: " + readAccessTag.getMemoryBankData());
                    psResult.println(TestID + " TIDMemoryBank " + "offset: " + i + " " + ":count " + 2 + ":PASSED");
                    psLog.println("<br>" + TestID + " TIDMemoryBank" + "offset: " + i + " " + ":count " + 2 + ":PASSED");

                    successCount++;
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psResult.println(TestID + "TIDMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                    psLog.println("<br>" + TestID + "TIDMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                    failureCount++;
                    break;
                }

            }

        }

        psLog.println("<br>-------------------------------------------------");
        psLog.println("<br>varying the wordcount and pointer = 0 For TID Memory Bank");
        psLog.println("<br>-------------------------------------------------");
        for (int i = 2; i <= 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadWait on ");
            for (int retry = 0; retry < 3; retry++) {

                Status = ReadWait(MEMORY_BANK.MEMORY_BANK_TID, 0, i, TagID);
                psLog.println("<br>Expected Result: Read " + readParams.getByteCount() + "bytes from off-set" + readParams.getByteOffset() + "in the MemoryBank" + readParams.getMemoryBank());
                if (Status == 0) {
                    psLog.println("<br>Actual Result: " + readAccessTag.getMemoryBankData());
                    psLog.println("<br>MemoryBankData: " + readAccessTag.getMemoryBankData());

                    psResult.println(TestID + "TIDMemoryBank " + "Count: " + i + ":offset " + 0 + ":PASSED");
                    psLog.println("<br>" + TestID + "TIDMemoryBank " + "Count: " + i + ":offset " + 0 + ":PASSED");

                    successCount++;
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }//status
                if (retry == 2) {
                    psResult.println(TestID + "TIDMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                    psLog.println("<br>" + TestID + "TIDMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                    failureCount++;
                    break;
                }
            }
        }

    }

    public void Test_ReadWaitEPC() {


        psLog.println("<br>--------------------------------------------------------------");
        psLog.println("<br>varying the pointer , word count = 2 bytes For EPC Memory Bank......");
        psLog.println("<br>--------------------------------------------------------------");
        TagID = "xxxxyyyy1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF";

        for (int i = 4; i < 16; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadWait on ");
            for (int retry = 0; retry < 3; retry++) {


                Status = ReadWait(MEMORY_BANK.MEMORY_BANK_EPC, i, 2, TagID);
                psLog.println("<br>Expected Result: Read " + readParams.getByteCount() + "bytes from off-set" + readParams.getByteOffset() + "in the MemoryBank" + readParams.getMemoryBank());
                psLog.println("<br>i.e., " + tagId.substring(i * 2, (i * 2) + 4));
                if (Status == 0) {
                    psLog.println("Actual Result: " + readAccessTag.getMemoryBankData());
                    readdata = readAccessTag.getMemoryBankData();
                    if (tagCompare(tagId, readdata, 2, i) == 0) {
                        psResult.println(TestID + " EPCMemoryBank" + "offset: " + i + " " + ":count " + 2 + ":PASSED");
                        psLog.println("<br>" + TestID + " EPCMemoryBank" + "offset: " + i + " " + ":count " + 2 + ":PASSED");
                        successCount++;
                    } else {
                        psResult.println(TestID + " EPCMemoryBank" + "offset: " + i + ":count " + 2 + ":FAILED");
                        psLog.println("<br>" + TestID + "EPCMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                        failureCount++;
                    }

                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }
                if (retry == 2) {
                    retrycount = false;
                    psResult.println(TestID + " EPCMemoryBank" + "offset: " + i + ":count " + 2 + ":FAILED");
                    psLog.println("<br>" + TestID + "EPCMemoryBank " + "offset: " + i + ":count " + 2 + ":FAILED");
                    failureCount++;
                    break;

                }


            }
        }


        psLog.println("<br>--------------------------------------------------------------");
        psLog.println("<br>varying the word count and pointer = 4 For EPC Memory Bank......");
        psLog.println("<br>--------------------------------------------------------------");

        for (int i = 2; i < 14; i = i + 2) {
            for (int retry = 0; retry < 3; retry++) {

                FormTestID(TestNo, SubNo++, "FUN");
                psLog.println("<br><b>Description:</b> ReadWait on ");
                Status = ReadWait(MEMORY_BANK.MEMORY_BANK_EPC, 4, i, TagID);
                psLog.println("<br>Expected Result: Read " + readParams.getByteCount() + "bytes from off-set" + readParams.getByteOffset() + "in the MemoryBank" + readParams.getMemoryBank());
                psLog.println("<br>i.e" + TagID.substring(8, 8 + (i * 2)));
                if (Status == 0) {
                    psLog.println("Actual Result: " + readAccessTag.getMemoryBankData());
                    readdata = readAccessTag.getMemoryBankData();
                    if (tagCompare(TagID, readdata, i, 4) == 0) {
                        psResult.println(TestID + "EPCMemoryBank " + "Count: " + i + ":offset " + 0 + ":PASSED");
                        psLog.println("<br>");
                        psLog.println(TestID + " EPCMemoryBank" + "Count: " + i + ":offset " + 0 + ":PASSED");
                        successCount++;
                    } else {
                        psResult.println(TestID + "EPCMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                        psLog.println("<br>" + TestID + "EPCMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                        failureCount++;
                    }


                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }

                }
                if (retry == 2) {
                    psResult.println(TestID + "EPCMemoryBank " + "Count: " + i + ":offset " + 0 + ":FAILED");
                    psLog.println("<br>" + TestID + "EPCMemoryBank" + "Count: " + i + ":offset " + 0 + ":FAILED");
                    failureCount++;
                    break;
                }

            }

        }

    }
}
