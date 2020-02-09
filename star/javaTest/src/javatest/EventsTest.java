/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
/**
 *
 * @author QMTN48
 */
class GetEventsListener implements RfidEventsListener
{
    String hostname;
    static int tagCount=0;
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    RFIDReader myReader;
    GetEventsListener(String host,RFIDReader reader)
    {
        myReader = reader;
        hostname = host;
        System.out.print("\n initialized with"+hostname);
    }

    public void eventReadNotify(RfidReadEvents rfidReadEvents)
    {

        //System.out.print("READ EVENT Happened");
        if(rfidReadEvents!=null)
        {
//            TagData tagdata[]=myReader.Actions.getReadTags(1000);
//            for(int i=0;i<tagdata.length;i++)
//            {
//                tagCount++;
//                TagData tagData = tagdata[i];
//                System.out.print("\n TAG "+tagData.getTagID());
//
//            }
            TagData tagData  = rfidReadEvents.getReadEventData().tagData;
            tagCount++;
            System.out.print("\n tags from "+hostname+"     "+tagData.getTagID());
        }
    }

    @Override
    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
    {

        //System.out.print("\nevent from"+hostname+"  "+rfidStatusEvents.StatusEventData.getStatusEventType());
        System.out.print("\n Tag Count"+tagCount);
        if( STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n HH event"+rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent());
        }
        else if( STATUS_EVENT_TYPE.GPI_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n GPI event Port: "+rfidStatusEvents.StatusEventData.GPIEventData.getGPIPort());
            System.out.print("\n GPI event Data: "+rfidStatusEvents.StatusEventData.GPIEventData.getGPIEventState());
        }
        else if( STATUS_EVENT_TYPE.ACCESS_START_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n Access Start event: "+rfidStatusEvents.StatusEventData.AccessStartEventData);
        }
        else if( STATUS_EVENT_TYPE.ACCESS_STOP_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n Access Stop event: "+rfidStatusEvents.StatusEventData.AccessStopEventData);
        }
        else if( STATUS_EVENT_TYPE.ANTENNA_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n antenna ID:"+rfidStatusEvents.StatusEventData.AntennaEventData.getAntennaID()+
                         "\tantenna event"+rfidStatusEvents.StatusEventData.AntennaEventData.getAntennaEvent());
        }
        else if( STATUS_EVENT_TYPE.BUFFER_FULL_WARNING_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n Buffer full warning:"+rfidStatusEvents.StatusEventData.getStatusEventType());
        }
        else if( STATUS_EVENT_TYPE.BUFFER_FULL_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n Buffer full event:");
        }
        else if( STATUS_EVENT_TYPE.DISCONNECTION_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
//           DISCONNECTION_EVENT_DATA di = rfidStatusEvents.StatusEventData.DisconnectionEventData.getDisconnectionEvent();
            //System.out.print("\n DisCon info:"+di.eventInfo.toString());
            DISCONNECTION_EVENT_TYPE di = rfidStatusEvents.StatusEventData.DisconnectionEventData.getDisconnectionEvent();
            System.out.print("\n DisCon info:"+di.toString());
//            try
//            {
//                myReader.reconnect();
//            }
//            catch(InvalidUsageException exp)
//            {
//                System.out.println( exp.getVendorMessage()+"    "+exp.getTimeStamp()+"   "+exp.getInfo());
//            }
//            catch(OperationFailureException opexp)
//            {
//                System.out.println(opexp.getVendorMessage()+"    "+opexp.getTimeStamp()+"   "+opexp.getStatusDescription());
//            }
        }
        else if( STATUS_EVENT_TYPE.INVENTORY_START_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n Inv Start event:"+rfidStatusEvents.StatusEventData.InventoryStartEventData );
        }
        else if( STATUS_EVENT_TYPE.INVENTORY_STOP_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n Inv Stop event:"+rfidStatusEvents.StatusEventData.InventoryStopEventData);
        }
        else if( STATUS_EVENT_TYPE.READER_EXCEPTION_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.print("\n Reader Exception:"+rfidStatusEvents.StatusEventData.ReaderExceptionEventData.getReaderExceptionEventInfo());
        }
//
    }

}
public class EventsTest
{
    GetEventsListener getEvents;
    private RFIDReader evReader;
    
