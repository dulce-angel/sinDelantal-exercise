package sindelantal.exercise

import grails.transaction.Transactional
import groovy.json.JsonSlurper
import wslite.rest.ContentType
import wslite.rest.RESTClient

@Transactional
class GeneralService {

    def connectWeatherApi(Map data) {

        RESTClient restClient = new RESTClient('https://api.openweathermap.org/data/2.5/weather')

        def responseRestClient
        Map responseApiWeather

        JsonSlurper jsonSlurper = new JsonSlurper()

        if(data.nameCity){
            try {
                responseRestClient = restClient.get(query:[q:data.nameCity, APPID: 'a92141374c8785d81331fa4b4bc4b949'])

                responseApiWeather = jsonSlurper.parseText(responseRestClient.contentAsString)


                responseApiWeather.put('success',true)

            }catch (ConnectException | IOException | URISyntaxException | IllegalStateException e){
                log.error "Error en la conexíon con apiWeather ${e.getMessage()}"
                return [status:409, message: 'Error interno del sistema', success: false, responseApiWeather:responseApiWeather]
            }
        }else if(data.coordinates){
            try {
                responseRestClient = restClient.get(query:[lat:data.coordinates.lat,lon:data.coordinates.lon, APPID: 'a92141374c8785d81331fa4b4bc4b949'])

                responseApiWeather = jsonSlurper.parseText(responseRestClient.contentAsString)


                responseApiWeather.put('success',true)

            }catch (ConnectException | IOException | URISyntaxException | IllegalStateException e){
                log.error "Error en la conexíon con apiWeather ${e.getMessage()}"
                return [status:409, message: 'Error interno del sistema', success: false, responseApiWeather:responseApiWeather]
            }
        }



        return responseApiWeather

    }

    def authSpotify(String dataEncode){
        RESTClient restClient = new RESTClient('https://accounts.spotify.com/api/token')

        def responseRestClient
        Map responseApiSpotify

        JsonSlurper jsonSlurper = new JsonSlurper()

        try{
            responseRestClient = restClient.post(headers: ['ContentType':'application/x-www-form-urlencoded',
                                                           'Authorization':'Basic '+dataEncode,
                                                           'Accept':'application/json']){
                type ContentType.URLENC
                urlenc grant_type:'client_credentials'
            }

            responseApiSpotify = jsonSlurper.parseText(responseRestClient.contentAsString)

            responseApiSpotify.put('success',true)

        }catch(ConnectException | IOException | URISyntaxException | IllegalStateException e){
            log.error "Error en la conexíon con apiAuthSpotify ${e.getMessage()}"
            return [status:409, message: 'Error interno del sistema', success: false, responseApiSpotify:responseApiSpotify]
        }

        return responseApiSpotify
    }

    def getPlaylist(String genreMusic){

        String code = 'f46a78ac00784d6a916e3a40c1237d25:a98b419e21104807abe1ac6790966c76'.encodeAsBase64()

        Map authSpotifyMap = authSpotify(code)

        if(!authSpotifyMap.success){
            return [status:authSpotifyMap.status, message: authSpotifyMap.message, success: false]
        }

        RESTClient restClient = new RESTClient('https://api.spotify.com/v1/recommendations?seed_genres='+genreMusic)

        def responseRestClient
        Map responseApiSpotify

        JsonSlurper jsonSlurper = new JsonSlurper()

        try{
            responseRestClient = restClient.get(headers: ['ContentType':'application/json',
                                                           'Authorization':authSpotifyMap.token_type+' '+authSpotifyMap.access_token,
                                                           'Accept':'application/json'])

            responseApiSpotify = jsonSlurper.parseText(responseRestClient.contentAsString)

            responseApiSpotify.put('success',true)

        }catch(ConnectException | IOException | URISyntaxException | IllegalStateException e){
            log.error "Error en la conexíon con apiAuthSpotify ${e.getMessage()}"
            return [status:409, message: 'Error interno del sistema', success: false, responseApiSpotify:responseApiSpotify]
        }

        return responseApiSpotify
    }
}
