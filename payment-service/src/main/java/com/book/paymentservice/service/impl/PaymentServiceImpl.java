package com.book.paymentservice.service.impl;

import com.book.paymentservice.dto.event.OrderEvent;
import com.book.paymentservice.entity.Payment;
import com.book.paymentservice.entity.PaymentTransaction;
import com.book.paymentservice.enums.PaymentStatus;
import com.book.paymentservice.enums.TransactionStatus;
import com.book.paymentservice.enums.TransactionType;
import com.book.paymentservice.mapper.PaymentMapper;
import com.book.paymentservice.producer.PaymentProducer;
import com.book.paymentservice.repository.PaymentRepository;
import com.book.paymentservice.repository.PaymentTransactionRepository;
import com.book.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    PaymentMapper paymentMapper;
    PaymentRepository paymentRepository;
    PaymentTransactionRepository paymentTransactionRepository;
    PaymentProducer paymentProducer;

    @Override
    public void processPayment(OrderEvent request) {
        Payment payment = null;
        PaymentTransaction transaction = null;
        log.info("[PAYMENT SERVICE] Processing payment for order: {}", request.getOrderId());

        try {
            // Tạo và lưu payment với status PENDING
            payment = paymentMapper.toPayment(request);

            payment = paymentRepository.save(payment);

            // Tạo transaction với status PENDING
            // Tạo transaction với status PENDING
            transaction = PaymentTransaction.builder()
                    .payment(payment)
                    .type(TransactionType.PAYMENT)
                    .status(TransactionStatus.PENDING)
                    .build();

            transaction = paymentTransactionRepository.save(transaction);

            log.info("[PAYMENT SERVICE] Payment and transaction created with PENDING status for order: {}",
                    request.getOrderId());

        } catch (Exception e) {
            log.error("[PAYMENT SERVICE] Error creating payment/transaction for order: {}", request.getOrderId(), e);
            throw e; // Rethrow để trigger transaction rollback
        }

        // Xử lý thanh toán thực tế (gọi hàm riêng)
        try {
            log.info("[PAYMENT SERVICE] Executing payment processing for order: {}", request.getOrderId());

            executePayment(); // Gọi hàm thực hiện thanh toán (có thể lỗi xác suất thấp)

            // Cập nhật transaction status thành SUCCESS
            transaction.setStatus(TransactionStatus.SUCCESS);
            paymentTransactionRepository.save(transaction);

            // Cập nhật payment status thành COMPLETED
            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment);

            log.info("[PAYMENT SERVICE] Payment completed successfully for order: {}", request.getOrderId());
            paymentProducer.sendPaymentSuccessEvent(request);

        } catch (Exception e) {
            log.error("[PAYMENT SERVICE] Error during payment processing for order: {}", request.getOrderId(), e);

            // Cập nhật transaction status thành FAILED
            try {
                transaction.setStatus(TransactionStatus.FAILED);
                payment.setStatus(PaymentStatus.FAILED);
                paymentTransactionRepository.save(transaction);
                paymentRepository.save(payment);

                log.error("[PAYMENT SERVICE] Payment and transaction status updated to FAILED for order: {}",
                        request.getOrderId());
                paymentProducer.sendPaymentFailEvent(request);
            } catch (Exception updateException) {
                log.error("[PAYMENT SERVICE] Error updating status to FAILED for order: {}", request.getOrderId(),
                        updateException);
            }
        }
    }

    // Hàm thực hiện thanh toán (có thể lỗi xác suất thấp)
    private void executePayment() throws Exception {
        // Giả lập thời gian xử lý
        Thread.sleep(1000);
        // Xác suất lỗi thấp (10%)
        if (Math.random() < 0.1) {
            throw new Exception("Simulated payment failure");
        }
    }
}