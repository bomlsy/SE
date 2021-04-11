package MethodUtils;

import BasicComponents.BloomFilter;
import DataPretreatment.Constant;
import DataPretreatment.Partition;
import DataPretreatment.PresentedNode;

import java.util.ArrayList;
import java.util.BitSet;


/**
 * Created by 96130 on 2018/5/2.
 */
public class Calculations
{
    /**
     * @Description: 生成一个关键词（字符串）的所有子串
     * @param key 待处理的关键词
     * @return 包含所有子串的字符串数组
     */
    public static String[] arrayofKeywords(String key)
    {
        int length = key.length();
        ArrayList<String> sw = new ArrayList<>();
        for(int i=1; i<length; i++)
        {
            for (int j = 0; j < length; j++)
            {
                String s = "";
                if (j + i <= length)
                {
                    s = key.substring(j, j+i);
                    sw.add(s);
                }
            }
        }
        String[] rtn = new String[sw.size()];
        return sw.toArray(rtn);
    }

    /**
     * @Description: 从Partition数组里获取代表点
     * @param partitions Partition数组，长度为：Constant.PartNumber
     * @return 代表点数组
     */
    public static PresentedNode[] retrievePresentNode(Partition[] partitions)
    {
        PresentedNode[] pNode = new PresentedNode[Constant.PartNumber];
        for(int i=0;i<Constant.PartNumber;i++)
        {
            pNode[i] = partitions[i].getPresentedNode();
        }
        return pNode;
    }

    /**
     * @Description: 将BitSet转换到String
     * @param bs BitSet
     * @return String
     */
    public static String transimtBitset2String(BitSet bs)
    {
        byte[] bytes = new byte[bs.size() / 8];
        for (int i = 0; i < bs.size(); i++)
        {
            int index = i / 8;
            int offset = 7 - i % 8;
            bytes[index] |= (bs.get(i) ? 1 : 0) << offset;
        }
        String rtn = new String(bytes);
        return rtn;
    }

    /**
     * @Description: 将String转换到BitSet
     * @param s String
     * @return BitSet
     */
    public static BitSet transimtString2Bitset(String s)
    {
        byte[] bytes = s.getBytes();
        BitSet bitSet = new BitSet(bytes.length * 8);
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 7; j >= 0; j--) {
                bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1 ? true : false);
            }
        }
        return bitSet;
    }

    public static long[] calculationSimilarityBetweenTwoPartitions(Partition[] part1,PresentedNode pNode)
    {
        long[] rtn = {0,0};
        long communicationSize = 0;
        double[] tmp;
        double minSimilarity = 1.0;
        long label =13;
        rtn[1] = label;
        for (Partition p:part1)
        {
            label = p.getLabel();
            String keyword = p.getPresentedNode().getKeyword();
            tmp = calculationSimilarityBetweenTwoPresentedNodes(keyword,pNode);
            communicationSize += (long)tmp[0];
            if(minSimilarity>tmp[1])
            {
                minSimilarity = tmp[1];
                rtn[1] = label;
            }
        }
        rtn[0] = communicationSize;
        return rtn;
    }

    public static double[] calculationSimilarityBetweenTwoPresentedNodes(String keyword, PresentedNode pNode)
    {
        double[] rtn = {0,0};
        String[] sw = arrayofKeywords(keyword);
        int length1 = sw.length;
        byte[] theVector = new byte[length1];
        BloomFilter bf = pNode.getBloomFilter();
        for (int i=0;i<length1;i++)
        {
            if(bf.contains(sw[i]))
            {
                theVector[i] = 1;
                rtn[1]++;
            }
            else
            {
                theVector[i] = 0;
            }
        }
        rtn[0] = length1/8;
        int length2 = arrayofKeywords(pNode.getKeyword()).length;
        rtn[1] = rtn[1]/(length1+length2-rtn[1]);
        return rtn;
    }

    public static Partition[] unionTwoPartitions(Partition[] part1,Partition[] part2,long[] index)
    {
        int length = 2*Constant.PartNumber;
        Partition[] rtn = new Partition[length];
        int quotient = 0;
        int remainder = 0;
        for(int i=0;i<length;i++)
        {
            quotient = (int)index[i]/10;
            remainder = (int)index[i]%10;
            if (quotient==1)
            {
                rtn[i] = part1[remainder];
            }
            else//quotient = 2
            {
                rtn[i] = part2[remainder];
            }
        }
        return rtn;
    }

}
