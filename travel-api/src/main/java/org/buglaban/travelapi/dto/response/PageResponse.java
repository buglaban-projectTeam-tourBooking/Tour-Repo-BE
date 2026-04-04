package org.buglaban.travelapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private int page;
    private int pageSize;
    private int totalPage;
    private long totalElements;
    private List<T> items;
}