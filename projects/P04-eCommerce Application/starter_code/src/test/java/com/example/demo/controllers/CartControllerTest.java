package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final ItemRepository itemRepo = mock(ItemRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
        TestUtils.injectObjects(cartController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void testAddTocart() {
        int quantity = 2;
        String username = "test";

        User user = new User();
        user.setUsername(username);
        user.setCart(new Cart());
        Item item = new Item();
        item.setId((long) 0);
        item.setName("myItem");
        item.setDescription("myDescription");
        item.setPrice(new BigDecimal(2.99));

        when(userRepo.findByUsername(username)).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(quantity);
        modifyCartRequest.setUsername(username);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().getItems().size() == quantity);
        BigDecimal expected = item.getPrice().add(item.getPrice());
        assertTrue(response.getBody().getTotal().compareTo(expected) == 0);
    }

    @Test
    public void removeItemFromCart() {
        int quantity = 2;
        String username = "test";

        User user = new User();
        user.setUsername(username);
        user.setCart(new Cart());
        Item item = new Item();
        item.setId((long) 0);
        item.setName("myItem");
        item.setDescription("myDescription");
        item.setPrice(new BigDecimal(2.99));

        when(userRepo.findByUsername(username)).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(quantity);
        modifyCartRequest.setUsername(username);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().getItems().size() == quantity);
        BigDecimal expected = item.getPrice().add(item.getPrice());
        assertTrue(response.getBody().getTotal().compareTo(expected) == 0);

        ResponseEntity<Cart> removeResponse = cartController.removeFromcart(modifyCartRequest);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().getItems().size() == 0);

    }
}
