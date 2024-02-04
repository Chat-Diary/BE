package com.kuit.chatdiary.controller.diary;

import com.kuit.chatdiary.dto.diary.TagPoolResponseDTO;
import com.kuit.chatdiary.service.diary.TagPoolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/diary")
public class TagPoolController {
    private final TagPoolService tagPoolService;

    public TagPoolController(TagPoolService tagPoolService) {
        this.tagPoolService = tagPoolService;
    }

    @GetMapping("/tags/pool")
    public ResponseEntity<List<TagPoolResponseDTO>> getTagPool(){
        return ResponseEntity.ok(tagPoolService.getTagPool());
    }
}
