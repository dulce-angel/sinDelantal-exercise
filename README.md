# Documentación

La aplicación está en Grails 2.5.6.

Después de correr la aplicación (run-app -https) se hará un envío de dato tipo rest (usar un servicio como postman, por ejemplo) con la siguiente estructura:

```JSON
url: http://localhost:8080/sinDelantal-exercise/api/v1/getPlaylist
type: POST
dataType: JSON
contentType: "application/json",

data: 	[{
  "nameCity":"Ciudad de México",
  "coordinates":{
    "lat":15,
    "lon":-99
    }
}]
   ```

Puede ir sin el nombre de la ciudad o sin las coordenadas, pero debe ir por lo menos uno de los dos parametros, si vienen los dos, hará caso primero al nombre de la ciudad
