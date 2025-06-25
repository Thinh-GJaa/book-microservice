//package com.book.paymentservice.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.book.paymentservice.dto.request.PaymentRequest;
//import com.book.paymentservice.dto.response.PaymentResponse;
//import com.book.paymentservice.service.PaymentService;
//
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping("/api/payments")
//@Slf4j
//@RequiredArgsConstructor
//public class PaymentController {
//    private final PaymentService paymentService;
//
//    @PostMapping
//    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
//        log.info("Received payment request for order: {}", request.getOrderId());
//        return ResponseEntity.ok(paymentService.processPayment(request));
//    }
//
//    @GetMapping("/{orderId}")
//    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String orderId) {
//        log.info("Getting payment details for order: {}", orderId);
//        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
//    }
//
//    @PostMapping("/{orderId}/refund")
//    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable String orderId) {
//        log.info("Processing refund for order: {}", orderId);
//        return ResponseEntity.ok(paymentService.refundPayment(orderId));
//    }
//}