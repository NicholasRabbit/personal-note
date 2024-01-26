#### 1，Ajax请求传参数

```javascript
function approve(id){
		$.ajax({
			type: "POST",
			url: '${ctx}/flyl/flylUser/save',
			async: false, // 使用同步的方法
			data: {"id":id,"state":"1"
			},
			dataType: 'json',
			success: function (result1) {
				console.log(result1);
				if (result1.result == "false") {
					msg = result1.message;
					layer.alert(msg);
				} else {
					layer.msg("审核通过！",{icon:6})
					$("#search").click();
				}
			}
		});

	};
```

