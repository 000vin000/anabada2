<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>대시보드</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script> <!-- Chart.js 라이브러리 추가 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
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
        
        
        .dashboard-box {
            margin: 20px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
        }
        .dashboard-box h3 {
            font-size: 24px;
        }
        .dashboard-box a {
            font-size: 18px;
            color: #007bff;
            text-decoration: none;
        }
        .dashboard-box a:hover {
            text-decoration: underline;
        }
        .chart-container {
            width: 100%;
            height: 400px;
        }
    </style>
</head>
<body>

    <!-- 네비게이션 메뉴 -->
    <nav>
        <ul>
            <!-- '재무관리' 탭을 대시보드로 연결 -->
            <li><a href="/dashboard">재무관리</a></li> <!-- 대시보드 페이지로 연결 -->
            <li><a href="/management">고객관리</a></li>
        </ul>
    </nav>


    <h1>관리자 대시보드</h1>
    
    <div class="dashboard-box">
        <h3>일매출</h3>
        <p><strong>₩${todaySalesAmount}</strong></p>
        <a href="/daily-details">상세보기</a>
    </div>

    <div class="dashboard-box">
        <h3>주매출</h3>
        <p><strong>₩${weeklySalesAmount}</strong></p>
        <a href="/weekly-details">상세보기</a>
    </div>

    <div class="dashboard-box">
        <h3>월매출</h3>
       	 <p><strong>₩${monthlySalesTotal}</strong></p>
        <a href="/monthly-details">상세보기</a>
    </div>

    <!-- 일매출 그래프 -->
    <div class="chart-container">
        <h3>일매출</h3>
        <canvas id="dailySalesChart"></canvas>
    </div>

    <!-- 월매출 그래프 -->
    <div class="chart-container">
        <h3>월매출</h3>
        <canvas id="monthlySalesChart"></canvas>
    </div>

    <script>
        // 일매출 그래프 데이터 설정
        const dailySalesData = {
            labels: [<c:forEach var="date" items="${dailySalesLabels}">
                "${date}", 
            </c:forEach>],
            datasets: [{
                label: '일매출',
                data: [<c:forEach var="amount" items="${dailySalesAmounts}">
                    ${amount},
                </c:forEach>],
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 2,
                fill: false
            }]
        };

        // 월매출 그래프 데이터 설정
        const monthlySalesData = {
            labels: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
            datasets: [{
                label: '월매출',
                data: [<c:forEach var="amount" items="${monthlySalesAmounts}">
                    ${amount},
                </c:forEach>],
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 2,
                fill: false
            }]
        };

        // 일매출 그래프 그리기
        const dailySalesChart = new Chart(document.getElementById('dailySalesChart'), {
            type: 'line',
            data: dailySalesData,
            options: {
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '날짜'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: '금액 (₩)'
                        }
                    }
                }
            }
        });

        // 월매출 그래프 그리기
        const monthlySalesChart = new Chart(document.getElementById('monthlySalesChart'), {
            type: 'line',
            data: monthlySalesData,
            options: {
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '월'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: '금액 (₩)'
                        }
                    }
                }
            }
        });
    </script>

</body>
</html>