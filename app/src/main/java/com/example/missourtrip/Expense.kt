package com.example.missourtrip

import java.util.Currency
import java.util.Date

data class Expense(val name: String, val amount: Double, val currency: Currency, val date: Date, val category: Category)
