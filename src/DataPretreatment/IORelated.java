package DataPretreatment;

import BasicComponents.BloomFilter;

import javax.crypto.Mac;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 96130 on 2018/5/3.
 */
public class IORelated
{
    /**
     * @Description: 读入原始数据，将其转换为Partition数组
     * @param func：MAC function
     * @param label: 不同DO的唯一标识
     * @return Partition 数组
     */
    public static Partition[] formsPartitionsFromData(Mac[] func, int label) throws Exception
    {
        Partition[] part = new Partition[Constant.PartNumber];
        String tmp = null;
        List<DataFormat> datas = new ArrayList<>();
        String[] arr;
        for(int i=0;i<Constant.PartNumber;i++)
        {
            int l = Integer.valueOf(""+label+i);//Partitions' label
            part[i] = new Partition(l);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(Constant.FilePath+label+i+".txt"));//“testi.txt”存放partition数据
            BufferedReader isbr = new BufferedReader(isr);
            tmp = isbr.readLine();
            arr = tmp.split(";");
            BloomFilter pNodeBf = new BloomFilter(Constant.BFSize,7,func);//代表点
            pNodeBf.addKeywords(arr[0]);
            PresentedNode presentedNode = new PresentedNode(arr[0],pNodeBf);
            part[i].setPresentedNode(presentedNode);
            arr = arr[1].split(",");
            Set<Long> fids = new HashSet<>();
            for (String s:arr)
            {
                fids.add(Long.parseLong(s));
            }
            DataFormat fdata = new DataFormat(pNodeBf,fids);
            datas.add(fdata);//第一个数据->DataFormat
            while ((tmp = isbr.readLine())!=null)
            {
                arr = tmp.split(";");
                BloomFilter bf = new BloomFilter(Constant.BFSize,7,func);
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
            part[i].setNumber(datas.size());
            //List<DataFormat> members = datas.subList(0,datas.size());
            part[i].setMember(datas);
        }
        return part;
    }

    public static void writeResult(String[] write) throws Exception
    {
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(Constant.OutFilePath));
        BufferedWriter writer  = new BufferedWriter(osw);
        for (String s:write)
        {
            writer.write(s);
        }
        writer.close();
    }
}
