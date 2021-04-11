package DataPretreatment;

import BasicComponents.BloomFilter;

import java.util.Set;

/**
 * Created by 96130 on 2018/4/28.
 */

/**
 * @Description: 用来组织原始数据，形式为：(BF(w),L(w))
 */
public class DataFormat
{
    private BloomFilter bf;//存关键词的BF
    private Set<Long> identifierSet;//存包含关键词的文件id

    public DataFormat(BloomFilter b, Set<Long> s)
    {
        this.bf = b;
        this.identifierSet = s;
    }

    public BloomFilter getBf()
    {
        return this.bf;
    }

    public void setBf(BloomFilter b)
    {
        this.bf = b;
    }

    public Set<Long> getIdentifierSet()
    {
        return this.identifierSet;
    }

    public void setIdentifierSet(Set<Long> s)
    {
        this.identifierSet = s;
    }
}
