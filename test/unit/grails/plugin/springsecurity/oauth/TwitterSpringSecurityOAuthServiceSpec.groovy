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

class TwitterSpringSecurityOAuthServiceSpec extends Specification {
  
    TwitterSpringSecurityOAuthService service
    
    def oauthService
    
    def setup() {
        service = new TwitterSpringSecurityOAuthService()
        oauthService = [:]
    }

    def "should throw OAuthLoginException for unexpected response"() {
        given:
            def exception = null
            def oauthAccessToken = new Token('token', 'secret', 'rawResponse=rawResponse')
            def response = [body: responseBody]
            oauthService.getTwitterResource = { accessToken, url ->
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
            def responseBody = '''{"id":12345432,
"entities":{"description":{"urls":[]}},"default_profile_image":false,"followers_count":146,"url":null,"friends_count":452,"favourites_count":0,"contributors_enabled":false,"statuses_count":23,"time_zone":"Rome","profile_background_color":"FFFFFF","utc_offset":3600,"verified":false,"name":"First Last","geo_enabled":false,"lang":"it","profile_background_image_url":"http://a0.twimg.com/images/themes/theme1/bg.png","location":"Verona, Italy","profile_link_color":"336699","protected":false,"profile_image_url":"http://a0.twimg.com/profile_images/1215353532/profile_normal.jpg","default_profile":false,"profile_use_background_image":false,"notifications":false,"follow_request_sent":false,"screen_name":"firstlast","profile_text_color":"333333","id_str":"93713205","following":false,"is_translator":false,"listed_count":4,"profile_sidebar_border_color":"FFFFFF","status":{"entities":{"hashtags":[{"indices":[23,33],"text":"wow"}],"user_mentions":[],"urls":[]},"place":null,"coordinates":null,"retweeted":false,"in_reply_to_status_id_str":null,"id_str":"73747145596993536","contributors":null,"in_reply_to_user_id":null,"in_reply_to_status_id":null,"in_reply_to_user_id_str":null,"retweet_count":0,"favorited":false,"in_reply_to_screen_name":null,"text":"this is #wow","geo":null,"truncated":false,"source":"web","id":12345678909876,"created_at":"Thu May 26 13:47:44 +0000 2012"},"created_at":"Mon Nov 30 21:28:53 +0000 2007","profile_background_image_url_https":"https://si0.twimg.com/images/themes/theme1/bg.png","profile_image_url_https":"https://si0.twimg.com/profile_images/12345678/profile_normal.jpg","profile_background_tile":false,"description":"The description","profile_sidebar_fill_color":"FFFFFF"}'''
            def oauthAccessToken = new Token('token', 'secret', 'rawResponse=rawResponse')
            def response = [body:responseBody]
            oauthService.getTwitterResource = { accessToken, url ->
                return response
            }
            service.oauthService = oauthService
        when:
            def token = service.createAuthToken( oauthAccessToken )
        then:
            token.principal == '12345432'
            token.socialId == '12345432'
            token.providerName == 'twitter'
    }
}
