package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.common.core.Token;
import com.goudong.authentication.common.core.UserDetail;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.lang.PageResult;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.req.BaseUserDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;

import java.util.List;

/**
 * 类描述：
 * 用户管理服务层接口
 * @author chenf
 */
public interface BaseUserManagerService {

    /**
     * 根据应用Id和用户名查询用户
     * @param appId     应用Id
     * @param username  用户名
     * @return          用户信息
     */
    BaseUser findOneByAppIdAndUsername(Long appId, String username);

    /**
     * 获取登录成功信息
     * @param myAuthentication  用户认证成功对象
     * @return                  用户基本信息和token
     */
    LoginResp login(MyAuthentication myAuthentication);

    /**
     * 刷新token
     * @param token refreshToken
     * @return      token对象
     */
    Token refreshToken(BaseUserRefreshTokenReq token);

    /**
     * 根据{@code token}获取用户信息
     * @param token     token
     * @return          用户信息
     */
    UserDetail getUserDetailByToken(String token);

    /**
     * 分页获取用户下拉，只返回操作人所在真实应用下的用户
     * @param req   请求参数
     * @return      用户下拉列表
     */
    PageResult<BaseUserDropDownResp> userDropDown(BaseUserDropDownReq req);

    /**
     * 分页查询用户
     * @param req   分页参数
     * @return      用户分页对象
     */
    PageResult<BaseUserPageResp> page(BaseUserPageReq req);

    /**
     * 简单方式创建用户
     * @param req   用户信息
     * @return      用户对象
     */
    BaseUserDTO simpleCreateUser(BaseUserSimpleCreateReq req);

    /**
     * 简单方式修改用户
     * @param req   修改用户参数
     * @return      用户信息
     */
    BaseUserDTO simpleUpdateUser(BaseUserSimpleUpdateReq req);

    /**
     * 批量删除用户
     * @param ids   被删除的用户id集合
     * @return      true:删除成功；false:删除失败
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 重置用户密码
     * @param userId    用户id
     * @return          true：修改成功；false：修改失败
     */
    Boolean resetPassword(Long userId);

    /**
     * 修改用户激活状态
     * @param userId    用户id
     * @return          true：修改成功；false：修改失败
     */
    Boolean changeEnabled(Long userId);

    /**
     * 修改用户锁定状态
     * @param userId    用户id
     * @return          true：修改成功；false：修改失败
     */
    Boolean changeLocked(Long userId);

    /**
     * 补充token信息
     * @param req 填充的内容
     * @return  填充后新生成的token
     */
    Token supplementToken(BaseUserSupplementTokenReq req);

    /**
     * 修改用户密码
     * @param req   前端请求参数
     * @return  true:修改成功；false：修改失败
     */
    Boolean changePassword(BaseUserChangePasswordReq req);

    /**
     * 给指定用户创建token
     * @param req   请求对象
     * @return  token
     */
    Token createToken(BaseUserCreateTokenReq req);
}
