package kr.co.anabada.admin.controller;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.anabada.buy.entity.Payment;
import kr.co.anabada.buy.repository.PaymentRepository;

@Controller
public class DashboardController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getDashboard(Model model) {
        // 서버 시간대에 맞춰서 날짜를 구합니다
        ZoneId zoneId = ZoneId.of("Asia/Seoul");  // 시간대를 서울로 설정
        ZonedDateTime today = ZonedDateTime.now(zoneId);  // 서울 시간대에 맞춘 날짜

        // 일매출 데이터 구하기
        LocalDateTime startOfDay = today.toLocalDateTime().with(LocalTime.MIDNIGHT);  // 오늘 00:00:00
        LocalDateTime endOfDay = today.toLocalDateTime().with(LocalTime.MAX);  // 오늘 23:59:59.999999999
        List<Object[]> dailySales = paymentRepository.sumTotalSalesByDateRange(startOfDay, endOfDay);

        long todayAmount = 0;
        for (Object[] data : dailySales) {
            todayAmount += ((BigDecimal) data[1]).longValue();  // BigDecimal을 long으로 변환 후 합산
        }

        // 주매출 데이터 구하기 (일주일 시작일과 종료일)
        ZonedDateTime startOfWeek = today.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);  // 이번 주 월요일 00:00
        ZonedDateTime endOfWeek = today.with(DayOfWeek.SUNDAY).truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);  // 이번 주 일요일 23:59:59.999999999

        // 주 매출 데이터 가져오기
        Long weeklySales = paymentRepository.sumTotalSalesByWeek(startOfWeek.toLocalDateTime(), endOfWeek.toLocalDateTime());
        if (weeklySales == null) {
            weeklySales = 0L;  // 주 매출이 없으면 0으로 설정
        }

        // 일매출 데이터를 차트용 데이터로 변환
        List<String> dailySalesLabels = new ArrayList<>(); // x축
        List<Double> dailySalesAmounts = new ArrayList<>(); // y축

        // 날짜 포맷터 정의 (MM/dd 형식)
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd");

        // 하루 매출 데이터가 여러 날짜로 나뉘므로, 일주일을 한 묶음으로 나누는 로직 추가
        ZonedDateTime startOfWeekForChart = today.with(LocalTime.MIDNIGHT).minusDays(today.getDayOfWeek().getValue() - 1); // 이번 주 월요일
        for (int i = 0; i < 7; i++) {
            ZonedDateTime startOfDayForWeek = startOfWeekForChart.plusDays(i);  // i번째 날
            ZonedDateTime endOfDayForWeek = startOfDayForWeek.plusDays(1).minusNanos(1);  // 해당 날짜 23:59:59

            List<Object[]> dailySalesInRange = paymentRepository.sumTotalSalesByDateRange(startOfDayForWeek.toLocalDateTime(), endOfDayForWeek.toLocalDateTime());
            long dailyAmount = 0;
            for (Object[] data : dailySalesInRange) {
                dailyAmount += ((BigDecimal) data[1]).longValue(); 
            }

            // 날짜를 추가할 때 날짜를 "MM/dd" 형식으로 포맷하여 추가
            String formattedDate = startOfDayForWeek.format(dateFormatter);
            dailySalesLabels.add(formattedDate);
            dailySalesAmounts.add((double) dailyAmount);  // 해당 날짜의 매출 금액
        }
        
        // 월매출 데이터 구하기
        LocalDateTime startOfMonth = today.toLocalDateTime().withDayOfMonth(1).with(LocalTime.MIDNIGHT);  // 월의 첫 날 00:00:00
        LocalDateTime endOfMonth = today.toLocalDateTime().withDayOfMonth(today.toLocalDate().lengthOfMonth()).with(LocalTime.MAX);  // 월의 마지막 날 23:59:59.999999999
        List<Object[]> monthlySales = paymentRepository.sumTotalSalesByMonth(startOfMonth, endOfMonth);

        // 월매출 데이터
        List<Double> monthlySalesAmounts = new ArrayList<>(12); // 12개월을 위한 크기 설정
        double monthlyTotalSales = 0; // 월매출 총합을 저장할 변수

        for (int i = 0; i < 12; i++) {
            monthlySalesAmounts.add(0.0);  // 초기값 설정
        }

        for (Object[] data : monthlySales) {
            Integer month = (Integer) data[0];  
            BigDecimal amountObj = (BigDecimal) data[1]; 

            if (month == 0) {
                month = 1;  // 만약 0이면 1월로 처리
            }

            monthlySalesAmounts.set(month - 1, amountObj.doubleValue()); 
            monthlyTotalSales += amountObj.doubleValue(); // 월매출 총합 계산
        }
        
        // 1월부터 12월까지 매출 데이터 구하기
        LocalDateTime startOfYear = today.toLocalDateTime().withMonth(1).withDayOfMonth(1).with(LocalTime.MIDNIGHT);
        LocalDateTime endOfYear = today.toLocalDateTime().withMonth(12).withDayOfMonth(31).with(LocalTime.MAX);

        List<Object[]> monthlySalesGraphData = paymentRepository.sumTotalSalesByMonth(startOfYear, endOfYear);

        // 월별 매출 데이터를 12개월 배열로 변환
        List<Double> monthlySalesGraphAmounts = new ArrayList<>(12); // 12개월을 위한 크기 설정
        for (int i = 0; i < 12; i++) {
            monthlySalesGraphAmounts.add(0.0);  // 초기값 설정
        }

        // 월별 매출 데이터 처리
        for (Object[] data : monthlySalesGraphData) {
            Integer month = (Integer) data[0];  // 월 (1부터 12까지)
            BigDecimal amountObj = (BigDecimal) data[1];  // 매출 금액

            if (month == 0) {
                month = 1;  // 만약 0이면 1월로 처리
            }

            // 해당 월에 매출을 기록
            monthlySalesGraphAmounts.set(month - 1, amountObj.doubleValue());
        }

        // 모델에 데이터 추가
        model.addAttribute("todaySalesAmount", todayAmount);  
        model.addAttribute("dailySalesLabels", dailySalesLabels);
        model.addAttribute("dailySalesAmounts", dailySalesAmounts);
        model.addAttribute("weeklySalesAmount", weeklySales); 
        model.addAttribute("monthlySalesAmounts", monthlySalesAmounts);
        model.addAttribute("monthlySalesTotal", monthlyTotalSales); // 월매출 총합
        model.addAttribute("monthlySalesGraphAmounts", monthlySalesGraphAmounts); // 1~12월 매출 데이터 (그래프용)
        
        return "admin/salesDashboard";  // 대시보드 JSP 페이지로 이동
    }
    

    @GetMapping("/admin/daily-details")
    public String getDailySales(Model model) {
        // 서버 시간대에 맞춰서 날짜를 구합니다
        ZoneId zoneId = ZoneId.of("Asia/Seoul");  // 시간대를 서울로 설정
        ZonedDateTime today = ZonedDateTime.now(zoneId);  // 서울 시간대에 맞춘 날짜

        // 일매출 데이터 구하기
        LocalDateTime startOfDay = today.toLocalDateTime().with(LocalTime.MIDNIGHT);  // 오늘 00:00:00
        LocalDateTime endOfDay = today.toLocalDateTime().with(LocalTime.MAX);  // 오늘 23:59:59.999999999

        // 하루 매출 리스트 구하기
        List<Payment> dailyPayments = paymentRepository.findPaymentsByDateRange(startOfDay, endOfDay);

        // 모델에 데이터 추가
        String startPeriod = startOfDay.toString();  // 오늘 시작 시간
        String endPeriod = endOfDay.toString();  // 오늘 끝 시간

        model.addAttribute("startPeriod", startPeriod);
        model.addAttribute("endPeriod", endPeriod);
        
        model.addAttribute("dailyPayments", dailyPayments);  // 일매출 리스트 추가

        return "admin/dailySalesDetails";  // 일매출 상세보기 JSP 페이지로 이동
    }
    
    @GetMapping("/admin/weekly-details")
    public String getWeeklySales(Model model) {
        // 서버 시간대에 맞춰서 날짜를 구합니다
        ZoneId zoneId = ZoneId.of("Asia/Seoul");  // 시간대를 서울로 설정
        ZonedDateTime today = ZonedDateTime.now(zoneId);  // 서울 시간대에 맞춘 날짜

        // 주 매출 데이터 구하기 (일주일 시작일과 종료일)
        ZonedDateTime startOfWeek = today.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);  // 이번 주 월요일 00:00
        ZonedDateTime endOfWeek = today.with(DayOfWeek.SUNDAY).truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);  // 이번 주 일요일 23:59:59.999999999

        // 주 매출 데이터 가져오기
        List<Object[]> weeklySales = paymentRepository.sumTotalSalesByDateRangeWithPaymentId(startOfWeek.toLocalDateTime(), endOfWeek.toLocalDateTime());

        model.addAttribute("startOfWeek", startOfWeek.toLocalDateTime());
        model.addAttribute("endOfWeek", endOfWeek.toLocalDateTime());
        model.addAttribute("weeklySales", weeklySales);  // 주 매출 리스트

        return "admin/weeklySalesDetail";  // 주 매출 상세보기 페이지로 이동
    }
    @GetMapping("/admin/monthly-details")
    public String getMonthlySales(Model model) {
        // 서버 시간대에 맞춰서 날짜를 구합니다
        ZoneId zoneId = ZoneId.of("Asia/Seoul");  // 시간대를 서울로 설정
        ZonedDateTime today = ZonedDateTime.now(zoneId);  // 서울 시간대에 맞춘 날짜

        // 1월 1일부터 12월 31일까지의 날짜 범위 구하기
        LocalDateTime startOfYear = today.toLocalDateTime().withMonth(1).withDayOfMonth(1).with(LocalTime.MIDNIGHT);
        LocalDateTime endOfYear = today.toLocalDateTime().withMonth(12).withDayOfMonth(31).with(LocalTime.MAX);

        List<Object[]> monthlySales = paymentRepository.sumTotalSalesByMonth(startOfYear, endOfYear);

        // 모델에 데이터를 추가
        model.addAttribute("startOfYear", startOfYear);  // 연도의 첫 날
        model.addAttribute("endOfYear", endOfYear);  // 연도의 마지막 날
        model.addAttribute("monthlySales", monthlySales);  // 월별 매출 데이터

        return "admin/monthlySalesDetail";  // 월 매출 상세보기 페이지로 이동
    }

}
