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

    /*

    {"id":93713205,"entities":{"description":{"urls":[]}},"default_profile_image":false,"followers_count":146,"url":null,"friends_count":452,"favourites_count":0,"contributors_enabled":false,"statuses_count":23,"time_zone":"Rome","profile_background_color":"FFFFFF","utc_offset":3600,"verified":false,"name":"Enrico Comiti","geo_enabled":false,"lang":"it","profile_background_image_url":"http:\/\/a0.twimg.com\/images\/themes\/theme1\/bg.png","location":"Verona, Italy","profile_link_color":"336699","protected":false,"profile_image_url":"http:\/\/a0.twimg.com\/profile_images\/1215353532\/profile_normal.jpg","default_profile":false,"profile_use_background_image":false,"notifications":false,"follow_request_sent":false,"screen_name":"enricocomiti","profile_text_color":"333333","id_str":"93713205","following":false,"is_translator":false,"listed_count":4,"profile_sidebar_border_color":"FFFFFF","status":{"entities":{"hashtags":[{"indices":[23,33],"text":"inboxzero"}],"user_mentions":[],"urls":[]},"place":null,"coordinates":null,"retweeted":false,"in_reply_to_status_id_str":null,"id_str":"73747145596993536","contributors":null,"in_reply_to_user_id":null,"in_reply_to_status_id":null,"in_reply_to_user_id_str":null,"retweet_count":0,"favorited":false,"in_reply_to_screen_name":null,"text":"the day after release: #inboxzero in my google reader","geo":null,"truncated":false,"source":"web","id":73747145596993536,"created_at":"Thu May 26 13:47:44 +0000 2011"},"created_at":"Mon Nov 30 21:28:53 +0000 2009","profile_background_image_url_https":"https:\/\/si0.twimg.com\/images\/themes\/theme1\/bg.png","profile_image_url_https":"https:\/\/si0.twimg.com\/profile_images\/1215353532\/profile_normal.jpg","profile_background_tile":false,"description":"Software developer and father","profile_sidebar_fill_color":"FFFFFF"}

    */
    def createAuthToken(Token accessToken) {
        def response = oauthService.getTwitterResource(accessToken, 'https://api.twitter.com/1.1/account/verify_credentials.json')
        def user = JSON.parse(response.body)
        if (!user?.id) {
            throw new Exception("No user id from Twitter")
        }
        String profileId = "${user.id}"
        return new TwitterOAuthToken(accessToken, profileId)
    }

}

