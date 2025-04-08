document.addEventListener("DOMContentLoaded", function () {
    fetchWeatherData();
});

function fetchWeatherData() {
    const cachedWeather = localStorage.getItem("cachedWeatherData");
    const cachedTime = localStorage.getItem("cachedWeatherTime"); 

    const now = new Date().getTime();
    const oneHour = 1000 * 60 * 60;

    if (cachedWeather && cachedTime && (now - cachedTime) < oneHour) {
        const parsedData = JSON.parse(cachedWeather);
        renderWeather(parsedData);
        return;
    }

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError);
    } else {
        document.getElementById("weatherError").innerHTML = "이 브라우저는 Geolocation을 지원하지 않습니다.";
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
            localStorage.setItem("cachedWeatherData", JSON.stringify(data));
            localStorage.setItem("cachedWeatherTime", new Date().getTime());
            renderWeather(data);
        })
        .catch(error => {
            console.error("Error fetching weather data:", error);
        });
}

function renderWeather(data) {
    const weatherContainer = document.querySelector('.weather-container');
    const itemListContainer = document.getElementById('item-list');
	
    const weatherIconMapping = {
        "맑음": "sunny.png", "구름 조금": "cloudy.png", "구름 많음": "cloudy.png", "온흐림": "cloudy.png",
        "흐림": "cloudy.png", "약한 비": "rainy.png", "비": "rainy.png", "강한 비": "rainy.png",
        "소나기": "rainy.png", "약한 뇌우": "thunderstormwithrain.png", "뇌우": "thunderstormwithrain.png",
        "강한 뇌우": "thunderstormwithrain.png", "약한 눈": "snowy.png", "눈": "snowy.png",
        "강한 눈": "snowy.png", "박무": "foggy.png", "안개": "foggy.png", "황사": "yellowdust.png",
        "연기": "smoke.png", "돌풍": "tornado.png", "토네이도": "tornado.png"
    };

    const cityNameMapping = {
        "Seoul": "서울", "Busan": "부산", "Incheon": "인천", "Daegu": "대구", "Daejeon": "대전", "Gwangju": "광주",
        "Ulsan": "울산", "Suwon": "수원", "Jeju City": "제주", "Changwon": "창원", "Goyang": "고양",
        "Seongnam": "성남", "Yongin": "용인", "Cheongju": "청주", "Jeonju": "전주", "Ansan": "안산",
        "Cheonan": "천안", "Jecheon": "제천", "Gimhae": "김해", "Pohang": "포항"
    };

    const now = new Date();
    const nowTimestamp = now.getTime();
    let closestTimeData = null;
    let closestTimeDiff = Infinity;
    const dailyWeather = {};

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
            dailyWeather[dateString].minTemp = Math.min(dailyWeather[dateString].minTemp, weatherData.main.temp_min);
            dailyWeather[dateString].maxTemp = Math.max(dailyWeather[dateString].maxTemp, weatherData.main.temp_max);
        }
    });

    const cityName = data.city.name;
    const cityKorean = cityNameMapping[cityName] || cityName;
    const formattedNow = now.toLocaleString("ko-KR", {
        year: "numeric", month: "2-digit", day: "2-digit",
        hour: "2-digit", minute: "2-digit", hour12: false
    }).replace(/\./g, '-').replace(/\s/g, '').replace(/(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})/, '$1-$2-$3 $4:$5');

    let weatherHTML = `
        <div class="main-weather">
            <div class="weather-header">
                <div class="city">${cityKorean}</div>
                <div class="today">${formattedNow}</div>
            </div>
            <div class="current-weather">
                <img src="../images/weather/${weatherIconMapping[closestTimeData.weather[0].description] || 'cloudy.png'}" alt="weather icon">
                <div>${closestTimeData.main.temp} °C</div>
            </div>
        </div>
        <div class="weather-table-container">
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

    weatherHTML += `</tbody></table></div>`;
    weatherContainer.innerHTML = weatherHTML;
	
	const updateTime = document.getElementById("weatherUpdateTime");
	const storageTime = localStorage.getItem("cachedWeatherTime");
	if (storageTime) {
	    const cachedDate = new Date(parseInt(storageTime)); 
	    const formattedCachedTime = cachedDate.toLocaleString("ko-KR", {
	        year: "numeric",
	        month: "2-digit",
	        day: "2-digit",
	        hour: "2-digit",
	        minute: "2-digit",
	        hour12: false
	    });
		
		updateTime.style.display = "flex";
	    updateTime.innerHTML = formattedCachedTime + " 업데이트";
		updateTime.style.color = "#969696";
		updateTime.style.justifyContent = "center";
	}

	
    const temp = closestTimeData.main.temp;
    const lat = data.city.coord.lat;
    const lon = data.city.coord.lon;

    if (temp && lat && lon) {
        fetch("/weather", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ temp, lat, lon })
        })
            .then(response => response.json())
            .then(data => {
                if (data.length > 0) {
                    itemListContainer.innerHTML = '';
                    const ulElement = document.createElement('ul');
                    data.forEach(item => {
                        const liElement = document.createElement('li');
                        const aElement = document.createElement('a');
                        aElement.className = "card";
                        aElement.href = '/item/detail/' + item.item_no;
						
						const remainingTimeStr = formatRemainingTime(item.item_sale_end_date);
						
                        aElement.innerHTML = `
                            <img src="data:image/png;base64,${item.base64Image}" width="100px" height="100px"/>
							<hr class="line">
                            <p class="itemName">${item.item_title}</p>
							<hr class="line">
                            <p class="itemPrice">${item.item_price}원</p>
                            <p class="seller">${item.user_nick}</p>
                            <p class="remainingTime">${remainingTimeStr}</p>
                        `;
                        liElement.appendChild(aElement);
                        ulElement.appendChild(liElement);
                    });
                    itemListContainer.appendChild(ulElement);
                } else {
                    itemListContainer.innerHTML = '<p>검색된 아이템이 없습니다.</p>';
                }
            })
            .catch(error => console.error("서버 전송 오류:", error));
    }
}

function showError(error) {
    document.getElementById("weatherError").style.display = "flex";
    document.getElementById("weatherError").innerHTML = "위치 정보 사용이 불가능하여 서울 기준으로 날씨를 표시합니다.";
    showPosition({ coords: { latitude: 37.5665, longitude: 126.9780 } });
}

function formatRemainingTime(endDateStr) {
    const now = new Date();
    const endDate = new Date(endDateStr);
    const diff = endDate - now;

    if (diff <= 0) return "마감됨";

    const minutes = Math.floor(diff / (1000 * 60)) % 60;
    const hours = Math.floor(diff / (1000 * 60 * 60)) % 24;
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));

    let result = "";
    if (days > 0) result += `${days}일 `;
    if (hours > 0 || days > 0) result += `${hours}시간 `;
    result += `${minutes}분`;

    return result.trim();
}

