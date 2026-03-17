package com.example.demo.repository;

import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Customer;
import com.example.demo.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderEntityRepository extends JpaRepository<OrderEntity,Integer> {
    boolean existsByCustomerAndOrderStatus(Customer customer, OrderStatus orderStatus);

    @Query("""
       SELECT o FROM OrderEntity o
       WHERE o.paymentStatus = 'PENDING'
       AND o.createdAt <= :expiryTime
       """)
    List<OrderEntity> findExpiredOrders(
            @Param("expiryTime") LocalDateTime expiryTime);

    @Modifying
    @Query("""
    UPDATE OrderEntity o
    SET o.orderStatus = :status
    WHERE o.id = :orderId
""")
    void updateOrderStatus(@Param("orderId") int orderId,
                          @Param("status") OrderStatus status);

    boolean existsByCustomerAndPaymentStatus(Customer customer, PaymentStatus paymentStatus);

    List<OrderEntity> findByOrderStatus(OrderStatus orderStatus);

    OrderEntity findByPaymentIntentId(String paymentIntentId);
}
