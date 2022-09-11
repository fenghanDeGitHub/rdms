package com.ruoyi.dutymanagement.mail.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.dutymanagement.mail.domain.param.MailParam;
import com.ruoyi.dutymanagement.mail.domain.vo.MailVO;
import com.ruoyi.dutymanagement.mail.service.IMailMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮件管理
 */
@RestController
@RequestMapping("/mail/message")
public class MailMessageController extends BaseController {

    @Autowired
    private IMailMessageService mailMessageService;

    /**
     * 查询邮件列表
     * @param mailParam
     * @return
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody MailParam mailParam){
        startPage();
        List<MailVO> mailVOList = mailMessageService.list(mailParam);
        return getDataTable(mailVOList);
    }
    /**
     * 查询邮件详情
     * @param mainId
     * @return
     */
    @GetMapping("/getMailInfoById")
    public AjaxResult getMailInfoById(@RequestParam String mainId){
        MailVO mailInfo = mailMessageService.getMailInfoById(mainId);
        return AjaxResult.success(mailInfo);
    }

    /**
     * 与机器人接口
     * @param status
     * @return
     */
    @GetMapping("/getRobotData")
    public AjaxResult getMailStatus(@RequestParam String status){
        if(status == null || "".equals(status)){
            return AjaxResult.error("status参数不能为空！");
        }
        String mailMessage = mailMessageService.getRobotData(status);
        if(mailMessage==null || "".equals(mailMessage)){
            AjaxResult.success("没有新邮件！");
        }
        return AjaxResult.success(mailMessage);
    }
}
