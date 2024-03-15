package com.goudong.authentication.client.function;

import java.util.function.Supplier;

/**
 * 接口描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/27 10:01
 */
@FunctionalInterface
public interface ObjectArraySupplier extends Supplier<Object[]> {

    @Override
    Object[] get();
}
