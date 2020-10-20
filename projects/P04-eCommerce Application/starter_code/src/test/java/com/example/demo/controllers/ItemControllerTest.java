package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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

public class ItemControllerTest {


    private ItemController itemController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final ItemRepository itemRepo = mock(ItemRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "userRepository", userRepo);
        TestUtils.injectObjects(itemController, "cartRepository", cartRepo);
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
        TestUtils.injectObjects(itemController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void getItems(){

        Item item1 = new Item();
        Item item2 = new Item();

        item1.setName("firstItem");
        item1.setDescription("firstItemDescription");
        item1.setPrice(new BigDecimal(2.50));
        item2.setName("secondItem");
        item2.setDescription("secondItemDescription");
        item2.setPrice(new BigDecimal(3.50));

        itemRepo.save(item1);
        itemRepo.save(item2);

        List<Item> itemList = new ArrayList<>(2);
        itemList.add(item1);
        itemList.add(item2);
        when(itemRepo.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> response =  itemController.getItems();
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().size() == 2);

    }

    @Test
    public void getItemById(){

        Item item1 = new Item();
        item1.setId((long) 1);
        item1.setName("firstItem");
        item1.setDescription("firstItemDescription");
        item1.setPrice(new BigDecimal(2.50));
        itemRepo.save(item1);
        when(itemRepo.findById(item1.getId())).thenReturn(Optional.of(item1));
        ResponseEntity<Item> responseItem =  itemController.getItemById((item1.getId()));
        assertTrue(responseItem.getStatusCode().is2xxSuccessful());
        assertTrue(responseItem.getBody().getName() == "firstItem");
    }


    @Test
    public void getItemsByName(){

        Item item1 = new Item();
        item1.setName("firstItem");
        item1.setDescription("firstItemDescription");
        item1.setPrice(new BigDecimal(2.50));
        itemRepo.save(item1);
        List<Item> itemList = new ArrayList<>(2);
        itemList.add(item1);
        when(itemRepo.findByName(item1.getName())).thenReturn(itemList);
        ResponseEntity<List<Item>> response =  itemController.getItemsByName("firstItem");
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().get(0).getName() == "firstItem");

    }
}
