package com.study.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

//@Component
public class AccessTokenFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(MyFilter.class);

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        logger.info("******zuul.filter.AccessTokenFilterz尚未实现");
        logger.info(String.format("%s AccessTokenFilter request to %s", request.getMethod(),
                request.getRequestURL().toString()));

        //ctx.setSendZuulResponse(true);
        //ctx.setResponseStatusCode(200);
        //ctx.setResponseBody("{\"name\":\"chhliu\"}");// 输出最终结果
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public String filterType() {
        return "pre";  //定义filter的类型，有pre、route、post、error四种
    }

}
