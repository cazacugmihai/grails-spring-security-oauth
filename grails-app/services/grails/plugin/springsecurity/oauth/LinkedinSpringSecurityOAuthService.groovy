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

import grails.converters.JSON

class LinkedinSpringSecurityOAuthService {

    def oauthService

    /*
    response.body =  {
      "firstName": "...",
      "headline": "...",
      "lastName": "...",
      "siteStandardProfileRequest": {
        "url": "http://www.linkedin.com/profile/view?id=123&authType=name&authToken=abcd&trk=api*a123456*s123456*"
      }
    }
    */
    def createAuthToken(accessToken) {
        def response = oauthService.getLinkedinResource(accessToken, "http://api.linkedin.com/v1/people/~?format=json")
        def user
        try {
            user = JSON.parse(response.body)
        } catch (Exception e) {
            log.error "Error parsing response from Linkedin. Response:\n${response.body}"
            throw new OAuthLoginException("Error parsing response from Linkedin", e)
        }
        def url = user?.siteStandardProfileRequest?.url
        if (!url) {
            log.error "No profile url from Linkedin. Response:\n${response.body}"
            throw new OAuthLoginException("No profile url from Linkedin")
        }
        def query = new URL(url).query
        if ( !query ) {
            throw new OAuthLoginException("No query string from Linkedin")
        }
        def params = query.split('&').inject([:]) { map, kv-> 
            def (key, value) = kv.split('=').toList(); 
            map[key] = value != null ? URLDecoder.decode(value) : null; 
            map
        }
        if (!params?.id) {
            throw new OAuthLoginException("No user id from Linkedin")
        }
        return new LinkedinOAuthToken(accessToken, params.id)
    }

}

