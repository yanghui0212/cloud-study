package com.study.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class TestFilter extends ZuulFilter {
    private static Map<String, String> urlMap = new HashMap<>();

    static {
        urlMap.put("user", "/");
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = request.getRequestURI(); // 列子 [/user/login/loginWx]
        String[] split = url.split("/", 3);    // 这里切割一下,好让下面判断是否是需要修改url的.
        if (split.length >= 2) {
            String val = urlMap.get(split[1]);
            if (StringUtils.isNotEmpty(val)) {
                url = url.replaceFirst("/" + split[1] + "/", val);// 根据配置好的去将url替换掉,这里可以写自己的转换url的规则
                url = "http://www.baidu.com";
                //ctx.put("requestURI", url); // 将替换掉的url set进去,在对应的转发请求的url就会使用这个url
            }
        }
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public String filterType() {
        return "post";// 在请求被处理之后，会进入该过滤器
    }

}
