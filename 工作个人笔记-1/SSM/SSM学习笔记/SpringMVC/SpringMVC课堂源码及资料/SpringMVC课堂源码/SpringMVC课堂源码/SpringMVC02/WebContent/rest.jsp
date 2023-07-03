<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
	function test() {
		$.ajax({
			url:"testAjax_DELETE",
			type:"DELETE",
			data:{id:1001},
			dataType:"json",
			success:function (obj){
				alert(obj);
			}
		});
	}
</script>
</head>
<body>

	<a href="testREST/1001">测试GET</a>
	<br />
	<form action="testREST" method="POST">
		<input type="submit" value="测试POST" />
	</form>
	<br />
	<form action="testREST" method="POST">
		<input type="hidden" name="_method" value="PUT" />
		<input type="submit" value="测试PUT" />
	</form>
	<br />
	<form action="testREST/1001" method="POST">
		<input type="hidden" name="_method" value="DELETE" />
		<input type="submit" value="测试DELETE" />
	</form>
	
	<input type="button" value="测试AJAX" onclick="test()" />

</body>
</html>