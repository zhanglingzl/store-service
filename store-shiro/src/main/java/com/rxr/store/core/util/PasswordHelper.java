package com.rxr.store.core.util;

import com.rxr.store.common.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordHelper {
    private static SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
    private static String algorithmName = "sha-256";
    private static Integer hashIterations = 2;

    public static void getSecurePassword(User user) {

        if(StringUtils.isBlank(user.getSalt())) {
            user.setSalt(secureRandomNumberGenerator.nextBytes().toHex());
        }
        String password = new SimpleHash(
                algorithmName,
                user.getPassword(),
                user.getCredentialsSalt(),
                hashIterations
        ).toHex();
        // 设置加密后的密码
        user.setPassword(password);

    }

    public static void main(String[] args) {
        User user = new User();
        user.setLoginName("admin");
        user.setPassword("admin123");
        PasswordHelper.getSecurePassword(user);
        System.out.println(user.getSalt());
        System.out.println(user.getPassword());
    }
}
