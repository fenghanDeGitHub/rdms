package com.ruoyi.dutymanagement.msm.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.dutymanagement.msm.domain.MsmEntity;
import com.ruoyi.dutymanagement.msm.domain.MsmInfoEntity;
import com.ruoyi.dutymanagement.msm.domain.param.LoginInfo;
import com.ruoyi.dutymanagement.msm.domain.param.MsmParam;
import com.ruoyi.dutymanagement.msm.domain.vo.MsmVO;
import com.ruoyi.dutymanagement.msm.mapper.ShortMessageMapper;
import com.ruoyi.dutymanagement.msm.service.IHttpClientService;
import com.ruoyi.dutymanagement.msm.service.IShortMessageService;
import com.ruoyi.dutymanagement.msm.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ShortMessageImpl implements IShortMessageService {

    @Autowired
    private ShortMessageMapper shortMessageMapper;

    @Autowired
    private IHttpClientService httpPostClientService;

    /**
     * 查询短信列表
     * @param msmParam
     * @return
     */
    @Override
    public List<MsmVO> list(MsmParam msmParam) {
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
    public JSONObject getJsonObject(String status) {
        //调取值班管理系统短信接口
        JSONObject jsonObject = httpPostClientService.doMsm(status);
        return jsonObject;
    }
    /**
     * 测试机器人接口
     * @param status
     * @return
     */
    @Override
    public String getRobotData(String status) {
        List<MsmVO> msmVOList = shortMessageMapper.getRobotData(status);
        String messageContent = null;
        for (MsmVO msmVO:msmVOList) {
            //发送时间
            String sendTime = DateUtils.dateRurnString(msmVO.getSendTime());
            //署名
            String signaTure = msmVO.getSignaTure();
            if(null != sendTime  && null != signaTure){

                messageContent =sendTime +signaTure+"来了一条新短消息,请注意查收！";
            }
        }
        return messageContent;
    }

    @Override
    public void add(MsmParam msmParam) throws ParseException {
        MsmEntity msmEntity = new MsmEntity();
//        msmEntity.setSendInfoId(msmParam.getSendInfoId());
        msmEntity.setAgeing(msmParam.getAgeing());
        Date sendTime =DateUtils.stringTurnDate(msmParam.getSendTime());
        msmEntity.setSendTime(sendTime);
        msmEntity.setBusinessType(msmParam.getBusinessType());
        msmEntity.setStatus(msmParam.getStatus());
        msmEntity.setContent(msmParam.getContent());
        msmEntity.setSendCount(msmParam.getSendCount());
        msmEntity.setFailCount(msmParam.getFailCount());
        msmEntity.setSignaTure(msmParam.getSignaTure());
        msmEntity.setSuccess(msmParam.getSuccess());
        shortMessageMapper.add(msmEntity);
    }
    /**
     * 获取token
     * @param loginInfo
     * @return
     */
    @Override
    public String getToken(LoginInfo loginInfo) {
       String token = httpPostClientService.getToken(loginInfo);
        return token;
    }
}
