package com.ruoyi.dutymanagement.msm.service;

import com.alibaba.fastjson2.JSONObject;

public interface IHttpPostClientService {
    /**
     * 调取值班管理系统短信接口
     * @param success
     * @return
     */
    public JSONObject doPost(String success);
}
