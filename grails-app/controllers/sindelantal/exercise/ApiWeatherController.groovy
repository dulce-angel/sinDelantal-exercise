package sindelantal.exercise

import grails.converters.JSON

class ApiWeatherController {

    static allowedMethods = [index: "POST"]

    def generalService

    def index() {

        Map data = request.JSON

        if(!data.nameCity && !data.coordinates){
            response.status = 409
            render([message:'Los datos estan incorrectos',playListJson:''] as JSON)
        }

        Map weatherMap = generalService.connectWeatherApi(data)

        if(!weatherMap.success){
            response.status = 500
            render ([message:weatherMap.message,playListJson:''] as JSON)
        }

        Double temp = weatherMap.main.temp as Double

        Double realTemp = temp - 273.15

        Map getPlaylistMap

        switch (realTemp){
            case { it instanceof Double && it > 30} :
                getPlaylistMap = generalService.getPlaylist('party')
                break
            case { it instanceof Double && (it > 15 && it < 30) } :
                getPlaylistMap = generalService.getPlaylist('pop')
                break
            case { it instanceof Double && (it > 10 && it < 15) } :
                getPlaylistMap = generalService.getPlaylist('rock')
                break
            case { it instanceof Double && it < 10 } :
                getPlaylistMap = generalService.getPlaylist('classical')
                break
        }

        if(!getPlaylistMap.success){
            response.status = 500
            render([message:getPlaylistMap.message,playListJson:''] as JSON)
        }

        List playListJson = []
        Map playListMap = [:]

        getPlaylistMap.tracks.each {
            playListMap = [name:it.name,
                           url:it.preview_url]
            playListJson.add(playListMap)
        }

        response.status = 200
        render([message:'Disfruta las canciones',playListJson:playListJson] as JSON)

    }
}
