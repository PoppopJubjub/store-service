package com.popjub.store_service.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.popjub.store_service.domain.entity.Category;

//JPA 말고 다른 ORM이 들어 왔을 때 서비스 코드가 변경되지 않기 위해 Repository분리
//의존성 역전
public interface CategoryRepository{
	//나 이런 이름의 메소드를 사용할거야
	boolean existsByName(String name);
	List<Category> findAllById(List<Long> categoryIds);
	Category save(Category category);
}

