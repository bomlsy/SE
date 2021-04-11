package DataPretreatment;

import BasicComponents.BloomFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by 96130 on 2018/4/28.
 */
public class Partition
{
    private int label;
    private int number;
    private PresentedNode presentedNode;
    private List<DataFormat> member;

    public Partition(int label)
    {
        this.label = label;
        this.number = 0;
        this.presentedNode = null;
        this.member = null;
    }

    public Partition(int label, int number, PresentedNode presentedNode, List<DataFormat> member)
    {
        this.label = label;
        this.number = number;
        this.presentedNode = presentedNode;
        this.member = member;
    }

    public int getLabel()
    {
        return this.label;
    }

    public void setLabel(int label)
    {
        this.label = label;
    }

    public int getNumber()
    {
        return this.number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public PresentedNode getPresentedNode()
    {
        return this.presentedNode;
    }

    public void setPresentedNode(PresentedNode bf)
    {
        this.presentedNode = bf;
    }

    public List<DataFormat> getMember()
    {
        return this.member;
    }

    public void setMember(List<DataFormat> member)
    {
        this.member = member;
    }
}
