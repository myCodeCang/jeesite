package com.thinkgem.jeesite.modules.sys.security;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by kevin on 2017/9/21.
 */
@Service
public class MultipleLoginCheckFilter extends
        org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
    @Resource
    private UserUserinfoService userUserinfoService;
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) {
        //判断XSS
        Enumeration enu=request.getParameterNames();
        while(enu.hasMoreElements()){
            String paraName=(String)enu.nextElement();
            String value = request.getParameter(paraName);
//			System.out.println(paraName+": "+value);
//			String val = stripXSS(value);
//			request.setAttribute(paraName,val);

            if("-1".equals(stripXSS(value))){
                return false;
            }
            if(!verifyXSS(value)){
                return false;
            }
        }
         String singleUserLogin= Global.getOption("system_user_set", "singleUserLogin");
        if("on".equals(singleUserLogin)) {
            String isLogin = request.getParameter("isLogin");
            if (!"1".equals(isLogin)) {
                Session session = UserInfoUtils.getSession();
                Object singleUserInfo = session.getAttribute("singleUserInfo");
                if (singleUserInfo==null){
                    return true;
                }
                if (!(singleUserInfo instanceof  UserUserinfo)) {
                    return false;
                }
                UserUserinfo userInfo = (UserUserinfo) singleUserInfo;
                UserUserinfo userUserinfo = userUserinfoService.getByName(userInfo.getUserName());

                if (userUserinfo == null) {
                    return true;
                }
                if (!userUserinfo.getLastLoginIp().equals(session.getId())) {
                    SystemService systemService = SpringContextHolder.getBean(SystemService.class);
                    systemService.getSessionDao().delete(session);
                }

            }
            }
        return true;
    }
    /**
     * 验证XSS攻击
     * @param value
     * @return
     */
    private boolean verifyXSS(String value) {
        if(value.indexOf("\"><script")!=-1){
            return false;
        }
        if(value.indexOf("script")!=-1){
            return false;
        }
        if(value.indexOf("src")!=-1){
            return false;
        }
        if(value.indexOf("eval")!=-1){
            return false;
        }
        if(value.indexOf("e-xpression")!=-1){
            return false;
        }
        if(value.indexOf("javascript")!=-1){
            return false;
        }
        if(value.indexOf("vbscript")!=-1){
            return false;
        }
        if(value.indexOf("expression")!=-1){
            return false;
        }
        if(value.indexOf("onload")!=-1){
            return false;
        }
        return true;
    }
    /**
     * 过滤xss攻击
     * @param value
     * @return
     */
    private String stripXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);
            // Avoid null characters
            value = value.replaceAll("", "");
            value = value.replaceAll("-1", "");
            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("-1");
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression
            scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-1");
            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("-1");
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-1");
            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-1");
            // Avoid e-xpression(...) expressions
            scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-1");
            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("-1");
            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("-1");
            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-1");
        }
        return value;
    }
}