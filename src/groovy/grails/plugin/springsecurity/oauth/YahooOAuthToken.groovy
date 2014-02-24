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
 * OAuth authentication token for Google users. It's a standard {@link OAuthToken}
 * that returns the Yahoo email address as the principal.
 *
 * @author Mihai CAZACU(cazacugmihai@gmail.com)
 */
class YahooOAuthToken extends OAuthToken {

    public static final String PROVIDER_NAME = "yahoo"

    Map profile

    YahooOAuthToken(Token scribeToken, Map profile) {
        super(scribeToken)
        this.profile = profile
        this.principal = profile.givenName + " " + profile.familyName
    }

    String getSocialId() {
        return profile.guid
    }

    String getScreenName() {
        return profile.guid
    }

    String getProviderName() {
        return PROVIDER_NAME
    }

}
