package com.github.lzm320a99981e.zodiac.tools;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 树形结构转换器
 */
public class TreeTransfer {
    private static final String DEFAULT_ID_KEY = "id";
    private static final String DEFAULT_PARENT_KEY = "parent";
    private static final String DEFAULT_CHILDREN_KEY = "children";

    /**
     * 节点ID
     */
    private String idKey = DEFAULT_ID_KEY;
    /**
     * 上级节点ID
     */
    private String parentKey = DEFAULT_PARENT_KEY;
    /**
     * 子节点
     */
    private String childrenKey = DEFAULT_CHILDREN_KEY;


    private TreeTransfer() {
    }

    private TreeTransfer(String idKey, String parentKey, String childrenKey) {
        this.idKey = Preconditions.checkNotNull(idKey);
        this.parentKey = Preconditions.checkNotNull(parentKey);
        this.childrenKey = Preconditions.checkNotNull(childrenKey);
    }

    public static TreeTransfer create(String idKey, String parentKey, String childrenKey) {
        return new TreeTransfer(idKey, parentKey, childrenKey);
    }

    public static TreeTransfer create() {
        return new TreeTransfer();
    }

    public TreeTransfer idKey(String idKey) {
        this.idKey = Preconditions.checkNotNull(idKey);
        return this;
    }

    public TreeTransfer parentKey(String parentKey) {
        this.parentKey = Preconditions.checkNotNull(parentKey);
        return this;
    }

    public TreeTransfer childrenKey(String childrenKey) {
        this.childrenKey = Preconditions.checkNotNull(childrenKey);
        return this;
    }

    /**
     * 将平滑数据转换为树状结构数据
     *
     * @param nodes
     * @return
     */
    public List transform(Collection nodes) {
        final Class<?> nodeClass = nodes.iterator().next().getClass();
        if (Map.class.isAssignableFrom(nodeClass)) {
            return nodesToTree(nodes);
        }
        final List<?> beanNodes = JSON.parseArray(JSON.toJSONString(nodes));
        return JSON.parseArray(JSON.toJSONString(nodesToTree((Collection<Map>) beanNodes)), nodeClass);
    }

    /**
     * 将评级节点转换为树状结构
     *
     * @param nodes
     * @return
     */
    private List<Map> nodesToTree(Collection<Map> nodes) {
        // 匹配子节点
        for (Map node : nodes) {
            matchChildren(node, nodes);
        }
        // 所有的节点的ID
        List<Object> ids = nodes.stream().map(node -> node.get(idKey)).collect(Collectors.toList());
        // 返回没有父节点的节点（顶级节点）
        return nodes.stream().filter(node -> (Objects.isNull(node.get(parentKey)) || !ids.contains(node.get(parentKey)))).collect(Collectors.toList());
    }

    /**
     * 匹配子节点
     *
     * @param parent
     * @param nodes
     */
    private void matchChildren(Map parent, Collection<Map> nodes) {
        Object parentId = parent.get(idKey);
        for (Map node : nodes) {
            if (Objects.equals(parentId, node.get(parentKey))) {
                if (Objects.isNull(parent.get(childrenKey))) {
                    parent.put(childrenKey, new ArrayList<>());
                }
                ((Collection) parent.get(childrenKey)).add(node);
            }
        }
    }
}