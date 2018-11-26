package com.chungo.basemore.advance;

import com.chungo.basemore.mvp.model.api.Api;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/11/26 17:01
 */
public class HeaderIntercepter implements Interceptor {
    private boolean isRun = true;

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isRun()) // 可以在 App 运行时, 随时通过 setRun(false) 来结束本框架的运行
            return chain.proceed(chain.request());
        return chain.proceed(setGlobalHeader(chain.request()));
    }

    private Request setGlobalHeader(Request request) {
        Request.Builder builder = request.newBuilder();
        List<String> headerValues = request.headers(Api.HEADER_DOMAIN_KEY_BASE);
        if (headerValues != null && headerValues.size() > 0) {
            builder.removeHeader(Api.HEADER_DOMAIN_KEY_BASE);
            HttpUrl baseUrl = request.url();
            String headerValue = headerValues.get(0);
            HttpUrl newBaseUrl = HttpUrl.parse(headerValue);
            String tid = "";
            String ua = "";
            String params = " ";
            builder.addHeader("tid", tid)
                    .addHeader("User-Agent", ua)
                    .addHeader("params", params);
            //重建新的HttpUrl
            HttpUrl newUrl = baseUrl
                    .newBuilder()
                    .scheme(newBaseUrl.scheme())//http
                    .host(newBaseUrl.host())//ad.szprize.cn
                    .port(newBaseUrl.port())//端口
                    .build();
            return builder.url(newUrl).build();
        } else {
            String host = request.url().host();
            if (host.equals(Api.APP_DOMAIN)) {
                return builder.build();
            } else
                return request;
        }
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }
}
