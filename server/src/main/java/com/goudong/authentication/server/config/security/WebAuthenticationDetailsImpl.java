package com.goudong.authentication.server.config.security;

import com.goudong.authentication.common.util.AssertUtil;
import com.goudong.authentication.common.util.StringUtil;
import com.goudong.authentication.server.constant.HttpHeaderConst;
import com.goudong.authentication.server.exception.BasicException;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 类描述：
 * 自定义web身份验证详细信息(用于登录验证中增加额外参数)
 *
 * @see AuthenticationDetailsSourceImpl
 * @see Authentication#getDetails() 能获取到这里配置的额外信息
 * @author chenf
 */
@Getter
public class WebAuthenticationDetailsImpl extends WebAuthenticationDetails implements Serializable {

    private static final long serialVersionUID = 660000566548310470L;

    /**
     * 登录时选择的应用Id
     */
    Long selectAppId;

    /**
     * 请求头中的应用Id
     */
    Long xAppId;

    WebAuthenticationDetailsImpl(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
        // 下拉选应用
        String selectAppIdStr = httpServletRequest.getParameter("appId");
        selectAppId = StringUtil.isNotBlank(selectAppIdStr) ? Long.parseLong(selectAppIdStr) : null;

        // 请求头应用Id
        String xAppIdStr = httpServletRequest.getHeader(HttpHeaderConst.X_APP_ID);
        AssertUtil.isNotBlank(xAppIdStr, () -> BasicException.client(String.format("请求头%s丢失", HttpHeaderConst.X_APP_ID)));
        xAppId = Long.parseLong(xAppIdStr);
    }

}
