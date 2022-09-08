package com.ruoyi.dutymanagement.msm.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.dutymanagement.msm.domain.param.MsmParam;
import com.ruoyi.dutymanagement.msm.domain.vo.MsmVO;
import com.ruoyi.dutymanagement.msm.service.IHttpPostClientService;
import com.ruoyi.dutymanagement.msm.service.IShortMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * 短信管理
 */
@RestController
@RequestMapping("/msm/message")
public class ShortMessageController extends BaseController {

    @Autowired
    private IShortMessageService shortMessageService;



    /**
     * 查询短信列表
     * @param msmParam
     * @return
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody MsmParam msmParam){
        startPage();
        List<MsmVO> msmVOList = shortMessageService.list(msmParam);
        return getDataTable(msmVOList);
    }

    /**
     * 查询短信详情
     * @param sendInfoId
     * @return
     */
    @GetMapping("/getInfoById")
    public AjaxResult getInfoById(@RequestParam String sendInfoId){
        if(sendInfoId==null||sendInfoId==""){
            return AjaxResult.error("sendInfoId不能为空！");
        }
        MsmVO msmVOList = shortMessageService.getInfoById(sendInfoId);
        return AjaxResult.success(msmVOList);
    }

    /**
     * 与机器人接口
     * @return
     */
    @GetMapping("/getJsonObject")
    public  AjaxResult getJsonObject(@RequestParam String success){
        JSONObject jsonObject = shortMessageService.getJsonObject(success);
        return AjaxResult.success(jsonObject);
    }
    /**
     * 测试
     * @param success
     * @return
     */
    @GetMapping("/testList")
    public TableDataInfo testList(@RequestParam String success){
        startPage();
        List<MsmVO> msmVOList = shortMessageService.testList(success);
        return getDataTable(msmVOList);
    }

    /**
     * 测试新增
     * @param param
     * @return
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody MsmParam param) throws ParseException {
        shortMessageService.add(param);
        return AjaxResult.success("新增成功");
    }
}
