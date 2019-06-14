<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Obten tu Playlist</title>

	</head>
	<body class="text-center">
		<div class="container">
			<div class="main-div">
				<div class="panel">
					<h2>
						Genera tu PlayList
					</h2>
					<p>
						Por favor, escoge qué dato quieres ingresar
					</p>
					<div>
						<select class="selectType form-control">
							<option>Selecciona uno...</option>
							<option value="nameCity">Nombre de la ciudad</option>
							<option value="coordinates">Coordenadas</option>
						</select>
					</div>
				</div>
				<div class="divFormCity hidden">
					<div class="form-group">
						<label>Ciudad</label>
						<input class="nameCity form-control"/>
					</div>
				</div>
				<div class="divFormCoord hidden">
					<div class="form-group">
						<label>Latitud</label>
						<input class="lat form-control" type="number" step="any"/>
					</div>
					<div class="form-group">
						<label>Longitud</label>
						<input class="lon form-control" type="number" step="any">
					</div>
				</div>
				<div class="divBtn text-right hidden">
					<button class="btn btn-secondary btnSendData">Generar</button>
				</div>
			</div>
			<div class="divTable dataTable hidden">
				<table id="dataTable" class="table table-striped table-bordered text-center">
					<thead class="thead-dark">
						<tr>
							<th>Nombre</th>
							<th>Canción</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<g:javascript>
			$(document).on('change','.selectType', function (e) {
				e.stopImmediatePropagation();
				e.stopPropagation();
				e.preventDefault();
				$('.divTable').addClass('hidden');
				var value = $(this).val();
				if(value=='nameCity'){
					$('.divFormCity,.divBtn').removeClass('hidden');
					$('.divFormCoord').addClass('hidden');
				}else if(value == 'coordinates'){
					$('.divFormCoord,.divBtn').removeClass('hidden');
					$('.divFormCity').addClass('hidden')
				}else{
					$('.divFormCity,.divFormCoord,.divBtn').addClass('hidden');
				}
			});
			$(document).on('click','.btnSendData',function (e) {
				e.stopImmediatePropagation();
				e.stopPropagation();
				e.preventDefault();
				$('#dataTable').DataTable().destroy();
				var item;
				if($('.selectType').val() == 'nameCity'){
					item = {nameCity:$('.nameCity').val()}
				}else if($('.selectType').val() == 'coordinates'){
					item = {coordinates:{lat:$('.lat').val(),lon:$('.lon').val()}}
				}
				$.ajax({
					type:'POST',
					data:JSON.stringify(item),
					contentType: "application/json",
					dataType: 'json',
					url:'<g:createLink controller="apiWeather" action="index"/> ',
					success:function(data) {
						var dataList = data.playListJson
						var dataSet = function(dataList){
							var return_data = new Array();
							for(var i=0; i<dataList.length; i++){
								return_data.push({
									'name':dataList[i].name,
									'song':'<audio src="'+dataList[i].url+'" controls></audio>'
								})
							}
							return return_data
						};
						$('#dataTable').DataTable({
							"paging":false,
							"ordering":false,
							"info":false,
							"filter":false,
							"deferRender":true,
							"data":dataSet(dataList),
							"columns":[
								{"data":"name"},
								{"data":"song"}
							]
						});
						$('.divTable').removeClass('hidden');
					},
					error: function(data) {

					}
				});
			});
		</g:javascript>
	</body>
</html>
