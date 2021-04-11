package BasicComponents;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
/**
 * Created by 96130 on 2019/1/25.
 */
public class AESCoder
{
    //非对称密钥算法
    public static final String KEY_ALGORITHM="ElGamal";


    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是8的倍数，在160到16384位之间
     * */
    private static final int KEY_SIZE=256;
    //公钥
    private static final String PUBLIC_KEY="ElGamalPublicKey";

    //私钥
    private static final String PRIVATE_KEY="ElGamalPrivateKey";

    /**
     * 初始化密钥对
     * @return Map 甲方密钥的Map
     * */
    public static Map<String,Object> initKey() throws Exception{
        //加入对BouncyCastle支持
        Security.addProvider(new BouncyCastleProvider());
        AlgorithmParameterGenerator apg=AlgorithmParameterGenerator.getInstance(KEY_ALGORITHM);
        //初始化参数生成器
        apg.init(KEY_SIZE);
        //生成算法参数
        AlgorithmParameters params=apg.generateParameters();
        //构建参数材料
        DHParameterSpec elParams=(DHParameterSpec)params.getParameterSpec(DHParameterSpec.class);

        //实例化密钥生成器
        KeyPairGenerator kpg=KeyPairGenerator.getInstance(KEY_ALGORITHM) ;

        //初始化密钥对生成器
        kpg.initialize(elParams,new SecureRandom());

        KeyPair keyPair=kpg.generateKeyPair();
        //甲方公钥
        PublicKey publicKey= keyPair.getPublic();
        //甲方私钥
        PrivateKey privateKey= keyPair.getPrivate();
        //将密钥存储在map中
        Map<String,Object> keyMap=new HashMap<String,Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }


    /**
     * 公钥加密
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密数据
     * */
    public static byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception{

        //实例化密钥工厂
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey=keyFactory.generatePublic(x509KeySpec);

        //数据加密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }
    /**
     * 私钥解密
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密数据
     * */
    public static byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception{
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     * @param keyMap 密钥map
     * @return byte[] 私钥
     * */
    public static byte[] getPrivateKey(Map<String,Object> keyMap) throws Exception
    {
        Key key=(Key)keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }
    /**
     * 取得公钥
     * @param keyMap 密钥map
     * @return byte[] 公钥
     * */
    public static byte[] getPublicKey(Map<String,Object> keyMap) throws Exception
    {
        Key key=(Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }
}
