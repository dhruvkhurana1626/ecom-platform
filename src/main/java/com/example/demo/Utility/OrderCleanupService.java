package com.example.demo.Utility;

import com.example.demo.enums.OrderStatus;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.OrderItems;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderEntityRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCleanupService {

    private final OrderEntityRepository orderEntityRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Scheduled(fixedRate = 6000) // runs every 1 minute
    public void releaseExpiredOrders() {

        List<OrderEntity> pendingOrders =
                orderEntityRepository.findByOrderStatus(OrderStatus.PENDING_PAYMENT);

        LocalDateTime now = LocalDateTime.now();

        for (OrderEntity order : pendingOrders) {

            long minutes =
                    Duration.between(order.getCreatedAt(), now).toMinutes();

            if (minutes >= 10) {

                orderEntityRepository.updateOrderStatus(order.getId(), OrderStatus.FAILED);

                for (OrderItems item : order.getOrderItems()) {
                    Product product = item.getProduct();
                    productRepository.increaseStock(
                            item.getProduct().getId(),
                            item.getQuantity()
                    );
                }
            }
        }
    }

}
