package com.lnquan.community.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lnquan.community.dto.AccessTokenDTO;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;


import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Component
public class NetWorkUtil {

    private static CloseableHttpClient httpClient;
    /**
     * 信任SSL证书
     */
    static {
        try {
            SSLContext sslContext = SSLContextBuilder.create().useProtocol(SSLConnectionSocketFactory.SSL).loadTrustMaterial((x, y) -> true).build();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
            X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
                @Override
                public void verify(String s, SSLSocket sslSocket) throws IOException {

                }
                @Override
                public void verify(String s, X509Certificate x509Certificate) throws SSLException {

                }

                @Override
                public void verify(String s, String[] strings, String[] strings1) throws SSLException {

                }

                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };

            httpClient = HttpClientBuilder.create().setSslcontext(sslContext).setDefaultRequestConfig(config).setHostnameVerifier(hostnameVerifier).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String post(String url, List<NameValuePair> parameters) throws URISyntaxException {
        URI uri = new URIBuilder(url).setParameters(parameters).build();
        HttpPost post = new HttpPost(uri);
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == 200){
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                return content;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String get(String url, List<NameValuePair> parameters) throws URISyntaxException {
        URI uri = new URIBuilder(url).setParameters(parameters).build();
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200){
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                return content;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
