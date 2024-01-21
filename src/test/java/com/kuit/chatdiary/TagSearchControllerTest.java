//package com.kuit.chatdiary;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import com.kuit.chatdiary.controller.TagSearchController;
//import com.kuit.chatdiary.dto.TagSearchResponse;
//import com.kuit.chatdiary.service.TagSearchService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import static org.hamcrest.Matchers.everyItem;
//import static org.hamcrest.Matchers.hasItem;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(TagSearchController.class)
//public class TagSearchControllerTest {
//
//    @MockBean
//    private TagSearchService tagSearchService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new TagSearchController(tagSearchService)).build();
//    }
//
//    @Test
//    void findByTagTest() throws Exception {
//        String testTagName1 = "holiday";
//        String testTagName2 = "work";
//
//        List<TagSearchResponse> mockResponses = createMockResponses();
//
//        when(tagSearchService.findByTag(anyString())).thenAnswer(invocation -> {
//            String tagName = invocation.getArgument(0, String.class);
//            return mockResponses.stream()
//                    .filter(response -> response.getTagList().contains(tagName))
//                    .collect(Collectors.toList());
//        });
//        /**
//         * 실제 엔드 포인트로 날리기
//         * */
//        mockMvc.perform(get("/diary/search_tag").param("tagName", testTagName1))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$[*].tagList", everyItem(hasItem(testTagName1))));
//
//        mockMvc.perform(get("/diary/search_tag").param("tagName", testTagName2))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$[*].tagList", everyItem(hasItem(testTagName2))));
//    }
//
//    private List<TagSearchResponse> createMockResponses() {
//        List<TagSearchResponse> responses = new ArrayList<>();
//        TagSearchResponse mockResponse1 = new TagSearchResponse();
//        TagSearchResponse mockResponse2 = new TagSearchResponse();
//        TagSearchResponse mockResponse3 = new TagSearchResponse();
//
//        mockResponse1.setDiaryId(1L);
//        mockResponse1.setTitle("회식 첫 회식");
//        mockResponse1.setContent("오늘 회식이다!");
//        mockResponse1.setDiaryDate(new Date());
//        mockResponse1.setTagList(Arrays.asList("holiday", "휴일"));
//        mockResponse1.setPhotoList(new ArrayList<>());
//
//        mockResponse2.setDiaryId(2L);
//        mockResponse2.setTitle("스프린트 2번째");
//        mockResponse2.setContent("스프린트 끝이다!");
//        mockResponse2.setDiaryDate(new Date());
//        mockResponse2.setTagList(Arrays.asList("work", "일"));
//        mockResponse2.setPhotoList(new ArrayList<>());
//
//        mockResponse3.setDiaryId(3L);
//        mockResponse3.setTitle("회식 두 번째 회식");
//        mockResponse3.setContent("오늘 또 회식이다!");
//        mockResponse3.setDiaryDate(new Date());
//        mockResponse3.setTagList(Arrays.asList("holiday", "휴일"));
//        mockResponse3.setPhotoList(new ArrayList<>());
//
//        responses.add(mockResponse1);
//        responses.add(mockResponse2);
//        responses.add(mockResponse3);
//        return responses;
//    }
//
//}