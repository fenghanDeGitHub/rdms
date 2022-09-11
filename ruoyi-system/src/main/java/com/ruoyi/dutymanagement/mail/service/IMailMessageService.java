package com.ruoyi.dutymanagement.mail.service;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.dutymanagement.mail.domain.param.MailParam;
import com.ruoyi.dutymanagement.mail.domain.vo.MailVO;

import java.util.List;

public interface IMailMessageService {
    /**
     * 查询邮件列表
     * @param mailParam
     * @return
     */
    public List<MailVO> list(MailParam mailParam);

    /**
     * 查询邮件详情
     * @param mainId
     * @return
     */
    public MailVO getMailInfoById(String mainId);

    /**
     * 机器人接口
     * @param status
     * @return
     */
    public String getRobotData(String status);
}
