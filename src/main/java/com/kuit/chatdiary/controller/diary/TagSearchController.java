package com.kuit.chatdiary.controller.diary;

import com.kuit.chatdiary.dto.diary.TagSearchResponseDTO;
import com.kuit.chatdiary.service.diary.TagSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("diary")
public class TagSearchController {
    private final TagSearchService tagSearchService;
    public TagSearchController(TagSearchService tagSearchService) {
        this.tagSearchService = tagSearchService;
    }
    @GetMapping("/list/tag")
    public List<TagSearchResponseDTO> findByTag(@RequestParam(name = "tagName") List<String> tagNames) {
        return tagSearchService.findByTag(tagNames);
    }
}
