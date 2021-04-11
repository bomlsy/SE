package EPASStree;

import BasicComponents.BloomFilter;

/**
 * Created by 96130 on 2018/4/27.
 */
public class Node
{
    protected BloomFilter mbf;
    protected boolean label;//标记一个节点是不是叶子节点, false:不是叶子节点
    protected Node liftChild;
    protected Node rightChild;

    public Node()
    {
        this.mbf = null;
        this.label = false;
        this.liftChild = null;
        this.rightChild = null;
    }
    public Node(BloomFilter bf)
    {
        this.mbf = bf;
        this.label = false;
        this.liftChild = null;
        this.rightChild = null;
    }
    public Node(BloomFilter bf,Node lc,Node rc)
    {
        this.mbf = bf;
        this.label = false;
        this.liftChild = lc;
        this.rightChild = rc;
    }

    public boolean contains(String s)
    {
        return mbf.contains(s);
    }

    public void setLeftChild(Node n)
    {
        this.liftChild = n;
    }

    public Node getLeftChild()
    {
        return this.liftChild;
    }

    public void setRightChild(Node n)
    {
        this.rightChild = n;
    }

    public Node getRightChild()
    {
        return this.rightChild;
    }

    public void setLabel(Boolean b)
    {
        this.label = b;
    }

    public Boolean getLabel()
    {
        return this.label;
    }

    public BloomFilter getMbf()
    {
        return mbf;
    }

    public void setMbf(BloomFilter mbf) {
        this.mbf = mbf;
    }
}
