// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = 'au.org.ala.' + appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

/******************************************************************************\
 *  RELOADABLE CONFIG
\******************************************************************************/
reloadable.cfgs = ["file:/data/${appName}/config/${appName}-config.properties"]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

grails.mongo.default.mapping = {
    version false
}

/******************************************************************************\
 *  APPLICATION CONFIG
 \*****************************************************************************/
app.dump.location = "/data/ecodata/dump/"
app.elasticsearch.location = "/data/ecodata/elasticsearch/"
app.elasticsearch.indexAllOnStartup = true
app.elasticsearch.indexOnGormEvents = true

/******************************************************************************\
 *  EXTERNAL SERVERS
\******************************************************************************/
if (!ala.baseURL) {
    ala.baseURL = "http://www.ala.org.au"
}
if (!collectory.baseURL) {
    collectory.baseURL = "http://collections.ala.org.au/"
}
if (!headerAndFooter.baseURL) {
    headerAndFooter.baseURL = "http://www2.ala.org.au/commonui"
}
if (!security.apikey.serviceUrl) {
    security.apikey.serviceUrl = "http://auth.ala.org.au/ws/check?apikey="
}

// CAS security conf
security.cas.casServerName = 'https://auth.ala.org.au'
security.cas.uriFilterPattern = "/admin/.*" // pattern for pages that require authentication
security.cas.uriExclusionFilterPattern = '/images.*,/css.*,/js.*,/less.*'
security.cas.authenticateOnlyIfLoggedInPattern = "" // pattern for pages that can optionally display info about the logged-in user
security.cas.loginUrl = 'https://auth.ala.org.au/cas/login'
security.cas.logoutUrl = 'https://auth.ala.org.au/cas/logout'
security.cas.casServerUrlPrefix = 'https://auth.ala.org.au/cas'
security.cas.bypass = false

environments {
    development {
        grails.logging.jul.usebridge = true
        ecodata.use.uuids = false
        app.external.model.dir = "/devt/ecodata/models/"
        //grails.hostname = "localhost"
        grails.hostname = "devt.ala.org.au"
        //grails.hostname = "192.168.0.15"
        serverName = "http://${grails.hostname}:8080"
        grails.app.context = "ecodata"
        grails.serverURL = serverName + "/" + grails.app.context
        security.cas.appServerName = serverName
        security.cas.contextPath = "/" + appName
        app.uploads.url = "http://" + grails.hostname + "/ecodata/uploads/"
        app.elasticsearch.indexAllOnStartup = true
        app.elasticsearch.indexOnGormEvents = true
    }
    test {
        grails.logging.jul.usebridge = false
        ecodata.use.uuids = false
        app.external.model.dir = "/data/ecodata/models/"
        grails.serverURL = "http://testweb1.ala.org.au:8080/ecodata"
        app.uploads.url = "http://testweb1.ala.org.au/uploads/"
        security.cas.appServerName = "http://testweb1.ala.org.au:8080"
        security.cas.contextPath = "/" + "ecodata"
    }
    nectartest {
        grails.logging.jul.usebridge = false
        ecodata.use.uuids = false
        grails.serverURL = "http://115.146.94.243/ecodata"
        app.uploads.url = grails.serverURL + "/uploads/"
        security.cas.appServerName = "http://115.146.94.243"
        security.cas.contextPath = "/" + "ecodata"
    }
    nectar {
        grails.logging.jul.usebridge = false
        ecodata.use.uuids = false
        app.external.model.dir = "/data/ecodata/models/"
        grails.serverURL = "http://ecodata-dev.ala.org.au"
        app.uploads.url = grails.serverURL + "/uploads/"
        security.cas.appServerName = grails.serverURL
        security.cas.contextPath = "/" + "ecodata"
    }
    production {
        grails.logging.jul.usebridge = false
        ecodata.use.uuids = false
        app.external.model.dir = "/data/fieldcapture/models/"
        grails.serverURL = "http://ecodata.ala.org.au"
        app.uploads.url = grails.serverURL + "/uploads/"
        security.cas.appServerName = grails.serverURL
        security.cas.contextPath = ""
    }
}

// log4j configuration
log4j = {
    appenders {
        environments{
            development {
                console name: "stdout",
                        layout: pattern(conversionPattern: "%d %-5p [%c{1}]  %m%n"),
                        threshold: org.apache.log4j.Level.DEBUG
                rollingFile name: "ecodataLog",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat6/ecodata.log",
                        threshold: org.apache.log4j.Level.INFO,
                        layout: pattern(conversionPattern: "%d %-5p [%c{1}]  %m%n")
                rollingFile name: "stacktrace",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat6/ecodata-stacktrace.log"
            }
            test {
                rollingFile name: "ecodataLog",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat6/ecodata.log",
                        threshold: org.apache.log4j.Level.INFO,
                        layout: pattern(conversionPattern: "%d %-5p [%c{1}]  %m%n")
                rollingFile name: "stacktrace",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat6/ecodata-stacktrace.log"
            }
            nectar {
                rollingFile name: "ecodataLog",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat6/ecodata.log",
                        threshold: org.apache.log4j.Level.INFO,
                        layout: pattern(conversionPattern: "%d %-5p [%c{1}]  %m%n")
                rollingFile name: "stacktrace",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat6/ecodata-stacktrace.log"
            }
            nectartest {
                rollingFile name: "ecodataLog",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat7/ecodata.log",
                        threshold: org.apache.log4j.Level.INFO,
                        layout: pattern(conversionPattern: "%d %-5p [%c{1}]  %m%n")
                rollingFile name: "stacktrace",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat7/ecodata-stacktrace.log"
            }
            production {
                rollingFile name: "ecodataLog",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat6/ecodata.log",
                        threshold: org.apache.log4j.Level.INFO,
                        layout: pattern(conversionPattern: "%d %-5p [%c{1}]  %m%n")
                rollingFile name: "stacktrace",
                        maxFileSize: 104857600,
                        file: "/var/log/tomcat6/ecodata-stacktrace.log"
            }
        }
    }

    environments {
        development {
            all additivity: false, stdout: [
                    'grails.app.controllers.au.org.ala.ecodata',
                    'grails.app.domain.au.org.ala.ecodata',
                    'grails.app.services.au.org.ala.ecodata',
                    'grails.app.taglib.au.org.ala.ecodata',
                    'grails.app.conf.au.org.ala.ecodata',
                    'grails.app.filters.au.org.ala.ecodata'/*,
                    'au.org.ala.cas.client'*/
            ]
        }
    }

    all additivity: false, ecodataLog: [
            'grails.app.controllers.au.org.ala.ecodata',
            'grails.app.domain.au.org.ala.ecodata',
            'grails.app.services.au.org.ala.ecodata',
            'grails.app.taglib.au.org.ala.ecodata',
            'grails.app.conf.au.org.ala.ecodata',
            'grails.app.filters.au.org.ala.ecodata'
    ]

    debug 'grails.app.controllers.au.org.ala','au.org.ala.ecodata'

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
            'org.codehaus.groovy.grails.web.pages',          // GSP
            'org.codehaus.groovy.grails.web.sitemesh',       // layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping',        // URL mapping
            'org.codehaus.groovy.grails.commons',            // core / classloading
            'org.codehaus.groovy.grails.plugins',            // plugins
            'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'
}