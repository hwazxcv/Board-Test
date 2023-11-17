package com.board.jpaex;

import com.board.entities.BoardData;
import com.board.entities.HashTag;
import com.board.repositories.BoardDataRepository;
import com.board.repositories.HashTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
public class Ex07 {

    @Autowired
    private BoardDataRepository boardDataRepository;
    @Autowired
    private HashTagRepository tagRepository;

    @BeforeEach
    void init(){
        List<HashTag> tags = new ArrayList<>();
        for(int i =1; i<=5; i++){
            HashTag tag = new HashTag();
            tag.setTag("태그"+i);
            tags.add(tag);
        }

        tagRepository.saveAllAndFlush(tags);

        List<BoardData> items = new ArrayList<>();

        for(int i =1; i<=5; i++){
            BoardData item = BoardData.builder()
                    .subject("제목"+i)
                    .content("내용"+i)
                   // .tags(tags)
                    .build();
            items.add(item);

        }
        boardDataRepository.saveAllAndFlush(items);


    }



    @Test
    void test1(){
        BoardData data = boardDataRepository.findById(1L).orElse(null);
      //  List<HashTag> tags = data.getTags();
     //   tags.stream().forEach(System.out::println);

    }

    @Test
    void test2(){
        HashTag tag = tagRepository.findById("태그2").orElse(null);
        List<BoardData> items = tag.getItems();
        items.stream().forEach(System.out::println);
    }
}
