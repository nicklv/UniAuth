package com.dianrong.common.uniauth.cas.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UrlPathHelper;

import com.google.common.collect.Lists;

public class XssUrlPathHelper extends UrlPathHelper {
    @Override
    public Map<String, String> decodePathVariables(HttpServletRequest request, Map<String, String> vars) {
        Map<String, String> result = super.decodePathVariables(request, vars);
        if (result != null && !result.isEmpty()) {
            Set<Entry<String, String>> entrySet = result.entrySet();
            for (Entry<String, String> entry: entrySet) {
                String key = entry.getKey();
                result.put(key, cleanXSS(entry.getValue()));
            }
        }
        return result;
    }

    @Override
    public MultiValueMap<String, String> decodeMatrixVariables(HttpServletRequest request, MultiValueMap<String, String> vars) {
        MultiValueMap<String, String> mvm = super.decodeMatrixVariables(request, vars);
        if (mvm != null && !mvm.isEmpty()) {
            Set<Entry<String, List<String>>> entrySet = mvm.entrySet();
            for (Entry<String, List<String>> entry: entrySet) {
                List<String> values = entry.getValue();
                List<String> newValues = Lists.newArrayList();
                for (String val: values) {
                    newValues.add(cleanXSS(val));
                }
                mvm.put(entry.getKey(), newValues);
            }
        }
        return mvm;
    }

    /**
     * 处理xss攻击的问题
     * @param value
     * @return
     */
    protected String cleanXSS(String value) {
        return StringEscapeUtils.escapeXml11(value);
    }
}
