/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import com.mot.rfid.api3.InvalidUsageException;
import com.mot.rfid.api3.OperationFailureException;
import com.mot.rfid.api3.RFIDReader;
import com.mot.rfid.api3.TagData;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import com.mot.rfid.api3.*;
import java.io.*;
import java.lang.Object.*;

/**
 *
 * @author WCV673
 */

class Reader1 extends Thread {

    public static FileOutputStream mystreamLog;
    public static PrintStream psLog = null;
    public static RFIDReader reader = null;

    public void run() { // override the run() method of 'Thread' class
        try {
            System.out.println("Reader1 Connected");
            mystreamLog = new FileOutputStream("Reader1_Log.html");
            psLog = new PrintStream(mystreamLog);
            //reader1.start();
            reader = new RFIDReader("10.17.131.138", 0, 5000);
            reader.connect();
            reader.Actions.Inventory.perform();
            Thread.sleep(5000);
            reader.Actions.Inventory.stop();
            TagData tagdata[] = reader.Actions.getReadTags(100);
            for (int i = 0; i < tagdata.length; i++) {
                psLog.println("<br>Reader1 - " + tagdata[i].getTagID() + ", Time - " + tagdata[i].SeenTime.getUTCTime());
                System.out.println("<br>Reader1 - " + tagdata[i].getTagID() + ", Time - " + tagdata[i].SeenTime.getUTCTime());
            }
            reader.disconnect();
        }  catch (Exception e) {
            psLog.println(e);
            System.out.println(e);
        }
    }
}

/* define a class 'B' which inherits from class 'Thread'
 * ( create 2nd thread )
 */
class Reader2 extends Thread {

    public static FileOutputStream mystreamLog;
    public static PrintStream psLog = null;
    public static RFIDReader reader = null;

    public void run() {
        try {
            System.out.println("Reader2 Connected");
            mystreamLog = new FileOutputStream("Reader1_Log.html");
            psLog = new PrintStream(mystreamLog);
            //reader1.start();
            reader = new RFIDReader("10.17.131.36", 0, 5000);
            reader.connect();
            reader.Actions.Inventory.perform();
            Thread.sleep(10000);
            reader.Actions.Inventory.stop();
            TagData tagdata[] = reader.Actions.getReadTags(100);
            for (int i = 0; i < tagdata.length; i++) {
                psLog.println("<br>Reader2 - " + tagdata[i].getTagID() + ", Time - " + tagdata[i].SeenTime.getUTCTime());
                System.out.println("<br>Reader2 - " + tagdata[i].getTagID() + ", Time - " + tagdata[i].SeenTime.getUTCTime());
            }
            reader.disconnect();
        } catch (Exception e) {
            psLog.println(e);
            System.out.println(e);
        }
    }
}

public class Multihost {
    Reader1 thread_a = new Reader1();
    Reader2 thread_b = new Reader2();
    
    public void Multihost(){
    thread_a.start();
        /* start the 1st thread ( start() is a method
                       defined in 'Thread' class ) */
    thread_b.start();
    }
}
