/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;
/**
 *
 * @author QMTN48
 */
class LocateListener implements RfidEventsListener
{
    public void eventReadNotify(RfidReadEvents rfidReadEvents)
    {
        //System.out.print("READ EVENT Happened");
        if(rfidReadEvents!=null)
        {
            TagData tagData  = rfidReadEvents.getReadEventData().tagData;
            System.out.print("\n Ant:"+tagData.getAntennaID()+" EPC : "+tagData.getTagID()+" LocateInfo : "+tagData.LocationInfo.getRelativeDistance());
        }
    }

    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
    {

        System.out.print("\nevent type :"+rfidStatusEvents.StatusEventData.getStatusEventType());
    }

}
public class Locationing extends Commonclass
{
    private RFIDReader myReader;
    private LocateListener listener;
    public Locationing( RFIDReader reader )
    {
        myReader = reader;
        listener = new LocateListener();
        
    }

    public void testLocationing()
    {
        try
        {
            // Logger initialization for logging the results
            try
            {
                mystreamLog=new FileOutputStream("Java API_Locationing_Log.html");
                mystreamResult=new FileOutputStream("Java API_Locationing_Result.txt");
                psLog = new PrintStream(mystreamLog);
                psLog.println("<HTML>\n<BODY>\n");
                psLog.println("<br><b>"+" -------------Testing DutyCycle and Locationing :"+ (new Date()) +"------------ </b><br>");
                psResult = new PrintStream(mystreamResult);
                logText = "Testing DutyCycle_Locationing";
            }
            catch(FileNotFoundException e)
            {
                psLog.println("\n "+e.getMessage());
            }
            
            TagData[] tag = new TagData[1];
            AntennaInfo antInfo = new AntennaInfo();
            short[] antList = new short[]{1};
            OPERATION_QUALIFIER[] opq = { OPERATION_QUALIFIER.LOCATE_TAG };
            antInfo.setAntennaID(antList);
            antInfo.setAntennaOperationQualifier(opq);
            TagStorageSettings tgSettings = new TagStorageSettings();
            tgSettings = myReader.Config.getTagStorageSettings();
            tgSettings.enableAccessReports(true);
            tgSettings.setMaxTagCount(1000);
            tgSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            myReader.Config.setTagStorageSettings(tgSettings);
            // DO inventory and get one tag for locationing
            //
            myReader.Actions.Inventory.perform();
            Thread.sleep(5000);
            myReader.Actions.Inventory.stop();
           
            tag = myReader.Actions.getReadTags(1);

            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Do Locationing on the tag"+tag[0].getTagID() );
            psLog.println( "<br><b>Expected Result </b>: Should get the Location Info for the Tag");
            psLog.println( "<br><b>Actual Result </b>: <br>");

            if( tag != null)
            {
                myReader.Actions.purgeTags();
                myReader.Actions.TagLocationing.Perform(tag[0].getTagID(), antInfo);
                Thread.sleep(10000);
                myReader.Actions.TagLocationing.Stop();
                TagData tags[] = myReader.Actions.getReadTags(1000);
                
                if( tags == null)
                {
                    psLog.println( "<b>NO Tags reported:</b> <br>");
                    psLog.println("\n Test is FAIL");
                    psResult.println(TestID+"   "+logText+"    :FAIL");
                }
                else
                {
                    for(int numberOfTags = 0; numberOfTags < tags.length; numberOfTags++ )
                    {
                        psLog.println("\n EPC  :"+tags[numberOfTags].getTagID()+" antenna ID "+tags[numberOfTags].getAntennaID()+" rel dist :"+tags[numberOfTags].LocationInfo.getRelativeDistance()+"<br> \r\n");
                    }
                    psLog.println("\n Test is PASS");
                    psResult.println(TestID+"   "+logText+"    :PASS");
                }
            }
            else
            {
                System.out.print("\n No tags are Read");
            }

            // Do on the other antenna
            antList[0] = 2;
            antInfo.setAntennaID(antList);
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Do Locationing on the tag"+tag[0].getTagID() );
            psLog.println( "<br><b>Expected Result </b>: Should get the Location Info for the Tag");
            psLog.println( "<br><b>Actual Result </b>: <br>");

            if( tag != null)
            {
                myReader.Actions.purgeTags();
                myReader.Actions.TagLocationing.Perform(tag[0].getTagID(), antInfo);
                Thread.sleep(10000);
                myReader.Actions.TagLocationing.Stop();
                TagData tags[] = myReader.Actions.getReadTags(1000);

                if( tags == null)
                {
                    psLog.println( "<b>NO Tags reported:</b> <br>");
                    psLog.println("\n Test is FAIL");
                    psResult.println(TestID+"   "+logText+"    :FAIL");
                }
                else
                {
                    for(int numberOfTags = 0; numberOfTags < tags.length; numberOfTags++ )
                    {
                        psLog.println("\n EPC  :"+tags[numberOfTags].getTagID()+" antenna ID "+tags[numberOfTags].getAntennaID()+" rel dist :"+tags[numberOfTags].LocationInfo.getRelativeDistance()+"<br> \r\n");
                    }
                    psLog.println("\n Test is PASS");
                    psResult.println(TestID+"   "+logText+"    :PASS");
                }
            }

            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Do Locationing without tagID" );
            psLog.println( "<br><b>Expected Result </b>: Should get the Location Info for the Tag");
            psLog.println( "<br><b>Actual Result </b>: <br>");
            boolean bExp = false;
            try
            {
                myReader.Actions.TagLocationing.Perform("", antInfo);
            }

            catch(InvalidUsageException exp)
            {
                psLog.println("\n invalid usage exception"+ exp.getVendorMessage()+exp.getInfo());
                bExp = true;
            }
            catch(OperationFailureException opexp)
            {
                psLog.println("\n Op Failure exception"+opexp.getVendorMessage()+opexp.getStatusDescription());
                bExp = true;
            }
            if(bExp)
            {
                psLog.println("\n Test is PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
            }
 
        }
        
        catch(InvalidUsageException exp)
        {
            psLog.println("\n invalid usage exception"+ exp.getVendorMessage()+exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            psLog.println("\n Op Failure exception"+opexp.getVendorMessage()+opexp.getStatusDescription());
        }
        catch(InterruptedException opexp)
        {
            System.out.println(" inturrupted exception"+opexp.getMessage());
        }

    }
    
    public void testDutyCycle()
    {
        short SetDutyCycle = 0;
        short getDutyCycle;
        int arrayLength = myReader.ReaderCapabilities.getDutyCycleValues().length;
        short[] DutyCycleValues = new short[arrayLength];
        
        while( SetDutyCycle != arrayLength)
        {
            try
            {
                short dut = myReader.Config.getDutyCycleIndex();
                myReader.Config.setDutyCycleIndex(SetDutyCycle);
                getDutyCycle = myReader.Config.getDutyCycleIndex();
                if( SetDutyCycle  == getDutyCycle)
                {
                    FormTestID(TestNo, SubNo++, "FUN");
                    psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
                    psLog.println("<br><b>Description:</b> Duty Cycle Set  "+SetDutyCycle+" and Get index  "+getDutyCycle );
                    psLog.println( "<br><b>Expected Result </b>: Get and Set be equal and inventory should read the tags");
                    psLog.println( "<br><b>Actual Result </b>: <br>");
                    myReader.Actions.Inventory.perform();
                    Thread.sleep(3000);
                    myReader.Actions.Inventory.stop();
                    TagData[] tags = myReader.Actions.getReadTags(1000);
                    if( tags == null)
                    {
                        psLog.println("\n No tags Read");
                    }
                    else
                    {
                       for(int i=0;i<tags.length;i++)
                       {
                           psLog.println("<br>\n Tag EPC :"+tags[i].getTagID()+"\t RSSI Value  "+tags[i].getPeakRSSI());
                       }
                       System.out.print("<br>\n Tags Read   :"+tags.length);
                       myReader.Actions.purgeTags();
                       if(tags.length > 0)
                       {
                            psLog.println("\n Test is PASS");
                            psResult.println(TestID+"   "+logText+"    :PASS");
                       }
                       else
                       {
                            psLog.println("\n Test is FAIL");
                            psResult.println(TestID+"   "+logText+"    :FAIL");
                       }
                    }
                    SetDutyCycle++;
                    Thread.sleep(5000);
                }
            }
            catch(InvalidUsageException exp)
            {
                System.out.println( exp.getVendorMessage());
            }
            catch(OperationFailureException opexp)
            {
                System.out.println(opexp.getVendorMessage());
            }
            catch(InterruptedException opexp)
            {
                System.out.println(opexp.getMessage());
            }

        }
    }

}