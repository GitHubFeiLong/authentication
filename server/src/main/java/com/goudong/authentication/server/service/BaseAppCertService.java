package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseAppCert;
import com.goudong.authentication.server.rest.req.BaseAppCertCreateReq;
import com.goudong.authentication.server.service.dto.BaseAppCertDTO;

import java.util.List;

/**
 * Service Interface for managing {@link BaseAppCert}.
 */
public interface BaseAppCertService {
    //~methods
    //==================================================================================================================
    /**
     * 根据证书序号查询证书
     * @param serialNumber 证书序号
     * @return 证书
     */
    BaseAppCertDTO getBySerialNumber(String serialNumber);

    /**
     * 查询应用的所有证书
     *
     * @param appId 应用id
     * @return 应用所有证书
     */
    List<BaseAppCertDTO> listCertsByAppId(Long appId);

    /**
     * 创建证书
     *
     * @param req 创建参数
     * @return 证书记录
     */
    BaseAppCertDTO save(BaseAppCertCreateReq req);
}
