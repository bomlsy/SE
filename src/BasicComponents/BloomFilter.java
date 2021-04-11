package BasicComponents;

import MethodUtils.Calculations;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by 96130 on 2019/1/26.
 */
public class BloomFilter
{
    private int msize;//Bloom filter 大小
    private BitSet mbitset;//通过结构BitSet实现m位的bit数组
    private int mk;//哈希函数个数
    private Mac[] mfunc;//k个Hash方程
    private int numberOfAddedElements;//过滤器容器中元素的实际数量

    public BloomFilter()
    {
        this.msize = 0;
        this.mbitset = new BitSet();
        this.mk = 0;
        this.numberOfAddedElements = 0;
        this.mfunc = null;
    }
    /**
     * 构造BloomFilter
     * @param size Bloomfilter 大小
     * @param k HashFunction 个数
     * @param func HashFunction
     * */
    public BloomFilter(int size,int k,Mac[] func)
    {
        this.msize = size;
        this.mbitset = new BitSet(msize);
        this.mk = k;
        this.mfunc = new Mac[mk];
        for (int i=0; i<k; i++)
        {
            mfunc[i] = func[i];
        }
        this.numberOfAddedElements=0;
    }

    /**
     * @Description: 根据输入的size大小，控制Hashsize大小
     * @return
     * 1：输入有误
     *  ：返回2^n, 2^{n-1}<=size<2^{n}
     */
    private int controlSize()
    {
        int count = 0;
        if (msize==0)
        {
            System.out.println("The input of Bloomfilter-controlSize is 0");
        }
        else
        {
            int size = msize;
            while(size>1)
            {
                size = size/2;
                count++;
            }
        }
        return count;
    }

    /**
     * @Description: 计算一个byte数组的hash值,值域为（0，msize）
     * @param hash：长度大于4的byte数组。
     * @return 输入byte数组的hash值
     */
    private int hashValue(byte[] hash)
    {
        int hashvalue = 0;
        int [] rtn = new int[4];
        int size = controlSize();
        int cnt = size/8+1;
        int rnd = 8-size%8;
        for (int i = 0; i < 4; i++)
        {
            for(int j=0;j<cnt;j++)
            {
                rtn[i]<<=8;
                rtn[i] |= ((int) hash[i*cnt+j]) & 0xFF;
            }
            rtn[i]>>=rnd;
        }
        hashvalue = rtn[3]+rtn[0]-rtn[1]-rtn[2];
        if(hashvalue<0)
        {
            hashvalue = (int) (hashvalue+Math.pow(2,size+1));
        }
        return hashvalue%msize;
    }

    /**
     * @Description: 向bloom filter 中添加一个任意长的字符串
     * @param s 输入字符串
     */
    public void addString(String s)
    {
        byte[] t = s.getBytes();
        for (Mac f:mfunc)
        {
            byte[] hash = f.doFinal(t);
            int id = hashValue(hash);
            mbitset.set(hashValue(hash));
        }
        this.numberOfAddedElements++;
    }

    /**
     * @Description: 判断某个字符串是否在BloomFilter中
     * @param s 输入字符串
     * @return true:在；false:不在
     */
    public boolean contains(String s)
    {

        boolean rtn = true;
        if (s==null)
        {
            rtn = false;
        }
        else
        {
            byte[] t = s.getBytes();
            for (Mac f:mfunc)
            {
                byte[] hash = f.doFinal(t);
                rtn = rtn && mbitset.get(hashValue(hash));
            }
        }
        return rtn;
    }

    /**
     * @Description: 静态方法，将多个BF融合成一个
     * @param bfs：输入的BF数组
     */
    public static BloomFilter combines(BloomFilter[] bfs)
    {
        if (bfs==null)
        {
            System.out.println("The input of BloomFilter--combines is null");
            return null;
        }
        if (bfs.length==1)
        {
            return bfs[0];
        }
        int size = bfs[0].getMsize();
        int k = bfs[0].getMk();
        Mac[] func = bfs[0].getMfunc();
        BloomFilter rtn = new BloomFilter(size,k,func);
        Boolean b = false;
        for (int i=0; i<size; i++)
        {
            for (int j=0; j<bfs.length; j++)
            {
                b = b || bfs[j].getMbitset().get(i);
                if(b)
                {
                    break;
                }
            }
            if (b)
            {
                rtn.getMbitset().set(i);
            }
            b = false;
        }
        return rtn;
    }

    public void addKeywords(String keyword)
    {
        String[] sw = Calculations.arrayofKeywords(keyword);
        for (String s:sw)
        {
            byte[] t = s.getBytes();
            for (Mac f:mfunc)
            {
                byte[] hash = f.doFinal(t);
                int id = hashValue(hash);
                mbitset.set(hashValue(hash));
            }
        }
    }

    public void setMsize(int size)
    {
        this.msize = size;
    }

    public void setMbitset(BitSet s)
    {
        this.mbitset = s;
    }

    public int getMsize()
    {
        return this.msize;
    }

    public BitSet getMbitset()
    {
        return this.mbitset;
    }

    public int getMk()
    {
        return this.mk;
    }

    public int getNumberOfAddedElements()
    {
        return this.numberOfAddedElements;
    }

    public Mac[] getMfunc()
    {
        return this.mfunc;
    }
}