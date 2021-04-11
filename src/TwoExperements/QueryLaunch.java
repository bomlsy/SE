package TwoExperements;

import BasicComponents.AESCoder;
import EPASStree.Leaf;
import EPASStree.Node;
import EPASStree.Tree;
import MethodUtils.Query;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 96130 on 2018/5/5.
 */
public class QueryLaunch
{
    /**
     * @Description: 查询过程
     * @param q 查询条件
     * @param t 索引结构 E-PASStree+
     * @return 匹配的文件索引集合
     */
    public static Set<Long> queryProcess(Query q, Tree t, Map<String,Object> keymap) throws  Exception
    {
        Set<Long> rtn = new HashSet<>();
        String keyword = q.getKeyword();
        byte[] tmp0 = {0};
        byte[] tmp1 = {1};
        byte[] privateKey = AESCoder.getPrivateKey(keymap);
        byte[] publicKey = AESCoder.getPublicKey(keymap);
        byte[] enc;
        BlockingQueue<Node> queue = new LinkedBlockingQueue<>();
        queue.offer(t.getRoot());
        while(!queue.isEmpty())
        {
            Node checked = queue.poll();
            if(checked.getLabel())
            {
                Leaf matched = (Leaf)checked;
                if(matched.contains(keyword))
                {
                    enc = AESCoder.encryptByPublicKey(tmp1,publicKey);
                    rtn.addAll(matched.gerIndentifierSet());
                    AESCoder.decryptByPrivateKey(enc,privateKey);
                }
            }
            else
            {
                //enc = AESCoder.encryptByPublicKey(tmp1,publicKey);
                //AESCoder.decryptByPrivateKey(enc,privateKey);
                if(checked.contains(keyword))
                {
                    enc = AESCoder.encryptByPublicKey(tmp1,publicKey);
                    AESCoder.decryptByPrivateKey(enc,privateKey);
                    queue.offer(checked.getLeftChild());
                    queue.offer(checked.getRightChild());
                }
            }
        }
        /*for(int i=0;i<log2(Constant.KeywordsNumber);i++)
        {
            enc = AESCoder.encryptByPublicKey(tmp1,publicKey);
            AESCoder.decryptByPrivateKey(enc,privateKey);
        }*/
        return rtn;
    }

    public static int log2(int value)
    {
       return  (int)(Math.log(value)/ Math.log(2)+1);
    }
}
