package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;


/**
 * Spring Data  repository for the BaseUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long>, JpaSpecificationExecutor<BaseUser> {

    @Query(value = "from BaseUser where appId=?1 and username=?2")
    BaseUser findByLogin(Long appId, String login);

    /**
     * 删除应用下的所有用户
     * @param appId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true, value = "delete from base_user where app_id = ?1 or real_app_id = ?1")
    int deleteByAppId(Long appId);

    /**
     * 根据真实应用id和用户名查询用户
     * @param realAppId 真实应用id
     * @param username  用户名
     * @return  用户
     */
    BaseUser findByRealAppIdAndUsername(Long realAppId, String username);

    /**
     * 根据应用id和真实应用id和用户名查询用户
     * @param appId     应用id
     * @param realAppId 真实应用id
     * @param username  用户名
     * @return  用户
     */
    BaseUser findByAppIdAndRealAppIdAndUsername(Long appId, Long realAppId, String username);

    /**
     * 更新最近登录时间，必须登录过才更新
     * @param lastLoginTime 最近登录时间
     * @param id    用户ID
     * @return  修改记录数
     */
    @Modifying
    @Query(nativeQuery = true, value = "update base_user set last_login_time=?1 where id = ?2")
    int updateLastLoginTime(Date lastLoginTime, Long id);
}
