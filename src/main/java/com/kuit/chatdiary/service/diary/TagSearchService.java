package com.kuit.chatdiary.service.diary;

import com.kuit.chatdiary.dto.diary.DiaryListResponseDTO;
import com.kuit.chatdiary.repository.diary.TagSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagSearchService {
    private final TagSearchRepository tagSearchRepository;
    public TagSearchService(TagSearchRepository tagSearchRepository) {
        this.tagSearchRepository = tagSearchRepository;
    }
    public List<DiaryListResponseDTO> findByTag(List<String> tagNames, long userId){
        return tagSearchRepository.findByTag(tagNames,userId);
    }

}