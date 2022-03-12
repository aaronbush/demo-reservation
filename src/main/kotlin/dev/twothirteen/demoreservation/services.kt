package dev.twothirteen.demoreservation

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Component
class ReservationService(
    val repository: ReservationRepository,
    val versionRepository: VersionedReservationRepository,
    val pessimisticReservationRepository: PessimisticReservationRepository
) {
    @Transactional
    fun reserve(delay: Long): Reservation {
        return findFirstAvailable(delay)
    }

    @Transactional
    fun versionedReserve(delay: Long): VersionedReservation {
        val r = versionRepository.findFirstByStatusEquals("available").get()
        println("found reservation with id: ${r.accountId}, sleeping for $delay sec")
        Thread.sleep(delay * 1000)
        r.status = "reserved"
        return versionRepository.save(r)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun reserveRepeatableRead(delay: Long): Reservation {
        return findFirstAvailable(delay)
    }

    @Transactional
    fun pessimisticReserve(delay: Long): Reservation {
        val r = pessimisticReservationRepository.findFirstByStatusEquals("available").get()
        println("found reservation with id: ${r.accountId}, sleeping for $delay sec")
        Thread.sleep(delay * 1000)
        r.status = "reserved"
        return pessimisticReservationRepository.save(r)
    }

    @Transactional
    fun reserveAndGetSkipLocked(delay: Long): Reservation {
        val r = repository.reserveAndGetSkipLocked().get()
        println("found reservation with id: ${r.accountId}, sleeping for $delay sec")
        Thread.sleep(delay * 1000)
        return r
    }

    private fun findFirstAvailable(delay: Long): Reservation {
        val r = repository.findFirstByStatusEquals("available").get()
        println("found reservation with id: ${r.accountId}, sleeping for $delay sec")
        Thread.sleep(delay * 1000)
        r.status = "reserved"
        return repository.save(r)
    }
}