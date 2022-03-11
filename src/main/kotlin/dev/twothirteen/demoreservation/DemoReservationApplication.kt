package dev.twothirteen.demoreservation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class DemoReservationApplication

fun main(args: Array<String>) {
    runApplication<DemoReservationApplication>(*args)
}
