package com.shopvn.form;
import java.util.*;

public class MockDatabase {
    private static final Set<String> REGISTERED_EMAILS = new HashSet<>(Arrays.asList(
        "existing@shopvn.com", "test@shopvn.com", "admin@shopvn.vn"
    ));
    private static final Set<String> REGISTERED_USERNAMES = new HashSet<>(Arrays.asList(
        "admin", "shopvn", "testuser1", "user_exist"
    ));
    private static final Set<String> VALID_REFERRAL_CODES = new HashSet<>(Arrays.asList(
        "ABC12345", "XYZ98765", "REF00001", "SHOP2024"
    ));

    private MockDatabase() {}

    public static boolean isEmailRegistered(String email) {
        return email != null && REGISTERED_EMAILS.contains(email.toLowerCase());
    }
    public static boolean isUsernameRegistered(String username) {
        return username != null && REGISTERED_USERNAMES.contains(username.toLowerCase());
    }
    public static boolean isValidReferralCode(String code) {
        return code != null && VALID_REFERRAL_CODES.contains(code);
    }
}