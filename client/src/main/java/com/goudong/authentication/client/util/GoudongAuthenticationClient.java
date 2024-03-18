package com.goudong.authentication.client.util;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 类描述：
 * 认证服务客户端工具，存储客户端的应用信息
 * @author chenf
 */
@Slf4j
public class GoudongAuthenticationClient {
    //~fields
    //==================================================================================================================
    /**
     * 应用集合，key是应用Id，value是应用对应的配置证书等信息（使用满足应用）。
     */
    private static final Map<Long, GoudongAuthenticationClient> CLIENT_MAP = new HashMap<>();

    /**
     * <p>客户端配置的默认应用id，当客户端第一次执行初始应用时，将应用id作为默认应用id，且不会发生修改。</p>
     * <p>方便某些单应用程序，使用API时可以不再单独去传应用id</p>
     */
    private static Long DEFAULT_APP_ID = null;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 证书序号
     */
    private String serialNumber;

    /**
     * 私钥
     */
    private String privateKeyStr;

    /**
     * 认证服务的主机
     */
    private String serverUrl;

    /**
     * 私钥对象
     */
    private PrivateKey privateKey;

    //~methods
    //==================================================================================================================
    /**
     * 初始化
     * @param serverUrl     认证服务接口前缀，例如：https://api.goudong.com/api/authentication-server
     * @param appId         应用id
     * @param serialNumber  证书序号
     * @param privateKeyStr 私钥
     */
    public synchronized static GoudongAuthenticationClient init(String serverUrl, Long appId, String appSecret, String serialNumber, String privateKeyStr) {
        AssertUtil.isNotBlank(serverUrl, "serverUrl不能为空");
        AssertUtil.isNotNull(appId, "appId不能为null");
        AssertUtil.isNotBlank(appSecret, "appSecret不能为空");
        AssertUtil.isNotBlank(serialNumber, "serialNumber不能为空");
        AssertUtil.isNotBlank(privateKeyStr, "privateKey不能为空");
        boolean containsApp = CLIENT_MAP.containsKey(appId);
        GoudongAuthenticationClient client = containsApp ? CLIENT_MAP.get(appId) : new GoudongAuthenticationClient();
        client.serverUrl = serverUrl;
        client.appId = appId;
        client.appSecret = appSecret;
        client.serialNumber = serialNumber;
        client.privateKeyStr = privateKeyStr;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            client.privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        if (!containsApp) {
            CLIENT_MAP.put(appId, client);
        }
        if (DEFAULT_APP_ID == null) {
            DEFAULT_APP_ID = appId;
        }
        return client;
    }

    /**
     * 判断应用是否存在客户端初始信息
     * @param appId 应用id
     * @return true：存在初始信息；false：不存在初始信息
     */
    public static boolean containsApp(Long appId) {
        return CLIENT_MAP.containsKey(appId);
    }

    /**
     * 获取默认应用客户端对象
     * @return 默认client对象
     */
    public static GoudongAuthenticationClient getDefaultClient() {
        AssertUtil.isNotNull(DEFAULT_APP_ID, "应用还未执行初始，请先初始应用信息。");
        return CLIENT_MAP.get(DEFAULT_APP_ID);
    }

    /**
     * 获取对象
     * @return client对象
     */
    public static GoudongAuthenticationClient getClient(Long appId) {
        if (appId == null) {
            return getDefaultClient();
        }
        return CLIENT_MAP.get(appId);
    }

    /**
     * 私有构造方法
     */
    private GoudongAuthenticationClient() {

    }

    /**
     * 获取应用id
     * @return  应用id
     */
    public Long getAppId() {
        return appId;
    }

    /**
     * 获取应用密钥
     * @return  应用密钥
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * 获取证书序号
     * @return  证书序号
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * 获取私钥BASE64字符串
     * @return 私钥字符串
     */
    public String getPrivateKeyStr() {
        return privateKeyStr;
    }

    /**
     * 获取认证服务url
     * @return  认证服务url
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * 获取私钥对象
     * @return  私钥对象
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 生成请求令牌，用于认证服务验签
     * @param body  请求体字符串
     * @return  GOUDONG-SHA256withRSA 模式的令牌
     */
    public String generateToken(String body) {
        return GouDongUtil.generateToken(this.appId, this.serialNumber, body, this.privateKey);
    }

}
