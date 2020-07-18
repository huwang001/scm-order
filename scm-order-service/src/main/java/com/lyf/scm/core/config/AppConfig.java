package com.lyf.scm.core.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置
 * @author zhangxu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    /**
     * 商户id
     */
    private Long merchantId = 10000L;

    /**
     * 团购发货仓类型
     */
    private Integer deliveryWarehouseType = 14;

    /**
     * innerapi client id
     */
    private String clientId;
    
    private String sapUserName;
    
    private String sapPassWord;
    
    private String sapUrl;

}