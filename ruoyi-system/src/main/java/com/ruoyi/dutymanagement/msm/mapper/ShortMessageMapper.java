package com.ruoyi.dutymanagement.msm.mapper;

import com.ruoyi.dutymanagement.msm.domain.MsmEntity;
import com.ruoyi.dutymanagement.msm.domain.MsmInfoEntity;
import com.ruoyi.dutymanagement.msm.domain.param.MsmParam;
import com.ruoyi.dutymanagement.msm.domain.vo.MsmVO;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShortMessageMapper {
    /**
     * 查询短信列表
     * @param msmParam
     * @return
     */
    public List<MsmVO> list(MsmParam msmParam);

    /**
     * 根据id查询短信信息
     * @param sendInfoId
     * @return
     */
    public MsmVO getShortMessageById(int sendInfoId);
    /**
     * 查询短信详情
     * @param sendInfoId
     * @return
     */
    public List<MsmInfoEntity> getInfoById(int sendInfoId);

    /**
     * 新增短信主信息
     * @param msmEntity
     */
    public void add(MsmEntity msmEntity);
    /**
     * 新增短信子信息
     * @param msmInfoEntity
     */
    public void addItem(MsmInfoEntity msmInfoEntity);

    /**
     * 测试机器人接口
     * @param status
     * @return
     */
    public List<MsmVO> getRobotData(String status);
}
