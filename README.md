Small demo application to explore some concurrent data access patterns via Spring Transaction (over JDBC).

Presently an entirely manual test process with http commands in `get reservation.http` file.

Currently Demonstrating (not exhaustive):

| Endpoint                         | Isolation       | Locking          | Retry | Notes                                                                                                                                                                             |
|----------------------------------|-----------------|------------------|-------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| /reserve/{delay}                 | DEFAULT         | DEFAULT          | No    | Run concurrently and you will have overlapping updates to the same record.  Not what we want to have happen.                                                                      |
| /versionedReserve/{delay}        | DEFAULT         | Optimistic       | Yes   | Will correctly error and retry when concurrent updates are run due to @Version attribute on Entity.                                                                               |
| /reserveRepeatableRead/{delay}   | REPEATABLE_READ | DEFAULT          | Yes   | Concurrent updates are detected with REPEATABLE_READ and will be retried.                                                                                                         |
| /reserveAndGetSkipLocked/{delay} | DEFAULT         | DEFAULT          | No    | Makes use of a native query to update (from subquery that is FOR UPDATE SKIP LOCKED) to avoid concurrent updates to the same row                                                  |
| /pessimisticReserve/{delay}      | DEFAULT         | PESSIMISTIC_READ | Yes   | Concurrent access is detected and will be retried.  *If using PESSIMISTIC_WRITE queries will probably queue up and not have concurrent errors - although will be slow/serialized. | 