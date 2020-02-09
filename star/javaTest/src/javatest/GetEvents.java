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
 class GetEvents implements RfidEventsListener {

    int test;
    int count;
    public RFIDReader myReader = null;
    public PrintStream myLogger = null;

//RFIDReader reader;
    public GetEvents(RFIDReader reader,PrintStream psLog)
    {
        myReader = reader;
        myLogger = psLog;
    }


     

     public void eventReadNotify(RfidReadEvents rfidReadEvents)
     {
          myLogger.println("<br>READ EVENT Happened");
         if(rfidReadEvents!=null)
         {
             TagData tagData  = rfidReadEvents.getReadEventData().tagData;
            myLogger.println("<br> tags ID :  "+tagData.getTagID()+"<br>membank  :"+tagData.getMemoryBank()+" memBank Data:  "+tagData.getMemoryBankData()+"<br>opCode  :"+tagData.getOpCode()
                    +"<br>OpCode Status  :"+tagData.getOpStatus());
           // System.out.print("\n"+rfidReadEvents.getReadEventData().tagData.getTagID()+" count= "+count);
         }
//         try{
//         TagData tagdata[] = myReader.Actions.getReadTags(100);
//
//                if (tagdata != null)
//                {
//                    for (int i = 0; i < tagdata.length; i++)
//                    {
//                     count++;
//                     System.out.println(tagdata[i].getTagID());
//                     myLogger.println("<br>getTagID: "+tagdata[i].getTagID());
//                     myLogger.println("getMemoryBankData: "+tagdata[i].getMemoryBankData());
//
//                    }
//                }
//             }
//
//          catch(Exception e)
//           {
//               System.out.print("\nOperationFailureException"+e.getMessage());
//            //psLog.println("\nOperationFailureException"+e.getMessage());
//           }


     }
     
     @Override
     public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
        {
           // System.out.println(""+rfidStatusEvents.StatusEventData.getStatusEventType());
             System.out.println("<br>"+rfidStatusEvents.StatusEventData.getStatusEventType());
            //System.out.println(""+rfidStatusEvents.StatusEventData.AccessStartEventData.);
           // if(rfidStatusEvents.StatusEventData.getStatusEventType() == "ACCESS_STOP_EVENT")
           // System.out.println(""+rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent());
          //  System.out.print("\n NXP EAS Event"+rfidStatusEvents.StatusEventData.getStatusEventType());
          
           System.out.print(" \nalram Code :"+rfidStatusEvents.StatusEventData.NXPEASAlarmEventData.getEASAlarmCode().toString());
           System.out.print(" antenna ID :"+ rfidStatusEvents.StatusEventData.NXPEASAlarmEventData.getantennaID());
        }

}
