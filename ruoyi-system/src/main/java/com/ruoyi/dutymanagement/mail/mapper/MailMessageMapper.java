package com.ruoyi.dutymanagement.mail.mapper;

import com.ruoyi.dutymanagement.mail.domain.MailEntity;
import com.ruoyi.dutymanagement.mail.domain.MailInfoEntity;
import com.ruoyi.dutymanagement.mail.domain.param.MailParam;
import com.ruoyi.dutymanagement.mail.domain.vo.MailVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MailMessageMapper {
    /**
     * 查询邮件列表
     * @param mailParam
     * @return
     */
    public List<MailVO> list(MailParam mailParam);

    /**
     * 根据邮件附件id查询邮件
     * @param mainId
     * @return
     */
    public  MailVO getMailById(int mainId);

    /**
     * 查询邮件详情
     * @param mainId
     * @return
     */
    public List<MailInfoEntity> getMailInfoById(String mainId);
    /**
     * 查询播报邮件
     * @param status
     * @return
     */
    public List<MailVO> getMailByStatus(String status);

    /**
     * 新增邮件信息
     * @param mailEntity
     */
    public void add(MailEntity mailEntity);

    /**
     * 修改邮件状态
     * @param id
     */
    void updateMailStatus(int id);
}
