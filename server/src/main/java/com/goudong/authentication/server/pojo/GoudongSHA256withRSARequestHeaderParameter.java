package com.goudong.authentication.server.pojo;

import com.goudong.authentication.common.security.cer.CertificateUtil;
import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.server.exception.ClientException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：
 * 请求头Authorization 模式是GOUDONG-SHA256withRSA的参数
 * @author chenf
 */
@Slf4j
@Data
public class GoudongSHA256withRSARequestHeaderParameter {
    //~fields
    //==================================================================================================================
    /**
     * 应用id
     */
    private Long appId;
    /**
     * 16位证书序列号
     */
    private String serialNumber;
    /**
     * 时间戳
     */
    private long timestamp;
    /**
     * 12位随机字符串
     */
    private String nonceStr;
    /**
     * 请求参数签名
     */
    private String signature;

    //~methods
    //==================================================================================================================

    /**
     * 根据令牌提取参数（证书序列号、时间戳、12位随机字符串、请求参数签名）
     * @param authentication    令牌
     * @return  提取结果对象
     */
    public static GoudongSHA256withRSARequestHeaderParameter getInstance(String authentication) {
        Pattern pattern = Pattern.compile("GOUDONG-SHA256withRSA appid=\"(\\d+)\",serial_number=\"(.*)\",timestamp=\"(\\d+)\",nonce_str=\"(.*)\",signature=\"(.*)\"");
        Matcher matcher = pattern.matcher(authentication);
        AssertUtil.isTrue(matcher.matches(), "请求头格式错误");
        GoudongSHA256withRSARequestHeaderParameter parameter = new GoudongSHA256withRSARequestHeaderParameter();
        parameter.appId =  Long.parseLong(matcher.group(1));        // 应用id
        parameter.serialNumber = matcher.group(2);                  // 16位证书序列号
        parameter.timestamp = Long.parseLong(matcher.group(3));     // 时间戳
        parameter.nonceStr =  matcher.group(4);                     // 12位随机字符串
        parameter.signature =  matcher.group(5);                    // 参数签名

        parameter.check();
        return parameter;
    }
    private GoudongSHA256withRSARequestHeaderParameter(){

    }

    /**
     * 对提取到的参数进行校验
     */
    private void check() {
        AssertUtil.isTrue(this.serialNumber != null && this.serialNumber.length() == 16, "请求头格式错误");
        AssertUtil.isTrue(this.nonceStr != null && this.nonceStr.length() == 12, "请求头格式错误");
        AssertUtil.isTrue(this.signature != null, "请求头格式错误");
    }

    /**
     * 对请求参数进行验签
     * @param requestBody   请求体内容
     */
    public void verify(String requestBody, String cert) {
        // 验签
        // 拼装消息 // 转换byte[]
        byte[] message = (appId + "\n" + serialNumber + "\n" + timestamp + "\n" + nonceStr + "\n" + requestBody + "\n").getBytes(StandardCharsets.UTF_8);
        try {
            // 加载证书
            X509Certificate certificate = CertificateUtil.loadCertificate(cert);
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(certificate);
            sign.update(message);
            byte[] signatureB = Base64.decodeBase64(signature); // 拉卡拉返回的签名转byte[]
            boolean verify = sign.verify(signatureB); // 验签,验拉卡拉返回的签名

            if (verify) {
                log.info("验证签名结束，验证成功");
            } else {
                log.error("验证签名结束，验证失败");
            }

            AssertUtil.isTrue(verify, () -> ClientException.client("验签失败"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前java环境不支持SHA256withRSA", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("无效的证书", e);
        } catch (SignatureException e) {
            throw new RuntimeException("签名验证过程发生了错误", e);
        }
    }
}
