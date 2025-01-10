/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class DateUtils {

    // Hàm chuyển đổi String thành Date
    public static Date convertStringToDate(String dateStr) {
        String format = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            // Xử lý lỗi nếu chuỗi không đúng định dạng
            e.printStackTrace();
            // Trả về null hoặc có thể ném ra một exception theo nhu cầu
            return null;
        }
    }
}
