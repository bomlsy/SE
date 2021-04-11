package TwoExperements;

import BasicComponents.BloomFilter;
import DataPretreatment.Constant;
import DataPretreatment.IORelated;
import DataPretreatment.Partition;
import DataPretreatment.PresentedNode;
import MethodUtils.Calculations;
import MethodUtils.Client;
import MethodUtils.Servers;

import javax.crypto.Mac;
import java.util.BitSet;

/**
 * Created by 96130 on 2018/5/4.
 */
public class ConstructTree
{
    public static long constructTree(Mac[] func,Partition[] part1,Partition[] part2,long[] index) throws Exception
    {
        long communicationSize = 0;

        //获取所有Partition的核心代表点
        PresentedNode[] pNode1 = Calculations.retrievePresentNode(part1);
        PresentedNode[] pNode2 = Calculations.retrievePresentNode(part2);
        BloomFilter bftmp = null;
        BitSet bstmp = null;
        long label = 0;
        long[] tmp;
        for(int i = 0; i< Constant.PartNumber; i++)
        {
            label = part2[i].getLabel();
            bftmp = pNode2[i].getBloomFilter();
            bstmp = bftmp.getMbitset();
            String stmp = Calculations.transimtBitset2String(bstmp);
            communicationSize += bstmp.size()/8;
            Servers servers = new Servers(stmp);
            Thread t = new Thread(servers);
            t.start();
            String rtn = Client.client();
            BitSet tbstmp = Calculations.transimtString2Bitset(rtn);
            communicationSize += tbstmp.length()/8;
            tmp = Calculations.calculationSimilarityBetweenTwoPartitions(part1,pNode2[i]);
            communicationSize += tmp[0];
            insertvalue(tmp[1],label,index);
        }
        communicationSize*=2;
        return communicationSize;
    }

    public static void insertvalue(long inp,long insert,long[] index)
    {
        int pos = 0;
        if (inp == 13)
        {
            pos = getValue(inp,index);
            index[pos+1] = insert;
        }
        else
        {
            for(int i=index.length-2;i>pos;i--)
            {
                index[i+1] = index[i];
            }
            index[pos+1] = insert;
        }
    }

    public static int getValue(long value,long[] index)
    {
        for(int i=0;i<index.length;i++)
        {
            if(index[i]==value)
            {
                return i;
            }
        }
        return 0;
    }

}
