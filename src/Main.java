import BasicComponents.AESCoder;
import BasicComponents.BloomFilter;
import DataPretreatment.*;
import EPASStree.Leaf;
import EPASStree.Node;
import EPASStree.Tree;
import MethodUtils.*;
import TwoExperements.QueryLaunch;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.*;
import java.util.*;

public class Main {

    public static void addTime(long[] time)
    {
        time[0]+=100;
    }
    public static void main(String[] args) throws Exception
    {
        long communicationSize = 0;//通信开销，单位Byte
        long startTime = 0;
        long endTime = 0;
        long[] size = {0};
        String[] write = new String[12];
        //初始化Mac Function
        SecretKey[] sk = new SecretKey[7];
        Mac[] func = new Mac[7];
        KeyGenerator kg = KeyGenerator.getInstance("HmacSHA256");
        for(int i=0;i<7;i++)
        {
            sk[i] = kg.generateKey();
            func[i] = Mac.getInstance("HmacSHA256");
            func[i].init(sk[i]);
        }
        //读入聚类数据，构造Partition类
        Partition[] part1 = IORelated.formsPartitionsFromData(func,1);
        Partition[] part2 = IORelated.formsPartitionsFromData(func,2);
        //System.out.println("the data is partitioned");
        // The processing of constructing the tree
        long[] sort = inialIntArray();
        startTime = System.currentTimeMillis();
        //communicationSize = ConstructTree.constructTree(func,part1,part2,sort);
        Partition[] newPartition = Calculations.unionTwoPartitions(part1,part2,sort);
        Tree t = new Tree(newPartition,size);
        //communicationSize += size[0];
        endTime = System.currentTimeMillis();
        System.out.println("the Tree is constructed");
        System.out.println("Constructing Time (ms):"+(endTime-startTime)+"\n");
        //write[1] = "The communicationSize in Constructing Tree (Byte):"+communicationSize+"\n";
        Map<String, Object> keyMap = AESCoder.initKey();
        byte[] publicKey = AESCoder.getPublicKey(keyMap);
        System.out.println("输入要查询的串（以‘，’分开）:\n");
        Scanner scanner = new Scanner(System.in);
        String info = scanner.nextLine();
        String[] qarr = info.split(",");
        int wn = 2;
        for (String s:qarr)
        {
//            Query q = new Query(Constant.queryKey,publicKey,0);
            Query q = new Query(s,publicKey,0);
            startTime = System.currentTimeMillis();
            Set<Long> result = QueryLaunch.queryProcess(q,t,keyMap);
            System.out.println(result.toString());
            endTime = System.currentTimeMillis();
//            write[wn] = "Querying Time (ms):"+(endTime-startTime)+"\n";
            System.out.println("Querying Time (ms):"+(endTime-startTime)+"\n");
            wn++;
        }
        //IORelated.writeResult(write);

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
            pos = getValue(inp,index);
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
    public static long[] inialIntArray()
    {
        long[] rtn = new long[2*Constant.PartNumber];
        for(int i=0;i<Constant.PartNumber;i++)
        {
            rtn[i]=Integer.parseInt(""+1+i);
        }
        for(int i=Constant.PartNumber;i<2*Constant.PartNumber;i++)
        {
            rtn[i]=0;
        }
        return rtn;
    }


    public static void transmitTest()
    {
        BitSet bs = new BitSet();
        bs.set(1);
        bs.set(7);
        bs.set(17);
        System.out.println(bs.length()+";"+bs.size());
        System.out.println("before the transmit:"+bs.toString());
        String s  = Calculations.transimtBitset2String(bs);
        BitSet bs1 = Calculations.transimtString2Bitset(s);
        System.out.println("After the transmit:"+bs1.toString());
    }
    public static void test() throws Exception
    {
        SecretKey[] sk = new SecretKey[7];
        Mac[] func = new Mac[7];
        KeyGenerator kg = KeyGenerator.getInstance("HmacSHA256");
        for(int i=0;i<7;i++)
        {
            sk[i] = kg.generateKey();
            func[i] = Mac.getInstance("HmacSHA256");
            func[i].init(sk[i]);
        }
        InputStreamReader isr = new InputStreamReader(new FileInputStream("test0.txt"));
        BufferedReader isbr = new BufferedReader(isr);
        String tmp = null;
        List<DataFormat> datas = new ArrayList<>();
        String[] arr;
        while ((tmp = isbr.readLine())!=null)
        {
            arr = tmp.split(";");
            BloomFilter bf = new BloomFilter(2<<12,7,func);
            bf.addKeywords(arr[0]);
            arr = arr[1].split(",");
            Set<Long> ids = new HashSet<>();
            for (String s:arr)
            {
                ids.add(Long.parseLong(s));
            }
            DataFormat data = new DataFormat(bf,ids);
            datas.add(data);
        }
        isbr.close();
        int length = datas.size();
        List<DataFormat> d1 = datas.subList(0,length/2);
        List<DataFormat> d2 = datas.subList(length/2,length);
        Partition[] part = new Partition[2];
        BloomFilter bf = new BloomFilter(2<<7,7,func);
        part[0] = new Partition(1);
        part[1] = new Partition(2);
        //Tree t = new Tree(part);
        Query q = new Query("Round",null,0);
        //Set<Long> result = QueryLaunch.queryProcess(q,t);
        //System.out.println(result.toString());
    }
    public static void Nodetest() throws Exception
    {
        SecretKey[] sk = new SecretKey[7];
        Mac[] func = new Mac[7];
        KeyGenerator kg = KeyGenerator.getInstance("HmacSHA256");
        for(int i=0;i<7;i++)
        {
            sk[i] = kg.generateKey();
            func[i] = Mac.getInstance("HmacSHA256");
            func[i].init(sk[i]);
        }
        BloomFilter bf = new BloomFilter(2<<7,7,func);
        bf.addString("tree");
        BloomFilter bfl = new BloomFilter(2<<7,7,func);
        bfl.addString("l");
        BloomFilter bfr = new BloomFilter(2<<7,7,func);
        bfr.addString("r");
        BloomFilter[] bfs = {bf,bfl,bfr};
        BloomFilter bn = BloomFilter.combines(bfs);
        Node lnode = new Node(bfl);
        Node rnode = new Node(bfr);
        Node node = new Node(bf,lnode,rnode);
        System.out.print(node.contains("tree"));
        System.out.print(node.contains("test"));
        System.out.print(node.getLeftChild().contains("l"));
        lnode.setLeftChild(rnode);
        Set<Long> set = new HashSet<Long>();
        set.add(new Long(123));
        Leaf leaf = new Leaf(bf,set);
        System.out.print(leaf.getLabel());
    }
    public static void BFtest() throws Exception
    {
        int k = 7;
        SecretKey[] sk = new SecretKey[7];
        Mac[] func = new Mac[7];
        KeyGenerator kg = KeyGenerator.getInstance("HmacSHA256");
        for(int i=0;i<7;i++)
        {
            sk[i] = kg.generateKey();
            func[i] = Mac.getInstance("HmacSHA256");
            func[i].init(sk[i]);
        }
        int size = 2<<6;
        BloomFilter bf = new BloomFilter(size,k,func);
        String[] strs = {"Bloom","Filter","Is","a","Big","Data","Index"};
        for (String s:strs)
        {
            bf.addString(s);
        }
        String[] test = {"Bloom","Data","big","test","123"};
        for (String s:test)
        {
            System.out.println(bf.contains(s));
        }
    }
    public static int hashValue(byte[] hash)
    {
        int hashvalue = 0;
        int [] rtn = new int[4];
        int size = 20;
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
        return hashvalue;
    }

    public static void HashTest() throws Exception
    {
        KeyGenerator kg = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = kg.generateKey();

        // Get instance of Mac object implementing HmacSHA256, and
        // initialize it with the above secret key
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(sk);
        byte[] result = mac.doFinal("Hi There".getBytes());
        System.out.println(result.length);
    }
    public static void ElgamalTest() throws Exception
    {
        //初始化密钥
        //生成密钥对
        Map<String,Object> keyMap= AESCoder.initKey();
        //公钥
        byte[] publicKey= AESCoder.getPublicKey(keyMap);

        //私钥
        byte[] privateKey= AESCoder.getPrivateKey(keyMap);
        System.out.println("公钥：/n"+Base64.encodeBase64String(publicKey));
        System.out.println("私钥：/n"+Base64.encodeBase64String(privateKey));

        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
        String str="FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
        System.out.println("/n===========甲方向乙方发送加密数据==============");
        System.out.println("原文length:\t"+str.getBytes().length);

        //乙方使用公钥对数据进行加密
        byte[] code2= AESCoder.encryptByPublicKey(str.getBytes(), publicKey);
        System.out.println("===========乙方使用公钥对数据进行加密==============");
        System.out.println("密文length:\t"+code2.length);
        System.out.println("加密后的数据："+ new String(code2));


        //甲方使用私钥对数据进行解密
        byte[] decode2= AESCoder.decryptByPrivateKey(code2, privateKey);

        System.out.println("甲方解密后的数据："+new String(decode2));
        //System.out.println("甲方解密后的数据："+Base64.encodeBase64String(decode2));
    }
}
