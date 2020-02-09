/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import java.util.Date;
import static javatest.Commonclass.logText;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.psSummary;
import static javatest.Commonclass.reader;
/**
 *
 * @author QMTN48
 */
public class RSSIFilter extends Commonclass
{
    private RFIDReader myReader;
    private short minRSSI,maxRSSI;
    private PostFilter postfilter;
    private RssiRangeFilter rssiRangeFilter;
    private TagPatternBase tpA;
    private TagPatternBase tpB;
    private byte[] tagMask = { (byte)0xFF, (byte)0xFF };
    private byte[] tagPattern1 = { 0x11, 0x11 };
    private byte[] tagPattern2 = { (byte)0xFF, (byte)0xFF };

    public void LogSuccessFailureCount()
    {
        psSummary.println("JavaAPI:Test RSSI Filters:" + successCount + ":" + failureCount + ":" + "0");
    }
    public RSSIFilter(RFIDReader reader)
    {
        successCount = 0;
        failureCount = 0;
        myReader = reader;
        postfilter = new PostFilter();
        rssiRangeFilter = new RssiRangeFilter();
        tpA = new TagPatternBase( );
        tpB = new TagPatternBase( );
        postfilter = new PostFilter( );

        //tag Pattern A
        tpA.setBitOffset(32);
        tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        tpA.setTagMask(tagMask);
        tpA.setTagMaskBitCount(16);
        tpA.setTagPattern(tagPattern1);
        tpA.setTagPatternBitCount(16);

        //tag pattern B
        tpB.setBitOffset(32);
        tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        tpB.setTagMask(tagMask);
        tpB.setTagMaskBitCount(16);
        tpB.setTagPattern(tagPattern2);
        tpB.setTagPatternBitCount(16);

         //post filter
        postfilter.TagPatternA = tpA;
        postfilter.TagPatternB = tpB;
        postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.NOTA_AND_NOTB);
        
    }
    
    private int ReadTags()
    {
        try
        {
            myReader.Actions.Inventory.perform(postfilter,null,null);
            Thread.sleep(10000);
            myReader.Actions.Inventory.stop();
            TagData[] tags = myReader.Actions.getReadTags(1000);
            if( tags == null)
            {
                return 0;
            }
            else
            {
                for(int i=0;i<tags.length;i++)
                {
                   psLog.println("\n EPC  :"+tags[i].getTagID()+" RSSI: "+tags[i].getPeakRSSI()+"<br> \r\n");
                }
                return tags.length;
            }
        }
        catch(InterruptedException e)
        {

        }
        catch(InvalidUsageException exp)
        {
            System.out.println(""+ exp.getVendorMessage());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(""+opexp.getStatusDescription());
        }
        return 0;
    }
    private boolean VerifyRSSI()
    {
        boolean bResult = true;
        try
        {
            myReader.Actions.Inventory.perform(postfilter, null, null);
            Thread.sleep(10000);
            myReader.Actions.Inventory.stop();
            TagData[] tags = myReader.Actions.getReadTags(10000);
            if(tags == null)
            {
                if(minRSSI == maxRSSI)
                    return true;
                else if((minRSSI+maxRSSI) == -1 && postfilter.RssiRangeFilter.getMatchRange() == MATCH_RANGE.OUTSIDE_RANGE )
                    return true;
                
                
            }
            else{    
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("\n EPC  :"+tags[i].getTagID()+" RSSI: "+tags[i].getPeakRSSI()+"<br> \r\n");
                    if(postfilter.RssiRangeFilter.getMatchRange() == MATCH_RANGE.WITHIN_RANGE)
                    {
                        if((tags[i].getPeakRSSI() < minRSSI) && (tags[i].getPeakRSSI() > maxRSSI))
                        {
                            bResult = false;
                        }
                    }

                    if(postfilter.RssiRangeFilter.getMatchRange() == MATCH_RANGE.OUTSIDE_RANGE)
                    {
                        if((tags[i].getPeakRSSI() > minRSSI) && (tags[i].getPeakRSSI() < maxRSSI))
                        {
                            bResult = false;
                        }
                    }

                    if(postfilter.RssiRangeFilter.getMatchRange() == MATCH_RANGE.GREATER_THAN_LOWER_LIMIT)
                    {
                        if(tags[i].getPeakRSSI() < minRSSI)
                        {
                            bResult = false;
                        }
                    }
                    if(postfilter.RssiRangeFilter.getMatchRange() == MATCH_RANGE.LOWER_THAN_UPPER_LIMIT)
                    {
                        if(tags[i].getPeakRSSI() > maxRSSI)
                        {
                            bResult = false;
                        }
                    }
                }
            }
            return bResult;
        }
        catch(InterruptedException e)
        {

        }
        catch(InvalidUsageException exp)
        {
            System.out.println(""+ exp.getVendorMessage());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(""+opexp.getStatusDescription());
        }
        return bResult;
    }

    private void setRSSIRange()
    {
        try
        {
            myReader.Actions.Inventory.perform();
            Thread.sleep(10000);
            myReader.Actions.Inventory.stop();
            TagData tagdata[] = myReader.Actions.getReadTags(100);
            short rssiArray[] = new short[tagdata.length];
            for (int i = 0; i < tagdata.length; i++)
            {
                rssiArray[i] = tagdata[i].getPeakRSSI();
            }
            myReader.Actions.purgeTags();
            short mxm, min;
            min = mxm = rssiArray[0];
            for (int i = 0; i < tagdata.length; i++)
            {
                if (rssiArray[i] > mxm)
                {
                    mxm = rssiArray[i];
                }

                if (rssiArray[i] < min)
                {
                    min = rssiArray[i];
                }
            }
            minRSSI = min;maxRSSI=mxm;
            psLog.println("<Br>Min RSSI is "+minRSSI+" Max RSSI is "+maxRSSI);
            psLog.println("<Br>Diff RSSI is "+ (minRSSI+maxRSSI));
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {

        }
    }
    
    public void TestRSSIFilter()
    {
        // Logger initialization for logging the results
        try
        {
            mystreamLog=new FileOutputStream("Java API_RSSIFIlter_Log.html");
            mystreamResult=new FileOutputStream("Java API_RSSIFIlter_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b>"+" -------------Testing RSSI Filter Start Time :"+ (new Date()) +"------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "Testing RSSI Filter";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
        if(myReader.isConnected())
        {
            TestNo = 1;
            setRSSIRange();
            rssiRangeFilter.setMatchRange(MATCH_RANGE.WITHIN_RANGE);
            rssiRangeFilter.setPeakRSSILowerLimit(minRSSI);
            rssiRangeFilter.setPeakRSSIUpperLimit(maxRSSI);

            postfilter.RssiRangeFilter = rssiRangeFilter;
            postfilter.setRSSIRangeFilter(true);
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +"min: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI with in the Range should only be Read");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI with in the Range should only be Read<br>");
            //read tags with in Range
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }

            try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}

            rssiRangeFilter.setMatchRange(MATCH_RANGE.OUTSIDE_RANGE);
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +"min: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI in out side the Range should only be Read");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI in out side the Range should only be Read<br>");
            //read tags with in Range
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
             try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}
            rssiRangeFilter.setMatchRange(MATCH_RANGE.GREATER_THAN_LOWER_LIMIT);
            //read tags with in Range
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +" minRSSI: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI greater than Lower Limit should only be Read");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI greater than Lower Limit should only be Read<br>");
            //read tags with in Range
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
             try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}
            rssiRangeFilter.setMatchRange(MATCH_RANGE.LOWER_THAN_UPPER_LIMIT);
            //read tags with in Range
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with  "+rssiRangeFilter.getMatchRange() +" minRSSI: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI Lower than upper limit should only be Read");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI Lower than upper Limit should only be Read<br>");
            //read tags with in Range
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
             try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}
            // set negative values for RSSI.
            rssiRangeFilter.setPeakRSSILowerLimit((short)-128);
            rssiRangeFilter.setPeakRSSIUpperLimit((short)127);
            rssiRangeFilter.setMatchRange(MATCH_RANGE.OUTSIDE_RANGE);
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +" minRSSI : "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI out side the Range should only be Read( No tags in this Case)");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI out side the Range should only be Read");
    //        if( ReadTags() > 0)
    //        {
    //            psLog.println("\n Test is FAIL");
    //            psResult.println(TestID+"   "+logText+"    :FAIL");
    //            failureCount++;
    //        }
    //        else
    //        {
    //            psLog.println("\n Test is Pass");
    //            psResult.println(TestID+"   "+logText+"    :PASS");
    //            successCount++;
    //        }
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
             try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}
             // set negative values for RSSI.
            rssiRangeFilter.setPeakRSSILowerLimit((short)-128);
            rssiRangeFilter.setPeakRSSIUpperLimit((short)127);
            rssiRangeFilter.setMatchRange(MATCH_RANGE.WITHIN_RANGE);
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +" min rssi: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI with in the Range should only be Read");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI with in the Range should only be Read<br>");
    //        if( ReadTags() > 0)
    //        {
    //            psLog.println("\n Test is Pass");
    //            psResult.println(TestID+"   "+logText+"    :PASS");
    //            successCount++;
    //        }
    //        else
    //        {
    //            psLog.println("\n Test is FAIL");
    //            psResult.println(TestID+"   "+logText+"    :FAIL");
    //            failureCount++;
    //        }
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
             try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}
            // set negative values for RSSI.
            rssiRangeFilter.setPeakRSSILowerLimit((short)0);
            rssiRangeFilter.setPeakRSSIUpperLimit((short)127);
            rssiRangeFilter.setMatchRange(MATCH_RANGE.WITHIN_RANGE);
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +" minRSSI: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI with in the Range should only be Read( no tags)");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI with in the Range should only be Read<br>");
    //        if( ReadTags() > 0)
    //        {
    //            psLog.println("\n Test is PASS");
    //            psResult.println(TestID+"   "+logText+"    :PASS");
    //            failureCount++;
    //        }
    //        else
    //        {
    //            psLog.println("\n Test is Fail");
    //            psResult.println(TestID+"   "+logText+"    :FAIL");
    //            successCount++;
    //        }
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
             try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}
            // set negative values for RSSI.
            rssiRangeFilter.setPeakRSSILowerLimit((short)-10);
            rssiRangeFilter.setPeakRSSIUpperLimit((short)0);
            rssiRangeFilter.setMatchRange(MATCH_RANGE.WITHIN_RANGE);
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +"min: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI with in the Range should only be Read( no tags)");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI with in the Range should only be Read<br>");
    //        if( ReadTags() > 0)
    //        {
    //            psLog.println("\n Test is FAIL");
    //            psResult.println(TestID+"   "+logText+"    :FAIL");
    //            failureCount++;
    //        }
    //        else
    //        {
    //            psLog.println("\n Test is Pass");
    //            psResult.println(TestID+"   "+logText+"    :PASS");
    //            successCount++;
    //        }
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
             try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}
            // set negative values for RSSI.
            rssiRangeFilter.setPeakRSSILowerLimit((short)-1);
            rssiRangeFilter.setPeakRSSIUpperLimit((short)0);
            rssiRangeFilter.setMatchRange(MATCH_RANGE.WITHIN_RANGE);
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +" min: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI with in the Range should only be Read( no tags)");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI with in the Range should only be Read<br>");
    //        if( ReadTags() > 0)
    //        {
    //            psLog.println("\n Test is FAIL");
    //            psResult.println(TestID+"   "+logText+"    :FAIL");
    //            failureCount++;
    //        }
    //        else
    //        {
    //            psLog.println("\n Test is Pass");
    //            psResult.println(TestID+"   "+logText+"    :PASS");
    //            successCount++;
    //        }
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
             try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}
            // set negative values for RSSI.
            rssiRangeFilter.setPeakRSSILowerLimit((short)-64);
            rssiRangeFilter.setPeakRSSIUpperLimit((short)-64);
            rssiRangeFilter.setMatchRange(MATCH_RANGE.WITHIN_RANGE);
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +"min: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI with in the Range should only be Read");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI with in the Range should only be Read<br>");
    //        if( ReadTags() > 0)
    //        {
    //            psLog.println("\n Test is Pass");
    //            psResult.println(TestID+"   "+logText+"    :PASS");
    //            successCount++;
    //        }
    //        else
    //        {
    //            psLog.println("\n Test is FAIL");
    //            psResult.println(TestID+"   "+logText+"    :FAIL");
    //            failureCount++;
    //        }
            if( VerifyRSSI() == true )
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
             try{ Thread.sleep(2000); 
            }catch(Exception e)
            {psLog.println(e.getMessage());}
            // set negative values for RSSI.
            rssiRangeFilter.setPeakRSSILowerLimit((short)-64);
            rssiRangeFilter.setPeakRSSIUpperLimit((short)-64);
            rssiRangeFilter.setMatchRange(MATCH_RANGE.OUTSIDE_RANGE);
            FormTestID(TestNo++, SubNo, "RSSIfilter");

            psLog.println("<br><b>Description:</b> RSSI Filter with"+rssiRangeFilter.getMatchRange() +" min: "+ rssiRangeFilter.getPeakRSSILowerLimit()+"max: "+ rssiRangeFilter.getPeakRSSIUpperLimit()+"<br>");
            psLog.println( "<br><b>Expected Result </b>: Tags that have RSSI out side the Range should only be Read");
            psLog.println( "<br><b>Actual Result </b>: Tags that have RSSI out side the Range should only be Read<br>");
            if( ReadTags() > 0)
            {
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
    //        

            psLog.println("<br><b>"+" -------------Testing RSSI Filter EndTime :"+ (new Date()) +"------------ </b><br>");
        }
    }
    
}
