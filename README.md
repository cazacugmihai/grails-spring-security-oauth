grails-spring-security-oauth
============================

Adds OAuth-based authentication to the [Spring Security plugin](http://grails.org/plugin/spring-security-core] using the [OAuth plugin](http://grails.org/plugin/oauth-scribe].

This plugin provides an OAuth realm that can easily be integrated into existing applications and a host of utility functions to make things like "log in with Twitter" almost trivial.


Installation
------------

    grails install-plugin spring-security-oauth


Usage
-----

Install the plugin

    grails install-plugin spring-security-oauth

then, follow Spring Security Core and OAuth plugins documentation.

Sample configuration for OAuth plugin:

```groovy
oauth {
    debug = true
    providers {
        facebook {
            api = org.scribe.builder.api.FacebookApi
            key = 'oauth_facebook_key'
            secret = 'oauth_facebook_secret'
            successUri = '/oauth/facebook/success'
            failureUri = '/oauth/facebook/error'
            callback = "${baseURL}/oauth/facebook/callback"
        }
        twitter {
            api = org.scribe.builder.api.TwitterApi
            key = 'oauth_twitter_key'
            secret = 'oauth_twitter_secret'
            successUri = '/oauth/twitter/success'
            failureUri = '/oauth/twitter/error'
            callback = "${baseURL}/oauth/twitter/callback"
        }
        linkedin {
            api = org.scribe.builder.api.LinkedInApi
            key = 'oauth_linkedin_key'
            secret = 'oauth_linkedin_secret'
            successUri = '/oauth/linkedin/success'
            failureUri = '/oauth/linkedin/error'
            callback = "${baseURL}/oauth/linkedin/callback"
        }
        google {
            api = org.scribe.builder.api.GoogleApi
            key = 'oauth_google_key'
            secret = 'oauth_google_secret'
            successUri = '/oauth/google/success'
            failureUri = '/oauth/google/error'
            callback = "${baseURL}/oauth/google/callback"
            scope = 'https://www.googleapis.com/auth/userinfo.email'
        }
    }
}
```

Once you have an user domain and configured provider names, go with:

    grails s2-init-oauth [domain-class-package] [oauthid-class-name]
Example:

    grails s2-init-oauth com.yourapp OAuthID

that creates:

- the domain class `com.yourapp.OAuthID`

- the controller class `com.yourapp.SpringSecurityOAuthController`

- the view `springSecurityOAuth/askToLinkOrCreateAccount.gsp`



Finally, add

```groovy
    static hasMany = [oAuthIDs: OAuthID]
```

to you user domain class and use this lines in your login view:

```xml
    <oauth:connect provider="twitter" id="twitter-connect-link">Twitter</oauth:connect>
    <oauth:connect provider="facebook" id="facebook-connect-link">Facebook</oauth:connect>
    <oauth:connect provider="google" id="google-connect-link">Google</oauth:connect>
    <oauth:connect provider="yahoo" id="yahoo-connect-link">Yahoo</oauth:connect>
```

That's it! 