package com.ruoyi.dutymanagement.msm.service;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.dutymanagement.msm.domain.param.MsmParam;
import com.ruoyi.dutymanagement.msm.domain.vo.MsmVO;

import java.util.List;

public interface IShortMessageService {
    /**
     * 查询短信列表
     * @param msmParam
     * @return
     */
    public List<MsmVO> list(MsmParam msmParam);

    /**
     * 查询短信详情
     * @param sendInfoId
     * @return
     */
    public MsmVO getInfoById(String sendInfoId);
    /**
     * 与机器人接口
     * @return
     */
    public JSONObject getJsonObject();
}
