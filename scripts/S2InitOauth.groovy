/* Copyright 2006-2010 the original author or authors.
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

import grails.util.GrailsNameUtils
import grails.plugin.springsecurity.SpringSecurityUtils

includeTargets << new File("$springSecurityOauthPluginDir/scripts/_OauthCommon.groovy")
//includeTargets << grailsScript('_GrailsBootstrap')

USAGE = """
Usage: grails s2-init-oauth [domain-class-package] [oauthid-class-name]

Creates an OAuthID class in the specified package

Example: grails s2-init-oauth com.yourapp OAuthID
"""

def oAuthIDPackageName = ''
def oAuthIDPackageDir = ''
def oAuthIDClassName = ''

target(s2InitOauth: 'Initializes OAuth artifacts for the Spring Security OAuth plugin') {
    depends(checkVersion, configureProxy, packageApp, classpath)

    if (!configure()) {
        return 1
    }

    createDomains()
    createControllers()
    createViews()
    createI18N()
    updateConfig()

    printMessage """
   *******************************************************
   * Created domain class, controllers, and GSPs. Your   *
   * grails-app/conf/Config.groovy has been updated with *
   * the class name of the configured domain class;      *
   * please verify that the values are correct.          *
   *******************************************************
   """
}

private boolean configure() {

    def argValues = parseArgs()
    if (!argValues) {
        return false
    }

    def SpringSecurityUtils = classLoader.loadClass(
            'grails.plugin.springsecurity.SpringSecurityUtils')
    SpringSecurityUtils.loadSecondaryConfig 'DefaultSpringSecurityOAuthConfig'
    conf = SpringSecurityUtils.securityConfig

    (oAuthIDPackageName, oAuthIDClassName) = argValues
    oAuthIDPackageDir = packageToDir(oAuthIDPackageName)
    def oAuthIDClassFullName = oAuthIDPackageName + '.' + oAuthIDClassName

    String oAuthIdsPropertyName = conf.oauth.userLookup.oAuthIdsPropertyName
    checkValue oAuthIdsPropertyName, 'oauth.userLookup.oAuthIdsPropertyName'

    String userClassFullName = conf.userLookup.userDomainClassName
    checkValue userClassFullName, 'userLookup.userDomainClassName'

    if (conf.oauth.registration.userDomainClassName) {
        userClassFullName = conf.oauth.registration.userDomainClassName
    }

    String userPackageName
    String userClassName
    (userPackageName, userClassName) = splitClassName(userClassFullName)

    String roleClassFullName = conf.authority.className
    checkValue roleClassFullName, 'authority.className'

    String rolePackageName
    String roleClassName
    (rolePackageName, roleClassName) = splitClassName(roleClassFullName)

    String userRoleFullClassName = conf.userLookup.authorityJoinClassName
    checkValue userRoleFullClassName, 'userLookup.authorityJoinClassName'

    String userRolePackageName
    String userRoleClassName
    (userRolePackageName, userRoleClassName) = splitClassName(userRoleFullClassName)

    String usernamePropertyName = conf.userLookup.usernamePropertyName
    checkValue usernamePropertyName, 'userLookup.usernamePropertyName'

    String passwordPropertyName = conf.userLookup.passwordPropertyName
    checkValue passwordPropertyName, 'userLookup.passwordPropertyName'

    String enabledPropertyName = conf.userLookup.enabledPropertyName
    checkValue enabledPropertyName, 'userLookup.enabledPropertyName'

    String roleNameField = conf.authority.nameField
    checkValue roleNameField, 'authority.nameField'

    templateAttributes = [userClassFullName: userClassFullName,
            userClassName: userClassName,
            roleClassName: roleClassName,
            authorityCapName: GrailsNameUtils.getClassName(roleNameField, null),
            usernameCapPropertyName: GrailsNameUtils.getClassName(usernamePropertyName, null),
            passwordCapPropertyName: GrailsNameUtils.getClassName(passwordPropertyName, null),
            roleClassFullName: roleClassFullName,
            userRoleFullClassName: userRoleFullClassName,
            userRoleClassName: userRoleClassName,
            usernamePropertyName: usernamePropertyName,
            passwordPropertyName: passwordPropertyName,
            enabledPropertyName: enabledPropertyName,
            oAuthIDPackageName: oAuthIDPackageName,
            oAuthIDClassName: oAuthIDClassName,
            oAuthIDClassFullName: oAuthIDClassFullName,
            oAuthIdsPropertyName: oAuthIdsPropertyName,
            oAuthIdsCapPropertyName: GrailsNameUtils.getClassName(oAuthIdsPropertyName, null)]
}

private void createDomains() {
    generateFile "$templateDir/OAuthID.groovy.template",
            "$appDir/domain/${oAuthIDPackageDir}${oAuthIDClassName}.groovy"
}

private void createControllers() {
    generateFile "$templateDir/SpringSecurityOAuthController.groovy.template",
            "$appDir/controllers/${oAuthIDPackageDir}/SpringSecurityOAuthController.groovy"
}

private void createViews() {
    String dir = 'springSecurityOAuth'
    ant.mkdir dir: "$appDir/views/$dir"
    generateFile "$templateDir/askToLinkOrCreateAccount.gsp.template", "$appDir/views/$dir/askToLinkOrCreateAccount.gsp"
}

private void createI18N() {
    generateFile "$templateDir/spring-security-oauth.messages.properties.template",
            "$appDir/i18n/spring-security-oauth.messages.properties"
}

private void updateConfig() {
    def configFile = new File(appDir, 'conf/Config.groovy')
    if (configFile.exists()) {
        configFile.withWriterAppend {
            it.writeLine '\n// Added by the Spring Security OAuth plugin:'
            it.writeLine "grails.plugin.springsecurity.oauth.domainClass = '${oAuthIDPackageName}.${oAuthIDClassName}'"
        }
    }
}

private parseArgs() {
    def args = argsMap.params

    if (args.size() == 2) {
        return args
    }

    errorMessage USAGE
    return null
}

setDefaultTarget 's2InitOauth'
