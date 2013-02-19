package grails.plugin.springsecurity.oauth

import grails.test.mixin.*
import spock.lang.Specification
import spock.lang.*
import org.scribe.model.Token

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(SpringSecurityOAuthTagLib)
class SpringSecurityOAuthTagLibSpec extends Specification {

    def springSecurityService
    
    def oauthService
    
    def setup() {
        springSecurityService = [:]
        oauthService = [:]
    }

    def "ifLoggedInWith should print body if session is valid"() {
        given:
            def sessionKey = "oas:${provider}"
            session[sessionKey] = new Token("${provider}_token", "${provider}_secret", "${provider}_rawResponse=rawResponse")
            springSecurityService.isLoggedIn = { ->
                true
            }
            oauthService.findSessionKeyForAccessToken = { providerName ->
                sessionKey
            }
            tagLib.springSecurityService = springSecurityService
            tagLib.oauthService = oauthService
            def template = "<s2o:ifLoggedInWith provider=\"${provider}\">Logged in using ${provider}</s2o:ifLoggedInWith>"
        and:
            def renderedContent = applyTemplate(template)
        expect:
            renderedContent == "Logged in using ${provider}"
        where:
            provider    | _
            'facebook'  | _
            'google'    | _
            'linkedin'  | _
            'twitter'   | _
    }

    def "ifLoggedInWith should print empty string if user is not logged in"() {
        given:
            springSecurityService.isLoggedIn = { ->
                return false
            }
            tagLib.springSecurityService = springSecurityService
            def template = '<s2o:ifLoggedInWith provider="unknown">Logged in using unknown provider</s2o:ifLoggedInWith>'
        when:
            def renderedContent = applyTemplate(template)
        then:
            renderedContent == ''
    }
    
    def "ifNotLoggedInWith should print body if user is not logged in"() {
        given:
            def message = "Not_Logged_In"
            springSecurityService.isLoggedIn = { ->
                return false
            }
            tagLib.springSecurityService = springSecurityService
            def template = "<s2o:ifNotLoggedInWith provider=\"facebook\">${message}</s2o:ifNotLoggedInWith>"
        when:
            def renderedContent = applyTemplate(template)
        then:
            renderedContent == message
    }
}
