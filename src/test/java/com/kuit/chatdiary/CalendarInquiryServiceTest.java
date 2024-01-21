package com.kuit.chatdiary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.repository.CalendarInquiryRepository;
import com.kuit.chatdiary.service.calendar.CalendarInquiryService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CalendarInquiryServiceTest {
    @Mock
    private CalendarInquiryRepository calendarInquiryRepository;
    private CalendarInquiryService calendarInquiryService;

    @BeforeEach
    void setUp() {
        calendarInquiryService = new CalendarInquiryService(calendarInquiryRepository);
    }

    @Test
    void existsChatByMonth() {
        long memberId = 1L;
        YearMonth yearMonth = YearMonth.of(2024, 1);
        Map<LocalDate, Map<Sender, Boolean>> mockResult = createMockResult();

        when(calendarInquiryRepository.existsChatByMonth(memberId, yearMonth)).thenReturn(mockResult);

        Map<LocalDate, Map<Sender, Boolean>> results = calendarInquiryService.existsChatByMonth(memberId, yearMonth);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            String jsonResult = objectMapper.writeValueAsString(results);
            System.out.println(jsonResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertThat(results).isEqualTo(mockResult);
    }


    private Map<LocalDate, Map<Sender, Boolean>> createMockResult() {
        Map<LocalDate, Map<Sender, Boolean>> mockResult = new HashMap<>();
        LocalDate sampleDate = LocalDate.of(2024, 1, 1);
        Map<Sender, Boolean> senderStatus = new HashMap<>();
        senderStatus.put(Sender.CHICHI, false);
        senderStatus.put(Sender.LULU, true);
        senderStatus.put(Sender.DADA, false);
        senderStatus.put(Sender.USER, true);
        mockResult.put(sampleDate, senderStatus);
        return mockResult;
    }

}
