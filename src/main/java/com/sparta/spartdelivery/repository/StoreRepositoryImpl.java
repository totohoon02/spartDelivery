package com.sparta.spartdelivery.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.enums.CategoryEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.sparta.spartdelivery.entity.QStore.store;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Store> findAllBySearchValue(String searchValue, Pageable pageable) {
        CategoryEnum categoryEnum = CategoryEnum.findByValue(searchValue);

        JPAQuery<Store> query = queryFactory.selectFrom(store)
                .where(
                        store.storeName.contains(searchValue)
                                .or(store.address.containsIgnoreCase(searchValue))
                                .or(categoryEnum != null ? store.categoryEnum.eq(categoryEnum) : null)
                );

        long total = query.fetchCount(); // 총 갯수 조회

        // 페이징 및 정렬 처리
        // 정렬 처리
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Store.class, "store");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        // 페이지 처리
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        List<Store> results = query.fetch(); // 결과 조회

        return new PageImpl<>(results, pageable, total); // Page 구현체 반환
    }
}
