package com.kuit.chatdiary.controller.notice;

import com.kuit.chatdiary.dto.diary.TagPoolResponseDTO;
import com.kuit.chatdiary.dto.notice.NoticeResponseDTO;
import com.kuit.chatdiary.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticeList(){
        return ResponseEntity.ok(noticeService.findNotices());
    }

    @GetMapping("/detail")
    public ResponseEntity<NoticeResponseDTO> getNoticeList(@RequestParam("noticeId") Long noticeId){
        return ResponseEntity.ok(noticeService.findOne(noticeId));
    }


}
