package com.board.commons.interceptors;

import com.board.commons.BasicConfig;
import com.board.commons.MemberUtil;
import com.board.commons.configs.BasicConfigInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

/**
 * 추가적인 기능을 위해 추가된 인터셉터 (BasicConfigInfoService --> 에서 사용됨)
 */
@Component
@RequiredArgsConstructor
public class BasicConfigInterceptor implements HandlerInterceptor {


    private final BasicConfigInfoService basicConfigInfoService;
    private final MemberUtil MemberUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        checkDevice(request);
        clearLoginData(request);
        loadSiteConfig(request);

        return true;
    }

    /**
     * PC, 모바일 수동 변경 처리
     *
     *  // device - PC : PC 뷰, Mobile : Mobile 뷰
     * @param request
     */
    private void checkDevice(HttpServletRequest request) {
        String device = request.getParameter("device");
        if (!StringUtils.hasText(device)) {
            return;
        }

        device = device.toUpperCase().equals("MOBILE") ? "MOBILE" : "PC";

        HttpSession session = request.getSession();
        session.setAttribute("device", device);
    }

    private void clearLoginData(HttpServletRequest request) {
        String URL = request.getRequestURI();
        if (URL.indexOf("/member/login") == -1) {
            HttpSession session = request.getSession();
            MemberUtil.clearLoginData(session);
        }
    }

    private void loadSiteConfig(HttpServletRequest request) {
        String[] excludes = {".js", ".css", ".png", ".jpg", ".jpeg", "gif", ".pdf", ".xls", ".xlxs", ".ppt"};

        String URL = request.getRequestURI().toLowerCase();

        boolean isIncluded = Arrays.stream(excludes).anyMatch(s -> URL.contains(s));
        if (isIncluded) {
            return;
        }

        BasicConfig config = basicConfigInfoService.get("basic", BasicConfig.class)
                .orElseGet(BasicConfig::new);

        request.setAttribute("siteConfig", config);
    }
}
