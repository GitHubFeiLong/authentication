package com.goudong.authentication.client.util;

import com.goudong.core.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * 类描述：
 * 认证服务客户端工具，用于调用接口
 * @author chenf
 */
@Slf4j
public class GoudongAuthenticationClient {
    //~fields
    //==================================================================================================================
    /**
     * 单例对象，懒汉式
     */
    private static GoudongAuthenticationClient client;

    /**
     * 是否执行过初始化，执行初始化后，不能二次执行初始操作
     * <pre>
     * 只有{@code flag}值是false时才能执行{@code init} 初始化,执行初始后，{@code flag} 值是true，再次执行初始化操作为抛出异常
     * </pre>
     */
    private static boolean flag = false;

    /**
     * 应用id
     */
    private Long appId;

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
    public synchronized static GoudongAuthenticationClient init(String serverUrl, Long appId, String serialNumber, String privateKeyStr) {
        if (flag) {
            log.error("只能初始化一次客户端信息");
            throw new RuntimeException("请勿重复执行初始流程");
        }
        AssertUtil.isNotBlank(serverUrl, "serverUrl不能为空");
        AssertUtil.isNotNull(appId, "appId不能为null");
        AssertUtil.isNotBlank(serialNumber, "serialNumber不能为空");
        AssertUtil.isNotBlank(privateKeyStr, "privateKey不能为空");
        client = new GoudongAuthenticationClient();
        client.serverUrl = serverUrl;
        client.appId = appId;
        client.serialNumber = serialNumber;
        client.privateKeyStr = privateKeyStr;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            client.privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        flag = true;
        return client;
    }

    /**
     * 获取对象
     * @return client对象
     */
    public static GoudongAuthenticationClient getClient() {
        if (client != null) {
            return client;
        }
        throw new RuntimeException("请先初始化客户端");
    }

    private GoudongAuthenticationClient() {

    }

    public Long getAppId() {
        return appId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getPrivateKeyStr() {
        return privateKeyStr;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    //~接口方法调用
    //==================================================================================================================

}
