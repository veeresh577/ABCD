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
public class ReadEvent extends Commonclass {

    String tagId = "1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF";
    short TagID[] = {0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55, 0x66, 0x66, 0x77, 0x77, 0x88, 0x88, 0x99, 0x99, 0x00, 0x00, 0xaa, 0xaa, 0xbb, 0xbb, 0xcc, 0xcc, 0xdd, 0xdd, 0xee, 0xee, 0xff, 0xff, 0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55, 0x66, 0x66, 0x77, 0x77, 0x88, 0x88, 0x99, 0x99, 0x00, 0x00, 0xaa, 0xaa, 0xbb, 0xbb, 0xcc, 0xcc, 0xdd, 0xdd, 0xee, 0xee, 0xff, 0xff};
//    TagAccess tagaccess = new TagAccess();
//    TagAccess.ReadAccessParams readParams = tagaccess.new ReadAccessParams();
    //accessfilter = new AccessFilter();
    ReadEventlistener getEvents;
    boolean retrycount;
    String readdata;

    public ReadEvent() {
        try {
            mystreamLog = new FileOutputStream("JavaAPI_ReadEvent_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_ReadEvent_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        } catch (FileNotFoundException e) {
            psLog.println("" + e.getMessage());
        }

        accessfilter.setRSSIRangeFilter(false);
        getEvents = new ReadEventlistener(reader, psLog);
        reader.Events.addEventsListener(getEvents);
        reader.Events.setTagReadEvent(true);
        reader.Events.setAttachTagDataWithReadEvent(true);

        //Register for access start and stop events
        reader.Events.setAccessStartEvent(true);
        reader.Events.setAccessStopEvent(true);
        byte[] tagPattern1 = {(byte) 0x11, (byte) 0x11};
        byte[] tagMask = {(byte) 0xFF, (byte) 0xFF};
        accessfilter.TagPatternA.setBitOffset(32);
        accessfilter.TagPatternA.setTagPattern(tagPattern1);
        accessfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        accessfilter.TagPatternA.setTagMaskBitCount(tagPattern1.length * 8);
        accessfilter.TagPatternA.setTagPatternBitCount((2 * 8));
        accessfilter.TagPatternA.setTagMask(tagMask);
        accessfilter.setAccessFilterMatchPattern(FILTER_MATCH_PATTERN.A);
    }

     public void Test_ReadEvent() {

        psLog.println("<html><br>");
        psLog.println("<body><br>");

        //Antenna Power settings
        AntennaPowersettings();

        successCount = 0;
        failureCount = 0;

        TestNo = 102;
        SubNo = 1;
        retrycount = false;
        psLog.println("<br>Read Event Test cases");
        //System.out.print("\nRead Event Test cases");
        //Test_ReadEventReserved();
        //Test_ReadEventUser();
        //Test_ReadEventTID();
        Test_ReadEventEPC();
        reader.Events.removeEventsListener(getEvents);
        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:ReadEvent_Testcases" + ":" + successCount + ":" + failureCount + ":0");
        psLog.println("</html>\n");
        psLog.println("</body>\n");


    }

    public void Test_ReadEventReserved() {
        psLog.println("<br>--------------------------------------------------------------------");
        psLog.println("<br>varying the pointer and word count = 2Bytes For Reserve Memory Bank");
        psLog.println("<br>----------------------------------------------------------------------");
        for (int i = 0; i < 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadEvent on ");
            for (int retry = 0; retry < 3; retry++) {
                if (ReadEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, i, 2, accessfilter) == 0) {
                    psLog.println("<br>Expected Result:");
                    for (int k = i; k < i + 2; k++) {
                        psLog.print("" + Integer.toHexString(TagID[k]));
                    }
                    readdata = getEvents.tagData.getMemoryBankData();
                    psLog.println("<br>Actual Result: " + readdata);
                    if (tagCompare(tagId, readdata, 2, i) == 0) {
                        psResult.println(TestID + ":ReserveMemoryBank" + " offset " + i + " " + "count " + 2 + ":PASSED");
                        psLog.println("<br>" + TestID + ":ReserveMemoryBank" + " offset " + i + " " + "count " + 2 + ":PASSED");
                        successCount++;
                    } else {
                        psResult.println(TestID + ":ReserveMemoryBank " + " offset " + i + ":count " + 2 + ":FAILED");
                        psLog.println("<br>" + TestID + ":ReserveMemoryBank " + " offset " + i + ":count " + 2 + ":FAILED");
                        failureCount++;
                        psLog.println("<br>Read data is not matching");
                    }


                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psResult.println(TestID + ":ReserveMemoryBank " + " offset " + i + ":count " + 2 + ":FAILED");
                    psLog.println("<br>" + TestID + ":ReserveMemoryBank " + " offset " + i + ":count " + 2 + ":FAILED");
                    failureCount++;
                    break;
                }



            }
        }




        psLog.println("<br>---------------------------------------------------");
        psLog.println("<br>varying the word count and pointer = 0(ByteOffset) For Reserve Memory Bank");
        psLog.println("<br>---------------------------------------------------");
        for (int i = 2; i <= 8; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadEvent on ");


            for (int retry = 0; retry < 3; retry++) {


                if (ReadEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, i, accessfilter) == 0) {

                    psLog.println("<br>Expected Result:");
                    for (int k = 0; k < i; k++) {
                        psLog.print("" + Integer.toHexString(TagID[k]));
                    }

                    readdata = getEvents.tagData.getMemoryBankData();
                    psLog.println("<br>ActualResult: " + getEvents.tagData.getMemoryBankData());
                    if (tagCompare(tagId, readdata, i, 0) == 0) {
                        psResult.println(TestID + ":ReserveMemoryBank " + " Count " + i + " offset " + 0 + ":PASSED");
                        psLog.println("<br>" + TestID + ":ReserveMemoryBank" + " Count " + i + " offset " + 0 + ":PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psResult.println(TestID + ":ReserveMemoryBank " + " Count " + i + " offset " + 0 + ":FAILED");
                        psLog.println("<br>" + TestID + ":ReserveMemoryBank " + " Count " + i + " offset " + 0 + ":FAILED");
                        failureCount++;
                    }

                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }
                if (retry == 2) {
                    psResult.println(TestID + ":ReserveMemoryBank " + " Count " + i + " offset " + 0 + ":FAILED");
                    psLog.println("<br>" + TestID + ":ReserveMemoryBank " + " Count " + i + " offset " + 0 + ":FAILED");
                    failureCount++;
                    break;
                }


            }

        }

    }

