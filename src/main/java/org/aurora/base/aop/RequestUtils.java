package org.aurora.base.aop;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.log4j.Log4j2;
import org.aurora.base.jackson.JSONUtils;
import org.aurora.base.util.CommonConstant;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public class RequestUtils {

    public static String getParams(HttpServletRequest request) {
        Map<String, String[]> map = new HashMap<>(request.getParameterMap());
        Set.of(CommonConstant.NO_LOG_REQUEST_PARAMS).forEach(param -> {
            if (map.containsKey(param)) {
                map.put(param, null);
            }
        });

        StringBuilder result = new StringBuilder(String.valueOf(JSONUtils.writeValueAsString(map)));

        String body = readRequestBody(request);
        if (body.startsWith("{") || body.startsWith("[{")) {
            result.append("\n").append(JSONUtils.checkValue(body));
        }

        String contentType = request.getContentType();
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            result.append("\n").append(getPartsInfo(request));
        }

        return result.toString();
    }

    public static String readRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
        return wrapper.getContentAsString();
    }

    public static String getPartsInfo(HttpServletRequest request) {
        try {
            Collection<Part> parts = request.getParts();
            StringBuilder result = new StringBuilder("[");
            parts.forEach(part -> {
                result.append("{name:").append(part.getSubmittedFileName()).append(",size:").append(part.getSize()).append("},");
            });
            return result.deleteCharAt(result.length() - 1).append("]").toString();
        } catch (IOException | ServletException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
