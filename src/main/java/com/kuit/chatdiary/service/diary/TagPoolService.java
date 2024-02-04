package com.kuit.chatdiary.service.diary;

import com.kuit.chatdiary.domain.Tag;
import com.kuit.chatdiary.dto.diary.TagPoolResponseDTO;
import com.kuit.chatdiary.repository.diary.TagPoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagPoolService {

    private final TagPoolRepository tagPoolRepository;

    public TagPoolService(TagPoolRepository tagPoolRepository) {
        this.tagPoolRepository = tagPoolRepository;
    }

    public List<TagPoolResponseDTO> getTagPool(){
        return tagPoolRepository.findAllTags();
    }

}
