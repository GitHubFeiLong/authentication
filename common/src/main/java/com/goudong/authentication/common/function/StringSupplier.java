package com.goudong.authentication.common.function;

import java.util.function.Supplier;

/**
 * 类描述：
 * 字符串函数式接口
 * @author chenf
 */
@FunctionalInterface
public interface StringSupplier extends Supplier<String> {

    @Override
    String get();
}
