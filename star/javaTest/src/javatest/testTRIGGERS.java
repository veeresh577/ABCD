
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;
/**
 *
 * @author QMTN48
 */

class triggersClassListener implements RfidEventsListener
{
    private PrintStream psLog;
    ManualResetEvent Start;
    ManualResetEvent Stop;
    int tagCount;
    public triggersClassListener( PrintStream fileStream,ManualResetEvent invStart,ManualResetEvent invStop,int tgCnt)
    {
        psLog = fileStream;
        Start = invStart;
        Stop = invStop;
        tagCount = tgCnt;
    }
    private String PrintTime( SeenTime s)
    {
	SYSTEMTIME systime;
	String day =null;
	// Determine day of the week.

	if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 0)
	{
	   day ="Sunday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 1)
        {
            day ="Monday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 2)
        {
            day ="Tuesday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 3)
        {
            day ="Wednesday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 4)
        {
            day ="Thursday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 5)
        {
            day ="friday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 6)
        {
            day ="Sat";
        }

	day = day+s.getUTCTime().getFirstSeenTimeStamp().Year+"-"+s.getUTCTime().getFirstSeenTimeStamp().Month+"-"+s.getUTCTime().getFirstSeenTimeStamp().Day+"-"+s.getUTCTime().getFirstSeenTimeStamp().Hour+"-"+s.getUTCTime().getFirstSeenTimeStamp().Minute+"-"+
                s.getUTCTime().getFirstSeenTimeStamp().Second+"-"+s.getUTCTime().getFirstSeenTimeStamp().Milliseconds;
        return day;
    }
    
    public void eventReadNotify(RfidReadEvents rfidReadEvents)
    {
        System.out.print("READ EVENT Happened");
        if(rfidReadEvents!=null)
        {
            TagData tagData  = rfidReadEvents.getReadEventData().tagData;
            psLog.println("\n EPC  :"+tagData.getTagID()+" antenna ID "+tagData.getAntennaID()+" Seen time"+PrintTime(tagData.SeenTime)+" RSSI: "+tagData.getPeakRSSI()+"<br> \r\n");
            System.out.println(tagData.getTagID()+" antenna ID "+tagData.getAntennaID()+" Seen time"+PrintTime(tagData.SeenTime)+" RSSI: "+tagData.getPeakRSSI());
            tagCount++;
        }
    }
    
    @Override
    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
    {
        psLog.println("\n Event  :"+rfidStatusEvents.StatusEventData.getStatusEventType()+"time :"+System.currentTimeMillis()+"<br> \r\n");
        if( STATUS_EVENT_TYPE.INVENTORY_START_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            Start.set();
            //System.out.println("Inventory Start Event");
            psLog.println("Inventory Start Event");
            System.out.println("Inventory Start Event");
        }
        else if( STATUS_EVENT_TYPE.INVENTORY_STOP_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            Stop.set();
            psLog.println("<br>\n TagCount : "+tagCount);
            tagCount = 0;
            //System.out.println("Inventory Stop Event");
            psLog.println("Inventory Stop Event");
            System.out.println("Inventory Stop Event");
        }
    }
}

class eventsListener implements RfidEventsListener
{
    public boolean bEvent;
    ManualResetEvent invStart;
    ManualResetEvent invStop;
    ManualResetEvent accStart;
    ManualResetEvent accStop;
    ManualResetEvent tagRead;
    ManualResetEvent bufferfullevent;
    ManualResetEvent bufferfullWarningevent;
    private PrintStream psLog;
    int tagCount;
    public eventsListener( PrintStream fileStream,ManualResetEvent inventroyStart,ManualResetEvent inventroyStop,
            ManualResetEvent accessStart,ManualResetEvent accessStop,ManualResetEvent bufferfull,ManualResetEvent bufferfullwarning)
    {
        psLog = fileStream;
        invStart = inventroyStart;
        invStop = inventroyStop;
        accStart = accessStart;
        accStop = accessStop;
        bufferfullevent = bufferfull;
        bufferfullWarningevent = bufferfullwarning;
    }
    private String PrintTime( SeenTime s)
    {
	SYSTEMTIME systime;
	String day =null;
	// Determine day of the week.

	if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 0)
	{
	   day ="Sunday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 1)
        {
            day ="Monday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 2)
        {
            day ="Tuesday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 3)
        {
            day ="Wednesday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 4)
        {
            day ="Thursday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 5)
        {
            day ="friday";
        }else if(s.getUTCTime().getFirstSeenTimeStamp().DayOfWeek == 6)
        {
            day ="Sat";
        }

	day = day+s.getUTCTime().getFirstSeenTimeStamp().Year+"-"+s.getUTCTime().getFirstSeenTimeStamp().Month+"-"+s.getUTCTime().getFirstSeenTimeStamp().Day+"-"+s.getUTCTime().getFirstSeenTimeStamp().Hour+"-"+s.getUTCTime().getFirstSeenTimeStamp().Minute+"-"+
                s.getUTCTime().getFirstSeenTimeStamp().Second+"-"+s.getUTCTime().getFirstSeenTimeStamp().Milliseconds;
        return day;
    }

    @Override
    public void eventReadNotify(RfidReadEvents rfidReadEvents)
    {
        //System.out.print("READ EVENT Happened");
        if(rfidReadEvents!=null)
        {
            TagData tagData  = rfidReadEvents.getReadEventData().tagData;
            psLog.println("\n EPC  :"+tagData.getTagID()+" antenna ID "+tagData.getAntennaID()+" Seen time"+PrintTime(tagData.SeenTime)+" RSSI: "+tagData.getPeakRSSI()+"<br> \r\n");
            System.out.println("EPC  :"+tagData.getTagID()+" antenna ID "+tagData.getAntennaID()+" Seen time"+PrintTime(tagData.SeenTime)+" RSSI: "+tagData.getPeakRSSI());
            
            tagCount++;
        }
    }

    @Override
    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
    {
        psLog.println("\n Event  :"+rfidStatusEvents.StatusEventData.getStatusEventType()+"<br> \r\n");
        if( STATUS_EVENT_TYPE.INVENTORY_START_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            invStart.set();
            bEvent = true;
        }
        else if( STATUS_EVENT_TYPE.INVENTORY_STOP_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            invStop.set();
            bEvent = true;
        }
        else if( STATUS_EVENT_TYPE.ACCESS_START_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            accStart.set();
            bEvent = true;
        }
        else if( STATUS_EVENT_TYPE.ACCESS_STOP_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            accStop.set();
            bEvent = true;
        }
        else if( STATUS_EVENT_TYPE.BUFFER_FULL_WARNING_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            bufferfullWarningevent.set();
            bEvent = true;
        }
        else if( STATUS_EVENT_TYPE.BUFFER_FULL_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            bufferfullevent.set();
            bEvent = true;
        }
    }
}

public class testTRIGGERS extends Commonclass
{
    private RFIDReader trigReader;
    private TriggerInfo tInfo;
    private triggersClassListener tgListener;
    private eventsListener evListener;
    private ManualResetEvent iStart = new ManualResetEvent(false);
    private ManualResetEvent iStop = new ManualResetEvent(false);
    private ManualResetEvent aStart = new ManualResetEvent(false);
    private ManualResetEvent aStop = new ManualResetEvent(false);
    private ManualResetEvent bfull = new ManualResetEvent(false);
    private ManualResetEvent bWarning = new ManualResetEvent(false);
    int tagCount;

    public void LogSuccessFailureCount()
    {
        psSummary.println("JavaAPI:Test TRIGGERS and Reader Events:" + successCount + ":" + failureCount + ":" + "0");
    }
    public testTRIGGERS(RFIDReader reader)
    {
        trigReader = reader;
        tInfo = new TriggerInfo();
        tagCount =0;
        successCount = 0;
        failureCount = 0;
        try
        {
            mystreamLog=new FileOutputStream("Java API_TRIGGERS_Log.html");
            mystreamResult=new FileOutputStream("Java API_TRIGGERS_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b>"+" -------------Testing TRIGGERS Start Time :"+ (new Date()) +"------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "Testing TRIGGERS and EVENTS";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
        
    }
   
    public void testRFID3Triggers()
    {
        // Logger initialization for logging the results
        

        try
        {
            // make sure no listeners are there from the previous use of the Reader
            //
            trigReader.Config.resetFactoryDefaults();
            TagStorageSettings tgSettings = new TagStorageSettings();
            tgSettings = trigReader.Config.getTagStorageSettings();
            tgSettings.setMaxTagIDLength(12);
            tgSettings.setMaxMemoryBankByteCount(64);
            tgSettings.enableAccessReports(true);
            tgSettings.setMaxTagCount(10000);
            tgSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            trigReader.Config.setTagStorageSettings(tgSettings);
            tgListener = new triggersClassListener(psLog,iStart,iStop,tagCount);
            trigReader.Events.addEventsListener(tgListener);
            
            trigReader.Events.setInventoryStartEvent(true);
            trigReader.Events.setInventoryStopEvent(true);
            trigReader.Events.setTagReadEvent(true);
            trigReader.Events.setAttachTagDataWithReadEvent(true);
            
            trigReader.Events.setAccessStartEvent(true);
            
            //myReader.Events.addEventsListener(new EventsHandler(mainApp));
//            tgListener.notifyAll();
//            tgListener.notify();

            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_DURATION);
            tInfo.StopTrigger.setDurationMilliSeconds(5000);
            tInfo.setTagReportTrigger(0);
            TestNo = 1;SubNo = 0;
            FormTestID(TestNo++, SubNo, "Triggers");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Triggers Start = "+tInfo.StartTrigger.getTriggerType()+"  Stop:"+tInfo.StopTrigger.getTriggerType()
                    +"Duration(ms): "+tInfo.StopTrigger.getDurationMilliSeconds() );
            psLog.println( "<br><b>Expected Result </b>: Tags should be Read and Inventory should stop after duration");
            psLog.println( "<br><b>Actual Result </b>: <br>");
            trigReader.Actions.Inventory.perform(null, tInfo, null);
            iStart.waitOne();
            //startWait();
            long start = System.currentTimeMillis(); // requires java 1.5
            iStop.waitOne();
            // Segment to monitor
            double elapsedTimeInSec = (System.currentTimeMillis() - start)* 1.0e-3;
            iStart.reset();iStop.reset();

            //read tags with in Range
            if(  elapsedTimeInSec >= 5 && elapsedTimeInSec < 6 )
            {
                psLog.println( "<b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
                psLog.println("\n Test is Pass");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            else
            {
                psLog.println( "<b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }

//            // Start = Periodic with period 6000  run manually
//            SYSTEMTIME StartTime = new SYSTEMTIME();
//            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_PERIODIC);
//            String date[]=StartTime.GetCurrentTime().split("/");
////        2013/12/06/Friday/12/10/33/038
//        StartTime.Year = Short.parseShort(date[0]);
//        StartTime.Month = Short.parseShort(date[1]);
//        StartTime.Day = Short.parseShort(date[2]);
//        StartTime.DayOfWeek = Short.parseShort(date[2]);
//        StartTime.Hour = Short.parseShort(date[4]);
//        StartTime.Minute = Short.parseShort(date[5]);
//        StartTime.Second = Short.parseShort(date[6]);
//        StartTime.Milliseconds = Short.parseShort(date[7]);
//            
//            tInfo.StartTrigger.Periodic.StartTime = StartTime;
////            System.out.print("\n Java"+ tInfo.StartTrigger.Periodic.StartTime.ConvertTimetoString());
//            tInfo.StartTrigger.Periodic.setPeriod(6000);
//            tInfo.StopTrigger.setDurationMilliSeconds(3000);
//
//            FormTestID(TestNo++, SubNo, "Triggers");
//            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
//            psLog.println("<br><b>Description:</b> Triggers Start = "+tInfo.StartTrigger.getTriggerType()+"  Stop:"+tInfo.StopTrigger.getTriggerType()
//                    +"Duration(ms): "+tInfo.StopTrigger.getDurationMilliSeconds() );
//            psLog.println( "<br><b>Expected Result </b>: Tags should be Read periodically");
//            psLog.println( "<br><b>Actual Result </b>: <br>");
//            iStart.reset();iStop.reset();
//            trigReader.Actions.Inventory.perform(null, tInfo, null);
//            iStart.waitOne();
//            start = System.currentTimeMillis(); // requires java 1.5
//            iStop.waitOne();
//            // Segment to monitor
//            elapsedTimeInSec = (System.currentTimeMillis() - start)* 1.0e-3;
//            iStart.reset();iStop.reset();
//
//            iStart.waitOne();
//            start = System.currentTimeMillis(); // requires java 1.5
//            iStop.waitOne();
//            // Segment to monitor
//            elapsedTimeInSec = (System.currentTimeMillis() - start)* 1.0e-3;
//            iStart.reset();iStop.reset();
//
//            //read tags with in Range
//            if( elapsedTimeInSec >= 3 && elapsedTimeInSec < 4 )
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is Pass");
//                psResult.println(TestID+"   "+logText+"    :PASS");
//                successCount++;
//            }
//            else
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is FAIL");
//                psResult.println(TestID+"   "+logText+"    :FAIL");
//                failureCount++;
//            }

//            //test both start and stop with immediate
//            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
//            tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
//            trigReader.Actions.purgeTags();
//            FormTestID(TestNo++, SubNo, "Triggers");
//            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
//            psLog.println("<br><b>Description:</b> Triggers Start = "+tInfo.StartTrigger.getTriggerType()+"  Stop:"+tInfo.StopTrigger.getTriggerType()
//                    +"Duration(ms): "+tInfo.StopTrigger.getDurationMilliSeconds() );
//            psLog.println( "<br><b>Expected Result </b>: Tags should be Read and Inventory should stop after Stop inv is issued");
//            psLog.println( "<br><b>Actual Result </b>: <br>");
//            trigReader.Actions.Inventory.perform(null, tInfo, null);
//            iStart.waitOne();
//            start = System.currentTimeMillis(); // requires java 1.5
//            Thread.sleep(2000);
//            trigReader.Actions.Inventory.stop();
//            iStop.waitOne();
//            elapsedTimeInSec = (System.currentTimeMillis() - start)* 1.0e-3;
//            iStart.reset();iStop.reset();
//            if( elapsedTimeInSec >= 2 && elapsedTimeInSec < 3 )
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is Pass");
//                psResult.println(TestID+"   "+logText+"    :PASS");
//                successCount++;
//            }
//            else
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is FAIL");
//                psResult.println(TestID+"   "+logText+"    :FAIL");
//                failureCount++;
//            }
/*
             start = System.currentTimeMillis(); // requires java 1.5
             elapsedTimeInSec = (System.currentTimeMillis() - start)* 1.0e-3;
            //test both start and stop as N attempts with time out
            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_N_ATTEMPTS_WITH_TIMEOUT);
            tInfo.StopTrigger.NumAttempts.setN((short)4);
            tInfo.StopTrigger.NumAttempts.setTimeout(5000);
            trigReader.Events.setAttachTagDataWithReadEvent(false);
            tInfo.setTagReportTrigger(0);
            AntennaInfo antInfo = new AntennaInfo();
            short[] antList = new short[]{1};
            antInfo.setAntennaID(antList);
            FormTestID(TestNo++, SubNo, "Triggers");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Triggers Start = "+tInfo.StartTrigger.getTriggerType()+"  Stop:"+tInfo.StopTrigger.getTriggerType()
                    +"Duration(ms): "+tInfo.StopTrigger.getDurationMilliSeconds() );
            psLog.println( "<br><b>Expected Result </b>: Tags should be Read and Inventory should stop n attempts");
            psLog.println( "<br><b>Actual Result </b>: <br>");
            iStop.reset();
            trigReader.Actions.Inventory.perform(null, tInfo, antInfo);
            iStop.waitOne();
            TagData[] tags = trigReader.Actions.getReadTags(1000);
            if( tags!= null)
            {
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("\n EPC  :"+tags[0].getTagID()+" antenna ID "+tags[0].getAntennaID()+" Seen Count"+tags[0].getTagSeenCount()+" RSSI: "+tags[0].getPeakRSSI()+"<br> \r\n");
                }
                
                if(tags[0].getTagSeenCount() <= 4)
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
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
           
            trigReader.Actions.purgeTags();
            //test both start and stop as N attempts with time out
            //
            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_TAG_OBSERVATION_WITH_TIMEOUT);
            tInfo.StopTrigger.TagObservation.setN((short)5);
            tInfo.StopTrigger.TagObservation.setTimeout(5000);
            FormTestID(TestNo++, SubNo, "Triggers");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Triggers Start = "+tInfo.StartTrigger.getTriggerType()+"  Stop:"+tInfo.StopTrigger.getTriggerType()
                    +"Duration(ms): "+tInfo.StopTrigger.getDurationMilliSeconds() );
            psLog.println( "<br><b>Expected Result </b>: Tags should be Read and Inventory should after n tags seen");
            psLog.println( "<br><b>Actual Result </b>: <br>");
            iStop.reset();
            trigReader.Actions.Inventory.perform(null, tInfo, null);
            iStop.waitOne();
            TagData[] tagsRead = trigReader.Actions.getReadTags(1000);
            
            if(tagsRead != null)
            {
                for(int i=0;i<tagsRead.length;i++)
                {
                    psLog.println("\n EPC  :"+tagsRead[0].getTagID()+" antenna ID "+tagsRead[0].getAntennaID()+" Seen Count"+tagsRead[0].getTagSeenCount()+" RSSI: "+tagsRead[0].getPeakRSSI()+"<br> \r\n");
                }
                int seenCnt = 0;
                for(int i=0;i<tagsRead.length;i++)
                {
                    seenCnt += tagsRead[i].getTagSeenCount();
                }

                if(seenCnt == 5)
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
            }
            else
            {
                psLog.println("\n Test is FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
*/
//           //test both start and stop as GPI with time out.
//            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
//            tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_HANDHELD_WITH_TIMEOUT);
//            tInfo.StopTrigger.Handheld.setHandheldTriggerEvent(HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED);
//            tInfo.StopTrigger.Handheld.setHandheldTriggerTimeout(5000);
//            FormTestID(TestNo++, SubNo, "Triggers");
//            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
//            psLog.println("<br><b>Description:</b> Triggers Start = "+tInfo.StartTrigger.getTriggerType()+"  Stop:"+tInfo.StopTrigger.getTriggerType()
//                    +"Duration(ms): "+tInfo.StopTrigger.Handheld.getHandheldTriggerTimeout());
//            psLog.println( "<br><b>Expected Result </b>: Tags should be Read and Inventory should stop GPI or timeout");
//            psLog.println( "<br><b>Actual Result </b>: <br>");
//            iStop.reset();
//            trigReader.Actions.Inventory.perform(null, tInfo, null);
//            start = System.currentTimeMillis(); // requires java 1.5
//            iStop.waitOne();
//            elapsedTimeInSec = (System.currentTimeMillis() - start)* 1.0e-3;
//            iStart.reset();iStop.reset();
//            if( elapsedTimeInSec >= 5 && elapsedTimeInSec < 7 )
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is Pass");
//                psResult.println(TestID+"   "+logText+"    :PASS");
//                successCount++;
//            }
//            else
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is FAIL");
//                psResult.println(TestID+"   "+logText+"    :FAIL");
//                failureCount++;
//            }
//
//             //test both start and stop as GPI with time out.
//            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
//            tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_HANDHELD_WITH_TIMEOUT);
//            tInfo.StopTrigger.Handheld.setHandheldTriggerEvent(HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED);
//            tInfo.StopTrigger.Handheld.setHandheldTriggerTimeout(5000);
//            FormTestID(TestNo++, SubNo, "Triggers");
//            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
//            psLog.println("<br><b>Description:</b> Triggers Start = "+tInfo.StartTrigger.getTriggerType()+"  Stop:"+tInfo.StopTrigger.getTriggerType()
//                    +"Duration(ms): "+tInfo.StopTrigger.Handheld.getHandheldTriggerTimeout() );
//            psLog.println( "<br><b>Expected Result </b>: Tags should be Read and Inventory should stop GPI or timeout");
//            psLog.println( "<br><b>Actual Result </b>: <br>");
//            iStop.reset();
//            trigReader.Actions.Inventory.perform(null, tInfo, null);
//            start = System.currentTimeMillis(); // requires java 1.5
//            iStop.waitOne();
//            elapsedTimeInSec = (System.currentTimeMillis() - start)* 1.0e-3;
//            iStart.reset();iStop.reset();
//            if( elapsedTimeInSec >= 5 && elapsedTimeInSec < 7 )
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is Pass");
//                psResult.println(TestID+"   "+logText+"    :PASS");
//                successCount++;
//            }
//            else
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is FAIL");
//                psResult.println(TestID+"   "+logText+"    :FAIL");
//                failureCount++;
//            }
//
//            //test both start and stop as GPI with time out.
//            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
//            tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_GPI_WITH_TIMEOUT);
//            tInfo.StopTrigger.GPI.setGPIEvent(true);
//            tInfo.StopTrigger.GPI.setPortNumber(2);
//            tInfo.StopTrigger.GPI.setTimeout(5000);
//            FormTestID(TestNo++, SubNo, "Triggers");
//            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
//            psLog.println("<br><b>Description:</b> Triggers Start = "+tInfo.StartTrigger.getTriggerType()+"  Stop:"+tInfo.StopTrigger.getTriggerType()
//                    +"Duration(ms): "+tInfo.StopTrigger.getDurationMilliSeconds() );
//            psLog.println( "<br><b>Expected Result </b>: Tags should be Read and Inventory should stop GPI or timeout");
//            psLog.println( "<br><b>Actual Result </b>: <br>");
//            trigReader.Actions.Inventory.perform(null, tInfo, null);
//            start = System.currentTimeMillis(); // requires java 1.5
//            iStop.waitOne();
//            elapsedTimeInSec = (System.currentTimeMillis() - start)* 1.0e-3;
//            iStart.reset();iStop.reset();
//            if( elapsedTimeInSec >= 5 && elapsedTimeInSec < 7 )
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is Pass");
//                psResult.println(TestID+"   "+logText+"    :PASS");
//                successCount++;
//            }
//            else
//            {
//                psLog.println( "<br><b>Duration the trigger Ran is(Sec):</b>:"+elapsedTimeInSec+"<br>");
//                psLog.println("\n Test is FAIL");
//                psResult.println(TestID+"   "+logText+"    :FAIL");
//                failureCount++;
//            }

            trigReader.Events.removeEventsListener(tgListener);
            trigReader.Config.resetFactoryDefaults();
            
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
        
    }

    public void testRFIDEvents()
    {
        try
        {
            // make sure no listeners are there from the previous use of the Reader
            //
            trigReader.Config.resetFactoryDefaults();
            TagStorageSettings tgSettings = new TagStorageSettings();
            tgSettings = trigReader.Config.getTagStorageSettings();
            tgSettings.setMaxTagIDLength(12);
            tgSettings.setMaxMemoryBankByteCount(64);
            tgSettings.enableAccessReports(true);
            tgSettings.setMaxTagCount(10000);
            tgSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            trigReader.Config.setTagStorageSettings(tgSettings);
            evListener = new eventsListener(psLog, iStart, iStop, aStart, aStop, bfull, bWarning);
            trigReader.Events.addEventsListener(evListener);
            TestNo = 3;SubNo = 0;
            FormTestID(TestNo, SubNo, "RFIDEvents");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Verify Inventory Start event" );
            psLog.println( "<br><b>Expected Result </b>: Inventory Start event should be triggered");
            psLog.println( "<br><b>Actual Result </b>: <br>");
            trigReader.Events.setInventoryStartEvent(true);
            iStart.reset();
            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_DURATION);
            tInfo.StopTrigger.setDurationMilliSeconds(3000);
            trigReader.Actions.Inventory.perform( null, tInfo, null);
            iStart.waitOne();
            Thread.sleep(5000);
            iStart.reset();
         //   trigReader.Events.setInventoryStartEvent(false);

            //read tags with in Range
            if(  evListener.bEvent == true )
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
            TestNo = 4;SubNo = 0;
            FormTestID(TestNo, SubNo++, "RFIDEvents");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Verify Inventory Stop event" );
            psLog.println( "<br><b>Expected Result </b>: Inventory Stop event should be triggered");
            psLog.println( "<br><b>Actual Result </b>: <br>");
            evListener.bEvent = false;
            trigReader.Events.setInventoryStopEvent(true);
            iStop.reset();
            trigReader.Actions.Inventory.perform( null, tInfo, null);
            iStop.waitOne();
            iStop.reset();
            trigReader.Events.setInventoryStopEvent(false);

            //read tags with in Range
            if(  evListener.bEvent == true )
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

            TestNo = 5;SubNo = 0;
            FormTestID(TestNo, SubNo++, "RFIDEvents");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Verify Access Start event" );
            psLog.println( "<br><b>Expected Result </b>: Access Start event should be triggered");
            psLog.println( "<br><b>Actual Result </b>: <br>");
            evListener.bEvent = false;
            trigReader.Events.setAccessStartEvent(true);
            TagAccess tagAccess = new TagAccess();
            TagAccess.ReadAccessParams rParams = tagAccess.new ReadAccessParams();
//            rParams.setByteCount(4);
//            rParams.setByteOffset(4);
//            rParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            aStart.reset();
            //trigReader.Actions.TagAccess.readEvent( rParams,null, null);
            //aStart.waitOne();
            //trigReader.Actions.TagAccess.OperationSequence.stopSequence();
            aStart.reset();
            ReadEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, 2, accessfilter);
            trigReader.Events.setAccessStartEvent(false);

            //read tags with in Range
            if(  evListener.bEvent == true )
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

            TestNo = 6;SubNo = 0;
            FormTestID(TestNo, SubNo++, "RFIDEvents");
            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
            psLog.println("<br><b>Description:</b> Verify Access Stop event" );
            psLog.println( "<br><b>Expected Result </b>: Access Stop event should be triggered");
            psLog.println( "<br><b>Actual Result </b>: <br>");
            evListener.bEvent = false;
            trigReader.Events.setAccessStopEvent(true);
            aStop.reset();
            ReadEvent(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, 2, accessfilter);
            aStop.reset();
            trigReader.Events.setAccessStopEvent(false);

            //read tags with in Range
            if(  evListener.bEvent == true )
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
            TestNo = 11;SubNo = 0;

//            FormTestID(TestNo, SubNo++, "RFIDEvents");
//            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
//            psLog.println("<br><b>Description:</b> Verify Bufferfull warning event" );
//            psLog.println( "<br><b>Expected Result </b>: bufferfull warning event should be triggered");
//            psLog.println( "<br><b>Actual Result </b>: <br>");
//            evListener.bEvent = false;
//            trigReader.Events.setBufferFullWarningEvent(true);
//            tgSettings.setMaxTagCount(10);
//            trigReader.Config.setTagStorageSettings(tgSettings);
//            bWarning.reset();
//            iStart.reset();
//            trigReader.Actions.Inventory.perform();
//            bWarning.waitOne();
//            bWarning.reset();
//            iStart.waitOne();
////            Thread.sleep(5000);
//            iStart.reset();
//            trigReader.Actions.Inventory.stop();
//            trigReader.Events.setBufferFullWarningEvent(false);
//
//            //read tags with in Range
//            if(  evListener.bEvent == true )
//            {
//                psLog.println("\n Test is Pass");
//                psResult.println(TestID+"   "+logText+"    :PASS");
//                successCount++;
//            }
//            else
//            {
//                psLog.println("\n Test is FAIL");
//                psResult.println(TestID+"   "+logText+"    :FAIL");
//                failureCount++;
//            }

//             FormTestID(TestNo, SubNo++, "RFIDEvents");
//            psLog.println("\n<br><a>TestCase No:%s</a> <br>"+TestID);
//            psLog.println("<br><b>Description:</b> Verify Bufferfull  event" );
//            psLog.println( "<br><b>Expected Result </b>: bufferfull  event should be triggered");
//            psLog.println( "<br><b>Actual Result </b>: <br>");
//            evListener.bEvent = false;
//            trigReader.Events.setBufferFullEvent(true);
//            tgSettings.setMaxTagCount(10);
//            trigReader.Config.setTagStorageSettings(tgSettings);
//            trigReader.Actions.Inventory.perform();
//            bWarning.waitOne();
//            bWarning.reset();
//            trigReader.Actions.Inventory.stop();
//            trigReader.Events.setBufferFullEvent(false);
//
//            //read tags with in Range
//            if(  evListener.bEvent == true )
//            {
//                psLog.println("\n Test is Pass");
//                psResult.println(TestID+"   "+logText+"    :PASS");
//            }
//            else
//            {
//                psLog.println("\n Test is FAIL");
//                psResult.println(TestID+"   "+logText+"    :FAIL");
//            }

            trigReader.Events.removeEventsListener(tgListener);
            trigReader.Config.resetFactoryDefaults();
            Thread.sleep(5000);

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

    }
}
