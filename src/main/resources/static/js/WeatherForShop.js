document.addEventListener("DOMContentLoaded", function() {
    fetchWeatherData();
});

function fetchWeatherData() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError);
    } else {
        document.getElementById("error").innerHTML = "이 브라우저는 Geolocation을 지원하지 않습니다.";
    }
}

function showPosition(position) {
    const lat = position.coords.latitude;
    const lon = position.coords.longitude;
    const apikey = "ed52d15758a57710850f5e242dd33b30";

    const weatherUrl = `http://api.openweathermap.org/data/2.5/forecast?lat=${lat}&lon=${lon}&appid=${apikey}&units=metric&lang=kr`;

    fetch(weatherUrl)
        .then(res => res.json())
        .then(data => {
            const dailyWeather = {};
            const now = new Date();
            const nowTimestamp = now.getTime();

            let closestTimeData = null;
            let closestTimeDiff = Infinity;

            data.list.forEach(weatherData => {
                const weatherTimestamp = weatherData.dt * 1000;
                const timeDiff = Math.abs(weatherTimestamp - nowTimestamp);

                if (timeDiff < closestTimeDiff) {
                    closestTimeDiff = timeDiff;
                    closestTimeData = weatherData;
                }

                const date = new Date(weatherData.dt * 1000);
                const dateString = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;

                if (!dailyWeather[dateString]) {
                    dailyWeather[dateString] = {
                        maxTemp: weatherData.main.temp_max,
                        minTemp: weatherData.main.temp_min,
                        description: weatherData.weather[0].description,
                    };
                } else {
                    if (weatherData.main.temp_min < dailyWeather[dateString].minTemp) {
                        dailyWeather[dateString].minTemp = weatherData.main.temp_min;
                    }
                    if (weatherData.main.temp_max > dailyWeather[dateString].maxTemp) {
                        dailyWeather[dateString].maxTemp = weatherData.main.temp_max;
                    }
                }
            });

            const weatherIconMapping = {
                "맑음": "sunny.png",
                "구름 조금": "cloudy.png",
                "구름 많음": "cloudy.png",
                "온흐림": "cloudy.png",
                "흐림": "cloudy.png",
                "약한 비": "rainy.png",
                "비": "rainy.png",
                "강한 비": "rainy.png",
                "소나기": "rainy.png",
                "약한 뇌우": "thunderstormwithrain.png",
                "뇌우": "thunderstormwithrain.png",
                "강한 뇌우": "thunderstormwithrain.png",
                "약한 눈": "snowy.png",
                "눈": "snowy.png",
                "강한 눈": "snowy.png",
                "박무": "foggy.png",
                "안개": "foggy.png",
                "황사": "yellowdust.png",
                "연기": "smoke.png",
                "돌풍": "tornado.png",
                "토네이도": "tornado.png"
            };

            const cityNameMapping = {
                "Seoul": "서울",
                "Busan": "부산",
                "Incheon": "인천",
                "Daegu": "대구",
                "Daejeon": "대전",
                "Gwangju": "광주",
                "Ulsan": "울산",
                "Suwon": "수원",
                "Jeju City": "제주",
                "Changwon": "창원",
                "Goyang": "고양",
                "Seongnam": "성남",
                "Yongin": "용인",
                "Cheongju": "청주",
                "Jeonju": "전주",
                "Ansan": "안산",
                "Cheonan": "천안",
                "Jecheon": "제천",
                "Gimhae": "김해",
                "Pohang": "포항"
            };

            const cityName = data.city.name;
            const cityKorean = cityNameMapping[cityName] || cityName;

            const nowDate = new Date();
            const formattedNow = nowDate.toLocaleString("ko-KR", {
                year: "numeric",
                month: "2-digit",
                day: "2-digit",
                hour: "2-digit",
                minute: "2-digit",
                hour12: false  // 24시간 형식 사용
            }).replace(/\./g, '-').replace(/\s/g, '').replace(/(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})/, '$1-$2-$3 $4:$5');

            const fourDaysAheadData = data.list[25];  // 4th day forecast is the 25th entry
            const fourDaysAheadDate = new Date(fourDaysAheadData.dt * 1000);
            const fourDaysFormattedDate = `${fourDaysAheadDate.getFullYear()}-${String(fourDaysAheadDate.getMonth() + 1).padStart(2, '0')}-${String(fourDaysAheadDate.getDate()).padStart(2, '0')}`;
			
			const temp = fourDaysAheadData.main.temp;
			if (temp) {
			    fetch("/weather", {
			        method: "POST",
			        headers: {
			            "Content-Type": "application/json"
			        },
			        body: JSON.stringify({ temp: temp }) // 서버로 전송
			    })
			    .then(response => {
			        if (!response.ok) {
			            throw new Error('Network response was not ok');
			        }
			        return response.json(); // JSON 응답을 처리
			    })
				.then(data => {
				    console.log("서버로부터 받은 응답:", data);

				    // 받아온 아이템 리스트를 HTML로 표시
				    const itemListContainer = document.getElementById('item-list'); // 아이템을 표시할 컨테이너

				    // itemList가 존재하면 아이템들을 화면에 표시
				    if (data.length > 0) {
				        itemListContainer.innerHTML = ''; // 기존 내용을 비우기
				        const ulElement = document.createElement('ul'); // <ul> 요소 생성
				        data.forEach(item => {
				            const liElement = document.createElement('li'); // <li> 요소 생성
				            const aElement = document.createElement('a'); // <a> 요소 생성
							aElement.className = "card";
				            aElement.href = '/item/detail/' + item.item_no; // 링크 설정

				            // 아이템 HTML 구성
				            aElement.innerHTML = `
				                <img src="data:image/png;base64,${item.base64Image}" width="100px" height="100px"/>
				                <p class="itemName">${item.item_title}</p>
				                <p class="itemprice">${item.item_price}원</p>
				                <p class="itemUserNick">${item.user_nick}</p>
				                <p class="remainTime">${item.item_sale_end_date}</p>
				            `;
				            
				            liElement.appendChild(aElement); // <a>를 <li>에 추가
				            ulElement.appendChild(liElement); // <li>를 <ul>에 추가
				        });
				        itemListContainer.appendChild(ulElement); // <ul>을 컨테이너에 추가
				    } else {
				        itemListContainer.innerHTML = '<p>검색된 아이템이 없습니다.</p>';
				    }
				})
			    .catch(error => {
			        console.error("Error sending data to server:", error);
			    });
			}
			
            let weatherHTML = `
				<div class="main-weather">
	                <div class="weather-item" style="display: flex">
	                    <p class="city">현재 위치 : ${cityKorean}</p>
	                    <p class="today">${formattedNow}</p>
						
	                    <p class="current-weather">
							<strong>오늘</strong>    
							<img src="../images/weather/${weatherIconMapping[closestTimeData.weather[0].description] || 'cloudy.png'}" alt="weather icon">
	                        ${closestTimeData.main.temp} °C <br>
	                    </p>
	                </div>
	                <div class="weather-item">
	                    <p class="future-weather">
							<strong>배송 예정</strong>
							${fourDaysFormattedDate}
	                        <img src="../images/weather/${weatherIconMapping[fourDaysAheadData.weather[0].description] || 'cloudy.png'}" alt="weather icon">
	                        ${fourDaysAheadData.main.temp} °C <br>
	                    </p>
					</div>
                </div>
                <table class="weather-table">
                    <thead>
                        <tr>
                            <th>날짜</th>
							<th>평균 기온 (°C)</th>
                            <th>최저/최고 기온 (°C)</th>
                            <th>날씨 아이콘</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            for (const date in dailyWeather) {
                const todayAvgTemp = (dailyWeather[date].maxTemp + dailyWeather[date].minTemp) / 2;
                const icon = weatherIconMapping[dailyWeather[date].description] || "cloudy.png";

                weatherHTML += `
                    <tr>
                        <td>${date}</td>
						<td>${todayAvgTemp.toFixed(2)} °C</td>
                        <td>${dailyWeather[date].minTemp} / ${dailyWeather[date].maxTemp} °C</td>
                        <td><img src="../images/weather/${icon}" alt="weather icon" style="width: 50px; height: 50px;"></td>
                    </tr>
                `;
            }

            weatherHTML += `</tbody></table>`;
            document.querySelector('.weather-container').innerHTML = weatherHTML;
        })
        .catch(error => {
            console.error("Error fetching weather data:", error);
        });
}

function showError(error) {
    let defaultLat = 37.5665; // 서울 위도
    let defaultLon = 126.9780; // 서울 경도
    const apikey = "ed52d15758a57710850f5e242dd33b30";

    const weatherUrl = `http://api.openweathermap.org/data/2.5/forecast?lat=${defaultLat}&lon=${defaultLon}&appid=${apikey}&units=metric&lang=kr`;

    fetch(weatherUrl)
        .then(res => res.json())
        .then(data => {
            document.getElementById("error").innerHTML = "위치 정보 사용이 불가능하여 서울 기준으로 날씨를 표시합니다.";
            showWeatherData(data); // 서울 날씨로 날씨 정보 표시 함수 실행
        })
        .catch(() => {
            document.getElementById("error").innerHTML = "서울 날씨 정보를 불러오는 데 실패했습니다.";
        });
}