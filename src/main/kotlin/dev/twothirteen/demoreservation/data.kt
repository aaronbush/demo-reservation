package dev.twothirteen.demoreservation

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

/**
create table demo1
(
account_id serial primary key,
status     char(10) not null,
version    integer
);
 */
@Entity
@Table(name = "demo1", schema = "public")
class Reservation(
    @Id
    @GeneratedValue
    var accountId: Int? = null,
    var status: String,
) {
    override fun toString(): String {
        return "Reservation(accountId=$accountId, status='$status')"
    }
}

@Entity
@Table(name = "demo1", schema = "public")
class VersionedReservation(
    @Id
    @GeneratedValue
    var accountId: Int? = null,
    var status: String,
    @Version
    var version: Long
) {
    override fun toString(): String {
        return "Reservation(accountId=$accountId, status='$status', version=$version)"
    }
}

// A repo for @Versioned entities
@Repository
interface VersionedReservationRepository : CrudRepository<VersionedReservation, Int> {
    fun findFirstByStatusEquals(status: String): Optional<VersionedReservation>
}

// A repo protecting access with Pessimistic access
@Repository
interface PessimisticReservationRepository : CrudRepository<Reservation, Int> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    fun findFirstByStatusEquals(status: String): Optional<Reservation>
}

// A more 'normal' repo; no @Version, @Pessimistic-locks.
@Repository
interface ReservationRepository : CrudRepository<Reservation, Int> {
    fun findFirstByStatusEquals(status: String): Optional<Reservation>

    @Query(
        nativeQuery = true,
        value = "UPDATE public.demo1 d1 " +
                "SET status = 'reserved' " +
                "FROM (SELECT account_id FROM public.demo1 WHERE status = 'available' LIMIT 1 FOR UPDATE SKIP LOCKED) AS d2 " +
                "WHERE d1.account_id = d2.account_id RETURNING *"
    )
//    @Modifying(clearAutomatically = true)  // will not allow rows to be returned - probably violates JPA spec since we do modify
    // why use JPA !?
    fun reserveAndGetSkipLocked(): Optional<Reservation>
}

