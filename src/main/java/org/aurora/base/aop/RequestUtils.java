package org.aurora.base.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aurora.base.jackson.JSONUtils;
import org.aurora.base.util.constant.CommonConstant;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestUtils {

    public static String getParams(HttpServletRequest request) {
        Map<String, String[]> map = new HashMap<>(request.getParameterMap());
        Set.of(CommonConstant.NO_LOG_REQUEST_PARAMS).forEach(param -> {
            if (map.containsKey(param)) {
                map.put(param, null);
            }
        });
        String params = JSONUtils.writeValueAsString(map);
        String body = readRequestBody(request);
        if (body.startsWith("{") || body.startsWith("[{")) {

            String resultBody = JSONUtils.checkValue(body);

            return params + "\n" + resultBody;
        }
        return params;
    }

    public static String readRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
        return wrapper.getContentAsString();
    }
}
