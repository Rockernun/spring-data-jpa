package com.study.data_jpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.study.data_jpa.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save() {
        itemRepository.save(new Item("A"));
    }

}