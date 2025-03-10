package service.event.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TextUtils {
    public static String normalizeKeyword(String keyword) {
        if (keyword == null) return "";

        // Loại bỏ dấu tiếng Việt
        String normalized = Normalizer.normalize(keyword, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", ""); // Xóa dấu

        // Chuẩn hóa khoảng trắng: bỏ khoảng trắng đầu/cuối và thay nhiều dấu cách thành 1
        normalized = normalized.trim().replaceAll("\\s+", " ");

        return normalized.toLowerCase(); // Chuyển thành chữ thường
    }

//    public static String removeAccents(String text) {
//        if (text == null) return null;
//
//        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
//        normalized = normalized.replaceAll("\\p{M}", "");
//
//        // Chuyển 'đ' → 'd' và 'Đ' → 'D'
//        return normalized.replace("đ", "d").replace("Đ", "D").toLowerCase().trim();
//    }
    public static String removeAccents(String text) {
        if (text == null) return null;

        // Chuẩn hóa chuỗi, loại bỏ dấu
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");

        // Chuyển 'đ' → 'd' và 'Đ' → 'D'
        normalized = normalized.replace("đ", "d").replace("Đ", "D");

        // Loại bỏ khoảng trắng dư thừa và chuyển thành chữ thường
        return normalized.replaceAll("\\s+", " ").trim().toLowerCase();
    }


}
