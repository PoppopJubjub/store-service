package com.popjub.storeservice.infrastructure.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.popjub.storeservice.application.dto.query.TimeSlotRuleView;
import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.TimeSlot;

public interface TimeSlotJpaRepository extends JpaRepository<TimeSlot, UUID> {
	Page<TimeSlot> findAllByStore_StoreIdAndDateAndDeletedAtIsNull(UUID storeId, LocalDate date, Pageable pageable);

	List<TimeSlot> findAllByStoreAndDateAndDeletedAtIsNull(Store store, LocalDate date);

	void deleteAllByStoreAndDate(Store store, LocalDate date);

	List<TimeSlot> findAllByStoreAndDeletedAtIsNull(Store store);

	//벌크 업데이트(네이티브 쿼리 사용) + Closed된 타임슬롯 ID 반환
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = """
  with updated as(update p_timeslot t
     set status = 'CLOSED' 			-- AVAILABLE을 대상으로 종료된 타임슬롯을 CLOSED로 변경
   where status = 'AVAILABLE'
     and (t.date + t.start_time + interval '20 minutes') < :now --타임슬롯 종료 시간 계산 후 현재 시간과 비교
  returning t.timeslot_id) select timeslot_id from updated
""", nativeQuery = true) //JPQL이 아닌 nativeQuery
	List<UUID> closeExpired(@Param("now") LocalDateTime now); //현재 시각 주입


	@Query("""
	select new com.popjub.storeservice.application.dto.query.TimeSlotRuleView(ts.interval, ts.capacity)
	from TimeSlot ts
	where ts.store = :store
	and ts.date = :date
	and ts.deletedAt is null
	order by ts.startTime asc
""")
	List<TimeSlotRuleView> findRuleByStoreAndDate(Store store, LocalDate date, Pageable pageable);
}
