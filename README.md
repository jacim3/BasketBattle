#홈쇼핑 사이트로 이동하여, 원하는 상품을 앱에 간편하게 저장하여 등록하며, 필요하다면 여러개의 상품 중 하나의 상품의 선택을 도와줄 수 있는 Web통신 기반 안드로이드 어플리케이션 입니다.<br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292557-1873ee00-99e6-11ea-80c7-bba239e40876.png" width=40%>
◈ 스플래시 화면         
<br><br><br>
<img src="https://user-images.githubusercontent.com/60813834/82292564-19a51b00-99e6-11ea-9554-085c511eb5a5.png" width=40%>
◈ 메인 화면<br><br>
 &nbsp;&nbsp;&nbsp;&nbsp;- 네비게이션 메뉴를 열어서 다른 프래그먼트 페이지로 넘어가거나, 홈쇼핑 사이트로 이동이 가능. 
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292567-1ad64800-99e6-11ea-90e9-afa55efa4d91.png" width=40%>
◈ 페이지 이동<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- Android Spinner를 사용, 앱에 등록되어 있는 쇼핑몰 페이지 중 하나를 선택하여 이동 가능.<br>
&nbsp;&nbsp;&nbsp;&nbsp;- 선택 후 'OK'버튼을 누르면, Android WebView가 작동하여, 웹 페이지 출력
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292570-1b6ede80-99e6-11ea-89d3-e09dcddf827f.png" width=40%>
◈ 데이터 저장(등록)확인창<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- 여러 상품들을 둘러본 후 원하는 상품에 등록 버튼을 누르면, 해당 데이터를 DB(SQLite)에 저장. 
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292574-1d38a200-99e6-11ea-9bed-f49c255312f3.png" width=40%>
◈ 저장한 상품들 ( DB + 리스트뷰 )<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- DB는 총 3개가 생성되어 사용자의 Swipe 동작이나 버튼 입력에 따라 각각의 DB에 저장된 내용을<br>
각각 'Registered Goods', 'Defeated Goods', 'Winners'라는 이름과 함께 ListView를 통하여 출력.<br>
&nbsp;&nbsp;&nbsp;&nbsp;- 처음 등록된 상품은 무조건 1번 DB에 저장되어 'Registerd Goods'로서, ListView에 출력.  
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292577-1e69cf00-99e6-11ea-86ce-c4f24a0a5848.png" width=40%>
◈ 상품 선택<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- 상품 등록을 2개 이상 하였을 경우에  
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292579-1f9afc00-99e6-11ea-8526-81961330282e.png" width=40%>
◈ 선택 완료<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- '상품 선택' 페이지에서 최종 선택을 완료하면, 1번DB에 있는 내용을 2번DB에 옮기고 1번DB의 내용은 전부 삭제.<br>
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292583-20cc2900-99e6-11ea-84d4-701aec0e137d.png" width=40%>
◈ 최종 선택된 상품<br><br>
<br><br><br>
