package com.board.commons.interceptors;

import com.board.commons.configs.ConfigInfoService;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 사이트 설정 유지
 *
 *
 * @Component("siteConf") --> 타임리프 안에서 사용될 이름
 * popup 래이아웃에서 전부 siteConfig -->  siteConf  변경함
 *
 * Board-Test에 대한 인테셉터 설정
 */
@Component("siteConf")
@RequiredArgsConstructor
public class SiteConfigInterceptor implements HandlerInterceptor {

    private final ConfigInfoService infoService;
    private final HttpServletRequest request;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String URI = request.getRequestURI();
        List<String> excludes = Arrays.asList(".css", ".js", ".png", ".jpg", ".jpeg", ".pdf", ".gif", ".xls", ".xlsx");
        boolean matched = excludes.stream().anyMatch(URI::contains);

        if (matched) {
            return true;
        }

        /** 사이트 설정 조회 */
        Map<String, String> siteConfigs = infoService.get("config", new TypeReference<Map<String, String>>() {});
        request.setAttribute("siteConfig", siteConfigs);
        return true;
    }

    public String get(String name) {
        Map<String, String> siteConfig = (Map<String, String>)request.getAttribute("siteConfig");
        String value = siteConfig == null ? "" : siteConfig.get(name);

        return value;
    }
}
