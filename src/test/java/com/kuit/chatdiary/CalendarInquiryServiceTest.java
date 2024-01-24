//package com.kuit.chatdiary;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.kuit.chatdiary.dto.CalendarInquiryResponse;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.api.BeforeEach;
//import static org.mockito.Mockito.when;
//import static org.assertj.core.api.Assertions.assertThat;
//import com.kuit.chatdiary.domain.Sender;
//import com.kuit.chatdiary.repository.CalendarInquiryRepository;
//import com.kuit.chatdiary.service.calendar.CalendarInquiryService;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@ExtendWith(MockitoExtension.class)
//public class CalendarInquiryServiceTest {
//
//    @Mock
//    private CalendarInquiryRepository calendarInquiryRepository;
//
//    private CalendarInquiryService calendarInquiryService;
//
//    @BeforeEach
//    void setUp() {
//        calendarInquiryService = new CalendarInquiryService(calendarInquiryRepository);
//    }
//
//    @Test
//    void existsChatByMonth() {
//        long memberId = 1L;
//        YearMonth yearMonth = YearMonth.of(2024, 1);
//        Map<LocalDate, List<CalendarInquiryResponse>> mockResult = createMockResponse();
//
//        when(calendarInquiryRepository.existsChatByMonth(memberId, yearMonth)).thenReturn(mockResult);
//
//        Map<LocalDate, List<CalendarInquiryResponse>> results = calendarInquiryService.existsChatByMonth(memberId, yearMonth);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        try {
//            String jsonResult = objectMapper.writeValueAsString(results);
//            System.out.println(jsonResult);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        assertThat(results).isEqualTo(mockResult);
//    }
//
//    private Map<LocalDate, List<CalendarInquiryResponse>> createMockResponse() {
//        Map<LocalDate, List<CalendarInquiryResponse>> mockResult = new HashMap<>();
//        LocalDate sampleDate = LocalDate.of(2024, 1, 1);
//        List<CalendarInquiryResponse> responses = Arrays.asList(
//                new CalendarInquiryResponse(Sender.CHICHI, false),
//                new CalendarInquiryResponse(Sender.LULU, true),
//                new CalendarInquiryResponse(Sender.DADA, false),
//                new CalendarInquiryResponse(Sender.USER, true)
//        );
//        mockResult.put(sampleDate, responses);
//        return mockResult;
//    }
//}
//
