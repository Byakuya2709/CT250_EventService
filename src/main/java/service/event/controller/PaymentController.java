package service.event.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import service.event.exceptions.EntityNotFoundExceptions;
import service.event.model.VNPayTransaction;
import service.event.paymentUtils.Config;
import service.event.repository.VNPayTransactionRepository;
import service.event.request.VNPayRequestDTO;
import service.event.services.TicketService;
import service.event.services.VNPayTransactionService;
import service.event.utils.ResponseHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    
    @Autowired
    VNPayTransactionService vnPayTransactionService;
    @Autowired
    TicketService ticketService;
    
    @PostMapping("/process")
    public String processPayment(@RequestParam("amount") long amount,
            @RequestParam(value = "bankCode", required = false) String bankCode,
            @RequestParam(value = "language", required = false) String locate,
            HttpServletRequest req) throws UnsupportedEncodingException {
        
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long vnp_Amount = amount * 100;
        
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        
        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        
        vnp_Params.put("vnp_Locale", (locate != null && !locate.isEmpty()) ? locate : "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        
        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = Config.vnp_PayUrl + "?" + query;
        
        JsonObject job = new JsonObject();
        job.addProperty("code", "00");
        job.addProperty("message", "success");
        job.addProperty("data", paymentUrl);
        
        return new Gson().toJson(job);
    }
    
    @PostMapping("")
    public String createPayment(HttpServletRequest request, @RequestBody VNPayRequestDTO requestDTO,
            @RequestParam("amount") long amount) {
        try {
            //  Tạo tham số VNPay

            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_TxnRef = Config.getRandomNumber(8);
            String vnp_IpAddr = Config.getIpAddress(request);
            String vnp_TmnCode = Config.vnp_TmnCode;
            
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", "billpayment");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            Date date = cld.getTime();
            String vnp_CreateDate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(date);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            
            cld.add(Calendar.MINUTE, 15);
            Date expireDate = cld.getTime();
            String vnp_ExpireDate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(expireDate);
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Sắp xếp param theo thứ tự key tăng dần
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String value = vnp_Params.get(fieldName);
                if (value != null && !value.isEmpty()) {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()));
                    query.append('&');
                }
            }

            // Xóa ký tự `&` cuối cùng
            String queryString = query.toString();
            if (queryString.endsWith("&")) {
                queryString = queryString.substring(0, queryString.length() - 1);
            }

            // Tạo chữ ký bảo mật
            String secureHash = Config.hmacSHA512(Config.secretKey, queryString);
            String paymentUrl = Config.vnp_PayUrl + "?" + queryString + "&vnp_SecureHash=" + secureHash;

            // ✅ Lưu thông tin thanh toán vào Database trước khi chuyển hướng
            VNPayTransaction transaction = new VNPayTransaction();
            transaction.setVnpTxnRef(vnp_TxnRef);
            transaction.setStatus("pending");
            transaction.setUserId(requestDTO.getUserId()); // Lưu userId vào DB
            transaction.setReceiverId(requestDTO.getReceiverId());
            transaction.setPaymentDescrption(requestDTO.getPaymentDescrption());
            transaction.setEventId(requestDTO.getEventId());
            
            transaction.setVnpAmount(amount);
            
            vnPayTransactionService.create(transaction, requestDTO);

