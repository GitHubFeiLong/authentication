package com.goudong.authentication.common.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 类描述：
 * Function获取序列化能力
 * @author msi
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {}