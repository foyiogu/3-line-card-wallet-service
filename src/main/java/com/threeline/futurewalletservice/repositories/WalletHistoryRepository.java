package com.threeline.futurewalletservice.repositories;

import com.threeline.futurewalletservice.entities.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Long> {

}
