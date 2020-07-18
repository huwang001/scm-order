package com.lyf.scm.core.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyf.scm.common.util.date.MyDateFormat;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.text.DateFormat;

/**
 * 对restTemplate封装,主要用于通过innerapi调用外部接口
 * http头部已带X-Co-Client
 * url已带innerapi地址
 */
@Slf4j
@Component
@AllArgsConstructor
public class RestClient {

    private final AppConfig appConfig;
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateFormat dateFormat = objectMapper.getDateFormat();
        objectMapper.setDateFormat(new MyDateFormat(dateFormat));
    }

    /**
     * @param uri
     * @param body 对应RequestBody类型参数
     * @return
     */
//    public <T, B> T post(String uri, B body, TypeReference<T> typeReference) throws Exception {
//        String response = HttpRequest.post(appConfig.getInnerApi() + uri)
//                .header("X-Co-Client", appConfig.getClientId())
//                .body(objectMapper.writeValueAsString(body))
//                .execute().body();
//        return objectMapper.readValue(response, typeReference);
//    }

    /**
     * @param uri
     * @param typeReference
     * @param path          对应PathVariable的变量
     * @param <T>
     * @return
     */
    public <T> T post(String uri, TypeReference<T> typeReference, String... path) throws Exception {
        // 处理PathVariable的变量
        for (String value : path) {
            String replaceStr = "{".concat(StrUtil.subBetween(uri, "{", "}")).concat("}");
            uri = StringUtils.replace(uri, replaceStr, value);
        }
        return post(uri, typeReference);
    }

//    public <T> T post(String uri, TypeReference<T> typeReference) throws Exception {
//        String response = HttpRequest.post(appConfig.getInnerApi() + uri)
//                .header("X-Co-Client", appConfig.getClientId())
//                .execute().body();
//        return objectMapper.readValue(response, typeReference);
//    }

    /**
     * 对于uri中包含PathVariable的变量
     *
     * @param uri
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T get(String uri, TypeReference<T> typeReference, String... path) throws Exception {
        // 处理PathVariable的变量
        for (String value : path) {
            String replaceStr = "{".concat(StrUtil.subBetween(uri, "{", "}")).concat("}");
            uri = StringUtils.replace(uri, replaceStr, value);
        }
        return get(uri, typeReference);
    }

//    public <T> T get(String uri, TypeReference<T> typeReference) throws Exception {
//        String response = HttpRequest.get(appConfig.getInnerApi() + uri)
//                .header("X-Co-Client", appConfig.getClientId())
//                .execute().body();
//        return objectMapper.readValue(response, typeReference);
//    }

    public <T, B> T postSap(String uri, B body, TypeReference<T> typeReference) throws Exception {
        String response = HttpRequest.post(appConfig.getSapUrl() + uri)
                .header("Authorization", getHeader(appConfig.getSapUserName(), appConfig.getSapPassWord()))
                .body(objectMapper.writeValueAsString(body))
                .execute().body();
        return objectMapper.readValue(response, typeReference);
    }

    /**
     * basic认证
     *
     * @param userName
     * @param passWord
     * @return
     */
    private static String getHeader(String userName, String passWord) {
        String auth = userName + ":" + passWord;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
        String authHeader = "Basic " + new String(encodedAuth, Charset.forName("UTF-8"));
        return authHeader;
    }

}