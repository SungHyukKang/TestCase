let index = {
	init: function() {
		$("#btn-save").on("click",()=>{
			this.save();
		});
		$("#btn-update").on("click",()=>{
			this.update();
		});
	},
	save: function(){
		let data={
			username:$("#username").val(),
			name: $("#name").val(),
			password:$("#password").val(),
			email:$("#email").val()
		}
		//ajax호출 시 default가 비동기 호출
		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), //http body 데이터 
			contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지(MIME)
			dataType: "json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열(생긴게 Json이라면) => javascript 
		}).done(function(resp){
			if(resp.status == 500){
				alert("회원가입 실패");
			}else{
			alert("회원가입완료");
//			console.log(resp);
			location.href="/";
			}
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); //ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	},
	
	update: function(){
		let data={
			id:$("#id").val(),
			username :$("#username").val(),
			password:$("#password").val(),
			email:$("#email").val()
		}
		//ajax호출 시 default가 비동기 호출
		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data), //http body 데이터 
			contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지(MIME)
			dataType: "json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열(생긴게 Json이라면) => javascript 
		}).done(function(resp){
			alert("회원수정완료");
//			console.log(resp);
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); //ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	},
}

index.init();