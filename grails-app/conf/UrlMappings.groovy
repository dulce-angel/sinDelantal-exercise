class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/home/index")
        "500"(view:'/error')

        "/api/v1/getPlaylist"(controller: 'apiWeather',
                action: 'index', method:'post')
	}
}
