class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

//        //lets say, I want to have /author/id/book.json (same as book.json?author=id
//        "/author/$id/book.$format"(controller:"book"){
//            action = [GET: "listByAuthor"]
//        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
