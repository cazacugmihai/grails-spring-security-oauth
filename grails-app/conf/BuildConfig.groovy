grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.project.dependency.resolution = {

    inherits "global"
    log "warn"

    repositories {
        mavenLocal()
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
        grailsRepo "http://grails.org/plugins"

        mavenRepo "http://maven.springframework.org/release"
        mavenRepo "http://maven.springframework.org/snapshot"
        mavenRepo "http://maven.springframework.org/milestone"
    }

    dependencies {
        compile "org.springframework.social:spring-social-core:1.0.2.RELEASE"
        compile "org.springframework.social:spring-social-facebook:1.0.1.RELEASE"
        compile "org.springframework.social:spring-social-twitter:1.0.2.RELEASE"
        runtime "org.scribe:scribe:1.3.2"
        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
    }

    plugins {
        compile ":spring-security-core:1.2.7.3"
        compile ":oauth:2.2.1", {
            transitive = false
        }

        build ":release:2.2.0", ":rest-client-builder:1.0.3", { 
            export = false 
        }

        test(":spock:0.7") {
            exclude "spock-grails-support"
        }
    }

}
