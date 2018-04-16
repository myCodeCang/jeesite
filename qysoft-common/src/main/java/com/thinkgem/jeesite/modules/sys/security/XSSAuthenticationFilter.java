/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.security;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.modules.sys.entity.Principal;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 表单验证（包含验证码）过滤类
 *
 * @author ThinkGem
 * @version 2014-5-19
 */
@Service
public class XSSAuthenticationFilter extends
		org.apache.shiro.web.filter.authc.UserFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
									  ServletResponse response, Object mappedValue) {

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