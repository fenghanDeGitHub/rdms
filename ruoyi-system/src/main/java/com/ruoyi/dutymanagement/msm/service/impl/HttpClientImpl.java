package com.ruoyi.dutymanagement.msm.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.dutymanagement.mail.domain.MailEntity;
import com.ruoyi.dutymanagement.mail.mapper.MailMessageMapper;
import com.ruoyi.dutymanagement.msm.domain.MsmEntity;
import com.ruoyi.dutymanagement.msm.domain.param.LoginInfo;
import com.ruoyi.dutymanagement.msm.mapper.ShortMessageMapper;
import com.ruoyi.dutymanagement.msm.service.IHttpClientService;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * 调外系统实现
 *
 */
@Service
public class HttpClientImpl implements IHttpClientService {
    @Autowired
    private ShortMessageMapper shortMessageMapper;

    @Autowired
    private MailMessageMapper mailMessageMapper;
    @Override
    public JSONObject doMsm(String status) {
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpGet post = new HttpGet("http://192.168.1.3:18092/zwfxzb/fxb/sendinfo/getMore"+status);
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
//                        String status = String.valueOf(object.get("status"));
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

    @Override
    public JSONObject doMail(String status) {
// 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpGet post = new HttpGet("http://192.168.1.3:18092/zwfxzb/fxb/sendinfo/getMore"+status);
        //入库对象
        MailEntity mailEntity = new MailEntity();
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
                        String createTime = String.valueOf(object.get("createTime"));
                        int mailAttachmentId = Integer.parseInt(String.valueOf(object.get("mailAttachmentId")));
                        String mailName = String.valueOf(object.get("mailName"));
                        String address = String.valueOf(object.get("address"));
                        String receivedDate = String.valueOf(object.get("receivedDate"));
                        String messageId = String.valueOf(object.get("messageId"));
                        String appCode = String.valueOf(object.get("appCode"));
                        String mailType = String.valueOf(object.get("mailType"));
                        int fileSize = Integer.parseInt(String.valueOf(object.get("fileSize")));
                        //数据封装
                        mailEntity.setCreateTime(createTime);
                        mailEntity.setMailAttachmentId(mailAttachmentId);
                        mailEntity.setMailName(mailName);
                        mailEntity.setAddress(address);
                        mailEntity.setReceivedDate(receivedDate);
                        mailEntity.setMessageId(messageId);
                        mailEntity.setAppCode(appCode);
                        mailEntity.setMailType(mailType);
                        mailEntity.setFileSize(fileSize);
                        //短信主信息入库
                        mailMessageMapper.add(mailEntity);
                        Object fileInfoList= object.get("fileInfoList");
                        if(!fileInfoList.equals(null)){
                            String objectStr = fileInfoList.toString();
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

    @Override
    public String getToken(LoginInfo loginInfo) {
        String userName =loginInfo.getUserName();
        String passWord = loginInfo.getPassWord();
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpPost httpRequest = new HttpPost("http://192.168.1.3:18092/zwfxzb/login");
//        httpRequest.setHeader("Authorization","eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjU4ZTI3YWVhLWE4ZDItNDhhNS05ZWUyLWViN2M4MTQ1NjQ0OSJ9.cPDRRvj7lmts66-ymYTRs2cacDK8gGyDbBTddt1x_8iqxIb8qWnxpw2Z7OMGHtkk5Kto8NeAiKFksTMWCPVEgA");
        //入库对象
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userName",userName);
//        jsonObject.put("passWord",passWord);
//        jsonObject.put("uuid","9e482f0633fc4bc5bb871e9144f1f8c9");
//        post.setEntity(new StringEntity(jsonObject.toString(), ContentType.create("text/json", "UTF-8")));
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("passWord", passWord));

        // 4. 执行请求并处理响应
        try {
            /*发出HTTP request*/
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            /*取得HTTP response*/
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            /*若状态码为200 ok*/
            if(httpResponse.getStatusLine().getStatusCode() == 200)
            {
                /*取出响应字符串*/
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(strResult);
            }
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                entity = new BufferedHttpEntity(entity);
                System.out.println("响应内容：");
                System.out.println(EntityUtils.toString(entity));
                // 从响应模型中获取响应实体
                byte[] bytes = EntityUtils.toByteArray(entity);
                if (bytes != null) {
                    String resultStr = new String(bytes,"UTF-8");
                    JSONObject json = JSONObject.parseObject(resultStr);
                    JSONArray jsonArray= JSONArray.parseArray(json.get("rows").toString());
                    for(int i=0;i<jsonArray.size();i++){
                        JSONObject object= (JSONObject) jsonArray.get(i);
                        //类型转换

                    }
                    return json.toJSONString();
                }
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