    public void Test_ReadEventUser() {

        psLog.println("<br>--------------------------------------------------------------------------------");
        psLog.println("<br>varying the pointer(ByteOffset) , word count = 1 For User Memory Bank...........");
        psLog.println("<br>--------------------------------------------------------------------------------");

        for (int i = 0; i < 64; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadEvent on ");


            for (int retry = 0; retry < 3; retry++) {
                if (ReadEvent(MEMORY_BANK.MEMORY_BANK_USER, i, 2, accessfilter) == 0) {
                    psLog.println("<br>Expected Result:");
                }
                for (int k = i; k < i + 2; k++) {
                    psLog.print("" + Integer.toHexString(TagID[k]));
                }

                readdata = getEvents.tagData.getMemoryBankData();
                psLog.println(" Actual Result: " + readdata);
                if (tagCompare(tagId, readdata, 2, i) == 0) {
                    psResult.println(TestID + ":UserMemoryBank" + " offset " + i + " " + " count " + 2 + ":PASSED");
                    psLog.println("<br>" + TestID + ":UserMemoryBank" + " offset " + i + " " + " count " + 2 + ":PASSED");
                    successCount++;
                } else {
                    psLog.println("<br>Read data is not matching");
                    psResult.println(TestID + ":UserMemoryBank " + " offset " + i + " count " + 2 + ":FAILED");
                    psLog.println("<br>" + TestID + ":UserMemoryBank " + " offset " + i + " count " + 2 + ":FAILED");
                    failureCount++;
                }

                retrycount = true;
                if (retrycount == true) {
                    break;
                }

                if (retry == 2) {
                    psResult.println(TestID + ":UserMemoryBank " + " offset " + i + " count " + 2 + ":FAILED");
                    psLog.println("<br>" + TestID + ":UserMemoryBank " + " offset " + i + " count " + 2 + ":FAILED");
                    failureCount++;
                    break;
                }


            }
        }


        psLog.println("<br>---------------------------------------------------------------------------------");
        psLog.println("<br>varying the word count and pointer = 0(ByteOffset) For UserMemory Bank...........");
        psLog.println("<br>---------------------------------------------------------------------------------");


        for (int i = 2; i <= 64; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadEvent on ");


            for (int retry = 0; retry < 3; retry++) {



                if (ReadEvent(MEMORY_BANK.MEMORY_BANK_USER, 0, i, accessfilter) == 0) {
                    psLog.println("<br>Expected Result:");
                    for (int k = 0; k < i; k++) {
                        psLog.print("" + Integer.toHexString(TagID[k]));
                    }

                    readdata = getEvents.tagData.getMemoryBankData();
                    psLog.println("<br>Actual Result: " + readdata);
                    if (tagCompare(tagId, readdata, i, 0) == 0) {
                        psResult.println(TestID + ":UserMemoryBank " + " Count " + i + " offset " + 0 + ":PASSED");
                        psLog.println("<br>" + TestID + ":UserMemoryBank" + " Count " + i + " offset " + 0 + ":PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psResult.println(TestID + ":UserMemoryBank" + " Count " + i + " offset " + 0 + ":FAILED");
                        psLog.println("<br>" + TestID + ":UserMemoryBank" + " Count " + i + " offset " + 0 + ":FAILED");
                        failureCount++;

                    }

                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psResult.println(TestID + ":UserMemoryBank" + " Count " + i + " offset " + 0 + ":FAILED");
                    psLog.println("<br>" + TestID + ":UserMemoryBank" + " Count " + i + " offset " + 0 + ":FAILED");
                    failureCount++;
                    break;
                }


            }
        }
    }

    public void Test_ReadEventTID() {
        psLog.println("<br>---------------------------------------------------------------------------------");
        psLog.println("<br>varying the pointer and word count = 2(ByteOffset) For TID Memory Bank...........");
        psLog.println("<br>---------------------------------------------------------------------------------");



        for (int i = 0; i < 12; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadEvent on ");

            for (int retry = 0; retry < 3; retry++) {

                if (ReadEvent(MEMORY_BANK.MEMORY_BANK_TID, i, 2, accessfilter) == 0) {
                    psLog.println("<br>Actual Result: " + getEvents.tagData.getMemoryBankData());
                    psResult.println(TestID + ":TIDMemoryBank " + " offset " + i + " " + " count " + 2 + ":PASSED");
                    psLog.println("<br>" + TestID + ":TIDMemoryBank" + " offset " + i + " " + " count " + 2 + ":PASSED");
                    successCount++;
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

            }

        }
        psLog.println("<br>---------------------------------------------------------------------------------");
        psLog.println("<br>varying the word count and pointer = 0(ByteOffset) For TID Memory Bank...........");
        psLog.println("<br>---------------------------------------------------------------------------------");

        for (int i = 2; i < 12; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadEvent on ");


            for (int retry = 0; retry < 3; retry++) {

                if (ReadEvent(MEMORY_BANK.MEMORY_BANK_TID, 0, i, accessfilter) == 0) {
                    psLog.println("<br>Actual Result: " + getEvents.tagData.getMemoryBankData());
                    psResult.println(TestID + ":TIDMemoryBank " + " Count " + i + " offset " + 0 + ":PASSED");
                    psLog.println("<br>" + TestID + ":TIDMemoryBank " + " Count " + i + " offset " + 0 + ":PASSED");
                    successCount++;
                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psResult.println(TestID + ":TIDMemoryBank" + " Count " + i + " offset " + 0 + ":FAILED");
                    psLog.println("<br>" + TestID + ":TIDMemoryBank " + " Count " + i + " offset " + 0 + ":FAILED");
                    failureCount++;
                    break;
                }


            }

        }
    }

    public void Test_ReadEventEPC() {
        tagId = "XXXXYYYY1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF";


        psLog.println("<br>-------------------------------------------------------------------------");
        psLog.println("<br>varying the pointer(ByteOffset) , word count = 2 bytes For EPC Memory Bank...............");
        psLog.println("<br>-------------------------------------------------------------------------");

        for (int i = 4; i < 16; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadEvent on ");


            for (int retry = 0; retry < 3; retry++) {

                psLog.println("<br>Expected Result:");
                for (int k = i; k < i + 2; k++) {
                    psLog.print("" + Integer.toHexString(TagID[k]));
                }
                if (ReadEvent(MEMORY_BANK.MEMORY_BANK_EPC, i, 2, accessfilter) == 0) {
                    readdata = getEvents.tagData.getMemoryBankData();
                    psLog.println("<br>Actual Result: " + readdata);
                    if (tagCompare(tagId, readdata, 2, i) == 0) {
                        psResult.println(TestID + ":EPCMemoryBank" + " offset " + i + " " + " count " + 2 + ":PASSED");
                        psLog.println("<br>" + TestID + ":EPCMemoryBank" + " offset " + i + " " + " count " + 2 + ":PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psResult.println(TestID + ":EPCMemoryBank" + " offset " + i + " count " + 2 + ":FAILED");
                        psLog.println("<br>" + TestID + ":EPCMemoryBank " + " offset " + i + " count " + 2 + ":FAILED");
                        failureCount++;
                    }

                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psResult.println(TestID + ":EPCMemoryBank" + " offset " + i + " count " + 2 + ":FAILED");
                    psLog.println("<br>" + TestID + ":EPCMemoryBank " + " offset " + i + " count " + 2 + ":FAILED");
                    failureCount++;
                    break;
                }

            }
        }
        psLog.println("<br>--------------------------------------------------------------");
        psLog.println("<br>varying the word count and pointer = 6 For EPC Memory Bank......");
        psLog.println("<br>--------------------------------------------------------------");


        for (int i = 4; i < 14; i = i + 2) {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b> ReadEvent on ");


            for (int retry = 0; retry < 3; retry++) {


                psLog.println("<br>Expected Result:");
                for (int k = 0; k < i; k++) {
                    psLog.print("" + Integer.toHexString(TagID[k]));
                }
                if (ReadEvent(MEMORY_BANK.MEMORY_BANK_EPC, 4, i, accessfilter) == 0) {
                    readdata = getEvents.tagData.getMemoryBankData();
                    psLog.println("<br>Actual Result: " + readdata);
                    if (tagCompare(tagId, readdata, i, 4) == 0) {
                        psResult.println(TestID + ":EPCMemoryBank " + " Count " + i + "offset " + 0 + ":PASSED");
                        psLog.println("<br>" + TestID + ":EPCMemoryBank" + " Count " + i + "offset " + 0 + ":PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>Read data is not matching");
                        psResult.println(TestID + ":EPCMemoryBank " + " Count: " + i + "offset " + 0 + ":FAILED");
                        psLog.println("<br>" + TestID + ":EPCMemoryBank " + " Count: " + i + "offset " + 0 + ":FAILED");
                        failureCount++;
                    }


                    retrycount = true;
                    if (retrycount == true) {
                        break;
                    }
                }

                if (retry == 2) {
                    psResult.println(TestID + ":EPCMemoryBank " + " Count: " + i + "offset " + 0 + ":FAILED");
                    psLog.println("<br>" + TestID + ":EPCMemoryBank " + " Count: " + i + "offset " + 0 + ":FAILED");
                    failureCount++;
                    break;
                }


            }
        }

    }
}

