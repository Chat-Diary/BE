package com.kuit.chatdiary.service.diary;

import com.kuit.chatdiary.dto.diary.TagSearchResponseDTO;
import com.kuit.chatdiary.repository.TagSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagSearchService {
    
    private final TagSearchRepository tagSearchRepository;
    public TagSearchService(TagSearchRepository tagSearchRepository) {
        this.tagSearchRepository = tagSearchRepository;
    }
    public List<TagSearchResponseDTO> findByTag(List<String> tagNames){
        return tagSearchRepository.findByTag(tagNames);
    }

}
