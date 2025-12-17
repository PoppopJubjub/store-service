package com.popjub.storeservice.infrastructure.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.popjub.storeservice.domain.entity.Store;


public interface StoreJpaRepository extends JpaRepository<Store, UUID> {

	Optional<Store> findByStoreIdAndDeletedAtIsNull(UUID storeId);

	Page<Store> findAllByDeletedAtIsNull(Pageable pageable);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
		update Store s
				set s.status = com.popjub.storeservice.domain.entity.StoreStatus.ACTIVE
				where s.status = com.popjub.storeservice.domain.entity.StoreStatus.UPCOMING
				and s.startDate <= :today
		""")
	int openStores(@Param("today") LocalDate today);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
    update Store s
       set s.status = com.popjub.storeservice.domain.entity.StoreStatus.CLOSED
     where s.status = com.popjub.storeservice.domain.entity.StoreStatus.ACTIVE
       and s.endDate < :today
""")
	int closeStores(@Param("today") LocalDate today);
}
