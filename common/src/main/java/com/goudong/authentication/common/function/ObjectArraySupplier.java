package com.goudong.authentication.common.function;

import java.util.function.Supplier;

/**
 * 类描述：
 * 
 * @author chenf
 */
@FunctionalInterface
public interface ObjectArraySupplier extends Supplier<Object[]> {

    @Override
    Object[] get();
}
