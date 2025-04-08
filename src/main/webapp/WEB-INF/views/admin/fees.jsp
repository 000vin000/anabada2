<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>관리자 수수료 내역</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        nav {
            margin-bottom: 20px;
        }
        nav ul {
            list-style: none;
            display: flex;
            justify-content: center;
            margin: 0;
            padding: 0;
        }
        nav ul li {
            margin: 0 15px;
        }
        nav ul li a {
            color: #007bff;
            text-decoration: none;
            font-size: 18px;
        }
        nav ul li a:hover {
            text-decoration: underline;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f4f4f4;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .chart-container {
            width: 80%;
            margin: 30px auto;
            text-align: center;
        }
    </style>
</head>
<body>
    <nav>
        <ul>
            <li><a href="/admin/dashboard">재무관리</a></li>
            <li><a href="/admin/management">고객관리</a></li>
            <li><a href="/admin/fees">수수료관리</a></li>
            <li><a href="/admin/acceptConversion">코인 전환 신청</a></li>
            <li><a href="/admin/depositWithdrawal">입/출금 관리</a></li>
            <li><a href="/admin/adminRole">관리자 권한 설정</a></li>
        </ul>
    </nav>

    <h1>관리자 수수료 내역</h1>
    <table>
        <thead>
            <tr>
                <th>오늘 날짜</th>
                <th>총 수수료 매출</th>
            </tr>
        </thead>
       <tbody>
    <tr>
        <td>
            <a href="/admin/fees/detail?date=${todayDate}" style="text-decoration: none; color: blue;">
                ${todayDate}
            </a>
        </td>
        <td>₩${todayTotalAmount}</td>
    </tr>
</tbody>
    </table>
       <div class="chart-container">
        <h2>일별 수수료 총합</h2>
        <canvas id="dailyFeeChart"></canvas>
    </div>

    <table>
        <thead>
            <tr>
                <th>월</th>
                <th>총 수수료 매출</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="month" items="${monthLabels}" varStatus="loop">
                <tr>
                    <td>
    				<a href="/admin/fees/monthlyDetail?month=${month}" style="text-decoration: none; color: blue;">${month}</a>
					</td>
                    <td>₩${monthAmounts[loop.index]}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <div class="chart-container">
        <h2>월별 수수료 총합</h2>
        <canvas id="monthlyFeeChart"></canvas>
    </div>
    
 
    
    <script>
        const monthLabels = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
        const monthAmountsMap = {
            <c:forEach var="month" items="${monthLabels}" varStatus="loop">
                "${month}": ${monthAmounts[loop.index]},
            </c:forEach>
        };
        const monthAmounts = monthLabels.map(month => monthAmountsMap[month] || 0);
        
        function getStartOfWeek(date) {
            const day = date.getDay();
            const diff = date.getDate() - day;
            return new Date(date.setDate(diff));
        }
        
        const today = new Date();
        const startOfWeek = getStartOfWeek(new Date());
        const last7Days = [];
        for (let i = 0; i < 7; i++) {
            const date = new Date(startOfWeek);
            date.setDate(startOfWeek.getDate() + i);
            last7Days.push(date.toISOString().split('T')[0]);
        }
        
        const dailyLabels = last7Days;
        const dailyAmountsMap = {
            <c:forEach var="entry" items="${dailyAmounts}" varStatus="loop">
                "${dailyLabels[loop.index]}": ${entry},
            </c:forEach>
        };
        const dailyAmounts = dailyLabels.map(day => dailyAmountsMap[day] || 0);
        
        const monthlyChartCtx = document.getElementById('monthlyFeeChart').getContext('2d');
        new Chart(monthlyChartCtx, {
            type: 'bar',
            data: {
                labels: monthLabels,
                datasets: [{
                    label: '월별 수수료 총합 (₩)',
                    data: monthAmounts,
                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: '수수료 금액 (₩)' } },
                    x: { title: { display: true, text: '월' } }
                }
            }
        });
      
        const dailyChartCtx = document.getElementById('dailyFeeChart').getContext('2d');
        new Chart(dailyChartCtx, {
            type: 'bar',
            data: {
                labels: dailyLabels,
                datasets: [{
                    label: '최근 7일간 일별 수수료 총합 (₩)',
                    data: dailyAmounts,
                    backgroundColor: 'rgba(255, 159, 64, 0.4)',
                    borderColor: 'rgba(255, 159, 64, 1)',
                    borderWidth: 2,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: '수수료 금액 (₩)' } },
                    x: { title: { display: true, text: '날짜' } }
                }
            }
        });
    </script>
</body>
</html>
