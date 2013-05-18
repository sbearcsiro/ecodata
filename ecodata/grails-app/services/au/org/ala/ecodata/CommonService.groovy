package au.org.ala.ecodata

import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

class CommonService {

    def grailsApplication

    /**
     * Updates all properties other than 'id' and converts date strings to BSON dates.
     *
     * Note that dates are assumed to be ISO8601 in UTC with no millisecs
     * @param o the domain instance
     * @param props the properties to use
     */
    def updateProperties(o, props) {
        assert grailsApplication
        def domainDescriptor = grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE,
                o.getClass().name)
        props.remove('id')
        props.remove('api_key')  // don't ever let this be stored in public data
        props.each { k,v ->
            log.debug "updating ${k} to ${v}"
            /*
             * Checks the domain for properties of type Date and converts them.
             * Expects dates as strings in the form 'yyyy-MM-ddThh:mm:ssZ'. As indicated by the 'Z' these must be
             * UTC time. They are converted to java dates by forcing a zero time offset so that local timezone is
             * not used. All conversions to and from local time are the responsibility of the service consumer.
             */
            if (domainDescriptor.hasProperty(k) && domainDescriptor?.getPropertyByName(k)?.getType() == Date) {
                v = v ? dateFormat.parse(v.replace("Z", "+0000")) : null
            }
            o[k] = v
        }
        // always flush the update so that that any exceptions are caught before the service returns
        o.save(flush:true,failOnError:true)
        if (o.hasErrors()) {
            log.error("has errors:")
            o.errors.each { log.error it }
            throw new Exception(o.errors[0] as String);
        }
    }

}
