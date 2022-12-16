package com.lazy.agent.ipinfo.server.http;

import com.lazy.agent.ipinfo.base.SnowflakeIdUtils;
import com.lazy.agent.ipinfo.base.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;

public class HttpBasicUtil {

    /**
     * Authorization: Digest username=“xxxxx”,realm=“myTomcat”,qop=“auth”,nonce=“xxxxx”,uri=“xxxx”,cnonce=“xxxxxx”,nc=00000001,response=“xxxxxxxxx”,opaque=“xxxxxxxxx” 。其中username是用户名；cnonce是客户端生成的随机字符串；nc是运行认证的次数；response就是最终计算得到的摘要。
     *
     * @param request
     * @return
     */
    public static HttpDigest ofDigestRequest(HttpRequest request) {
        String authorization = request.getHeader("Authorization");
        if ((authorization != null) && (authorization.length() > 7)) {
            authorization = authorization.substring(7);
            HttpDigest digest = new HttpDigest();
            digest.parse(authorization);
            digest.setMethod(request.getMethod());
            return digest;
        }
        return null;
    }

    public static String base64DecodeNonce() {
        return base64Encode((System.currentTimeMillis() + "::" + SnowflakeIdUtils.getInstance().nextId()).getBytes(StandardCharsets.UTF_8))
                .replaceAll("==", "");
    }


    public static String base64DecodeNonce(String nonceBase64) {
        return base64Decode(nonceBase64);
    }

    public static HttpDigest ofDigestResponse(HttpRequest request) {
        HttpDigest digestResponse = new HttpDigest();
        digestResponse.setNonce(base64DecodeNonce());
        digestResponse.setQop("auth");
        digestResponse.setAlgorithm("MD5");
        digestResponse.setRealm("LazyAgentConsole");
        return digestResponse;
    }

    /**
     * 编码
     *
     * @param bstr
     * @return String
     */
    @SuppressWarnings("restriction")
    public static String base64Encode(byte[] bstr) {
        String strEncode = new BASE64Encoder().encode(bstr);
        return strEncode;
    }

    /**
     * 解码
     *
     * @param str
     * @return
     */
    public static String base64Decode(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String s = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(str);
            s = new String(b, StandardCharsets.UTF_8);
        } catch (Exception ignored) {

        }
        return s;
    }

}
