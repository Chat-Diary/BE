package com.kuit.chatdiary.controller;

import com.kuit.chatdiary.dto.TagSearchResponse;
import com.kuit.chatdiary.service.TagSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagSearchController {
    private final TagSearchService tagSearchService;
    public TagSearchController(TagSearchService tagSearchService) {
        this.tagSearchService = tagSearchService;
    }
    @GetMapping("/search-tag")
    public List<TagSearchResponse> findByTag(@RequestParam String tagName) {
        return tagSearchService.findByTag(tagName);
    }
}
