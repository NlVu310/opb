package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "bank_account_edit_history")
public class BankAccountEditHistoryEntity extends BaseEntity {
    @Column(name = "bank_account_id")
    private Long bankAccountId;
    @Column(name = "old_from_date")
    private OffsetDateTime oldFromDate;
    @Column(name = "old_to_date")
    private OffsetDateTime oldToDate;
    @Column(name = "new_from_date")
    private OffsetDateTime newFromDate;
    @Column(name = "new_to_date")
    private OffsetDateTime newToDate;
}
