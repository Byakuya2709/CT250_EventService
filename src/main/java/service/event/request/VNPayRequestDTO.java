package service.event.request;

public class VNPayRequestDTO {

    private String userId;
    private String receiverId;
    private String paymentDescrption;
    private Long eventId;

    public VNPayRequestDTO() {
    }

    public VNPayRequestDTO(String userId, String receiverId, String paymentDescrption, Long eventId) {
        this.userId = userId;
        this.receiverId = receiverId;
        this.paymentDescrption = paymentDescrption;
        this.eventId = eventId;
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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
