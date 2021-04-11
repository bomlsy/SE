package EPASStree;

import BasicComponents.AESCoder;
import BasicComponents.BloomFilter;
import DataPretreatment.Constant;
import DataPretreatment.DataFormat;
import DataPretreatment.Partition;
import MethodUtils.Calculations;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 96130 on 2018/4/29.
 */
public class Tree
{
    private Node root;
    private int high = 0;

    public Tree()
    {
        this.root = null;
        this.high = 0;
    }

    public Tree (Partition[] parts,long[] communicationSize) throws Exception
    {
        BloomFilter transimit = null;
        if (parts==null)
        {
            new Tree();
        }
        else
        {
            BlockingQueue<Node> nodeQue = new LinkedBlockingQueue<>();
            List<DataFormat> members = new ArrayList<>();
            for (Partition p:parts)
            {
                members.addAll(p.getMember());
            }
            for (DataFormat df:members)
            {
                Node leaf = new Leaf(df.getBf(),df.getIdentifierSet());
                nodeQue.offer(leaf);
                transimit = leaf.mbf;
            }
            byte[] bt;
            byte[] bt1;
            byte[] tmp={0};
            Map<String,Object> keymap = AESCoder.initKey();
            byte[] publicKey = AESCoder.getPublicKey(keymap);
            byte[] privateKey = AESCoder.getPrivateKey(keymap);
            for(int i=0;i<Constant.KeywordsNumber;i++)//模拟Algorithm3
            {
                bt = Calculations.transimtBitset2String(transimit.getMbitset()).getBytes();
                communicationSize[0]+=bt.length;
                bt1 = AESCoder.encryptByPublicKey(tmp,publicKey);
                bt1 = AESCoder.decryptByPrivateKey(bt1,privateKey);
                String stm = new String(bt);
                BitSet bst = Calculations.transimtString2Bitset(stm);
                communicationSize[0]+=bst.length()/8;
            }
            while (!nodeQue.isEmpty())
            {
                if(nodeQue.size()==1)
                {
                    break;
                }
                else if(nodeQue.size()==2)
                {
                    this.root = new Node();
                    this.root.liftChild = nodeQue.poll();
                    this.root.rightChild = nodeQue.poll();
                    break;
                }
                else
                {
                    Node current = new Node();
                    current.liftChild = nodeQue.poll();
                    current.rightChild = nodeQue.poll();
                    nodeQue.offer(current);
                }
            }
            BlockingQueue<Node> nodeQue2 = new LinkedBlockingQueue<>();
            nodeQue2.offer(this.root);
            Stack<Node> stk = new Stack<>();
            while(!nodeQue2.isEmpty())
            {
                Node currt = nodeQue2.poll();
                if(!currt.label)
                {
                    stk.push(currt);
                    nodeQue2.offer(currt.liftChild);
                    nodeQue2.offer(currt.rightChild);
                }
            }
            BloomFilter[] twobfs = new BloomFilter[2];
            while (!stk.isEmpty())
            {
                Node cut = stk.pop();
                twobfs[0] = cut.liftChild.mbf;
                twobfs[1] = cut.rightChild.mbf;
                cut.mbf = BloomFilter.combines(twobfs);
            }
        }
    }
    public Tree(Partition[] parts)
    {
        int count = 0;//Node的个数
        long startTime = 0;
        long endTime = 0;
        if (parts==null)
        {
            new Tree();
        }
        else
        {
            BlockingQueue<List<DataFormat>> que = new LinkedBlockingQueue<>();
            BlockingQueue<Node> nodeQue = new LinkedBlockingQueue<>();

            List<DataFormat> members = new ArrayList<>();
            for (Partition p:parts)
            {
                members.addAll(p.getMember());
            }

            BloomFilter tmp = retrieveBFfromDataFormats(members);
            Node currentParent = new Node(tmp);//当前处理的（父）节点
            this.root = currentParent;
            this.high +=1;
            int length = members.size();
            List<DataFormat> lc =  members.subList(0,length/2);//left child
            List<DataFormat> rc =  members.subList(length/2,length);//right child
            que.offer(lc);
            que.offer(rc);

            Node leftChild = new Node(retrieveBFfromDataFormats(lc));
            currentParent.setLeftChild(leftChild);
            nodeQue.offer(leftChild);
            Node rightChild = new Node(retrieveBFfromDataFormats(rc));
            currentParent.setRightChild(rightChild);
            nodeQue.offer(rightChild);
            List<DataFormat> cur;//处理当前队列里的第一个元素
            //Node currentParent;
            while(!que.isEmpty())
            {
                cur = que.poll();
                currentParent = nodeQue.poll();
                length = cur.size();
                if(length <= 3)//叶子节点的父节点
                {
                    //currentParent = new Node(retrieveBFfromDataFormats(cur));
                    lc = cur.subList(0,length/2);
                    rc = cur.subList(length/2,length);
                    leftChild = new Leaf(lc.get(0).getBf(),lc.get(0).getIdentifierSet());
                    currentParent.setLeftChild(leftChild);
                    if(rc.size()==2)
                    {
                        que.offer(rc);
                        rightChild = new Node(retrieveBFfromDataFormats(rc));
                        currentParent.setRightChild(rightChild);
                        nodeQue.offer(rightChild);
                    }
                    if(rc.size()==1)
                    {
                        rightChild = new Leaf(rc.get(0).getBf(),rc.get(0).getIdentifierSet());
                        currentParent.setRightChild(rightChild);
                    }
                    count +=1;
                }
                else
                {
                    //currentParent = new Node(retrieveBFfromDataFormats(cur));
                    lc = cur.subList(0,length/2);
                    rc = cur.subList(length/2,length);
                    que.offer(lc);
                    que.offer(rc);
                    leftChild = new Node(retrieveBFfromDataFormats(lc));
                    currentParent.setLeftChild(leftChild);
                    nodeQue.offer(leftChild);
                    rightChild = new Node(retrieveBFfromDataFormats(rc));
                    currentParent.setRightChild(rightChild);
                    nodeQue.offer(rightChild);
                    count +=1;
                }
            }
            this.high += (int)(Math.log(count)/ Math.log(2));
        }
    }

    /**
     * @Description: 生成中间节点的BloomFilter
     * @param dataFormat 中间节点所代表的数据
     * @return bloom filter
     */
    public BloomFilter retrieveBFfromDataFormats(List<DataFormat> dataFormat)
    {
        int length = dataFormat.size();
        BloomFilter[] tmp = new BloomFilter[length];
        int i = 0;
        for (DataFormat df:dataFormat)
        {
            tmp[i] = df.getBf();
            i++;
        }
        return BloomFilter.combines(tmp);
    }

    public int getHigh()
    {
        return this.high;
    }

    public void setHigh(int h)
    {
        this.high = h;
    }

    public Node getRoot()
    {
        return this.root;
    }

    public void setRoot(Node n)
    {
        this.root = n;
    }
}
