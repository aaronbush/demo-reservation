package dev.twothirteen.demoreservation

import org.springframework.retry.annotation.Retryable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ReservationController
    (
    private val repository: ReservationRepository,
    private val service: ReservationService
) {

    @GetMapping("/{id}")
    fun getReservation(@PathVariable id: Int) = repository.findById(id).toString()

    @GetMapping("/reserve/{delay}")
    fun reserve(@PathVariable delay: Long) = service.reserve(delay).toString()

    @GetMapping("/versionedReserve/{delay}")
    @Retryable
    fun versionedReserve(@PathVariable delay: Long) = service.versionedReserve(delay).toString()

    @GetMapping("/reserveRepeatableRead/{delay}")
    @Retryable
    fun reserveRepeatableRead(@PathVariable delay: Long) = service.reserveRepeatableRead(delay).toString()

    @GetMapping("/reserveAndGetSkipLocked/{delay}")
    fun reserve2(@PathVariable delay: Long): String = service.reserveAndGetSkipLocked(delay).toString()

    @GetMapping("/pessimisticReserve/{delay}")
    @Retryable
    fun pessimisticReserve(@PathVariable delay: Long) = service.pessimisticReserve(delay).toString()
}