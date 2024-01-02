package employee.example.EmployeeProjetc.Service.Impl;

import employee.example.EmployeeProjetc.DTO.OrderDTO;
import employee.example.EmployeeProjetc.Entity.*;
import employee.example.EmployeeProjetc.Repository.CartItemProductRepository;
import employee.example.EmployeeProjetc.Repository.CartItemRepository;
import employee.example.EmployeeProjetc.Repository.EmployeeRepository;
import employee.example.EmployeeProjetc.Repository.OrderRepository;
import employee.example.EmployeeProjetc.Service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemProductRepository cartItemProductRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    @Transactional
    public String purchaseOrder(OrderDTO order) {
        Optional<CartItem> cartItem = cartItemRepository.findById(order.getCartItemId());
        Optional<Employee> employee = employeeRepository.findEmployeeByEmployeeId(String.valueOf(order.getEmployeeId()));

        if (cartItem.isEmpty()) {
            return "Error purchase order";
        }
        Order duplicateOrder = orderRepository.findOrderByCartItem(cartItem.get());
        if (duplicateOrder != null) {
            return "Order already existed!!";
        }
        List<CartItemProduct> cartItemProduct = cartItemProductRepository.findCartItemProductByCartItemId(order.getCartItemId());
        if (cartItemProduct.isEmpty()) {
            return "Error purchase order";
        }
        Order newOrder = new Order();
        newOrder.setOrderStatus(1);
        newOrder.setOrderDate(order.getOrderDate());
        newOrder.setAddress(order.getAddress());
        newOrder.setTotalPrice(order.getTotalPrice());
        newOrder.setMethodPayment(order.getMethodPayment());
        newOrder.setCartItem(cartItem.get());
        newOrder.setEmployee(employee.get());
        newOrder = orderRepository.save(newOrder);
        cartItem.get().setStatus(1);
        cartItemRepository.save(cartItem.get());
        return "Order successfully " + newOrder.getId();
    }

    @Override
    @Transactional
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }


    @Override
    @Transactional
    public String updateOrderStatus(Integer orderId, int newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isEmpty()) {
            return "Order not found";
        }

        Order order = optionalOrder.get();
        order.setOrderStatus(newStatus);
        orderRepository.save(order);

        return "Order status updated successfully";
    }
}