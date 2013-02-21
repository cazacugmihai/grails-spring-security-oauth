/*
 * Copyright 2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.springsecurity.oauth

import spock.lang.Specification
import spock.lang.*
import org.scribe.model.Token

class FacebookSpringSecurityOAuthServiceSpec extends Specification {
  
    FacebookSpringSecurityOAuthService service
    
    def oauthService
    
    def setup() {
        service = new FacebookSpringSecurityOAuthService()
        oauthService = [:]
    }

    def "should throw OAuthLoginException for unexpected response"() {
        given:
            def exception = null
            def oauthAccessToken = new Token('token', 'secret', 'rawResponse=rawResponse')
            def response = [body: responseBody]
            oauthService.getFacebookResource = { accessToken, url ->
                return response
            }
            service.oauthService = oauthService
        and:
            try {
                def token = service.createAuthToken( oauthAccessToken )
            } catch (Throwable throwable) {
                exception = throwable
            }
        expect:
            exception instanceof OAuthLoginException
        where:
            responseBody      |  _
            ''                |  _
            null              |  _
            '{}'              |  _
            '{"test"="test"}' |  _
    }
    
    def "should return the correct OAuth token"() {
        given:
            def responseBody = '''{"id":"123123123",
"name":"My Name","first_name":"My","last_name":"Name","link":"http:\\/\\/www.facebook.com\\/my.name","username":"my.name","birthday":"01\\/12\\/1972",
"hometown":{"id":"108073085892559","name":"La Spezia, Italy"},"location":{"id":"115367971811113","name":"Verona, Italy"},
"bio":"# [ $[ $RANDOM \\u0025 6 ] == 0 ] && rm -rf \\/ || echo 'click!'",
"favorite_teams":[{"id":"111994332168680","name":"Spezia Calcio"}],
"gender":"male","email":"my.name\\u0040gmail.com","timezone":1,"locale":"en_US","verified":true,"updated_time":"2012-08-16T12:33:51+0000"}'''
            def oauthAccessToken = new Token('token', 'secret', 'rawResponse=rawResponse')
            def response = [body:responseBody]
            oauthService.getFacebookResource = { accessToken, url ->
                return response
            }
            service.oauthService = oauthService
        when:
            def token = service.createAuthToken( oauthAccessToken )
        then:
            token.principal == '123123123'
            token.socialId == '123123123'
            token.providerName == 'facebook'
    }
}
