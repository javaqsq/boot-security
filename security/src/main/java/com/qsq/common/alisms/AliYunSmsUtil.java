package com.qsq.common.alisms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.qsq.enums.ExceptionEnum;
import com.qsq.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author QSQ
 * @create 2019/4/24 17:58
 * No, again
 * 〈 阿里云短信服务  〉
 */
@Slf4j
public class AliYunSmsUtil {


    //产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";

    public static void defaultConnectTimeout() {
        //可自助调整超时时间  ---> http 连接超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

    }

    /**
     * 发送验证码
     *
     * @param phoneNumbers 可以多个手机号码 一逗号隔开
     * @param params       模板参数
     * @return
     */
    public static AliSendSmsResponse sendSmsCode(String phoneNumbers, AliTemplateParams params) {
        if (StringUtils.isEmpty(phoneNumbers)) {
            throw ExceptionEnum.REQUEST_ID_MISS.getException();
        }
        log.info("SMS## sendSmsCode params : phoneNumbers : {}  ; params  :{} ", phoneNumbers, JsonUtil.pojoToJson(params));
        defaultConnectTimeout();
        SendSmsRequest request = new SendSmsRequest();
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", AliYunSmsConstant.ACCESS_KEY_ID, AliYunSmsConstant.ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint("cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumbers);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(AliYunSmsConstant.TEST_SIGN_NAME);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(AliYunSmsConstant.SMS_SIMPLE_TEMPLATE);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(JsonUtil.pojoToJson(params));
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("注册服务");
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = null;
        AliSendSmsResponse aliSendSmsResponse = new AliSendSmsResponse();
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            converter(sendSmsResponse, aliSendSmsResponse);
        } catch (ClientException e) {
            log.error("SMS## sendSms error " + e);
            throw ExceptionEnum.OUT_SYSTEM_SMS_ERROR.getException();
        }
        log.info("SMS## sendSmsResponse params : " + JsonUtil.pojoToJson(sendSmsResponse));
        return aliSendSmsResponse;
    }

    private static void converter(SendSmsResponse sendSmsResponse, AliSendSmsResponse aliSendSmsResponse) {
        aliSendSmsResponse.setBizId(sendSmsResponse.getBizId());
        aliSendSmsResponse.setCode(sendSmsResponse.getCode());
        aliSendSmsResponse.setMessage(sendSmsResponse.getMessage());
        aliSendSmsResponse.setRequestId(sendSmsResponse.getRequestId());
        aliSendSmsResponse.setSuccess("OK".equalsIgnoreCase(sendSmsResponse.getCode()));
    }


    /**
     * 查询短信功能
     *
     * @param bizId       服务商返回的id
     * @param phoneNumber 手机号码
     * @param date        发送日期
     * @param pageSize    页码
     * @param currentPage 当前页
     * @return
     * @throws ClientException
     */
    public static QuerySendDetailsResponse querySendDetails(String bizId, String phoneNumber, Date date, long pageSize, long currentPage) {
        if (StringUtils.isEmpty(phoneNumber)) {
            throw ExceptionEnum.REQUEST_ID_MISS.getException();
        }
        log.info("SMS## querySendDetails request params : bizId : {} , phoneNumber {} ,  date {}  , pageSize {}  currentPage {} ", bizId, phoneNumber, date.toString(), pageSize, currentPage);
        defaultConnectTimeout();
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", AliYunSmsConstant.ACCESS_KEY_ID, AliYunSmsConstant.ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint("cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phoneNumber);
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(date));
        //必填-页大小
        request.setPageSize(pageSize);
        //必填-当前页码从1开始计数
        request.setCurrentPage(currentPage);
        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = null;
        try {
            querySendDetailsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("SMS## querySendDetails error " + e);
            throw ExceptionEnum.OUT_SYSTEM_SMS_ERROR.getException();
        }
        log.info("SMS## querySendDetails response : " + JsonUtil.pojoToJson(querySendDetailsResponse));
        return querySendDetailsResponse;
    }


}