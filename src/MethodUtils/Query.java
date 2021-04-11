package MethodUtils;


/**
 * Created by 96130 on 2018/5/2.
 */
public class Query
{
    private String keyword;
    private byte[] pubKey;//public key
    private int symbol;//并,0;交,1;差,2

    public Query()
    {
        this.keyword = null;
        this.pubKey = null;
        this.symbol = 0;
    }

    public Query(String keyword,byte[] pubKey,int symbol)
    {
        this.keyword = keyword;
        this.pubKey = pubKey;
        this.symbol = symbol;
    }

    public String getKeyword()
    {
        return this.keyword;
    }

    public void setKeyword(String s)
    {
        this.keyword = s;
    }

    public byte[] getPubKey()
    {
        return this.pubKey;
    }

    public void setPubKey(byte[] b)
    {
        this.pubKey = b;
    }

    public int getSymbol()
    {
        return this.symbol;
    }

    public void setSymbol(int a)
    {
        if(a==0||a==1||a==2)
        {
            this.symbol=a;
        }
        else
        {
            this.symbol=0;
        }
    }
}
