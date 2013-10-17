import grails.plugin.springsecurity.SpringSecurityUtils

class SpringSecurityOauthGrailsPlugin {
    def version = "2.0.1.2"
    def grailsVersion = "1.2.2 > *"
    def dependsOn = [springSecurityCore: '2.0-RC2 > *', oauth: "2.0 > *"]
    def pluginExcludes = [
            "web-app/css",
            "web-app/images",
            "web-app/js/prototype",
            "web-app/js/application.js"
    ]

    def title = "Spring Security OAuth plugin"
    def author = "Mihai Cazacu"
    def authorEmail = "cazacugmihai@gmail.com"
    def description = '''Adds OAuth-based authentication to the
[Spring Security plugin|http://grails.org/plugin/spring-security-core] using the
[OAuth plugin|http://grails.org/plugin/oauth-scribe]. This plugin provides an OAuth realm that can easily be integrated
into existing applications and a host of utility functions to make things like "log in with Twitter" almost trivial.'''

    def documentation = "http://grails.org/plugin/spring-security-oauth"

    def license = "APACHE"
    def organization = [name: "Macrobit Software", url: "http://macrobit.ro/"]
    def developers = [[name: "Mihai Cazacu", email: "cazacugmihai@gmail.com"]]
    def issueManagement = [system: "JIRA", url: "http://jira.grails.org/browse/GPSPRINGSECURITYOAUTH"]
    def scm = [url: 'https://github.com/grails-plugins/grails-spring-security-oauth/']

    def doWithSpring = {
        def conf = SpringSecurityUtils.securityConfig
        if (!conf) {
            println 'ERROR: There is no Spring Security configuration'
            println 'ERROR: Stop configuring Spring Security Oauth'
            return
        }

        println 'Configuring Spring Security OAuth ...'
        SpringSecurityUtils.loadSecondaryConfig 'DefaultSpringSecurityOAuthConfig'
        // have to get again after overlaying DefaultSpringSecurityOAuthConfig
        // conf = SpringSecurityUtils.securityConfig
    }

}
