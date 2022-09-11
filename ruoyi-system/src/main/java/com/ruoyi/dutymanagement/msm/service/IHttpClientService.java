package com.ruoyi.dutymanagement.msm.service;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.dutymanagement.msm.domain.param.LoginInfo;

/**
 *
 * 调外系统接口
 *
 */
public interface IHttpClientService {
    /**
     * 调值班管理系统短信接口
     * @param success
     * @return
     */
    public JSONObject doMsm(String success);

    /**
     * 调值班管理系统邮件接口
     * @return
     */
    public JSONObject doMail(String status);

    /**
     * 获取token
     * @param loginInfo
     * @return
     */
    public  String getToken(LoginInfo loginInfo);

}
