package DataPretreatment;

import BasicComponents.BloomFilter;

/**
 * Created by 96130 on 2018/5/4.
 */
public class PresentedNode
{
    private String keyword;//代表点的关键词
    private BloomFilter bloomFilter;//代表点的BloomFilter

    public PresentedNode()
    {
        this.keyword = null;
        this.bloomFilter = null;
    }

    public PresentedNode(String keyword,BloomFilter bloomFilter)
    {
        this.keyword = keyword;
        this.bloomFilter = bloomFilter;
    }

    public String getKeyword()
    {
        return this.keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public BloomFilter getBloomFilter()
    {
        return this.bloomFilter;
    }

    public void setBloomFilter(BloomFilter bloomFilter)
    {
        this.bloomFilter = bloomFilter;
    }
}
