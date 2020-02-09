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
public class TagEvent extends Commonclass{

    private TriggerInfo tInfo;

    public TagEvent(){

        tInfo = new TriggerInfo();

        try {

            mystreamLog = new FileOutputStream("JavaAPI_TagEvent_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_TagEvent_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        } catch (FileNotFoundException e) {
            psLog.println("" + e.getMessage());

        }
    }
    private void setTagEventInfo(TAG_EVENT_REPORT_TRIGGER newtagEvent,short newModeTime,TAG_EVENT_REPORT_TRIGGER tagInvisible,short InvisTime,TAG_EVENT_REPORT_TRIGGER back2Visible,short back2VisTime)
    {
        tInfo.TagEventReportInfo.setReportNewTagEvent(newtagEvent);
        tInfo.TagEventReportInfo.setNewTagEventModeratedTimeoutMilliseconds(newModeTime);
        tInfo.TagEventReportInfo.setReportTagInvisibleEvent(tagInvisible);
        tInfo.TagEventReportInfo.setTagInvisibleEventModeratedTimeoutMilliseconds(InvisTime);
        tInfo.TagEventReportInfo.setReportTagBackToVisibilityEvent(back2Visible);
        tInfo.TagEventReportInfo.setTagBackToVisibilityModeratedTimeoutMilliseconds(back2VisTime);
        tInfo.setEnableTagEventReport(true);
    }
    public void TestRFIDTagEvents() {

        psLog.println("<html><br>");
        psLog.println("<body><br>");

        //Antenna Power settings
        AntennaPowersettings();

        successCount = 0;
        failureCount = 0;

        TestNo = 1;
        SubNo = 0;

        psLog.println("<br>RFIDTagEvents Test cases");
        
        //set only the Inventory stop event.
        try{
            reader.Actions.purgeTags();
            reader.Events.setInventoryStartEvent(false);
            reader.Events.setAccessStartEvent(false);
            reader.Events.setAccessStopEvent(false);
            reader.Events.setAntennaEvent(false);
            reader.Events.setBufferFullEvent(false);
            reader.Events.setBufferFullWarningEvent(false);
            reader.Events.setAttachTagDataWithReadEvent(true);
            reader.Events.setInventoryStopEvent(true);

            TagStorageSettings tagStorage = new TagStorageSettings();
            tagStorage.enableAccessReports(true);
            tagStorage.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            reader.Config.setTagStorageSettings(tagStorage);

            tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_DURATION);
            tInfo.StopTrigger.setDurationMilliSeconds(5000);
            tInfo.setTagReportTrigger(1);

            setTagEventInfo(TAG_EVENT_REPORT_TRIGGER.IMMEDIATE,(short)100,TAG_EVENT_REPORT_TRIGGER.IMMEDIATE,(short)100,TAG_EVENT_REPORT_TRIGGER.IMMEDIATE,(short)100);
            // for each of the tag event combination
            TAG_EVENT_REPORT_TRIGGER[] NewTag = {TAG_EVENT_REPORT_TRIGGER.IMMEDIATE,TAG_EVENT_REPORT_TRIGGER.MODERATED,TAG_EVENT_REPORT_TRIGGER.NEVER};
            for( int ReportNewTagEvent = 0; ReportNewTagEvent <3; ReportNewTagEvent++ )
            {
                for( int ReportTagBackToVisibilityEvent = 0; ReportTagBackToVisibilityEvent <3; ReportTagBackToVisibilityEvent++ )
                {
                    for (int ReportTagInvisibleEvent = 0; ReportTagInvisibleEvent < 3; ReportTagInvisibleEvent++)
                    {

                        tInfo.TagEventReportInfo.setReportNewTagEvent(NewTag[ReportNewTagEvent]);
                        tInfo.TagEventReportInfo.setReportTagBackToVisibilityEvent(NewTag[ReportTagBackToVisibilityEvent]);
                        tInfo.TagEventReportInfo.setReportTagInvisibleEvent(NewTag[ReportTagInvisibleEvent]);
                        FormTestID(TestNo++, SubNo, "TagEvents");
                        psLog.println("<br><br><br><b>Description:</b> Testing TagEvents Events(timeout :1Sec): TagEvents Info  ");
                        psLog.println("NEWTAG: "+tInfo.TagEventReportInfo.getReportNewTagEvent()+" BACK2VIS: "+tInfo.TagEventReportInfo.getReportTagBackToVisibilityEvent()+" TAGINVISIBLE: "+tInfo.TagEventReportInfo.getReportTagInvisibleEvent());
                        psLog.println("<br>Expected Result: Tags should be reported according to the trigger info set");

                        reader.Actions.Inventory.perform(null,tInfo,null);
                        Thread.sleep(7000);
                        reader.Actions.Inventory.stop();
                        TagData[] tags = reader.Actions.getReadTags(1000);
                        //tags[] = {"A22F22223333444455556666"};                 
                        if (ValidateTagEvents(tags, tInfo))
                        {
                            psLog.println("<br>" + TestID + ":TagEvents " + ":PASSED");
                            psResult.println("<br>" + TestID + ":TagEvents " + ":PASSED");
                            successCount++;
                        }
                        else
                        {
                            psLog.println("<br>" + TestID + ":TagEvents " + ":FAILED");
                            psResult.println("<br>" + TestID + ":TagEvents " + ":FAILED");
                            failureCount++;
                        }

                    }

                }
            }

            psSummary.println("JavaAPI:Test Tag Events( Vis,InVis,Back2Vis):" + successCount + ":" + failureCount + ":" + "0");
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
        catch(InterruptedException e)
        {
            CleanupPendingSequence();
            psLog.println("<br>\nInterruptedException"+e.getMessage());
        }




    }
    boolean ValidateTagEvents(TagData[] tags,TriggerInfo tInfo)
    {
        boolean bNewTagReport, bTagInvisReport, bTagBack2VisReport;
        bNewTagReport = bTagInvisReport = bTagBack2VisReport = false;
        // if Never,Never,Never set for tag events return true upon no tags are seen
        if (tags == null && (tInfo.TagEventReportInfo.getReportNewTagEvent() == TAG_EVENT_REPORT_TRIGGER.NEVER && tInfo.TagEventReportInfo.getReportTagBackToVisibilityEvent() == TAG_EVENT_REPORT_TRIGGER.NEVER &&
                                tInfo.TagEventReportInfo.getReportTagInvisibleEvent() == TAG_EVENT_REPORT_TRIGGER.NEVER))
            {
                return true;
            }
            else if (tags == null)
            {
                return false;
            }
            // Validate new tag event
            if (tInfo.TagEventReportInfo.getReportNewTagEvent() == TAG_EVENT_REPORT_TRIGGER.IMMEDIATE)
            {
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("<br>tag id: "+tags[i].getTagID()+" tagevent: "+tags[i].getTagEvent());
                    if (tags[i].getTagEvent() == TAG_EVENT.NEW_TAG_VISIBLE)
                    {
                        bNewTagReport = true;
                    }
                }
            }
            else if (tInfo.TagEventReportInfo.getReportNewTagEvent() == TAG_EVENT_REPORT_TRIGGER.MODERATED)
            {
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("<br>tag id: "+tags[i].getTagID()+" tagevent: "+tags[i].getTagEvent());
                    if (tags[i].getTagEvent() == TAG_EVENT.NEW_TAG_VISIBLE)
                    {
                        bNewTagReport = true;
                    }
                }

            }
            else if (tInfo.TagEventReportInfo.getReportNewTagEvent() == TAG_EVENT_REPORT_TRIGGER.NEVER)
            {
                bNewTagReport = true;
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("<br>tag id: "+tags[i].getTagID()+" tagevent: "+tags[i].getTagEvent());
                    if (tags[i].getTagEvent() == TAG_EVENT.NEW_TAG_VISIBLE)
                    {
                        bNewTagReport = false;
                    }
                }
            }
        // Validate tag invisible evetns
            if (tInfo.TagEventReportInfo.getReportTagInvisibleEvent() == TAG_EVENT_REPORT_TRIGGER.IMMEDIATE)
            {
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("<br>tag id: "+tags[i].getTagID()+" tagevent: "+tags[i].getTagEvent());
                    if (tags[i].getTagEvent() == TAG_EVENT.TAG_NOT_VISIBLE)
                    {
                        bTagInvisReport = true;
                    }
                }
            }
            else if (tInfo.TagEventReportInfo.getReportTagInvisibleEvent() == TAG_EVENT_REPORT_TRIGGER.MODERATED)
            {
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("<br>tag id: "+tags[i].getTagID()+" tagevent: "+tags[i].getTagEvent());
                    if (tags[i].getTagEvent() == TAG_EVENT.TAG_NOT_VISIBLE)
                    {
                        bTagInvisReport = true;
                    }
                }

            }
            else if (tInfo.TagEventReportInfo.getReportTagInvisibleEvent() == TAG_EVENT_REPORT_TRIGGER.NEVER)
            {
                bTagInvisReport = true;
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("<br>tag id: "+tags[i].getTagID()+" tagevent: "+tags[i].getTagEvent());
                    if (tags[i].getTagEvent() == TAG_EVENT.TAG_NOT_VISIBLE)
                    {
                        bTagInvisReport = false;
                    }
                }
            }
        // Validate tag back to visibility evetns
            if (tInfo.TagEventReportInfo.getReportTagBackToVisibilityEvent() == TAG_EVENT_REPORT_TRIGGER.IMMEDIATE)
            {

                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("<br>tag id: "+tags[i].getTagID()+" tagevent: "+tags[i].getTagEvent());
                    if (tags[i].getTagEvent() == TAG_EVENT.TAG_BACK_TO_VISIBILITY)
                    {
                        bTagBack2VisReport = true;
                    }
                }
            }
            else if (tInfo.TagEventReportInfo.getReportTagBackToVisibilityEvent() == TAG_EVENT_REPORT_TRIGGER.MODERATED)
            {
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("<br>tag id: "+tags[i].getTagID()+" tagevent: "+tags[i].getTagEvent());
                    if (tags[i].getTagEvent() == TAG_EVENT.TAG_BACK_TO_VISIBILITY)
                    {
                        bTagBack2VisReport = true;
                    }
                }

            }
            else if (tInfo.TagEventReportInfo.getReportTagBackToVisibilityEvent() == TAG_EVENT_REPORT_TRIGGER.NEVER)
            {
                bTagBack2VisReport = true;
                for(int i=0;i<tags.length;i++)
                {
                    psLog.println("<br>tag id: "+tags[i].getTagID()+" tagevent: "+tags[i].getTagEvent());
                    if (tags[i].getTagEvent() == TAG_EVENT.TAG_BACK_TO_VISIBILITY)
                    {
                        bTagBack2VisReport = false;
                    }
                }
            }






        return true;
    }


}