    public EventsTest( RFIDReader reader )
    {
        evReader = reader;
    }

    void testReaderDisconnectEvent( )
    {
       try
        {
           evReader.disconnect();
           for(int i=0;i<100;i++)
           {
               //Thread.sleep(10000);
               if(i!=0)
               {
                   evReader.reconnect();
                   Antennas.SingulationControl s =evReader.Config.Antennas.getSingulationControl(1);
                   System.out.print("\n Session:"+s.getSession());
               //evReader.Config.resetFactoryDefaults();
               }
                if(i==0)
                {
                    evReader.connect();
                    
                    Antennas.SingulationControl s =evReader.Config.Antennas.getSingulationControl(1);
                    s.setSession(SESSION.SESSION_S2);

                    //evReader.Config.resetFactoryDefaults();
                getEvents = new GetEventsListener(evReader.getHostName(),evReader);
                evReader.Events.addEventsListener(getEvents);
                evReader.Events.setReaderDisconnectEvent(true);
                evReader.Events.setTagReadEvent(true);
                if(i==0)evReader.Actions.Inventory.perform();
               }
                Thread.sleep(10000);
                //evReader.disconnect();
            }
        }
        catch(InvalidUsageException exp)
        {
            System.out.println( exp.getVendorMessage()+"    "+exp.getTimeStamp()+"   "+exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(opexp.getVendorMessage()+"    "+opexp.getTimeStamp()+"   "+opexp.getStatusDescription());
        }
        catch(InterruptedException opexp)
        {
            System.out.println(opexp.getMessage());
        }
    }

    void testHandHeldEvent( )
    {
       try
        {
            getEvents = new GetEventsListener(evReader.getHostName(),evReader);
            evReader.Events.addEventsListener(getEvents);
            //evReader.Events.setHandheldEvent(true);
//            evReader.Events.setGPIEvent(true);
//            evReader.Events.setInventoryStartEvent(true);
//            evReader.Events.setInventoryStopEvent(true);
            evReader.Events.setReaderDisconnectEvent(true);
//            evReader.Events.setAntennaEvent(true);
//            evReader.Events.setBufferFullEvent(true);
//            evReader.Events.setBufferFullWarningEvent(true);
            //evReader.Events.setTagReadEvent(true);
            TagStorageSettings tgSettings = evReader.Config.getTagStorageSettings();
            tgSettings.setMaxTagCount(10);
            tgSettings.discardTagsOnInventoryStop(false);
            evReader.Actions.Inventory.perform();
            Thread.sleep(900000);
            evReader.Actions.Inventory.stop();
            TagData[] tags = evReader.Actions.getReadTags(1);
            if( tags != null)
            {
                evReader.Actions.TagAccess.readWait(tags[0].getTagID(), null, null);
            }
        }
        catch(InvalidUsageException exp)
        {
            System.out.println( exp.getVendorMessage() + exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(opexp.getVendorMessage()+ opexp.getStatusDescription());
        }
        catch(InterruptedException opexp)
        {
            System.out.println(opexp.getMessage());
        }
    }

    void testEventsSet( RFIDReader reader )
    {

       boolean accessStart,accessStop,InvStart,InvStop,hhEvent,gpiEvent,disconnect;
       try
        {
            reader.connect();
            if((accessStart = reader.Events.isAccessStartEventSet()))
            {
                System.out.println("access Start : "+accessStart);
            }

            if((accessStop = reader.Events.isAccessStopEventSet()))
            {
                System.out.println("access Stop : "+accessStop);
            }

            if((InvStart = reader.Events.isInventoryStartEventSet()))
            {
                System.out.println("Inv Start : "+InvStart);
            }

            if((InvStop = reader.Events.isInventoryStopEventSet()))
            {
                System.out.println("Inv Stop : "+InvStop);
            }
            
            if((hhEvent = reader.Events.isHandheldEventSet()))
            {
                System.out.println("HH Event : "+hhEvent);
            }
            reader.disconnect();
        }
        catch(InvalidUsageException exp)
        {
            System.out.println( exp.getVendorMessage());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(opexp.getVendorMessage());
        }
    }
}
