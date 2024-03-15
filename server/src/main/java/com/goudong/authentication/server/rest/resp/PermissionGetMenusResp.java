package com.goudong.authentication.server.rest.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 获取所有菜单
 * @author cfl
 * @version 1.0
 */
@Data
public class PermissionGetMenusResp implements Serializable{

    private static final long serialVersionUID = 5299706948323095938L;

    @ApiModelProperty("菜单集合")
    private List<MenuInner> menus;

    /**
     * 类描述：
     * 菜单基本信息
     * @author cfl
     * @version 1.0
     */
    @Data
    public static class MenuInner{
        @ApiModelProperty(value = "菜单id")
        private Long id;

        /**
         * 父级主键id
         */
        @ApiModelProperty(value = "父级主键id")
        private Long parentId;

        /**
         * 应用id
         */
        @NotNull
        @ApiModelProperty(value = "应用id", required = true)
        private Long appId;

        /**
         * 权限标识
         */
        @NotNull
        @Size(min = 1, max = 64)
        @ApiModelProperty(value = "权限标识", required = true)
        private String permissionId;

        /**
         * 菜单名称
         */
        @NotNull
        @Size(min = 1, max = 64)
        @ApiModelProperty(value = "菜单名称", required = true)
        private String name;

        /**
         * 菜单类型（1：菜单；2：按钮；3：接口）
         */
        @NotNull
        @Min(value = 1)
        @Max(value = 3)
        @ApiModelProperty(value = "菜单类型（1：菜单；2：按钮；3：接口）", required = true)
        private Integer type;

        /**
         * 路由或接口地址
         */
        @Size(max = 255)
        @ApiModelProperty(value = "路由或接口地址")
        private String path;

        /**
         * 请求方式
         */
        @ApiModelProperty(value = "请求方式")
        private String method;

        /**
         * 排序字段（值越小越靠前，仅仅针对前端路由）
         */
        @Min(value = 1)
        @Max(value = 2147483647)
        @ApiModelProperty(value = "排序字段（值越小越靠前，仅仅针对前端路由）")
        private Integer sortNum;

        /**
         * 是否是隐藏菜单
         */
        @NotNull
        @ApiModelProperty(value = "是否是隐藏菜单", required = true)
        private Boolean hide;

        /**
         * 前端菜单元数据
         */
        @Size(max = 255)
        @ApiModelProperty(value = "前端菜单元数据")
        private String meta;
    }
}
