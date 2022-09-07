package com.ruoyi.dutymanagement.msm.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.dutymanagement.msm.domain.MsmEntity;
import com.ruoyi.dutymanagement.msm.domain.MsmInfoEntity;
import com.ruoyi.dutymanagement.msm.domain.param.MsmParam;
import com.ruoyi.dutymanagement.msm.domain.vo.MsmVO;
import com.ruoyi.dutymanagement.msm.httpclient.HttpPostClient;
import com.ruoyi.dutymanagement.msm.mapper.ShortMessageMapper;
import com.ruoyi.dutymanagement.msm.service.IShortMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class ShortMessageImpl implements IShortMessageService {

    @Autowired
    private ShortMessageMapper shortMessageMapper;

    private HttpPostClient httpPostClient;

    /**
     * 查询短信列表
     * @param msmParam
     * @return
     */
    @Override
    public List<MsmVO> list(MsmParam msmParam) {
        msmParam.setBeginTime(new Date());
        List<MsmVO> msmVOList = shortMessageMapper.list(msmParam);
        return msmVOList;
    }

    /**
     * 查询短信详情
     * @param sendInfoId
     * @return
     */
    @Override
    public MsmVO getInfoById(String sendInfoId) {
        int mainId = Integer.valueOf(sendInfoId);
        //根据sendInfoId查询短信信息
        MsmVO msmVO= shortMessageMapper.getShortMessageById(mainId);
        //根据sendInfoId查询短信详情
        List<MsmInfoEntity> msmInfoEntityList = shortMessageMapper.getInfoById(mainId);
        msmVO.setMsmInfoList(msmInfoEntityList);
        return msmVO;
    }
    /**
     * 与机器人接口
     * @return
     */
    @Override
    public JSONObject getJsonObject() {
        //调取值班管理系统短信接口
        JSONObject jsonObject = httpPostClient.doPost();
        return jsonObject;
    }
}
