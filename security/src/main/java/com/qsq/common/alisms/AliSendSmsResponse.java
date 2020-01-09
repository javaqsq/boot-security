package com.qsq.common.alisms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import lombok.Data;

/**
 * @author QSQ
 * @create 2019/10/1 22:08
 * No, again
 * 〈阿里短信返回体〉
 */
@Data
public class AliSendSmsResponse extends SendSmsResponse {

    private boolean isSuccess;

}