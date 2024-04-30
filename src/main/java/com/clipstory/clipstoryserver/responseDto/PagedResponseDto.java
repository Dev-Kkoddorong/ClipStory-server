package com.clipstory.clipstoryserver.responseDto;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
public class PagedResponseDto<T> {

    private final int currentPage;

    private final boolean hasPrevious;

    private final boolean hasNext;

    private final int totalPages;

    private final long totalItems;

    private final int currentItemsNumber;

    private final List<T> items;

    public PagedResponseDto(Page<T> page) {
        this.currentPage = page.getNumber();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.currentItemsNumber = page.getNumberOfElements();
        this.items = page.getContent();
    }

}
