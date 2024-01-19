//package com.kuit.chatdiary;
//
//import com.kuit.chatdiary.domain.Diary;
//import com.kuit.chatdiary.domain.DiaryTag;
//import com.kuit.chatdiary.domain.Photo;
//import com.kuit.chatdiary.domain.Tag;
//import com.kuit.chatdiary.dto.TagSearchResponse;
//import com.kuit.chatdiary.repository.TagSearchRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//class TagSearchRepositoryTest {
//
//    @Mock
//    private EntityManager entityManager;
//
//    @Mock
//    private TypedQuery<Diary> typedQuery;
//
//    private TagSearchRepository tagSearchRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        tagSearchRepository = new TagSearchRepository(entityManager);
//
//        // Mock Diary 객체들 생성
//        Diary diary1 = createMockDiary(1L, "테스트 일기 1", "첫 번째 테스트 일기입니다.", Arrays.asList("태그1", "태그2"), Arrays.asList("imageUrl1", "imageUrl2"));
//        Diary diary2 = createMockDiary(2L, "테스트 일기 2", "두 번째 테스트 일기입니다.", Arrays.asList("태그3"), Arrays.asList("imageUrl3"));
//        Diary diary3 = createMockDiary(3L, "테스트 일기 3", "세 번째 테스트 일기입니다.", Arrays.asList("태그4", "태그5", "태그6"), Arrays.asList("imageUrl4", "imageUrl5"));
//
//        // TypedQuery의 동작을 모의 설정
//        when(entityManager.createQuery(anyString(), eq(Diary.class))).thenReturn(typedQuery);
//        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(Arrays.asList(diary1, diary2, diary3));
//    }
//
//    private Diary createMockDiary(Long id, String title, String content, List<String> tagNames, List<String> photoUrls) {
//        Diary diary = new Diary();
//        diary.setDiaryId(id);
//        diary.setTitle(title);
//        diary.setContent(content);
//        diary.setCreateAt(LocalDateTime.now());
//        diary.setUpdateAt(LocalDateTime.now());
//        diary.setStatus("ACTIVE");
//
//        // 태그 리스트 설정
//        List<DiaryTag> tags = tagNames.stream().map(tagName -> {
//            Tag tag = new Tag();
//            tag.setTagName(tagName);
//
//            DiaryTag diaryTag = new DiaryTag();
//            diaryTag.setTag(tag);
//            return diaryTag;
//        }).collect(Collectors.toList());
//        diary.setDiaryTagList(tags);
//
//        // 사진 리스트 설정
//        List<Photo> photos = photoUrls.stream().map(url -> {
//            Photo photo = new Photo();
//            photo.setImageUrl(url);
//            photo.setDiary(diary);
//            return photo;
//        }).collect(Collectors.toList());
//        diary.setPhotoList(photos);
//
//        return diary;
//    }
//
//    @Test
//    void findByTagTest() {
//        // 테스트 실행
//        List<TagSearchResponse> responses = tagSearchRepository.findByTag("testTag");
//
//        // 결과 검증
//        assertFalse(responses.isEmpty(), "응답이 비어있으면 안됩니다.");
//        assertEquals(3, responses.size(), "응답에는 세 개의 일기 항목이 있어야 합니다.");
//
//        // 결과 출력
//        for (TagSearchResponse response : responses) {
//            System.out.println(response.getTitle() + ": " + response.getContent() +
//                    ", 태그: " + response.getTagList() +
//                    ", 사진 URL: " + response.getPhotoList());
//        }
//    }
//}