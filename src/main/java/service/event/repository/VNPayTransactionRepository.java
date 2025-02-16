package service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.event.model.VNPayTransaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface VNPayTransactionRepository extends JpaRepository<VNPayTransaction,Long> {

    // Tìm giao dịch theo mã vnpTxnRef (Order ID)
    Optional<VNPayTransaction> findByVnpTxnRef(String vnpTxnRef);

    // Lấy danh sách giao dịch theo userId
    List<VNPayTransaction> findByUserId(String userId);

    // Kiểm tra xem giao dịch có tồn tại hay không
    boolean existsByVnpTxnRef(String vnpTxnRef);

//    void deleteByEventTicket_EventId(Long eventId);


}