//            return "<a href=\"" + paymentUrl + "\">";
            return paymentUrl;
        } catch (Exception e) {
            return "Error creating payment URL: " + e.getMessage();
        }
    }
    
    @GetMapping("/vnpay_return")
    public void vnpayReturn(@RequestParam Map<String, String> params, HttpServletResponse response) {
        Map<String, Object> responseData = new HashMap<>(params); // Lưu toàn bộ tham số từ VNPay

        String responseCode = params.get("vnp_ResponseCode");
        String status = "fail";
        String message = "Thanh toán thất bại!";
        
        if ("00".equals(responseCode)) {
            status = "success";
            message = "Thanh toán thành công!";
            try {
                String vnp_TxnRef = params.get("vnp_TxnRef");
                VNPayTransaction transaction = vnPayTransactionService.getTransactionByTxnRef(vnp_TxnRef);

                // Cập nhật thông tin giao dịch
                transaction.setVnpBankCode(params.get("vnp_BankCode"));
                transaction.setVnpTransactionNo(params.get("vnp_TransactionNo"));
                transaction.setVnpTmnCode(params.get("vnp_TmnCode"));
                transaction.setVnpOrderInfo(params.get("vnp_OrderInfo"));
                transaction.setVnpAmount(Long.parseLong(params.get("vnp_Amount")) / 100);
                transaction.setVnpResponseCode(responseCode);
                transaction.setVnpTransactionStatus(params.get("vnp_TransactionStatus"));
                transaction.setVnpPayDate(params.get("vnp_PayDate"));
                transaction.setVnpCardType(params.get("vnp_CardType"));
                transaction.setStatus("success");
                
                vnPayTransactionService.saveTransaction(transaction);
                
                ticketService.updatePAIDTicket(transaction);
                
            } catch (EntityNotFoundExceptions e) {
                message = "Không tìm thấy giao dịch với mã: " + params.get("vnp_TxnRef");
            } catch (Exception e) {
                message = "Lỗi khi xử lý thanh toán! " + e.getMessage();
            }
        }

        // Chuyển hướng người dùng về frontend với trạng thái thanh toán
        try {
            String redirectUrl = "http://localhost:3001/user/payment-result?"
                    + "status=" + status
                    + "&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi chuyển hướng về trang thanh toán!", e);
        }
    }
    
    @GetMapping("/queryTransaction")
    public Map<String, Object> queryTransaction(@RequestParam("txnRef") String txnRef,
            @RequestParam("transDate") String transDate) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "querydr";
            String vnp_TmnCode = Config.vnp_TmnCode;
            String vnp_IpAddr = "192.168.1.1"; // Hoặc lấy từ request
            String vnp_CreateDate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_TxnRef", txnRef);
            vnp_Params.put("vnp_TransDate", transDate);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            // Sắp xếp tham số theo thứ tự key
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String value = vnp_Params.get(fieldName);
                if (value != null && !value.isEmpty()) {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append("=")
                            .append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()))
                            .append("&");
                }
            }

            // Xóa ký tự `&` cuối cùng
            String queryString = query.toString();
            if (queryString.endsWith("&")) {
                queryString = queryString.substring(0, queryString.length() - 1);
            }

            // Tạo chữ ký bảo mật
            String secureHash = Config.hmacSHA512(Config.secretKey, queryString);
            queryString += "&vnp_SecureHash=" + secureHash;

            // Gửi yêu cầu HTTP GET đến VNPAY
            String requestUrl = Config.vnp_ApiUrl + "?" + queryString;
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Đọc phản hồi từ VNPAY
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            // Chuyển phản hồi thành JSON
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("queryResult", response.toString());
            
            return jsonResponse;
        } catch (Exception e) {
            return Collections.singletonMap("error", "Failed to query transaction: " + e.getMessage());
        }
    }
    
    @PostMapping("/query")
    public ResponseEntity<?> queryTransaction(@RequestParam String orderId, @RequestParam String transDate, HttpServletRequest req) {
        try {
            String vnp_RequestId = Config.getRandomNumber(8);
            String vnp_Version = "2.1.0";
            String vnp_Command = "querydr";
            String vnp_TmnCode = Config.vnp_TmnCode;
            String vnp_TxnRef = orderId;
            String vnp_OrderInfo = "Kiem tra ket qua GD OrderId: " + vnp_TxnRef;
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(TimeZone.getTimeZone("Etc/GMT+7"));
            String vnp_CreateDate = formatter.format(new Date());
            
            String vnp_IpAddr = Config.getIpAddress(req);
            
            JsonObject vnp_Params = new JsonObject();
            vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
            vnp_Params.addProperty("vnp_Version", vnp_Version);
            vnp_Params.addProperty("vnp_Command", vnp_Command);
            vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.addProperty("vnp_TransactionDate", transDate);
            vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

            // Tạo SecureHash
            String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, transDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
            String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hash_Data);
            vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

            // Gửi request đến VNPay API
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(vnp_Params.toString(), headers);
            
            ResponseEntity<String> response = restTemplate.exchange(Config.vnp_ApiUrl, HttpMethod.POST, entity, String.class);

            // Trả về JSON từ VNPay
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }
    
    @PostMapping("/refund")
    public ResponseEntity<String> refund(HttpServletRequest req, @RequestParam String trantype,
            @RequestParam String order_id, @RequestParam int amount, @RequestParam String trans_date,
            @RequestParam String user) throws IOException {

        // Tham số yêu cầu
        String vnp_RequestId = Config.getRandomNumber(8); // Tạo mã yêu cầu
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = Config.vnp_TmnCode;
        String vnp_TransactionType = trantype; // "02" hoặc "03"
        String vnp_TxnRef = order_id; // Mã giao dịch thanh toán trước đó
        long amountInCents = amount * 100; // Đổi sang đơn vị nhỏ nhất (cent)
        String vnp_Amount = String.valueOf(amountInCents);
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;
        String vnp_TransactionNo = ""; // Mã giao dịch ghi nhận tại VNPAY (nếu có)
        String vnp_TransactionDate = trans_date; // Thời gian ghi nhận giao dịch
        String vnp_CreateBy = user; // Người thực hiện hoàn tiền
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        String vnp_IpAddr = Config.getIpAddress(req); // Lấy IP của máy gửi yêu cầu

        // Tạo các tham số JSON
        JsonObject vnp_Params = new JsonObject();
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", vnp_Amount);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        // Tạo hash cho SecureHash
        String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo, vnp_TransactionDate,
                vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hash_Data);
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        // Tạo URL API
        String url = Config.vnp_ApiUrl;

        // Tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo Entity để gửi yêu cầu
        HttpEntity<String> entity = new HttpEntity<>(vnp_Params.toString(), headers);
        
        RestTemplate restTemplate = new RestTemplate();
        // Gửi yêu cầu POST và nhận phản hồi
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Kiểm tra kết quả
        String responseMessage = response.getBody();
        if (responseMessage != null && responseMessage.contains("success")) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Refund failed");
        }
    }
    
}
