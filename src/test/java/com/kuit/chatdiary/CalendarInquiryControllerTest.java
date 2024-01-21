package com.kuit.chatdiary;

import com.kuit.chatdiary.controller.calendar.CalendarInquiryController;
import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.CalendarInquiryResponse;
import com.kuit.chatdiary.service.calendar.CalendarInquiryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CalendarInquiryControllerTest {

    @Mock
    private CalendarInquiryService calendarInquiryService;

    @InjectMocks
    private CalendarInquiryController calendarInquiryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(calendarInquiryController).build();
    }

    @Test
    void getChatExistsByMonth() throws Exception {
        long memberId = 1L;
        YearMonth yearMonth = YearMonth.of(2024, 1);
        Map<LocalDate, List<CalendarInquiryResponse>> mockResult = createMockResponse();

        when(calendarInquiryService.existsChatByMonth(memberId, yearMonth)).thenReturn(mockResult);

        mockMvc.perform(get("/chat/chat-exists-month")
                        .param("memberId", String.valueOf(memberId))
                        .param("month", yearMonth.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private Map<LocalDate, List<CalendarInquiryResponse>> createMockResponse() {
        Map<LocalDate, List<CalendarInquiryResponse>> mockResult = new HashMap<>();
        LocalDate sampleDate = LocalDate.of(2024, 1, 1);
        List<CalendarInquiryResponse> responses = Arrays.asList(
                new CalendarInquiryResponse(Sender.CHICHI, false),
                new CalendarInquiryResponse(Sender.LULU, true),
                new CalendarInquiryResponse(Sender.DADA, false),
                new CalendarInquiryResponse(Sender.USER, true)
        );
        mockResult.put(sampleDate, responses);
        return mockResult;
    }
}

