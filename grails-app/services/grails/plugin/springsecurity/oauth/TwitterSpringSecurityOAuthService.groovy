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

import org.scribe.model.Token
import grails.converters.JSON

/**
 * @author Mihai CAZACU(cazacugmihai@gmail.com)
 */
class TwitterSpringSecurityOAuthService {

    def oauthService

    def grailsApplication

    def createAuthToken(Token accessToken) {
        def response = oauthService.getTwitterResource(accessToken, 'https://api.twitter.com/1.1/account/verify_credentials.json')
        def user
        try {
            user = JSON.parse(response.body)
        } catch (Exception e) {
            log.error "Error parsing response from Twitter. Response:\n${response.body}"
            throw new OAuthLoginException("Error parsing response from Twitter", e)
        }
        if (! user?.id) {
            log.error "No user id from Twitter. Response:\n${response.body}"
            throw new OAuthLoginException("No user id from Twitter")
        }
        String profileId = "${user.id}"
        String screenName = "${user.screen_name}"
        return new TwitterOAuthToken(accessToken, profileId, screenName)
    }

}

