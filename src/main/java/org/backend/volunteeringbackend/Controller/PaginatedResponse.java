package org.backend.volunteeringbackend.Controller;

import java.util.List;

class PaginatedResponse<T> {
    private List<T> data;
    private long total;

    public PaginatedResponse(List<T> data, long total) {
        this.data = data;
        this.total = total;
    }

    // Getters
    public List<T> getData() {
        return data;
    }

    public long getTotal() {
        return total;
    }
}