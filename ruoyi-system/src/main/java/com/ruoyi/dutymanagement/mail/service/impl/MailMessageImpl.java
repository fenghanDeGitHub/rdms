package com.ruoyi.dutymanagement.mail.service.impl;

import com.ruoyi.dutymanagement.mail.domain.MailInfoEntity;
import com.ruoyi.dutymanagement.mail.domain.param.MailParam;
import com.ruoyi.dutymanagement.mail.domain.vo.MailVO;
import com.ruoyi.dutymanagement.mail.mapper.MailMessageMapper;
import com.ruoyi.dutymanagement.mail.service.IMailMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailMessageImpl implements IMailMessageService {

    @Autowired
    private MailMessageMapper mailMessageMapper;
    /**
     * 查询邮件列表
     * @param mailParam
     * @return
     */
    @Override
    public List<MailVO> list(MailParam mailParam) {
        List<MailVO> mailVOList = mailMessageMapper.list(mailParam);
        return mailVOList;
    }
    /**
     * 查询邮件详情
     * @param mainId
     * @return
     */
    @Override
    public MailVO getMailInfoById(String mainId) {
        //查询邮件信息
        MailVO mailVO = mailMessageMapper.getMailById(Integer.valueOf(mainId));
        //查询邮件详情信息
        List<MailInfoEntity> mailInfoList = mailMessageMapper.getMailInfoById(mainId);
        mailVO.setMailInfoList(mailInfoList);
        return mailVO;
    }

    @Override
    public String getRobotData(String status) {
        //查询未读邮件
        List<MailVO> mailVOList = mailMessageMapper.getMailByStatus(status);
        String mailMessage = null;
        for(MailVO mailVO : mailVOList){
            //id
            int id = mailVO.getId();
            //邮件接收时间
            String  receivedDate =mailVO.getReceivedDate();
            //邮件标题
            String mailName =mailVO.getMailName();
            mailMessage=receivedDate+"收到标题为："+mailName+"邮件，请注意查收！";
            if(receivedDate!=null || mailName!=null ){
                //将邮件状态改为1
                mailMessageMapper.updateMailStatus(id);
            }
        }
        return mailMessage;
    }
}
