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
    // Try both formats: one with seconds and one without
    String[] formats = {"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm"};
    for (String format : formats) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            // Ignore exception and try next format
        }
    }
    // If no format matched, return null or throw an exception as needed
    return null;
}

}
