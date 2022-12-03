package com.example.missourtrip

//this is a class for exchange rate (there are 2 classes another one is Currency)
class Request (
        var result: String,
        var time_last_update_utc: String,
        var time_next_update_utc: String,
        var base_code: String,
        var rates: Currency
        )