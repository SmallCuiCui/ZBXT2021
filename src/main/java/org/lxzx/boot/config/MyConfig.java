package org.lxzx.boot.config;

import org.springframework.context.annotation.Configuration;

//配置类MyConfig本身也是一个类
@Configuration(proxyBeanMethods = true)
// proxyBeanMethods = true  默认模式，全代理。多次调用得到的是相同实例，解决组件依赖问题，保证单实例
// proxyBeanMethods = false  多次调用得到的是不同实例
public class MyConfig {



}
