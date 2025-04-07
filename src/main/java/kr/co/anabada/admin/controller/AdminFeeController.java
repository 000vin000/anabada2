package kr.co.anabada.admin.controller;

import kr.co.anabada.admin.entity.AdminFee;
import kr.co.anabada.admin.repository.AdminFeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AdminFeeController {

    private final AdminFeeRepository adminFeeRepository;

    public AdminFeeController(AdminFeeRepository adminFeeRepository) {
        this.adminFeeRepository = adminFeeRepository;
    }

    @GetMapping("/admin/fees")
    public String showAdminFees(Model model) {
        List<AdminFee> adminFees = adminFeeRepository.findAll();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        Map<String, BigDecimal> monthlyFeeMap = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthlyFeeMap.put(String.format("%02d", i), BigDecimal.ZERO);
        }

        Map<String, BigDecimal> weeklyFeeMap = new TreeMap<>();
        Map<LocalDate, BigDecimal> dailyFeeMap = new TreeMap<>();

        // 오늘 날짜 계산
        LocalDate today = LocalDate.now();
        BigDecimal todayTotalAmount = BigDecimal.ZERO;

        for (AdminFee fee : adminFees) {
            LocalDate date = fee.getAdmincoinAt().toLocalDate();
            String monthKey = date.format(monthFormatter);
            int weekOfYear = date.get(weekFields.weekOfWeekBasedYear());
            LocalDate startOfWeek = date.with(weekFields.dayOfWeek(), 1);
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            String weekKey = startOfWeek + " ~ " + endOfWeek;

            monthlyFeeMap.put(monthKey, monthlyFeeMap.get(monthKey).add(fee.getAdmincoinAmount()));
            weeklyFeeMap.put(weekKey, weeklyFeeMap.getOrDefault(weekKey, BigDecimal.ZERO).add(fee.getAdmincoinAmount()));
            dailyFeeMap.put(date, dailyFeeMap.getOrDefault(date, BigDecimal.ZERO).add(fee.getAdmincoinAmount()));

            // 오늘 날짜의 수수료 매출 합산
            if (date.equals(today)) {
                todayTotalAmount = todayTotalAmount.add(fee.getAdmincoinAmount());
            }
        }

        model.addAttribute("monthLabels", new ArrayList<>(monthlyFeeMap.keySet()));
        model.addAttribute("monthAmounts", new ArrayList<>(monthlyFeeMap.values()));
        model.addAttribute("weeklyLabels", new ArrayList<>(weeklyFeeMap.keySet()));
        model.addAttribute("weeklyAmounts", new ArrayList<>(weeklyFeeMap.values()));
        model.addAttribute("dailyLabels", dailyFeeMap.keySet().stream().map(date -> date.format(dateFormatter)).collect(Collectors.toList()));
        model.addAttribute("dailyAmounts", new ArrayList<>(dailyFeeMap.values()));

        // 오늘 날짜 & 수수료 매출 추가
        model.addAttribute("todayDate", today.format(dateFormatter));
        model.addAttribute("todayTotalAmount", todayTotalAmount);

        return "admin/fees";
    }
    
    @GetMapping("/admin/fees/detail")
    public String showFeeDetails(@RequestParam("date") String date, Model model) {
        try {
            // 입력된 날짜를 LocalDate로 변환
            LocalDate localDate = LocalDate.parse(date); 

            // 해당 날짜의 시작 시간 (00:00:00)과 끝 시간 (23:59:59) 지정
            LocalDateTime startOfDay = localDate.atStartOfDay(); 
            LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX); 

            List<AdminFee> feeDetails = adminFeeRepository.findByDate(startOfDay, endOfDay);

            model.addAttribute("date", date);
            model.addAttribute("feeDetails", feeDetails);
            
            return "admin/feesDetail"; 
        } catch (Exception e) {
            e.printStackTrace(); 
            return "redirect:/admin/fees"; // 오류 발생 시 수수료 목록으로 리디렉션
        }
    }
    
    @GetMapping("/admin/fees/monthlyDetail")
    public String showMonthlyFeeDetails(@RequestParam("month") String month, Model model) {
        try {
            Year currentYear = Year.now(); 
            YearMonth yearMonth = YearMonth.of(currentYear.getValue(), Integer.parseInt(month));
            LocalDate startOfMonth = yearMonth.atDay(1);
            LocalDate endOfMonth = yearMonth.atEndOfMonth();

            LocalDateTime startDateTime = startOfMonth.atStartOfDay();
            LocalDateTime endDateTime = endOfMonth.atTime(LocalTime.MAX);

            // 해당 월의 수수료 데이터 조회
            List<AdminFee> monthlyFeeDetails = adminFeeRepository.findByDate(startDateTime, endDateTime);

            model.addAttribute("month", month);
            model.addAttribute("feeDetails", monthlyFeeDetails);

            return "admin/feesMonthlyDetail";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/fees";
        }
    }



}
