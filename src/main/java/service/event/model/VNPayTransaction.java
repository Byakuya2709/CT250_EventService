package service.event.model;

import javax.persistence.*;

@Entity
@Table(name = "vnpay_transactions")
public class VNPayTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vnpTxnRef; // orderId
    private String vnpBankCode;
    private String vnpTransactionNo;
    private String vnpTmnCode;
    private String vnpOrderInfo;
    private Long vnpAmount;
    private String vnpResponseCode;
    private String vnpTransactionStatus;
    private String vnpPayDate;
    private String vnpCardType;
    private String status;


    private String userId;
    private String receiverId;
    private String paymentDescrption;
    private Long eventId;

    public VNPayTransaction() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @OneToOne(mappedBy = "transaction")
    private EventTicket eventTicket;



    public VNPayTransaction(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVnpTxnRef() {
        return vnpTxnRef;
    }

    public void setVnpTxnRef(String vnpTxnRef) {
        this.vnpTxnRef = vnpTxnRef;
    }

    public String getVnpBankCode() {
        return vnpBankCode;
    }

    public void setVnpBankCode(String vnpBankCode) {
        this.vnpBankCode = vnpBankCode;
    }

    public String getVnpTransactionNo() {
        return vnpTransactionNo;
    }

    public void setVnpTransactionNo(String vnpTransactionNo) {
        this.vnpTransactionNo = vnpTransactionNo;
    }

    public String getVnpTmnCode() {
        return vnpTmnCode;
    }

    public void setVnpTmnCode(String vnpTmnCode) {
        this.vnpTmnCode = vnpTmnCode;
    }

    public String getVnpOrderInfo() {
        return vnpOrderInfo;
    }

    public void setVnpOrderInfo(String vnpOrderInfo) {
        this.vnpOrderInfo = vnpOrderInfo;
    }

    public Long getVnpAmount() {
        return vnpAmount;
    }

    public void setVnpAmount(Long vnpAmount) {
        this.vnpAmount = vnpAmount;
    }

    public String getVnpResponseCode() {
        return vnpResponseCode;
    }

    public void setVnpResponseCode(String vnpResponseCode) {
        this.vnpResponseCode = vnpResponseCode;
    }

    public String getVnpTransactionStatus() {
        return vnpTransactionStatus;
    }

    public void setVnpTransactionStatus(String vnpTransactionStatus) {
        this.vnpTransactionStatus = vnpTransactionStatus;
    }

    public String getVnpPayDate() {
        return vnpPayDate;
    }

    public void setVnpPayDate(String vnpPayDate) {
        this.vnpPayDate = vnpPayDate;
    }

    public String getVnpCardType() {
        return vnpCardType;
    }

    public void setVnpCardType(String vnpCardType) {
        this.vnpCardType = vnpCardType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getPaymentDescrption() {
        return paymentDescrption;
    }

    public void setPaymentDescrption(String paymentDescrption) {
        this.paymentDescrption = paymentDescrption;
    }

    public EventTicket getEventTicket() {
        return eventTicket;
    }

    public void setEventTicket(EventTicket eventTicket) {
        this.eventTicket = eventTicket;
    }
// Getters và Setters
}
