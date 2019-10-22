import jenkins.model.Jenkins
import hudson.security.HudsonPrivateSecurityRealm
import hudson.security.FullControlOnceLoggedInAuthorizationStrategy
import jenkins.security.s2m.AdminWhitelistRule

import hudson.security.csrf.DefaultCrumbIssuer

def instance = Jenkins.get()

// Disable submitting usage statistics for privacy
println "Checking if usage statistics are collected: ${instance.isUsageStatisticsCollected()}"
if (instance.isUsageStatisticsCollected()) {
    println "Disable submitting anonymous usage statistics to jenkins-ci.org for privacy."
    instance.setNoUsageStatistics(true)
}

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("{{cfg.jenkins.security.username}}", "{{cfg.jenkins.security.password}}")
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)

println "Setting slave port to {{cfg.jenkins.slave.port}}"
instance.setSlaveAgentPort({{cfg.jenkins.slave.port}})

// Disable slave-to-master kill switch
instance.getInjector()
       .getInstance(AdminWhitelistRule.class)
       .setMasterKillSwitch(false)

println "Setting crumb issuer"
instance.setCrumbIssuer(new DefaultCrumbIssuer(true))

instance.save()
