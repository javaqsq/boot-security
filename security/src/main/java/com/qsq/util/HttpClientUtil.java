package com.qsq.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

/**
 * @author QSQ
 * @create 2019/4/13 12:06
 * No, again
 * 〈  http client工具类封装 〉
 */
@Slf4j
public class HttpClientUtil {

    private static final String CHARSET = "UTF-8";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    public static <RES> RES get(String url, Class<RES> clazz) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        String result = HttpClients.createDefault().execute(httpGet, new BasicResponseHandler());

        System.out.println("[HttpClientUtil] get result:" + result);

        return JsonUtil.jsonToPojo(result, clazz);
    }

    public static String get(String url, Map head) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        Iterator entries = head.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            httpGet.setHeader((String) entry.getKey(), (String) entry.getValue());
        }
        String result = HttpClients.createDefault().execute(httpGet, new BasicResponseHandler());
        log.info("[HttpClientUtil] get result:{}",result);
        return result;
    }


    public static <REQ, RES> RES post(String url, REQ req, Class<RES> clazz) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", CONTENT_TYPE);
        httpPost.setEntity(new StringEntity(JsonUtil.pojoToJson(req), CHARSET));
        String result = HttpClients.createDefault().execute(httpPost, new BasicResponseHandler());
        log.info("[HttpClientUtil] get result:{}",result);
        return JsonUtil.jsonToPojo(result, clazz);
    }

    public static String post(String url, Map head, String body) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        Iterator entries = head.entrySet().iterator();

        while (entries.hasNext()) {

            Map.Entry entry = (Map.Entry) entries.next();
            httpPost.setHeader((String) entry.getKey(), (String) entry.getValue());
        }
        /* UrlEncodedFormEntity entity = new UrlEncodedFormEntity(body, "UTF-8");*/
        StringEntity entity = new StringEntity(body);
        httpPost.setEntity(entity);


        BufferedReader reader = new BufferedReader(new InputStreamReader(httpPost.getEntity().getContent()));

        StringBuilder sb = new StringBuilder();

        String line = null;

        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }
        System.out.println(sb.toString());

        return httpClient.execute(httpPost, new BasicResponseHandler());
    }

}