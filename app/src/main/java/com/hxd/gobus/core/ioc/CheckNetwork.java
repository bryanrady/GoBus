package com.hxd.gobus.core.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 自定义一个注解 使用在点击事件的时候 查看网络连接是否正常
 * @author wangqingbin
 * @time 2019/7/10 16:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckNetwork {

    String value() default IocInjectUtils.NETWORK_NOT_CONNECT;

}
