package com.maochong.xiaojun.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author jokin
 * EhCache配置，需要在spring配置下 添加文件扫描：<context:component-scan base-package="com.maochong.xiaojun.config"/>
 * 因为这里使用dubbo的Main.main启动，Dubbo默认扫描路径calsspath:META-INF/spring下文件
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    /**
     * ehCache管理
     * */
    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }

    /**
     * 据shared与否的设置,Spring分别通过CacheManager.create()或new CacheManager()方式来创建一个ehcache基地.
     * */
    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("config/ehcache.xml"));
        cmfb.setShared(true);
        return cmfb;
    }

    /*
    <!-- 缓存配置(使用XML配置) -->
    <!-- 启用缓存注解功能(请将其配置在Spring主配置文件中) -->
    <cache:annotation-driven/>
    <!-- Spring自带的Cache!!! Spring自己的基于java.util.concurrent.ConcurrentHashMap实现的缓存管理器(该功能是从Spring3.1开始提供的) -->
    <!--
    <bean id="cacheManager" class="org.springframework.cache.support.SimpleCacheManager">
        <property name="caches">
            <set>
                <bean name="movieFindCache" class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean"/>  //对应@Cacheable(value="movieFindCache"
            </set>
        </property>
    </bean>
     -->
    <!-- 若只想使用Spring自身提供的缓存器,则注释掉下面的两个关于Ehcache配置的bean,并启用上面的SimpleCacheManager即可 -->
    <!-- Spring提供的基于的Ehcache实现的缓存管理器 -->
    <!--<bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheCacheManager">-->
        <!--<property name="cacheManager" ref="cacheManagerFactory"/>-->
    <!--</bean>-->

    <bean id="cacheManagerFactory" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
        <property name="configLocation" value="classpath:config/ehcache.xml"/>
        <property name="shared" value="true"/>
    </bean>
    * */
}
