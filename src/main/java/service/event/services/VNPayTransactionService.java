package service.event.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.event.exceptions.EntityNotFoundExceptions;
import service.event.model.EventTicket;
import service.event.model.VNPayTransaction;
import service.event.repository.VNPayTransactionRepository;
import service.event.request.VNPayRequestDTO;

import java.util.List;

@Service
public class VNPayTransactionService {

    @Autowired
    private VNPayTransactionRepository transactionRepository;

    @Autowired
    private TicketService bookingService;

    public List<VNPayTransaction> getTransactionsByUserId(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    public boolean transactionExists(String txnRef) {

        return transactionRepository.existsByVnpTxnRef(txnRef);
    }

    public VNPayTransaction create(VNPayTransaction transaction, VNPayRequestDTO req){
        EventTicket ticket = bookingService.findById(req.getTicketId());

        if (ticket == null) {
            throw new EntityNotFoundExceptions("EventTicket không tồn tại với ID: " + req.getEventId());
        }

//        transactionRepository.deleteByEventTicket_EventId(req.getEventId());

        ticket.setTransaction(transaction);
        transaction.setEventTicket(ticket);
        return transactionRepository.save(transaction);
    }

    public VNPayTransaction getTransactionByTxnRef(String vnpTxnRef) {
        return transactionRepository.findByVnpTxnRef(vnpTxnRef)
                .orElseThrow(() -> new EntityNotFoundExceptions("Không tìm thấy giao dịch với vnpTxnRef: " + vnpTxnRef));
    }

    public VNPayTransaction saveTransaction(VNPayTransaction transaction) {
        return transactionRepository.save(transaction);
    }
}