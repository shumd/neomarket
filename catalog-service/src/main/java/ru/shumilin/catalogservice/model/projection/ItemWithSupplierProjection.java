package ru.shumilin.catalogservice.model.projection;

import java.math.BigDecimal;

public interface ItemWithSupplierProjection {
    Integer getId();
    String getName();
    BigDecimal getPrice();
    String getSupplierName();
    Integer getQuantity();
}
