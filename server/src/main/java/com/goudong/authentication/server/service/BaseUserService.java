package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.lang.PageResult;
import com.goudong.authentication.server.rest.req.BaseUserPageReq;
import com.goudong.authentication.server.rest.req.BaseUserDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.dto.BaseUserDTO;

import java.util.Date;
import java.util.List;

/**
 * Service Interface for managing {@link BaseUser}.
 */
public interface BaseUserService {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 根据应用Id和用户名查询用户
     * @param appId     应用Id
     * @param username  用户名
     * @return          用户
     */
    BaseUser findOneByAppIdAndUsername(Long appId, String username);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户对象
     */
    BaseUser findById(Long id);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户对象详细信息（保留角色菜单）
     */
    BaseUser findDetailById(Long id);

    /**
     * 分页获取用户下拉，只返回操作人所在真实应用下的用户
     *
     * @param req 请求参数
     * @return 用户下拉列表
     */
    PageResult<BaseUserDropDownResp> userDropDown(BaseUserDropDownReq req);

    /**
     * 分页查询用户
     *
     * @param req 分页参数
     * @return 用户分页对象
     */
    PageResult<BaseUserPageResp> page(BaseUserPageReq req);

    /**
     * 新增/修改用户
     * @param user
     * @return
     */
    BaseUserDTO save(BaseUser user);

    /**
     * 批量新增/修改用户
     * @param users 用户
     * @return
     */
    List<BaseUserDTO> saveAll(List<BaseUser> users);

    /**
     * 批量删除用户
     *
     * @param ids 被删除的用户id集合
     * @return true删除成功；false删除失败
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 重置用户密码
     * @param userId 用户id
     * @return true：修改成功；false：修改失败
     */
    Boolean resetPassword(Long userId);

    /**
     * 修改用户激活状态
     *
     * @param userId 用户id
     * @return
     */
    Boolean changeEnabled(Long userId);

    /**
     * 修改用户锁定状态
     *
     * @param userId 用户id
     * @return
     */
    Boolean changeLocked(Long userId);

    /**
     * 查询用户
     * @param realAppId 用户的真实应用id
     * @param username  用户名
     * @return  用户对象
     */
    BaseUser findOneByRealAppIdAndUsername(Long realAppId, String username);

    /**
     * 查询应用管理员
     * @param appId     应用id
     * @param realAppId 真实应用id
     * @param name      应用名
     * @return  应用管理员
     */
    BaseUser findOneByAppIdAndRealAppIdAndUsername(Long appId, Long realAppId, String name);

    /**
     * 更新用户最近登录时间
     * @param lastLoginTime 最近登录时间
     * @param id    用户ID
     * @return  修改成功的记录数
     */
    int updateLastLoginTime(Date lastLoginTime, Long id);
}
