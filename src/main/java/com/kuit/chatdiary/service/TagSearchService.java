package com.kuit.chatdiary.service;

import com.kuit.chatdiary.dto.TagSearchResponse;
import com.kuit.chatdiary.repository.TagSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagSearchService {
    private final TagSearchRepository tagSearchRepository;
    public TagSearchService(TagSearchRepository tagSearchRepository) {
        this.tagSearchRepository = tagSearchRepository;
    }
    public List<TagSearchResponse> findByTag(String tagName){
        return tagSearchRepository.findByTag(tagName);
    }
}
