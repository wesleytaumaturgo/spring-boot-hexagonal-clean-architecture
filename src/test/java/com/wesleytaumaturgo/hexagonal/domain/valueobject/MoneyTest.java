package com.wesleytaumaturgo.hexagonal.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

// REQ-1.EARS-3
class MoneyTest {

    @Test
    void shouldCreateMoneyWhenAmountIsPositive() {
        Money money = new Money(new BigDecimal("99.99"));
        assertThat(money.getAmount()).isEqualByComparingTo("99.99");
    }

    @Test
    void shouldCreateMoneyFromDoubleValue() {
        Money money = new Money(49.90);
        assertThat(money.getAmount()).isEqualByComparingTo("49.90");
    }

    @Test
    void shouldRoundAmountToTwoDecimalPlaces() {
        Money money = new Money(new BigDecimal("9.999"));
        assertThat(money.getAmount().scale()).isEqualTo(2);
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        assertThatThrownBy(() -> new Money((BigDecimal) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Money amount must not be null");
    }

    @Test
    void shouldThrowWhenAmountIsZero() {
        assertThatThrownBy(() -> new Money(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Money amount must be positive");
    }

    @Test
    void shouldThrowWhenAmountIsNegative() {
        assertThatThrownBy(() -> new Money(new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Money amount must be positive");
    }

    @Test
    void shouldBeEqualWhenAmountsAreEqual() {
        Money a = new Money(new BigDecimal("10.00"));
        Money b = new Money(new BigDecimal("10.00"));
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
