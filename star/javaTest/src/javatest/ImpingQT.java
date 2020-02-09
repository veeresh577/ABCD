/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

/**
 *
 * @author XDG734
 */

import com.mot.rfid.api3.*;
import com.mot.rfid.api3.Impinj;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.psSummary;
import static javatest.Commonclass.reader;

//import com.mot.rfid.api3.Impinj.QTWriteAccessParams;

public class ImpingQT extends Commonclass {
   
    ReadEventlistener getEvents;
    boolean bsuccess = false;
   
    //Constructor
    public ImpingQT()
    {
        
        
        try
        {
            mystreamLog = new FileOutputStream("JavaAPI_ImpingQT_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_ImpingQT_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            //AppendText();
            successCount = 0;
            failureCount = 0;
            TestNo = 1;
            SubNo = 0;
        }
        catch (FileNotFoundException e)
        {
            psLog.println("" + e.getMessage());
        }
    }
    
    
    
    void Test_WriteControlDataOpSeq()
    {
           if(reader.isConnected())
           {
              
               // initialize the filter for which to do access on
            byte[] tagPattern1 = { (byte)0x30,(byte)0x08};
            byte[] tagMask = {(byte)0xFF,(byte)0xFF};
            accessfilter.TagPatternA.setBitOffset(32);
            accessfilter.TagPatternA.setTagPattern(tagPattern1);
            accessfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            accessfilter.TagPatternA.setTagMaskBitCount(16);
            accessfilter.TagPatternA.setTagPatternBitCount(16);
            accessfilter.TagPatternA.setTagMask(tagMask);
            accessfilter.setAccessFilterMatchPattern(FILTER_MATCH_PATTERN.A);
            accessfilter.setRSSIRangeFilter(false);
               
               Impinj impinj = new Impinj(tagaccess);
               Impinj.QTWriteAccessParams qtWriteParams = impinj.new QTWriteAccessParams();
               long AccessPasswor = 0x00000001;
               short m_QTControlData = 0x0000;
//               qtWrite
//               qtWriteParams.setAccessPassword(AccessPasswor);
//               qtWriteParams.setQTControlData(m_QTControlData);
//               qtWriteParams.setQTPersist(true);
//               TagAccess ta = new TagAccess();
//               TagAccess.Sequence seq = ta.new Sequence(ta);
//               TagAccess.Sequence.Operation op = seq.new Operation();
//               op.setAccessOperationCode(ACCESS_OPERATION_CODE.ACCESS_OPERATION_IMPINJ_QT_WRITE);
////               //qtread
               Impinj.QTReadAccessParams qtReadParams = impinj.new QTReadAccessParams();
               qtReadParams.setAccessPassword(0x00000001);
               TagAccess ta = new TagAccess();
               TagAccess.Sequence seq = ta.new Sequence(ta);
               TagAccess.Sequence.Operation op = seq.new Operation();
               op.setAccessOperationCode(ACCESS_OPERATION_CODE.ACCESS_OPERATION_IMPINJ_QT_READ);
               System.out.print("\nAccess Password "+qtReadParams.getAccessPassword());
//////               
//////               //read
//               TagAccess ta = new TagAccess();
//               TagAccess.Sequence seq = ta.new Sequence(ta);
//               TagAccess.Sequence.Operation op = seq.new Operation();
//               TagAccess.ReadAccessParams rParams = tagaccess.new ReadAccessParams();
//               rParams.setByteCount(2);
//               rParams.setByteOffset(4);
//               rParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
//               op.setAccessOperationCode(ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ);
                       
               try
               {
                   reader.Actions.purgeTags();
                   reader.Actions.TagAccess.OperationSequence.add(op);
                   reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, null, null);
//                    reader.Actions.TagAccess.OperationSequence.performSequence();
                   Thread.sleep(5000);
                  
                   reader.Actions.TagAccess.OperationSequence.stopSequence();
                   reader.Actions.TagAccess.OperationSequence.deleteAll();
                    TagData[] tags = reader.Actions.getReadTags(1000);
               if( tags != null)
               {
                  for(int i=0;i<tags.length;i++)
                {
                    //System.out.print("\n"+tagdata[i].getTagID());
                    int[] pass = new int[1]; int[] fail = new int[1];
                    reader.Actions.TagAccess.getLastAccessResult(pass, fail);
                    
                   if(ACCESS_OPERATION_STATUS.ACCESS_SUCCESS == tags[i].getOpStatus())
                   {
                       psLog.println("Tag Data written succesfully");
                       bsuccess = true;
                       break;
                   }
                   else
                   {
                       for(i=0;i<tags.length;i++)
                        {
                                //System.out.print("\n"+tagdata[i].getTagID());
                             psLog.print("\n"+tags[i].getTagID());
                        }
                    }
                   }
                }
               
                   else
                {
                //
                psLog.print("<br>No.of Tags read:"+tags.length);               
                 
                } 
               seq.delete(op);
               
               //Perform Inventory & Verify EPC length is 128 bit.
               if(bsuccess)
               {
                   reader.Actions.Inventory.perform( null, null, null);
                   Thread.sleep(5000);
                   reader.Actions.Inventory.stop();
                   
                   
               }      
             }
               catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("\nInvalidUsageException" + exp.getInfo());
           
        } catch (OperationFailureException exp) {
            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("\nOperationFailureException" + exp.getMessage());
            
        }
               catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
               
               
                

              
            }
            psSummary.println("JavaAPI:ImpinjQT_Testcases"+":"+successCount+":"+failureCount+":0");
//        psLog.close();
//        psResult.close();  
//            
    }
    
     void Test_WriteControlDataNormal()
    {
           if(reader.isConnected())
           {
               Impinj impinj = new Impinj(tagaccess);
               Impinj.QTWriteAccessParams qtWriteParams = impinj.new QTWriteAccessParams();
               Impinj.QTReadAccessParams qtReadParams = impinj.new QTReadAccessParams();
               long AccessPasswor = 0x00000001;
               short m_QTControlData = 0x0000;
               //short m_QTControlData = (short)0x8000;
//               short m_QTControlData = (short)0x4000;
               qtWriteParams.setAccessPassword(AccessPasswor);
               qtWriteParams.setQTControlData(m_QTControlData);
               qtWriteParams.setQTPersist(true);
               qtReadParams.setAccessPassword(1);
               TagAccess ta = new TagAccess();
                             try
               {
                   //F00D0001F00D0002F00D0003F00D0004
//                   reader.Actions.TagAccess.Impinj.QTWriteWait("F00D0001F00D0002F00D0003F00D0004", qtWriteParams, null);
                   TagData tg = reader.Actions.TagAccess.Impinj.QTReadWait("300833B2DDD9014000000000", qtReadParams, null);
//                    reader.Actions.TagAccess.Impinj.QTReadEvent(qtReadParams, accessfilter, null);
                   //getLastAccessResult(pass, fail);
                    int[] pass = new int[1]; int[] fail = new int[1];
                    reader.Actions.TagAccess.getLastAccessResult(pass, fail);
                    System.out.println(" Config Word Value is "+tg.AccessOperationResult.ImpinjQTData.getQTControlData());
                    TagData[] tags = reader.Actions.getReadTags(1);
               if( tags != null)
               {
                  for(int i=0;i<tags.length;i++)
                {
                    //System.out.print("\n"+tagdata[i].getTagID());
//                    int[] pass = new int[1]; int[] fail = new int[1];
//                    reader.Actions.TagAccess.getLastAccessResult(pass, fail);
                    
                   if(ACCESS_OPERATION_STATUS.ACCESS_SUCCESS == tags[i].getOpStatus())
                   {
                       psLog.println("Tag Data written succesfully");
                       bsuccess = true;
                       break;
                   }
                   else
                   {
                       for(i=0;i<tags.length;i++)
                        {
                                //System.out.print("\n"+tagdata[i].getTagID());
                             psLog.print("\n"+tags[i].getTagID());
                        }
                    }
                   }
                }
               
                   else
                {
                //
                psLog.print("<br>No.of Tags read:"+tags.length);               
                 
                } 
               
               
               //Perform Inventory & Verify EPC length is 128 bit.
               if(bsuccess)
               {
                   reader.Actions.Inventory.perform( null, null, null);
                   Thread.sleep(5000);
                   reader.Actions.Inventory.stop();
                   
                   
               }      
             }
               catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("\nInvalidUsageException" + exp.getInfo());
           
        } catch (OperationFailureException exp) {
            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("\nOperationFailureException" + exp.getMessage());
            
        }
               catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
               
               
                

              
            }
            psSummary.println("JavaAPI:ImpinjQT_Testcases"+":"+successCount+":"+failureCount+":0");
