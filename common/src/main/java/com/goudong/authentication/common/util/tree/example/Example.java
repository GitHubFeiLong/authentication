package com.goudong.authentication.common.util.tree.example;

import com.goudong.authentication.common.util.CollectionUtil;
import com.goudong.authentication.common.util.tree.v1.GeneralNode;
import com.goudong.authentication.common.util.tree.v1.GeneralStructureHandler;
import com.goudong.authentication.common.util.tree.v1.TreeStructureHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * tree的使用示例
 * @author msi
 */
public class Example {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        // 第一棵树
        Menu menu1 = new Menu(1, null, null);
        Menu menu2 = new Menu(2, 1, null);
        Menu menu3 = new Menu(3, 1, null);
        Menu menu4 = new Menu(4, 2, null);
        Menu menu5 = new Menu(5, 4, null);

        // 第二棵树
        Menu menu6 = new Menu(6, null, null);
        Menu menu7 = new Menu(7, 6, null);
        Menu menu8 = new Menu(8, 7, null);
        Menu menu9 = new Menu(9, 8, null);

        ArrayList<Menu> menus = CollectionUtil.newArrayList(menu1, menu2, menu3, menu4, menu5);
        menus.addAll(CollectionUtil.newArrayList(menu6, menu7, menu8, menu9));
        System.out.println("准备的参数 menus = " + menus);

        /*
            构造函数参数不能出错。
            根据需求，创建不同的实例对象
                TreeStructureHandler：树形相关的处理器
                GeneralStructureHandler：一维结构的处理器
         */
        /*
            树
         */
        TreeStructureHandler<Menu> treeStructureHandler = new TreeStructureHandler<Menu>("id", "parentId", "children", menus);
        // 获取转换后的树形结构（树）
        List<Menu> expand = treeStructureHandler.toTreeStructure();
        System.out.println("expand = " + expand);
        // 获取一维结构集合（一般）
        List<Menu> toGeneralStructure = treeStructureHandler.toGeneralStructure();
        System.out.println("toGeneralStructure = " + toGeneralStructure);
        // 获取指定节点详细信息（一般）
        GeneralNode<Menu> nodeDetailBySelfValue2GeneralNode = treeStructureHandler.getNodeDetailBySelfValue2GeneralNode(8);
        System.out.println("nodeDetailBySelfValue2GeneralNode = " + nodeDetailBySelfValue2GeneralNode);
        // 获取指定节点详细信息(树)
        Menu nodeDetailBySelfValue = treeStructureHandler.getNodeDetailBySelfValue2T(2);
        System.out.println("nodeDetailBySelfValue = " + nodeDetailBySelfValue);

        System.out.println("====分割线====");
        /*
            一般
         */
        GeneralStructureHandler<Menu> menuGeneralStructureHandler = new GeneralStructureHandler<>("id", "parentId", "children", expand);
        // 获取转换后的树形结构（树）
        List<Menu> toTreeStructure = menuGeneralStructureHandler.toTreeStructure();
        System.out.println("toTreeStructure = " + toTreeStructure);
        // 获取一维结构集合（一般）
        List<Menu> toGeneralStructure1 = menuGeneralStructureHandler.toGeneralStructure();
        System.out.println("toGeneralStructure1 = " + toGeneralStructure1);
        // 获取指定节点详细信息（一般）
        GeneralNode<Menu> nodeDetailBySelfValue2GeneralNode1 = menuGeneralStructureHandler.getNodeDetailBySelfValue2GeneralNode(8);
        System.out.println("nodeDetailBySelfValue2GeneralNode1 = " + nodeDetailBySelfValue2GeneralNode1);
        // 获取指定节点详细信息(树)
        Menu nodeDetailBySelfValue2T = menuGeneralStructureHandler.getNodeDetailBySelfValue2T(2);
        System.out.println("nodeDetailBySelfValue2T = " + nodeDetailBySelfValue2T);


        System.out.println(1);
    }
}
