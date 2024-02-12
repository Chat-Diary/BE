package com.kuit.chatdiary.service.notice;

import com.kuit.chatdiary.domain.Notice;
import com.kuit.chatdiary.dto.notice.NoticeListResponseDTO;
import com.kuit.chatdiary.dto.notice.NoticeResponseDTO;
import com.kuit.chatdiary.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     *
     * */

    public List<NoticeListResponseDTO> findNotices() {
        List<Notice> notices = noticeRepository.findAll();
        return notices.stream()
                .map(notice -> new NoticeListResponseDTO(
                        notice.getNoticeId(),
                        notice.getTitle(),
                        notice.getNoticeDate()))
                .collect(Collectors.toList());
    }

    public NoticeResponseDTO findOne(Long noticeId) {
        Notice notice = noticeRepository.findOne(noticeId);
        if (notice == null) {
            return null;
        }
        return new NoticeResponseDTO(
                notice.getNoticeId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getNoticeDate()
        );
    }

}
