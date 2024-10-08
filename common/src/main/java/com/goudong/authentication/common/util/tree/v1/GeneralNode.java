package com.goudong.authentication.common.util.tree.v1;


import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 在获取指定节点的详细信息时的对象
 * @author msi
 * @version 1.0
 */
@Setter
public class GeneralNode<T> {
    /**
     * 查找的本身对象
     */
    private T node;

    /**
     * 查找node的子元素集合
     */
    private List<T> children = new ArrayList<>();

    public T getNode() {
        return node;
    }

    public List<T> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralNode<?> that = (GeneralNode<?>) o;
        return Objects.equals(node, that.node) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, children);
    }

    @Override
    public String toString() {
        return "GeneralNode{" +
                "node=" + node +
                ", children=" + children +
                '}';
    }
}
