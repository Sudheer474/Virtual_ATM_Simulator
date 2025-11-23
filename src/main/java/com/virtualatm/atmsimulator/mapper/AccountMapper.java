package com.virtualatm.atmsimulator.mapper;

import com.virtualatm.atmsimulator.dto.account.AccountDTO;
import com.virtualatm.atmsimulator.model.Account;
import com.virtualatm.atmsimulator.model.enums.AccountStatus;
import com.virtualatm.atmsimulator.model.enums.AccountType;

public class AccountMapper {

    public static AccountDTO toDTO(Account acc) {
        AccountDTO dto = new AccountDTO();
        dto.setId(acc.getId());
        dto.setAccountNumber(acc.getAccountNumber());
        dto.setAccountType(acc.getAccountType().name());
        dto.setBalance(acc.getBalance());
        dto.setIfscCode(acc.getIfscCode());
        dto.setStatus(acc.getStatus().name());

        dto.setUserId(acc.getUser() != null ? acc.getUser().getId() : null);
        dto.setCardId(acc.getCard() != null ? acc.getCard().getId() : null);

        return dto;
    }

    public static Account toEntity(AccountDTO dto) {
        Account acc = new Account();
        acc.setId(dto.getId());
        acc.setAccountNumber(dto.getAccountNumber());
        acc.setAccountType(AccountType.valueOf(dto.getAccountType()));
        acc.setBalance(dto.getBalance());
        acc.setIfscCode(dto.getIfscCode());
        acc.setStatus(AccountStatus.valueOf(dto.getStatus()));

        return acc; // attach User & Card in Service layer manually
    }
}
