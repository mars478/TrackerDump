import static ch.qos.logback.classic.Level.*
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.status.OnConsoleStatusListener
import ch.qos.logback.core.FileAppender

statusListener OnConsoleStatusListener

def catalina = System.getProperty("catalina.base")

appender("FILE_EXC", FileAppender) {        
    def logfileDate = timestamp('yyyy-MM-dd')
    file = "${catalina}/logs/pitStop.exc.${logfileDate}.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%-5level %-12logger{12} %logger - %msg%n"
    }
}

appender("FILE_DB", FileAppender) {        
    def logfileDate = timestamp('yyyy-MM-dd')
    file = "${catalina}/logs/pitStop.db.${logfileDate}.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%-5level %-12logger{12} %logger - %msg%n"
    }
}

appender("systemOut", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%-5level %-12logger{12} %logger --- %msg%n"
    }
}

root(INFO, ["systemOut"])
logger("com.mars.trackerdump", ERROR, ["FILE_EXC"])
logger("com.mars.trackerdump.db", INFO, ["FILE_DB"])