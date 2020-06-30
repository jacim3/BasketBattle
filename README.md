- Tech Stack : Java, Android, JSoup, SQLite, HTML, CSS, JQuery

홈쇼핑 사이트로 이동하여, 원하는 상품을 앱에 간편하게 저장하여 등록하며, 필요하다면 여러개의 상품 중 하나의 상품의 선택을 도와줄 수 있는 Web 기반 안드로이드 Native 어플리케이션 입니다.<br><br>

![ezgif com-video-to-gif](https://user-images.githubusercontent.com/60813834/86097887-49b90100-baf0-11ea-8776-ed561aa9f57b.gif)

<img src="https://user-images.githubusercontent.com/60813834/82292557-1873ee00-99e6-11ea-80c7-bba239e40876.png" width=40%>
◈ 스플래시 화면         
<br><br><br>
<img src="https://user-images.githubusercontent.com/60813834/82292564-19a51b00-99e6-11ea-9554-085c511eb5a5.png" width=40%>
◈ 메인 화면<br><br>
 &nbsp;&nbsp;&nbsp;&nbsp;- 네비게이션 메뉴를 열어서 다른 프래그먼트 페이지로 넘어가거나, 홈쇼핑 사이트로 이동이 가능. 
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292567-1ad64800-99e6-11ea-90e9-afa55efa4d91.png" width=40%>
◈ 페이지 이동<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- 앱에 등록되어 있는 쇼핑몰 페이지 중 하나를 선택하여 이동 가능.<br>
&nbsp;&nbsp;&nbsp;&nbsp;- 선택 후 'OK'버튼을 누르면, Android WebView가 작동하여, 웹 페이지 출력
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292570-1b6ede80-99e6-11ea-89d3-e09dcddf827f.png" width=40%>
◈ 데이터 저장(등록)확인창<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- 여러 상품들을 둘러본 후 원하는 상품에 등록 버튼을 누르면, 해당 데이터를 DB(SQLite)에 저장.<br>
&nbsp;&nbsp;&nbsp;&nbsp;- Java의 JSoup 라이브러리를 사용, 웹 페이지의 CSS 정보를 읽어옴으로써 작동하는 원리
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292574-1d38a200-99e6-11ea-9bed-f49c255312f3.png" width=40%>
◈ 저장된 상품 확인 ( DB + 리스트뷰 )<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- 리스트뷰는 각각의 'Registered Goods', 'Defeated Goods', 'Winners' 라는 이름을 가진 상태로 출력되며, 
사용자의 'Swipe'동작이나 버튼입력으로 보고자 하는 데이터를 가진 DB를 선택 가능<br>
&nbsp;&nbsp;&nbsp;&nbsp;- 등록된 상품은 'Registerd Goods'로서, 무조건 1번DB에 저장되어 ListView를 통하여 출력.  
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292577-1e69cf00-99e6-11ea-86ce-c4f24a0a5848.png" width=40%>
◈ 상품 선택 ( Web Page ) <br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- 상품 등록을 2개 이상 하였을 경우에만 활성화.<br>
&nbsp;&nbsp;&nbsp;&nbsp;- Android의 DB에 있는 정보를 JSON객체로 파싱하여 WebPage에 보냄.<br>
&nbsp;&nbsp;&nbsp;&nbsp;- Android의 WebView를 통하여, 안드로이드 프로젝트 내 WebPage가 동작하게 함<br>
&nbsp;&nbsp;&nbsp;&nbsp;- 사용자의 터치입력에 반응하여 JQuery의 스크립트와 JQuery Ui의 애니메이션 기능이 작동<br>
&nbsp;&nbsp;&nbsp;&nbsp;- 선택한 상품은 남게 되고, 선택받지 못하면 버려져서, 최종으로 한개가 남을때까지 알고리즘이 수행
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292579-1f9afc00-99e6-11ea-8526-81961330282e.png" width=40%>
◈ 선택 완료<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- 선택이 모두 완료되어 최종적으로 하나의 상품만 남을 때 발생<br>
&nbsp;&nbsp;&nbsp;&nbsp;- 하단의 생성된 버튼을 누르면, 최종 결과가 안드로이드에 반영되어 최종 선택된 상품에 대한 정보가 
Android에 전달.
<br><br><br>

<img src="https://user-images.githubusercontent.com/60813834/82292583-20cc2900-99e6-11ea-84d4-701aec0e137d.png" width=40%>
◈ 최종 선택된 상품<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;- 최종 선택이 완료된 경우, 1번 DB(Registered Goods)에 있는 내용을 2번 DB(Defeated Goods)에 옮기고 1번 DB의 내용은 전부 삭제.<br>
&nbsp;&nbsp;&nbsp;&nbsp;- 2번 DB에 있는 내용 중 Web에서 보내진 최종 선택 상품 정보를 가진 데이터를 3번 DB(Winners)로 옮기며, 옮겨진 데이터는 2번 DB에서 삭제
<br><br><br>
