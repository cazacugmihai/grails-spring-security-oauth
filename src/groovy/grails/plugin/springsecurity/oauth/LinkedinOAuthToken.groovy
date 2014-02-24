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

/**
 * OAuth authentication token for Linkedin users.
 *
 */
class LinkedinOAuthToken extends OAuthToken {

    public  static final String PROVIDER_NAME = "linkedin"

    String profileId

    LinkedinOAuthToken(Token scribeToken, profileId) {
        super(scribeToken)
        this.profileId = profileId
        this.principal = profileId
    }

    String getSocialId() {
        return profileId
    }

    String getScreenName() {
        return profileId
    }

    String getProviderName() {
        return PROVIDER_NAME
    }

}
