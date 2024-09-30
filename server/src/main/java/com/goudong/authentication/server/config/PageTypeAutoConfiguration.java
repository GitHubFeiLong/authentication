package com.goudong.authentication.server.config;

import com.goudong.authentication.server.util.PageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * 类描述：
 * 分页类型自动配置
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 14:55
 */
public class PageTypeAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(PageTypeAutoConfiguration.class);

    @Bean
    @ConditionalOnClass(name = {"org.springframework.data.jpa.repository.JpaRepository"})
    public void springData() {
        if (log.isDebugEnabled()) {
           log.debug("CLIENT_TYPES add JPA");
        }
        PageTypeEnum.CLIENT_TYPES.add(PageTypeEnum.JPA);
    }

}
