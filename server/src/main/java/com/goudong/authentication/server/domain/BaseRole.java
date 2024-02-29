package com.goudong.authentication.server.domain;


import com.goudong.authentication.common.constant.CommonConst;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 角色表
 * @author chenf
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "base_role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class BaseRole extends BasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用id
     */
    @NotNull
    @Column(name = "app_id", nullable = false)
    private Long appId;

    /**
     * 角色名称
     */
    @NotNull
    @Size(min = 4, max = 16)
    @Column(name = "name", length = 16, nullable = false)
    private String name;

    /**
     * 备注
     */
    @Size(max = 255)
    @Column(name = "remark")
    private String remark;

    @ManyToMany(targetEntity=BaseUser.class, fetch = FetchType.LAZY)
    @JoinTable(name = "base_user_role", joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns={@JoinColumn(name = "user_id")})
    private List<BaseUser> users = new ArrayList<>();

    @ManyToMany(targetEntity=BaseMenu.class, fetch = FetchType.LAZY)
    @JoinTable(name = "base_role_menu", joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns={@JoinColumn(name = "menu_id")})
    private List<BaseMenu> menus = new ArrayList<>();

    @Override
    public String toString() {
        return "BaseRole{" +
                "id=" + id +
                ", appId=" + appId +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    /**
     * 是否是超级管理员
     * @return true 超级管理员，false 不是超级管理员
     */
    public boolean superAdmin() {
        return Objects.equals(this.name, CommonConst.ROLE_APP_SUPER_ADMIN);
    }

    /**
     * 判断登录用户，是否是管理员
     * @return true 管理员，false 普通用户
     */
    public boolean admin() {
        return Objects.equals(this.name, CommonConst.ROLE_APP_ADMIN);
    }
}
