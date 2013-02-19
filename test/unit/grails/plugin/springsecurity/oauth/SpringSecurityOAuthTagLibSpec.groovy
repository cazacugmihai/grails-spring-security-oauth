package grails.plugin.springsecurity.oauth

import grails.test.mixin.*
import spock.lang.Specification
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(SpringSecurityOAuthTagLib)
class SpringSecurityOAuthTagLibSpec extends Specification {

    def springSecurityService
    
    def setup() {
        springSecurityService = [:]
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