//        psLog.close();
//        psResult.close();  
//            
    }
       
    void Test_QTWriteEvent()
    {
        
        getEvents = new ReadEventlistener(reader, psLog);
         reader.Events.setAttachTagDataWithReadEvent(true);
        if(reader.isConnected() )
           {
               Impinj impinj = new Impinj(tagaccess);
               Impinj.QTWriteAccessParams qtWriteParams = impinj.new QTWriteAccessParams();
               Impinj.QTReadAccessParams qtReadParams = impinj.new QTReadAccessParams();
               long AccessPasswor = 0x00000001;
//               short m_QTControlData = 0x0000;
               short m_QTControlData = (short)0x8000;
//               short m_QTControlData = (short)0x4000;
               qtWriteParams.setAccessPassword(AccessPasswor);
               qtWriteParams.setQTControlData(m_QTControlData);
               qtWriteParams.setQTPersist(true);
               qtReadParams.setAccessPassword(1);
               TagAccess ta = new TagAccess();
               
                byte[] tagPattern1 = { (byte)0xF0,(byte)0x0D};
            byte[] tagMask = {(byte)0xFF,(byte)0xFF};
            accessfilter.TagPatternA.setBitOffset(32);
            accessfilter.TagPatternA.setTagPattern(tagPattern1);
            accessfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            accessfilter.TagPatternA.setTagMaskBitCount(16);
            accessfilter.TagPatternA.setTagPatternBitCount(16);
            accessfilter.TagPatternA.setTagMask(tagMask);
            accessfilter.setAccessFilterMatchPattern(FILTER_MATCH_PATTERN.A);
            accessfilter.setRSSIRangeFilter(false);
                             try
               {
                   //F00D0001F00D0002F00D0003F00D0004
//                   TagData tg = new TagData();
                   reader.Actions.TagAccess.Impinj.QTWriteEvent(qtWriteParams,null,null);
                   reader.Actions.TagAccess.Impinj.QTReadEvent(qtReadParams, accessfilter, null);
//                  String read = getEvents.tagData.getMemoryBankData();
//                   TagData tg = reader.Actions.TagAccess.Impinj.QTReadWait("F00D0001F00D0002F00D0003", qtReadParams, null);
                   //getLastAccessResult(pass, fail);
                    int[] pass = new int[1]; int[] fail = new int[1];
                    reader.Actions.TagAccess.getLastAccessResult(pass, fail);                   
                    
                    TagData[] tags = reader.Actions.getReadTags(1);
                    //System.out.println(" Config Word Value is "+tags.AccessOperationResult.ImpinjQTData.getQTControlData());
               if( tags != null)
               {
                  for(int i=0;i<tags.length;i++)
                    {
                    //System.out.print("\n"+tagdata[i].getTagID());
//                    int[] pass = new int[1]; int[] fail = new int[1];
//                    reader.Actions.TagAccess.getLastAccessResult(pass, fail);
                    
                   if(ACCESS_OPERATION_STATUS.ACCESS_SUCCESS == tags[i].getOpStatus())
                   {
                       psLog.println("Tag Data written succesfully");
                       bsuccess = true;
                       break;
                   }
                   else
                   {
                       for(i=0;i<tags.length;i++)
                        {
                                //System.out.print("\n"+tagdata[i].getTagID());
                             psLog.print("\n"+tags[i].getTagID());
                        }
                    }
                   }
                }
               
                   else
                {
                //
                psLog.print("<br>No.of Tags read:"+tags.length);               
                 
                } 
               
               
               //Perform Inventory & Verify EPC length is 128 bit.
               if(bsuccess)
               {
                   reader.Actions.Inventory.perform( null, null, null);
                   Thread.sleep(5000);
                   reader.Actions.Inventory.stop();
                   
                   
               }      
             }
               catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("\nInvalidUsageException" + exp.getInfo());
           
        } catch (OperationFailureException exp) {
            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("\nOperationFailureException" + exp.getMessage());
            
        }
               catch (NullPointerException e) {
            System.out.println("Exception: " + e.getMessage());
        }
               
            catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }    
                

              
            }
            psSummary.println("JavaAPI:ImpinjQT_Testcases"+":"+successCount+":"+failureCount+":0");
//        psLog.close();
//        psResult.close(); 
    }
    
}

