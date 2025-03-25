package kr.co.anabada.admin.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.buy.repository.PaymentRepository;

@Service
public class DashboardService {

    @Autowired
    private PaymentRepository paymentRepository;

    // 하루 결제 금액 합산 (날짜별)
    public List<Object[]> getTotalPaymentByDay(LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        // 일매출 데이터를 가져온 후 금액을 BigDecimal로 변환
        List<Object[]> dailySales = paymentRepository.sumTotalSalesByDateRange(startOfDay, endOfDay);

        // 금액 변환 후 반환할 리스트
        List<Object[]> transformedSales = new ArrayList<>();
        for (Object[] data : dailySales) {
            LocalDateTime saleDate = (LocalDateTime) data[0];  // 날짜
            Long amountObj = (Long) data[1];  // 금액

            BigDecimal amount = new BigDecimal(amountObj); // Long을 BigDecimal로 변환
            transformedSales.add(new Object[] { saleDate, amount }); // 변환된 데이터를 리스트에 추가
        }

        return transformedSales;
    }

    // 일주일 결제 금액 합산
    public BigDecimal getTotalPaymentByWeek(LocalDateTime date) {
        // 주의 시작일: 이번 주 월요일 (startOfWeek)
        LocalDateTime startOfWeek = date.minusWeeks(1).with(java.time.DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        
        // 주의 종료일: 이번 주 일요일 (endOfWeek) (23:59:59로 설정)
        LocalDateTime endOfWeek = date.with(java.time.DayOfWeek.SUNDAY).toLocalDate().atTime(23, 59, 59);

        // 주매출 데이터를 가져오는 쿼리 호출
        Long weeklySales = paymentRepository.sumTotalSalesByWeek(startOfWeek, endOfWeek);

        // weeklySales가 null일 경우 0으로 처리
        if (weeklySales == null) {
            weeklySales = 0L;
        }

        // Long을 BigDecimal로 변환하여 반환
        return new BigDecimal(weeklySales);
    }

    // 한달 결제 금액 합산 (월별)
    public List<Object[]> getTotalPaymentByMonth(LocalDateTime date) {
        LocalDateTime startOfMonth = date.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfMonth = date.withDayOfMonth(date.toLocalDate().lengthOfMonth()).toLocalDate().atTime(23, 59, 59);

        // 월매출 데이터를 가져온 후 금액을 BigDecimal로 변환
        List<Object[]> monthlySales = paymentRepository.sumTotalSalesByMonth(startOfMonth, endOfMonth);

        // 금액 변환 후 반환할 리스트
        List<Object[]> transformedSales = new ArrayList<>();
        for (Object[] data : monthlySales) {
            Integer month = (Integer) data[0];  // 월
            Long amountObj = (Long) data[1];  // 금액

            BigDecimal amount = new BigDecimal(amountObj); // Long을 BigDecimal로 변환
            transformedSales.add(new Object[] { month, amount }); // 변환된 데이터를 리스트에 추가
        }

        return transformedSales;
    }
}