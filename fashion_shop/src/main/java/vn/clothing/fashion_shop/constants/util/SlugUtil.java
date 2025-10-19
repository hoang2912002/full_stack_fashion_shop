package vn.clothing.fashion_shop.constants.util;

import java.text.Normalizer;
import java.util.Locale;

public class SlugUtil {

    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        // 1️⃣ Chuẩn hoá ký tự Unicode (xử lý dấu tiếng Việt)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // 2️⃣ Loại bỏ dấu
        String slug = normalized.replaceAll("\\p{M}", "");

        // 3️⃣ Chuyển về chữ thường
        slug = slug.toLowerCase(Locale.ROOT);

        // 4️⃣ Loại bỏ ký tự đặc biệt (chỉ giữ a-z, 0-9, khoảng trắng)
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");

        // 5️⃣ Đổi khoảng trắng thành dấu gạch ngang
        slug = slug.trim().replaceAll("\\s+", "-");

        // 6️⃣ Gộp các dấu gạch ngang liên tiếp
        slug = slug.replaceAll("-+", "-");

        return slug;
    }
}