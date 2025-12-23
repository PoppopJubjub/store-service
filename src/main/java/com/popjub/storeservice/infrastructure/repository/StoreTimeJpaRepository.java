package com.popjub.storeservice.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreTime;

public interface StoreTimeJpaRepository extends JpaRepository<StoreTime, UUID> {

	Optional<StoreTime> findByStoreAndDate(Store store, LocalDate date);

	@Query("""
	select st
	from StoreTime st
	where st.store.storeId = :storeId
	and st.deletedAt is Null
""")
	List<StoreTime> findAllByStoreId(UUID storeId);
	List<StoreTime> findAllByStore(Store store);
}
