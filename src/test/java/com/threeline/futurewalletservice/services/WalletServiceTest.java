package com.threeline.futurewalletservice.services;

import com.threeline.futurewalletservice.entities.Wallet;
import com.threeline.futurewalletservice.entities.WalletHistory;
import com.threeline.futurewalletservice.enums.*;
import com.threeline.futurewalletservice.pojos.APIResponse;
import com.threeline.futurewalletservice.pojos.PaymentDTO;
import com.threeline.futurewalletservice.pojos.UserDTO;
import com.threeline.futurewalletservice.repositories.WalletHistoryRepository;
import com.threeline.futurewalletservice.repositories.WalletRepository;
import com.threeline.futurewalletservice.util.App;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class WalletServiceTest {

    @Mock
    private WalletRepository mockWalletRepository;
    @Mock
    private WalletHistoryRepository mockWalletHistoryRepository;
    @Mock
    private UserService mockUserService;
    @Mock
    private App mockApp;

    private WalletService walletServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        walletServiceUnderTest = new WalletService(mockWalletRepository, mockWalletHistoryRepository, mockUserService,
                mockApp);
    }

    @Test
    void testFindWallet() {
        // Setup
        final Wallet expectedResult = new Wallet(0L, 0L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"),
                Currency.NGN, "accountNumber", Role.CONTENT_CREATOR, false,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Configure WalletRepository.findById(...).
        final Optional<Wallet> wallet = Optional.of(
                new Wallet(0L, 0L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"), Currency.NGN,
                        "accountNumber", Role.CONTENT_CREATOR, false,
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
        when(mockWalletRepository.findById(0L)).thenReturn(wallet);

        // Run the test
        final Wallet result = walletServiceUnderTest.findWallet(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindWallet_WalletRepositoryReturnsAbsent() {
        // Setup
        when(mockWalletRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(
                () -> {
                    walletServiceUnderTest.findWallet(0L);
                }).isInstanceOf(NullPointerException.class);

    }

    @Test
    void testFindWalletByUserId() {
        // Setup
        final Wallet expectedResult = new Wallet(0L, 0L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"),
                Currency.NGN, "accountNumber", Role.CONTENT_CREATOR, false,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Configure WalletRepository.findById(...).
        final Optional<Wallet> wallet = Optional.of(
                new Wallet(0L, 0L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"), Currency.NGN,
                        "accountNumber", Role.CONTENT_CREATOR, false,
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
        when(mockWalletRepository.findById(0L)).thenReturn(wallet);

        // Run the test
        final Wallet result = walletServiceUnderTest.findWalletByUserId(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    void testCreateWalletForUser2() {
        // Setup
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setFirstName("firstName");
        userDTO.setLastName("lastName");
        userDTO.setUuid("userUuid");
        userDTO.setEmail("email");
        userDTO.setPhoneNumber("phoneNumber");
        userDTO.setWalletId("walletId");
        userDTO.setWalletAccountNumber("walletAccountNumber");
        userDTO.setKycLevel(1);
        userDTO.setRole(Role.CONTENT_CREATOR);

        final Wallet expectedResult = new Wallet(0L, 0L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"),
                Currency.NGN, "accountNumber", Role.CONTENT_CREATOR, false,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockWalletRepository.existsByUserId(0L)).thenReturn(false);
        when(mockApp.generateAccountNumber()).thenReturn("accountNumber");

        // Configure WalletRepository.save(...).
        final Wallet wallet = new Wallet(0L, 0L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"),
                Currency.NGN, "accountNumber", Role.CONTENT_CREATOR, false,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockWalletRepository.save(any(Wallet.class))).thenReturn(wallet);

        // Configure WalletRepository.findByUserId().
        when(mockWalletRepository.findByUserId(0L)).thenReturn(Optional.empty());

        // Run the test
        final Wallet result = walletServiceUnderTest.createWalletForUser(userDTO);

        verify(mockWalletRepository, times(1)).save(any(Wallet.class));

        assertThat(result.getUserId()).isEqualTo(userDTO.getId());
    }

    @Test
    void testCreateWalletForUser2_WalletRepositoryFindByUserIdReturnsAbsent() {
        // Setup
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setFirstName("firstName");
        userDTO.setLastName("lastName");
        userDTO.setUuid("userUuid");
        userDTO.setEmail("email");
        userDTO.setPhoneNumber("phoneNumber");
        userDTO.setWalletId("walletId");
        userDTO.setWalletAccountNumber("walletAccountNumber");
        userDTO.setKycLevel(0);
        userDTO.setRole(Role.CONTENT_CREATOR);

        final Wallet expectedResult = new Wallet(0L, 0L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"),
                Currency.NGN, "accountNumber", Role.CONTENT_CREATOR, false,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockWalletRepository.existsByUserId(0L)).thenReturn(false);
        when(mockApp.generateAccountNumber()).thenReturn("accountNumber");

        // Configure WalletRepository.save(...).
        final Wallet wallet = new Wallet(0L, 0L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"),
                Currency.NGN, "accountNumber", Role.CONTENT_CREATOR, false,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockWalletRepository.save(any(Wallet.class))).thenReturn(wallet);

        when(mockWalletRepository.findByUserId(0L)).thenReturn(Optional.empty());

        // Run the test
        final Wallet result = walletServiceUnderTest.createWalletForUser(userDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    void testFundWalletsAfterPayment() {
        // Setup

        final PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(0L);
        paymentDTO.setCustomerName("customerName");
        paymentDTO.setCustomerEmail("customerEmail");
        paymentDTO.setProductName("productName");
        paymentDTO.setProductId(0L);
        paymentDTO.setProductCreatorId(0L);
        paymentDTO.setOrderRef("orderRef");
        paymentDTO.setAmountPaid(new BigDecimal("0.00"));
        paymentDTO.setPaymentReference("paymentReference");
        paymentDTO.setPaymentMethod(PaymentMethod.CARD);
        paymentDTO.setCurrency(Currency.NGN);
        paymentDTO.setTransactionDirection(TransactionDirection.CREDIT);
        paymentDTO.setPaymentDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        paymentDTO.setStatus(Status.SUCCESSFUL);
        paymentDTO.setSettlementStatus(SettlementStatus.SETTLED);
        final APIResponse<PaymentDTO> expectedResult = new APIResponse<>("wallet settled", true, paymentDTO);

        // Configure WalletRepository.findByRole(...).
        final Optional<Wallet> wallet = Optional.of(
                new Wallet(1L, 1L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"), Currency.NGN,
                        "accountNumber", Role.CLIENT_INSTITUTION, false,
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));

        final Optional<Wallet> walletC = Optional.of(
                new Wallet(2L, 2L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"), Currency.NGN,
                        "accountNumber", Role.CONTRACTING_INSTITUTION, false,
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));

        when(mockWalletRepository.findByRole(any(Role.class))).thenReturn(wallet);


        // Configure WalletRepository.findByUserId(...).
        final Optional<Wallet> wallet2 = Optional.of(
                new Wallet(0L, 0L, "userUuid", "accountName", "userEmail", new BigDecimal("0.00"), Currency.NGN,
                        "accountNumber", Role.CONTENT_CREATOR, false,
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
        when(mockWalletRepository.findByUserId(anyLong())).thenReturn(wallet2);

        when(mockApp.generateTransactionReference()).thenReturn("transactionReference");

        // Configure WalletHistoryRepository.save(...).
        final WalletHistory walletHistory = new WalletHistory(0L, 0L, 0L, new BigDecimal("0.00"),
                new BigDecimal("0.00"), "paymentReference", "orderReference", "transactionReference",
                TransactionDirection.CREDIT, Currency.NGN, Status.SUCCESSFUL,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockWalletHistoryRepository.save(any(WalletHistory.class))).thenReturn(walletHistory);

        // Run the test
        final APIResponse<PaymentDTO> result = walletServiceUnderTest.fundWalletsAfterPayment(paymentDTO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockWalletHistoryRepository, times(3)).save(any(WalletHistory.class));
    }

}