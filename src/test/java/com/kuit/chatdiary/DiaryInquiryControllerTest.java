package com.kuit.chatdiary;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.kuit.chatdiary.controller.diary.DiaryInquiryController;
import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.DiaryTag;
import com.kuit.chatdiary.domain.Member;
import com.kuit.chatdiary.domain.Photo;
import com.kuit.chatdiary.service.diary.DiaryInquiryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DiaryInquiryControllerTest {

    @Mock
    private DiaryInquiryService diaryInquiryService;

    @InjectMocks
    private DiaryInquiryController diaryInquiryController;


    @Test
    public void 사용자ID와날짜를기준으로일기목록을반환하면_일기정보_사진_태그가포함되어있는지확인() {
        Diary diary1 = createMockDiary(1L, "2024-01-14", "일기 제목 1", "일기 내용 1");
        Diary diary2 = createMockDiary(2L, "2024-01-14", "일기 제목 2", "일기 내용 2");

        List<Diary> mockDiaries = Arrays.asList(diary1, diary2);
        Long userId = 1L;
        LocalDate date = LocalDate.parse("2024-01-14");

        when(diaryInquiryService.getDiaryWithPhotos(userId, date)).thenReturn(mockDiaries);

        ResponseEntity<List<Diary>> response = diaryInquiryController.getDiaryList(userId, date.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Diary> returnedDiaries = response.getBody();
        assertNotNull(returnedDiaries);
        assertFalse(returnedDiaries.isEmpty());

        for (Diary diary : returnedDiaries) {
            assertNotNull(diary.getTitle());
            assertNotNull(diary.getContent());
            assertFalse(diary.getDiaryTagList().isEmpty());
            assertFalse(diary.getPhotoList().isEmpty());
        }
        verify(diaryInquiryService).getDiaryWithPhotos(userId, date);
    }


    private Diary createMockDiary(Long userId, String day, String title, String content) {
        Diary diary = new Diary();
        Member member = new Member();
        member.setUserId(userId);
        diary.setMember(member);
        diary.setDay(day);
        diary.setTitle(title);
        diary.setContent(content);
        diary.setCreateAt(LocalDateTime.now());
        diary.setUpdateAt(LocalDateTime.now());
        diary.setStatus("ACTIVE");
        DiaryTag tag1 = new DiaryTag();
        DiaryTag tag2 = new DiaryTag();

        diary.setDiaryTagList(Arrays.asList(tag1, tag2));

        Photo photo1 = new Photo();
        Photo photo2 = new Photo();
        diary.setPhotoList(Arrays.asList(photo1, photo2));

        return diary;
    }
}