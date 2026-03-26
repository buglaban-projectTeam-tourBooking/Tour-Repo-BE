package org.buglaban.travelapi.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageResponse <T> {
    int page;
    int pageSize;
    int totalPage;
    T item;
}
