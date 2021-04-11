package EPASStree;

import BasicComponents.BloomFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 96130 on 2018/4/27.
 */
public class Leaf extends Node
{
    private Set<Long> midentifierSet;//包含该节点所代表的关键词的记录的id

    public Leaf(BloomFilter bf,Set s)
    {
        super(bf);
        this.label = true;//标记该节点是叶子节点
        this.midentifierSet = s;
    }

    public void setIdentifierSet(Set s)
    {
        this.midentifierSet = s;
    }

    public Set gerIndentifierSet()
    {
        return this.midentifierSet;
    }
}
