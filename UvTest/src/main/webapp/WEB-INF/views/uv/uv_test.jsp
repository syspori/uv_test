<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<script>
	var myHeaders = new Headers();
		myHeaders.append("x-access-token", "openuv-13xk1crlxbidfq2-io");
		myHeaders.append("Content-Type", "application/json");

		var requestOptions = {
		  method: 'GET',
		  headers: myHeaders,
		  redirect: 'follow'
		};

		fetch("https://api.openuv.io/api/v1/forecast?lat="+${lat}+"&lng="+${lng}+"&alt=100&dt=2024-06-17", requestOptions)
		  .then(response => {
				  if (!response.ok) { // ==  xhr.readyState && xhr.status
				      throw new Error('네트워크 응답에 문제가 있습니다.');
				    }
				  return response.text();
		  		}) // 응답 객체를 받아(response) 그 내용을 텍스트로 변환
		  .then(result => {location.href="uv_selectList.do?result=" + encodeURIComponent(result);})
		  .catch(error => console.log('error', error));
</script>
</body>
</html>