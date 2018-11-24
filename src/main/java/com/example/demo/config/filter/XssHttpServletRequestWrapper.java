package com.example.demo.config.filter;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.esapi.ESAPI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = this.cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> encodedMap = new HashMap<>(super.getParameterMap());
        for (Map.Entry<String, String[]> entry : encodedMap.entrySet()) {
            String[] value = entry.getValue();
            String[] encodedValues = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                encodedValues[i] = this.cleanXSS(value[i]);
            }
            encodedMap.put(entry.getKey(), encodedValues);
        }
        return encodedMap;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return this.cleanXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) return null;
        return this.cleanXSS(value);
    }

    private String cleanXSS(String value) {
        if (value != null) {
            //避免编码xss攻击
            value = ESAPI.encoder().canonicalize(value);
            value = StringEscapeUtils.escapeHtml(value);
            //避免输入null字符
            value = value.replaceAll("\0","");

            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return this.filter(value);
    }

    /**
     * 过滤特殊字符
     **/
    private String filter(String value) {
        if (value == null) return null;
        StringBuilder result = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); ++i) {
            switch (value.charAt(i)) {
                case '<': result.append("<"); break;
                case '>': result.append(">"); break;
                case '"': result.append("\""); break;
                case '\'': result.append("'"); break;
                case '%': result.append("%"); break;
                case ';': result.append(";"); break;
                case '(': result.append("("); break;
                case ')': result.append(")"); break;
                case '&': result.append("&"); break;
                case '+': result.append("+"); break;
                default: result.append(value.charAt(i));
            }
        }
        return result.toString();
    }

}
