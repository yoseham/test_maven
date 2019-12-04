package cn.edu.szu.bigdata.rsp_platform.common.utils;

import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author longhao
 * @date 2019/6/23 20:58
 */
public class UserUtil {
    /**
     * 获取当前登录的user
     */
    public static User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object object = authentication.getPrincipal();
            if (object != null) {
                return (User) object;
            }
        }
        return null;
    }

    /**
     * 获取当前登录的userId
     */
    public static String getLoginUserId() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }
}
