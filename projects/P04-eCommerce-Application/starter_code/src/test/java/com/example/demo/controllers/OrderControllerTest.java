package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.criteria.Order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final OrderRepository orderRepo = mock(OrderRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "cartRepository", cartRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void submitOrder(){
        String username = "test";
        User user = new User();
        Cart cart = new Cart();
        user.setUsername(username);
        Item item = new Item();
        item.setId((long) 0);
        item.setName("myItem");
        item.setDescription("myDescription");
        item.setPrice(new BigDecimal(2.99));
        Item item2 = new Item();
        item2.setId((long) 0);
        item2.setName("myItem2");
        item2.setDescription("myDescription2");
        item2.setPrice(new BigDecimal(3.99));
        List<Item> itemList = new ArrayList<>();
        cart.setItems(itemList);
        cart.setUser(user);
        cart.setTotal(new BigDecimal(6.98));
        user.setCart(cart);
        when(userRepo.findByUsername(username)).thenReturn(user);
        ResponseEntity<UserOrder> responseEntity = orderController.submit(username);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(responseEntity.getBody().getUser());
        assertNotNull(responseEntity.getBody().getItems());
        assertNotNull(responseEntity.getBody().getTotal());
    }

    @Test
    public void getOrdersForUser(){
        String username = "test";
        User user = new User();
        user.setUsername(username);

        Item item = new Item();
        item.setId((long) 0);
        item.setName("myItem");
        item.setDescription("myDescription");
        item.setPrice(new BigDecimal(2.99));
        Item item2 = new Item();
        item2.setId((long) 0);
        item2.setName("myItem2");
        item2.setDescription("myDescription2");
        item2.setPrice(new BigDecimal(3.99));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        itemList.add(item2);

        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setItems(itemList);
        userOrder.setTotal(new BigDecimal(6));

        List<UserOrder> orderList = new ArrayList<>();
        orderList.add(userOrder);

        when(userRepo.findByUsername(username)).thenReturn(user);
        when(orderRepo.findByUser(user)).thenReturn(orderList);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(username);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(responseEntity.getBody().get(0).getUser() == user);
        assertTrue(responseEntity.getBody().get(0).getItems().size() == 2);
        assertNotNull(responseEntity.getBody().get(0).getTotal());

    }
}
