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

/**
 * @author Mihai CAZACU(cazacugmihai@gmail.com)
 */
class GoogleSpringSecurityOAuthService {

    def oauthService

    def createAuthToken(accessToken) {
        // Requires scope of "https://www.googleapis.com/auth/userinfo.email"
        def response = oauthService.getGoogleResource(accessToken, "https://www.googleapis.com/oauth2/v1/userinfo")
        def user = JSON.parse(response.body)
        return new GoogleOAuthToken(accessToken, user.email)
    }

}

