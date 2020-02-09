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
public class BlockPermaLock
{
    private TagData[] tagData;
    private RFIDReader blkReader;
    private TagAccess tagAccess;
    private byte[] tagMask = { (byte)0x12, (byte)0x34 };
    private byte[] tagPattern1 = { (byte)0xBE,(byte) 0xDD };
    private byte[] tagPattern2 = { (byte)0x04, (byte)0x01 };
    private byte[] accessPwd = { 0x12,0x34,0x56,0x78 };
    private short[] antennaList  = { 1,2 };
    private OPERATION_QUALIFIER antOPQ[] = { OPERATION_QUALIFIER.C1G2_OPERATION, OPERATION_QUALIFIER.C1G2_OPERATION};
    private TagPatternBase tpA;
    private TagPatternBase tpB;
    private PostFilter postfilter;
    private static TagAccess.BlockPermalockAccessParams blkParams;
    private TagAccess.ReadAccessParams rParams;
    private TagAccess.WriteAccessParams wParams;

    private byte[] Short2Bytes( short s )
    {
        return new byte[]{(byte)(s & 0x00FF),(byte)((s & 0xFF00)>>8)};
    }
    public BlockPermaLock( RFIDReader reader)
    {
        blkReader = reader;
        tagAccess = new TagAccess( );
        rParams = tagAccess.new ReadAccessParams( );
        wParams = tagAccess.new WriteAccessParams( );
        blkParams = tagAccess.new BlockPermalockAccessParams();
        
        tpA = new TagPatternBase( );
        tpB = new TagPatternBase( );
        postfilter = new PostFilter( );

        // fill block perma lock parameters
        blkParams = tagAccess.new BlockPermalockAccessParams();
        blkParams.setReadLock(true);
        blkParams.setByteOffset(0);
        blkParams.setByteCount(1);
        blkParams.setAccessPassword(0x12341234);
        blkParams.setMaskLength(2);
        //tag Pattern A
        tpA.setBitOffset(32);
        tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        tpA.setTagMask(tagMask);
        tpA.setTagMaskBitCount(16);
        tpA.setTagPattern(tagPattern1);
        tpA.setTagPatternBitCount(16);

//        //tag pattern B
//        tpB.setBitOffset(32);
//        tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
//        tpB.setTagMask(tagMask);
//        tpB.setTagMaskBitCount(16);
//        tpB.setTagPattern(tagPattern2);
//        tpB.setTagPatternBitCount(16);

         //post filter
        postfilter.TagPatternA = tpA;
        postfilter.TagPatternB = tpB;

        postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A);
        postfilter.setRSSIRangeFilter(false);
        tagData = new TagData[1];
    }
    
    void testBlockWriteWait( )
    {
        try
        {

            TagData[] tag = new TagData[1];
            blkReader.Actions.Inventory.perform(postfilter, null, null);
            Thread.sleep(5000);
            blkReader.Actions.Inventory.stop();
            tag = blkReader.Actions.getReadTags(1);
            if( tag != null)
            {
                System.out.print("\n EPC :"+tag[0].getTagID());
                blkReader.Actions.purgeTags();
                /* Write Access Passsword to the Reserved Memory Bank*/
                wParams.setAccessPassword(0);
                wParams.setByteOffset(4);
                wParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_RESERVED);
                wParams.setWriteData(accessPwd);
                wParams.setWriteDataLength(4);
                blkReader.Actions.TagAccess.blockWriteWait(tag[0].getTagID(), wParams, null);
                byte writeData[] = new byte[64];
                wParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                wParams.setWriteData(writeData);
                wParams.setWriteDataLength(12);
                blkReader.Actions.TagAccess.blockWriteWait(tag[0].getTagID(), wParams, null);
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
    void testBlockPermaLock( )
    {
        try
        {
            TagData[] tag = new TagData[1];
            blkReader.Actions.Inventory.perform(postfilter, null, null);
            Thread.sleep(1000);
            blkReader.Actions.Inventory.stop();
            tag = blkReader.Actions.getReadTags(1);
//            if( tag != null)
//            {
                //System.out.print("\n EPC :"+tag[0].getTagID());
                blkReader.Actions.purgeTags();

                /* Write Access Passsword to the Reserved Memory Bank*/
//                wParams.setAccessPassword(0);
//                wParams.setByteOffset(4);
//                wParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_RESERVED);
//                wParams.setWriteData(accessPwd);
//                wParams.setWriteDataLength(4);
//                blkReader.Actions.TagAccess.writeWait(tag[0].getTagID(), wParams, null);
                
                blkParams.setMask(tagMask);
                blkParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
                // Try Perma Lock on EPC memory and it should FAil*/
                try
                {
                    blkReader.Actions.TagAccess.blockPermalockWait("11112222333344445555666677778888", blkParams, null);
                }
                catch(InvalidUsageException exp)
                {
                    System.out.print("\n  "+ exp.getVendorMessage());
                }
                catch(OperationFailureException opexp)
                {
                    System.out.print("\n  "+opexp.getVendorMessage());
                }
                int successCount[] = new int[1];int failureCount[] = new int[1];
            blkReader.Actions.TagAccess.getLastAccessResult(successCount, failureCount);
                // Try Perma Lock on TID memory and it should FAil*/
                blkParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_TID);
                try
                {
                    blkReader.Actions.TagAccess.blockPermalockWait("11112222333344445555666677778888", blkParams, null);
                }
                catch(InvalidUsageException exp)
                {
                    System.out.print("\n  "+ exp.getVendorMessage());
                }
                catch(OperationFailureException opexp)
                {
                    System.out.print("\n  "+opexp.getVendorMessage());
                }

                 blkReader.Actions.TagAccess.getLastAccessResult(successCount, failureCount);
                // Try Perma Lock on Reserved memory and it should FAil*/
                blkParams.setMemoryBank( MEMORY_BANK.MEMORY_BANK_RESERVED );
                try
                {
                    TagData tagData = blkReader.Actions.TagAccess.blockPermalockWait("11112222333344445555666677778888", blkParams, null);
                }
                catch(InvalidUsageException exp)
                {
                    System.out.print("\n  "+ exp.getVendorMessage());
                }
                catch(OperationFailureException opexp)
                {
                    System.out.print("\n  "+opexp.getVendorMessage());
                }
                 blkReader.Actions.TagAccess.getLastAccessResult(successCount, failureCount);
                /* block Perma lock Higgs 3 user memory for all the blocks of memory*/
                for( int blockCount = 0; blockCount < 8; blockCount++ )
                {
                    // fill block perma lock parameters
                    blkParams = tagAccess.new BlockPermalockAccessParams();
                    blkParams.setReadLock(true);
                    blkParams.setByteOffset(0);
                    blkParams.setByteCount(0);
                    blkParams.setAccessPassword(0xABCDABCD);
                    blkParams.setMaskLength(2);
                    
                    byte[] blkMask = new byte[2];
                    switch( blockCount)
                    {
                        case 0:
                        {
                            blkMask[0] = (byte)0x80;
                            blkMask[1] = 0x00;
                            break;
                        }
                        case 1:
                        {
                            blkMask[0] = 0x40;
                            blkMask[1] = 0x00;
                            break;
                        }
                        case 2:
                        {
                            blkMask[0] = 0x20;
                            blkMask[1] = 0x00;
                            break;
                        }
                        case 3:
                        {
                            blkMask[0] = 0x10;
                            blkMask[1] = 0x00;
                            break;
                        }
                        case 4:
                        {
                            blkMask[0] = 0x08;
                            blkMask[1] = 0x00;
                            break;
                        }
                        case 5:
                        {
                            blkMask[0] = 0x04;
                            blkMask[1] = 0x00;
                            break;
                        }
                        case 6:
                        {
                            blkMask[0] = 0x02;
                            blkMask[1] = 0x00;
                            break;
                        }
                        case 7:
                        {
                            blkMask[0] = 0x01;
                            blkMask[1] = 0x00;
                            break;
                        }
                        default:
                            break;
                    }
                    System.out.printf("\n Set Mask Data  :%02x%02x",blkMask[0],blkMask[1]);
                    blkParams.setMask(blkMask);
                    
                    /*Perma lock user memory*/
                    blkParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                    try
                    {
                        TagData tagData = blkReader.Actions.TagAccess.blockPermalockWait("11112222333344445555666677778888", blkParams, null);
                    }
                    catch(InvalidUsageException exp)
                    {
                        System.out.println( exp.getVendorMessage());
                    }
                    catch(OperationFailureException opexp)
                    {
                        System.out.println(opexp.getVendorMessage());
                    }
                   
                    /*read the mask back and print it*/
                    blkParams.setReadLock(false);
                    blkMask[0] = (byte)0xff;
                    blkMask[1] = 0x00;
                    blkParams.setMask(blkMask);
                    try
                    {
                        TagData tagData = blkReader.Actions.TagAccess.blockPermalockWait("11112222333344445555666677778888", blkParams, null);
                        if(tagData != null)
                        {
                            System.out.print("\n Permalock on MemBank :"+tagData.getMemoryBank()+" readback  Mask :"+tagData.getMemoryBankData());
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
                    byte[] writeData = {(byte)0xaa,(byte)0xbb,(byte)0xcc,(byte)0xdd,(byte)0xee,(byte)0xff};
                    int offset = blockCount * 4; /*because block is 4words for higgs,increment block by 4 every time*/
                    wParams.setAccessPassword(0x12341234);
                    wParams.setByteOffset(offset);
                    wParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                    wParams.setWriteData(writeData);
                    wParams.setWriteDataLength(6);
                    try
                    {
                        blkReader.Actions.TagAccess.writeWait("11112222333344445555666677778888", wParams, null);
                        System.out.print("\n Perma Lock FAIL");
                    }
                    catch(InvalidUsageException exp)
                    {
                        System.out.print("\n "+ exp.getVendorMessage());
                        System.out.print("\n Perma Lock Success");
                    }
                    catch(OperationFailureException opexp)
                    {
                        System.out.print("\n  "+opexp.getVendorMessage());
                        System.out.print("\n Perma Lock Success");
                    }
                }
            //}
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
//public void testBlockPermalock1()
//    {
//        try {
//            TagAccess tagAccess = new TagAccess();
//            TagAccess.BlockPermalockAccessParams blkPermaLockParams = tagAccess.new BlockPermalockAccessParams();
//            long password = 0x00000001 ;
//            short[] mask = new short[2];
//            mask[0] = 0xff;
//            mask[1] = 0x00;
//            blkPermaLockParams.setAccessPassword(password);
//            blkPermaLockParams.setByteCount(1);
//            blkPermaLockParams.setByteOffset(0);
//            blkPermaLockParams.setMask(mask);
//            blkPermaLockParams.setMaskLength(2);
//            blkPermaLockParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
//            blkPermaLockParams.setReadLock(true);
//            blkReader.Actions.TagAccess.blockPermalockWait("E2110000000002520990B359", blkPermaLockParams, null);
//
//            blkPermaLockParams.setReadLock(false);
//
//            TagData tag = blkReader.Actions.TagAccess.blockPermalockWait("E2110000000002520990B359", blkPermaLockParams, null);
//
//            int i = 100;
//
//        }
//        catch (InvalidUsageException ex)
//        {
//            System.out.println( ex.getVendorMessage());
//            ex.printStackTrace();
//        } catch (OperationFailureException ex)
//        {
//            System.out.println(ex.getVendorMessage());
//            ex.printStackTrace();
//        }
//
//
//
//    }
//
//}
