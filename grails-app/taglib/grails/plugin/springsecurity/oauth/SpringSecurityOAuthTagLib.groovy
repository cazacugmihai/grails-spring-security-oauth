package grails.plugin.springsecurity.oauth

import org.scribe.model.Token

class SpringSecurityOAuthTagLib {

    static namespace = 's2o'

    def oauthService
    def springSecurityService

    /**
     * Renders the body if the user is authenticated with the given provider.
     */
    def ifLoggedInWith = { attrs, body ->
        def provider = attrs.remove('provider')
        if (currentUserIsLoggedInWithProvider(provider)) {
            out << body()
        }
    }

    /**
     * Renders the body if the user is not authenticated with the given provider.
     */
    def ifNotLoggedInWith = { attrs, body ->
        def provider = attrs.remove('provider')
        if (!currentUserIsLoggedInWithProvider(provider)) {
            out << body()
        }
    }
    
    private boolean currentUserIsLoggedInWithProvider(String provider) {
        if (!provider || !springSecurityService.isLoggedIn()) {
            return false
        }
        def currentUser = springSecurityService.currentUser
        def sessionKey = oauthService.findSessionKeyForAccessToken(provider)
        return (session[sessionKey] instanceof Token)
    }
}
