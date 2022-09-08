package com.ruoyi.dutymanagement.msm.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.dutymanagement.msm.domain.MsmEntity;
import com.ruoyi.dutymanagement.msm.domain.MsmInfoEntity;
import com.ruoyi.dutymanagement.msm.mapper.ShortMessageMapper;
import com.ruoyi.dutymanagement.msm.service.IHttpPostClientService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 调外系统实现
 *
 */
@Service
public class HttpPostClientImpl implements IHttpPostClientService {
    @Autowired
    private ShortMessageMapper shortMessageMapper;

    @Override
    public JSONObject doPost(String success) {
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpGet post = new HttpGet("http://192.168.1.9/dev-api/msm/message/testList?success="+success);
        //入库对象
        MsmEntity msmEntity = new MsmEntity();
        // 4. 执行请求并处理响应
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                entity = new BufferedHttpEntity(entity);
                System.out.println("响应内容：");
                System.out.println(EntityUtils.toString(entity));
                // 从响应模型中获取响应实体
                byte[] bytes = EntityUtils.toByteArray(entity);
                if (bytes != null) {
                    String resultStr = new String(bytes,"UTF-8");
                    JSONObject jsonObject = JSONObject.parseObject(resultStr);
                    JSONArray jsonArray= JSONArray.parseArray(jsonObject.get("rows").toString());
                    for(int i=0;i<jsonArray.size();i++){
                        JSONObject object= (JSONObject) jsonArray.get(i);
                        //类型转换
                        String ageing = String.valueOf(object.get("ageing"));
                        Date sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(object.get("sendTime")));
                        String businessType = String.valueOf(object.get("businessType"));
                        String status = String.valueOf(object.get("status"));
                        String content = String.valueOf(object.get("content"));
                        int sendCount = Integer.parseInt(String.valueOf(object.get("sendCount")));
                        int failCount = Integer.parseInt(String.valueOf(object.get("failCount")));
                        String signature = String.valueOf(object.get("signature"));
                        String successParam = String.valueOf(object.get("success"));
                        //数据封装
                        msmEntity.setAgeing(ageing);
                        msmEntity.setSendTime(sendTime);
                        msmEntity.setBusinessType(businessType);
                        msmEntity.setStatus(status);
                        msmEntity.setContent(content);
                        msmEntity.setSendCount(sendCount);
                        msmEntity.setFailCount(failCount);
                        msmEntity.setSignaTure(signature);
                        msmEntity.setSuccess(successParam);
                        //短信主信息入库
                        shortMessageMapper.add(msmEntity);
                        Object receiverList= object.get("receiverList");
                        if(!receiverList.equals(null)){
                            String objectStr = receiverList.toString();
                            System.out.println("++++++++++++++++++++"+objectStr+"+++++++++++++++");
                        }
//                        if(receiverList.size()!=0){
//                            for(MsmInfoEntity msmInfoEntity : receiverList){
//                                //短信子信息入库
//                                shortMessageMapper.addItem(msmInfoEntity);
//                            }
//                        }
                    }
                    return jsonObject;
                }
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            // 5. 释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
