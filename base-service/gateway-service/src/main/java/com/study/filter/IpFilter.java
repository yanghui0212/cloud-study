package com.study.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class IpFilter extends ZuulFilter {

    @Value("${firewall.enabled}")
    private boolean firewallEnabled = true;

    Logger logger= LoggerFactory.getLogger(getClass());

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        if (!firewallEnabled)
            return null;

        RequestContext ctx= RequestContext.getCurrentContext();
        HttpServletRequest req=ctx.getRequest();
        String ipAddr=this.getIpAddr(req);
        logger.info("请求IP地址为：[{}]",ipAddr);

        //配置本地IP黑名单，生产环境可放入数据库或者redis中
        List<String> denyList = getDenyList();
        if (denyList.contains("*")){
            //配置本地IP白名单，生产环境可放入数据库或者redis中
            List<String> allowList = getAllowList();
            if(allowList.contains(ipAddr)){
                return null;
            }
        }
        if(denyList.contains(ipAddr)){
            logger.info("黑名单:"+ipAddr+" IP地址校验不通过！！！");
            ctx.setResponseStatusCode(401);
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("IpAddr is forbidden!");
        }

        /*//配置本地IP白名单，生产环境可放入数据库或者redis中
        List<String> allowList = getAllowList();
        if(!allowList.contains(ipAddr)){
            logger.info("IP地址校验不通过！！！");
            ctx.setResponseStatusCode(401);
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("IpAddr is forbidden!");
        }*/
        return null;
    }

    /**
     * 获取白名单列表
     * @return
     */
    public List<String> getAllowList(){
        List<String> list = new ArrayList<String>();
        list.add("127.0.0.1");
        return list;
    }

    /**
     * 获取黑名单列表
     * @return
     */
    public List<String> getDenyList(){
        List<String> list = new ArrayList<String>();
        list.add("127.0.0.1");
        return list;
    }

    /**
     * 获取Ip地址
     * @param request
     * @return
     */
    public  String getIpAddr(HttpServletRequest request){

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
