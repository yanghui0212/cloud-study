package com.study.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;


//@Component
public class UrlRedirectFilter extends ZuulFilter {


    /**
     *  重定向的规则,根据key来重定向到val.
     */
    private static Map<String, String>urlMap=new HashMap<>();
    static {
        urlMap.put("cargo", "http://www.baidu.com");
    }
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = request.getRequestURI(); // 列子 [/user/login/loginWx]
        String[] split = url.split("/", 3);	// 这里切割一下,好让下面判断是否是需要修改url的.
        if (split.length>=2) {
            String val = urlMap.get(split[1]);
            if (StringUtils.isNotEmpty(val)) {
                url=url.replaceFirst("/"+split[1]+"/", val);// 根据配置好的去将url替换掉,这里可以写自己的转换url的规则
                ctx.put("requestURI", url); // 将替换掉的url set进去,在对应的转发请求的url就会使用这个url
            }
        }
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    //filterOrder：过滤的顺序
    @Override
    public int filterOrder() {
        return 1;
    }

    /* (non-Javadoc)filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
                    pre：路由之前
                    routing：路由之时
                    post： 路由之后
                    error：发送错误调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

}