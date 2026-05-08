package com.example.demo.Utility;

import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.OrderItems;
import com.example.demo.repository.OrderEntityRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCleanupService {

    private final OrderEntityRepository orderEntityRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void releaseExpiredOrders() {

        LocalDateTime expiryTime =
                LocalDateTime.now().minusMinutes(10);

        List<OrderEntity> expiredOrders =
                orderEntityRepository.findExpiredOrders(expiryTime);

        for (OrderEntity order : expiredOrders) {

            order.setOrderStatus(OrderStatus.FAILED);
            order.setPaymentStatus(PaymentStatus.FAILED);

            for (OrderItems item : order.getOrderItems()) {

                productRepository.incrementStock(
                        item.getProduct().getId(),
                        item.getQuantity()
                );
            }
        }
    }

    //to clean all the failed and cancelled orders from database
//    @Transactional
//    @Scheduled(fixedRate = 6000000)
//    public void clearCancelledandFailedOrders(){
//
//        List<OrderEntity> cancelledOrders = orderEntityRepository.findByOrderStatus(OrderStatus.CANCELLED);
//        for(OrderEntity order : cancelledOrders){
//            orderEntityRepository.delete(order);
//        }
//
//        List<OrderEntity> failedOrders = orderEntityRepository.findByOrderStatus(OrderStatus.FAILED);
//        for(OrderEntity order : failedOrders){
//            orderEntityRepository.delete(order);
//        }
//
//    }

}
